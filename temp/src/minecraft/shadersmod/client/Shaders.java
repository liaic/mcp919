package shadersmod.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.src.Config;
import net.minecraft.src.CustomColors;
import net.minecraft.src.Lang;
import net.minecraft.src.PropertiesOrdered;
import net.minecraft.src.Reflector;
import net.minecraft.src.StrUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import shadersmod.client.BlockAliases;
import shadersmod.client.CustomTexture;
import shadersmod.client.EnumShaderOption;
import shadersmod.client.HFNoiseTexture;
import shadersmod.client.ICustomTexture;
import shadersmod.client.IShaderPack;
import shadersmod.client.PropertyDefaultFastFancyOff;
import shadersmod.client.PropertyDefaultTrueFalse;
import shadersmod.client.SMath;
import shadersmod.client.ScreenShaderOptions;
import shadersmod.client.ShaderLine;
import shadersmod.client.ShaderOption;
import shadersmod.client.ShaderOptionProfile;
import shadersmod.client.ShaderOptionRest;
import shadersmod.client.ShaderPackDefault;
import shadersmod.client.ShaderPackFolder;
import shadersmod.client.ShaderPackNone;
import shadersmod.client.ShaderPackParser;
import shadersmod.client.ShaderPackZip;
import shadersmod.client.ShaderParser;
import shadersmod.client.ShaderProfile;
import shadersmod.client.ShaderUtils;
import shadersmod.client.ShadersBuiltIn;
import shadersmod.client.ShadersRender;
import shadersmod.client.ShadersTex;
import shadersmod.client.SimpleShaderTexture;
import shadersmod.common.SMCLog;
import shadersmod.uniform.CustomUniforms;
import shadersmod.uniform.LegacyUniforms;
import shadersmod.uniform.ShaderUniformFloat4;
import shadersmod.uniform.ShaderUniformInt;
import shadersmod.uniform.Smoother;

public class Shaders {
   static Minecraft mc;
   static EntityRenderer entityRenderer;
   public static boolean isInitializedOnce = false;
   public static boolean isShaderPackInitialized = false;
   public static ContextCapabilities capabilities;
   public static String glVersionString;
   public static String glVendorString;
   public static String glRendererString;
   public static boolean hasGlGenMipmap = false;
   public static boolean hasForge = false;
   public static int numberResetDisplayList = 0;
   static boolean needResetModels = false;
   private static int renderDisplayWidth = 0;
   private static int renderDisplayHeight = 0;
   public static int renderWidth = 0;
   public static int renderHeight = 0;
   public static boolean isRenderingWorld = false;
   public static boolean isRenderingSky = false;
   public static boolean isCompositeRendered = false;
   public static boolean isRenderingDfb = false;
   public static boolean isShadowPass = false;
   public static boolean isSleeping;
   private static boolean isRenderingFirstPersonHand;
   private static boolean isHandRenderedMain;
   public static boolean renderItemKeepDepthMask = false;
   public static boolean itemToRenderMainTranslucent = false;
   static float[] sunPosition = new float[4];
   static float[] moonPosition = new float[4];
   static float[] shadowLightPosition = new float[4];
   static float[] upPosition = new float[4];
   static float[] shadowLightPositionVector = new float[4];
   static float[] upPosModelView = new float[]{0.0F, 100.0F, 0.0F, 0.0F};
   static float[] sunPosModelView = new float[]{0.0F, 100.0F, 0.0F, 0.0F};
   static float[] moonPosModelView = new float[]{0.0F, -100.0F, 0.0F, 0.0F};
   private static float[] tempMat = new float[16];
   static float clearColorR;
   static float clearColorG;
   static float clearColorB;
   static float skyColorR;
   static float skyColorG;
   static float skyColorB;
   static long worldTime = 0L;
   static long lastWorldTime = 0L;
   static long diffWorldTime = 0L;
   static float celestialAngle = 0.0F;
   static float sunAngle = 0.0F;
   static float shadowAngle = 0.0F;
   static int moonPhase = 0;
   static long systemTime = 0L;
   static long lastSystemTime = 0L;
   static long diffSystemTime = 0L;
   static int frameCounter = 0;
   static float frameTime = 0.0F;
   static float frameTimeCounter = 0.0F;
   static int systemTimeInt32 = 0;
   static float rainStrength = 0.0F;
   static float wetness = 0.0F;
   public static float wetnessHalfLife = 600.0F;
   public static float drynessHalfLife = 200.0F;
   public static float eyeBrightnessHalflife = 10.0F;
   static boolean usewetness = false;
   static int isEyeInWater = 0;
   static int eyeBrightness = 0;
   static float eyeBrightnessFadeX = 0.0F;
   static float eyeBrightnessFadeY = 0.0F;
   static float eyePosY = 0.0F;
   static float centerDepth = 0.0F;
   static float centerDepthSmooth = 0.0F;
   static float centerDepthSmoothHalflife = 1.0F;
   static boolean centerDepthSmoothEnabled = false;
   static int superSamplingLevel = 1;
   static float nightVision = 0.0F;
   static float blindness = 0.0F;
   static boolean updateChunksErrorRecorded = false;
   static boolean lightmapEnabled = false;
   static boolean fogEnabled = true;
   public static int entityAttrib = 10;
   public static int midTexCoordAttrib = 11;
   public static int tangentAttrib = 12;
   public static boolean useEntityAttrib = false;
   public static boolean useMidTexCoordAttrib = false;
   public static boolean useMultiTexCoord3Attrib = false;
   public static boolean useTangentAttrib = false;
   public static boolean progUseEntityAttrib = false;
   public static boolean progUseMidTexCoordAttrib = false;
   public static boolean progUseTangentAttrib = false;
   public static int atlasSizeX = 0;
   public static int atlasSizeY = 0;
   public static ShaderUniformFloat4 uniformEntityColor = new ShaderUniformFloat4("entityColor");
   public static ShaderUniformInt uniformEntityId = new ShaderUniformInt("entityId");
   public static ShaderUniformInt uniformBlockEntityId = new ShaderUniformInt("blockEntityId");
   static double previousCameraPositionX;
   static double previousCameraPositionY;
   static double previousCameraPositionZ;
   static double cameraPositionX;
   static double cameraPositionY;
   static double cameraPositionZ;
   static int shadowPassInterval = 0;
   public static boolean needResizeShadow = false;
   static int shadowMapWidth = 1024;
   static int shadowMapHeight = 1024;
   static int spShadowMapWidth = 1024;
   static int spShadowMapHeight = 1024;
   static float shadowMapFOV = 90.0F;
   static float shadowMapHalfPlane = 160.0F;
   static boolean shadowMapIsOrtho = true;
   static float shadowDistanceRenderMul = -1.0F;
   static int shadowPassCounter = 0;
   static int preShadowPassThirdPersonView;
   public static boolean shouldSkipDefaultShadow = false;
   static boolean waterShadowEnabled = false;
   static final int MaxDrawBuffers = 8;
   static final int MaxColorBuffers = 8;
   static final int MaxDepthBuffers = 3;
   static final int MaxShadowColorBuffers = 8;
   static final int MaxShadowDepthBuffers = 2;
   static int usedColorBuffers = 0;
   static int usedDepthBuffers = 0;
   static int usedShadowColorBuffers = 0;
   static int usedShadowDepthBuffers = 0;
   static int usedColorAttachs = 0;
   static int usedDrawBuffers = 0;
   static int dfb = 0;
   static int sfb = 0;
   private static int[] gbuffersFormat = new int[8];
   public static boolean[] gbuffersClear = new boolean[8];
   public static int activeProgram = 0;
   public static final int ProgramNone = 0;
   public static final int ProgramBasic = 1;
   public static final int ProgramTextured = 2;
   public static final int ProgramTexturedLit = 3;
   public static final int ProgramSkyBasic = 4;
   public static final int ProgramSkyTextured = 5;
   public static final int ProgramClouds = 6;
   public static final int ProgramTerrain = 7;
   public static final int ProgramTerrainSolid = 8;
   public static final int ProgramTerrainCutoutMip = 9;
   public static final int ProgramTerrainCutout = 10;
   public static final int ProgramDamagedBlock = 11;
   public static final int ProgramWater = 12;
   public static final int ProgramBlock = 13;
   public static final int ProgramBeaconBeam = 14;
   public static final int ProgramItem = 15;
   public static final int ProgramEntities = 16;
   public static final int ProgramArmorGlint = 17;
   public static final int ProgramSpiderEyes = 18;
   public static final int ProgramHand = 19;
   public static final int ProgramWeather = 20;
   public static final int ProgramComposite = 21;
   public static final int ProgramComposite1 = 22;
   public static final int ProgramComposite2 = 23;
   public static final int ProgramComposite3 = 24;
   public static final int ProgramComposite4 = 25;
   public static final int ProgramComposite5 = 26;
   public static final int ProgramComposite6 = 27;
   public static final int ProgramComposite7 = 28;
   public static final int ProgramFinal = 29;
   public static final int ProgramShadow = 30;
   public static final int ProgramShadowSolid = 31;
   public static final int ProgramShadowCutout = 32;
   public static final int ProgramDeferred = 33;
   public static final int ProgramDeferred1 = 34;
   public static final int ProgramDeferred2 = 35;
   public static final int ProgramDeferred3 = 36;
   public static final int ProgramDeferred4 = 37;
   public static final int ProgramDeferred5 = 38;
   public static final int ProgramDeferred6 = 39;
   public static final int ProgramDeferred7 = 40;
   public static final int ProgramHandWater = 41;
   public static final int ProgramDeferredLast = 42;
   public static final int ProgramCompositeLast = 43;
   public static final int ProgramCount = 44;
   public static final int MaxCompositePasses = 8;
   public static final int MaxDeferredPasses = 8;
   private static final String[] programNames = new String[]{"", "gbuffers_basic", "gbuffers_textured", "gbuffers_textured_lit", "gbuffers_skybasic", "gbuffers_skytextured", "gbuffers_clouds", "gbuffers_terrain", "gbuffers_terrain_solid", "gbuffers_terrain_cutout_mip", "gbuffers_terrain_cutout", "gbuffers_damagedblock", "gbuffers_water", "gbuffers_block", "gbuffers_beaconbeam", "gbuffers_item", "gbuffers_entities", "gbuffers_armor_glint", "gbuffers_spidereyes", "gbuffers_hand", "gbuffers_weather", "composite", "composite1", "composite2", "composite3", "composite4", "composite5", "composite6", "composite7", "final", "shadow", "shadow_solid", "shadow_cutout", "deferred", "deferred1", "deferred2", "deferred3", "deferred4", "deferred5", "deferred6", "deferred7", "gbuffers_hand_water", "deferred_last", "composite_last"};
   private static final int[] programBackups = new int[]{0, 0, 1, 2, 1, 2, 2, 3, 7, 7, 7, 7, 7, 7, 2, 3, 3, 2, 2, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 30, 30, 0, 0, 0, 0, 0, 0, 0, 0, 19, 0, 0};
   static int[] programsID = new int[44];
   private static int[] programsRef = new int[44];
   private static int programIDCopyDepth = 0;
   private static boolean hasDeferredPrograms = false;
   public static String[] programsDrawBufSettings = new String[44];
   private static String newDrawBufSetting = null;
   static IntBuffer[] programsDrawBuffers = new IntBuffer[44];
   static IntBuffer activeDrawBuffers = null;
   private static String[] programsColorAtmSettings = new String[44];
   private static String newColorAtmSetting = null;
   private static String activeColorAtmSettings = null;
   private static int[] programsCompositeMipmapSetting = new int[44];
   private static int newCompositeMipmapSetting = 0;
   private static int activeCompositeMipmapSetting = 0;
   public static Properties loadedShaders = null;
   public static Properties shadersConfig = null;
   public static ITextureObject defaultTexture = null;
   public static boolean normalMapEnabled = false;
   public static boolean[] shadowHardwareFilteringEnabled = new boolean[2];
   public static boolean[] shadowMipmapEnabled = new boolean[2];
   public static boolean[] shadowFilterNearest = new boolean[2];
   public static boolean[] shadowColorMipmapEnabled = new boolean[8];
   public static boolean[] shadowColorFilterNearest = new boolean[8];
   public static boolean configTweakBlockDamage = false;
   public static boolean configCloudShadow = false;
   public static float configHandDepthMul = 0.125F;
   public static float configRenderResMul = 1.0F;
   public static float configShadowResMul = 1.0F;
   public static int configTexMinFilB = 0;
   public static int configTexMinFilN = 0;
   public static int configTexMinFilS = 0;
   public static int configTexMagFilB = 0;
   public static int configTexMagFilN = 0;
   public static int configTexMagFilS = 0;
   public static boolean configShadowClipFrustrum = true;
   public static boolean configNormalMap = true;
   public static boolean configSpecularMap = true;
   public static PropertyDefaultTrueFalse configOldLighting = new PropertyDefaultTrueFalse("oldLighting", "Classic Lighting", 0);
   public static PropertyDefaultTrueFalse configOldHandLight = new PropertyDefaultTrueFalse("oldHandLight", "Old Hand Light", 0);
   public static int configAntialiasingLevel = 0;
   public static final int texMinFilRange = 3;
   public static final int texMagFilRange = 2;
   public static final String[] texMinFilDesc = new String[]{"Nearest", "Nearest-Nearest", "Nearest-Linear"};
   public static final String[] texMagFilDesc = new String[]{"Nearest", "Linear"};
   public static final int[] texMinFilValue = new int[]{9728, 9984, 9986};
   public static final int[] texMagFilValue = new int[]{9728, 9729};
   static IShaderPack shaderPack = null;
   public static boolean shaderPackLoaded = false;
   static File currentshader;
   static String currentshadername;
   public static String packNameNone = "OFF";
   static String packNameDefault = "(internal)";
   static String shaderpacksdirname = "shaderpacks";
   static String optionsfilename = "optionsshaders.txt";
   static File shadersdir;
   static File shaderpacksdir;
   static File configFile;
   static ShaderOption[] shaderPackOptions = null;
   static Set<String> shaderPackOptionSliders = null;
   static ShaderProfile[] shaderPackProfiles = null;
   static Map<String, ScreenShaderOptions> shaderPackGuiScreens = null;
   public static PropertyDefaultFastFancyOff shaderPackClouds = new PropertyDefaultFastFancyOff("clouds", "Clouds", 0);
   public static PropertyDefaultTrueFalse shaderPackOldLighting = new PropertyDefaultTrueFalse("oldLighting", "Classic Lighting", 0);
   public static PropertyDefaultTrueFalse shaderPackOldHandLight = new PropertyDefaultTrueFalse("oldHandLight", "Old Hand Light", 0);
   public static PropertyDefaultTrueFalse shaderPackDynamicHandLight = new PropertyDefaultTrueFalse("dynamicHandLight", "Dynamic Hand Light", 0);
   public static PropertyDefaultTrueFalse shaderPackShadowTranslucent = new PropertyDefaultTrueFalse("shadowTranslucent", "Shadow Translucent", 0);
   public static PropertyDefaultTrueFalse shaderPackUnderwaterOverlay = new PropertyDefaultTrueFalse("underwaterOverlay", "Underwater Overlay", 0);
   public static PropertyDefaultTrueFalse shaderPackSun = new PropertyDefaultTrueFalse("sun", "Sun", 0);
   public static PropertyDefaultTrueFalse shaderPackMoon = new PropertyDefaultTrueFalse("moon", "Moon", 0);
   public static PropertyDefaultTrueFalse shaderPackVignette = new PropertyDefaultTrueFalse("vignette", "Vignette", 0);
   public static PropertyDefaultTrueFalse shaderPackBackFaceSolid = new PropertyDefaultTrueFalse("backFace.solid", "Back-face Solid", 0);
   public static PropertyDefaultTrueFalse shaderPackBackFaceCutout = new PropertyDefaultTrueFalse("backFace.cutout", "Back-face Cutout", 0);
   public static PropertyDefaultTrueFalse shaderPackBackFaceCutoutMipped = new PropertyDefaultTrueFalse("backFace.cutoutMipped", "Back-face Cutout Mipped", 0);
   public static PropertyDefaultTrueFalse shaderPackBackFaceTranslucent = new PropertyDefaultTrueFalse("backFace.translucent", "Back-face Translucent", 0);
   private static Map<String, String> shaderPackResources = new HashMap();
   private static World currentWorld = null;
   private static List<Integer> shaderPackDimensions = new ArrayList();
   private static CustomTexture[] customTexturesGbuffers = null;
   private static CustomTexture[] customTexturesComposite = null;
   private static CustomTexture[] customTexturesDeferred = null;
   private static String noiseTexturePath = null;
   private static CustomUniforms customUniforms = null;
   private static final int STAGE_GBUFFERS = 0;
   private static final int STAGE_COMPOSITE = 1;
   private static final int STAGE_DEFERRED = 2;
   private static final String[] STAGE_NAMES = new String[]{"gbuffers", "composite", "deferred"};
   public static final boolean enableShadersOption = true;
   private static final boolean enableShadersDebug = true;
   private static final boolean saveFinalShaders = System.getProperty("shaders.debug.save", "false").equals("true");
   public static float blockLightLevel05 = 0.5F;
   public static float blockLightLevel06 = 0.6F;
   public static float blockLightLevel08 = 0.8F;
   public static float aoLevel = -1.0F;
   public static float sunPathRotation = 0.0F;
   public static float shadowAngleInterval = 0.0F;
   public static int fogMode = 0;
   public static float fogColorR;
   public static float fogColorG;
   public static float fogColorB;
   public static float shadowIntervalSize = 2.0F;
   public static int terrainIconSize = 16;
   public static int[] terrainTextureSize = new int[2];
   private static ICustomTexture noiseTexture;
   private static boolean noiseTextureEnabled = false;
   private static int noiseTextureResolution = 256;
   static final int[] dfbColorTexturesA = new int[16];
   static final int[] colorTexturesToggle = new int[8];
   static final int[] colorTextureTextureImageUnit = new int[]{0, 1, 2, 3, 7, 8, 9, 10};
   static final boolean[][] programsToggleColorTextures = new boolean[44][8];
   private static final int bigBufferSize = 2548;
   private static final ByteBuffer bigBuffer = (ByteBuffer)BufferUtils.createByteBuffer(2548).limit(0);
   static final float[] faProjection = new float[16];
   static final float[] faProjectionInverse = new float[16];
   static final float[] faModelView = new float[16];
   static final float[] faModelViewInverse = new float[16];
   static final float[] faShadowProjection = new float[16];
   static final float[] faShadowProjectionInverse = new float[16];
   static final float[] faShadowModelView = new float[16];
   static final float[] faShadowModelViewInverse = new float[16];
   static final FloatBuffer projection = nextFloatBuffer(16);
   static final FloatBuffer projectionInverse = nextFloatBuffer(16);
   static final FloatBuffer modelView = nextFloatBuffer(16);
   static final FloatBuffer modelViewInverse = nextFloatBuffer(16);
   static final FloatBuffer shadowProjection = nextFloatBuffer(16);
   static final FloatBuffer shadowProjectionInverse = nextFloatBuffer(16);
   static final FloatBuffer shadowModelView = nextFloatBuffer(16);
   static final FloatBuffer shadowModelViewInverse = nextFloatBuffer(16);
   static final FloatBuffer previousProjection = nextFloatBuffer(16);
   static final FloatBuffer previousModelView = nextFloatBuffer(16);
   static final FloatBuffer tempMatrixDirectBuffer = nextFloatBuffer(16);
   static final FloatBuffer tempDirectFloatBuffer = nextFloatBuffer(16);
   static final IntBuffer dfbColorTextures = nextIntBuffer(16);
   static final IntBuffer dfbDepthTextures = nextIntBuffer(3);
   static final IntBuffer sfbColorTextures = nextIntBuffer(8);
   static final IntBuffer sfbDepthTextures = nextIntBuffer(2);
   static final IntBuffer dfbDrawBuffers = nextIntBuffer(8);
   static final IntBuffer sfbDrawBuffers = nextIntBuffer(8);
   static final IntBuffer drawBuffersNone = nextIntBuffer(8);
   static final IntBuffer drawBuffersAll = nextIntBuffer(8);
   static final IntBuffer drawBuffersClear0 = nextIntBuffer(8);
   static final IntBuffer drawBuffersClear1 = nextIntBuffer(8);
   static final IntBuffer drawBuffersClearColor = nextIntBuffer(8);
   static final IntBuffer drawBuffersColorAtt0 = nextIntBuffer(8);
   static final IntBuffer[] drawBuffersBuffer = nextIntBufferArray(44, 8);
   static Map<Block, Integer> mapBlockToEntityData;
   private static final String[] formatNames = new String[]{"R8", "RG8", "RGB8", "RGBA8", "R8_SNORM", "RG8_SNORM", "RGB8_SNORM", "RGBA8_SNORM", "R16", "RG16", "RGB16", "RGBA16", "R16_SNORM", "RG16_SNORM", "RGB16_SNORM", "RGBA16_SNORM", "R16F", "RG16F", "RGB16F", "RGBA16F", "R32F", "RG32F", "RGB32F", "RGBA32F", "R32I", "RG32I", "RGB32I", "RGBA32I", "R32UI", "RG32UI", "RGB32UI", "RGBA32UI", "R3_G3_B2", "RGB5_A1", "RGB10_A2", "R11F_G11F_B10F", "RGB9_E5"};
   private static final int[] formatIds = new int[]{'\u8229', '\u822b', '\u8051', '\u8058', '\u8f94', '\u8f95', '\u8f96', '\u8f97', '\u822a', '\u822c', '\u8054', '\u805b', '\u8f98', '\u8f99', '\u8f9a', '\u8f9b', '\u822d', '\u822f', '\u881b', '\u881a', '\u822e', '\u8230', '\u8815', '\u8814', '\u8235', '\u823b', '\u8d83', '\u8d82', '\u8236', '\u823c', '\u8d71', '\u8d70', 10768, '\u8057', '\u8059', '\u8c3a', '\u8c3d'};
   private static final Pattern patternLoadEntityDataMap = Pattern.compile("\\s*([\\w:]+)\\s*=\\s*([-]?\\d+)\\s*");
   public static int[] entityData = new int[32];
   public static int entityDataIndex = 0;

   private static ByteBuffer nextByteBuffer(int size) {
      ByteBuffer bytebuffer = bigBuffer;
      int i = bytebuffer.limit();
      bytebuffer.position(i).limit(i + size);
      return bytebuffer.slice();
   }

   private static IntBuffer nextIntBuffer(int size) {
      ByteBuffer bytebuffer = bigBuffer;
      int i = bytebuffer.limit();
      bytebuffer.position(i).limit(i + size * 4);
      return bytebuffer.asIntBuffer();
   }

   private static FloatBuffer nextFloatBuffer(int size) {
      ByteBuffer bytebuffer = bigBuffer;
      int i = bytebuffer.limit();
      bytebuffer.position(i).limit(i + size * 4);
      return bytebuffer.asFloatBuffer();
   }

   private static IntBuffer[] nextIntBufferArray(int count, int size) {
      IntBuffer[] aintbuffer = new IntBuffer[count];

      for(int i = 0; i < count; ++i) {
         aintbuffer[i] = nextIntBuffer(size);
      }

      return aintbuffer;
   }

   public static void loadConfig() {
      SMCLog.info("Load ShadersMod configuration.");

      try {
         if(!shaderpacksdir.exists()) {
            shaderpacksdir.mkdir();
         }
      } catch (Exception var8) {
         SMCLog.severe("Failed to open the shaderpacks directory: " + shaderpacksdir);
      }

      shadersConfig = new PropertiesOrdered();
      shadersConfig.setProperty(EnumShaderOption.SHADER_PACK.getPropertyKey(), "");
      if(configFile.exists()) {
         try {
            FileReader filereader = new FileReader(configFile);
            shadersConfig.load((Reader)filereader);
            filereader.close();
         } catch (Exception var7) {
            ;
         }
      }

      if(!configFile.exists()) {
         try {
            storeConfig();
         } catch (Exception var6) {
            ;
         }
      }

      EnumShaderOption[] aenumshaderoption = EnumShaderOption.values();

      for(int i = 0; i < aenumshaderoption.length; ++i) {
         EnumShaderOption enumshaderoption = aenumshaderoption[i];
         String s = enumshaderoption.getPropertyKey();
         String s1 = enumshaderoption.getValueDefault();
         String s2 = shadersConfig.getProperty(s, s1);
         setEnumShaderOption(enumshaderoption, s2);
      }

      loadShaderPack();
   }

   private static void setEnumShaderOption(EnumShaderOption eso, String str) {
      if(str == null) {
         str = eso.getValueDefault();
      }

      switch(eso) {
      case ANTIALIASING:
         configAntialiasingLevel = Config.parseInt(str, 0);
         break;
      case NORMAL_MAP:
         configNormalMap = Config.parseBoolean(str, true);
         break;
      case SPECULAR_MAP:
         configSpecularMap = Config.parseBoolean(str, true);
         break;
      case RENDER_RES_MUL:
         configRenderResMul = Config.parseFloat(str, 1.0F);
         break;
      case SHADOW_RES_MUL:
         configShadowResMul = Config.parseFloat(str, 1.0F);
         break;
      case HAND_DEPTH_MUL:
         configHandDepthMul = Config.parseFloat(str, 0.125F);
         break;
      case CLOUD_SHADOW:
         configCloudShadow = Config.parseBoolean(str, true);
         break;
      case OLD_HAND_LIGHT:
         configOldHandLight.setPropertyValue(str);
         break;
      case OLD_LIGHTING:
         configOldLighting.setPropertyValue(str);
         break;
      case SHADER_PACK:
         currentshadername = str;
         break;
      case TWEAK_BLOCK_DAMAGE:
         configTweakBlockDamage = Config.parseBoolean(str, true);
         break;
      case SHADOW_CLIP_FRUSTRUM:
         configShadowClipFrustrum = Config.parseBoolean(str, true);
         break;
      case TEX_MIN_FIL_B:
         configTexMinFilB = Config.parseInt(str, 0);
         break;
      case TEX_MIN_FIL_N:
         configTexMinFilN = Config.parseInt(str, 0);
         break;
      case TEX_MIN_FIL_S:
         configTexMinFilS = Config.parseInt(str, 0);
         break;
      case TEX_MAG_FIL_B:
         configTexMagFilB = Config.parseInt(str, 0);
         break;
      case TEX_MAG_FIL_N:
         configTexMagFilB = Config.parseInt(str, 0);
         break;
      case TEX_MAG_FIL_S:
         configTexMagFilB = Config.parseInt(str, 0);
         break;
      default:
         throw new IllegalArgumentException("Unknown option: " + eso);
      }

   }

   public static void storeConfig() {
      SMCLog.info("Save ShadersMod configuration.");
      if(shadersConfig == null) {
         shadersConfig = new PropertiesOrdered();
      }

      EnumShaderOption[] aenumshaderoption = EnumShaderOption.values();

      for(int i = 0; i < aenumshaderoption.length; ++i) {
         EnumShaderOption enumshaderoption = aenumshaderoption[i];
         String s = enumshaderoption.getPropertyKey();
         String s1 = getEnumShaderOption(enumshaderoption);
         shadersConfig.setProperty(s, s1);
      }

      try {
         FileWriter filewriter = new FileWriter(configFile);
         shadersConfig.store((Writer)filewriter, (String)null);
         filewriter.close();
      } catch (Exception exception) {
         SMCLog.severe("Error saving configuration: " + exception.getClass().getName() + ": " + exception.getMessage());
      }

   }

   public static String getEnumShaderOption(EnumShaderOption eso) {
      switch(eso) {
      case ANTIALIASING:
         return Integer.toString(configAntialiasingLevel);
      case NORMAL_MAP:
         return Boolean.toString(configNormalMap);
      case SPECULAR_MAP:
         return Boolean.toString(configSpecularMap);
      case RENDER_RES_MUL:
         return Float.toString(configRenderResMul);
      case SHADOW_RES_MUL:
         return Float.toString(configShadowResMul);
      case HAND_DEPTH_MUL:
         return Float.toString(configHandDepthMul);
      case CLOUD_SHADOW:
         return Boolean.toString(configCloudShadow);
      case OLD_HAND_LIGHT:
         return configOldHandLight.getPropertyValue();
      case OLD_LIGHTING:
         return configOldLighting.getPropertyValue();
      case SHADER_PACK:
         return currentshadername;
      case TWEAK_BLOCK_DAMAGE:
         return Boolean.toString(configTweakBlockDamage);
      case SHADOW_CLIP_FRUSTRUM:
         return Boolean.toString(configShadowClipFrustrum);
      case TEX_MIN_FIL_B:
         return Integer.toString(configTexMinFilB);
      case TEX_MIN_FIL_N:
         return Integer.toString(configTexMinFilN);
      case TEX_MIN_FIL_S:
         return Integer.toString(configTexMinFilS);
      case TEX_MAG_FIL_B:
         return Integer.toString(configTexMagFilB);
      case TEX_MAG_FIL_N:
         return Integer.toString(configTexMagFilB);
      case TEX_MAG_FIL_S:
         return Integer.toString(configTexMagFilB);
      default:
         throw new IllegalArgumentException("Unknown option: " + eso);
      }
   }

   public static void setShaderPack(String par1name) {
      currentshadername = par1name;
      shadersConfig.setProperty(EnumShaderOption.SHADER_PACK.getPropertyKey(), par1name);
      loadShaderPack();
   }

   public static void loadShaderPack() {
      boolean flag = shaderPackLoaded;
      boolean flag1 = isOldLighting();
      shaderPackLoaded = false;
      if(shaderPack != null) {
         shaderPack.close();
         shaderPack = null;
         shaderPackResources.clear();
         shaderPackDimensions.clear();
         shaderPackOptions = null;
         shaderPackOptionSliders = null;
         shaderPackProfiles = null;
         shaderPackGuiScreens = null;
         shaderPackClouds.resetValue();
         shaderPackOldHandLight.resetValue();
         shaderPackDynamicHandLight.resetValue();
         shaderPackOldLighting.resetValue();
         resetCustomTextures();
         noiseTexturePath = null;
      }

      boolean flag2 = false;
      if(Config.isAntialiasing()) {
         SMCLog.info("Shaders can not be loaded, Antialiasing is enabled: " + Config.getAntialiasingLevel() + "x");
         flag2 = true;
      }

      if(Config.isAnisotropicFiltering()) {
         SMCLog.info("Shaders can not be loaded, Anisotropic Filtering is enabled: " + Config.getAnisotropicFilterLevel() + "x");
         flag2 = true;
      }

      if(Config.isFastRender()) {
         SMCLog.info("Shaders can not be loaded, Fast Render is enabled.");
         flag2 = true;
      }

      String s = shadersConfig.getProperty(EnumShaderOption.SHADER_PACK.getPropertyKey(), packNameDefault);
      if(!s.isEmpty() && !s.equals(packNameNone) && !flag2) {
         if(s.equals(packNameDefault)) {
            shaderPack = new ShaderPackDefault();
            shaderPackLoaded = true;
         } else {
            try {
               File file1 = new File(shaderpacksdir, s);
               if(file1.isDirectory()) {
                  shaderPack = new ShaderPackFolder(s, file1);
                  shaderPackLoaded = true;
               } else if(file1.isFile() && s.toLowerCase().endsWith(".zip")) {
                  shaderPack = new ShaderPackZip(s, file1);
                  shaderPackLoaded = true;
               }
            } catch (Exception var6) {
               ;
            }
         }
      }

      if(shaderPack != null) {
         SMCLog.info("Loaded shaderpack: " + getShaderPackName());
      } else {
         SMCLog.info("No shaderpack loaded.");
         shaderPack = new ShaderPackNone();
      }

      loadShaderPackResources();
      loadShaderPackDimensions();
      shaderPackOptions = loadShaderPackOptions();
      loadShaderPackProperties();
      boolean flag4 = shaderPackLoaded != flag;
      boolean flag3 = isOldLighting() != flag1;
      if(flag4 || flag3) {
         DefaultVertexFormats.updateVertexFormats();
         if(Reflector.LightUtil.exists()) {
            Reflector.LightUtil_itemConsumer.setValue((Object)null);
            Reflector.LightUtil_tessellator.setValue((Object)null);
         }

         updateBlockLightLevel();
         if(mc.func_110438_M() != null) {
            mc.func_175603_A();
         }
      }

   }

   private static void loadShaderPackDimensions() {
      shaderPackDimensions.clear();

      for(int i = -128; i <= 128; ++i) {
         String s = "/shaders/world" + i;
         if(shaderPack.hasDirectory(s)) {
            shaderPackDimensions.add(Integer.valueOf(i));
         }
      }

      if(shaderPackDimensions.size() > 0) {
         Integer[] ainteger = (Integer[])((Integer[])shaderPackDimensions.toArray(new Integer[shaderPackDimensions.size()]));
         Config.dbg("[Shaders] Worlds: " + Config.arrayToString((Object[])ainteger));
      }

   }

   private static void loadShaderPackProperties() {
      shaderPackClouds.resetValue();
      shaderPackOldHandLight.resetValue();
      shaderPackDynamicHandLight.resetValue();
      shaderPackOldLighting.resetValue();
      shaderPackShadowTranslucent.resetValue();
      shaderPackUnderwaterOverlay.resetValue();
      shaderPackSun.resetValue();
      shaderPackMoon.resetValue();
      shaderPackVignette.resetValue();
      shaderPackBackFaceSolid.resetValue();
      shaderPackBackFaceCutout.resetValue();
      shaderPackBackFaceCutoutMipped.resetValue();
      shaderPackBackFaceTranslucent.resetValue();
      BlockAliases.reset();
      customUniforms = null;
      LegacyUniforms.reset();
      if(shaderPack != null) {
         BlockAliases.update(shaderPack);
         String s = "/shaders/shaders.properties";

         try {
            InputStream inputstream = shaderPack.getResourceAsStream(s);
            if(inputstream == null) {
               return;
            }

            Properties properties = new PropertiesOrdered();
            properties.load(inputstream);
            inputstream.close();
            shaderPackClouds.loadFrom(properties);
            shaderPackOldHandLight.loadFrom(properties);
            shaderPackDynamicHandLight.loadFrom(properties);
            shaderPackOldLighting.loadFrom(properties);
            shaderPackShadowTranslucent.loadFrom(properties);
            shaderPackUnderwaterOverlay.loadFrom(properties);
            shaderPackSun.loadFrom(properties);
            shaderPackVignette.loadFrom(properties);
            shaderPackMoon.loadFrom(properties);
            shaderPackBackFaceSolid.loadFrom(properties);
            shaderPackBackFaceCutout.loadFrom(properties);
            shaderPackBackFaceCutoutMipped.loadFrom(properties);
            shaderPackBackFaceTranslucent.loadFrom(properties);
            shaderPackOptionSliders = ShaderPackParser.parseOptionSliders(properties, shaderPackOptions);
            shaderPackProfiles = ShaderPackParser.parseProfiles(properties, shaderPackOptions);
            shaderPackGuiScreens = ShaderPackParser.parseGuiScreens(properties, shaderPackProfiles, shaderPackOptions);
            customTexturesGbuffers = loadCustomTextures(properties, 0);
            customTexturesComposite = loadCustomTextures(properties, 1);
            customTexturesDeferred = loadCustomTextures(properties, 2);
            noiseTexturePath = properties.getProperty("texture.noise");
            if(noiseTexturePath != null) {
               noiseTextureEnabled = true;
            }

            customUniforms = ShaderPackParser.parseCustomUniforms(properties);
         } catch (IOException var3) {
            Config.warn("[Shaders] Error reading: " + s);
         }

      }
   }

   private static CustomTexture[] loadCustomTextures(Properties props, int stage) {
      String s = "texture." + STAGE_NAMES[stage] + ".";
      Set set = props.keySet();
      List<CustomTexture> list = new ArrayList();

      for(String s1 : set) {
         if(s1.startsWith(s)) {
            String s2 = s1.substring(s.length());
            String s3 = props.getProperty(s1).trim();
            int i = getTextureIndex(stage, s2);
            if(i < 0) {
               SMCLog.warning("Invalid texture name: " + s1);
            } else {
               CustomTexture customtexture = loadCustomTexture(i, s3);
               if(customtexture != null) {
                  list.add(customtexture);
               }
            }
         }
      }

      if(list.size() <= 0) {
         return null;
      } else {
         CustomTexture[] acustomtexture = (CustomTexture[])((CustomTexture[])list.toArray(new CustomTexture[list.size()]));
         return acustomtexture;
      }
   }

   private static CustomTexture loadCustomTexture(int index, String path) {
      if(path == null) {
         return null;
      } else {
         path = path.trim();
         if(path.indexOf(46) < 0) {
            path = path + ".png";
         }

         try {
            String s = "shaders/" + StrUtils.removePrefix(path, "/");
            InputStream inputstream = shaderPack.getResourceAsStream(s);
            if(inputstream == null) {
               SMCLog.warning("Texture not found: " + path);
               return null;
            } else {
               IOUtils.closeQuietly(inputstream);
               SimpleShaderTexture simpleshadertexture = new SimpleShaderTexture(s);
               simpleshadertexture.func_110551_a(mc.func_110442_L());
               CustomTexture customtexture = new CustomTexture(index, s, simpleshadertexture);
               return customtexture;
            }
         } catch (IOException ioexception) {
            SMCLog.warning("Error loading texture: " + path);
            SMCLog.warning("" + ioexception.getClass().getName() + ": " + ioexception.getMessage());
            return null;
         }
      }
   }

   private static int getTextureIndex(int stage, String name) {
      if(stage == 0) {
         if(name.equals("texture")) {
            return 0;
         }

         if(name.equals("lightmap")) {
            return 1;
         }

         if(name.equals("normals")) {
            return 2;
         }

         if(name.equals("specular")) {
            return 3;
         }

         if(name.equals("shadowtex0") || name.equals("watershadow")) {
            return 4;
         }

         if(name.equals("shadow")) {
            return waterShadowEnabled?5:4;
         }

         if(name.equals("shadowtex1")) {
            return 5;
         }

         if(name.equals("depthtex0")) {
            return 6;
         }

         if(name.equals("gaux1")) {
            return 7;
         }

         if(name.equals("gaux2")) {
            return 8;
         }

         if(name.equals("gaux3")) {
            return 9;
         }

         if(name.equals("gaux4")) {
            return 10;
         }

         if(name.equals("depthtex1")) {
            return 12;
         }

         if(name.equals("shadowcolor0") || name.equals("shadowcolor")) {
            return 13;
         }

         if(name.equals("shadowcolor1")) {
            return 14;
         }

         if(name.equals("noisetex")) {
            return 15;
         }
      }

      if(stage == 1 || stage == 2) {
         if(name.equals("colortex0") || name.equals("colortex0")) {
            return 0;
         }

         if(name.equals("colortex1") || name.equals("gdepth")) {
            return 1;
         }

         if(name.equals("colortex2") || name.equals("gnormal")) {
            return 2;
         }

         if(name.equals("colortex3") || name.equals("composite")) {
            return 3;
         }

         if(name.equals("shadowtex0") || name.equals("watershadow")) {
            return 4;
         }

         if(name.equals("shadow")) {
            return waterShadowEnabled?5:4;
         }

         if(name.equals("shadowtex1")) {
            return 5;
         }

         if(name.equals("depthtex0") || name.equals("gdepthtex")) {
            return 6;
         }

         if(name.equals("colortex4") || name.equals("gaux1")) {
            return 7;
         }

         if(name.equals("colortex5") || name.equals("gaux2")) {
            return 8;
         }

         if(name.equals("colortex6") || name.equals("gaux3")) {
            return 9;
         }

         if(name.equals("colortex7") || name.equals("gaux4")) {
            return 10;
         }

         if(name.equals("depthtex1")) {
            return 11;
         }

         if(name.equals("depthtex2")) {
            return 12;
         }

         if(name.equals("shadowcolor0") || name.equals("shadowcolor")) {
            return 13;
         }

         if(name.equals("shadowcolor1")) {
            return 14;
         }

         if(name.equals("noisetex")) {
            return 15;
         }
      }

      return -1;
   }

   private static void bindCustomTextures(CustomTexture[] cts) {
      if(cts != null) {
         for(int i = 0; i < cts.length; ++i) {
            CustomTexture customtexture = cts[i];
            GlStateManager.func_179138_g('\u84c0' + customtexture.getTextureUnit());
            ITextureObject itextureobject = customtexture.getTexture();
            GlStateManager.func_179144_i(itextureobject.func_110552_b());
         }

      }
   }

   private static void resetCustomTextures() {
      deleteCustomTextures(customTexturesGbuffers);
      deleteCustomTextures(customTexturesComposite);
      deleteCustomTextures(customTexturesDeferred);
      customTexturesGbuffers = null;
      customTexturesComposite = null;
      customTexturesDeferred = null;
   }

   private static void deleteCustomTextures(CustomTexture[] cts) {
      if(cts != null) {
         for(int i = 0; i < cts.length; ++i) {
            CustomTexture customtexture = cts[i];
            customtexture.deleteTexture();
         }

      }
   }

   public static ShaderOption[] getShaderPackOptions(String screenName) {
      ShaderOption[] ashaderoption = (ShaderOption[])shaderPackOptions.clone();
      if(shaderPackGuiScreens == null) {
         if(shaderPackProfiles != null) {
            ShaderOptionProfile shaderoptionprofile = new ShaderOptionProfile(shaderPackProfiles, ashaderoption);
            ashaderoption = (ShaderOption[])((ShaderOption[])Config.addObjectToArray(ashaderoption, shaderoptionprofile, 0));
         }

         ashaderoption = getVisibleOptions(ashaderoption);
         return ashaderoption;
      } else {
         String s = screenName != null?"screen." + screenName:"screen";
         ScreenShaderOptions screenshaderoptions = (ScreenShaderOptions)shaderPackGuiScreens.get(s);
         if(screenshaderoptions == null) {
            return new ShaderOption[0];
         } else {
            ShaderOption[] ashaderoption1 = screenshaderoptions.getShaderOptions();
            List<ShaderOption> list = new ArrayList();

            for(int i = 0; i < ashaderoption1.length; ++i) {
               ShaderOption shaderoption = ashaderoption1[i];
               if(shaderoption == null) {
                  list.add((ShaderOption)null);
               } else if(shaderoption instanceof ShaderOptionRest) {
                  ShaderOption[] ashaderoption2 = getShaderOptionsRest(shaderPackGuiScreens, ashaderoption);
                  list.addAll(Arrays.<ShaderOption>asList(ashaderoption2));
               } else {
                  list.add(shaderoption);
               }
            }

            ShaderOption[] ashaderoption3 = (ShaderOption[])((ShaderOption[])list.toArray(new ShaderOption[list.size()]));
            return ashaderoption3;
         }
      }
   }

   public static int getShaderPackColumns(String screenName, int def) {
      String s = screenName != null?"screen." + screenName:"screen";
      if(shaderPackGuiScreens == null) {
         return def;
      } else {
         ScreenShaderOptions screenshaderoptions = (ScreenShaderOptions)shaderPackGuiScreens.get(s);
         return screenshaderoptions == null?def:screenshaderoptions.getColumns();
      }
   }

   private static ShaderOption[] getShaderOptionsRest(Map<String, ScreenShaderOptions> mapScreens, ShaderOption[] ops) {
      Set<String> set = new HashSet();

      for(String s : mapScreens.keySet()) {
         ScreenShaderOptions screenshaderoptions = (ScreenShaderOptions)mapScreens.get(s);
         ShaderOption[] ashaderoption = screenshaderoptions.getShaderOptions();

         for(int i = 0; i < ashaderoption.length; ++i) {
            ShaderOption shaderoption = ashaderoption[i];
            if(shaderoption != null) {
               set.add(shaderoption.getName());
            }
         }
      }

      List<ShaderOption> list = new ArrayList();

      for(int j = 0; j < ops.length; ++j) {
         ShaderOption shaderoption1 = ops[j];
         if(shaderoption1.isVisible()) {
            String s1 = shaderoption1.getName();
            if(!set.contains(s1)) {
               list.add(shaderoption1);
            }
         }
      }

      ShaderOption[] ashaderoption1 = (ShaderOption[])((ShaderOption[])list.toArray(new ShaderOption[list.size()]));
      return ashaderoption1;
   }

   public static ShaderOption getShaderOption(String name) {
      return ShaderUtils.getShaderOption(name, shaderPackOptions);
   }

   public static ShaderOption[] getShaderPackOptions() {
      return shaderPackOptions;
   }

   public static boolean isShaderPackOptionSlider(String name) {
      return shaderPackOptionSliders == null?false:shaderPackOptionSliders.contains(name);
   }

   private static ShaderOption[] getVisibleOptions(ShaderOption[] ops) {
      List<ShaderOption> list = new ArrayList();

      for(int i = 0; i < ops.length; ++i) {
         ShaderOption shaderoption = ops[i];
         if(shaderoption.isVisible()) {
            list.add(shaderoption);
         }
      }

      ShaderOption[] ashaderoption = (ShaderOption[])((ShaderOption[])list.toArray(new ShaderOption[list.size()]));
      return ashaderoption;
   }

   public static void saveShaderPackOptions() {
      saveShaderPackOptions(shaderPackOptions, shaderPack);
   }

   private static void saveShaderPackOptions(ShaderOption[] sos, IShaderPack sp) {
      Properties properties = new Properties();
      if(shaderPackOptions != null) {
         for(int i = 0; i < sos.length; ++i) {
            ShaderOption shaderoption = sos[i];
            if(shaderoption.isChanged() && shaderoption.isEnabled()) {
               properties.setProperty(shaderoption.getName(), shaderoption.getValue());
            }
         }
      }

      try {
         saveOptionProperties(sp, properties);
      } catch (IOException ioexception) {
         Config.warn("[Shaders] Error saving configuration for " + shaderPack.getName());
         ioexception.printStackTrace();
      }

   }

   private static void saveOptionProperties(IShaderPack sp, Properties props) throws IOException {
      String s = shaderpacksdirname + "/" + sp.getName() + ".txt";
      File file1 = new File(Minecraft.func_71410_x().field_71412_D, s);
      if(props.isEmpty()) {
         file1.delete();
      } else {
         FileOutputStream fileoutputstream = new FileOutputStream(file1);
         props.store((OutputStream)fileoutputstream, (String)null);
         fileoutputstream.flush();
         fileoutputstream.close();
      }
   }

   private static ShaderOption[] loadShaderPackOptions() {
      try {
         ShaderOption[] ashaderoption = ShaderPackParser.parseShaderPackOptions(shaderPack, programNames, shaderPackDimensions);
         Properties properties = loadOptionProperties(shaderPack);

         for(int i = 0; i < ashaderoption.length; ++i) {
            ShaderOption shaderoption = ashaderoption[i];
            String s = properties.getProperty(shaderoption.getName());
            if(s != null) {
               shaderoption.resetValue();
               if(!shaderoption.setValue(s)) {
                  Config.warn("[Shaders] Invalid value, option: " + shaderoption.getName() + ", value: " + s);
               }
            }
         }

         return ashaderoption;
      } catch (IOException ioexception) {
         Config.warn("[Shaders] Error reading configuration for " + shaderPack.getName());
         ioexception.printStackTrace();
         return null;
      }
   }

   private static Properties loadOptionProperties(IShaderPack sp) throws IOException {
      Properties properties = new Properties();
      String s = shaderpacksdirname + "/" + sp.getName() + ".txt";
      File file1 = new File(Minecraft.func_71410_x().field_71412_D, s);
      if(file1.exists() && file1.isFile() && file1.canRead()) {
         FileInputStream fileinputstream = new FileInputStream(file1);
         properties.load((InputStream)fileinputstream);
         fileinputstream.close();
         return properties;
      } else {
         return properties;
      }
   }

   public static ShaderOption[] getChangedOptions(ShaderOption[] ops) {
      List<ShaderOption> list = new ArrayList();

      for(int i = 0; i < ops.length; ++i) {
         ShaderOption shaderoption = ops[i];
         if(shaderoption.isEnabled() && shaderoption.isChanged()) {
            list.add(shaderoption);
         }
      }

      ShaderOption[] ashaderoption = (ShaderOption[])((ShaderOption[])list.toArray(new ShaderOption[list.size()]));
      return ashaderoption;
   }

   private static String applyOptions(String line, ShaderOption[] ops) {
      if(ops != null && ops.length > 0) {
         for(int i = 0; i < ops.length; ++i) {
            ShaderOption shaderoption = ops[i];
            String s = shaderoption.getName();
            if(shaderoption.matchesLine(line)) {
               line = shaderoption.getSourceLine();
               break;
            }
         }

         return line;
      } else {
         return line;
      }
   }

   static ArrayList listOfShaders() {
      ArrayList<String> arraylist = new ArrayList();
      arraylist.add(packNameNone);
      arraylist.add(packNameDefault);

      try {
         if(!shaderpacksdir.exists()) {
            shaderpacksdir.mkdir();
         }

         File[] afile = shaderpacksdir.listFiles();

         for(int i = 0; i < afile.length; ++i) {
            File file1 = afile[i];
            String s = file1.getName();
            if(file1.isDirectory()) {
               if(!s.equals("debug")) {
                  File file2 = new File(file1, "shaders");
                  if(file2.exists() && file2.isDirectory()) {
                     arraylist.add(s);
                  }
               }
            } else if(file1.isFile() && s.toLowerCase().endsWith(".zip")) {
               arraylist.add(s);
            }
         }
      } catch (Exception var6) {
         ;
      }

      return arraylist;
   }

   static String versiontostring(int vv) {
      String s = Integer.toString(vv);
      return Integer.toString(Integer.parseInt(s.substring(1, 3))) + "." + Integer.toString(Integer.parseInt(s.substring(3, 5))) + "." + Integer.toString(Integer.parseInt(s.substring(5)));
   }

   static void checkOptifine() {
   }

   public static int checkFramebufferStatus(String location) {
      int i = EXTFramebufferObject.glCheckFramebufferStatusEXT('\u8d40');
      if(i != '\u8cd5') {
         System.err.format("FramebufferStatus 0x%04X at %s\n", new Object[]{Integer.valueOf(i), location});
      }

      return i;
   }

   public static int checkGLError(String location) {
      int i = GL11.glGetError();
      if(i != 0) {
         boolean flag = false;
         if(!flag) {
            if(i == 1286) {
               int j = EXTFramebufferObject.glCheckFramebufferStatusEXT('\u8d40');
               System.err.format("GL error 0x%04X: %s (Fb status 0x%04X) at %s\n", new Object[]{Integer.valueOf(i), GLU.gluErrorString(i), Integer.valueOf(j), location});
            } else {
               System.err.format("GL error 0x%04X: %s at %s\n", new Object[]{Integer.valueOf(i), GLU.gluErrorString(i), location});
            }
         }
      }

      return i;
   }

   public static int checkGLError(String location, String info) {
      int i = GL11.glGetError();
      if(i != 0) {
         System.err.format("GL error 0x%04x: %s at %s %s\n", new Object[]{Integer.valueOf(i), GLU.gluErrorString(i), location, info});
      }

      return i;
   }

   public static int checkGLError(String location, String info1, String info2) {
      int i = GL11.glGetError();
      if(i != 0) {
         System.err.format("GL error 0x%04x: %s at %s %s %s\n", new Object[]{Integer.valueOf(i), GLU.gluErrorString(i), location, info1, info2});
      }

      return i;
   }

   private static void printChat(String str) {
      mc.field_71456_v.func_146158_b().func_146227_a(new ChatComponentText(str));
   }

   private static void printChatAndLogError(String str) {
      SMCLog.severe(str);
      mc.field_71456_v.func_146158_b().func_146227_a(new ChatComponentText(str));
   }

   public static void printIntBuffer(String title, IntBuffer buf) {
      StringBuilder stringbuilder = new StringBuilder(128);
      stringbuilder.append(title).append(" [pos ").append(buf.position()).append(" lim ").append(buf.limit()).append(" cap ").append(buf.capacity()).append(" :");
      int i = buf.limit();

      for(int j = 0; j < i; ++j) {
         stringbuilder.append(" ").append(buf.get(j));
      }

      stringbuilder.append("]");
      SMCLog.info(stringbuilder.toString());
   }

   public static void startup(Minecraft mc) {
      checkShadersModInstalled();
      mc = mc;
      mc = Minecraft.func_71410_x();
      capabilities = GLContext.getCapabilities();
      glVersionString = GL11.glGetString(7938);
      glVendorString = GL11.glGetString(7936);
      glRendererString = GL11.glGetString(7937);
      SMCLog.info("ShadersMod version: 2.4.12");
      SMCLog.info("OpenGL Version: " + glVersionString);
      SMCLog.info("Vendor:  " + glVendorString);
      SMCLog.info("Renderer: " + glRendererString);
      SMCLog.info("Capabilities: " + (capabilities.OpenGL20?" 2.0 ":" - ") + (capabilities.OpenGL21?" 2.1 ":" - ") + (capabilities.OpenGL30?" 3.0 ":" - ") + (capabilities.OpenGL32?" 3.2 ":" - ") + (capabilities.OpenGL40?" 4.0 ":" - "));
      SMCLog.info("GL_MAX_DRAW_BUFFERS: " + GL11.glGetInteger('\u8824'));
      SMCLog.info("GL_MAX_COLOR_ATTACHMENTS_EXT: " + GL11.glGetInteger('\u8cdf'));
      SMCLog.info("GL_MAX_TEXTURE_IMAGE_UNITS: " + GL11.glGetInteger('\u8872'));
      hasGlGenMipmap = capabilities.OpenGL30;
      loadConfig();
   }

   private static String toStringYN(boolean b) {
      return b?"Y":"N";
   }

   public static void updateBlockLightLevel() {
      if(isOldLighting()) {
         blockLightLevel05 = 0.5F;
         blockLightLevel06 = 0.6F;
         blockLightLevel08 = 0.8F;
      } else {
         blockLightLevel05 = 1.0F;
         blockLightLevel06 = 1.0F;
         blockLightLevel08 = 1.0F;
      }

   }

   public static boolean isOldHandLight() {
      return !configOldHandLight.isDefault()?configOldHandLight.isTrue():(!shaderPackOldHandLight.isDefault()?shaderPackOldHandLight.isTrue():true);
   }

   public static boolean isDynamicHandLight() {
      return !shaderPackDynamicHandLight.isDefault()?shaderPackDynamicHandLight.isTrue():true;
   }

   public static boolean isOldLighting() {
      return !configOldLighting.isDefault()?configOldLighting.isTrue():(!shaderPackOldLighting.isDefault()?shaderPackOldLighting.isTrue():true);
   }

   public static boolean isRenderShadowTranslucent() {
      return !shaderPackShadowTranslucent.isFalse();
   }

   public static boolean isUnderwaterOverlay() {
      return !shaderPackUnderwaterOverlay.isFalse();
   }

   public static boolean isSun() {
      return !shaderPackSun.isFalse();
   }

   public static boolean isMoon() {
      return !shaderPackMoon.isFalse();
   }

   public static boolean isVignette() {
      return !shaderPackVignette.isFalse();
   }

   public static boolean isRenderBackFace(EnumWorldBlockLayer blockLayerIn) {
      switch(blockLayerIn) {
      case SOLID:
         return shaderPackBackFaceSolid.isTrue();
      case CUTOUT:
         return shaderPackBackFaceCutout.isTrue();
      case CUTOUT_MIPPED:
         return shaderPackBackFaceCutoutMipped.isTrue();
      case TRANSLUCENT:
         return shaderPackBackFaceTranslucent.isTrue();
      default:
         return false;
      }
   }

   public static void init() {
      boolean flag;
      if(!isInitializedOnce) {
         isInitializedOnce = true;
         flag = true;
      } else {
         flag = false;
      }

      if(!isShaderPackInitialized) {
         checkGLError("Shaders.init pre");
         if(getShaderPackName() != null) {
            ;
         }

         if(!capabilities.OpenGL20) {
            printChatAndLogError("No OpenGL 2.0");
         }

         if(!capabilities.GL_EXT_framebuffer_object) {
            printChatAndLogError("No EXT_framebuffer_object");
         }

         dfbDrawBuffers.position(0).limit(8);
         dfbColorTextures.position(0).limit(16);
         dfbDepthTextures.position(0).limit(3);
         sfbDrawBuffers.position(0).limit(8);
         sfbDepthTextures.position(0).limit(2);
         sfbColorTextures.position(0).limit(8);
         usedColorBuffers = 4;
         usedDepthBuffers = 1;
         usedShadowColorBuffers = 0;
         usedShadowDepthBuffers = 0;
         usedColorAttachs = 1;
         usedDrawBuffers = 1;
         Arrays.fill((int[])gbuffersFormat, (int)6408);
         Arrays.fill(gbuffersClear, true);
         Arrays.fill(shadowHardwareFilteringEnabled, false);
         Arrays.fill(shadowMipmapEnabled, false);
         Arrays.fill(shadowFilterNearest, false);
         Arrays.fill(shadowColorMipmapEnabled, false);
         Arrays.fill(shadowColorFilterNearest, false);
         centerDepthSmoothEnabled = false;
         noiseTextureEnabled = false;
         sunPathRotation = 0.0F;
         shadowIntervalSize = 2.0F;
         shadowMapWidth = 1024;
         shadowMapHeight = 1024;
         spShadowMapWidth = 1024;
         spShadowMapHeight = 1024;
         shadowMapFOV = 90.0F;
         shadowMapHalfPlane = 160.0F;
         shadowMapIsOrtho = true;
         shadowDistanceRenderMul = -1.0F;
         aoLevel = -1.0F;
         useEntityAttrib = false;
         useMidTexCoordAttrib = false;
         useMultiTexCoord3Attrib = false;
         useTangentAttrib = false;
         waterShadowEnabled = false;
         updateChunksErrorRecorded = false;
         updateBlockLightLevel();
         Smoother.reset();
         LegacyUniforms.reset();
         ShaderProfile shaderprofile = ShaderUtils.detectProfile(shaderPackProfiles, shaderPackOptions, false);
         String s = "";
         if(currentWorld != null) {
            int i = currentWorld.field_73011_w.func_177502_q();
            if(shaderPackDimensions.contains(Integer.valueOf(i))) {
               s = "world" + i + "/";
            }
         }

         if(saveFinalShaders) {
            clearDirectory(new File(shaderpacksdir, "debug"));
         }

         for(int k1 = 0; k1 < 44; ++k1) {
            String s1 = programNames[k1];
            if(s1.equals("")) {
               programsID[k1] = programsRef[k1] = 0;
               programsDrawBufSettings[k1] = null;
               programsColorAtmSettings[k1] = null;
               programsCompositeMipmapSetting[k1] = 0;
            } else {
               newDrawBufSetting = null;
               newColorAtmSetting = null;
               newCompositeMipmapSetting = 0;
               String s2 = s + s1;
               if(shaderprofile != null && shaderprofile.isProgramDisabled(s2)) {
                  SMCLog.info("Program disabled: " + s2);
                  s1 = "<disabled>";
                  s2 = s + s1;
               }

               String s3 = "/shaders/" + s2;
               int j = setupProgram(k1, s3 + ".vsh", s3 + ".fsh");
               if(j > 0) {
                  SMCLog.info("Program loaded: " + s2);
               }

               programsID[k1] = programsRef[k1] = j;
               programsDrawBufSettings[k1] = j != 0?newDrawBufSetting:null;
               programsColorAtmSettings[k1] = j != 0?newColorAtmSetting:null;
               programsCompositeMipmapSetting[k1] = j != 0?newCompositeMipmapSetting:0;
            }
         }

         int l1 = GL11.glGetInteger('\u8824');
         new HashMap();

         for(int i2 = 0; i2 < 44; ++i2) {
            Arrays.fill(programsToggleColorTextures[i2], false);
            if(i2 == 29) {
               programsDrawBuffers[i2] = null;
            } else if(programsID[i2] == 0) {
               if(i2 == 30) {
                  programsDrawBuffers[i2] = drawBuffersNone;
               } else {
                  programsDrawBuffers[i2] = drawBuffersColorAtt0;
               }
            } else {
               String s4 = programsDrawBufSettings[i2];
               if(s4 != null) {
                  IntBuffer intbuffer = drawBuffersBuffer[i2];
                  int k = s4.length();
                  if(k > usedDrawBuffers) {
                     usedDrawBuffers = k;
                  }

                  if(k > l1) {
                     k = l1;
                  }

                  programsDrawBuffers[i2] = intbuffer;
                  intbuffer.limit(k);

                  for(int l = 0; l < k; ++l) {
                     int i1 = 0;
                     if(s4.length() > l) {
                        int j1 = s4.charAt(l) - 48;
                        if(i2 != 30) {
                           if(j1 >= 0 && j1 <= 7) {
                              programsToggleColorTextures[i2][j1] = true;
                              i1 = j1 + '\u8ce0';
                              if(j1 > usedColorAttachs) {
                                 usedColorAttachs = j1;
                              }

                              if(j1 > usedColorBuffers) {
                                 usedColorBuffers = j1;
                              }
                           }
                        } else if(j1 >= 0 && j1 <= 1) {
                           i1 = j1 + '\u8ce0';
                           if(j1 > usedShadowColorBuffers) {
                              usedShadowColorBuffers = j1;
                           }
                        }
                     }

                     intbuffer.put(l, i1);
                  }
               } else if(i2 != 30 && i2 != 31 && i2 != 32) {
                  programsDrawBuffers[i2] = dfbDrawBuffers;
                  usedDrawBuffers = usedColorBuffers;
                  Arrays.fill(programsToggleColorTextures[i2], 0, usedColorBuffers, true);
               } else {
                  programsDrawBuffers[i2] = sfbDrawBuffers;
               }
            }
         }

         hasDeferredPrograms = false;

         for(int j2 = 0; j2 < 8; ++j2) {
            if(programsID[33 + j2] != 0) {
               hasDeferredPrograms = true;
               break;
            }
         }

         usedColorAttachs = usedColorBuffers;
         shadowPassInterval = usedShadowDepthBuffers > 0?1:0;
         shouldSkipDefaultShadow = usedShadowDepthBuffers > 0;
         SMCLog.info("usedColorBuffers: " + usedColorBuffers);
         SMCLog.info("usedDepthBuffers: " + usedDepthBuffers);
         SMCLog.info("usedShadowColorBuffers: " + usedShadowColorBuffers);
         SMCLog.info("usedShadowDepthBuffers: " + usedShadowDepthBuffers);
         SMCLog.info("usedColorAttachs: " + usedColorAttachs);
         SMCLog.info("usedDrawBuffers: " + usedDrawBuffers);
         dfbDrawBuffers.position(0).limit(usedDrawBuffers);
         dfbColorTextures.position(0).limit(usedColorBuffers * 2);

         for(int k2 = 0; k2 < usedDrawBuffers; ++k2) {
            dfbDrawBuffers.put(k2, '\u8ce0' + k2);
         }

         if(usedDrawBuffers > l1) {
            printChatAndLogError("[Shaders] Error: Not enough draw buffers, needed: " + usedDrawBuffers + ", available: " + l1);
         }

         sfbDrawBuffers.position(0).limit(usedShadowColorBuffers);

         for(int l2 = 0; l2 < usedShadowColorBuffers; ++l2) {
            sfbDrawBuffers.put(l2, '\u8ce0' + l2);
         }

         for(int i3 = 0; i3 < 44; ++i3) {
            int j3;
            for(j3 = i3; programsID[j3] == 0 && programBackups[j3] != j3; j3 = programBackups[j3]) {
               ;
            }

            if(j3 != i3 && i3 != 30) {
               programsID[i3] = programsID[j3];
               programsDrawBufSettings[i3] = programsDrawBufSettings[j3];
               programsDrawBuffers[i3] = programsDrawBuffers[j3];
            }
         }

         resize();
         resizeShadow();
         if(noiseTextureEnabled) {
            setupNoiseTexture();
         }

         if(defaultTexture == null) {
            defaultTexture = ShadersTex.createDefaultTexture();
         }

         GlStateManager.func_179094_E();
         GlStateManager.func_179114_b(-90.0F, 0.0F, 1.0F, 0.0F);
         preCelestialRotate();
         postCelestialRotate();
         GlStateManager.func_179121_F();
         isShaderPackInitialized = true;
         loadEntityDataMap();
         resetDisplayList();
         if(!flag) {
            ;
         }

         checkGLError("Shaders.init");
      }

   }

   public static void resetDisplayList() {
      ++numberResetDisplayList;
      needResetModels = true;
      SMCLog.info("Reset world renderers");
      mc.field_71438_f.func_72712_a();
   }

   public static void resetDisplayListModels() {
      if(needResetModels) {
         needResetModels = false;
         SMCLog.info("Reset model renderers");

         for(Render render : mc.func_175598_ae().getEntityRenderMap().values()) {
            if(render instanceof RendererLivingEntity) {
               RendererLivingEntity rendererlivingentity = (RendererLivingEntity)render;
               resetDisplayListModel(rendererlivingentity.func_177087_b());
            }
         }
      }

   }

   public static void resetDisplayListModel(ModelBase model) {
      if(model != null) {
         for(Object object : model.field_78092_r) {
            if(object instanceof ModelRenderer) {
               resetDisplayListModelRenderer((ModelRenderer)object);
            }
         }
      }

   }

   public static void resetDisplayListModelRenderer(ModelRenderer mrr) {
      mrr.resetDisplayList();
      if(mrr.field_78805_m != null) {
         int i = 0;

         for(int j = mrr.field_78805_m.size(); i < j; ++i) {
            resetDisplayListModelRenderer((ModelRenderer)mrr.field_78805_m.get(i));
         }
      }

   }

   private static int setupProgram(int program, String vShaderPath, String fShaderPath) {
      checkGLError("pre setupProgram");
      int i = ARBShaderObjects.glCreateProgramObjectARB();
      checkGLError("create");
      if(i != 0) {
         progUseEntityAttrib = false;
         progUseMidTexCoordAttrib = false;
         progUseTangentAttrib = false;
         int j = createVertShader(vShaderPath);
         int k = createFragShader(fShaderPath);
         checkGLError("create");
         if(j == 0 && k == 0) {
            ARBShaderObjects.glDeleteObjectARB(i);
            i = 0;
         } else {
            if(j != 0) {
               ARBShaderObjects.glAttachObjectARB(i, j);
               checkGLError("attach");
            }

            if(k != 0) {
               ARBShaderObjects.glAttachObjectARB(i, k);
               checkGLError("attach");
            }

            if(progUseEntityAttrib) {
               ARBVertexShader.glBindAttribLocationARB(i, entityAttrib, (CharSequence)"mc_Entity");
               checkGLError("mc_Entity");
            }

            if(progUseMidTexCoordAttrib) {
               ARBVertexShader.glBindAttribLocationARB(i, midTexCoordAttrib, (CharSequence)"mc_midTexCoord");
               checkGLError("mc_midTexCoord");
            }

            if(progUseTangentAttrib) {
               ARBVertexShader.glBindAttribLocationARB(i, tangentAttrib, (CharSequence)"at_tangent");
               checkGLError("at_tangent");
            }

            ARBShaderObjects.glLinkProgramARB(i);
            if(GL20.glGetProgrami(i, '\u8b82') != 1) {
               SMCLog.severe("Error linking program: " + i);
            }

            printLogInfo(i, vShaderPath + ", " + fShaderPath);
            if(j != 0) {
               ARBShaderObjects.glDetachObjectARB(i, j);
               ARBShaderObjects.glDeleteObjectARB(j);
            }

            if(k != 0) {
               ARBShaderObjects.glDetachObjectARB(i, k);
               ARBShaderObjects.glDeleteObjectARB(k);
            }

            programsID[program] = i;
            useProgram(program);
            ARBShaderObjects.glValidateProgramARB(i);
            useProgram(0);
            printLogInfo(i, vShaderPath + ", " + fShaderPath);
            int l = GL20.glGetProgrami(i, '\u8b83');
            if(l != 1) {
               String s = "\"";
               printChatAndLogError("[Shaders] Error: Invalid program " + s + programNames[program] + s);
               ARBShaderObjects.glDeleteObjectARB(i);
               i = 0;
            }
         }
      }

      return i;
   }

   private static int createVertShader(String filename) {
      int i = ARBShaderObjects.glCreateShaderObjectARB('\u8b31');
      if(i == 0) {
         return 0;
      } else {
         StringBuilder stringbuilder = new StringBuilder(131072);
         BufferedReader bufferedreader = null;

         try {
            bufferedreader = new BufferedReader(getShaderReader(filename));
         } catch (Exception var8) {
            ARBShaderObjects.glDeleteObjectARB(i);
            return 0;
         }

         ShaderOption[] ashaderoption = getChangedOptions(shaderPackOptions);
         List<String> list = new ArrayList();
         if(bufferedreader != null) {
            try {
               bufferedreader = ShaderPackParser.resolveIncludes(bufferedreader, filename, shaderPack, 0, list, 0);

               while(true) {
                  String s = bufferedreader.readLine();
                  if(s == null) {
                     bufferedreader.close();
                     break;
                  }

                  s = applyOptions(s, ashaderoption);
                  stringbuilder.append(s).append('\n');
                  ShaderLine shaderline = ShaderParser.parseLine(s);
                  if(shaderline != null) {
                     if(shaderline.isAttribute("mc_Entity")) {
                        useEntityAttrib = true;
                        progUseEntityAttrib = true;
                     } else if(shaderline.isAttribute("mc_midTexCoord")) {
                        useMidTexCoordAttrib = true;
                        progUseMidTexCoordAttrib = true;
                     } else if(s.contains("gl_MultiTexCoord3")) {
                        useMultiTexCoord3Attrib = true;
                     } else if(shaderline.isAttribute("at_tangent")) {
                        useTangentAttrib = true;
                        progUseTangentAttrib = true;
                     }
                  }
               }
            } catch (Exception exception) {
               SMCLog.severe("Couldn\'t read " + filename + "!");
               exception.printStackTrace();
               ARBShaderObjects.glDeleteObjectARB(i);
               return 0;
            }
         }

         if(saveFinalShaders) {
            saveShader(filename, stringbuilder.toString());
         }

         ARBShaderObjects.glShaderSourceARB(i, (CharSequence)stringbuilder);
         ARBShaderObjects.glCompileShaderARB(i);
         if(GL20.glGetShaderi(i, '\u8b81') != 1) {
            SMCLog.severe("Error compiling vertex shader: " + filename);
         }

         printShaderLogInfo(i, filename, list);
         return i;
      }
   }

   private static int createFragShader(String filename) {
      int i = ARBShaderObjects.glCreateShaderObjectARB('\u8b30');
      if(i == 0) {
         return 0;
      } else {
         StringBuilder stringbuilder = new StringBuilder(131072);
         BufferedReader bufferedreader = null;

         try {
            bufferedreader = new BufferedReader(getShaderReader(filename));
         } catch (Exception var12) {
            ARBShaderObjects.glDeleteObjectARB(i);
            return 0;
         }

         ShaderOption[] ashaderoption = getChangedOptions(shaderPackOptions);
         List<String> list = new ArrayList();
         if(bufferedreader != null) {
            try {
               bufferedreader = ShaderPackParser.resolveIncludes(bufferedreader, filename, shaderPack, 0, list, 0);

               while(true) {
                  String s = bufferedreader.readLine();
                  if(s == null) {
                     bufferedreader.close();
                     break;
                  }

                  s = applyOptions(s, ashaderoption);
                  stringbuilder.append(s).append('\n');
                  ShaderLine shaderline = ShaderParser.parseLine(s);
                  if(shaderline != null) {
                     if(shaderline.isUniform()) {
                        String s5 = shaderline.getName();
                        int k1;
                        if((k1 = ShaderParser.getShadowDepthIndex(s5)) >= 0) {
                           usedShadowDepthBuffers = Math.max(usedShadowDepthBuffers, k1 + 1);
                        } else if((k1 = ShaderParser.getShadowColorIndex(s5)) >= 0) {
                           usedShadowColorBuffers = Math.max(usedShadowColorBuffers, k1 + 1);
                        } else if((k1 = ShaderParser.getDepthIndex(s5)) >= 0) {
                           usedDepthBuffers = Math.max(usedDepthBuffers, k1 + 1);
                        } else if(s5.equals("gdepth") && gbuffersFormat[1] == 6408) {
                           gbuffersFormat[1] = '\u8814';
                        } else if((k1 = ShaderParser.getColorIndex(s5)) >= 0) {
                           usedColorBuffers = Math.max(usedColorBuffers, k1 + 1);
                        } else if(s5.equals("centerDepthSmooth")) {
                           centerDepthSmoothEnabled = true;
                        }
                     } else if(!shaderline.isConstInt("shadowMapResolution") && !shaderline.isProperty("SHADOWRES")) {
                        if(!shaderline.isConstFloat("shadowMapFov") && !shaderline.isProperty("SHADOWFOV")) {
                           if(!shaderline.isConstFloat("shadowDistance") && !shaderline.isProperty("SHADOWHPL")) {
                              if(shaderline.isConstFloat("shadowDistanceRenderMul")) {
                                 shadowDistanceRenderMul = shaderline.getValueFloat();
                                 SMCLog.info("Shadow distance render mul: " + shadowDistanceRenderMul);
                              } else if(shaderline.isConstFloat("shadowIntervalSize")) {
                                 shadowIntervalSize = shaderline.getValueFloat();
                                 SMCLog.info("Shadow map interval size: " + shadowIntervalSize);
                              } else if(shaderline.isConstBool("generateShadowMipmap", true)) {
                                 Arrays.fill(shadowMipmapEnabled, true);
                                 SMCLog.info("Generate shadow mipmap");
                              } else if(shaderline.isConstBool("generateShadowColorMipmap", true)) {
                                 Arrays.fill(shadowColorMipmapEnabled, true);
                                 SMCLog.info("Generate shadow color mipmap");
                              } else if(shaderline.isConstBool("shadowHardwareFiltering", true)) {
                                 Arrays.fill(shadowHardwareFilteringEnabled, true);
                                 SMCLog.info("Hardware shadow filtering enabled.");
                              } else if(shaderline.isConstBool("shadowHardwareFiltering0", true)) {
                                 shadowHardwareFilteringEnabled[0] = true;
                                 SMCLog.info("shadowHardwareFiltering0");
                              } else if(shaderline.isConstBool("shadowHardwareFiltering1", true)) {
                                 shadowHardwareFilteringEnabled[1] = true;
                                 SMCLog.info("shadowHardwareFiltering1");
                              } else if(shaderline.isConstBool("shadowtex0Mipmap", "shadowtexMipmap", true)) {
                                 shadowMipmapEnabled[0] = true;
                                 SMCLog.info("shadowtex0Mipmap");
                              } else if(shaderline.isConstBool("shadowtex1Mipmap", true)) {
                                 shadowMipmapEnabled[1] = true;
                                 SMCLog.info("shadowtex1Mipmap");
                              } else if(shaderline.isConstBool("shadowcolor0Mipmap", "shadowColor0Mipmap", true)) {
                                 shadowColorMipmapEnabled[0] = true;
                                 SMCLog.info("shadowcolor0Mipmap");
                              } else if(shaderline.isConstBool("shadowcolor1Mipmap", "shadowColor1Mipmap", true)) {
                                 shadowColorMipmapEnabled[1] = true;
                                 SMCLog.info("shadowcolor1Mipmap");
                              } else if(shaderline.isConstBool("shadowtex0Nearest", "shadowtexNearest", "shadow0MinMagNearest", true)) {
                                 shadowFilterNearest[0] = true;
                                 SMCLog.info("shadowtex0Nearest");
                              } else if(shaderline.isConstBool("shadowtex1Nearest", "shadow1MinMagNearest", true)) {
                                 shadowFilterNearest[1] = true;
                                 SMCLog.info("shadowtex1Nearest");
                              } else if(shaderline.isConstBool("shadowcolor0Nearest", "shadowColor0Nearest", "shadowColor0MinMagNearest", true)) {
                                 shadowColorFilterNearest[0] = true;
                                 SMCLog.info("shadowcolor0Nearest");
                              } else if(shaderline.isConstBool("shadowcolor1Nearest", "shadowColor1Nearest", "shadowColor1MinMagNearest", true)) {
                                 shadowColorFilterNearest[1] = true;
                                 SMCLog.info("shadowcolor1Nearest");
                              } else if(!shaderline.isConstFloat("wetnessHalflife") && !shaderline.isProperty("WETNESSHL")) {
                                 if(!shaderline.isConstFloat("drynessHalflife") && !shaderline.isProperty("DRYNESSHL")) {
                                    if(shaderline.isConstFloat("eyeBrightnessHalflife")) {
                                       eyeBrightnessHalflife = shaderline.getValueFloat();
                                       SMCLog.info("Eye brightness halflife: " + eyeBrightnessHalflife);
                                    } else if(shaderline.isConstFloat("centerDepthHalflife")) {
                                       centerDepthSmoothHalflife = shaderline.getValueFloat();
                                       SMCLog.info("Center depth halflife: " + centerDepthSmoothHalflife);
                                    } else if(shaderline.isConstFloat("sunPathRotation")) {
                                       sunPathRotation = shaderline.getValueFloat();
                                       SMCLog.info("Sun path rotation: " + sunPathRotation);
                                    } else if(shaderline.isConstFloat("ambientOcclusionLevel")) {
                                       aoLevel = Config.limit(shaderline.getValueFloat(), 0.0F, 1.0F);
                                       SMCLog.info("AO Level: " + aoLevel);
                                    } else if(shaderline.isConstInt("superSamplingLevel")) {
                                       int i1 = shaderline.getValueInt();
                                       if(i1 > 1) {
                                          SMCLog.info("Super sampling level: " + i1 + "x");
                                          superSamplingLevel = i1;
                                       } else {
                                          superSamplingLevel = 1;
                                       }
                                    } else if(shaderline.isConstInt("noiseTextureResolution")) {
                                       noiseTextureResolution = shaderline.getValueInt();
                                       noiseTextureEnabled = true;
                                       SMCLog.info("Noise texture enabled");
                                       SMCLog.info("Noise texture resolution: " + noiseTextureResolution);
                                    } else if(shaderline.isConstIntSuffix("Format")) {
                                       String s4 = StrUtils.removeSuffix(shaderline.getName(), "Format");
                                       String s6 = shaderline.getValue();
                                       int k = getBufferIndexFromString(s4);
                                       int l = getTextureFormatFromString(s6);
                                       if(k >= 0 && l != 0) {
                                          gbuffersFormat[k] = l;
                                          SMCLog.info("%s format: %s", new Object[]{s4, s6});
                                       }
                                    } else if(shaderline.isConstBoolSuffix("Clear", false)) {
                                       if(ShaderParser.isComposite(filename) || ShaderParser.isDeferred(filename)) {
                                          String s3 = StrUtils.removeSuffix(shaderline.getName(), "Clear");
                                          int j1 = getBufferIndexFromString(s3);
                                          if(j1 >= 0) {
                                             gbuffersClear[j1] = false;
                                             SMCLog.info("%s clear disabled", new Object[]{s3});
                                          }
                                       }
                                    } else if(shaderline.isProperty("GAUX4FORMAT", "RGBA32F")) {
                                       gbuffersFormat[7] = '\u8814';
                                       SMCLog.info("gaux4 format : RGB32AF");
                                    } else if(shaderline.isProperty("GAUX4FORMAT", "RGB32F")) {
                                       gbuffersFormat[7] = '\u8815';
                                       SMCLog.info("gaux4 format : RGB32F");
                                    } else if(shaderline.isProperty("GAUX4FORMAT", "RGB16")) {
                                       gbuffersFormat[7] = '\u8054';
                                       SMCLog.info("gaux4 format : RGB16");
                                    } else if(shaderline.isConstBoolSuffix("MipmapEnabled", true)) {
                                       if(ShaderParser.isComposite(filename) || ShaderParser.isDeferred(filename) || ShaderParser.isFinal(filename)) {
                                          String s2 = StrUtils.removeSuffix(shaderline.getName(), "MipmapEnabled");
                                          int j = getBufferIndexFromString(s2);
                                          if(j >= 0) {
                                             newCompositeMipmapSetting |= 1 << j;
                                             SMCLog.info("%s mipmap enabled", new Object[]{s2});
                                          }
                                       }
                                    } else if(shaderline.isProperty("DRAWBUFFERS")) {
                                       String s1 = shaderline.getValue();
                                       if(ShaderParser.isValidDrawBuffers(s1)) {
                                          newDrawBufSetting = s1;
                                       } else {
                                          SMCLog.warning("Invalid draw buffers: " + s1);
                                       }
                                    }
                                 } else {
                                    drynessHalfLife = shaderline.getValueFloat();
                                    SMCLog.info("Dryness halflife: " + drynessHalfLife);
                                 }
                              } else {
                                 wetnessHalfLife = shaderline.getValueFloat();
                                 SMCLog.info("Wetness halflife: " + wetnessHalfLife);
                              }
                           } else {
                              shadowMapHalfPlane = shaderline.getValueFloat();
                              shadowMapIsOrtho = true;
                              SMCLog.info("Shadow map distance: " + shadowMapHalfPlane);
                           }
                        } else {
                           shadowMapFOV = shaderline.getValueFloat();
                           shadowMapIsOrtho = false;
                           SMCLog.info("Shadow map field of view: " + shadowMapFOV);
                        }
                     } else {
                        spShadowMapWidth = spShadowMapHeight = shaderline.getValueInt();
                        shadowMapWidth = shadowMapHeight = Math.round((float)spShadowMapWidth * configShadowResMul);
                        SMCLog.info("Shadow map resolution: " + spShadowMapWidth);
                     }
                  }
               }
            } catch (Exception exception) {
               SMCLog.severe("Couldn\'t read " + filename + "!");
               exception.printStackTrace();
               ARBShaderObjects.glDeleteObjectARB(i);
               return 0;
            }
         }

         if(saveFinalShaders) {
            saveShader(filename, stringbuilder.toString());
         }

         ARBShaderObjects.glShaderSourceARB(i, (CharSequence)stringbuilder);
         ARBShaderObjects.glCompileShaderARB(i);
         if(GL20.glGetShaderi(i, '\u8b81') != 1) {
            SMCLog.severe("Error compiling fragment shader: " + filename);
         }

         printShaderLogInfo(i, filename, list);
         return i;
      }
   }

   private static Reader getShaderReader(String filename) {
      Reader reader = ShadersBuiltIn.getShaderReader(filename);
      return (Reader)(reader != null?reader:new InputStreamReader(shaderPack.getResourceAsStream(filename)));
   }

   private static void saveShader(String filename, String code) {
      try {
         File file1 = new File(shaderpacksdir, "debug/" + filename);
         file1.getParentFile().mkdirs();
         Config.writeFile(file1, code);
      } catch (IOException ioexception) {
         Config.warn("Error saving: " + filename);
         ioexception.printStackTrace();
      }

   }

   private static void clearDirectory(File dir) {
      if(dir.exists()) {
         if(dir.isDirectory()) {
            File[] afile = dir.listFiles();
            if(afile != null) {
               for(int i = 0; i < afile.length; ++i) {
                  File file1 = afile[i];
                  if(file1.isDirectory()) {
                     clearDirectory(file1);
                  }

                  file1.delete();
               }

            }
         }
      }
   }

   private static boolean printLogInfo(int obj, String name) {
      IntBuffer intbuffer = BufferUtils.createIntBuffer(1);
      ARBShaderObjects.glGetObjectParameterARB(obj, '\u8b84', (IntBuffer)intbuffer);
      int i = intbuffer.get();
      if(i > 1) {
         ByteBuffer bytebuffer = BufferUtils.createByteBuffer(i);
         intbuffer.flip();
         ARBShaderObjects.glGetInfoLogARB(obj, intbuffer, bytebuffer);
         byte[] abyte = new byte[i];
         bytebuffer.get(abyte);
         if(abyte[i - 1] == 0) {
            abyte[i - 1] = 10;
         }

         String s = new String(abyte);
         SMCLog.info("Info log: " + name + "\n" + s);
         return false;
      } else {
         return true;
      }
   }

   private static boolean printShaderLogInfo(int shader, String name, List<String> listFiles) {
      IntBuffer intbuffer = BufferUtils.createIntBuffer(1);
      int i = GL20.glGetShaderi(shader, '\u8b84');
      if(i <= 1) {
         return true;
      } else {
         for(int j = 0; j < listFiles.size(); ++j) {
            String s = (String)listFiles.get(j);
            SMCLog.info("File: " + (j + 1) + " = " + s);
         }

         String s1 = GL20.glGetShaderInfoLog(shader, i);
         SMCLog.info("Shader info log: " + name + "\n" + s1);
         return false;
      }
   }

   public static void setDrawBuffers(IntBuffer drawBuffers) {
      if(drawBuffers == null) {
         drawBuffers = drawBuffersNone;
      }

      if(activeDrawBuffers != drawBuffers) {
         activeDrawBuffers = drawBuffers;
         GL20.glDrawBuffers(drawBuffers);
      }

   }

   public static void useProgram(int program) {
      checkGLError("pre-useProgram");
      if(isShadowPass) {
         program = 30;
         if(programsID[30] == 0) {
            normalMapEnabled = false;
            return;
         }
      }

      if(activeProgram != program) {
         activeProgram = program;
         ARBShaderObjects.glUseProgramObjectARB(programsID[program]);
         if(programsID[program] == 0) {
            normalMapEnabled = false;
         } else {
            if(checkGLError("useProgram ", programNames[program]) != 0) {
               programsID[program] = 0;
            }

            IntBuffer intbuffer = programsDrawBuffers[program];
            if(isRenderingDfb) {
               setDrawBuffers(intbuffer);
               checkGLError(programNames[program], " draw buffers = ", programsDrawBufSettings[program]);
            }

            activeCompositeMipmapSetting = programsCompositeMipmapSetting[program];
            uniformEntityColor.setProgram(programsID[activeProgram]);
            uniformEntityId.setProgram(programsID[activeProgram]);
            uniformBlockEntityId.setProgram(programsID[activeProgram]);
            switch(program) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 16:
            case 18:
            case 19:
            case 20:
            case 41:
               normalMapEnabled = true;
               setProgramUniform1i("texture", 0);
               setProgramUniform1i("lightmap", 1);
               setProgramUniform1i("normals", 2);
               setProgramUniform1i("specular", 3);
               setProgramUniform1i("shadow", waterShadowEnabled?5:4);
               setProgramUniform1i("watershadow", 4);
               setProgramUniform1i("shadowtex0", 4);
               setProgramUniform1i("shadowtex1", 5);
               setProgramUniform1i("depthtex0", 6);
               if(customTexturesGbuffers != null || hasDeferredPrograms) {
                  setProgramUniform1i("gaux1", 7);
                  setProgramUniform1i("gaux2", 8);
                  setProgramUniform1i("gaux3", 9);
                  setProgramUniform1i("gaux4", 10);
               }

               setProgramUniform1i("depthtex1", 12);
               setProgramUniform1i("shadowcolor", 13);
               setProgramUniform1i("shadowcolor0", 13);
               setProgramUniform1i("shadowcolor1", 14);
               setProgramUniform1i("noisetex", 15);
               break;
            case 14:
            case 15:
            case 17:
            default:
               normalMapEnabled = false;
               break;
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 42:
            case 43:
               normalMapEnabled = false;
               setProgramUniform1i("gcolor", 0);
               setProgramUniform1i("gdepth", 1);
               setProgramUniform1i("gnormal", 2);
               setProgramUniform1i("composite", 3);
               setProgramUniform1i("gaux1", 7);
               setProgramUniform1i("gaux2", 8);
               setProgramUniform1i("gaux3", 9);
               setProgramUniform1i("gaux4", 10);
               setProgramUniform1i("colortex0", 0);
               setProgramUniform1i("colortex1", 1);
               setProgramUniform1i("colortex2", 2);
               setProgramUniform1i("colortex3", 3);
               setProgramUniform1i("colortex4", 7);
               setProgramUniform1i("colortex5", 8);
               setProgramUniform1i("colortex6", 9);
               setProgramUniform1i("colortex7", 10);
               setProgramUniform1i("shadow", waterShadowEnabled?5:4);
               setProgramUniform1i("watershadow", 4);
               setProgramUniform1i("shadowtex0", 4);
               setProgramUniform1i("shadowtex1", 5);
               setProgramUniform1i("gdepthtex", 6);
               setProgramUniform1i("depthtex0", 6);
               setProgramUniform1i("depthtex1", 11);
               setProgramUniform1i("depthtex2", 12);
               setProgramUniform1i("shadowcolor", 13);
               setProgramUniform1i("shadowcolor0", 13);
               setProgramUniform1i("shadowcolor1", 14);
               setProgramUniform1i("noisetex", 15);
               break;
            case 30:
            case 31:
            case 32:
               setProgramUniform1i("tex", 0);
               setProgramUniform1i("texture", 0);
               setProgramUniform1i("lightmap", 1);
               setProgramUniform1i("normals", 2);
               setProgramUniform1i("specular", 3);
               setProgramUniform1i("shadow", waterShadowEnabled?5:4);
               setProgramUniform1i("watershadow", 4);
               setProgramUniform1i("shadowtex0", 4);
               setProgramUniform1i("shadowtex1", 5);
               if(customTexturesGbuffers != null) {
                  setProgramUniform1i("gaux1", 7);
                  setProgramUniform1i("gaux2", 8);
                  setProgramUniform1i("gaux3", 9);
                  setProgramUniform1i("gaux4", 10);
               }

               setProgramUniform1i("shadowcolor", 13);
               setProgramUniform1i("shadowcolor0", 13);
               setProgramUniform1i("shadowcolor1", 14);
               setProgramUniform1i("noisetex", 15);
            }

            ItemStack itemstack = mc.field_71439_g != null?mc.field_71439_g.func_70694_bm():null;
            Item item = itemstack != null?itemstack.func_77973_b():null;
            int i = -1;
            Block block = null;
            if(item != null) {
               i = Item.field_150901_e.func_148757_b(item);
               block = (Block)Block.field_149771_c.func_148754_a(i);
            }

            int j = block != null?block.func_149750_m():0;
            setProgramUniform1i("heldItemId", i);
            setProgramUniform1i("heldBlockLightValue", j);
            setProgramUniform1i("fogMode", fogEnabled?fogMode:0);
            setProgramUniform3f("fogColor", fogColorR, fogColorG, fogColorB);
            setProgramUniform3f("skyColor", skyColorR, skyColorG, skyColorB);
            setProgramUniform1i("worldTime", (int)(worldTime % 24000L));
            setProgramUniform1i("worldDay", (int)(worldTime / 24000L));
            setProgramUniform1i("moonPhase", moonPhase);
            setProgramUniform1i("frameCounter", frameCounter);
            setProgramUniform1f("frameTime", frameTime);
            setProgramUniform1f("frameTimeCounter", frameTimeCounter);
            setProgramUniform1f("sunAngle", sunAngle);
            setProgramUniform1f("shadowAngle", shadowAngle);
            setProgramUniform1f("rainStrength", rainStrength);
            setProgramUniform1f("aspectRatio", (float)renderWidth / (float)renderHeight);
            setProgramUniform1f("viewWidth", (float)renderWidth);
            setProgramUniform1f("viewHeight", (float)renderHeight);
            setProgramUniform1f("near", 0.05F);
            setProgramUniform1f("far", (float)(mc.field_71474_y.field_151451_c * 16));
            setProgramUniform3f("sunPosition", sunPosition[0], sunPosition[1], sunPosition[2]);
            setProgramUniform3f("moonPosition", moonPosition[0], moonPosition[1], moonPosition[2]);
            setProgramUniform3f("shadowLightPosition", shadowLightPosition[0], shadowLightPosition[1], shadowLightPosition[2]);
            setProgramUniform3f("upPosition", upPosition[0], upPosition[1], upPosition[2]);
            setProgramUniform3f("previousCameraPosition", (float)previousCameraPositionX, (float)previousCameraPositionY, (float)previousCameraPositionZ);
            setProgramUniform3f("cameraPosition", (float)cameraPositionX, (float)cameraPositionY, (float)cameraPositionZ);
            setProgramUniformMatrix4ARB("gbufferModelView", false, modelView);
            setProgramUniformMatrix4ARB("gbufferModelViewInverse", false, modelViewInverse);
            setProgramUniformMatrix4ARB("gbufferPreviousProjection", false, previousProjection);
            setProgramUniformMatrix4ARB("gbufferProjection", false, projection);
            setProgramUniformMatrix4ARB("gbufferProjectionInverse", false, projectionInverse);
            setProgramUniformMatrix4ARB("gbufferPreviousModelView", false, previousModelView);
            if(usedShadowDepthBuffers > 0) {
               setProgramUniformMatrix4ARB("shadowProjection", false, shadowProjection);
               setProgramUniformMatrix4ARB("shadowProjectionInverse", false, shadowProjectionInverse);
               setProgramUniformMatrix4ARB("shadowModelView", false, shadowModelView);
               setProgramUniformMatrix4ARB("shadowModelViewInverse", false, shadowModelViewInverse);
            }

            setProgramUniform1f("wetness", wetness);
            setProgramUniform1f("eyeAltitude", eyePosY);
            setProgramUniform2i("eyeBrightness", eyeBrightness & '\uffff', eyeBrightness >> 16);
            setProgramUniform2i("eyeBrightnessSmooth", Math.round(eyeBrightnessFadeX), Math.round(eyeBrightnessFadeY));
            setProgramUniform2i("terrainTextureSize", terrainTextureSize[0], terrainTextureSize[1]);
            setProgramUniform1i("terrainIconSize", terrainIconSize);
            setProgramUniform1i("isEyeInWater", isEyeInWater);
            setProgramUniform1f("nightVision", nightVision);
            setProgramUniform1f("blindness", blindness);
            setProgramUniform1f("screenBrightness", mc.field_71474_y.field_74333_Y);
            setProgramUniform1i("hideGUI", mc.field_71474_y.field_74319_N?1:0);
            setProgramUniform1f("centerDepthSmooth", centerDepthSmooth);
            setProgramUniform2i("atlasSize", atlasSizeX, atlasSizeY);
            if(customUniforms != null) {
               customUniforms.setProgram(programsID[activeProgram]);
               customUniforms.update();
            }

            checkGLError("useProgram ", programNames[program]);
         }
      }
   }

   public static void setProgramUniform1i(String name, int x) {
      int i = programsID[activeProgram];
      if(i != 0) {
         int j = ARBShaderObjects.glGetUniformLocationARB(i, (CharSequence)name);
         ARBShaderObjects.glUniform1iARB(j, x);
         if(isCustomUniforms()) {
            LegacyUniforms.setInt(name, x);
         }

         checkGLError(programNames[activeProgram], name);
      }

   }

   public static void setProgramUniform2i(String name, int x, int y) {
      int i = programsID[activeProgram];
      if(i != 0) {
         int j = ARBShaderObjects.glGetUniformLocationARB(i, (CharSequence)name);
         ARBShaderObjects.glUniform2iARB(j, x, y);
         if(isCustomUniforms()) {
            LegacyUniforms.setIntXy(name, x, y);
         }

         checkGLError(programNames[activeProgram], name);
      }

   }

   public static void setProgramUniform1f(String name, float x) {
      int i = programsID[activeProgram];
      if(i != 0) {
         int j = ARBShaderObjects.glGetUniformLocationARB(i, (CharSequence)name);
         ARBShaderObjects.glUniform1fARB(j, x);
         if(isCustomUniforms()) {
            LegacyUniforms.setFloat(name, x);
         }

         checkGLError(programNames[activeProgram], name);
      }

   }

   public static void setProgramUniform3f(String name, float x, float y, float z) {
      int i = programsID[activeProgram];
      if(i != 0) {
         int j = ARBShaderObjects.glGetUniformLocationARB(i, (CharSequence)name);
         ARBShaderObjects.glUniform3fARB(j, x, y, z);
         if(isCustomUniforms()) {
            if(name.endsWith("Color")) {
               LegacyUniforms.setFloatRgb(name, x, y, z);
            } else {
               LegacyUniforms.setFloatXyz(name, x, y, z);
            }
         }

         checkGLError(programNames[activeProgram], name);
      }

   }

   public static void setProgramUniformMatrix4ARB(String name, boolean transpose, FloatBuffer matrix) {
      int i = programsID[activeProgram];
      if(i != 0 && matrix != null) {
         int j = ARBShaderObjects.glGetUniformLocationARB(i, (CharSequence)name);
         ARBShaderObjects.glUniformMatrix4ARB(j, transpose, matrix);
         checkGLError(programNames[activeProgram], name);
      }

   }

   private static int getBufferIndexFromString(String name) {
      return !name.equals("colortex0") && !name.equals("gcolor")?(!name.equals("colortex1") && !name.equals("gdepth")?(!name.equals("colortex2") && !name.equals("gnormal")?(!name.equals("colortex3") && !name.equals("composite")?(!name.equals("colortex4") && !name.equals("gaux1")?(!name.equals("colortex5") && !name.equals("gaux2")?(!name.equals("colortex6") && !name.equals("gaux3")?(!name.equals("colortex7") && !name.equals("gaux4")?-1:7):6):5):4):3):2):1):0;
   }

   private static int getTextureFormatFromString(String par) {
      par = par.trim();

      for(int i = 0; i < formatNames.length; ++i) {
         String s = formatNames[i];
         if(par.equals(s)) {
            return formatIds[i];
         }
      }

      return 0;
   }

   private static void setupNoiseTexture() {
      if(noiseTexture == null && noiseTexturePath != null) {
         noiseTexture = loadCustomTexture(15, noiseTexturePath);
      }

      if(noiseTexture == null) {
         noiseTexture = new HFNoiseTexture(noiseTextureResolution, noiseTextureResolution);
      }

   }

   private static void loadEntityDataMap() {
      mapBlockToEntityData = new IdentityHashMap(300);
      if(mapBlockToEntityData.isEmpty()) {
         for(ResourceLocation resourcelocation : Block.field_149771_c.func_148742_b()) {
            Block block = (Block)Block.field_149771_c.func_82594_a(resourcelocation);
            int i = Block.field_149771_c.func_148757_b(block);
            mapBlockToEntityData.put(block, Integer.valueOf(i));
         }
      }

      BufferedReader bufferedreader = null;

      try {
         bufferedreader = new BufferedReader(new InputStreamReader(shaderPack.getResourceAsStream("/mc_Entity_x.txt")));
      } catch (Exception var8) {
         ;
      }

      if(bufferedreader != null) {
         String s1;
         try {
            while((s1 = bufferedreader.readLine()) != null) {
               Matcher matcher = patternLoadEntityDataMap.matcher(s1);
               if(matcher.matches()) {
                  String s2 = matcher.group(1);
                  String s = matcher.group(2);
                  int j = Integer.parseInt(s);
                  Block block1 = Block.func_149684_b(s2);
                  if(block1 != null) {
                     mapBlockToEntityData.put(block1, Integer.valueOf(j));
                  } else {
                     SMCLog.warning("Unknown block name %s", new Object[]{s2});
                  }
               } else {
                  SMCLog.warning("unmatched %s\n", new Object[]{s1});
               }
            }
         } catch (Exception var9) {
            SMCLog.warning("Error parsing mc_Entity_x.txt");
         }
      }

      if(bufferedreader != null) {
         try {
            bufferedreader.close();
         } catch (Exception var7) {
            ;
         }
      }

   }

   private static IntBuffer fillIntBufferZero(IntBuffer buf) {
      int i = buf.limit();

      for(int j = buf.position(); j < i; ++j) {
         buf.put(j, 0);
      }

      return buf;
   }

   public static void uninit() {
      if(isShaderPackInitialized) {
         checkGLError("Shaders.uninit pre");

         for(int i = 0; i < 44; ++i) {
            if(programsRef[i] != 0) {
               ARBShaderObjects.glDeleteObjectARB(programsRef[i]);
               checkGLError("del programRef");
            }

            programsRef[i] = 0;
            programsID[i] = 0;
            programsDrawBufSettings[i] = null;
            programsDrawBuffers[i] = null;
            programsCompositeMipmapSetting[i] = 0;
         }

         hasDeferredPrograms = false;
         if(dfb != 0) {
            EXTFramebufferObject.glDeleteFramebuffersEXT(dfb);
            dfb = 0;
            checkGLError("del dfb");
         }

         if(sfb != 0) {
            EXTFramebufferObject.glDeleteFramebuffersEXT(sfb);
            sfb = 0;
            checkGLError("del sfb");
         }

         if(dfbDepthTextures != null) {
            GlStateManager.deleteTextures(dfbDepthTextures);
            fillIntBufferZero(dfbDepthTextures);
            checkGLError("del dfbDepthTextures");
         }

         if(dfbColorTextures != null) {
            GlStateManager.deleteTextures(dfbColorTextures);
            fillIntBufferZero(dfbColorTextures);
            checkGLError("del dfbTextures");
         }

         if(sfbDepthTextures != null) {
            GlStateManager.deleteTextures(sfbDepthTextures);
            fillIntBufferZero(sfbDepthTextures);
            checkGLError("del shadow depth");
         }

         if(sfbColorTextures != null) {
            GlStateManager.deleteTextures(sfbColorTextures);
            fillIntBufferZero(sfbColorTextures);
            checkGLError("del shadow color");
         }

         if(dfbDrawBuffers != null) {
            fillIntBufferZero(dfbDrawBuffers);
         }

         if(noiseTexture != null) {
            noiseTexture.deleteTexture();
            noiseTexture = null;
         }

         SMCLog.info("Uninit");
         shadowPassInterval = 0;
         shouldSkipDefaultShadow = false;
         isShaderPackInitialized = false;
         checkGLError("Shaders.uninit");
      }

   }

   public static void scheduleResize() {
      renderDisplayHeight = 0;
   }

   public static void scheduleResizeShadow() {
      needResizeShadow = true;
   }

   private static void resize() {
      renderDisplayWidth = mc.field_71443_c;
      renderDisplayHeight = mc.field_71440_d;
      renderWidth = Math.round((float)renderDisplayWidth * configRenderResMul);
      renderHeight = Math.round((float)renderDisplayHeight * configRenderResMul);
      setupFrameBuffer();
   }

   private static void resizeShadow() {
      needResizeShadow = false;
      shadowMapWidth = Math.round((float)spShadowMapWidth * configShadowResMul);
      shadowMapHeight = Math.round((float)spShadowMapHeight * configShadowResMul);
      setupShadowFrameBuffer();
   }

   private static void setupFrameBuffer() {
      if(dfb != 0) {
         EXTFramebufferObject.glDeleteFramebuffersEXT(dfb);
         GlStateManager.deleteTextures(dfbDepthTextures);
         GlStateManager.deleteTextures(dfbColorTextures);
      }

      dfb = EXTFramebufferObject.glGenFramebuffersEXT();
      GL11.glGenTextures((IntBuffer)dfbDepthTextures.clear().limit(usedDepthBuffers));
      GL11.glGenTextures((IntBuffer)dfbColorTextures.clear().limit(16));
      dfbDepthTextures.position(0);
      dfbColorTextures.position(0);
      dfbColorTextures.get(dfbColorTexturesA).position(0);
      EXTFramebufferObject.glBindFramebufferEXT('\u8d40', dfb);
      GL20.glDrawBuffers(0);
      GL11.glReadBuffer(0);

      for(int i = 0; i < usedDepthBuffers; ++i) {
         GlStateManager.func_179144_i(dfbDepthTextures.get(i));
         GL11.glTexParameteri(3553, 10242, 10496);
         GL11.glTexParameteri(3553, 10243, 10496);
         GL11.glTexParameteri(3553, 10241, 9728);
         GL11.glTexParameteri(3553, 10240, 9728);
         GL11.glTexParameteri(3553, '\u884b', 6409);
         GL11.glTexImage2D(3553, 0, 6402, renderWidth, renderHeight, 0, 6402, 5126, (FloatBuffer)((FloatBuffer)null));
      }

      EXTFramebufferObject.glFramebufferTexture2DEXT('\u8d40', '\u8d00', 3553, dfbDepthTextures.get(0), 0);
      GL20.glDrawBuffers(dfbDrawBuffers);
      GL11.glReadBuffer(0);
      checkGLError("FT d");

      for(int k = 0; k < usedColorBuffers; ++k) {
         GlStateManager.func_179144_i(dfbColorTexturesA[k]);
         GL11.glTexParameteri(3553, 10242, 10496);
         GL11.glTexParameteri(3553, 10243, 10496);
         GL11.glTexParameteri(3553, 10241, 9729);
         GL11.glTexParameteri(3553, 10240, 9729);
         GL11.glTexImage2D(3553, 0, gbuffersFormat[k], renderWidth, renderHeight, 0, '\u80e1', '\u8367', (ByteBuffer)((ByteBuffer)null));
         EXTFramebufferObject.glFramebufferTexture2DEXT('\u8d40', '\u8ce0' + k, 3553, dfbColorTexturesA[k], 0);
         checkGLError("FT c");
      }

      for(int l = 0; l < usedColorBuffers; ++l) {
         GlStateManager.func_179144_i(dfbColorTexturesA[8 + l]);
         GL11.glTexParameteri(3553, 10242, 10496);
         GL11.glTexParameteri(3553, 10243, 10496);
         GL11.glTexParameteri(3553, 10241, 9729);
         GL11.glTexParameteri(3553, 10240, 9729);
         GL11.glTexImage2D(3553, 0, gbuffersFormat[l], renderWidth, renderHeight, 0, '\u80e1', '\u8367', (ByteBuffer)((ByteBuffer)null));
         checkGLError("FT ca");
      }

      int i1 = EXTFramebufferObject.glCheckFramebufferStatusEXT('\u8d40');
      if(i1 == '\u8cda') {
         printChatAndLogError("[Shaders] Error: Failed framebuffer incomplete formats");

         for(int j = 0; j < usedColorBuffers; ++j) {
            GlStateManager.func_179144_i(dfbColorTextures.get(j));
            GL11.glTexImage2D(3553, 0, 6408, renderWidth, renderHeight, 0, '\u80e1', '\u8367', (ByteBuffer)((ByteBuffer)null));
            EXTFramebufferObject.glFramebufferTexture2DEXT('\u8d40', '\u8ce0' + j, 3553, dfbColorTextures.get(j), 0);
            checkGLError("FT c");
         }

         i1 = EXTFramebufferObject.glCheckFramebufferStatusEXT('\u8d40');
         if(i1 == '\u8cd5') {
            SMCLog.info("complete");
         }
      }

      GlStateManager.func_179144_i(0);
      if(i1 != '\u8cd5') {
         printChatAndLogError("[Shaders] Error: Failed creating framebuffer! (Status " + i1 + ")");
      } else {
         SMCLog.info("Framebuffer created.");
      }

   }

   private static void setupShadowFrameBuffer() {
      if(usedShadowDepthBuffers != 0) {
         if(sfb != 0) {
            EXTFramebufferObject.glDeleteFramebuffersEXT(sfb);
            GlStateManager.deleteTextures(sfbDepthTextures);
            GlStateManager.deleteTextures(sfbColorTextures);
         }

         sfb = EXTFramebufferObject.glGenFramebuffersEXT();
         EXTFramebufferObject.glBindFramebufferEXT('\u8d40', sfb);
         GL11.glDrawBuffer(0);
         GL11.glReadBuffer(0);
         GL11.glGenTextures((IntBuffer)sfbDepthTextures.clear().limit(usedShadowDepthBuffers));
         GL11.glGenTextures((IntBuffer)sfbColorTextures.clear().limit(usedShadowColorBuffers));
         sfbDepthTextures.position(0);
         sfbColorTextures.position(0);

         for(int i = 0; i < usedShadowDepthBuffers; ++i) {
            GlStateManager.func_179144_i(sfbDepthTextures.get(i));
            GL11.glTexParameterf(3553, 10242, 10496.0F);
            GL11.glTexParameterf(3553, 10243, 10496.0F);
            int j = shadowFilterNearest[i]?9728:9729;
            GL11.glTexParameteri(3553, 10241, j);
            GL11.glTexParameteri(3553, 10240, j);
            if(shadowHardwareFilteringEnabled[i]) {
               GL11.glTexParameteri(3553, '\u884c', '\u884e');
            }

            GL11.glTexImage2D(3553, 0, 6402, shadowMapWidth, shadowMapHeight, 0, 6402, 5126, (FloatBuffer)((FloatBuffer)null));
         }

         EXTFramebufferObject.glFramebufferTexture2DEXT('\u8d40', '\u8d00', 3553, sfbDepthTextures.get(0), 0);
         checkGLError("FT sd");

         for(int k = 0; k < usedShadowColorBuffers; ++k) {
            GlStateManager.func_179144_i(sfbColorTextures.get(k));
            GL11.glTexParameterf(3553, 10242, 10496.0F);
            GL11.glTexParameterf(3553, 10243, 10496.0F);
            int i1 = shadowColorFilterNearest[k]?9728:9729;
            GL11.glTexParameteri(3553, 10241, i1);
            GL11.glTexParameteri(3553, 10240, i1);
            GL11.glTexImage2D(3553, 0, 6408, shadowMapWidth, shadowMapHeight, 0, '\u80e1', '\u8367', (ByteBuffer)((ByteBuffer)null));
            EXTFramebufferObject.glFramebufferTexture2DEXT('\u8d40', '\u8ce0' + k, 3553, sfbColorTextures.get(k), 0);
            checkGLError("FT sc");
         }

         GlStateManager.func_179144_i(0);
         if(usedShadowColorBuffers > 0) {
            GL20.glDrawBuffers(sfbDrawBuffers);
         }

         int l = EXTFramebufferObject.glCheckFramebufferStatusEXT('\u8d40');
         if(l != '\u8cd5') {
            printChatAndLogError("[Shaders] Error: Failed creating shadow framebuffer! (Status " + l + ")");
         } else {
            SMCLog.info("Shadow framebuffer created.");
         }

      }
   }

   public static void beginRender(Minecraft minecraft, float partialTicks, long finishTimeNano) {
      checkGLError("pre beginRender");
      checkWorldChanged(mc.field_71441_e);
      mc = minecraft;
      mc.field_71424_I.func_76320_a("init");
      entityRenderer = mc.field_71460_t;
      if(!isShaderPackInitialized) {
         try {
            init();
         } catch (IllegalStateException illegalstateexception) {
            if(Config.normalize(illegalstateexception.getMessage()).equals("Function is not supported")) {
               printChatAndLogError("[Shaders] Error: " + illegalstateexception.getMessage());
               illegalstateexception.printStackTrace();
               setShaderPack(packNameNone);
               return;
            }
         }
      }

      if(mc.field_71443_c != renderDisplayWidth || mc.field_71440_d != renderDisplayHeight) {
         resize();
      }

      if(needResizeShadow) {
         resizeShadow();
      }

      worldTime = mc.field_71441_e.func_72820_D();
      diffWorldTime = (worldTime - lastWorldTime) % 24000L;
      if(diffWorldTime < 0L) {
         diffWorldTime += 24000L;
      }

      lastWorldTime = worldTime;
      moonPhase = mc.field_71441_e.func_72853_d();
      ++frameCounter;
      if(frameCounter >= 720720) {
         frameCounter = 0;
      }

      systemTime = System.currentTimeMillis();
      if(lastSystemTime == 0L) {
         lastSystemTime = systemTime;
      }

      diffSystemTime = systemTime - lastSystemTime;
      lastSystemTime = systemTime;
      frameTime = (float)diffSystemTime / 1000.0F;
      frameTimeCounter += frameTime;
      frameTimeCounter %= 3600.0F;
      rainStrength = minecraft.field_71441_e.func_72867_j(partialTicks);
      float f = (float)diffSystemTime * 0.01F;
      float f1 = (float)Math.exp(Math.log(0.5D) * (double)f / (double)(wetness < rainStrength?drynessHalfLife:wetnessHalfLife));
      wetness = wetness * f1 + rainStrength * (1.0F - f1);
      Entity entity = mc.func_175606_aa();
      if(entity != null) {
         isSleeping = entity instanceof EntityLivingBase && ((EntityLivingBase)entity).func_70608_bn();
         eyePosY = (float)entity.field_70163_u * partialTicks + (float)entity.field_70137_T * (1.0F - partialTicks);
         eyeBrightness = entity.func_70070_b(partialTicks);
         f1 = (float)diffSystemTime * 0.01F;
         float f2 = (float)Math.exp(Math.log(0.5D) * (double)f1 / (double)eyeBrightnessHalflife);
         eyeBrightnessFadeX = eyeBrightnessFadeX * f2 + (float)(eyeBrightness & '\uffff') * (1.0F - f2);
         eyeBrightnessFadeY = eyeBrightnessFadeY * f2 + (float)(eyeBrightness >> 16) * (1.0F - f2);
         isEyeInWater = 0;
         if(mc.field_71474_y.field_74320_O == 0 && !isSleeping) {
            if(entity.func_70055_a(Material.field_151586_h)) {
               isEyeInWater = 1;
            } else if(entity.func_70055_a(Material.field_151587_i)) {
               isEyeInWater = 2;
            }
         }

         if(mc.field_71439_g != null) {
            nightVision = 0.0F;
            if(mc.field_71439_g.func_70644_a(Potion.field_76439_r)) {
               nightVision = Config.getMinecraft().field_71460_t.func_180438_a(mc.field_71439_g, partialTicks);
            }

            blindness = 0.0F;
            if(mc.field_71439_g.func_70644_a(Potion.field_76440_q)) {
               int i = mc.field_71439_g.func_70660_b(Potion.field_76440_q).func_76459_b();
               blindness = Config.limit((float)i / 20.0F, 0.0F, 1.0F);
            }
         }

         Vec3 vec3 = mc.field_71441_e.func_72833_a(entity, partialTicks);
         vec3 = CustomColors.getWorldSkyColor(vec3, currentWorld, entity, partialTicks);
         skyColorR = (float)vec3.field_72450_a;
         skyColorG = (float)vec3.field_72448_b;
         skyColorB = (float)vec3.field_72449_c;
      }

      isRenderingWorld = true;
      isCompositeRendered = false;
      isHandRenderedMain = false;
      if(usedShadowDepthBuffers >= 1) {
         GlStateManager.func_179138_g('\u84c4');
         GlStateManager.func_179144_i(sfbDepthTextures.get(0));
         if(usedShadowDepthBuffers >= 2) {
            GlStateManager.func_179138_g('\u84c5');
            GlStateManager.func_179144_i(sfbDepthTextures.get(1));
         }
      }

      GlStateManager.func_179138_g('\u84c0');

      for(int j = 0; j < usedColorBuffers; ++j) {
         GlStateManager.func_179144_i(dfbColorTexturesA[j]);
         GL11.glTexParameteri(3553, 10240, 9729);
         GL11.glTexParameteri(3553, 10241, 9729);
         GlStateManager.func_179144_i(dfbColorTexturesA[8 + j]);
         GL11.glTexParameteri(3553, 10240, 9729);
         GL11.glTexParameteri(3553, 10241, 9729);
      }

      GlStateManager.func_179144_i(0);

      for(int k = 0; k < 4 && 4 + k < usedColorBuffers; ++k) {
         GlStateManager.func_179138_g('\u84c7' + k);
         GlStateManager.func_179144_i(dfbColorTextures.get(4 + k));
      }

      GlStateManager.func_179138_g('\u84c6');
      GlStateManager.func_179144_i(dfbDepthTextures.get(0));
      if(usedDepthBuffers >= 2) {
         GlStateManager.func_179138_g('\u84cb');
         GlStateManager.func_179144_i(dfbDepthTextures.get(1));
         if(usedDepthBuffers >= 3) {
            GlStateManager.func_179138_g('\u84cc');
            GlStateManager.func_179144_i(dfbDepthTextures.get(2));
         }
      }

      for(int l = 0; l < usedShadowColorBuffers; ++l) {
         GlStateManager.func_179138_g('\u84cd' + l);
         GlStateManager.func_179144_i(sfbColorTextures.get(l));
      }

      if(noiseTextureEnabled) {
         GlStateManager.func_179138_g('\u84c0' + noiseTexture.getTextureUnit());
         GlStateManager.func_179144_i(noiseTexture.getTextureId());
      }

      bindCustomTextures(customTexturesGbuffers);
      GlStateManager.func_179138_g('\u84c0');
      previousCameraPositionX = cameraPositionX;
      previousCameraPositionY = cameraPositionY;
      previousCameraPositionZ = cameraPositionZ;
      previousProjection.position(0);
      projection.position(0);
      previousProjection.put(projection);
      previousProjection.position(0);
      projection.position(0);
      previousModelView.position(0);
      modelView.position(0);
      previousModelView.put(modelView);
      previousModelView.position(0);
      modelView.position(0);
      checkGLError("beginRender");
      ShadersRender.renderShadowMap(entityRenderer, 0, partialTicks, finishTimeNano);
      mc.field_71424_I.func_76319_b();
      EXTFramebufferObject.glBindFramebufferEXT('\u8d40', dfb);

      for(int i1 = 0; i1 < usedColorBuffers; ++i1) {
         colorTexturesToggle[i1] = 0;
         EXTFramebufferObject.glFramebufferTexture2DEXT('\u8d40', '\u8ce0' + i1, 3553, dfbColorTexturesA[i1], 0);
      }

      checkGLError("end beginRender");
   }

   private static void checkWorldChanged(World world) {
      if(currentWorld != world) {
         World world = currentWorld;
         currentWorld = world;
         if(world != null && world != null) {
            int i = world.field_73011_w.func_177502_q();
            int j = world.field_73011_w.func_177502_q();
            boolean flag = shaderPackDimensions.contains(Integer.valueOf(i));
            boolean flag1 = shaderPackDimensions.contains(Integer.valueOf(j));
            if(flag || flag1) {
               uninit();
            }
         }

         Smoother.reset();
      }
   }

   public static void beginRenderPass(int pass, float partialTicks, long finishTimeNano) {
      if(!isShadowPass) {
         EXTFramebufferObject.glBindFramebufferEXT('\u8d40', dfb);
         GL11.glViewport(0, 0, renderWidth, renderHeight);
         activeDrawBuffers = null;
         ShadersTex.bindNSTextures(defaultTexture.getMultiTexID());
         useProgram(2);
         checkGLError("end beginRenderPass");
      }
   }

   public static void setViewport(int vx, int vy, int vw, int vh) {
      GlStateManager.func_179135_a(true, true, true, true);
      if(isShadowPass) {
         GL11.glViewport(0, 0, shadowMapWidth, shadowMapHeight);
      } else {
         GL11.glViewport(0, 0, renderWidth, renderHeight);
         EXTFramebufferObject.glBindFramebufferEXT('\u8d40', dfb);
         isRenderingDfb = true;
         GlStateManager.func_179089_o();
         GlStateManager.func_179126_j();
         setDrawBuffers(drawBuffersNone);
         useProgram(2);
         checkGLError("beginRenderPass");
      }

   }

   public static int setFogMode(int val) {
      fogMode = val;
      return val;
   }

   public static void setFogColor(float r, float g, float b) {
      fogColorR = r;
      fogColorG = g;
      fogColorB = b;
      setProgramUniform3f("fogColor", fogColorR, fogColorG, fogColorB);
   }

   public static void setClearColor(float red, float green, float blue, float alpha) {
      GlStateManager.func_179082_a(red, green, blue, alpha);
      clearColorR = red;
      clearColorG = green;
      clearColorB = blue;
   }

   public static void clearRenderBuffer() {
      if(isShadowPass) {
         checkGLError("shadow clear pre");
         EXTFramebufferObject.glFramebufferTexture2DEXT('\u8d40', '\u8d00', 3553, sfbDepthTextures.get(0), 0);
         GL11.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
         GL20.glDrawBuffers(programsDrawBuffers[30]);
         checkFramebufferStatus("shadow clear");
         GL11.glClear(16640);
         checkGLError("shadow clear");
      } else {
         checkGLError("clear pre");
         if(gbuffersClear[0]) {
            GL20.glDrawBuffers('\u8ce0');
            GL11.glClear(16384);
         }

         if(gbuffersClear[1]) {
            GL20.glDrawBuffers('\u8ce1');
            GL11.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glClear(16384);
         }

         for(int i = 2; i < usedColorBuffers; ++i) {
            if(gbuffersClear[i]) {
               GL20.glDrawBuffers('\u8ce0' + i);
               GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
               GL11.glClear(16384);
            }
         }

         setDrawBuffers(dfbDrawBuffers);
         checkFramebufferStatus("clear");
         checkGLError("clear");
      }
   }

   public static void setCamera(float partialTicks) {
      Entity entity = mc.func_175606_aa();
      double d0 = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)partialTicks;
      double d1 = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)partialTicks;
      double d2 = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)partialTicks;
      cameraPositionX = d0;
      cameraPositionY = d1;
      cameraPositionZ = d2;
      GL11.glGetFloat(2983, (FloatBuffer)projection.position(0));
      SMath.invertMat4FBFA((FloatBuffer)projectionInverse.position(0), (FloatBuffer)projection.position(0), faProjectionInverse, faProjection);
      projection.position(0);
      projectionInverse.position(0);
      GL11.glGetFloat(2982, (FloatBuffer)modelView.position(0));
      SMath.invertMat4FBFA((FloatBuffer)modelViewInverse.position(0), (FloatBuffer)modelView.position(0), faModelViewInverse, faModelView);
      modelView.position(0);
      modelViewInverse.position(0);
      checkGLError("setCamera");
   }

   public static void setCameraShadow(float partialTicks) {
      Entity entity = mc.func_175606_aa();
      double d0 = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * (double)partialTicks;
      double d1 = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * (double)partialTicks;
      double d2 = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * (double)partialTicks;
      cameraPositionX = d0;
      cameraPositionY = d1;
      cameraPositionZ = d2;
      GL11.glGetFloat(2983, (FloatBuffer)projection.position(0));
      SMath.invertMat4FBFA((FloatBuffer)projectionInverse.position(0), (FloatBuffer)projection.position(0), faProjectionInverse, faProjection);
      projection.position(0);
      projectionInverse.position(0);
      GL11.glGetFloat(2982, (FloatBuffer)modelView.position(0));
      SMath.invertMat4FBFA((FloatBuffer)modelViewInverse.position(0), (FloatBuffer)modelView.position(0), faModelViewInverse, faModelView);
      modelView.position(0);
      modelViewInverse.position(0);
      GL11.glViewport(0, 0, shadowMapWidth, shadowMapHeight);
      GL11.glMatrixMode(5889);
      GL11.glLoadIdentity();
      if(shadowMapIsOrtho) {
         GL11.glOrtho((double)(-shadowMapHalfPlane), (double)shadowMapHalfPlane, (double)(-shadowMapHalfPlane), (double)shadowMapHalfPlane, 0.05000000074505806D, 256.0D);
      } else {
         GLU.gluPerspective(shadowMapFOV, (float)shadowMapWidth / (float)shadowMapHeight, 0.05F, 256.0F);
      }

      GL11.glMatrixMode(5888);
      GL11.glLoadIdentity();
      GL11.glTranslatef(0.0F, 0.0F, -100.0F);
      GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
      celestialAngle = mc.field_71441_e.func_72826_c(partialTicks);
      sunAngle = celestialAngle < 0.75F?celestialAngle + 0.25F:celestialAngle - 0.75F;
      float f = celestialAngle * -360.0F;
      float f1 = shadowAngleInterval > 0.0F?f % shadowAngleInterval - shadowAngleInterval * 0.5F:0.0F;
      if((double)sunAngle <= 0.5D) {
         GL11.glRotatef(f - f1, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(sunPathRotation, 1.0F, 0.0F, 0.0F);
         shadowAngle = sunAngle;
      } else {
         GL11.glRotatef(f + 180.0F - f1, 0.0F, 0.0F, 1.0F);
         GL11.glRotatef(sunPathRotation, 1.0F, 0.0F, 0.0F);
         shadowAngle = sunAngle - 0.5F;
      }

      if(shadowMapIsOrtho) {
         float f2 = shadowIntervalSize;
         float f3 = f2 / 2.0F;
         GL11.glTranslatef((float)d0 % f2 - f3, (float)d1 % f2 - f3, (float)d2 % f2 - f3);
      }

      float f9 = sunAngle * 6.2831855F;
      float f10 = (float)Math.cos((double)f9);
      float f4 = (float)Math.sin((double)f9);
      float f5 = sunPathRotation * 6.2831855F;
      float f6 = f10;
      float f7 = f4 * (float)Math.cos((double)f5);
      float f8 = f4 * (float)Math.sin((double)f5);
      if((double)sunAngle > 0.5D) {
         f6 = -f10;
         f7 = -f7;
         f8 = -f8;
      }

      shadowLightPositionVector[0] = f6;
      shadowLightPositionVector[1] = f7;
      shadowLightPositionVector[2] = f8;
      shadowLightPositionVector[3] = 0.0F;
      GL11.glGetFloat(2983, (FloatBuffer)shadowProjection.position(0));
      SMath.invertMat4FBFA((FloatBuffer)shadowProjectionInverse.position(0), (FloatBuffer)shadowProjection.position(0), faShadowProjectionInverse, faShadowProjection);
      shadowProjection.position(0);
      shadowProjectionInverse.position(0);
      GL11.glGetFloat(2982, (FloatBuffer)shadowModelView.position(0));
      SMath.invertMat4FBFA((FloatBuffer)shadowModelViewInverse.position(0), (FloatBuffer)shadowModelView.position(0), faShadowModelViewInverse, faShadowModelView);
      shadowModelView.position(0);
      shadowModelViewInverse.position(0);
      setProgramUniformMatrix4ARB("gbufferProjection", false, projection);
      setProgramUniformMatrix4ARB("gbufferProjectionInverse", false, projectionInverse);
      setProgramUniformMatrix4ARB("gbufferPreviousProjection", false, previousProjection);
      setProgramUniformMatrix4ARB("gbufferModelView", false, modelView);
      setProgramUniformMatrix4ARB("gbufferModelViewInverse", false, modelViewInverse);
      setProgramUniformMatrix4ARB("gbufferPreviousModelView", false, previousModelView);
      setProgramUniformMatrix4ARB("shadowProjection", false, shadowProjection);
      setProgramUniformMatrix4ARB("shadowProjectionInverse", false, shadowProjectionInverse);
      setProgramUniformMatrix4ARB("shadowModelView", false, shadowModelView);
      setProgramUniformMatrix4ARB("shadowModelViewInverse", false, shadowModelViewInverse);
      mc.field_71474_y.field_74320_O = 1;
      checkGLError("setCamera");
   }

   public static void preCelestialRotate() {
      GL11.glRotatef(sunPathRotation * 1.0F, 0.0F, 0.0F, 1.0F);
      checkGLError("preCelestialRotate");
   }

   public static void postCelestialRotate() {
      FloatBuffer floatbuffer = tempMatrixDirectBuffer;
      floatbuffer.clear();
      GL11.glGetFloat(2982, floatbuffer);
      floatbuffer.get(tempMat, 0, 16);
      SMath.multiplyMat4xVec4(sunPosition, tempMat, sunPosModelView);
      SMath.multiplyMat4xVec4(moonPosition, tempMat, moonPosModelView);
      System.arraycopy(shadowAngle == sunAngle?sunPosition:moonPosition, 0, shadowLightPosition, 0, 3);
      setProgramUniform3f("sunPosition", sunPosition[0], sunPosition[1], sunPosition[2]);
      setProgramUniform3f("moonPosition", moonPosition[0], moonPosition[1], moonPosition[2]);
      setProgramUniform3f("shadowLightPosition", shadowLightPosition[0], shadowLightPosition[1], shadowLightPosition[2]);
      checkGLError("postCelestialRotate");
   }

   public static void setUpPosition() {
      FloatBuffer floatbuffer = tempMatrixDirectBuffer;
      floatbuffer.clear();
      GL11.glGetFloat(2982, floatbuffer);
      floatbuffer.get(tempMat, 0, 16);
      SMath.multiplyMat4xVec4(upPosition, tempMat, upPosModelView);
      setProgramUniform3f("upPosition", upPosition[0], upPosition[1], upPosition[2]);
   }

   public static void genCompositeMipmap() {
      if(hasGlGenMipmap) {
         for(int i = 0; i < usedColorBuffers; ++i) {
            if((activeCompositeMipmapSetting & 1 << i) != 0) {
               GlStateManager.func_179138_g('\u84c0' + colorTextureTextureImageUnit[i]);
               GL11.glTexParameteri(3553, 10241, 9987);
               GL30.glGenerateMipmap(3553);
            }
         }

         GlStateManager.func_179138_g('\u84c0');
      }

   }

   public static void drawComposite() {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glBegin(7);
      GL11.glTexCoord2f(0.0F, 0.0F);
      GL11.glVertex3f(0.0F, 0.0F, 0.0F);
      GL11.glTexCoord2f(1.0F, 0.0F);
      GL11.glVertex3f(1.0F, 0.0F, 0.0F);
      GL11.glTexCoord2f(1.0F, 1.0F);
      GL11.glVertex3f(1.0F, 1.0F, 0.0F);
      GL11.glTexCoord2f(0.0F, 1.0F);
      GL11.glVertex3f(0.0F, 1.0F, 0.0F);
      GL11.glEnd();
   }

   public static void renderDeferred() {
      if(hasDeferredPrograms) {
         checkGLError("pre-render Deferred");
         renderComposites(33, 8, false);
         mc.func_110434_K().func_110577_a(TextureMap.field_110575_b);
      }
   }

   public static void renderCompositeFinal() {
      checkGLError("pre-render CompositeFinal");
      renderComposites(21, 8, true);
   }

   private static void renderComposites(int programBase, int countPrograms, boolean renderFinal) {
      if(!isShadowPass) {
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         GL11.glMatrixMode(5889);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         GL11.glOrtho(0.0D, 1.0D, 0.0D, 1.0D, 0.0D, 1.0D);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.func_179098_w();
         GlStateManager.func_179118_c();
         GlStateManager.func_179084_k();
         GlStateManager.func_179126_j();
         GlStateManager.func_179143_c(519);
         GlStateManager.func_179132_a(false);
         GlStateManager.func_179140_f();
         if(usedShadowDepthBuffers >= 1) {
            GlStateManager.func_179138_g('\u84c4');
            GlStateManager.func_179144_i(sfbDepthTextures.get(0));
            if(usedShadowDepthBuffers >= 2) {
               GlStateManager.func_179138_g('\u84c5');
               GlStateManager.func_179144_i(sfbDepthTextures.get(1));
            }
         }

         for(int i = 0; i < usedColorBuffers; ++i) {
            GlStateManager.func_179138_g('\u84c0' + colorTextureTextureImageUnit[i]);
            GlStateManager.func_179144_i(dfbColorTexturesA[i]);
         }

         GlStateManager.func_179138_g('\u84c6');
         GlStateManager.func_179144_i(dfbDepthTextures.get(0));
         if(usedDepthBuffers >= 2) {
            GlStateManager.func_179138_g('\u84cb');
            GlStateManager.func_179144_i(dfbDepthTextures.get(1));
            if(usedDepthBuffers >= 3) {
               GlStateManager.func_179138_g('\u84cc');
               GlStateManager.func_179144_i(dfbDepthTextures.get(2));
            }
         }

         for(int j1 = 0; j1 < usedShadowColorBuffers; ++j1) {
            GlStateManager.func_179138_g('\u84cd' + j1);
            GlStateManager.func_179144_i(sfbColorTextures.get(j1));
         }

         if(noiseTextureEnabled) {
            GlStateManager.func_179138_g('\u84c0' + noiseTexture.getTextureUnit());
            GlStateManager.func_179144_i(noiseTexture.getTextureId());
         }

         if(renderFinal) {
            bindCustomTextures(customTexturesComposite);
         } else {
            bindCustomTextures(customTexturesDeferred);
         }

         GlStateManager.func_179138_g('\u84c0');
         boolean flag = true;

         for(int j = 0; j < usedColorBuffers; ++j) {
            EXTFramebufferObject.glFramebufferTexture2DEXT('\u8d40', '\u8ce0' + j, 3553, dfbColorTexturesA[8 + j], 0);
         }

         EXTFramebufferObject.glFramebufferTexture2DEXT('\u8d40', '\u8d00', 3553, dfbDepthTextures.get(0), 0);
         GL20.glDrawBuffers(dfbDrawBuffers);
         checkGLError("pre-composite");

         for(int k1 = 0; k1 < countPrograms; ++k1) {
            if(programsID[programBase + k1] != 0) {
               useProgram(programBase + k1);
               checkGLError(programNames[programBase + k1]);
               if(activeCompositeMipmapSetting != 0) {
                  genCompositeMipmap();
               }

               drawComposite();

               for(int k = 0; k < usedColorBuffers; ++k) {
                  if(programsToggleColorTextures[programBase + k1][k]) {
                     int l = colorTexturesToggle[k];
                     int i1 = colorTexturesToggle[k] = 8 - l;
                     GlStateManager.func_179138_g('\u84c0' + colorTextureTextureImageUnit[k]);
                     GlStateManager.func_179144_i(dfbColorTexturesA[i1 + k]);
                     EXTFramebufferObject.glFramebufferTexture2DEXT('\u8d40', '\u8ce0' + k, 3553, dfbColorTexturesA[l + k], 0);
                  }
               }

               GlStateManager.func_179138_g('\u84c0');
            }
         }

         checkGLError("composite");
         int l1 = renderFinal?43:42;
         if(programsID[l1] != 0) {
            useProgram(l1);
            checkGLError(programNames[l1]);
            if(activeCompositeMipmapSetting != 0) {
               genCompositeMipmap();
            }

            drawComposite();

            for(int i2 = 0; i2 < usedColorBuffers; ++i2) {
               if(programsToggleColorTextures[l1][i2]) {
                  int k2 = colorTexturesToggle[i2];
                  int l2 = colorTexturesToggle[i2] = 8 - k2;
                  GlStateManager.func_179138_g('\u84c0' + colorTextureTextureImageUnit[i2]);
                  GlStateManager.func_179144_i(dfbColorTexturesA[l2 + i2]);
                  EXTFramebufferObject.glFramebufferTexture2DEXT('\u8d40', '\u8ce0' + i2, 3553, dfbColorTexturesA[k2 + i2], 0);
               }
            }

            GlStateManager.func_179138_g('\u84c0');
         }

         if(renderFinal) {
            renderFinal();
         }

         if(renderFinal) {
            isCompositeRendered = true;
         }

         if(!renderFinal) {
            for(int j2 = 0; j2 < usedColorBuffers; ++j2) {
               EXTFramebufferObject.glFramebufferTexture2DEXT('\u8d40', '\u8ce0' + j2, 3553, dfbColorTexturesA[j2], 0);
            }

            setDrawBuffers(programsDrawBuffers[12]);
         }

         GlStateManager.func_179145_e();
         GlStateManager.func_179098_w();
         GlStateManager.func_179141_d();
         GlStateManager.func_179147_l();
         GlStateManager.func_179143_c(515);
         GlStateManager.func_179132_a(true);
         GL11.glPopMatrix();
         GL11.glMatrixMode(5888);
         GL11.glPopMatrix();
         useProgram(0);
      }
   }

   private static void renderFinal() {
      isRenderingDfb = false;
      mc.func_147110_a().func_147610_a(true);
      OpenGlHelper.func_153188_a(OpenGlHelper.field_153198_e, OpenGlHelper.field_153200_g, 3553, mc.func_147110_a().field_147617_g, 0);
      GL11.glViewport(0, 0, mc.field_71443_c, mc.field_71440_d);
      if(EntityRenderer.field_78517_a) {
         boolean flag = EntityRenderer.field_78515_b != 0;
         GlStateManager.func_179135_a(flag, !flag, !flag, true);
      }

      GlStateManager.func_179132_a(true);
      GL11.glClearColor(clearColorR, clearColorG, clearColorB, 1.0F);
      GL11.glClear(16640);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179098_w();
      GlStateManager.func_179118_c();
      GlStateManager.func_179084_k();
      GlStateManager.func_179126_j();
      GlStateManager.func_179143_c(519);
      GlStateManager.func_179132_a(false);
      checkGLError("pre-final");
      useProgram(29);
      checkGLError("final");
      if(activeCompositeMipmapSetting != 0) {
         genCompositeMipmap();
      }

      drawComposite();
      checkGLError("renderCompositeFinal");
   }

   public static void endRender() {
      if(isShadowPass) {
         checkGLError("shadow endRender");
      } else {
         if(!isCompositeRendered) {
            renderCompositeFinal();
         }

         isRenderingWorld = false;
         GlStateManager.func_179135_a(true, true, true, true);
         useProgram(0);
         RenderHelper.func_74518_a();
         checkGLError("endRender end");
      }
   }

   public static void beginSky() {
      isRenderingSky = true;
      fogEnabled = true;
      setDrawBuffers(dfbDrawBuffers);
      useProgram(5);
      pushEntity(-2, 0);
   }

   public static void setSkyColor(Vec3 v3color) {
      skyColorR = (float)v3color.field_72450_a;
      skyColorG = (float)v3color.field_72448_b;
      skyColorB = (float)v3color.field_72449_c;
      setProgramUniform3f("skyColor", skyColorR, skyColorG, skyColorB);
   }

   public static void drawHorizon() {
      WorldRenderer worldrenderer = Tessellator.func_178181_a().func_178180_c();
      float f = (float)(mc.field_71474_y.field_151451_c * 16);
      double d0 = (double)f * 0.9238D;
      double d1 = (double)f * 0.3826D;
      double d2 = -d1;
      double d3 = -d0;
      double d4 = 16.0D;
      double d5 = -cameraPositionY;
      worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
      worldrenderer.func_181662_b(d2, d5, d3).func_181675_d();
      worldrenderer.func_181662_b(d2, d4, d3).func_181675_d();
      worldrenderer.func_181662_b(d3, d4, d2).func_181675_d();
      worldrenderer.func_181662_b(d3, d5, d2).func_181675_d();
      worldrenderer.func_181662_b(d3, d5, d2).func_181675_d();
      worldrenderer.func_181662_b(d3, d4, d2).func_181675_d();
      worldrenderer.func_181662_b(d3, d4, d1).func_181675_d();
      worldrenderer.func_181662_b(d3, d5, d1).func_181675_d();
      worldrenderer.func_181662_b(d3, d5, d1).func_181675_d();
      worldrenderer.func_181662_b(d3, d4, d1).func_181675_d();
      worldrenderer.func_181662_b(d2, d4, d1).func_181675_d();
      worldrenderer.func_181662_b(d2, d5, d1).func_181675_d();
      worldrenderer.func_181662_b(d2, d5, d1).func_181675_d();
      worldrenderer.func_181662_b(d2, d4, d1).func_181675_d();
      worldrenderer.func_181662_b(d1, d4, d0).func_181675_d();
      worldrenderer.func_181662_b(d1, d5, d0).func_181675_d();
      worldrenderer.func_181662_b(d1, d5, d0).func_181675_d();
      worldrenderer.func_181662_b(d1, d4, d0).func_181675_d();
      worldrenderer.func_181662_b(d0, d4, d1).func_181675_d();
      worldrenderer.func_181662_b(d0, d5, d1).func_181675_d();
      worldrenderer.func_181662_b(d0, d5, d1).func_181675_d();
      worldrenderer.func_181662_b(d0, d4, d1).func_181675_d();
      worldrenderer.func_181662_b(d0, d4, d2).func_181675_d();
      worldrenderer.func_181662_b(d0, d5, d2).func_181675_d();
      worldrenderer.func_181662_b(d0, d5, d2).func_181675_d();
      worldrenderer.func_181662_b(d0, d4, d2).func_181675_d();
      worldrenderer.func_181662_b(d1, d4, d3).func_181675_d();
      worldrenderer.func_181662_b(d1, d5, d3).func_181675_d();
      worldrenderer.func_181662_b(d1, d5, d3).func_181675_d();
      worldrenderer.func_181662_b(d1, d4, d3).func_181675_d();
      worldrenderer.func_181662_b(d2, d4, d3).func_181675_d();
      worldrenderer.func_181662_b(d2, d5, d3).func_181675_d();
      Tessellator.func_178181_a().func_78381_a();
   }

   public static void preSkyList() {
      setUpPosition();
      GL11.glColor3f(fogColorR, fogColorG, fogColorB);
      drawHorizon();
      GL11.glColor3f(skyColorR, skyColorG, skyColorB);
   }

   public static void endSky() {
      isRenderingSky = false;
      setDrawBuffers(dfbDrawBuffers);
      useProgram(lightmapEnabled?3:2);
      popEntity();
   }

   public static void beginUpdateChunks() {
      checkGLError("beginUpdateChunks1");
      checkFramebufferStatus("beginUpdateChunks1");
      if(!isShadowPass) {
         useProgram(7);
      }

      checkGLError("beginUpdateChunks2");
      checkFramebufferStatus("beginUpdateChunks2");
   }

   public static void endUpdateChunks() {
      checkGLError("endUpdateChunks1");
      checkFramebufferStatus("endUpdateChunks1");
      if(!isShadowPass) {
         useProgram(7);
      }

      checkGLError("endUpdateChunks2");
      checkFramebufferStatus("endUpdateChunks2");
   }

   public static boolean shouldRenderClouds(GameSettings gs) {
      if(!shaderPackLoaded) {
         return true;
      } else {
         checkGLError("shouldRenderClouds");
         return isShadowPass?configCloudShadow:gs.field_74345_l > 0;
      }
   }

   public static void beginClouds() {
      fogEnabled = true;
      pushEntity(-3, 0);
      useProgram(6);
   }

   public static void endClouds() {
      disableFog();
      popEntity();
      useProgram(lightmapEnabled?3:2);
   }

   public static void beginEntities() {
      if(isRenderingWorld) {
         useProgram(16);
         resetDisplayListModels();
      }

   }

   public static void nextEntity(Entity entity) {
      if(isRenderingWorld) {
         useProgram(16);
         setEntityId(entity);
      }

   }

   public static void setEntityId(Entity entity) {
      if(isRenderingWorld && !isShadowPass && uniformEntityId.isDefined()) {
         int i = EntityList.func_75619_a(entity);
         uniformEntityId.setValue(i);
      }

   }

   public static void beginSpiderEyes() {
      if(isRenderingWorld && programsID[18] != programsID[0]) {
         useProgram(18);
         GlStateManager.func_179141_d();
         GlStateManager.func_179092_a(516, 0.0F);
         GlStateManager.func_179112_b(770, 771);
      }

   }

   public static void endSpiderEyes() {
      if(isRenderingWorld && programsID[18] != programsID[0]) {
         useProgram(16);
         GlStateManager.func_179118_c();
      }

   }

   public static void endEntities() {
      if(isRenderingWorld) {
         useProgram(lightmapEnabled?3:2);
      }

   }

   public static void setEntityColor(float r, float g, float b, float a) {
      if(isRenderingWorld && !isShadowPass) {
         uniformEntityColor.setValue(r, g, b, a);
      }

   }

   public static void beginLivingDamage() {
      if(isRenderingWorld) {
         ShadersTex.bindTexture(defaultTexture);
         if(!isShadowPass) {
            setDrawBuffers(drawBuffersColorAtt0);
         }
      }

   }

   public static void endLivingDamage() {
      if(isRenderingWorld && !isShadowPass) {
         setDrawBuffers(programsDrawBuffers[16]);
      }

   }

   public static void beginBlockEntities() {
      if(isRenderingWorld) {
         checkGLError("beginBlockEntities");
         useProgram(13);
      }

   }

   public static void nextBlockEntity(TileEntity tileEntity) {
      if(isRenderingWorld) {
         checkGLError("nextBlockEntity");
         useProgram(13);
         setBlockEntityId(tileEntity);
      }

   }

   public static void setBlockEntityId(TileEntity tileEntity) {
      if(isRenderingWorld && !isShadowPass && uniformBlockEntityId.isDefined()) {
         Block block = tileEntity.func_145838_q();
         int i = Block.func_149682_b(block);
         uniformBlockEntityId.setValue(i);
      }

   }

   public static void endBlockEntities() {
      if(isRenderingWorld) {
         checkGLError("endBlockEntities");
         useProgram(lightmapEnabled?3:2);
         ShadersTex.bindNSTextures(defaultTexture.getMultiTexID());
      }

   }

   public static void beginLitParticles() {
      useProgram(3);
   }

   public static void beginParticles() {
      useProgram(2);
   }

   public static void endParticles() {
      useProgram(3);
   }

   public static void readCenterDepth() {
      if(!isShadowPass && centerDepthSmoothEnabled) {
         tempDirectFloatBuffer.clear();
         GL11.glReadPixels(renderWidth / 2, renderHeight / 2, 1, 1, 6402, 5126, (FloatBuffer)tempDirectFloatBuffer);
         centerDepth = tempDirectFloatBuffer.get(0);
         float f = (float)diffSystemTime * 0.01F;
         float f1 = (float)Math.exp(Math.log(0.5D) * (double)f / (double)centerDepthSmoothHalflife);
         centerDepthSmooth = centerDepthSmooth * f1 + centerDepth * (1.0F - f1);
      }

   }

   public static void beginWeather() {
      if(!isShadowPass) {
         if(usedDepthBuffers >= 3) {
            GlStateManager.func_179138_g('\u84cc');
            GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, renderWidth, renderHeight);
            GlStateManager.func_179138_g('\u84c0');
         }

         GlStateManager.func_179126_j();
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(770, 771);
         GlStateManager.func_179141_d();
         useProgram(20);
      }

   }

   public static void endWeather() {
      GlStateManager.func_179084_k();
      useProgram(3);
   }

   public static void preWater() {
      if(usedDepthBuffers >= 2) {
         GlStateManager.func_179138_g('\u84cb');
         checkGLError("pre copy depth");
         GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, renderWidth, renderHeight);
         checkGLError("copy depth");
         GlStateManager.func_179138_g('\u84c0');
      }

      ShadersTex.bindNSTextures(defaultTexture.getMultiTexID());
   }

   public static void beginWater() {
      if(isRenderingWorld) {
         if(!isShadowPass) {
            renderDeferred();
            useProgram(12);
            GlStateManager.func_179147_l();
            GlStateManager.func_179132_a(true);
         } else {
            GlStateManager.func_179132_a(true);
         }
      }

   }

   public static void endWater() {
      if(isRenderingWorld) {
         if(isShadowPass) {
            ;
         }

         useProgram(lightmapEnabled?3:2);
      }

   }

   public static void beginProjectRedHalo() {
      if(isRenderingWorld) {
         useProgram(1);
      }

   }

   public static void endProjectRedHalo() {
      if(isRenderingWorld) {
         useProgram(3);
      }

   }

   public static void applyHandDepth() {
      if((double)configHandDepthMul != 1.0D) {
         GL11.glScaled(1.0D, 1.0D, (double)configHandDepthMul);
      }

   }

   public static void beginHand(boolean translucent) {
      GL11.glMatrixMode(5888);
      GL11.glPushMatrix();
      GL11.glMatrixMode(5889);
      GL11.glPushMatrix();
      GL11.glMatrixMode(5888);
      if(translucent) {
         useProgram(41);
      } else {
         useProgram(19);
      }

      checkGLError("beginHand");
      checkFramebufferStatus("beginHand");
   }

   public static void endHand() {
      checkGLError("pre endHand");
      checkFramebufferStatus("pre endHand");
      GL11.glMatrixMode(5889);
      GL11.glPopMatrix();
      GL11.glMatrixMode(5888);
      GL11.glPopMatrix();
      GlStateManager.func_179112_b(770, 771);
      checkGLError("endHand");
   }

   public static void beginFPOverlay() {
      GlStateManager.func_179140_f();
      GlStateManager.func_179084_k();
   }

   public static void endFPOverlay() {
   }

   public static void glEnableWrapper(int cap) {
      GL11.glEnable(cap);
      if(cap == 3553) {
         enableTexture2D();
      } else if(cap == 2912) {
         enableFog();
      }

   }

   public static void glDisableWrapper(int cap) {
      GL11.glDisable(cap);
      if(cap == 3553) {
         disableTexture2D();
      } else if(cap == 2912) {
         disableFog();
      }

   }

   public static void sglEnableT2D(int cap) {
      GL11.glEnable(cap);
      enableTexture2D();
   }

   public static void sglDisableT2D(int cap) {
      GL11.glDisable(cap);
      disableTexture2D();
   }

   public static void sglEnableFog(int cap) {
      GL11.glEnable(cap);
      enableFog();
   }

   public static void sglDisableFog(int cap) {
      GL11.glDisable(cap);
      disableFog();
   }

   public static void enableTexture2D() {
      if(isRenderingSky) {
         useProgram(5);
      } else if(activeProgram == 1) {
         useProgram(lightmapEnabled?3:2);
      }

   }

   public static void disableTexture2D() {
      if(isRenderingSky) {
         useProgram(4);
      } else if(activeProgram == 2 || activeProgram == 3) {
         useProgram(1);
      }

   }

   public static void beginLeash() {
      useProgram(1);
   }

   public static void endLeash() {
      useProgram(16);
   }

   public static void enableFog() {
      fogEnabled = true;
      setProgramUniform1i("fogMode", fogMode);
   }

   public static void disableFog() {
      fogEnabled = false;
      setProgramUniform1i("fogMode", 0);
   }

   public static void setFog(int fogMode) {
      GlStateManager.func_179093_d(fogMode);
      fogMode = fogMode;
      if(fogEnabled) {
         setProgramUniform1i("fogMode", fogMode);
      }

   }

   public static void sglFogi(int pname, int param) {
      GL11.glFogi(pname, param);
      if(pname == 2917) {
         fogMode = param;
         if(fogEnabled) {
            setProgramUniform1i("fogMode", fogMode);
         }
      }

   }

   public static void enableLightmap() {
      lightmapEnabled = true;
      if(activeProgram == 2) {
         useProgram(3);
      }

   }

   public static void disableLightmap() {
      lightmapEnabled = false;
      if(activeProgram == 3) {
         useProgram(2);
      }

   }

   public static int getEntityData() {
      return entityData[entityDataIndex * 2];
   }

   public static int getEntityData2() {
      return entityData[entityDataIndex * 2 + 1];
   }

   public static int setEntityData1(int data1) {
      entityData[entityDataIndex * 2] = entityData[entityDataIndex * 2] & '\uffff' | data1 << 16;
      return data1;
   }

   public static int setEntityData2(int data2) {
      entityData[entityDataIndex * 2 + 1] = entityData[entityDataIndex * 2 + 1] & -65536 | data2 & '\uffff';
      return data2;
   }

   public static void pushEntity(int data0, int data1) {
      ++entityDataIndex;
      entityData[entityDataIndex * 2] = data0 & '\uffff' | data1 << 16;
      entityData[entityDataIndex * 2 + 1] = 0;
   }

   public static void pushEntity(int data0) {
      ++entityDataIndex;
      entityData[entityDataIndex * 2] = data0 & '\uffff';
      entityData[entityDataIndex * 2 + 1] = 0;
   }

   public static void pushEntity(Block block) {
      ++entityDataIndex;
      entityData[entityDataIndex * 2] = Block.field_149771_c.func_148757_b(block) & '\uffff' | block.func_149645_b() << 16;
      entityData[entityDataIndex * 2 + 1] = 0;
   }

   public static void popEntity() {
      entityData[entityDataIndex * 2] = 0;
      entityData[entityDataIndex * 2 + 1] = 0;
      --entityDataIndex;
   }

   public static void mcProfilerEndSection() {
      mc.field_71424_I.func_76319_b();
   }

   public static String getShaderPackName() {
      return shaderPack == null?null:(shaderPack instanceof ShaderPackNone?null:shaderPack.getName());
   }

   public static InputStream getShaderPackResourceStream(String path) {
      return shaderPack == null?null:shaderPack.getResourceAsStream(path);
   }

   public static void nextAntialiasingLevel() {
      configAntialiasingLevel += 2;
      configAntialiasingLevel = configAntialiasingLevel / 2 * 2;
      if(configAntialiasingLevel > 4) {
         configAntialiasingLevel = 0;
      }

      configAntialiasingLevel = Config.limit(configAntialiasingLevel, 0, 4);
   }

   public static void checkShadersModInstalled() {
      try {
         Class oclass = Class.forName("shadersmod.transform.SMCClassTransformer");
      } catch (Throwable var1) {
         return;
      }

      throw new RuntimeException("Shaders Mod detected. Please remove it, OptiFine has built-in support for shaders.");
   }

   public static void resourcesReloaded() {
      loadShaderPackResources();
      if(shaderPackLoaded) {
         BlockAliases.resourcesReloaded();
      }

   }

   private static void loadShaderPackResources() {
      shaderPackResources = new HashMap();
      if(shaderPackLoaded) {
         List<String> list = new ArrayList();
         String s = "/shaders/lang/";
         String s1 = "en_US";
         String s2 = ".lang";
         list.add(s + s1 + s2);
         if(!Config.getGameSettings().field_74363_ab.equals(s1)) {
            list.add(s + Config.getGameSettings().field_74363_ab + s2);
         }

         try {
            for(String s3 : list) {
               InputStream inputstream = shaderPack.getResourceAsStream(s3);
               if(inputstream != null) {
                  Properties properties = new Properties();
                  Lang.loadLocaleData(inputstream, properties);
                  inputstream.close();

                  for(String s4 : properties.keySet()) {
                     String s5 = properties.getProperty(s4);
                     shaderPackResources.put(s4, s5);
                  }
               }
            }
         } catch (IOException ioexception) {
            ioexception.printStackTrace();
         }

      }
   }

   public static String translate(String key, String def) {
      String s = (String)shaderPackResources.get(key);
      return s == null?def:s;
   }

   public static boolean isProgramPath(String program) {
      if(program == null) {
         return false;
      } else if(program.length() <= 0) {
         return false;
      } else {
         int i = program.lastIndexOf("/");
         if(i >= 0) {
            program = program.substring(i + 1);
         }

         return Arrays.asList(programNames).contains(program);
      }
   }

   public static void setItemToRenderMain(ItemStack itemToRenderMain) {
      itemToRenderMainTranslucent = isTranslucentBlock(itemToRenderMain);
   }

   public static boolean isItemToRenderMainTranslucent() {
      return itemToRenderMainTranslucent;
   }

   private static boolean isTranslucentBlock(ItemStack stack) {
      if(stack == null) {
         return false;
      } else {
         Item item = stack.func_77973_b();
         if(item == null) {
            return false;
         } else if(!(item instanceof ItemBlock)) {
            return false;
         } else {
            ItemBlock itemblock = (ItemBlock)item;
            Block block = itemblock.func_179223_d();
            if(block == null) {
               return false;
            } else {
               EnumWorldBlockLayer enumworldblocklayer = block.func_180664_k();
               return enumworldblocklayer == EnumWorldBlockLayer.TRANSLUCENT;
            }
         }
      }
   }

   public static boolean isRenderBothHands() {
      return false;
   }

   public static boolean isHandRenderedMain() {
      return isHandRenderedMain;
   }

   public static void setHandRenderedMain(boolean isHandRenderedMain) {
      isHandRenderedMain = isHandRenderedMain;
   }

   public static float getShadowRenderDistance() {
      return shadowDistanceRenderMul < 0.0F?-1.0F:shadowMapHalfPlane * shadowDistanceRenderMul;
   }

   public static void setRenderingFirstPersonHand(boolean flag) {
      isRenderingFirstPersonHand = flag;
   }

   public static boolean isRenderingFirstPersonHand() {
      return isRenderingFirstPersonHand;
   }

   public static void beginBeacon() {
      if(isRenderingWorld) {
         useProgram(14);
      }

   }

   public static void endBeacon() {
      if(isRenderingWorld) {
         useProgram(13);
      }

   }

   public static World getCurrentWorld() {
      return currentWorld;
   }

   public static BlockPos getCameraPosition() {
      return new BlockPos(cameraPositionX, cameraPositionY, cameraPositionZ);
   }

   public static boolean isCustomUniforms() {
      return customUniforms != null;
   }

   static {
      shadersdir = new File(Minecraft.func_71410_x().field_71412_D, "shaders");
      shaderpacksdir = new File(Minecraft.func_71410_x().field_71412_D, shaderpacksdirname);
      configFile = new File(Minecraft.func_71410_x().field_71412_D, optionsfilename);
      drawBuffersNone.limit(0);
      drawBuffersColorAtt0.put('\u8ce0').position(0).limit(1);
   }
}
