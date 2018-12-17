import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.curator.framework.listen.Listenable;
import org.apache.curator.framework.listen.ListenerContainer;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author zhangjuwa
 * @description curator实现事件监听
 * @date 2018/9/19
 * @see {https://blog.csdn.net/en_joker/article/details/78788059}
 * @since jdk1.8
 */
public class NodeCacheTest extends BaseTest {

    static ExecutorService executorService = new ScheduledThreadPoolExecutor(10,
            new BasicThreadFactory.Builder().priority(5).namingPattern("zkEvent-pool-%d").daemon(true).build());

    /**
     * 使用NodeCache实现监听节点更改事件，多次更改节点、删除节点，都会触发事件
     *
     * @throws Exception
     */
    @Test
    public void nodeCache() throws Exception {
        NodeCache nodeCache = new NodeCache(client, getPath(), false);
        nodeCache.start(true);
        //Listenable<ConnectionStateListener> stateListenerListenable = client.getConnectionStateListenable();
        ListenerContainer<NodeCacheListener> container = nodeCache.getListenable();
        container.addListener(() -> {
            if (nodeCache.getCurrentData() == null) {
                logger.info("节点已删除");
            } else {
                logger.info("Node data update, new data，new Data is {}", new String(nodeCache.getCurrentData().getData()));
            }
        });

        Stat stat1 = client.checkExists().forPath(getPath());
        String path = null;
        if (stat1 == null) {
            path = client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(getPath(), "Data0".getBytes());
        }

        Thread.sleep(1000);
        Stat stat = client.setData().forPath(path, "Data1".getBytes());
        logger.info("更新之后节点状态:{}", stat);
        Thread.sleep(1000);
        Stat stat2 = client.setData().forPath(path, "Data2".getBytes());
        logger.info("更新之后节点状态:{}", stat2);
        Thread.sleep(1000);
        Void aVoid = client.delete().deletingChildrenIfNeeded().withVersion(stat2.getVersion()).forPath(path);

    }

    /**
     * 监听子节点事件,和其他zookeeper产品一样，对子节点进行监听的cache，不能对二级子节点进行监听,
     * 而且当前节点创建删除更新不会监听
     * executorService:用于指定线程池处理事件通知
     */
    @Test
    public void subNodeEvent() throws Exception {
        PathChildrenCache childrenCache = new PathChildrenCache(client, getPath(), true, false, executorService);
        childrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        ListenerContainer<PathChildrenCacheListener> container = childrenCache.getListenable();
        container.addListener((client, event) -> {
            logger.info("客户端信息:{}", client);
            switch (event.getType()) {
                case CHILD_ADDED:
                    logger.info("添加子节点:{}", event.getData());
                    break;
                case INITIALIZED:
                    logger.info("节点初始化:{}", event.getData());
                    break;
                case CHILD_REMOVED:
                    logger.info("删除子节点:{}", event.getData());
                    break;
                case CHILD_UPDATED:
                    logger.info("更新子节点:{}", event.getData());
                    break;
                default:
                    logger.info("其他事件:{}", event.getData());
                    break;
            }
        });
        Stat stat = client.checkExists().creatingParentContainersIfNeeded().forPath(getPath());
        String path = null;
        if (stat == null) {
            path = client.create().withMode(CreateMode.PERSISTENT).forPath(getPath());
        }
        Thread.sleep(1000);
        String path1 = client.create().withMode(CreateMode.PERSISTENT).forPath(getPath() + "/event1");
        Thread.sleep(1000);
        Stat stat1 = client.setData().forPath(getPath() + "/event1");
        Thread.sleep(1000);
        Void aVoid = client.delete().deletingChildrenIfNeeded().withVersion(stat1.getVersion()).forPath(getPath() + "/event1");
        Thread.sleep(1000);
        Void aVoid1 = client.delete().deletingChildrenIfNeeded().forPath(getPath());

    }

    /**
     * 多级监听，可以监听本节点， 多级子节点，可以通过参数setMaxDepth(3)
     * 设置最多监听几层节点
     */
    @Test
    public void treeCache() throws Exception {
        TreeCache treeCache = TreeCache.newBuilder(client, getPath()).setMaxDepth(3).setDataIsCompressed(false)
                .setExecutor(executorService).setCacheData(true).setCreateParentNodes(true).build();
        treeCache.start();
        Listenable<TreeCacheListener> listenable = treeCache.getListenable();
        listenable.addListener((client, event) -> {
           switch (event.getType()) {
               case NODE_ADDED:
                   logger.info("添加子节点:{}", event.getData());
                   break;
               case INITIALIZED:
                   logger.info("节点初始化:{}", event.getData());
                   break;
               case NODE_REMOVED:
                   logger.info("删除子节点:{}", event.getData());
                   break;
               case NODE_UPDATED:
                   logger.info("更新子节点:{}", event.getData());
                   break;
               default:
                   logger.info("其他事件:{}", event.getData());
                   break;
           }
        });
        String path = client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(getPath() + "/tree/branch1");
        Thread.sleep(1000);
        Stat stat = client.setData().forPath(getPath() + "/tree", "updateData".getBytes());
        Thread.sleep(1000);
        Void aVoid = client.delete().deletingChildrenIfNeeded().forPath(getPath());
        Thread.sleep(1000);
    }


    @Override
    protected String getPath() {
        return "/curator/event";
    }
}
