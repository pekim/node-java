/**
 * 
 */
package uk.co.pekim.nodejava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            String json = args[0];

            new Server(json);
        } catch (NodeJavaException exception) {
            LOGGER.error("Fatal error", exception);
            System.exit(1);
        }
    }
}
