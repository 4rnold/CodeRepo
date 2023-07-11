package mayfly.core.permission;

import mayfly.core.exception.BizException;
import mayfly.core.model.result.CommonCodeEnum;
import mayfly.core.util.bean.BeanUtils;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author meilin.huang
 * @date 2020-03-03 8:40 上午
 */
public class LoginAccount implements Serializable {

    private static final long serialVersionUID = -8397728352145291302L;

    /**
     * 账号id
     */
    private Long id;

    /**
     * 账号用户名
     */
    private String username;

    /**
     * 线程上下文，用于保存到登录账号信息
     */
    private static final ThreadLocal<LoginAccount> CONTEXT = new ThreadLocal<>();

    public LoginAccount() {
    }

    public LoginAccount(Long id) {
        this.id = id;
    }

    /**
     * 创建登录账号信息
     *
     * @param id 账号id
     * @return {@linkplain LoginAccount}
     */
    public static LoginAccount create(Long id) {
        return new LoginAccount(id);
    }

    /**
     * 设置登录账号上下文
     *
     * @param loginAccount login account
     */
    public static void setToContext(LoginAccount loginAccount) {
        CONTEXT.set(loginAccount);
    }

    /**
     * 获取该线程上下文的登录账号
     */
    public static LoginAccount getFromContext() {
        return CONTEXT.get();
    }

    /**
     * 移除登录账号
     */
    public static void removeFromContext() {
        CONTEXT.remove();
    }

    /**
     * 从上下文获取登录账号id
     *
     * @return id（上下文没有登录信息则抛没有权限异常）
     */
    public static Long getLoginAccountId() {
        return Optional.ofNullable(LoginAccount.getFromContext()).map(LoginAccount::getId)
                .orElseThrow(() -> new BizException(CommonCodeEnum.NO_PERMISSION));
    }

    /**
     * 转换为另一登录账号实体类
     *
     * @param clazz class
     * @param <T>   实体泛型
     * @return 实体
     */
    public <T> T to(Class<T> clazz) {
        return BeanUtils.copy(this, clazz);
    }


    //-----    链式赋值调用   -----//

    public LoginAccount username(String username) {
        this.username = username;
        return this;
    }

    //---------  getter setter  --------//

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
