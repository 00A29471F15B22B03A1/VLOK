package core;

public class ClientMain {

    public static void main(String[] args) {
        NetworkClient client = new NetworkClient();
        client.connect("localhost");
        client.sendTCP("hehe");
    }

}
