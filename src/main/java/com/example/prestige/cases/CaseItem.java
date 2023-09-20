package com.example.prestige.cases;

import com.example.prestige.ExampleMod;
import com.example.prestige.config.PrestigeModCommonConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

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
        TEXTURE = new ResourceLocation("minecraft", "textures/item/"+item.getItem().getName(item).getString().toLowerCase()+".png");
        this.x = 0;
        this.y = 0;
        //ItemStack itemStack = new ItemStack(Items.DIAMOND, 1); // Change Items.DIAMOND to whatever item you want.
    }

    public CaseItem() {
        this.itemId = ++ID_COUNTER;
        chooseRandomItem();
        TEXTURE = new ResourceLocation("minecraft", "textures/item/" + caseItem.getItem().getName(caseItem).getString().toLowerCase() + ".png");
        this.x = 0;
        this.y = 0;
    }

    public void setTextureFromIndex(int index){
        this.caseItem = PrestigeModCommonConfig.getPrestigeCrateItems()[index];
        TEXTURE = new ResourceLocation("minecraft", "textures/item/" + caseItem.getItem().getName(caseItem).getString().toLowerCase() + ".png");
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
        TEXTURE = new ResourceLocation("minecraft", "textures/item/" + caseItem.getItem().getName(caseItem).getString().toLowerCase() + ".png");
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
