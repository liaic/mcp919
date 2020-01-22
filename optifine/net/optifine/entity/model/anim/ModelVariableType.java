package net.optifine.entity.model.anim;

import net.minecraft.client.model.ModelRenderer;
import optifine.Config;

public enum ModelVariableType {

   POS_X("POS_X", 0, "tx"),
   POS_Y("POS_Y", 1, "ty"),
   POS_Z("POS_Z", 2, "tz"),
   ANGLE_X("ANGLE_X", 3, "rx"),
   ANGLE_Y("ANGLE_Y", 4, "ry"),
   ANGLE_Z("ANGLE_Z", 5, "rz"),
   OFFSET_X("OFFSET_X", 6, "ox"),
   OFFSET_Y("OFFSET_Y", 7, "oy"),
   OFFSET_Z("OFFSET_Z", 8, "oz"),
   SCALE_X("SCALE_X", 9, "sx"),
   SCALE_Y("SCALE_Y", 10, "sy"),
   SCALE_Z("SCALE_Z", 11, "sz");
   public String name;
   public static ModelVariableType[] VALUES = values();
   // $FF: synthetic field
   public static final ModelVariableType[] $VALUES = new ModelVariableType[]{POS_X, POS_Y, POS_Z, ANGLE_X, ANGLE_Y, ANGLE_Z, OFFSET_X, OFFSET_Y, OFFSET_Z, SCALE_X, SCALE_Y, SCALE_Z};


   public ModelVariableType(String var1, int var2, String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public float getFloat(ModelRenderer mr) {
      switch(ModelVariableType.NamelessClass725695082.$SwitchMap$net$optifine$entity$model$anim$ModelVariableType[this.ordinal()]) {
      case 1:
         return mr.rotationPointX;
      case 2:
         return mr.rotationPointY;
      case 3:
         return mr.rotationPointZ;
      case 4:
         return mr.rotateAngleX;
      case 5:
         return mr.rotateAngleY;
      case 6:
         return mr.rotateAngleZ;
      case 7:
         return mr.offsetX;
      case 8:
         return mr.offsetY;
      case 9:
         return mr.offsetZ;
      case 10:
         return mr.scaleX;
      case 11:
         return mr.scaleY;
      case 12:
         return mr.scaleZ;
      default:
         Config.warn("GetFloat not supported for: " + this);
         return 0.0F;
      }
   }

   public void setFloat(ModelRenderer mr, float val) {
      switch(ModelVariableType.NamelessClass725695082.$SwitchMap$net$optifine$entity$model$anim$ModelVariableType[this.ordinal()]) {
      case 1:
         mr.rotationPointX = val;
         return;
      case 2:
         mr.rotationPointY = val;
         return;
      case 3:
         mr.rotationPointZ = val;
         return;
      case 4:
         mr.rotateAngleX = val;
         return;
      case 5:
         mr.rotateAngleY = val;
         return;
      case 6:
         mr.rotateAngleZ = val;
         return;
      case 7:
         mr.offsetX = val;
         return;
      case 8:
         mr.offsetY = val;
         return;
      case 9:
         mr.offsetZ = val;
         return;
      case 10:
         mr.scaleX = val;
         return;
      case 11:
         mr.scaleY = val;
         return;
      case 12:
         mr.scaleZ = val;
         return;
      default:
         Config.warn("SetFloat not supported for: " + this);
      }
   }

   public static ModelVariableType parse(String str) {
      for(int i = 0; i < VALUES.length; ++i) {
         ModelVariableType var = VALUES[i];
         if(var.getName().equals(str)) {
            return var;
         }
      }

      return null;
   }


   // $FF: synthetic class
   public static class NamelessClass725695082 {

      // $FF: synthetic field
      public static final int[] $SwitchMap$net$optifine$entity$model$anim$ModelVariableType = new int[ModelVariableType.values().length];


      static {
         try {
            $SwitchMap$net$optifine$entity$model$anim$ModelVariableType[ModelVariableType.POS_X.ordinal()] = 1;
         } catch (NoSuchFieldError var12) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$ModelVariableType[ModelVariableType.POS_Y.ordinal()] = 2;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$ModelVariableType[ModelVariableType.POS_Z.ordinal()] = 3;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$ModelVariableType[ModelVariableType.ANGLE_X.ordinal()] = 4;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$ModelVariableType[ModelVariableType.ANGLE_Y.ordinal()] = 5;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$ModelVariableType[ModelVariableType.ANGLE_Z.ordinal()] = 6;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$ModelVariableType[ModelVariableType.OFFSET_X.ordinal()] = 7;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$ModelVariableType[ModelVariableType.OFFSET_Y.ordinal()] = 8;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$ModelVariableType[ModelVariableType.OFFSET_Z.ordinal()] = 9;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$ModelVariableType[ModelVariableType.SCALE_X.ordinal()] = 10;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$ModelVariableType[ModelVariableType.SCALE_Y.ordinal()] = 11;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$ModelVariableType[ModelVariableType.SCALE_Z.ordinal()] = 12;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
