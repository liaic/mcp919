package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import shadersmod.client.Shaders;

public class LayerEndermanEyes implements LayerRenderer {

   public static final ResourceLocation RES_ENDERMAN_EYES = new ResourceLocation("textures/entity/enderman/enderman_eyes.png");
   public final RenderEnderman endermanRenderer;
   public static final String __OBFID = "CL_00002418";


   public LayerEndermanEyes(RenderEnderman p_i46117_1_) {
      this.endermanRenderer = p_i46117_1_;
   }

   public void doRenderLayer(EntityEnderman p_177201_1_, float p_177201_2_, float p_177201_3_, float p_177201_4_, float p_177201_5_, float p_177201_6_, float p_177201_7_, float p_177201_8_) {
      this.endermanRenderer.bindTexture(RES_ENDERMAN_EYES);
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.blendFunc(1, 1);
      GlStateManager.disableLighting();
      GlStateManager.depthMask(!p_177201_1_.isInvisible());
      char var9 = '\uf0f0';
      int var10 = var9 % 65536;
      int var11 = var9 / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var10 / 1.0F, (float)var11 / 1.0F);
      GlStateManager.enableLighting();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      if(Config.isShaders()) {
         Shaders.beginSpiderEyes();
      }

      this.endermanRenderer.getMainModel().render(p_177201_1_, p_177201_2_, p_177201_3_, p_177201_5_, p_177201_6_, p_177201_7_, p_177201_8_);
      if(Config.isShaders()) {
         Shaders.endSpiderEyes();
      }

      this.endermanRenderer.setLightmap(p_177201_1_, p_177201_4_);
      GlStateManager.depthMask(true);
      GlStateManager.disableBlend();
      GlStateManager.enableAlpha();
   }

   public boolean shouldCombineTextures() {
      return false;
   }

   public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
      this.doRenderLayer((EntityEnderman)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
   }

}
