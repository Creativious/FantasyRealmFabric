package net.creativious.fantasyrealm.mixin.player;

import net.creativious.fantasyrealm.FantasyRealmPlayerManager;
import net.creativious.fantasyrealm.interfaces.IFantasyRealmPlayerManager;
import net.creativious.fantasyrealm.levelingsystem.PlayerStatsManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements IFantasyRealmPlayerManager {

    private final FantasyRealmPlayerManager fantasyRealmPlayerManager = new FantasyRealmPlayerManager();

    private final PlayerStatsManager playerStatsManager = fantasyRealmPlayerManager.getPlayerStatsManager();

    private final PlayerEntity playerEntity = (PlayerEntity) (Object) this;

    /**
     * Instantiates a new Player entity mixin.
     *
     * @param entityType the entity type
     * @param world      the world
     */
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Read custom data from nbt
     *
     * @param tag  the tag
     * @param info the info
     */
    @Inject(method = "readCustomDataFromNbt", at = @At(value = "TAIL"))
    public void readCustomDataFromNbtMixin(NbtCompound tag, CallbackInfo info) {
        this.fantasyRealmPlayerManager.readNBT(tag);
    }

    /**
     * Write custom data to nbt
     *
     * @param tag  the tag
     * @param info the info
     */
    @Inject(method = "writeCustomDataToNbt", at = @At(value = "TAIL"))
    public void writeCustomDataToNbtMixin(NbtCompound tag, CallbackInfo info) {
        this.fantasyRealmPlayerManager.writeNBT(tag);
    }

    @Override
    public FantasyRealmPlayerManager getFantasyRealmPlayerManager(PlayerEntity player) {
        return this.fantasyRealmPlayerManager;
    }
}
