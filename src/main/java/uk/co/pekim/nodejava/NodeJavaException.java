/**
 * 
 */
package uk.co.pekim.nodejava;

/**
 * A problem somewhere in the Node/Java bridge.
 * 
 * @author Mike D Pilsbury
 * 
 */
public class NodeJavaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message
     */
    public NodeJavaException(String message) {
        super(message);
    }

    /**
     * @param string
     * @param cause
     */
    public NodeJavaException(String message, Throwable cause) {
        super(message, cause);
    }
}
