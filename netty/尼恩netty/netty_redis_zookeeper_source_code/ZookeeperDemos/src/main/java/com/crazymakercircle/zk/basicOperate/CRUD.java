package com.crazymakercircle.zk.basicOperate;

import com.crazymakercircle.util.Logger;
import com.crazymakercircle.zk.ClientFactory;
import com.crazymakercircle.zk.ZKclient;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * create by 尼恩 @ 疯狂创客圈
 **/
@Slf4j
public class CRUD {
    //本机
//    private static final String ZK_ADDRESS = "127.0.0.1:2181";

    //虚拟机
    private static final String ZK_ADDRESS = "cdh1:2181";
    public static final String TEST_CRUD_REMOTE_NODE_1 = "/test/CRUD/remoteNode-1";

    /**
     * 检查节点
     */
    @Test
    public void checkNode() {
        //创建客户端
        CuratorFramework client = ClientFactory.createSimple(ZK_ADDRESS);
        try {
            //启动客户端实例,连接服务器
            client.start();

            // 创建一个 ZNode 节点
            // 节点的数据为 payload

            String zkPath = TEST_CRUD_REMOTE_NODE_1;
            long stime = System.currentTimeMillis();
            Stat stat = client.checkExists().forPath(zkPath);
            if (null == stat) {
                log.info("节点不存在:", zkPath);
            } else {

                log.info("节点存在 stat is:", stat.toString());
            }
            Logger.info("forPath耗费时间ms：" + (System.currentTimeMillis() - stime));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }


    /**
     * 创建节点
     */
    @Test
    public void createNode() {

        //创建客户端
        CuratorFramework client = ClientFactory.createSimple(ZK_ADDRESS);
        try {
            //启动客户端实例,连接服务器
            client.start();

            // 创建一个 ZNode 节点
            // 节点的数据为 payload

            String data = "hello";
            byte[] payload = data.getBytes("UTF-8");
            String zkPath = TEST_CRUD_REMOTE_NODE_1;
            long stime = System.currentTimeMillis();
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(zkPath, payload);
            Logger.info("forPath耗费时间ms：" + (System.currentTimeMillis() - stime));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    @Test
    public void testmod() {
        for (int i = 0; i < 128; i++) {
            int id = 1000 + i;
            System.out.print("devId = " + id);
            System.out.print("  devId%2 = " + id % 2);
            System.out.print("   devId/2%16 = " + id / 2 % 32);
            System.out.println(" ");
        }
    }

    /**
     * 创建 临时 节点
     */
    @Test
    public void createEphemeralNode() {
        //创建客户端
        CuratorFramework client = ClientFactory.createSimple(ZK_ADDRESS);
        try {
            //启动客户端实例,连接服务器
            client.start();

            // 创建一个 ZNode 节点
            // 节点的数据为 payload

            String data = "hello";
            byte[] payload = data.getBytes("UTF-8");
            String zkPath = "/test/remoteNode-2";
            client.create()
                    .creatingParentsIfNeeded()

                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(zkPath, payload);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    /**
     * 创建 持久化 顺序 节点
     */
    @Test
    public void createPersistentSeqNode() {
        //创建客户端
        CuratorFramework client = ClientFactory.createSimple(ZK_ADDRESS);
        try {
            //启动客户端实例,连接服务器
            client.start();

            // 创建一个 ZNode 节点
            // 节点的数据为 payload

            String data = "hello";

            for (int i = 0; i < 10; i++) {
                byte[] payload = data.getBytes("UTF-8");
                String zkPath = "/test/remoteNode-seq-";
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                        .forPath(zkPath, payload);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    /**
     * 读取节点
     */
    @Test
    public void readNode() {
        //创建客户端
        CuratorFramework client = ClientFactory.createSimple(ZK_ADDRESS);
        try {
            //启动客户端实例,连接服务器
            client.start();

            String zkPath = TEST_CRUD_REMOTE_NODE_1;


            Stat stat = client.checkExists().forPath(zkPath);
            if (null != stat) {
                //读取节点的数据
                byte[] payload = client.getData().forPath(zkPath);
                String data = new String(payload, "UTF-8");
                log.info("读到数据:"+ data);

                String parentPath = "/test";
                List<String> children = client.getChildren().forPath(parentPath);

                for (String child : children) {
                    log.info("孩子节点:"+ child);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }


    /**
     * 更新节点
     */
    @Test
    public void updateNode() {
        //创建客户端
        CuratorFramework client = ClientFactory.createSimple(ZK_ADDRESS);
        try {
            //启动客户端实例,连接服务器
            client.start();


            String data = "hello world";
            byte[] payload = data.getBytes("UTF-8");
            String zkPath = TEST_CRUD_REMOTE_NODE_1;

            client.setData()
                    .forPath(zkPath, payload);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }


    /**
     * 更新节点 - 异步模式
     */
    @Test
    public void updateNodeAsync() {
        //创建客户端
        CuratorFramework client = ClientFactory.createSimple(ZK_ADDRESS);
        CountDownLatch latch = new CountDownLatch(1);
        try {

            //更新完成监听器,  书里边是这个 回调处理器，版本升级之后， 回调失败
            AsyncCallback.StringCallback callback = new AsyncCallback.StringCallback() {

                @Override
                public void processResult(int i, String s, Object o, String s1) {
                    log.info("执行回调");
                    log.info(
                            "i = " + i + " | " +
                                    "s = " + s + " | " +
                                    "o = " + o + " | " +
                                    "s1 = " + s1
                    );

                    latch.countDown();
                }
            };

            //新的异步回调方法
            BackgroundCallback callbacNew= new BackgroundCallback() {
                @Override
                public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent) {
                    log.info("执行回调");
                    System.out.println("结点路径" + curatorEvent.getPath());
                    System.out.println("事件类型" + curatorEvent.getType());
                    if (0 != curatorEvent.getResultCode() )
                        System.out.println("操作失败");//0,执行成功，其它，执行失败
                    else
                        System.out.println("操作成功");
                     latch.countDown();
                }
            };
            //启动客户端实例,连接服务器
            client.start();

            String data = "hello ,every body, update  async! ";
            byte[] payload = data.getBytes("UTF-8");
            String zkPath = TEST_CRUD_REMOTE_NODE_1;
            client.setData()
                    .inBackground(callbacNew)
                    .forPath(zkPath, payload);
            log.info("开始操作");

//            Thread.sleep(10000);
            latch.await(10, TimeUnit.SECONDS);
            log.info("回调结束");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    /**
     * 删除节点
     */
    @Test
    public void deleteNode() {
        //创建客户端
        CuratorFramework client = ClientFactory.createSimple(ZK_ADDRESS);
        try {
            //启动客户端实例,连接服务器
            client.start();

            //删除节点
            String zkPath = TEST_CRUD_REMOTE_NODE_1;
            client.delete().forPath(zkPath);


            //删除后查看结果
            String parentPath = "/test";
            List<String> children = client.getChildren().forPath(parentPath);

            for (String child : children) {
                log.info("child:", child);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.closeQuietly(client);
        }
    }


}
