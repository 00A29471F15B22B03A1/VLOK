package core;

import com.esotericsoftware.kryonet.Connection;
import core.packets.DocumentationPacket;

import java.util.HashMap;
import java.util.Map;

public class DocumentationLoader {

    private static final String documentationLocation = "Server/documentation/";

    private static Map<String, String> documentation = new HashMap<>();

    static {
        load("Uploading");
    }

    private static void load(String name) {
        String text = Utils.readFile(documentationLocation + name + ".txt");
        documentation.put(name, text);
    }

    public static void sendDocumentation(Connection connection) {
        for (Map.Entry<String, String> entry : documentation.entrySet()) {
            DocumentationPacket packet = new DocumentationPacket();
            packet.name = entry.getKey();
            packet.text = entry.getValue();
            connection.sendTCP(packet);
        }
    }

}
