package uk.co.pekim.nodejava.configuration;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import uk.co.pekim.nodejava.NodeJavaException;

/**
 * Server configuration, built from a JSON string.
 * 
 * @author Mike D Pilsbury
 */
public class Configuration {
    private int nodePort;
    private Map<String, String> requestHandlers;

    /**
     * Parse configuration from a JSON string.
     * 
     * @param json
     *            the configuration string to be parsed.
     * @return configuration
     */
    public static Configuration parseJson(final String json) {
        try {
            return new ObjectMapper().readValue(json, Configuration.class);
        } catch (JsonParseException exception) {
            throw new NodeJavaException("Failed to parse configuration JSON " + json, exception);
        } catch (JsonMappingException exception) {
            throw new NodeJavaException("Failed to parse configuration JSON " + json, exception);
        } catch (IOException exception) {
            throw new NodeJavaException("Failed to parse configuration JSON " + json, exception);
        }
    }

    /**
     * @param nodePort
     *            the nodePort to set
     */
    public void setNodePort(final int nodePort) {
        this.nodePort = nodePort;
    }

    /**
     * @return the nodePort
     */
    public int getNodePort() {
        return nodePort;
    }

    /**
     * @param requestHandlers
     *            the requestHandlers to set
     */
    public void setRequestHandlers(final Map<String, String> requestHandlers) {
        this.requestHandlers = requestHandlers;
    }

    /**
     * @return the requestHandlers
     */
    public Map<String, String> getRequestHandlers() {
        return requestHandlers;
    }
}
