/**
 * 
 */
package uk.co.pekim.nodejava.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import uk.co.pekim.nodejava.nodehandler.echo.EchoHandler;

/**
 * Test {@link JsonRequestHandler}.
 * 
 * @author Mike D Pilsbury
 */
public class JsonRequestHandlerTest {
    private JsonRequestHandler jsonRequestHandler;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        jsonRequestHandler = new JsonRequestHandler();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void handlerNotFound() throws Exception {
        String responseString = jsonRequestHandler.handle("bad-class-name", "request");

        @SuppressWarnings("rawtypes")
        Map response = objectMapper.readValue(responseString, Map.class);

        assertNotNull(response.containsKey("error"));
        assertNotNull(response.containsKey("message"));
    }

    @Test
    public void handlerNotImplementNodeJavaHandler() throws Exception {
        String responseString = jsonRequestHandler.handle("java.lang.Object", "request");

        @SuppressWarnings("rawtypes")
        Map response = objectMapper.readValue(responseString, Map.class);

        assertNotNull(response.containsKey("error"));
        assertNotNull(response.containsKey("message"));
    }

    @Test
    public void echoHandler() {
        String request = "{\"text\":\"some-text\", \"number\":6}";
        String response = jsonRequestHandler.handle(EchoHandler.class.getName(), request);

        assertEquals("{\"text\":\"some-text\",\"incrementedNumber\":7}", response);
    }
}
