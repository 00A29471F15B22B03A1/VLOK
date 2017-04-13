package core.packethandlers;

import com.esotericsoftware.kryonet.Connection;
import core.NetworkInterface;
import core.PacketHandler;
import core.ServerMain;
import core.UserManager;
import core.database.UserDatabase;
import core.packets.ErrorPacket;
import core.packets.LoginPacket;
import core.packets.Packet;
import core.packets.UpdatePacket;

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

        if (UserDatabase.isValid(loginKey, loginCode)) {
            response.sessionKey = UserManager.newUser(loginKey, c.getID());
            ni.sendTCP(response, c.getID());

            sendUpdateIfNecessary(packet.version, ni, c);
        } else {
            ErrorPacket wrongLogin = new ErrorPacket("Failed to login");
            ni.sendTCP(wrongLogin, c.getID());
        }
    }

    private void sendUpdateIfNecessary(float version, NetworkInterface ni, Connection c) {
        if (version == ServerMain.NEWEST_VERSION)
            return;

        UpdatePacket updatePacket = new UpdatePacket(ServerMain.DOWNLOAD_URL);
        ni.sendTCP(updatePacket, c.getID());
    }
}
