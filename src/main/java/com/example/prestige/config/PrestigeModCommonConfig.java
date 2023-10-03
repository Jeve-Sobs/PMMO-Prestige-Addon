package com.example.prestige.config;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class PrestigeModCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> PRESTIGE_CRATE_ROOT;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Double>> PRESTIGE_CRATE_DROP_CHANCES;

    public static final ForgeConfigSpec.IntValue MAX_PRESTIGE_LEVEL;
    public static final ForgeConfigSpec.IntValue XP_LEVELS_TO_PRESTIGE;

    static {
        BUILDER.push("Configs for Prestige Mod");

        PRESTIGE_CRATE_ROOT = BUILDER.comment("List of items in the Prestige Crate")
                .defineList("Prestige Crate Items", List.of(
                                "twilightdelight:teardrop_sword",
                                "lost_aether_content:phoenix_pickaxe",
                                "lost_aether_content:phoenix_sword",
                                "lost_aether_content:phoenix_axe",
                                "lost_aether_content:phoenix_shovel",
                                "deep_aether:stratus_sword",
                                "deeperdarker:warden_helmet",
                                "deeperdarker:warden_chestplate",
                                "deeperdarker:warden_leggings",
                                "deeperdarker:warden_boots",
                                "deeperdarker:warden_sword",
                                "deep_aether:stratus_axe",
                                "deeperdarker:warden_axe",
                                "deeperdarker:warden_shovel",
                                "minecraft:coal",
                                "deep_aether:stratus_sword"),
                        s -> s instanceof String);

        PRESTIGE_CRATE_DROP_CHANCES = BUILDER.comment("Drop chances for items in the Prestige Crate (must total to 100%)")
                .defineList("Prestige Crate Drop Chances", List.of(6.25, 6.25, 6.25, 6.25, 6.25, 6.25, 6.25, 6.25, 6.25, 6.25, 6.25, 6.25, 6.25, 6.25, 6.25, 6.25),
                        d -> d instanceof Double && (double) d >= 0);

        MAX_PRESTIGE_LEVEL = BUILDER.comment("Maximum Prestige Level")
                .defineInRange("Max Prestige Level", 20, 1, 1000);

        XP_LEVELS_TO_PRESTIGE = BUILDER.comment("XP Levels needed to Prestige")
                .defineInRange("XP Levels to Prestige", 1000, 0, 50000);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    // Use this method to get the actual ItemStack list from the config
    public static ItemStack[] getPrestigeCrateItems() {
        List<String> items = (List<String>) PRESTIGE_CRATE_ROOT.get();
        return items.stream().map(item -> {
            return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(item)),1);
        }).toArray(ItemStack[]::new);
    }

    // Use this method to get the drop chances
    public static List<Double> getPrestigeCrateDropChances() {
        return (List<Double>) PRESTIGE_CRATE_DROP_CHANCES.get();
    }
}
