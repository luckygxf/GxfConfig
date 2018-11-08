package com.gxf.config.demo;

import com.gxf.config.annotations.GxfConfig;

/**
 * @Author: <guanxianseng@163.com>
 * @Description:
 * @Date: Created in : 2018/11/8 4:51 PM
 **/
public class Person {

  @GxfConfig(key= "person_name", value = "zhangsan")
  public static String name;

  @GxfConfig(key= "person_address", value = "beijing")
  public static String address ;

}
