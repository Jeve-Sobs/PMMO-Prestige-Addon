package com.example.prestige.networking.serverPackets;

import com.example.prestige.cases.ServerCaseHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SP_GiveItem {
    public SP_GiveItem() {
    }
    public SP_GiveItem(FriendlyByteBuf buf) {
    }
    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        ServerPlayer player = context.getSender();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerCaseHandler.giveItem(player);
        });
        return true;
    }
}
