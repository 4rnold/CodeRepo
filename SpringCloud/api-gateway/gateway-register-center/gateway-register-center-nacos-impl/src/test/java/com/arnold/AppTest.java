package com.arnold;

import static org.junit.Assert.assertTrue;

import com.arnold.common.config.ServiceDefinition;
import com.arnold.common.config.dubboInvoker.DubboServiceInvoker;
import com.arnold.common.config.invoker.AbstractServiceInvoker;
import com.arnold.common.config.invoker.HttpServiceInvoker;
import com.arnold.common.config.invoker.ServiceInvoker;
import com.arnold.common.utils.JSONUtil;
import org.junit.Test;

import java.util.HashMap;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        ServiceDefinition serviceDefinition = new ServiceDefinition();
        serviceDefinition.setUniqueId("1");
        serviceDefinition.setServiceId("2");
        serviceDefinition.setVersion("3");
        serviceDefinition.setProtocol("4");
        serviceDefinition.setPatternPath("5");
        serviceDefinition.setEnvType("6");
        serviceDefinition.setEnable(false);

        HttpServiceInvoker httpServiceInvoker = new HttpServiceInvoker();
        httpServiceInvoker.setInvokerPath("p");
        httpServiceInvoker.setTimeout(0);

        //dubbo
        DubboServiceInvoker dubboServiceInvoker = new DubboServiceInvoker();
        dubboServiceInvoker.setRegisterAddress("127.0.0.1:2181");
        dubboServiceInvoker.setInterfaceClass("com.arnold.common.config.dubboInvoker.DubboServiceInvoker");
        dubboServiceInvoker.setMethodName("ping");

        HashMap<String, AbstractServiceInvoker> stringServiceInvokerHashMap = new HashMap<>();
        stringServiceInvokerHashMap.put("/http", httpServiceInvoker);
        stringServiceInvokerHashMap.put("/duboo", dubboServiceInvoker);
        serviceDefinition.setInvokerMap(stringServiceInvokerHashMap);
        //{"uniqueId":"backend-http-server:1.0.0","serviceId":"backend-http-server","version":"1.0.0","protocol":"http","patternPath":"/http-demo/**","envType":"dev","enable":true,"invokerMap":{"/http-demo/ping":{"invokerPath":"/http-demo/ping","timeout":0}}}




        String jsonString = JSONUtil.toJSONString(serviceDefinition);
        System.out.println(jsonString);

        ServiceDefinition parse = JSONUtil.parse(jsonString, ServiceDefinition.class);
        System.out.println(parse);
    }
}
