package com.xmw.udp;

import lombok.ToString;

import java.net.InetSocketAddress;

/**
 * @author xmw.
 * @date 2018/8/23 22:50.
 */
@ToString
public final class LogEvent {
    public static final byte SEPARATOR = (byte) ':';
    private final InetSocketAddress source;
    private final String logfile;
    private final String msg;
    private final long received;

    // 用于传出消息的构造函数
    public LogEvent(String logfile, String msg) {
        this(null, -1, logfile, msg);
    }

    // 用于传入消息的构造函数
    public LogEvent(InetSocketAddress source, long received,
                    String logfile, String msg) {
        this.source = source;
        this.logfile = logfile;
        this.msg = msg;
        this.received = received;
    }

    // 返回发送LogEvent的源的InetSocketAddress
    public InetSocketAddress getSource() {
        return source;
    }

    // 返回所发送的LogEvent的日志文件的名称
    public String getLogfile() {
        return logfile;
    }

    // 返回消息内容
    public String getMsg() {
        return msg;
    }

    // 返回接收LogEvent的时间
    public long getReceivedTimestamp() {
        return received;
    }
}
