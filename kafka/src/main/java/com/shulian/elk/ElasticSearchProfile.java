package com.shulian.elk;

/**
 * 了解一下 wireshark  2018-10-25
 * @see {https://www.elastic.co/guide/en/elasticsearch/reference/current/getting-started.html}
 * es概念了解
 *  使用Java语言开发，基于jdk1.8，基于Lucene全文搜索引擎为内核的搜索服务器，在Lucene的基础上封装了
 *  restful接口，方便开发者不需要了解Lucene复杂的处理逻辑，就可以快速实现高效的搜索功能
 *
 * :开源的全文搜索、分析引擎，允许你以近实时的方式快速存储，搜索，分析大量的数据。通常作为基础技术支持
 * 商业应用： GitHub、StackOverflow、Wiki
 *
 *
 * 应用系统复杂的搜索特性和需求。一些试用的场景：
 * 1.在线商场搜索
 * 2.日志存储分析: 通过logstash收集解析好的数据放入es后，就可以运行es的搜索，分析功能，从这些数据中寻找有价值的信息，
 *      市场趋势，统计资料，反常情况等。
 * 3.价格预警: 可以针对那些对价格敏感的客户，客户可能对某款商品感兴趣，并且希望在该商品的价格下降到某价位之后通知他，
 *      这个场景就可以通过es搜集所有卖家这款商品价格数据，并且使用ES反向搜索来匹配价格变动，一旦发现有低于某价位的卖家，
 *      就发送通知给客户
 * 4.商业数据审计分析，并进行图形化展示: 这个情况可以使用ES存储数据之后，再用Kibana接入ES，对存储的数据进行可视化展示；使用
 *      ES的集成功能，可以从数据中挖掘更有意义更复杂的商业信息并展示
 *
 * 几个基本特性
 * 1.Near Realtime 近实时性
 *      es是一个近实时的搜索平台，把一个文档导入ES到可以搜索出文档内容，只有轻微的延时
 * 2.Cluster
 *      集群是多台服务器节点组成的， ES集群所有的节点保存你的全部数据并且提供联合索引和搜索功能的节点集合。
 *  每一个集群有一个唯一标识，集群里面的每个节点都要表明所属节点的标识，只有使用的相同集群标识的节点才会进入
 *  同一个集群。确保没有在不同的环境使用了相同的集群标识，否则可能把节点加入错误的集群。在日志收集用例中，你
 *  可以分别使用logging-dev,logging-test,logging-prod给开发，测试，生产环境做集群标识
 *
 * 3.ES节点
 *      每一个ES服务就是ES集群中的一个节点，每个ES节点也和ES集群一样有一个唯一标识，默认情况下是UUID,
 *  也可以自定义节点标识，ES节点通过设置一个集群标识来加入一个ES集群，如果没有设置集群标识，默认情况会加入一个
 *  集群标识为elasticsearch的集群。这个节点标识，对于集群管理节点，识别哪台服务器是对应哪个集群下的哪个节点是很重要的
 *
 * 4.Index 索引
 *      一个索引就是包含某些相似特性的文档的集合，例如可以有一个用户数据的索引，商品目录的索引，及其他一些有规则数据
 *   的索引。一个索引被一个名称(必须是小写)唯一标识，集群中可以根据需求设置多个索引
 *
 * 5.Document 文档
 *      文档是可被索引的数据的基础单元。例如可以为一个用户单独创建一个文档，为一个产品创建一个文档，及其他有规则数据
 *   的独立单元创建文档.这个文档用JSON格式，在一个索引中，可以根据需求存储任意多符合同一类数据特性的文档。
 *
 * 6.Shards & Replicas 分片和复制
 *      在一个索引里存储的数据，数据量增多的情况下，可能会达到单节点索引存储数据的上限，例如一个索引存储了上千万条文档。
 *   这个时候索引所在节点的服务器磁盘可能已满，就算没有满，那么多数据存放在同一个节点上，也会导致针对这个索引的
 *   搜索功能变得缓慢。为了解决这个问题，ES允许对索引进行分片的能力，把索引细分成多个分片，每个分片可以存放在不同
 *   的ES节点，且每个分片都提供完整的索引功能。在创建索引的时候就可以指定需要为该索引指定的分片数量。
 *   分片有两个原因很重要：
 *   6.1 提供了应用水平扩展的可能
 *   6.2 每个分片都提供完整的索引功能，所以存储在不同节点的多个分片就可以并行执行，提高吞吐量
 *
 *   分片是如何分配的? 各个分配的文档又是怎么聚合起来以应对搜索需求的？
 *      它的实现技术由Elasticsearch完全管理，并且对用户是透明的。
 *
 *   分片副本
 *      在一个网络环境下或者是云环境下，故障可能会随时发生，
 *    有一个故障恢复机制是非常有用并且是高度推荐的，以防一个分片或节点不明原因下线，或者因为一些原因去除没有了。
 *    为了达到这个目的，Elasticsearch允许你制作分片的一个或多个拷贝放入一个叫做复制分片或短暂复制品中。
 *    分片复制对于以下两个主要原因很重要：
 *  6.3 高可用。防止某分片宕机和停止，他的分片副本这个时候可以继续提供服务，这里要注意，不要将主分片和他的分片
 *      副本放在同一台机器上。
 *  6.4 高并发。允许索引提供超出自身吞吐量的搜索服务，搜索行为可以在分片及其副本中并发进行
 *
 *  总结：
 *      每个索引可以有多个分片，每个分片可以有0到多个副本，最好每个主分片和他的副本不要在一个服务节点上达到高可用。
 *      主分片和分片副本数量都可以在索引创建的时候指定，索引创建之后也可以动态修改分片副本数，但主分片不能更改。
 *      默认情况，ES为每个索引分片5个主分片和一分拷贝。
 *      每个分片是一个Lucene索引，一个Lucene索引可以包含的文档数量最大值，截止Lucene-5843,这个阈值是
 *      Integer.MAX_VALUE-128个文档,你可以使用_cat/shards API监控分片大小。
 *
 *
 *      查看集群健康状态xxx:9200/_cat/health?v
 * 返回结果1475247709 17:01:49  elasticsearch green           1         1      0   0    0    0        0             0                  -                100.0%
 * 对应属性名epoch      timestamp cluster    status       node.total node.data shards pri relo init unassign pending_tasks max_task_wait_time active_shards_percent
 *
 * 集群状态有三种green,yellow,red
 * Green - everything is good (cluster is fully functional)
 * Yellow - all data is available but some replicas are not yet allocated (cluster is fully functional)
 * Red - some data is not available for whatever reason (cluster is partially functional)
 *
 * 7.  ES安装：
 * @see {https://www.elastic.co/guide/cn/elasticsearch/guide/current/intro.html}
 *
 *   启动完成之后，可以在日志中看见启动的ES服务节点名称，如果没有指定会生成一个UUID,也可以在ES服务启动的时候指定
 *   启动ES服务: bin/elasticsearch
 *   启动ES并设置集群唯一标识和节点唯一标识 bin/elasticsearch -E cluster.name=my_cluster_name -E node.name=my_node_name
 *
 *  8. elasticsearch-head 插件:对ES集群消息，节点信息进行可视化展示，能够执行查询语句，创建索引，文档等
 *
 *   启动: 先在elasticsearch-head-master路径下执行: grunt server，查看grunt版本  grunt --version
 *   再在elasticsearch主目录下执行 bin/elasticsearch
 *   然后通过浏览器192.168.2.111:9100就可以获取可视化信息
 *
 *   启动es的时候同时自定义节点名和集群名
 *   ./elasticsearch -Ecluster.name=my_cluster_name -Enode.name=my_node_name
 *
 *  8.1 es提供的rest API提供提下功能
 *   1.Check your cluster, node, and index health, status, and statistics
 *   2.Administer your cluster, node, and index data and metadata
 *   3.Perform CRUD (Create, Read, Update, and Delete) and search operations against your indexes
 *   4.Execute advanced search operations such as paging, sorting, filtering, scripting, aggregations, and many others

 * es没有要求在把一个文档放入某个索引之前，必须先创建这个索引，他会检查索引是否存在，不存在会自行创建一个索引然后把文档放进去
 *
 * 8.2es对外提供的restAPI学习
     * 创建一个customer索引: PUT /customer?v
     * 查看所有索引: GET /_cat/indices?v
 *     删除customer索引 : DELETE /customer
     * 查看叫customer的索引 GET /_cat/indices/customer?v
     * 在索引customer下创建一个ID为2的文档: PUT /customer/_doc/2   在请求以里面带上数据{"name":"johnwall"}
 *     查看索引customer下ID为2的文档 GET /customer/_doc/2
 *     查看索引customer下ID为2的文档 DELETE /customer/_doc/2
     * 替换索引customer下ID为2的文档: PUT /customer/_doc/2   在请求以里面带上数据{"name":"tom"}
     * 更新索引customer下ID为2的文档: POST /customer/_doc/2/_update  在请求体里面带上数据{"doc":{"name":"jackson", age:22}}
 * 8.2.1 替换(PUT customer/_doc/2)和更新(POST customer/_doc/2/_update)文档的区别:
 *  1.替换将整个文档内容换成新的数据
 *  2.更新只会更新请求里面携带的属性对应的参数，没有携带的保持原值，原文档没有的属性就新增进去
 *  3.替换请求的请求体里面直接包装要替换的参数，更新请求体里面要在更新的数据外面包装一层doc属性或者script属性
 *
 *   删除customer索引下ID为2的文档
 *
     * 查看索引customer下ID为2 的文档 GET /customer/_doc/2
     * 删除某个索引: DELETE /customer
 *
 * 9.BULK操作:{https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-bulk.html}
 *      es提供了批量执行的接口，可以通过一个rest请求,执行多个index/delete/upate/get操作的组合，加快业务处理速度，
 * 每个执行操作的结构是，批处理中的某个命令执行失败，整个请求不会失败，他还可以正常执行完其他请求，
 * 在返回结果中会显示每个命令的执行成功与否，结果展示的顺序和提交bulk的时候命令顺序一致。es没有明确规定一个批处理操作
 * 里面合理的最多的命令操作数，这和具体的请求数据量大小，服务端业务处理复杂度有关，由具体情况而定。
 * {元数据指令}\n
 * {对应操作的数据}
 *   9.1在索引customer里面同时添加两个文档,ID分别是5,6
 *   POST /customer/_doc/_bulk
 *   请求体内容:
 *   {"index":{"_id":5}}\n
 *   {"name":"messi","number":10,"age":30,"team":"barca"}
 *   {"index":{"_id":6}}\n
 *   {"name":"neymar","number":11, "age":26, "team":"paries"}
 *
 *   9.2在索引customer里面更新id为5的文档，删除ID为6的文档，元数据指令update支持的文档类型包括doc,script,upsert,doc_as_upsert,lang,params,
 *   每个命令操作都有一个版本号，
 *   POST /customer/_doc/_bulk
 *   请求体：
 *   {"update":{"_id":5}}\n
 *   {"name":"messi","age":31,"team":"barca"}
 *   {"delete":{"_id":6}}\n
 *
 *  10.THe Search API，可以通过在请求Uri后面拼接路径参数进行查询，可以把请求参数放在请求体传入进行查询。通常会使用请求体传递查询参数。
 *  即是参数保密性更高，也可以通过请求体写更复杂的json格式的查询参数表达式
 *   10.1查询bank索引下所有文档，默认返回10条,q=*,表示bank任何文档都可以匹配，sort=xxx表示查询结果的排序字段，这里asc是升序
 *   GET /bank/_doc/_search?q=*&sort=account_number:asc
 *   返回的响应结果参数解释
 *   took – time in milliseconds for Elasticsearch to execute the search
 * timed_out – tells us if the search timed out or not
 * _shards – tells us how many shards were searched, as well as a count of the successful/failed searched shards
 * hits – search results
 * hits.total – total number of documents matching our search criteria
 * hits.hits – actual array of search results (defaults to first 10 documents)
 * hits.sort - sort key for results (missing if sorting by score)
 * hits._score and max_score - ignore these fields for now
 *
 * 10.2 使用请求体提交参数，es提供了用于查询的QUERY DSL,es默认使用GET方法进行查询，但是换成POST也是可以的
 * @see {https://www.elastic.co/guide/en/elasticsearch/reference/current/_introducing_the_query_language.html}
 *   10.2.1查询bank索引下所有文档,这里默认会返回10条文档
 *   GET /bank/_search
 *   请求体:{"query":{"match_all":{}}}
 *   10.2.2查询bank索引下所有文档并返回1条文档,size参数如果没有显式设置，默认是10,所有第一个查询最多会返回10条文档
 *   GET /bank/_search
 *   {
 *   "query":{"match_all":{}},
 *   "size":1
 *   }
 *   10.2.3查询bank索引下符合匹配条件的文档，从结果中返回下标为10的文档开始的10条文档,es文档下标从0开始，如果from参数
 *   没有显式设置，from默认从0开始
 *   {
 *       "query":{"match_all":{}},
 *       "from":10,
 *       "size":10
 *   }
 *   10.2.4this example does a match_all query and sorts the search results by account_balance in descending order
 *   and return the top 10 documents
 *   GET/POST /bank/_search
 *   {
 *       "query":{"match_all":{}},
 *       "sort":{"account_balance":{"order":"desc"}}
 *   }
 *   10.2.5we have ability to request only a few fields from within source to be returned,查询文档的返回结果，默认除了文档数据
 *   之外，还有其他一些属性，文档数据是在一个_source属性里面，我们可以设置只返回文档里面的某些属性,但是整体结构不变，只是文档里面
 *   返回的属性只有设定的那些，整体默认返回数据结构入{defaultSearchResult.json}
 *  @see { defaultSearchResult.json}
 *   返回结果中，bank索引下符合匹配条件文档里面的属性只返回account_number and balance(inside of _source)，整体返回数据结构不变
 *   GET/POST /bank/_source
 *   {
 *       "query":{"match_all":{}},
 *       "_source":["account_number", "balance"]
 *   }
 *
 *
 *
 *
 * @Author zhangjuwa
 * @Description:
 * @Date 2018/10/8
 * @Since jdk1.8
 */
public class ElasticSearchProfile {


}
