package net.minecraft.client.renderer.block.model;

import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.src.Config;
import net.minecraft.src.Reflector;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.model.ITransformation;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import shadersmod.client.Shaders;

public class FaceBakery {
   private static final float field_178418_a = 1.0F / (float)Math.cos(0.39269909262657166D) - 1.0F;
   private static final float field_178417_b = 1.0F / (float)Math.cos(0.7853981633974483D) - 1.0F;
   private static final String __OBFID = "CL_00002490";

   public BakedQuad func_178414_a(Vector3f p_178414_1_, Vector3f p_178414_2_, BlockPartFace p_178414_3_, TextureAtlasSprite p_178414_4_, EnumFacing p_178414_5_, ModelRotation p_178414_6_, BlockPartRotation p_178414_7_, boolean p_178414_8_, boolean p_178414_9_) {
      return this.makeBakedQuad(p_178414_1_, p_178414_2_, p_178414_3_, p_178414_4_, p_178414_5_, p_178414_6_, p_178414_7_, p_178414_8_, p_178414_9_);
   }

   public BakedQuad makeBakedQuad(Vector3f p_makeBakedQuad_1_, Vector3f p_makeBakedQuad_2_, BlockPartFace p_makeBakedQuad_3_, TextureAtlasSprite p_makeBakedQuad_4_, EnumFacing p_makeBakedQuad_5_, ITransformation p_makeBakedQuad_6_, BlockPartRotation p_makeBakedQuad_7_, boolean p_makeBakedQuad_8_, boolean p_makeBakedQuad_9_) {
      int[] aint = this.makeQuadVertexData(p_makeBakedQuad_3_, p_makeBakedQuad_4_, p_makeBakedQuad_5_, this.func_178403_a(p_makeBakedQuad_1_, p_makeBakedQuad_2_), p_makeBakedQuad_6_, p_makeBakedQuad_7_, p_makeBakedQuad_8_, p_makeBakedQuad_9_);
      EnumFacing enumfacing = func_178410_a(aint);
      if(p_makeBakedQuad_8_) {
         this.func_178409_a(aint, enumfacing, p_makeBakedQuad_3_.field_178243_e, p_makeBakedQuad_4_);
      }

      if(p_makeBakedQuad_7_ == null) {
         this.func_178408_a(aint, enumfacing);
      }

      if(Reflector.ForgeHooksClient_fillNormal.exists()) {
         Reflector.callVoid(Reflector.ForgeHooksClient_fillNormal, new Object[]{aint, enumfacing});
      }

      return new BakedQuad(aint, p_makeBakedQuad_3_.field_178245_c, enumfacing, p_makeBakedQuad_4_);
   }

   private int[] makeQuadVertexData(BlockPartFace p_makeQuadVertexData_1_, TextureAtlasSprite p_makeQuadVertexData_2_, EnumFacing p_makeQuadVertexData_3_, float[] p_makeQuadVertexData_4_, ITransformation p_makeQuadVertexData_5_, BlockPartRotation p_makeQuadVertexData_6_, boolean p_makeQuadVertexData_7_, boolean p_makeQuadVertexData_8_) {
      int i = 28;
      if(Config.isShaders()) {
         i = 56;
      }

      int[] aint = new int[i];

      for(int j = 0; j < 4; ++j) {
         this.fillVertexData(aint, j, p_makeQuadVertexData_3_, p_makeQuadVertexData_1_, p_makeQuadVertexData_4_, p_makeQuadVertexData_2_, p_makeQuadVertexData_5_, p_makeQuadVertexData_6_, p_makeQuadVertexData_7_, p_makeQuadVertexData_8_);
      }

      return aint;
   }

   private int func_178413_a(EnumFacing p_178413_1_) {
      float f = this.func_178412_b(p_178413_1_);
      int i = MathHelper.func_76125_a((int)(f * 255.0F), 0, 255);
      return -16777216 | i << 16 | i << 8 | i;
   }

   private float func_178412_b(EnumFacing p_178412_1_) {
      switch(FaceBakery.FaceBakery$1.field_178400_a[p_178412_1_.ordinal()]) {
      case 1:
         if(Config.isShaders()) {
            return Shaders.blockLightLevel05;
         }

         return 0.5F;
      case 2:
         return 1.0F;
      case 3:
      case 4:
         if(Config.isShaders()) {
            return Shaders.blockLightLevel08;
         }

         return 0.8F;
      case 5:
      case 6:
         if(Config.isShaders()) {
            return Shaders.blockLightLevel06;
         }

         return 0.6F;
      default:
         return 1.0F;
      }
   }

   private float[] func_178403_a(Vector3f p_178403_1_, Vector3f p_178403_2_) {
      float[] afloat = new float[EnumFacing.values().length];
      afloat[EnumFaceDirection.Constants.field_179176_f] = p_178403_1_.x / 16.0F;
      afloat[EnumFaceDirection.Constants.field_179178_e] = p_178403_1_.y / 16.0F;
      afloat[EnumFaceDirection.Constants.field_179177_d] = p_178403_1_.z / 16.0F;
      afloat[EnumFaceDirection.Constants.field_179180_c] = p_178403_2_.x / 16.0F;
      afloat[EnumFaceDirection.Constants.field_179179_b] = p_178403_2_.y / 16.0F;
      afloat[EnumFaceDirection.Constants.field_179181_a] = p_178403_2_.z / 16.0F;
      return afloat;
   }

   private void fillVertexData(int[] p_fillVertexData_1_, int p_fillVertexData_2_, EnumFacing p_fillVertexData_3_, BlockPartFace p_fillVertexData_4_, float[] p_fillVertexData_5_, TextureAtlasSprite p_fillVertexData_6_, ITransformation p_fillVertexData_7_, BlockPartRotation p_fillVertexData_8_, boolean p_fillVertexData_9_, boolean p_fillVertexData_10_) {
      EnumFacing enumfacing = p_fillVertexData_7_.rotate(p_fillVertexData_3_);
      int i = p_fillVertexData_10_?this.func_178413_a(enumfacing):-1;
      EnumFaceDirection.VertexInformation enumfacedirection$vertexinformation = EnumFaceDirection.func_179027_a(p_fillVertexData_3_).func_179025_a(p_fillVertexData_2_);
      Vector3f vector3f = new Vector3f(p_fillVertexData_5_[enumfacedirection$vertexinformation.field_179184_a], p_fillVertexData_5_[enumfacedirection$vertexinformation.field_179182_b], p_fillVertexData_5_[enumfacedirection$vertexinformation.field_179183_c]);
      this.func_178407_a(vector3f, p_fillVertexData_8_);
      int j = this.rotateVertex(vector3f, p_fillVertexData_3_, p_fillVertexData_2_, p_fillVertexData_7_, p_fillVertexData_9_);
      this.func_178404_a(p_fillVertexData_1_, j, p_fillVertexData_2_, vector3f, i, p_fillVertexData_6_, p_fillVertexData_4_.field_178243_e);
   }

   private void func_178404_a(int[] p_178404_1_, int p_178404_2_, int p_178404_3_, Vector3f p_178404_4_, int p_178404_5_, TextureAtlasSprite p_178404_6_, BlockFaceUV p_178404_7_) {
      int i = p_178404_1_.length / 4;
      int j = p_178404_2_ * i;
      p_178404_1_[j] = Float.floatToRawIntBits(p_178404_4_.x);
      p_178404_1_[j + 1] = Float.floatToRawIntBits(p_178404_4_.y);
      p_178404_1_[j + 2] = Float.floatToRawIntBits(p_178404_4_.z);
      p_178404_1_[j + 3] = p_178404_5_;
      p_178404_1_[j + 4] = Float.floatToRawIntBits(p_178404_6_.func_94214_a((double)p_178404_7_.func_178348_a(p_178404_3_)));
      p_178404_1_[j + 4 + 1] = Float.floatToRawIntBits(p_178404_6_.func_94207_b((double)p_178404_7_.func_178346_b(p_178404_3_)));
   }

   private void func_178407_a(Vector3f p_178407_1_, BlockPartRotation p_178407_2_) {
      if(p_178407_2_ != null) {
         Matrix4f matrix4f = this.func_178411_a();
         Vector3f vector3f = new Vector3f(0.0F, 0.0F, 0.0F);
         switch(FaceBakery.FaceBakery$1.field_178399_b[p_178407_2_.field_178342_b.ordinal()]) {
         case 1:
            Matrix4f.rotate(p_178407_2_.field_178343_c * 0.017453292F, new Vector3f(1.0F, 0.0F, 0.0F), matrix4f, matrix4f);
            vector3f.set(0.0F, 1.0F, 1.0F);
            break;
         case 2:
            Matrix4f.rotate(p_178407_2_.field_178343_c * 0.017453292F, new Vector3f(0.0F, 1.0F, 0.0F), matrix4f, matrix4f);
            vector3f.set(1.0F, 0.0F, 1.0F);
            break;
         case 3:
            Matrix4f.rotate(p_178407_2_.field_178343_c * 0.017453292F, new Vector3f(0.0F, 0.0F, 1.0F), matrix4f, matrix4f);
            vector3f.set(1.0F, 1.0F, 0.0F);
         }

         if(p_178407_2_.field_178341_d) {
            if(Math.abs(p_178407_2_.field_178343_c) == 22.5F) {
               vector3f.scale(field_178418_a);
            } else {
               vector3f.scale(field_178417_b);
            }

            Vector3f.add(vector3f, new Vector3f(1.0F, 1.0F, 1.0F), vector3f);
         } else {
            vector3f.set(1.0F, 1.0F, 1.0F);
         }

         this.func_178406_a(p_178407_1_, new Vector3f(p_178407_2_.field_178344_a), matrix4f, vector3f);
      }

   }

   public int func_178415_a(Vector3f p_178415_1_, EnumFacing p_178415_2_, int p_178415_3_, ModelRotation p_178415_4_, boolean p_178415_5_) {
      return this.rotateVertex(p_178415_1_, p_178415_2_, p_178415_3_, p_178415_4_, p_178415_5_);
   }

   public int rotateVertex(Vector3f p_rotateVertex_1_, EnumFacing p_rotateVertex_2_, int p_rotateVertex_3_, ITransformation p_rotateVertex_4_, boolean p_rotateVertex_5_) {
      if(p_rotateVertex_4_ == ModelRotation.X0_Y0) {
         return p_rotateVertex_3_;
      } else {
         if(Reflector.ForgeHooksClient_transform.exists()) {
            Reflector.call(Reflector.ForgeHooksClient_transform, new Object[]{p_rotateVertex_1_, p_rotateVertex_4_.getMatrix()});
         } else {
            this.func_178406_a(p_rotateVertex_1_, new Vector3f(0.5F, 0.5F, 0.5F), ((ModelRotation)p_rotateVertex_4_).func_177525_a(), new Vector3f(1.0F, 1.0F, 1.0F));
         }

         return p_rotateVertex_4_.rotate(p_rotateVertex_2_, p_rotateVertex_3_);
      }
   }

   private void func_178406_a(Vector3f p_178406_1_, Vector3f p_178406_2_, Matrix4f p_178406_3_, Vector3f p_178406_4_) {
      Vector4f vector4f = new Vector4f(p_178406_1_.x - p_178406_2_.x, p_178406_1_.y - p_178406_2_.y, p_178406_1_.z - p_178406_2_.z, 1.0F);
      Matrix4f.transform(p_178406_3_, vector4f, vector4f);
      vector4f.x *= p_178406_4_.x;
      vector4f.y *= p_178406_4_.y;
      vector4f.z *= p_178406_4_.z;
      p_178406_1_.set(vector4f.x + p_178406_2_.x, vector4f.y + p_178406_2_.y, vector4f.z + p_178406_2_.z);
   }

   private Matrix4f func_178411_a() {
      Matrix4f matrix4f = new Matrix4f();
      matrix4f.setIdentity();
      return matrix4f;
   }

   public static EnumFacing func_178410_a(int[] p_178410_0_) {
      int i = p_178410_0_.length / 4;
      int j = i * 2;
      int k = i * 3;
      Vector3f vector3f = new Vector3f(Float.intBitsToFloat(p_178410_0_[0]), Float.intBitsToFloat(p_178410_0_[1]), Float.intBitsToFloat(p_178410_0_[2]));
      Vector3f vector3f1 = new Vector3f(Float.intBitsToFloat(p_178410_0_[i]), Float.intBitsToFloat(p_178410_0_[i + 1]), Float.intBitsToFloat(p_178410_0_[i + 2]));
      Vector3f vector3f2 = new Vector3f(Float.intBitsToFloat(p_178410_0_[j]), Float.intBitsToFloat(p_178410_0_[j + 1]), Float.intBitsToFloat(p_178410_0_[j + 2]));
      Vector3f vector3f3 = new Vector3f();
      Vector3f vector3f4 = new Vector3f();
      Vector3f vector3f5 = new Vector3f();
      Vector3f.sub(vector3f, vector3f1, vector3f3);
      Vector3f.sub(vector3f2, vector3f1, vector3f4);
      Vector3f.cross(vector3f4, vector3f3, vector3f5);
      float f = (float)Math.sqrt((double)(vector3f5.x * vector3f5.x + vector3f5.y * vector3f5.y + vector3f5.z * vector3f5.z));
      vector3f5.x /= f;
      vector3f5.y /= f;
      vector3f5.z /= f;
      EnumFacing enumfacing = null;
      float f1 = 0.0F;

      for(EnumFacing enumfacing1 : EnumFacing.values()) {
         Vec3i vec3i = enumfacing1.func_176730_m();
         Vector3f vector3f6 = new Vector3f((float)vec3i.func_177958_n(), (float)vec3i.func_177956_o(), (float)vec3i.func_177952_p());
         float f2 = Vector3f.dot(vector3f5, vector3f6);
         if(f2 >= 0.0F && f2 > f1) {
            f1 = f2;
            enumfacing = enumfacing1;
         }
      }

      if(f1 < 0.719F) {
         if(enumfacing != EnumFacing.EAST && enumfacing != EnumFacing.WEST && enumfacing != EnumFacing.NORTH && enumfacing != EnumFacing.SOUTH) {
            enumfacing = EnumFacing.UP;
         } else {
            enumfacing = EnumFacing.NORTH;
         }
      }

      return enumfacing == null?EnumFacing.UP:enumfacing;
   }

   public void func_178409_a(int[] p_178409_1_, EnumFacing p_178409_2_, BlockFaceUV p_178409_3_, TextureAtlasSprite p_178409_4_) {
      for(int i = 0; i < 4; ++i) {
         this.func_178401_a(i, p_178409_1_, p_178409_2_, p_178409_3_, p_178409_4_);
      }

   }

   private void func_178408_a(int[] p_178408_1_, EnumFacing p_178408_2_) {
      int[] aint = new int[p_178408_1_.length];
      System.arraycopy(p_178408_1_, 0, aint, 0, p_178408_1_.length);
      float[] afloat = new float[EnumFacing.values().length];
      afloat[EnumFaceDirection.Constants.field_179176_f] = 999.0F;
      afloat[EnumFaceDirection.Constants.field_179178_e] = 999.0F;
      afloat[EnumFaceDirection.Constants.field_179177_d] = 999.0F;
      afloat[EnumFaceDirection.Constants.field_179180_c] = -999.0F;
      afloat[EnumFaceDirection.Constants.field_179179_b] = -999.0F;
      afloat[EnumFaceDirection.Constants.field_179181_a] = -999.0F;
      int j = p_178408_1_.length / 4;

      for(int k = 0; k < 4; ++k) {
         int i = j * k;
         float f1 = Float.intBitsToFloat(aint[i]);
         float f2 = Float.intBitsToFloat(aint[i + 1]);
         float f = Float.intBitsToFloat(aint[i + 2]);
         if(f1 < afloat[EnumFaceDirection.Constants.field_179176_f]) {
            afloat[EnumFaceDirection.Constants.field_179176_f] = f1;
         }

         if(f2 < afloat[EnumFaceDirection.Constants.field_179178_e]) {
            afloat[EnumFaceDirection.Constants.field_179178_e] = f2;
         }

         if(f < afloat[EnumFaceDirection.Constants.field_179177_d]) {
            afloat[EnumFaceDirection.Constants.field_179177_d] = f;
         }

         if(f1 > afloat[EnumFaceDirection.Constants.field_179180_c]) {
            afloat[EnumFaceDirection.Constants.field_179180_c] = f1;
         }

         if(f2 > afloat[EnumFaceDirection.Constants.field_179179_b]) {
            afloat[EnumFaceDirection.Constants.field_179179_b] = f2;
         }

         if(f > afloat[EnumFaceDirection.Constants.field_179181_a]) {
            afloat[EnumFaceDirection.Constants.field_179181_a] = f;
         }
      }

      EnumFaceDirection enumfacedirection = EnumFaceDirection.func_179027_a(p_178408_2_);

      for(int j1 = 0; j1 < 4; ++j1) {
         int k1 = j * j1;
         EnumFaceDirection.VertexInformation enumfacedirection$vertexinformation = enumfacedirection.func_179025_a(j1);
         float f8 = afloat[enumfacedirection$vertexinformation.field_179184_a];
         float f3 = afloat[enumfacedirection$vertexinformation.field_179182_b];
         float f4 = afloat[enumfacedirection$vertexinformation.field_179183_c];
         p_178408_1_[k1] = Float.floatToRawIntBits(f8);
         p_178408_1_[k1 + 1] = Float.floatToRawIntBits(f3);
         p_178408_1_[k1 + 2] = Float.floatToRawIntBits(f4);

         for(int l = 0; l < 4; ++l) {
            int i1 = j * l;
            float f5 = Float.intBitsToFloat(aint[i1]);
            float f6 = Float.intBitsToFloat(aint[i1 + 1]);
            float f7 = Float.intBitsToFloat(aint[i1 + 2]);
            if(MathHelper.func_180185_a(f8, f5) && MathHelper.func_180185_a(f3, f6) && MathHelper.func_180185_a(f4, f7)) {
               p_178408_1_[k1 + 4] = aint[i1 + 4];
               p_178408_1_[k1 + 4 + 1] = aint[i1 + 4 + 1];
            }
         }
      }

   }

   private void func_178401_a(int p_178401_1_, int[] p_178401_2_, EnumFacing p_178401_3_, BlockFaceUV p_178401_4_, TextureAtlasSprite p_178401_5_) {
      int i = p_178401_2_.length / 4;
      int j = i * p_178401_1_;
      float f = Float.intBitsToFloat(p_178401_2_[j]);
      float f1 = Float.intBitsToFloat(p_178401_2_[j + 1]);
      float f2 = Float.intBitsToFloat(p_178401_2_[j + 2]);
      if(f < -0.1F || f >= 1.1F) {
         f -= (float)MathHelper.func_76141_d(f);
      }

      if(f1 < -0.1F || f1 >= 1.1F) {
         f1 -= (float)MathHelper.func_76141_d(f1);
      }

      if(f2 < -0.1F || f2 >= 1.1F) {
         f2 -= (float)MathHelper.func_76141_d(f2);
      }

      float f3 = 0.0F;
      float f4 = 0.0F;
      switch(FaceBakery.FaceBakery$1.field_178400_a[p_178401_3_.ordinal()]) {
      case 1:
         f3 = f * 16.0F;
         f4 = (1.0F - f2) * 16.0F;
         break;
      case 2:
         f3 = f * 16.0F;
         f4 = f2 * 16.0F;
         break;
      case 3:
         f3 = (1.0F - f) * 16.0F;
         f4 = (1.0F - f1) * 16.0F;
         break;
      case 4:
         f3 = f * 16.0F;
         f4 = (1.0F - f1) * 16.0F;
         break;
      case 5:
         f3 = f2 * 16.0F;
         f4 = (1.0F - f1) * 16.0F;
         break;
      case 6:
         f3 = (1.0F - f2) * 16.0F;
         f4 = (1.0F - f1) * 16.0F;
      }

      int k = p_178401_4_.func_178345_c(p_178401_1_) * i;
      p_178401_2_[k + 4] = Float.floatToRawIntBits(p_178401_5_.func_94214_a((double)f3));
      p_178401_2_[k + 4 + 1] = Float.floatToRawIntBits(p_178401_5_.func_94207_b((double)f4));
   }

   static final class FaceBakery$1 {
      static final int[] field_178400_a;
      static final int[] field_178399_b = new int[EnumFacing.Axis.values().length];
      private static final String __OBFID = "CL_00002489";

      static {
         try {
            field_178399_b[EnumFacing.Axis.X.ordinal()] = 1;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            field_178399_b[EnumFacing.Axis.Y.ordinal()] = 2;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            field_178399_b[EnumFacing.Axis.Z.ordinal()] = 3;
         } catch (NoSuchFieldError var7) {
            ;
         }

         field_178400_a = new int[EnumFacing.values().length];

         try {
            field_178400_a[EnumFacing.DOWN.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            field_178400_a[EnumFacing.UP.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            field_178400_a[EnumFacing.NORTH.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            field_178400_a[EnumFacing.SOUTH.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            field_178400_a[EnumFacing.WEST.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            field_178400_a[EnumFacing.EAST.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
