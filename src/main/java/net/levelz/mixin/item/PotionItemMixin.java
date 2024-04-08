package net.levelz.mixin.item;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.At;

import net.levelz.access.PlayerStatsManagerAccess;
import net.levelz.init.ConfigInit;
import net.levelz.stats.Skill;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.world.World;

@Mixin(PotionItem.class)
public class PotionItemMixin {

    @ModifyVariable(
            method = "finishUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/potion/PotionUtil;getPotionEffects(Lnet/minecraft/item/ItemStack;)Ljava/util/List;",
                    ordinal = 0
            ),
            ordinal = 0,
            name = "list"
    )
    private List<StatusEffectInstance> finishUsingMixin(List<StatusEffectInstance> original, ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity) {
            int alchemyLevel = ((PlayerStatsManagerAccess) user).getPlayerStatsManager().getSkillLevel(Skill.ALCHEMY);
            if (alchemyLevel >= ConfigInit.CONFIG.maxLevel && (float) alchemyLevel * ConfigInit.CONFIG.alchemyPotionChance > world.random.nextFloat()) {
                List<StatusEffectInstance> newEffectList = new ArrayList<>();
                for (StatusEffectInstance statusEffectInstance : original) {
                    newEffectList.add(
                            new StatusEffectInstance(statusEffectInstance.getEffectType(), statusEffectInstance.getEffectType().isInstant() ? statusEffectInstance.getDuration() : statusEffectInstance.getDuration() * 2,
                                    statusEffectInstance.getEffectType().isInstant() ? statusEffectInstance.getAmplifier() + 1 : statusEffectInstance.getAmplifier(), statusEffectInstance.isAmbient(),
                                    statusEffectInstance.shouldShowParticles(), statusEffectInstance.shouldShowIcon()));
                }
                return newEffectList;
            }
        }
        return original;
    }
}
