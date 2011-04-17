/**
 * 
 */
package uk.co.pekim.nodejava.channelhandler;

import static org.jboss.netty.buffer.ChannelBuffers.buffer;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author Mike D Pilsbury
 */
public class NetstringEncoder extends SimpleChannelHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetstringEncoder.class);

    @Override
    public void writeRequested(final ChannelHandlerContext context, final MessageEvent event) throws Exception {
        final String jsonString = (String) event.getMessage();
        final String netstring = Netstring.build(jsonString);
        LOGGER.debug("Writing " + netstring);

        final byte[] netstringBytes = netstring.getBytes(Netstring.CHARSET);
        ChannelBuffer buffer = buffer(netstringBytes.length);
        buffer.writeBytes(netstringBytes);

        Channels.write(context, event.getFuture(), buffer);
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext context, final ExceptionEvent event) {
        LOGGER.error("Failure", event.getCause());

        Channel channel = event.getChannel();
        channel.close();
    }
}
