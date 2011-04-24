/**
 * 
 */
package uk.co.pekim.nodejava.nodehandler.echo;

import uk.co.pekim.nodejava.nodehandler.NodeJavaHandler;
import uk.co.pekim.nodejava.nodehandler.NodeJavaRequest;
import uk.co.pekim.nodejava.nodehandler.NodeJavaResponse;

/**
 * Test handler, that echoes its request.
 * 
 * <p>
 * Useful for both unit and integration tests.
 * </p>
 * 
 * @author Mike D Pilsbury
 */
public class EchoHandler implements NodeJavaHandler {
    @Override
    public Class<? extends NodeJavaRequest> getRequestClass() {
        return EchoRequest.class;
    }

    @Override
    public NodeJavaResponse handle(final NodeJavaRequest nodeJavaRequest) {
        EchoRequest request = (EchoRequest) nodeJavaRequest;
        EchoResponse response = new EchoResponse();

        response.setText(request.getText());
        response.setIncrementedNumber(request.getNumber() + 1);

        return response;
    }
}
