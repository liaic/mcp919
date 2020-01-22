package net.optifine.entity.model.anim;

import net.optifine.entity.model.anim.ExpressionParser;
import net.optifine.entity.model.anim.IExpressionFloat;
import net.optifine.entity.model.anim.IModelResolver;
import net.optifine.entity.model.anim.ModelVariableFloat;
import net.optifine.entity.model.anim.ParseException;
import optifine.Config;

public class ModelVariableUpdater {

   public String modelVariableName;
   public String expressionText;
   public ModelVariableFloat modelVariable;
   public IExpressionFloat expression;


   public boolean initialize(IModelResolver mr) {
      this.modelVariable = mr.getModelVariable(this.modelVariableName);
      if(this.modelVariable == null) {
         Config.warn("Model variable not found: " + this.modelVariableName);
         return false;
      } else {
         try {
            ExpressionParser e = new ExpressionParser(mr);
            this.expression = e.parseFloat(this.expressionText);
            return true;
         } catch (ParseException var3) {
            Config.warn("Error parsing expression: " + this.expressionText);
            Config.warn(var3.getClass().getName() + ": " + var3.getMessage());
            return false;
         }
      }
   }

   public ModelVariableUpdater(String modelVariableName, String expressionText) {
      this.modelVariableName = modelVariableName;
      this.expressionText = expressionText;
   }

   public void update() {
      float val = this.expression.eval();
      this.modelVariable.setValue(val);
   }
}
