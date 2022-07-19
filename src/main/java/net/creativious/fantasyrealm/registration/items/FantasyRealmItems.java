package net.creativious.fantasyrealm.registration.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class FantasyRealmItems {

    public static final Item BRONZE_COIN = new Item(new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item GOLD_COIN = new Item(new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item SILVER_COIN = new Item(new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item PLATINUM_COIN = new Item(new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item COIN_BAG = new CoinBagItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));

    public static void init() {
        Registry.register(Registry.ITEM, new Identifier("fantasyrealm", "bronze_coin"), BRONZE_COIN);
        Registry.register(Registry.ITEM, new Identifier("fantasyrealm", "gold_coin"), GOLD_COIN);
        Registry.register(Registry.ITEM, new Identifier("fantasyrealm", "silver_coin"), SILVER_COIN);
        Registry.register(Registry.ITEM, new Identifier("fantasyrealm", "platinum_coin"), PLATINUM_COIN);
        Registry.register(Registry.ITEM, new Identifier("fantasyrealm", "coin_bag"), COIN_BAG);

    }

}
