package net.minecraft.client.renderer.tileentity;

import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBeacon.BeamSegment;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import org.lwjgl.opengl.GL11;
import shadersmod.client.Shaders;

public class TileEntityBeaconRenderer extends TileEntitySpecialRenderer {

   public static final ResourceLocation beaconBeam = new ResourceLocation("textures/entity/beacon_beam.png");
   public static final String __OBFID = "CL_00000962";


   public void renderTileEntityAt(TileEntityBeacon p_180536_1_, double p_180536_2_, double p_180536_4_, double p_180536_6_, float p_180536_8_, int p_180536_9_) {
      float var10 = p_180536_1_.shouldBeamRender();
      if(var10 > 0.0F) {
         if(Config.isShaders()) {
            Shaders.beginBeacon();
         }

         GlStateManager.alphaFunc(516, 0.1F);
         if(var10 > 0.0F) {
            Tessellator var11 = Tessellator.getInstance();
            WorldRenderer var12 = var11.getWorldRenderer();
            GlStateManager.disableFog();
            List var13 = p_180536_1_.getBeamSegments();
            int var14 = 0;

            for(int var15 = 0; var15 < var13.size(); ++var15) {
               BeamSegment var16 = (BeamSegment)var13.get(var15);
               int var17 = var14 + var16.getHeight();
               this.bindTexture(beaconBeam);
               GL11.glTexParameterf(3553, 10242, 10497.0F);
               GL11.glTexParameterf(3553, 10243, 10497.0F);
               GlStateManager.disableLighting();
               GlStateManager.disableCull();
               GlStateManager.disableBlend();
               GlStateManager.depthMask(true);
               GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
               double var18 = (double)p_180536_1_.getWorld().getTotalWorldTime() + (double)p_180536_8_;
               double var20 = MathHelper.func_181162_h(-var18 * 0.2D - (double)MathHelper.floor_double(-var18 * 0.1D));
               float var22 = var16.getColors()[0];
               float var23 = var16.getColors()[1];
               float var24 = var16.getColors()[2];
               double var25 = var18 * 0.025D * -1.5D;
               double var27 = 0.2D;
               double var29 = 0.5D + Math.cos(var25 + 2.356194490192345D) * 0.2D;
               double var31 = 0.5D + Math.sin(var25 + 2.356194490192345D) * 0.2D;
               double var33 = 0.5D + Math.cos(var25 + 0.7853981633974483D) * 0.2D;
               double var35 = 0.5D + Math.sin(var25 + 0.7853981633974483D) * 0.2D;
               double var37 = 0.5D + Math.cos(var25 + 3.9269908169872414D) * 0.2D;
               double var39 = 0.5D + Math.sin(var25 + 3.9269908169872414D) * 0.2D;
               double var41 = 0.5D + Math.cos(var25 + 5.497787143782138D) * 0.2D;
               double var43 = 0.5D + Math.sin(var25 + 5.497787143782138D) * 0.2D;
               double var45 = 0.0D;
               double var47 = 1.0D;
               double var49 = -1.0D + var20;
               double var51 = (double)((float)var16.getHeight() * var10) * 2.5D + var49;
               var12.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
               var12.pos(p_180536_2_ + var29, p_180536_4_ + (double)var17, p_180536_6_ + var31).tex(1.0D, var51).color(var22, var23, var24, 1.0F).endVertex();
               var12.pos(p_180536_2_ + var29, p_180536_4_ + (double)var14, p_180536_6_ + var31).tex(1.0D, var49).color(var22, var23, var24, 1.0F).endVertex();
               var12.pos(p_180536_2_ + var33, p_180536_4_ + (double)var14, p_180536_6_ + var35).tex(0.0D, var49).color(var22, var23, var24, 1.0F).endVertex();
               var12.pos(p_180536_2_ + var33, p_180536_4_ + (double)var17, p_180536_6_ + var35).tex(0.0D, var51).color(var22, var23, var24, 1.0F).endVertex();
               var12.pos(p_180536_2_ + var41, p_180536_4_ + (double)var17, p_180536_6_ + var43).tex(1.0D, var51).color(var22, var23, var24, 1.0F).endVertex();
               var12.pos(p_180536_2_ + var41, p_180536_4_ + (double)var14, p_180536_6_ + var43).tex(1.0D, var49).color(var22, var23, var24, 1.0F).endVertex();
               var12.pos(p_180536_2_ + var37, p_180536_4_ + (double)var14, p_180536_6_ + var39).tex(0.0D, var49).color(var22, var23, var24, 1.0F).endVertex();
               var12.pos(p_180536_2_ + var37, p_180536_4_ + (double)var17, p_180536_6_ + var39).tex(0.0D, var51).color(var22, var23, var24, 1.0F).endVertex();
               var12.pos(p_180536_2_ + var33, p_180536_4_ + (double)var17, p_180536_6_ + var35).tex(1.0D, var51).color(var22, var23, var24, 1.0F).endVertex();
               var12.pos(p_180536_2_ + var33, p_180536_4_ + (double)var14, p_180536_6_ + var35).tex(1.0D, var49).color(var22, var23, var24, 1.0F).endVertex();
               var12.pos(p_180536_2_ + var41, p_180536_4_ + (double)var14, p_180536_6_ + var43).tex(0.0D, var49).color(var22, var23, var24, 1.0F).endVertex();
               var12.pos(p_180536_2_ + var41, p_180536_4_ + (double)var17, p_180536_6_ + var43).tex(0.0D, var51).color(var22, var23, var24, 1.0F).endVertex();
               var12.pos(p_180536_2_ + var37, p_180536_4_ + (double)var17, p_180536_6_ + var39).tex(1.0D, var51).color(var22, var23, var24, 1.0F).endVertex();
               var12.pos(p_180536_2_ + var37, p_180536_4_ + (double)var14, p_180536_6_ + var39).tex(1.0D, var49).color(var22, var23, var24, 1.0F).endVertex();
               var12.pos(p_180536_2_ + var29, p_180536_4_ + (double)var14, p_180536_6_ + var31).tex(0.0D, var49).color(var22, var23, var24, 1.0F).endVertex();
               var12.pos(p_180536_2_ + var29, p_180536_4_ + (double)var17, p_180536_6_ + var31).tex(0.0D, var51).color(var22, var23, var24, 1.0F).endVertex();
               var11.draw();
               GlStateManager.enableBlend();
               GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
               GlStateManager.depthMask(false);
               var25 = 0.2D;
               var27 = 0.2D;
               var29 = 0.8D;
               var31 = 0.2D;
               var33 = 0.2D;
               var35 = 0.8D;
               var37 = 0.8D;
               var39 = 0.8D;
               var41 = 0.0D;
               var43 = 1.0D;
               var45 = -1.0D + var20;
               var47 = (double)((float)var16.getHeight() * var10) + var45;
               var12.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
               var12.pos(p_180536_2_ + 0.2D, p_180536_4_ + (double)var17, p_180536_6_ + 0.2D).tex(1.0D, var47).color(var22, var23, var24, 0.125F).endVertex();
               var12.pos(p_180536_2_ + 0.2D, p_180536_4_ + (double)var14, p_180536_6_ + 0.2D).tex(1.0D, var45).color(var22, var23, var24, 0.125F).endVertex();
               var12.pos(p_180536_2_ + 0.8D, p_180536_4_ + (double)var14, p_180536_6_ + 0.2D).tex(0.0D, var45).color(var22, var23, var24, 0.125F).endVertex();
               var12.pos(p_180536_2_ + 0.8D, p_180536_4_ + (double)var17, p_180536_6_ + 0.2D).tex(0.0D, var47).color(var22, var23, var24, 0.125F).endVertex();
               var12.pos(p_180536_2_ + 0.8D, p_180536_4_ + (double)var17, p_180536_6_ + 0.8D).tex(1.0D, var47).color(var22, var23, var24, 0.125F).endVertex();
               var12.pos(p_180536_2_ + 0.8D, p_180536_4_ + (double)var14, p_180536_6_ + 0.8D).tex(1.0D, var45).color(var22, var23, var24, 0.125F).endVertex();
               var12.pos(p_180536_2_ + 0.2D, p_180536_4_ + (double)var14, p_180536_6_ + 0.8D).tex(0.0D, var45).color(var22, var23, var24, 0.125F).endVertex();
               var12.pos(p_180536_2_ + 0.2D, p_180536_4_ + (double)var17, p_180536_6_ + 0.8D).tex(0.0D, var47).color(var22, var23, var24, 0.125F).endVertex();
               var12.pos(p_180536_2_ + 0.8D, p_180536_4_ + (double)var17, p_180536_6_ + 0.2D).tex(1.0D, var47).color(var22, var23, var24, 0.125F).endVertex();
               var12.pos(p_180536_2_ + 0.8D, p_180536_4_ + (double)var14, p_180536_6_ + 0.2D).tex(1.0D, var45).color(var22, var23, var24, 0.125F).endVertex();
               var12.pos(p_180536_2_ + 0.8D, p_180536_4_ + (double)var14, p_180536_6_ + 0.8D).tex(0.0D, var45).color(var22, var23, var24, 0.125F).endVertex();
               var12.pos(p_180536_2_ + 0.8D, p_180536_4_ + (double)var17, p_180536_6_ + 0.8D).tex(0.0D, var47).color(var22, var23, var24, 0.125F).endVertex();
               var12.pos(p_180536_2_ + 0.2D, p_180536_4_ + (double)var17, p_180536_6_ + 0.8D).tex(1.0D, var47).color(var22, var23, var24, 0.125F).endVertex();
               var12.pos(p_180536_2_ + 0.2D, p_180536_4_ + (double)var14, p_180536_6_ + 0.8D).tex(1.0D, var45).color(var22, var23, var24, 0.125F).endVertex();
               var12.pos(p_180536_2_ + 0.2D, p_180536_4_ + (double)var14, p_180536_6_ + 0.2D).tex(0.0D, var45).color(var22, var23, var24, 0.125F).endVertex();
               var12.pos(p_180536_2_ + 0.2D, p_180536_4_ + (double)var17, p_180536_6_ + 0.2D).tex(0.0D, var47).color(var22, var23, var24, 0.125F).endVertex();
               var11.draw();
               GlStateManager.enableLighting();
               GlStateManager.enableTexture2D();
               GlStateManager.depthMask(true);
               var14 = var17;
            }

            GlStateManager.enableFog();
         }

         if(Config.isShaders()) {
            Shaders.endBeacon();
         }

      }
   }

   public boolean forceTileEntityRender() {
      return true;
   }

   public void renderTileEntityAt(TileEntity p_180535_1_, double p_180535_2_, double p_180535_4_, double p_180535_6_, float p_180535_8_, int p_180535_9_) {
      this.renderTileEntityAt((TileEntityBeacon)p_180535_1_, p_180535_2_, p_180535_4_, p_180535_6_, p_180535_8_, p_180535_9_);
   }

}
