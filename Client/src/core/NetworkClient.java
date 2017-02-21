package core;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

public class NetworkClient {

    private static Client client;

    public NetworkClient() {
        client = new Client(60000, 60000);
        client.start();
        KryoUtil.registerClientClass(client);
    }

    public void addListener(Listener listener) {
        client.addListener(listener);
    }

    public List<InetAddress> discoverHosts() {
        return client.discoverHosts(54777, 1000);
    }

    public void sendTCP(Object o) {
        client.sendTCP(o);
    }

    public void sendUDP(Object o) {
        client.sendUDP(o);
    }

    public void connect(String address) {
        try {
            client.connect(5000, address, 54555, 54777);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect(InetAddress address) {
        try {
            client.connect(5000, address, 54555, 54777);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to connect to " + address);
        }
    }

    public void stop() {
        client.close();
        client.stop();
    }
}
