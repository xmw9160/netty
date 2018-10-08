package com.xmw.rpc.core.msg;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xmw.
 * @date 2018/7/14 15:47.
 */
@Data
public class InvokeMsg implements Serializable{

    // 服务名称
    private String className;
    // 调用方法
    private String methodName;
    // 参数列表
    private Class<?>[] params;
    // 参数值
    private Object[] values;
}
