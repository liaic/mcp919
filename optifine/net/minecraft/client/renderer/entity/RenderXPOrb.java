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
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.CustomColors;

public class RenderXPOrb extends Render {

   public static final ResourceLocation experienceOrbTextures = new ResourceLocation("textures/entity/experience_orb.png");
   public static final String __OBFID = "CL_00000993";


   public RenderXPOrb(RenderManager p_i46178_1_) {
      super(p_i46178_1_);
      this.shadowSize = 0.15F;
      this.shadowOpaque = 0.75F;
   }

   public void doRender(EntityXPOrb p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      GlStateManager.pushMatrix();
      GlStateManager.translate((float)p_76986_2_, (float)p_76986_4_, (float)p_76986_6_);
      this.bindEntityTexture(p_76986_1_);
      int var10 = p_76986_1_.getTextureByXP();
      float var11 = (float)(var10 % 4 * 16 + 0) / 64.0F;
      float var12 = (float)(var10 % 4 * 16 + 16) / 64.0F;
      float var13 = (float)(var10 / 4 * 16 + 0) / 64.0F;
      float var14 = (float)(var10 / 4 * 16 + 16) / 64.0F;
      float var15 = 1.0F;
      float var16 = 0.5F;
      float var17 = 0.25F;
      int var18 = p_76986_1_.getBrightnessForRender(p_76986_9_);
      int var19 = var18 % 65536;
      int var20 = var18 / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var19 / 1.0F, (float)var20 / 1.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      float var26 = 255.0F;
      float var27 = ((float)p_76986_1_.xpColor + p_76986_9_) / 2.0F;
      var20 = (int)((MathHelper.sin(var27 + 0.0F) + 1.0F) * 0.5F * 255.0F);
      boolean var21 = true;
      int var22 = (int)((MathHelper.sin(var27 + 4.1887903F) + 1.0F) * 0.1F * 255.0F);
      GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      float var23 = 0.3F;
      GlStateManager.scale(0.3F, 0.3F, 0.3F);
      Tessellator var24 = Tessellator.getInstance();
      WorldRenderer var25 = var24.getWorldRenderer();
      var25.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
      int red = var20;
      int green = 255;
      int blue = var22;
      if(Config.isCustomColors()) {
         int col = CustomColors.getXpOrbColor(var27);
         if(col >= 0) {
            red = col >> 16 & 255;
            green = col >> 8 & 255;
            blue = col >> 0 & 255;
         }
      }

      var25.pos((double)(0.0F - var16), (double)(0.0F - var17), 0.0D).tex((double)var11, (double)var14).color(red, green, blue, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
      var25.pos((double)(var15 - var16), (double)(0.0F - var17), 0.0D).tex((double)var12, (double)var14).color(red, green, blue, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
      var25.pos((double)(var15 - var16), (double)(1.0F - var17), 0.0D).tex((double)var12, (double)var13).color(red, green, blue, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
      var25.pos((double)(0.0F - var16), (double)(1.0F - var17), 0.0D).tex((double)var11, (double)var13).color(red, green, blue, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
      var24.draw();
      GlStateManager.disableBlend();
      GlStateManager.disableRescaleNormal();
      GlStateManager.popMatrix();
      super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
   }

   public ResourceLocation getEntityTexture(EntityXPOrb p_180555_1_) {
      return experienceOrbTextures;
   }

   public ResourceLocation getEntityTexture(Entity p_110775_1_) {
      return this.getEntityTexture((EntityXPOrb)p_110775_1_);
   }

   public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      this.doRender((EntityXPOrb)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
   }

}
