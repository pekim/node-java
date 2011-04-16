/**
 * 
 */
package uk.co.pekim.nodejava;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

/**
 * 
 * 
 * @author Mike D Pilsbury
 */
public class DiscardHandler extends SimpleChannelHandler {
    @Override
    public void messageReceived(ChannelHandlerContext context, MessageEvent event) {
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
