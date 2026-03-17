package com.arroff.mythoria.items;

import com.arroff.mythoria.Config;
import com.arroff.mythoria.Fonts;
import com.arroff.mythoria.MythoriaRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RepairTalisman extends Item implements ICurioItem {

    public RepairTalisman(int tier) {
        super(new Properties().stacksTo(1));
        this.tier = tier;
    }
    int tier;

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltip, flag);
        //Line 1
        tooltip.add(Component.literal("Repairs all items in the inventory").setStyle(Fonts.minecraft).withColor(0x00AAAA));
        //Line 2
        tooltip.add(Component.literal("Lv." + this.tier).setStyle(Fonts.minecraft).withColor(0xFFAA00));
        //Line 3
        int rate = switch (this.tier) {
            case 1 -> Config.REPAIR_TALISMAN1.get();
            case 2 -> Config.REPAIR_TALISMAN2.get();
            case 3 -> Config.REPAIR_TALISMAN3.get();
            case 4 -> Config.REPAIR_TALISMAN4.get();
            case 5 -> Config.REPAIR_TALISMAN5.get();
            default -> 0;
        };
        tooltip.add(Component.literal("Rate: " +  rate + "/s").setStyle(Fonts.minecraft).withColor(0xAA0000));
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        AtomicBoolean result = new AtomicBoolean(false);
        CuriosApi.getCuriosInventory(slotContext.entity()).ifPresent(inventory -> {
            int amount = inventory.findCurios(MythoriaRegistry.Items.REPAIR_TALISMAN1.asItem()).size()
                    + inventory.findCurios(MythoriaRegistry.Items.REPAIR_TALISMAN2.asItem()).size()
                    + inventory.findCurios(MythoriaRegistry.Items.REPAIR_TALISMAN3.asItem()).size()
                    + inventory.findCurios(MythoriaRegistry.Items.REPAIR_TALISMAN4.asItem()).size()
                    + inventory.findCurios(MythoriaRegistry.Items.REPAIR_TALISMAN5.asItem()).size();
            result.set(amount==0);
        });
        return result.get();
    }

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Pre event) {
        //Only run on Server
        if (event.getEntity() instanceof ServerPlayer player) {
            //Check Cooldown
            if (!player.getCooldowns().isOnCooldown(MythoriaRegistry.Items.REPAIR_TALISMAN1.asItem())) {
                CuriosApi.getCuriosInventory(player).ifPresent(inventory -> {
                    player.getCooldowns().addCooldown(MythoriaRegistry.Items.REPAIR_TALISMAN1.asItem(), 20);
                    player.getCooldowns().addCooldown(MythoriaRegistry.Items.REPAIR_TALISMAN2.asItem(), 20);
                    player.getCooldowns().addCooldown(MythoriaRegistry.Items.REPAIR_TALISMAN3.asItem(), 20);
                    player.getCooldowns().addCooldown(MythoriaRegistry.Items.REPAIR_TALISMAN4.asItem(), 20);
                    player.getCooldowns().addCooldown(MythoriaRegistry.Items.REPAIR_TALISMAN5.asItem(), 20);

                    //Calculate total RepairValue
                    int repairValue = inventory.findCurios(MythoriaRegistry.Items.REPAIR_TALISMAN1.asItem()).size() * Config.REPAIR_TALISMAN1.get()
                            + inventory.findCurios(MythoriaRegistry.Items.REPAIR_TALISMAN2.asItem()).size() * Config.REPAIR_TALISMAN2.get()
                            + inventory.findCurios(MythoriaRegistry.Items.REPAIR_TALISMAN3.asItem()).size() * Config.REPAIR_TALISMAN3.get()
                            + inventory.findCurios(MythoriaRegistry.Items.REPAIR_TALISMAN4.asItem()).size() * Config.REPAIR_TALISMAN4.get()
                            + inventory.findCurios(MythoriaRegistry.Items.REPAIR_TALISMAN5.asItem()).size() * Config.REPAIR_TALISMAN5.get();
                    //Repair Inventory
                    for (int i=0; i<=41; i++) {
                        ItemStack stack = player.getInventory().getItem(i);
                        if (stack.isDamaged()) stack.setDamageValue(stack.getDamageValue() - repairValue);
                    }
                });
            }
        }
    }
}