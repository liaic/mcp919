package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelSheep1;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderSheep;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.CustomColors;

public class LayerSheepWool implements LayerRenderer {

   public static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/sheep/sheep_fur.png");
   public final RenderSheep sheepRenderer;
   public final ModelSheep1 sheepModel = new ModelSheep1();
   public static final String __OBFID = "CL_00002413";


   public LayerSheepWool(RenderSheep p_i46112_1_) {
      this.sheepRenderer = p_i46112_1_;
   }

   public void doRenderLayer(EntitySheep p_177162_1_, float p_177162_2_, float p_177162_3_, float p_177162_4_, float p_177162_5_, float p_177162_6_, float p_177162_7_, float p_177162_8_) {
      if(!p_177162_1_.getSheared() && !p_177162_1_.isInvisible()) {
         this.sheepRenderer.bindTexture(TEXTURE);
         if(p_177162_1_.hasCustomName() && "jeb_".equals(p_177162_1_.getCustomNameTag())) {
            boolean var91 = true;
            int var10 = p_177162_1_.ticksExisted / 25 + p_177162_1_.getEntityId();
            int var11 = EnumDyeColor.values().length;
            int var12 = var10 % var11;
            int var13 = (var10 + 1) % var11;
            float var14 = ((float)(p_177162_1_.ticksExisted % 25) + p_177162_4_) / 25.0F;
            float[] var15 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(var12));
            float[] var16 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(var13));
            if(Config.isCustomColors()) {
               var15 = CustomColors.getSheepColors(EnumDyeColor.byMetadata(var12), var15);
               var16 = CustomColors.getSheepColors(EnumDyeColor.byMetadata(var13), var16);
            }

            GlStateManager.color(var15[0] * (1.0F - var14) + var16[0] * var14, var15[1] * (1.0F - var14) + var16[1] * var14, var15[2] * (1.0F - var14) + var16[2] * var14);
         } else {
            float[] var9 = EntitySheep.getDyeRgb(p_177162_1_.getFleeceColor());
            if(Config.isCustomColors()) {
               var9 = CustomColors.getSheepColors(p_177162_1_.getFleeceColor(), var9);
            }

            GlStateManager.color(var9[0], var9[1], var9[2]);
         }

         this.sheepModel.setModelAttributes(this.sheepRenderer.getMainModel());
         this.sheepModel.setLivingAnimations(p_177162_1_, p_177162_2_, p_177162_3_, p_177162_4_);
         this.sheepModel.render(p_177162_1_, p_177162_2_, p_177162_3_, p_177162_5_, p_177162_6_, p_177162_7_, p_177162_8_);
      }

   }

   public boolean shouldCombineTextures() {
      return true;
   }

   public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
      this.doRenderLayer((EntitySheep)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
   }

}
