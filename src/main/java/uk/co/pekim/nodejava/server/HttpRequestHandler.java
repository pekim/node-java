/**
 * 
 */
package uk.co.pekim.nodejava.server;

import java.io.IOException;
import java.io.PrintStream;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.pekim.nodejava.NodeJavaException;

/**
 * Process an HTTP request.
 * 
 * @author Mike D Pilsbury
 */
public class HttpRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestHandler.class);

    /**
     * The name of the HTTP header used to convey the class name of the request
     * handler class.
     */
    public static final String HEADER_HANDLERCLASS = "X-NodeJava-Handler";

    private final JsonRequestHandler jsonRequestHandler;

    /**
     * Create an HTTP request handler.
     * 
     * @param jsonRequestHandler
     *            the handler that the real work is delegated to.
     */
    public HttpRequestHandler(final JsonRequestHandler jsonRequestHandler) {
        this.jsonRequestHandler = jsonRequestHandler;

    }

    /**
     * Process a request, hiding the HTTP details from the JSON handler.
     * 
     * @param request
     *            the HTTP request.
     * @param response
     *            The HTTP response.
     */
    void handle(final Request request, final Response response) {
        try {
            final String handlerClassName = request.getValue(HEADER_HANDLERCLASS);
            if (handlerClassName == null) {
                throw new NodeJavaException("Missing header " + HEADER_HANDLERCLASS);
            }

            final String jsonRequest = request.getContent();
            LOGGER.debug("Request : " + jsonRequest);

            final String jsonResponse = jsonRequestHandler.handle(handlerClassName, jsonRequest);
            LOGGER.debug("Response : " + jsonResponse);

            PrintStream body = response.getPrintStream();

            response.set("Content-Type", "application/json");

            body.print(jsonResponse);
            body.close();
        } catch (IOException exception) {
            throw new NodeJavaException("Failed to process request", exception);
        }
    }
}
