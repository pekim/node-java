/**
 * 
 */
package uk.co.pekim.nodejava.channelhandler;

import static org.jboss.netty.buffer.ChannelBuffers.buffer;

import org.jboss.netty.buffer.ChannelBuffer;
import org.junit.Test;

/**
 * 
 * 
 * @author Mike D Pilsbury
 */
public class NetstringDecoderTest {
    @Test
    public void test() throws Exception {
        byte[] netstring = "3:qaz,".getBytes();

        NetstringDecoder decoder = new NetstringDecoder();
        ChannelBuffer buffer = buffer(netstring.length);
        buffer.writeBytes(netstring);

        Object string = decoder.decode(null, null, buffer);
        System.out.println(string);
    }
}
