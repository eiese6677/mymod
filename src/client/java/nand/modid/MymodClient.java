package nand.modid;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class MymodClient implements ClientModInitializer {

	public static KeyBinding DASH_KEY;

	@Override
	public void onInitializeClient() {
		DASH_KEY = KeyBindingHelper.registerKeyBinding(
				new KeyBinding(
						"key.mymod.dash",
						InputUtil.Type.KEYSYM,
						GLFW.GLFW_KEY_LEFT_SHIFT,
						"category.mymod"
				)
		);
		ClientTickEvents.END_CLIENT_TICK.register(client ->
		{
			while (DASH_KEY.wasPressed()) {
				System.out.println("Shift 한번 눌림");
				ClientPlayNetworking.send(DashPacket.ID, PacketByteBufs.empty());
			}
		});
	}
}