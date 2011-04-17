/**
 * 
 */
package uk.co.pekim.nodejava.channelhandler;

import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author Mike D Pilsbury
 */
public class JsonHandler extends SimpleChannelHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonHandler.class);

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    @Override
    public void writeRequested(final ChannelHandlerContext context, final MessageEvent event) throws Exception {
        final String jsonString = JSON_MAPPER.writeValueAsString(event.getMessage());
        LOGGER.info("Writing " + jsonString);

        Channels.write(context, event.getFuture(), jsonString);
    }
}
