package com.example.prestige.cases;

import com.example.prestige.config.PrestigeModCommonConfig;
import com.example.prestige.corePrestige.PlayerPrestigeProvider;
import com.example.prestige.networking.NetworkPackets;
import com.example.prestige.networking.clientPackets.CP_SetCaseItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.Map;

public class  ServerCaseHandler {
    public static void openCase(ServerPlayer player) {
        player.getCapability(PlayerPrestigeProvider.PLAYER_PRESTIGE).ifPresent(prestige -> {
            NetworkPackets.sendToPlayer(new CP_SetCaseItem(prestige.getPrestigeItemIndex()), player);

        });

    }
    public static void giveItem(ServerPlayer player) {
        player.getCapability(PlayerPrestigeProvider.PLAYER_PRESTIGE).ifPresent(prestige -> {
            if (prestige.getPrestigeCrates() > 0) {
                ItemStack toGive = PrestigeModCommonConfig.getPrestigeCrateItems()[prestige.getPrestigeItemIndex()];
                Item givenItem = toGive.getItem();

                // Apply Enchantments to Tools, Weapons, and Armor
                Map<Enchantment, Integer> existingEnchantments = EnchantmentHelper.getEnchantments(toGive);
                if (givenItem instanceof SwordItem || givenItem instanceof AxeItem || givenItem instanceof PickaxeItem || givenItem instanceof ShovelItem || givenItem instanceof HoeItem || givenItem instanceof ArmorItem) {
                    existingEnchantments.put(Enchantments.UNBREAKING, 10);
                    existingEnchantments.put(Enchantments.MENDING, 1); // Mending does not have levels

                    if (givenItem instanceof SwordItem || givenItem instanceof AxeItem) {
                        existingEnchantments.put(Enchantments.SHARPNESS, 7);
                    }

                    if (givenItem instanceof PickaxeItem || givenItem instanceof ShovelItem || givenItem instanceof HoeItem) {
                        existingEnchantments.put(Enchantments.BLOCK_EFFICIENCY, 7);
                    }

                    if (givenItem instanceof ArmorItem) {
                        existingEnchantments.put(Enchantments.ALL_DAMAGE_PROTECTION, 7);
                    }

                    EnchantmentHelper.setEnchantments(existingEnchantments, toGive);
                }

                // Adding to inventory or dropping at player location
                boolean wasAddedToInventory = player.getInventory().add(toGive);
                if (!wasAddedToInventory) {
                    player.drop(toGive, false);
                }

                prestige.setPrestigeCrates(prestige.getPrestigeCrates() - 1);
                prestige.refreshCaseIndex();
            } else {
                player.sendSystemMessage(Component.literal("You do not have any prestige crates to open")
                        .withStyle(ChatFormatting.AQUA));
            }
        });
    }




}
