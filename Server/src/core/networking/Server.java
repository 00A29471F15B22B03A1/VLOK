package core.networking;

import core.PacketListener;
import core.logging.Console;
import core.serialization.VLOKDatabase;
import core.serialization.VLOKField;
import core.serialization.VLOKObject;
import core.serialization.VLOKString;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {

    private Map<Integer, ServerClient> connectedClients = new HashMap<>();

    private int port;

    private ServerSocket socket;

    private boolean running = false;

    private List<PacketListener> packetListeners = new ArrayList<>();

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to start server on port " + port);
            return;
        }

        running = true;

        new Thread(this::checkForNewConnections, "NewConnectionThread").start();
        new Thread(() -> {
            while (running) {
                send(new VLOKDatabase("keep_alive"));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "KeepAliveThread").start();
    }

    private void checkForNewConnections() {
        while (running) {
            try {
                Socket connection = socket.accept();
                ServerClient newClient = new ServerClient(connection, this);
                connectedClients.put(newClient.getId(), newClient);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void process(byte[] data, int id) {
        if (new String(data, 0, 6).equals("VLOKDB")) {
            VLOKDatabase db = VLOKDatabase.Deserialize(data);

            if (db == null) {
                Console.err("Couldn't deserialize database");
                return;
            }

            for (PacketListener packetListener : packetListeners)
                if (packetListener.getPacketName().equals(db.getName()))
                    packetListener.packetReceived(db, id, this);
        } else {
            System.err.println("Packet received is not a VLOK database");
        }
    }

    public void send(VLOKDatabase db) {
        for (Map.Entry<Integer, ServerClient> clients : connectedClients.entrySet())
            send(db, clients.getKey());
    }

    public void send(VLOKDatabase db, int id) {
        byte[] data = new byte[db.getSize()];
        db.getBytes(data, 0);

        try {
            ServerClient client = connectedClients.get(id);
            if (client != null)
                client.getOutputStream().write(data);
            else
                throw new IOException("ServerClient is null");
        } catch (IOException e) {
            removeClient(id);
        }
        System.out.println("Sent " + db.getName());
    }

    public void stop() {
        running = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeClient(int id) {
        ServerClient client = connectedClients.get(id);
        if (client != null)
            client.stop();
        connectedClients.remove(id);
        System.out.println("Client " + id + " disconnected");
    }

    private void dump(VLOKDatabase db) {
        System.out.println("------------------------------");
        System.out.println("           DATABASE           ");
        System.out.println("------------------------------");
        System.out.println("Name: " + db.getName());
        System.out.println("Size: " + db.getSize());
        System.out.println("Objects:");
        for (VLOKObject object : db.objects) {
            System.out.println("\tName: " + object.getName());
            System.out.println("\tFields:");
            for (VLOKField field : object.fields) {
                System.out.println("\t\tName: " + field.getName());
            }
            for (VLOKString string : object.strings) {
                System.out.println("\t\tName: " + string.getName());
                System.out.println("\t\tValue: " + string.getString());
            }
            System.out.println();
        }
        System.out.println("------------------------------");
    }

    public void addPacketListener(PacketListener packetListener) {
        packetListeners.add(packetListener);
    }

}
