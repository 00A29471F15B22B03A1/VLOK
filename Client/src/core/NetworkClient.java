package core;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

public class NetworkClient {

    private static Client client;

    /**
     * Creates a new client instance and registers serializable classes
     */
    public NetworkClient() {
        client = new Client(600000, 600000);
        client.start();

        KryoUtil.registerClientClass(client);
    }

    /**
     * Adds a listener to the client
     *
     * @param listener to add to client
     */
    public void addListener(Listener listener) {
        client.addListener(listener);
    }

    /**
     * Discovers all servers on the local network
     *
     * @return List of InetAddresses
     */
    public List<InetAddress> discoverHosts() {
        return client.discoverHosts(54777, 1000);
    }

    /**
     * Sends a TCP message to the server
     *
     * @param object to send
     */
    public void sendTCP(Object object) {
        client.sendTCP(object);
    }

    /**
     * Sends an UDP message to the server
     *
     * @param object to send
     */
    public void sendUDP(Object object) {
        client.sendUDP(object);
    }

    /**
     * Connects to a server using a String as the address
     *
     * @param address to connect to
     */
    public void connect(String address) {
        try {
            client.connect(5000, address, 54555, 54777);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to connect to " + address);
        }
    }

    /**
     * Connects to a server using an InetAddress as the address
     *
     * @param address to connect to
     */
    public void connect(InetAddress address) {
        try {
            client.connect(5000, address, 54555, 54777);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to connect to " + address);
        }
    }

    /**
     * Stops the client
     */
    public void stop() {
        client.close();
        client.stop();
    }
}
