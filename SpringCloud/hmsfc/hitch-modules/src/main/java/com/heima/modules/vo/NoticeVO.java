package com.heima.modules.vo;

import com.heima.commons.domin.vo.VO;
import com.heima.commons.enums.InitialResolverType;
import com.heima.commons.groups.Group;
import com.heima.commons.initial.annotation.InitialResolver;
import com.heima.modules.po.NoticePO;

public class NoticeVO implements VO {

    /**
     * 发送人ID
     */
    /**
     * 当前登陆人ID
     */
    @InitialResolver(resolver = InitialResolverType.CURRENTA_ACCOUNT, groups = {Group.Select.class})
    private String senderId;

    /**
     * 发送人姓名
     */
    private String senderUseralias;

    /**
     * 接收人ID
     */
    private String receiverId;

    /**
     * 接收人人姓名
     */
    private String receiverUseralias;

    /**
     *
     * 乘客行程ID
     */
    private String tripId;


    private String message;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderUseralias() {
        return senderUseralias;
    }

    public void setSenderUseralias(String senderUseralias) {
        this.senderUseralias = senderUseralias;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverUseralias() {
        return receiverUseralias;
    }

    public void setReceiverUseralias(String receiverUseralias) {
        this.receiverUseralias = receiverUseralias;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Class getPO() {
        return NoticePO.class;
    }
}
