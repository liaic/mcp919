package net.minecraft.client.renderer;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import optifine.CustomColors;
import optifine.RenderEnv;

public class BlockFluidRenderer {

   public TextureAtlasSprite[] atlasSpritesLava = new TextureAtlasSprite[2];
   public TextureAtlasSprite[] atlasSpritesWater = new TextureAtlasSprite[2];
   public static final String __OBFID = "CL_00002519";


   public BlockFluidRenderer() {
      this.initAtlasSprites();
   }

   public void initAtlasSprites() {
      TextureMap var1 = Minecraft.getMinecraft().getTextureMapBlocks();
      this.atlasSpritesLava[0] = var1.getAtlasSprite("minecraft:blocks/lava_still");
      this.atlasSpritesLava[1] = var1.getAtlasSprite("minecraft:blocks/lava_flow");
      this.atlasSpritesWater[0] = var1.getAtlasSprite("minecraft:blocks/water_still");
      this.atlasSpritesWater[1] = var1.getAtlasSprite("minecraft:blocks/water_flow");
   }

   public boolean renderFluid(IBlockAccess p_178270_1_, IBlockState p_178270_2_, BlockPos p_178270_3_, WorldRenderer p_178270_4_) {
      BlockLiquid var5 = (BlockLiquid)p_178270_2_.getBlock();
      var5.setBlockBoundsBasedOnState(p_178270_1_, p_178270_3_);
      TextureAtlasSprite[] var6 = var5.getMaterial() == Material.lava?this.atlasSpritesLava:this.atlasSpritesWater;
      RenderEnv renderEnv = RenderEnv.getInstance(p_178270_1_, p_178270_2_, p_178270_3_);
      int var7 = CustomColors.getFluidColor(p_178270_1_, p_178270_2_, p_178270_3_, renderEnv);
      float var8 = (float)(var7 >> 16 & 255) / 255.0F;
      float var9 = (float)(var7 >> 8 & 255) / 255.0F;
      float var10 = (float)(var7 & 255) / 255.0F;
      boolean var11 = var5.shouldSideBeRendered(p_178270_1_, p_178270_3_.up(), EnumFacing.UP);
      boolean var12 = var5.shouldSideBeRendered(p_178270_1_, p_178270_3_.down(), EnumFacing.DOWN);
      boolean[] var13 = renderEnv.getBorderFlags();
      var13[0] = var5.shouldSideBeRendered(p_178270_1_, p_178270_3_.north(), EnumFacing.NORTH);
      var13[1] = var5.shouldSideBeRendered(p_178270_1_, p_178270_3_.south(), EnumFacing.SOUTH);
      var13[2] = var5.shouldSideBeRendered(p_178270_1_, p_178270_3_.west(), EnumFacing.WEST);
      var13[3] = var5.shouldSideBeRendered(p_178270_1_, p_178270_3_.east(), EnumFacing.EAST);
      if(!var11 && !var12 && !var13[0] && !var13[1] && !var13[2] && !var13[3]) {
         return false;
      } else {
         boolean var14 = false;
         float var15 = 0.5F;
         float var16 = 1.0F;
         float var17 = 0.8F;
         float var18 = 0.6F;
         Material var19 = var5.getMaterial();
         float var20 = this.getFluidHeight(p_178270_1_, p_178270_3_, var19);
         float var21 = this.getFluidHeight(p_178270_1_, p_178270_3_.south(), var19);
         float var22 = this.getFluidHeight(p_178270_1_, p_178270_3_.east().south(), var19);
         float var23 = this.getFluidHeight(p_178270_1_, p_178270_3_.east(), var19);
         double var24 = (double)p_178270_3_.getX();
         double var26 = (double)p_178270_3_.getY();
         double var28 = (double)p_178270_3_.getZ();
         float var30 = 0.001F;
         TextureAtlasSprite var31;
         float var32;
         float var33;
         float var37;
         float var34;
         float var35;
         float var36;
         float var46;
         if(var11) {
            var14 = true;
            var31 = var6[0];
            var32 = (float)BlockLiquid.getFlowDirection(p_178270_1_, p_178270_3_, var19);
            if(var32 > -999.0F) {
               var31 = var6[1];
            }

            p_178270_4_.setSprite(var31);
            var20 -= var30;
            var21 -= var30;
            var22 -= var30;
            var23 -= var30;
            float var58;
            float var59;
            float var60;
            if(var32 < -999.0F) {
               var33 = var31.getInterpolatedU(0.0D);
               var37 = var31.getInterpolatedV(0.0D);
               var34 = var33;
               var58 = var31.getInterpolatedV(16.0D);
               var35 = var31.getInterpolatedU(16.0D);
               var59 = var58;
               var36 = var35;
               var60 = var37;
            } else {
               float var61 = MathHelper.sin(var32) * 0.25F;
               float var68 = MathHelper.cos(var32) * 0.25F;
               float var70 = 8.0F;
               var33 = var31.getInterpolatedU((double)(8.0F + (-var68 - var61) * 16.0F));
               var37 = var31.getInterpolatedV((double)(8.0F + (-var68 + var61) * 16.0F));
               var34 = var31.getInterpolatedU((double)(8.0F + (-var68 + var61) * 16.0F));
               var58 = var31.getInterpolatedV((double)(8.0F + (var68 + var61) * 16.0F));
               var35 = var31.getInterpolatedU((double)(8.0F + (var68 + var61) * 16.0F));
               var59 = var31.getInterpolatedV((double)(8.0F + (var68 - var61) * 16.0F));
               var36 = var31.getInterpolatedU((double)(8.0F + (var68 - var61) * 16.0F));
               var60 = var31.getInterpolatedV((double)(8.0F + (-var68 - var61) * 16.0F));
            }

            int var701 = var5.getMixedBrightnessForBlock(p_178270_1_, p_178270_3_);
            int var72 = var701 >> 16 & '\uffff';
            int var73 = var701 & '\uffff';
            float var44 = var16 * var8;
            float var45 = var16 * var9;
            var46 = var16 * var10;
            p_178270_4_.pos(var24 + 0.0D, var26 + (double)var20, var28 + 0.0D).color(var44, var45, var46, 1.0F).tex((double)var33, (double)var37).lightmap(var72, var73).endVertex();
            p_178270_4_.pos(var24 + 0.0D, var26 + (double)var21, var28 + 1.0D).color(var44, var45, var46, 1.0F).tex((double)var34, (double)var58).lightmap(var72, var73).endVertex();
            p_178270_4_.pos(var24 + 1.0D, var26 + (double)var22, var28 + 1.0D).color(var44, var45, var46, 1.0F).tex((double)var35, (double)var59).lightmap(var72, var73).endVertex();
            p_178270_4_.pos(var24 + 1.0D, var26 + (double)var23, var28 + 0.0D).color(var44, var45, var46, 1.0F).tex((double)var36, (double)var60).lightmap(var72, var73).endVertex();
            if(var5.shouldRenderSides(p_178270_1_, p_178270_3_.up())) {
               p_178270_4_.pos(var24 + 0.0D, var26 + (double)var20, var28 + 0.0D).color(var44, var45, var46, 1.0F).tex((double)var33, (double)var37).lightmap(var72, var73).endVertex();
               p_178270_4_.pos(var24 + 1.0D, var26 + (double)var23, var28 + 0.0D).color(var44, var45, var46, 1.0F).tex((double)var36, (double)var60).lightmap(var72, var73).endVertex();
               p_178270_4_.pos(var24 + 1.0D, var26 + (double)var22, var28 + 1.0D).color(var44, var45, var46, 1.0F).tex((double)var35, (double)var59).lightmap(var72, var73).endVertex();
               p_178270_4_.pos(var24 + 0.0D, var26 + (double)var21, var28 + 1.0D).color(var44, var45, var46, 1.0F).tex((double)var34, (double)var58).lightmap(var72, var73).endVertex();
            }
         }

         int var67;
         int var681;
         int var691;
         if(var12) {
            var32 = var6[0].getMinU();
            var33 = var6[0].getMaxU();
            var34 = var6[0].getMinV();
            var35 = var6[0].getMaxV();
            var67 = var5.getMixedBrightnessForBlock(p_178270_1_, p_178270_3_.down());
            var681 = var67 >> 16 & '\uffff';
            var691 = var67 & '\uffff';
            p_178270_4_.pos(var24, var26, var28 + 1.0D).color(var15, var15, var15, 1.0F).tex((double)var32, (double)var35).lightmap(var681, var691).endVertex();
            p_178270_4_.pos(var24, var26, var28).color(var15, var15, var15, 1.0F).tex((double)var32, (double)var34).lightmap(var681, var691).endVertex();
            p_178270_4_.pos(var24 + 1.0D, var26, var28).color(var15, var15, var15, 1.0F).tex((double)var33, (double)var34).lightmap(var681, var691).endVertex();
            p_178270_4_.pos(var24 + 1.0D, var26, var28 + 1.0D).color(var15, var15, var15, 1.0F).tex((double)var33, (double)var35).lightmap(var681, var691).endVertex();
            var14 = true;
         }

         for(var67 = 0; var67 < 4; ++var67) {
            var681 = 0;
            var691 = 0;
            if(var67 == 0) {
               --var691;
            }

            if(var67 == 1) {
               ++var691;
            }

            if(var67 == 2) {
               --var681;
            }

            if(var67 == 3) {
               ++var681;
            }

            BlockPos var711 = p_178270_3_.add(var681, 0, var691);
            var31 = var6[1];
            p_178270_4_.setSprite(var31);
            if(var13[var67]) {
               double var65;
               double var69;
               double var66;
               double var71;
               if(var67 == 0) {
                  var36 = var20;
                  var37 = var23;
                  var65 = var24;
                  var69 = var24 + 1.0D;
                  var66 = var28 + (double)var30;
                  var71 = var28 + (double)var30;
               } else if(var67 == 1) {
                  var36 = var22;
                  var37 = var21;
                  var65 = var24 + 1.0D;
                  var69 = var24;
                  var66 = var28 + 1.0D - (double)var30;
                  var71 = var28 + 1.0D - (double)var30;
               } else if(var67 == 2) {
                  var36 = var21;
                  var37 = var20;
                  var65 = var24 + (double)var30;
                  var69 = var24 + (double)var30;
                  var66 = var28 + 1.0D;
                  var71 = var28;
               } else {
                  var36 = var23;
                  var37 = var22;
                  var65 = var24 + 1.0D - (double)var30;
                  var69 = var24 + 1.0D - (double)var30;
                  var66 = var28;
                  var71 = var28 + 1.0D;
               }

               var14 = true;
               var46 = var31.getInterpolatedU(0.0D);
               float var47 = var31.getInterpolatedU(8.0D);
               float var48 = var31.getInterpolatedV((double)((1.0F - var36) * 16.0F * 0.5F));
               float var49 = var31.getInterpolatedV((double)((1.0F - var37) * 16.0F * 0.5F));
               float var50 = var31.getInterpolatedV(8.0D);
               int var51 = var5.getMixedBrightnessForBlock(p_178270_1_, var711);
               int var52 = var51 >> 16 & '\uffff';
               int var53 = var51 & '\uffff';
               float var54 = var67 < 2?var17:var18;
               float var55 = var16 * var54 * var8;
               float var56 = var16 * var54 * var9;
               float var57 = var16 * var54 * var10;
               p_178270_4_.pos(var65, var26 + (double)var36, var66).color(var55, var56, var57, 1.0F).tex((double)var46, (double)var48).lightmap(var52, var53).endVertex();
               p_178270_4_.pos(var69, var26 + (double)var37, var71).color(var55, var56, var57, 1.0F).tex((double)var47, (double)var49).lightmap(var52, var53).endVertex();
               p_178270_4_.pos(var69, var26 + 0.0D, var71).color(var55, var56, var57, 1.0F).tex((double)var47, (double)var50).lightmap(var52, var53).endVertex();
               p_178270_4_.pos(var65, var26 + 0.0D, var66).color(var55, var56, var57, 1.0F).tex((double)var46, (double)var50).lightmap(var52, var53).endVertex();
               p_178270_4_.pos(var65, var26 + 0.0D, var66).color(var55, var56, var57, 1.0F).tex((double)var46, (double)var50).lightmap(var52, var53).endVertex();
               p_178270_4_.pos(var69, var26 + 0.0D, var71).color(var55, var56, var57, 1.0F).tex((double)var47, (double)var50).lightmap(var52, var53).endVertex();
               p_178270_4_.pos(var69, var26 + (double)var37, var71).color(var55, var56, var57, 1.0F).tex((double)var47, (double)var49).lightmap(var52, var53).endVertex();
               p_178270_4_.pos(var65, var26 + (double)var36, var66).color(var55, var56, var57, 1.0F).tex((double)var46, (double)var48).lightmap(var52, var53).endVertex();
            }
         }

         p_178270_4_.setSprite((TextureAtlasSprite)null);
         return var14;
      }
   }

   public float getFluidHeight(IBlockAccess p_178269_1_, BlockPos p_178269_2_, Material p_178269_3_) {
      int var4 = 0;
      float var5 = 0.0F;

      for(int var6 = 0; var6 < 4; ++var6) {
         BlockPos var7 = p_178269_2_.add(-(var6 & 1), 0, -(var6 >> 1 & 1));
         if(p_178269_1_.getBlockState(var7.up()).getBlock().getMaterial() == p_178269_3_) {
            return 1.0F;
         }

         IBlockState var8 = p_178269_1_.getBlockState(var7);
         Material var9 = var8.getBlock().getMaterial();
         if(var9 == p_178269_3_) {
            int var10 = ((Integer)var8.getValue(BlockLiquid.LEVEL)).intValue();
            if(var10 >= 8 || var10 == 0) {
               var5 += BlockLiquid.getLiquidHeightPercent(var10) * 10.0F;
               var4 += 10;
            }

            var5 += BlockLiquid.getLiquidHeightPercent(var10);
            ++var4;
         } else if(!var9.isSolid()) {
            ++var5;
            ++var4;
         }
      }

      return 1.0F - var5 / (float)var4;
   }
}
