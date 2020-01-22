package net.minecraft.client.renderer;

import com.google.common.primitives.Floats;
import java.util.Comparator;
import net.minecraft.client.renderer.WorldRenderer;

class WorldRenderer$1 implements Comparator {

   public final float[] field_181659_a;
   public final WorldRenderer field_181660_b;


   public WorldRenderer$1(WorldRenderer p_i46380_1_, float[] p_i46380_2_) {
      this.field_181660_b = p_i46380_1_;
      this.field_181659_a = p_i46380_2_;
   }

   public int compare(Integer p_a_1_, Integer p_a_2_) {
      return Floats.compare(this.field_181659_a[p_a_2_.intValue()], this.field_181659_a[p_a_1_.intValue()]);
   }

   public int compare(Object p_compare_1_, Object p_compare_2_) {
      return this.compare((Integer)p_compare_1_, (Integer)p_compare_2_);
   }
}
