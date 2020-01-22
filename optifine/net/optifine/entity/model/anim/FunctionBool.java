package net.optifine.entity.model.anim;

import net.optifine.entity.model.anim.ExpressionType;
import net.optifine.entity.model.anim.FunctionType;
import net.optifine.entity.model.anim.IExpression;
import net.optifine.entity.model.anim.IExpressionBool;

public class FunctionBool implements IExpressionBool {

   public FunctionType type;
   public IExpression[] arguments;


   public FunctionBool(FunctionType type, IExpression[] arguments) {
      this.type = type;
      this.arguments = arguments;
   }

   public boolean eval() {
      return this.type.evalBool(this.arguments);
   }

   public ExpressionType getExpressionType() {
      return ExpressionType.BOOL;
   }

   public String toString() {
      return "" + this.type + "()";
   }
}
