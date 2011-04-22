package uk.co.pekim.nodejava;

import java.io.IOException;
import java.io.PrintStream;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author Mike D Pilsbury
 */
public class Server implements Container {
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    @Override
    public void handle(final Request request, final Response response) {
        try {
            LOGGER.info(Thread.currentThread().getName());

            PrintStream body = response.getPrintStream();
            long time = System.currentTimeMillis();

            response.set("Content-Type", "text/plain");
            response.set("Server", "HelloWorld/1.0 (Simple 4.0)");
            response.setDate("Date", time);
            response.setDate("Last-Modified", time);

            body.println("Hello World");
            body.close();
        } catch (IOException exception) {
            throw new NodeJavaException("Failed to process request", exception);
        }
    }
}