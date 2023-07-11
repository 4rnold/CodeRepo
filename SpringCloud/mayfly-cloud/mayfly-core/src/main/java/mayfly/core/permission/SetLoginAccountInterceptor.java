package mayfly.core.permission;

import mayfly.core.constant.HeaderConst;
import mayfly.core.exception.BizException;
import mayfly.core.util.StringUtils;
import mayfly.core.web.WebUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 设置登录账号信息拦截器,用于将请求头中的账号信息赋值于ThreadLocal中的{@link LoginAccount}
 *
 * @author hml
 */
public class SetLoginAccountInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }
        try {
            if (!(handler instanceof HandlerMethod)) {
                return true;
            }
            String accountId = request.getHeader(HeaderConst.ACCOUNT_ID);
            String username = request.getHeader(HeaderConst.ACCOUNT_USERNAME);
            if (!StringUtils.isEmpty(accountId) && !StringUtils.isEmpty(username)) {
                LoginAccount.setToContext(LoginAccount.create(Long.parseLong(accountId)).username(WebUtils.urlDecode(username)));
            }
            return true;
        } catch (BizException e) {
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 移除ThreadLocal值
        LoginAccount.removeFromContext();
    }
}
