package com.example.prestige.cases;

import com.example.prestige.networking.NetworkPackets;
import com.example.prestige.networking.clientPackets.CP_SetCaseItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class  ServerCaseHandler {
    public static void openCase(ServerPlayer player) {
        NetworkPackets.sendToPlayer(new CP_SetCaseItem(), player);
    }
    public static void giveItem(ServerPlayer player){
        player.addItem(new ItemStack(Items.DIAMOND,1));
    }
}
