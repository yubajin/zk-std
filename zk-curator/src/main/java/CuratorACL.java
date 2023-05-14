import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.util.ArrayList;

/**
 * ClassName: CuratorACL
 * Description:
 *
 * @Author: yubj45081
 * Date: 2023/4/16 13:52
 */
public class CuratorACL {

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
                .authorization("digest", "yubj:yubj".getBytes())
                .sessionTimeoutMs(15000)
                .build();
        curatorFramework.start();
        return curatorFramework;
    }

    /**
     * zk权限控制
     * scheme:id:perm
     * eg： ‘world,'anyone: cdrwa
     * scheme权限模式:
     * word; auth;  digest 用户密码方式;  ip
     * id 授权对象:
     * perm授予的权限:
     * 	c create; d delete; r read; w write; a admin允许act
     */
    public void aclOperation() throws Exception {
        Id digest = new Id("digest", DigestAuthenticationProvider.generateDigest("yubj:yubj"));
        ArrayList<ACL> acls = new ArrayList<>();
        acls.add(new ACL(ZooDefs.Perms.ALL, digest));
        curatorFramework.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(acls, false)
                .forPath("/curator-auth-test", "YBJ-Auth".getBytes());
        byte[] bytes = curatorFramework.getData().forPath("/curator-auth-test");
        String nodeValue = new String(bytes);
        System.out.println("数据查询结果为: " + nodeValue);
    }

    public static void main(String[] args) throws Exception {
        CuratorACL curatorACL = new CuratorACL();
        curatorACL.aclOperation();
    }
}
