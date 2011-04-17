/**
 * 
 */
package uk.co.pekim.nodejava.channelhandler;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.pekim.nodejava.NodeJavaException;

/**
 * 
 * 
 * @author Mike D Pilsbury
 */
public class NetstringDecoder extends FrameDecoder {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetstringDecoder.class);

    @Override
    protected Object decode(final ChannelHandlerContext context, final Channel channel, final ChannelBuffer buffer)
            throws Exception {
        LOGGER.info("Readable bytes : " + buffer.readableBytes());
        buffer.markReaderIndex();

        while (buffer.readable()) {
            if (buffer.readByte() == ':') {
                break;
            }
        }

        // Must be at least a ',' (comma) left, even if we found the ':'.
        if (!buffer.readable()) {
            buffer.resetReaderIndex();
            return null;
        }

        // Extract string length.
        byte[] lengthBytes = new byte[buffer.readerIndex() - 1];
        buffer.getBytes(0, lengthBytes);
        String lengthString = new String(lengthBytes, Netstring.CHARSET);
        int length;
        try {
            length = Integer.parseInt(lengthString.toString());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Cannot parse netstring length from '" + lengthString + "'", exception);
        }
        LOGGER.info("Length : " + length);

        if (buffer.readableBytes() < length + 1) {
            buffer.resetReaderIndex();
            return null;
        }

        // Extract string.
        byte[] stringBytes = new byte[length];
        buffer.readBytes(stringBytes);
        String string = new String(stringBytes, Netstring.CHARSET);
        LOGGER.info("String : " + string);

        // Consume ','.
        if (buffer.readByte() != ',') {
            throw new NodeJavaException("Expected ',' to terminate netstring \"" + string + "\"");
        }

        return string;
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext context, final ExceptionEvent event) {
        LOGGER.error("Failure", event.getCause());

        Channel channel = event.getChannel();
        channel.close();
    }
}