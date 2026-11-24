package pet.aprl.hintfix.mixin;

import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.freetype.FTC_ImageType;
import org.lwjgl.util.freetype.FreeType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FreeType.class, remap = false)
abstract class FreeTypeMixin {
	@Unique
	private static final int UNHINTED = FreeType.FT_LOAD_NO_HINTING | FreeType.FT_LOAD_NO_AUTOHINT;

	@ModifyVariable(method = {"nFT_Load_Glyph", "nFT_Get_Advance"}, at = @At("HEAD"), argsOnly = true, ordinal = 1)
	private static int loadGlyph(int flags) {
		return flags | UNHINTED;
	}

	@ModifyVariable(method = "nFT_Load_Char", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private static int loadChar(int flags) {
		return flags | UNHINTED;
	}

	@ModifyVariable(method = "nFT_Get_Advances", at = @At("HEAD"), argsOnly = true, ordinal = 2)
	private static int getAdvances(int flags) {
		return flags | UNHINTED;
	}

	@ModifyVariable(
		method = {"nFTC_ImageCache_LookupScaler", "nFTC_SBitCache_LookupScaler"},
		at = @At("HEAD"),
		argsOnly = true,
		ordinal = 2
	)
	private static long lookupScaler(long flags) {
		return flags | UNHINTED;
	}

	@Inject(method = {"nFTC_ImageCache_Lookup", "nFTC_SBitCache_Lookup"}, at = @At("HEAD"))
	private static void lookup(long cache, long type, int gindex, long out, long node, CallbackInfoReturnable<Integer> cir) {
		if (type == 0) {
			return;
		}
		long flags = type + FTC_ImageType.FLAGS;
		MemoryUtil.memPutInt(flags, MemoryUtil.memGetInt(flags) | UNHINTED);
	}
}
