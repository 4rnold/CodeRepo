package com.tensquare.sms.listener;

import com.tensquare.sms.util.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 监听短信消息队列的监听器
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@Component
@RabbitListener(queues = "sms")
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String template_code;

    @Value("${aliyun.sms.sign_name}")
    private String sign_name;

    /**
     * 真正用于发送短信的方法
     * @param map
     */
    @RabbitHandler
    public void sendSms(Map<String,String> map)throws Exception{
        String mobile = map.get("mobile");
        String code = map.get("code");
        System.out.println("得到的手机号是："+mobile+",验证码是："+code);
        //发送验证码
        smsUtil.sendSms(mobile,template_code,sign_name,"{\"code\":\""+code+"\"}");
    }
}
