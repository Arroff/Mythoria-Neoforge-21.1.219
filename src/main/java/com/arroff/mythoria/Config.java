package com.arroff.mythoria;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<Integer> REPAIR_TALISMAN1 = BUILDER.comment("How much durability the talisman repairs each second")
                                                                                     .define("repair_talisman1", 1);
    public static final ModConfigSpec.ConfigValue<Integer> REPAIR_TALISMAN2 = BUILDER.define("repair_talisman2", 2);
    public static final ModConfigSpec.ConfigValue<Integer> REPAIR_TALISMAN3 = BUILDER.define("repair_talisman3", 4);
    public static final ModConfigSpec.ConfigValue<Integer> REPAIR_TALISMAN4 = BUILDER.define("repair_talisman4", 8);
    public static final ModConfigSpec.ConfigValue<Integer> REPAIR_TALISMAN5 = BUILDER.define("repair_talisman5", 16);

    static final ModConfigSpec SPEC = BUILDER.build();
}
