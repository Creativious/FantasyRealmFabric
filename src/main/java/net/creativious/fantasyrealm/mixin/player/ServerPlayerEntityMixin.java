package net.creativious.fantasyrealm.mixin.player;

import com.mojang.authlib.GameProfile;
import net.creativious.fantasyrealm.levelingsystem.PlayerStatsManager;
import net.creativious.fantasyrealm.levelingsystem.interfaces.IPlayerStatsManager;
import net.creativious.fantasyrealm.levelingsystem.interfaces.IServerPlayerEntity;
import net.creativious.fantasyrealm.network.PlayerStatsServerPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements IServerPlayerEntity {

    public int syncedPlayerStatsLevel = -1;
    public float syncedPlayerStatsLevelProgress = -1;
    public float syncedPlayerStatsTotalExperience = -1;

    PlayerStatsManager playerStatsManager = ((IPlayerStatsManager) this).getPlayerStatsManager((PlayerEntity) (Object) this);

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    @Inject(method="onSpawn", at=@At(value="TAIL"))
    public void onSpawn(CallbackInfo ci) {
        PlayerStatsServerPacket.writeS2CLevelPacket(playerStatsManager, (ServerPlayerEntity) (Object) this);
    }

    @Inject(method="playerTick", at=@At(value="TAIL"))
    public void playerTick(CallbackInfo ci) {
        if (this.playerStatsManager.getLevel() != this.syncedPlayerStatsLevel) {
            PlayerStatsServerPacket.writeS2CLevelPacket(playerStatsManager, (ServerPlayerEntity) (Object) this);
            this.syncedPlayerStatsLevel = this.playerStatsManager.getLevel();
        }
        if (this.playerStatsManager.getLevelProgress() != this.syncedPlayerStatsLevelProgress) {
            PlayerStatsServerPacket.writeS2CLevelPacket(playerStatsManager, (ServerPlayerEntity) (Object) this);
            this.syncedPlayerStatsLevelProgress = this.playerStatsManager.getLevelProgress();
        }
        if (this.playerStatsManager.getTotalLevelExperience() != this.syncedPlayerStatsTotalExperience) {
            PlayerStatsServerPacket.writeS2CLevelPacket(playerStatsManager, (ServerPlayerEntity) (Object) this);
            this.syncedPlayerStatsTotalExperience = this.playerStatsManager.getTotalLevelExperience();
        }
    }

}
