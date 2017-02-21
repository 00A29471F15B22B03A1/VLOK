package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ServerMain {

    public static void main(String[] args) {
        NetworkServer server = new NetworkServer();

        server.start();

        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                System.out.println(connection.getID() + " connected");
            }

            @Override
            public void received(Connection connection, Object o) {
                System.out.println("received");
            }
        });
    }

}
