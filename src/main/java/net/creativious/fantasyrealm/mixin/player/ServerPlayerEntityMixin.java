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

    /**
     * The Player stats manager.
     */
    PlayerStatsManager playerStatsManager = ((IPlayerStatsManager) this).getPlayerStatsManager((PlayerEntity) (Object) this);

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile, @Nullable PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    /**
     * Injects to the onSpawn method within ServerPlayerEntity
     *
     * When the player respawns/joins the server it'll make sure the correct level is given based on the experience they have
     * and also updates the client player with the information from the server
     *
     * @param ci CallbackInfo
     */
    @Inject(method="onSpawn", at=@At(value="TAIL"))
    public void onSpawn(CallbackInfo ci) {
        this.playerStatsManager.autoFixLevel();
        PlayerStatsServerPacket.writeS2CLevelPacket(playerStatsManager, (ServerPlayerEntity) (Object) this);
    }

    /**
     * Injects into the playerTick method with ServerPlayerEntity
     *
     * WARNING ALWAYS HAVE AN IF STATEMENT CHECK IF IT IS REALLY NEEDED TO RUN THINGS WITHIN HERE, THIS IS A COMMON CRASH IF YOU USE THIS IMPROPERLY
     *
     * Ensures that the server and the client are synced with playerStatsManager
     *
     * @param ci Callback Info
     */
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

    /**
     * Injects into the updateKilledAdvancementCriterion method with ServerPlayerEntity
     *
     * Used by me to know when an entity is killed by the player so I can use it to level up the player for each max hp point the killed creature had.
     *
     * @param entityKilled the entity killed
     * @param score        the score
     * @param damageSource the damage source
     * @param ci           Callback info
     */
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
