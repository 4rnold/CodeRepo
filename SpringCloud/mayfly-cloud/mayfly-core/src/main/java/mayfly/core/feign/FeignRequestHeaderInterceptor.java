package mayfly.core.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import mayfly.core.constant.HeaderConst;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 设置一些请求头信息将其传递至被调用者,如登录账号信息，token以及调用者应用名等.
 *
 * @author meilin.huang
 * @date 2022-04-06 10:30
 */
public class FeignRequestHeaderInterceptor implements RequestInterceptor {

    private final String application;

    public FeignRequestHeaderInterceptor(String applicationName) {
        this.application = applicationName;
    }

    @Override
    public void apply(RequestTemplate template) {
        // 记录调用方信息
        template.header(HeaderConst.CALLER, application);
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return;
        }

        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 向接口传递登录账号及token信息
        template.header(HeaderConst.ACCOUNT_ID, request.getHeader(HeaderConst.ACCOUNT_ID));
        template.header(HeaderConst.ACCOUNT_USERNAME, request.getHeader(HeaderConst.ACCOUNT_USERNAME));
        template.header(HeaderConst.TOKEN_NAME, request.getHeader(HeaderConst.TOKEN_NAME));
    }
}
