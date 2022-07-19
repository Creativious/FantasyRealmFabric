package net.creativious.fantasyrealm.mixin.blocks;


import net.creativious.fantasyrealm.FantasyRealmPlayerManager;
import net.creativious.fantasyrealm.interfaces.IFantasyRealmPlayerManager;
import net.creativious.fantasyrealm.registration.tags.FantasyRealmTags;
import net.creativious.fantasyrealm.levelingsystem.PlayerStatsManager;
import net.creativious.fantasyrealm.network.PlayerStatsServerPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FurnaceOutputSlot.class)
public class FurnaceOutputSlotMixin extends Slot {

    @Shadow
    private final PlayerEntity player;


    public FurnaceOutputSlotMixin(Inventory inventory, int index, int x, int y, PlayerEntity player) {
        super(inventory, index, x, y);
        this.player = player;
    }

    /**
     * Upon taking items out of the smelted part of the furnace the player gains xp relative to the items retrieved.
     *
     * @param stack  the stack
     * @param amount the amount
     * @param ci     the ci
     */
    @Inject(method="onCrafted(Lnet/minecraft/item/ItemStack;I)V", at=@At(value="TAIL"))
    protected void onCraftedMixin(ItemStack stack, int amount, CallbackInfo ci) {
        if (player instanceof ServerPlayerEntity) {
            // @TODO: Different XP Levels based on type of item smelted, WAY DOWN IN THE FUTURE I DO NOT WANT TO DO IT
            List<TagKey<Item>> tagList = stack.streamTags().toList();
            FantasyRealmPlayerManager fantasyRealmPlayerManager = ((IFantasyRealmPlayerManager) player).getFantasyRealmPlayerManager((PlayerEntity) (Object) player);
            PlayerStatsManager playerStatsManager = fantasyRealmPlayerManager.getPlayerStatsManager();


            if (tagList.contains(FantasyRealmTags.BLACKSMITH_STAT_SMELTING_XP_GAIN)) {
                // Blacksmithing xp gain
                playerStatsManager.blacksmithingStat.addExperience(stack.getCount());
                playerStatsManager.blacksmithingStat.autoFixLevel();

            }

            if (tagList.contains((FantasyRealmTags.FOOD))) {
                // Cooking xp gain
                playerStatsManager.cookingStat.addExperience(stack.getCount());
                playerStatsManager.cookingStat.autoFixLevel();
            }

            playerStatsManager.addExperience(stack.getCount());
            playerStatsManager.autoFixLevel();
            PlayerStatsServerPacket.writeS2CLevelPacket(fantasyRealmPlayerManager, (ServerPlayerEntity) (Object) player);
        }
    }
}
