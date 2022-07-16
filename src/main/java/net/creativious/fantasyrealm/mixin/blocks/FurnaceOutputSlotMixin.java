package net.creativious.fantasyrealm.mixin.blocks;


import net.creativious.fantasyrealm.levelingsystem.PlayerStatsManager;
import net.creativious.fantasyrealm.levelingsystem.interfaces.IPlayerStatsManager;
import net.creativious.fantasyrealm.network.PlayerStatsServerPacket;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FurnaceOutputSlot.class)
public class FurnaceOutputSlotMixin extends Slot {

    @Shadow
    private final PlayerEntity player;


    public FurnaceOutputSlotMixin(Inventory inventory, int index, int x, int y, PlayerEntity player) {
        super(inventory, index, x, y);
        this.player = player;
    }

    @Inject(method="onCrafted(Lnet/minecraft/item/ItemStack;I)V", at=@At(value="TAIL"))
    protected void onCraftedMixin(ItemStack stack, int amount, CallbackInfo ci) {
        if (player instanceof ServerPlayerEntity) {
            PlayerStatsManager playerStatsManager = ((IPlayerStatsManager) player).getPlayerStatsManager((PlayerEntity) (Object) player);
            playerStatsManager.addExperience(stack.getCount());
            playerStatsManager.autoFixLevel();
            PlayerStatsServerPacket.writeS2CLevelPacket(playerStatsManager, (ServerPlayerEntity) (Object) player);
        }
    }
}
