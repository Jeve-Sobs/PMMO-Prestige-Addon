package com.example.prestige.corePrestige;

import net.minecraft.nbt.CompoundTag;

public class PlayerPrestige implements IPrestige {
    private int level;

    private final int MAX_PRESTIGE = 20;

    public PlayerPrestige(){
        this.level = 0;
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
    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("prestigeLevel",this.level);
    }

    @Override
    public void loadNBTData(CompoundTag nbt) {
        level = nbt.getInt("prestigeLevel");
    }

    @Override
    public void copyFrom(PlayerPrestige source) {
        this.level = source.level;
    }
}
