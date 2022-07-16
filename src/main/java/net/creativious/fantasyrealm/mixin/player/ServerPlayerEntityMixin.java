package net.creativious.fantasyrealm.mixin.player;

import com.mojang.authlib.GameProfile;
import net.creativious.fantasyrealm.levelingsystem.PlayerStatsManager;
import net.creativious.fantasyrealm.levelingsystem.interfaces.IPlayerStatsManager;
import net.creativious.fantasyrealm.levelingsystem.interfaces.IServerPlayerEntity;
import net.creativious.fantasyrealm.network.PlayerStatsServerPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
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
        this.playerStatsManager.autoFixLevel();
        PlayerStatsServerPacket.writeS2CLevelPacket(playerStatsManager, (ServerPlayerEntity) (Object) this);
    }

    @Inject(method="playerTick", at=@At(value="TAIL"))
    public void playerTick(CallbackInfo ci) {
        if (this.playerStatsManager.getLevel() != this.syncedPlayerStatsLevel) {
            if(this.playerStatsManager.getTotalLevelExperience() > this.playerStatsManager.neededTotalExperienceForLevelUp()) {
                this.playerStatsManager.autoFixLevel();
            }
            PlayerStatsServerPacket.writeS2CLevelPacket(playerStatsManager, (ServerPlayerEntity) (Object) this);
            this.syncedPlayerStatsLevel = this.playerStatsManager.getLevel();
        }
        if (this.playerStatsManager.getTotalLevelExperience() != this.syncedPlayerStatsTotalExperience) {
            if(this.playerStatsManager.getTotalLevelExperience() > this.playerStatsManager.neededTotalExperienceForLevelUp()) {
                this.playerStatsManager.autoFixLevel();
                this.syncedPlayerStatsLevel = this.playerStatsManager.getLevel();
            }
            PlayerStatsServerPacket.writeS2CLevelPacket(playerStatsManager, (ServerPlayerEntity) (Object) this);
            this.syncedPlayerStatsTotalExperience = this.playerStatsManager.getTotalLevelExperience();
        }
    }

    @Inject(method="updateKilledAdvancementCriterion", at=@At(value="TAIL"))
    public void mobKilled(Entity entityKilled, int score, DamageSource damageSource, CallbackInfo ci) {
        if (entityKilled != this) {
            MobEntity entity = (MobEntity) entityKilled;
            double max_health = entity.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH);
            int xpGained = (int) Math.round(max_health);
            this.playerStatsManager.addExperience(xpGained);
            this.playerStatsManager.autoFixLevel();
            PlayerStatsServerPacket.writeS2CLevelPacket(playerStatsManager, (ServerPlayerEntity) (Object) this);
        }
    }

}
