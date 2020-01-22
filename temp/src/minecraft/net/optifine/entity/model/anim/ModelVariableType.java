package net.optifine.entity.model.anim;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.src.Config;

public enum ModelVariableType {
   POS_X("tx"),
   POS_Y("ty"),
   POS_Z("tz"),
   ANGLE_X("rx"),
   ANGLE_Y("ry"),
   ANGLE_Z("rz"),
   OFFSET_X("ox"),
   OFFSET_Y("oy"),
   OFFSET_Z("oz"),
   SCALE_X("sx"),
   SCALE_Y("sy"),
   SCALE_Z("sz");

   private String name;
   public static ModelVariableType[] VALUES = values();

   private ModelVariableType(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public float getFloat(ModelRenderer mr) {
      switch(this) {
      case POS_X:
         return mr.field_78800_c;
      case POS_Y:
         return mr.field_78797_d;
      case POS_Z:
         return mr.field_78798_e;
      case ANGLE_X:
         return mr.field_78795_f;
      case ANGLE_Y:
         return mr.field_78796_g;
      case ANGLE_Z:
         return mr.field_78808_h;
      case OFFSET_X:
         return mr.field_82906_o;
      case OFFSET_Y:
         return mr.field_82908_p;
      case OFFSET_Z:
         return mr.field_82907_q;
      case SCALE_X:
         return mr.scaleX;
      case SCALE_Y:
         return mr.scaleY;
      case SCALE_Z:
         return mr.scaleZ;
      default:
         Config.warn("GetFloat not supported for: " + this);
         return 0.0F;
      }
   }

   public void setFloat(ModelRenderer mr, float val) {
      switch(this) {
      case POS_X:
         mr.field_78800_c = val;
         return;
      case POS_Y:
         mr.field_78797_d = val;
         return;
      case POS_Z:
         mr.field_78798_e = val;
         return;
      case ANGLE_X:
         mr.field_78795_f = val;
         return;
      case ANGLE_Y:
         mr.field_78796_g = val;
         return;
      case ANGLE_Z:
         mr.field_78808_h = val;
         return;
      case OFFSET_X:
         mr.field_82906_o = val;
         return;
      case OFFSET_Y:
         mr.field_82908_p = val;
         return;
      case OFFSET_Z:
         mr.field_82907_q = val;
         return;
      case SCALE_X:
         mr.scaleX = val;
         return;
      case SCALE_Y:
         mr.scaleY = val;
         return;
      case SCALE_Z:
         mr.scaleZ = val;
         return;
      default:
         Config.warn("SetFloat not supported for: " + this);
      }
   }

   public static ModelVariableType parse(String str) {
      for(int i = 0; i < VALUES.length; ++i) {
         ModelVariableType modelvariabletype = VALUES[i];
         if(modelvariabletype.getName().equals(str)) {
            return modelvariabletype;
         }
      }

      return null;
   }
}
