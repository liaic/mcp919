package net.minecraft.client.renderer.texture;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;
import shadersmod.client.MultiTexID;
import shadersmod.client.ShadersTex;

public abstract class AbstractTexture implements ITextureObject {
   protected int field_110553_a = -1;
   protected boolean field_174940_b;
   protected boolean field_174941_c;
   protected boolean field_174938_d;
   protected boolean field_174939_e;
   private static final String __OBFID = "CL_00001047";
   public MultiTexID multiTex;

   public void func_174937_a(boolean p_174937_1_, boolean p_174937_2_) {
      this.field_174940_b = p_174937_1_;
      this.field_174941_c = p_174937_2_;
      boolean flag = true;
      boolean flag1 = true;
      int i;
      short short1;
      if(p_174937_1_) {
         i = p_174937_2_?9987:9729;
         short1 = 9729;
      } else {
         i = p_174937_2_?9986:9728;
         short1 = 9728;
      }

      GlStateManager.func_179144_i(this.func_110552_b());
      GL11.glTexParameteri(3553, 10241, i);
      GL11.glTexParameteri(3553, 10240, short1);
   }

   public void func_174936_b(boolean p_174936_1_, boolean p_174936_2_) {
      this.field_174938_d = this.field_174940_b;
      this.field_174939_e = this.field_174941_c;
      this.func_174937_a(p_174936_1_, p_174936_2_);
   }

   public void func_174935_a() {
      this.func_174937_a(this.field_174938_d, this.field_174939_e);
   }

   public int func_110552_b() {
      if(this.field_110553_a == -1) {
         this.field_110553_a = TextureUtil.func_110996_a();
      }

      return this.field_110553_a;
   }

   public void func_147631_c() {
      ShadersTex.deleteTextures(this, this.field_110553_a);
      if(this.field_110553_a != -1) {
         TextureUtil.func_147942_a(this.field_110553_a);
         this.field_110553_a = -1;
      }

   }

   public MultiTexID getMultiTexID() {
      return ShadersTex.getMultiTexID(this);
   }
}
