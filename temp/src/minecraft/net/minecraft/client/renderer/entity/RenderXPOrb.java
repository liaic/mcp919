package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.src.Config;
import net.minecraft.src.CustomColors;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderXPOrb extends Render {
   private static final ResourceLocation field_110785_a = new ResourceLocation("textures/entity/experience_orb.png");
   private static final String __OBFID = "CL_00000993";

   public RenderXPOrb(RenderManager p_i46178_1_) {
      super(p_i46178_1_);
      this.field_76989_e = 0.15F;
      this.field_76987_f = 0.75F;
   }

   public void func_76986_a(EntityXPOrb p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      GlStateManager.func_179094_E();
      GlStateManager.func_179109_b((float)p_76986_2_, (float)p_76986_4_, (float)p_76986_6_);
      this.func_180548_c(p_76986_1_);
      int i = p_76986_1_.func_70528_g();
      float f = (float)(i % 4 * 16 + 0) / 64.0F;
      float f1 = (float)(i % 4 * 16 + 16) / 64.0F;
      float f2 = (float)(i / 4 * 16 + 0) / 64.0F;
      float f3 = (float)(i / 4 * 16 + 16) / 64.0F;
      float f4 = 1.0F;
      float f5 = 0.5F;
      float f6 = 0.25F;
      int j = p_76986_1_.func_70070_b(p_76986_9_);
      int k = j % 65536;
      int l = j / 65536;
      OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)k / 1.0F, (float)l / 1.0F);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      float f7 = 255.0F;
      float f8 = ((float)p_76986_1_.field_70533_a + p_76986_9_) / 2.0F;
      l = (int)((MathHelper.func_76126_a(f8 + 0.0F) + 1.0F) * 0.5F * 255.0F);
      boolean flag = true;
      int i1 = (int)((MathHelper.func_76126_a(f8 + 4.1887903F) + 1.0F) * 0.1F * 255.0F);
      GlStateManager.func_179114_b(180.0F - this.field_76990_c.field_78735_i, 0.0F, 1.0F, 0.0F);
      GlStateManager.func_179114_b(-this.field_76990_c.field_78732_j, 1.0F, 0.0F, 0.0F);
      float f9 = 0.3F;
      GlStateManager.func_179152_a(0.3F, 0.3F, 0.3F);
      Tessellator tessellator = Tessellator.func_178181_a();
      WorldRenderer worldrenderer = tessellator.func_178180_c();
      worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181712_l);
      int j1 = l;
      int k1 = 255;
      int l1 = i1;
      if(Config.isCustomColors()) {
         int i2 = CustomColors.getXpOrbColor(f8);
         if(i2 >= 0) {
            j1 = i2 >> 16 & 255;
            k1 = i2 >> 8 & 255;
            l1 = i2 >> 0 & 255;
         }
      }

      worldrenderer.func_181662_b((double)(0.0F - f5), (double)(0.0F - f6), 0.0D).func_181673_a((double)f, (double)f3).func_181669_b(j1, k1, l1, 128).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      worldrenderer.func_181662_b((double)(f4 - f5), (double)(0.0F - f6), 0.0D).func_181673_a((double)f1, (double)f3).func_181669_b(j1, k1, l1, 128).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      worldrenderer.func_181662_b((double)(f4 - f5), (double)(1.0F - f6), 0.0D).func_181673_a((double)f1, (double)f2).func_181669_b(j1, k1, l1, 128).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      worldrenderer.func_181662_b((double)(0.0F - f5), (double)(1.0F - f6), 0.0D).func_181673_a((double)f, (double)f2).func_181669_b(j1, k1, l1, 128).func_181663_c(0.0F, 1.0F, 0.0F).func_181675_d();
      tessellator.func_78381_a();
      GlStateManager.func_179084_k();
      GlStateManager.func_179101_C();
      GlStateManager.func_179121_F();
      super.func_76986_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
   }

   protected ResourceLocation func_110775_a(EntityXPOrb p_110775_1_) {
      return field_110785_a;
   }

   protected ResourceLocation func_110775_a(Entity p_110775_1_) {
      return this.func_110775_a((EntityXPOrb)p_110775_1_);
   }

   public void func_76986_a(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      this.func_76986_a((EntityXPOrb)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
   }
}
