package core;

import core.networking.Server;
import core.serialization.VLOKDatabase;
import core.serialization.VLOKObject;
import core.serialization.VLOKString;

import java.util.HashMap;
import java.util.Map;

public class DocumentationLoader {

    private static final String documentationLocation = "Server/documentation/";

    private static Map<String, String> documentation = new HashMap<>();

    static {
        load("Uploading");
        load("Chat");
    }

    private static void load(String name) {
        String text = Utils.readFile(documentationLocation + name + ".txt");
        documentation.put(name, text);
    }

    public static void sendDocumentation(Server server, int connection) {
        for (Map.Entry<String, String> entry : documentation.entrySet()) {
            VLOKDatabase db = new VLOKDatabase("documentation");
            VLOKObject data = new VLOKObject("data");
            data.addString(VLOKString.Create("name", entry.getKey()));
            data.addString(VLOKString.Create("text", entry.getValue()));
            db.addObject(data);
            server.send(db, connection);
        }
    }

}
