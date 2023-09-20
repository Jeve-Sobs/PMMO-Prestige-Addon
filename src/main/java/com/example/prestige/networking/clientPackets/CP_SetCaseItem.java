package com.example.prestige.networking.clientPackets;

import com.example.prestige.client.GUI.ClientHooks;
import com.example.prestige.client.GUI.GetItemScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CP_SetCaseItem {
    public CP_SetCaseItem() {
    }
    public CP_SetCaseItem(FriendlyByteBuf buf) {
    }
    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            //GetItemScreen.setWinningIndex(0);
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> GetItemScreen.setWinningIndex(0));

        });
        return true;
    }
}
