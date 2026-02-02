package nand.modid;

import net.fabricmc.api.ModInitializer;

public class Mymod implements ModInitializer {
	public static final String MOD_ID = "mymod";

	@Override
	public void onInitialize() {
		ModItems.register();
	}
}
