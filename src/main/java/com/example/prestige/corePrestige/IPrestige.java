package com.example.prestige.corePrestige;

import net.minecraft.nbt.CompoundTag;

public interface IPrestige {
    void setLevel(int level);
    int getLevel();

    void saveNBTData(CompoundTag nbt);
    void loadNBTData(CompoundTag nbt);

    void copyFrom(PlayerPrestige source);
}
