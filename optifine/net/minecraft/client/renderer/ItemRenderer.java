package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import optifine.Config;
import optifine.DynamicLights;
import optifine.Reflector;
import org.lwjgl.opengl.GL11;
import shadersmod.client.Shaders;

public class ItemRenderer {

   public static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
   public static final ResourceLocation RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");
   public final Minecraft mc;
   public ItemStack itemToRender;
   public float equippedProgress;
   public float prevEquippedProgress;
   public final RenderManager renderManager;
   public final RenderItem itemRenderer;
   public int equippedItemSlot = -1;
   public static final String __OBFID = "CL_00000953";


   public ItemRenderer(Minecraft mcIn) {
      this.mc = mcIn;
      this.renderManager = mcIn.getRenderManager();
      this.itemRenderer = mcIn.getRenderItem();
   }

   public void renderItem(EntityLivingBase p_178099_1_, ItemStack p_178099_2_, TransformType p_178099_3_) {
      if(p_178099_2_ != null) {
         Item var4 = p_178099_2_.getItem();
         Block var5 = Block.getBlockFromItem(var4);
         GlStateManager.pushMatrix();
         if(this.itemRenderer.shouldRenderItemIn3D(p_178099_2_)) {
            GlStateManager.scale(2.0F, 2.0F, 2.0F);
            if(this.isBlockTranslucent(var5) && (!Config.isShaders() || !Shaders.renderItemKeepDepthMask)) {
               GlStateManager.depthMask(false);
            }
         }

         this.itemRenderer.renderItemModelForEntity(p_178099_2_, p_178099_1_, p_178099_3_);
         if(this.isBlockTranslucent(var5)) {
            GlStateManager.depthMask(true);
         }

         GlStateManager.popMatrix();
      }

   }

   public boolean isBlockTranslucent(Block p_178107_1_) {
      return p_178107_1_ != null && p_178107_1_.getBlockLayer() == EnumWorldBlockLayer.TRANSLUCENT;
   }

   public void rotateArroundXAndY(float p_178101_1_, float p_178101_2_) {
      GlStateManager.pushMatrix();
      GlStateManager.rotate(p_178101_1_, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(p_178101_2_, 0.0F, 1.0F, 0.0F);
      RenderHelper.enableStandardItemLighting();
      GlStateManager.popMatrix();
   }

   public void setLightMapFromPlayer(AbstractClientPlayer p_178109_1_) {
      int var2 = this.mc.theWorld.getCombinedLight(new BlockPos(p_178109_1_.posX, p_178109_1_.posY + (double)p_178109_1_.getEyeHeight(), p_178109_1_.posZ), 0);
      if(Config.isDynamicLights()) {
         var2 = DynamicLights.getCombinedLight(this.mc.getRenderViewEntity(), var2);
      }

      float var3 = (float)(var2 & '\uffff');
      float var4 = (float)(var2 >> 16);
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var3, var4);
   }

   public void rotateWithPlayerRotations(EntityPlayerSP p_178110_1_, float p_178110_2_) {
      float var3 = p_178110_1_.prevRenderArmPitch + (p_178110_1_.renderArmPitch - p_178110_1_.prevRenderArmPitch) * p_178110_2_;
      float var4 = p_178110_1_.prevRenderArmYaw + (p_178110_1_.renderArmYaw - p_178110_1_.prevRenderArmYaw) * p_178110_2_;
      GlStateManager.rotate((p_178110_1_.rotationPitch - var3) * 0.1F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate((p_178110_1_.rotationYaw - var4) * 0.1F, 0.0F, 1.0F, 0.0F);
   }

   public float getMapAngleFromPitch(float p_178100_1_) {
      float var2 = 1.0F - p_178100_1_ / 45.0F + 0.1F;
      var2 = MathHelper.clamp_float(var2, 0.0F, 1.0F);
      var2 = -MathHelper.cos(var2 * 3.1415927F) * 0.5F + 0.5F;
      return var2;
   }

   public void renderRightArm(RenderPlayer p_180534_1_) {
      GlStateManager.pushMatrix();
      GlStateManager.rotate(54.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(64.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(-62.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.translate(0.25F, -0.85F, 0.75F);
      p_180534_1_.renderRightArm(this.mc.thePlayer);
      GlStateManager.popMatrix();
   }

   public void renderLeftArm(RenderPlayer p_178106_1_) {
      GlStateManager.pushMatrix();
      GlStateManager.rotate(92.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(41.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.translate(-0.3F, -1.1F, 0.45F);
      p_178106_1_.renderLeftArm(this.mc.thePlayer);
      GlStateManager.popMatrix();
   }

   public void renderPlayerArms(AbstractClientPlayer p_178102_1_) {
      this.mc.getTextureManager().bindTexture(p_178102_1_.getLocationSkin());
      Render var2 = this.renderManager.getEntityRenderObject(this.mc.thePlayer);
      RenderPlayer var3 = (RenderPlayer)var2;
      if(!p_178102_1_.isInvisible()) {
         GlStateManager.disableCull();
         this.renderRightArm(var3);
         this.renderLeftArm(var3);
         GlStateManager.enableCull();
      }

   }

   public void renderItemMap(AbstractClientPlayer p_178097_1_, float p_178097_2_, float p_178097_3_, float p_178097_4_) {
      float var5 = -0.4F * MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * 3.1415927F);
      float var6 = 0.2F * MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * 3.1415927F * 2.0F);
      float var7 = -0.2F * MathHelper.sin(p_178097_4_ * 3.1415927F);
      GlStateManager.translate(var5, var6, var7);
      float var8 = this.getMapAngleFromPitch(p_178097_2_);
      GlStateManager.translate(0.0F, 0.04F, -0.72F);
      GlStateManager.translate(0.0F, p_178097_3_ * -1.2F, 0.0F);
      GlStateManager.translate(0.0F, var8 * -0.5F, 0.0F);
      GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(var8 * -85.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
      this.renderPlayerArms(p_178097_1_);
      float var9 = MathHelper.sin(p_178097_4_ * p_178097_4_ * 3.1415927F);
      float var10 = MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * 3.1415927F);
      GlStateManager.rotate(var9 * -20.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(var10 * -20.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate(var10 * -80.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.scale(0.38F, 0.38F, 0.38F);
      GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.translate(-1.0F, -1.0F, 0.0F);
      GlStateManager.scale(0.015625F, 0.015625F, 0.015625F);
      this.mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
      Tessellator var11 = Tessellator.getInstance();
      WorldRenderer var12 = var11.getWorldRenderer();
      GL11.glNormal3f(0.0F, 0.0F, -1.0F);
      var12.begin(7, DefaultVertexFormats.POSITION_TEX);
      var12.pos(-7.0D, 135.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
      var12.pos(135.0D, 135.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
      var12.pos(135.0D, -7.0D, 0.0D).tex(1.0D, 0.0D).endVertex();
      var12.pos(-7.0D, -7.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
      var11.draw();
      MapData var13 = Items.filled_map.getMapData(this.itemToRender, this.mc.theWorld);
      if(var13 != null) {
         this.mc.entityRenderer.getMapItemRenderer().renderMap(var13, false);
      }

   }

   public void renderPlayerArm(AbstractClientPlayer p_178095_1_, float p_178095_2_, float p_178095_3_) {
      float var4 = -0.3F * MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * 3.1415927F);
      float var5 = 0.4F * MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * 3.1415927F * 2.0F);
      float var6 = -0.4F * MathHelper.sin(p_178095_3_ * 3.1415927F);
      GlStateManager.translate(var4, var5, var6);
      GlStateManager.translate(0.64000005F, -0.6F, -0.71999997F);
      GlStateManager.translate(0.0F, p_178095_2_ * -0.6F, 0.0F);
      GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
      float var7 = MathHelper.sin(p_178095_3_ * p_178095_3_ * 3.1415927F);
      float var8 = MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * 3.1415927F);
      GlStateManager.rotate(var8 * 70.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(var7 * -20.0F, 0.0F, 0.0F, 1.0F);
      this.mc.getTextureManager().bindTexture(p_178095_1_.getLocationSkin());
      GlStateManager.translate(-1.0F, 3.6F, 3.5F);
      GlStateManager.rotate(120.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.scale(1.0F, 1.0F, 1.0F);
      GlStateManager.translate(5.6F, 0.0F, 0.0F);
      Render var9 = this.renderManager.getEntityRenderObject(this.mc.thePlayer);
      GlStateManager.disableCull();
      RenderPlayer var10 = (RenderPlayer)var9;
      var10.renderRightArm(this.mc.thePlayer);
      GlStateManager.enableCull();
   }

   public void doItemUsedTransformations(float p_178105_1_) {
      float var2 = -0.4F * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * 3.1415927F);
      float var3 = 0.2F * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * 3.1415927F * 2.0F);
      float var4 = -0.2F * MathHelper.sin(p_178105_1_ * 3.1415927F);
      GlStateManager.translate(var2, var3, var4);
   }

   public void performDrinking(AbstractClientPlayer p_178104_1_, float p_178104_2_) {
      float var3 = (float)p_178104_1_.getItemInUseCount() - p_178104_2_ + 1.0F;
      float var4 = var3 / (float)this.itemToRender.getMaxItemUseDuration();
      float var5 = MathHelper.abs(MathHelper.cos(var3 / 4.0F * 3.1415927F) * 0.1F);
      if(var4 >= 0.8F) {
         var5 = 0.0F;
      }

      GlStateManager.translate(0.0F, var5, 0.0F);
      float var6 = 1.0F - (float)Math.pow((double)var4, 27.0D);
      GlStateManager.translate(var6 * 0.6F, var6 * -0.5F, var6 * 0.0F);
      GlStateManager.rotate(var6 * 90.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(var6 * 10.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(var6 * 30.0F, 0.0F, 0.0F, 1.0F);
   }

   public void transformFirstPersonItem(float p_178096_1_, float p_178096_2_) {
      GlStateManager.translate(0.56F, -0.52F, -0.71999997F);
      GlStateManager.translate(0.0F, p_178096_1_ * -0.6F, 0.0F);
      GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
      float var3 = MathHelper.sin(p_178096_2_ * p_178096_2_ * 3.1415927F);
      float var4 = MathHelper.sin(MathHelper.sqrt_float(p_178096_2_) * 3.1415927F);
      GlStateManager.rotate(var3 * -20.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(var4 * -20.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate(var4 * -80.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.scale(0.4F, 0.4F, 0.4F);
   }

   public void doBowTransformations(float p_178098_1_, AbstractClientPlayer p_178098_2_) {
      GlStateManager.rotate(-18.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.rotate(-12.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-8.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.translate(-0.9F, 0.2F, 0.0F);
      float var3 = (float)this.itemToRender.getMaxItemUseDuration() - ((float)p_178098_2_.getItemInUseCount() - p_178098_1_ + 1.0F);
      float var4 = var3 / 20.0F;
      var4 = (var4 * var4 + var4 * 2.0F) / 3.0F;
      if(var4 > 1.0F) {
         var4 = 1.0F;
      }

      if(var4 > 0.1F) {
         float var5 = MathHelper.sin((var3 - 0.1F) * 1.3F);
         float var6 = var4 - 0.1F;
         float var7 = var5 * var6;
         GlStateManager.translate(var7 * 0.0F, var7 * 0.01F, var7 * 0.0F);
      }

      GlStateManager.translate(var4 * 0.0F, var4 * 0.0F, var4 * 0.1F);
      GlStateManager.scale(1.0F, 1.0F, 1.0F + var4 * 0.2F);
   }

   public void doBlockTransformations() {
      GlStateManager.translate(-0.5F, 0.2F, 0.0F);
      GlStateManager.rotate(30.0F, 0.0F, 1.0F, 0.0F);
      GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
   }

   public void renderItemInFirstPerson(float p_78440_1_) {
      float var2 = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * p_78440_1_);
      EntityPlayerSP var3 = this.mc.thePlayer;
      float var4 = var3.getSwingProgress(p_78440_1_);
      float var5 = var3.prevRotationPitch + (var3.rotationPitch - var3.prevRotationPitch) * p_78440_1_;
      float var6 = var3.prevRotationYaw + (var3.rotationYaw - var3.prevRotationYaw) * p_78440_1_;
      this.rotateArroundXAndY(var5, var6);
      this.setLightMapFromPlayer(var3);
      this.rotateWithPlayerRotations(var3, p_78440_1_);
      GlStateManager.enableRescaleNormal();
      GlStateManager.pushMatrix();
      if(this.itemToRender != null) {
         if(this.itemToRender.getItem() instanceof ItemMap) {
            this.renderItemMap(var3, var5, var2, var4);
         } else if(var3.getItemInUseCount() > 0) {
            EnumAction var7 = this.itemToRender.getItemUseAction();
            switch(ItemRenderer.SwitchEnumAction.field_178094_a[var7.ordinal()]) {
            case 1:
               this.transformFirstPersonItem(var2, 0.0F);
               break;
            case 2:
            case 3:
               this.performDrinking(var3, p_78440_1_);
               this.transformFirstPersonItem(var2, 0.0F);
               break;
            case 4:
               this.transformFirstPersonItem(var2, 0.0F);
               this.doBlockTransformations();
               break;
            case 5:
               this.transformFirstPersonItem(var2, 0.0F);
               this.doBowTransformations(p_78440_1_, var3);
            }
         } else {
            this.doItemUsedTransformations(var4);
            this.transformFirstPersonItem(var2, var4);
         }

         this.renderItem(var3, this.itemToRender, TransformType.FIRST_PERSON);
      } else if(!var3.isInvisible()) {
         this.renderPlayerArm(var3, var2, var4);
      }

      GlStateManager.popMatrix();
      GlStateManager.disableRescaleNormal();
      RenderHelper.disableStandardItemLighting();
   }

   public void renderOverlays(float p_78447_1_) {
      GlStateManager.disableAlpha();
      if(this.mc.thePlayer.isEntityInsideOpaqueBlock()) {
         IBlockState var2 = this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer));
         BlockPos overlayPos = new BlockPos(this.mc.thePlayer);
         EntityPlayerSP var3 = this.mc.thePlayer;

         for(int overlayType = 0; overlayType < 8; ++overlayType) {
            double var5 = var3.posX + (double)(((float)((overlayType >> 0) % 2) - 0.5F) * var3.width * 0.8F);
            double var7 = var3.posY + (double)(((float)((overlayType >> 1) % 2) - 0.5F) * 0.1F);
            double var9 = var3.posZ + (double)(((float)((overlayType >> 2) % 2) - 0.5F) * var3.width * 0.8F);
            BlockPos var11 = new BlockPos(var5, var7 + (double)var3.getEyeHeight(), var9);
            IBlockState var12 = this.mc.theWorld.getBlockState(var11);
            if(var12.getBlock().isVisuallyOpaque()) {
               var2 = var12;
               overlayPos = var11;
            }
         }

         if(var2.getBlock().getRenderType() != -1) {
            Object var14 = Reflector.getFieldValue(Reflector.RenderBlockOverlayEvent_OverlayType_BLOCK);
            if(!Reflector.callBoolean(Reflector.ForgeEventFactory_renderBlockOverlay, new Object[]{this.mc.thePlayer, Float.valueOf(p_78447_1_), var14, var2, overlayPos})) {
               this.renderBlockInHand(p_78447_1_, this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(var2));
            }
         }
      }

      if(!this.mc.thePlayer.isSpectator()) {
         if(this.mc.thePlayer.isInsideOfMaterial(Material.water) && !Reflector.callBoolean(Reflector.ForgeEventFactory_renderWaterOverlay, new Object[]{this.mc.thePlayer, Float.valueOf(p_78447_1_)})) {
            this.renderWaterOverlayTexture(p_78447_1_);
         }

         if(this.mc.thePlayer.isBurning() && !Reflector.callBoolean(Reflector.ForgeEventFactory_renderFireOverlay, new Object[]{this.mc.thePlayer, Float.valueOf(p_78447_1_)})) {
            this.renderFireInFirstPerson(p_78447_1_);
         }
      }

      GlStateManager.enableAlpha();
   }

   public void renderBlockInHand(float p_178108_1_, TextureAtlasSprite p_178108_2_) {
      this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
      Tessellator var3 = Tessellator.getInstance();
      WorldRenderer var4 = var3.getWorldRenderer();
      float var5 = 0.1F;
      GlStateManager.color(0.1F, 0.1F, 0.1F, 0.5F);
      GlStateManager.pushMatrix();
      float var6 = -1.0F;
      float var7 = 1.0F;
      float var8 = -1.0F;
      float var9 = 1.0F;
      float var10 = -0.5F;
      float var11 = p_178108_2_.getMinU();
      float var12 = p_178108_2_.getMaxU();
      float var13 = p_178108_2_.getMinV();
      float var14 = p_178108_2_.getMaxV();
      var4.begin(7, DefaultVertexFormats.POSITION_TEX);
      var4.pos(-1.0D, -1.0D, -0.5D).tex((double)var12, (double)var14).endVertex();
      var4.pos(1.0D, -1.0D, -0.5D).tex((double)var11, (double)var14).endVertex();
      var4.pos(1.0D, 1.0D, -0.5D).tex((double)var11, (double)var13).endVertex();
      var4.pos(-1.0D, 1.0D, -0.5D).tex((double)var12, (double)var13).endVertex();
      var3.draw();
      GlStateManager.popMatrix();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public void renderWaterOverlayTexture(float p_78448_1_) {
      if(!Config.isShaders() || Shaders.isUnderwaterOverlay()) {
         this.mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
         Tessellator var2 = Tessellator.getInstance();
         WorldRenderer var3 = var2.getWorldRenderer();
         float var4 = this.mc.thePlayer.getBrightness(p_78448_1_);
         GlStateManager.color(var4, var4, var4, 0.5F);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.pushMatrix();
         float var5 = 4.0F;
         float var6 = -1.0F;
         float var7 = 1.0F;
         float var8 = -1.0F;
         float var9 = 1.0F;
         float var10 = -0.5F;
         float var11 = -this.mc.thePlayer.rotationYaw / 64.0F;
         float var12 = this.mc.thePlayer.rotationPitch / 64.0F;
         var3.begin(7, DefaultVertexFormats.POSITION_TEX);
         var3.pos(-1.0D, -1.0D, -0.5D).tex((double)(4.0F + var11), (double)(4.0F + var12)).endVertex();
         var3.pos(1.0D, -1.0D, -0.5D).tex((double)(0.0F + var11), (double)(4.0F + var12)).endVertex();
         var3.pos(1.0D, 1.0D, -0.5D).tex((double)(0.0F + var11), (double)(0.0F + var12)).endVertex();
         var3.pos(-1.0D, 1.0D, -0.5D).tex((double)(4.0F + var11), (double)(0.0F + var12)).endVertex();
         var2.draw();
         GlStateManager.popMatrix();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.disableBlend();
      }
   }

   public void renderFireInFirstPerson(float p_78442_1_) {
      Tessellator var2 = Tessellator.getInstance();
      WorldRenderer var3 = var2.getWorldRenderer();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 0.9F);
      GlStateManager.depthFunc(519);
      GlStateManager.depthMask(false);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      float var4 = 1.0F;

      for(int var5 = 0; var5 < 2; ++var5) {
         GlStateManager.pushMatrix();
         TextureAtlasSprite var6 = this.mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
         this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
         float var7 = var6.getMinU();
         float var8 = var6.getMaxU();
         float var9 = var6.getMinV();
         float var10 = var6.getMaxV();
         float var11 = (0.0F - var4) / 2.0F;
         float var12 = var11 + var4;
         float var13 = 0.0F - var4 / 2.0F;
         float var14 = var13 + var4;
         float var15 = -0.5F;
         GlStateManager.translate((float)(-(var5 * 2 - 1)) * 0.24F, -0.3F, 0.0F);
         GlStateManager.rotate((float)(var5 * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
         var3.begin(7, DefaultVertexFormats.POSITION_TEX);
         var3.pos((double)var11, (double)var13, (double)var15).tex((double)var8, (double)var10).endVertex();
         var3.pos((double)var12, (double)var13, (double)var15).tex((double)var7, (double)var10).endVertex();
         var3.pos((double)var12, (double)var14, (double)var15).tex((double)var7, (double)var9).endVertex();
         var3.pos((double)var11, (double)var14, (double)var15).tex((double)var8, (double)var9).endVertex();
         var2.draw();
         GlStateManager.popMatrix();
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableBlend();
      GlStateManager.depthMask(true);
      GlStateManager.depthFunc(515);
   }

   public void updateEquippedItem() {
      this.prevEquippedProgress = this.equippedProgress;
      EntityPlayerSP var1 = this.mc.thePlayer;
      ItemStack var2 = var1.inventory.getCurrentItem();
      boolean var3 = false;
      if(this.itemToRender != null && var2 != null) {
         if(!this.itemToRender.getIsItemStackEqual(var2)) {
            if(Reflector.ForgeItem_shouldCauseReequipAnimation.exists()) {
               boolean var4 = Reflector.callBoolean(this.itemToRender.getItem(), Reflector.ForgeItem_shouldCauseReequipAnimation, new Object[]{this.itemToRender, var2, Boolean.valueOf(this.equippedItemSlot != var1.inventory.currentItem)});
               if(!var4) {
                  this.itemToRender = var2;
                  this.equippedItemSlot = var1.inventory.currentItem;
                  return;
               }
            }

            var3 = true;
         }
      } else if(this.itemToRender == null && var2 == null) {
         var3 = false;
      } else {
         var3 = true;
      }

      float var41 = 0.4F;
      float var5 = var3?0.0F:1.0F;
      float var6 = MathHelper.clamp_float(var5 - this.equippedProgress, -var41, var41);
      this.equippedProgress += var6;
      if(this.equippedProgress < 0.1F) {
         if(Config.isShaders()) {
            Shaders.setItemToRenderMain(var2);
         }

         this.itemToRender = var2;
         this.equippedItemSlot = var1.inventory.currentItem;
      }

   }

   public void resetEquippedProgress() {
      this.equippedProgress = 0.0F;
   }

   public void resetEquippedProgress2() {
      this.equippedProgress = 0.0F;
   }


   public static final class SwitchEnumAction {

      public static final int[] field_178094_a = new int[EnumAction.values().length];
      public static final String __OBFID = "CL_00002537";


      static {
         try {
            field_178094_a[EnumAction.NONE.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            field_178094_a[EnumAction.EAT.ordinal()] = 2;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            field_178094_a[EnumAction.DRINK.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            field_178094_a[EnumAction.BLOCK.ordinal()] = 4;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            field_178094_a[EnumAction.BOW.ordinal()] = 5;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
