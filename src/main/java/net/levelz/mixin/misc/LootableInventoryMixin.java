package net.levelz.mixin.misc;

import net.levelz.access.PlayerDropAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.LootableInventory;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LootableInventory.class)
public interface LootableInventoryMixin {

    @Inject(method = "generateLoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/loot/context/LootContextParameterSet$Builder;luck(F)Lnet/minecraft/loot/context/LootContextParameterSet$Builder;"))
    private void checkLootInteractionMixin(@Nullable PlayerEntity player, CallbackInfo info) {
        ((PlayerDropAccess) player).resetKilledMobStat();
    }
}
