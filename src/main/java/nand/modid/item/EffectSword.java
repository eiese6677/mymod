package nand.modid.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class EffectSword extends Item {
    public EffectSword(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.RESISTANCE,
                        20*10,
                        3,
                        false,
                        true,
                        true
                )
        );
        user.addStatusEffect(
                new StatusEffectInstance(
                        StatusEffects.INSTANT_HEALTH,
                        20*10,
                        5,
                        false,
                        true,
                        true
                )
        );
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
