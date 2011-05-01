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
 * @param <REQUEST>
 *            the request class.
 * @param <RESPONSE>
 *            the reponse class.
 * 
 * @author Mike D Pilsbury
 */
public interface NodeJavaHandler<REQUEST extends NodeJavaRequest, RESPONSE extends NodeJavaResponse> {
    /**
     * The class that the JSON request string should be marshalled in to an
     * instance of.
     * 
     * @return the reqyuest class.
     */
    Class<REQUEST> getRequestClass();

    /**
     * Handle a request from a Node instance.
     * 
     * @param request
     *            the request from Node.
     * @return the response to Node.
     */
    RESPONSE handle(REQUEST request);
}
