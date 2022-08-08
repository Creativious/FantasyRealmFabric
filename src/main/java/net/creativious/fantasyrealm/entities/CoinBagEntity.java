package net.creativious.fantasyrealm.entities;

import net.creativious.fantasyrealm.interfaces.ICoinBagInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public class CoinBagEntity extends BlockEntity implements ICoinBagInventory {

    public CoinBagEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /**
     * @return
     */
    @Override
    public DefaultedList<ItemStack> getItems() {
        return null;
    }

    /**
     *
     */
    @Override
    public void clear() {

    }
}
