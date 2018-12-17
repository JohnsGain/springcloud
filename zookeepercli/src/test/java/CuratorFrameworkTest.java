import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author zhangjuwa
 * @description 使用封装zookeeper的Curator组件之Framework访问zookeeper服务
 * @date 2018/9/18
 * @since jdk1.8
 */
public class CuratorFrameworkTest extends BaseTest{

    protected static Logger mylogger = LoggerFactory.getLogger(CuratorFrameworkTest.class);

    /**
     * curator创建节点
     */
    @Test
    public void create() throws Exception {
        //若创建节点的父节点不存在则先创建父节点再创建子节点
        String path = client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/root/john1", "john1".getBytes());
        mylogger.info("新增节点路径: " + path);
    }

    /**
     * 执行创建，并在创建完成之后执行回调,这个
     * @Note 这个测试没有成功，可能是客户端版本和服务器zookeeper服务版本不统一
     */
    @Test
    public void createAndCallBack() throws Exception {
        String path = client.create().creatingParentsIfNeeded().withProtection().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .inBackground((client, event) -> {
            mylogger.info("完成回调,客户端是{}，事件是{}", client, event);
        }).forPath("/root/john2", "callback".getBytes());
        mylogger.info("新增节点路径: " + path);
    }

    /**
     * 获取子节点列表
     */
    @Test
    public void list() throws Exception {
        List<String> strings = client.getChildren().usingWatcher((CuratorWatcher) item -> {
            mylogger.info("事件驱动==="  + item);
        }).forPath("/curator");
        strings.forEach(item -> {
            mylogger.info("子节点 : " + item);
        });
    }

    /**
     * 判断是否存在,Stat就是对znode所有属性的一个映射，stat=null表示节点不存在
     */
    @Test
    public void exists() throws Exception {
        String path = "/curator";
        Stat stat = client.checkExists().forPath(path);
        if (stat != null) {
            mylogger.info("节点存在 : " + stat);
        } else {
            mylogger.info("节点{}不存在", path);
        }
    }

    /**
     * 对节点数据的更改
     */
    @Test
    public void update() throws Exception {
        Stat stat = client.setData().forPath("/curator/barrier2", "updateData3".getBytes());
        mylogger.info("更改之后的节点属性状态：" + stat);
    }

    /**
     * 查询节点数据信息
     */
    @Test
    public void select() throws Exception {
        //获取节点数据
        byte[] bytes = client.getData().forPath("/root");
        mylogger.info("节点数据：" + new String(bytes));

    }

    /**
     * 获取节点数据的同时返回节点属性
     */
    @Test
    public void selectAndGetStat() throws Exception {
        Stat stat = new Stat();
        byte[] bytes = client.getData().storingStatIn(stat).forPath("/root/john1");
        mylogger.info("节点存储的数据：" + new String(bytes));
        mylogger.info("节点属性状态：" + stat);
    }

    /**
     * 删除节点
     */
    @Test
    public void delete() throws Exception {
        Void aVoid = client.delete()
        //保障机制，若未删除成功，只要会话有效会在后台一直尝试删除
                .guaranteed()
                ///若当前节点包含子节点就一并删除
                .deletingChildrenIfNeeded()
                //指定版本号，若为-1则忽略版本号
                .withVersion(-1)
                .forPath("/curator");
    }

    /**
     * Curator 还支持事务，一组crud操作要么都完成，要么都不完成：
     * 老版本的方式
     *  curator.inTransaction().
     *  *             create().forPath("/path-one", path-one-data).
     *  *             and().create().forPath("/path-two", path-two-data).
     *  *             and().commit();
     */
    @Test
    public void transaction4OldVersion() throws Exception {
        //curator.inTransaction().
        //        *  *             create().forPath("/path-one", path-one-data).
        //        *  *             and().create().forPath("/path-two", path-two-data).
        //        *  *             and().commit();
    }

    /**
     * 新版本事务支持操作方式, curator版本3.xx以上
     */
    @Test
    public void trans4NewVersion() {
        //启动事务
        //CuratorMultiTransaction transaction = client.transaction();
        //
        ////定义需要原子执行的任务
        //TransactionOp transactionOp = client.transactionOp();
        //CuratorOp set = transactionOp.setData().forPath("");
        //CuratorOp check = transactionOp.check().withVersion(2).forPath("");
        //
        ////把任务放入集合
        //List<CuratorOp> curatorOps = Lists.newArrayList(set, check);
        //CuratorMultiTransactionImpl
    }

    @Override
    protected String getPath() {
        return null;
    }
}
