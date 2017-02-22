package core;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientMain {

    public static final char SPLIT_CHAR = ' ';
    public static final String UNIQUE_KEY = "3K5865Y7431S4";
    public static final String PERMISSION_LVL = "5H873E6";

    public static void main(String[] args) {
        NetworkClient client = new NetworkClient();
        client.connect("localhost");

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object o) {
                System.out.println("Received");

                if (o instanceof ConnectionResponsePacket) {
                    System.out.println("Login: " + ((ConnectionResponsePacket) o).connectionSuccesfull);
                }
            }
        });

        ConnectionRequestPacket connectionRequestPacket = new ConnectionRequestPacket();
        connectionRequestPacket.fullKey = UNIQUE_KEY + SPLIT_CHAR + PERMISSION_LVL;

        client.sendTCP(connectionRequestPacket);

    }

}
