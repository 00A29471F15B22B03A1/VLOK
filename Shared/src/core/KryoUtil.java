package core;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import core.packets.FileCompletedPacket;
import core.packets.FileUploadPacket;

/**
 * Class for all KryoNet utilities
 */
public class KryoUtil {

    public static final int TCP_PORT = 54555;   //TCP port for kryonet
    public static final int UDP_PORT = 54777;   //UDP port for kryonet

    /**
     * Registers serializable classes for kryonet server
     *
     * @param server to register classes for
     */
    public static void registerServerClass(Server server) {
        register(server.getKryo());
    }

    /**
     * Registers serializable classes for kryonet client
     *
     * @param client to register classes for
     */
    public static void registerClientClass(Client client) {
        register(client.getKryo());
    }


    /**
     * Generic function for registering serializable classes
     *
     * @param kryo instance to use for registration
     */
    private static void register(Kryo kryo) {
        kryo.register(int.class);
        kryo.register(boolean.class);
        kryo.register(String.class);
        kryo.register(String[].class);
        kryo.register(byte[].class);

        kryo.register(FileUploadPacket.class);
        kryo.register(FileCompletedPacket.class);
    }

}
