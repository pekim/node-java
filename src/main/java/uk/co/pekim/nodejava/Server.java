/**
 * 
 */
package uk.co.pekim.nodejava;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.logging.Slf4JLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author Mike D Pilsbury
 */
public class Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    private static final int MIN_PORT = 1024;
    private static final int MAX_PORT = 0xFFFF;

    private final ChannelGroup allChannels = new DefaultChannelGroup("node-java-server");
    private final ChannelFactory channelFactory;

    private int port;

    /**
     * @param json
     *            Configuration
     */
    public Server(final String json) {
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());

        LOGGER.info("Starting");

        channelFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool());

        ServerBootstrap bootstrap = new ServerBootstrap(channelFactory);

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
                return Channels.pipeline(new DiscardHandler());
            }
        });

        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);

        bind(bootstrap);
        LOGGER.info("Listening on port " + port);
    }

    /**
     * 
     */
    public void shutdown() {
        LOGGER.info("Shutting down");

        ChannelGroupFuture future = allChannels.close();
        future.awaitUninterruptibly();
        channelFactory.releaseExternalResources();

        LOGGER.info("Shutdown");
    }

    private int bind(final ServerBootstrap bootstrap) {
        // The only reason we have to try to bind to various ports
        // is that if we bind to an ephemeral port, Netty doesn't
        // expose a means of finding out which port is used.

        boolean bound = false;
        while (!bound) {
            try {
                port = (int) ((Math.random() * (MAX_PORT + 1 - MIN_PORT)) + MIN_PORT);
                InetSocketAddress address = new InetSocketAddress(port);

                Channel channel = bootstrap.bind(address);
                allChannels.add(channel);

                bound = true;
            } catch (ChannelException exception) {
                LOGGER.debug("Port in use : " + port);
                port++;
            }
        }
        return port;
    }
}
