package core.packethandlers;

import com.esotericsoftware.kryonet.Connection;
import core.NetworkInterface;
import core.PacketHandler;
import core.localization.Localization;
import core.packets.ErrorPacket;
import core.packets.Packet;
import core.ui.Popup;

public class ErrorPacketHandler extends PacketHandler {

    public ErrorPacketHandler() {
        super(ErrorPacket.class);
    }

    @Override
    public void handlePacket(Packet p, Connection c, NetworkInterface ni) {
        Popup.alert(Localization.get("error.error"), ((ErrorPacket) p).error);
    }
}
