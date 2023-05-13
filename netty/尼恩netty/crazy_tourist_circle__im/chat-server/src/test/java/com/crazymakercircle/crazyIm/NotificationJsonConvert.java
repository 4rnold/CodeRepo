package com.crazymakercircle.crazyIm;

import com.crazymakercircle.entity.ImNode;
import com.crazymakercircle.im.common.bean.Notification;
import com.crazymakercircle.util.JsonUtil;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

public class NotificationJsonConvert
{

    @Test
    public void convertFromJson()
    {
        ImNode imNode = new ImNode("unKnown", 111);;
        Notification<ImNode> notification = new Notification<ImNode>(imNode);
        notification.setType(Notification.CONNECT_FINISHED);
        String json = JsonUtil.pojoToJson(notification);


        Notification<ImNode> no2 = JsonUtil.jsonToPojo(json, new TypeToken<Notification<ImNode>>()
        {
        }.getType());

        System.out.println("no2 = " + no2);


    }

}
