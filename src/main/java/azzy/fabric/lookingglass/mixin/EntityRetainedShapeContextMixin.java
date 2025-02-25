package azzy.fabric.lookingglass.mixin;

import azzy.fabric.lookingglass.util.ShapeContextMixinInterface;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityShapeContext.class)
public class EntityRetainedShapeContextMixin implements ShapeContextMixinInterface {
    @Unique
    public Entity entity;

    @Inject(method = "<init>(Lnet/minecraft/entity/Entity;)V", at = @At("TAIL"))
    public void init(Entity entity, CallbackInfo callbackInfo) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public Entity setEntity(Entity storedEntity) {
        return this.entity = storedEntity;
    }
}