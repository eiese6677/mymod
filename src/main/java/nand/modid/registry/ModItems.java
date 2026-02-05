package nand.modid.registry;

import nand.modid.Mymod;
import nand.modid.item.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

public class ModItems {

    // 아이템 인스턴스 생성
    public static final Item LIGHTNING_BOLT = new LightningBolt();
    public static final Item GRAB = new Grab(new Item.Settings());
    public static final Item TNT_SWORD = new TNTSword(new Item.Settings());
    public static final Item EFFECT_SWORD = new EffectSword(new Item.Settings());
    public static void register() {
        Registry.register(
                Registries.ITEM,
                Identifier.of(Mymod.MOD_ID, "lightning_bolt"),
                LIGHTNING_BOLT
        );

        Registry.register(
                Registries.ITEM,
                Identifier.of(Mymod.MOD_ID, "grab"),
                GRAB
        );

        Registry.register(
                Registries.ITEM,
                Identifier.of(Mymod.MOD_ID, "tnt_sword"),
                TNT_SWORD
        );

        Registry.register(
                Registries.ITEM,
                Identifier.of(Mymod.MOD_ID, "effect_sword"),
                EFFECT_SWORD
        );

        // 아이템 그룹에 추가
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> {
            content.add(LIGHTNING_BOLT);
            content.add(GRAB);
            content.add(TNT_SWORD);
            content.add(EFFECT_SWORD);
        });
    }
}
