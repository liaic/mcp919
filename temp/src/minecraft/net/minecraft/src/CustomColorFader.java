package net.minecraft.src;

import net.minecraft.src.Config;
import net.minecraft.util.Vec3;

public class CustomColorFader {
   private Vec3 color = null;
   private long timeUpdate = System.currentTimeMillis();

   public Vec3 getColor(double p_getColor_1_, double p_getColor_3_, double p_getColor_5_) {
      if(this.color == null) {
         this.color = new Vec3(p_getColor_1_, p_getColor_3_, p_getColor_5_);
         return this.color;
      } else {
         long i = System.currentTimeMillis();
         long j = i - this.timeUpdate;
         if(j == 0L) {
            return this.color;
         } else {
            this.timeUpdate = i;
            if(Math.abs(p_getColor_1_ - this.color.field_72450_a) < 0.004D && Math.abs(p_getColor_3_ - this.color.field_72448_b) < 0.004D && Math.abs(p_getColor_5_ - this.color.field_72449_c) < 0.004D) {
               return this.color;
            } else {
               double d0 = (double)j * 0.001D;
               d0 = Config.limit(d0, 0.0D, 1.0D);
               double d1 = p_getColor_1_ - this.color.field_72450_a;
               double d2 = p_getColor_3_ - this.color.field_72448_b;
               double d3 = p_getColor_5_ - this.color.field_72449_c;
               double d4 = this.color.field_72450_a + d1 * d0;
               double d5 = this.color.field_72448_b + d2 * d0;
               double d6 = this.color.field_72449_c + d3 * d0;
               this.color = new Vec3(d4, d5, d6);
               return this.color;
            }
         }
      }
   }
}