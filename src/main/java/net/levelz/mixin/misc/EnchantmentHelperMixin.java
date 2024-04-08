package net.levelz.mixin.misc;

import net.levelz.access.PlayerStatsManagerAccess;
import net.levelz.data.LevelLists;
import net.levelz.init.ConfigInit;
import net.levelz.stats.PlayerStatsManager;
import net.levelz.stats.Skill;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @ModifyVariable(method = "getEquipmentLevel", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/lang/Iterable;iterator()Ljava/util/Iterator;"), ordinal = 0)
    private static int getEquipmentLevelMixin(int original, Enchantment enchantment, LivingEntity entity) {
        if (original != 0 && entity instanceof PlayerEntity player && (float) ((PlayerStatsManagerAccess) player).getPlayerStatsManager().getSkillLevel(Skill.ALCHEMY)
                * ConfigInit.CONFIG.alchemyEnchantmentChance > entity.getWorld().getRandom().nextFloat())
            return original += 1;
        else
            return original;
    }

    @Inject(method = "onTargetDamaged", at = @At("HEAD"), cancellable = true)
    private static void onTargetDamagedMixin(LivingEntity user, Entity target, CallbackInfo info) {
        checkItem(user, info);
    }

    @Inject(method = "onUserDamaged", at = @At("HEAD"), cancellable = true)
    private static void onUserDamagedMixin(LivingEntity user, Entity attacker, CallbackInfo info) {
        checkItem(user, info);
    }

    private static void checkItem(LivingEntity user, CallbackInfo info) {
        if (user instanceof PlayerEntity playerEntity) {
            Item item = user.getStackInHand(user.getActiveHand()).getItem();
            if (item instanceof ToolItem) {
                ArrayList<Object> levelList = LevelLists.customItemList;
                if (!levelList.isEmpty() && levelList.contains(Registries.ITEM.getId(item).toString())) {
                    if (!PlayerStatsManager.playerLevelisHighEnough(playerEntity, levelList, Registries.ITEM.getId(item).toString(), true))
                        info.cancel();
                } else {
                    levelList = null;
                    if (item instanceof SwordItem) {
                        levelList = LevelLists.swordList;
                    } else if (item instanceof AxeItem)
                        levelList = LevelLists.axeList;
                    else if (item instanceof HoeItem)
                        levelList = LevelLists.hoeList;
                    else if (item instanceof PickaxeItem || item instanceof ShovelItem)
                        levelList = LevelLists.toolList;
                    if (levelList != null && (!PlayerStatsManager.playerLevelisHighEnough(playerEntity, levelList, ((ToolItem) item).getMaterial().toString().toLowerCase(), true)))
                            info.cancel();
                }
            }
        }
    }
}
