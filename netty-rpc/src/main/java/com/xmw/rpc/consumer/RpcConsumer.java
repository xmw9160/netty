package com.xmw.rpc.consumer;

import com.xmw.rpc.api.IRpcCalc;
import com.xmw.rpc.api.IRpcHello;
import com.xmw.rpc.consumer.proxy.RpcProxy;

/**
 * @author xmw.
 * @date 2018/7/14 11:38.
 */
public class RpcConsumer {

    public static void main(String[] args) {
        // 本地一个人玩, 自娱自乐
//        IRpcHello rpcHello = new RpcHello();
//        rpcHello.hello("tom");

        // 用动态代理来实现的, 传给他一个接口, 返回一个实例, 伪代理
        IRpcHello rpcHello = RpcProxy.create(IRpcHello.class);
        String hello = rpcHello.hello("xmw");
        System.out.println(hello);

        IRpcCalc calc = RpcProxy.create(IRpcCalc.class);
        int a = 8, b = 2;
        System.out.println("a + b = " + calc.add(a, b));
        System.out.println("a - b = " + calc.sub(a, b));
        System.out.println("a * b = " + calc.mult(a, b));
        System.out.println("a / b = " + calc.div(a, b));
    }
}
