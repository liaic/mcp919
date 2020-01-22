package net.minecraft.client.renderer;

import java.util.concurrent.Callable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;

class EntityRenderer$2 implements Callable {

   public final EntityRenderer field_90025_c;
   public static final String __OBFID = "CL_00000948";


   public EntityRenderer$2(EntityRenderer p_i1243_1_) {
      this.field_90025_c = p_i1243_1_;
   }

   public String call() throws Exception {
      return Minecraft.getMinecraft().currentScreen.getClass().getCanonicalName();
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object call() throws Exception {
      return this.call();
   }
}
