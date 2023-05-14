import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * ClassName: CuratorConnection
 * Description:
 *
 * @Author: yubj45081
 * Date: 2023/4/15 23:34
 */
public class CuratorConnection {
    public static void main(String[] args) throws Exception {
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

        byte[] bytes = curatorFramework.getData().forPath("/config");
        String result = new String(bytes);
        System.out.println(result);

    }
}
