/**
 * 
 */
package uk.co.pekim.nodejava.server;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.pekim.nodejava.NodeJavaException;
import uk.co.pekim.nodejava.nodehandler.ErrorResponse;
import uk.co.pekim.nodejava.nodehandler.NodeJavaHandler;
import uk.co.pekim.nodejava.nodehandler.NodeJavaRequest;
import uk.co.pekim.nodejava.nodehandler.NodeJavaResponse;

/**
 * 
 * 
 * @author Mike D Pilsbury
 */
public class JsonRequestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonRequestHandler.class);

    private final ObjectMapper jsonMapper;

    /**
     * Create a JSON request handler.
     */
    public JsonRequestHandler() {
        jsonMapper = new ObjectMapper();
        jsonMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * Process a JSON request, and return a JSON response.
     * 
     * @param jsonRequest
     *            the JSON request.
     * @param handlerClassName
     *            The name of the class that should handle this request.
     * @return the JSON response.
     */
    public String handle(final String handlerClassName, final String jsonRequest) {
        try {
            NodeJavaHandler handler = getHandler(handlerClassName);
            Class<? extends NodeJavaRequest> requestClass = handler.getRequestClass();

            NodeJavaRequest request = jsonMapper.readValue(jsonRequest, requestClass);
            NodeJavaResponse response = handler.handle(request);

            final String jsonResponse = jsonMapper.writeValueAsString(response);

            return jsonResponse;
        } catch (Throwable exception) {
            LOGGER.error("Failed to process request, handlerClassName:" + handlerClassName + ", jsonRequest:"
                    + jsonRequest, exception);

            return generateErrorResponse(exception);
        }
    }

    private String generateErrorResponse(final Throwable exception) {
        try {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            exception.printStackTrace(printWriter);

            ErrorResponse errorResponse = new ErrorResponse(exception.getMessage(), stringWriter.toString());

            return jsonMapper.writeValueAsString(errorResponse);
        } catch (Exception exception2) {
            LOGGER.error("Failed to build ErrorResponse", exception2);

            return "\"error\": \"Failed to build ErrorResponse\", \"message\": \"See log for details\"";
        }
    }

    private NodeJavaHandler getHandler(final String handlerClassName) {
        try {
            Class<? extends NodeJavaHandler> handlerClass = Class.forName(handlerClassName).asSubclass(
                    NodeJavaHandler.class);

            handlerClass.getConstructors();

            return handlerClass.getConstructor().newInstance();
        } catch (SecurityException exception) {
            throw new NodeJavaException("Failed to get handler for " + handlerClassName, exception);
        } catch (ClassNotFoundException exception) {
            throw new NodeJavaException("Failed to get handler for " + handlerClassName, exception);
        } catch (NoSuchMethodException exception) {
            throw new NodeJavaException("Failed to get handler for " + handlerClassName, exception);
        } catch (IllegalArgumentException exception) {
            throw new NodeJavaException("Failed to get handler for " + handlerClassName, exception);
        } catch (InstantiationException exception) {
            throw new NodeJavaException("Failed to get handler for " + handlerClassName, exception);
        } catch (IllegalAccessException exception) {
            throw new NodeJavaException("Failed to get handler for " + handlerClassName, exception);
        } catch (InvocationTargetException exception) {
            throw new NodeJavaException("Failed to get handler for " + handlerClassName, exception);
        }
    }
}
