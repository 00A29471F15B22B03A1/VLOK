package core;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import core.packets.FileInfoPacket;
import core.packets.FileStoragePacket;
import core.packets.FileUploadPacket;
import core.packets.RequestPacket;

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for all KryoNet utilities
 */
public class KryoUtil {

    public static final int TCP_PORT = 54555;   //TCP port for kryonet
    public static final int UDP_PORT = 54777;   //UDP port for kryonet

    private static byte[] key = generateKey();

    /**
     * Generates a new blowfish key
     *
     * @return key as byte[]
     */
    private static byte[] generateKey() {
        byte[] key = null;
        try {
            key = KeyGenerator.getInstance("Blowfish").generateKey().getEncoded();
            System.out.println("Successfully generated key");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return key;
    }

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
        kryo.register(String.class);
        kryo.register(List.class);
        kryo.register(ArrayList.class);
        kryo.register(byte[].class);

        kryo.register(FileInfo.class);
        kryo.register(FileStorage.class);

        kryo.register(RequestPacket.class);
        kryo.register(RequestPacket.Type.class);
        kryo.register(FileInfoPacket.class);
        kryo.register(FileStoragePacket.class);
        kryo.register(FileUploadPacket.class);

        System.out.println("Registered serialization classes");
    }

}
