package cn.yubajin.std.Locator.impl;

import cn.yubajin.std.Locator.PropertySourceLocator;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.yubajin.std.listener.NodeDataChangeCuratorCacheListener;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.Map;

/**
 * ClassName: ZookeeperPropertySourceLocator
 * Description:
 * 通过PropertySourceLocator接口实现类去加载配置
 *  1、此处加载封装配置到PropertySource(MutablePropertySources)
 *  2、在ApplicationContextInitializer中将PropertySource加载到spring中
 * @author: yubj45081
 * @date: 2023/4/25 21:05
 */
public class ZookeeperPropertySourceLocator implements PropertySourceLocator {
    private static CuratorFramework curatorFramework;
    private final String DATA_NODE = "/config";

    public ZookeeperPropertySourceLocator() {
        curatorFramework= CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(10000)
                .connectionTimeoutMs(10000)
                .retryPolicy(new ExponentialBackoffRetry(1000,3))
                .build();
        curatorFramework.start();
    }
    @Override
    public PropertySource<?> locate(Environment environment, ConfigurableApplicationContext applicationContext) {
        System.out.println("开始加载外部化配置");
        CompositePropertySource composite = new CompositePropertySource("configService");
        try {
            Map<String,Object> dataMap = getRemoteEnvironment();
            MapPropertySource mapPropertySource = new MapPropertySource("configService", dataMap);
            // 加载远程Zookeeper的配置保存到一个PropertySource
            composite.addPropertySource(mapPropertySource);
            /**
             * 注释:
             * 添加节点的数据变更的事件监听
             * zk配置有修改则会触发
             * 触发后会发布spring事件
             ** 让修改了属性值的bean去更新属性
             ** 并且重新刷新获取属性值
             */
            addListener(environment, applicationContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return composite;
    }

    private Map<String,Object> getRemoteEnvironment() throws Exception {
        Thread.sleep(10000);
        String data = new String (curatorFramework.getData().forPath(DATA_NODE));
        // 支持JSON格式
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(data, Map.class);
        return map;
    }

    /**
     * 定义的监听器
     * zk配置有修改则会触发
     * @param environment
     * @param applicationContext
     */
    private void addListener(Environment environment, ConfigurableApplicationContext applicationContext){
        // 注释: 自定义的监听器，监听器监听后会去触发spring事件，让修改了属性值的bean去更新属性，并且重新刷新获取属性值;
        // 具体查看该事件
        NodeDataChangeCuratorCacheListener ndc = new NodeDataChangeCuratorCacheListener(environment, applicationContext);
        CuratorCache curatorCache = CuratorCache.build(curatorFramework, DATA_NODE, CuratorCache.Options.SINGLE_NODE_CACHE);
        CuratorCacheListener listener = CuratorCacheListener
                .builder()
                .forChanges(ndc).build();
        curatorCache.listenable().addListener(listener);
        curatorCache.start();
    }
}
