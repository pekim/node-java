/**
 * 
 */
package uk.co.pekim.nodejava.server;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import uk.co.pekim.nodejava.NodeJavaException;
import uk.co.pekim.nodejava.nodenotify.NotifyInitialised;

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
    }

    /**
     * Process a JSON request, and return a JSON response.
     * 
     * @param request
     *            the JSON request.
     * @return the JSON response.
     */
    public String handle(final String request) {
        try {
            final String response = jsonMapper.writeValueAsString(new NotifyInitialised(42));

            return response;
        } catch (IOException exception) {
            throw new NodeJavaException("Failed to build JSON response", exception);
        }
    }
}
