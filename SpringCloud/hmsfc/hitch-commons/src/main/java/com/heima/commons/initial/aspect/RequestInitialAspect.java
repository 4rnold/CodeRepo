package com.heima.commons.initial.aspect;

import com.heima.commons.initial.annotation.RequestInitial;
import com.heima.commons.initial.factory.InitialParserFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;


@Aspect
public class RequestInitialAspect {
    @Autowired
    private HttpServletRequest request;

    @Pointcut("@annotation(com.heima.commons.initial.annotation.RequestInitial)")
    public void annotationPoinCut() {
    }

    @Around("annotationPoinCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        RequestInitial requestInitial = getRequestInitial(pjp);
        Object[] parameterValues = pjp.getArgs();
        if (null != requestInitial) {
            //拦截后给属性赋值
            InitialParserFactory.initialDefValue(parameterValues, requestInitial);
        }
        return pjp.proceed(parameterValues);
    }

    private RequestInitial getRequestInitial(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        return method.getAnnotation(RequestInitial.class);
    }

}
