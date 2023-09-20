package com.example.prestige;

import com.example.prestige.corePrestige.PlayerPrestige;
import com.example.prestige.corePrestige.PlayerPrestigeProvider;
import com.example.prestige.corePrestige.RomanNumeralUtil;
import net.minecraft.network.chat.Component;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID)
public class EventHandler {

    // mod events

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(PlayerPrestigeProvider.PLAYER_PRESTIGE).isPresent()) {
                event.addCapability(new ResourceLocation(ExampleMod.MODID, "properties"), new PlayerPrestigeProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().reviveCaps();
            event.getOriginal().getCapability(PlayerPrestigeProvider.PLAYER_PRESTIGE).ifPresent(oldStore -> {
                event.getEntity().getCapability(PlayerPrestigeProvider.PLAYER_PRESTIGE).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                    event.getOriginal().invalidateCaps();
                });
            });
        }
    }


    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerPrestige.class);
    }


    @SubscribeEvent
    public static void onTabListNameFormat(PlayerEvent.TabListNameFormat event) {
        UUID playerUUID = event.getEntity().getUUID();
        AtomicInteger prestigeLevel = new AtomicInteger(0);
        event.getEntity().getCapability(PlayerPrestigeProvider.PLAYER_PRESTIGE).ifPresent(prestige -> {
            prestigeLevel.set(prestige.getLevel());
        });
        String prestigeRoman = RomanNumeralUtil.toRoman(prestigeLevel.get());
        String prestigePrefix;
        //System.out.println("FUCK MY TIGHT LITTLE CUNT        "+event.getEntity().getDisplayName().getString());
        event.setDisplayName(event.getEntity().getDisplayName());
        //event.setDisplayName(Component.nullToEmpty(event.getEntity().getDisplayName().getString()));
    }





    @SubscribeEvent
    public static void onNameFormat(PlayerEvent.NameFormat event) {
        UUID playerUUID = event.getEntity().getUUID();
        AtomicInteger prestigeLevel = new AtomicInteger(0);
        event.getEntity().getCapability(PlayerPrestigeProvider.PLAYER_PRESTIGE).ifPresent(prestige -> {
            prestigeLevel.set(prestige.getLevel());
        });
        String prestigeRoman = RomanNumeralUtil.toRoman(prestigeLevel.get());
        String prestigePrefix = "";
        if(prestigeLevel.get() >0){
            prestigePrefix = "§6["+String.valueOf(prestigeRoman)+"] §6"+ event.getDisplayname().getString();
        } else{
            prestigePrefix = event.getDisplayname().getString();
        }
        event.setDisplayname(Component.nullToEmpty(prestigePrefix));
    }

    // client events


}
