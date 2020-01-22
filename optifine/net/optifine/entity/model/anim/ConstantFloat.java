package net.optifine.entity.model.anim;

import net.optifine.entity.model.anim.ExpressionType;
import net.optifine.entity.model.anim.IExpressionFloat;

public class ConstantFloat implements IExpressionFloat {

   public float value;


   public ConstantFloat(float value) {
      this.value = value;
   }

   public float eval() {
      return this.value;
   }

   public ExpressionType getExpressionType() {
      return ExpressionType.FLOAT;
   }

   public String toString() {
      return "" + this.value;
   }
}
