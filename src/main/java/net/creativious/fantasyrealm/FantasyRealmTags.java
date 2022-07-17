package net.creativious.fantasyrealm;

import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FantasyRealmTags {
    public static final TagKey<Item> BLACKSMITH_STAT_SMELTING_XP_GAIN = TagKey.of(Registry.ITEM_KEY, new Identifier("fantasyrealm", "blacksmith_stat_smelting_xp_gain"));
    public static final TagKey<Item> FOOD = TagKey.of(Registry.ITEM_KEY, new Identifier("c", "food"));
}
