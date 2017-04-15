package core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class ServerClient {

    private final int MAX_PACKET_SIZE = 65536;
    private byte[] receivedDataBuffer = new byte[MAX_PACKET_SIZE * 10];

    private int id;

    private Server server;

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public ServerClient(Socket socket, Server server) {
        this.id = getNewClientID();
        this.socket = socket;
        this.server = server;
        try {
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(this::listen, "ListenThread").start();
    }


    private void listen() {
        while (true) {
            try {
                try {
                    inputStream.read(receivedDataBuffer);
                } catch (SocketException e) {
                    server.removeClient(id);
                    break;
                }

                server.process(receivedDataBuffer, id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getId() {
        return id;
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    private static int currentClientID;

    private static int getNewClientID() {
        return currentClientID++;
    }
}
