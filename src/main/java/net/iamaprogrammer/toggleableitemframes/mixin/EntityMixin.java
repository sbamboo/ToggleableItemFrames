package net.iamaprogrammer.toggleableitemframes.mixin;

import net.iamaprogrammer.toggleableitemframes.ToggleableItemFramesClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Entity.class)
public class EntityMixin {
    @Shadow private Level level;
    @Unique
    private final Entity TARGET = (Entity)(Object)this;

    @Inject(method = "isInvisible", at = @At("RETURN"), cancellable = true)
    private void toggleableitemframes_ShowInvisibleFramesWhenHeld(CallbackInfoReturnable<Boolean> cir) {
        if (this.TARGET instanceof ItemFrame && this.level.isClientSide() && ToggleableItemFramesClient.CONFIG.shouldShowInvisibleFramesWhenHeld()) {
            Minecraft client = Minecraft.getInstance();
            LocalPlayer player = client.player;

            if (player != null) {
                if (player.isHolding(Items.ITEM_FRAME) || player.isHolding(Items.GLOW_ITEM_FRAME)) {
                    cir.setReturnValue(false);
                    return;
                }
            }

            cir.setReturnValue(cir.getReturnValue() || ToggleableItemFramesClient.ALWAYS_INVISIBLE);
        }
    }
}
