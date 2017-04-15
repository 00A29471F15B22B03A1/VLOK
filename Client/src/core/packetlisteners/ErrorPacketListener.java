package core.packetlisteners;

import core.Client;
import core.localization.Localization;
import core.serialization.VLOKDatabase;
import core.ui.Popup;

public class ErrorPacketListener extends PacketListener {

    public ErrorPacketListener() {
        super("error");
    }

    @Override
    public void packetReceived(VLOKDatabase db, Client c) {
        String error = db.findObject("data").findString("message").getString();
        Popup.alert(Localization.get("error.error"), error);
    }

}
