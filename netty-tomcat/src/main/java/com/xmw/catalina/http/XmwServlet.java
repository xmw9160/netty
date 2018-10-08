package com.xmw.catalina.http;

/**
 * @author xmw.
 * @date 2018/7/8 10:50.
 */
public abstract class XmwServlet {
    public abstract void doGet(XmwRequest request, XmwResponse response);

    public abstract void doPost(XmwRequest request, XmwResponse response);
}
