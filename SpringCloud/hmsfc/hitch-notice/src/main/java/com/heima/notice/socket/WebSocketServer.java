package com.heima.notice.socket;


import com.alibaba.fastjson.JSON;
import com.heima.commons.constant.HtichConstants;
import com.heima.commons.domin.vo.response.ResponseVO;
import com.heima.commons.entity.SessionContext;
import com.heima.commons.enums.BusinessErrors;
import com.heima.commons.helper.RedisSessionHelper;
import com.heima.commons.utils.LocalCollectionUtils;
import com.heima.commons.utils.SpringUtil;
import com.heima.modules.po.NoticePO;
import com.heima.modules.vo.NoticeVO;
import com.heima.notice.handler.NoticeHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/ws/socket")
public class WebSocketServer {


    //concurrent包的线程安全Map，用来存放每个客户端对应的WebSocketServer对象。
    private static Map<String, Session> sessionPools = new ConcurrentHashMap<>();

    /**
     * 获取所有在线用户列表
     *
     * @return
     */
    public List<String> getInLineAccountIds() {
        List<String> list = new ArrayList();
        list.addAll(sessionPools.keySet());
        return list;
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        String accountId = validToken(session);
        if (StringUtils.isEmpty(accountId)) {
            return;
        }
        NoticeVO noticeVO = JSON.parseObject(message, NoticeVO.class);
        noticeVO.setSenderId(accountId);
        NoticeHandler noticeHandler = SpringUtil.getBean(NoticeHandler.class);
        if (null != noticeHandler) {
            boolean sendOK = noticeHandler.saveNotice(noticeVO);
            if (!sendOK) {
                ResponseVO responseVO = ResponseVO.error(BusinessErrors.WS_SEND_FAILED);
                sendMessage(session, JSON.toJSONString(responseVO));
            }
        }
    }


    /**
     * 连接建立成功调用
     *
     * @param session 客户端与socket建立的会话
     * @param session 客户端的userId
     */
    @OnOpen
    public void onOpen(Session session) {
        String accountId = validToken(session);
        if (StringUtils.isEmpty(accountId)) {
            return;
        }
        sessionPools.remove(accountId);
        sessionPools.put(accountId, session);
    }

    /**
     * 关闭连接时调用
     *
     * @param session 关闭连接的客户端的姓名
     */
    @OnClose
    public void onClose(Session session) {
        String accountId = validToken(session);
        if (StringUtils.isEmpty(accountId)) {
            return;
        }
        sessionPools.remove(accountId);
    }


    /**
     * 发生错误时候
     *
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("发生错误");
        throwable.printStackTrace();
    }

    /**
     * 给指定用户发送消息
     *
     * @param noticePO 需要推送的消息
     * @throws IOException
     */
    public void pushMessage(NoticePO noticePO) {
        //获取当前会话
        Session session = sessionPools.get(noticePO.getReceiverId());
        if (null != session && null != noticePO) {
            //获取消息体
            sendMessage(session, JSON.toJSONString(noticePO));
        }
    }

    /**
     * 发送消息
     *
     * @param session
     * @param message
     */
    private void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量发送消息
     *
     * @param messageBOList
     */
    public void pushMessage(List<NoticePO> messageBOList) {
        if (null != messageBOList && !messageBOList.isEmpty()) {
            for (NoticePO noticePO : messageBOList) {
                pushMessage(noticePO);
            }
        }
    }

    private String validToken(Session session) {
        String token = getSessionToken(session);
        RedisSessionHelper redisSessionHelper = SpringUtil.getBean(RedisSessionHelper.class);
        if (null == redisSessionHelper) {
            return null;
        }
        SessionContext context = redisSessionHelper.getSession(token);
        boolean isisValid = redisSessionHelper.isValid(context);
        if (isisValid) {
            return context.getAccountID();
        }
        return null;
    }

    private String getSessionToken(Session session) {
        Map<String, List<String>> paramMap = session.getRequestParameterMap();
        List<String> paramList = paramMap.get(HtichConstants.SESSION_TOKEN_KEY);
        return LocalCollectionUtils.getOne(paramList);
    }
}