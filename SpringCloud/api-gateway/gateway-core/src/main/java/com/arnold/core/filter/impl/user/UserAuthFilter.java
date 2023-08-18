package com.arnold.core.filter.impl.user;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.arnold.common.constants.FilterConst;
import com.arnold.common.enums.ResponseCode;
import com.arnold.common.exception.ResponseException;
import com.arnold.common.rule.Rule;
import com.arnold.core.context.GatewayContext;
import com.arnold.core.filter.Filter;
import com.arnold.core.filter.FilterAspect;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import io.netty.handler.codec.http.cookie.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.HashMap;

import static com.arnold.common.constants.FilterConst.*;

@Slf4j
@FilterAspect(id = FilterConst.USER_AUTH_FILTER_ID, name = USER_AUTH_FILTER_NAME, order = USER_AUTH_FILTER_ORDER)
public class UserAuthFilter implements Filter {

    private static final String SECRET_KEY = "secretKey";

    private static final String COOKIE_NAME = "jwt";

    @Override
    public void doFilter(GatewayContext context) {
        if (context.getRule().getFilterConfigById(USER_AUTH_FILTER_ID) == null) {
            return;
        }
        Cookie cookie = context.getRequest().getCookie(COOKIE_NAME);
        if (cookie == null) {
            throw new ResponseException(ResponseCode.UNAUTHORIZED);
        }
        String token = cookie.value();
        if(StringUtils.isBlank(token)){
            throw new ResponseException(ResponseCode.UNAUTHORIZED);
        }

        if (!JWTUtil.verify(token, SECRET_KEY.getBytes())) {
            throw new ResponseException(ResponseCode.UNAUTHORIZED);
        }

        try {
            long userId =parseUserId(token);
            context.getRequest().setUserId(userId);
        } catch (Exception e) {
            e.printStackTrace( );
            throw new ResponseException(ResponseCode.UNAUTHORIZED);
        }
    }

    private long parseUserId(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        return NumberUtils.toLong(jwt.getPayload("userId").toString());
    }

    public static void main(String[] args) {
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("userId", 100010L);
        String token = JWTUtil.createToken(payload, SECRET_KEY.getBytes());
        System.out.println(token);

        JWT jwt = JWTUtil.parseToken(token);
        System.out.println(jwt.getPayload("userId"));

    }

}