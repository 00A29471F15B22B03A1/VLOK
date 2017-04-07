package core;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import core.localization.Localization;
import core.logging.Console;
import core.ui.Popup;

import java.io.IOException;

public class NetworkClient extends NetworkInterface {

    private Client client;
    private String address;

    public NetworkClient(String address) {
        client = new Client(600000, 600000);
        client.start();

        this.address = address;

        KryoUtil.registerClientClass(client);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object o) {
                handlePacket(connection, o);
            }
        });
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
            Console.err("Failed to connect to " + address);
            Popup.alert(Localization.get("error.error"), Localization.get("error.connect_to_server"));
        }
    }

    public void stop() {
        client.close();
        client.stop();
    }
}
