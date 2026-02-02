package nand.modid;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.math.Vec3d;

public class Mymod implements ModInitializer {

	@Override
	public void onInitialize() {

		ModItems.register();

		ServerPlayNetworking.registerGlobalReceiver(
				DashPacket.ID,
				context -> {
					context.server().execute(() -> {
						var player = context.player();
						Vec3d look = player.getRotationVec(1.0f);

						player.addVelocity(
								look.x * 2.0,
								0.1,
								look.z * 2.0
						);
						player.velocityModified = true;
					});
				}
		);
	}
}
