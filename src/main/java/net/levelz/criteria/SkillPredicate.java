package net.levelz.criteria;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.mojang.serialization.Codec;
import net.minecraft.util.JsonHelper;

public class SkillPredicate {

    public static final Codec<SkillPredicate> CODEC = Codec.STRING.xmap(SkillPredicate::new, skillPredicate -> skillPredicate.jobName);
    private final String jobName;

    public SkillPredicate(String jobName) {
        this.jobName = jobName;
    }

    public boolean test(String jobName) {
        if (this.jobName.equals(jobName)) {
            return true;
        } else {
            return false;
        }
    }

    public static SkillPredicate fromJson(JsonElement json) {
        String jobName = JsonHelper.asString(json, "skill_name");
        return new SkillPredicate(jobName);
    }

    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("skill_name", this.jobName);
        return jsonObject;
    }

}
