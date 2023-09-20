package com.example.prestige.networking;

import com.example.prestige.ExampleMod;
import com.example.prestige.networking.clientPackets.CP_OpenPrestigeScreen;
import com.example.prestige.networking.clientPackets.CP_SetCaseItem;
import com.example.prestige.networking.serverPackets.SP_GiveItem;
import com.example.prestige.networking.serverPackets.SP_OpenCase;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkPackets {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(ExampleMod.MODID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();


        INSTANCE.messageBuilder(CP_OpenPrestigeScreen.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(CP_OpenPrestigeScreen::new)
                .encoder(CP_OpenPrestigeScreen::toBytes)
                .consumerMainThread(CP_OpenPrestigeScreen::handle)
                .add();

        INSTANCE.messageBuilder(SP_OpenCase.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SP_OpenCase::new)
                .encoder(SP_OpenCase::toBytes)
                .consumerMainThread(SP_OpenCase::handle)
                .add();
        INSTANCE.messageBuilder(CP_SetCaseItem.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(CP_SetCaseItem::new)
                .encoder(CP_SetCaseItem::toBytes)
                .consumerMainThread(CP_SetCaseItem::handle)
                .add();
        INSTANCE.messageBuilder(SP_GiveItem.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SP_GiveItem::new)
                .encoder(SP_GiveItem::toBytes)
                .consumerMainThread(SP_GiveItem::handle)
                .add();


    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
