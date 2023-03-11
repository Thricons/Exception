package cn.exception.mixins;


import cn.exception.utils.BlurUtil;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Inject(method = "func_147119_ah", at = @At("RETURN"))
    public void onResize(CallbackInfo callbackInfo){
        BlurUtil.onFrameBufferResize(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
    }
}
