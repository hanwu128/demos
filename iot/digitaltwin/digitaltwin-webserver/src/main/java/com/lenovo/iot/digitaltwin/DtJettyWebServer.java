package com.lenovo.iot.digitaltwin;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Desc: 基于Jetty实现的Web服务器，部署DT前端页面。
 * @Name: com.lenovo.iot.digitaltwin.DtJettyWebServer
 * @Author: chench9@lenovo.com
 * @Date: 2018/6/28
 */
public class DtJettyWebServer {

    public static void main(String[] args) throws Exception {
        String host = "";
        int port = 8080;
        String webRoot = ".";

        // 从配置文件读取参数
        InputStream is = null;
        try {
            is = DtJettyWebServer.class.getClassLoader().getResourceAsStream("config.properties");
            Properties prop = new Properties();
            prop.load(is);
            host = prop.getProperty("host");
            port = Integer.valueOf(prop.getProperty("port"));
            webRoot = prop.getProperty("webRoot");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 解析命令行参数
        // Parse command-line, with short and long versions of the options.
        for (int i = 0; i < args.length; ++i) {
            if (args[i].equalsIgnoreCase("-h") || args[i].equalsIgnoreCase("--host")) {
                host = args[i + 1];
            } else if (args[i].equalsIgnoreCase("-p") || args[i].equalsIgnoreCase("--port")) {
                port = Integer.parseInt(args[i + 1]);
            } else if (args[i].equalsIgnoreCase("-d") || args[i].equalsIgnoreCase("--dir")) {
                webRoot = new File(args[i + 1]).getAbsolutePath();
            }
        }

        staticContentWebServer(port, webRoot);
    }

    // 静态文件web服务器
    private static final void staticContentWebServer(int port, String webRoot) throws Exception {
        // Create a basic Jetty server object that will listen on port 8080.  Note that if you set this to port 0
        // then a randomly available port will be assigned that you can either look in the logs for the port,
        // or programmatically obtain it for use in test cases.
        Server server = new Server(port);

        // Create the ResourceHandler. It is the object that will actually handle the request for a given file. It is
        // a Jetty Handler object so it is suitable for chaining with other handlers as you will see in other examples.
        ResourceHandler resourceHandler = new ResourceHandler();

        // Configure the ResourceHandler. Setting the resource base indicates where the files should be served out of.
        // In this example it is the current directory but it can be configured to anything that the jvm has access to.
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{ "index.html" });
        //resourceHandler.setResourceBase(".");
        resourceHandler.setResourceBase(webRoot);

        // Add the ResourceHandler to the server.
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {resourceHandler, new DefaultHandler() });
        server.setHandler(handlers);

        // Start things up! By using the server.join() the server thread will join with the current thread.
        // See "http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Thread.html#join()" for more details.
        server.start();
        server.join();
    }

    private static void test() throws Exception {
        Server server = new Server(8081);

        ServletContextHandler ctx = new ServletContextHandler();
        ctx.setContextPath("/");

        DefaultServlet defaultServlet = new DefaultServlet();
        ServletHolder holderPwd = new ServletHolder("default", defaultServlet);
        holderPwd.setInitParameter("resourceBase", "D:\\sun\\workspace\\iot\\digitaltwin\\digitaltwin-web\\work\\dist");

        ctx.addServlet(holderPwd, "/*");

        server.setHandler(ctx);

        server.start();
        server.join();
    }

    private static void start() throws Exception {
        Server server = new Server(8080);

        ServletContextHandler context = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        context.setContextPath("/");
//        context.setResourceBase(System.getProperty("java.io.tmpdir"));
        context.setResourceBase("D:\\sun\\workspace\\iot\\digitaltwin\\digitaltwin-web\\work\\dist");
        server.setHandler(context);

        // Add default servlet
        context.addServlet(DefaultServlet.class, "/");

        server.start();
        server.join();
    }

}
