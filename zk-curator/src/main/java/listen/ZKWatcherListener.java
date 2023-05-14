package listen;

import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;

/**
 * ClassName: ZKWatcherListener
 * Description:
 *
 * @Author: yubj45081
 * Date: 2023/4/16 15:27
 */
public class ZKWatcherListener implements CuratorCacheListener {
    @Override
    public void event(Type type, ChildData childData, ChildData childData1) {
        System.out.println("事件类型：" + type + ":oldData:" + childData + ":data" + childData1);
    }
}
