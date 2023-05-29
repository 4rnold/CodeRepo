package com.lagou.kafka.demo.monitor;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

public class JMXMonitorDemo {

    public static void main(String[] args) throws IOException, MalformedObjectNameException, AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException {

        String jmxServiceURL = "service:jmx:rmi:///jndi/rmi://192.168.100.103:9581/jmxrmi";

        JMXServiceURL jmxURL = null;
        JMXConnector jmxc = null;
        MBeanServerConnection jmxs = null;
        ObjectName mbeanObjName = null;
        Iterator sampleIter = null;
        Set sampleSet = null;

        // 创建JMXServiceURL对象，参数是
        jmxURL = new JMXServiceURL(jmxServiceURL);
        // 建立到指定URL服务器的连接
        jmxc = JMXConnectorFactory.connect(jmxURL);
        // 返回代表远程MBean服务器的MBeanServerConnection对象
        jmxs = jmxc.getMBeanServerConnection();
        // 根据传入的字符串，创建ObjectName对象
//        mbeanObjName = new ObjectName("kafka.server:type=BrokerTopicMetrics,name=MessagesInPerSec");
//        mbeanObjName = new ObjectName("kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec,topic=tp_eagle_01");
        mbeanObjName = new ObjectName("kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec");
        // 获取指定ObjectName对应的MBeans
        sampleSet = jmxs.queryMBeans(null, mbeanObjName);
        // 迭代器
        sampleIter = sampleSet.iterator();

        if (sampleSet.isEmpty()) {

        } else {
            // 如果返回了，则打印信息
            while (sampleIter.hasNext()) {
                // Used to represent the object name of an MBean and its class name.
                // If the MBean is a Dynamic MBean the class name should be retrieved from the MBeanInfo it provides.
                // 用于表示MBean的ObjectName和ClassName
                ObjectInstance sampleObj = (ObjectInstance) sampleIter.next();
                ObjectName objectName = sampleObj.getObjectName();
                // 查看指定MBean指定属性的值
                String count = jmxs.getAttribute(objectName, "Count").toString();
                System.out.println(count);
            }
        }

        // 关闭
        jmxc.close();
    }

}