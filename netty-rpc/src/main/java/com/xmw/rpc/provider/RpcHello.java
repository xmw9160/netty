package com.xmw.rpc.provider;

import com.xmw.rpc.api.IRpcHello;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xmw.
 * @date 2018/7/14 15:08.
 */
@Slf4j
public class RpcHello implements IRpcHello {
    @Override
    public String hello(String name) {
        log.info("server parameter: " + name);
        return "hello " + name + " ...";
    }
}
