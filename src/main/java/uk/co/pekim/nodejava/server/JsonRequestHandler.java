/**
 * 
 */
package uk.co.pekim.nodejava.server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import uk.co.pekim.nodejava.NodeJavaException;

/**
 * 
 * 
 * @author Mike D Pilsbury
 */
public class JsonRequestHandler {
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
        } catch (IOException exception) {
            throw new NodeJavaException("Failed to build JSON response", exception);
        } catch (ClassCastException exception) {
            throw new NodeJavaException("Failed to build JSON response", exception);
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
