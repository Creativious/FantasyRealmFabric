package net.creativious.fantasyrealm.mixin.blocks;


import io.netty.handler.logging.LogLevel;
import net.creativious.fantasyrealm.FantasyRealm;
import net.creativious.fantasyrealm.levelingsystem.PlayerStatsManager;
import net.creativious.fantasyrealm.levelingsystem.interfaces.IPlayerStatsManager;
import net.creativious.fantasyrealm.network.PlayerStatsServerPacket;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.message.MessageSender;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.Console;
import java.util.logging.Level;
import java.util.logging.LogRecord;

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
            // @TODO: Different XP Levels based on type of item smelted
            // @TODO: Connect Blacksmith and Cooking stats to these with different xp levels based on the item and sort based on what fits
            for (TagKey<?> tag : stack.streamTags().toList()) {
                ((ServerPlayerEntity) player).sendChatMessage(SignedMessage.of(Text.of(tag.toString())), MessageSender.of(Text.of("Console")), MessageType.CHAT);

            }

            PlayerStatsManager playerStatsManager = ((IPlayerStatsManager) player).getPlayerStatsManager((PlayerEntity) (Object) player);
            playerStatsManager.addExperience(stack.getCount());
            playerStatsManager.autoFixLevel();
            PlayerStatsServerPacket.writeS2CLevelPacket(playerStatsManager, (ServerPlayerEntity) (Object) player);
        }
    }
}
