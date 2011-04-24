package uk.co.pekim.nodejava.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.pekim.nodejava.NodeJavaException;

/**
 * An HTTP processor that executes requests asynchronously.
 * 
 * @author Mike D Pilsbury
 */
public class HttpContainer implements Container {
    private static final int THREADPOOL_SIZE = 20;

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpContainer.class);

    private final HttpRequestHandler handler;
    private final ExecutorService threadPool;

    private static final class RequestTask implements Runnable {
        private final Request request;
        private final Response response;
        private final HttpRequestHandler handler;

        private RequestTask(final HttpRequestHandler handler, final Request request, final Response response) {
            this.handler = handler;
            this.request = request;
            this.response = response;
        }

        @Override
        public void run() {
            handler.handle(request, response);
        }
    }

    /**
     * Create an HTTP request processor.
     * 
     * @param handler
     *            the handler that processing of the requests will be delegated
     *            to.
     */
    public HttpContainer(final HttpRequestHandler handler) {
        this.handler = handler;
        this.threadPool = Executors.newFixedThreadPool(THREADPOOL_SIZE);
    }

    @Override
    public void handle(final Request request, final Response response) {
        try {
            threadPool.execute(new RequestTask(handler, request, response));
        } catch (NodeJavaException exception) {
            LOGGER.error("Failed to process request : " + request, exception);
        }
    }
}
