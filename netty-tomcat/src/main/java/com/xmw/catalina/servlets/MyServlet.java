package com.xmw.catalina.servlets;

import com.xmw.catalina.http.XmwRequest;
import com.xmw.catalina.http.XmwResponse;
import com.xmw.catalina.http.XmwServlet;

/**
 * @author xmw.
 * @date 2018/7/8 10:53.
 */
public class MyServlet extends XmwServlet {
    @Override
    public void doGet(XmwRequest request, XmwResponse response) {
        try {
            response.write(request.getParameter("name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(XmwRequest request, XmwResponse response) {
        doGet(request, response);
    }
}
