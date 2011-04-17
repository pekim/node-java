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
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                throw new NodeJavaException("Expected 1 argument, a JSON string");
            }
            String json = args[0];

            Server server = new Server(json);
            server.run();
        } catch (NodeJavaException exception) {
            LOGGER.error("Fatal error", exception);
            System.exit(1);
        }
    }
}
