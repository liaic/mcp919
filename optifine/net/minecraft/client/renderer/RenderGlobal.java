package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.DestroyBlockProgress;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderList;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VboRenderList;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.ListChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.chunk.VboChunkFactory;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemRecord;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Matrix4f;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vector3d;
import net.minecraft.util.BlockPos.MutableBlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import optifine.ChunkUtils;
import optifine.CloudRenderer;
import optifine.Config;
import optifine.CustomColors;
import optifine.CustomSky;
import optifine.DynamicLights;
import optifine.Lagometer;
import optifine.RandomMobs;
import optifine.Reflector;
import optifine.RenderInfoLazy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersRender;
import shadersmod.client.ShadowUtils;

public class RenderGlobal implements IWorldAccess, IResourceManagerReloadListener {

   public static final Logger logger = LogManager.getLogger();
   public static final ResourceLocation locationMoonPhasesPng = new ResourceLocation("textures/environment/moon_phases.png");
   public static final ResourceLocation locationSunPng = new ResourceLocation("textures/environment/sun.png");
   public static final ResourceLocation locationCloudsPng = new ResourceLocation("textures/environment/clouds.png");
   public static final ResourceLocation locationEndSkyPng = new ResourceLocation("textures/environment/end_sky.png");
   public static final ResourceLocation locationForcefieldPng = new ResourceLocation("textures/misc/forcefield.png");
   public final Minecraft mc;
   public final TextureManager renderEngine;
   public final RenderManager renderManager;
   public WorldClient theWorld;
   public Set chunksToUpdate = Sets.newLinkedHashSet();
   public List renderInfos = Lists.newArrayListWithCapacity(69696);
   public final Set setTileEntities = Sets.newHashSet();
   public ViewFrustum viewFrustum;
   public int starGLCallList = -1;
   public int glSkyList = -1;
   public int glSkyList2 = -1;
   public VertexFormat vertexBufferFormat;
   public VertexBuffer starVBO;
   public VertexBuffer skyVBO;
   public VertexBuffer sky2VBO;
   public int cloudTickCounter;
   public final Map damagedBlocks = Maps.newHashMap();
   public final Map mapSoundPositions = Maps.newHashMap();
   public final TextureAtlasSprite[] destroyBlockIcons = new TextureAtlasSprite[10];
   public Framebuffer entityOutlineFramebuffer;
   public ShaderGroup entityOutlineShader;
   public double frustumUpdatePosX = Double.MIN_VALUE;
   public double frustumUpdatePosY = Double.MIN_VALUE;
   public double frustumUpdatePosZ = Double.MIN_VALUE;
   public int frustumUpdatePosChunkX = Integer.MIN_VALUE;
   public int frustumUpdatePosChunkY = Integer.MIN_VALUE;
   public int frustumUpdatePosChunkZ = Integer.MIN_VALUE;
   public double lastViewEntityX = Double.MIN_VALUE;
   public double lastViewEntityY = Double.MIN_VALUE;
   public double lastViewEntityZ = Double.MIN_VALUE;
   public double lastViewEntityPitch = Double.MIN_VALUE;
   public double lastViewEntityYaw = Double.MIN_VALUE;
   public final ChunkRenderDispatcher renderDispatcher = new ChunkRenderDispatcher();
   public ChunkRenderContainer renderContainer;
   public int renderDistanceChunks = -1;
   public int renderEntitiesStartupCounter = 2;
   public int countEntitiesTotal;
   public int countEntitiesRendered;
   public int countEntitiesHidden;
   public boolean debugFixTerrainFrustum = false;
   public ClippingHelper debugFixedClippingHelper;
   public final Vector4f[] debugTerrainMatrix = new Vector4f[8];
   public final Vector3d debugTerrainFrustumPosition = new Vector3d();
   public boolean vboEnabled = false;
   public IRenderChunkFactory renderChunkFactory;
   public double prevRenderSortX;
   public double prevRenderSortY;
   public double prevRenderSortZ;
   public boolean displayListEntitiesDirty = true;
   public static final String __OBFID = "CL_00000954";
   public CloudRenderer cloudRenderer;
   public Entity renderedEntity;
   public Set chunksToResortTransparency = new LinkedHashSet();
   public Set chunksToUpdateForced = new LinkedHashSet();
   public Deque visibilityDeque = new ArrayDeque();
   public List renderInfosEntities = new ArrayList(1024);
   public List renderInfosTileEntities = new ArrayList(1024);
   public List renderInfosNormal = new ArrayList(1024);
   public List renderInfosEntitiesNormal = new ArrayList(1024);
   public List renderInfosTileEntitiesNormal = new ArrayList(1024);
   public List renderInfosShadow = new ArrayList(1024);
   public List renderInfosEntitiesShadow = new ArrayList(1024);
   public List renderInfosTileEntitiesShadow = new ArrayList(1024);
   public int renderDistance = 0;
   public int renderDistanceSq = 0;
   public static final Set SET_ALL_FACINGS = Collections.unmodifiableSet(new HashSet(Arrays.asList(EnumFacing.VALUES)));
   public int countTileEntitiesRendered;
   public boolean renderOverlayDamaged = false;
   public boolean renderOverlayEyes = false;


   public RenderGlobal(Minecraft mcIn) {
      this.cloudRenderer = new CloudRenderer(mcIn);
      this.mc = mcIn;
      this.renderManager = mcIn.getRenderManager();
      this.renderEngine = mcIn.getTextureManager();
      this.renderEngine.bindTexture(locationForcefieldPng);
      GL11.glTexParameteri(3553, 10242, 10497);
      GL11.glTexParameteri(3553, 10243, 10497);
      GlStateManager.bindTexture(0);
      this.updateDestroyBlockIcons();
      this.vboEnabled = OpenGlHelper.useVbo();
      if(this.vboEnabled) {
         this.renderContainer = new VboRenderList();
         this.renderChunkFactory = new VboChunkFactory();
      } else {
         this.renderContainer = new RenderList();
         this.renderChunkFactory = new ListChunkFactory();
      }

      this.vertexBufferFormat = new VertexFormat();
      this.vertexBufferFormat.addElement(new VertexFormatElement(0, EnumType.FLOAT, EnumUsage.POSITION, 3));
      this.generateStars();
      this.generateSky();
      this.generateSky2();
   }

   public void onResourceManagerReload(IResourceManager resourceManager) {
      this.updateDestroyBlockIcons();
   }

   public void updateDestroyBlockIcons() {
      TextureMap var1 = this.mc.getTextureMapBlocks();

      for(int var2 = 0; var2 < this.destroyBlockIcons.length; ++var2) {
         this.destroyBlockIcons[var2] = var1.getAtlasSprite("minecraft:blocks/destroy_stage_" + var2);
      }

   }

   public void makeEntityOutlineShader() {
      if(OpenGlHelper.shadersSupported) {
         if(ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
            ShaderLinkHelper.setNewStaticShaderLinkHelper();
         }

         ResourceLocation var1 = new ResourceLocation("shaders/post/entity_outline.json");

         try {
            this.entityOutlineShader = new ShaderGroup(this.mc.getTextureManager(), this.mc.getResourceManager(), this.mc.getFramebuffer(), var1);
            this.entityOutlineShader.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
            this.entityOutlineFramebuffer = this.entityOutlineShader.getFramebufferRaw("final");
         } catch (IOException var3) {
            logger.warn("Failed to load shader: " + var1, var3);
            this.entityOutlineShader = null;
            this.entityOutlineFramebuffer = null;
         } catch (JsonSyntaxException var4) {
            logger.warn("Failed to load shader: " + var1, var4);
            this.entityOutlineShader = null;
            this.entityOutlineFramebuffer = null;
         }
      } else {
         this.entityOutlineShader = null;
         this.entityOutlineFramebuffer = null;
      }

   }

   public void renderEntityOutlineFramebuffer() {
      if(this.isRenderEntityOutlines()) {
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
         this.entityOutlineFramebuffer.framebufferRenderExt(this.mc.displayWidth, this.mc.displayHeight, false);
         GlStateManager.disableBlend();
      }

   }

   public boolean isRenderEntityOutlines() {
      return !Config.isFastRender() && !Config.isShaders() && !Config.isAntialiasing()?this.entityOutlineFramebuffer != null && this.entityOutlineShader != null && this.mc.thePlayer != null && this.mc.thePlayer.isSpectator() && this.mc.gameSettings.keyBindSpectatorOutlines.isKeyDown():false;
   }

   public void generateSky2() {
      Tessellator var1 = Tessellator.getInstance();
      WorldRenderer var2 = var1.getWorldRenderer();
      if(this.sky2VBO != null) {
         this.sky2VBO.deleteGlBuffers();
      }

      if(this.glSkyList2 >= 0) {
         GLAllocation.deleteDisplayLists(this.glSkyList2);
         this.glSkyList2 = -1;
      }

      if(this.vboEnabled) {
         this.sky2VBO = new VertexBuffer(this.vertexBufferFormat);
         this.renderSky(var2, -16.0F, true);
         var2.finishDrawing();
         var2.reset();
         this.sky2VBO.bufferData(var2.getByteBuffer());
      } else {
         this.glSkyList2 = GLAllocation.generateDisplayLists(1);
         GL11.glNewList(this.glSkyList2, 4864);
         this.renderSky(var2, -16.0F, true);
         var1.draw();
         GL11.glEndList();
      }

   }

   public void generateSky() {
      Tessellator var1 = Tessellator.getInstance();
      WorldRenderer var2 = var1.getWorldRenderer();
      if(this.skyVBO != null) {
         this.skyVBO.deleteGlBuffers();
      }

      if(this.glSkyList >= 0) {
         GLAllocation.deleteDisplayLists(this.glSkyList);
         this.glSkyList = -1;
      }

      if(this.vboEnabled) {
         this.skyVBO = new VertexBuffer(this.vertexBufferFormat);
         this.renderSky(var2, 16.0F, false);
         var2.finishDrawing();
         var2.reset();
         this.skyVBO.bufferData(var2.getByteBuffer());
      } else {
         this.glSkyList = GLAllocation.generateDisplayLists(1);
         GL11.glNewList(this.glSkyList, 4864);
         this.renderSky(var2, 16.0F, false);
         var1.draw();
         GL11.glEndList();
      }

   }

   public void renderSky(WorldRenderer worldRendererIn, float p_174968_2_, boolean p_174968_3_) {
      boolean var4 = true;
      boolean var5 = true;
      worldRendererIn.begin(7, DefaultVertexFormats.POSITION);
      int skyDist = (this.renderDistance / 64 + 1) * 64 + 64;

      for(int var6 = -skyDist; var6 <= skyDist; var6 += 64) {
         for(int var7 = -skyDist; var7 <= skyDist; var7 += 64) {
            float var8 = (float)var6;
            float var9 = (float)(var6 + 64);
            if(p_174968_3_) {
               var9 = (float)var6;
               var8 = (float)(var6 + 64);
            }

            worldRendererIn.pos((double)var8, (double)p_174968_2_, (double)var7).endVertex();
            worldRendererIn.pos((double)var9, (double)p_174968_2_, (double)var7).endVertex();
            worldRendererIn.pos((double)var9, (double)p_174968_2_, (double)(var7 + 64)).endVertex();
            worldRendererIn.pos((double)var8, (double)p_174968_2_, (double)(var7 + 64)).endVertex();
         }
      }

   }

   public void generateStars() {
      Tessellator var1 = Tessellator.getInstance();
      WorldRenderer var2 = var1.getWorldRenderer();
      if(this.starVBO != null) {
         this.starVBO.deleteGlBuffers();
      }

      if(this.starGLCallList >= 0) {
         GLAllocation.deleteDisplayLists(this.starGLCallList);
         this.starGLCallList = -1;
      }

      if(this.vboEnabled) {
         this.starVBO = new VertexBuffer(this.vertexBufferFormat);
         this.renderStars(var2);
         var2.finishDrawing();
         var2.reset();
         this.starVBO.bufferData(var2.getByteBuffer());
      } else {
         this.starGLCallList = GLAllocation.generateDisplayLists(1);
         GlStateManager.pushMatrix();
         GL11.glNewList(this.starGLCallList, 4864);
         this.renderStars(var2);
         var1.draw();
         GL11.glEndList();
         GlStateManager.popMatrix();
      }

   }

   public void renderStars(WorldRenderer worldRendererIn) {
      Random var2 = new Random(10842L);
      worldRendererIn.begin(7, DefaultVertexFormats.POSITION);

      for(int var3 = 0; var3 < 1500; ++var3) {
         double var4 = (double)(var2.nextFloat() * 2.0F - 1.0F);
         double var6 = (double)(var2.nextFloat() * 2.0F - 1.0F);
         double var8 = (double)(var2.nextFloat() * 2.0F - 1.0F);
         double var10 = (double)(0.15F + var2.nextFloat() * 0.1F);
         double var12 = var4 * var4 + var6 * var6 + var8 * var8;
         if(var12 < 1.0D && var12 > 0.01D) {
            var12 = 1.0D / Math.sqrt(var12);
            var4 *= var12;
            var6 *= var12;
            var8 *= var12;
            double var14 = var4 * 100.0D;
            double var16 = var6 * 100.0D;
            double var18 = var8 * 100.0D;
            double var20 = Math.atan2(var4, var8);
            double var22 = Math.sin(var20);
            double var24 = Math.cos(var20);
            double var26 = Math.atan2(Math.sqrt(var4 * var4 + var8 * var8), var6);
            double var28 = Math.sin(var26);
            double var30 = Math.cos(var26);
            double var32 = var2.nextDouble() * 3.141592653589793D * 2.0D;
            double var34 = Math.sin(var32);
            double var36 = Math.cos(var32);

            for(int var38 = 0; var38 < 4; ++var38) {
               double var39 = 0.0D;
               double var41 = (double)((var38 & 2) - 1) * var10;
               double var43 = (double)((var38 + 1 & 2) - 1) * var10;
               double var45 = 0.0D;
               double var47 = var41 * var36 - var43 * var34;
               double var49 = var43 * var36 + var41 * var34;
               double var53 = var47 * var28 + 0.0D * var30;
               double var55 = 0.0D * var28 - var47 * var30;
               double var57 = var55 * var22 - var49 * var24;
               double var61 = var49 * var22 + var55 * var24;
               worldRendererIn.pos(var14 + var57, var16 + var53, var18 + var61).endVertex();
            }
         }
      }

   }

   public void setWorldAndLoadRenderers(WorldClient worldClientIn) {
      if(this.theWorld != null) {
         this.theWorld.removeWorldAccess(this);
      }

      this.frustumUpdatePosX = Double.MIN_VALUE;
      this.frustumUpdatePosY = Double.MIN_VALUE;
      this.frustumUpdatePosZ = Double.MIN_VALUE;
      this.frustumUpdatePosChunkX = Integer.MIN_VALUE;
      this.frustumUpdatePosChunkY = Integer.MIN_VALUE;
      this.frustumUpdatePosChunkZ = Integer.MIN_VALUE;
      this.renderManager.set(worldClientIn);
      this.theWorld = worldClientIn;
      if(Config.isDynamicLights()) {
         DynamicLights.clear();
      }

      if(worldClientIn != null) {
         worldClientIn.addWorldAccess(this);
         this.loadRenderers();
      }

   }

   public void loadRenderers() {
      if(this.theWorld != null) {
         this.displayListEntitiesDirty = true;
         Blocks.leaves.setGraphicsLevel(Config.isTreesFancy());
         Blocks.leaves2.setGraphicsLevel(Config.isTreesFancy());
         BlockModelRenderer.updateAoLightValue();
         if(Config.isDynamicLights()) {
            DynamicLights.clear();
         }

         this.renderDistanceChunks = this.mc.gameSettings.renderDistanceChunks;
         this.renderDistance = this.renderDistanceChunks * 16;
         this.renderDistanceSq = this.renderDistance * this.renderDistance;
         boolean var1 = this.vboEnabled;
         this.vboEnabled = OpenGlHelper.useVbo();
         if(var1 && !this.vboEnabled) {
            this.renderContainer = new RenderList();
            this.renderChunkFactory = new ListChunkFactory();
         } else if(!var1 && this.vboEnabled) {
            this.renderContainer = new VboRenderList();
            this.renderChunkFactory = new VboChunkFactory();
         }

         this.generateStars();
         this.generateSky();
         this.generateSky2();
         if(this.viewFrustum != null) {
            this.viewFrustum.deleteGlResources();
         }

         this.stopChunkUpdates();
         Set var2 = this.setTileEntities;
         Set var5 = this.setTileEntities;
         synchronized(this.setTileEntities) {
            this.setTileEntities.clear();
         }

         this.viewFrustum = new ViewFrustum(this.theWorld, this.mc.gameSettings.renderDistanceChunks, this, this.renderChunkFactory);
         if(this.theWorld != null) {
            Entity var51 = this.mc.getRenderViewEntity();
            if(var51 != null) {
               this.viewFrustum.updateChunkPositions(var51.posX, var51.posZ);
            }
         }

         this.renderEntitiesStartupCounter = 2;
      }

   }

   public void stopChunkUpdates() {
      this.chunksToUpdate.clear();
      this.renderDispatcher.stopChunkUpdates();
   }

   public void createBindEntityOutlineFbs(int p_72720_1_, int p_72720_2_) {
      if(OpenGlHelper.shadersSupported && this.entityOutlineShader != null) {
         this.entityOutlineShader.createBindFramebuffers(p_72720_1_, p_72720_2_);
      }

   }

   public void renderEntities(Entity p_180446_1_, ICamera p_180446_2_, float partialTicks) {
      int pass = 0;
      if(Reflector.MinecraftForgeClient_getRenderPass.exists()) {
         pass = Reflector.callInt(Reflector.MinecraftForgeClient_getRenderPass, new Object[0]);
      }

      if(this.renderEntitiesStartupCounter > 0) {
         if(pass > 0) {
            return;
         }

         --this.renderEntitiesStartupCounter;
      } else {
         double var4 = p_180446_1_.prevPosX + (p_180446_1_.posX - p_180446_1_.prevPosX) * (double)partialTicks;
         double var6 = p_180446_1_.prevPosY + (p_180446_1_.posY - p_180446_1_.prevPosY) * (double)partialTicks;
         double var8 = p_180446_1_.prevPosZ + (p_180446_1_.posZ - p_180446_1_.prevPosZ) * (double)partialTicks;
         this.theWorld.theProfiler.startSection("prepare");
         TileEntityRendererDispatcher.instance.cacheActiveRenderInfo(this.theWorld, this.mc.getTextureManager(), this.mc.fontRendererObj, this.mc.getRenderViewEntity(), partialTicks);
         this.renderManager.cacheActiveRenderInfo(this.theWorld, this.mc.fontRendererObj, this.mc.getRenderViewEntity(), this.mc.pointedEntity, this.mc.gameSettings, partialTicks);
         if(pass == 0) {
            this.countEntitiesTotal = 0;
            this.countEntitiesRendered = 0;
            this.countEntitiesHidden = 0;
            this.countTileEntitiesRendered = 0;
         }

         Entity var10 = this.mc.getRenderViewEntity();
         double var11 = var10.lastTickPosX + (var10.posX - var10.lastTickPosX) * (double)partialTicks;
         double var13 = var10.lastTickPosY + (var10.posY - var10.lastTickPosY) * (double)partialTicks;
         double var15 = var10.lastTickPosZ + (var10.posZ - var10.lastTickPosZ) * (double)partialTicks;
         TileEntityRendererDispatcher.staticPlayerX = var11;
         TileEntityRendererDispatcher.staticPlayerY = var13;
         TileEntityRendererDispatcher.staticPlayerZ = var15;
         this.renderManager.setRenderPosition(var11, var13, var15);
         this.mc.entityRenderer.enableLightmap();
         this.theWorld.theProfiler.endStartSection("global");
         List var17 = this.theWorld.getLoadedEntityList();
         if(pass == 0) {
            this.countEntitiesTotal = var17.size();
         }

         if(Config.isFogOff() && this.mc.entityRenderer.fogStandard) {
            GlStateManager.disableFog();
         }

         boolean forgeEntityPass = Reflector.ForgeEntity_shouldRenderInPass.exists();
         boolean forgeTileEntityPass = Reflector.ForgeTileEntity_shouldRenderInPass.exists();

         int var18;
         Entity var19;
         for(var18 = 0; var18 < this.theWorld.weatherEffects.size(); ++var18) {
            var19 = (Entity)this.theWorld.weatherEffects.get(var18);
            if(!forgeEntityPass || Reflector.callBoolean(var19, Reflector.ForgeEntity_shouldRenderInPass, new Object[]{Integer.valueOf(pass)})) {
               ++this.countEntitiesRendered;
               if(var19.isInRangeToRender3d(var4, var6, var8)) {
                  this.renderManager.renderEntitySimple(var19, partialTicks);
               }
            }
         }

         boolean isShaders;
         if(this.isRenderEntityOutlines()) {
            GlStateManager.depthFunc(519);
            GlStateManager.disableFog();
            this.entityOutlineFramebuffer.framebufferClear();
            this.entityOutlineFramebuffer.bindFramebuffer(false);
            this.theWorld.theProfiler.endStartSection("entityOutlines");
            RenderHelper.disableStandardItemLighting();
            this.renderManager.setRenderOutlines(true);

            for(var18 = 0; var18 < var17.size(); ++var18) {
               var19 = (Entity)var17.get(var18);
               if(!forgeEntityPass || Reflector.callBoolean(var19, Reflector.ForgeEntity_shouldRenderInPass, new Object[]{Integer.valueOf(pass)})) {
                  isShaders = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();
                  boolean var28 = var19.isInRangeToRender3d(var4, var6, var8) && (var19.ignoreFrustumCheck || p_180446_2_.isBoundingBoxInFrustum(var19.getEntityBoundingBox()) || var19.riddenByEntity == this.mc.thePlayer) && var19 instanceof EntityPlayer;
                  if((var19 != this.mc.getRenderViewEntity() || this.mc.gameSettings.thirdPersonView != 0 || isShaders) && var28) {
                     this.renderManager.renderEntitySimple(var19, partialTicks);
                  }
               }
            }

            this.renderManager.setRenderOutlines(false);
            RenderHelper.enableStandardItemLighting();
            GlStateManager.depthMask(false);
            this.entityOutlineShader.loadShaderGroup(partialTicks);
            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);
            this.mc.getFramebuffer().bindFramebuffer(false);
            GlStateManager.enableFog();
            GlStateManager.enableBlend();
            GlStateManager.enableColorMaterial();
            GlStateManager.depthFunc(515);
            GlStateManager.enableDepth();
            GlStateManager.enableAlpha();
         }

         this.theWorld.theProfiler.endStartSection("entities");
         isShaders = Config.isShaders();
         if(isShaders) {
            Shaders.beginEntities();
         }

         Iterator var421 = this.renderInfosEntities.iterator();
         boolean oldFancyGraphics = this.mc.gameSettings.fancyGraphics;
         this.mc.gameSettings.fancyGraphics = Config.isDroppedItemsFancy();

         RenderGlobal.ContainerLocalRenderInformation var30;
         Iterator var32;
         while(var421.hasNext()) {
            var30 = (RenderGlobal.ContainerLocalRenderInformation)var421.next();
            Chunk fontRenderer = var30.renderChunk.getChunk();
            ClassInheritanceMultiMap var29 = fontRenderer.getEntityLists()[var30.renderChunk.getPosition().getY() / 16];
            if(!var29.isEmpty()) {
               var32 = var29.iterator();

               while(var32.hasNext()) {
                  Entity var36 = (Entity)var32.next();
                  if(!forgeEntityPass || Reflector.callBoolean(var36, Reflector.ForgeEntity_shouldRenderInPass, new Object[]{Integer.valueOf(pass)})) {
                     boolean var39 = this.renderManager.shouldRender(var36, p_180446_2_, var4, var6, var8) || var36.riddenByEntity == this.mc.thePlayer;
                     if(var39) {
                        boolean var42 = this.mc.getRenderViewEntity() instanceof EntityLivingBase?((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping():false;
                        if(var36 == this.mc.getRenderViewEntity() && this.mc.gameSettings.thirdPersonView == 0 && !var42 || var36.posY >= 0.0D && var36.posY < 256.0D && !this.theWorld.isBlockLoaded(new BlockPos(var36))) {
                           continue;
                        }

                        ++this.countEntitiesRendered;
                        if(var36.getClass() == EntityItemFrame.class) {
                           var36.renderDistanceWeight = 0.06D;
                        }

                        this.renderedEntity = var36;
                        if(isShaders) {
                           Shaders.nextEntity(var36);
                        }

                        this.renderManager.renderEntitySimple(var36, partialTicks);
                        this.renderedEntity = null;
                     }

                     if(!var39 && var36 instanceof EntityWitherSkull) {
                        if(isShaders) {
                           Shaders.nextEntity(var36);
                        }

                        this.mc.getRenderManager().renderWitherSkull(var36, partialTicks);
                     }
                  }
               }
            }
         }

         this.mc.gameSettings.fancyGraphics = oldFancyGraphics;
         FontRenderer var43 = TileEntityRendererDispatcher.instance.getFontRenderer();
         if(isShaders) {
            Shaders.endEntities();
            Shaders.beginBlockEntities();
         }

         this.theWorld.theProfiler.endStartSection("blockentities");
         RenderHelper.enableStandardItemLighting();
         if(Reflector.ForgeTileEntityRendererDispatcher_preDrawBatch.exists()) {
            Reflector.call(TileEntityRendererDispatcher.instance, Reflector.ForgeTileEntityRendererDispatcher_preDrawBatch, new Object[0]);
         }

         var421 = this.renderInfosTileEntities.iterator();

         while(var421.hasNext()) {
            var30 = (RenderGlobal.ContainerLocalRenderInformation)var421.next();
            List var44 = var30.renderChunk.getCompiledChunk().getTileEntities();
            if(!var44.isEmpty()) {
               var32 = var44.iterator();

               while(var32.hasNext()) {
                  TileEntity var48 = (TileEntity)var32.next();
                  if(forgeTileEntityPass) {
                     if(!Reflector.callBoolean(var48, Reflector.ForgeTileEntity_shouldRenderInPass, new Object[]{Integer.valueOf(pass)})) {
                        continue;
                     }

                     AxisAlignedBB var51 = (AxisAlignedBB)Reflector.call(var48, Reflector.ForgeTileEntity_getRenderBoundingBox, new Object[0]);
                     if(var51 != null && !p_180446_2_.isBoundingBoxInFrustum(var51)) {
                        continue;
                     }
                  }

                  Class var53 = var48.getClass();
                  if(var53 == TileEntitySign.class && !Config.zoomMode) {
                     EntityPlayerSP shouldRender = this.mc.thePlayer;
                     double distSq = var48.getDistanceSq(shouldRender.posX, shouldRender.posY, shouldRender.posZ);
                     if(distSq > 256.0D) {
                        var43.enabled = false;
                     }
                  }

                  if(isShaders) {
                     Shaders.nextBlockEntity(var48);
                  }

                  TileEntityRendererDispatcher.instance.renderTileEntity(var48, partialTicks, -1);
                  ++this.countTileEntitiesRendered;
                  var43.enabled = true;
               }
            }
         }

         Set var45 = this.setTileEntities;
         Set var46 = this.setTileEntities;
         TileEntity var52;
         synchronized(this.setTileEntities) {
            Iterator var49 = this.setTileEntities.iterator();

            while(var49.hasNext()) {
               var52 = (TileEntity)var49.next();
               if(forgeTileEntityPass) {
                  if(!Reflector.callBoolean(var52, Reflector.ForgeTileEntity_shouldRenderInPass, new Object[]{Integer.valueOf(pass)})) {
                     continue;
                  }

                  AxisAlignedBB var54 = (AxisAlignedBB)Reflector.call(var52, Reflector.ForgeTileEntity_getRenderBoundingBox, new Object[0]);
                  if(var54 != null && !p_180446_2_.isBoundingBoxInFrustum(var54)) {
                     continue;
                  }
               }

               Class var57 = var52.getClass();
               if(var57 == TileEntitySign.class && !Config.zoomMode) {
                  EntityPlayerSP tileEntity = this.mc.thePlayer;
                  double distSq1 = var52.getDistanceSq(tileEntity.posX, tileEntity.posY, tileEntity.posZ);
                  if(distSq1 > 256.0D) {
                     var43.enabled = false;
                  }
               }

               if(isShaders) {
                  Shaders.nextBlockEntity(var52);
               }

               TileEntityRendererDispatcher.instance.renderTileEntity(var52, partialTicks, -1);
               var43.enabled = true;
            }
         }

         if(Reflector.ForgeTileEntityRendererDispatcher_drawBatch.exists()) {
            Reflector.call(TileEntityRendererDispatcher.instance, Reflector.ForgeTileEntityRendererDispatcher_drawBatch, new Object[]{Integer.valueOf(pass)});
         }

         this.preRenderDamagedBlocks();
         var421 = this.damagedBlocks.values().iterator();

         while(var421.hasNext()) {
            DestroyBlockProgress var47 = (DestroyBlockProgress)var421.next();
            BlockPos var50 = var47.getPosition();
            var52 = this.theWorld.getTileEntity(var50);
            if(var52 instanceof TileEntityChest) {
               TileEntityChest var55 = (TileEntityChest)var52;
               if(var55.adjacentChestXNeg != null) {
                  var50 = var50.offset(EnumFacing.WEST);
                  var52 = this.theWorld.getTileEntity(var50);
               } else if(var55.adjacentChestZNeg != null) {
                  var50 = var50.offset(EnumFacing.NORTH);
                  var52 = this.theWorld.getTileEntity(var50);
               }
            }

            Block var56 = this.theWorld.getBlockState(var50).getBlock();
            boolean var58;
            if(forgeTileEntityPass) {
               var58 = false;
               if(var52 != null && Reflector.callBoolean(var52, Reflector.ForgeTileEntity_shouldRenderInPass, new Object[]{Integer.valueOf(pass)}) && Reflector.callBoolean(var52, Reflector.ForgeTileEntity_canRenderBreaking, new Object[0])) {
                  AxisAlignedBB aabb = (AxisAlignedBB)Reflector.call(var52, Reflector.ForgeTileEntity_getRenderBoundingBox, new Object[0]);
                  if(aabb != null) {
                     var58 = p_180446_2_.isBoundingBoxInFrustum(aabb);
                  }
               }
            } else {
               var58 = var52 != null && (var56 instanceof BlockChest || var56 instanceof BlockEnderChest || var56 instanceof BlockSign || var56 instanceof BlockSkull);
            }

            if(var58) {
               if(isShaders) {
                  Shaders.nextBlockEntity(var52);
               }

               TileEntityRendererDispatcher.instance.renderTileEntity(var52, partialTicks, var47.getPartialBlockDamage());
            }
         }

         this.postRenderDamagedBlocks();
         this.mc.entityRenderer.disableLightmap();
         this.mc.mcProfiler.endSection();
      }

   }

   public String getDebugInfoRenders() {
      int var1 = this.viewFrustum.renderChunks.length;
      int var2 = 0;
      Iterator var3 = this.renderInfos.iterator();

      while(var3.hasNext()) {
         RenderGlobal.ContainerLocalRenderInformation var4 = (RenderGlobal.ContainerLocalRenderInformation)var3.next();
         CompiledChunk var5 = var4.renderChunk.compiledChunk;
         if(var5 != CompiledChunk.DUMMY && !var5.isEmpty()) {
            ++var2;
         }
      }

      return String.format("C: %d/%d %sD: %d, %s", new Object[]{Integer.valueOf(var2), Integer.valueOf(var1), this.mc.renderChunksMany?"(s) ":"", Integer.valueOf(this.renderDistanceChunks), this.renderDispatcher.getDebugInfo()});
   }

   public String getDebugInfoEntities() {
      return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ", B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered) + ", " + Config.getVersionDebug();
   }

   public void setupTerrain(Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator) {
      if(this.mc.gameSettings.renderDistanceChunks != this.renderDistanceChunks) {
         this.loadRenderers();
      }

      this.theWorld.theProfiler.startSection("camera");
      double var7 = viewEntity.posX - this.frustumUpdatePosX;
      double var9 = viewEntity.posY - this.frustumUpdatePosY;
      double var11 = viewEntity.posZ - this.frustumUpdatePosZ;
      if(this.frustumUpdatePosChunkX != viewEntity.chunkCoordX || this.frustumUpdatePosChunkY != viewEntity.chunkCoordY || this.frustumUpdatePosChunkZ != viewEntity.chunkCoordZ || var7 * var7 + var9 * var9 + var11 * var11 > 16.0D) {
         this.frustumUpdatePosX = viewEntity.posX;
         this.frustumUpdatePosY = viewEntity.posY;
         this.frustumUpdatePosZ = viewEntity.posZ;
         this.frustumUpdatePosChunkX = viewEntity.chunkCoordX;
         this.frustumUpdatePosChunkY = viewEntity.chunkCoordY;
         this.frustumUpdatePosChunkZ = viewEntity.chunkCoordZ;
         this.viewFrustum.updateChunkPositions(viewEntity.posX, viewEntity.posZ);
      }

      if(Config.isDynamicLights()) {
         DynamicLights.update(this);
      }

      this.theWorld.theProfiler.endStartSection("renderlistcamera");
      double var13 = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * partialTicks;
      double var15 = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * partialTicks;
      double var17 = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * partialTicks;
      this.renderContainer.initialize(var13, var15, var17);
      this.theWorld.theProfiler.endStartSection("cull");
      if(this.debugFixedClippingHelper != null) {
         Frustum var35 = new Frustum(this.debugFixedClippingHelper);
         var35.setPosition(this.debugTerrainFrustumPosition.x, this.debugTerrainFrustumPosition.y, this.debugTerrainFrustumPosition.z);
         camera = var35;
      }

      this.mc.mcProfiler.endStartSection("culling");
      BlockPos var351 = new BlockPos(var13, var15 + (double)viewEntity.getEyeHeight(), var17);
      RenderChunk var20 = this.viewFrustum.getRenderChunk(var351);
      BlockPos var21 = new BlockPos(MathHelper.floor_double(var13 / 16.0D) * 16, MathHelper.floor_double(var15 / 16.0D) * 16, MathHelper.floor_double(var17 / 16.0D) * 16);
      this.displayListEntitiesDirty = this.displayListEntitiesDirty || !this.chunksToUpdate.isEmpty() || viewEntity.posX != this.lastViewEntityX || viewEntity.posY != this.lastViewEntityY || viewEntity.posZ != this.lastViewEntityZ || (double)viewEntity.rotationPitch != this.lastViewEntityPitch || (double)viewEntity.rotationYaw != this.lastViewEntityYaw;
      this.lastViewEntityX = viewEntity.posX;
      this.lastViewEntityY = viewEntity.posY;
      this.lastViewEntityZ = viewEntity.posZ;
      this.lastViewEntityPitch = (double)viewEntity.rotationPitch;
      this.lastViewEntityYaw = (double)viewEntity.rotationYaw;
      boolean var22 = this.debugFixedClippingHelper != null;
      Lagometer.timerVisibility.start();
      if(Shaders.isShadowPass) {
         this.renderInfos = this.renderInfosShadow;
         this.renderInfosEntities = this.renderInfosEntitiesShadow;
         this.renderInfosTileEntities = this.renderInfosTileEntitiesShadow;
         if(!var22 && this.displayListEntitiesDirty) {
            this.renderInfos.clear();
            this.renderInfosEntities.clear();
            this.renderInfosTileEntities.clear();
            RenderInfoLazy var39 = new RenderInfoLazy();
            Iterator var41 = ShadowUtils.makeShadowChunkIterator(this.theWorld, partialTicks, viewEntity, this.renderDistanceChunks, this.viewFrustum);

            while(var41.hasNext()) {
               RenderChunk var36 = (RenderChunk)var41.next();
               if(var36 != null) {
                  var39.setRenderChunk(var36);
                  if(!var36.compiledChunk.isEmpty() || var36.isNeedsUpdate()) {
                     this.renderInfos.add(var39.getRenderInfo());
                  }

                  BlockPos var37 = var36.getPosition();
                  if(ChunkUtils.hasEntities(this.theWorld.getChunkFromBlockCoords(var37))) {
                     this.renderInfosEntities.add(var39.getRenderInfo());
                  }

                  if(var36.getCompiledChunk().getTileEntities().size() > 0) {
                     this.renderInfosTileEntities.add(var39.getRenderInfo());
                  }
               }
            }
         }
      } else {
         this.renderInfos = this.renderInfosNormal;
         this.renderInfosEntities = this.renderInfosEntitiesNormal;
         this.renderInfosTileEntities = this.renderInfosTileEntitiesNormal;
      }

      RenderGlobal.ContainerLocalRenderInformation var361;
      RenderChunk var371;
      if(!var22 && this.displayListEntitiesDirty && !Shaders.isShadowPass) {
         this.displayListEntitiesDirty = false;
         this.renderInfos.clear();
         this.renderInfosEntities.clear();
         this.renderInfosTileEntities.clear();
         this.visibilityDeque.clear();
         Deque var38 = this.visibilityDeque;
         boolean var40 = this.mc.renderChunksMany;
         int var30;
         if(var20 == null) {
            int var46 = var351.getY() > 0?248:8;

            for(var30 = -this.renderDistanceChunks; var30 <= this.renderDistanceChunks; ++var30) {
               for(int var43 = -this.renderDistanceChunks; var43 <= this.renderDistanceChunks; ++var43) {
                  RenderChunk var45 = this.viewFrustum.getRenderChunk(new BlockPos((var30 << 4) + 8, var46, (var43 << 4) + 8));
                  if(var45 != null && ((ICamera)camera).isBoundingBoxInFrustum(var45.boundingBox)) {
                     var45.setFrameIndex(frameCount);
                     var38.add(new RenderGlobal.ContainerLocalRenderInformation(var45, (EnumFacing)null, 0, (Object)null));
                  }
               }
            }
         } else {
            boolean var42 = false;
            RenderGlobal.ContainerLocalRenderInformation var44 = new RenderGlobal.ContainerLocalRenderInformation(var20, (EnumFacing)null, 0, (Object)null);
            Set var451 = SET_ALL_FACINGS;
            if(var451.size() == 1) {
               Vector3f var47 = this.getViewVector(viewEntity, partialTicks);
               EnumFacing var31 = EnumFacing.getFacingFromVector(var47.x, var47.y, var47.z).getOpposite();
               var451.remove(var31);
            }

            if(var451.isEmpty()) {
               var42 = true;
            }

            if(var42 && !playerSpectator) {
               this.renderInfos.add(var44);
            } else {
               if(playerSpectator && this.theWorld.getBlockState(var351).getBlock().isOpaqueCube()) {
                  var40 = false;
               }

               var20.setFrameIndex(frameCount);
               var38.add(var44);
            }
         }

         EnumFacing[] var431 = EnumFacing.VALUES;
         var30 = var431.length;

         while(!var38.isEmpty()) {
            var361 = (RenderGlobal.ContainerLocalRenderInformation)var38.poll();
            var371 = var361.renderChunk;
            EnumFacing var461 = var361.facing;
            BlockPos var48 = var371.getPosition();
            if(!var371.compiledChunk.isEmpty() || var371.isNeedsUpdate()) {
               this.renderInfos.add(var361);
            }

            if(ChunkUtils.hasEntities(var371.getChunk())) {
               this.renderInfosEntities.add(var361);
            }

            if(var371.getCompiledChunk().getTileEntities().size() > 0) {
               this.renderInfosTileEntities.add(var361);
            }

            for(int var49 = 0; var49 < var30; ++var49) {
               EnumFacing var32 = var431[var49];
               if((!var40 || !var361.setFacing.contains(var32.getOpposite())) && (!var40 || var461 == null || var371.getCompiledChunk().isVisible(var461.getOpposite(), var32))) {
                  RenderChunk var33 = this.getRenderChunkOffset(var351, var371, var32);
                  if(var33 != null && var33.setFrameIndex(frameCount) && ((ICamera)camera).isBoundingBoxInFrustum(var33.boundingBox)) {
                     RenderGlobal.ContainerLocalRenderInformation var34 = new RenderGlobal.ContainerLocalRenderInformation(var33, var32, var361.counter + 1, (Object)null);
                     var34.setFacing.addAll(var361.setFacing);
                     var34.setFacing.add(var32);
                     var38.add(var34);
                  }
               }
            }
         }
      }

      if(this.debugFixTerrainFrustum) {
         this.fixTerrainFrustum(var13, var15, var17);
         this.debugFixTerrainFrustum = false;
      }

      Lagometer.timerVisibility.end();
      if(Shaders.isShadowPass) {
         Shaders.mcProfilerEndSection();
      } else {
         this.renderDispatcher.clearChunkUpdates();
         Set var391 = this.chunksToUpdate;
         this.chunksToUpdate = Sets.newLinkedHashSet();
         Iterator var411 = this.renderInfos.iterator();
         Lagometer.timerChunkUpdate.start();

         while(var411.hasNext()) {
            var361 = (RenderGlobal.ContainerLocalRenderInformation)var411.next();
            var371 = var361.renderChunk;
            if(var371.isNeedsUpdate() || var391.contains(var371)) {
               this.displayListEntitiesDirty = true;
               if(this.isPositionInRenderChunk(var21, var361.renderChunk)) {
                  if(!var371.isPlayerUpdate()) {
                     this.chunksToUpdateForced.add(var371);
                  } else {
                     this.mc.mcProfiler.startSection("build near");
                     this.renderDispatcher.updateChunkNow(var371);
                     var371.setNeedsUpdate(false);
                     this.mc.mcProfiler.endSection();
                  }
               } else {
                  this.chunksToUpdate.add(var371);
               }
            }
         }

         Lagometer.timerChunkUpdate.end();
         this.chunksToUpdate.addAll(var391);
         this.mc.mcProfiler.endSection();
      }
   }

   public boolean isPositionInRenderChunk(BlockPos p_174983_1_, RenderChunk p_174983_2_) {
      BlockPos var3 = p_174983_2_.getPosition();
      return MathHelper.abs_int(p_174983_1_.getX() - var3.getX()) > 16?false:(MathHelper.abs_int(p_174983_1_.getY() - var3.getY()) > 16?false:MathHelper.abs_int(p_174983_1_.getZ() - var3.getZ()) <= 16);
   }

   public Set getVisibleFacings(BlockPos p_174978_1_) {
      VisGraph var2 = new VisGraph();
      BlockPos var3 = new BlockPos(p_174978_1_.getX() >> 4 << 4, p_174978_1_.getY() >> 4 << 4, p_174978_1_.getZ() >> 4 << 4);
      Chunk var4 = this.theWorld.getChunkFromBlockCoords(var3);
      Iterator var5 = BlockPos.getAllInBoxMutable(var3, var3.add(15, 15, 15)).iterator();

      while(var5.hasNext()) {
         MutableBlockPos var6 = (MutableBlockPos)var5.next();
         if(var4.getBlock(var6).isOpaqueCube()) {
            var2.func_178606_a(var6);
         }
      }

      return var2.func_178609_b(p_174978_1_);
   }

   public RenderChunk getRenderChunkOffset(BlockPos p_174973_1_, RenderChunk renderChunk, EnumFacing p_174973_3_) {
      BlockPos var4 = renderChunk.getPositionOffset16(p_174973_3_);
      if(var4.getY() >= 0 && var4.getY() < 256) {
         int dx = MathHelper.abs_int(p_174973_1_.getX() - var4.getX());
         int dz = MathHelper.abs_int(p_174973_1_.getZ() - var4.getZ());
         if(Config.isFogOff()) {
            if(dx > this.renderDistance || dz > this.renderDistance) {
               return null;
            }
         } else {
            int distSq = dx * dx + dz * dz;
            if(distSq > this.renderDistanceSq) {
               return null;
            }
         }

         return this.viewFrustum.getRenderChunk(var4);
      } else {
         return null;
      }
   }

   public void fixTerrainFrustum(double p_174984_1_, double p_174984_3_, double p_174984_5_) {
      this.debugFixedClippingHelper = new ClippingHelperImpl();
      ((ClippingHelperImpl)this.debugFixedClippingHelper).init();
      Matrix4f var7 = new Matrix4f(this.debugFixedClippingHelper.modelviewMatrix);
      var7.transpose();
      Matrix4f var8 = new Matrix4f(this.debugFixedClippingHelper.projectionMatrix);
      var8.transpose();
      Matrix4f var9 = new Matrix4f();
      Matrix4f.mul(var8, var7, var9);
      var9.invert();
      this.debugTerrainFrustumPosition.x = p_174984_1_;
      this.debugTerrainFrustumPosition.y = p_174984_3_;
      this.debugTerrainFrustumPosition.z = p_174984_5_;
      this.debugTerrainMatrix[0] = new Vector4f(-1.0F, -1.0F, -1.0F, 1.0F);
      this.debugTerrainMatrix[1] = new Vector4f(1.0F, -1.0F, -1.0F, 1.0F);
      this.debugTerrainMatrix[2] = new Vector4f(1.0F, 1.0F, -1.0F, 1.0F);
      this.debugTerrainMatrix[3] = new Vector4f(-1.0F, 1.0F, -1.0F, 1.0F);
      this.debugTerrainMatrix[4] = new Vector4f(-1.0F, -1.0F, 1.0F, 1.0F);
      this.debugTerrainMatrix[5] = new Vector4f(1.0F, -1.0F, 1.0F, 1.0F);
      this.debugTerrainMatrix[6] = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
      this.debugTerrainMatrix[7] = new Vector4f(-1.0F, 1.0F, 1.0F, 1.0F);

      for(int var10 = 0; var10 < 8; ++var10) {
         Matrix4f.transform(var9, this.debugTerrainMatrix[var10], this.debugTerrainMatrix[var10]);
         this.debugTerrainMatrix[var10].x /= this.debugTerrainMatrix[var10].w;
         this.debugTerrainMatrix[var10].y /= this.debugTerrainMatrix[var10].w;
         this.debugTerrainMatrix[var10].z /= this.debugTerrainMatrix[var10].w;
         this.debugTerrainMatrix[var10].w = 1.0F;
      }

   }

   public Vector3f getViewVector(Entity p_500145_1_, double p_500145_2_) {
      float var4 = (float)((double)p_500145_1_.prevRotationPitch + (double)(p_500145_1_.rotationPitch - p_500145_1_.prevRotationPitch) * p_500145_2_);
      float var5 = (float)((double)p_500145_1_.prevRotationYaw + (double)(p_500145_1_.rotationYaw - p_500145_1_.prevRotationYaw) * p_500145_2_);
      if(Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
         var4 += 180.0F;
      }

      float var6 = MathHelper.cos(-var5 * 0.017453292F - 3.1415927F);
      float var7 = MathHelper.sin(-var5 * 0.017453292F - 3.1415927F);
      float var8 = -MathHelper.cos(-var4 * 0.017453292F);
      float var9 = MathHelper.sin(-var4 * 0.017453292F);
      return new Vector3f(var7 * var8, var9, var6 * var8);
   }

   public int renderBlockLayer(EnumWorldBlockLayer blockLayerIn, double partialTicks, int pass, Entity entityIn) {
      RenderHelper.disableStandardItemLighting();
      if(blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT) {
         this.mc.mcProfiler.startSection("translucent_sort");
         double var6 = entityIn.posX - this.prevRenderSortX;
         double var8 = entityIn.posY - this.prevRenderSortY;
         double var10 = entityIn.posZ - this.prevRenderSortZ;
         if(var6 * var6 + var8 * var8 + var10 * var10 > 1.0D) {
            this.prevRenderSortX = entityIn.posX;
            this.prevRenderSortY = entityIn.posY;
            this.prevRenderSortZ = entityIn.posZ;
            int var18 = 0;
            Iterator var13 = this.renderInfos.iterator();
            this.chunksToResortTransparency.clear();

            while(var13.hasNext()) {
               RenderGlobal.ContainerLocalRenderInformation var14 = (RenderGlobal.ContainerLocalRenderInformation)var13.next();
               if(var14.renderChunk.compiledChunk.isLayerStarted(blockLayerIn) && var18++ < 15) {
                  this.chunksToResortTransparency.add(var14.renderChunk);
               }
            }
         }

         this.mc.mcProfiler.endSection();
      }

      this.mc.mcProfiler.startSection("filterempty");
      int var15 = 0;
      boolean var7 = blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT;
      int var16 = var7?this.renderInfos.size() - 1:0;
      int var9 = var7?-1:this.renderInfos.size();
      int var17 = var7?-1:1;

      for(int var11 = var16; var11 != var9; var11 += var17) {
         RenderChunk var21 = ((RenderGlobal.ContainerLocalRenderInformation)this.renderInfos.get(var11)).renderChunk;
         if(!var21.getCompiledChunk().isLayerEmpty(blockLayerIn)) {
            ++var15;
            this.renderContainer.addRenderChunk(var21, blockLayerIn);
         }
      }

      if(var15 == 0) {
         this.mc.mcProfiler.endSection();
         return var15;
      } else {
         if(Config.isFogOff() && this.mc.entityRenderer.fogStandard) {
            GlStateManager.disableFog();
         }

         this.mc.mcProfiler.endStartSection("render_" + blockLayerIn);
         this.renderBlockLayer(blockLayerIn);
         this.mc.mcProfiler.endSection();
         return var15;
      }
   }

   public void renderBlockLayer(EnumWorldBlockLayer blockLayerIn) {
      this.mc.entityRenderer.enableLightmap();
      if(OpenGlHelper.useVbo()) {
         GL11.glEnableClientState('\u8074');
         OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
         GL11.glEnableClientState('\u8078');
         OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
         GL11.glEnableClientState('\u8078');
         OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
         GL11.glEnableClientState('\u8076');
      }

      if(Config.isShaders()) {
         ShadersRender.preRenderChunkLayer(blockLayerIn);
      }

      this.renderContainer.renderChunkLayer(blockLayerIn);
      if(Config.isShaders()) {
         ShadersRender.postRenderChunkLayer(blockLayerIn);
      }

      if(OpenGlHelper.useVbo()) {
         List var2 = DefaultVertexFormats.BLOCK.getElements();
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            VertexFormatElement var4 = (VertexFormatElement)var3.next();
            EnumUsage var5 = var4.getUsage();
            int var6 = var4.getIndex();
            switch(RenderGlobal.SwitchEnumUseage.field_178037_a[var5.ordinal()]) {
            case 1:
               GL11.glDisableClientState('\u8074');
               break;
            case 2:
               OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + var6);
               GL11.glDisableClientState('\u8078');
               OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
               break;
            case 3:
               GL11.glDisableClientState('\u8076');
               GlStateManager.resetColor();
            }
         }
      }

      this.mc.entityRenderer.disableLightmap();
   }

   public void cleanupDamagedBlocks(Iterator p_174965_1_) {
      while(p_174965_1_.hasNext()) {
         DestroyBlockProgress var2 = (DestroyBlockProgress)p_174965_1_.next();
         int var3 = var2.getCreationCloudUpdateTick();
         if(this.cloudTickCounter - var3 > 400) {
            p_174965_1_.remove();
         }
      }

   }

   public void updateClouds() {
      if(Config.isShaders() && Keyboard.isKeyDown(61) && Keyboard.isKeyDown(19)) {
         Shaders.uninit();
         Shaders.loadShaderPack();
      }

      ++this.cloudTickCounter;
      if(this.cloudTickCounter % 20 == 0) {
         this.cleanupDamagedBlocks(this.damagedBlocks.values().iterator());
      }

   }

   public void renderSkyEnd() {
      if(Config.isSkyEnabled()) {
         GlStateManager.disableFog();
         GlStateManager.disableAlpha();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         RenderHelper.disableStandardItemLighting();
         GlStateManager.depthMask(false);
         this.renderEngine.bindTexture(locationEndSkyPng);
         Tessellator var1 = Tessellator.getInstance();
         WorldRenderer var2 = var1.getWorldRenderer();

         for(int var3 = 0; var3 < 6; ++var3) {
            GlStateManager.pushMatrix();
            if(var3 == 1) {
               GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            }

            if(var3 == 2) {
               GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            }

            if(var3 == 3) {
               GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            }

            if(var3 == 4) {
               GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
            }

            if(var3 == 5) {
               GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
            }

            var2.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            var2.pos(-100.0D, -100.0D, -100.0D).tex(0.0D, 0.0D).color(40, 40, 40, 255).endVertex();
            var2.pos(-100.0D, -100.0D, 100.0D).tex(0.0D, 16.0D).color(40, 40, 40, 255).endVertex();
            var2.pos(100.0D, -100.0D, 100.0D).tex(16.0D, 16.0D).color(40, 40, 40, 255).endVertex();
            var2.pos(100.0D, -100.0D, -100.0D).tex(16.0D, 0.0D).color(40, 40, 40, 255).endVertex();
            var1.draw();
            GlStateManager.popMatrix();
         }

         GlStateManager.depthMask(true);
         GlStateManager.enableTexture2D();
         GlStateManager.enableAlpha();
         GlStateManager.disableBlend();
      }
   }

   public void renderSky(float partialTicks, int pass) {
      if(Reflector.ForgeWorldProvider_getSkyRenderer.exists()) {
         WorldProvider isShaders = this.mc.theWorld.provider;
         Object var3 = Reflector.call(isShaders, Reflector.ForgeWorldProvider_getSkyRenderer, new Object[0]);
         if(var3 != null) {
            Reflector.callVoid(var3, Reflector.IRenderHandler_render, new Object[]{Float.valueOf(partialTicks), this.theWorld, this.mc});
            return;
         }
      }

      if(this.mc.theWorld.provider.getDimensionId() == 1) {
         this.renderSkyEnd();
      } else if(this.mc.theWorld.provider.isSurfaceWorld()) {
         GlStateManager.disableTexture2D();
         boolean var25 = Config.isShaders();
         if(var25) {
            Shaders.disableTexture2D();
         }

         Vec3 var261 = this.theWorld.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
         var261 = CustomColors.getSkyColor(var261, this.mc.theWorld, this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().posY + 1.0D, this.mc.getRenderViewEntity().posZ);
         if(var25) {
            Shaders.setSkyColor(var261);
         }

         float var4 = (float)var261.xCoord;
         float var5 = (float)var261.yCoord;
         float var6 = (float)var261.zCoord;
         if(pass != 2) {
            float var20 = (var4 * 30.0F + var5 * 59.0F + var6 * 11.0F) / 100.0F;
            float var21 = (var4 * 30.0F + var5 * 70.0F) / 100.0F;
            float var22 = (var4 * 30.0F + var6 * 70.0F) / 100.0F;
            var4 = var20;
            var5 = var21;
            var6 = var22;
         }

         GlStateManager.color(var4, var5, var6);
         Tessellator var271 = Tessellator.getInstance();
         WorldRenderer var28 = var271.getWorldRenderer();
         GlStateManager.depthMask(false);
         GlStateManager.enableFog();
         if(var25) {
            Shaders.enableFog();
         }

         GlStateManager.color(var4, var5, var6);
         if(var25) {
            Shaders.preSkyList();
         }

         if(Config.isSkyEnabled()) {
            if(this.vboEnabled) {
               this.skyVBO.bindBuffer();
               GL11.glEnableClientState('\u8074');
               GL11.glVertexPointer(3, 5126, 12, 0L);
               this.skyVBO.drawArrays(7);
               this.skyVBO.unbindBuffer();
               GL11.glDisableClientState('\u8074');
            } else {
               GlStateManager.callList(this.glSkyList);
            }
         }

         GlStateManager.disableFog();
         if(var25) {
            Shaders.disableFog();
         }

         GlStateManager.disableAlpha();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         RenderHelper.disableStandardItemLighting();
         float[] var29 = this.theWorld.provider.calcSunriseSunsetColors(this.theWorld.getCelestialAngle(partialTicks), partialTicks);
         float var10;
         float var11;
         float var12;
         float var13;
         float var14;
         float var15;
         int var27;
         float var16;
         float var17;
         if(var29 != null && Config.isSunMoonEnabled()) {
            GlStateManager.disableTexture2D();
            if(var25) {
               Shaders.disableTexture2D();
            }

            GlStateManager.shadeModel(7425);
            GlStateManager.pushMatrix();
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(MathHelper.sin(this.theWorld.getCelestialAngleRadians(partialTicks)) < 0.0F?180.0F:0.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
            var10 = var29[0];
            var11 = var29[1];
            var12 = var29[2];
            if(pass != 2) {
               var13 = (var10 * 30.0F + var11 * 59.0F + var12 * 11.0F) / 100.0F;
               var14 = (var10 * 30.0F + var11 * 70.0F) / 100.0F;
               var15 = (var10 * 30.0F + var12 * 70.0F) / 100.0F;
               var10 = var13;
               var11 = var14;
               var12 = var15;
            }

            var28.begin(6, DefaultVertexFormats.POSITION_COLOR);
            var28.pos(0.0D, 100.0D, 0.0D).color(var10, var11, var12, var29[3]).endVertex();
            boolean var19 = true;

            for(var27 = 0; var27 <= 16; ++var27) {
               var15 = (float)var27 * 3.1415927F * 2.0F / 16.0F;
               var16 = MathHelper.sin(var15);
               var17 = MathHelper.cos(var15);
               var28.pos((double)(var16 * 120.0F), (double)(var17 * 120.0F), (double)(-var17 * 40.0F * var29[3])).color(var29[0], var29[1], var29[2], 0.0F).endVertex();
            }

            var271.draw();
            GlStateManager.popMatrix();
            GlStateManager.shadeModel(7424);
         }

         GlStateManager.enableTexture2D();
         if(var25) {
            Shaders.enableTexture2D();
         }

         GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
         GlStateManager.pushMatrix();
         var10 = 1.0F - this.theWorld.getRainStrength(partialTicks);
         GlStateManager.color(1.0F, 1.0F, 1.0F, var10);
         GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
         CustomSky.renderSky(this.theWorld, this.renderEngine, partialTicks);
         if(var25) {
            Shaders.preCelestialRotate();
         }

         GlStateManager.rotate(this.theWorld.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
         if(var25) {
            Shaders.postCelestialRotate();
         }

         var11 = 30.0F;
         if(Config.isSunTexture()) {
            this.renderEngine.bindTexture(locationSunPng);
            var28.begin(7, DefaultVertexFormats.POSITION_TEX);
            var28.pos((double)(-var11), 100.0D, (double)(-var11)).tex(0.0D, 0.0D).endVertex();
            var28.pos((double)var11, 100.0D, (double)(-var11)).tex(1.0D, 0.0D).endVertex();
            var28.pos((double)var11, 100.0D, (double)var11).tex(1.0D, 1.0D).endVertex();
            var28.pos((double)(-var11), 100.0D, (double)var11).tex(0.0D, 1.0D).endVertex();
            var271.draw();
         }

         var11 = 20.0F;
         if(Config.isMoonTexture()) {
            this.renderEngine.bindTexture(locationMoonPhasesPng);
            int var30 = this.theWorld.getMoonPhase();
            int var26 = var30 % 4;
            var27 = var30 / 4 % 2;
            var15 = (float)(var26 + 0) / 4.0F;
            var16 = (float)(var27 + 0) / 2.0F;
            var17 = (float)(var26 + 1) / 4.0F;
            float var18 = (float)(var27 + 1) / 2.0F;
            var28.begin(7, DefaultVertexFormats.POSITION_TEX);
            var28.pos((double)(-var11), -100.0D, (double)var11).tex((double)var17, (double)var18).endVertex();
            var28.pos((double)var11, -100.0D, (double)var11).tex((double)var15, (double)var18).endVertex();
            var28.pos((double)var11, -100.0D, (double)(-var11)).tex((double)var15, (double)var16).endVertex();
            var28.pos((double)(-var11), -100.0D, (double)(-var11)).tex((double)var17, (double)var16).endVertex();
            var271.draw();
         }

         GlStateManager.disableTexture2D();
         if(var25) {
            Shaders.disableTexture2D();
         }

         float var31 = this.theWorld.getStarBrightness(partialTicks) * var10;
         if(var31 > 0.0F && Config.isStarsEnabled() && !CustomSky.hasSkyLayers(this.theWorld)) {
            GlStateManager.color(var31, var31, var31, var31);
            if(this.vboEnabled) {
               this.starVBO.bindBuffer();
               GL11.glEnableClientState('\u8074');
               GL11.glVertexPointer(3, 5126, 12, 0L);
               this.starVBO.drawArrays(7);
               this.starVBO.unbindBuffer();
               GL11.glDisableClientState('\u8074');
            } else {
               GlStateManager.callList(this.starGLCallList);
            }
         }

         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.disableBlend();
         GlStateManager.enableAlpha();
         GlStateManager.enableFog();
         if(var25) {
            Shaders.enableFog();
         }

         GlStateManager.popMatrix();
         GlStateManager.disableTexture2D();
         if(var25) {
            Shaders.disableTexture2D();
         }

         GlStateManager.color(0.0F, 0.0F, 0.0F);
         double var23 = this.mc.thePlayer.getPositionEyes(partialTicks).yCoord - this.theWorld.getHorizon();
         if(var23 < 0.0D) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 12.0F, 0.0F);
            if(this.vboEnabled) {
               this.sky2VBO.bindBuffer();
               GL11.glEnableClientState('\u8074');
               GL11.glVertexPointer(3, 5126, 12, 0L);
               this.sky2VBO.drawArrays(7);
               this.sky2VBO.unbindBuffer();
               GL11.glDisableClientState('\u8074');
            } else {
               GlStateManager.callList(this.glSkyList2);
            }

            GlStateManager.popMatrix();
            var12 = 1.0F;
            var13 = -((float)(var23 + 65.0D));
            var14 = -1.0F;
            var28.begin(7, DefaultVertexFormats.POSITION_COLOR);
            var28.pos(-1.0D, (double)var13, 1.0D).color(0, 0, 0, 255).endVertex();
            var28.pos(1.0D, (double)var13, 1.0D).color(0, 0, 0, 255).endVertex();
            var28.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            var28.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            var28.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
            var28.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
            var28.pos(1.0D, (double)var13, -1.0D).color(0, 0, 0, 255).endVertex();
            var28.pos(-1.0D, (double)var13, -1.0D).color(0, 0, 0, 255).endVertex();
            var28.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
            var28.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            var28.pos(1.0D, (double)var13, 1.0D).color(0, 0, 0, 255).endVertex();
            var28.pos(1.0D, (double)var13, -1.0D).color(0, 0, 0, 255).endVertex();
            var28.pos(-1.0D, (double)var13, -1.0D).color(0, 0, 0, 255).endVertex();
            var28.pos(-1.0D, (double)var13, 1.0D).color(0, 0, 0, 255).endVertex();
            var28.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            var28.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
            var28.pos(-1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
            var28.pos(-1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            var28.pos(1.0D, -1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            var28.pos(1.0D, -1.0D, -1.0D).color(0, 0, 0, 255).endVertex();
            var271.draw();
         }

         if(this.theWorld.provider.isSkyColored()) {
            GlStateManager.color(var4 * 0.2F + 0.04F, var5 * 0.2F + 0.04F, var6 * 0.6F + 0.1F);
         } else {
            GlStateManager.color(var4, var5, var6);
         }

         if(this.mc.gameSettings.renderDistanceChunks <= 4) {
            GlStateManager.color(this.mc.entityRenderer.fogColorRed, this.mc.entityRenderer.fogColorGreen, this.mc.entityRenderer.fogColorBlue);
         }

         GlStateManager.pushMatrix();
         GlStateManager.translate(0.0F, -((float)(var23 - 16.0D)), 0.0F);
         if(Config.isSkyEnabled()) {
            if(this.vboEnabled) {
               this.sky2VBO.bindBuffer();
               GL11.glEnableClientState('\u8074');
               GL11.glVertexPointer(3, 5126, 12, 0L);
               this.sky2VBO.drawArrays(7);
               this.sky2VBO.unbindBuffer();
               GL11.glDisableClientState('\u8074');
            } else {
               GlStateManager.callList(this.glSkyList2);
            }
         }

         GlStateManager.popMatrix();
         GlStateManager.enableTexture2D();
         if(var25) {
            Shaders.enableTexture2D();
         }

         GlStateManager.depthMask(true);
      }

   }

   public void renderClouds(float p_180447_1_, int p_180447_2_) {
      if(!Config.isCloudsOff()) {
         if(Reflector.ForgeWorldProvider_getCloudRenderer.exists()) {
            WorldProvider partialTicks = this.mc.theWorld.provider;
            Object var3 = Reflector.call(partialTicks, Reflector.ForgeWorldProvider_getCloudRenderer, new Object[0]);
            if(var3 != null) {
               Reflector.callVoid(var3, Reflector.IRenderHandler_render, new Object[]{Float.valueOf(p_180447_1_), this.theWorld, this.mc});
               return;
            }
         }

         if(this.mc.theWorld.provider.isSurfaceWorld()) {
            if(Config.isShaders()) {
               Shaders.beginClouds();
            }

            if(Config.isCloudsFancy()) {
               this.renderCloudsFancy(p_180447_1_, p_180447_2_);
            } else {
               float partialTicks1 = p_180447_1_;
               p_180447_1_ = 0.0F;
               GlStateManager.disableCull();
               float var31 = (float)(this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().posY - this.mc.getRenderViewEntity().lastTickPosY) * (double)p_180447_1_);
               boolean var4 = true;
               boolean var5 = true;
               Tessellator var6 = Tessellator.getInstance();
               WorldRenderer var7 = var6.getWorldRenderer();
               this.renderEngine.bindTexture(locationCloudsPng);
               GlStateManager.enableBlend();
               GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
               Vec3 var8 = this.theWorld.getCloudColour(p_180447_1_);
               float var9 = (float)var8.xCoord;
               float var10 = (float)var8.yCoord;
               float var11 = (float)var8.zCoord;
               this.cloudRenderer.prepareToRender(false, this.cloudTickCounter, partialTicks1, var8);
               if(this.cloudRenderer.shouldUpdateGlList()) {
                  this.cloudRenderer.startUpdateGlList();
                  float var12;
                  if(p_180447_2_ != 2) {
                     var12 = (var9 * 30.0F + var10 * 59.0F + var11 * 11.0F) / 100.0F;
                     float var13 = (var9 * 30.0F + var10 * 70.0F) / 100.0F;
                     float var14 = (var9 * 30.0F + var11 * 70.0F) / 100.0F;
                     var9 = var12;
                     var10 = var13;
                     var11 = var14;
                  }

                  var12 = 4.8828125E-4F;
                  double var26 = (double)((float)this.cloudTickCounter + p_180447_1_);
                  double var15 = this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().posX - this.mc.getRenderViewEntity().prevPosX) * (double)p_180447_1_ + var26 * 0.029999999329447746D;
                  double var17 = this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().posZ - this.mc.getRenderViewEntity().prevPosZ) * (double)p_180447_1_;
                  int var19 = MathHelper.floor_double(var15 / 2048.0D);
                  int var20 = MathHelper.floor_double(var17 / 2048.0D);
                  var15 -= (double)(var19 * 2048);
                  var17 -= (double)(var20 * 2048);
                  float var21 = this.theWorld.provider.getCloudHeight() - var31 + 0.33F;
                  var21 += this.mc.gameSettings.ofCloudsHeight * 128.0F;
                  float var22 = (float)(var15 * 4.8828125E-4D);
                  float var23 = (float)(var17 * 4.8828125E-4D);
                  var7.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

                  for(int var24 = -256; var24 < 256; var24 += 32) {
                     for(int var25 = -256; var25 < 256; var25 += 32) {
                        var7.pos((double)(var24 + 0), (double)var21, (double)(var25 + 32)).tex((double)((float)(var24 + 0) * 4.8828125E-4F + var22), (double)((float)(var25 + 32) * 4.8828125E-4F + var23)).color(var9, var10, var11, 0.8F).endVertex();
                        var7.pos((double)(var24 + 32), (double)var21, (double)(var25 + 32)).tex((double)((float)(var24 + 32) * 4.8828125E-4F + var22), (double)((float)(var25 + 32) * 4.8828125E-4F + var23)).color(var9, var10, var11, 0.8F).endVertex();
                        var7.pos((double)(var24 + 32), (double)var21, (double)(var25 + 0)).tex((double)((float)(var24 + 32) * 4.8828125E-4F + var22), (double)((float)(var25 + 0) * 4.8828125E-4F + var23)).color(var9, var10, var11, 0.8F).endVertex();
                        var7.pos((double)(var24 + 0), (double)var21, (double)(var25 + 0)).tex((double)((float)(var24 + 0) * 4.8828125E-4F + var22), (double)((float)(var25 + 0) * 4.8828125E-4F + var23)).color(var9, var10, var11, 0.8F).endVertex();
                     }
                  }

                  var6.draw();
                  this.cloudRenderer.endUpdateGlList();
               }

               this.cloudRenderer.renderGlList();
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.disableBlend();
               GlStateManager.enableCull();
            }

            if(Config.isShaders()) {
               Shaders.endClouds();
            }
         }

      }
   }

   public boolean hasCloudFog(double p_72721_1_, double p_72721_3_, double p_72721_5_, float p_72721_7_) {
      return false;
   }

   public void renderCloudsFancy(float p_180445_1_, int p_180445_2_) {
      float partialTicks = p_180445_1_;
      p_180445_1_ = 0.0F;
      GlStateManager.disableCull();
      float var3 = (float)(this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().posY - this.mc.getRenderViewEntity().lastTickPosY) * (double)p_180445_1_);
      Tessellator var4 = Tessellator.getInstance();
      WorldRenderer var5 = var4.getWorldRenderer();
      float var6 = 12.0F;
      float var7 = 4.0F;
      double var8 = (double)((float)this.cloudTickCounter + p_180445_1_);
      double var10 = (this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().posX - this.mc.getRenderViewEntity().prevPosX) * (double)p_180445_1_ + var8 * 0.029999999329447746D) / 12.0D;
      double var12 = (this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().posZ - this.mc.getRenderViewEntity().prevPosZ) * (double)p_180445_1_) / 12.0D + 0.33000001311302185D;
      float var14 = this.theWorld.provider.getCloudHeight() - var3 + 0.33F;
      var14 += this.mc.gameSettings.ofCloudsHeight * 128.0F;
      int var15 = MathHelper.floor_double(var10 / 2048.0D);
      int var16 = MathHelper.floor_double(var12 / 2048.0D);
      var10 -= (double)(var15 * 2048);
      var12 -= (double)(var16 * 2048);
      this.renderEngine.bindTexture(locationCloudsPng);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      Vec3 var17 = this.theWorld.getCloudColour(p_180445_1_);
      float var18 = (float)var17.xCoord;
      float var19 = (float)var17.yCoord;
      float var20 = (float)var17.zCoord;
      this.cloudRenderer.prepareToRender(true, this.cloudTickCounter, partialTicks, var17);
      float var21;
      float var22;
      float var23;
      if(p_180445_2_ != 2) {
         var21 = (var18 * 30.0F + var19 * 59.0F + var20 * 11.0F) / 100.0F;
         var22 = (var18 * 30.0F + var19 * 70.0F) / 100.0F;
         var23 = (var18 * 30.0F + var20 * 70.0F) / 100.0F;
         var18 = var21;
         var19 = var22;
         var20 = var23;
      }

      var21 = var18 * 0.9F;
      var22 = var19 * 0.9F;
      var23 = var20 * 0.9F;
      float var24 = var18 * 0.7F;
      float var25 = var19 * 0.7F;
      float var26 = var20 * 0.7F;
      float var27 = var18 * 0.8F;
      float var28 = var19 * 0.8F;
      float var29 = var20 * 0.8F;
      float var30 = 0.00390625F;
      float var31 = (float)MathHelper.floor_double(var10) * 0.00390625F;
      float var32 = (float)MathHelper.floor_double(var12) * 0.00390625F;
      float var33 = (float)(var10 - (double)MathHelper.floor_double(var10));
      float var34 = (float)(var12 - (double)MathHelper.floor_double(var12));
      boolean var35 = true;
      boolean var36 = true;
      float var37 = 9.765625E-4F;
      GlStateManager.scale(12.0F, 1.0F, 12.0F);

      int var39;
      for(var39 = 0; var39 < 2; ++var39) {
         if(var39 == 0) {
            GlStateManager.colorMask(false, false, false, false);
         } else {
            switch(p_180445_2_) {
            case 0:
               GlStateManager.colorMask(false, true, true, true);
               break;
            case 1:
               GlStateManager.colorMask(true, false, false, true);
               break;
            case 2:
               GlStateManager.colorMask(true, true, true, true);
            }
         }

         this.cloudRenderer.renderGlList();
      }

      if(this.cloudRenderer.shouldUpdateGlList()) {
         this.cloudRenderer.startUpdateGlList();

         for(var39 = -3; var39 <= 4; ++var39) {
            for(int var40 = -3; var40 <= 4; ++var40) {
               var5.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
               float var41 = (float)(var39 * 8);
               float var42 = (float)(var40 * 8);
               float var43 = var41 - var33;
               float var44 = var42 - var34;
               if(var14 > -5.0F) {
                  var5.pos((double)(var43 + 0.0F), (double)(var14 + 0.0F), (double)(var44 + 8.0F)).tex((double)((var41 + 0.0F) * 0.00390625F + var31), (double)((var42 + 8.0F) * 0.00390625F + var32)).color(var24, var25, var26, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                  var5.pos((double)(var43 + 8.0F), (double)(var14 + 0.0F), (double)(var44 + 8.0F)).tex((double)((var41 + 8.0F) * 0.00390625F + var31), (double)((var42 + 8.0F) * 0.00390625F + var32)).color(var24, var25, var26, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                  var5.pos((double)(var43 + 8.0F), (double)(var14 + 0.0F), (double)(var44 + 0.0F)).tex((double)((var41 + 8.0F) * 0.00390625F + var31), (double)((var42 + 0.0F) * 0.00390625F + var32)).color(var24, var25, var26, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                  var5.pos((double)(var43 + 0.0F), (double)(var14 + 0.0F), (double)(var44 + 0.0F)).tex((double)((var41 + 0.0F) * 0.00390625F + var31), (double)((var42 + 0.0F) * 0.00390625F + var32)).color(var24, var25, var26, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
               }

               if(var14 <= 5.0F) {
                  var5.pos((double)(var43 + 0.0F), (double)(var14 + 4.0F - 9.765625E-4F), (double)(var44 + 8.0F)).tex((double)((var41 + 0.0F) * 0.00390625F + var31), (double)((var42 + 8.0F) * 0.00390625F + var32)).color(var18, var19, var20, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                  var5.pos((double)(var43 + 8.0F), (double)(var14 + 4.0F - 9.765625E-4F), (double)(var44 + 8.0F)).tex((double)((var41 + 8.0F) * 0.00390625F + var31), (double)((var42 + 8.0F) * 0.00390625F + var32)).color(var18, var19, var20, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                  var5.pos((double)(var43 + 8.0F), (double)(var14 + 4.0F - 9.765625E-4F), (double)(var44 + 0.0F)).tex((double)((var41 + 8.0F) * 0.00390625F + var31), (double)((var42 + 0.0F) * 0.00390625F + var32)).color(var18, var19, var20, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                  var5.pos((double)(var43 + 0.0F), (double)(var14 + 4.0F - 9.765625E-4F), (double)(var44 + 0.0F)).tex((double)((var41 + 0.0F) * 0.00390625F + var31), (double)((var42 + 0.0F) * 0.00390625F + var32)).color(var18, var19, var20, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
               }

               int var45;
               if(var39 > -1) {
                  for(var45 = 0; var45 < 8; ++var45) {
                     var5.pos((double)(var43 + (float)var45 + 0.0F), (double)(var14 + 0.0F), (double)(var44 + 8.0F)).tex((double)((var41 + (float)var45 + 0.5F) * 0.00390625F + var31), (double)((var42 + 8.0F) * 0.00390625F + var32)).color(var21, var22, var23, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                     var5.pos((double)(var43 + (float)var45 + 0.0F), (double)(var14 + 4.0F), (double)(var44 + 8.0F)).tex((double)((var41 + (float)var45 + 0.5F) * 0.00390625F + var31), (double)((var42 + 8.0F) * 0.00390625F + var32)).color(var21, var22, var23, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                     var5.pos((double)(var43 + (float)var45 + 0.0F), (double)(var14 + 4.0F), (double)(var44 + 0.0F)).tex((double)((var41 + (float)var45 + 0.5F) * 0.00390625F + var31), (double)((var42 + 0.0F) * 0.00390625F + var32)).color(var21, var22, var23, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                     var5.pos((double)(var43 + (float)var45 + 0.0F), (double)(var14 + 0.0F), (double)(var44 + 0.0F)).tex((double)((var41 + (float)var45 + 0.5F) * 0.00390625F + var31), (double)((var42 + 0.0F) * 0.00390625F + var32)).color(var21, var22, var23, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                  }
               }

               if(var39 <= 1) {
                  for(var45 = 0; var45 < 8; ++var45) {
                     var5.pos((double)(var43 + (float)var45 + 1.0F - 9.765625E-4F), (double)(var14 + 0.0F), (double)(var44 + 8.0F)).tex((double)((var41 + (float)var45 + 0.5F) * 0.00390625F + var31), (double)((var42 + 8.0F) * 0.00390625F + var32)).color(var21, var22, var23, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                     var5.pos((double)(var43 + (float)var45 + 1.0F - 9.765625E-4F), (double)(var14 + 4.0F), (double)(var44 + 8.0F)).tex((double)((var41 + (float)var45 + 0.5F) * 0.00390625F + var31), (double)((var42 + 8.0F) * 0.00390625F + var32)).color(var21, var22, var23, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                     var5.pos((double)(var43 + (float)var45 + 1.0F - 9.765625E-4F), (double)(var14 + 4.0F), (double)(var44 + 0.0F)).tex((double)((var41 + (float)var45 + 0.5F) * 0.00390625F + var31), (double)((var42 + 0.0F) * 0.00390625F + var32)).color(var21, var22, var23, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                     var5.pos((double)(var43 + (float)var45 + 1.0F - 9.765625E-4F), (double)(var14 + 0.0F), (double)(var44 + 0.0F)).tex((double)((var41 + (float)var45 + 0.5F) * 0.00390625F + var31), (double)((var42 + 0.0F) * 0.00390625F + var32)).color(var21, var22, var23, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                  }
               }

               if(var40 > -1) {
                  for(var45 = 0; var45 < 8; ++var45) {
                     var5.pos((double)(var43 + 0.0F), (double)(var14 + 4.0F), (double)(var44 + (float)var45 + 0.0F)).tex((double)((var41 + 0.0F) * 0.00390625F + var31), (double)((var42 + (float)var45 + 0.5F) * 0.00390625F + var32)).color(var27, var28, var29, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                     var5.pos((double)(var43 + 8.0F), (double)(var14 + 4.0F), (double)(var44 + (float)var45 + 0.0F)).tex((double)((var41 + 8.0F) * 0.00390625F + var31), (double)((var42 + (float)var45 + 0.5F) * 0.00390625F + var32)).color(var27, var28, var29, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                     var5.pos((double)(var43 + 8.0F), (double)(var14 + 0.0F), (double)(var44 + (float)var45 + 0.0F)).tex((double)((var41 + 8.0F) * 0.00390625F + var31), (double)((var42 + (float)var45 + 0.5F) * 0.00390625F + var32)).color(var27, var28, var29, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                     var5.pos((double)(var43 + 0.0F), (double)(var14 + 0.0F), (double)(var44 + (float)var45 + 0.0F)).tex((double)((var41 + 0.0F) * 0.00390625F + var31), (double)((var42 + (float)var45 + 0.5F) * 0.00390625F + var32)).color(var27, var28, var29, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                  }
               }

               if(var40 <= 1) {
                  for(var45 = 0; var45 < 8; ++var45) {
                     var5.pos((double)(var43 + 0.0F), (double)(var14 + 4.0F), (double)(var44 + (float)var45 + 1.0F - 9.765625E-4F)).tex((double)((var41 + 0.0F) * 0.00390625F + var31), (double)((var42 + (float)var45 + 0.5F) * 0.00390625F + var32)).color(var27, var28, var29, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                     var5.pos((double)(var43 + 8.0F), (double)(var14 + 4.0F), (double)(var44 + (float)var45 + 1.0F - 9.765625E-4F)).tex((double)((var41 + 8.0F) * 0.00390625F + var31), (double)((var42 + (float)var45 + 0.5F) * 0.00390625F + var32)).color(var27, var28, var29, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                     var5.pos((double)(var43 + 8.0F), (double)(var14 + 0.0F), (double)(var44 + (float)var45 + 1.0F - 9.765625E-4F)).tex((double)((var41 + 8.0F) * 0.00390625F + var31), (double)((var42 + (float)var45 + 0.5F) * 0.00390625F + var32)).color(var27, var28, var29, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                     var5.pos((double)(var43 + 0.0F), (double)(var14 + 0.0F), (double)(var44 + (float)var45 + 1.0F - 9.765625E-4F)).tex((double)((var41 + 0.0F) * 0.00390625F + var31), (double)((var42 + (float)var45 + 0.5F) * 0.00390625F + var32)).color(var27, var28, var29, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                  }
               }

               var4.draw();
            }
         }

         this.cloudRenderer.endUpdateGlList();
      }

      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.disableBlend();
      GlStateManager.enableCull();
   }

   public void updateChunks(long p_174967_1_) {
      p_174967_1_ = (long)((double)p_174967_1_ + 1.0E8D);
      this.displayListEntitiesDirty |= this.renderDispatcher.runChunkUploads(p_174967_1_);
      Iterator countUpdated;
      RenderChunk updatesPerFrame;
      if(this.chunksToUpdateForced.size() > 0) {
         countUpdated = this.chunksToUpdateForced.iterator();

         while(countUpdated.hasNext()) {
            updatesPerFrame = (RenderChunk)countUpdated.next();
            if(!this.renderDispatcher.updateChunkLater(updatesPerFrame)) {
               break;
            }

            updatesPerFrame.setNeedsUpdate(false);
            countUpdated.remove();
            this.chunksToUpdate.remove(updatesPerFrame);
            this.chunksToResortTransparency.remove(updatesPerFrame);
         }
      }

      if(this.chunksToResortTransparency.size() > 0) {
         countUpdated = this.chunksToResortTransparency.iterator();
         if(countUpdated.hasNext()) {
            updatesPerFrame = (RenderChunk)countUpdated.next();
            if(this.renderDispatcher.updateTransparencyLater(updatesPerFrame)) {
               countUpdated.remove();
            }
         }
      }

      int var8 = 0;
      int var9 = Config.getUpdatesPerFrame();
      Iterator var3 = this.chunksToUpdate.iterator();

      while(var3.hasNext()) {
         RenderChunk var4 = (RenderChunk)var3.next();
         boolean empty = var4.isChunkRegionEmpty();
         if(empty) {
            if(!this.renderDispatcher.updateChunkNow(var4)) {
               break;
            }
         } else if(!this.renderDispatcher.updateChunkLater(var4)) {
            break;
         }

         var4.setNeedsUpdate(false);
         var3.remove();
         if(!empty) {
            ++var8;
            if(var8 >= var9) {
               break;
            }
         }
      }

   }

   public void renderWorldBorder(Entity p_180449_1_, float p_180449_2_) {
      Tessellator var3 = Tessellator.getInstance();
      WorldRenderer var4 = var3.getWorldRenderer();
      WorldBorder var5 = this.theWorld.getWorldBorder();
      double var6 = (double)(this.mc.gameSettings.renderDistanceChunks * 16);
      if(p_180449_1_.posX >= var5.maxX() - var6 || p_180449_1_.posX <= var5.minX() + var6 || p_180449_1_.posZ >= var5.maxZ() - var6 || p_180449_1_.posZ <= var5.minZ() + var6) {
         double var8 = 1.0D - var5.getClosestDistance(p_180449_1_) / var6;
         var8 = Math.pow(var8, 4.0D);
         double var10 = p_180449_1_.lastTickPosX + (p_180449_1_.posX - p_180449_1_.lastTickPosX) * (double)p_180449_2_;
         double var12 = p_180449_1_.lastTickPosY + (p_180449_1_.posY - p_180449_1_.lastTickPosY) * (double)p_180449_2_;
         double var14 = p_180449_1_.lastTickPosZ + (p_180449_1_.posZ - p_180449_1_.lastTickPosZ) * (double)p_180449_2_;
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
         this.renderEngine.bindTexture(locationForcefieldPng);
         GlStateManager.depthMask(false);
         GlStateManager.pushMatrix();
         int var16 = var5.getStatus().getID();
         float var17 = (float)(var16 >> 16 & 255) / 255.0F;
         float var18 = (float)(var16 >> 8 & 255) / 255.0F;
         float var19 = (float)(var16 & 255) / 255.0F;
         GlStateManager.color(var17, var18, var19, (float)var8);
         GlStateManager.doPolygonOffset(-3.0F, -3.0F);
         GlStateManager.enablePolygonOffset();
         GlStateManager.alphaFunc(516, 0.1F);
         GlStateManager.enableAlpha();
         GlStateManager.disableCull();
         float var20 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F;
         float var21 = 0.0F;
         float var22 = 0.0F;
         float var23 = 128.0F;
         var4.begin(7, DefaultVertexFormats.POSITION_TEX);
         var4.setTranslation(-var10, -var12, -var14);
         double var24 = Math.max((double)MathHelper.floor_double(var14 - var6), var5.minZ());
         double var26 = Math.min((double)MathHelper.ceiling_double_int(var14 + var6), var5.maxZ());
         float var28;
         double var29;
         double var31;
         float var33;
         if(var10 > var5.maxX() - var6) {
            var28 = 0.0F;

            for(var29 = var24; var29 < var26; var28 += 0.5F) {
               var31 = Math.min(1.0D, var26 - var29);
               var33 = (float)var31 * 0.5F;
               var4.pos(var5.maxX(), 256.0D, var29).tex((double)(var20 + var28), (double)(var20 + 0.0F)).endVertex();
               var4.pos(var5.maxX(), 256.0D, var29 + var31).tex((double)(var20 + var33 + var28), (double)(var20 + 0.0F)).endVertex();
               var4.pos(var5.maxX(), 0.0D, var29 + var31).tex((double)(var20 + var33 + var28), (double)(var20 + 128.0F)).endVertex();
               var4.pos(var5.maxX(), 0.0D, var29).tex((double)(var20 + var28), (double)(var20 + 128.0F)).endVertex();
               ++var29;
            }
         }

         if(var10 < var5.minX() + var6) {
            var28 = 0.0F;

            for(var29 = var24; var29 < var26; var28 += 0.5F) {
               var31 = Math.min(1.0D, var26 - var29);
               var33 = (float)var31 * 0.5F;
               var4.pos(var5.minX(), 256.0D, var29).tex((double)(var20 + var28), (double)(var20 + 0.0F)).endVertex();
               var4.pos(var5.minX(), 256.0D, var29 + var31).tex((double)(var20 + var33 + var28), (double)(var20 + 0.0F)).endVertex();
               var4.pos(var5.minX(), 0.0D, var29 + var31).tex((double)(var20 + var33 + var28), (double)(var20 + 128.0F)).endVertex();
               var4.pos(var5.minX(), 0.0D, var29).tex((double)(var20 + var28), (double)(var20 + 128.0F)).endVertex();
               ++var29;
            }
         }

         var24 = Math.max((double)MathHelper.floor_double(var10 - var6), var5.minX());
         var26 = Math.min((double)MathHelper.ceiling_double_int(var10 + var6), var5.maxX());
         if(var14 > var5.maxZ() - var6) {
            var28 = 0.0F;

            for(var29 = var24; var29 < var26; var28 += 0.5F) {
               var31 = Math.min(1.0D, var26 - var29);
               var33 = (float)var31 * 0.5F;
               var4.pos(var29, 256.0D, var5.maxZ()).tex((double)(var20 + var28), (double)(var20 + 0.0F)).endVertex();
               var4.pos(var29 + var31, 256.0D, var5.maxZ()).tex((double)(var20 + var33 + var28), (double)(var20 + 0.0F)).endVertex();
               var4.pos(var29 + var31, 0.0D, var5.maxZ()).tex((double)(var20 + var33 + var28), (double)(var20 + 128.0F)).endVertex();
               var4.pos(var29, 0.0D, var5.maxZ()).tex((double)(var20 + var28), (double)(var20 + 128.0F)).endVertex();
               ++var29;
            }
         }

         if(var14 < var5.minZ() + var6) {
            var28 = 0.0F;

            for(var29 = var24; var29 < var26; var28 += 0.5F) {
               var31 = Math.min(1.0D, var26 - var29);
               var33 = (float)var31 * 0.5F;
               var4.pos(var29, 256.0D, var5.minZ()).tex((double)(var20 + var28), (double)(var20 + 0.0F)).endVertex();
               var4.pos(var29 + var31, 256.0D, var5.minZ()).tex((double)(var20 + var33 + var28), (double)(var20 + 0.0F)).endVertex();
               var4.pos(var29 + var31, 0.0D, var5.minZ()).tex((double)(var20 + var33 + var28), (double)(var20 + 128.0F)).endVertex();
               var4.pos(var29, 0.0D, var5.minZ()).tex((double)(var20 + var28), (double)(var20 + 128.0F)).endVertex();
               ++var29;
            }
         }

         var3.draw();
         var4.setTranslation(0.0D, 0.0D, 0.0D);
         GlStateManager.enableCull();
         GlStateManager.disableAlpha();
         GlStateManager.doPolygonOffset(0.0F, 0.0F);
         GlStateManager.disablePolygonOffset();
         GlStateManager.enableAlpha();
         GlStateManager.disableBlend();
         GlStateManager.popMatrix();
         GlStateManager.depthMask(true);
      }

   }

   public void preRenderDamagedBlocks() {
      GlStateManager.tryBlendFuncSeparate(774, 768, 1, 0);
      GlStateManager.enableBlend();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
      GlStateManager.doPolygonOffset(-3.0F, -3.0F);
      GlStateManager.enablePolygonOffset();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.enableAlpha();
      GlStateManager.pushMatrix();
      if(Config.isShaders()) {
         ShadersRender.beginBlockDamage();
      }

   }

   public void postRenderDamagedBlocks() {
      GlStateManager.disableAlpha();
      GlStateManager.doPolygonOffset(0.0F, 0.0F);
      GlStateManager.disablePolygonOffset();
      GlStateManager.enableAlpha();
      GlStateManager.depthMask(true);
      GlStateManager.popMatrix();
      if(Config.isShaders()) {
         ShadersRender.endBlockDamage();
      }

   }

   public void drawBlockDamageTexture(Tessellator p_174981_1_, WorldRenderer p_174981_2_, Entity p_174981_3_, float p_174981_4_) {
      double var5 = p_174981_3_.lastTickPosX + (p_174981_3_.posX - p_174981_3_.lastTickPosX) * (double)p_174981_4_;
      double var7 = p_174981_3_.lastTickPosY + (p_174981_3_.posY - p_174981_3_.lastTickPosY) * (double)p_174981_4_;
      double var9 = p_174981_3_.lastTickPosZ + (p_174981_3_.posZ - p_174981_3_.lastTickPosZ) * (double)p_174981_4_;
      if(!this.damagedBlocks.isEmpty()) {
         this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
         this.preRenderDamagedBlocks();
         p_174981_2_.begin(7, DefaultVertexFormats.BLOCK);
         p_174981_2_.setTranslation(-var5, -var7, -var9);
         p_174981_2_.noColor();
         Iterator var11 = this.damagedBlocks.values().iterator();

         while(var11.hasNext()) {
            DestroyBlockProgress var12 = (DestroyBlockProgress)var11.next();
            BlockPos var13 = var12.getPosition();
            double var14 = (double)var13.getX() - var5;
            double var16 = (double)var13.getY() - var7;
            double var18 = (double)var13.getZ() - var9;
            Block var20 = this.theWorld.getBlockState(var13).getBlock();
            boolean renderBreaking;
            if(Reflector.ForgeTileEntity_canRenderBreaking.exists()) {
               boolean var22 = var20 instanceof BlockChest || var20 instanceof BlockEnderChest || var20 instanceof BlockSign || var20 instanceof BlockSkull;
               if(!var22) {
                  TileEntity var23 = this.theWorld.getTileEntity(var13);
                  if(var23 != null) {
                     var22 = Reflector.callBoolean(var23, Reflector.ForgeTileEntity_canRenderBreaking, new Object[0]);
                  }
               }

               renderBreaking = !var22;
            } else {
               renderBreaking = !(var20 instanceof BlockChest) && !(var20 instanceof BlockEnderChest) && !(var20 instanceof BlockSign) && !(var20 instanceof BlockSkull);
            }

            if(renderBreaking) {
               if(var14 * var14 + var16 * var16 + var18 * var18 > 1024.0D) {
                  var11.remove();
               } else {
                  IBlockState var21 = this.theWorld.getBlockState(var13);
                  if(var21.getBlock().getMaterial() != Material.air) {
                     int var221 = var12.getPartialBlockDamage();
                     TextureAtlasSprite var231 = this.destroyBlockIcons[var221];
                     BlockRendererDispatcher var24 = this.mc.getBlockRendererDispatcher();
                     var24.renderBlockDamage(var21, var13, var231, this.theWorld);
                  }
               }
            }
         }

         p_174981_1_.draw();
         p_174981_2_.setTranslation(0.0D, 0.0D, 0.0D);
         this.postRenderDamagedBlocks();
      }

   }

   public void drawSelectionBox(EntityPlayer p_72731_1_, MovingObjectPosition p_72731_2_, int p_72731_3_, float p_72731_4_) {
      if(p_72731_3_ == 0 && p_72731_2_.typeOfHit == MovingObjectType.BLOCK) {
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         GlStateManager.color(0.0F, 0.0F, 0.0F, 0.4F);
         GL11.glLineWidth(2.0F);
         GlStateManager.disableTexture2D();
         if(Config.isShaders()) {
            Shaders.disableTexture2D();
         }

         GlStateManager.depthMask(false);
         float var5 = 0.002F;
         BlockPos var6 = p_72731_2_.getBlockPos();
         Block var7 = this.theWorld.getBlockState(var6).getBlock();
         if(var7.getMaterial() != Material.air && this.theWorld.getWorldBorder().contains(var6)) {
            var7.setBlockBoundsBasedOnState(this.theWorld, var6);
            double var8 = p_72731_1_.lastTickPosX + (p_72731_1_.posX - p_72731_1_.lastTickPosX) * (double)p_72731_4_;
            double var10 = p_72731_1_.lastTickPosY + (p_72731_1_.posY - p_72731_1_.lastTickPosY) * (double)p_72731_4_;
            double var12 = p_72731_1_.lastTickPosZ + (p_72731_1_.posZ - p_72731_1_.lastTickPosZ) * (double)p_72731_4_;
            drawSelectionBoundingBox(var7.getSelectedBoundingBox(this.theWorld, var6).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-var8, -var10, -var12));
         }

         GlStateManager.depthMask(true);
         GlStateManager.enableTexture2D();
         if(Config.isShaders()) {
            Shaders.enableTexture2D();
         }

         GlStateManager.disableBlend();
      }

   }

   public static void drawSelectionBoundingBox(AxisAlignedBB p_500146_0_) {
      Tessellator var1 = Tessellator.getInstance();
      WorldRenderer var2 = var1.getWorldRenderer();
      var2.begin(3, DefaultVertexFormats.POSITION);
      var2.pos(p_500146_0_.minX, p_500146_0_.minY, p_500146_0_.minZ).endVertex();
      var2.pos(p_500146_0_.maxX, p_500146_0_.minY, p_500146_0_.minZ).endVertex();
      var2.pos(p_500146_0_.maxX, p_500146_0_.minY, p_500146_0_.maxZ).endVertex();
      var2.pos(p_500146_0_.minX, p_500146_0_.minY, p_500146_0_.maxZ).endVertex();
      var2.pos(p_500146_0_.minX, p_500146_0_.minY, p_500146_0_.minZ).endVertex();
      var1.draw();
      var2.begin(3, DefaultVertexFormats.POSITION);
      var2.pos(p_500146_0_.minX, p_500146_0_.maxY, p_500146_0_.minZ).endVertex();
      var2.pos(p_500146_0_.maxX, p_500146_0_.maxY, p_500146_0_.minZ).endVertex();
      var2.pos(p_500146_0_.maxX, p_500146_0_.maxY, p_500146_0_.maxZ).endVertex();
      var2.pos(p_500146_0_.minX, p_500146_0_.maxY, p_500146_0_.maxZ).endVertex();
      var2.pos(p_500146_0_.minX, p_500146_0_.maxY, p_500146_0_.minZ).endVertex();
      var1.draw();
      var2.begin(1, DefaultVertexFormats.POSITION);
      var2.pos(p_500146_0_.minX, p_500146_0_.minY, p_500146_0_.minZ).endVertex();
      var2.pos(p_500146_0_.minX, p_500146_0_.maxY, p_500146_0_.minZ).endVertex();
      var2.pos(p_500146_0_.maxX, p_500146_0_.minY, p_500146_0_.minZ).endVertex();
      var2.pos(p_500146_0_.maxX, p_500146_0_.maxY, p_500146_0_.minZ).endVertex();
      var2.pos(p_500146_0_.maxX, p_500146_0_.minY, p_500146_0_.maxZ).endVertex();
      var2.pos(p_500146_0_.maxX, p_500146_0_.maxY, p_500146_0_.maxZ).endVertex();
      var2.pos(p_500146_0_.minX, p_500146_0_.minY, p_500146_0_.maxZ).endVertex();
      var2.pos(p_500146_0_.minX, p_500146_0_.maxY, p_500146_0_.maxZ).endVertex();
      var1.draw();
   }

   public static void drawOutlinedBoundingBox(AxisAlignedBB p_500147_0_, int p_500147_1_, int p_500147_2_, int p_500147_3_, int p_500147_4_) {
      Tessellator var5 = Tessellator.getInstance();
      WorldRenderer var6 = var5.getWorldRenderer();
      var6.begin(3, DefaultVertexFormats.POSITION_COLOR);
      var6.pos(p_500147_0_.minX, p_500147_0_.minY, p_500147_0_.minZ).color(p_500147_1_, p_500147_2_, p_500147_3_, p_500147_4_).endVertex();
      var6.pos(p_500147_0_.maxX, p_500147_0_.minY, p_500147_0_.minZ).color(p_500147_1_, p_500147_2_, p_500147_3_, p_500147_4_).endVertex();
      var6.pos(p_500147_0_.maxX, p_500147_0_.minY, p_500147_0_.maxZ).color(p_500147_1_, p_500147_2_, p_500147_3_, p_500147_4_).endVertex();
      var6.pos(p_500147_0_.minX, p_500147_0_.minY, p_500147_0_.maxZ).color(p_500147_1_, p_500147_2_, p_500147_3_, p_500147_4_).endVertex();
      var6.pos(p_500147_0_.minX, p_500147_0_.minY, p_500147_0_.minZ).color(p_500147_1_, p_500147_2_, p_500147_3_, p_500147_4_).endVertex();
      var5.draw();
      var6.begin(3, DefaultVertexFormats.POSITION_COLOR);
      var6.pos(p_500147_0_.minX, p_500147_0_.maxY, p_500147_0_.minZ).color(p_500147_1_, p_500147_2_, p_500147_3_, p_500147_4_).endVertex();
      var6.pos(p_500147_0_.maxX, p_500147_0_.maxY, p_500147_0_.minZ).color(p_500147_1_, p_500147_2_, p_500147_3_, p_500147_4_).endVertex();
      var6.pos(p_500147_0_.maxX, p_500147_0_.maxY, p_500147_0_.maxZ).color(p_500147_1_, p_500147_2_, p_500147_3_, p_500147_4_).endVertex();
      var6.pos(p_500147_0_.minX, p_500147_0_.maxY, p_500147_0_.maxZ).color(p_500147_1_, p_500147_2_, p_500147_3_, p_500147_4_).endVertex();
      var6.pos(p_500147_0_.minX, p_500147_0_.maxY, p_500147_0_.minZ).color(p_500147_1_, p_500147_2_, p_500147_3_, p_500147_4_).endVertex();
      var5.draw();
      var6.begin(1, DefaultVertexFormats.POSITION_COLOR);
      var6.pos(p_500147_0_.minX, p_500147_0_.minY, p_500147_0_.minZ).color(p_500147_1_, p_500147_2_, p_500147_3_, p_500147_4_).endVertex();
      var6.pos(p_500147_0_.minX, p_500147_0_.maxY, p_500147_0_.minZ).color(p_500147_1_, p_500147_2_, p_500147_3_, p_500147_4_).endVertex();
      var6.pos(p_500147_0_.maxX, p_500147_0_.minY, p_500147_0_.minZ).color(p_500147_1_, p_500147_2_, p_500147_3_, p_500147_4_).endVertex();
      var6.pos(p_500147_0_.maxX, p_500147_0_.maxY, p_500147_0_.minZ).color(p_500147_1_, p_500147_2_, p_500147_3_, p_500147_4_).endVertex();
      var6.pos(p_500147_0_.maxX, p_500147_0_.minY, p_500147_0_.maxZ).color(p_500147_1_, p_500147_2_, p_500147_3_, p_500147_4_).endVertex();
      var6.pos(p_500147_0_.maxX, p_500147_0_.maxY, p_500147_0_.maxZ).color(p_500147_1_, p_500147_2_, p_500147_3_, p_500147_4_).endVertex();
      var6.pos(p_500147_0_.minX, p_500147_0_.minY, p_500147_0_.maxZ).color(p_500147_1_, p_500147_2_, p_500147_3_, p_500147_4_).endVertex();
      var6.pos(p_500147_0_.minX, p_500147_0_.maxY, p_500147_0_.maxZ).color(p_500147_1_, p_500147_2_, p_500147_3_, p_500147_4_).endVertex();
      var5.draw();
   }

   public void markBlocksForUpdate(int p_72725_1_, int p_72725_2_, int p_72725_3_, int p_72725_4_, int p_72725_5_, int p_72725_6_) {
      this.viewFrustum.markBlocksForUpdate(p_72725_1_, p_72725_2_, p_72725_3_, p_72725_4_, p_72725_5_, p_72725_6_);
   }

   public void markBlockForUpdate(BlockPos pos) {
      int var2 = pos.getX();
      int var3 = pos.getY();
      int var4 = pos.getZ();
      this.markBlocksForUpdate(var2 - 1, var3 - 1, var4 - 1, var2 + 1, var3 + 1, var4 + 1);
   }

   public void notifyLightSet(BlockPos pos) {
      int var2 = pos.getX();
      int var3 = pos.getY();
      int var4 = pos.getZ();
      this.markBlocksForUpdate(var2 - 1, var3 - 1, var4 - 1, var2 + 1, var3 + 1, var4 + 1);
   }

   public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
      this.markBlocksForUpdate(x1 - 1, y1 - 1, z1 - 1, x2 + 1, y2 + 1, z2 + 1);
   }

   public void playRecord(String p_174961_1_, BlockPos p_174961_2_) {
      ISound var3 = (ISound)this.mapSoundPositions.get(p_174961_2_);
      if(var3 != null) {
         this.mc.getSoundHandler().stopSound(var3);
         this.mapSoundPositions.remove(p_174961_2_);
      }

      if(p_174961_1_ != null) {
         ItemRecord var4 = ItemRecord.getRecord(p_174961_1_);
         if(var4 != null) {
            this.mc.ingameGUI.setRecordPlayingMessage(var4.getRecordNameLocal());
         }

         ResourceLocation resource = null;
         if(Reflector.ForgeItemRecord_getRecordResource.exists() && var4 != null) {
            resource = (ResourceLocation)Reflector.call(var4, Reflector.ForgeItemRecord_getRecordResource, new Object[]{p_174961_1_});
         }

         if(resource == null) {
            resource = new ResourceLocation(p_174961_1_);
         }

         PositionedSoundRecord var5 = PositionedSoundRecord.create(resource, (float)p_174961_2_.getX(), (float)p_174961_2_.getY(), (float)p_174961_2_.getZ());
         this.mapSoundPositions.put(p_174961_2_, var5);
         this.mc.getSoundHandler().playSound(var5);
      }

   }

   public void playSound(String soundName, double x, double y, double z, float volume, float pitch) {}

   public void playSoundToNearExcept(EntityPlayer except, String soundName, double x, double y, double z, float volume, float pitch) {}

   public void spawnParticle(int p_180442_1_, boolean p_180442_2_, final double p_180442_3_, final double p_180442_5_, final double p_180442_7_, double p_180442_9_, double p_180442_11_, double p_180442_13_, int ... p_180442_15_) {
      try {
         this.spawnEntityFX(p_180442_1_, p_180442_2_, p_180442_3_, p_180442_5_, p_180442_7_, p_180442_9_, p_180442_11_, p_180442_13_, p_180442_15_);
      } catch (Throwable var19) {
         CrashReport var17 = CrashReport.makeCrashReport(var19, "Exception while adding particle");
         CrashReportCategory var18 = var17.makeCategory("Particle being added");
         var18.addCrashSection("ID", Integer.valueOf(p_180442_1_));
         if(p_180442_15_ != null) {
            var18.addCrashSection("Parameters", p_180442_15_);
         }

         var18.addCrashSectionCallable("Position", new Callable() {

            public static final String __OBFID = "CL_00000955";

            public String call() throws Exception {
               return CrashReportCategory.getCoordinateInfo(p_180442_3_, p_180442_5_, p_180442_7_);
            }
            // $FF: synthetic method
            // $FF: bridge method
            public Object call() throws Exception {
               return this.call();
            }
         });
         throw new ReportedException(var17);
      }
   }

   public void spawnParticle(EnumParticleTypes p_174972_1_, double p_174972_2_, double p_174972_4_, double p_174972_6_, double p_174972_8_, double p_174972_10_, double p_174972_12_, int ... p_174972_14_) {
      this.spawnParticle(p_174972_1_.getParticleID(), p_174972_1_.getShouldIgnoreRange(), p_174972_2_, p_174972_4_, p_174972_6_, p_174972_8_, p_174972_10_, p_174972_12_, p_174972_14_);
   }

   public EntityFX spawnEntityFX(int p_174974_1_, boolean p_174974_2_, double p_174974_3_, double p_174974_5_, double p_174974_7_, double p_174974_9_, double p_174974_11_, double p_174974_13_, int ... p_174974_15_) {
      if(this.mc != null && this.mc.getRenderViewEntity() != null && this.mc.effectRenderer != null) {
         int var16 = this.mc.gameSettings.particleSetting;
         if(var16 == 1 && this.theWorld.rand.nextInt(3) == 0) {
            var16 = 2;
         }

         double var17 = this.mc.getRenderViewEntity().posX - p_174974_3_;
         double var19 = this.mc.getRenderViewEntity().posY - p_174974_5_;
         double var21 = this.mc.getRenderViewEntity().posZ - p_174974_7_;
         if(p_174974_1_ == EnumParticleTypes.EXPLOSION_HUGE.getParticleID() && !Config.isAnimatedExplosion()) {
            return null;
         } else if(p_174974_1_ == EnumParticleTypes.EXPLOSION_LARGE.getParticleID() && !Config.isAnimatedExplosion()) {
            return null;
         } else if(p_174974_1_ == EnumParticleTypes.EXPLOSION_NORMAL.getParticleID() && !Config.isAnimatedExplosion()) {
            return null;
         } else if(p_174974_1_ == EnumParticleTypes.SUSPENDED.getParticleID() && !Config.isWaterParticles()) {
            return null;
         } else if(p_174974_1_ == EnumParticleTypes.SUSPENDED_DEPTH.getParticleID() && !Config.isVoidParticles()) {
            return null;
         } else if(p_174974_1_ == EnumParticleTypes.SMOKE_NORMAL.getParticleID() && !Config.isAnimatedSmoke()) {
            return null;
         } else if(p_174974_1_ == EnumParticleTypes.SMOKE_LARGE.getParticleID() && !Config.isAnimatedSmoke()) {
            return null;
         } else if(p_174974_1_ == EnumParticleTypes.SPELL_MOB.getParticleID() && !Config.isPotionParticles()) {
            return null;
         } else if(p_174974_1_ == EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID() && !Config.isPotionParticles()) {
            return null;
         } else if(p_174974_1_ == EnumParticleTypes.SPELL.getParticleID() && !Config.isPotionParticles()) {
            return null;
         } else if(p_174974_1_ == EnumParticleTypes.SPELL_INSTANT.getParticleID() && !Config.isPotionParticles()) {
            return null;
         } else if(p_174974_1_ == EnumParticleTypes.SPELL_WITCH.getParticleID() && !Config.isPotionParticles()) {
            return null;
         } else if(p_174974_1_ == EnumParticleTypes.PORTAL.getParticleID() && !Config.isAnimatedPortal()) {
            return null;
         } else if(p_174974_1_ == EnumParticleTypes.FLAME.getParticleID() && !Config.isAnimatedFlame()) {
            return null;
         } else if(p_174974_1_ == EnumParticleTypes.REDSTONE.getParticleID() && !Config.isAnimatedRedstone()) {
            return null;
         } else if(p_174974_1_ == EnumParticleTypes.DRIP_WATER.getParticleID() && !Config.isDrippingWaterLava()) {
            return null;
         } else if(p_174974_1_ == EnumParticleTypes.DRIP_LAVA.getParticleID() && !Config.isDrippingWaterLava()) {
            return null;
         } else if(p_174974_1_ == EnumParticleTypes.FIREWORKS_SPARK.getParticleID() && !Config.isFireworkParticles()) {
            return null;
         } else if(p_174974_2_) {
            return this.mc.effectRenderer.spawnEffectParticle(p_174974_1_, p_174974_3_, p_174974_5_, p_174974_7_, p_174974_9_, p_174974_11_, p_174974_13_, p_174974_15_);
         } else {
            double var23 = 16.0D;
            double maxDistSq = 256.0D;
            if(p_174974_1_ == EnumParticleTypes.CRIT.getParticleID()) {
               maxDistSq = 38416.0D;
            }

            if(var17 * var17 + var19 * var19 + var21 * var21 > maxDistSq) {
               return null;
            } else if(var16 > 1) {
               return null;
            } else {
               EntityFX entityFx = this.mc.effectRenderer.spawnEffectParticle(p_174974_1_, p_174974_3_, p_174974_5_, p_174974_7_, p_174974_9_, p_174974_11_, p_174974_13_, p_174974_15_);
               if(p_174974_1_ == EnumParticleTypes.WATER_BUBBLE.getParticleID()) {
                  CustomColors.updateWaterFX(entityFx, this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_);
               }

               if(p_174974_1_ == EnumParticleTypes.WATER_SPLASH.getParticleID()) {
                  CustomColors.updateWaterFX(entityFx, this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_);
               }

               if(p_174974_1_ == EnumParticleTypes.WATER_DROP.getParticleID()) {
                  CustomColors.updateWaterFX(entityFx, this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_);
               }

               if(p_174974_1_ == EnumParticleTypes.TOWN_AURA.getParticleID()) {
                  CustomColors.updateMyceliumFX(entityFx);
               }

               if(p_174974_1_ == EnumParticleTypes.PORTAL.getParticleID()) {
                  CustomColors.updatePortalFX(entityFx);
               }

               if(p_174974_1_ == EnumParticleTypes.REDSTONE.getParticleID()) {
                  CustomColors.updateReddustFX(entityFx, this.theWorld, p_174974_3_, p_174974_5_, p_174974_7_);
               }

               return entityFx;
            }
         }
      } else {
         return null;
      }
   }

   public void onEntityAdded(Entity entityIn) {
      RandomMobs.entityLoaded(entityIn, this.theWorld);
      if(Config.isDynamicLights()) {
         DynamicLights.entityAdded(entityIn, this);
      }

   }

   public void onEntityRemoved(Entity entityIn) {
      if(Config.isDynamicLights()) {
         DynamicLights.entityRemoved(entityIn, this);
      }

   }

   public void deleteAllDisplayLists() {}

   public void broadcastSound(int p_180440_1_, BlockPos p_180440_2_, int p_180440_3_) {
      switch(p_180440_1_) {
      case 1013:
      case 1018:
         if(this.mc.getRenderViewEntity() != null) {
            double var4 = (double)p_180440_2_.getX() - this.mc.getRenderViewEntity().posX;
            double var6 = (double)p_180440_2_.getY() - this.mc.getRenderViewEntity().posY;
            double var8 = (double)p_180440_2_.getZ() - this.mc.getRenderViewEntity().posZ;
            double var10 = Math.sqrt(var4 * var4 + var6 * var6 + var8 * var8);
            double var12 = this.mc.getRenderViewEntity().posX;
            double var14 = this.mc.getRenderViewEntity().posY;
            double var16 = this.mc.getRenderViewEntity().posZ;
            if(var10 > 0.0D) {
               var12 += var4 / var10 * 2.0D;
               var14 += var6 / var10 * 2.0D;
               var16 += var8 / var10 * 2.0D;
            }

            if(p_180440_1_ == 1013) {
               this.theWorld.playSound(var12, var14, var16, "mob.wither.spawn", 1.0F, 1.0F, false);
            } else {
               this.theWorld.playSound(var12, var14, var16, "mob.enderdragon.end", 5.0F, 1.0F, false);
            }
         }
      default:
      }
   }

   public void playAuxSFX(EntityPlayer p_180439_1_, int p_180439_2_, BlockPos p_180439_3_, int p_180439_4_) {
      Random var5 = this.theWorld.rand;
      double var9;
      double var11;
      double var32;
      double var25;
      double var27;
      double var7;
      int var13;
      int var18;
      double var19;
      double var21;
      double var23;
      switch(p_180439_2_) {
      case 1000:
         this.theWorld.playSoundAtPos(p_180439_3_, "random.click", 1.0F, 1.0F, false);
         break;
      case 1001:
         this.theWorld.playSoundAtPos(p_180439_3_, "random.click", 1.0F, 1.2F, false);
         break;
      case 1002:
         this.theWorld.playSoundAtPos(p_180439_3_, "random.bow", 1.0F, 1.2F, false);
         break;
      case 1003:
         this.theWorld.playSoundAtPos(p_180439_3_, "random.door_open", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
         break;
      case 1004:
         this.theWorld.playSoundAtPos(p_180439_3_, "random.fizz", 0.5F, 2.6F + (var5.nextFloat() - var5.nextFloat()) * 0.8F, false);
         break;
      case 1005:
         if(Item.getItemById(p_180439_4_) instanceof ItemRecord) {
            this.theWorld.playRecord(p_180439_3_, "records." + ((ItemRecord)Item.getItemById(p_180439_4_)).recordName);
         } else {
            this.theWorld.playRecord(p_180439_3_, (String)null);
         }
         break;
      case 1006:
         this.theWorld.playSoundAtPos(p_180439_3_, "random.door_close", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
         break;
      case 1007:
         this.theWorld.playSoundAtPos(p_180439_3_, "mob.ghast.charge", 10.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1008:
         this.theWorld.playSoundAtPos(p_180439_3_, "mob.ghast.fireball", 10.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1009:
         this.theWorld.playSoundAtPos(p_180439_3_, "mob.ghast.fireball", 2.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1010:
         this.theWorld.playSoundAtPos(p_180439_3_, "mob.zombie.wood", 2.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1011:
         this.theWorld.playSoundAtPos(p_180439_3_, "mob.zombie.metal", 2.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1012:
         this.theWorld.playSoundAtPos(p_180439_3_, "mob.zombie.woodbreak", 2.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1014:
         this.theWorld.playSoundAtPos(p_180439_3_, "mob.wither.shoot", 2.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1015:
         this.theWorld.playSoundAtPos(p_180439_3_, "mob.bat.takeoff", 0.05F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1016:
         this.theWorld.playSoundAtPos(p_180439_3_, "mob.zombie.infect", 2.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1017:
         this.theWorld.playSoundAtPos(p_180439_3_, "mob.zombie.unfect", 2.0F, (var5.nextFloat() - var5.nextFloat()) * 0.2F + 1.0F, false);
         break;
      case 1020:
         this.theWorld.playSoundAtPos(p_180439_3_, "random.anvil_break", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
         break;
      case 1021:
         this.theWorld.playSoundAtPos(p_180439_3_, "random.anvil_use", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
         break;
      case 1022:
         this.theWorld.playSoundAtPos(p_180439_3_, "random.anvil_land", 0.3F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
         break;
      case 2000:
         int var31 = p_180439_4_ % 3 - 1;
         int var8 = p_180439_4_ / 3 % 3 - 1;
         var9 = (double)p_180439_3_.getX() + (double)var31 * 0.6D + 0.5D;
         var11 = (double)p_180439_3_.getY() + 0.5D;
         var32 = (double)p_180439_3_.getZ() + (double)var8 * 0.6D + 0.5D;

         for(int var45 = 0; var45 < 10; ++var45) {
            double var34 = var5.nextDouble() * 0.2D + 0.01D;
            double var35 = var9 + (double)var31 * 0.01D + (var5.nextDouble() - 0.5D) * (double)var8 * 0.5D;
            var25 = var11 + (var5.nextDouble() - 0.5D) * 0.5D;
            var27 = var32 + (double)var8 * 0.01D + (var5.nextDouble() - 0.5D) * (double)var31 * 0.5D;
            double var24 = (double)var31 * var34 + var5.nextGaussian() * 0.01D;
            double var26 = -0.03D + var5.nextGaussian() * 0.01D;
            double var28 = (double)var8 * var34 + var5.nextGaussian() * 0.01D;
            this.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var35, var25, var27, var24, var26, var28, new int[0]);
         }

         return;
      case 2001:
         Block var6 = Block.getBlockById(p_180439_4_ & 4095);
         if(var6.getMaterial() != Material.air) {
            this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(var6.stepSound.getBreakSound()), (var6.stepSound.getVolume() + 1.0F) / 2.0F, var6.stepSound.getFrequency() * 0.8F, (float)p_180439_3_.getX() + 0.5F, (float)p_180439_3_.getY() + 0.5F, (float)p_180439_3_.getZ() + 0.5F));
         }

         this.mc.effectRenderer.addBlockDestroyEffects(p_180439_3_, var6.getStateFromMeta(p_180439_4_ >> 12 & 255));
         break;
      case 2002:
         var7 = (double)p_180439_3_.getX();
         var9 = (double)p_180439_3_.getY();
         var11 = (double)p_180439_3_.getZ();

         for(var13 = 0; var13 < 8; ++var13) {
            this.spawnParticle(EnumParticleTypes.ITEM_CRACK, var7, var9, var11, var5.nextGaussian() * 0.15D, var5.nextDouble() * 0.2D, var5.nextGaussian() * 0.15D, new int[]{Item.getIdFromItem(Items.potionitem), p_180439_4_});
         }

         var13 = Items.potionitem.getColorFromDamage(p_180439_4_);
         float var14 = (float)(var13 >> 16 & 255) / 255.0F;
         float var15 = (float)(var13 >> 8 & 255) / 255.0F;
         float var16 = (float)(var13 >> 0 & 255) / 255.0F;
         EnumParticleTypes var17 = EnumParticleTypes.SPELL;
         if(Items.potionitem.isEffectInstant(p_180439_4_)) {
            var17 = EnumParticleTypes.SPELL_INSTANT;
         }

         for(var18 = 0; var18 < 100; ++var18) {
            var19 = var5.nextDouble() * 4.0D;
            var21 = var5.nextDouble() * 3.141592653589793D * 2.0D;
            var23 = Math.cos(var21) * var19;
            var25 = 0.01D + var5.nextDouble() * 0.5D;
            var27 = Math.sin(var21) * var19;
            EntityFX var29 = this.spawnEntityFX(var17.getParticleID(), var17.getShouldIgnoreRange(), var7 + var23 * 0.1D, var9 + 0.3D, var11 + var27 * 0.1D, var23, var25, var27, new int[0]);
            if(var29 != null) {
               float var30 = 0.75F + var5.nextFloat() * 0.25F;
               var29.setRBGColorF(var14 * var30, var15 * var30, var16 * var30);
               var29.multiplyVelocity((float)var19);
            }
         }

         this.theWorld.playSoundAtPos(p_180439_3_, "game.potion.smash", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
         break;
      case 2003:
         var7 = (double)p_180439_3_.getX() + 0.5D;
         var9 = (double)p_180439_3_.getY();
         var11 = (double)p_180439_3_.getZ() + 0.5D;

         for(var13 = 0; var13 < 8; ++var13) {
            this.spawnParticle(EnumParticleTypes.ITEM_CRACK, var7, var9, var11, var5.nextGaussian() * 0.15D, var5.nextDouble() * 0.2D, var5.nextGaussian() * 0.15D, new int[]{Item.getIdFromItem(Items.ender_eye)});
         }

         for(var32 = 0.0D; var32 < 6.283185307179586D; var32 += 0.15707963267948966D) {
            this.spawnParticle(EnumParticleTypes.PORTAL, var7 + Math.cos(var32) * 5.0D, var9 - 0.4D, var11 + Math.sin(var32) * 5.0D, Math.cos(var32) * -5.0D, 0.0D, Math.sin(var32) * -5.0D, new int[0]);
            this.spawnParticle(EnumParticleTypes.PORTAL, var7 + Math.cos(var32) * 5.0D, var9 - 0.4D, var11 + Math.sin(var32) * 5.0D, Math.cos(var32) * -7.0D, 0.0D, Math.sin(var32) * -7.0D, new int[0]);
         }

         return;
      case 2004:
         for(var18 = 0; var18 < 20; ++var18) {
            var19 = (double)p_180439_3_.getX() + 0.5D + ((double)this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
            var21 = (double)p_180439_3_.getY() + 0.5D + ((double)this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
            var23 = (double)p_180439_3_.getZ() + 0.5D + ((double)this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
            this.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var19, var21, var23, 0.0D, 0.0D, 0.0D, new int[0]);
            this.theWorld.spawnParticle(EnumParticleTypes.FLAME, var19, var21, var23, 0.0D, 0.0D, 0.0D, new int[0]);
         }

         return;
      case 2005:
         ItemDye.spawnBonemealParticles(this.theWorld, p_180439_3_, p_180439_4_);
      }

   }

   public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
      if(progress >= 0 && progress < 10) {
         DestroyBlockProgress var4 = (DestroyBlockProgress)this.damagedBlocks.get(Integer.valueOf(breakerId));
         if(var4 == null || var4.getPosition().getX() != pos.getX() || var4.getPosition().getY() != pos.getY() || var4.getPosition().getZ() != pos.getZ()) {
            var4 = new DestroyBlockProgress(breakerId, pos);
            this.damagedBlocks.put(Integer.valueOf(breakerId), var4);
         }

         var4.setPartialBlockDamage(progress);
         var4.setCloudUpdateTick(this.cloudTickCounter);
      } else {
         this.damagedBlocks.remove(Integer.valueOf(breakerId));
      }

   }

   public void setDisplayListEntitiesDirty() {
      this.displayListEntitiesDirty = true;
   }

   public void resetClouds() {
      this.cloudRenderer.reset();
   }

   public int getCountRenderers() {
      return this.viewFrustum.renderChunks.length;
   }

   public int getCountActiveRenderers() {
      return this.renderInfos.size();
   }

   public int getCountEntitiesRendered() {
      return this.countEntitiesRendered;
   }

   public int getCountTileEntitiesRendered() {
      return this.countTileEntitiesRendered;
   }

   public RenderChunk getRenderChunk(BlockPos pos) {
      return this.viewFrustum.getRenderChunk(pos);
   }

   public RenderChunk getRenderChunk(RenderChunk renderChunk, EnumFacing facing) {
      if(renderChunk == null) {
         return null;
      } else {
         BlockPos pos = renderChunk.getBlockPosOffset16(facing);
         return this.viewFrustum.getRenderChunk(pos);
      }
   }

   public WorldClient getWorld() {
      return this.theWorld;
   }

   public void updateTileEntities(Collection p_500148_1_, Collection p_500148_2_) {
      Set var3 = this.setTileEntities;
      Set var4 = this.setTileEntities;
      synchronized(this.setTileEntities) {
         this.setTileEntities.removeAll(p_500148_1_);
         this.setTileEntities.addAll(p_500148_2_);
      }
   }


   public static final class SwitchEnumUseage {

      public static final int[] field_178037_a = new int[EnumUsage.values().length];
      public static final String __OBFID = "CL_00002535";


      static {
         try {
            field_178037_a[EnumUsage.POSITION.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            field_178037_a[EnumUsage.UV.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            field_178037_a[EnumUsage.COLOR.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static class ContainerLocalRenderInformation {

      public final RenderChunk renderChunk;
      public final EnumFacing facing;
      public final Set setFacing;
      public final int counter;
      public static final String __OBFID = "CL_00002534";


      public ContainerLocalRenderInformation(RenderChunk p_i46248_2_, EnumFacing p_i46248_3_, int p_i46248_4_) {
         this.setFacing = EnumSet.noneOf(EnumFacing.class);
         this.renderChunk = p_i46248_2_;
         this.facing = p_i46248_3_;
         this.counter = p_i46248_4_;
      }

      public ContainerLocalRenderInformation(RenderChunk p_i46249_2_, EnumFacing p_i46249_3_, int p_i46249_4_, Object p_i46249_5_) {
         this(p_i46249_2_, p_i46249_3_, p_i46249_4_);
      }
   }
}
