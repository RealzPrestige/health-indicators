package indicators;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer<T extends Entity> {


    @Shadow
    protected abstract boolean hasLabel(T entity);

    @Shadow
    protected abstract void renderLabelIfPresent(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);

    /**
     * @author zPrestige_
     * @reason lean
     */

    @Overwrite
    public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (this.hasLabel(entity)) {
            String text = entity.getDisplayName().getString();
            if (entity instanceof PlayerEntity){
                float health = (float) Math.ceil(getHealth(entity));
                String add = " | " + getColor(health) + health + Formatting.RESET + " | ";
                text += add;
            }
            this.renderLabelIfPresent(entity, Text.of(text), matrices, vertexConsumers, light);
        }
    }

    private Formatting getColor(float health) {
        if (health <= 5.0f) {
            return Formatting.RED;
        }
        if (health <= 10.0f) {
            return Formatting.GOLD;
        }
        if (health <= 15.0f) {
            return Formatting.YELLOW;
        }
        if (health <= 20.0f) {
            return Formatting.GREEN;
        }
        return Formatting.DARK_GREEN;
    }

    private float getHealth(T entity) {
        return ((PlayerEntity) entity).getHealth() + ((PlayerEntity) entity).getAbsorptionAmount();
    }
}
