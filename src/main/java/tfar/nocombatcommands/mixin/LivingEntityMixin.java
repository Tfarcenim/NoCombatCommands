package tfar.nocombatcommands.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.nocombatcommands.LivingEntityDuck;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityDuck {

    @Unique
    int lastAttackedByPlayer = -1000000000;

    @Unique
    int lastAttackedPlayer = -1000000000;

    public LivingEntityMixin(EntityType<?> pType, World pLevel) {
        super(pType, pLevel);
    }

    @Inject(method = "setLastHurtByMob",at = @At("RETURN"))
    private void trackPlayer(LivingEntity pLivingEntity, CallbackInfo ci) {
        if (pLivingEntity instanceof PlayerEntity) {
            lastAttackedByPlayer = this.tickCount;
        }
    }

    @Inject(method = "setLastHurtMob",at = @At("RETURN"))
    private void trackHitPlayer(Entity pEntity, CallbackInfo ci) {
        if (pEntity instanceof PlayerEntity) {
            lastAttackedPlayer = this.tickCount;
        }
    }

    @Override
    public int getLastAttackedByPlayer() {
        return lastAttackedByPlayer;
    }

    @Override
    public int getLastAttackedPlayer() {
        return lastAttackedPlayer;
    }
}
