package pt.isel.ls.http;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpApp {

    private static final Logger log = LoggerFactory.getLogger(HttpApp.class);

    private static final int LISTEN_PORT = 8080;

    public static void main(String[] args) throws Exception {
        String portDef = System.getenv("PORT");
        int port = portDef != null ? Integer.parseInt(portDef) : LISTEN_PORT;
        runServer(port);
    }

    public static void runServer(int port) throws Exception {
        if (port != LISTEN_PORT) {
            System.err.println("PORT CANNOT BE SOMETHING SOMETHING THAT IS NOT 8080 ");
            port = LISTEN_PORT;
        }
        log.info("main started");
        log.info("configured listening port is {}", port);

        Server server = new Server(port);
        ServletHandler handler = new ServletHandler();
        Http servlet = new Http();

        handler.addServletWithMapping(new ServletHolder(servlet), "/*");
        log.info("registered {} on all paths", servlet);

        server.setHandler(handler);
        server.start();

        log.info("server started listening on port {}", port);

        //server.join();
        System.in.read();

        server.stop();

        log.info("main is ending");
    }
}