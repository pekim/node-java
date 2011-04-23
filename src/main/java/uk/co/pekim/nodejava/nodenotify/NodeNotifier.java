/**
 * 
 */
package uk.co.pekim.nodejava.nodenotify;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.pekim.nodejava.NodeJavaException;

/**
 * Notify the parent Node process of various events.
 * 
 * @author Mike D Pilsbury
 */
public class NodeNotifier {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeNotifier.class);

    private final ObjectMapper jsonMapper;
    private final URI nodeUri;
    private final HttpClient httpclient;

    /**
     * Create a Node notifier.
     * 
     * @param nodePort
     *            the port that a Node instance is listening on.
     */
    public NodeNotifier(final int nodePort) {
        try {
            jsonMapper = new ObjectMapper();
            nodeUri = URIUtils.createURI("http", "localhost", nodePort, "/", null, null);
            httpclient = new DefaultHttpClient();

            LOGGER.info("Node notification URI : " + nodeUri);
        } catch (URISyntaxException exception) {
            throw new NodeJavaException("Failed to construct URI for requests to the node server.", exception);
        }
    }

    /**
     * Send a message to the Node instance. The message is send as a JSON
     * representation of the message.
     * 
     * @param message
     *            the message to send.
     */
    public void send(final NotifyMessage message) {
        try {
            final String jsonString = jsonMapper.writeValueAsString(message);

            final HttpPost post = new HttpPost(nodeUri);
            post.setEntity(new StringEntity(jsonString));

            final HttpResponse response = httpclient.execute(post);

            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() != HttpStatus.SC_OK) {
                throw new NodeJavaException("Failed to send a message to the node server : " + statusLine);
            }
        } catch (ClientProtocolException exception) {
            throw new NodeJavaException("Failed to send a message to the node server.", exception);
        } catch (IOException exception) {
            throw new NodeJavaException("Failed to send a message to the node server.", exception);
        }
    }
}
