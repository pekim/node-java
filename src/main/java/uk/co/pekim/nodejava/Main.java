/**
 * 
 */
package uk.co.pekim.nodejava;

import org.simpleframework.http.core.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.pekim.nodejava.nodenotify.NodeNotifier;
import uk.co.pekim.nodejava.nodenotify.NotifyInitialised;
import uk.co.pekim.nodejava.server.HttpRequestHandler;
import uk.co.pekim.nodejava.server.HttpContainer;
import uk.co.pekim.nodejava.server.JsonRequestHandler;
import uk.co.pekim.nodejava.server.Server;

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

            Server server = createServer();

            NotifyInitialised initialisedMessage = new NotifyInitialised(server.getPort());
            NodeNotifier nodeNotifier = new NodeNotifier(configuration.getNodePort());
            nodeNotifier.send(initialisedMessage);
        } catch (NodeJavaException exception) {
            LOGGER.error("Fatal error", exception);
            System.exit(1);
        }
    }

    private static Server createServer() {
        final JsonRequestHandler jsonRequestHandler = new JsonRequestHandler();
        final HttpRequestHandler handler = new HttpRequestHandler(jsonRequestHandler);
        final Container requestProcessor = new HttpContainer(handler);
        final Server server = new Server(requestProcessor);

        return server;
    }
}
