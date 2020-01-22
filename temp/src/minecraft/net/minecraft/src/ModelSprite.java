package net.minecraft.src;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class ModelSprite {
   private ModelRenderer modelRenderer = null;
   private int textureOffsetX = 0;
   private int textureOffsetY = 0;
   private float posX = 0.0F;
   private float posY = 0.0F;
   private float posZ = 0.0F;
   private int sizeX = 0;
   private int sizeY = 0;
   private int sizeZ = 0;
   private float sizeAdd = 0.0F;
   private float minU = 0.0F;
   private float minV = 0.0F;
   private float maxU = 0.0F;
   private float maxV = 0.0F;

   public ModelSprite(ModelRenderer p_i75_1_, int p_i75_2_, int p_i75_3_, float p_i75_4_, float p_i75_5_, float p_i75_6_, int p_i75_7_, int p_i75_8_, int p_i75_9_, float p_i75_10_) {
      this.modelRenderer = p_i75_1_;
      this.textureOffsetX = p_i75_2_;
      this.textureOffsetY = p_i75_3_;
      this.posX = p_i75_4_;
      this.posY = p_i75_5_;
      this.posZ = p_i75_6_;
      this.sizeX = p_i75_7_;
      this.sizeY = p_i75_8_;
      this.sizeZ = p_i75_9_;
      this.sizeAdd = p_i75_10_;
      this.minU = (float)p_i75_2_ / p_i75_1_.field_78801_a;
      this.minV = (float)p_i75_3_ / p_i75_1_.field_78799_b;
      this.maxU = (float)(p_i75_2_ + p_i75_7_) / p_i75_1_.field_78801_a;
      this.maxV = (float)(p_i75_3_ + p_i75_8_) / p_i75_1_.field_78799_b;
   }

   public void render(Tessellator p_render_1_, float p_render_2_) {
      GlStateManager.func_179109_b(this.posX * p_render_2_, this.posY * p_render_2_, this.posZ * p_render_2_);
      float f = this.minU;
      float f1 = this.maxU;
      float f2 = this.minV;
      float f3 = this.maxV;
      if(this.modelRenderer.field_78809_i) {
         f = this.maxU;
         f1 = this.minU;
      }

      if(this.modelRenderer.mirrorV) {
         f2 = this.maxV;
         f3 = this.minV;
      }

      renderItemIn2D(p_render_1_, f, f2, f1, f3, this.sizeX, this.sizeY, p_render_2_ * (float)this.sizeZ, this.modelRenderer.field_78801_a, this.modelRenderer.field_78799_b);
      GlStateManager.func_179109_b(-this.posX * p_render_2_, -this.posY * p_render_2_, -this.posZ * p_render_2_);
   }

   public static void renderItemIn2D(Tessellator p_renderItemIn2D_0_, float p_renderItemIn2D_1_, float p_renderItemIn2D_2_, float p_renderItemIn2D_3_, float p_renderItemIn2D_4_, int p_renderItemIn2D_5_, int p_renderItemIn2D_6_, float p_renderItemIn2D_7_, float p_renderItemIn2D_8_, float p_renderItemIn2D_9_) {
      if(p_renderItemIn2D_7_ < 6.25E-4F) {
         p_renderItemIn2D_7_ = 6.25E-4F;
      }

      float f = p_renderItemIn2D_3_ - p_renderItemIn2D_1_;
      float f1 = p_renderItemIn2D_4_ - p_renderItemIn2D_2_;
      double d0 = (double)(MathHelper.func_76135_e(f) * (p_renderItemIn2D_8_ / 16.0F));
      double d1 = (double)(MathHelper.func_76135_e(f1) * (p_renderItemIn2D_9_ / 16.0F));
      WorldRenderer worldrenderer = p_renderItemIn2D_0_.func_178180_c();
      GL11.glNormal3f(0.0F, 0.0F, -1.0F);
      worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
      worldrenderer.func_181662_b(0.0D, d1, 0.0D).func_181673_a((double)p_renderItemIn2D_1_, (double)p_renderItemIn2D_4_).func_181675_d();
      worldrenderer.func_181662_b(d0, d1, 0.0D).func_181673_a((double)p_renderItemIn2D_3_, (double)p_renderItemIn2D_4_).func_181675_d();
      worldrenderer.func_181662_b(d0, 0.0D, 0.0D).func_181673_a((double)p_renderItemIn2D_3_, (double)p_renderItemIn2D_2_).func_181675_d();
      worldrenderer.func_181662_b(0.0D, 0.0D, 0.0D).func_181673_a((double)p_renderItemIn2D_1_, (double)p_renderItemIn2D_2_).func_181675_d();
      p_renderItemIn2D_0_.func_78381_a();
      GL11.glNormal3f(0.0F, 0.0F, 1.0F);
      worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
      worldrenderer.func_181662_b(0.0D, 0.0D, (double)p_renderItemIn2D_7_).func_181673_a((double)p_renderItemIn2D_1_, (double)p_renderItemIn2D_2_).func_181675_d();
      worldrenderer.func_181662_b(d0, 0.0D, (double)p_renderItemIn2D_7_).func_181673_a((double)p_renderItemIn2D_3_, (double)p_renderItemIn2D_2_).func_181675_d();
      worldrenderer.func_181662_b(d0, d1, (double)p_renderItemIn2D_7_).func_181673_a((double)p_renderItemIn2D_3_, (double)p_renderItemIn2D_4_).func_181675_d();
      worldrenderer.func_181662_b(0.0D, d1, (double)p_renderItemIn2D_7_).func_181673_a((double)p_renderItemIn2D_1_, (double)p_renderItemIn2D_4_).func_181675_d();
      p_renderItemIn2D_0_.func_78381_a();
      float f2 = 0.5F * f / (float)p_renderItemIn2D_5_;
      float f3 = 0.5F * f1 / (float)p_renderItemIn2D_6_;
      GL11.glNormal3f(-1.0F, 0.0F, 0.0F);
      worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);

      for(int i = 0; i < p_renderItemIn2D_5_; ++i) {
         float f4 = (float)i / (float)p_renderItemIn2D_5_;
         float f5 = p_renderItemIn2D_1_ + f * f4 + f2;
         worldrenderer.func_181662_b((double)f4 * d0, d1, (double)p_renderItemIn2D_7_).func_181673_a((double)f5, (double)p_renderItemIn2D_4_).func_181675_d();
         worldrenderer.func_181662_b((double)f4 * d0, d1, 0.0D).func_181673_a((double)f5, (double)p_renderItemIn2D_4_).func_181675_d();
         worldrenderer.func_181662_b((double)f4 * d0, 0.0D, 0.0D).func_181673_a((double)f5, (double)p_renderItemIn2D_2_).func_181675_d();
         worldrenderer.func_181662_b((double)f4 * d0, 0.0D, (double)p_renderItemIn2D_7_).func_181673_a((double)f5, (double)p_renderItemIn2D_2_).func_181675_d();
      }

      p_renderItemIn2D_0_.func_78381_a();
      GL11.glNormal3f(1.0F, 0.0F, 0.0F);
      worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);

      for(int j = 0; j < p_renderItemIn2D_5_; ++j) {
         float f7 = (float)j / (float)p_renderItemIn2D_5_;
         float f10 = p_renderItemIn2D_1_ + f * f7 + f2;
         float f6 = f7 + 1.0F / (float)p_renderItemIn2D_5_;
         worldrenderer.func_181662_b((double)f6 * d0, 0.0D, (double)p_renderItemIn2D_7_).func_181673_a((double)f10, (double)p_renderItemIn2D_2_).func_181675_d();
         worldrenderer.func_181662_b((double)f6 * d0, 0.0D, 0.0D).func_181673_a((double)f10, (double)p_renderItemIn2D_2_).func_181675_d();
         worldrenderer.func_181662_b((double)f6 * d0, d1, 0.0D).func_181673_a((double)f10, (double)p_renderItemIn2D_4_).func_181675_d();
         worldrenderer.func_181662_b((double)f6 * d0, d1, (double)p_renderItemIn2D_7_).func_181673_a((double)f10, (double)p_renderItemIn2D_4_).func_181675_d();
      }

      p_renderItemIn2D_0_.func_78381_a();
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);

      for(int k = 0; k < p_renderItemIn2D_6_; ++k) {
         float f8 = (float)k / (float)p_renderItemIn2D_6_;
         float f11 = p_renderItemIn2D_2_ + f1 * f8 + f3;
         float f13 = f8 + 1.0F / (float)p_renderItemIn2D_6_;
         worldrenderer.func_181662_b(0.0D, (double)f13 * d1, (double)p_renderItemIn2D_7_).func_181673_a((double)p_renderItemIn2D_1_, (double)f11).func_181675_d();
         worldrenderer.func_181662_b(d0, (double)f13 * d1, (double)p_renderItemIn2D_7_).func_181673_a((double)p_renderItemIn2D_3_, (double)f11).func_181675_d();
         worldrenderer.func_181662_b(d0, (double)f13 * d1, 0.0D).func_181673_a((double)p_renderItemIn2D_3_, (double)f11).func_181675_d();
         worldrenderer.func_181662_b(0.0D, (double)f13 * d1, 0.0D).func_181673_a((double)p_renderItemIn2D_1_, (double)f11).func_181675_d();
      }

      p_renderItemIn2D_0_.func_78381_a();
      GL11.glNormal3f(0.0F, -1.0F, 0.0F);
      worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);

      for(int l = 0; l < p_renderItemIn2D_6_; ++l) {
         float f9 = (float)l / (float)p_renderItemIn2D_6_;
         float f12 = p_renderItemIn2D_2_ + f1 * f9 + f3;
         worldrenderer.func_181662_b(d0, (double)f9 * d1, (double)p_renderItemIn2D_7_).func_181673_a((double)p_renderItemIn2D_3_, (double)f12).func_181675_d();
         worldrenderer.func_181662_b(0.0D, (double)f9 * d1, (double)p_renderItemIn2D_7_).func_181673_a((double)p_renderItemIn2D_1_, (double)f12).func_181675_d();
         worldrenderer.func_181662_b(0.0D, (double)f9 * d1, 0.0D).func_181673_a((double)p_renderItemIn2D_1_, (double)f12).func_181675_d();
         worldrenderer.func_181662_b(d0, (double)f9 * d1, 0.0D).func_181673_a((double)p_renderItemIn2D_3_, (double)f12).func_181675_d();
      }

      p_renderItemIn2D_0_.func_78381_a();
   }
}
