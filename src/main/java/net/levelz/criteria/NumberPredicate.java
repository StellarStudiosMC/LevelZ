package net.levelz.criteria;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import net.minecraft.util.JsonHelper;

public class NumberPredicate {
    private final int levelZ;
    public static final Codec<NumberPredicate> CODEC = Codec.INT.xmap(NumberPredicate::new, (numberPredicate) -> numberPredicate.levelZ);

    public NumberPredicate(int levelZ) {
        this.levelZ = levelZ;
    }

    public boolean test(int level) {
        return this.levelZ == 0 || this.levelZ == level;
    }

}
