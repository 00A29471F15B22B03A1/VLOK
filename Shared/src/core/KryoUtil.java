package core;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import core.logging.Logger;
import core.packets.*;

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
     * Generates a new blowfish fullKey
     *
     * @return fullKey as byte[]
     */
    private static byte[] generateKey() {
        byte[] key = null;
        try {
            key = KeyGenerator.getInstance("Blowfish").generateKey().getEncoded();
            Logger.info("Successfully generated fullKey");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Logger.err("Failed to generate encryption fullKey");
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
        kryo.register(FileStructure.class);

        kryo.register(RequestPacket.class);
        kryo.register(RequestPacket.Type.class);
        kryo.register(FileStructurePacket.class);
        kryo.register(FileTransferPacket.class);
        kryo.register(LoginPacket.class);
        kryo.register(ErrorPacket.class);

        Logger.info("Registered serialization classes");
    }

}
