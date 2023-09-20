package com.example.prestige.corePrestige;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerPrestigeProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerPrestige> PLAYER_PRESTIGE = CapabilityManager.get(new CapabilityToken<PlayerPrestige>(){});

    private PlayerPrestige prestige = null;
    private final LazyOptional<PlayerPrestige> optional = LazyOptional.of(this::createPlayerPrestige);

    private PlayerPrestige createPlayerPrestige(){
        if (this.prestige== null){
            this.prestige = new PlayerPrestige();
        }
        return this.prestige;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == PLAYER_PRESTIGE){
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        createPlayerPrestige().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerPrestige().loadNBTData(nbt);
    }
}
