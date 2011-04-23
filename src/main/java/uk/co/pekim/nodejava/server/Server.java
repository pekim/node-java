/**
 * 
 */
package uk.co.pekim.nodejava.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.simpleframework.http.core.Container;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.pekim.nodejava.NodeJavaException;

/**
 * Http server that processes requests from a Node instance.
 * 
 * @author Mike D Pilsbury
 */
public class Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private InetSocketAddress address;
    private Connection connection;

    /**
     * Construct a server.
     * 
     * @param requestProcessor
     *            the container that will process all requests.
     */
    public Server(final Container requestProcessor) {
        try {
            connection = new SocketConnection(requestProcessor);
            SocketAddress address = new InetSocketAddress(0);

            this.address = (InetSocketAddress) connection.connect(address);

            LOGGER.info("Server started on port " + getPort());
        } catch (IOException exception) {
            throw new NodeJavaException("Failed to create server", exception);
        }
    }

    /**
     * The port that the server is listening on.
     * 
     * @return the port.
     */
    public int getPort() {
        return address.getPort();
    }

    /**
     * Shutdown the server.
     */
    public void shutdown() {
        try {
            connection.close();

            LOGGER.info("Server shutdown");
        } catch (IOException exception) {
            throw new NodeJavaException("Failed to shutdown server", exception);
        }
    }
}
