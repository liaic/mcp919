package shadersmod.uniform;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.optifine.entity.model.anim.ExpressionType;
import net.optifine.entity.model.anim.IExpressionBool;

public enum ShaderParameterBool implements IExpressionBool {

   IS_ALIVE("IS_ALIVE", 0, "is_alive"),
   IS_BURNING("IS_BURNING", 1, "is_burning"),
   IS_CHILD("IS_CHILD", 2, "is_child"),
   IS_GLOWING("IS_GLOWING", 3, "is_glowing"),
   IS_HURT("IS_HURT", 4, "is_hurt"),
   IS_IN_LAVA("IS_IN_LAVA", 5, "is_in_lava"),
   IS_IN_WATER("IS_IN_WATER", 6, "is_in_water"),
   IS_INVISIBLE("IS_INVISIBLE", 7, "is_invisible"),
   IS_ON_GROUND("IS_ON_GROUND", 8, "is_on_ground"),
   IS_RIDDEN("IS_RIDDEN", 9, "is_ridden"),
   IS_RIDING("IS_RIDING", 10, "is_riding"),
   IS_SNEAKING("IS_SNEAKING", 11, "is_sneaking"),
   IS_SPRINTING("IS_SPRINTING", 12, "is_sprinting"),
   IS_WET("IS_WET", 13, "is_wet");
   public String name;
   public RenderManager renderManager;
   public static final ShaderParameterBool[] VALUES = values();
   // $FF: synthetic field
   public static final ShaderParameterBool[] $VALUES = new ShaderParameterBool[]{IS_ALIVE, IS_BURNING, IS_CHILD, IS_GLOWING, IS_HURT, IS_IN_LAVA, IS_IN_WATER, IS_INVISIBLE, IS_ON_GROUND, IS_RIDDEN, IS_RIDING, IS_SNEAKING, IS_SPRINTING, IS_WET};


   public ShaderParameterBool(String var1, int var2, String name) {
      this.name = name;
      this.renderManager = Minecraft.getMinecraft().getRenderManager();
   }

   public String getName() {
      return this.name;
   }

   public ExpressionType getExpressionType() {
      return ExpressionType.BOOL;
   }

   public boolean eval() {
      Entity entityGeneral = Minecraft.getMinecraft().getRenderViewEntity();
      if(entityGeneral instanceof EntityLivingBase) {
         EntityLivingBase entity = (EntityLivingBase)entityGeneral;
         switch(ShaderParameterBool.NamelessClass274107080.$SwitchMap$shadersmod$uniform$ShaderParameterBool[this.ordinal()]) {
         case 1:
            return entity.isEntityAlive();
         case 2:
            return entity.isBurning();
         case 3:
            return entity.isChild();
         case 4:
            return false;
         case 5:
            return entity.hurtTime > 0;
         case 6:
            return entity.isInLava();
         case 7:
            return entity.isInWater();
         case 8:
            return entity.isInvisible();
         case 9:
            return entity.onGround;
         case 10:
            return entity.riddenByEntity != null;
         case 11:
            return entity.isRiding();
         case 12:
            return entity.isSneaking();
         case 13:
            return entity.isSprinting();
         case 14:
            return entity.isWet();
         }
      }

      return false;
   }

   public static ShaderParameterBool parse(String str) {
      if(str == null) {
         return null;
      } else {
         for(int i = 0; i < VALUES.length; ++i) {
            ShaderParameterBool type = VALUES[i];
            if(type.getName().equals(str)) {
               return type;
            }
         }

         return null;
      }
   }


   // $FF: synthetic class
   public static class NamelessClass274107080 {

      // $FF: synthetic field
      public static final int[] $SwitchMap$shadersmod$uniform$ShaderParameterBool = new int[ShaderParameterBool.values().length];


      static {
         try {
            $SwitchMap$shadersmod$uniform$ShaderParameterBool[ShaderParameterBool.IS_ALIVE.ordinal()] = 1;
         } catch (NoSuchFieldError var14) {
            ;
         }

         try {
            $SwitchMap$shadersmod$uniform$ShaderParameterBool[ShaderParameterBool.IS_BURNING.ordinal()] = 2;
         } catch (NoSuchFieldError var13) {
            ;
         }

         try {
            $SwitchMap$shadersmod$uniform$ShaderParameterBool[ShaderParameterBool.IS_CHILD.ordinal()] = 3;
         } catch (NoSuchFieldError var12) {
            ;
         }

         try {
            $SwitchMap$shadersmod$uniform$ShaderParameterBool[ShaderParameterBool.IS_GLOWING.ordinal()] = 4;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            $SwitchMap$shadersmod$uniform$ShaderParameterBool[ShaderParameterBool.IS_HURT.ordinal()] = 5;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            $SwitchMap$shadersmod$uniform$ShaderParameterBool[ShaderParameterBool.IS_IN_LAVA.ordinal()] = 6;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            $SwitchMap$shadersmod$uniform$ShaderParameterBool[ShaderParameterBool.IS_IN_WATER.ordinal()] = 7;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            $SwitchMap$shadersmod$uniform$ShaderParameterBool[ShaderParameterBool.IS_INVISIBLE.ordinal()] = 8;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            $SwitchMap$shadersmod$uniform$ShaderParameterBool[ShaderParameterBool.IS_ON_GROUND.ordinal()] = 9;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$shadersmod$uniform$ShaderParameterBool[ShaderParameterBool.IS_RIDDEN.ordinal()] = 10;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$shadersmod$uniform$ShaderParameterBool[ShaderParameterBool.IS_RIDING.ordinal()] = 11;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$shadersmod$uniform$ShaderParameterBool[ShaderParameterBool.IS_SNEAKING.ordinal()] = 12;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$shadersmod$uniform$ShaderParameterBool[ShaderParameterBool.IS_SPRINTING.ordinal()] = 13;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$shadersmod$uniform$ShaderParameterBool[ShaderParameterBool.IS_WET.ordinal()] = 14;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
