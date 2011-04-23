/**
 * 
 */
package uk.co.pekim.nodejava.nodenotify;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;

import uk.co.pekim.nodejava.NodeJavaException;

/**
 * Test {@link NodeNotifier}
 * 
 * @author Mike D Pilsbury
 */
public class NodeNotifierTest {
    private BlockingQueue<String> notify;
    private int serverPort;
    private Connection connection;

    @Before
    public void createServer() throws Exception {
        notify = new LinkedBlockingQueue<String>();

        Server server = new Server(notify);
        connection = new SocketConnection(server);
        SocketAddress address = new InetSocketAddress(0);
        InetSocketAddress address2 = (InetSocketAddress) connection.connect(address);
        serverPort = address2.getPort();
    }

    @After
    public void closeServer() throws Exception {
        connection.close();
    }

    @Test
    public void testInitialisedMessage() throws Exception {
        NotifyInitialised initialisedMessage = new NotifyInitialised(123);
        NodeNotifier nodeNotifier = new NodeNotifier(serverPort);
        nodeNotifier.send(initialisedMessage);

        String received = notify.take();
        assertTrue(received.contains("initialised"));
        assertTrue(received.contains("port"));
        assertTrue(received.contains("123"));
    }

    private class Server implements Container {
        private final BlockingQueue<String> notify;

        private Server(BlockingQueue<String> notify) {
            this.notify = notify;
        }

        @Override
        public void handle(final Request request, final Response response) {
            try {
                notify.add(request.getContent());

                PrintStream body = response.getPrintStream();
                response.set("Content-Type", "text/plain");
                body.close();
            } catch (IOException exception) {
                throw new NodeJavaException("Failed to process request", exception);
            }
        }
    }
}
