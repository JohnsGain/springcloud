import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * curator组件之Receipts
 * Recipes 模块主要有 Elections(选举)，Locks(锁)，Barriers(关卡)，Atomic(原子量)，Caches，Queues 等。
 * 选举主要依赖于 LeaderSelector 和 LeaderLatch 两个类。前者是所有存活的客户端不间断的轮流做 Leader。后者是一旦选举出 Leader，
 * 除非有客户端挂掉重新触发选举，否则不会交出领导权。这两者在实现上是可以切换的。
 * @author ""
 * @description
 * @date 2018/9/18
 * @since jdk1.8
 */
public class CuratorReceiptsTest {

    static Logger logger = LoggerFactory.getLogger(CuratorFrameworkTest.class);


    public static class LeaderSelectorClient extends LeaderSelectorListenerAdapter implements Closeable {

        private final String name;

        private final LeaderSelector leaderSelector;

        private String path = "/leaderselector";

        public LeaderSelectorClient(String name, CuratorFramework client) {
            this.name = name;
            this.leaderSelector = new LeaderSelector(client, path, this);
            this.leaderSelector.autoRequeue();
        }

        /**
         * client成为leader后，会调用此方法
         */
        @Override
        public void takeLeadership(CuratorFramework client) throws Exception {
            int waitSeconds = (int) (5 * Math.random()) + 1;
            logger.info(name + "是当前的leader");
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(waitSeconds));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                logger.info(name + "让出领导权\n");
            }
        }

        public void start() {
            this.leaderSelector.start();
        }

        @Override
        public void close() throws IOException {
            this.leaderSelector.close();
        }
    }

}
