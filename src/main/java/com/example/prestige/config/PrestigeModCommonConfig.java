package com.example.prestige.config;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class PrestigeModCommonConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> PRESTIGE_CRATE_ROOT;
    public static final ForgeConfigSpec.ConfigValue<List<? extends Double>> PRESTIGE_CRATE_DROP_CHANCES;

    static {
        BUILDER.push("Configs for Prestige Mod");

        PRESTIGE_CRATE_ROOT = BUILDER.comment("List of items in the Prestige Crate")
                .defineList("Prestige Crate Items",
                        List.of("minecraft:diamond", "minecraft:diamond",
                                "minecraft:coal", "minecraft:coal",
                                "minecraft:diamond", "minecraft:emerald",
                                "minecraft:diamond", "minecraft:coal",
                                "minecraft:diamond", "minecraft:emerald"),
                        s -> s instanceof String);

        PRESTIGE_CRATE_DROP_CHANCES = BUILDER.comment("Drop chances for items in the Prestige Crate (must total to 100%)")
                .defineList("Prestige Crate Drop Chances",
                        List.of(10.0, 10.0, 20.0, 20.0, 10.0, 10.0, 10.0, 5.0, 2.5, 2.5),
                        d -> d instanceof Double && (double) d >= 0);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    // Use this method to get the actual ItemStack list from the config
    public static ItemStack[] getPrestigeCrateItems() {
        List<String> items = (List<String>) PRESTIGE_CRATE_ROOT.get();
        return items.stream().map(item -> {
            switch (item) {
                case "minecraft:diamond":
                    return new ItemStack(Items.DIAMOND, 1);
                case "minecraft:coal":
                    return new ItemStack(Items.COAL, 1);
                case "minecraft:lapis":
                    return new ItemStack(Items.LAPIS_LAZULI, 1);
                case "minecraft:emerald":
                    return new ItemStack(Items.EMERALD, 1);
                default:
                    return ItemStack.EMPTY;
            }
        }).toArray(ItemStack[]::new);
    }

    // Use this method to get the drop chances
    public static List<Double> getPrestigeCrateDropChances() {
        return (List<Double>) PRESTIGE_CRATE_DROP_CHANCES.get();
    }
}
