package cn.yubajin.std.Locator;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * ClassName: PropertySourceLocator
 * Description:
 *
 * @Author: yubj45081
 * Date: 2023/4/18 0:26
 */
public interface PropertySourceLocator {

    PropertySource<?> locate(Environment environment, ConfigurableApplicationContext applicationContext);

    // 默认方法
    default Collection<PropertySource<?>>locateCollection(Environment environment, ConfigurableApplicationContext applicationContext) {
        return locateCollections(this, environment, applicationContext);

    }

    // 默认方法具体实现
    static Collection<PropertySource<?>> locateCollections(PropertySourceLocator locator, Environment environment, ConfigurableApplicationContext applicationContext) {
        PropertySource<?> propertySource = locator.locate(environment, applicationContext);
        if (propertySource == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(propertySource);
    }
}
