package com.lenovo.iot.digitaltwin;

import fi.iki.elonen.NanoHTTPD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DtServerRunner {

    /**
     * logger to log to.
     */
    private static final Logger LOG = LoggerFactory.getLogger(DtServerRunner.class.getName());

    public static void executeInstance(NanoHTTPD server) {
        try {
            server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
            System.exit(-1);
        }
        System.out.println("Server started.");

        /*System.out.println("Server started, Hit Enter to stop.\n");
        try {
            System.in.read();
        } catch (Throwable ignored) {
        }
        server.stop();
        System.out.println("Server stopped.\n");*/
    }

    public static <T extends NanoHTTPD> void run(Class<T> serverClass) {
        try {
            executeInstance(serverClass.newInstance());
        } catch (Exception e) {
            LOG.error("Cound nor create server", e);
        }
    }
}
