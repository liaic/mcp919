package net.minecraft.client.gui;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonLanguage;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import optifine.CustomPanorama;
import optifine.CustomPanoramaProperties;
import optifine.Reflector;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.Project;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {

   public static final AtomicInteger field_175373_f = new AtomicInteger(0);
   public static final Logger logger = LogManager.getLogger();
   public static final Random RANDOM = new Random();
   public float updateCounter;
   public String splashText;
   public GuiButton buttonResetDemo;
   public int panoramaTimer;
   public DynamicTexture viewportTexture;
   public boolean field_175375_v = true;
   public final Object threadLock = new Object();
   public String openGLWarning1;
   public String openGLWarning2;
   public String openGLWarningLink;
   public static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
   public static final ResourceLocation minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
   public static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[]{new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")};
   public static final String field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
   public int field_92024_r;
   public int field_92023_s;
   public int field_92022_t;
   public int field_92021_u;
   public int field_92020_v;
   public int field_92019_w;
   public ResourceLocation backgroundTexture;
   public GuiButton realmsButton;
   public static final String __OBFID = "CL_00001154";
   public GuiButton modButton;
   public GuiScreen modUpdateNotification;


   public GuiMainMenu() {
      this.openGLWarning2 = field_96138_a;
      this.splashText = "missingno";
      BufferedReader var1 = null;

      try {
         ArrayList var11 = Lists.newArrayList();
         var1 = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));

         String var3;
         while((var3 = var1.readLine()) != null) {
            var3 = var3.trim();
            if(!var3.isEmpty()) {
               var11.add(var3);
            }
         }

         if(!var11.isEmpty()) {
            do {
               this.splashText = (String)var11.get(RANDOM.nextInt(var11.size()));
            } while(this.splashText.hashCode() == 125780783);
         }
      } catch (IOException var12) {
         ;
      } finally {
         if(var1 != null) {
            try {
               var1.close();
            } catch (IOException var111) {
               ;
            }
         }

      }

      this.updateCounter = RANDOM.nextFloat();
      this.openGLWarning1 = "";
      if(!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
         this.openGLWarning1 = I18n.format("title.oldgl1", new Object[0]);
         this.openGLWarning2 = I18n.format("title.oldgl2", new Object[0]);
         this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
      }

   }

   public void updateScreen() {
      ++this.panoramaTimer;
   }

   public boolean doesGuiPauseGame() {
      return false;
   }

   public void keyTyped(char typedChar, int keyCode) throws IOException {}

   public void initGui() {
      this.viewportTexture = new DynamicTexture(256, 256);
      this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
      Calendar var1 = Calendar.getInstance();
      var1.setTime(new Date());
      if(var1.get(2) + 1 == 11 && var1.get(5) == 9) {
         this.splashText = "Happy birthday, ez!";
      } else if(var1.get(2) + 1 == 6 && var1.get(5) == 1) {
         this.splashText = "Happy birthday, Notch!";
      } else if(var1.get(2) + 1 == 12 && var1.get(5) == 24) {
         this.splashText = "Merry X-mas!";
      } else if(var1.get(2) + 1 == 1 && var1.get(5) == 1) {
         this.splashText = "Happy new year!";
      } else if(var1.get(2) + 1 == 10 && var1.get(5) == 31) {
         this.splashText = "OOoooOOOoooo! Spooky!";
      }

      boolean var2 = true;
      int var3 = this.height / 4 + 48;
      if(this.mc.isDemo()) {
         this.addDemoButtons(var3, 24);
      } else {
         this.addSingleplayerMultiplayerButtons(var3, 24);
      }

      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, var3 + 72 + 12, 98, 20, I18n.format("menu.options", new Object[0])));
      this.buttonList.add(new GuiButton(4, this.width / 2 + 2, var3 + 72 + 12, 98, 20, I18n.format("menu.quit", new Object[0])));
      this.buttonList.add(new GuiButtonLanguage(5, this.width / 2 - 124, var3 + 72 + 12));
      Object var4 = this.threadLock;
      Object var5 = this.threadLock;
      synchronized(this.threadLock) {
         this.field_92023_s = this.fontRendererObj.getStringWidth(this.openGLWarning1);
         this.field_92024_r = this.fontRendererObj.getStringWidth(this.openGLWarning2);
         int var51 = Math.max(this.field_92023_s, this.field_92024_r);
         this.field_92022_t = (this.width - var51) / 2;
         this.field_92021_u = ((GuiButton)this.buttonList.get(0)).yPosition - 24;
         this.field_92020_v = this.field_92022_t + var51;
         this.field_92019_w = this.field_92021_u + 24;
      }

      this.mc.setConnectedToRealms(false);
   }

   public void addSingleplayerMultiplayerButtons(int p_73969_1_, int p_73969_2_) {
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer", new Object[0])));
      this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 1, I18n.format("menu.multiplayer", new Object[0])));
      if(Reflector.GuiModList_Constructor.exists()) {
         this.buttonList.add(this.realmsButton = new GuiButton(14, this.width / 2 + 2, p_73969_1_ + p_73969_2_ * 2, 98, 20, I18n.format("menu.online", new Object[0]).replace("Minecraft", "").trim()));
         this.buttonList.add(this.modButton = new GuiButton(6, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, 98, 20, I18n.format("fml.menu.mods", new Object[0])));
      } else {
         this.buttonList.add(this.realmsButton = new GuiButton(14, this.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, I18n.format("menu.online", new Object[0])));
      }

   }

   public void addDemoButtons(int p_73972_1_, int p_73972_2_) {
      this.buttonList.add(new GuiButton(11, this.width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo", new Object[0])));
      this.buttonList.add(this.buttonResetDemo = new GuiButton(12, this.width / 2 - 100, p_73972_1_ + p_73972_2_ * 1, I18n.format("menu.resetdemo", new Object[0])));
      ISaveFormat var3 = this.mc.getSaveLoader();
      WorldInfo var4 = var3.getWorldInfo("Demo_World");
      if(var4 == null) {
         this.buttonResetDemo.enabled = false;
      }

   }

   public void actionPerformed(GuiButton button) throws IOException {
      if(button.id == 0) {
         this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
      }

      if(button.id == 5) {
         this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
      }

      if(button.id == 1) {
         this.mc.displayGuiScreen(new GuiSelectWorld(this));
      }

      if(button.id == 2) {
         this.mc.displayGuiScreen(new GuiMultiplayer(this));
      }

      if(button.id == 14 && this.realmsButton.visible) {
         this.a();
      }

      if(button.id == 4) {
         this.mc.shutdown();
      }

      if(button.id == 6 && Reflector.GuiModList_Constructor.exists()) {
         this.mc.displayGuiScreen((GuiScreen)Reflector.newInstance(Reflector.GuiModList_Constructor, new Object[]{this}));
      }

      if(button.id == 11) {
         this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
      }

      if(button.id == 12) {
         ISaveFormat var2 = this.mc.getSaveLoader();
         WorldInfo var3 = var2.getWorldInfo("Demo_World");
         if(var3 != null) {
            GuiYesNo var4 = GuiSelectWorld.makeDeleteWorldYesNo(this, var3.getWorldName(), 12);
            this.mc.displayGuiScreen(var4);
         }
      }

   }

   public void a() {
      RealmsBridge var1 = new RealmsBridge();
      var1.switchToRealms(this);
   }

   public void confirmClicked(boolean result, int id) {
      if(result && id == 12) {
         ISaveFormat var52 = this.mc.getSaveLoader();
         var52.flushCache();
         var52.deleteWorldDirectory("Demo_World");
         this.mc.displayGuiScreen(this);
      } else if(id == 13) {
         if(result) {
            try {
               Class var5 = Class.forName("java.awt.Desktop");
               Object var4 = var5.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
               var5.getMethod("browse", new Class[]{URI.class}).invoke(var4, new Object[]{new URI(this.openGLWarningLink)});
            } catch (Throwable var51) {
               logger.error("Couldn\'t open link", var51);
            }
         }

         this.mc.displayGuiScreen(this);
      }

   }

   public void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
      Tessellator var4 = Tessellator.getInstance();
      WorldRenderer var5 = var4.getWorldRenderer();
      GlStateManager.matrixMode(5889);
      GlStateManager.pushMatrix();
      GlStateManager.loadIdentity();
      Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
      GlStateManager.matrixMode(5888);
      GlStateManager.pushMatrix();
      GlStateManager.loadIdentity();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.disableCull();
      GlStateManager.depthMask(false);
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      byte var6 = 8;
      int blurCount1 = 64;
      CustomPanoramaProperties cpp = CustomPanorama.getCustomPanoramaProperties();
      if(cpp != null) {
         blurCount1 = cpp.getBlur1();
      }

      for(int var7 = 0; var7 < blurCount1; ++var7) {
         GlStateManager.pushMatrix();
         float var8 = ((float)(var7 % var6) / (float)var6 - 0.5F) / 64.0F;
         float var9 = ((float)(var7 / var6) / (float)var6 - 0.5F) / 64.0F;
         float var10 = 0.0F;
         GlStateManager.translate(var8, var9, var10);
         GlStateManager.rotate(MathHelper.sin(((float)this.panoramaTimer + p_73970_3_) / 400.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate(-((float)this.panoramaTimer + p_73970_3_) * 0.1F, 0.0F, 1.0F, 0.0F);

         for(int var11 = 0; var11 < 6; ++var11) {
            GlStateManager.pushMatrix();
            if(var11 == 1) {
               GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
            }

            if(var11 == 2) {
               GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            }

            if(var11 == 3) {
               GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
            }

            if(var11 == 4) {
               GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            }

            if(var11 == 5) {
               GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            }

            ResourceLocation[] panoramaLocations = titlePanoramaPaths;
            if(cpp != null) {
               panoramaLocations = cpp.getPanoramaLocations();
            }

            this.mc.getTextureManager().bindTexture(panoramaLocations[var11]);
            var5.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            int var12 = 255 / (var7 + 1);
            float var13 = 0.0F;
            var5.pos(-1.0D, -1.0D, 1.0D).tex(0.0D, 0.0D).color(255, 255, 255, var12).endVertex();
            var5.pos(1.0D, -1.0D, 1.0D).tex(1.0D, 0.0D).color(255, 255, 255, var12).endVertex();
            var5.pos(1.0D, 1.0D, 1.0D).tex(1.0D, 1.0D).color(255, 255, 255, var12).endVertex();
            var5.pos(-1.0D, 1.0D, 1.0D).tex(0.0D, 1.0D).color(255, 255, 255, var12).endVertex();
            var4.draw();
            GlStateManager.popMatrix();
         }

         GlStateManager.popMatrix();
         GlStateManager.colorMask(true, true, true, false);
      }

      var5.setTranslation(0.0D, 0.0D, 0.0D);
      GlStateManager.colorMask(true, true, true, true);
      GlStateManager.matrixMode(5889);
      GlStateManager.popMatrix();
      GlStateManager.matrixMode(5888);
      GlStateManager.popMatrix();
      GlStateManager.depthMask(true);
      GlStateManager.enableCull();
      GlStateManager.enableDepth();
   }

   public void rotateAndBlurSkybox(float p_73968_1_) {
      this.mc.getTextureManager().bindTexture(this.backgroundTexture);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.colorMask(true, true, true, false);
      Tessellator var2 = Tessellator.getInstance();
      WorldRenderer var3 = var2.getWorldRenderer();
      var3.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      GlStateManager.disableAlpha();
      byte var4 = 3;
      int blurCount2 = 3;
      CustomPanoramaProperties cpp = CustomPanorama.getCustomPanoramaProperties();
      if(cpp != null) {
         blurCount2 = cpp.getBlur2();
      }

      for(int var5 = 0; var5 < blurCount2; ++var5) {
         float var6 = 1.0F / (float)(var5 + 1);
         int var7 = this.width;
         int var8 = this.height;
         float var9 = (float)(var5 - var4 / 2) / 256.0F;
         var3.pos((double)var7, (double)var8, (double)this.zLevel).tex((double)(0.0F + var9), 1.0D).color(1.0F, 1.0F, 1.0F, var6).endVertex();
         var3.pos((double)var7, 0.0D, (double)this.zLevel).tex((double)(1.0F + var9), 1.0D).color(1.0F, 1.0F, 1.0F, var6).endVertex();
         var3.pos(0.0D, 0.0D, (double)this.zLevel).tex((double)(1.0F + var9), 0.0D).color(1.0F, 1.0F, 1.0F, var6).endVertex();
         var3.pos(0.0D, (double)var8, (double)this.zLevel).tex((double)(0.0F + var9), 0.0D).color(1.0F, 1.0F, 1.0F, var6).endVertex();
      }

      var2.draw();
      GlStateManager.enableAlpha();
      GlStateManager.colorMask(true, true, true, true);
   }

   public void renderSkybox(int p_73971_1_, int p_73971_2_, float p_73971_3_) {
      this.mc.getFramebuffer().unbindFramebuffer();
      GlStateManager.viewport(0, 0, 256, 256);
      this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
      this.rotateAndBlurSkybox(p_73971_3_);
      int blurCount3 = 3;
      CustomPanoramaProperties cpp = CustomPanorama.getCustomPanoramaProperties();
      if(cpp != null) {
         blurCount3 = cpp.getBlur3();
      }

      for(int var4 = 0; var4 < blurCount3; ++var4) {
         this.rotateAndBlurSkybox(p_73971_3_);
         this.rotateAndBlurSkybox(p_73971_3_);
      }

      this.mc.getFramebuffer().bindFramebuffer(true);
      GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
      float var13 = this.width > this.height?120.0F / (float)this.width:120.0F / (float)this.height;
      float var5 = (float)this.height * var13 / 256.0F;
      float var6 = (float)this.width * var13 / 256.0F;
      int var7 = this.width;
      int var8 = this.height;
      Tessellator var9 = Tessellator.getInstance();
      WorldRenderer var10 = var9.getWorldRenderer();
      var10.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
      var10.pos(0.0D, (double)var8, (double)this.zLevel).tex((double)(0.5F - var5), (double)(0.5F + var6)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
      var10.pos((double)var7, (double)var8, (double)this.zLevel).tex((double)(0.5F - var5), (double)(0.5F - var6)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
      var10.pos((double)var7, 0.0D, (double)this.zLevel).tex((double)(0.5F + var5), (double)(0.5F - var6)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
      var10.pos(0.0D, 0.0D, (double)this.zLevel).tex((double)(0.5F + var5), (double)(0.5F + var6)).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
      var9.draw();
   }

   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
      GlStateManager.disableAlpha();
      this.renderSkybox(mouseX, mouseY, partialTicks);
      GlStateManager.enableAlpha();
      Tessellator var4 = Tessellator.getInstance();
      WorldRenderer var5 = var4.getWorldRenderer();
      short var6 = 274;
      int var7 = this.width / 2 - var6 / 2;
      byte var8 = 30;
      int overlay1Top = -2130706433;
      int overlay1Bottom = 16777215;
      int overlay2Top = 0;
      int overlay2Bottom = Integer.MIN_VALUE;
      CustomPanoramaProperties cpp = CustomPanorama.getCustomPanoramaProperties();
      if(cpp != null) {
         overlay1Top = cpp.getOverlay1Top();
         overlay1Bottom = cpp.getOverlay1Bottom();
         overlay2Top = cpp.getOverlay2Top();
         overlay2Bottom = cpp.getOverlay2Bottom();
      }

      this.drawGradientRect(0, 0, this.width, this.height, overlay1Top, overlay1Bottom);
      this.drawGradientRect(0, 0, this.width, this.height, overlay2Top, overlay2Bottom);
      this.mc.getTextureManager().bindTexture(minecraftTitleTextures);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      if((double)this.updateCounter < 1.0E-4D) {
         this.drawTexturedModalRect(var7 + 0, var8 + 0, 0, 0, 99, 44);
         this.drawTexturedModalRect(var7 + 99, var8 + 0, 129, 0, 27, 44);
         this.drawTexturedModalRect(var7 + 99 + 26, var8 + 0, 126, 0, 3, 44);
         this.drawTexturedModalRect(var7 + 99 + 26 + 3, var8 + 0, 99, 0, 26, 44);
         this.drawTexturedModalRect(var7 + 155, var8 + 0, 0, 45, 155, 44);
      } else {
         this.drawTexturedModalRect(var7 + 0, var8 + 0, 0, 0, 155, 44);
         this.drawTexturedModalRect(var7 + 155, var8 + 0, 0, 45, 155, 44);
      }

      GlStateManager.pushMatrix();
      GlStateManager.translate((float)(this.width / 2 + 90), 70.0F, 0.0F);
      GlStateManager.rotate(-20.0F, 0.0F, 0.0F, 1.0F);
      float var9 = 1.8F - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() % 1000L) / 1000.0F * 3.1415927F * 2.0F) * 0.1F);
      var9 = var9 * 100.0F / (float)(this.fontRendererObj.getStringWidth(this.splashText) + 32);
      GlStateManager.scale(var9, var9, var9);
      this.drawCenteredString(this.fontRendererObj, this.splashText, 0, -8, -256);
      GlStateManager.popMatrix();
      String var10 = "Minecraft 1.8.9";
      if(this.mc.isDemo()) {
         var10 = var10 + " Demo";
      }

      if(Reflector.FMLCommonHandler_getBrandings.exists()) {
         Object var11 = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
         List brandings = Lists.reverse((List)Reflector.call(var11, Reflector.FMLCommonHandler_getBrandings, new Object[]{Boolean.valueOf(true)}));

         for(int brdline = 0; brdline < brandings.size(); ++brdline) {
            String brd = (String)brandings.get(brdline);
            if(!Strings.isNullOrEmpty(brd)) {
               this.drawString(this.fontRendererObj, brd, 2, this.height - (10 + brdline * (this.fontRendererObj.FONT_HEIGHT + 1)), 16777215);
            }
         }

         if(Reflector.ForgeHooksClient_renderMainMenu.exists()) {
            Reflector.call(Reflector.ForgeHooksClient_renderMainMenu, new Object[]{this, this.fontRendererObj, Integer.valueOf(this.width), Integer.valueOf(this.height)});
         }
      } else {
         this.drawString(this.fontRendererObj, var10, 2, this.height - 10, -1);
      }

      String var20 = "Copyright Mojang AB. Do not distribute!";
      this.drawString(this.fontRendererObj, var20, this.width - this.fontRendererObj.getStringWidth(var20) - 2, this.height - 10, -1);
      if(this.openGLWarning1 != null && this.openGLWarning1.length() > 0) {
         drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1, 1428160512);
         this.drawString(this.fontRendererObj, this.openGLWarning1, this.field_92022_t, this.field_92021_u, -1);
         this.drawString(this.fontRendererObj, this.openGLWarning2, (this.width - this.field_92024_r) / 2, ((GuiButton)this.buttonList.get(0)).yPosition - 12, -1);
      }

      super.drawScreen(mouseX, mouseY, partialTicks);
      if(this.modUpdateNotification != null) {
         this.modUpdateNotification.drawScreen(mouseX, mouseY, partialTicks);
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      Object var4 = this.threadLock;
      Object var5 = this.threadLock;
      synchronized(this.threadLock) {
         if(this.openGLWarning1.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v && mouseY >= this.field_92021_u && mouseY <= this.field_92019_w) {
            GuiConfirmOpenLink var51 = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
            var51.disableSecurityWarning();
            this.mc.displayGuiScreen(var51);
         }

      }
   }

}
