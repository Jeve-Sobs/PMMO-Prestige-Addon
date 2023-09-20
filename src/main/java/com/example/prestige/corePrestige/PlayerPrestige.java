package com.example.prestige.corePrestige;

import com.example.prestige.config.PrestigeModCommonConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.Random;

public class PlayerPrestige implements IPrestige {
    private int level;
    private int prestigeCrates;
    private int prestigeItemIndex;
    private static final Random RANDOM = new Random();


    private final int MAX_PRESTIGE = 20;

    public PlayerPrestige(){
        this.level = 0;
        this.prestigeCrates = 0;
        this.prestigeItemIndex = getRandomInt();
    }

    /**
     * Refreshes the item index, this is called after the player gets their item
     */
    public void refreshCaseIndex(){
        this.prestigeItemIndex = getRandomInt();
    }

    /**
     * Gets the index of the next item unbox using config item weights
     * @return index of item you will open
     */
    private int getRandomInt(){
        double totalChance = 100.0;  // This should always be 100 based on your validation
        double chosenChance = RANDOM.nextDouble() * totalChance;
        double cumulativeChance = 0.0;

        ItemStack[] items = PrestigeModCommonConfig.getPrestigeCrateItems();
        for (int i = 0; i < items.length; i++) {
            cumulativeChance += PrestigeModCommonConfig.getPrestigeCrateDropChances().get(i);
            if (chosenChance <= cumulativeChance) {
                return i;
            }
        }
        throw new IllegalStateException("Error getting case item index");
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public void setPrestigeCrates(int crates) {
        this.prestigeCrates = crates;
    }

    @Override
    public int getPrestigeCrates() {
        return this.prestigeCrates;
    }

    @Override
    public void setPrestigeItemIndex(int index) {
        this.prestigeItemIndex = index;
    }

    @Override
    public int getPrestigeItemIndex() {
        return this.prestigeItemIndex;
    }

    @Override
    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("prestigeLevel", this.level);
        nbt.putInt("prestigeCrates", this.prestigeCrates);
        nbt.putInt("prestigeItemIndex", this.prestigeItemIndex);
    }

    @Override
    public void loadNBTData(CompoundTag nbt) {
        level = nbt.getInt("prestigeLevel");
        prestigeCrates = nbt.getInt("prestigeCrates");
        prestigeItemIndex = nbt.getInt("prestigeItemIndex");
    }

    @Override
    public void copyFrom(PlayerPrestige source) {
        this.level = source.level;
        this.prestigeCrates = source.prestigeCrates;
        this.prestigeItemIndex = source.prestigeItemIndex;
    }
}
