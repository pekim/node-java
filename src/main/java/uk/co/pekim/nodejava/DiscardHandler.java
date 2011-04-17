/**
 * 
 */
package uk.co.pekim.nodejava;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
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
public class DiscardHandler extends SimpleChannelHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscardHandler.class);

    @Override
    public void messageReceived(ChannelHandlerContext context, MessageEvent event) {
        LOGGER.info(event.getMessage().toString());

        Channel channel = event.getChannel();
        channel.write(event.getMessage());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, ExceptionEvent event) {
        event.getCause().printStackTrace();

        Channel ch = event.getChannel();
        ch.close();
    }
}
