/**
 * 
 */
package uk.co.pekim.nodejava.nodenotify;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

/**
 * Test {@link NodeNotifier}
 * 
 * @author Mike D Pilsbury
 */
public class NodeNotifierTest {
    private final class ThreadExtension extends Thread {
        private final ServerSocket serverSocket;
        private final BlockingQueue<String> notify;

        private ThreadExtension(final ServerSocket serverSocket, final BlockingQueue<String> notify) {
            this.serverSocket = serverSocket;
            this.notify = notify;
        }

        public void run() {
            Socket socket;
            try {
                socket = serverSocket.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                char[] buffer = new char[200];
                in.read(buffer);
                notify.add(new String(buffer));

                in.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test() throws Exception {
        final BlockingQueue<String> notify = new LinkedBlockingQueue<String>();
        final ServerSocket serverSocket = new ServerSocket(0);

        Thread thread = new ThreadExtension(serverSocket, notify);
        thread.start();

        NotifyInitialised initialisedMessage = new NotifyInitialised(123);
        new NodeNotifier(serverSocket.getLocalPort(), initialisedMessage);

        String received = notify.take();
        assertTrue(received.contains("initialised"));
        assertTrue(received.contains("port"));
        assertTrue(received.contains("123"));
    }
}
