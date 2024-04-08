package net.levelz.mixin.misc;

import net.levelz.stats.PlayerStatsManager;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiConsumer;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {
    @Inject(method = "onExploded", at = @At("HEAD"), cancellable = true)
    public void onExplodedMixin(BlockState state, World world, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> stackMerger, CallbackInfo ci) {
        if (explosion.getCausingEntity() instanceof PlayerEntity player && (PlayerStatsManager.listContainsItemOrBlock(player, Registries.BLOCK.getRawId(state.getBlock()), 1))) {
            ci.cancel();
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

}
