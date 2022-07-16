package net.creativious.fantasyrealm.network;

import io.netty.buffer.Unpooled;
import net.creativious.fantasyrealm.levelingsystem.PlayerStatsManager;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class PlayerStatsServerPacket {

    public static final Identifier LEVEL_PACKET = new Identifier("fantasyrealm", "player_level");

    public static void init() {
        // Init function for PlayerStats Server Packet
    }

    public static void writeS2CLevelPacket(PlayerStatsManager playerStatsManager, ServerPlayerEntity serverPlayerEntity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(playerStatsManager.getLevel()); // Level
        buf.writeInt(playerStatsManager.getTotalLevelExperience()); // Total Level Experience

        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(LEVEL_PACKET, buf);
        serverPlayerEntity.networkHandler.sendPacket(packet);
    }
}
