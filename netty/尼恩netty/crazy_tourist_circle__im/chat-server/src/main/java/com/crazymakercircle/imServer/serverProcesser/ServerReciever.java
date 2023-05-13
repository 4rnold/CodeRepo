package com.crazymakercircle.imServer.serverProcesser;


import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.imServer.server.session.LocalSession;

/**
 * 操作类
 */
public interface ServerReciever
{

    ProtoMsg.HeadType op();

    Boolean action(LocalSession ch, ProtoMsg.Message proto);

}
