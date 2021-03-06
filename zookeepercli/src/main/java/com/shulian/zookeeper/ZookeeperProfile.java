package com.shulian.zookeeper;

/**
 * @author ""
 * @description zookeeper简介
 * @see {https://www.cnblogs.com/ggjucheng/p/3370359.html}
 * @date 2018/9/10
 * @since jdk1.8
 */
public class ZookeeperProfile {

    /*
        zookeeper是一个针对分布式应用程序的协调服务，用于对应用做集群状态监控管理，应用程序配置集中配置管理，实时更新通知，让
    程序可以即时读取最新配置数据。他维护个服务节点的状态，保持同一应用的各服务节点数据最终一致性。
     zookeeper做出的承诺：
     1.最终一致性:client不管连接到那个服务节点，得到的都是同样的视图结果。
     2.可靠性:具有简单，健壮，良好的性能，如果消息被一台服务器接受，那也会被其他服务器接受。
     3.实时性:zookeeper可以保证在一个时间范围内客户端可以获取节点的更新数据，或者服务器失效信息，但由于网络延迟等原因，
        zookeeper不能保证多个客户端可以同时刻获取同样的更新信息，若要让客户端肯定获最新的数据，则要在读取数据之前执行sync()操作。
     4.等待无关性(wait-free):慢的client或者失效的client不能干预快速的client请求，使得每个客户端都能有效的等待
     5.原子性:数据更新要么成功，要么失败，没有中间状态
     6.顺序性:zookeeper顺序包括全局有序和偏序。全局有序是指，如果在一个服务节点上消息1在消息2之前发布，那么在其他节点消息1也会在消息2
        之前发布;偏序值如果消息2在也是同一个发布者发布的消息1之后发布，那么可以保证在其他服务节点上消息2也是在消息1之后被发布

        =============Zookeeper工作原理========================================

        Zookeeper 的核心是原子广播，这个机制保证了各个Server之间的同步。实现这个机制的协议叫做Zab协议。Zab协议有两种模式，
        它们分别是恢复模式（选主）和广播模式（同步）。当服务启动或者在领导者崩溃后，Zab就进入了恢复模式，当领导者被选举出来，
        且大多数Server完成了和 leader的状态同步以后，恢复模式就结束了。状态同步保证了leader和Server具有相同的系统状态。

        为了保证事务的顺序一致性，zookeeper采用了递增的事务id号（zxid）来标识事务。所有的提议（proposal）都在被提出的时候加上了zxid。
        实现中zxid是一个64位的数字，它高32位是epoch用来标识leader关系是否改变，每次一个leader被选出来，它都会有一个新的epoch，标识当
        前属于那个leader的统治时期。低32位用于递增计数。

        =========zookeeper下服务节点的工作状态
        Looking 当前服务的leader节点不知道是谁，正在寻找
        Leading 当前节点即为选举出来的leader节点
        follolwing 当前服务的leader节点已经选出，其他节点就成为follower节点，负责保持与Leader节点的数据同步

        zookeeper提供哪些功能:
        1。命名服务
        2. 配置管理
        3. 集群管理
        4. 分布式锁
          某集群服务需要在共享某个资源的时候进行同步处理，分布式环境同步需要分布式锁，在通过zookeeper做集群管理的架构中，
          可以通过zookeeper获得分布式锁。具体是：同一应用下的每一个服务节点在某一约定的父目录下创建EPHEMERAL_SEQUENTIAL类型
          的子目录，然后每个节点调用父目录的getChildren(path, false)获取父目录下的子目录节点，并获取顺序编号最小的目录节点，
          检测最小顺序编号节点是不是当前服务节点创建的，如果是，当前服务就获得了锁执行资源操作。如果不是自己创建的，则监听改父
          目录下的节点变化情况，有节点变化会收到zookeeper通知，这里是事件驱动的，收到通知之后就执行exists(String path, boolean watch)
          判断比自己创建的节点编号更小的节点是否存在，存在就继续监听，不存在则判断当前最小的节点是否是自己创建的，是，就获得锁。
          怎么释放锁？ 在获得锁的服务执行完后，需要释放锁，让其他服务节点获得资源，只需要将当前服务节点的目录节点删除就可以了。
          1. 当前服务获得的节点是最小节点，如果他获得锁之后，还没有删除节点就宕机了，是不是会死锁？如果是持久节点，那么节点
          会一直存在造成死锁；如果是临时节点，zk服务在一段时间没有收到该客户端心跳，就会判断该会话失效从而删除节点
          2. getChildren(path, false)：这个操作是原子的，保证在获得子节点的同时注册成功监听事件，保证在获得子节点，然后在注册监听事件
          之前，子节点没有变更
          3. 对于这个算法有个极大的优化点：假如当前有1000个节点在等待锁，如果获得锁的客户端释放锁时，这1000个客户端都会被唤醒
          ，这种情况称为“羊群效应”；在这种羊群效应中，zookeeper需要通知1000个客户端，这会阻塞其他的操作，最好的情况应该只唤醒
          新的最小节点对应的客户端。应该怎么做呢？在设置事件监听时，每个客户端应该对刚好在它之前的子节点设置事件监听，例如子节
          点列表为/lock/lock-0000000000、/lock/lock-0000000001、/lock/lock-0000000002，序号为1的客户端监听序号为0的子节点
          删除消息，序号为2的监听序号为1的子节点删除消息。
        5.队列管理
        同步队列模式:当队列所有成员都到齐之后，队列才可用，否则一直等待所有成员到达，这种是同步队列。
            核心思路：创建一个父级目录/sychronizing，所有成员监控/sychronizing/start目录是否存在，存在则说明对列同步结束，未存在，每个对列成员进来之后创建
        EPHEMERAL_SEQUENTIAL子目录/sychronizing/member_i，然后每个成员都获取子目录节点/sychronizing/member_总个数i，
        看是否达到了对列要求的总个数，如果小于总个数则等待/sychronizing/start出现，达到了就创建/sychronizing/start目录

        发布订阅(pub-sub):队列按照 FIFO 方式进行入队和出队操作，例如实现生产者和消费者模型。
            核心思路:生产者每个队列元素都在约定的股父级目录下面创建EPHEMERAL_SEQUENTIAL子目录，消费者从队列中调用
        getChildren()获取子目录个数，然后取其中入队编号最小的元素进行消费，这样就保证了FIFO

=============================== 总结  ==============================
         Zookeeper 作为 Hadoop 项目中的一个子项目，是 Hadoop 集群管理的一个必不可少的模块，它主要用来控制集群中的数据，
     Hadoop 集群中的 NameNode，还有 Hbase 中 Master Election、Server 之间状态同步等。
        本文介绍的 Zookeeper 的基本知识，以及介绍了几个典型的应用场景。这些都是 Zookeeper 的基本功能，最重要的是 Zoopkeeper
    提供了一套很好的分布式集群管理的机制，就是它这种基于层次型的目录树的数据结构，并对树中的节点进行有效管理，从而可以设计
    出多种多样的分布式的数据管理模型，而不仅仅局限于上面提到的几个常用应用场景。


    ===================环境变量配置,在/etc目录下找到profile文件，配置zookeeper服务环境变量方式如下=================
export ZOOKEEPER_HOME=/usr/local/zk
export PATH=.:$ZOOKEEPER_HOME/bin

启动ZooKeeper的Server：zkServer.sh start；关闭ZooKeeper的Server：zkServer.sh stop

查看zookeeper是否启动 ps -aux | grep 'zookeeper'

客户端连接zookeeper服务时，身份的认证有4种方式：
world：默认方式，相当于全世界都能访问
auth：代表已经认证通过的用户(cli中可以通过addauth digest user:pwd 来添加当前上下文中的授权用户)
digest：即用户名:密码这种方式认证，这也是业务系统中最常用的
ip：使用Ip地址认证

//zk的作用之一是分布式环境中保持高吞吐量低延迟，所以节点数据是保存在内存中，且每个节点最多可以存储1M的数据

    //create,delete,set会触发事件，getData,getChildren,exist,会设置监听对应事件
   getData :监听改节点更新，删除事件
    getChildren:监听改节点更新删除事件，以及其子节点新增，删除事件
    exist:监听改节点创建，更新，删除事件
    当前zookeeper有如下四种事件({@link EventType})：1）节点创建；2）节点删除；3）节点数据修改；4）子节点变更。


    */
}
