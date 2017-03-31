package core;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import core.localization.Localization;
import core.logging.Logger;

import javax.swing.*;
import java.io.IOException;

public class NetworkClient extends NetworkInterface {

    private Client client;
    private String address;

    public NetworkClient(String address) {
        client = new Client(600000, 600000);
        client.start();

        this.address = address;

        KryoUtil.registerClientClass(client);
    }

    @Override
    public void addListener(Listener listener) {
        client.addListener(listener);
    }

    @Override
    public void sendTCP(Object o) {
        client.sendTCP(o);
    }

    @Override
    public void sendUDP(Object o) {
        client.sendUDP(o);
    }

    @Override
    public void sendTCP(Object o, int connection) {
        sendTCP(o);
    }

    @Override
    public void sendUDP(Object o, int connection) {
        sendUDP(o);
    }

    @Override
    public void start() {
        try {
            client.connect(5000, address, 54555, 54777);
        } catch (IOException e) {
            e.printStackTrace();
            Logger.err("Failed to connect to " + address);
            JOptionPane.showMessageDialog(null, Localization.getString("error.connect_to_server"), Localization.getString("error.error"), JOptionPane.ERROR_MESSAGE);
        }
    }

    public void stop() {
        client.close();
        client.stop();
    }
}
