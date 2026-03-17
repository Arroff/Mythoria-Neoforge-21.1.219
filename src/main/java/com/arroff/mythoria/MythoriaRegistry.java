package com.arroff.mythoria;

import com.arroff.mythoria.items.RepairTalisman;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MythoriaRegistry {
    protected static List<DeferredHolder<?, ?>> dataRegistry = new ArrayList<>();

    protected static void register(IEventBus bus) {
        Blocks.REGISTER.register(bus);
        Items.REGISTER.register(bus);
        CreativeTabs.REGISTER.register(bus);
    }

    //Creative Tabs
    public static class CreativeTabs {
        public static final DeferredRegister<CreativeModeTab> REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Mythoria.MODID);

        //Blocks Tab
//        public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_BLOCKS = createTab("blocks", Blocks.DUMMY_BLOCK, (parameters, output) -> {
//            MythoriaRegistry.dataRegistry.forEach(object -> {
//                if(object.get() instanceof Block block) output.accept(block);
//            });
//        });
        //Items Tab
        public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB_ITEMS = createTab("items", Items.REPAIR_TALISMAN1, (parameters, output) ->
                MythoriaRegistry.dataRegistry.forEach(object -> {
                    if(object.get() instanceof Item item) output.accept(item);
                })
        );

        //Logic
        private static DeferredHolder<CreativeModeTab, CreativeModeTab> createTab(String name, DeferredItem<Item> icon, CreativeModeTab.DisplayItemsGenerator itemList) {
            return REGISTER.register(name, () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + Mythoria.MODID + "." + name))
                    .icon(icon::toStack)
                    .displayItems(itemList)
                    .build());
        }
        private static DeferredHolder<CreativeModeTab, CreativeModeTab> createTab(String name, DeferredBlock<Block> icon, CreativeModeTab.DisplayItemsGenerator itemList) {
            return REGISTER.register(name, () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + Mythoria.MODID + "." + name))
                    .icon(icon::toStack)
                    .displayItems(itemList)
                    .build());
        }
    }

    //Items
    public static class Items {
        public static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(Mythoria.MODID);

        //public static final DeferredItem<Item> DUMMY_ITEM = addSimple("dummy_item");
        public static final DeferredItem<Item> REPAIR_TALISMAN1 = add("repair_talisman1", () -> new RepairTalisman(1));
        public static final DeferredItem<Item> REPAIR_TALISMAN2 = add("repair_talisman2", () -> new RepairTalisman(2));
        public static final DeferredItem<Item> REPAIR_TALISMAN3 = add("repair_talisman3", () -> new RepairTalisman(3));
        public static final DeferredItem<Item> REPAIR_TALISMAN4 = add("repair_talisman4", () -> new RepairTalisman(4));
        public static final DeferredItem<Item> REPAIR_TALISMAN5 = add("repair_talisman5", () -> new RepairTalisman(5));

        //Logic
        private static DeferredItem<Item> add(String name, Supplier<Item> sup) {
            DeferredItem<Item> var = REGISTER.register(name, sup);
            dataRegistry.add(var);
            return var;
        }
        private static DeferredItem<Item> addSimple(String name) {
            DeferredItem<Item> var = REGISTER.registerSimpleItem(name);
            dataRegistry.add(var);
            return var;
        }
    }

    //Blocks
    public static class Blocks {
        public static final DeferredRegister.Blocks REGISTER = DeferredRegister.createBlocks(Mythoria.MODID);

        //public static final DeferredBlock<Block> DUMMY_BLOCK = add("dummy_block", () -> new Block(BlockBehaviour.Properties.of()));

        //Logic
        private static DeferredBlock<Block> add(String name, Supplier<Block> sup) {
            DeferredBlock<Block> block = REGISTER.register(name, sup);
            Items.REGISTER.registerSimpleBlockItem(name, block);
            dataRegistry.add(block);
            return block;
        }
    }
}