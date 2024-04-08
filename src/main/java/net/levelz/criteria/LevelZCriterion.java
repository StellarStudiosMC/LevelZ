package net.levelz.criteria;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.entity.LootContextPredicateValidator;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;

public class LevelZCriterion extends AbstractCriterion<LevelZCriterion.Conditions> {

    public void trigger(ServerPlayerEntity player, int level) {
        this.trigger(player, conditions -> conditions.matches(level));
    }

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<NumberPredicate> level) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player")
                                .forGetter(Conditions::player),
                        Codecs.createStrictOptionalFieldCodec(NumberPredicate.CODEC, "level")
                                .forGetter(Conditions::level))
                .apply(instance, Conditions::new));

        public boolean matches(int level) {
            return this.level.map(predicate -> predicate.test(level)).orElse(false);
        }

        @Override
        public void validate(LootContextPredicateValidator validator) {
            AbstractCriterion.Conditions.super.validate(validator);
            validator.validateEntityPredicate(this.player, ".player");
        }

    }

}
