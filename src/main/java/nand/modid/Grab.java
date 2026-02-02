package nand.modid;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Grab extends Item {
    public Grab(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) return TypedActionResult.pass(user.getStackInHand(hand));

        double reach = 20;

        Vec3d start = user.getCameraPosVec(1.0f);
        Vec3d look = user.getRotationVec(1.0f);
        Vec3d end = start.add(look.multiply(reach));

        EntityHitResult result = ProjectileUtil.getEntityCollision(
                world,
                user,
                start,
                end,
                user.getBoundingBox().stretch(look.multiply(reach)).expand(1),
                e -> e instanceof LivingEntity && e != user
        );

        if (result == null) return TypedActionResult.pass(user.getStackInHand(hand));

        Entity target = result.getEntity();

        Vec3d dir = user.getPos().subtract(target.getPos()).normalize();

        target.addVelocity(
                dir.x * 1.5,
                dir.y * 0.5,
                dir.z * 1.5
        );

        target.velocityModified = true;

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
