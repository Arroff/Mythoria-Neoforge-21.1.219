package com.arroff.mythoria;

import com.arroff.mythoria.items.RepairTalisman;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Mythoria.MODID)
public class Mythoria {
    public static final String MODID = "mythoria";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public Mythoria(IEventBus bus, ModContainer modContainer) {
        bus.addListener(MythoriaDataGenerator::gatherData);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        MythoriaRegistry.register(bus);

        NeoForge.EVENT_BUS.addListener(RepairTalisman::playerTick);
    }
}
