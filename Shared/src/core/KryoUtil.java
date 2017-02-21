package core;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;

public class KryoUtil {

    public static final int TCP_PORT = 54555;
    public static final int UDP_PORT = 54777;

    public static void registerServerClass(Server server) {
        register(server.getKryo());
    }

    public static void registerClientClass(Client client) {
        register(client.getKryo());
    }

    private static void register(Kryo kryo) {
        kryo.register(int.class);
        kryo.register(boolean.class);
        kryo.register(String.class);
        kryo.register(String[].class);

        kryo.register(ConnectionRequestPacket.class);
        kryo.register(ConnectionResponsePacket.class);

    }

}
