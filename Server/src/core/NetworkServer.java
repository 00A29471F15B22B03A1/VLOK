package core;

import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class NetworkServer {

    private Server server;

    public NetworkServer() {
        server = new Server(60000, 60000);
        server.start();
        KryoUtil.registerServerClass(server);
    }

    public void addListener(Listener listener) {
        server.addListener(listener);
    }

    public void sendTCP(int i, Object o) {
        server.sendToTCP(i, o);
    }

    public void sendUDP(int i, Object o) {
        server.sendToUDP(i, o);
    }

    public void sendToAllTCP(Object o) {
        server.sendToAllTCP(o);
    }

    public void sendToAllUDP(Object o) {
        server.sendToAllUDP(o);
    }

    public void sendToAllExceptTCP(int i, Object o) {
        server.sendToAllExceptTCP(i, o);
    }

    public void sendToAllExceptUDP(int i, Object o) {
        server.sendToAllExceptUDP(i, o);
    }

    public void start() {
        try {
            server.bind(KryoUtil.TCP_PORT, KryoUtil.UDP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to start server");
        }
    }

    public void stop() {
        server.close();
        server.stop();
    }

}