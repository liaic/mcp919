package net.minecraft.client.resources.model;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.src.Reflector;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.model.IModelPart;
import net.minecraftforge.client.model.IModelState;
import net.minecraftforge.client.model.ITransformation;
import net.minecraftforge.client.model.TRSRTransformation;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public enum ModelRotation implements IModelState, ITransformation {
   X0_Y0("X0_Y0", 0, 0, 0),
   X0_Y90("X0_Y90", 1, 0, 90),
   X0_Y180("X0_Y180", 2, 0, 180),
   X0_Y270("X0_Y270", 3, 0, 270),
   X90_Y0("X90_Y0", 4, 90, 0),
   X90_Y90("X90_Y90", 5, 90, 90),
   X90_Y180("X90_Y180", 6, 90, 180),
   X90_Y270("X90_Y270", 7, 90, 270),
   X180_Y0("X180_Y0", 8, 180, 0),
   X180_Y90("X180_Y90", 9, 180, 90),
   X180_Y180("X180_Y180", 10, 180, 180),
   X180_Y270("X180_Y270", 11, 180, 270),
   X270_Y0("X270_Y0", 12, 270, 0),
   X270_Y90("X270_Y90", 13, 270, 90),
   X270_Y180("X270_Y180", 14, 270, 180),
   X270_Y270("X270_Y270", 15, 270, 270);

   private static final Map field_177546_q = Maps.newHashMap();
   private final int field_177545_r;
   private final Matrix4f field_177544_s;
   private final int field_177543_t;
   private final int field_177542_u;
   private static final ModelRotation[] $VALUES = new ModelRotation[]{X0_Y0, X0_Y90, X0_Y180, X0_Y270, X90_Y0, X90_Y90, X90_Y180, X90_Y270, X180_Y0, X180_Y90, X180_Y180, X180_Y270, X270_Y0, X270_Y90, X270_Y180, X270_Y270};
   private static final String __OBFID = "CL_00002393";

   private static int func_177521_b(int p_177521_0_, int p_177521_1_) {
      return p_177521_0_ * 360 + p_177521_1_;
   }

   private ModelRotation(String p_i20_3_, int p_i20_4_, int p_i20_5_, int p_i20_6_) {
      this.field_177545_r = func_177521_b(p_i20_5_, p_i20_6_);
      this.field_177544_s = new Matrix4f();
      Matrix4f matrix4f = new Matrix4f();
      matrix4f.setIdentity();
      Matrix4f.rotate((float)(-p_i20_5_) * 0.017453292F, new Vector3f(1.0F, 0.0F, 0.0F), matrix4f, matrix4f);
      this.field_177543_t = MathHelper.func_76130_a(p_i20_5_ / 90);
      Matrix4f matrix4f1 = new Matrix4f();
      matrix4f1.setIdentity();
      Matrix4f.rotate((float)(-p_i20_6_) * 0.017453292F, new Vector3f(0.0F, 1.0F, 0.0F), matrix4f1, matrix4f1);
      this.field_177542_u = MathHelper.func_76130_a(p_i20_6_ / 90);
      Matrix4f.mul(matrix4f1, matrix4f, this.field_177544_s);
   }

   public Matrix4f func_177525_a() {
      return this.field_177544_s;
   }

   public EnumFacing func_177523_a(EnumFacing p_177523_1_) {
      EnumFacing enumfacing = p_177523_1_;

      for(int i = 0; i < this.field_177543_t; ++i) {
         enumfacing = enumfacing.func_176732_a(EnumFacing.Axis.X);
      }

      if(enumfacing.func_176740_k() != EnumFacing.Axis.Y) {
         for(int j = 0; j < this.field_177542_u; ++j) {
            enumfacing = enumfacing.func_176732_a(EnumFacing.Axis.Y);
         }
      }

      return enumfacing;
   }

   public int func_177520_a(EnumFacing p_177520_1_, int p_177520_2_) {
      int i = p_177520_2_;
      if(p_177520_1_.func_176740_k() == EnumFacing.Axis.X) {
         i = (p_177520_2_ + this.field_177543_t) % 4;
      }

      EnumFacing enumfacing = p_177520_1_;

      for(int j = 0; j < this.field_177543_t; ++j) {
         enumfacing = enumfacing.func_176732_a(EnumFacing.Axis.X);
      }

      if(enumfacing.func_176740_k() == EnumFacing.Axis.Y) {
         i = (i + this.field_177542_u) % 4;
      }

      return i;
   }

   public static ModelRotation func_177524_a(int p_177524_0_, int p_177524_1_) {
      return (ModelRotation)field_177546_q.get(Integer.valueOf(func_177521_b(MathHelper.func_180184_b(p_177524_0_, 360), MathHelper.func_180184_b(p_177524_1_, 360))));
   }

   public Optional<TRSRTransformation> apply(Optional<? extends IModelPart> p_apply_1_) {
      return (Optional)Reflector.call(Reflector.ForgeHooksClient_applyTransform, new Object[]{this.getMatrix(), p_apply_1_});
   }

   public javax.vecmath.Matrix4f getMatrix() {
      return Reflector.ForgeHooksClient_getMatrix.exists()?(javax.vecmath.Matrix4f)Reflector.call(Reflector.ForgeHooksClient_getMatrix, new Object[]{this}):new javax.vecmath.Matrix4f(this.func_177525_a());
   }

   public EnumFacing rotate(EnumFacing p_rotate_1_) {
      return this.func_177523_a(p_rotate_1_);
   }

   public int rotate(EnumFacing p_rotate_1_, int p_rotate_2_) {
      return this.func_177520_a(p_rotate_1_, p_rotate_2_);
   }

   static {
      for(ModelRotation modelrotation : values()) {
         field_177546_q.put(Integer.valueOf(modelrotation.field_177545_r), modelrotation);
      }

   }
}
