package core.packethandlers;

import core.PacketListener;
import core.Server;
import core.ServerMain;
import core.UserManager;
import core.database.UserDatabase;
import core.serialization.VLOKDatabase;
import core.serialization.VLOKObject;
import core.serialization.VLOKString;

public class LoginPacketListener extends PacketListener {

    public LoginPacketListener() {
        super("login");
    }

    @Override
    public void packetReceived(VLOKDatabase db, int c, Server server) {
        VLOKObject data = db.findObject("data");

        String loginKey = data.findString("key").getString();
        String loginCode = data.findString("code").getString();

        if (UserDatabase.isValid(loginKey, loginCode)) {
            VLOKDatabase response = new VLOKDatabase("login");
            VLOKObject responseData = new VLOKObject("data");
            responseData.addString(VLOKString.Create("session", UserManager.newUser(loginKey, c)));
            response.addObject(responseData);
            server.send(response, c);

            float version = data.findField("version").getFloat();

            sendUpdateIfNecessary(version, c, server);
        } else {
            VLOKDatabase error = new VLOKDatabase("error");
            VLOKObject errorData = new VLOKObject("data");
            //TODO: add serialization
            errorData.addString(VLOKString.Create("message", "Wrong username or password"));
            error.addObject(errorData);
            server.send(error, c);
        }
    }

    private void sendUpdateIfNecessary(float version, int c, Server server) {
        if (version == ServerMain.NEWEST_VERSION)
            return;

        VLOKDatabase db = new VLOKDatabase("update");
        VLOKObject data = new VLOKObject("data");
        data.addString(VLOKString.Create("url", ServerMain.DOWNLOAD_URL));
        db.addObject(data);

        server.send(db, c);
    }
}
