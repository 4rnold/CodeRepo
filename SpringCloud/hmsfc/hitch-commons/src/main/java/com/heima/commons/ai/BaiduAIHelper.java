package com.heima.commons.ai;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.heima.commons.constant.HtichConstants;
import com.heima.commons.enums.BusinessErrors;
import com.heima.commons.exception.BusinessRuntimeException;
import com.heima.commons.utils.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class BaiduAIHelper {

    private String apiKey;
    private String secretKey;
    private RedisTemplate<String, String> redisTemplate;

    public BaiduAIHelper(String apiKey, String secretKey, RedisTemplate<String, String> redisTemplate) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        this.redisTemplate = redisTemplate;

    }

    private static String ACCESS_TOKEN = null;

    private static final String ACCESS_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token?";

    private static final String LICENSE_PLATE_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/license_plate";

    private static final String ID_CARD_URL = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard";

    private static final String ID_CARD_AUTHENTICATION_URL = "https://aip.baidubce.com/rest/2.0/face/v3/person/idmatch";


    /**
     * 获取访问Token
     *
     * @return
     */
    public String getAccessToken() {
        // 获取token地址
        String getAccessTokenUrl = ACCESS_TOKEN_URL
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + apiKey
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + secretKey;

        String result = null;
        try {
            result = HttpClientUtils.doGet(getAccessTokenUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
        return jsonObject.getString("access_token");
    }

    /**
     * 获取 带有Token的URL
     *
     * @param requestUrl
     * @return
     */
    private String getApiAccessTokenUrl(String requestUrl) {
        if (StringUtils.isEmpty(ACCESS_TOKEN)) {
            ACCESS_TOKEN = getAccessToken();
        }
        return requestUrl + "?access_token=" + ACCESS_TOKEN;
    }

    /**
     * 获取与车牌信息
     *
     * @param imageurl
     * @return
     */
    public AIResult getLicensePlateMap(String imageurl) {
        String resultData = getCacheData(imageurl, url -> getLicensePlateData(url));
        List<AiParam> aiParams = parseData(resultData, AiParamFactory.getLicensePlateParam());
        return AIResult.build(aiParams);
    }

    /**
     * 获取身份证信息
     *
     * @param imageurl
     * @return
     */
    public AIResult getIdCardMap(String imageurl) {
        String resultData = getCacheData(imageurl, url -> getIdCardData(url));
        List<AiParam> aiParams = parseData(resultData, AiParamFactory.getIdCardParam());
        AIResult aiResult = AIResult.build(aiParams);
        //验证身份证是否匹配
        AIResult authResult = getIdCardAuthenticationMap(aiResult.getParameter("idCardNumber"), aiResult.getParameter("useralias"));
        aiResult.addResult(authResult);
        return aiResult;
    }


    /**
     * 身份证实名认证接口
     *
     * @param idCard 身份证号码
     * @param name   姓名
     * @return
     */
    public AIResult getIdCardAuthenticationMap(String idCard, String name) {
        //String resultData = getIdCardAuthenticationData(idCard, name);
        String resultData = getIdCardAuthenticationDataMock(idCard, name);
        List<AiParam> aiParams = parseData(resultData, AiParamFactory.getIdCardAuthParam());
        return AIResult.build(aiParams);
    }


    /**
     * 获取车辆数据
     *
     * @return
     */
    private String getLicensePlateData(String imageurl) {
        String reqUrl = getApiAccessTokenUrl(LICENSE_PLATE_URL);
        Map<String, String> param = new HashMap<>();
        param.put("url", imageurl);
        String result = null;
        try {
            result = HttpClientUtils.doPost(reqUrl, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取身份证数据
     *
     * @return
     */
    public String getIdCardData(String imageurl) {
        String reqUrl = getApiAccessTokenUrl(ID_CARD_URL);
        Map<String, String> param = new HashMap<>();
        param.put("url", imageurl);
        param.put("id_card_side", "front");
        String result = null;
        try {
            result = HttpClientUtils.doPost(reqUrl, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 获取实名认证数据
     *
     * @param idCard 身份证号码
     * @param name   姓名
     * @return
     */
    public String getIdCardAuthenticationData(String idCard, String name) {
        String reqUrl = getApiAccessTokenUrl(ID_CARD_AUTHENTICATION_URL);
        Map<String, String> param = new HashMap<>();
        param.put("id_card_number", idCard);
        param.put("name", name);
        String result = null;
        try {
            //httpclient调用接口
            result = HttpClientUtils.doPost(reqUrl, param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解析数据
     *
     * @param result
     * @param aiParamList
     * @return
     */
    public List<AiParam> parseData(String result, List<AiParam> aiParamList) {
        //将json字符串转换为JSONObject
        JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
        //判断是否包含有错误码
        if (null != jsonObject.get("error_code")) {
            throw new BusinessRuntimeException(BusinessErrors.AUTHENTICATION_ERROR, "身份认证错误");
        }
        //遍历需要解析的字段
        for (AiParam param : aiParamList) {
            String[] fieldArray = param.getField().split("\\.");
            Object tmp = jsonObject.get(fieldArray[0]);
            for (int i = 1; i < fieldArray.length; i++) {
                tmp = ((JSONObject) tmp).get(fieldArray[i]);
            }
            if (null != tmp) {
                param.setValue(String.valueOf(tmp));
            }

        }
        return aiParamList;
    }

    /**
     * 检查数据是否正确
     *
     * @param result
     * @return
     */
    private void checkResult(String result) {
        JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
        if (null != jsonObject.get("error_code")) {
            throw new BusinessRuntimeException(BusinessErrors.AUTHENTICATION_ERROR, "身份认证错误");
        }
    }

    /**
     * 缓存中获取数据
     *
     * @param imageURL
     * @param function
     * @return
     */
    private String getCacheData(String imageURL, Function<String, String> function) {
        //获取redis hash对象
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        //从redis中获取结果
        String result = hashOperations.get(HtichConstants.BAIDU_AI_RESULT, imageURL);
        //结果不为空返回数据
        if (StringUtils.isNotEmpty(result)) {
            return result;
        }
        //调用具体方法
        result = function.apply(imageURL);
        //校验结果数据
        checkResult(result);
        //校验通过设置缓存
        hashOperations.put(HtichConstants.BAIDU_AI_RESULT, imageURL, result);
        //返回结果数据
        return result;
    }

    private static Map<String, String> authMap = new HashMap<>();

    static {
        authMap.put("410122198909290077", "白云鹏");
    }

    /**
     * 身份认证接口mock测试
     *
     * @param idCard
     * @param name
     * @return
     */
    private String getIdCardAuthenticationDataMock(String idCard, String name) {
        String errorCode = "0";
        String errorMgs = "";
        String usernamne = authMap.get(idCard);
        if (StringUtils.isEmpty(usernamne)) {
            errorCode = "223103";
            errorMgs = "user is not exist";
        } else if (!usernamne.equals(name)) {
            errorCode = "222351";
            errorMgs = "id number and name not match or id number not exist";
        } else {
            errorCode = "0";
            errorMgs = "";
        }
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("errorCode", errorCode);
        resultMap.put("errorMgs", errorMgs);
        return JSON.toJSONString(resultMap);
    }
}
