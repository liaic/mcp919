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
import net.minecraft.src.Config;
import net.minecraft.src.Reflector;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import shadersmod.client.ClippingHelperShadow;
import shadersmod.client.Shaders;

public class ShadersRender {
   private static final ResourceLocation END_PORTAL_TEXTURE = new ResourceLocation("textures/entity/end_portal.png");

   public static void setFrustrumPosition(Frustum frustrum, double x, double y, double z) {
      frustrum.func_78547_a(x, y, z);
   }

   public static void setupTerrain(RenderGlobal renderGlobal, Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator) {
      renderGlobal.func_174970_a(viewEntity, partialTicks, camera, frameCount, playerSpectator);
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
            GlStateManager.func_179138_g('\u84cb');
            Shaders.checkGLError("pre copy depth");
            GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, Shaders.renderWidth, Shaders.renderHeight);
            Shaders.checkGLError("copy depth");
            GlStateManager.func_179138_g('\u84c0');
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
         boolean flag = Shaders.isItemToRenderMainTranslucent();
         if(!flag) {
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
         GlStateManager.func_179147_l();
         Shaders.beginHand(true);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         er.renderHand(par1, par2, true, false, true);
         Shaders.endHand();
         Shaders.setHandRenderedMain(true);
      }

   }

   public static void renderItemFP(ItemRenderer itemRenderer, float par1, boolean renderTranslucent) {
      Shaders.setRenderingFirstPersonHand(true);
      GlStateManager.func_179132_a(true);
      if(renderTranslucent) {
         GlStateManager.func_179143_c(519);
         GL11.glPushMatrix();
         IntBuffer intbuffer = Shaders.activeDrawBuffers;
         Shaders.setDrawBuffers(Shaders.drawBuffersNone);
         Shaders.renderItemKeepDepthMask = true;
         itemRenderer.func_78440_a(par1);
         Shaders.renderItemKeepDepthMask = false;
         Shaders.setDrawBuffers(intbuffer);
         GL11.glPopMatrix();
      }

      GlStateManager.func_179143_c(515);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      itemRenderer.func_78440_a(par1);
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
            GlStateManager.func_179132_a(false);
         }
      }

   }

   public static void endBlockDamage() {
      if(Shaders.isRenderingWorld) {
         GlStateManager.func_179132_a(true);
         Shaders.useProgram(3);
      }

   }

   public static void renderShadowMap(EntityRenderer entityRenderer, int pass, float partialTicks, long finishTimeNano) {
      if(Shaders.usedShadowDepthBuffers > 0 && --Shaders.shadowPassCounter <= 0) {
         Minecraft minecraft = Minecraft.func_71410_x();
         minecraft.field_71424_I.func_76318_c("shadow pass");
         RenderGlobal renderglobal = minecraft.field_71438_f;
         Shaders.isShadowPass = true;
         Shaders.shadowPassCounter = Shaders.shadowPassInterval;
         Shaders.preShadowPassThirdPersonView = minecraft.field_71474_y.field_74320_O;
         minecraft.field_71474_y.field_74320_O = 1;
         Shaders.checkGLError("pre shadow");
         GL11.glMatrixMode(5889);
         GL11.glPushMatrix();
         GL11.glMatrixMode(5888);
         GL11.glPushMatrix();
         minecraft.field_71424_I.func_76318_c("shadow clear");
         EXTFramebufferObject.glBindFramebufferEXT('\u8d40', Shaders.sfb);
         Shaders.checkGLError("shadow bind sfb");
         Shaders.useProgram(30);
         minecraft.field_71424_I.func_76318_c("shadow camera");
         entityRenderer.func_78479_a(partialTicks, 2);
         Shaders.setCameraShadow(partialTicks);
         ActiveRenderInfo.func_74583_a(minecraft.field_71439_g, minecraft.field_71474_y.field_74320_O == 2);
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
         minecraft.field_71424_I.func_76318_c("shadow frustum");
         ClippingHelper clippinghelper = ClippingHelperShadow.getInstance();
         minecraft.field_71424_I.func_76318_c("shadow culling");
         Frustum frustum = new Frustum(clippinghelper);
         Entity entity = minecraft.func_175606_aa();
         double d0 = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)partialTicks;
         double d1 = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)partialTicks;
         double d2 = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)partialTicks;
         frustum.func_78547_a(d0, d1, d2);
         GlStateManager.func_179103_j(7425);
         GlStateManager.func_179126_j();
         GlStateManager.func_179143_c(515);
         GlStateManager.func_179132_a(true);
         GlStateManager.func_179135_a(true, true, true, true);
         GlStateManager.func_179129_p();
         minecraft.field_71424_I.func_76318_c("shadow prepareterrain");
         minecraft.func_110434_K().func_110577_a(TextureMap.field_110575_b);
         minecraft.field_71424_I.func_76318_c("shadow setupterrain");
         int i = 0;
         i = entityRenderer.field_175084_ae;
         entityRenderer.field_175084_ae = i + 1;
         renderglobal.func_174970_a(entity, (double)partialTicks, frustum, i, minecraft.field_71439_g.func_175149_v());
         minecraft.field_71424_I.func_76318_c("shadow updatechunks");
         minecraft.field_71424_I.func_76318_c("shadow terrain");
         GlStateManager.func_179128_n(5888);
         GlStateManager.func_179094_E();
         GlStateManager.func_179118_c();
         renderglobal.func_174977_a(EnumWorldBlockLayer.SOLID, (double)partialTicks, 2, entity);
         Shaders.checkGLError("shadow terrain solid");
         GlStateManager.func_179141_d();
         renderglobal.func_174977_a(EnumWorldBlockLayer.CUTOUT_MIPPED, (double)partialTicks, 2, entity);
         Shaders.checkGLError("shadow terrain cutoutmipped");
         minecraft.func_110434_K().func_110581_b(TextureMap.field_110575_b).func_174936_b(false, false);
         renderglobal.func_174977_a(EnumWorldBlockLayer.CUTOUT, (double)partialTicks, 2, entity);
         Shaders.checkGLError("shadow terrain cutout");
         minecraft.func_110434_K().func_110581_b(TextureMap.field_110575_b).func_174935_a();
         GlStateManager.func_179103_j(7424);
         GlStateManager.func_179092_a(516, 0.1F);
         GlStateManager.func_179128_n(5888);
         GlStateManager.func_179121_F();
         GlStateManager.func_179094_E();
         minecraft.field_71424_I.func_76318_c("shadow entities");
         if(Reflector.ForgeHooksClient_setRenderPass.exists()) {
            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[]{Integer.valueOf(0)});
         }

         renderglobal.func_180446_a(entity, frustum, partialTicks);
         Shaders.checkGLError("shadow entities");
         GlStateManager.func_179128_n(5888);
         GlStateManager.func_179121_F();
         GlStateManager.func_179132_a(true);
         GlStateManager.func_179084_k();
         GlStateManager.func_179089_o();
         GlStateManager.func_179120_a(770, 771, 1, 0);
         GlStateManager.func_179092_a(516, 0.1F);
         if(Shaders.usedShadowDepthBuffers >= 2) {
            GlStateManager.func_179138_g('\u84c5');
            Shaders.checkGLError("pre copy shadow depth");
            GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, Shaders.shadowMapWidth, Shaders.shadowMapHeight);
            Shaders.checkGLError("copy shadow depth");
            GlStateManager.func_179138_g('\u84c0');
         }

         GlStateManager.func_179084_k();
         GlStateManager.func_179132_a(true);
         minecraft.func_110434_K().func_110577_a(TextureMap.field_110575_b);
         GlStateManager.func_179103_j(7425);
         Shaders.checkGLError("shadow pre-translucent");
         GL20.glDrawBuffers(Shaders.sfbDrawBuffers);
         Shaders.checkGLError("shadow drawbuffers pre-translucent");
         Shaders.checkFramebufferStatus("shadow pre-translucent");
         if(Shaders.isRenderShadowTranslucent()) {
            minecraft.field_71424_I.func_76318_c("shadow translucent");
            renderglobal.func_174977_a(EnumWorldBlockLayer.TRANSLUCENT, (double)partialTicks, 2, entity);
            Shaders.checkGLError("shadow translucent");
         }

         if(Reflector.ForgeHooksClient_setRenderPass.exists()) {
            RenderHelper.func_74519_b();
            Reflector.call(Reflector.ForgeHooksClient_setRenderPass, new Object[]{Integer.valueOf(1)});
            renderglobal.func_180446_a(entity, frustum, partialTicks);
            Reflector.call(Reflector.ForgeHooksClient_setRenderPass, new Object[]{Integer.valueOf(-1)});
            RenderHelper.func_74518_a();
            Shaders.checkGLError("shadow entities 1");
         }

         GlStateManager.func_179103_j(7424);
         GlStateManager.func_179132_a(true);
         GlStateManager.func_179089_o();
         GlStateManager.func_179084_k();
         GL11.glFlush();
         Shaders.checkGLError("shadow flush");
         Shaders.isShadowPass = false;
         minecraft.field_71474_y.field_74320_O = Shaders.preShadowPassThirdPersonView;
         minecraft.field_71424_I.func_76318_c("shadow postprocess");
         if(Shaders.hasGlGenMipmap) {
            if(Shaders.usedShadowDepthBuffers >= 1) {
               if(Shaders.shadowMipmapEnabled[0]) {
                  GlStateManager.func_179138_g('\u84c4');
                  GlStateManager.func_179144_i(Shaders.sfbDepthTextures.get(0));
                  GL30.glGenerateMipmap(3553);
                  GL11.glTexParameteri(3553, 10241, Shaders.shadowFilterNearest[0]?9984:9987);
               }

               if(Shaders.usedShadowDepthBuffers >= 2 && Shaders.shadowMipmapEnabled[1]) {
                  GlStateManager.func_179138_g('\u84c5');
                  GlStateManager.func_179144_i(Shaders.sfbDepthTextures.get(1));
                  GL30.glGenerateMipmap(3553);
                  GL11.glTexParameteri(3553, 10241, Shaders.shadowFilterNearest[1]?9984:9987);
               }

               GlStateManager.func_179138_g('\u84c0');
            }

            if(Shaders.usedShadowColorBuffers >= 1) {
               if(Shaders.shadowColorMipmapEnabled[0]) {
                  GlStateManager.func_179138_g('\u84cd');
                  GlStateManager.func_179144_i(Shaders.sfbColorTextures.get(0));
                  GL30.glGenerateMipmap(3553);
                  GL11.glTexParameteri(3553, 10241, Shaders.shadowColorFilterNearest[0]?9984:9987);
               }

               if(Shaders.usedShadowColorBuffers >= 2 && Shaders.shadowColorMipmapEnabled[1]) {
                  GlStateManager.func_179138_g('\u84ce');
                  GlStateManager.func_179144_i(Shaders.sfbColorTextures.get(1));
                  GL30.glGenerateMipmap(3553);
                  GL11.glTexParameteri(3553, 10241, Shaders.shadowColorFilterNearest[1]?9984:9987);
               }

               GlStateManager.func_179138_g('\u84c0');
            }
         }

         Shaders.checkGLError("shadow postprocess");
         EXTFramebufferObject.glBindFramebufferEXT('\u8d40', Shaders.dfb);
         GL11.glViewport(0, 0, Shaders.renderWidth, Shaders.renderHeight);
         Shaders.activeDrawBuffers = null;
         minecraft.func_110434_K().func_110577_a(TextureMap.field_110575_b);
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
         GlStateManager.func_179129_p();
      }

      if(OpenGlHelper.func_176075_f()) {
         GL11.glEnableClientState('\u8075');
         GL20.glEnableVertexAttribArray(Shaders.midTexCoordAttrib);
         GL20.glEnableVertexAttribArray(Shaders.tangentAttrib);
         GL20.glEnableVertexAttribArray(Shaders.entityAttrib);
      }

   }

   public static void postRenderChunkLayer(EnumWorldBlockLayer blockLayerIn) {
      if(OpenGlHelper.func_176075_f()) {
         GL11.glDisableClientState('\u8075');
         GL20.glDisableVertexAttribArray(Shaders.midTexCoordAttrib);
         GL20.glDisableVertexAttribArray(Shaders.tangentAttrib);
         GL20.glDisableVertexAttribArray(Shaders.entityAttrib);
      }

      if(Shaders.isRenderBackFace(blockLayerIn)) {
         GlStateManager.func_179089_o();
      }

   }

   public static void setupArrayPointersVbo() {
      int i = 14;
      GL11.glVertexPointer(3, 5126, 56, 0L);
      GL11.glColorPointer(4, 5121, 56, 12L);
      GL11.glTexCoordPointer(2, 5126, 56, 16L);
      OpenGlHelper.func_77472_b(OpenGlHelper.field_77476_b);
      GL11.glTexCoordPointer(2, 5122, 56, 24L);
      OpenGlHelper.func_77472_b(OpenGlHelper.field_77478_a);
      GL11.glNormalPointer(5120, 56, 28L);
      GL20.glVertexAttribPointer(Shaders.midTexCoordAttrib, 2, 5126, false, 56, 32L);
      GL20.glVertexAttribPointer(Shaders.tangentAttrib, 4, 5122, false, 56, 40L);
      GL20.glVertexAttribPointer(Shaders.entityAttrib, 3, 5122, false, 56, 48L);
   }

   public static void beaconBeamBegin() {
      Shaders.useProgram(14);
   }

   public static void beaconBeamStartQuad1() {
   }

   public static void beaconBeamStartQuad2() {
   }

   public static void beaconBeamDraw1() {
   }

   public static void beaconBeamDraw2() {
      GlStateManager.func_179084_k();
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
         GlStateManager.func_179140_f();
         Config.getTextureManager().func_110577_a(END_PORTAL_TEXTURE);
         Tessellator tessellator = Tessellator.func_178181_a();
         WorldRenderer worldrenderer = tessellator.func_178180_c();
         worldrenderer.func_181668_a(7, DefaultVertexFormats.field_176600_a);
         float f = 0.5F;
         float f1 = f * 0.15F;
         float f2 = f * 0.3F;
         float f3 = f * 0.4F;
         float f4 = 0.0F;
         float f5 = 0.2F;
         float f6 = (float)(System.currentTimeMillis() % 100000L) / 100000.0F;
         int i = 240;
         worldrenderer.func_181662_b(x, y + (double)offset, z + 1.0D).func_181666_a(f1, f2, f3, 1.0F).func_181673_a((double)(f4 + f6), (double)(f4 + f6)).func_181671_a(i, i).func_181675_d();
         worldrenderer.func_181662_b(x + 1.0D, y + (double)offset, z + 1.0D).func_181666_a(f1, f2, f3, 1.0F).func_181673_a((double)(f4 + f6), (double)(f5 + f6)).func_181671_a(i, i).func_181675_d();
         worldrenderer.func_181662_b(x + 1.0D, y + (double)offset, z).func_181666_a(f1, f2, f3, 1.0F).func_181673_a((double)(f5 + f6), (double)(f5 + f6)).func_181671_a(i, i).func_181675_d();
         worldrenderer.func_181662_b(x, y + (double)offset, z).func_181666_a(f1, f2, f3, 1.0F).func_181673_a((double)(f5 + f6), (double)(f4 + f6)).func_181671_a(i, i).func_181675_d();
         tessellator.func_78381_a();
         GlStateManager.func_179145_e();
         return true;
      }
   }
}
