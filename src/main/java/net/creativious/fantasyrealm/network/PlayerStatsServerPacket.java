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

    /**
     * Write server to client packets for the levelingSystem.
     *
     * @param playerStatsManager the player stats manager
     * @param serverPlayerEntity the server player entity
     */
    public static void writeS2CLevelPacket(PlayerStatsManager playerStatsManager, ServerPlayerEntity serverPlayerEntity) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        /**
         * When working on networking, keep the order that you write to the buffers the same as you read them.
         * For stats just use playerStatsManager.<statName>.writebuffer(buf); // <statName>
         */
        buf.writeInt(playerStatsManager.getLevel()); // Level buffer
        buf.writeInt(playerStatsManager.getTotalLevelExperience()); // Total Level Experience buffer
        playerStatsManager.blacksmithingStat.writeBuffer(buf); // Blacksmith buffer
        playerStatsManager.cookingStat.writeBuffer(buf); // Cooking buffer

        CustomPayloadS2CPacket packet = new CustomPayloadS2CPacket(LEVEL_PACKET, buf); // Creating packet with buffer
        serverPlayerEntity.networkHandler.sendPacket(packet); // Sending packet to client
    }
}
