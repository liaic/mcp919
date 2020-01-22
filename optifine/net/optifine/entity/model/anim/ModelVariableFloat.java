package net.optifine.entity.model.anim;

import net.minecraft.client.model.ModelRenderer;
import net.optifine.entity.model.anim.ExpressionType;
import net.optifine.entity.model.anim.IExpressionFloat;
import net.optifine.entity.model.anim.ModelVariableType;

public class ModelVariableFloat implements IExpressionFloat {

   public String name;
   public ModelRenderer modelRenderer;
   public ModelVariableType enumModelVariable;


   public ModelVariableFloat(String name, ModelRenderer modelRenderer, ModelVariableType enumModelVariable) {
      this.name = name;
      this.modelRenderer = modelRenderer;
      this.enumModelVariable = enumModelVariable;
   }

   public ExpressionType getExpressionType() {
      return ExpressionType.FLOAT;
   }

   public float eval() {
      return this.getValue();
   }

   public float getValue() {
      return this.enumModelVariable.getFloat(this.modelRenderer);
   }

   public void setValue(float value) {
      this.enumModelVariable.setFloat(this.modelRenderer, value);
   }

   public String toString() {
      return this.name;
   }
}
