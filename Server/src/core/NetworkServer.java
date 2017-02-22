package core;

import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class NetworkServer {

    private Server server;

    /**
     * Creates a new server instance and registers serializable classes
     */
    public NetworkServer() {
        server = new Server(60000, 60000);
        server.start();
        KryoUtil.registerServerClass(server);
    }

    /**
     * Adds a listener to the server
     *
     * @param listener to add to server
     */
    public void addListener(Listener listener) {
        server.addListener(listener);
    }

    /**
     * Sends a TCP message to a certain client
     *
     * @param to     id of client
     * @param object to send
     */
    public void sendTCP(int to, Object object) {
        server.sendToTCP(to, object);
    }

    /**
     * Sends an UDP message to a certain client
     *
     * @param to     id of client
     * @param object to send
     */
    public void sendUDP(int to, Object object) {
        server.sendToUDP(to, object);
    }

    /**
     * Sends a TCP message to all connected clients
     *
     * @param object to send
     */
    public void sendToAllTCP(Object object) {
        server.sendToAllTCP(object);
    }

    /**
     * Sends an UDP message to all connected clients
     *
     * @param object to send
     */
    public void sendToAllUDP(Object object) {
        server.sendToAllUDP(object);
    }

    /**
     * Sends a TCP message to all clients except one
     *
     * @param client to exclude
     * @param object to send
     */
    public void sendToAllExceptTCP(int client, Object object) {
        server.sendToAllExceptTCP(client, object);
    }

    /**
     * Sends an UDP message to all clients except one
     *
     * @param client to exclude
     * @param object to send
     */
    public void sendToAllExceptUDP(int client, Object object) {
        server.sendToAllExceptUDP(client, object);
    }

    /**
     * Starts the server
     */
    public void start() {
        try {
            server.bind(KryoUtil.TCP_PORT, KryoUtil.UDP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to start server");
        }
    }

    /**
     * Stops the server
     */
    public void stop() {
        server.close();
        server.stop();
    }

}