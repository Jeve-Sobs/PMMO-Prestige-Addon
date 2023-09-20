package com.example.prestige.networking.clientPackets;

import com.example.prestige.client.GUI.ClientHooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CP_OpenPrestigeScreen {
    private int cratesToOpen;
    public CP_OpenPrestigeScreen(int cratesToOpen) {
        this.cratesToOpen = cratesToOpen;
    }
    public CP_OpenPrestigeScreen(FriendlyByteBuf buf) {
        this.cratesToOpen = buf.readInt();
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.cratesToOpen);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientHooks.openItemOpenScreen(this.cratesToOpen));

        });
        return true;
    }
}
