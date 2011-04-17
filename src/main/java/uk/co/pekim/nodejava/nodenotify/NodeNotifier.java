/**
 * 
 */
package uk.co.pekim.nodejava.nodenotify;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.pekim.nodejava.channelhandler.JsonHandler;
import uk.co.pekim.nodejava.channelhandler.NetstringHandler;

/**
 * Notify the parent Node process of various events.
 * 
 * @author Mike D Pilsbury
 */
public class NodeNotifier {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeNotifier.class);

    private Channel channel;

    /**
     * Create a Node notifier.
     * 
     * @param nodePort
     *            the port that a Node instance is listening on.
     * @param firstMessage
     *            a message to send to node.
     */
    public NodeNotifier(final int nodePort, final NotifyMessage firstMessage) {
        ChannelFactory factory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool());

        ClientBootstrap bootstrap = new ClientBootstrap(factory);

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
                return Channels.pipeline(new NetstringHandler(), new JsonHandler());
            }
        });

        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("keepAlive", true);

        ChannelFuture future = bootstrap.connect(new InetSocketAddress(nodePort));
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future) throws Exception {
                channel = future.getChannel();
                LOGGER.info("Notifier initialised");

                send(firstMessage);
            }
        });
    }

    /**
     * Send a message to the Node instance. The message is send as a JSON
     * representation of the message, wrapped in a netstring.
     * 
     * @param message
     *            the message to send.
     */
    public void send(final NotifyMessage message) {
        channel.write(message);
    }
}
