/**
 * 
 */
package uk.co.pekim.nodejava.nodehandler.echo;

import uk.co.pekim.nodejava.nodehandler.NodeJavaHandler;

/**
 * Test handler, that echoes its request.
 * 
 * <p>
 * Useful for both unit and integration tests.
 * </p>
 * 
 * @author Mike D Pilsbury
 */
public class EchoHandler implements NodeJavaHandler<EchoRequest, EchoResponse> {
    @Override
    public Class<EchoRequest> getRequestClass() {
        return EchoRequest.class;
    }

    @Override
    public EchoResponse handle(final EchoRequest request) {
        EchoResponse response = new EchoResponse();

        response.setText(request.getText());
        response.setIncrementedNumber(request.getNumber() + 1);

        return response;
    }
}
