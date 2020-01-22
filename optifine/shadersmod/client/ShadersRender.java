package shadersmod.client;

import java.nio.IntBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.Reflector;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import shadersmod.client.ClippingHelperShadow;
import shadersmod.client.Shaders;

public class ShadersRender {

   public static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");


   public static void setFrustrumPosition(Frustum frustrum, double x, double y, double z) {
      frustrum.setPosition(x, y, z);
   }

   public static void setupTerrain(RenderGlobal renderGlobal, Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator) {
      renderGlobal.setupTerrain(viewEntity, partialTicks, camera, frameCount, playerSpectator);
   }

   public static void beginTerrainSolid() {
      if(Shaders.isRenderingWorld) {
         Shaders.fogEnabled = true;
         Shaders.useProgram(7);
      }

   }

   public static void beginTerrainCutoutMipped() {
      if(Shaders.isRenderingWorld) {
         Shaders.useProgram(7);
      }

   }

   public static void beginTerrainCutout() {
      if(Shaders.isRenderingWorld) {
         Shaders.useProgram(7);
      }

   }

   public static void endTerrain() {
      if(Shaders.isRenderingWorld) {
         Shaders.useProgram(3);
      }

   }

   public static void beginTranslucent() {
      if(Shaders.isRenderingWorld) {
         if(Shaders.usedDepthBuffers >= 2) {
            GlStateManager.setActiveTexture('\u84cb');
            Shaders.checkGLError("pre copy depth");
            GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, Shaders.renderWidth, Shaders.renderHeight);
            Shaders.checkGLError("copy depth");
            GlStateManager.setActiveTexture('\u84c0');
         }

         Shaders.useProgram(12);
      }

   }

   public static void endTranslucent() {
      if(Shaders.isRenderingWorld) {
         Shaders.useProgram(3);
      }

   }

   public static void renderHand0(EntityRenderer er, float par1, int par2) {
      if(!Shaders.isShadowPass) {
         boolean blockTranslucentMain = Shaders.isItemToRenderMainTranslucent();
         if(!blockTranslucentMain) {
            Shaders.readCenterDepth();
            Shaders.beginHand(false);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            er.renderHand(par1, par2, true, false, false);
            Shaders.endHand();
            Shaders.setHandRenderedMain(true);
         }
      }

   }

   public static void renderHand1(EntityRenderer er, float par1, int par2) {
      if(!Shaders.isShadowPass && !Shaders.isHandRenderedMain()) {
         Shaders.readCenterDepth();
         GlStateManager.enableBlend();
         Shaders.beginHand(true);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         er.renderHand(par1, par2, true, false, true);
         Shaders.endHand();
         Shaders.setHandRenderedMain(true);
      }

   }

   public static void renderItemFP(ItemRenderer itemRenderer, float par1, boolean renderTranslucent) {
      Shaders.setRenderingFirstPersonHand(true);
      GlStateManager.depthMask(true);
      if(renderTranslucent) {
         GlStateManager.depthFunc(519);
         GL11.glPushMatrix();
         IntBuffer drawBuffers = Shaders.activeDrawBuffers;
         Shaders.setDrawBuffers(Shaders.drawBuffersNone);
         Shaders.renderItemKeepDepthMask = true;
         itemRenderer.renderItemInFirstPerson(par1);
         Shaders.renderItemKeepDepthMask = false;
         Shaders.setDrawBuffers(drawBuffers);
         GL11.glPopMatrix();
      }

      GlStateManager.depthFunc(515);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      itemRenderer.renderItemInFirstPerson(par1);
      Shaders.setRenderingFirstPersonHand(false);
   }

   public static void renderFPOverlay(EntityRenderer er, float par1, int par2) {
      if(!Shaders.isShadowPass) {
         Shaders.beginFPOverlay();
         er.renderHand(par1, par2, false, true, false);
         Shaders.endFPOverlay();
      }

   }

   public static void beginBlockDamage() {
      if(Shaders.isRenderingWorld) {
         Shaders.useProgram(11);
         if(Shaders.programsID[11] == Shaders.programsID[7]) {
            Shaders.setDrawBuffers(Shaders.drawBuffersColorAtt0);
            GlStateManager.depthMask(false);
         }
      }

   }

   public static void endBlockDamage() {
      if(Shaders.isRenderingWorld) {
         GlStateManager.depthMask(true);
         Shaders.useProgram(3);
      }

   }

   public static void renderShadowMap(EntityRenderer entityRenderer, int pass, float partialTicks, long finishTimeNano) {
      if(Shaders.usedShadowDepthBuffers > 0 && --Shaders.shadowPassCounter <= 0) {
         Minecraft mc = Minecraft.getMinecraft();
         mc.mcProfiler.endStartSection("shadow pass");
         RenderGlobal renderGlobal = mc.renderGlobal;
         Shaders.isShadowPass = true;
         Shaders.shadowPassCounter = Shaders.shadowPassInterval;
         Shaders.preShadowPassThirdPersonView = mc.gameSettings.thirdPersonView;
         mc.gameSettings.thirdPersonView = 1;
         Shaders.checkGLError("pre shadow");
         GL11.glMatrixMode(5889);
         GL11.glPushMatrix();
         GL11.glMatrixMode(5888);
         GL11.glPushMatrix();
         mc.mcProfiler.endStartSection("shadow clear");
         EXTFramebufferObject.glBindFramebufferEXT('\u8d40', Shaders.sfb);
         Shaders.checkGLError("shadow bind sfb");
         Shaders.useProgram(30);
         mc.mcProfiler.endStartSection("shadow camera");
         entityRenderer.setupCameraTransform(partialTicks, 2);
         Shaders.setCameraShadow(partialTicks);
         ActiveRenderInfo.updateRenderInfo(mc.thePlayer, mc.gameSettings.thirdPersonView == 2);
         Shaders.checkGLError("shadow camera");
         GL20.glDrawBuffers(Shaders.sfbDrawBuffers);
         Shaders.checkGLError("shadow drawbuffers");
         GL11.glReadBuffer(0);
         Shaders.checkGLError("shadow readbuffer");
         EXTFramebufferObject.glFramebufferTexture2DEXT('\u8d40', '\u8d00', 3553, Shaders.sfbDepthTextures.get(0), 0);
         if(Shaders.usedShadowColorBuffers != 0) {
            EXTFramebufferObject.glFramebufferTexture2DEXT('\u8d40', '\u8ce0', 3553, Shaders.sfbColorTextures.get(0), 0);
         }

         Shaders.checkFramebufferStatus("shadow fb");
         GL11.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glClear(Shaders.usedShadowColorBuffers != 0?16640:256);
         Shaders.checkGLError("shadow clear");
         mc.mcProfiler.endStartSection("shadow frustum");
         ClippingHelper clippingHelper = ClippingHelperShadow.getInstance();
         mc.mcProfiler.endStartSection("shadow culling");
         Frustum frustum = new Frustum(clippingHelper);
         Entity viewEntity = mc.getRenderViewEntity();
         double viewPosX = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * (double)partialTicks;
         double viewPosY = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * (double)partialTicks;
         double viewPosZ = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * (double)partialTicks;
         frustum.setPosition(viewPosX, viewPosY, viewPosZ);
         GlStateManager.shadeModel(7425);
         GlStateManager.enableDepth();
         GlStateManager.depthFunc(515);
         GlStateManager.depthMask(true);
         GlStateManager.colorMask(true, true, true, true);
         GlStateManager.disableCull();
         mc.mcProfiler.endStartSection("shadow prepareterrain");
         mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
         mc.mcProfiler.endStartSection("shadow setupterrain");
         boolean frameCount = false;
         int var17 = entityRenderer.frameCount;
         entityRenderer.frameCount = var17 + 1;
         renderGlobal.setupTerrain(viewEntity, (double)partialTicks, frustum, var17, mc.thePlayer.isSpectator());
         mc.mcProfiler.endStartSection("shadow updatechunks");
         mc.mcProfiler.endStartSection("shadow terrain");
         GlStateManager.matrixMode(5888);
         GlStateManager.pushMatrix();
         GlStateManager.disableAlpha();
         renderGlobal.renderBlockLayer(EnumWorldBlockLayer.SOLID, (double)partialTicks, 2, viewEntity);
         Shaders.checkGLError("shadow terrain solid");
         GlStateManager.enableAlpha();
         renderGlobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, (double)partialTicks, 2, viewEntity);
         Shaders.checkGLError("shadow terrain cutoutmipped");
         mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
         renderGlobal.renderBlockLayer(EnumWorldBlockLayer.CUTOUT, (double)partialTicks, 2, viewEntity);
         Shaders.checkGLError("shadow terrain cutout");
         mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
         GlStateManager.shadeModel(7424);
         GlStateManager.alphaFunc(516, 0.1F);
         GlStateManager.matrixMode(5888);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         mc.mcProfiler.endStartSection("shadow entities");
         if(Reflector.ForgeHooksClient_setRenderPass.exists()) {
            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[]{Integer.valueOf(0)});
         }

         renderGlobal.renderEntities(viewEntity, frustum, partialTicks);
         Shaders.checkGLError("shadow entities");
         GlStateManager.matrixMode(5888);
         GlStateManager.popMatrix();
         GlStateManager.depthMask(true);
         GlStateManager.disableBlend();
         GlStateManager.enableCull();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.alphaFunc(516, 0.1F);
         if(Shaders.usedShadowDepthBuffers >= 2) {
            GlStateManager.setActiveTexture('\u84c5');
            Shaders.checkGLError("pre copy shadow depth");
            GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, Shaders.shadowMapWidth, Shaders.shadowMapHeight);
            Shaders.checkGLError("copy shadow depth");
            GlStateManager.setActiveTexture('\u84c0');
         }

         GlStateManager.disableBlend();
         GlStateManager.depthMask(true);
         mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
         GlStateManager.shadeModel(7425);
         Shaders.checkGLError("shadow pre-translucent");
         GL20.glDrawBuffers(Shaders.sfbDrawBuffers);
         Shaders.checkGLError("shadow drawbuffers pre-translucent");
         Shaders.checkFramebufferStatus("shadow pre-translucent");
         if(Shaders.isRenderShadowTranslucent()) {
            mc.mcProfiler.endStartSection("shadow translucent");
            renderGlobal.renderBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, (double)partialTicks, 2, viewEntity);
            Shaders.checkGLError("shadow translucent");
         }

         if(Reflector.ForgeHooksClient_setRenderPass.exists()) {
            RenderHelper.enableStandardItemLighting();
            Reflector.call(Reflector.ForgeHooksClient_setRenderPass, new Object[]{Integer.valueOf(1)});
            renderGlobal.renderEntities(viewEntity, frustum, partialTicks);
            Reflector.call(Reflector.ForgeHooksClient_setRenderPass, new Object[]{Integer.valueOf(-1)});
            RenderHelper.disableStandardItemLighting();
            Shaders.checkGLError("shadow entities 1");
         }

         GlStateManager.shadeModel(7424);
         GlStateManager.depthMask(true);
         GlStateManager.enableCull();
         GlStateManager.disableBlend();
         GL11.glFlush();
         Shaders.checkGLError("shadow flush");
         Shaders.isShadowPass = false;
         mc.gameSettings.thirdPersonView = Shaders.preShadowPassThirdPersonView;
         mc.mcProfiler.endStartSection("shadow postprocess");
         if(Shaders.hasGlGenMipmap) {
            if(Shaders.usedShadowDepthBuffers >= 1) {
               if(Shaders.shadowMipmapEnabled[0]) {
                  GlStateManager.setActiveTexture('\u84c4');
                  GlStateManager.bindTexture(Shaders.sfbDepthTextures.get(0));
                  GL30.glGenerateMipmap(3553);
                  GL11.glTexParameteri(3553, 10241, Shaders.shadowFilterNearest[0]?9984:9987);
               }

               if(Shaders.usedShadowDepthBuffers >= 2 && Shaders.shadowMipmapEnabled[1]) {
                  GlStateManager.setActiveTexture('\u84c5');
                  GlStateManager.bindTexture(Shaders.sfbDepthTextures.get(1));
                  GL30.glGenerateMipmap(3553);
                  GL11.glTexParameteri(3553, 10241, Shaders.shadowFilterNearest[1]?9984:9987);
               }

               GlStateManager.setActiveTexture('\u84c0');
            }

            if(Shaders.usedShadowColorBuffers >= 1) {
               if(Shaders.shadowColorMipmapEnabled[0]) {
                  GlStateManager.setActiveTexture('\u84cd');
                  GlStateManager.bindTexture(Shaders.sfbColorTextures.get(0));
                  GL30.glGenerateMipmap(3553);
                  GL11.glTexParameteri(3553, 10241, Shaders.shadowColorFilterNearest[0]?9984:9987);
               }

               if(Shaders.usedShadowColorBuffers >= 2 && Shaders.shadowColorMipmapEnabled[1]) {
                  GlStateManager.setActiveTexture('\u84ce');
                  GlStateManager.bindTexture(Shaders.sfbColorTextures.get(1));
                  GL30.glGenerateMipmap(3553);
                  GL11.glTexParameteri(3553, 10241, Shaders.shadowColorFilterNearest[1]?9984:9987);
               }

               GlStateManager.setActiveTexture('\u84c0');
            }
         }

         Shaders.checkGLError("shadow postprocess");
         EXTFramebufferObject.glBindFramebufferEXT('\u8d40', Shaders.dfb);
         GL11.glViewport(0, 0, Shaders.renderWidth, Shaders.renderHeight);
         Shaders.activeDrawBuffers = null;
         mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
         Shaders.useProgram(7);
         GL11.glMatrixMode(5888);
         GL11.glPopMatrix();
         GL11.glMatrixMode(5889);
         GL11.glPopMatrix();
         GL11.glMatrixMode(5888);
         Shaders.checkGLError("shadow end");
      }

   }

   public static void preRenderChunkLayer(EnumWorldBlockLayer blockLayerIn) {
      if(Shaders.isRenderBackFace(blockLayerIn)) {
         GlStateManager.disableCull();
      }

      if(OpenGlHelper.useVbo()) {
         GL11.glEnableClientState('\u8075');
         GL20.glEnableVertexAttribArray(Shaders.midTexCoordAttrib);
         GL20.glEnableVertexAttribArray(Shaders.tangentAttrib);
         GL20.glEnableVertexAttribArray(Shaders.entityAttrib);
      }

   }

   public static void postRenderChunkLayer(EnumWorldBlockLayer blockLayerIn) {
      if(OpenGlHelper.useVbo()) {
         GL11.glDisableClientState('\u8075');
         GL20.glDisableVertexAttribArray(Shaders.midTexCoordAttrib);
         GL20.glDisableVertexAttribArray(Shaders.tangentAttrib);
         GL20.glDisableVertexAttribArray(Shaders.entityAttrib);
      }

      if(Shaders.isRenderBackFace(blockLayerIn)) {
         GlStateManager.enableCull();
      }

   }

   public static void setupArrayPointersVbo() {
      boolean vertexSizeI = true;
      GL11.glVertexPointer(3, 5126, 56, 0L);
      GL11.glColorPointer(4, 5121, 56, 12L);
      GL11.glTexCoordPointer(2, 5126, 56, 16L);
      OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
      GL11.glTexCoordPointer(2, 5122, 56, 24L);
      OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
      GL11.glNormalPointer(5120, 56, 28L);
      GL20.glVertexAttribPointer(Shaders.midTexCoordAttrib, 2, 5126, false, 56, 32L);
      GL20.glVertexAttribPointer(Shaders.tangentAttrib, 4, 5122, false, 56, 40L);
      GL20.glVertexAttribPointer(Shaders.entityAttrib, 3, 5122, false, 56, 48L);
   }

   public static void beaconBeamBegin() {
      Shaders.useProgram(14);
   }

   public static void beaconBeamStartQuad1() {}

   public static void beaconBeamStartQuad2() {}

   public static void beaconBeamDraw1() {}

   public static void beaconBeamDraw2() {
      GlStateManager.disableBlend();
   }

   public static void renderEnchantedGlintBegin() {
      Shaders.useProgram(17);
   }

   public static void renderEnchantedGlintEnd() {
      if(Shaders.isRenderingWorld) {
         if(Shaders.isRenderingFirstPersonHand() && Shaders.isRenderBothHands()) {
            Shaders.useProgram(19);
         } else {
            Shaders.useProgram(16);
         }
      } else {
         Shaders.useProgram(0);
      }

   }

   public static boolean renderEndPortal(TileEntityEndPortal te, double x, double y, double z, float partialTicks, int destroyStage, float offset) {
      if(!Shaders.isShadowPass && Shaders.programsID[Shaders.activeProgram] == 0) {
         return false;
      } else {
         GlStateManager.disableLighting();
         Config.getTextureManager().bindTexture(END_PORTAL_TEXTURE);
         Tessellator tessellator = Tessellator.getInstance();
         WorldRenderer vertexbuffer = tessellator.getWorldRenderer();
         vertexbuffer.begin(7, DefaultVertexFormats.BLOCK);
         float col = 0.5F;
         float r = col * 0.15F;
         float g = col * 0.3F;
         float b = col * 0.4F;
         float u0 = 0.0F;
         float u1 = 0.2F;
         float du = (float)(System.currentTimeMillis() % 100000L) / 100000.0F;
         short lu = 240;
         vertexbuffer.pos(x, y + (double)offset, z + 1.0D).color(r, g, b, 1.0F).tex((double)(u0 + du), (double)(u0 + du)).lightmap(lu, lu).endVertex();
         vertexbuffer.pos(x + 1.0D, y + (double)offset, z + 1.0D).color(r, g, b, 1.0F).tex((double)(u0 + du), (double)(u1 + du)).lightmap(lu, lu).endVertex();
         vertexbuffer.pos(x + 1.0D, y + (double)offset, z).color(r, g, b, 1.0F).tex((double)(u1 + du), (double)(u1 + du)).lightmap(lu, lu).endVertex();
         vertexbuffer.pos(x, y + (double)offset, z).color(r, g, b, 1.0F).tex((double)(u1 + du), (double)(u0 + du)).lightmap(lu, lu).endVertex();
         tessellator.draw();
         GlStateManager.enableLighting();
         return true;
      }
   }

}
