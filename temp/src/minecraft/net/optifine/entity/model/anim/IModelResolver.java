package net.optifine.entity.model.anim;

import net.minecraft.client.model.ModelRenderer;
import net.optifine.entity.model.anim.IExpressionResolver;
import net.optifine.entity.model.anim.ModelVariableFloat;

public interface IModelResolver extends IExpressionResolver {
   ModelRenderer getModelRenderer(String var1);

   ModelVariableFloat getModelVariable(String var1);
}
