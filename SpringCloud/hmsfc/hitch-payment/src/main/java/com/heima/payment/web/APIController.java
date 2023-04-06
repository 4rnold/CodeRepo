package com.heima.payment.web;

import com.github.wxpay.sdk.WXPayUtil;
import com.heima.commons.constant.HtichConstants;
import com.heima.commons.domin.vo.response.ResponseVO;
import com.heima.commons.groups.Group;
import com.heima.commons.initial.annotation.RequestInitial;
import com.heima.commons.utils.CommonsUtils;
import com.heima.modules.vo.OrderVO;
import com.heima.modules.vo.PaymentVO;
import com.heima.payment.handler.PaymentHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@RestController
@RequestMapping("/api/")
@Api(value = "支付操作Controller", tags = {"支付管理"})
@ApiResponses(@ApiResponse(code = 200, message = "处理成功"))
public class APIController {
    @Autowired
    private PaymentHandler paymentHandler;

    @ApiOperation(value = "异步通知API", tags = {"支付管理"})
    @RequestMapping("/nofify")
    public void notify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("支付成功回调。。。。");
        //输入流转换为xml字符串
        String xml = CommonsUtils.streamToString(request.getInputStream());
        if (StringUtils.isEmpty(xml)) {
            return;
        }
        System.out.println("notifyURL:" + xml);
        Map<String, String> payResultMap = WXPayUtil.xmlToMap(xml);
        PaymentVO paymentVO = new PaymentVO();
        if (!"SUCCESS".equals(payResultMap.get("return_code"))) {
            System.out.println("返回状态错误");
            return;
        }
        if (!"SUCCESS".equals(payResultMap.get("result_code"))) {
            System.out.println("返回结果状态错误");
            return;
        }
        paymentVO.setOrderId(payResultMap.get("out_trade_no"));
        paymentVO.setAmount(Float.parseFloat(payResultMap.get("total_fee")));
        //确认支付
        paymentHandler.confirmPay(paymentVO);
        //如果成功，给微信支付一个成功的响应
        response.setContentType("text/xml");
        response.getWriter().write(HtichConstants.WX_NOTIFY_SUCCESSFUL_RESPONSE_RESULT);
    }

    /**
     * 进行支付
     *
     * @return
     */
    @ApiOperation(value = "预支付接口API", tags = {"支付管理"})
    @PostMapping("/payment")
    @RequestInitial(groups = Group.Create.class)
    public ResponseVO<PaymentVO> payment(@RequestBody PaymentVO paymentVO) throws Exception {
        return paymentHandler.prePay(paymentVO);
    }

    @ApiOperation(value = "支付查询接口API", tags = {"支付管理"})
    @PostMapping("/query")
    @RequestInitial(groups = Group.Select.class)
    public ResponseVO<OrderVO> orderQuery(@RequestBody PaymentVO paymentVO) throws Exception {
        return paymentHandler.orderQuery(paymentVO);
    }


}
