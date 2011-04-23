/**
 * 
 */
package uk.co.pekim.nodejava;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.pekim.nodejava.nodenotify.NodeNotifier;
import uk.co.pekim.nodejava.nodenotify.NotifyInitialised;

/**
 * 
 * 
 * @author Mike D Pilsbury
 */
public final class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private Main() {
    }

    /**
     * Start server.
     * 
     * @param args
     *            arguments.
     */
    public static void main(final String[] args) {
        try {
            if (args.length < 1) {
                throw new NodeJavaException("Expected 1 argument, a JSON string");
            }
            String configurationJson = args[0];
            Configuration configuration = Configuration.parseJson(configurationJson);

            InetSocketAddress address2;
            try {
                Server server = new Server();
                Connection connection = new SocketConnection(server);
                SocketAddress address = new InetSocketAddress(0);

                address2 = (InetSocketAddress) connection.connect(address);
                LOGGER.info("Port : " + address2.getPort());
            } catch (IOException exception) {
                throw new NodeJavaException("Failed to create server", exception);
            }

            NotifyInitialised initialisedMessage = new NotifyInitialised(address2.getPort());
            NodeNotifier nodeNotifier = new NodeNotifier(configuration.getNodePort());
            nodeNotifier.send(initialisedMessage);
        } catch (NodeJavaException exception) {
            LOGGER.error("Fatal error", exception);
            System.exit(1);
        }
    }
}
