package core.networking;

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

    private boolean running;

    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public ServerClient(Socket socket, Server server) {
        this.socket = socket;
        this.id = getNewClientID();
        this.server = server;
        try {
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        running = true;
        new Thread(this::listen, "ListenThread").start();
    }


    private void listen() {
        while (running) {
            try {
                try {
                    inputStream.read(receivedDataBuffer);
                } catch (SocketException e) {
                    break;
                }

                server.process(receivedDataBuffer, id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        stop();
    }

    public void stop() {
        try {
            running = false;
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
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
