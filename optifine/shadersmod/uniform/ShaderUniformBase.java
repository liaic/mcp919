package shadersmod.uniform;

import org.lwjgl.opengl.ARBShaderObjects;

public abstract class ShaderUniformBase {

   public String name;
   public int program = -1;
   public int location = -1;


   public ShaderUniformBase(String name) {
      this.name = name;
   }

   public void setProgram(int program) {
      if(this.program != program) {
         this.program = program;
         this.location = ARBShaderObjects.glGetUniformLocationARB(program, this.name);
         this.onProgramChanged();
      }
   }

   public abstract void onProgramChanged();

   public String getName() {
      return this.name;
   }

   public int getProgram() {
      return this.program;
   }

   public int getLocation() {
      return this.location;
   }

   public boolean isDefined() {
      return this.location >= 0;
   }
}
