package com.heima.commons.template;

import com.heima.commons.constant.HtichConstants;
import com.heima.commons.entity.SessionContext;
import com.heima.commons.utils.SnowflakeIdWorker;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class SessionTemplate {
    //Session超时时间30分钟
    private int timeOut = 18000;


    /**
     * 创建session
     *
     * @param data
     * @param headerMap
     */
    public SessionContext createSession(Object data, String accountID, String username, String useralias, Map<String, List<String>> headerMap) {
        SessionContext sessionContext = getSessionByAccount(accountID);
        if (null != sessionContext) {
            deleteSession(sessionContext.getSessionID());
        }
        //创建Session
        sessionContext = new SessionContext(data, accountID, headerMap);
        sessionContext.setUsername(username);
        sessionContext.setUseralias(useralias);
        storeSession(sessionContext);
        //超时时间处理
        delayExpirTime(sessionContext.getSessionID(), DateUtils.addSeconds(new Date(), timeOut));
        return sessionContext;
    }

    /**
     * 更新Session 用户信息
     *
     * @param accountID
     * @param useralias
     * @return
     */
    public SessionContext updateSessionUseralias(String accountID, String useralias) {
        SessionContext sessionContext = getSessionByAccount(accountID);
        if (null == sessionContext) {
            return null;
        }
        sessionContext.setUseralias(useralias);
        storeSession(sessionContext);
        delayExpirTime(sessionContext.getSessionID(), DateUtils.addSeconds(new Date(), timeOut));
        return sessionContext;
    }


    public boolean isValid(SessionContext sessionContext) {
        if (null == sessionContext) {
            return false;
        }
        int isLetter = DateUtils.truncatedCompareTo(new Date(), DateUtils.addSeconds(sessionContext.getLastVisitTime(), timeOut), Calendar.MINUTE);
        if (isLetter > 0) {
            deleteSession(sessionContext.getSessionID());
            return false;
        }
        delayExpirTime(sessionContext.getSessionID(), DateUtils.addSeconds(new Date(), timeOut));
        return true;
    }


    /**
     * 存储Session
     */
    public abstract void storeSession(SessionContext sessionContext);

    /**
     * 延迟过期时间
     */
    public abstract void delayExpirTime(String sessionId, Date expirDate);


    /**
     * 获取Session
     *
     * @param sessionId
     * @return
     */
    public abstract SessionContext getSession(String sessionId);

    /**
     * 删除Session
     *
     * @param sessionId
     */
    public abstract void deleteSession(String sessionId);

    /**
     * 获取SessionID
     *
     * @param accountID
     * @return
     */
    public abstract SessionContext getSessionByAccount(String accountID);


}
