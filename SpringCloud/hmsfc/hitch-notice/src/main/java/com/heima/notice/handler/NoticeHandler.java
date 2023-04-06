package com.heima.notice.handler;

import com.heima.commons.domin.vo.response.ResponseVO;
import com.heima.commons.enums.BusinessErrors;
import com.heima.commons.exception.BusinessRuntimeException;
import com.heima.commons.utils.CommonsUtils;
import com.heima.modules.po.AccountPO;
import com.heima.modules.po.NoticePO;
import com.heima.modules.po.StrokePO;
import com.heima.modules.vo.NoticeVO;
import com.heima.notice.service.AccountAPIService;
import com.heima.notice.service.NoticeService;
import com.heima.notice.service.StrokeAPIService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class NoticeHandler {
    @Autowired
    private NoticeService noticeService;

    @Autowired
    private StrokeAPIService strokeAPIService;

    @Autowired
    private AccountAPIService accountAPIService;

    /**
     * 查询消息列表
     *
     * @param noticeVO
     * @return
     */
    public ResponseVO<NoticeVO> list(NoticeVO noticeVO) {
        initNotifyData(noticeVO);
        NoticePO noticePO = CommonsUtils.toPO(noticeVO);
        boolean check = checkParameter(noticePO);
        if (!check) {
            throw new BusinessRuntimeException(BusinessErrors.PARAM_CANNOT_EMPTY);
        }
        List<NoticePO> noticePOList = noticeService.queryList(noticeVO);
        return ResponseVO.success(noticePOList);
    }

    /**
     * 发送通知
     *
     * @param noticeVO
     */
    public boolean saveNotice(NoticeVO noticeVO) {
        boolean sendOK = false;
        initNotifyData(noticeVO);
        NoticePO noticePO = CommonsUtils.toPO(noticeVO);
        boolean check = checkParameter(noticePO);
        if (check) {
            noticeService.addNotice(noticePO);
            sendOK = true;
        }
        return sendOK;
    }

    /**
     * 初始化数据
     *
     * @param noticeVO
     */
    private void initNotifyData(NoticeVO noticeVO) {
        noticeVO.setSenderUseralias(getUserAlias(noticeVO.getSenderId()));
        String receiverId = noticeVO.getReceiverId();
        if (StringUtils.isEmpty(receiverId)) {
            StrokePO tripPO = strokeAPIService.selectByID(noticeVO.getTripId());
            if (null == tripPO) {
                return;
            }
            receiverId = tripPO.getPublisherId();
            noticeVO.setReceiverId(receiverId);
        }
        noticeVO.setReceiverUseralias(getUserAlias(receiverId));
    }

    public boolean checkParameter(NoticePO noticePO) {
        if (null == noticePO) {
            return false;
        }
        if (StringUtils.isEmpty(noticePO.getSenderId())) {
            return false;
        }
        if (StringUtils.isEmpty(noticePO.getReceiverId())) {
            return false;
        }
        return true;
    }

    /**
     * 获取当前用户姓名
     *
     * @param accountId
     * @return
     */
    private String getUserAlias(String accountId) {
        if (StringUtils.isEmpty(accountId)) {
            return null;
        }
        AccountPO accountPO = accountAPIService.getAccountByID(accountId);
        if (null == accountPO) {
            return null;
        }
        return StringUtils.isEmpty(accountPO.getUseralias()) ? accountPO.getUsername() : accountPO.getUseralias();
    }


}
