package com.arroff.mythoria;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

public class MythoriaDataGenerator {

    public static class Lang extends LanguageProvider {
        public Lang(PackOutput output) {super(output, Mythoria.MODID,"en_us");}

        @Override
        protected void addTranslations() {
//          tab(MythoriaRegistry.CreativeTabs.TAB_BLOCKS);
            tab(MythoriaRegistry.CreativeTabs.TAB_ITEMS);

            MythoriaRegistry.dataRegistry.forEach(object -> {
                if(object.get() instanceof Item item) add(item, translateName(object.getId().getPath()));
                if(object.get() instanceof Block block) add(block, translateName(object.getId().getPath()));
            });
        }

        private void tab(DeferredHolder<CreativeModeTab, CreativeModeTab> tab) {
            add(tab.get().getDisplayName().getString(), Mythoria.class.getSimpleName() + " " + translateName(tab.getId().getPath()));
        }

        private static String translateName(String in) {
            StringBuilder text = new StringBuilder(in.substring(0, 1).toUpperCase()); //First Letter always uppercase
            for(int i=1; i<in.length(); i++) {
                if (in.charAt(i) == '_') {
                    text.append(" ");
                    i++;
                    if (in.length() > i)
                        text.append(Character.toString(in.charAt(i)).toUpperCase()); //Uppercase after underscore and replace with space
                } else {
                    text.append(in.charAt(i)); //Just add to new text
                }
            }
            Mythoria.LOGGER.info("Translating " + in + " to " + text);
            return text.toString();
        }
    }

    public static class BlockModels extends BlockStateProvider {
        public BlockModels(PackOutput output, ExistingFileHelper existingFileHelper) {super(output, Mythoria.MODID, existingFileHelper);}

        @Override
        protected void registerStatesAndModels() {
            MythoriaRegistry.dataRegistry.forEach(object -> {
                if(object.get() instanceof Block block) simpleBlock(block);
            });
        }
    }

    public static class ItemModels extends ItemModelProvider {
        public ItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {super(output, Mythoria.MODID, existingFileHelper);}

        @Override
        protected void registerModels() {
            MythoriaRegistry.dataRegistry.forEach(object -> {
                if(object.get() instanceof Item item) basicItem(item);
                if(object.get() instanceof Block block) simpleBlockItem(block);
            });
        }
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(),new Lang(output));
        generator.addProvider(event.includeClient(),new BlockModels(output, existingFileHelper));
        generator.addProvider(event.includeClient(),new ItemModels(output, existingFileHelper));
    }
}