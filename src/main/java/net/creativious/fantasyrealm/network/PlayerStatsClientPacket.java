package net.creativious.fantasyrealm.network;

import io.netty.buffer.Unpooled;
import net.creativious.fantasyrealm.levelingsystem.PlayerStatsManager;
import net.creativious.fantasyrealm.levelingsystem.interfaces.IPlayerStatsManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;

@Environment(EnvType.CLIENT)
public class PlayerStatsClientPacket {

    public static void init() {
        // Init function for PlayerStats Client Packet
        ClientPlayNetworking.registerGlobalReceiver(PlayerStatsServerPacket.LEVEL_PACKET, (client, handler, buf, sender) -> {
           if (client.player != null) {
               executeLevelPacket(client.player, buf);
           } else {
               PacketByteBuf newBuffer = new PacketByteBuf(Unpooled.buffer());
               newBuffer.writeInt(buf.readInt());
               client.execute(() -> {
                   executeLevelPacket(client.player, newBuffer);
               });
           }
        });
    }

    /**
     * Syncs playerStatsManager with the buffer sent by the server that has the information from the server side playerStatsManager
     *
     * @param player the player
     * @param buf    the buffer
     */
    public static void executeLevelPacket(PlayerEntity player, PacketByteBuf buf) {
        PlayerStatsManager playerStatsManager = ((IPlayerStatsManager) player).getPlayerStatsManager(player);
        /**
         * When working on networking, keep the order that you read the buffers the same as you write to them.
         * For stats just use playerStatsManager.<statName>.readbuffer(buf); // <statName>
         */
        playerStatsManager.setLevel(buf.readInt()); // Level
        playerStatsManager.setTotalLevelExperience(buf.readInt()); // Total Level Experience
        playerStatsManager.blacksmithingStat.readBuffer(buf); // Blacksmith buffer
        playerStatsManager.cookingStat.readBuffer(buf); // Cooking buffer
    }
}
