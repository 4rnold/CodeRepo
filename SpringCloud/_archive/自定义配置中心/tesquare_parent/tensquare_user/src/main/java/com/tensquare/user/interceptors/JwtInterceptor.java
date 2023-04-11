package com.tensquare.user.interceptors;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器：验证权限的
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 此方法是拦截方法，当返回值为true的时候，就是放行
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.取出消息头
        String header = request.getHeader("Authorization");
        //2.判断消息头是否存在，且符合规则
        if(!StringUtils.isEmpty(header) && header.startsWith("Bearer ")){
            //3.取出消息头的token信息
            String token = header.split(" ")[1];
            //4.验证token
            Claims claims = jwtUtil.parseJWT(token);
            //5.判断是用户权限还是管理员权限
            String roles = (String)claims.get("roles");
            if("user_role".equals(roles)){
                request.setAttribute("user_claims",claims);
            }else if("admin_role".equals(roles)){
                request.setAttribute("admin_claims",claims);
            }else{
                //没权限
            }
        }

        return true;//无论有没有权限都放行
    }
}
