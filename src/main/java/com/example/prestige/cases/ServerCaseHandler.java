package com.example.prestige.cases;

import com.example.prestige.config.PrestigeModCommonConfig;
import com.example.prestige.corePrestige.PlayerPrestigeProvider;
import com.example.prestige.networking.NetworkPackets;
import com.example.prestige.networking.clientPackets.CP_SetCaseItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

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

                // Check if the player's inventory can add the item
                boolean wasAddedToInventory = player.getInventory().add(toGive);

                // If the item wasn't added, drop it at the player's location
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
