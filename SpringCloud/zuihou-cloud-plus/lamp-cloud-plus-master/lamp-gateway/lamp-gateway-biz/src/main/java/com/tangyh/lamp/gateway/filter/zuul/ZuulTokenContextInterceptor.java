package com.tangyh.lamp.gateway.filter.zuul;

import cn.hutool.core.util.StrUtil;
import com.tangyh.basic.context.ContextConstants;
import com.tangyh.basic.context.ContextUtil;
import com.tangyh.basic.database.properties.MultiTenantType;
import com.tangyh.basic.jwt.TokenUtil;
import com.tangyh.basic.jwt.model.AuthInfo;
import com.tangyh.basic.jwt.utils.JwtUtil;
import com.tangyh.lamp.common.properties.IgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.tangyh.basic.context.ContextConstants.BASIC_HEADER_KEY;
import static com.tangyh.basic.context.ContextConstants.BEARER_HEADER_KEY;
import static com.tangyh.basic.context.ContextConstants.JWT_KEY_TENANT;

/**
 * 解决zuul服务的Controller请求，无法先通过zuul的过滤器（TokenContextFilter）解析租户编码和用户token的问题
 * <p>
 * 这个拦截器会拦截zuul 服务中所有的Controller，实现的功能跟 （TokenContextFilter） 基本一致
 *
 * @author zuihou
 * @date 2019-06-20 22:22
 */
@Slf4j
@AllArgsConstructor
public class ZuulTokenContextInterceptor extends HandlerInterceptorAdapter {

    private final TokenUtil tokenUtil;
    private final String multiTenantType;
    protected final IgnoreProperties ignoreTokenProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return super.preHandle(request, response, handler);
        }

        //1, 解码 请求头中的租户信息
        if (!MultiTenantType.NONE.name().equals(multiTenantType)) {
            String base64Tenant = getHeader(JWT_KEY_TENANT, request);
            if (StrUtil.isNotEmpty(base64Tenant)) {
                String tenant = JwtUtil.base64Decoder(base64Tenant);
                ContextUtil.setTenant(tenant);
                MDC.put(ContextConstants.JWT_KEY_TENANT, ContextUtil.getTenant());
            }
        }

        // 2,解码 Authorization 后面完善
        String base64Authorization = getHeader(BASIC_HEADER_KEY, request);
        if (StrUtil.isNotEmpty(base64Authorization)) {
            String[] client = JwtUtil.getClient(base64Authorization);
            ContextUtil.setClientId(client[0]);
        }

        // 忽略 token 认证的接口
        if (isIgnoreToken(request.getRequestURI())) {
            log.debug("access filter not execute");
            return super.preHandle(request, response, handler);
        }

        //获取token， 解析，然后想信息放入 header
        //3, 获取token
        String token = getHeader(BEARER_HEADER_KEY, request);

        // 4, 解析 并 验证 token
        AuthInfo authInfo = tokenUtil.getAuthInfo(token);

        //6, 转换，将 token 解析出来的用户身份 和 解码后的tenant、Authorization 重新封装到请求头
        if (authInfo != null) {
            ContextUtil.setUserId(authInfo.getUserId());
            ContextUtil.setName(authInfo.getName());
            ContextUtil.setAccount(authInfo.getAccount());
            MDC.put(ContextConstants.JWT_KEY_USER_ID, String.valueOf(authInfo.getUserId()));
        }

        return super.preHandle(request, response, handler);
    }

    /**
     * 忽略应用级token
     *
     */
    protected boolean isIgnoreToken(String uri) {
        return ignoreTokenProperties.isIgnoreToken(uri);
    }

    protected String getHeader(String headerName, HttpServletRequest request) {
        String token = request.getHeader(headerName);
        if (StrUtil.isBlank(token)) {
            token = request.getParameter(headerName);
        }
        return token;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ContextUtil.remove();
        super.afterCompletion(request, response, handler, ex);
    }

}
