package com.heima.modules.po;

import com.heima.commons.domin.po.PO;
import com.heima.modules.vo.NoticeVO;

import java.io.Serializable;
import java.util.Date;

public class NoticePO implements Serializable, PO {

    /**
     * 发送人ID
     */
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
     * 乘客行程ID
     */
    private String tripId;

    /**
     * 是否已读
     */
    private boolean read = false;


    private String message;

    /**
     * 创建时间
     */
    private Date createdTime;

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

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public Class getVO() {
        return NoticeVO.class;
    }
}
