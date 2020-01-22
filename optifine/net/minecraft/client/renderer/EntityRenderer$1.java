package net.minecraft.client.renderer;

import com.google.common.base.Predicate;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;

class EntityRenderer$1 implements Predicate {

   public final EntityRenderer field_90032_a;


   public EntityRenderer$1(EntityRenderer p_i46382_1_) {
      this.field_90032_a = p_i46382_1_;
   }

   public boolean apply(Entity p_a_1_) {
      return p_a_1_.canBeCollidedWith();
   }

   public boolean apply(Object p_apply_1_) {
      return this.apply((Entity)p_apply_1_);
   }
}
