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
     *            the detail message
     */
    public NodeJavaException(final String message) {
        super(message);
    }

    /**
     * @param message
     *            the detail message
     * @param cause
     *            the cause
     */
    public NodeJavaException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
