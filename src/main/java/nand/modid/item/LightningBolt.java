package nand.modid.item;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.util.math.Box;

import java.util.Iterator;
import java.util.List;


public class LightningBolt extends Item {

    public LightningBolt() {
        super(new Item.Settings()
                .maxDamage(100) // 내구도 100
        );
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            Box box = user.getBoundingBox().expand(10);
            List<LivingEntity> targets =
                    world.getEntitiesByClass(
                            LivingEntity.class,
                            box,
                            e -> e != user
                    );

            Iterator<LivingEntity> it = targets.iterator();

            user.sendMessage(Text.literal("벽력일섬!"), false);

            ServerTickEvents.END_SERVER_TICK.register(server -> {
                if (it.hasNext()) {
                    LivingEntity target = it.next();
                    ServerPlayerEntity player = (ServerPlayerEntity) user;

                    player.teleport(
                            player.getServerWorld(),
                            target.getX(),
                            target.getY(),
                            target.getZ(),
                            player.getYaw(),
                            player.getPitch()
                    );

                    // 무적
                    player.addStatusEffect(
                            new StatusEffectInstance(
                                    StatusEffects.RESISTANCE,
                                    20 * 3,
                                    4,
                                    false,
                                    false,
                                    false
                            )
                    );
                    player.addStatusEffect(
                            new StatusEffectInstance(
                                    StatusEffects.INVISIBILITY,
                                    20,
                                    0,
                                    false,
                                    false,
                                    false
                            )
                    );

                    LightningEntity lightning = new LightningEntity(
                            EntityType.LIGHTNING_BOLT,
                            world
                    );

                    lightning.setPosition(
                            player.getX(),
                            player.getY(),
                            player.getZ()
                    );

                    world.spawnEntity(lightning);
                }
            });

        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.getWorld().isClient) {
            World world = target.getWorld();

            LightningEntity lightning = new LightningEntity(
                    EntityType.LIGHTNING_BOLT,
                    world
            );

            lightning.setPosition(
                    target.getX(),
                    target.getY(),
                    target.getZ()
            );

            world.spawnEntity(lightning);
        }

        attacker.heal(5.0f);
        target.setHealth(target.getHealth());

        return true;
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        // FIRE만 캘 수 있음
        return state.isOf(net.minecraft.block.Blocks.FIRE);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        return false;
    }
}
