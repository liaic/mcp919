package net.optifine.entity.model.anim;

import net.optifine.entity.model.anim.ExpressionType;
import net.optifine.entity.model.anim.FunctionType;
import net.optifine.entity.model.anim.IExpression;
import net.optifine.entity.model.anim.IExpressionFloat;

public class FunctionFloat implements IExpressionFloat {

   public FunctionType type;
   public IExpression[] arguments;


   public FunctionFloat(FunctionType type, IExpression[] arguments) {
      this.type = type;
      this.arguments = arguments;
   }

   public float eval() {
      return this.type.evalFloat(this.arguments);
   }

   public ExpressionType getExpressionType() {
      return ExpressionType.FLOAT;
   }

   public String toString() {
      return "" + this.type + "()";
   }
}
