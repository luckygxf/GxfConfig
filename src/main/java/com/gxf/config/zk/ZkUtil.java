package com.gxf.config.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @Author: <guanxianseng@163.com>
 * @Description:
 * @Date: Created in : 2018/11/8 4:12 PM
 **/
public class ZkUtil {
  private static CuratorFramework client = null;
  private static String zkPrefix = "/gxf_config";

  public static void init(){
    client = getClient();
    client.start();
    try {
//      if(client.checkExists().forPath(zkPrefix) == null){
//        System.out.println("/gxf_config is not exist, create it!");
//        client.create().forPath(zkPrefix);
//      }
      //添加监听器
      NodeCache nodeCache = new NodeCache(client, zkPrefix);
      nodeCache.start();
      nodeCache.getListenable().addListener(new FieldNodeCacheListener(nodeCache));

      //监听一个路径
      PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "/gxf_config", true);
      pathChildrenCache.start();
      pathChildrenCache.getListenable().addListener(new FieldPathChildrenCacheListener());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 字段添加监听器
   * */
  public static void addFiledListener(String fieldKey, String fieldValue){
    StringBuilder zkPath = new StringBuilder(zkPrefix);
    zkPath.append("/").append(fieldKey);
    //节点不存在创建
    try {
      if(client.checkExists().forPath(zkPath.toString()) == null){
        client.create().forPath(zkPath.toString());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 获取zk客户端
   * */
  public static CuratorFramework getClient(){
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3);
    CuratorFramework client = CuratorFrameworkFactory.builder()
        .connectString("127.0.0.1:2181")
        .retryPolicy(retryPolicy)
        .sessionTimeoutMs(6000)
        .connectionTimeoutMs(3000)
        .namespace("gxf_config")
        .build();
    return client;
  }
}
