package jiagoubaiduren.aspect;

import org.apache.shardingsphere.api.hint.HintManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * DAO 强制全部走主库
 */
@Aspect
@Component
public class DaoForceMasterDbAspect {

    @Around("execution(* jiagoubaiduren.dao..*.*(..))")
    public Object around(ProceedingJoinPoint joinpoint) throws Throwable {
        if (!HintManager.isMasterRouteOnly()) {
            HintManager.getInstance().setMasterRouteOnly();
        }
        return joinpoint.proceed();
    }

}
