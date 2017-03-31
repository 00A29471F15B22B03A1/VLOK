package core.packethandlers;

import com.esotericsoftware.kryonet.Connection;
import core.NetworkInterface;
import core.PacketHandler;
import core.UserManager;
import core.database.UserDatabase;
import core.packets.LoginPacket;
import core.packets.Packet;

public class LoginPacketHandler extends PacketHandler {

    public LoginPacketHandler() {
        super(LoginPacket.class);
    }

    @Override
    public void handlePacket(Packet p, Connection c, NetworkInterface ni) {
        LoginPacket packet = (LoginPacket) p;

        String[] keys = packet.fullKey.split("¡");

        String loginKey = keys[0];
        String loginCode = keys[1];

        LoginPacket response = new LoginPacket();

        if (UserDatabase.isValid(loginKey, loginCode))
            response.sessionKey = UserManager.newUser(loginKey, c.getID());

        ni.sendTCP(response, c.getID());
    }
}
