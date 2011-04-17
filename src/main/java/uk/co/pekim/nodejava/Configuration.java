package uk.co.pekim.nodejava;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class Configuration {
    private int nodePort;

    public static Configuration parseJson(String json) {
        try {
            return new ObjectMapper().readValue(json, Configuration.class);
        } catch (JsonParseException exception) {
            throw new NodeJavaException("Failed to parse JSON " + json, exception);
        } catch (JsonMappingException exception) {
            throw new NodeJavaException("Failed to parse JSON " + json, exception);
        } catch (IOException exception) {
            throw new NodeJavaException("Failed to parse JSON " + json, exception);
        }
    }

    /**
     * @param nodePort
     *            the nodePort to set
     */
    public void setNodePort(int nodePort) {
        this.nodePort = nodePort;
    }

    /**
     * @return the nodePort
     */
    public int getNodePort() {
        return nodePort;
    }
}
