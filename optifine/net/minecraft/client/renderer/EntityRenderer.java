package net.minecraft.client.renderer;

import com.google.common.base.Predicates;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.EntityRenderer$1;
import net.minecraft.client.renderer.EntityRenderer$2;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MouseFilter;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos.MutableBlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.biome.BiomeGenBase;
import optifine.Config;
import optifine.CustomColors;
import optifine.Lagometer;
import optifine.RandomMobs;
import optifine.Reflector;
import optifine.ReflectorForge;
import optifine.TextureUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Project;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersRender;

public class EntityRenderer implements IResourceManagerReloadListener {

   public static final Logger logger = LogManager.getLogger();
   public static final ResourceLocation locationRainPng = new ResourceLocation("textures/environment/rain.png");
   public static final ResourceLocation locationSnowPng = new ResourceLocation("textures/environment/snow.png");
   public static boolean anaglyphEnable;
   public static int anaglyphField;
   public Minecraft mc;
   public final IResourceManager resourceManager;
   public Random random = new Random();
   public float farPlaneDistance;
   public ItemRenderer itemRenderer;
   public final MapItemRenderer theMapItemRenderer;
   public int rendererUpdateCount;
   public Entity pointedEntity;
   public MouseFilter mouseFilterXAxis = new MouseFilter();
   public MouseFilter mouseFilterYAxis = new MouseFilter();
   public float thirdPersonDistance = 4.0F;
   public float thirdPersonDistanceTemp = 4.0F;
   public float smoothCamYaw;
   public float smoothCamPitch;
   public float smoothCamFilterX;
   public float smoothCamFilterY;
   public float smoothCamPartialTicks;
   public float fovModifierHand;
   public float fovModifierHandPrev;
   public float bossColorModifier;
   public float bossColorModifierPrev;
   public boolean cloudFog;
   public boolean renderHand = true;
   public boolean drawBlockOutline = true;
   public long prevFrameTime = Minecraft.getSystemTime();
   public long renderEndNanoTime;
   public final DynamicTexture lightmapTexture;
   public final int[] lightmapColors;
   public final ResourceLocation locationLightMap;
   public boolean lightmapUpdateNeeded;
   public float torchFlickerX;
   public float torchFlickerDX;
   public int rainSoundCounter;
   public float[] rainXCoords = new float[1024];
   public float[] rainYCoords = new float[1024];
   public FloatBuffer fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
   public float fogColorRed;
   public float fogColorGreen;
   public float fogColorBlue;
   public float fogColor2;
   public float fogColor1;
   public int debugViewDirection = 0;
   public boolean debugView = false;
   public double cameraZoom = 1.0D;
   public double cameraYaw;
   public double cameraPitch;
   public ShaderGroup theShaderGroup;
   public static final ResourceLocation[] shaderResourceLocations = new ResourceLocation[]{new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"), new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"), new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"), new ResourceLocation("shaders/post/color_convolve.json"), new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"), new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"), new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"), new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"), new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"), new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"), new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"), new ResourceLocation("shaders/post/antialias.json"), new ResourceLocation("shaders/post/creeper.json"), new ResourceLocation("shaders/post/spider.json")};
   public static final int shaderCount = shaderResourceLocations.length;
   public int shaderIndex;
   public boolean useShader;
   public int frameCount;
   public static final String __OBFID = "CL_00000947";
   public boolean initialized = false;
   public World updatedWorld = null;
   public boolean showDebugInfo = false;
   public boolean fogStandard = false;
   public float clipDistance = 128.0F;
   public long lastServerTime = 0L;
   public int lastServerTicks = 0;
   public int serverWaitTime = 0;
   public int serverWaitTimeCurrent = 0;
   public float avgServerTimeDiff = 0.0F;
   public float avgServerTickDiff = 0.0F;
   public long lastErrorCheckTimeMs = 0L;
   public ShaderGroup[] fxaaShaders = new ShaderGroup[10];


   public EntityRenderer(Minecraft mcIn, IResourceManager p_i45076_2_) {
      this.shaderIndex = shaderCount;
      this.useShader = false;
      this.frameCount = 0;
      this.mc = mcIn;
      this.resourceManager = p_i45076_2_;
      this.itemRenderer = mcIn.getItemRenderer();
      this.theMapItemRenderer = new MapItemRenderer(mcIn.getTextureManager());
      this.lightmapTexture = new DynamicTexture(16, 16);
      this.locationLightMap = mcIn.getTextureManager().getDynamicTextureLocation("lightMap", this.lightmapTexture);
      this.lightmapColors = this.lightmapTexture.getTextureData();
      this.theShaderGroup = null;

      for(int var3 = 0; var3 < 32; ++var3) {
         for(int var4 = 0; var4 < 32; ++var4) {
            float var5 = (float)(var4 - 16);
            float var6 = (float)(var3 - 16);
            float var7 = MathHelper.sqrt_float(var5 * var5 + var6 * var6);
            this.rainXCoords[var3 << 5 | var4] = -var6 / var7;
            this.rainYCoords[var3 << 5 | var4] = var5 / var7;
         }
      }

   }

   public boolean isShaderActive() {
      return OpenGlHelper.shadersSupported && this.theShaderGroup != null;
   }

   public void stopUseShader() {
      if(this.theShaderGroup != null) {
         this.theShaderGroup.deleteShaderGroup();
      }

      this.theShaderGroup = null;
      this.shaderIndex = shaderCount;
   }

   public void switchUseShader() {
      this.useShader = !this.useShader;
   }

   public void loadEntityShader(Entity p_175066_1_) {
      if(OpenGlHelper.shadersSupported) {
         if(this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
         }

         this.theShaderGroup = null;
         if(p_175066_1_ instanceof EntityCreeper) {
            this.loadShader(new ResourceLocation("shaders/post/creeper.json"));
         } else if(p_175066_1_ instanceof EntitySpider) {
            this.loadShader(new ResourceLocation("shaders/post/spider.json"));
         } else if(p_175066_1_ instanceof EntityEnderman) {
            this.loadShader(new ResourceLocation("shaders/post/invert.json"));
         } else if(Reflector.ForgeHooksClient_loadEntityShader.exists()) {
            Reflector.call(Reflector.ForgeHooksClient_loadEntityShader, new Object[]{p_175066_1_, this});
         }
      }

   }

   public void activateNextShader() {
      if(OpenGlHelper.shadersSupported && this.mc.getRenderViewEntity() instanceof EntityPlayer) {
         if(this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
         }

         this.shaderIndex = (this.shaderIndex + 1) % (shaderResourceLocations.length + 1);
         if(this.shaderIndex != shaderCount) {
            this.loadShader(shaderResourceLocations[this.shaderIndex]);
         } else {
            this.theShaderGroup = null;
         }
      }

   }

   public void loadShader(ResourceLocation p_175069_1_) {
      if(OpenGlHelper.isFramebufferEnabled()) {
         try {
            this.theShaderGroup = new ShaderGroup(this.mc.getTextureManager(), this.resourceManager, this.mc.getFramebuffer(), p_175069_1_);
            this.theShaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
            this.useShader = true;
         } catch (IOException var3) {
            logger.warn("Failed to load shader: " + p_175069_1_, var3);
            this.shaderIndex = shaderCount;
            this.useShader = false;
         } catch (JsonSyntaxException var4) {
            logger.warn("Failed to load shader: " + p_175069_1_, var4);
            this.shaderIndex = shaderCount;
            this.useShader = false;
         }

      }
   }

   public void onResourceManagerReload(IResourceManager resourceManager) {
      if(this.theShaderGroup != null) {
         this.theShaderGroup.deleteShaderGroup();
      }

      this.theShaderGroup = null;
      if(this.shaderIndex != shaderCount) {
         this.loadShader(shaderResourceLocations[this.shaderIndex]);
      } else {
         this.loadEntityShader(this.mc.getRenderViewEntity());
      }

   }

   public void updateRenderer() {
      if(OpenGlHelper.shadersSupported && ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
         ShaderLinkHelper.setNewStaticShaderLinkHelper();
      }

      this.updateFovModifierHand();
      this.updateTorchFlicker();
      this.fogColor2 = this.fogColor1;
      this.thirdPersonDistanceTemp = this.thirdPersonDistance;
      float var1;
      float var2;
      if(this.mc.gameSettings.smoothCamera) {
         var1 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
         var2 = var1 * var1 * var1 * 8.0F;
         this.smoothCamFilterX = this.mouseFilterXAxis.smooth(this.smoothCamYaw, 0.05F * var2);
         this.smoothCamFilterY = this.mouseFilterYAxis.smooth(this.smoothCamPitch, 0.05F * var2);
         this.smoothCamPartialTicks = 0.0F;
         this.smoothCamYaw = 0.0F;
         this.smoothCamPitch = 0.0F;
      } else {
         this.smoothCamFilterX = 0.0F;
         this.smoothCamFilterY = 0.0F;
         this.mouseFilterXAxis.reset();
         this.mouseFilterYAxis.reset();
      }

      if(this.mc.getRenderViewEntity() == null) {
         this.mc.setRenderViewEntity(this.mc.thePlayer);
      }

      Entity viewEntity = this.mc.getRenderViewEntity();
      double vx = viewEntity.posX;
      double vy = viewEntity.posY + (double)viewEntity.getEyeHeight();
      double vz = viewEntity.posZ;
      var1 = this.mc.theWorld.getLightBrightness(new BlockPos(vx, vy, vz));
      var2 = (float)this.mc.gameSettings.renderDistanceChunks / 16.0F;
      var2 = MathHelper.clamp_float(var2, 0.0F, 1.0F);
      float var3 = var1 * (1.0F - var2) + var2;
      this.fogColor1 += (var3 - this.fogColor1) * 0.1F;
      ++this.rendererUpdateCount;
      this.itemRenderer.updateEquippedItem();
      this.addRainParticles();
      this.bossColorModifierPrev = this.bossColorModifier;
      if(BossStatus.hasColorModifier) {
         this.bossColorModifier += 0.05F;
         if(this.bossColorModifier > 1.0F) {
            this.bossColorModifier = 1.0F;
         }

         BossStatus.hasColorModifier = false;
      } else if(this.bossColorModifier > 0.0F) {
         this.bossColorModifier -= 0.0125F;
      }

   }

   public ShaderGroup getShaderGroup() {
      return this.theShaderGroup;
   }

   public void updateShaderGroupSize(int p_147704_1_, int p_147704_2_) {
      if(OpenGlHelper.shadersSupported) {
         if(this.theShaderGroup != null) {
            this.theShaderGroup.createBindFramebuffers(p_147704_1_, p_147704_2_);
         }

         this.mc.renderGlobal.createBindEntityOutlineFbs(p_147704_1_, p_147704_2_);
      }

   }

   public void getMouseOver(float p_78473_1_) {
      Entity var2 = this.mc.getRenderViewEntity();
      if(var2 != null && this.mc.theWorld != null) {
         this.mc.mcProfiler.startSection("pick");
         this.mc.pointedEntity = null;
         double var3 = (double)this.mc.playerController.getBlockReachDistance();
         this.mc.objectMouseOver = var2.rayTrace(var3, p_78473_1_);
         double var5 = var3;
         Vec3 var7 = var2.getPositionEyes(p_78473_1_);
         boolean var8 = false;
         boolean var9 = true;
         if(this.mc.playerController.extendedReach()) {
            var3 = 6.0D;
            var5 = 6.0D;
         } else {
            if(var3 > 3.0D) {
               var8 = true;
            }

            var3 = var3;
         }

         if(this.mc.objectMouseOver != null) {
            var5 = this.mc.objectMouseOver.hitVec.distanceTo(var7);
         }

         Vec3 var10 = var2.getLook(p_78473_1_);
         Vec3 var11 = var7.addVector(var10.xCoord * var3, var10.yCoord * var3, var10.zCoord * var3);
         this.pointedEntity = null;
         Vec3 var12 = null;
         float var13 = 1.0F;
         List var14 = this.mc.theWorld.getEntitiesInAABBexcluding(var2, var2.getEntityBoundingBox().addCoord(var10.xCoord * var3, var10.yCoord * var3, var10.zCoord * var3).expand((double)var13, (double)var13, (double)var13), Predicates.and(EntitySelectors.NOT_SPECTATING, new EntityRenderer$1(this)));
         double var15 = var5;

         for(int var17 = 0; var17 < var14.size(); ++var17) {
            Entity var18 = (Entity)var14.get(var17);
            float var19 = var18.getCollisionBorderSize();
            AxisAlignedBB var20 = var18.getEntityBoundingBox().expand((double)var19, (double)var19, (double)var19);
            MovingObjectPosition var21 = var20.calculateIntercept(var7, var11);
            if(var20.isVecInside(var7)) {
               if(var15 >= 0.0D) {
                  this.pointedEntity = var18;
                  var12 = var21 == null?var7:var21.hitVec;
                  var15 = 0.0D;
               }
            } else if(var21 != null) {
               double var22 = var7.distanceTo(var21.hitVec);
               if(var22 < var15 || var15 == 0.0D) {
                  boolean canRiderInteract = false;
                  if(Reflector.ForgeEntity_canRiderInteract.exists()) {
                     canRiderInteract = Reflector.callBoolean(var18, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                  }

                  if(var18 == var2.ridingEntity && !canRiderInteract) {
                     if(var15 == 0.0D) {
                        this.pointedEntity = var18;
                        var12 = var21.hitVec;
                     }
                  } else {
                     this.pointedEntity = var18;
                     var12 = var21.hitVec;
                     var15 = var22;
                  }
               }
            }
         }

         if(this.pointedEntity != null && var8 && var7.distanceTo(var12) > 3.0D) {
            this.pointedEntity = null;
            this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectType.MISS, var12, (EnumFacing)null, new BlockPos(var12));
         }

         if(this.pointedEntity != null && (var15 < var5 || this.mc.objectMouseOver == null)) {
            this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, var12);
            if(this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
               this.mc.pointedEntity = this.pointedEntity;
            }
         }

         this.mc.mcProfiler.endSection();
      }

   }

   public void updateFovModifierHand() {
      float var1 = 1.0F;
      if(this.mc.getRenderViewEntity() instanceof AbstractClientPlayer) {
         AbstractClientPlayer var2 = (AbstractClientPlayer)this.mc.getRenderViewEntity();
         var1 = var2.getFovModifier();
      }

      this.fovModifierHandPrev = this.fovModifierHand;
      this.fovModifierHand += (var1 - this.fovModifierHand) * 0.5F;
      if(this.fovModifierHand > 1.5F) {
         this.fovModifierHand = 1.5F;
      }

      if(this.fovModifierHand < 0.1F) {
         this.fovModifierHand = 0.1F;
      }

   }

   public float getFOVModifier(float partialTicks, boolean p_78481_2_) {
      if(this.debugView) {
         return 90.0F;
      } else {
         Entity var3 = this.mc.getRenderViewEntity();
         float var4 = 70.0F;
         if(p_78481_2_) {
            var4 = this.mc.gameSettings.fovSetting;
            if(Config.isDynamicFov()) {
               var4 *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * partialTicks;
            }
         }

         boolean zoomActive = false;
         if(this.mc.currentScreen == null) {
            GameSettings var10000 = this.mc.gameSettings;
            zoomActive = GameSettings.isKeyDown(this.mc.gameSettings.ofKeyBindZoom);
         }

         if(zoomActive) {
            if(!Config.zoomMode) {
               Config.zoomMode = true;
               this.mc.gameSettings.smoothCamera = true;
            }

            if(Config.zoomMode) {
               var4 /= 4.0F;
            }
         } else if(Config.zoomMode) {
            Config.zoomMode = false;
            this.mc.gameSettings.smoothCamera = false;
            this.mouseFilterXAxis = new MouseFilter();
            this.mouseFilterYAxis = new MouseFilter();
            this.mc.renderGlobal.displayListEntitiesDirty = true;
         }

         if(var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).getHealth() <= 0.0F) {
            float var6 = (float)((EntityLivingBase)var3).deathTime + partialTicks;
            var4 /= (1.0F - 500.0F / (var6 + 500.0F)) * 2.0F + 1.0F;
         }

         Block var61 = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, var3, partialTicks);
         if(var61.getMaterial() == Material.water) {
            var4 = var4 * 60.0F / 70.0F;
         }

         return Reflector.ForgeHooksClient_getFOVModifier.exists()?Reflector.callFloat(Reflector.ForgeHooksClient_getFOVModifier, new Object[]{this, var3, var61, Float.valueOf(partialTicks), Float.valueOf(var4)}):var4;
      }
   }

   public void hurtCameraEffect(float p_78482_1_) {
      if(this.mc.getRenderViewEntity() instanceof EntityLivingBase) {
         EntityLivingBase var2 = (EntityLivingBase)this.mc.getRenderViewEntity();
         float var3 = (float)var2.hurtTime - p_78482_1_;
         float var4;
         if(var2.getHealth() <= 0.0F) {
            var4 = (float)var2.deathTime + p_78482_1_;
            GlStateManager.rotate(40.0F - 8000.0F / (var4 + 200.0F), 0.0F, 0.0F, 1.0F);
         }

         if(var3 < 0.0F) {
            return;
         }

         var3 /= (float)var2.maxHurtTime;
         var3 = MathHelper.sin(var3 * var3 * var3 * var3 * 3.1415927F);
         var4 = var2.attackedAtYaw;
         GlStateManager.rotate(-var4, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(-var3 * 14.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager.rotate(var4, 0.0F, 1.0F, 0.0F);
      }

   }

   public void setupViewBobbing(float p_78475_1_) {
      if(this.mc.getRenderViewEntity() instanceof EntityPlayer) {
         EntityPlayer var2 = (EntityPlayer)this.mc.getRenderViewEntity();
         float var3 = var2.distanceWalkedModified - var2.prevDistanceWalkedModified;
         float var4 = -(var2.distanceWalkedModified + var3 * p_78475_1_);
         float var5 = var2.prevCameraYaw + (var2.cameraYaw - var2.prevCameraYaw) * p_78475_1_;
         float var6 = var2.prevCameraPitch + (var2.cameraPitch - var2.prevCameraPitch) * p_78475_1_;
         GlStateManager.translate(MathHelper.sin(var4 * 3.1415927F) * var5 * 0.5F, -Math.abs(MathHelper.cos(var4 * 3.1415927F) * var5), 0.0F);
         GlStateManager.rotate(MathHelper.sin(var4 * 3.1415927F) * var5 * 3.0F, 0.0F, 0.0F, 1.0F);
         GlStateManager.rotate(Math.abs(MathHelper.cos(var4 * 3.1415927F - 0.2F) * var5) * 5.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate(var6, 1.0F, 0.0F, 0.0F);
      }

   }

   public void orientCamera(float p_78467_1_) {
      Entity var2 = this.mc.getRenderViewEntity();
      float var3 = var2.getEyeHeight();
      double var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * (double)p_78467_1_;
      double var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * (double)p_78467_1_ + (double)var3;
      double var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double)p_78467_1_;
      float yaw;
      float pitch;
      if(var2 instanceof EntityLivingBase && ((EntityLivingBase)var2).isPlayerSleeping()) {
         var3 = (float)((double)var3 + 1.0D);
         GlStateManager.translate(0.0F, 0.3F, 0.0F);
         if(!this.mc.gameSettings.debugCamEnable) {
            BlockPos var28 = new BlockPos(var2);
            IBlockState partialTicks = this.mc.theWorld.getBlockState(var28);
            Block var33 = partialTicks.getBlock();
            if(Reflector.ForgeHooksClient_orientBedCamera.exists()) {
               Reflector.callVoid(Reflector.ForgeHooksClient_orientBedCamera, new Object[]{this.mc.theWorld, var28, partialTicks, var2});
            } else if(var33 == Blocks.bed) {
               int var34 = ((EnumFacing)partialTicks.getValue(BlockBed.FACING)).getHorizontalIndex();
               GlStateManager.rotate((float)(var34 * 90), 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.rotate(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * p_78467_1_ + 180.0F, 0.0F, -1.0F, 0.0F);
            GlStateManager.rotate(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * p_78467_1_, -1.0F, 0.0F, 0.0F);
         }
      } else if(this.mc.gameSettings.thirdPersonView > 0) {
         double var10 = (double)(this.thirdPersonDistanceTemp + (this.thirdPersonDistance - this.thirdPersonDistanceTemp) * p_78467_1_);
         if(this.mc.gameSettings.debugCamEnable) {
            GlStateManager.translate(0.0F, 0.0F, (float)(-var10));
         } else {
            yaw = var2.rotationYaw;
            pitch = var2.rotationPitch;
            if(this.mc.gameSettings.thirdPersonView == 2) {
               pitch += 180.0F;
            }

            double var14 = (double)(-MathHelper.sin(yaw / 180.0F * 3.1415927F) * MathHelper.cos(pitch / 180.0F * 3.1415927F)) * var10;
            double var16 = (double)(MathHelper.cos(yaw / 180.0F * 3.1415927F) * MathHelper.cos(pitch / 180.0F * 3.1415927F)) * var10;
            double var18 = (double)(-MathHelper.sin(pitch / 180.0F * 3.1415927F)) * var10;

            for(int var20 = 0; var20 < 8; ++var20) {
               float var21 = (float)((var20 & 1) * 2 - 1);
               float var22 = (float)((var20 >> 1 & 1) * 2 - 1);
               float var23 = (float)((var20 >> 2 & 1) * 2 - 1);
               var21 *= 0.1F;
               var22 *= 0.1F;
               var23 *= 0.1F;
               MovingObjectPosition var24 = this.mc.theWorld.rayTraceBlocks(new Vec3(var4 + (double)var21, var6 + (double)var22, var8 + (double)var23), new Vec3(var4 - var14 + (double)var21 + (double)var23, var6 - var18 + (double)var22, var8 - var16 + (double)var23));
               if(var24 != null) {
                  double var25 = var24.hitVec.distanceTo(new Vec3(var4, var6, var8));
                  if(var25 < var10) {
                     var10 = var25;
                  }
               }
            }

            if(this.mc.gameSettings.thirdPersonView == 2) {
               GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            }

            GlStateManager.rotate(var2.rotationPitch - pitch, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(var2.rotationYaw - yaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, 0.0F, (float)(-var10));
            GlStateManager.rotate(yaw - var2.rotationYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(pitch - var2.rotationPitch, 1.0F, 0.0F, 0.0F);
         }
      } else {
         GlStateManager.translate(0.0F, 0.0F, -0.1F);
      }

      if(Reflector.EntityViewRenderEvent_CameraSetup_Constructor.exists()) {
         if(!this.mc.gameSettings.debugCamEnable) {
            yaw = var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * p_78467_1_ + 180.0F;
            pitch = var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * p_78467_1_;
            float roll = 0.0F;
            if(var2 instanceof EntityAnimal) {
               EntityAnimal block = (EntityAnimal)var2;
               yaw = block.prevRotationYawHead + (block.rotationYawHead - block.prevRotationYawHead) * p_78467_1_ + 180.0F;
            }

            Block var35 = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, var2, p_78467_1_);
            Object event = Reflector.newInstance(Reflector.EntityViewRenderEvent_CameraSetup_Constructor, new Object[]{this, var2, var35, Float.valueOf(p_78467_1_), Float.valueOf(yaw), Float.valueOf(pitch), Float.valueOf(roll)});
            Reflector.postForgeBusEvent(event);
            roll = Reflector.getFieldValueFloat(event, Reflector.EntityViewRenderEvent_CameraSetup_roll, roll);
            pitch = Reflector.getFieldValueFloat(event, Reflector.EntityViewRenderEvent_CameraSetup_pitch, pitch);
            yaw = Reflector.getFieldValueFloat(event, Reflector.EntityViewRenderEvent_CameraSetup_yaw, yaw);
            GlStateManager.rotate(roll, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
         }
      } else if(!this.mc.gameSettings.debugCamEnable) {
         GlStateManager.rotate(var2.prevRotationPitch + (var2.rotationPitch - var2.prevRotationPitch) * p_78467_1_, 1.0F, 0.0F, 0.0F);
         if(var2 instanceof EntityAnimal) {
            EntityAnimal var32 = (EntityAnimal)var2;
            GlStateManager.rotate(var32.prevRotationYawHead + (var32.rotationYawHead - var32.prevRotationYawHead) * p_78467_1_ + 180.0F, 0.0F, 1.0F, 0.0F);
         } else {
            GlStateManager.rotate(var2.prevRotationYaw + (var2.rotationYaw - var2.prevRotationYaw) * p_78467_1_ + 180.0F, 0.0F, 1.0F, 0.0F);
         }
      }

      GlStateManager.translate(0.0F, -var3, 0.0F);
      var4 = var2.prevPosX + (var2.posX - var2.prevPosX) * (double)p_78467_1_;
      var6 = var2.prevPosY + (var2.posY - var2.prevPosY) * (double)p_78467_1_ + (double)var3;
      var8 = var2.prevPosZ + (var2.posZ - var2.prevPosZ) * (double)p_78467_1_;
      this.cloudFog = this.mc.renderGlobal.hasCloudFog(var4, var6, var8, p_78467_1_);
   }

   public void setupCameraTransform(float partialTicks, int pass) {
      this.farPlaneDistance = (float)(this.mc.gameSettings.renderDistanceChunks * 16);
      if(Config.isFogFancy()) {
         this.farPlaneDistance *= 0.95F;
      }

      if(Config.isFogFast()) {
         this.farPlaneDistance *= 0.83F;
      }

      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      float var3 = 0.07F;
      if(this.mc.gameSettings.anaglyph) {
         GlStateManager.translate((float)(-(pass * 2 - 1)) * var3, 0.0F, 0.0F);
      }

      this.clipDistance = this.farPlaneDistance * 2.0F;
      if(this.clipDistance < 173.0F) {
         this.clipDistance = 173.0F;
      }

      if(this.mc.theWorld.provider.getDimensionId() == 1) {
         this.clipDistance = 256.0F;
      }

      if(this.cameraZoom != 1.0D) {
         GlStateManager.translate((float)this.cameraYaw, (float)(-this.cameraPitch), 0.0F);
         GlStateManager.scale(this.cameraZoom, this.cameraZoom, 1.0D);
      }

      Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.clipDistance);
      GlStateManager.matrixMode(5888);
      GlStateManager.loadIdentity();
      if(this.mc.gameSettings.anaglyph) {
         GlStateManager.translate((float)(pass * 2 - 1) * 0.1F, 0.0F, 0.0F);
      }

      this.hurtCameraEffect(partialTicks);
      if(this.mc.gameSettings.viewBobbing) {
         this.setupViewBobbing(partialTicks);
      }

      float var4 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * partialTicks;
      if(var4 > 0.0F) {
         byte var5 = 20;
         if(this.mc.thePlayer.isPotionActive(Potion.confusion)) {
            var5 = 7;
         }

         float var6 = 5.0F / (var4 * var4 + 5.0F) - var4 * 0.04F;
         var6 *= var6;
         GlStateManager.rotate(((float)this.rendererUpdateCount + partialTicks) * (float)var5, 0.0F, 1.0F, 1.0F);
         GlStateManager.scale(1.0F / var6, 1.0F, 1.0F);
         GlStateManager.rotate(-((float)this.rendererUpdateCount + partialTicks) * (float)var5, 0.0F, 1.0F, 1.0F);
      }

      this.orientCamera(partialTicks);
      if(this.debugView) {
         switch(this.debugViewDirection) {
         case 0:
            GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
            break;
         case 1:
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            break;
         case 2:
            GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
            break;
         case 3:
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            break;
         case 4:
            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
         }
      }

   }

   public void renderHand(float partialTicks, int pass) {
      this.renderHand(partialTicks, pass, true, true, false);
   }

   public void renderHand(float p_78476_1_, int p_78476_2_, boolean renderItem, boolean renderOverlay, boolean renderTranslucent) {
      if(!this.debugView) {
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         float var3 = 0.07F;
         if(this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((float)(-(p_78476_2_ * 2 - 1)) * var3, 0.0F, 0.0F);
         }

         if(Config.isShaders()) {
            Shaders.applyHandDepth();
         }

         Project.gluPerspective(this.getFOVModifier(p_78476_1_, false), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.farPlaneDistance * 2.0F);
         GlStateManager.matrixMode(5888);
         GlStateManager.loadIdentity();
         if(this.mc.gameSettings.anaglyph) {
            GlStateManager.translate((float)(p_78476_2_ * 2 - 1) * 0.1F, 0.0F, 0.0F);
         }

         boolean var4 = false;
         if(renderItem) {
            GlStateManager.pushMatrix();
            this.hurtCameraEffect(p_78476_1_);
            if(this.mc.gameSettings.viewBobbing) {
               this.setupViewBobbing(p_78476_1_);
            }

            var4 = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();
            boolean shouldRenderHand = !ReflectorForge.renderFirstPersonHand(this.mc.renderGlobal, p_78476_1_, p_78476_2_);
            if(shouldRenderHand && this.mc.gameSettings.thirdPersonView == 0 && !var4 && !this.mc.gameSettings.hideGUI && !this.mc.playerController.isSpectator()) {
               this.enableLightmap();
               if(Config.isShaders()) {
                  ShadersRender.renderItemFP(this.itemRenderer, p_78476_1_, renderTranslucent);
               } else {
                  this.itemRenderer.renderItemInFirstPerson(p_78476_1_);
               }

               this.disableLightmap();
            }

            GlStateManager.popMatrix();
         }

         if(!renderOverlay) {
            return;
         }

         this.disableLightmap();
         if(this.mc.gameSettings.thirdPersonView == 0 && !var4) {
            this.itemRenderer.renderOverlays(p_78476_1_);
            this.hurtCameraEffect(p_78476_1_);
         }

         if(this.mc.gameSettings.viewBobbing) {
            this.setupViewBobbing(p_78476_1_);
         }
      }

   }

   public void disableLightmap() {
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.disableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      if(Config.isShaders()) {
         Shaders.disableLightmap();
      }

   }

   public void enableLightmap() {
      GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GlStateManager.matrixMode(5890);
      GlStateManager.loadIdentity();
      float var1 = 0.00390625F;
      GlStateManager.scale(var1, var1, var1);
      GlStateManager.translate(8.0F, 8.0F, 8.0F);
      GlStateManager.matrixMode(5888);
      this.mc.getTextureManager().bindTexture(this.locationLightMap);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glTexParameteri(3553, 10242, 10496);
      GL11.glTexParameteri(3553, 10243, 10496);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableTexture2D();
      GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
      if(Config.isShaders()) {
         Shaders.enableLightmap();
      }

   }

   public void updateTorchFlicker() {
      this.torchFlickerDX = (float)((double)this.torchFlickerDX + (Math.random() - Math.random()) * Math.random() * Math.random());
      this.torchFlickerDX = (float)((double)this.torchFlickerDX * 0.9D);
      this.torchFlickerX += (this.torchFlickerDX - this.torchFlickerX) * 1.0F;
      this.lightmapUpdateNeeded = true;
   }

   public void updateLightmap(float partialTicks) {
      if(this.lightmapUpdateNeeded) {
         this.mc.mcProfiler.startSection("lightTex");
         WorldClient var2 = this.mc.theWorld;
         if(var2 != null) {
            if(Config.isCustomColors() && CustomColors.updateLightmap(var2, this.torchFlickerX, this.lightmapColors, this.mc.thePlayer.isPotionActive(Potion.nightVision), partialTicks)) {
               this.lightmapTexture.updateDynamicTexture();
               this.lightmapUpdateNeeded = false;
               this.mc.mcProfiler.endSection();
               return;
            }

            float var3 = var2.getSunBrightness(1.0F);
            float var4 = var3 * 0.95F + 0.05F;

            for(int var5 = 0; var5 < 256; ++var5) {
               float var6 = var2.provider.getLightBrightnessTable()[var5 / 16] * var4;
               float var7 = var2.provider.getLightBrightnessTable()[var5 % 16] * (this.torchFlickerX * 0.1F + 1.5F);
               if(var2.getLastLightningBolt() > 0) {
                  var6 = var2.provider.getLightBrightnessTable()[var5 / 16];
               }

               float var8 = var6 * (var3 * 0.65F + 0.35F);
               float var9 = var6 * (var3 * 0.65F + 0.35F);
               float var12 = var7 * ((var7 * 0.6F + 0.4F) * 0.6F + 0.4F);
               float var13 = var7 * (var7 * var7 * 0.6F + 0.4F);
               float var14 = var8 + var7;
               float var15 = var9 + var12;
               float var16 = var6 + var13;
               var14 = var14 * 0.96F + 0.03F;
               var15 = var15 * 0.96F + 0.03F;
               var16 = var16 * 0.96F + 0.03F;
               float var17;
               if(this.bossColorModifier > 0.0F) {
                  var17 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
                  var14 = var14 * (1.0F - var17) + var14 * 0.7F * var17;
                  var15 = var15 * (1.0F - var17) + var15 * 0.6F * var17;
                  var16 = var16 * (1.0F - var17) + var16 * 0.6F * var17;
               }

               if(var2.provider.getDimensionId() == 1) {
                  var14 = 0.22F + var7 * 0.75F;
                  var15 = 0.28F + var12 * 0.75F;
                  var16 = 0.25F + var13 * 0.75F;
               }

               float var18;
               if(this.mc.thePlayer.isPotionActive(Potion.nightVision)) {
                  var17 = this.getNightVisionBrightness(this.mc.thePlayer, partialTicks);
                  var18 = 1.0F / var14;
                  if(var18 > 1.0F / var15) {
                     var18 = 1.0F / var15;
                  }

                  if(var18 > 1.0F / var16) {
                     var18 = 1.0F / var16;
                  }

                  var14 = var14 * (1.0F - var17) + var14 * var18 * var17;
                  var15 = var15 * (1.0F - var17) + var15 * var18 * var17;
                  var16 = var16 * (1.0F - var17) + var16 * var18 * var17;
               }

               if(var14 > 1.0F) {
                  var14 = 1.0F;
               }

               if(var15 > 1.0F) {
                  var15 = 1.0F;
               }

               if(var16 > 1.0F) {
                  var16 = 1.0F;
               }

               var17 = this.mc.gameSettings.gammaSetting;
               var18 = 1.0F - var14;
               float var19 = 1.0F - var15;
               float var20 = 1.0F - var16;
               var18 = 1.0F - var18 * var18 * var18 * var18;
               var19 = 1.0F - var19 * var19 * var19 * var19;
               var20 = 1.0F - var20 * var20 * var20 * var20;
               var14 = var14 * (1.0F - var17) + var18 * var17;
               var15 = var15 * (1.0F - var17) + var19 * var17;
               var16 = var16 * (1.0F - var17) + var20 * var17;
               var14 = var14 * 0.96F + 0.03F;
               var15 = var15 * 0.96F + 0.03F;
               var16 = var16 * 0.96F + 0.03F;
               if(var14 > 1.0F) {
                  var14 = 1.0F;
               }

               if(var15 > 1.0F) {
                  var15 = 1.0F;
               }

               if(var16 > 1.0F) {
                  var16 = 1.0F;
               }

               if(var14 < 0.0F) {
                  var14 = 0.0F;
               }

               if(var15 < 0.0F) {
                  var15 = 0.0F;
               }

               if(var16 < 0.0F) {
                  var16 = 0.0F;
               }

               short var21 = 255;
               int var22 = (int)(var14 * 255.0F);
               int var23 = (int)(var15 * 255.0F);
               int var24 = (int)(var16 * 255.0F);
               this.lightmapColors[var5] = var21 << 24 | var22 << 16 | var23 << 8 | var24;
            }

            this.lightmapTexture.updateDynamicTexture();
            this.lightmapUpdateNeeded = false;
            this.mc.mcProfiler.endSection();
         }
      }

   }

   public float getNightVisionBrightness(EntityLivingBase p_180438_1_, float partialTicks) {
      int var3 = p_180438_1_.getActivePotionEffect(Potion.nightVision).getDuration();
      return var3 > 200?1.0F:0.7F + MathHelper.sin(((float)var3 - partialTicks) * 3.1415927F * 0.2F) * 0.3F;
   }

   public void updateCameraAndRender(float partialTicks, long nanoTimeStart) {
      Config.renderPartialTicks = partialTicks;
      this.frameInit();
      boolean var4 = Display.isActive();
      if(!var4 && this.mc.gameSettings.pauseOnLostFocus && (!this.mc.gameSettings.touchscreen || !Mouse.isButtonDown(1))) {
         if(Minecraft.getSystemTime() - this.prevFrameTime > 500L) {
            this.mc.displayInGameMenu();
         }
      } else {
         this.prevFrameTime = Minecraft.getSystemTime();
      }

      this.mc.mcProfiler.startSection("mouse");
      if(var4 && Minecraft.isRunningOnMac && this.mc.inGameHasFocus && !Mouse.isInsideWindow()) {
         Mouse.setGrabbed(false);
         Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
         Mouse.setGrabbed(true);
      }

      if(this.mc.inGameHasFocus && var4) {
         this.mc.mouseHelper.mouseXYChange();
         float var17 = this.mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
         float var18 = var17 * var17 * var17 * 8.0F;
         float var19 = (float)this.mc.mouseHelper.deltaX * var18;
         float var20 = (float)this.mc.mouseHelper.deltaY * var18;
         byte var21 = 1;
         if(this.mc.gameSettings.invertMouse) {
            var21 = -1;
         }

         if(this.mc.gameSettings.smoothCamera) {
            this.smoothCamYaw += var19;
            this.smoothCamPitch += var20;
            float var22 = partialTicks - this.smoothCamPartialTicks;
            this.smoothCamPartialTicks = partialTicks;
            var19 = this.smoothCamFilterX * var22;
            var20 = this.smoothCamFilterY * var22;
            this.mc.thePlayer.setAngles(var19, var20 * (float)var21);
         } else {
            this.smoothCamYaw = 0.0F;
            this.smoothCamPitch = 0.0F;
            this.mc.thePlayer.setAngles(var19, var20 * (float)var21);
         }
      }

      this.mc.mcProfiler.endSection();
      if(!this.mc.skipRenderWorld) {
         anaglyphEnable = this.mc.gameSettings.anaglyph;
         final ScaledResolution var171 = new ScaledResolution(this.mc);
         int var182 = var171.getScaledWidth();
         int var191 = var171.getScaledHeight();
         final int var201 = Mouse.getX() * var182 / this.mc.displayWidth;
         final int var211 = var191 - Mouse.getY() * var191 / this.mc.displayHeight - 1;
         int var221 = this.mc.gameSettings.limitFramerate;
         if(this.mc.theWorld != null) {
            this.mc.mcProfiler.startSection("level");
            int var16 = Math.min(Minecraft.getDebugFPS(), var221);
            var16 = Math.max(var16, 60);
            long var12 = System.nanoTime() - nanoTimeStart;
            long var14 = Math.max((long)(1000000000 / var16 / 4) - var12, 0L);
            this.renderWorld(partialTicks, System.nanoTime() + var14);
            if(OpenGlHelper.shadersSupported) {
               this.mc.renderGlobal.renderEntityOutlineFramebuffer();
               if(this.theShaderGroup != null && this.useShader) {
                  GlStateManager.matrixMode(5890);
                  GlStateManager.pushMatrix();
                  GlStateManager.loadIdentity();
                  this.theShaderGroup.loadShaderGroup(partialTicks);
                  GlStateManager.popMatrix();
               }

               this.mc.getFramebuffer().bindFramebuffer(true);
            }

            this.renderEndNanoTime = System.nanoTime();
            this.mc.mcProfiler.endStartSection("gui");
            if(!this.mc.gameSettings.hideGUI || this.mc.currentScreen != null) {
               GlStateManager.alphaFunc(516, 0.1F);
               this.mc.ingameGUI.renderGameOverlay(partialTicks);
               if(this.mc.gameSettings.ofShowFps && !this.mc.gameSettings.showDebugInfo) {
                  Config.drawFps();
               }

               if(this.mc.gameSettings.showDebugInfo) {
                  Lagometer.showLagometer(var171);
               }
            }

            this.mc.mcProfiler.endSection();
         } else {
            GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            this.setupOverlayRendering();
            this.renderEndNanoTime = System.nanoTime();
            TileEntityRendererDispatcher.instance.renderEngine = this.mc.getTextureManager();
         }

         if(this.mc.currentScreen != null) {
            GlStateManager.clear(256);

            try {
               if(Reflector.ForgeHooksClient_drawScreen.exists()) {
                  Reflector.callVoid(Reflector.ForgeHooksClient_drawScreen, new Object[]{this.mc.currentScreen, Integer.valueOf(var201), Integer.valueOf(var211), Float.valueOf(partialTicks)});
               } else {
                  this.mc.currentScreen.drawScreen(var201, var211, partialTicks);
               }
            } catch (Throwable var181) {
               CrashReport var23 = CrashReport.makeCrashReport(var181, "Rendering screen");
               CrashReportCategory var13 = var23.makeCategory("Screen render details");
               var13.addCrashSectionCallable("Screen name", new EntityRenderer$2(this));
               var13.addCrashSectionCallable("Mouse location", new Callable() {

                  public static final String __OBFID = "CL_00000950";

                  public String call() throws Exception {
                     return String.format("Scaled: (%d, %d). Absolute: (%d, %d)", new Object[]{Integer.valueOf(var201), Integer.valueOf(var211), Integer.valueOf(Mouse.getX()), Integer.valueOf(Mouse.getY())});
                  }
                  // $FF: synthetic method
                  // $FF: bridge method
                  public Object call() throws Exception {
                     return this.call();
                  }
               });
               var13.addCrashSectionCallable("Screen size", new Callable() {

                  public static final String __OBFID = "CL_00000951";

                  public String call() throws Exception {
                     return String.format("Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", new Object[]{Integer.valueOf(var171.getScaledWidth()), Integer.valueOf(var171.getScaledHeight()), Integer.valueOf(EntityRenderer.this.mc.displayWidth), Integer.valueOf(EntityRenderer.this.mc.displayHeight), Integer.valueOf(var171.getScaleFactor())});
                  }
                  // $FF: synthetic method
                  // $FF: bridge method
                  public Object call() throws Exception {
                     return this.call();
                  }
               });
               throw new ReportedException(var23);
            }
         }
      }

      this.frameFinish();
      this.waitForServerThread();
      Lagometer.updateLagometer();
      if(this.mc.gameSettings.ofProfiler) {
         this.mc.gameSettings.showDebugProfilerChart = true;
      }

   }

   public void renderStreamIndicator(float p_152430_1_) {
      this.setupOverlayRendering();
      this.mc.ingameGUI.renderStreamIndicator(new ScaledResolution(this.mc));
   }

   public boolean isDrawBlockOutline() {
      if(!this.drawBlockOutline) {
         return false;
      } else {
         Entity var1 = this.mc.getRenderViewEntity();
         boolean var2 = var1 instanceof EntityPlayer && !this.mc.gameSettings.hideGUI;
         if(var2 && !((EntityPlayer)var1).capabilities.allowEdit) {
            ItemStack var3 = ((EntityPlayer)var1).getCurrentEquippedItem();
            if(this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK) {
               BlockPos var4 = this.mc.objectMouseOver.getBlockPos();
               IBlockState state = this.mc.theWorld.getBlockState(var4);
               Block var5 = state.getBlock();
               if(this.mc.playerController.getCurrentGameType() == GameType.SPECTATOR) {
                  var2 = ReflectorForge.blockHasTileEntity(state) && this.mc.theWorld.getTileEntity(var4) instanceof IInventory;
               } else {
                  var2 = var3 != null && (var3.canDestroy(var5) || var3.canPlaceOn(var5));
               }
            }
         }

         return var2;
      }
   }

   public void renderWorldDirections(float p_175067_1_) {
      if(this.mc.gameSettings.showDebugInfo && !this.mc.gameSettings.hideGUI && !this.mc.thePlayer.hasReducedDebug() && !this.mc.gameSettings.reducedDebugInfo) {
         Entity var2 = this.mc.getRenderViewEntity();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GL11.glLineWidth(1.0F);
         GlStateManager.disableTexture2D();
         GlStateManager.depthMask(false);
         GlStateManager.pushMatrix();
         GlStateManager.matrixMode(5888);
         GlStateManager.loadIdentity();
         this.orientCamera(p_175067_1_);
         GlStateManager.translate(0.0F, var2.getEyeHeight(), 0.0F);
         RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.005D, 1.0E-4D, 1.0E-4D), 255, 0, 0, 255);
         RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0E-4D, 1.0E-4D, 0.005D), 0, 0, 255, 255);
         RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0E-4D, 0.0033D, 1.0E-4D), 0, 255, 0, 255);
         GlStateManager.popMatrix();
         GlStateManager.depthMask(true);
         GlStateManager.enableTexture2D();
         GlStateManager.disableBlend();
      }

   }

   public void renderWorld(float partialTicks, long finishTimeNano) {
      this.updateLightmap(partialTicks);
      if(this.mc.getRenderViewEntity() == null) {
         this.mc.setRenderViewEntity(this.mc.thePlayer);
      }

      this.getMouseOver(partialTicks);
      if(Config.isShaders()) {
         Shaders.beginRender(this.mc, partialTicks, finishTimeNano);
      }

      GlStateManager.enableDepth();
      GlStateManager.enableAlpha();
      GlStateManager.alphaFunc(516, 0.1F);
      this.mc.mcProfiler.startSection("center");
      if(this.mc.gameSettings.anaglyph) {
         anaglyphField = 0;
         GlStateManager.colorMask(false, true, true, false);
         this.renderWorldPass(0, partialTicks, finishTimeNano);
         anaglyphField = 1;
         GlStateManager.colorMask(true, false, false, false);
         this.renderWorldPass(1, partialTicks, finishTimeNano);
         GlStateManager.colorMask(true, true, true, false);
      } else {
         this.renderWorldPass(2, partialTicks, finishTimeNano);
      }

      this.mc.mcProfiler.endSection();
   }

   public void renderWorldPass(int pass, float partialTicks, long finishTimeNano) {
      boolean isShaders = Config.isShaders();
      if(isShaders) {
         Shaders.beginRenderPass(pass, partialTicks, finishTimeNano);
      }

      RenderGlobal var5 = this.mc.renderGlobal;
      EffectRenderer var6 = this.mc.effectRenderer;
      boolean var7 = this.isDrawBlockOutline();
      GlStateManager.enableCull();
      this.mc.mcProfiler.endStartSection("clear");
      if(isShaders) {
         Shaders.setViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
      } else {
         GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
      }

      this.updateFogColor(partialTicks);
      GlStateManager.clear(16640);
      if(isShaders) {
         Shaders.clearRenderBuffer();
      }

      this.mc.mcProfiler.endStartSection("camera");
      this.setupCameraTransform(partialTicks, pass);
      if(isShaders) {
         Shaders.setCamera(partialTicks);
      }

      ActiveRenderInfo.updateRenderInfo(this.mc.thePlayer, this.mc.gameSettings.thirdPersonView == 2);
      this.mc.mcProfiler.endStartSection("frustum");
      ClippingHelperImpl.getInstance();
      this.mc.mcProfiler.endStartSection("culling");
      Frustum var8 = new Frustum();
      Entity var9 = this.mc.getRenderViewEntity();
      double var10 = var9.lastTickPosX + (var9.posX - var9.lastTickPosX) * (double)partialTicks;
      double var12 = var9.lastTickPosY + (var9.posY - var9.lastTickPosY) * (double)partialTicks;
      double var14 = var9.lastTickPosZ + (var9.posZ - var9.lastTickPosZ) * (double)partialTicks;
      if(isShaders) {
         ShadersRender.setFrustrumPosition(var8, var10, var12, var14);
      } else {
         var8.setPosition(var10, var12, var14);
      }

      if((Config.isSkyEnabled() || Config.isSunMoonEnabled() || Config.isStarsEnabled()) && !Shaders.isShadowPass) {
         this.setupFog(-1, partialTicks);
         this.mc.mcProfiler.endStartSection("sky");
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.clipDistance);
         GlStateManager.matrixMode(5888);
         if(isShaders) {
            Shaders.beginSky();
         }

         var5.renderSky(partialTicks, pass);
         if(isShaders) {
            Shaders.endSky();
         }

         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.clipDistance);
         GlStateManager.matrixMode(5888);
      } else {
         GlStateManager.disableBlend();
      }

      this.setupFog(0, partialTicks);
      GlStateManager.shadeModel(7425);
      if(var9.posY + (double)var9.getEyeHeight() < 128.0D + (double)(this.mc.gameSettings.ofCloudsHeight * 128.0F)) {
         this.renderCloudsCheck(var5, partialTicks, pass);
      }

      this.mc.mcProfiler.endStartSection("prepareterrain");
      this.setupFog(0, partialTicks);
      this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
      RenderHelper.disableStandardItemLighting();
      this.mc.mcProfiler.endStartSection("terrain_setup");
      if(isShaders) {
         ShadersRender.setupTerrain(var5, var9, (double)partialTicks, var8, this.frameCount++, this.mc.thePlayer.isSpectator());
      } else {
         var5.setupTerrain(var9, (double)partialTicks, var8, this.frameCount++, this.mc.thePlayer.isSpectator());
      }

      if(pass == 0 || pass == 2) {
         this.mc.mcProfiler.endStartSection("updatechunks");
         Lagometer.timerChunkUpload.start();
         this.mc.renderGlobal.updateChunks(finishTimeNano);
         Lagometer.timerChunkUpload.end();
      }

      this.mc.mcProfiler.endStartSection("terrain");
      Lagometer.timerTerrain.start();
      if(this.mc.gameSettings.ofSmoothFps && pass > 0) {
         this.mc.mcProfiler.endStartSection("finish");
         GL11.glFinish();
         this.mc.mcProfiler.endStartSection("terrain");
      }

      GlStateManager.matrixMode(5888);
      GlStateManager.pushMatrix();
      GlStateManager.disableAlpha();
      if(isShaders) {
         ShadersRender.beginTerrainSolid();
      }

      var5.renderBlockLayer(EnumWorldBlockLayer.SOLID, (double)partialTicks, pass, var9);
      GlStateManager.enableAlpha();
      if(isShaders) {
         ShadersRender.beginTerrainCutoutMipped();
      }

      var5.renderBlockLayer(EnumWorldBlockLayer.CUTOUT_MIPPED, (double)partialTicks, pass, var9);
      this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
      if(isShaders) {
         ShadersRender.beginTerrainCutout();
      }

      var5.renderBlockLayer(EnumWorldBlockLayer.CUTOUT, (double)partialTicks, pass, var9);
      this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
      if(isShaders) {
         ShadersRender.endTerrain();
      }

      Lagometer.timerTerrain.end();
      GlStateManager.shadeModel(7424);
      GlStateManager.alphaFunc(516, 0.1F);
      EntityPlayer var16;
      if(!this.debugView) {
         GlStateManager.matrixMode(5888);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         RenderHelper.enableStandardItemLighting();
         this.mc.mcProfiler.endStartSection("entities");
         if(Reflector.ForgeHooksClient_setRenderPass.exists()) {
            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[]{Integer.valueOf(0)});
         }

         var5.renderEntities(var9, var8, partialTicks);
         if(Reflector.ForgeHooksClient_setRenderPass.exists()) {
            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[]{Integer.valueOf(-1)});
         }

         RenderHelper.disableStandardItemLighting();
         this.disableLightmap();
         GlStateManager.matrixMode(5888);
         GlStateManager.popMatrix();
         GlStateManager.pushMatrix();
         if(this.mc.objectMouseOver != null && var9.isInsideOfMaterial(Material.water) && var7) {
            var16 = (EntityPlayer)var9;
            GlStateManager.disableAlpha();
            this.mc.mcProfiler.endStartSection("outline");
            if((!Reflector.ForgeHooksClient_onDrawBlockHighlight.exists() || !Reflector.callBoolean(Reflector.ForgeHooksClient_onDrawBlockHighlight, new Object[]{var5, var16, this.mc.objectMouseOver, Integer.valueOf(0), var16.getHeldItem(), Float.valueOf(partialTicks)})) && !this.mc.gameSettings.hideGUI) {
               var5.drawSelectionBox(var16, this.mc.objectMouseOver, 0, partialTicks);
            }

            GlStateManager.enableAlpha();
         }
      }

      GlStateManager.matrixMode(5888);
      GlStateManager.popMatrix();
      if(var7 && this.mc.objectMouseOver != null && !var9.isInsideOfMaterial(Material.water)) {
         var16 = (EntityPlayer)var9;
         GlStateManager.disableAlpha();
         this.mc.mcProfiler.endStartSection("outline");
         if((!Reflector.ForgeHooksClient_onDrawBlockHighlight.exists() || !Reflector.callBoolean(Reflector.ForgeHooksClient_onDrawBlockHighlight, new Object[]{var5, var16, this.mc.objectMouseOver, Integer.valueOf(0), var16.getHeldItem(), Float.valueOf(partialTicks)})) && !this.mc.gameSettings.hideGUI) {
            var5.drawSelectionBox(var16, this.mc.objectMouseOver, 0, partialTicks);
         }

         GlStateManager.enableAlpha();
      }

      if(!var5.damagedBlocks.isEmpty()) {
         this.mc.mcProfiler.endStartSection("destroyProgress");
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
         this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
         var5.drawBlockDamageTexture(Tessellator.getInstance(), Tessellator.getInstance().getWorldRenderer(), var9, partialTicks);
         this.mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
         GlStateManager.disableBlend();
      }

      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.disableBlend();
      if(!this.debugView) {
         this.enableLightmap();
         this.mc.mcProfiler.endStartSection("litParticles");
         if(isShaders) {
            Shaders.beginLitParticles();
         }

         var6.renderLitParticles(var9, partialTicks);
         RenderHelper.disableStandardItemLighting();
         this.setupFog(0, partialTicks);
         this.mc.mcProfiler.endStartSection("particles");
         if(isShaders) {
            Shaders.beginParticles();
         }

         var6.renderParticles(var9, partialTicks);
         if(isShaders) {
            Shaders.endParticles();
         }

         this.disableLightmap();
      }

      GlStateManager.depthMask(false);
      GlStateManager.enableCull();
      this.mc.mcProfiler.endStartSection("weather");
      if(isShaders) {
         Shaders.beginWeather();
      }

      this.renderRainSnow(partialTicks);
      if(isShaders) {
         Shaders.endWeather();
      }

      GlStateManager.depthMask(true);
      var5.renderWorldBorder(var9, partialTicks);
      if(isShaders) {
         ShadersRender.renderHand0(this, partialTicks, pass);
         Shaders.preWater();
      }

      GlStateManager.disableBlend();
      GlStateManager.enableCull();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.alphaFunc(516, 0.1F);
      this.setupFog(0, partialTicks);
      GlStateManager.enableBlend();
      GlStateManager.depthMask(false);
      this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
      GlStateManager.shadeModel(7425);
      this.mc.mcProfiler.endStartSection("translucent");
      if(isShaders) {
         Shaders.beginWater();
      }

      var5.renderBlockLayer(EnumWorldBlockLayer.TRANSLUCENT, (double)partialTicks, pass, var9);
      if(isShaders) {
         Shaders.endWater();
      }

      if(Reflector.ForgeHooksClient_setRenderPass.exists() && !this.debugView) {
         RenderHelper.enableStandardItemLighting();
         this.mc.mcProfiler.endStartSection("entities");
         Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[]{Integer.valueOf(1)});
         this.mc.renderGlobal.renderEntities(var9, var8, partialTicks);
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         Reflector.callVoid(Reflector.ForgeHooksClient_setRenderPass, new Object[]{Integer.valueOf(-1)});
         RenderHelper.disableStandardItemLighting();
      }

      GlStateManager.shadeModel(7424);
      GlStateManager.depthMask(true);
      GlStateManager.enableCull();
      GlStateManager.disableBlend();
      GlStateManager.disableFog();
      if(var9.posY + (double)var9.getEyeHeight() >= 128.0D + (double)(this.mc.gameSettings.ofCloudsHeight * 128.0F)) {
         this.mc.mcProfiler.endStartSection("aboveClouds");
         this.renderCloudsCheck(var5, partialTicks, pass);
      }

      if(Reflector.ForgeHooksClient_dispatchRenderLast.exists()) {
         this.mc.mcProfiler.endStartSection("forge_render_last");
         Reflector.callVoid(Reflector.ForgeHooksClient_dispatchRenderLast, new Object[]{var5, Float.valueOf(partialTicks)});
      }

      this.mc.mcProfiler.endStartSection("hand");
      boolean handRendered = ReflectorForge.renderFirstPersonHand(this.mc.renderGlobal, partialTicks, pass);
      if(!handRendered && this.renderHand && !Shaders.isShadowPass) {
         if(isShaders) {
            ShadersRender.renderHand1(this, partialTicks, pass);
            Shaders.renderCompositeFinal();
         }

         GlStateManager.clear(256);
         if(isShaders) {
            ShadersRender.renderFPOverlay(this, partialTicks, pass);
         } else {
            this.renderHand(partialTicks, pass);
         }

         this.renderWorldDirections(partialTicks);
      }

      if(isShaders) {
         Shaders.endRender();
      }

   }

   public void renderCloudsCheck(RenderGlobal p_180437_1_, float partialTicks, int pass) {
      if(this.mc.gameSettings.renderDistanceChunks >= 4 && !Config.isCloudsOff() && Shaders.shouldRenderClouds(this.mc.gameSettings)) {
         this.mc.mcProfiler.endStartSection("clouds");
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.clipDistance * 4.0F);
         GlStateManager.matrixMode(5888);
         GlStateManager.pushMatrix();
         this.setupFog(0, partialTicks);
         p_180437_1_.renderClouds(partialTicks, pass);
         GlStateManager.disableFog();
         GlStateManager.popMatrix();
         GlStateManager.matrixMode(5889);
         GlStateManager.loadIdentity();
         Project.gluPerspective(this.getFOVModifier(partialTicks, true), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.clipDistance);
         GlStateManager.matrixMode(5888);
      }

   }

   public void addRainParticles() {
      float var1 = this.mc.theWorld.getRainStrength(1.0F);
      if(!Config.isRainFancy()) {
         var1 /= 2.0F;
      }

      if(var1 != 0.0F && Config.isRainSplash()) {
         this.random.setSeed((long)this.rendererUpdateCount * 312987231L);
         Entity var2 = this.mc.getRenderViewEntity();
         WorldClient var3 = this.mc.theWorld;
         BlockPos var4 = new BlockPos(var2);
         byte var5 = 10;
         double var6 = 0.0D;
         double var8 = 0.0D;
         double var10 = 0.0D;
         int var12 = 0;
         int var13 = (int)(100.0F * var1 * var1);
         if(this.mc.gameSettings.particleSetting == 1) {
            var13 >>= 1;
         } else if(this.mc.gameSettings.particleSetting == 2) {
            var13 = 0;
         }

         for(int var14 = 0; var14 < var13; ++var14) {
            BlockPos var15 = var3.getPrecipitationHeight(var4.add(this.random.nextInt(var5) - this.random.nextInt(var5), 0, this.random.nextInt(var5) - this.random.nextInt(var5)));
            BiomeGenBase var16 = var3.getBiomeGenForCoords(var15);
            BlockPos var17 = var15.down();
            Block var18 = var3.getBlockState(var17).getBlock();
            if(var15.getY() <= var4.getY() + var5 && var15.getY() >= var4.getY() - var5 && var16.canRain() && var16.getFloatTemperature(var15) >= 0.15F) {
               double var19 = this.random.nextDouble();
               double var21 = this.random.nextDouble();
               if(var18.getMaterial() == Material.lava) {
                  this.mc.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, (double)var15.getX() + var19, (double)((float)var15.getY() + 0.1F) - var18.getBlockBoundsMinY(), (double)var15.getZ() + var21, 0.0D, 0.0D, 0.0D, new int[0]);
               } else if(var18.getMaterial() != Material.air) {
                  var18.setBlockBoundsBasedOnState(var3, var17);
                  ++var12;
                  if(this.random.nextInt(var12) == 0) {
                     var6 = (double)var17.getX() + var19;
                     var8 = (double)((float)var17.getY() + 0.1F) + var18.getBlockBoundsMaxY() - 1.0D;
                     var10 = (double)var17.getZ() + var21;
                  }

                  this.mc.theWorld.spawnParticle(EnumParticleTypes.WATER_DROP, (double)var17.getX() + var19, (double)((float)var17.getY() + 0.1F) + var18.getBlockBoundsMaxY(), (double)var17.getZ() + var21, 0.0D, 0.0D, 0.0D, new int[0]);
               }
            }
         }

         if(var12 > 0 && this.random.nextInt(3) < this.rainSoundCounter++) {
            this.rainSoundCounter = 0;
            if(var8 > (double)(var4.getY() + 1) && var3.getPrecipitationHeight(var4).getY() > MathHelper.floor_float((float)var4.getY())) {
               this.mc.theWorld.playSound(var6, var8, var10, "ambient.weather.rain", 0.1F, 0.5F, false);
            } else {
               this.mc.theWorld.playSound(var6, var8, var10, "ambient.weather.rain", 0.2F, 1.0F, false);
            }
         }
      }

   }

   public void renderRainSnow(float partialTicks) {
      if(Reflector.ForgeWorldProvider_getWeatherRenderer.exists()) {
         WorldProvider var2 = this.mc.theWorld.provider;
         Object var3 = Reflector.call(var2, Reflector.ForgeWorldProvider_getWeatherRenderer, new Object[0]);
         if(var3 != null) {
            Reflector.callVoid(var3, Reflector.IRenderHandler_render, new Object[]{Float.valueOf(partialTicks), this.mc.theWorld, this.mc});
            return;
         }
      }

      float var53 = this.mc.theWorld.getRainStrength(partialTicks);
      if(var53 > 0.0F) {
         if(Config.isRainOff()) {
            return;
         }

         this.enableLightmap();
         Entity var54 = this.mc.getRenderViewEntity();
         WorldClient var4 = this.mc.theWorld;
         int var5 = MathHelper.floor_double(var54.posX);
         int var6 = MathHelper.floor_double(var54.posY);
         int var7 = MathHelper.floor_double(var54.posZ);
         Tessellator var8 = Tessellator.getInstance();
         WorldRenderer var9 = var8.getWorldRenderer();
         GlStateManager.disableCull();
         GL11.glNormal3f(0.0F, 1.0F, 0.0F);
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.alphaFunc(516, 0.1F);
         double var10 = var54.lastTickPosX + (var54.posX - var54.lastTickPosX) * (double)partialTicks;
         double var12 = var54.lastTickPosY + (var54.posY - var54.lastTickPosY) * (double)partialTicks;
         double var14 = var54.lastTickPosZ + (var54.posZ - var54.lastTickPosZ) * (double)partialTicks;
         int var16 = MathHelper.floor_double(var12);
         byte var17 = 5;
         if(Config.isRainFancy()) {
            var17 = 10;
         }

         byte var18 = -1;
         float var19 = (float)this.rendererUpdateCount + partialTicks;
         var9.setTranslation(-var10, -var12, -var14);
         if(Config.isRainFancy()) {
            var17 = 10;
         }

         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         MutableBlockPos var20 = new MutableBlockPos();

         for(int var21 = var7 - var17; var21 <= var7 + var17; ++var21) {
            for(int var22 = var5 - var17; var22 <= var5 + var17; ++var22) {
               int var23 = (var21 - var7 + 16) * 32 + var22 - var5 + 16;
               double var24 = (double)this.rainXCoords[var23] * 0.5D;
               double var26 = (double)this.rainYCoords[var23] * 0.5D;
               var20.set(var22, 0, var21);
               BiomeGenBase var28 = var4.getBiomeGenForCoords(var20);
               if(var28.canRain() || var28.getEnableSnow()) {
                  int var29 = var4.getPrecipitationHeight(var20).getY();
                  int var30 = var6 - var17;
                  int var31 = var6 + var17;
                  if(var30 < var29) {
                     var30 = var29;
                  }

                  if(var31 < var29) {
                     var31 = var29;
                  }

                  int var32 = var29;
                  if(var29 < var16) {
                     var32 = var16;
                  }

                  if(var30 != var31) {
                     this.random.setSeed((long)(var22 * var22 * 3121 + var22 * 45238971 ^ var21 * var21 * 418711 + var21 * 13761));
                     var20.set(var22, var30, var21);
                     float var33 = var28.getFloatTemperature(var20);
                     double var34;
                     double var36;
                     double var38;
                     if(var4.getWorldChunkManager().getTemperatureAtHeight(var33, var29) >= 0.15F) {
                        if(var18 != 0) {
                           if(var18 >= 0) {
                              var8.draw();
                           }

                           var18 = 0;
                           this.mc.getTextureManager().bindTexture(locationRainPng);
                           var9.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }

                        var34 = ((double)(this.rendererUpdateCount + var22 * var22 * 3121 + var22 * 45238971 + var21 * var21 * 418711 + var21 * 13761 & 31) + (double)partialTicks) / 32.0D * (3.0D + this.random.nextDouble());
                        var36 = (double)((float)var22 + 0.5F) - var54.posX;
                        var38 = (double)((float)var21 + 0.5F) - var54.posZ;
                        float var40 = MathHelper.sqrt_double(var36 * var36 + var38 * var38) / (float)var17;
                        float var41 = ((1.0F - var40 * var40) * 0.5F + 0.5F) * var53;
                        var20.set(var22, var32, var21);
                        int var42 = var4.getCombinedLight(var20, 0);
                        int var43 = var42 >> 16 & '\uffff';
                        int var51 = var42 & '\uffff';
                        var9.pos((double)var22 - var24 + 0.5D, (double)var30, (double)var21 - var26 + 0.5D).tex(0.0D, (double)var30 * 0.25D + var34).color(1.0F, 1.0F, 1.0F, var41).lightmap(var43, var51).endVertex();
                        var9.pos((double)var22 + var24 + 0.5D, (double)var30, (double)var21 + var26 + 0.5D).tex(1.0D, (double)var30 * 0.25D + var34).color(1.0F, 1.0F, 1.0F, var41).lightmap(var43, var51).endVertex();
                        var9.pos((double)var22 + var24 + 0.5D, (double)var31, (double)var21 + var26 + 0.5D).tex(1.0D, (double)var31 * 0.25D + var34).color(1.0F, 1.0F, 1.0F, var41).lightmap(var43, var51).endVertex();
                        var9.pos((double)var22 - var24 + 0.5D, (double)var31, (double)var21 - var26 + 0.5D).tex(0.0D, (double)var31 * 0.25D + var34).color(1.0F, 1.0F, 1.0F, var41).lightmap(var43, var51).endVertex();
                     } else {
                        if(var18 != 1) {
                           if(var18 >= 0) {
                              var8.draw();
                           }

                           var18 = 1;
                           this.mc.getTextureManager().bindTexture(locationSnowPng);
                           var9.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                        }

                        var34 = (double)(((float)(this.rendererUpdateCount & 511) + partialTicks) / 512.0F);
                        var36 = this.random.nextDouble() + (double)var19 * 0.01D * (double)((float)this.random.nextGaussian());
                        var38 = this.random.nextDouble() + (double)(var19 * (float)this.random.nextGaussian()) * 0.001D;
                        double var49 = (double)((float)var22 + 0.5F) - var54.posX;
                        double var50 = (double)((float)var21 + 0.5F) - var54.posZ;
                        float var55 = MathHelper.sqrt_double(var49 * var49 + var50 * var50) / (float)var17;
                        float var45 = ((1.0F - var55 * var55) * 0.3F + 0.5F) * var53;
                        var20.set(var22, var32, var21);
                        int var46 = (var4.getCombinedLight(var20, 0) * 3 + 15728880) / 4;
                        int var47 = var46 >> 16 & '\uffff';
                        int var48 = var46 & '\uffff';
                        var9.pos((double)var22 - var24 + 0.5D, (double)var30, (double)var21 - var26 + 0.5D).tex(0.0D + var36, (double)var30 * 0.25D + var34 + var38).color(1.0F, 1.0F, 1.0F, var45).lightmap(var47, var48).endVertex();
                        var9.pos((double)var22 + var24 + 0.5D, (double)var30, (double)var21 + var26 + 0.5D).tex(1.0D + var36, (double)var30 * 0.25D + var34 + var38).color(1.0F, 1.0F, 1.0F, var45).lightmap(var47, var48).endVertex();
                        var9.pos((double)var22 + var24 + 0.5D, (double)var31, (double)var21 + var26 + 0.5D).tex(1.0D + var36, (double)var31 * 0.25D + var34 + var38).color(1.0F, 1.0F, 1.0F, var45).lightmap(var47, var48).endVertex();
                        var9.pos((double)var22 - var24 + 0.5D, (double)var31, (double)var21 - var26 + 0.5D).tex(0.0D + var36, (double)var31 * 0.25D + var34 + var38).color(1.0F, 1.0F, 1.0F, var45).lightmap(var47, var48).endVertex();
                     }
                  }
               }
            }
         }

         if(var18 >= 0) {
            var8.draw();
         }

         var9.setTranslation(0.0D, 0.0D, 0.0D);
         GlStateManager.enableCull();
         GlStateManager.disableBlend();
         GlStateManager.alphaFunc(516, 0.1F);
         this.disableLightmap();
      }

   }

   public void setupOverlayRendering() {
      ScaledResolution var1 = new ScaledResolution(this.mc);
      GlStateManager.clear(256);
      GlStateManager.matrixMode(5889);
      GlStateManager.loadIdentity();
      GlStateManager.ortho(0.0D, var1.getScaledWidth_double(), var1.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
      GlStateManager.matrixMode(5888);
      GlStateManager.loadIdentity();
      GlStateManager.translate(0.0F, 0.0F, -2000.0F);
   }

   public void updateFogColor(float partialTicks) {
      WorldClient var2 = this.mc.theWorld;
      Entity var3 = this.mc.getRenderViewEntity();
      float var4 = 0.25F + 0.75F * (float)this.mc.gameSettings.renderDistanceChunks / 32.0F;
      var4 = 1.0F - (float)Math.pow((double)var4, 0.25D);
      Vec3 var5 = var2.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
      var5 = CustomColors.getWorldSkyColor(var5, var2, this.mc.getRenderViewEntity(), partialTicks);
      float var6 = (float)var5.xCoord;
      float var7 = (float)var5.yCoord;
      float var8 = (float)var5.zCoord;
      Vec3 var9 = var2.getFogColor(partialTicks);
      var9 = CustomColors.getWorldFogColor(var9, var2, this.mc.getRenderViewEntity(), partialTicks);
      this.fogColorRed = (float)var9.xCoord;
      this.fogColorGreen = (float)var9.yCoord;
      this.fogColorBlue = (float)var9.zCoord;
      float var13;
      if(this.mc.gameSettings.renderDistanceChunks >= 4) {
         double var10 = -1.0D;
         Vec3 var20 = MathHelper.sin(var2.getCelestialAngleRadians(partialTicks)) > 0.0F?new Vec3(var10, 0.0D, 0.0D):new Vec3(1.0D, 0.0D, 0.0D);
         var13 = (float)var3.getLook(partialTicks).dotProduct(var20);
         if(var13 < 0.0F) {
            var13 = 0.0F;
         }

         if(var13 > 0.0F) {
            float[] var21 = var2.provider.calcSunriseSunsetColors(var2.getCelestialAngle(partialTicks), partialTicks);
            if(var21 != null) {
               var13 *= var21[3];
               this.fogColorRed = this.fogColorRed * (1.0F - var13) + var21[0] * var13;
               this.fogColorGreen = this.fogColorGreen * (1.0F - var13) + var21[1] * var13;
               this.fogColorBlue = this.fogColorBlue * (1.0F - var13) + var21[2] * var13;
            }
         }
      }

      this.fogColorRed += (var6 - this.fogColorRed) * var4;
      this.fogColorGreen += (var7 - this.fogColorGreen) * var4;
      this.fogColorBlue += (var8 - this.fogColorBlue) * var4;
      float var19 = var2.getRainStrength(partialTicks);
      float var11;
      float var201;
      if(var19 > 0.0F) {
         var11 = 1.0F - var19 * 0.5F;
         var201 = 1.0F - var19 * 0.4F;
         this.fogColorRed *= var11;
         this.fogColorGreen *= var11;
         this.fogColorBlue *= var201;
      }

      var11 = var2.getThunderStrength(partialTicks);
      if(var11 > 0.0F) {
         var201 = 1.0F - var11 * 0.5F;
         this.fogColorRed *= var201;
         this.fogColorGreen *= var201;
         this.fogColorBlue *= var201;
      }

      Block var211 = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, var3, partialTicks);
      Vec3 colUnderlava;
      if(this.cloudFog) {
         colUnderlava = var2.getCloudColour(partialTicks);
         this.fogColorRed = (float)colUnderlava.xCoord;
         this.fogColorGreen = (float)colUnderlava.yCoord;
         this.fogColorBlue = (float)colUnderlava.zCoord;
      } else if(var211.getMaterial() == Material.water) {
         var13 = (float)EnchantmentHelper.getRespiration(var3) * 0.2F;
         if(var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).isPotionActive(Potion.waterBreathing)) {
            var13 = var13 * 0.3F + 0.6F;
         }

         this.fogColorRed = 0.02F + var13;
         this.fogColorGreen = 0.02F + var13;
         this.fogColorBlue = 0.2F + var13;
         colUnderlava = CustomColors.getUnderwaterColor(this.mc.theWorld, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0D, this.mc.getRenderViewEntity().posZ);
         if(colUnderlava != null) {
            this.fogColorRed = (float)colUnderlava.xCoord;
            this.fogColorGreen = (float)colUnderlava.yCoord;
            this.fogColorBlue = (float)colUnderlava.zCoord;
         }
      } else if(var211.getMaterial() == Material.lava) {
         this.fogColorRed = 0.6F;
         this.fogColorGreen = 0.1F;
         this.fogColorBlue = 0.0F;
         colUnderlava = CustomColors.getUnderlavaColor(this.mc.theWorld, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0D, this.mc.getRenderViewEntity().posZ);
         if(colUnderlava != null) {
            this.fogColorRed = (float)colUnderlava.xCoord;
            this.fogColorGreen = (float)colUnderlava.yCoord;
            this.fogColorBlue = (float)colUnderlava.zCoord;
         }
      }

      var13 = this.fogColor2 + (this.fogColor1 - this.fogColor2) * partialTicks;
      this.fogColorRed *= var13;
      this.fogColorGreen *= var13;
      this.fogColorBlue *= var13;
      double fogYFactor = var2.provider.getVoidFogYFactor();
      double var23 = (var3.lastTickPosY + (var3.posY - var3.lastTickPosY) * (double)partialTicks) * fogYFactor;
      if(var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).isPotionActive(Potion.blindness)) {
         int var24 = ((EntityLivingBase)var3).getActivePotionEffect(Potion.blindness).getDuration();
         if(var24 < 20) {
            var23 *= (double)(1.0F - (float)var24 / 20.0F);
         } else {
            var23 = 0.0D;
         }
      }

      if(var23 < 1.0D) {
         if(var23 < 0.0D) {
            var23 = 0.0D;
         }

         var23 *= var23;
         this.fogColorRed = (float)((double)this.fogColorRed * var23);
         this.fogColorGreen = (float)((double)this.fogColorGreen * var23);
         this.fogColorBlue = (float)((double)this.fogColorBlue * var23);
      }

      float var241;
      if(this.bossColorModifier > 0.0F) {
         var241 = this.bossColorModifierPrev + (this.bossColorModifier - this.bossColorModifierPrev) * partialTicks;
         this.fogColorRed = this.fogColorRed * (1.0F - var241) + this.fogColorRed * 0.7F * var241;
         this.fogColorGreen = this.fogColorGreen * (1.0F - var241) + this.fogColorGreen * 0.6F * var241;
         this.fogColorBlue = this.fogColorBlue * (1.0F - var241) + this.fogColorBlue * 0.6F * var241;
      }

      float var17;
      if(var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).isPotionActive(Potion.nightVision)) {
         var241 = this.getNightVisionBrightness((EntityLivingBase)var3, partialTicks);
         var17 = 1.0F / this.fogColorRed;
         if(var17 > 1.0F / this.fogColorGreen) {
            var17 = 1.0F / this.fogColorGreen;
         }

         if(var17 > 1.0F / this.fogColorBlue) {
            var17 = 1.0F / this.fogColorBlue;
         }

         if(Float.isInfinite(var17)) {
            var17 = Math.nextAfter(var17, 0.0D);
         }

         this.fogColorRed = this.fogColorRed * (1.0F - var241) + this.fogColorRed * var17 * var241;
         this.fogColorGreen = this.fogColorGreen * (1.0F - var241) + this.fogColorGreen * var17 * var241;
         this.fogColorBlue = this.fogColorBlue * (1.0F - var241) + this.fogColorBlue * var17 * var241;
      }

      if(this.mc.gameSettings.anaglyph) {
         var241 = (this.fogColorRed * 30.0F + this.fogColorGreen * 59.0F + this.fogColorBlue * 11.0F) / 100.0F;
         var17 = (this.fogColorRed * 30.0F + this.fogColorGreen * 70.0F) / 100.0F;
         float event = (this.fogColorRed * 30.0F + this.fogColorBlue * 70.0F) / 100.0F;
         this.fogColorRed = var241;
         this.fogColorGreen = var17;
         this.fogColorBlue = event;
      }

      if(Reflector.EntityViewRenderEvent_FogColors_Constructor.exists()) {
         Object event1 = Reflector.newInstance(Reflector.EntityViewRenderEvent_FogColors_Constructor, new Object[]{this, var3, var211, Float.valueOf(partialTicks), Float.valueOf(this.fogColorRed), Float.valueOf(this.fogColorGreen), Float.valueOf(this.fogColorBlue)});
         Reflector.postForgeBusEvent(event1);
         this.fogColorRed = Reflector.getFieldValueFloat(event1, Reflector.EntityViewRenderEvent_FogColors_red, this.fogColorRed);
         this.fogColorGreen = Reflector.getFieldValueFloat(event1, Reflector.EntityViewRenderEvent_FogColors_green, this.fogColorGreen);
         this.fogColorBlue = Reflector.getFieldValueFloat(event1, Reflector.EntityViewRenderEvent_FogColors_blue, this.fogColorBlue);
      }

      Shaders.setClearColor(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 0.0F);
   }

   public void setupFog(int p_78468_1_, float partialTicks) {
      Entity var3 = this.mc.getRenderViewEntity();
      boolean var4 = false;
      this.fogStandard = false;
      if(var3 instanceof EntityPlayer) {
         var4 = ((EntityPlayer)var3).capabilities.isCreativeMode;
      }

      GL11.glFog(2918, this.setFogColorBuffer(this.fogColorRed, this.fogColorGreen, this.fogColorBlue, 1.0F));
      GL11.glNormal3f(0.0F, -1.0F, 0.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      Block var5 = ActiveRenderInfo.getBlockAtEntityViewpoint(this.mc.theWorld, var3, partialTicks);
      float forgeFogDensity = -1.0F;
      if(Reflector.ForgeHooksClient_getFogDensity.exists()) {
         forgeFogDensity = Reflector.callFloat(Reflector.ForgeHooksClient_getFogDensity, new Object[]{this, var3, var5, Float.valueOf(partialTicks), Float.valueOf(0.1F)});
      }

      if(forgeFogDensity >= 0.0F) {
         GlStateManager.setFogDensity(forgeFogDensity);
      } else {
         float var6;
         if(var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).isPotionActive(Potion.blindness)) {
            var6 = 5.0F;
            int var7 = ((EntityLivingBase)var3).getActivePotionEffect(Potion.blindness).getDuration();
            if(var7 < 20) {
               var6 = 5.0F + (this.farPlaneDistance - 5.0F) * (1.0F - (float)var7 / 20.0F);
            }

            if(Config.isShaders()) {
               Shaders.setFog(9729);
            } else {
               GlStateManager.setFog(9729);
            }

            if(p_78468_1_ == -1) {
               GlStateManager.setFogStart(0.0F);
               GlStateManager.setFogEnd(var6 * 0.8F);
            } else {
               GlStateManager.setFogStart(var6 * 0.25F);
               GlStateManager.setFogEnd(var6);
            }

            if(GLContext.getCapabilities().GL_NV_fog_distance && Config.isFogFancy()) {
               GL11.glFogi('\u855a', '\u855b');
            }
         } else if(this.cloudFog) {
            if(Config.isShaders()) {
               Shaders.setFog(2048);
            } else {
               GlStateManager.setFog(2048);
            }

            GlStateManager.setFogDensity(0.1F);
         } else if(var5.getMaterial() == Material.water) {
            if(Config.isShaders()) {
               Shaders.setFog(2048);
            } else {
               GlStateManager.setFog(2048);
            }

            if(var3 instanceof EntityLivingBase && ((EntityLivingBase)var3).isPotionActive(Potion.waterBreathing)) {
               GlStateManager.setFogDensity(0.01F);
            } else {
               GlStateManager.setFogDensity(0.1F - (float)EnchantmentHelper.getRespiration(var3) * 0.03F);
            }

            if(Config.isClearWater()) {
               GlStateManager.setFogDensity(0.02F);
            }
         } else if(var5.getMaterial() == Material.lava) {
            if(Config.isShaders()) {
               Shaders.setFog(2048);
            } else {
               GlStateManager.setFog(2048);
            }

            GlStateManager.setFogDensity(2.0F);
         } else {
            var6 = this.farPlaneDistance;
            this.fogStandard = true;
            if(Config.isShaders()) {
               Shaders.setFog(9729);
            } else {
               GlStateManager.setFog(9729);
            }

            if(p_78468_1_ == -1) {
               GlStateManager.setFogStart(0.0F);
               GlStateManager.setFogEnd(var6);
            } else {
               GlStateManager.setFogStart(var6 * Config.getFogStart());
               GlStateManager.setFogEnd(var6);
            }

            if(GLContext.getCapabilities().GL_NV_fog_distance) {
               if(Config.isFogFancy()) {
                  GL11.glFogi('\u855a', '\u855b');
               }

               if(Config.isFogFast()) {
                  GL11.glFogi('\u855a', '\u855c');
               }
            }

            if(this.mc.theWorld.provider.doesXZShowFog((int)var3.posX, (int)var3.posZ)) {
               GlStateManager.setFogStart(var6 * 0.05F);
               GlStateManager.setFogEnd(var6);
            }

            if(Reflector.ForgeHooksClient_onFogRender.exists()) {
               Reflector.callVoid(Reflector.ForgeHooksClient_onFogRender, new Object[]{this, var3, var5, Float.valueOf(partialTicks), Integer.valueOf(p_78468_1_), Float.valueOf(var6)});
            }
         }
      }

      GlStateManager.enableColorMaterial();
      GlStateManager.enableFog();
      GlStateManager.colorMaterial(1028, 4608);
   }

   public FloatBuffer setFogColorBuffer(float p_78469_1_, float p_78469_2_, float p_78469_3_, float p_78469_4_) {
      if(Config.isShaders()) {
         Shaders.setFogColor(p_78469_1_, p_78469_2_, p_78469_3_);
      }

      this.fogColorBuffer.clear();
      this.fogColorBuffer.put(p_78469_1_).put(p_78469_2_).put(p_78469_3_).put(p_78469_4_);
      this.fogColorBuffer.flip();
      return this.fogColorBuffer;
   }

   public MapItemRenderer getMapItemRenderer() {
      return this.theMapItemRenderer;
   }

   public void waitForServerThread() {
      this.serverWaitTimeCurrent = 0;
      if(Config.isSmoothWorld() && Config.isSingleProcessor()) {
         if(this.mc.isIntegratedServerRunning()) {
            IntegratedServer srv = this.mc.getIntegratedServer();
            if(srv != null) {
               boolean paused = this.mc.isGamePaused();
               if(!paused && !(this.mc.currentScreen instanceof GuiDownloadTerrain)) {
                  if(this.serverWaitTime > 0) {
                     Lagometer.timerServer.start();
                     Config.sleep((long)this.serverWaitTime);
                     Lagometer.timerServer.end();
                     this.serverWaitTimeCurrent = this.serverWaitTime;
                  }

                  long timeNow = System.nanoTime() / 1000000L;
                  if(this.lastServerTime != 0L && this.lastServerTicks != 0) {
                     long timeDiff = timeNow - this.lastServerTime;
                     if(timeDiff < 0L) {
                        this.lastServerTime = timeNow;
                        timeDiff = 0L;
                     }

                     if(timeDiff >= 50L) {
                        this.lastServerTime = timeNow;
                        int ticks = srv.getTickCounter();
                        int tickDiff = ticks - this.lastServerTicks;
                        if(tickDiff < 0) {
                           this.lastServerTicks = ticks;
                           tickDiff = 0;
                        }

                        if(tickDiff < 1 && this.serverWaitTime < 100) {
                           this.serverWaitTime += 2;
                        }

                        if(tickDiff > 1 && this.serverWaitTime > 0) {
                           --this.serverWaitTime;
                        }

                        this.lastServerTicks = ticks;
                     }
                  } else {
                     this.lastServerTime = timeNow;
                     this.lastServerTicks = srv.getTickCounter();
                     this.avgServerTickDiff = 1.0F;
                     this.avgServerTimeDiff = 50.0F;
                  }
               } else {
                  if(this.mc.currentScreen instanceof GuiDownloadTerrain) {
                     Config.sleep(20L);
                  }

                  this.lastServerTime = 0L;
                  this.lastServerTicks = 0;
               }
            }
         }
      } else {
         this.lastServerTime = 0L;
         this.lastServerTicks = 0;
      }
   }

   public void frameInit() {
      if(!this.initialized) {
         TextureUtils.registerResourceListener();
         if(Config.getBitsOs() == 64 && Config.getBitsJre() == 32) {
            Config.setNotify64BitJava(true);
         }

         this.initialized = true;
      }

      Config.checkDisplayMode();
      WorldClient world = this.mc.theWorld;
      if(world != null) {
         if(Config.getNewRelease() != null) {
            String msg = "HD_U".replace("HD_U", "HD Ultra").replace("L", "Light");
            String fullNewVer = msg + " " + Config.getNewRelease();
            ChatComponentText msg1 = new ChatComponentText(I18n.format("of.message.newVersion", new Object[]{fullNewVer}));
            this.mc.ingameGUI.getChatGUI().printChatMessage(msg1);
            Config.setNewRelease((String)null);
         }

         if(Config.isNotify64BitJava()) {
            Config.setNotify64BitJava(false);
            ChatComponentText msg2 = new ChatComponentText(I18n.format("of.message.java64Bit", new Object[0]));
            this.mc.ingameGUI.getChatGUI().printChatMessage(msg2);
         }
      }

      if(this.mc.currentScreen instanceof GuiMainMenu) {
         this.updateMainMenu((GuiMainMenu)this.mc.currentScreen);
      }

      if(this.updatedWorld != world) {
         RandomMobs.worldChanged(this.updatedWorld, world);
         Config.updateThreadPriorities();
         this.lastServerTime = 0L;
         this.lastServerTicks = 0;
         this.updatedWorld = world;
      }

      if(!this.setFxaaShader(Shaders.configAntialiasingLevel)) {
         Shaders.configAntialiasingLevel = 0;
      }

   }

   public void frameFinish() {
      if(this.mc.theWorld != null) {
         long now = System.currentTimeMillis();
         if(now > this.lastErrorCheckTimeMs + 10000L) {
            this.lastErrorCheckTimeMs = now;
            int err = GL11.glGetError();
            if(err != 0) {
               String text = GLU.gluErrorString(err);
               ChatComponentText msg = new ChatComponentText(I18n.format("of.message.openglError", new Object[]{Integer.valueOf(err), text}));
               this.mc.ingameGUI.getChatGUI().printChatMessage(msg);
            }
         }
      }

   }

   public void updateMainMenu(GuiMainMenu mainGui) {
      try {
         String e = null;
         Calendar calendar = Calendar.getInstance();
         calendar.setTime(new Date());
         int day = calendar.get(5);
         int month = calendar.get(2) + 1;
         if(day == 8 && month == 4) {
            e = "Happy birthday, OptiFine!";
         }

         if(day == 14 && month == 8) {
            e = "Happy birthday, sp614x!";
         }

         if(e == null) {
            return;
         }

         Field[] fs = GuiMainMenu.class.getDeclaredFields();

         for(int i = 0; i < fs.length; ++i) {
            if(fs[i].getType() == String.class) {
               fs[i].setAccessible(true);
               fs[i].set(mainGui, e);
               break;
            }
         }
      } catch (Throwable var8) {
         ;
      }

   }

   public boolean setFxaaShader(int fxaaLevel) {
      if(!OpenGlHelper.isFramebufferEnabled()) {
         return false;
      } else if(this.theShaderGroup != null && this.theShaderGroup != this.fxaaShaders[2] && this.theShaderGroup != this.fxaaShaders[4]) {
         return true;
      } else if(fxaaLevel != 2 && fxaaLevel != 4) {
         if(this.theShaderGroup == null) {
            return true;
         } else {
            this.theShaderGroup.deleteShaderGroup();
            this.theShaderGroup = null;
            return true;
         }
      } else if(this.theShaderGroup != null && this.theShaderGroup == this.fxaaShaders[fxaaLevel]) {
         return true;
      } else if(this.mc.theWorld == null) {
         return true;
      } else {
         this.loadShader(new ResourceLocation("shaders/post/fxaa_of_" + fxaaLevel + "x.json"));
         this.fxaaShaders[fxaaLevel] = this.theShaderGroup;
         return this.useShader;
      }
   }

}
