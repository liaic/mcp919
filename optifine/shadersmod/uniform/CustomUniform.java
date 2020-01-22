package shadersmod.uniform;

import net.optifine.entity.model.anim.IExpression;
import shadersmod.uniform.ShaderUniformBase;
import shadersmod.uniform.UniformType;

public class CustomUniform {

   public String name;
   public UniformType type;
   public IExpression expression;
   public ShaderUniformBase shaderUniform;


   public CustomUniform(String name, UniformType type, IExpression expression) {
      this.name = name;
      this.type = type;
      this.expression = expression;
      this.shaderUniform = type.makeShaderUniform(name);
   }

   public void setProgram(int program) {
      this.shaderUniform.setProgram(program);
   }

   public void update() {
      this.type.updateUniform(this.expression, this.shaderUniform);
   }

   public String getName() {
      return this.name;
   }

   public UniformType getType() {
      return this.type;
   }

   public IExpression getExpression() {
      return this.expression;
   }

   public ShaderUniformBase getShaderUniform() {
      return this.shaderUniform;
   }
}
