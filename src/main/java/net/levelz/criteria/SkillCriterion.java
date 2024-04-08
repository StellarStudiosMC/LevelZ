package net.levelz.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.entity.LootContextPredicateValidator;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;

public class SkillCriterion extends AbstractCriterion<SkillCriterion.Conditions> {

    public void trigger(ServerPlayerEntity player, String skillName, int skillLevel) {
        this.trigger(player, conditions -> conditions.matches(skillName, skillLevel));
    }

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<SkillPredicate> skillName, Optional<NumberPredicate> skillLevel) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = Codecs.validate(RecordCodecBuilder.create(instance -> instance.group(
                        EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC
                                .optionalFieldOf("player")
                                .forGetter(Conditions::player),
                        SkillPredicate.CODEC
                                .optionalFieldOf("skill_name")
                                .forGetter(Conditions::skillName),
                        NumberPredicate.CODEC
                                .optionalFieldOf("skill_level")
                                .forGetter(Conditions::skillLevel))
                .apply(instance, Conditions::new)), Conditions::validate);

        private static DataResult<Conditions> validate(Conditions conditions) {
            if (conditions.player.isPresent() && conditions.skillName.isPresent() && conditions.skillLevel.isPresent()) {
                return DataResult.success(conditions);
            } else {
                return DataResult.error(() -> "A skill criterion needs a player, skill name, and skill level");
            }
        }

        public boolean matches(String skillName, int skillLevel) {
            return this.skillName.map(predicate -> predicate.test(skillName)).orElse(false) &&
                    this.skillLevel.map(predicate -> predicate.test(skillLevel)).orElse(false);
        }
        @Override
        public void validate(LootContextPredicateValidator validator) {
            AbstractCriterion.Conditions.super.validate(validator);
            validator.validateEntityPredicate(this.player, ".player");
        }
    }

}
