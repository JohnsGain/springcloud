import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhangjuwa
 * @description 使用原生zookeeper客户端访问zookeepr服务
 * @date 2018/9/14
 * @since jdk1.8
 */
public class ZookeeperTest {

    // 会话超时时间，设置为与系统默认时间一致
    private static final int SESSION_TIMEOUT = 30 * 1000;

    private CountDownLatch latch = new CountDownLatch(1);

    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperTest.class);

    /**
     * 创建zookeeper实例
     */
    private ZooKeeper zooKeeper;

    public static void main(String[] args) throws KeeperException, InterruptedException {
        ZookeeperTest test = new ZookeeperTest();
        //和zookeeper服务建立连接之后，会首先创建一个zookeeper节点
        test.init();
        //查看是否存在/zoo2节点，watch是否监听path node的创建, 删除事件, 以及数据更新事件.
        Stat exists = test.zooKeeper.exists("/zoo2", true);
        if (exists != null) {
            LOGGER.info("存在zoo2节点,获取版本: " + exists.getVersion());
            LOGGER.info("===" + exists.getEphemeralOwner());
            LOGGER.info("获取子节点数量 ： " + exists.getNumChildren());
            LOGGER.info("===" + exists.getMzxid());
            byte[] data = test.zooKeeper.getData("/zoo2", test.watcher, null);
            LOGGER.info("获取zoo2节点数据 ： " + new String(data));
        } else {
            //没有节点,创建 ZooKeeper 节点 (znode ： zoo2, 数据： myData2 ，权限： OPEN_ACL_UNSAFE ，节点类型： Persistent
            String node = test.zooKeeper.create("/zoo2", "myfirstNode".getBytes(Charset.forName("UTF-8")),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
            LOGGER.info("节点名称 ：" + node);
        }

        /*
            前面一行我们添加了对/zoo2节点的监视，所以这里对/zoo2进行修改的时候，会触发Watch事件。
         如果version和真实的版本不同, 更新操作将失败，指定version为-1则忽略版本检查
         注：zookeeper原生watcher事件监听，只会触发两次，当创建客户端连接的完成之后会触发一次，
            第一次更新监听节点的时候触发，之后就不再触发
        */
        test.zooKeeper.setData("/zoo2", "myModify4zoo2".getBytes(Charset.forName("UTF-8")), -1);
        test.zooKeeper.setData("/zoo2", "myModify4zoo3".getBytes(Charset.forName("UTF-8")), -1);
        test.zooKeeper.setData("/zoo2", "myModify4zoo4".getBytes(Charset.forName("UTF-8")), -1);

        /*
            getData方法用于获取node关联的数据.,watch参数用于指定是否监听path node的删除事件, 以及数据更新事件，注意,
         不监听path node的创建事件, 因为如果path node不存在, 该方法将抛出KeeperException.NoNodeException异常.
         */
        byte[] data = test.zooKeeper.getData("/zoo2", test.watcher, null);
        LOGGER.info("获取zoo2节点更新之后数据 ： " + new String(data));

        //watch参数用于指定是否监听path node的子node的增加和删除事件, 以及path node本身的删除事件.
        List<String> children = test.zooKeeper.getChildren("/", true);
        LOGGER.info("具有子节点个数 ：" + children.size());
        children.forEach(item -> System.out.print(item + " "));

        //删除节点， 如果version和真实的版本不同, 删除操作将失败
        test.zooKeeper.delete("/zoo2", -1);

        Stat exists1 = test.zooKeeper.exists("/zoo2", true);
        if (exists1 == null) {
            LOGGER.info("have been deleted");
        }
        test.close();
    }

    /**
     * 创建watcher实例,/ 连接建立, 回调process接口时, 其event.getState()为KeeperState.SyncConnected
     */
    private Watcher watcher = item -> {
        LOGGER.info("WATCHER EVENT>>> " + item.getPath() + " ： " + item.getType() + " : " + item.getState());
        if (Watcher.Event.KeeperState.SyncConnected.equals(item.getState())) {
            LOGGER.info("事件驱动");
            // 放开闸门, wait在init方法上的线程将被唤醒
            latch.countDown();
        }
    };

    /**
     * 初始化zookeeper
     */
    private void init() {
        try {
            zooKeeper = new ZooKeeper("192.168.2.111:2181", SESSION_TIMEOUT, watcher);
            // 等待连接完成
            latch.await();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("zookeeper连接初始化失败");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void close() throws InterruptedException {
        zooKeeper.close();
    }

}
