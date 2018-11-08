package com.gxf.config.zk;

import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;

/**
 * @Author: <guanxianseng@163.com>
 * @Description:
 * @Date: Created in : 2018/11/8 4:47 PM
 **/
public class FieldNodeCacheListener implements NodeCacheListener {
  private NodeCache nodeCache;

  public FieldNodeCacheListener(NodeCache nodeCache) {
    this.nodeCache = nodeCache;
  }

  public void nodeChanged() throws Exception {
    System.out.println("receive new value:" + nodeCache.getCurrentData());
    System.out.println("nodeCache.getPath() : " + nodeCache.getPath());
  }
}
