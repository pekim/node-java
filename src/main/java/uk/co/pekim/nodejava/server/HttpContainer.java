package uk.co.pekim.nodejava.server;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.pekim.nodejava.NodeJavaException;

/**
 * 
 * 
 * @author Mike D Pilsbury
 */
public class HttpContainer implements Container {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpContainer.class);

    private final HttpRequestHandler handler;

    /**
     * Create an HTTP request processor.
     * 
     * @param handler
     *            the handler that processing of the requests will be delegated
     *            to.
     */
    public HttpContainer(final HttpRequestHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handle(final Request request, final Response response) {
        try {
            handler.handle(request, response);
        } catch (NodeJavaException exception) {
            LOGGER.error("Failed to process request : " + request, exception);
        }
    }
}
