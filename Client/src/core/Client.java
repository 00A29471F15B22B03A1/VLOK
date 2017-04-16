package core;

import core.logging.Console;
import core.packetlisteners.PacketListener;
import core.serialization.VLOKDatabase;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Client {

    private int port;
    private String ipAddress;

    private Socket socket;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private boolean running;

    private final int MAX_PACKET_SIZE = 65536;
    private byte[] receivedDataBuffer = new byte[MAX_PACKET_SIZE * 10];

    private List<PacketListener> packetListeners = new ArrayList<>();

    private List<VLOKDatabase> DBsToProcess = new ArrayList<>();

    public Client(String host, int port) {
        this.ipAddress = host;
        this.port = port;
    }

    public void start() {
        try {
            socket = new Socket(ipAddress, port);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Failed to connect to server");
            System.exit(-1);
            return;
        }

        running = true;
        new Thread(this::listen, "ListenThread").start();

        new Thread(() -> {
            while (running) {
                if (DBsToProcess.isEmpty()) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }

                process(DBsToProcess.get(0));
                DBsToProcess.remove(0);
            }
        }, "ProcessThread").start();
    }

    private void listen() {
        while (running) {
            try {
                inputStream.read(receivedDataBuffer);

                if (new String(receivedDataBuffer, 0, 6).equals("VLOKDB"))
                    DBsToProcess.add(VLOKDatabase.Deserialize(receivedDataBuffer));
            } catch (IOException e) {
                stop();
            }
        }
    }

    private void process(VLOKDatabase db) {
        Console.info("Received " + db.getName());

        for (PacketListener packetListener : packetListeners)
            if (packetListener.getPacketName().equals(db.getName()))
                packetListener.packetReceived(db, this);
    }

    public void send(VLOKDatabase db) {
        if (socket == null || !socket.isConnected()) {
            Console.err("Socket is not connected");
            return;
        }

        byte[] data = new byte[db.getSize()];
        db.getBytes(data, 0);

        try {
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            running = false;
            socket.close();
            Console.info("Stopped Client");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addPacketListener(PacketListener packetListener) {
        packetListeners.add(packetListener);
    }
}
