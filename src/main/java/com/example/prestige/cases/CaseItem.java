package com.example.prestige.cases;

import com.example.prestige.ExampleMod;
import com.example.prestige.config.PrestigeModCommonConfig;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Matrix4f;


import java.util.Random;

public class CaseItem {
    private int x,y;



    private ResourceLocation TEXTURE;

    private static final Random RANDOM = new Random();
    private ItemStack caseItem;

    private static int ID_COUNTER = 0;
    private int itemId;

    public CaseItem(ItemStack item){
        this.caseItem = item;
        this.itemId = ++ID_COUNTER;
        TEXTURE = new ResourceLocation("minecraft", "textures/item/" + caseItem.getItem().getName(caseItem).getString().toLowerCase() + ".png");
        //TEXTURE = new ResourceLocation("minecraft", "textures/item/"+item.getItem().getName(item).getString().toLowerCase()+".png");
        this.x = 0;
        this.y = 0;
        //ItemStack itemStack = new ItemStack(Items.DIAMOND, 1); // Change Items.DIAMOND to whatever item you want.
    }

    public CaseItem() {
        this.itemId = ++ID_COUNTER;
        chooseRandomItem();
        this.setTexture();
        //TEXTURE = new ResourceLocation("minecraft", "textures/item/" + caseItem.getItem().getName(caseItem).getString().toLowerCase() + ".png");
        //TEXTURE = new ResourceLocation("minecraft", "textures/item/" + caseItem.getItem().getName(caseItem).getString().toLowerCase() + ".png");
        System.out.println(TEXTURE);
        this.x = 0;
        this.y = 0;
    }

    private void setTexture() {

        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(caseItem.getItem());
        String namespace = registryName.getNamespace();
        String path = registryName.getPath();
        TEXTURE = new ResourceLocation(namespace, "textures/item/" + path + ".png");
    }

    public void setTextureFromIndex(int index){
        this.caseItem = PrestigeModCommonConfig.getPrestigeCrateItems()[index];
        this.setTexture();
        //TEXTURE = new ResourceLocation("minecraft", "textures/item/" + caseItem.getItem().getName(caseItem).getString().toLowerCase() + ".png");
    }

    private void chooseRandomItem() {
        double totalChance = 100.0;  // This should always be 100 based on your validation
        double chosenChance = RANDOM.nextDouble() * totalChance;
        double cumulativeChance = 0.0;

        ItemStack[] items = PrestigeModCommonConfig.getPrestigeCrateItems();
        for (int i = 0; i < items.length; i++) {
            cumulativeChance += PrestigeModCommonConfig.getPrestigeCrateDropChances().get(i);
            if (chosenChance <= cumulativeChance) {
                this.caseItem = items[i];
                break;
            }
        }
    }

    public void refreshItem(){
        chooseRandomItem();
        this.setTexture();
        //TEXTURE = new ResourceLocation("minecraft", "textures/item/" + caseItem.getItem().getName(caseItem).getString().toLowerCase() + ".png");
        this.itemId = ++ID_COUNTER;
    }

    public ResourceLocation getTEXTURE() {
        return TEXTURE;
    }
    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /*
    public void test(){
        ItemStack item = new ItemStack(Blocks.DIAMOND_BLOCK);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.translate(150, 150, 100.0F);
        poseStack.scale(16.0F, 16.0F, 16.0F); // Scaling

        float angle = (System.currentTimeMillis() / 100) % 360; // Rotation Angle
        Matrix4f m = new PoseStack().last().pose();

        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        //Minecraft.getInstance().getItemRenderer().renderStatic();
        bufferSource.endBatch();
        poseStack.popPose();
        RenderSystem.disableBlend();
    }
     */

    public void movePos(){
        this.x++;
    }

    public int getID(){
        return this.itemId;
    }

    public static void resetIDCounter(){
        ID_COUNTER = 0;
    }

    @Override
    public String toString(){
        return this.TEXTURE.toString()+ "   Item ID : " + this.itemId;
    }
}
