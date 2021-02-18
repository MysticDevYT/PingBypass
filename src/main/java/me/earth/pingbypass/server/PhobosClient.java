package me.earth.pingbypass.server;

import com.github.steveice10.mc.protocol.data.handshake.HandshakeIntent;
import com.github.steveice10.mc.protocol.packet.handshake.client.HandshakePacket;
import me.earth.earthhack.api.util.Globals;
import me.earth.pingbypass.PingBypass;
import me.earth.pingbypass.server.managers.CPacketManager;
import me.earth.pingbypass.server.managers.PayloadManager;
import me.earth.pingbypass.server.managers.util.PayloadUtil;
import me.earth.pingbypass.util.wrappers.CPacketWrapper;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;

public class PhobosClient implements Globals
{
    private final CPacketManager cPacketManager;
    private final PayloadManager payloadManager;
    private HandshakePacket packet;

    public PhobosClient()
    {
        payloadManager = new PayloadManager();
        cPacketManager = new CPacketManager(this);
    }

    public void prepareConnection(HandshakePacket packet)
    {
        if (packet.getIntent() == HandshakeIntent.LOGIN)
        {
            ServerAddress address = ServerAddress.fromString(packet.getHostName());
            this.packet = new HandshakePacket(packet.getProtocolVersion(), address.getIP(), packet.getPort(), packet.getIntent());
        }
    }

    public void connect()
    {
        if (packet != null)
        {
            mc.addScheduledTask(() ->
                    mc.displayGuiScreen(new GuiConnecting(mc.currentScreen != null ? mc.currentScreen : new GuiMainMenu(), mc, packet.getHostName(), packet.getPort())));
        }
    }

    public void sendToServer(com.github.steveice10.packetlib.packet.Packet packet)
    {
        CPacketWrapper wrapper = new CPacketWrapper(packet);
        sendToServer(wrapper);
    }

    public void sendToServer(Packet<?> packet)
    {
        NetHandlerPlayClient connection = mc.getConnection();
        if (connection != null)
        {
            connection.sendPacket(packet);
        }
    }

    protected CPacketManager getPacketManager()
    {
        return cPacketManager;
    }

    public PayloadManager getPayloadManager()
    {
        return payloadManager;
    }

}
