package jiagoubaiduren.aspect;

import org.apache.shardingsphere.api.hint.HintManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SqlReadSlaveDbAspect {

    @Around(value = "@annotation(sqlReadSlave)")
    public Object around(ProceedingJoinPoint joinpoint, SqlReadSlave sqlReadSlave) throws Throwable {
        if (HintManager.isMasterRouteOnly()) {
            HintManager.clear();
        }

        return joinpoint.proceed();
    }

}
