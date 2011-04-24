/**
 * 
 */
package uk.co.pekim.nodejava.nodehandler;

/**
 * Process a request from a Node instance.
 * 
 * <p>
 * Implementing classes must have a no-args constructor.
 * </p>
 * 
 * @author Mike D Pilsbury
 */
public interface NodeJavaHandler {
    /**
     * The class that the JSON request string should be marshalled in to an
     * instance of.
     * 
     * @return the reqyuest class.
     */
    Class<? extends NodeJavaRequest> getRequestClass();

    /**
     * Handle a request from a Node instance.
     * 
     * @param request
     *            the request from Node.
     * @return the response to Node.
     */
    NodeJavaResponse handle(NodeJavaRequest request);
}
