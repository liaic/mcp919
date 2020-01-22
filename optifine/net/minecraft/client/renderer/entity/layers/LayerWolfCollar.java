package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.CustomColors;

public class LayerWolfCollar implements LayerRenderer {

   public static final ResourceLocation WOLF_COLLAR = new ResourceLocation("textures/entity/wolf/wolf_collar.png");
   public final RenderWolf wolfRenderer;
   public static final String __OBFID = "CL_00002405";


   public LayerWolfCollar(RenderWolf p_i46104_1_) {
      this.wolfRenderer = p_i46104_1_;
   }

   public void doRenderLayer(EntityWolf p_177145_1_, float p_177145_2_, float p_177145_3_, float p_177145_4_, float p_177145_5_, float p_177145_6_, float p_177145_7_, float p_177145_8_) {
      if(p_177145_1_.isTamed() && !p_177145_1_.isInvisible()) {
         this.wolfRenderer.bindTexture(WOLF_COLLAR);
         EnumDyeColor var9 = EnumDyeColor.byMetadata(p_177145_1_.getCollarColor().getMetadata());
         float[] var10 = EntitySheep.getDyeRgb(var9);
         if(Config.isCustomColors()) {
            var10 = CustomColors.getWolfCollarColors(var9, var10);
         }

         GlStateManager.color(var10[0], var10[1], var10[2]);
         this.wolfRenderer.getMainModel().render(p_177145_1_, p_177145_2_, p_177145_3_, p_177145_5_, p_177145_6_, p_177145_7_, p_177145_8_);
      }

   }

   public boolean shouldCombineTextures() {
      return true;
   }

   public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
      this.doRenderLayer((EntityWolf)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
   }

}
