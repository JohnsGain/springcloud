package com.john.shardingjdbc.algorithm;

import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

/**
 * 如果我们希望订单表t_order用 user_id 做分片健进行分库分表，但是 t_order 表中却没有 user_id
 * 这个字段，这时可以通过 Hint API 在外部手动指定分片健或分片库。
 * @author zhangjuwa  <a href="mailto:zhangjuwa@gmail.com">zhangjuwa</a>
 * @date 2023/8/17 22:18
 * @since jdk1.8
 */
public class MyTableHintShardingAlgorithm implements HintShardingAlgorithm<String> {

    @Override
    public Collection<String> doSharding(Collection<String> tableNames, HintShardingValue<String> hintShardingValue) {
        Collection<String> result = new ArrayList<>();
        for (String tableName : tableNames) {
            for (String shardingValue : hintShardingValue.getValues()) {
                if (tableName.endsWith(String.valueOf(Long.parseLong(shardingValue) % tableNames.size()))) {
                    result.add(tableName);
                }
            }
        }
        return result;
    }

    @Override
    public Properties getProps() {
        return null;
    }

    @Override
    public void init(Properties props) {

    }
}
