package com.gxf.config.zk;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.data.Stat;

/**
 * @Author: <guanxianseng@163.com>
 * @Description:
 * @Date: Created in : 2018/11/8 5:03 PM
 **/
public class FieldPathChildrenCacheListener implements PathChildrenCacheListener {

  private static Map<String, Field> key2Field = new HashMap<String, Field>();
  private static String zkPrefix = "/gxf_config";


  public void childEvent(CuratorFramework curatorFramework,
      PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
    System.out.println("pathChildrenCacheEvent.getData(): " + pathChildrenCacheEvent.getData());
    String zkPath = pathChildrenCacheEvent.getData().getPath();
    System.out.println("zkpath: " + zkPath);
    String zkData = new String(pathChildrenCacheEvent.getData().getData());
    System.out.println("zkData: " + zkData);
    int zkPrefixIndex = zkPath.indexOf(zkPrefix);
    String key = zkPath.substring(zkPrefixIndex + zkPrefix.length() + 1, zkPath.length());
    System.out.println("key: " + key);
    Field field = key2Field.get(key);
    field.setAccessible(true);
    field.set(null, zkData);
  }

  public static void registerFiled(String key, Field field, String value) {
    key2Field.put(key, field);
    CuratorFramework client = ZkUtil.getClient();
    client.start();
    try {
      String zkPath = zkPrefix + "/" + key;
      Stat stat = client.checkExists().forPath(zkPath);
      if (stat == null) {
        //注册到zk
        String createResult = client.create().forPath(zkPath, value.getBytes());
        System.out.println("createResult : " + createResult);
      } else {
        //从zk拉取数据
        String zkData = new String(client.getData().forPath(zkPath));
        System.out.println("registerFiled zkData: " + zkData);
        field.setAccessible(true);
        field.set(null, zkData);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
