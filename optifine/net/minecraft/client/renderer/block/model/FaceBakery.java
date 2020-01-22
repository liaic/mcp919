package net.minecraft.client.renderer.block.model;

import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.client.renderer.EnumFaceDirection.Constants;
import net.minecraft.client.renderer.EnumFaceDirection.VertexInformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;
import net.minecraftforge.client.model.ITransformation;
import optifine.Config;
import optifine.Reflector;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import shadersmod.client.Shaders;

public class FaceBakery {

   public static final float SCALE_ROTATION_22_5 = 1.0F / (float)Math.cos(0.39269909262657166D) - 1.0F;
   public static final float SCALE_ROTATION_GENERAL = 1.0F / (float)Math.cos(0.7853981633974483D) - 1.0F;
   public static final String __OBFID = "CL_00002490";


   public BakedQuad makeBakedQuad(Vector3f posFrom, Vector3f posTo, BlockPartFace face, TextureAtlasSprite sprite, EnumFacing facing, ModelRotation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade) {
      return this.makeBakedQuad(posFrom, posTo, face, sprite, facing, (ITransformation)modelRotationIn, partRotation, uvLocked, shade);
   }

   public BakedQuad makeBakedQuad(Vector3f posFrom, Vector3f posTo, BlockPartFace face, TextureAtlasSprite sprite, EnumFacing facing, ITransformation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade) {
      int[] var10 = this.makeQuadVertexData(face, sprite, facing, this.getPositionsDiv16(posFrom, posTo), modelRotationIn, partRotation, uvLocked, shade);
      EnumFacing var11 = getFacingFromVertexData(var10);
      if(uvLocked) {
         this.lockUv(var10, var11, face.blockFaceUV, sprite);
      }

      if(partRotation == null) {
         this.applyFacing(var10, var11);
      }

      if(Reflector.ForgeHooksClient_fillNormal.exists()) {
         Reflector.callVoid(Reflector.ForgeHooksClient_fillNormal, new Object[]{var10, var11});
      }

      return new BakedQuad(var10, face.tintIndex, var11, sprite);
   }

   public int[] makeQuadVertexData(BlockPartFace p_178405_1_, TextureAtlasSprite p_178405_2_, EnumFacing p_178405_3_, float[] p_178405_4_, ITransformation p_178405_5_, BlockPartRotation p_178405_6_, boolean p_178405_7_, boolean shade) {
      byte vertexSize = 28;
      if(Config.isShaders()) {
         vertexSize = 56;
      }

      int[] var9 = new int[vertexSize];

      for(int var10 = 0; var10 < 4; ++var10) {
         this.fillVertexData(var9, var10, p_178405_3_, p_178405_1_, p_178405_4_, p_178405_2_, p_178405_5_, p_178405_6_, p_178405_7_, shade);
      }

      return var9;
   }

   public int getFaceShadeColor(EnumFacing p_178413_1_) {
      float var2 = this.getFaceBrightness(p_178413_1_);
      int var3 = MathHelper.clamp_int((int)(var2 * 255.0F), 0, 255);
      return -16777216 | var3 << 16 | var3 << 8 | var3;
   }

   public float getFaceBrightness(EnumFacing p_178412_1_) {
      switch(FaceBakery.SwitchEnumFacing.field_178400_a[p_178412_1_.ordinal()]) {
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

   public float[] getPositionsDiv16(Vector3f pos1, Vector3f pos2) {
      float[] var3 = new float[EnumFacing.values().length];
      var3[Constants.WEST_INDEX] = pos1.x / 16.0F;
      var3[Constants.DOWN_INDEX] = pos1.y / 16.0F;
      var3[Constants.NORTH_INDEX] = pos1.z / 16.0F;
      var3[Constants.EAST_INDEX] = pos2.x / 16.0F;
      var3[Constants.UP_INDEX] = pos2.y / 16.0F;
      var3[Constants.SOUTH_INDEX] = pos2.z / 16.0F;
      return var3;
   }

   public void fillVertexData(int[] faceData, int vertexIndex, EnumFacing facing, BlockPartFace partFace, float[] p_178402_5_, TextureAtlasSprite sprite, ITransformation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade) {
      EnumFacing var11 = modelRotationIn.rotate(facing);
      int var12 = shade?this.getFaceShadeColor(var11):-1;
      VertexInformation var13 = EnumFaceDirection.getFacing(facing).getVertexInformation(vertexIndex);
      Vector3f var14 = new Vector3f(p_178402_5_[var13.xIndex], p_178402_5_[var13.yIndex], p_178402_5_[var13.zIndex]);
      this.rotatePart(var14, partRotation);
      int var15 = this.rotateVertex(var14, facing, vertexIndex, modelRotationIn, uvLocked);
      this.storeVertexData(faceData, var15, vertexIndex, var14, var12, sprite, partFace.blockFaceUV);
   }

   public void storeVertexData(int[] faceData, int storeIndex, int vertexIndex, Vector3f position, int shadeColor, TextureAtlasSprite sprite, BlockFaceUV faceUV) {
      int step = faceData.length / 4;
      int var8 = storeIndex * step;
      faceData[var8] = Float.floatToRawIntBits(position.x);
      faceData[var8 + 1] = Float.floatToRawIntBits(position.y);
      faceData[var8 + 2] = Float.floatToRawIntBits(position.z);
      faceData[var8 + 3] = shadeColor;
      faceData[var8 + 4] = Float.floatToRawIntBits(sprite.getInterpolatedU((double)faceUV.func_178348_a(vertexIndex)));
      faceData[var8 + 4 + 1] = Float.floatToRawIntBits(sprite.getInterpolatedV((double)faceUV.func_178346_b(vertexIndex)));
   }

   public void rotatePart(Vector3f p_178407_1_, BlockPartRotation p_178407_2_) {
      if(p_178407_2_ != null) {
         Matrix4f var3 = this.getMatrixIdentity();
         Vector3f var4 = new Vector3f(0.0F, 0.0F, 0.0F);
         switch(FaceBakery.SwitchEnumFacing.field_178399_b[p_178407_2_.axis.ordinal()]) {
         case 1:
            Matrix4f.rotate(p_178407_2_.angle * 0.017453292F, new Vector3f(1.0F, 0.0F, 0.0F), var3, var3);
            var4.set(0.0F, 1.0F, 1.0F);
            break;
         case 2:
            Matrix4f.rotate(p_178407_2_.angle * 0.017453292F, new Vector3f(0.0F, 1.0F, 0.0F), var3, var3);
            var4.set(1.0F, 0.0F, 1.0F);
            break;
         case 3:
            Matrix4f.rotate(p_178407_2_.angle * 0.017453292F, new Vector3f(0.0F, 0.0F, 1.0F), var3, var3);
            var4.set(1.0F, 1.0F, 0.0F);
         }

         if(p_178407_2_.rescale) {
            if(Math.abs(p_178407_2_.angle) == 22.5F) {
               var4.scale(SCALE_ROTATION_22_5);
            } else {
               var4.scale(SCALE_ROTATION_GENERAL);
            }

            Vector3f.add(var4, new Vector3f(1.0F, 1.0F, 1.0F), var4);
         } else {
            var4.set(1.0F, 1.0F, 1.0F);
         }

         this.rotateScale(p_178407_1_, new Vector3f(p_178407_2_.origin), var3, var4);
      }

   }

   public int rotateVertex(Vector3f position, EnumFacing facing, int vertexIndex, ModelRotation modelRotationIn, boolean uvLocked) {
      return this.rotateVertex(position, facing, vertexIndex, (ITransformation)modelRotationIn, uvLocked);
   }

   public int rotateVertex(Vector3f position, EnumFacing facing, int vertexIndex, ITransformation modelRotationIn, boolean uvLocked) {
      if(modelRotationIn == ModelRotation.X0_Y0) {
         return vertexIndex;
      } else {
         if(Reflector.ForgeHooksClient_transform.exists()) {
            Reflector.call(Reflector.ForgeHooksClient_transform, new Object[]{position, modelRotationIn.getMatrix()});
         } else {
            this.rotateScale(position, new Vector3f(0.5F, 0.5F, 0.5F), ((ModelRotation)modelRotationIn).getMatrix4d(), new Vector3f(1.0F, 1.0F, 1.0F));
         }

         return modelRotationIn.rotate(facing, vertexIndex);
      }
   }

   public void rotateScale(Vector3f position, Vector3f rotationOrigin, Matrix4f rotationMatrix, Vector3f scale) {
      Vector4f var5 = new Vector4f(position.x - rotationOrigin.x, position.y - rotationOrigin.y, position.z - rotationOrigin.z, 1.0F);
      Matrix4f.transform(rotationMatrix, var5, var5);
      var5.x *= scale.x;
      var5.y *= scale.y;
      var5.z *= scale.z;
      position.set(var5.x + rotationOrigin.x, var5.y + rotationOrigin.y, var5.z + rotationOrigin.z);
   }

   public Matrix4f getMatrixIdentity() {
      Matrix4f var1 = new Matrix4f();
      var1.setIdentity();
      return var1;
   }

   public static EnumFacing getFacingFromVertexData(int[] p_178410_0_) {
      int step = p_178410_0_.length / 4;
      int step2 = step * 2;
      int step3 = step * 3;
      Vector3f var1 = new Vector3f(Float.intBitsToFloat(p_178410_0_[0]), Float.intBitsToFloat(p_178410_0_[1]), Float.intBitsToFloat(p_178410_0_[2]));
      Vector3f var2 = new Vector3f(Float.intBitsToFloat(p_178410_0_[step]), Float.intBitsToFloat(p_178410_0_[step + 1]), Float.intBitsToFloat(p_178410_0_[step + 2]));
      Vector3f var3 = new Vector3f(Float.intBitsToFloat(p_178410_0_[step2]), Float.intBitsToFloat(p_178410_0_[step2 + 1]), Float.intBitsToFloat(p_178410_0_[step2 + 2]));
      Vector3f var4 = new Vector3f();
      Vector3f var5 = new Vector3f();
      Vector3f var6 = new Vector3f();
      Vector3f.sub(var1, var2, var4);
      Vector3f.sub(var3, var2, var5);
      Vector3f.cross(var5, var4, var6);
      float var7 = (float)Math.sqrt((double)(var6.x * var6.x + var6.y * var6.y + var6.z * var6.z));
      var6.x /= var7;
      var6.y /= var7;
      var6.z /= var7;
      EnumFacing var8 = null;
      float var9 = 0.0F;
      EnumFacing[] var10 = EnumFacing.values();
      int var11 = var10.length;

      for(int var12 = 0; var12 < var11; ++var12) {
         EnumFacing var13 = var10[var12];
         Vec3i var14 = var13.getDirectionVec();
         Vector3f var15 = new Vector3f((float)var14.getX(), (float)var14.getY(), (float)var14.getZ());
         float var16 = Vector3f.dot(var6, var15);
         if(var16 >= 0.0F && var16 > var9) {
            var9 = var16;
            var8 = var13;
         }
      }

      if(var9 < 0.719F) {
         if(var8 != EnumFacing.EAST && var8 != EnumFacing.WEST && var8 != EnumFacing.NORTH && var8 != EnumFacing.SOUTH) {
            var8 = EnumFacing.UP;
         } else {
            var8 = EnumFacing.NORTH;
         }
      }

      return var8 == null?EnumFacing.UP:var8;
   }

   public void lockUv(int[] p_178409_1_, EnumFacing p_178409_2_, BlockFaceUV p_178409_3_, TextureAtlasSprite p_178409_4_) {
      for(int var5 = 0; var5 < 4; ++var5) {
         this.lockVertexUv(var5, p_178409_1_, p_178409_2_, p_178409_3_, p_178409_4_);
      }

   }

   public void applyFacing(int[] p_178408_1_, EnumFacing p_178408_2_) {
      int[] var3 = new int[p_178408_1_.length];
      System.arraycopy(p_178408_1_, 0, var3, 0, p_178408_1_.length);
      float[] var4 = new float[EnumFacing.values().length];
      var4[Constants.WEST_INDEX] = 999.0F;
      var4[Constants.DOWN_INDEX] = 999.0F;
      var4[Constants.NORTH_INDEX] = 999.0F;
      var4[Constants.EAST_INDEX] = -999.0F;
      var4[Constants.UP_INDEX] = -999.0F;
      var4[Constants.SOUTH_INDEX] = -999.0F;
      int step = p_178408_1_.length / 4;

      int var6;
      float var9;
      for(int var17 = 0; var17 < 4; ++var17) {
         var6 = step * var17;
         float var18 = Float.intBitsToFloat(var3[var6]);
         float var19 = Float.intBitsToFloat(var3[var6 + 1]);
         var9 = Float.intBitsToFloat(var3[var6 + 2]);
         if(var18 < var4[Constants.WEST_INDEX]) {
            var4[Constants.WEST_INDEX] = var18;
         }

         if(var19 < var4[Constants.DOWN_INDEX]) {
            var4[Constants.DOWN_INDEX] = var19;
         }

         if(var9 < var4[Constants.NORTH_INDEX]) {
            var4[Constants.NORTH_INDEX] = var9;
         }

         if(var18 > var4[Constants.EAST_INDEX]) {
            var4[Constants.EAST_INDEX] = var18;
         }

         if(var19 > var4[Constants.UP_INDEX]) {
            var4[Constants.UP_INDEX] = var19;
         }

         if(var9 > var4[Constants.SOUTH_INDEX]) {
            var4[Constants.SOUTH_INDEX] = var9;
         }
      }

      EnumFaceDirection var181 = EnumFaceDirection.getFacing(p_178408_2_);

      for(var6 = 0; var6 < 4; ++var6) {
         int var191 = step * var6;
         VertexInformation var20 = var181.getVertexInformation(var6);
         var9 = var4[var20.xIndex];
         float var10 = var4[var20.yIndex];
         float var11 = var4[var20.zIndex];
         p_178408_1_[var191] = Float.floatToRawIntBits(var9);
         p_178408_1_[var191 + 1] = Float.floatToRawIntBits(var10);
         p_178408_1_[var191 + 2] = Float.floatToRawIntBits(var11);

         for(int var12 = 0; var12 < 4; ++var12) {
            int var13 = step * var12;
            float var14 = Float.intBitsToFloat(var3[var13]);
            float var15 = Float.intBitsToFloat(var3[var13 + 1]);
            float var16 = Float.intBitsToFloat(var3[var13 + 2]);
            if(MathHelper.epsilonEquals(var9, var14) && MathHelper.epsilonEquals(var10, var15) && MathHelper.epsilonEquals(var11, var16)) {
               p_178408_1_[var191 + 4] = var3[var13 + 4];
               p_178408_1_[var191 + 4 + 1] = var3[var13 + 4 + 1];
            }
         }
      }

   }

   public void lockVertexUv(int p_178401_1_, int[] p_178401_2_, EnumFacing p_178401_3_, BlockFaceUV p_178401_4_, TextureAtlasSprite p_178401_5_) {
      int step = p_178401_2_.length / 4;
      int var6 = step * p_178401_1_;
      float var7 = Float.intBitsToFloat(p_178401_2_[var6]);
      float var8 = Float.intBitsToFloat(p_178401_2_[var6 + 1]);
      float var9 = Float.intBitsToFloat(p_178401_2_[var6 + 2]);
      if(var7 < -0.1F || var7 >= 1.1F) {
         var7 -= (float)MathHelper.floor_float(var7);
      }

      if(var8 < -0.1F || var8 >= 1.1F) {
         var8 -= (float)MathHelper.floor_float(var8);
      }

      if(var9 < -0.1F || var9 >= 1.1F) {
         var9 -= (float)MathHelper.floor_float(var9);
      }

      float var10 = 0.0F;
      float var11 = 0.0F;
      switch(FaceBakery.SwitchEnumFacing.field_178400_a[p_178401_3_.ordinal()]) {
      case 1:
         var10 = var7 * 16.0F;
         var11 = (1.0F - var9) * 16.0F;
         break;
      case 2:
         var10 = var7 * 16.0F;
         var11 = var9 * 16.0F;
         break;
      case 3:
         var10 = (1.0F - var7) * 16.0F;
         var11 = (1.0F - var8) * 16.0F;
         break;
      case 4:
         var10 = var7 * 16.0F;
         var11 = (1.0F - var8) * 16.0F;
         break;
      case 5:
         var10 = var9 * 16.0F;
         var11 = (1.0F - var8) * 16.0F;
         break;
      case 6:
         var10 = (1.0F - var9) * 16.0F;
         var11 = (1.0F - var8) * 16.0F;
      }

      int var12 = p_178401_4_.func_178345_c(p_178401_1_) * step;
      p_178401_2_[var12 + 4] = Float.floatToRawIntBits(p_178401_5_.getInterpolatedU((double)var10));
      p_178401_2_[var12 + 4 + 1] = Float.floatToRawIntBits(p_178401_5_.getInterpolatedV((double)var11));
   }


   public static final class SwitchEnumFacing {

      public static final int[] field_178400_a;
      public static final int[] field_178399_b = new int[EnumFacing.Axis.values().length];
      public static final String __OBFID = "CL_00002489";


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
