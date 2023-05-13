package com.crazymakercircle.imClient.clientMsgProcesser;


import com.crazymakercircle.im.common.bean.msg.ProtoMsg;
import com.crazymakercircle.imClient.client.ClientSession;

/**
 * 操作类
 */
public interface Proc
{

    ProtoMsg.HeadType op();

    void action(ClientSession ch, ProtoMsg.Message proto) throws Exception;

}
