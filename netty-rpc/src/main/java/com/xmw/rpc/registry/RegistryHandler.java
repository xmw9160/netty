package com.xmw.rpc.registry;

import com.xmw.rpc.core.msg.InvokeMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 处理整个注册中心的业务逻辑
 *
 * @author xmw.
 * @date 2018/7/14 15:26.
 */
@Slf4j
public class RegistryHandler extends ChannelInboundHandlerAdapter {

    // 在注册中心的服务需要一个容器存放
    private static ConcurrentHashMap<String, Object> registryMap = new ConcurrentHashMap<>();

    private List<String> classCache = new ArrayList<>();

    // 约定, 只要写在provider包下面的所有类都认为是一个可以对外提供服务的实现类
    // com.xmw.rpc.registry

    public RegistryHandler() {
        scanClass("com.xmw.rpc.provider");
        doRegistry();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Object result = new Object();
        // 客户端传过来的调用信息
        InvokeMsg request = (InvokeMsg) msg;
        if (registryMap.containsKey(request.getClassName())) {
            Object clazz = registryMap.get(request.getClassName());
            Method method = clazz.getClass().getMethod(request.getMethodName(), request.getParams());
            result = method.invoke(clazz, request.getValues());
        }

        ctx.writeAndFlush(result);
        ctx.close();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        log.error(cause.getMessage());
        ctx.close();
    }

    // IOC容器
    // 扫描出所有的class
    private void scanClass(String packageName) {
        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                scanClass(packageName + "." + file.getName());
            } else {
                classCache.add(packageName + "." + file.getName().replace(".class", "").trim());
            }
        }
    }

    // 把扫描到的class实例化， 放到map中，这就是注册过程
    // 注册的服务的名字， 叫接口名字
    // 约定优于配置
    private void doRegistry() {
        if (classCache.size() == 0) {
            return;
        }
        classCache.forEach(className ->{
            try {
                Class<?> clazz = Class.forName(className);
                Class<?> interfaces = clazz.getInterfaces()[0];
                registryMap.put(interfaces.getName(), clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
