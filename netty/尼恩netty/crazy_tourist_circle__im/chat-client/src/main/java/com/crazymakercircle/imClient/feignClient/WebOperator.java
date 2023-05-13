package com.crazymakercircle.imClient.feignClient;

import com.crazymakercircle.constants.ServerConstants;
import com.crazymakercircle.entity.LoginBack;
import com.crazymakercircle.util.JsonUtil;
import feign.Feign;
import feign.codec.StringDecoder;

public class WebOperator
{

    public static LoginBack login(String userName, String password)
    {
        UserAction action = Feign.builder()
//                .decoder(new GsonDecoder())
                .decoder(new StringDecoder())
                .target(UserAction.class, ServerConstants.WEB_URL);

        String s = action.loginAction(userName, password);

        LoginBack back = JsonUtil.jsonToPojo(s, LoginBack.class);
        return back;

    }
}
