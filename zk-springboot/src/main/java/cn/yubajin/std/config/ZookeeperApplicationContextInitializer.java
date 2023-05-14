package cn.yubajin.std.config;

import cn.yubajin.std.Locator.PropertySourceLocator;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ClassName: ZookeeperApplicationContextInitializer
 * Description:
 *
 *  initializer将locator加载到PropertySource的配置
 *  加载到环境environment中去
 * @Author: yubj45081
 * Date: 2023/4/18 0:42
 */
public class ZookeeperApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    
    private final List<PropertySourceLocator> propertySourceLocators;
    
    public ZookeeperApplicationContextInitializer() {
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        //加载所有的PropertySourceLocator的扩展实现（SPI）
        propertySourceLocators = new ArrayList<>(SpringFactoriesLoader
                .loadFactories(PropertySourceLocator.class, classLoader));
        System.out.println("====加载所有的PropertySourceLocator的扩展实现（SPI）====");
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        //去动态加载扩展的配置 到Environment中
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        MutablePropertySources mutablePropertySources = environment.getPropertySources();
        for (PropertySourceLocator locator : this.propertySourceLocators) {
           Collection<PropertySource<?>> sources = locator.locateCollection(environment, applicationContext);
           if (sources==null || sources.size() == 0){
               continue;
           }
           for (PropertySource<?> p : sources) {
               mutablePropertySources.addLast(p); //把属性源添加到Environment
           }
        }
    }
}
