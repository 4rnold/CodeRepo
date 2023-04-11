package com.itheima.init.auto;
//import com.github.tobato.fastdfs.FdfsClientConfig;
import com.github.tobato.fastdfs.FdfsClientConfig;
import com.itheima.utils.IdWorkerUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * 配置中心服务端自动配种导入
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Import(FdfsClientConfig.class) // 导入FastDFS-Client组件
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING) // 解决jmx重复注册bean的问题
//@ComponentScan({"com.itheima"})
public class ConfigServerAutoImport {


    @Bean
    public IdWorkerUtil createIdWorkerUtil(){
        return new IdWorkerUtil(1,1);
    }
}
