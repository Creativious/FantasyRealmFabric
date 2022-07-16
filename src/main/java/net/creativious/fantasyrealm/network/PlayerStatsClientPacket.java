package net.creativious.fantasyrealm.network;

import io.netty.buffer.Unpooled;
import net.creativious.fantasyrealm.levelingsystem.PlayerStatsManager;
import net.creativious.fantasyrealm.levelingsystem.interfaces.IPlayerStatsManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;

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

    public static void executeLevelPacket(PlayerEntity player, PacketByteBuf buf) {
        PlayerStatsManager playerStatsManager = ((IPlayerStatsManager) player).getPlayerStatsManager(player);
        playerStatsManager.setLevel(buf.readInt()); // Level
        playerStatsManager.setTotalLevelExperience(buf.readInt()); // Total Level Experience
    }
}
