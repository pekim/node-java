/**
 * 
 */
package uk.co.pekim.nodejava.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import uk.co.pekim.nodejava.nodehandler.echo.EchoHandler;
import uk.co.pekim.nodejava.server.JsonRequestHandler;

/**
 * Test {@link JsonRequestHandler}.
 * 
 * @author Mike D Pilsbury
 */
public class JsonRequestHandlerTest {
    private JsonRequestHandler jsonRequestHandler;

    @Before
    public void setup() {
        jsonRequestHandler = new JsonRequestHandler();
    }

    @Test
    public void handlerNotFound() {
        String response = jsonRequestHandler.handle("bad", "request");

        assertTrue(response.contains("error"));
        assertTrue(response.contains("handler"));
    }

    @Test
    public void handlerNotImplementNodeJavaHandler() {
        String response = jsonRequestHandler.handle("java.lang.Object", "request");

        assertTrue(response.contains("error"));
        assertTrue(response.contains("ClassCastException"));
    }

    @Test
    public void echoHandler() {
        String request = "{\"text\":\"some-text\", \"number\":6}";
        String response = jsonRequestHandler.handle(EchoHandler.class.getName(), request);

        assertEquals("{\"text\":\"some-text\",\"incrementedNumber\":7}", response);
    }
}
