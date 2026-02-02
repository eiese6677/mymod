package nand.modid;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
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
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.projectile.ProjectileUtil;
import java.util.Iterator;
import java.util.List;


public class TestItem extends Item {

    public TestItem(Settings settings) {
        super(settings);
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

        return true;
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
