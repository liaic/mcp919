package shadersmod.uniform;

import org.lwjgl.opengl.ARBShaderObjects;
import shadersmod.client.Shaders;
import shadersmod.uniform.ShaderUniformBase;

public class ShaderUniformInt extends ShaderUniformBase {

   public int value = -1;


   public ShaderUniformInt(String name) {
      super(name);
   }

   public void onProgramChanged() {
      this.value = -1;
   }

   public void setValue(int value) {
      if(this.getLocation() >= 0) {
         if(this.value != value) {
            ARBShaderObjects.glUniform1iARB(this.getLocation(), value);
            Shaders.checkGLError(this.getName());
            this.value = value;
         }
      }
   }

   public int getValue() {
      return this.value;
   }
}
