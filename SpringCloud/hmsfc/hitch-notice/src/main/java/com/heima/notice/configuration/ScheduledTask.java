package com.heima.notice.configuration;


import com.heima.commons.utils.PollingRound;
import com.heima.modules.po.NoticePO;
import com.heima.notice.service.NoticeService;
import com.heima.notice.socket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 定时任务 推送暂存消息
 */
@Component
public class ScheduledTask {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    @Autowired
    private NoticeService noticeService;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Autowired
    private WebSocketServer webSocketServer;

    @PostConstruct
    public void init() {
        executorService.execute(() -> {
            autoPushMessage();
        });

    }


    /**
     * 自动推送消息
     */
    public void autoPushMessage() {
        //轮询并发送消息
        PollingRound.pollingPull(() -> {
            //获取最新需要推送的消息
            List<NoticePO> pushMessagesList = getPushMessages();
            //校验消息
            if (null != pushMessagesList && !pushMessagesList.isEmpty()) {
                logger.debug("推送消息线程工作工作中,推送数据条数:{}", pushMessagesList.size());
                //推送消息
                webSocketServer.pushMessage(pushMessagesList);
                return PollingRound.delayLoop(100);
            }
            logger.debug("推送消息线程工作工作中,推送数据条数:{}", 0);
            return PollingRound.delayLoop(1000);
        });
    }

    /**
     * 获取有效消息
     *
     * @return
     */
    public List<NoticePO> getPushMessages() {
        List<String> accountIds = webSocketServer.getInLineAccountIds();
        if (null != accountIds && !accountIds.isEmpty()) {
            //在MongoDB中获取当前在线用户的暂存消息
            List<NoticePO> pushMessageList = noticeService.getNoticeByAccountIds(accountIds);
            //返回消息
            return pushMessageList;
        }
        return null;
    }

}
