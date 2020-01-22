package net.optifine.entity.model.anim;

import net.optifine.entity.model.anim.IModelResolver;
import net.optifine.entity.model.anim.ModelVariableUpdater;

public class ModelUpdater {

   public ModelVariableUpdater[] modelVariableUpdaters;


   public ModelUpdater(ModelVariableUpdater[] modelVariableUpdaters) {
      this.modelVariableUpdaters = modelVariableUpdaters;
   }

   public void update() {
      for(int i = 0; i < this.modelVariableUpdaters.length; ++i) {
         ModelVariableUpdater mvu = this.modelVariableUpdaters[i];
         mvu.update();
      }

   }

   public boolean initialize(IModelResolver mr) {
      for(int i = 0; i < this.modelVariableUpdaters.length; ++i) {
         ModelVariableUpdater mvu = this.modelVariableUpdaters[i];
         if(!mvu.initialize(mr)) {
            return false;
         }
      }

      return true;
   }
}
