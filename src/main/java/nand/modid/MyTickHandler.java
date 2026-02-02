package nand.modid;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

public class MyTickHandler {

    public static void drawCircle(World world, Vec3d center, double radius) {
        for (int i = 0; i < 360; i += 10) {
            double rad = Math.toRadians(i);
            double x = center.x + Math.cos(rad) * radius;
            double z = center.z + Math.sin(rad) * radius;

            // (x, z)에서 가장 높은 블록 찾기
            int topY = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    (int)x,
                    (int)z);

            world.addParticle(
                    ParticleTypes.END_ROD,
                    x + 0.5,
                    topY + 1.0,   // 블록 위
                    z + 0.5,
                    0, 0, 0
            );
        }
    }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

                World world = player.getWorld();
                Vec3d center = player.getPos();

                drawCircle(world, center, 5.0); // 반지름 5블록
            }
        });
    }
}
