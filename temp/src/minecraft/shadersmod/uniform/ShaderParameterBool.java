package shadersmod.uniform;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.optifine.entity.model.anim.ExpressionType;
import net.optifine.entity.model.anim.IExpressionBool;

public enum ShaderParameterBool implements IExpressionBool {
   IS_ALIVE("is_alive"),
   IS_BURNING("is_burning"),
   IS_CHILD("is_child"),
   IS_GLOWING("is_glowing"),
   IS_HURT("is_hurt"),
   IS_IN_LAVA("is_in_lava"),
   IS_IN_WATER("is_in_water"),
   IS_INVISIBLE("is_invisible"),
   IS_ON_GROUND("is_on_ground"),
   IS_RIDDEN("is_ridden"),
   IS_RIDING("is_riding"),
   IS_SNEAKING("is_sneaking"),
   IS_SPRINTING("is_sprinting"),
   IS_WET("is_wet");

   private String name;
   private RenderManager renderManager;
   private static final ShaderParameterBool[] VALUES = values();

   private ShaderParameterBool(String name) {
      this.name = name;
      this.renderManager = Minecraft.func_71410_x().func_175598_ae();
   }

   public String getName() {
      return this.name;
   }

   public ExpressionType getExpressionType() {
      return ExpressionType.BOOL;
   }

   public boolean eval() {
      Entity entity = Minecraft.func_71410_x().func_175606_aa();
      if(entity instanceof EntityLivingBase) {
         EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
         switch(this) {
         case IS_ALIVE:
            return entitylivingbase.func_70089_S();
         case IS_BURNING:
            return entitylivingbase.func_70027_ad();
         case IS_CHILD:
            return entitylivingbase.func_70631_g_();
         case IS_GLOWING:
            return false;
         case IS_HURT:
            return entitylivingbase.field_70737_aN > 0;
         case IS_IN_LAVA:
            return entitylivingbase.func_180799_ab();
         case IS_IN_WATER:
            return entitylivingbase.func_70090_H();
         case IS_INVISIBLE:
            return entitylivingbase.func_82150_aj();
         case IS_ON_GROUND:
            return entitylivingbase.field_70122_E;
         case IS_RIDDEN:
            return entitylivingbase.field_70153_n != null;
         case IS_RIDING:
            return entitylivingbase.func_70115_ae();
         case IS_SNEAKING:
            return entitylivingbase.func_70093_af();
         case IS_SPRINTING:
            return entitylivingbase.func_70051_ag();
         case IS_WET:
            return entitylivingbase.func_70026_G();
         }
      }

      return false;
   }

   public static ShaderParameterBool parse(String str) {
      if(str == null) {
         return null;
      } else {
         for(int i = 0; i < VALUES.length; ++i) {
            ShaderParameterBool shaderparameterbool = VALUES[i];
            if(shaderparameterbool.getName().equals(str)) {
               return shaderparameterbool;
            }
         }

         return null;
      }
   }
}
