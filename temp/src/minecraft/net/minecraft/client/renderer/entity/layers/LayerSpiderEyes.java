package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import shadersmod.client.Shaders;

public class LayerSpiderEyes implements LayerRenderer {
   private static final ResourceLocation field_177150_a = new ResourceLocation("textures/entity/spider_eyes.png");
   private final RenderSpider field_177149_b;
   private static final String __OBFID = "CL_00002410";

   public LayerSpiderEyes(RenderSpider p_i46109_1_) {
      this.field_177149_b = p_i46109_1_;
   }

   public void func_177141_a(EntitySpider p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
      this.field_177149_b.func_110776_a(field_177150_a);
      GlStateManager.func_179147_l();
      GlStateManager.func_179118_c();
      GlStateManager.func_179112_b(1, 1);
      if(p_177141_1_.func_82150_aj()) {
         GlStateManager.func_179132_a(false);
      } else {
         GlStateManager.func_179132_a(true);
      }

      char c0 = '\uf0f0';
      int i = c0 % 65536;
      int j = c0 / 65536;
      OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)i / 1.0F, (float)j / 1.0F);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      if(Config.isShaders()) {
         Shaders.beginSpiderEyes();
      }

      this.field_177149_b.func_177087_b().func_78088_a(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
      if(Config.isShaders()) {
         Shaders.endSpiderEyes();
      }

      int k = p_177141_1_.func_70070_b(p_177141_4_);
      i = k % 65536;
      j = k / 65536;
      OpenGlHelper.func_77475_a(OpenGlHelper.field_77476_b, (float)i / 1.0F, (float)j / 1.0F);
      this.field_177149_b.func_177105_a(p_177141_1_, p_177141_4_);
      GlStateManager.func_179084_k();
      GlStateManager.func_179141_d();
   }

   public boolean func_177142_b() {
      return false;
   }

   public void func_177141_a(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
      this.func_177141_a((EntitySpider)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
   }
}
