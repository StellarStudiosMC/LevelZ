package net.levelz.mixin.player;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.levelz.access.PlayerBreakBlockAccess;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin implements PlayerBreakBlockAccess {

    @Shadow
    @Mutable
    @Final
    public PlayerEntity player;

    @Shadow
    @Mutable
    @Final
    public DefaultedList<ItemStack> main;

    @Shadow
    public int selectedSlot;

    @Unique
    public boolean canBreakBlock = true;

    @Unique
    public float blockBreakExtraDelta = 1.0F;

    @Inject(method = "getBlockBreakingSpeed", at = @At(value = "HEAD"), cancellable = true)
    private void getBlockBreakingSpeedMixin(BlockState block, CallbackInfoReturnable<Float> info) {
        if (!this.canBreakBlock)
            info.setReturnValue(1.0F);
    }

    @Override
    public void levelZ$setInventoryBlockBreakable(boolean breakable) {
        this.canBreakBlock = breakable;
    }

    @Override
    public void levelZ$setAbstractBlockBreakDelta(float breakingDelta) {
        this.blockBreakExtraDelta = breakingDelta;
    }

    @Override
    public float levelZ$getBreakingAbstractBlockDelta() {
        return this.blockBreakExtraDelta;
    }

}
