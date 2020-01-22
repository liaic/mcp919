package net.optifine.entity.model.anim;

import net.optifine.entity.model.anim.ExpressionType;
import net.optifine.entity.model.anim.IExpression;
import net.optifine.entity.model.anim.IParameters;

public class Parameters implements IParameters {
   private ExpressionType[] parameterTypes;

   public Parameters(ExpressionType[] parameterTypes) {
      this.parameterTypes = parameterTypes;
   }

   public ExpressionType[] getParameterTypes(IExpression[] params) {
      return this.parameterTypes;
   }
}
