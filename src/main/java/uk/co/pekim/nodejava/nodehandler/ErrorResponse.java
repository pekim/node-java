/**
 * 
 */
package uk.co.pekim.nodejava.nodehandler;

/**
 * An response representing an unexpected error.
 * 
 * @author Mike D Pilsbury
 */
public class ErrorResponse implements NodeJavaResponse {
    private final String error;
    private final String message;

    /**
     * Create an error response.
     * 
     * @param error
     *            the error.
     * @param message
     *            a more descriptive version of error.
     */
    public ErrorResponse(final String error, final String message) {
        this.error = error;
        this.message = message;

    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
