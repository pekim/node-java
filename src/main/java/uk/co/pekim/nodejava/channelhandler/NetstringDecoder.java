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
        LOGGER.debug("Readable bytes : " + buffer.readableBytes());
        buffer.markReaderIndex();

        while (buffer.readable()) {
            if (buffer.readByte() == Netstring.LENGTH_DELIMITER) {
                break;
            }
        }

        // Must be at least a ',' (comma) left, even if we found the ':'.
        if (!buffer.readable()) {
            buffer.resetReaderIndex();
            return null;
        }

        int length = extractStringLength(buffer);

        if (buffer.readableBytes() < length + 1) {
            buffer.resetReaderIndex();
            return null;
        }

        String string = extractString(buffer, length);
        consumeComma(buffer, string);

        return string;
    }

    private void consumeComma(final ChannelBuffer buffer, final String string) {
        if (buffer.readByte() != Netstring.STRING_DELIMITER) {
            throw new NodeJavaException("Expected ',' to terminate netstring \"" + string + "\"");
        }
    }

    private String extractString(final ChannelBuffer buffer, final int length) {
        byte[] stringBytes = new byte[length];
        buffer.readBytes(stringBytes);
        String string = new String(stringBytes, Netstring.CHARSET);

        LOGGER.debug("String : " + string);

        return string;
    }

    private int extractStringLength(final ChannelBuffer buffer) {
        byte[] lengthBytes = new byte[buffer.readerIndex() - 1];
        buffer.getBytes(0, lengthBytes);
        String lengthString = new String(lengthBytes, Netstring.CHARSET);

        int length;
        try {
            length = Integer.parseInt(lengthString.toString());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Cannot parse netstring length from '" + lengthString + "'", exception);
        }

        LOGGER.debug("Length : " + length);

        return length;
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext context, final ExceptionEvent event) {
        LOGGER.error("Failure", event.getCause());

        Channel channel = event.getChannel();
        channel.close();
    }
}
