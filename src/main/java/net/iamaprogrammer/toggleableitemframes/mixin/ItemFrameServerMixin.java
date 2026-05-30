package net.iamaprogrammer.toggleableitemframes.mixin;


import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
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
    @Inject(method = "interact", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/Level;isClientSide:Z", opcode = Opcodes.GETFIELD, shift = At.Shift.AFTER), cancellable = true)
    private void toggleableitemframes_ToggleItemFrame(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (!this.getWorld().isClientSide() && !player.isSpectator() && player.getMainHandItem().isEmpty() && player.isShiftKeyDown() && hand.equals(InteractionHand.MAIN_HAND)) {
            this.setInvisible(!this.isInvisible());
            this.playSound(this.getRotateItemSound(), 1.0f, 1.0f);
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
