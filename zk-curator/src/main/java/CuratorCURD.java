import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * ClassName: CuratorCURD
 * Description:
 *
 * @Author: yubj45081
 * Date: 2023/4/15 23:45
 */
public class CuratorCURD {

    static CuratorFramework curatorFramework = getConnection();

    static CuratorFramework getConnection() {
        curatorFramework = CuratorFrameworkFactory
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

    public String  createNode(String node) throws Exception {
        String value = "Hello zk !!!";
        String newNode = curatorFramework.create()
                .creatingParentContainersIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(node, value.getBytes());
        return newNode;
    }

    public void setValue(String node, String value) throws Exception {
        Stat stat = new Stat();
        curatorFramework.getData().storingStatIn(stat).forPath(node);
        curatorFramework.setData()
                .withVersion(stat.getVersion())
                .forPath(node, value.getBytes());
    }

    public String getValue(String node) throws Exception {
        byte[] bytes = curatorFramework.getData().forPath(node);
        System.out.println("节点" + node + "的值为: " + new String(bytes));
        return new String(bytes);
    }

    public void delValue(String node) throws Exception {
        curatorFramework.delete().forPath(node);
        Stat existStat = curatorFramework.checkExists().forPath(node);
        if (existStat == null) {
            System.out.println(node + " 已节点删除");
        }
    }

    public static void main(String[] args) throws Exception {
        CuratorCURD curatorCURD = new CuratorCURD();
        CuratorCURD.getConnection();
//        String node = "/ybj_node";
//        String newValue = "ybj_new_value";
//        curatorCURD.createNode(node);
//        curatorCURD.getValue(node);
//        curatorCURD.setValue(node, newValue);
//        curatorCURD.getValue(node);
//        curatorCURD.delValue(node);
        String node1 = "/config";
        String value1 = "{\"name\":\"yuerer\", \"job\":\"tacher\", \"sex\":\"男\"}";
        curatorCURD.createNode(node1);
        curatorCURD.setValue(node1, value1);
        curatorCURD.getValue(node1);
    }
}
