package com.example.prestige.client.GUI;

import com.example.prestige.ExampleMod;
import com.example.prestige.cases.CaseItem;
import com.example.prestige.config.PrestigeModCommonConfig;
import com.example.prestige.networking.NetworkPackets;
import com.example.prestige.networking.serverPackets.SP_GiveItem;
import com.example.prestige.networking.serverPackets.SP_OpenCase;
import harmonised.pmmo.network.Networking;
import harmonised.pmmo.network.clientpackets.CP_SyncData_ClearXp;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class GetItemScreen extends Screen {
    private static final Component TITLE =
            Component.translatable("Prestige Case Opener");
    private static final Component OPEN_BUTTON =
            Component.translatable("Open Case");

    private static final Component CLOSE_BUTTON =
            Component.translatable("Open Later");

    private static final ResourceLocation BACKGROUND_TEXTURE =
            new ResourceLocation(ExampleMod.MODID, "textures/gui/example_block_screen.png");

    private static final ResourceLocation CASE_OPEN_OVERLAY =
            new ResourceLocation(ExampleMod.MODID, "textures/gui/case_open_overlay.png");

    private static final ResourceLocation DIAMOND_TEXTURE =
            new ResourceLocation("minecraft", "textures/item/diamond.png");


    private final int backgroundImageWidth, backgroundImageHeight;

    private int leftPos, topPos;

    private int rightPos;

    private final int ITEM_SIZE = 16;

    private boolean isOpening;

    private int spinSpeed;

    private Button button;

    private int tickCount;

    private CaseItem[] caseContents = new CaseItem[10];

    private static int winningIndex = 2;

    private boolean spinFinished = false;


    public GetItemScreen() {
        super(TITLE);
        tickCount = 0;
        this.backgroundImageWidth = 176;
        this.backgroundImageHeight = 166;
        this.isOpening = false;
        super.init();
        //diamondX = this.leftPos + 20;
        //diamondY = this.topPos + 80;
        tickCount = 0;
    }

    public static void setWinningIndex(int index){
        winningIndex = index;
        System.out.println("THIS LINE");
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - this.backgroundImageWidth) / 2;
        this.topPos = (this.height - this.backgroundImageHeight) / 2;
        this.rightPos = this.leftPos+this.backgroundImageWidth - ITEM_SIZE;


        for(int i = 0; i < caseContents.length; i++){
            //
            caseContents[i] = new CaseItem();
            caseContents[i].setX(this.leftPos+5+(ITEM_SIZE*i));
        }

        this.button = addRenderableWidget(
                Button.builder(
                                OPEN_BUTTON,
                                this::handleExampleButton)
                        .bounds(this.leftPos + 8, this.topPos + this.backgroundImageHeight - 30, 70, 20)
                        .tooltip(Tooltip.create(OPEN_BUTTON))
                        .build());
        this.addRenderableWidget(
                Button.builder(
                                CLOSE_BUTTON,
                                this::handleCloseButton) // Action to take when the close button is clicked
                        .bounds(this.leftPos + 8 + 80 + 5, this.topPos + this.backgroundImageHeight - 30, 70, 20)
                        // ^ Position it 5 pixels to the right of the first button
                        .tooltip(Tooltip.create(CLOSE_BUTTON))
                        .build()
        );
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        tickCount++;
        graphics.blit(BACKGROUND_TEXTURE, this.leftPos, this.topPos, 0, 0, this.backgroundImageWidth, this.backgroundImageHeight);
        //System.out.println(mouseX);
        // winning item has ID = 62
        for(int i = 0; i < caseContents.length; i++){
            graphics.blit(caseContents[i].getTEXTURE(),caseContents[i].getX(), this.topPos+78 , 0, 0, ITEM_SIZE, ITEM_SIZE,ITEM_SIZE,ITEM_SIZE);
            if(isOpening){
                if(tickCount > 360){
                    spinSpeed = 0;
                    spinFinished = true;
                }

                caseContents[i].setX(caseContents[i].getX()+spinSpeed);
                if(caseContents[i].getX() >rightPos){
                    caseContents[i].setX(leftPos);
                    caseContents[i].refreshItem();
                    if(caseContents[i].getID() == 62){
                        caseContents[i].setTextureFromIndex(winningIndex);
                    }
                }
            }
        }
        if(spinFinished){
            if(tickCount > 400){

                this.minecraft.setScreen(null);
                NetworkPackets.sendToServer(new SP_GiveItem());
            }
        }
        graphics.blit(CASE_OPEN_OVERLAY, this.leftPos, this.topPos, 0, 0, this.backgroundImageWidth, this.backgroundImageHeight);
        super.render(graphics, mouseX, mouseY, partialTicks);
        graphics.drawString(this.font,
                TITLE,
                this.leftPos + 8,
                this.topPos + 8,
                0x404040,
                false);

    }

    private void handleExampleButton(Button button) {
        // logic here
        if(!isOpening){
            tickCount = 0;
            spinSpeed = 3;
            isOpening = true;
            CaseItem.resetIDCounter();
            NetworkPackets.sendToServer(new SP_OpenCase());
        }
    }

    private void handleCloseButton(Button button) {
        this.minecraft.setScreen(null); // Closes the current screen
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}