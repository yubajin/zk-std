//package cn.yubajin.std.rebinder;
//
//import cn.yubajin.std.config.ConfigurationPropertiesBeans;
//import cn.yubajin.std.event.EnvironmentChangeEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Component;
//
///**
// * ClassName: ConfigurationPropertiesRebinder
// * Description:
// * rebinder: 重新绑定器
// * 定义监听到绑定事件(zk配置修改)需要做的操作
// *      监听到后，需要重置设置属性值，并且刷新到spring中
// * @author: yubj45081
// * @date: 2023/4/25 23:24
// */
// TODO 模拟配置动态刷新
//@Component
//public class ConfigurationPropertiesRebinder  implements ApplicationListener<EnvironmentChangeEvent> {
//
//    private ConfigurationPropertiesBeans beans;
//    private Environment environment;
//
//    public ConfigurationPropertiesRebinder(ConfigurationPropertiesBeans beans, Environment environment) {
//        this.beans=beans;
//        this.environment=environment;
//    }
//
//    @Override
//    public void onApplicationEvent(EnvironmentChangeEvent environmentChangeEvent) {
//        System.out.println("收到environment变更事件");
//        rebind();
//    }
//
//    // 重置设置，在收到事件后
//    // 后置处理器ConfigurationPropertiesBeans在spring启动时
//    // 就将fieldMapper中的bean, field和value设置了值
//    public void rebind(){
//        this.beans.getFieldMapper().forEach((k, v) -> {
//            v.forEach(f -> f.resetValue(environment));
//        });
//    }
//}
