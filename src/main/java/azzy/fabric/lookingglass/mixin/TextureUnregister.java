package azzy.fabric.lookingglass.mixin;

import azzy.fabric.lookingglass.util.client.TexManRegEdit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.texture.TextureTickListener;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.Set;


@Mixin(TextureManager.class)
public abstract class TextureUnregister implements TexManRegEdit {

    @Shadow
    @Final
    private Map<Identifier, AbstractTexture> textures;

    @Shadow
    @Final
    private Set<TextureTickListener> tickListeners;

    @Environment(EnvType.CLIENT)
    public void unregisterTexture(final Identifier id) {
        final AbstractTexture texture = textures.get(id);

        if (texture instanceof TextureTickListener) {
            tickListeners.remove(texture);
        }

        textures.remove(id);
    }
}
