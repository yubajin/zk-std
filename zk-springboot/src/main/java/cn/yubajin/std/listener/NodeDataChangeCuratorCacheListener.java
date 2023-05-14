package cn.yubajin.std.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.yubajin.std.event.EnvironmentChangeEvent;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCacheListenerBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

/**
 * ClassName: NodeDataChangeCuratorCacheListener
 * Description:
 *  zk配置修改的监听器，修改后发布spring事件，让修改了属性值的bean去更新属性
 *      并且重新刷新获取属性值
 *
 * @author: yubj45081
 * @date: 2023/4/25 23:11
 */
public class NodeDataChangeCuratorCacheListener implements CuratorCacheListenerBuilder.ChangeListener {

    private Environment environment;
    private ConfigurableApplicationContext applicationContext;

    public NodeDataChangeCuratorCacheListener(Environment environment, ConfigurableApplicationContext applicationContext) {
        this.environment = environment;
        this.applicationContext = applicationContext;
    }

    @Override
    public void event(ChildData oldNode, ChildData node) {
        System.out.println("收到数据变更事件");
        String resultData = new String (node.getData());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //吧json格式的数据转换为map
            Map<String,Object> map = objectMapper.readValue(resultData, Map.class);
            MapPropertySource mapPropertySource = new MapPropertySource("configService", map);

            ConfigurableEnvironment cfe = (ConfigurableEnvironment)this.environment;
            //注释: 环境中替换掉原来的PropertySource
            cfe.getPropertySources().replace("configService", mapPropertySource);
            /**
             * 发送一个数据变更事件
             * 订阅事件的会去修改属性值
             */
            // TODO 模拟配置动态刷新
            // 发事件只是更新bean属性值，环境中的值已经变了，输出的是环境中的值，
            // 输出的如果是属性值的话，才需要发事件去同步bean的属性值，
            // 才需要rebinder去将环境的值同步到bean,
            // 才需要PropertiesProcessor设置fieldMapper给rebinder用
//            applicationContext.publishEvent(new EnvironmentChangeEvent(this));
            System.out.println("数据更新完成");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
