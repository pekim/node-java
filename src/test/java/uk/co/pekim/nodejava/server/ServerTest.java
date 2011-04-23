/**
 * 
 */
package uk.co.pekim.nodejava.server;

import static org.junit.Assert.assertTrue;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.http.core.Container;

/**
 * Test {@link Server}
 * 
 * @author Mike D Pilsbury
 */
public class ServerTest {
    private Server server;
    private DefaultHttpClient httpClient;
    private URI uri;

    @Before
    public void setup() throws Exception {
        final JsonRequestHandler jsonRequestHandler = new JsonRequestHandler();
        final HttpRequestHandler handler = new HttpRequestHandler(jsonRequestHandler);
        final Container requestProcessor = new HttpContainer(handler);
        server = new Server(requestProcessor);

        httpClient = new DefaultHttpClient();
        uri = URIUtils.createURI("http", "localhost", server.getPort(), "/", null, null);
    }

    @After
    public void teardwon() {
        server.shutdown();
    }

    @Test
    public void simple() throws Exception {
        final HttpPost post = new HttpPost(uri);
        post.setEntity(new StringEntity(""));

        final HttpResponse response = httpClient.execute(post);
        String responseText = EntityUtils.toString(response.getEntity());

        assertTrue(responseText.startsWith("{"));
        assertTrue(responseText.endsWith("}"));
    }
}
