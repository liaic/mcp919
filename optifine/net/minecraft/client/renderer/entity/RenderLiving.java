package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import optifine.Config;
import shadersmod.client.Shaders;

public abstract class RenderLiving extends RendererLivingEntity {

   public static final String __OBFID = "CL_00001015";


   public RenderLiving(RenderManager p_i46153_1_, ModelBase p_i46153_2_, float p_i46153_3_) {
      super(p_i46153_1_, p_i46153_2_, p_i46153_3_);
   }

   public boolean canRenderName(EntityLiving targetEntity) {
      return super.canRenderName((EntityLivingBase)targetEntity) && (targetEntity.getAlwaysRenderNameTagForRender() || targetEntity.hasCustomName() && targetEntity == this.renderManager.pointedEntity);
   }

   public boolean shouldRender(EntityLiving p_177104_1_, ICamera p_177104_2_, double p_177104_3_, double p_177104_5_, double p_177104_7_) {
      if(super.shouldRender(p_177104_1_, p_177104_2_, p_177104_3_, p_177104_5_, p_177104_7_)) {
         return true;
      } else if(p_177104_1_.getLeashed() && p_177104_1_.getLeashedToEntity() != null) {
         Entity var9 = p_177104_1_.getLeashedToEntity();
         return p_177104_2_.isBoundingBoxInFrustum(var9.getEntityBoundingBox());
      } else {
         return false;
      }
   }

   public void doRender(EntityLiving p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      super.doRender((EntityLivingBase)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
      this.renderLeash(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
   }

   public void setLightmap(EntityLiving p_177105_1_, float p_177105_2_) {
      int var3 = p_177105_1_.getBrightnessForRender(p_177105_2_);
      int var4 = var3 % 65536;
      int var5 = var3 / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var4 / 1.0F, (float)var5 / 1.0F);
   }

   public double interpolateValue(double start, double end, double pct) {
      return start + (end - start) * pct;
   }

   public void renderLeash(EntityLiving p_110827_1_, double p_110827_2_, double p_110827_4_, double p_110827_6_, float p_110827_8_, float p_110827_9_) {
      if(!Config.isShaders() || !Shaders.isShadowPass) {
         Entity var10 = p_110827_1_.getLeashedToEntity();
         if(var10 != null) {
            p_110827_4_ -= (1.6D - (double)p_110827_1_.height) * 0.5D;
            Tessellator var11 = Tessellator.getInstance();
            WorldRenderer var12 = var11.getWorldRenderer();
            double var13 = this.interpolateValue((double)var10.prevRotationYaw, (double)var10.rotationYaw, (double)(p_110827_9_ * 0.5F)) * 0.01745329238474369D;
            double var15 = this.interpolateValue((double)var10.prevRotationPitch, (double)var10.rotationPitch, (double)(p_110827_9_ * 0.5F)) * 0.01745329238474369D;
            double var17 = Math.cos(var13);
            double var19 = Math.sin(var13);
            double var21 = Math.sin(var15);
            if(var10 instanceof EntityHanging) {
               var17 = 0.0D;
               var19 = 0.0D;
               var21 = -1.0D;
            }

            double var23 = Math.cos(var15);
            double var25 = this.interpolateValue(var10.prevPosX, var10.posX, (double)p_110827_9_) - var17 * 0.7D - var19 * 0.5D * var23;
            double var27 = this.interpolateValue(var10.prevPosY + (double)var10.getEyeHeight() * 0.7D, var10.posY + (double)var10.getEyeHeight() * 0.7D, (double)p_110827_9_) - var21 * 0.5D - 0.25D;
            double var29 = this.interpolateValue(var10.prevPosZ, var10.posZ, (double)p_110827_9_) - var19 * 0.7D + var17 * 0.5D * var23;
            double var31 = this.interpolateValue((double)p_110827_1_.prevRenderYawOffset, (double)p_110827_1_.renderYawOffset, (double)p_110827_9_) * 0.01745329238474369D + 1.5707963267948966D;
            var17 = Math.cos(var31) * (double)p_110827_1_.width * 0.4D;
            var19 = Math.sin(var31) * (double)p_110827_1_.width * 0.4D;
            double var33 = this.interpolateValue(p_110827_1_.prevPosX, p_110827_1_.posX, (double)p_110827_9_) + var17;
            double var35 = this.interpolateValue(p_110827_1_.prevPosY, p_110827_1_.posY, (double)p_110827_9_);
            double var37 = this.interpolateValue(p_110827_1_.prevPosZ, p_110827_1_.posZ, (double)p_110827_9_) + var19;
            p_110827_2_ += var17;
            p_110827_6_ += var19;
            double var39 = (double)((float)(var25 - var33));
            double var41 = (double)((float)(var27 - var35));
            double var43 = (double)((float)(var29 - var37));
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            if(Config.isShaders()) {
               Shaders.beginLeash();
            }

            boolean var45 = true;
            double var46 = 0.025D;
            var12.begin(5, DefaultVertexFormats.POSITION_COLOR);

            int var48;
            float var49;
            float var50;
            float var51;
            float var52;
            for(var48 = 0; var48 <= 24; ++var48) {
               var49 = 0.5F;
               var50 = 0.4F;
               var51 = 0.3F;
               if(var48 % 2 == 0) {
                  var49 *= 0.7F;
                  var50 *= 0.7F;
                  var51 *= 0.7F;
               }

               var52 = (float)var48 / 24.0F;
               var12.pos(p_110827_2_ + var39 * (double)var52 + 0.0D, p_110827_4_ + var41 * (double)(var52 * var52 + var52) * 0.5D + (double)((24.0F - (float)var48) / 18.0F + 0.125F), p_110827_6_ + var43 * (double)var52).color(var49, var50, var51, 1.0F).endVertex();
               var12.pos(p_110827_2_ + var39 * (double)var52 + 0.025D, p_110827_4_ + var41 * (double)(var52 * var52 + var52) * 0.5D + (double)((24.0F - (float)var48) / 18.0F + 0.125F) + 0.025D, p_110827_6_ + var43 * (double)var52).color(var49, var50, var51, 1.0F).endVertex();
            }

            var11.draw();
            var12.begin(5, DefaultVertexFormats.POSITION_COLOR);

            for(var48 = 0; var48 <= 24; ++var48) {
               var49 = 0.5F;
               var50 = 0.4F;
               var51 = 0.3F;
               if(var48 % 2 == 0) {
                  var49 *= 0.7F;
                  var50 *= 0.7F;
                  var51 *= 0.7F;
               }

               var52 = (float)var48 / 24.0F;
               var12.pos(p_110827_2_ + var39 * (double)var52 + 0.0D, p_110827_4_ + var41 * (double)(var52 * var52 + var52) * 0.5D + (double)((24.0F - (float)var48) / 18.0F + 0.125F) + 0.025D, p_110827_6_ + var43 * (double)var52).color(var49, var50, var51, 1.0F).endVertex();
               var12.pos(p_110827_2_ + var39 * (double)var52 + 0.025D, p_110827_4_ + var41 * (double)(var52 * var52 + var52) * 0.5D + (double)((24.0F - (float)var48) / 18.0F + 0.125F), p_110827_6_ + var43 * (double)var52 + 0.025D).color(var49, var50, var51, 1.0F).endVertex();
            }

            var11.draw();
            if(Config.isShaders()) {
               Shaders.endLeash();
            }

            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.enableCull();
         }

      }
   }

   public boolean canRenderName(EntityLivingBase targetEntity) {
      return this.canRenderName((EntityLiving)targetEntity);
   }

   public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      this.doRender((EntityLiving)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
   }

   public boolean canRenderName(Entity p_177070_1_) {
      return this.canRenderName((EntityLiving)p_177070_1_);
   }

   public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      this.doRender((EntityLiving)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
   }

   public boolean shouldRender(Entity p_177071_1_, ICamera p_177071_2_, double p_177071_3_, double p_177071_5_, double p_177071_7_) {
      return this.shouldRender((EntityLiving)p_177071_1_, p_177071_2_, p_177071_3_, p_177071_5_, p_177071_7_);
   }
}
