package com.example.prestige;

import com.example.prestige.config.PrestigeModCommonConfig;
import com.example.prestige.corePrestige.PlayerPrestigeProvider;
import com.example.prestige.networking.NetworkPackets;
import com.example.prestige.networking.clientPackets.CP_OpenPrestigeScreen;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.core.IDataStorage;
import harmonised.pmmo.network.Networking;
import harmonised.pmmo.network.clientpackets.CP_SyncData_ClearXp;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod
{
    public static final String MODID = "prestige";
    private static final String TARGET_ARG = "Target";

    public ExampleMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PrestigeModCommonConfig.SPEC, "prestige-common.toml");

        // Validate the drop chances sum to 100%

    }


    private void setup(final FMLCommonSetupEvent event) {
        NetworkPackets.register();
        // validate coofig
        double totalDropChance = PrestigeModCommonConfig.getPrestigeCrateDropChances().stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(totalDropChance - 100.0) > 0.001) { // Adding a small tolerance for floating-point comparisons
            throw new IllegalStateException("Total drop chances in prestige-common.toml do not sum up to 100%. Found: " + totalDropChance + "%");
        }

        // Validate the lengths of drop chances and items are the same
        int dropChancesSize = PrestigeModCommonConfig.getPrestigeCrateDropChances().size();
        int itemsSize = PrestigeModCommonConfig.getPrestigeCrateItems().length;

        if (dropChancesSize != itemsSize) {
            throw new IllegalStateException("The number of items (" + itemsSize + ") does not match the number of drop chances (" + dropChancesSize + ") in prestige-common.toml");
        }
    }

    public void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("prestige").executes(ExampleMod::PrestigeCommand));
        event.getDispatcher().register(Commands.literal("openPrestigeCrate").executes(ExampleMod::openPrestigeCrateCommand));

        event.getDispatcher().register(Commands.literal("resetPrestigeLevel")
                .requires(commandSource -> commandSource.hasPermission(2)) // Restricts command to OP level 2 or higher
                .then(Commands.argument(TARGET_ARG, EntityArgument.player()) // Add the target argument
                        .executes(ExampleMod::ResetCommand)));
    }

    public static int openPrestigeCrateCommand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException{
        ServerPlayer player = context.getSource().getPlayer();
        player.getCapability(PlayerPrestigeProvider.PLAYER_PRESTIGE).ifPresent(prestige -> {
            if (prestige.getPrestigeCrates() > 0) {
                NetworkPackets.sendToPlayer(new CP_OpenPrestigeScreen(prestige.getPrestigeCrates()),player);
            } else {
                player.sendSystemMessage(Component.literal("You do not have any prestige crates to open")
                        .withStyle(ChatFormatting.AQUA));
            }

        });

        return 0;
    }
    public static int ResetCommand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException{
        //etworkPackets.sendToServer(new ResetPrestigeLevelC2SPacket());
        IDataStorage data = Core.get(LogicalSide.SERVER).getData();

        // Use the target argument to get the player to be reset.
        ServerPlayer targetPlayer = EntityArgument.getPlayer(context, TARGET_ARG);

        targetPlayer.getCapability(PlayerPrestigeProvider.PLAYER_PRESTIGE).ifPresent(prestige -> {
            prestige.setLevel(0);
            prestige.setPrestigeCrates(0);
            targetPlayer.sendSystemMessage(Component.literal("Your prestige level has been reset to 0" )
                    .withStyle(ChatFormatting.AQUA));
            targetPlayer.refreshDisplayName();
            targetPlayer.refreshTabListName();
        });
        return 0;
    }

    public static int PrestigeCommand(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        // Send a request to the server to increment the prestige level for the command issuer
        AtomicInteger casesToOPen = new AtomicInteger();
        Core core = Core.get(LogicalSide.SERVER);
        IDataStorage data = Core.get(LogicalSide.SERVER).getData();
        ServerPlayer player = context.getSource().getPlayer();
        int totalLevels = core.getData()
                .getXpMap(player.getUUID())
                .values()
                .stream().mapToInt(core.getData()::getLevelFromXP).sum();
        if (totalLevels >= PrestigeModCommonConfig.XP_LEVELS_TO_PRESTIGE.get()){
            player.getCapability(PlayerPrestigeProvider.PLAYER_PRESTIGE).ifPresent(prestige -> {
                if(prestige.getLevel() < PrestigeModCommonConfig.MAX_PRESTIGE_LEVEL.get()){
                    prestige.setLevel(prestige.getLevel()+1);
                    prestige.setPrestigeCrates(prestige.getPrestigeCrates()+1);
                    player.sendSystemMessage(Component.literal("You have successfully prestiged to level " +prestige.getLevel())
                            .withStyle(ChatFormatting.AQUA));
                    player.refreshDisplayName();
                    context.getSource().getPlayer().refreshTabListName();
                    casesToOPen.set(prestige.getPrestigeCrates());
                    for (ServerPlayer otherPlayer : player.server.getPlayerList().getPlayers()) {
                        if (!otherPlayer.equals(player)) { // Ensure it's not the player who just prestiged
                            otherPlayer.sendSystemMessage(Component.literal(player.getDisplayName().getString() + " has just prestiged to level " + prestige.getLevel()).withStyle(ChatFormatting.GOLD));
                        }
                    }
                    data.setXpMap(player.getUUID(), new HashMap<>());
                    Networking.sendToClient(new CP_SyncData_ClearXp(), player);
                    NetworkPackets.sendToPlayer(new CP_OpenPrestigeScreen(casesToOPen.get()),player);
                } else{
                    player.sendSystemMessage(Component.literal("You have reached the max prestige level of "+PrestigeModCommonConfig.MAX_PRESTIGE_LEVEL.get())
                            .withStyle(ChatFormatting.AQUA));
                }

            });
        } else{
            player.sendSystemMessage(Component.literal("You need "+PrestigeModCommonConfig.XP_LEVELS_TO_PRESTIGE.get()+" levels to prestige, you have "+totalLevels)
                    .withStyle(ChatFormatting.AQUA));
        }


        return 0;
    }

}
