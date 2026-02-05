package nand.modid.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TNTSword extends Item {
    public TNTSword(Settings settings) {
        super(settings);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        if (!world.isClient && miner instanceof PlayerEntity player) {

            TntEntity tnt = new TntEntity(
                    world,
                    pos.getX() + 0.5,
                    pos.getY() + 1,
                    pos.getZ() + 0.5,
                    player
            );

            tnt.setFuse(40); // 40틱 = 2초
            world.spawnEntity(tnt);
            player.addStatusEffect(
                    new StatusEffectInstance(
                            StatusEffects.RESISTANCE,
                            50,
                            4,
                            false,
                            false,
                            false
                    )
            );
            player.sendMessage(Text.literal("💣 TNT 소환!"), false);
        }
        return true;
    }
}
