package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import core.logging.Console;

import java.io.IOException;

public class NetworkServer extends NetworkInterface {

    private Server server;

    public NetworkServer() {
        server = new Server(60000000, 60000000);
        server.start();

        KryoUtil.registerServerClass(server);

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object o) {
                handlePacket(connection, o);
            }
        });
    }

    @Override
    public void addListener(Listener listener) {
        server.addListener(listener);
    }

    @Override
    public void sendTCP(Object o) {
        server.sendToAllTCP(o);
    }

    @Override
    public void sendUDP(Object o) {
        server.sendToAllUDP(o);
    }

    @Override
    public void sendTCP(Object o, int connection) {
        server.sendToTCP(connection, o);
    }

    @Override
    public void sendUDP(Object o, int connection) {
        server.sendToUDP(connection, o);
    }

    @Override
    public void start() {
        try {
            server.bind(KryoUtil.TCP_PORT, KryoUtil.UDP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            Console.err("Failed to start server");
        }
    }

    @Override
    public void stop() {
        server.close();
        server.stop();
    }

}
