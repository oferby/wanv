package com.huawei.sdn.pathselector.ui;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.huawei.sdn.pathselector.tools.BeanFactory;
import com.huawei.sdn.commons.topology.manager.SwitchConfigManager;

public class DebugServlet implements Servlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    private void addHeader(PrintWriter out, String title) {
        out.println("<HTML>");
        out.println("<HEAD><TITLE>" + title + "</TITLE></HEAD>");
        out.println("<BODY>");
    }

    private void addFooter(PrintWriter out) {
        out.println("</BODY></HTML>");
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
//        res.setContentType("text/html");
//        final PrintWriter out = res.getWriter();
//        final String operation = req.getParameter("operation");
//        if ("flows".equalsIgnoreCase(operation)) {
//            addHeader(out, "Flows List");
//            final String ip = req.getParameter("ip");
//            final ODLPathSelectorEngine pathSelectorEngine = (ODLPathSelectorEngine) BeanFactory.getInstance().getBean(
//                    ODLPathSelectorEngine.class);
//            out.println(pathSelectorEngine.getAllFlowsAsHtml(ip));
//        } else if ("config".equalsIgnoreCase(operation)) {
//            addHeader(out, "Configuration");
//            final SwitchConfigManager switchConfigManager = (SwitchConfigManager) BeanFactory.getInstance().getBean(
//                    SwitchConfigManager.class);
//            out.println(switchConfigManager.getConfigurationAsHtml());
//        } else {
//            addHeader(out, "Usage");
//            out.println("?operation=flows => show all cached flows.<br/>");
//            out.println("?operation=flows&ip=X.X => show all cached flows with destination or source IP start with X.X.<br/>");
//            out.println("?operation=config => show the configuration.<br/>");
//        }
//        addFooter(out);
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {
    }

}
