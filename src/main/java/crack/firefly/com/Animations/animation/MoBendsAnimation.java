package crack.firefly.com.Animations.animation;

import crack.firefly.com.Animations.data.MoBends_EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;

public abstract class MoBendsAnimation {
	public abstract void animate(EntityLivingBase argEntity, ModelBase argModel, MoBends_EntityData argData);
	public abstract String getName();
}
