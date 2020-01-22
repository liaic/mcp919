package net.minecraft.client.renderer.entity;

import com.google.common.collect.Lists;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.Team.EnumVisible;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import optifine.Config;
import optifine.Reflector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import shadersmod.client.Shaders;

public abstract class RendererLivingEntity extends Render {

   public static final Logger logger = LogManager.getLogger();
   public static final DynamicTexture textureBrightness = new DynamicTexture(16, 16);
   public ModelBase mainModel;
   public FloatBuffer brightnessBuffer = GLAllocation.createDirectFloatBuffer(4);
   public List layerRenderers = Lists.newArrayList();
   public boolean renderOutlines = false;
   public static final String __OBFID = "CL_00001012";
   public static float NAME_TAG_RANGE = 64.0F;
   public static float NAME_TAG_RANGE_SNEAK = 32.0F;


   public RendererLivingEntity(RenderManager p_i46156_1_, ModelBase p_i46156_2_, float p_i46156_3_) {
      super(p_i46156_1_);
      this.mainModel = p_i46156_2_;
      this.shadowSize = p_i46156_3_;
   }

   public boolean addLayer(LayerRenderer p_177094_1_) {
      return this.layerRenderers.add(p_177094_1_);
   }

   public boolean removeLayer(LayerRenderer p_177089_1_) {
      return this.layerRenderers.remove(p_177089_1_);
   }

   public ModelBase getMainModel() {
      return this.mainModel;
   }

   public float interpolateRotation(float p_77034_1_, float p_77034_2_, float p_77034_3_) {
      float var4;
      for(var4 = p_77034_2_ - p_77034_1_; var4 < -180.0F; var4 += 360.0F) {
         ;
      }

      while(var4 >= 180.0F) {
         var4 -= 360.0F;
      }

      return p_77034_1_ + p_77034_3_ * var4;
   }

   public void transformHeldFull3DItemLayer() {}

   public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      if(!Reflector.RenderLivingEvent_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Pre_Constructor, new Object[]{p_76986_1_, this, Double.valueOf(p_76986_2_), Double.valueOf(p_76986_4_), Double.valueOf(p_76986_6_)})) {
         GlStateManager.pushMatrix();
         GlStateManager.disableCull();
         this.mainModel.swingProgress = this.getSwingProgress(p_76986_1_, p_76986_9_);
         this.mainModel.isRiding = p_76986_1_.isRiding();
         if(Reflector.ForgeEntity_shouldRiderSit.exists()) {
            this.mainModel.isRiding = p_76986_1_.isRiding() && p_76986_1_.ridingEntity != null && Reflector.callBoolean(p_76986_1_.ridingEntity, Reflector.ForgeEntity_shouldRiderSit, new Object[0]);
         }

         this.mainModel.isChild = p_76986_1_.isChild();

         try {
            float var19 = this.interpolateRotation(p_76986_1_.prevRenderYawOffset, p_76986_1_.renderYawOffset, p_76986_9_);
            float var11 = this.interpolateRotation(p_76986_1_.prevRotationYawHead, p_76986_1_.rotationYawHead, p_76986_9_);
            float var12 = var11 - var19;
            float var14;
            if(this.mainModel.isRiding && p_76986_1_.ridingEntity instanceof EntityLivingBase) {
               EntityLivingBase var20 = (EntityLivingBase)p_76986_1_.ridingEntity;
               var19 = this.interpolateRotation(var20.prevRenderYawOffset, var20.renderYawOffset, p_76986_9_);
               var12 = var11 - var19;
               var14 = MathHelper.wrapAngleTo180_float(var12);
               if(var14 < -85.0F) {
                  var14 = -85.0F;
               }

               if(var14 >= 85.0F) {
                  var14 = 85.0F;
               }

               var19 = var11 - var14;
               if(var14 * var14 > 2500.0F) {
                  var19 += var14 * 0.2F;
               }
            }

            float var201 = p_76986_1_.prevRotationPitch + (p_76986_1_.rotationPitch - p_76986_1_.prevRotationPitch) * p_76986_9_;
            this.renderLivingAt(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_);
            var14 = this.handleRotationFloat(p_76986_1_, p_76986_9_);
            this.rotateCorpse(p_76986_1_, var14, var19, p_76986_9_);
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(-1.0F, -1.0F, 1.0F);
            this.preRenderCallback(p_76986_1_, p_76986_9_);
            float var15 = 0.0625F;
            GlStateManager.translate(0.0F, -1.5078125F, 0.0F);
            float var16 = p_76986_1_.prevLimbSwingAmount + (p_76986_1_.limbSwingAmount - p_76986_1_.prevLimbSwingAmount) * p_76986_9_;
            float var17 = p_76986_1_.limbSwing - p_76986_1_.limbSwingAmount * (1.0F - p_76986_9_);
            if(p_76986_1_.isChild()) {
               var17 *= 3.0F;
            }

            if(var16 > 1.0F) {
               var16 = 1.0F;
            }

            GlStateManager.enableAlpha();
            this.mainModel.setLivingAnimations(p_76986_1_, var17, var16, p_76986_9_);
            this.mainModel.setRotationAngles(var17, var16, var14, var12, var201, 0.0625F, p_76986_1_);
            boolean var18;
            if(this.renderOutlines) {
               var18 = this.setScoreTeamColor(p_76986_1_);
               this.renderModel(p_76986_1_, var17, var16, var14, var12, var201, 0.0625F);
               if(var18) {
                  this.unsetScoreTeamColor();
               }
            } else {
               var18 = this.setDoRenderBrightness(p_76986_1_, p_76986_9_);
               this.renderModel(p_76986_1_, var17, var16, var14, var12, var201, 0.0625F);
               if(var18) {
                  this.unsetBrightness();
               }

               GlStateManager.depthMask(true);
               if(!(p_76986_1_ instanceof EntityPlayer) || !((EntityPlayer)p_76986_1_).isSpectator()) {
                  this.renderLayers(p_76986_1_, var17, var16, p_76986_9_, var14, var12, var201, 0.0625F);
               }
            }

            GlStateManager.disableRescaleNormal();
         } catch (Exception var191) {
            logger.error("Couldn\'t render entity", var191);
         }

         GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
         GlStateManager.enableTexture2D();
         GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
         GlStateManager.enableCull();
         GlStateManager.popMatrix();
         if(!this.renderOutlines) {
            super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
         }

         if(!Reflector.RenderLivingEvent_Post_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Post_Constructor, new Object[]{p_76986_1_, this, Double.valueOf(p_76986_2_), Double.valueOf(p_76986_4_), Double.valueOf(p_76986_6_)})) {
            ;
         }
      }
   }

   public boolean setScoreTeamColor(EntityLivingBase p_177088_1_) {
      int var2 = 16777215;
      if(p_177088_1_ instanceof EntityPlayer) {
         ScorePlayerTeam var6 = (ScorePlayerTeam)p_177088_1_.getTeam();
         if(var6 != null) {
            String var7 = FontRenderer.getFormatFromString(var6.getColorPrefix());
            if(var7.length() >= 2) {
               var2 = this.getFontRendererFromRenderManager().getColorCode(var7.charAt(1));
            }
         }
      }

      float var61 = (float)(var2 >> 16 & 255) / 255.0F;
      float var71 = (float)(var2 >> 8 & 255) / 255.0F;
      float var5 = (float)(var2 & 255) / 255.0F;
      GlStateManager.disableLighting();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      GlStateManager.color(var61, var71, var5, 1.0F);
      GlStateManager.disableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.disableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      return true;
   }

   public void unsetScoreTeamColor() {
      GlStateManager.enableLighting();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      GlStateManager.enableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.enableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
   }

   public void renderModel(EntityLivingBase p_77036_1_, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_) {
      boolean var8 = !p_77036_1_.isInvisible();
      boolean var9 = !var8 && !p_77036_1_.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer);
      if(var8 || var9) {
         if(!this.bindEntityTexture(p_77036_1_)) {
            return;
         }

         if(var9) {
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
            GlStateManager.depthMask(false);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.alphaFunc(516, 0.003921569F);
         }

         this.mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
         if(var9) {
            GlStateManager.disableBlend();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
         }
      }

   }

   public boolean setDoRenderBrightness(EntityLivingBase p_177090_1_, float p_177090_2_) {
      return this.setBrightness(p_177090_1_, p_177090_2_, true);
   }

   public boolean setBrightness(EntityLivingBase p_177092_1_, float p_177092_2_, boolean p_177092_3_) {
      float var4 = p_177092_1_.getBrightness(p_177092_2_);
      int var5 = this.getColorMultiplier(p_177092_1_, var4, p_177092_2_);
      boolean var6 = (var5 >> 24 & 255) > 0;
      boolean var7 = p_177092_1_.hurtTime > 0 || p_177092_1_.deathTime > 0;
      if(!var6 && !var7) {
         return false;
      } else if(!var6 && !p_177092_3_) {
         return false;
      } else {
         GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
         GlStateManager.enableTexture2D();
         GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
         GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
         GlStateManager.enableTexture2D();
         GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, OpenGlHelper.GL_INTERPOLATE);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_CONSTANT);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE2_RGB, OpenGlHelper.GL_CONSTANT);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND2_RGB, 770);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
         this.brightnessBuffer.position(0);
         if(var7) {
            this.brightnessBuffer.put(1.0F);
            this.brightnessBuffer.put(0.0F);
            this.brightnessBuffer.put(0.0F);
            this.brightnessBuffer.put(0.3F);
            if(Config.isShaders()) {
               Shaders.setEntityColor(1.0F, 0.0F, 0.0F, 0.3F);
            }
         } else {
            float var8 = (float)(var5 >> 24 & 255) / 255.0F;
            float var9 = (float)(var5 >> 16 & 255) / 255.0F;
            float var10 = (float)(var5 >> 8 & 255) / 255.0F;
            float var11 = (float)(var5 & 255) / 255.0F;
            this.brightnessBuffer.put(var9);
            this.brightnessBuffer.put(var10);
            this.brightnessBuffer.put(var11);
            this.brightnessBuffer.put(1.0F - var8);
            if(Config.isShaders()) {
               Shaders.setEntityColor(var9, var10, var11, 1.0F - var8);
            }
         }

         this.brightnessBuffer.flip();
         GL11.glTexEnv(8960, 8705, this.brightnessBuffer);
         GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
         GlStateManager.enableTexture2D();
         GlStateManager.bindTexture(textureBrightness.getGlTextureId());
         GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_PREVIOUS);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.lightmapTexUnit);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
         GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
         GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
         return true;
      }
   }

   public void unsetBrightness() {
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      GlStateManager.enableTexture2D();
      GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_ALPHA, OpenGlHelper.GL_PRIMARY_COLOR);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_ALPHA, 770);
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
      GlStateManager.disableTexture2D();
      GlStateManager.bindTexture(0);
      GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
      GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      if(Config.isShaders()) {
         Shaders.setEntityColor(0.0F, 0.0F, 0.0F, 0.0F);
      }

   }

   public void renderLivingAt(EntityLivingBase p_77039_1_, double p_77039_2_, double p_77039_4_, double p_77039_6_) {
      GlStateManager.translate((float)p_77039_2_, (float)p_77039_4_, (float)p_77039_6_);
   }

   public void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
      GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
      if(p_77043_1_.deathTime > 0) {
         float var6 = ((float)p_77043_1_.deathTime + p_77043_4_ - 1.0F) / 20.0F * 1.6F;
         var6 = MathHelper.sqrt_float(var6);
         if(var6 > 1.0F) {
            var6 = 1.0F;
         }

         GlStateManager.rotate(var6 * this.getDeathMaxRotation(p_77043_1_), 0.0F, 0.0F, 1.0F);
      } else {
         String var61 = EnumChatFormatting.getTextWithoutFormattingCodes(p_77043_1_.getName());
         if(var61 != null && (var61.equals("Dinnerbone") || var61.equals("Grumm")) && (!(p_77043_1_ instanceof EntityPlayer) || ((EntityPlayer)p_77043_1_).isWearing(EnumPlayerModelParts.CAPE))) {
            GlStateManager.translate(0.0F, p_77043_1_.height + 0.1F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
         }
      }

   }

   public float getSwingProgress(EntityLivingBase p_77040_1_, float p_77040_2_) {
      return p_77040_1_.getSwingProgress(p_77040_2_);
   }

   public float handleRotationFloat(EntityLivingBase p_77044_1_, float p_77044_2_) {
      return (float)p_77044_1_.ticksExisted + p_77044_2_;
   }

   public void renderLayers(EntityLivingBase p_177093_1_, float p_177093_2_, float p_177093_3_, float p_177093_4_, float p_177093_5_, float p_177093_6_, float p_177093_7_, float p_177093_8_) {
      Iterator var9 = this.layerRenderers.iterator();

      while(var9.hasNext()) {
         LayerRenderer var10 = (LayerRenderer)var9.next();
         boolean var11 = this.setBrightness(p_177093_1_, p_177093_4_, var10.shouldCombineTextures());
         var10.doRenderLayer(p_177093_1_, p_177093_2_, p_177093_3_, p_177093_4_, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_);
         if(var11) {
            this.unsetBrightness();
         }
      }

   }

   public float getDeathMaxRotation(EntityLivingBase p_77037_1_) {
      return 90.0F;
   }

   public int getColorMultiplier(EntityLivingBase p_77030_1_, float p_77030_2_, float p_77030_3_) {
      return 0;
   }

   public void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_) {}

   public void renderName(EntityLivingBase p_77033_1_, double p_77033_2_, double p_77033_4_, double p_77033_6_) {
      if(!Reflector.RenderLivingEvent_Specials_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Pre_Constructor, new Object[]{p_77033_1_, this, Double.valueOf(p_77033_2_), Double.valueOf(p_77033_4_), Double.valueOf(p_77033_6_)})) {
         if(this.canRenderName(p_77033_1_)) {
            double var8 = p_77033_1_.getDistanceSqToEntity(this.renderManager.livingPlayer);
            float var10 = p_77033_1_.isSneaking()?NAME_TAG_RANGE_SNEAK:NAME_TAG_RANGE;
            if(var8 < (double)(var10 * var10)) {
               String var11 = p_77033_1_.getDisplayName().getFormattedText();
               float var12 = 0.02666667F;
               GlStateManager.alphaFunc(516, 0.1F);
               if(p_77033_1_.isSneaking()) {
                  FontRenderer var13 = this.getFontRendererFromRenderManager();
                  GlStateManager.pushMatrix();
                  GlStateManager.translate((float)p_77033_2_, (float)p_77033_4_ + p_77033_1_.height + 0.5F - (p_77033_1_.isChild()?p_77033_1_.height / 2.0F:0.0F), (float)p_77033_6_);
                  GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                  GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                  GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                  GlStateManager.scale(-0.02666667F, -0.02666667F, 0.02666667F);
                  GlStateManager.translate(0.0F, 9.374999F, 0.0F);
                  GlStateManager.disableLighting();
                  GlStateManager.depthMask(false);
                  GlStateManager.enableBlend();
                  GlStateManager.disableTexture2D();
                  GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                  int var14 = var13.getStringWidth(var11) / 2;
                  Tessellator var15 = Tessellator.getInstance();
                  WorldRenderer var16 = var15.getWorldRenderer();
                  var16.begin(7, DefaultVertexFormats.POSITION_COLOR);
                  var16.pos((double)(-var14 - 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                  var16.pos((double)(-var14 - 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                  var16.pos((double)(var14 + 1), 8.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                  var16.pos((double)(var14 + 1), -1.0D, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                  var15.draw();
                  GlStateManager.enableTexture2D();
                  GlStateManager.depthMask(true);
                  var13.drawString(var11, -var13.getStringWidth(var11) / 2, 0, 553648127);
                  GlStateManager.enableLighting();
                  GlStateManager.disableBlend();
                  GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                  GlStateManager.popMatrix();
               } else {
                  this.renderOffsetLivingLabel(p_77033_1_, p_77033_2_, p_77033_4_ - (p_77033_1_.isChild()?(double)(p_77033_1_.height / 2.0F):0.0D), p_77033_6_, var11, 0.02666667F, var8);
               }
            }
         }

         if(!Reflector.RenderLivingEvent_Specials_Post_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Post_Constructor, new Object[]{p_77033_1_, this, Double.valueOf(p_77033_2_), Double.valueOf(p_77033_4_), Double.valueOf(p_77033_6_)})) {
            ;
         }
      }
   }

   public boolean canRenderName(EntityLivingBase targetEntity) {
      EntityPlayerSP var2 = Minecraft.getMinecraft().thePlayer;
      if(targetEntity instanceof EntityPlayer && targetEntity != var2) {
         Team var3 = targetEntity.getTeam();
         Team var4 = var2.getTeam();
         if(var3 != null) {
            EnumVisible var5 = var3.getNameTagVisibility();
            switch(RendererLivingEntity.SwitchEnumVisible.field_178679_a[var5.ordinal()]) {
            case 1:
               return true;
            case 2:
               return false;
            case 3:
               return var4 == null || var3.isSameTeam(var4);
            case 4:
               return var4 == null || !var3.isSameTeam(var4);
            default:
               return true;
            }
         }
      }

      return Minecraft.isGuiEnabled() && targetEntity != this.renderManager.livingPlayer && !targetEntity.isInvisibleToPlayer(var2) && targetEntity.riddenByEntity == null;
   }

   public void setRenderOutlines(boolean p_177086_1_) {
      this.renderOutlines = p_177086_1_;
   }

   public boolean canRenderName(Entity p_177070_1_) {
      return this.canRenderName((EntityLivingBase)p_177070_1_);
   }

   public void renderName(Entity p_177067_1_, double p_177067_2_, double p_177067_4_, double p_177067_6_) {
      this.renderName((EntityLivingBase)p_177067_1_, p_177067_2_, p_177067_4_, p_177067_6_);
   }

   public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
      this.doRender((EntityLivingBase)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
   }

   static {
      int[] var0 = textureBrightness.getTextureData();

      for(int var1 = 0; var1 < 256; ++var1) {
         var0[var1] = -1;
      }

      textureBrightness.updateDynamicTexture();
   }

   public static final class SwitchEnumVisible {

      public static final int[] field_178679_a = new int[EnumVisible.values().length];
      public static final String __OBFID = "CL_00002435";


      static {
         try {
            field_178679_a[EnumVisible.ALWAYS.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            field_178679_a[EnumVisible.NEVER.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            field_178679_a[EnumVisible.HIDE_FOR_OTHER_TEAMS.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            field_178679_a[EnumVisible.HIDE_FOR_OWN_TEAM.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
