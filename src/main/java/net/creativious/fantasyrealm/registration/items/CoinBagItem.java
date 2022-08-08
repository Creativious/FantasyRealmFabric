package net.creativious.fantasyrealm.registration.items;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class CoinBagItem extends Item {

    public UUID uuid = null;

    public int coinCount;

    public CoinBagItem(Settings settings) {
        super(settings);
    }

    /**
     * @param world
     * @param user
     * @param hand
     * @return
     */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!user.getStackInHand(hand).hasNbt()) {
            ItemStack itemStack = initNBT(user.getStackInHand(hand));
        }
        if (uuid == null) {
            NbtCompound tag = user.getStackInHand(hand).getNbt();
            assert tag != null;
            this.uuid = tag.getUuid("UUID");
            this.coinCount = tag.getInt("coin_count");
        }
        if (user.getStackInHand(hand).hasNbt()) {
            //
        }
        return super.use(world, user, hand);
    }

    private ItemStack initNBT(ItemStack itemStack) {
        this.uuid = UUID.randomUUID();
        this.coinCount = 0;
        NbtCompound tag = new NbtCompound();
        tag.putUuid("UUID", this.uuid);
        tag.putInt("coin_count", this.coinCount);
        itemStack.setNbt(tag);

        return itemStack;
    }

    /**
     * @param stack
     * @param world
     * @param player
     */
    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        ItemStack newStack;
        if (!stack.hasNbt()) {
            newStack = initNBT(stack);
        } else {
            newStack = stack;
        }
        super.onCraft(newStack, world, player);
    }

    /**
     * @param stack
     * @param world
     * @param tooltip
     * @param context
     */
    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
    }
}
