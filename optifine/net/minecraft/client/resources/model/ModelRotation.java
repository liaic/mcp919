package net.minecraft.client.resources.model;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.model.IModelPart;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ITransformation;
import net.minecraftforge.client.model.TRSRTransformation;
import optifine.Reflector;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public enum ModelRotation implements IModelState, ITransformation {

   X0_Y0("X0_Y0", 0, "X0_Y0", 0, 0, 0),
   X0_Y90("X0_Y90", 1, "X0_Y90", 1, 0, 90),
   X0_Y180("X0_Y180", 2, "X0_Y180", 2, 0, 180),
   X0_Y270("X0_Y270", 3, "X0_Y270", 3, 0, 270),
   X90_Y0("X90_Y0", 4, "X90_Y0", 4, 90, 0),
   X90_Y90("X90_Y90", 5, "X90_Y90", 5, 90, 90),
   X90_Y180("X90_Y180", 6, "X90_Y180", 6, 90, 180),
   X90_Y270("X90_Y270", 7, "X90_Y270", 7, 90, 270),
   X180_Y0("X180_Y0", 8, "X180_Y0", 8, 180, 0),
   X180_Y90("X180_Y90", 9, "X180_Y90", 9, 180, 90),
   X180_Y180("X180_Y180", 10, "X180_Y180", 10, 180, 180),
   X180_Y270("X180_Y270", 11, "X180_Y270", 11, 180, 270),
   X270_Y0("X270_Y0", 12, "X270_Y0", 12, 270, 0),
   X270_Y90("X270_Y90", 13, "X270_Y90", 13, 270, 90),
   X270_Y180("X270_Y180", 14, "X270_Y180", 14, 270, 180),
   X270_Y270("X270_Y270", 15, "X270_Y270", 15, 270, 270);
   public static final Map mapRotations = Maps.newHashMap();
   public final int combinedXY;
   public final Matrix4f matrix4d;
   public final int quartersX;
   public final int quartersY;
   public static final ModelRotation[] $VALUES = new ModelRotation[]{X0_Y0, X0_Y90, X0_Y180, X0_Y270, X90_Y0, X90_Y90, X90_Y180, X90_Y270, X180_Y0, X180_Y90, X180_Y180, X180_Y270, X270_Y0, X270_Y90, X270_Y180, X270_Y270};
   public static final String __OBFID = "CL_00002393";
   // $FF: synthetic field
   public static final ModelRotation[] $VALUES$ = new ModelRotation[]{X0_Y0, X0_Y90, X0_Y180, X0_Y270, X90_Y0, X90_Y90, X90_Y180, X90_Y270, X180_Y0, X180_Y90, X180_Y180, X180_Y270, X270_Y0, X270_Y90, X270_Y180, X270_Y270};


   public static int combineXY(int p_177521_0_, int p_177521_1_) {
      return p_177521_0_ * 360 + p_177521_1_;
   }

   public ModelRotation(String var1, int var2, String p_i46087_1_, int p_i46087_2_, int p_i46087_3_, int p_i46087_4_) {
      this.combinedXY = combineXY(p_i46087_3_, p_i46087_4_);
      this.matrix4d = new Matrix4f();
      Matrix4f var5 = new Matrix4f();
      var5.setIdentity();
      Matrix4f.rotate((float)(-p_i46087_3_) * 0.017453292F, new Vector3f(1.0F, 0.0F, 0.0F), var5, var5);
      this.quartersX = MathHelper.abs_int(p_i46087_3_ / 90);
      Matrix4f var6 = new Matrix4f();
      var6.setIdentity();
      Matrix4f.rotate((float)(-p_i46087_4_) * 0.017453292F, new Vector3f(0.0F, 1.0F, 0.0F), var6, var6);
      this.quartersY = MathHelper.abs_int(p_i46087_4_ / 90);
      Matrix4f.mul(var6, var5, this.matrix4d);
   }

   public Matrix4f getMatrix4d() {
      return this.matrix4d;
   }

   public EnumFacing rotateFace(EnumFacing p_177523_1_) {
      EnumFacing var2 = p_177523_1_;

      int var3;
      for(var3 = 0; var3 < this.quartersX; ++var3) {
         var2 = var2.rotateAround(EnumFacing.Axis.X);
      }

      if(var2.getAxis() != EnumFacing.Axis.Y) {
         for(var3 = 0; var3 < this.quartersY; ++var3) {
            var2 = var2.rotateAround(EnumFacing.Axis.Y);
         }
      }

      return var2;
   }

   public int rotateVertex(EnumFacing facing, int vertexIndex) {
      int var3 = vertexIndex;
      if(facing.getAxis() == EnumFacing.Axis.X) {
         var3 = (vertexIndex + this.quartersX) % 4;
      }

      EnumFacing var4 = facing;

      for(int var5 = 0; var5 < this.quartersX; ++var5) {
         var4 = var4.rotateAround(EnumFacing.Axis.X);
      }

      if(var4.getAxis() == EnumFacing.Axis.Y) {
         var3 = (var3 + this.quartersY) % 4;
      }

      return var3;
   }

   public static ModelRotation getModelRotation(int p_177524_0_, int p_177524_1_) {
      return (ModelRotation)mapRotations.get(Integer.valueOf(combineXY(MathHelper.normalizeAngle(p_177524_0_, 360), MathHelper.normalizeAngle(p_177524_1_, 360))));
   }

   public Optional<TRSRTransformation> apply(Optional<? extends IModelPart> part) {
      return (Optional)Reflector.call(Reflector.ForgeHooksClient_applyTransform, new Object[]{this.getMatrix(), part});
   }

   public javax.vecmath.Matrix4f getMatrix() {
      return Reflector.ForgeHooksClient_getMatrix.exists()?(javax.vecmath.Matrix4f)Reflector.call(Reflector.ForgeHooksClient_getMatrix, new Object[]{this}):new javax.vecmath.Matrix4f(this.getMatrix4d());
   }

   public EnumFacing rotate(EnumFacing facing) {
      return this.rotateFace(facing);
   }

   public int rotate(EnumFacing facing, int vertexIndex) {
      return this.rotateVertex(facing, vertexIndex);
   }

   static {
      ModelRotation[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         ModelRotation var3 = var0[var2];
         mapRotations.put(Integer.valueOf(var3.combinedXY), var3);
      }

   }
}
