package com.heima.account.handler;

import com.heima.account.service.AccountAPIService;
import com.heima.account.service.AuthenticationAPIService;
import com.heima.account.service.VehicleAPIService;
import com.heima.commons.ai.AIResult;
import com.heima.commons.ai.BaiduAIHelper;
import com.heima.commons.constant.HtichConstants;
import com.heima.commons.domin.vo.response.ResponseVO;
import com.heima.commons.entity.SessionContext;
import com.heima.commons.enums.BusinessErrors;
import com.heima.commons.exception.BusinessRuntimeException;
import com.heima.commons.helper.RedisSessionHelper;
import com.heima.commons.template.SessionTemplate;
import com.heima.commons.utils.CommonsUtils;
import com.heima.commons.utils.RequestUtils;
import com.heima.commons.utils.SnowflakeIdWorker;
import com.heima.modules.po.AccountPO;
import com.heima.modules.po.AuthenticationPO;
import com.heima.modules.po.VehiclePO;
import com.heima.modules.vo.AccountVO;
import com.heima.modules.vo.AuthenticationVO;
import com.heima.modules.vo.VehicleVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountHandler {

    private SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);

    @Autowired
    private RedisSessionHelper redisSessionHelper;

    @Autowired
    private SessionTemplate sessionTemplate;

    @Autowired
    private AccountAPIService accountAPIService;

    @Autowired
    private AuthenticationAPIService authenticationAPIService;

    @Autowired
    private VehicleAPIService vehicleAPIService;

    @Autowired
    private BaiduAIHelper baiduAIHelper;


    /**
     * 用户注册
     *
     * @param accountVO
     * @return
     */
    public ResponseVO<AccountVO> register(AccountVO accountVO) {
        if (StringUtils.isAnyEmpty(accountVO.getUsername(), accountVO.getPhone(), accountVO.getPassword())) {
            throw new BusinessRuntimeException(BusinessErrors.PARAM_CANNOT_EMPTY);
        }
        AccountPO accountPO = accountAPIService.checkLogin(CommonsUtils.toPO(accountVO));
        if (null != accountPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_DUPLICATION);
        }
        accountVO.setPassword(CommonsUtils.encodeMD5(accountVO.getPassword()));
        accountVO.setId(String.valueOf(idWorker.nextId()));
        //默认身份：乘客，车主认证后 为1
        accountVO.setRole(0);
        //默认用户未实名认证 认证通过后改为1
        accountVO.setStatus(0);
        //添加用户认证信息
        AccountPO result = accountAPIService.register(CommonsUtils.toPO(accountVO));
        return ResponseVO.success(result);
    }

    /**
     * 用户密码修改
     *
     * @param accountVO
     * @return
     */
    public ResponseVO<AccountVO> modifyPassword(AccountVO accountVO) {
        String userid = accountVO.getCurrentUserId();
        if (StringUtils.isAnyEmpty(accountVO.getPassword(), accountVO.getNewPassword())) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "新老密码不允许为空");
        }
        AccountPO accountPO = accountAPIService.getAccountByID(userid);
        if (null == accountPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "用户信息不存在");
        }
        String oldPassword = CommonsUtils.encodeMD5(accountVO.getPassword());
        String newPassword = CommonsUtils.encodeMD5(accountVO.getNewPassword());
        if (!oldPassword.equals(accountPO.getPassword())) {
            throw new BusinessRuntimeException(BusinessErrors.AUTHENTICATION_ERROR, "旧密码输入错误");
        }
        accountPO.setPassword(newPassword);
        accountAPIService.update(accountPO);
        return ResponseVO.success(null, "修改密码成功");
    }

    /**
     * 修改用户信息
     *
     * @param accountVO
     * @return
     */
    public ResponseVO<AccountVO> modify(AccountVO accountVO) {
        AccountPO accountPO = accountAPIService.getAccountByID(accountVO.getCurrentUserId());
        if (null == accountPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "用户信息不存在");
        }
        AccountPO po = CommonsUtils.toPO(accountVO);
        po.setId(accountPO.getId());
        accountAPIService.update(po);
        return ResponseVO.success(null, "修改用户信息成功");
    }


    /**
     * 生成Token
     *
     * @param accountVO
     * @return
     */
    public ResponseVO accountLogin(AccountVO accountVO) {
        AccountVO vo = verifyAccountLogin(accountVO);
        SessionContext sessionContext = redisSessionHelper.createSession(vo, vo.getId(), vo.getUsername(), vo.getUseralias(), null);
        vo.setToken(sessionContext.getSessionID());
        return ResponseVO.success(vo);
    }

    /**
     * 获取用户基本信息
     *
     * @param
     * @return
     */
    public ResponseVO<AccountVO> userinfo() {
        AccountPO accountPO = getCurrentAccountPO();
        return ResponseVO.success(accountPO);
    }


    /**
     * 获取用户认证信息
     *
     * @param
     * @return
     */
    public ResponseVO<AuthenticationVO> getAuthenticationInfo() {
        AccountPO accountPO = getCurrentAccountPO();
        AuthenticationPO authenticationPO = getAuthenticationPO(accountPO);
        if (null == authenticationPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "用户认证信息不存在");
        }
        return ResponseVO.success(authenticationPO);
    }

    /**
     * 修改用户认证信息
     *
     * @param authenticationVO
     * @return
     */
    public ResponseVO<AuthenticationVO> modifyAuthentication(AuthenticationVO authenticationVO) {
        AccountPO accountPO = getCurrentAccountPO();
        AuthenticationPO authenticationPO = getAuthenticationPO(accountPO);
        if (null == authenticationPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "用户认证信息不存在");
        }
        AuthenticationPO updatePo = CommonsUtils.toPO(authenticationVO);
        updatePo.setId(authenticationPO.getId());
        authenticationAPIService.update(updatePo);
        return ResponseVO.success(updatePo);
    }

    /**
     * 身份认证接口
     *
     * @return
     */
    public ResponseVO<AuthenticationVO> identityAuth() {
        AccountPO accountPO = getCurrentAccountPO();
        AuthenticationPO authenticationPO = getAuthenticationPO(accountPO);
        if (null == authenticationPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "用户认证信息不存在");
        }
        String cardIdFrontPhotoAddr = authenticationPO.getCardIdFrontPhoto();
        if (StringUtils.isEmpty(cardIdFrontPhotoAddr)) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "身份证正面照片不存在");
        }
        AIResult aiResult = baiduAIHelper.getIdCardMap(HtichConstants.getImageUrl(cardIdFrontPhotoAddr));
        authenticationPO.setBirth(aiResult.getParameter("birth"));
        authenticationPO.setUseralias(aiResult.getParameter("useralias"));
        authenticationPO.setCardId(aiResult.getParameter("idCardNumber"));
        //设置Account用户信息
        accountPO.setUseralias(authenticationPO.getUseralias());
        accountPO.setStatus(1);
        //更新Redis缓存
        sessionTemplate.updateSessionUseralias(accountPO.getId(), accountPO.getUseralias());
        accountAPIService.update(accountPO);
        authenticationAPIService.update(authenticationPO);
        return ResponseVO.success(authenticationPO);
    }


    /**
     * 获取车辆认证信息
     *
     * @return
     */
    public ResponseVO<VehicleVO> getVehicleInfo() {
        AccountPO accountPO = getCurrentAccountPO();
        VehiclePO vehiclePO = getVehiclePO(accountPO);
        if (null == vehiclePO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "用户认证信息不存在");
        }
        return ResponseVO.success(vehiclePO);
    }

    /**
     * 修改车辆信息
     *
     * @param vehicleVO
     * @return
     */
    public ResponseVO<VehicleVO> modifyVehicle(VehicleVO vehicleVO) {
        AccountPO accountPO = getCurrentAccountPO();
        VehiclePO vehiclePO = getVehiclePO(accountPO);
        if (null == vehiclePO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "车辆认证信息不存在");
        }
        VehiclePO updatePo = CommonsUtils.toPO(vehicleVO);
        updatePo.setId(vehiclePO.getId());
        vehicleAPIService.update(updatePo);
        return ResponseVO.success(updatePo);
    }


    /**
     * 身份认证接口
     *
     * @return
     */
    public ResponseVO<VehicleVO> vehicleAuth() {
        AccountPO accountPO = getCurrentAccountPO();
        VehiclePO vehiclePO = getVehiclePO(accountPO);
        if (null == vehiclePO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "车辆认证信息不存在");
        }
        String carFrontPhotoAddr = vehiclePO.getCarFrontPhoto();
        if (StringUtils.isEmpty(carFrontPhotoAddr)) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "车辆正面照片不存在");
        }
        AIResult aiResult = baiduAIHelper.getLicensePlateMap(HtichConstants.getImageUrl(carFrontPhotoAddr));
        vehiclePO.setCarNumber(aiResult.getParameter("card_number"));
        //设置用户角色为司机
        accountPO.setRole(1);
        accountAPIService.update(accountPO);
        vehicleAPIService.update(vehiclePO);
        return ResponseVO.success(vehiclePO);
    }


    private VehiclePO getVehiclePO(AccountPO accountPO) {
        return vehicleAPIService.selectByPhone(accountPO.getPhone());
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    private AccountPO getCurrentAccountPO() {
        String userId = RequestUtils.getCurrentUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessRuntimeException(BusinessErrors.AUTHENTICATION_ERROR);
        }
        AccountPO accountPO = accountAPIService.getAccountByID(userId);
        if (null == accountPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "用户信息不存在");
        }
        return accountPO;
    }

    /**
     * 获取用户认证对象
     *
     * @param accountPO
     * @return
     */

    private AuthenticationPO getAuthenticationPO(AccountPO accountPO) {
        if (null == accountPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST, "用户信息不存在");
        }
        return authenticationAPIService.selectByPhone(accountPO.getPhone());
    }

    /**
     * 验证账户登录
     *
     * @param accountVO
     * @return
     */
    private AccountVO verifyAccountLogin(AccountVO accountVO) {
        //用户或者手机号不能为空
        if (StringUtils.isAllEmpty(accountVO.getUsername(), accountVO.getPhone())) {
            throw new BusinessRuntimeException(BusinessErrors.PARAM_CANNOT_EMPTY);
        }
        //密码不能为空
        if (StringUtils.isEmpty(accountVO.getPassword())) {
            throw new BusinessRuntimeException(BusinessErrors.PARAM_CANNOT_EMPTY, "密码不能为空");
        }
        //查询用户登录
        AccountPO accountPO = accountAPIService.checkLogin(CommonsUtils.toPO(accountVO));
        //非空校验
        if (null == accountPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST);
        }
        //验证密码
        if (!CommonsUtils.encodeMD5(accountVO.getPassword()).equals(accountPO.getPassword())) {
            throw new BusinessRuntimeException(BusinessErrors.PARAM_CANNOT_EMPTY, "用户名或者密码错误");
        }
        return (AccountVO) CommonsUtils.toVO(accountPO);
    }

    /**
     * 验证Token
     *
     * @param sessionID
     * @return
     */
    public ResponseVO verifyToken(String sessionID) {
        SessionContext sessionContext = redisSessionHelper.getSession(sessionID);
        if (null == sessionContext) {
            throw new BusinessRuntimeException(BusinessErrors.TOKEN_IS_INVALID);
        }
        if (StringUtils.isEmpty(sessionContext.getAccountID())) {
            throw new BusinessRuntimeException(BusinessErrors.TOKEN_IS_INVALID);
        }
        AccountPO accountPO = accountAPIService.getAccountByID(sessionContext.getAccountID());
        if (null == accountPO) {
            throw new BusinessRuntimeException(BusinessErrors.DATA_NOT_EXIST);
        }

        return ResponseVO.success(accountPO);
    }

}
