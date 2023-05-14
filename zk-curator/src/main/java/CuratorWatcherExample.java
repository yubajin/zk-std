import listen.ZKWatcherListener;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;

/**
 * ClassName: CuratorWatcher
 * Description:
 *
 * @Author: yubj45081
 * Date: 2023/4/16 14:28
 */
public class CuratorWatcherExample {

    static CuratorFramework curatorFramework = getConnection();

    static CuratorFramework getConnection() {
        CuratorFramework curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectionTimeoutMs(20000)
                .connectString("127.0.0.1:2181")
                // baseSleepTimeMs*Math.max(1,random.nextInt(1<<(maxRetries+1))
                /**
                 * RetryNTimes 指定最大重试次数
                 * RetryOneTimes
                 * RetryUntilElapsed 一直重试，直到达到规定时间
                 */
                .retryPolicy(new ExponentialBackoffRetry(1000, 5))
                .sessionTimeoutMs(15000)
                .build();
        curatorFramework.start();
        return curatorFramework;
    }

    public void normalWatch(String path) throws Exception {
        CuratorWatcher curatorWatcher = new CuratorWatcher() {
            @Override
            public void process(WatchedEvent watchedEvent) throws Exception {
                System.out.println("监听的事件：" + watchedEvent.toString());
                // 能够监听到多次
//                curatorFramework.checkExists().usingWatcher(this).forPath(watchedEvent.getPath());
            }
        };
        String node = curatorFramework.create().forPath(path, "watch string".getBytes());
        curatorFramework.getData().usingWatcher(curatorWatcher).forPath(node);

    }

    public void persistWatcher(String node) throws Exception {
        // PathChildCache / NodeCache /TreeCache
        //CuratorCacheListener
        CuratorCache curatorCache = CuratorCache.
                build(curatorFramework, node, CuratorCache.Options.SINGLE_NODE_CACHE);
        CuratorCacheListener listener= CuratorCacheListener
                .builder()
                .forAll(new ZKWatcherListener())
                .build();
        curatorCache.listenable().addListener(listener);
        curatorCache.start();
    }

    public void operation(String node) throws Exception {
        curatorFramework.setData().forPath(node, "change date 0".getBytes());
        Thread.sleep(1000);
        curatorFramework.setData().forPath(node, "change date 1".getBytes());
    }

    public void operation1(String node) throws Exception {
        curatorFramework.create().forPath(node);
        curatorFramework.setData().forPath(node, "change persis date 0".getBytes());
        curatorFramework.setData().forPath(node, "change persis date 1".getBytes());
    }

    public static void main(String[] args) throws Exception {
        CuratorWatcherExample curatorWatcherExample = new CuratorWatcherExample();
//        String node = "/watcher";
//        //普通监听
//        curatorWatcherExample.normalWatch(node);
//        curatorWatcherExample.operation(node);

        String node1 = "/persist watcher";
        //持久监听
        curatorWatcherExample.persistWatcher(node1);
        curatorWatcherExample.operation1(node1);
        System.in.read();
    }
}
