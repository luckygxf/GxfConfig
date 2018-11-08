package com.gxf.config;

import com.gxf.config.annotations.GxfConfig;
import com.gxf.config.demo.Person;
import com.gxf.config.util.ClassUtil;
import com.gxf.config.zk.FieldPathChildrenCacheListener;
import com.gxf.config.zk.ZkUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: <guanxianseng@163.com>
 * @Description:
 * @Date: Created in : 2018/11/8 4:11 PM
 **/
public class GxfConfigMain {
    private static String scanPackage;
    private static Map<String, Field> key2Filed = new HashMap<String, Field>();

    /**
     * 设置需要扫描包的路径
     * */
    public static void setScanPackage(String packagePath){
      scanPackage = packagePath;
    }

    /**
     * 启动
     * todo:注册到zk，添加监听器
     * */
    public static void start() throws Exception {
        //初始化zk客户端
        ZkUtil.init();

        if(StringUtils.isEmpty(scanPackage)){
            throw new Exception("scanPackage is emptye!");
        }
        List<Class<?>> classList = ClassUtil.getClasses(scanPackage);
        for(Class clazz : classList){
            //遍历所有字段
            Field[] fields = clazz.getDeclaredFields();
            for(Field field : fields){
                GxfConfig gxfConfig = field.getAnnotation(GxfConfig.class);
                if(gxfConfig == null){
                    continue;
                }
                //注册到zk listener
                FieldPathChildrenCacheListener.registerFiled(gxfConfig.key(), field, gxfConfig.value());
            }
        }
    }


    public static void main(String[] args) {
        try {
            setScanPackage("com.gxf.config.demo");
            start();
            while(true){
                System.out.println("person name:" + Person.name);
                System.out.println("person address:" + Person.address);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
