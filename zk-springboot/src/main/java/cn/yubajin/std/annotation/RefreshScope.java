package cn.yubajin.std.annotation;

import java.lang.annotation.*;

/**
 * ClassName: RefreshScope
 * Description:
 * 注解用于区分哪些是zk对应的配置类
 * @author: yubj45081
 * @date: 2023/4/25 23:50
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RefreshScope {
}
