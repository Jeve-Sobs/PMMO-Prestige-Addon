package com.example.prestige.networking.clientPackets;

import com.example.prestige.client.GUI.ClientHooks;
import com.example.prestige.client.GUI.GetItemScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CP_SetCaseItem {

    private int winningIndex;

    public CP_SetCaseItem(int winningIndex) {
        this.winningIndex = winningIndex;
    }

    public CP_SetCaseItem(FriendlyByteBuf buf) {
        this.winningIndex = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(winningIndex);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> GetItemScreen.setWinningIndex(winningIndex));
        });
        return true;
    }
}
