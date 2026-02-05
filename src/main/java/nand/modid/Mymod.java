package nand.modid;

import nand.modid.registry.ModItems;
import net.fabricmc.api.ModInitializer;

public class Mymod implements ModInitializer {
	public static final String MOD_ID = "mymod";

	@Override
	public void onInitialize() {
		// 아이템 등록
		ModItems.register();
	}
}
