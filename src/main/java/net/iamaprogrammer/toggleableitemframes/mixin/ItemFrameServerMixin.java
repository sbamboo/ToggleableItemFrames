package net.iamaprogrammer.toggleableitemframes.mixin;


import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemFrame.class)
public abstract class ItemFrameServerMixin extends HangingEntity {
    @Shadow public abstract SoundEvent getRotateItemSound();
    protected ItemFrameServerMixin(EntityType<? extends HangingEntity> entityType, Level world) {
        super(entityType, world);
    }
    @Inject(
            method = "interact(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/InteractionResult;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void toggleableitemframes_ToggleItemFrame(
            Player player,
            InteractionHand hand,
            Vec3 location,
            CallbackInfoReturnable<InteractionResult> cir
    ) {
        if (!this.level().isClientSide() && !player.isSpectator() && player.getMainHandItem().isEmpty() && player.isShiftKeyDown() && hand.equals(InteractionHand.MAIN_HAND)) {
            this.setInvisible(!this.isInvisible());
            this.playSound(this.getRotateItemSound(), 1.0f, 1.0f);
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
