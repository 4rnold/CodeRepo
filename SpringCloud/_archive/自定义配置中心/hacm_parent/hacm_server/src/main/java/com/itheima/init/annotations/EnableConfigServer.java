package com.itheima.init.annotations;

import com.itheima.init.auto.ConfigServerAutoImport;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author 黑马程序员
 * @Company http://www.itheima.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(ConfigServerAutoImport.class)
public @interface EnableConfigServer {
}
