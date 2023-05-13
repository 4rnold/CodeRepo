package com.crazymakercircle.imServer.server.session.entity;

import com.crazymakercircle.entity.ImNode;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
@Data
@Builder
public class SessionCache implements Serializable
{
    private static final long serialVersionUID = -403010884211394856L;

    //用户的id
    private String userId;
    //session id
    private String sessionId;

    //节点信息
    private ImNode imNode;

    public SessionCache()
    {
        userId = "";
        sessionId = "";
        imNode = new ImNode("unKnown", 0);
    }

    public SessionCache(
            String sessionId, String userId, ImNode imNode)
    {
        this.sessionId = sessionId;
        this.userId = userId;
        this.imNode = imNode;
    }


}
