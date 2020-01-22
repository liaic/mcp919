package net.minecraft.client.settings;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.stream.TwitchStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.src.ClearWater;
import net.minecraft.src.Config;
import net.minecraft.src.CustomColors;
import net.minecraft.src.CustomGuis;
import net.minecraft.src.CustomSky;
import net.minecraft.src.DynamicLights;
import net.minecraft.src.Lang;
import net.minecraft.src.NaturalTextures;
import net.minecraft.src.RandomMobs;
import net.minecraft.src.Reflector;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import shadersmod.client.Shaders;

public class GameSettings {
   private static final Logger field_151454_ax = LogManager.getLogger();
   private static final Gson field_151450_ay = new Gson();
   private static final ParameterizedType field_151449_az = new ParameterizedType() {
      private static final String __OBFID = "CL_00000651";

      public Type[] getActualTypeArguments() {
         return new Type[]{String.class};
      }

      public Type getRawType() {
         return List.class;
      }

      public Type getOwnerType() {
         return null;
      }
   };
   private static final String[] field_74367_ae = new String[]{"options.guiScale.auto", "options.guiScale.small", "options.guiScale.normal", "options.guiScale.large"};
   private static final String[] field_74364_ag = new String[]{"options.particles.all", "options.particles.decreased", "options.particles.minimal"};
   private static final String[] field_98303_au = new String[]{"options.ao.off", "options.ao.min", "options.ao.max"};
   private static final String[] field_152391_aS = new String[]{"options.stream.compression.low", "options.stream.compression.medium", "options.stream.compression.high"};
   private static final String[] field_152392_aT = new String[]{"options.stream.chat.enabled.streaming", "options.stream.chat.enabled.always", "options.stream.chat.enabled.never"};
   private static final String[] field_152393_aU = new String[]{"options.stream.chat.userFilter.all", "options.stream.chat.userFilter.subs", "options.stream.chat.userFilter.mods"};
   private static final String[] field_152394_aV = new String[]{"options.stream.mic_toggle.mute", "options.stream.mic_toggle.talk"};
   private static final String[] field_181149_aW = new String[]{"options.off", "options.graphics.fast", "options.graphics.fancy"};
   public float field_74341_c = 0.5F;
   public boolean field_74338_d;
   public int field_151451_c = -1;
   public boolean field_74336_f = true;
   public boolean field_74337_g;
   public boolean field_151448_g = true;
   public int field_74350_i = 120;
   public int field_74345_l = 2;
   public boolean field_74347_j = true;
   public int field_74348_k = 2;
   public List field_151453_l = Lists.newArrayList();
   public List field_183018_l = Lists.newArrayList();
   public EntityPlayer.EnumChatVisibility field_74343_n = EntityPlayer.EnumChatVisibility.FULL;
   public boolean field_74344_o = true;
   public boolean field_74359_p = true;
   public boolean field_74358_q = true;
   public float field_74357_r = 1.0F;
   public boolean field_74355_t = true;
   public boolean field_74353_u;
   public boolean field_74352_v = true;
   public boolean field_178881_t = false;
   public boolean field_178880_u = true;
   public boolean field_178879_v = false;
   public boolean field_80005_w;
   public boolean field_82882_x;
   public boolean field_82881_y = true;
   private final Set field_178882_aU = Sets.newHashSet(EnumPlayerModelParts.values());
   public boolean field_85185_A;
   public int field_92118_B;
   public int field_92119_C;
   public boolean field_92117_D = true;
   public float field_96691_E = 1.0F;
   public float field_96692_F = 1.0F;
   public float field_96693_G = 0.44366196F;
   public float field_96694_H = 1.0F;
   public boolean field_151441_H = true;
   public int field_151442_I = 4;
   private Map field_151446_aD = Maps.newEnumMap(SoundCategory.class);
   public float field_152400_J = 0.5F;
   public float field_152401_K = 1.0F;
   public float field_152402_L = 1.0F;
   public float field_152403_M = 0.5412844F;
   public float field_152404_N = 0.31690142F;
   public int field_152405_O = 1;
   public boolean field_152406_P = true;
   public String field_152407_Q = "";
   public int field_152408_R = 0;
   public int field_152409_S = 0;
   public int field_152410_T = 0;
   public boolean field_181150_U = true;
   public boolean field_181151_V = true;
   public boolean field_183509_X;
   public KeyBinding field_74351_w = new KeyBinding("key.forward", 17, "key.categories.movement");
   public KeyBinding field_74370_x = new KeyBinding("key.left", 30, "key.categories.movement");
   public KeyBinding field_74368_y = new KeyBinding("key.back", 31, "key.categories.movement");
   public KeyBinding field_74366_z = new KeyBinding("key.right", 32, "key.categories.movement");
   public KeyBinding field_74314_A = new KeyBinding("key.jump", 57, "key.categories.movement");
   public KeyBinding field_74311_E = new KeyBinding("key.sneak", 42, "key.categories.movement");
   public KeyBinding field_151444_V = new KeyBinding("key.sprint", 29, "key.categories.movement");
   public KeyBinding field_151445_Q = new KeyBinding("key.inventory", 18, "key.categories.inventory");
   public KeyBinding field_74313_G = new KeyBinding("key.use", -99, "key.categories.gameplay");
   public KeyBinding field_74316_C = new KeyBinding("key.drop", 16, "key.categories.gameplay");
   public KeyBinding field_74312_F = new KeyBinding("key.attack", -100, "key.categories.gameplay");
   public KeyBinding field_74322_I = new KeyBinding("key.pickItem", -98, "key.categories.gameplay");
   public KeyBinding field_74310_D = new KeyBinding("key.chat", 20, "key.categories.multiplayer");
   public KeyBinding field_74321_H = new KeyBinding("key.playerlist", 15, "key.categories.multiplayer");
   public KeyBinding field_74323_J = new KeyBinding("key.command", 53, "key.categories.multiplayer");
   public KeyBinding field_151447_Z = new KeyBinding("key.screenshot", 60, "key.categories.misc");
   public KeyBinding field_151457_aa = new KeyBinding("key.togglePerspective", 63, "key.categories.misc");
   public KeyBinding field_151458_ab = new KeyBinding("key.smoothCamera", 0, "key.categories.misc");
   public KeyBinding field_152395_am = new KeyBinding("key.fullscreen", 87, "key.categories.misc");
   public KeyBinding field_178883_an = new KeyBinding("key.spectatorOutlines", 0, "key.categories.misc");
   public KeyBinding field_152396_an = new KeyBinding("key.streamStartStop", 64, "key.categories.stream");
   public KeyBinding field_152397_ao = new KeyBinding("key.streamPauseUnpause", 65, "key.categories.stream");
   public KeyBinding field_152398_ap = new KeyBinding("key.streamCommercial", 0, "key.categories.stream");
   public KeyBinding field_152399_aq = new KeyBinding("key.streamToggleMic", 0, "key.categories.stream");
   public KeyBinding[] field_151456_ac = new KeyBinding[]{new KeyBinding("key.hotbar.1", 2, "key.categories.inventory"), new KeyBinding("key.hotbar.2", 3, "key.categories.inventory"), new KeyBinding("key.hotbar.3", 4, "key.categories.inventory"), new KeyBinding("key.hotbar.4", 5, "key.categories.inventory"), new KeyBinding("key.hotbar.5", 6, "key.categories.inventory"), new KeyBinding("key.hotbar.6", 7, "key.categories.inventory"), new KeyBinding("key.hotbar.7", 8, "key.categories.inventory"), new KeyBinding("key.hotbar.8", 9, "key.categories.inventory"), new KeyBinding("key.hotbar.9", 10, "key.categories.inventory")};
   public KeyBinding[] field_74324_K;
   protected Minecraft field_74317_L;
   private File field_74354_ai;
   public EnumDifficulty field_74318_M;
   public boolean field_74319_N;
   public int field_74320_O;
   public boolean field_74330_P;
   public boolean field_74329_Q;
   public boolean field_181657_aC;
   public String field_74332_R;
   public boolean field_74326_T;
   public boolean field_74325_U;
   public float field_74334_X;
   public float field_74333_Y;
   public float field_151452_as;
   public int field_74335_Z;
   public int field_74362_aa;
   public String field_74363_ab;
   public boolean field_151455_aw;
   private static final String __OBFID = "CL_00000650";
   public int ofFogType = 1;
   public float ofFogStart = 0.8F;
   public int ofMipmapType = 0;
   public boolean ofOcclusionFancy = false;
   public boolean ofSmoothFps = false;
   public boolean ofSmoothWorld = Config.isSingleProcessor();
   public boolean ofLazyChunkLoading = Config.isSingleProcessor();
   public float ofAoLevel = 1.0F;
   public int ofAaLevel = 0;
   public int ofAfLevel = 1;
   public int ofClouds = 0;
   public float ofCloudsHeight = 0.0F;
   public int ofTrees = 0;
   public int ofRain = 0;
   public int ofDroppedItems = 0;
   public int ofBetterGrass = 3;
   public int ofAutoSaveTicks = 4000;
   public boolean ofLagometer = false;
   public boolean ofProfiler = false;
   public boolean ofShowFps = false;
   public boolean ofWeather = true;
   public boolean ofSky = true;
   public boolean ofStars = true;
   public boolean ofSunMoon = true;
   public int ofVignette = 0;
   public int ofChunkUpdates = 1;
   public boolean ofChunkUpdatesDynamic = false;
   public int ofTime = 0;
   public boolean ofClearWater = false;
   public boolean ofBetterSnow = false;
   public String ofFullscreenMode = "Default";
   public boolean ofSwampColors = true;
   public boolean ofRandomMobs = true;
   public boolean ofSmoothBiomes = true;
   public boolean ofCustomFonts = true;
   public boolean ofCustomColors = true;
   public boolean ofCustomSky = true;
   public boolean ofShowCapes = true;
   public int ofConnectedTextures = 2;
   public boolean ofCustomItems = true;
   public boolean ofNaturalTextures = false;
   public boolean ofFastMath = false;
   public boolean ofFastRender = true;
   public int ofTranslucentBlocks = 0;
   public boolean ofDynamicFov = true;
   public int ofDynamicLights = 3;
   public boolean ofCustomGuis = true;
   public int ofAnimatedWater = 0;
   public int ofAnimatedLava = 0;
   public boolean ofAnimatedFire = true;
   public boolean ofAnimatedPortal = true;
   public boolean ofAnimatedRedstone = true;
   public boolean ofAnimatedExplosion = true;
   public boolean ofAnimatedFlame = true;
   public boolean ofAnimatedSmoke = true;
   public boolean ofVoidParticles = true;
   public boolean ofWaterParticles = true;
   public boolean ofRainSplash = true;
   public boolean ofPortalParticles = true;
   public boolean ofPotionParticles = true;
   public boolean ofFireworkParticles = true;
   public boolean ofDrippingWaterLava = true;
   public boolean ofAnimatedTerrain = true;
   public boolean ofAnimatedTextures = true;
   public static final int DEFAULT = 0;
   public static final int FAST = 1;
   public static final int FANCY = 2;
   public static final int OFF = 3;
   public static final int SMART = 4;
   public static final int ANIM_ON = 0;
   public static final int ANIM_GENERATED = 1;
   public static final int ANIM_OFF = 2;
   public static final String DEFAULT_STR = "Default";
   private static final int[] OF_TREES_VALUES = new int[]{0, 1, 4, 2};
   private static final int[] OF_DYNAMIC_LIGHTS = new int[]{3, 1, 2};
   private static final String[] KEYS_DYNAMIC_LIGHTS = new String[]{"options.off", "options.graphics.fast", "options.graphics.fancy"};
   public KeyBinding ofKeyBindZoom;
   private File optionsFileOF;

   public GameSettings(Minecraft p_i46326_1_, File p_i46326_2_) {
      this.field_74324_K = (KeyBinding[])((KeyBinding[])ArrayUtils.addAll(new KeyBinding[]{this.field_74312_F, this.field_74313_G, this.field_74351_w, this.field_74370_x, this.field_74368_y, this.field_74366_z, this.field_74314_A, this.field_74311_E, this.field_151444_V, this.field_74316_C, this.field_151445_Q, this.field_74310_D, this.field_74321_H, this.field_74322_I, this.field_74323_J, this.field_151447_Z, this.field_151457_aa, this.field_151458_ab, this.field_152396_an, this.field_152397_ao, this.field_152398_ap, this.field_152399_aq, this.field_152395_am, this.field_178883_an}, this.field_151456_ac));
      this.field_74318_M = EnumDifficulty.NORMAL;
      this.field_74332_R = "";
      this.field_74334_X = 70.0F;
      this.field_74363_ab = "en_US";
      this.field_151455_aw = false;
      this.field_74317_L = p_i46326_1_;
      this.field_74354_ai = new File(p_i46326_2_, "options.txt");
      this.optionsFileOF = new File(p_i46326_2_, "optionsof.txt");
      this.field_74350_i = (int)GameSettings.Options.FRAMERATE_LIMIT.func_148267_f();
      this.ofKeyBindZoom = new KeyBinding("of.key.zoom", 46, "key.categories.misc");
      this.field_74324_K = (KeyBinding[])((KeyBinding[])ArrayUtils.add(this.field_74324_K, this.ofKeyBindZoom));
      GameSettings.Options.RENDER_DISTANCE.func_148263_a(32.0F);
      long i = 1000000L;
      if(Runtime.getRuntime().maxMemory() >= 1500L * i) {
         GameSettings.Options.RENDER_DISTANCE.func_148263_a(48.0F);
      }

      if(Runtime.getRuntime().maxMemory() >= 2500L * i) {
         GameSettings.Options.RENDER_DISTANCE.func_148263_a(64.0F);
      }

      this.field_151451_c = 8;
      this.func_74300_a();
      Config.initGameSettings(this);
   }

   public GameSettings() {
      this.field_74324_K = (KeyBinding[])((KeyBinding[])ArrayUtils.addAll(new KeyBinding[]{this.field_74312_F, this.field_74313_G, this.field_74351_w, this.field_74370_x, this.field_74368_y, this.field_74366_z, this.field_74314_A, this.field_74311_E, this.field_151444_V, this.field_74316_C, this.field_151445_Q, this.field_74310_D, this.field_74321_H, this.field_74322_I, this.field_74323_J, this.field_151447_Z, this.field_151457_aa, this.field_151458_ab, this.field_152396_an, this.field_152397_ao, this.field_152398_ap, this.field_152399_aq, this.field_152395_am, this.field_178883_an}, this.field_151456_ac));
      this.field_74318_M = EnumDifficulty.NORMAL;
      this.field_74332_R = "";
      this.field_74334_X = 70.0F;
      this.field_74363_ab = "en_US";
      this.field_151455_aw = false;
   }

   public static String func_74298_c(int p_74298_0_) {
      return p_74298_0_ < 0?I18n.func_135052_a("key.mouseButton", new Object[]{Integer.valueOf(p_74298_0_ + 101)}):(p_74298_0_ < 256?Keyboard.getKeyName(p_74298_0_):String.format("%c", new Object[]{Character.valueOf((char)(p_74298_0_ - 256))}).toUpperCase());
   }

   public static boolean func_100015_a(KeyBinding p_100015_0_) {
      int i = p_100015_0_.func_151463_i();
      return i >= -100 && i <= 255?(p_100015_0_.func_151463_i() == 0?false:(p_100015_0_.func_151463_i() < 0?Mouse.isButtonDown(p_100015_0_.func_151463_i() + 100):Keyboard.isKeyDown(p_100015_0_.func_151463_i()))):false;
   }

   public void func_151440_a(KeyBinding p_151440_1_, int p_151440_2_) {
      p_151440_1_.func_151462_b(p_151440_2_);
      this.func_74303_b();
   }

   public void func_74304_a(GameSettings.Options p_74304_1_, float p_74304_2_) {
      this.setOptionFloatValueOF(p_74304_1_, p_74304_2_);
      if(p_74304_1_ == GameSettings.Options.SENSITIVITY) {
         this.field_74341_c = p_74304_2_;
      }

      if(p_74304_1_ == GameSettings.Options.FOV) {
         this.field_74334_X = p_74304_2_;
      }

      if(p_74304_1_ == GameSettings.Options.GAMMA) {
         this.field_74333_Y = p_74304_2_;
      }

      if(p_74304_1_ == GameSettings.Options.FRAMERATE_LIMIT) {
         this.field_74350_i = (int)p_74304_2_;
         this.field_74352_v = false;
         if(this.field_74350_i <= 0) {
            this.field_74350_i = (int)GameSettings.Options.FRAMERATE_LIMIT.func_148267_f();
            this.field_74352_v = true;
         }

         this.updateVSync();
      }

      if(p_74304_1_ == GameSettings.Options.CHAT_OPACITY) {
         this.field_74357_r = p_74304_2_;
         this.field_74317_L.field_71456_v.func_146158_b().func_146245_b();
      }

      if(p_74304_1_ == GameSettings.Options.CHAT_HEIGHT_FOCUSED) {
         this.field_96694_H = p_74304_2_;
         this.field_74317_L.field_71456_v.func_146158_b().func_146245_b();
      }

      if(p_74304_1_ == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED) {
         this.field_96693_G = p_74304_2_;
         this.field_74317_L.field_71456_v.func_146158_b().func_146245_b();
      }

      if(p_74304_1_ == GameSettings.Options.CHAT_WIDTH) {
         this.field_96692_F = p_74304_2_;
         this.field_74317_L.field_71456_v.func_146158_b().func_146245_b();
      }

      if(p_74304_1_ == GameSettings.Options.CHAT_SCALE) {
         this.field_96691_E = p_74304_2_;
         this.field_74317_L.field_71456_v.func_146158_b().func_146245_b();
      }

      if(p_74304_1_ == GameSettings.Options.MIPMAP_LEVELS) {
         int i = this.field_151442_I;
         this.field_151442_I = (int)p_74304_2_;
         if((float)i != p_74304_2_) {
            this.field_74317_L.func_147117_R().func_147633_a(this.field_151442_I);
            this.field_74317_L.func_110434_K().func_110577_a(TextureMap.field_110575_b);
            this.field_74317_L.func_147117_R().func_174937_a(false, this.field_151442_I > 0);
            this.field_74317_L.func_175603_A();
         }
      }

      if(p_74304_1_ == GameSettings.Options.BLOCK_ALTERNATIVES) {
         this.field_178880_u = !this.field_178880_u;
         this.field_74317_L.field_71438_f.func_72712_a();
      }

      if(p_74304_1_ == GameSettings.Options.RENDER_DISTANCE) {
         this.field_151451_c = (int)p_74304_2_;
         this.field_74317_L.field_71438_f.func_174979_m();
      }

      if(p_74304_1_ == GameSettings.Options.STREAM_BYTES_PER_PIXEL) {
         this.field_152400_J = p_74304_2_;
      }

      if(p_74304_1_ == GameSettings.Options.STREAM_VOLUME_MIC) {
         this.field_152401_K = p_74304_2_;
         this.field_74317_L.func_152346_Z().func_152915_s();
      }

      if(p_74304_1_ == GameSettings.Options.STREAM_VOLUME_SYSTEM) {
         this.field_152402_L = p_74304_2_;
         this.field_74317_L.func_152346_Z().func_152915_s();
      }

      if(p_74304_1_ == GameSettings.Options.STREAM_KBPS) {
         this.field_152403_M = p_74304_2_;
      }

      if(p_74304_1_ == GameSettings.Options.STREAM_FPS) {
         this.field_152404_N = p_74304_2_;
      }

   }

   public void func_74306_a(GameSettings.Options p_74306_1_, int p_74306_2_) {
      this.setOptionValueOF(p_74306_1_, p_74306_2_);
      if(p_74306_1_ == GameSettings.Options.INVERT_MOUSE) {
         this.field_74338_d = !this.field_74338_d;
      }

      if(p_74306_1_ == GameSettings.Options.GUI_SCALE) {
         this.field_74335_Z = this.field_74335_Z + p_74306_2_ & 3;
      }

      if(p_74306_1_ == GameSettings.Options.PARTICLES) {
         this.field_74362_aa = (this.field_74362_aa + p_74306_2_) % 3;
      }

      if(p_74306_1_ == GameSettings.Options.VIEW_BOBBING) {
         this.field_74336_f = !this.field_74336_f;
      }

      if(p_74306_1_ == GameSettings.Options.RENDER_CLOUDS) {
         this.field_74345_l = (this.field_74345_l + p_74306_2_) % 3;
      }

      if(p_74306_1_ == GameSettings.Options.FORCE_UNICODE_FONT) {
         this.field_151455_aw = !this.field_151455_aw;
         this.field_74317_L.field_71466_p.func_78264_a(this.field_74317_L.func_135016_M().func_135042_a() || this.field_151455_aw);
      }

      if(p_74306_1_ == GameSettings.Options.FBO_ENABLE) {
         this.field_151448_g = !this.field_151448_g;
      }

      if(p_74306_1_ == GameSettings.Options.ANAGLYPH) {
         if(!this.field_74337_g && Config.isShaders()) {
            Config.showGuiMessage(Lang.get("of.message.an.shaders1"), Lang.get("of.message.an.shaders2"));
            return;
         }

         this.field_74337_g = !this.field_74337_g;
         this.field_74317_L.func_110436_a();
      }

      if(p_74306_1_ == GameSettings.Options.GRAPHICS) {
         this.field_74347_j = !this.field_74347_j;
         this.updateRenderClouds();
         this.field_74317_L.field_71438_f.func_72712_a();
      }

      if(p_74306_1_ == GameSettings.Options.AMBIENT_OCCLUSION) {
         this.field_74348_k = (this.field_74348_k + p_74306_2_) % 3;
         this.field_74317_L.field_71438_f.func_72712_a();
      }

      if(p_74306_1_ == GameSettings.Options.CHAT_VISIBILITY) {
         this.field_74343_n = EntityPlayer.EnumChatVisibility.func_151426_a((this.field_74343_n.func_151428_a() + p_74306_2_) % 3);
      }

      if(p_74306_1_ == GameSettings.Options.STREAM_COMPRESSION) {
         this.field_152405_O = (this.field_152405_O + p_74306_2_) % 3;
      }

      if(p_74306_1_ == GameSettings.Options.STREAM_SEND_METADATA) {
         this.field_152406_P = !this.field_152406_P;
      }

      if(p_74306_1_ == GameSettings.Options.STREAM_CHAT_ENABLED) {
         this.field_152408_R = (this.field_152408_R + p_74306_2_) % 3;
      }

      if(p_74306_1_ == GameSettings.Options.STREAM_CHAT_USER_FILTER) {
         this.field_152409_S = (this.field_152409_S + p_74306_2_) % 3;
      }

      if(p_74306_1_ == GameSettings.Options.STREAM_MIC_TOGGLE_BEHAVIOR) {
         this.field_152410_T = (this.field_152410_T + p_74306_2_) % 2;
      }

      if(p_74306_1_ == GameSettings.Options.CHAT_COLOR) {
         this.field_74344_o = !this.field_74344_o;
      }

      if(p_74306_1_ == GameSettings.Options.CHAT_LINKS) {
         this.field_74359_p = !this.field_74359_p;
      }

      if(p_74306_1_ == GameSettings.Options.CHAT_LINKS_PROMPT) {
         this.field_74358_q = !this.field_74358_q;
      }

      if(p_74306_1_ == GameSettings.Options.SNOOPER_ENABLED) {
         this.field_74355_t = !this.field_74355_t;
      }

      if(p_74306_1_ == GameSettings.Options.TOUCHSCREEN) {
         this.field_85185_A = !this.field_85185_A;
      }

      if(p_74306_1_ == GameSettings.Options.USE_FULLSCREEN) {
         this.field_74353_u = !this.field_74353_u;
         if(this.field_74317_L.func_71372_G() != this.field_74353_u) {
            this.field_74317_L.func_71352_k();
         }
      }

      if(p_74306_1_ == GameSettings.Options.ENABLE_VSYNC) {
         this.field_74352_v = !this.field_74352_v;
         Display.setVSyncEnabled(this.field_74352_v);
      }

      if(p_74306_1_ == GameSettings.Options.USE_VBO) {
         this.field_178881_t = !this.field_178881_t;
         this.field_74317_L.field_71438_f.func_72712_a();
      }

      if(p_74306_1_ == GameSettings.Options.BLOCK_ALTERNATIVES) {
         this.field_178880_u = !this.field_178880_u;
         this.field_74317_L.field_71438_f.func_72712_a();
      }

      if(p_74306_1_ == GameSettings.Options.REDUCED_DEBUG_INFO) {
         this.field_178879_v = !this.field_178879_v;
      }

      if(p_74306_1_ == GameSettings.Options.ENTITY_SHADOWS) {
         this.field_181151_V = !this.field_181151_V;
      }

      this.func_74303_b();
   }

   public float func_74296_a(GameSettings.Options p_74296_1_) {
      return p_74296_1_ == GameSettings.Options.CLOUD_HEIGHT?this.ofCloudsHeight:(p_74296_1_ == GameSettings.Options.AO_LEVEL?this.ofAoLevel:(p_74296_1_ == GameSettings.Options.AA_LEVEL?(float)this.ofAaLevel:(p_74296_1_ == GameSettings.Options.AF_LEVEL?(float)this.ofAfLevel:(p_74296_1_ == GameSettings.Options.MIPMAP_TYPE?(float)this.ofMipmapType:(p_74296_1_ == GameSettings.Options.FRAMERATE_LIMIT?((float)this.field_74350_i == GameSettings.Options.FRAMERATE_LIMIT.func_148267_f() && this.field_74352_v?0.0F:(float)this.field_74350_i):(p_74296_1_ == GameSettings.Options.FOV?this.field_74334_X:(p_74296_1_ == GameSettings.Options.GAMMA?this.field_74333_Y:(p_74296_1_ == GameSettings.Options.SATURATION?this.field_151452_as:(p_74296_1_ == GameSettings.Options.SENSITIVITY?this.field_74341_c:(p_74296_1_ == GameSettings.Options.CHAT_OPACITY?this.field_74357_r:(p_74296_1_ == GameSettings.Options.CHAT_HEIGHT_FOCUSED?this.field_96694_H:(p_74296_1_ == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED?this.field_96693_G:(p_74296_1_ == GameSettings.Options.CHAT_SCALE?this.field_96691_E:(p_74296_1_ == GameSettings.Options.CHAT_WIDTH?this.field_96692_F:(p_74296_1_ == GameSettings.Options.FRAMERATE_LIMIT?(float)this.field_74350_i:(p_74296_1_ == GameSettings.Options.MIPMAP_LEVELS?(float)this.field_151442_I:(p_74296_1_ == GameSettings.Options.RENDER_DISTANCE?(float)this.field_151451_c:(p_74296_1_ == GameSettings.Options.STREAM_BYTES_PER_PIXEL?this.field_152400_J:(p_74296_1_ == GameSettings.Options.STREAM_VOLUME_MIC?this.field_152401_K:(p_74296_1_ == GameSettings.Options.STREAM_VOLUME_SYSTEM?this.field_152402_L:(p_74296_1_ == GameSettings.Options.STREAM_KBPS?this.field_152403_M:(p_74296_1_ == GameSettings.Options.STREAM_FPS?this.field_152404_N:0.0F))))))))))))))))))))));
   }

   public boolean func_74308_b(GameSettings.Options p_74308_1_) {
      switch(GameSettings.GameSettings$2.field_151477_a[p_74308_1_.ordinal()]) {
      case 1:
         return this.field_74338_d;
      case 2:
         return this.field_74336_f;
      case 3:
         return this.field_74337_g;
      case 4:
         return this.field_151448_g;
      case 5:
         return this.field_74344_o;
      case 6:
         return this.field_74359_p;
      case 7:
         return this.field_74358_q;
      case 8:
         return this.field_74355_t;
      case 9:
         return this.field_74353_u;
      case 10:
         return this.field_74352_v;
      case 11:
         return this.field_178881_t;
      case 12:
         return this.field_85185_A;
      case 13:
         return this.field_152406_P;
      case 14:
         return this.field_151455_aw;
      case 15:
         return this.field_178880_u;
      case 16:
         return this.field_178879_v;
      case 17:
         return this.field_181151_V;
      default:
         return false;
      }
   }

   private static String func_74299_a(String[] p_74299_0_, int p_74299_1_) {
      if(p_74299_1_ < 0 || p_74299_1_ >= p_74299_0_.length) {
         p_74299_1_ = 0;
      }

      return I18n.func_135052_a(p_74299_0_[p_74299_1_], new Object[0]);
   }

   public String func_74297_c(GameSettings.Options p_74297_1_) {
      String s = this.getKeyBindingOF(p_74297_1_);
      if(s != null) {
         return s;
      } else {
         String s1 = I18n.func_135052_a(p_74297_1_.func_74378_d(), new Object[0]) + ": ";
         if(p_74297_1_.func_74380_a()) {
            float f1 = this.func_74296_a(p_74297_1_);
            float f = p_74297_1_.func_148266_c(f1);
            return p_74297_1_ == GameSettings.Options.SENSITIVITY?(f == 0.0F?s1 + I18n.func_135052_a("options.sensitivity.min", new Object[0]):(f == 1.0F?s1 + I18n.func_135052_a("options.sensitivity.max", new Object[0]):s1 + (int)(f * 200.0F) + "%")):(p_74297_1_ == GameSettings.Options.FOV?(f1 == 70.0F?s1 + I18n.func_135052_a("options.fov.min", new Object[0]):(f1 == 110.0F?s1 + I18n.func_135052_a("options.fov.max", new Object[0]):s1 + (int)f1)):(p_74297_1_ == GameSettings.Options.FRAMERATE_LIMIT?(f1 == p_74297_1_.field_148272_O?s1 + I18n.func_135052_a("options.framerateLimit.max", new Object[0]):s1 + (int)f1 + " fps"):(p_74297_1_ == GameSettings.Options.RENDER_CLOUDS?(f1 == p_74297_1_.field_148271_N?s1 + I18n.func_135052_a("options.cloudHeight.min", new Object[0]):s1 + ((int)f1 + 128)):(p_74297_1_ == GameSettings.Options.GAMMA?(f == 0.0F?s1 + I18n.func_135052_a("options.gamma.min", new Object[0]):(f == 1.0F?s1 + I18n.func_135052_a("options.gamma.max", new Object[0]):s1 + "+" + (int)(f * 100.0F) + "%")):(p_74297_1_ == GameSettings.Options.SATURATION?s1 + (int)(f * 400.0F) + "%":(p_74297_1_ == GameSettings.Options.CHAT_OPACITY?s1 + (int)(f * 90.0F + 10.0F) + "%":(p_74297_1_ == GameSettings.Options.CHAT_HEIGHT_UNFOCUSED?s1 + GuiNewChat.func_146243_b(f) + "px":(p_74297_1_ == GameSettings.Options.CHAT_HEIGHT_FOCUSED?s1 + GuiNewChat.func_146243_b(f) + "px":(p_74297_1_ == GameSettings.Options.CHAT_WIDTH?s1 + GuiNewChat.func_146233_a(f) + "px":(p_74297_1_ == GameSettings.Options.RENDER_DISTANCE?s1 + (int)f1 + " chunks":(p_74297_1_ == GameSettings.Options.MIPMAP_LEVELS?(f1 == 0.0F?s1 + I18n.func_135052_a("options.off", new Object[0]):s1 + (int)f1):(p_74297_1_ == GameSettings.Options.STREAM_FPS?s1 + TwitchStream.func_152948_a(f) + " fps":(p_74297_1_ == GameSettings.Options.STREAM_KBPS?s1 + TwitchStream.func_152946_b(f) + " Kbps":(p_74297_1_ == GameSettings.Options.STREAM_BYTES_PER_PIXEL?s1 + String.format("%.3f bpp", new Object[]{Float.valueOf(TwitchStream.func_152947_c(f))}):(f == 0.0F?s1 + I18n.func_135052_a("options.off", new Object[0]):s1 + (int)(f * 100.0F) + "%")))))))))))))));
         } else if(p_74297_1_.func_74382_b()) {
            boolean flag = this.func_74308_b(p_74297_1_);
            return flag?s1 + I18n.func_135052_a("options.on", new Object[0]):s1 + I18n.func_135052_a("options.off", new Object[0]);
         } else if(p_74297_1_ == GameSettings.Options.GUI_SCALE) {
            return s1 + func_74299_a(field_74367_ae, this.field_74335_Z);
         } else if(p_74297_1_ == GameSettings.Options.CHAT_VISIBILITY) {
            return s1 + I18n.func_135052_a(this.field_74343_n.func_151429_b(), new Object[0]);
         } else if(p_74297_1_ == GameSettings.Options.PARTICLES) {
            return s1 + func_74299_a(field_74364_ag, this.field_74362_aa);
         } else if(p_74297_1_ == GameSettings.Options.AMBIENT_OCCLUSION) {
            return s1 + func_74299_a(field_98303_au, this.field_74348_k);
         } else if(p_74297_1_ == GameSettings.Options.STREAM_COMPRESSION) {
            return s1 + func_74299_a(field_152391_aS, this.field_152405_O);
         } else if(p_74297_1_ == GameSettings.Options.STREAM_CHAT_ENABLED) {
            return s1 + func_74299_a(field_152392_aT, this.field_152408_R);
         } else if(p_74297_1_ == GameSettings.Options.STREAM_CHAT_USER_FILTER) {
            return s1 + func_74299_a(field_152393_aU, this.field_152409_S);
         } else if(p_74297_1_ == GameSettings.Options.STREAM_MIC_TOGGLE_BEHAVIOR) {
            return s1 + func_74299_a(field_152394_aV, this.field_152410_T);
         } else if(p_74297_1_ == GameSettings.Options.RENDER_CLOUDS) {
            return s1 + func_74299_a(field_181149_aW, this.field_74345_l);
         } else if(p_74297_1_ == GameSettings.Options.GRAPHICS) {
            if(this.field_74347_j) {
               return s1 + I18n.func_135052_a("options.graphics.fancy", new Object[0]);
            } else {
               String s2 = "options.graphics.fast";
               return s1 + I18n.func_135052_a("options.graphics.fast", new Object[0]);
            }
         } else {
            return s1;
         }
      }
   }

   public void func_74300_a() {
      try {
         if(!this.field_74354_ai.exists()) {
            return;
         }

         BufferedReader bufferedreader = new BufferedReader(new FileReader(this.field_74354_ai));
         String s = "";
         this.field_151446_aD.clear();

         while((s = bufferedreader.readLine()) != null) {
            try {
               String[] astring = s.split(":");
               if(astring[0].equals("mouseSensitivity")) {
                  this.field_74341_c = this.func_74305_a(astring[1]);
               }

               if(astring[0].equals("fov")) {
                  this.field_74334_X = this.func_74305_a(astring[1]) * 40.0F + 70.0F;
               }

               if(astring[0].equals("gamma")) {
                  this.field_74333_Y = this.func_74305_a(astring[1]);
               }

               if(astring[0].equals("saturation")) {
                  this.field_151452_as = this.func_74305_a(astring[1]);
               }

               if(astring[0].equals("invertYMouse")) {
                  this.field_74338_d = astring[1].equals("true");
               }

               if(astring[0].equals("renderDistance")) {
                  this.field_151451_c = Integer.parseInt(astring[1]);
               }

               if(astring[0].equals("guiScale")) {
                  this.field_74335_Z = Integer.parseInt(astring[1]);
               }

               if(astring[0].equals("particles")) {
                  this.field_74362_aa = Integer.parseInt(astring[1]);
               }

               if(astring[0].equals("bobView")) {
                  this.field_74336_f = astring[1].equals("true");
               }

               if(astring[0].equals("anaglyph3d")) {
                  this.field_74337_g = astring[1].equals("true");
               }

               if(astring[0].equals("maxFps")) {
                  this.field_74350_i = Integer.parseInt(astring[1]);
                  this.field_74352_v = false;
                  if(this.field_74350_i <= 0) {
                     this.field_74350_i = (int)GameSettings.Options.FRAMERATE_LIMIT.func_148267_f();
                     this.field_74352_v = true;
                  }

                  this.updateVSync();
               }

               if(astring[0].equals("fboEnable")) {
                  this.field_151448_g = astring[1].equals("true");
               }

               if(astring[0].equals("difficulty")) {
                  this.field_74318_M = EnumDifficulty.func_151523_a(Integer.parseInt(astring[1]));
               }

               if(astring[0].equals("fancyGraphics")) {
                  this.field_74347_j = astring[1].equals("true");
                  this.updateRenderClouds();
               }

               if(astring[0].equals("ao")) {
                  if(astring[1].equals("true")) {
                     this.field_74348_k = 2;
                  } else if(astring[1].equals("false")) {
                     this.field_74348_k = 0;
                  } else {
                     this.field_74348_k = Integer.parseInt(astring[1]);
                  }
               }

               if(astring[0].equals("renderClouds")) {
                  if(astring[1].equals("true")) {
                     this.field_74345_l = 2;
                  } else if(astring[1].equals("false")) {
                     this.field_74345_l = 0;
                  } else if(astring[1].equals("fast")) {
                     this.field_74345_l = 1;
                  }
               }

               if(astring[0].equals("resourcePacks")) {
                  this.field_151453_l = (List)field_151450_ay.fromJson((String)s.substring(s.indexOf(58) + 1), field_151449_az);
                  if(this.field_151453_l == null) {
                     this.field_151453_l = Lists.newArrayList();
                  }
               }

               if(astring[0].equals("incompatibleResourcePacks")) {
                  this.field_183018_l = (List)field_151450_ay.fromJson((String)s.substring(s.indexOf(58) + 1), field_151449_az);
                  if(this.field_183018_l == null) {
                     this.field_183018_l = Lists.newArrayList();
                  }
               }

               if(astring[0].equals("lastServer") && astring.length >= 2) {
                  this.field_74332_R = s.substring(s.indexOf(58) + 1);
               }

               if(astring[0].equals("lang") && astring.length >= 2) {
                  this.field_74363_ab = astring[1];
               }

               if(astring[0].equals("chatVisibility")) {
                  this.field_74343_n = EntityPlayer.EnumChatVisibility.func_151426_a(Integer.parseInt(astring[1]));
               }

               if(astring[0].equals("chatColors")) {
                  this.field_74344_o = astring[1].equals("true");
               }

               if(astring[0].equals("chatLinks")) {
                  this.field_74359_p = astring[1].equals("true");
               }

               if(astring[0].equals("chatLinksPrompt")) {
                  this.field_74358_q = astring[1].equals("true");
               }

               if(astring[0].equals("chatOpacity")) {
                  this.field_74357_r = this.func_74305_a(astring[1]);
               }

               if(astring[0].equals("snooperEnabled")) {
                  this.field_74355_t = astring[1].equals("true");
               }

               if(astring[0].equals("fullscreen")) {
                  this.field_74353_u = astring[1].equals("true");
               }

               if(astring[0].equals("enableVsync")) {
                  this.field_74352_v = astring[1].equals("true");
                  this.updateVSync();
               }

               if(astring[0].equals("useVbo")) {
                  this.field_178881_t = astring[1].equals("true");
               }

               if(astring[0].equals("hideServerAddress")) {
                  this.field_80005_w = astring[1].equals("true");
               }

               if(astring[0].equals("advancedItemTooltips")) {
                  this.field_82882_x = astring[1].equals("true");
               }

               if(astring[0].equals("pauseOnLostFocus")) {
                  this.field_82881_y = astring[1].equals("true");
               }

               if(astring[0].equals("touchscreen")) {
                  this.field_85185_A = astring[1].equals("true");
               }

               if(astring[0].equals("overrideHeight")) {
                  this.field_92119_C = Integer.parseInt(astring[1]);
               }

               if(astring[0].equals("overrideWidth")) {
                  this.field_92118_B = Integer.parseInt(astring[1]);
               }

               if(astring[0].equals("heldItemTooltips")) {
                  this.field_92117_D = astring[1].equals("true");
               }

               if(astring[0].equals("chatHeightFocused")) {
                  this.field_96694_H = this.func_74305_a(astring[1]);
               }

               if(astring[0].equals("chatHeightUnfocused")) {
                  this.field_96693_G = this.func_74305_a(astring[1]);
               }

               if(astring[0].equals("chatScale")) {
                  this.field_96691_E = this.func_74305_a(astring[1]);
               }

               if(astring[0].equals("chatWidth")) {
                  this.field_96692_F = this.func_74305_a(astring[1]);
               }

               if(astring[0].equals("showInventoryAchievementHint")) {
                  this.field_151441_H = astring[1].equals("true");
               }

               if(astring[0].equals("mipmapLevels")) {
                  this.field_151442_I = Integer.parseInt(astring[1]);
               }

               if(astring[0].equals("streamBytesPerPixel")) {
                  this.field_152400_J = this.func_74305_a(astring[1]);
               }

               if(astring[0].equals("streamMicVolume")) {
                  this.field_152401_K = this.func_74305_a(astring[1]);
               }

               if(astring[0].equals("streamSystemVolume")) {
                  this.field_152402_L = this.func_74305_a(astring[1]);
               }

               if(astring[0].equals("streamKbps")) {
                  this.field_152403_M = this.func_74305_a(astring[1]);
               }

               if(astring[0].equals("streamFps")) {
                  this.field_152404_N = this.func_74305_a(astring[1]);
               }

               if(astring[0].equals("streamCompression")) {
                  this.field_152405_O = Integer.parseInt(astring[1]);
               }

               if(astring[0].equals("streamSendMetadata")) {
                  this.field_152406_P = astring[1].equals("true");
               }

               if(astring[0].equals("streamPreferredServer") && astring.length >= 2) {
                  this.field_152407_Q = s.substring(s.indexOf(58) + 1);
               }

               if(astring[0].equals("streamChatEnabled")) {
                  this.field_152408_R = Integer.parseInt(astring[1]);
               }

               if(astring[0].equals("streamChatUserFilter")) {
                  this.field_152409_S = Integer.parseInt(astring[1]);
               }

               if(astring[0].equals("streamMicToggleBehavior")) {
                  this.field_152410_T = Integer.parseInt(astring[1]);
               }

               if(astring[0].equals("forceUnicodeFont")) {
                  this.field_151455_aw = astring[1].equals("true");
               }

               if(astring[0].equals("allowBlockAlternatives")) {
                  this.field_178880_u = astring[1].equals("true");
               }

               if(astring[0].equals("reducedDebugInfo")) {
                  this.field_178879_v = astring[1].equals("true");
               }

               if(astring[0].equals("useNativeTransport")) {
                  this.field_181150_U = astring[1].equals("true");
               }

               if(astring[0].equals("entityShadows")) {
                  this.field_181151_V = astring[1].equals("true");
               }

               for(KeyBinding keybinding : this.field_74324_K) {
                  if(astring[0].equals("key_" + keybinding.func_151464_g())) {
                     keybinding.func_151462_b(Integer.parseInt(astring[1]));
                  }
               }

               for(SoundCategory soundcategory : SoundCategory.values()) {
                  if(astring[0].equals("soundCategory_" + soundcategory.func_147155_a())) {
                     this.field_151446_aD.put(soundcategory, Float.valueOf(this.func_74305_a(astring[1])));
                  }
               }

               for(EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts.values()) {
                  if(astring[0].equals("modelPart_" + enumplayermodelparts.func_179329_c())) {
                     this.func_178878_a(enumplayermodelparts, astring[1].equals("true"));
                  }
               }
            } catch (Exception exception) {
               field_151454_ax.warn("Skipping bad option: " + s);
               exception.printStackTrace();
            }
         }

         KeyBinding.func_74508_b();
         bufferedreader.close();
      } catch (Exception exception1) {
         field_151454_ax.error((String)"Failed to load options", (Throwable)exception1);
      }

      this.loadOfOptions();
   }

   private float func_74305_a(String p_74305_1_) {
      return p_74305_1_.equals("true")?1.0F:(p_74305_1_.equals("false")?0.0F:Float.parseFloat(p_74305_1_));
   }

   public void func_74303_b() {
      if(Reflector.FMLClientHandler.exists()) {
         Object object = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
         if(object != null && Reflector.callBoolean(object, Reflector.FMLClientHandler_isLoading, new Object[0])) {
            return;
         }
      }

      try {
         PrintWriter printwriter = new PrintWriter(new FileWriter(this.field_74354_ai));
         printwriter.println("invertYMouse:" + this.field_74338_d);
         printwriter.println("mouseSensitivity:" + this.field_74341_c);
         printwriter.println("fov:" + (this.field_74334_X - 70.0F) / 40.0F);
         printwriter.println("gamma:" + this.field_74333_Y);
         printwriter.println("saturation:" + this.field_151452_as);
         printwriter.println("renderDistance:" + this.field_151451_c);
         printwriter.println("guiScale:" + this.field_74335_Z);
         printwriter.println("particles:" + this.field_74362_aa);
         printwriter.println("bobView:" + this.field_74336_f);
         printwriter.println("anaglyph3d:" + this.field_74337_g);
         printwriter.println("maxFps:" + this.field_74350_i);
         printwriter.println("fboEnable:" + this.field_151448_g);
         printwriter.println("difficulty:" + this.field_74318_M.func_151525_a());
         printwriter.println("fancyGraphics:" + this.field_74347_j);
         printwriter.println("ao:" + this.field_74348_k);
         switch(this.field_74345_l) {
         case 0:
            printwriter.println("renderClouds:false");
            break;
         case 1:
            printwriter.println("renderClouds:fast");
            break;
         case 2:
            printwriter.println("renderClouds:true");
         }

         printwriter.println("resourcePacks:" + field_151450_ay.toJson((Object)this.field_151453_l));
         printwriter.println("incompatibleResourcePacks:" + field_151450_ay.toJson((Object)this.field_183018_l));
         printwriter.println("lastServer:" + this.field_74332_R);
         printwriter.println("lang:" + this.field_74363_ab);
         printwriter.println("chatVisibility:" + this.field_74343_n.func_151428_a());
         printwriter.println("chatColors:" + this.field_74344_o);
         printwriter.println("chatLinks:" + this.field_74359_p);
         printwriter.println("chatLinksPrompt:" + this.field_74358_q);
         printwriter.println("chatOpacity:" + this.field_74357_r);
         printwriter.println("snooperEnabled:" + this.field_74355_t);
         printwriter.println("fullscreen:" + this.field_74353_u);
         printwriter.println("enableVsync:" + this.field_74352_v);
         printwriter.println("useVbo:" + this.field_178881_t);
         printwriter.println("hideServerAddress:" + this.field_80005_w);
         printwriter.println("advancedItemTooltips:" + this.field_82882_x);
         printwriter.println("pauseOnLostFocus:" + this.field_82881_y);
         printwriter.println("touchscreen:" + this.field_85185_A);
         printwriter.println("overrideWidth:" + this.field_92118_B);
         printwriter.println("overrideHeight:" + this.field_92119_C);
         printwriter.println("heldItemTooltips:" + this.field_92117_D);
         printwriter.println("chatHeightFocused:" + this.field_96694_H);
         printwriter.println("chatHeightUnfocused:" + this.field_96693_G);
         printwriter.println("chatScale:" + this.field_96691_E);
         printwriter.println("chatWidth:" + this.field_96692_F);
         printwriter.println("showInventoryAchievementHint:" + this.field_151441_H);
         printwriter.println("mipmapLevels:" + this.field_151442_I);
         printwriter.println("streamBytesPerPixel:" + this.field_152400_J);
         printwriter.println("streamMicVolume:" + this.field_152401_K);
         printwriter.println("streamSystemVolume:" + this.field_152402_L);
         printwriter.println("streamKbps:" + this.field_152403_M);
         printwriter.println("streamFps:" + this.field_152404_N);
         printwriter.println("streamCompression:" + this.field_152405_O);
         printwriter.println("streamSendMetadata:" + this.field_152406_P);
         printwriter.println("streamPreferredServer:" + this.field_152407_Q);
         printwriter.println("streamChatEnabled:" + this.field_152408_R);
         printwriter.println("streamChatUserFilter:" + this.field_152409_S);
         printwriter.println("streamMicToggleBehavior:" + this.field_152410_T);
         printwriter.println("forceUnicodeFont:" + this.field_151455_aw);
         printwriter.println("allowBlockAlternatives:" + this.field_178880_u);
         printwriter.println("reducedDebugInfo:" + this.field_178879_v);
         printwriter.println("useNativeTransport:" + this.field_181150_U);
         printwriter.println("entityShadows:" + this.field_181151_V);

         for(KeyBinding keybinding : this.field_74324_K) {
            printwriter.println("key_" + keybinding.func_151464_g() + ":" + keybinding.func_151463_i());
         }

         for(SoundCategory soundcategory : SoundCategory.values()) {
            printwriter.println("soundCategory_" + soundcategory.func_147155_a() + ":" + this.func_151438_a(soundcategory));
         }

         for(EnumPlayerModelParts enumplayermodelparts : EnumPlayerModelParts.values()) {
            printwriter.println("modelPart_" + enumplayermodelparts.func_179329_c() + ":" + this.field_178882_aU.contains(enumplayermodelparts));
         }

         printwriter.close();
      } catch (Exception exception) {
         field_151454_ax.error((String)"Failed to save options", (Throwable)exception);
      }

      this.saveOfOptions();
      this.func_82879_c();
   }

   public float func_151438_a(SoundCategory p_151438_1_) {
      return this.field_151446_aD.containsKey(p_151438_1_)?((Float)this.field_151446_aD.get(p_151438_1_)).floatValue():1.0F;
   }

   public void func_151439_a(SoundCategory p_151439_1_, float p_151439_2_) {
      this.field_74317_L.func_147118_V().func_147684_a(p_151439_1_, p_151439_2_);
      this.field_151446_aD.put(p_151439_1_, Float.valueOf(p_151439_2_));
   }

   public void func_82879_c() {
      if(this.field_74317_L.field_71439_g != null) {
         int i = 0;

         for(EnumPlayerModelParts enumplayermodelparts : this.field_178882_aU) {
            i |= enumplayermodelparts.func_179327_a();
         }

         this.field_74317_L.field_71439_g.field_71174_a.func_147297_a(new C15PacketClientSettings(this.field_74363_ab, this.field_151451_c, this.field_74343_n, this.field_74344_o, i));
      }

   }

   public Set func_178876_d() {
      return ImmutableSet.copyOf(this.field_178882_aU);
   }

   public void func_178878_a(EnumPlayerModelParts p_178878_1_, boolean p_178878_2_) {
      if(p_178878_2_) {
         this.field_178882_aU.add(p_178878_1_);
      } else {
         this.field_178882_aU.remove(p_178878_1_);
      }

      this.func_82879_c();
   }

   public void func_178877_a(EnumPlayerModelParts p_178877_1_) {
      if(!this.func_178876_d().contains(p_178877_1_)) {
         this.field_178882_aU.add(p_178877_1_);
      } else {
         this.field_178882_aU.remove(p_178877_1_);
      }

      this.func_82879_c();
   }

   public int func_181147_e() {
      return this.field_151451_c >= 4?this.field_74345_l:0;
   }

   public boolean func_181148_f() {
      return this.field_181150_U;
   }

   private void setOptionFloatValueOF(GameSettings.Options p_setOptionFloatValueOF_1_, float p_setOptionFloatValueOF_2_) {
      if(p_setOptionFloatValueOF_1_ == GameSettings.Options.CLOUD_HEIGHT) {
         this.ofCloudsHeight = p_setOptionFloatValueOF_2_;
         this.field_74317_L.field_71438_f.resetClouds();
      }

      if(p_setOptionFloatValueOF_1_ == GameSettings.Options.AO_LEVEL) {
         this.ofAoLevel = p_setOptionFloatValueOF_2_;
         this.field_74317_L.field_71438_f.func_72712_a();
      }

      if(p_setOptionFloatValueOF_1_ == GameSettings.Options.AA_LEVEL) {
         int i = (int)p_setOptionFloatValueOF_2_;
         if(i > 0 && Config.isShaders()) {
            Config.showGuiMessage(Lang.get("of.message.aa.shaders1"), Lang.get("of.message.aa.shaders2"));
            return;
         }

         int[] aint = new int[]{0, 2, 4, 6, 8, 12, 16};
         this.ofAaLevel = 0;

         for(int j = 0; j < aint.length; ++j) {
            if(i >= aint[j]) {
               this.ofAaLevel = aint[j];
            }
         }

         this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
      }

      if(p_setOptionFloatValueOF_1_ == GameSettings.Options.AF_LEVEL) {
         int k = (int)p_setOptionFloatValueOF_2_;
         if(k > 1 && Config.isShaders()) {
            Config.showGuiMessage(Lang.get("of.message.af.shaders1"), Lang.get("of.message.af.shaders2"));
            return;
         }

         for(this.ofAfLevel = 1; this.ofAfLevel * 2 <= k; this.ofAfLevel *= 2) {
            ;
         }

         this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
         this.field_74317_L.func_110436_a();
      }

      if(p_setOptionFloatValueOF_1_ == GameSettings.Options.MIPMAP_TYPE) {
         int l = (int)p_setOptionFloatValueOF_2_;
         this.ofMipmapType = Config.limit(l, 0, 3);
         this.field_74317_L.func_110436_a();
      }

   }

   private void setOptionValueOF(GameSettings.Options p_setOptionValueOF_1_, int p_setOptionValueOF_2_) {
      if(p_setOptionValueOF_1_ == GameSettings.Options.FOG_FANCY) {
         switch(this.ofFogType) {
         case 1:
            this.ofFogType = 2;
            if(!Config.isFancyFogAvailable()) {
               this.ofFogType = 3;
            }
            break;
         case 2:
            this.ofFogType = 3;
            break;
         case 3:
            this.ofFogType = 1;
            break;
         default:
            this.ofFogType = 1;
         }
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.FOG_START) {
         this.ofFogStart += 0.2F;
         if(this.ofFogStart > 0.81F) {
            this.ofFogStart = 0.2F;
         }
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.SMOOTH_FPS) {
         this.ofSmoothFps = !this.ofSmoothFps;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.SMOOTH_WORLD) {
         this.ofSmoothWorld = !this.ofSmoothWorld;
         Config.updateThreadPriorities();
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.CLOUDS) {
         ++this.ofClouds;
         if(this.ofClouds > 3) {
            this.ofClouds = 0;
         }

         this.updateRenderClouds();
         this.field_74317_L.field_71438_f.resetClouds();
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.TREES) {
         this.ofTrees = nextValue(this.ofTrees, OF_TREES_VALUES);
         this.field_74317_L.field_71438_f.func_72712_a();
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.DROPPED_ITEMS) {
         ++this.ofDroppedItems;
         if(this.ofDroppedItems > 2) {
            this.ofDroppedItems = 0;
         }
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.RAIN) {
         ++this.ofRain;
         if(this.ofRain > 3) {
            this.ofRain = 0;
         }
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.ANIMATED_WATER) {
         ++this.ofAnimatedWater;
         if(this.ofAnimatedWater == 1) {
            ++this.ofAnimatedWater;
         }

         if(this.ofAnimatedWater > 2) {
            this.ofAnimatedWater = 0;
         }
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.ANIMATED_LAVA) {
         ++this.ofAnimatedLava;
         if(this.ofAnimatedLava == 1) {
            ++this.ofAnimatedLava;
         }

         if(this.ofAnimatedLava > 2) {
            this.ofAnimatedLava = 0;
         }
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.ANIMATED_FIRE) {
         this.ofAnimatedFire = !this.ofAnimatedFire;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.ANIMATED_PORTAL) {
         this.ofAnimatedPortal = !this.ofAnimatedPortal;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.ANIMATED_REDSTONE) {
         this.ofAnimatedRedstone = !this.ofAnimatedRedstone;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.ANIMATED_EXPLOSION) {
         this.ofAnimatedExplosion = !this.ofAnimatedExplosion;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.ANIMATED_FLAME) {
         this.ofAnimatedFlame = !this.ofAnimatedFlame;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.ANIMATED_SMOKE) {
         this.ofAnimatedSmoke = !this.ofAnimatedSmoke;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.VOID_PARTICLES) {
         this.ofVoidParticles = !this.ofVoidParticles;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.WATER_PARTICLES) {
         this.ofWaterParticles = !this.ofWaterParticles;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.PORTAL_PARTICLES) {
         this.ofPortalParticles = !this.ofPortalParticles;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.POTION_PARTICLES) {
         this.ofPotionParticles = !this.ofPotionParticles;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.FIREWORK_PARTICLES) {
         this.ofFireworkParticles = !this.ofFireworkParticles;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.DRIPPING_WATER_LAVA) {
         this.ofDrippingWaterLava = !this.ofDrippingWaterLava;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.ANIMATED_TERRAIN) {
         this.ofAnimatedTerrain = !this.ofAnimatedTerrain;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.ANIMATED_TEXTURES) {
         this.ofAnimatedTextures = !this.ofAnimatedTextures;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.RAIN_SPLASH) {
         this.ofRainSplash = !this.ofRainSplash;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.LAGOMETER) {
         this.ofLagometer = !this.ofLagometer;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.SHOW_FPS) {
         this.ofShowFps = !this.ofShowFps;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.AUTOSAVE_TICKS) {
         this.ofAutoSaveTicks *= 10;
         if(this.ofAutoSaveTicks > '\u9c40') {
            this.ofAutoSaveTicks = 40;
         }
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.BETTER_GRASS) {
         ++this.ofBetterGrass;
         if(this.ofBetterGrass > 3) {
            this.ofBetterGrass = 1;
         }

         this.field_74317_L.field_71438_f.func_72712_a();
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.CONNECTED_TEXTURES) {
         ++this.ofConnectedTextures;
         if(this.ofConnectedTextures > 3) {
            this.ofConnectedTextures = 1;
         }

         if(this.ofConnectedTextures == 2) {
            this.field_74317_L.field_71438_f.func_72712_a();
         } else {
            this.field_74317_L.func_110436_a();
         }
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.WEATHER) {
         this.ofWeather = !this.ofWeather;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.SKY) {
         this.ofSky = !this.ofSky;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.STARS) {
         this.ofStars = !this.ofStars;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.SUN_MOON) {
         this.ofSunMoon = !this.ofSunMoon;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.VIGNETTE) {
         ++this.ofVignette;
         if(this.ofVignette > 2) {
            this.ofVignette = 0;
         }
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.CHUNK_UPDATES) {
         ++this.ofChunkUpdates;
         if(this.ofChunkUpdates > 5) {
            this.ofChunkUpdates = 1;
         }
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.CHUNK_UPDATES_DYNAMIC) {
         this.ofChunkUpdatesDynamic = !this.ofChunkUpdatesDynamic;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.TIME) {
         ++this.ofTime;
         if(this.ofTime > 2) {
            this.ofTime = 0;
         }
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.CLEAR_WATER) {
         this.ofClearWater = !this.ofClearWater;
         this.updateWaterOpacity();
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.PROFILER) {
         this.ofProfiler = !this.ofProfiler;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.BETTER_SNOW) {
         this.ofBetterSnow = !this.ofBetterSnow;
         this.field_74317_L.field_71438_f.func_72712_a();
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.SWAMP_COLORS) {
         this.ofSwampColors = !this.ofSwampColors;
         CustomColors.updateUseDefaultGrassFoliageColors();
         this.field_74317_L.field_71438_f.func_72712_a();
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.RANDOM_MOBS) {
         this.ofRandomMobs = !this.ofRandomMobs;
         RandomMobs.resetTextures();
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.SMOOTH_BIOMES) {
         this.ofSmoothBiomes = !this.ofSmoothBiomes;
         CustomColors.updateUseDefaultGrassFoliageColors();
         this.field_74317_L.field_71438_f.func_72712_a();
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.CUSTOM_FONTS) {
         this.ofCustomFonts = !this.ofCustomFonts;
         this.field_74317_L.field_71466_p.func_110549_a(Config.getResourceManager());
         this.field_74317_L.field_71464_q.func_110549_a(Config.getResourceManager());
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.CUSTOM_COLORS) {
         this.ofCustomColors = !this.ofCustomColors;
         CustomColors.update();
         this.field_74317_L.field_71438_f.func_72712_a();
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.CUSTOM_ITEMS) {
         this.ofCustomItems = !this.ofCustomItems;
         this.field_74317_L.func_110436_a();
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.CUSTOM_SKY) {
         this.ofCustomSky = !this.ofCustomSky;
         CustomSky.update();
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.SHOW_CAPES) {
         this.ofShowCapes = !this.ofShowCapes;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.NATURAL_TEXTURES) {
         this.ofNaturalTextures = !this.ofNaturalTextures;
         NaturalTextures.update();
         this.field_74317_L.field_71438_f.func_72712_a();
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.FAST_MATH) {
         this.ofFastMath = !this.ofFastMath;
         MathHelper.fastMath = this.ofFastMath;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.FAST_RENDER) {
         if(!this.ofFastRender && Config.isShaders()) {
            Config.showGuiMessage(Lang.get("of.message.fr.shaders1"), Lang.get("of.message.fr.shaders2"));
            return;
         }

         this.ofFastRender = !this.ofFastRender;
         if(this.ofFastRender) {
            this.field_74317_L.field_71460_t.func_181022_b();
         }

         Config.updateFramebufferSize();
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.TRANSLUCENT_BLOCKS) {
         if(this.ofTranslucentBlocks == 0) {
            this.ofTranslucentBlocks = 1;
         } else if(this.ofTranslucentBlocks == 1) {
            this.ofTranslucentBlocks = 2;
         } else if(this.ofTranslucentBlocks == 2) {
            this.ofTranslucentBlocks = 0;
         } else {
            this.ofTranslucentBlocks = 0;
         }

         this.field_74317_L.field_71438_f.func_72712_a();
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.LAZY_CHUNK_LOADING) {
         this.ofLazyChunkLoading = !this.ofLazyChunkLoading;
         Config.updateAvailableProcessors();
         if(!Config.isSingleProcessor()) {
            this.ofLazyChunkLoading = false;
         }

         this.field_74317_L.field_71438_f.func_72712_a();
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.FULLSCREEN_MODE) {
         List list = Arrays.asList(Config.getDisplayModeNames());
         if(this.ofFullscreenMode.equals("Default")) {
            this.ofFullscreenMode = (String)list.get(0);
         } else {
            int i = list.indexOf(this.ofFullscreenMode);
            if(i < 0) {
               this.ofFullscreenMode = "Default";
            } else {
               ++i;
               if(i >= list.size()) {
                  this.ofFullscreenMode = "Default";
               } else {
                  this.ofFullscreenMode = (String)list.get(i);
               }
            }
         }
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.DYNAMIC_FOV) {
         this.ofDynamicFov = !this.ofDynamicFov;
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.DYNAMIC_LIGHTS) {
         this.ofDynamicLights = nextValue(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
         DynamicLights.removeLights(this.field_74317_L.field_71438_f);
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.CUSTOM_GUIS) {
         this.ofCustomGuis = !this.ofCustomGuis;
         CustomGuis.update();
      }

      if(p_setOptionValueOF_1_ == GameSettings.Options.HELD_ITEM_TOOLTIPS) {
         this.field_92117_D = !this.field_92117_D;
      }

   }

   private String getKeyBindingOF(GameSettings.Options p_getKeyBindingOF_1_) {
      String s = I18n.func_135052_a(p_getKeyBindingOF_1_.func_74378_d(), new Object[0]) + ": ";
      if(s == null) {
         s = p_getKeyBindingOF_1_.func_74378_d();
      }

      if(p_getKeyBindingOF_1_ == GameSettings.Options.RENDER_DISTANCE) {
         int l = (int)this.func_74296_a(p_getKeyBindingOF_1_);
         String s1 = I18n.func_135052_a("options.renderDistance.tiny", new Object[0]);
         int i = 2;
         if(l >= 4) {
            s1 = I18n.func_135052_a("options.renderDistance.short", new Object[0]);
            i = 4;
         }

         if(l >= 8) {
            s1 = I18n.func_135052_a("options.renderDistance.normal", new Object[0]);
            i = 8;
         }

         if(l >= 16) {
            s1 = I18n.func_135052_a("options.renderDistance.far", new Object[0]);
            i = 16;
         }

         if(l >= 32) {
            s1 = Lang.get("of.options.renderDistance.extreme");
            i = 32;
         }

         if(l >= 48) {
            s1 = Lang.get("of.options.renderDistance.insane");
            i = 48;
         }

         if(l >= 64) {
            s1 = Lang.get("of.options.renderDistance.ludicrous");
            i = 64;
         }

         int j = this.field_151451_c - i;
         String s2 = s1;
         if(j > 0) {
            s2 = s1 + "+";
         }

         return s + l + " " + s2 + "";
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.FOG_FANCY) {
         switch(this.ofFogType) {
         case 1:
            return s + Lang.getFast();
         case 2:
            return s + Lang.getFancy();
         case 3:
            return s + Lang.getOff();
         default:
            return s + Lang.getOff();
         }
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.FOG_START) {
         return s + this.ofFogStart;
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.MIPMAP_TYPE) {
         switch(this.ofMipmapType) {
         case 0:
            return s + Lang.get("of.options.mipmap.nearest");
         case 1:
            return s + Lang.get("of.options.mipmap.linear");
         case 2:
            return s + Lang.get("of.options.mipmap.bilinear");
         case 3:
            return s + Lang.get("of.options.mipmap.trilinear");
         default:
            return s + "of.options.mipmap.nearest";
         }
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.SMOOTH_FPS) {
         return this.ofSmoothFps?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.SMOOTH_WORLD) {
         return this.ofSmoothWorld?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.CLOUDS) {
         switch(this.ofClouds) {
         case 1:
            return s + Lang.getFast();
         case 2:
            return s + Lang.getFancy();
         case 3:
            return s + Lang.getOff();
         default:
            return s + Lang.getDefault();
         }
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.TREES) {
         switch(this.ofTrees) {
         case 1:
            return s + Lang.getFast();
         case 2:
            return s + Lang.getFancy();
         case 3:
         default:
            return s + Lang.getDefault();
         case 4:
            return s + Lang.get("of.general.smart");
         }
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.DROPPED_ITEMS) {
         switch(this.ofDroppedItems) {
         case 1:
            return s + Lang.getFast();
         case 2:
            return s + Lang.getFancy();
         default:
            return s + Lang.getDefault();
         }
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.RAIN) {
         switch(this.ofRain) {
         case 1:
            return s + Lang.getFast();
         case 2:
            return s + Lang.getFancy();
         case 3:
            return s + Lang.getOff();
         default:
            return s + Lang.getDefault();
         }
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.ANIMATED_WATER) {
         switch(this.ofAnimatedWater) {
         case 1:
            return s + Lang.get("of.options.animation.dynamic");
         case 2:
            return s + Lang.getOff();
         default:
            return s + Lang.getOn();
         }
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.ANIMATED_LAVA) {
         switch(this.ofAnimatedLava) {
         case 1:
            return s + Lang.get("of.options.animation.dynamic");
         case 2:
            return s + Lang.getOff();
         default:
            return s + Lang.getOn();
         }
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.ANIMATED_FIRE) {
         return this.ofAnimatedFire?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.ANIMATED_PORTAL) {
         return this.ofAnimatedPortal?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.ANIMATED_REDSTONE) {
         return this.ofAnimatedRedstone?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.ANIMATED_EXPLOSION) {
         return this.ofAnimatedExplosion?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.ANIMATED_FLAME) {
         return this.ofAnimatedFlame?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.ANIMATED_SMOKE) {
         return this.ofAnimatedSmoke?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.VOID_PARTICLES) {
         return this.ofVoidParticles?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.WATER_PARTICLES) {
         return this.ofWaterParticles?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.PORTAL_PARTICLES) {
         return this.ofPortalParticles?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.POTION_PARTICLES) {
         return this.ofPotionParticles?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.FIREWORK_PARTICLES) {
         return this.ofFireworkParticles?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.DRIPPING_WATER_LAVA) {
         return this.ofDrippingWaterLava?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.ANIMATED_TERRAIN) {
         return this.ofAnimatedTerrain?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.ANIMATED_TEXTURES) {
         return this.ofAnimatedTextures?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.RAIN_SPLASH) {
         return this.ofRainSplash?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.LAGOMETER) {
         return this.ofLagometer?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.SHOW_FPS) {
         return this.ofShowFps?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.AUTOSAVE_TICKS) {
         return this.ofAutoSaveTicks <= 40?s + Lang.get("of.options.save.default"):(this.ofAutoSaveTicks <= 400?s + Lang.get("of.options.save.20s"):(this.ofAutoSaveTicks <= 4000?s + Lang.get("of.options.save.3min"):s + Lang.get("of.options.save.30min")));
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.BETTER_GRASS) {
         switch(this.ofBetterGrass) {
         case 1:
            return s + Lang.getFast();
         case 2:
            return s + Lang.getFancy();
         default:
            return s + Lang.getOff();
         }
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.CONNECTED_TEXTURES) {
         switch(this.ofConnectedTextures) {
         case 1:
            return s + Lang.getFast();
         case 2:
            return s + Lang.getFancy();
         default:
            return s + Lang.getOff();
         }
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.WEATHER) {
         return this.ofWeather?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.SKY) {
         return this.ofSky?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.STARS) {
         return this.ofStars?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.SUN_MOON) {
         return this.ofSunMoon?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.VIGNETTE) {
         switch(this.ofVignette) {
         case 1:
            return s + Lang.getFast();
         case 2:
            return s + Lang.getFancy();
         default:
            return s + Lang.getDefault();
         }
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.CHUNK_UPDATES) {
         return s + this.ofChunkUpdates;
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.CHUNK_UPDATES_DYNAMIC) {
         return this.ofChunkUpdatesDynamic?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.TIME) {
         return this.ofTime == 1?s + Lang.get("of.options.time.dayOnly"):(this.ofTime == 2?s + Lang.get("of.options.time.nightOnly"):s + Lang.getDefault());
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.CLEAR_WATER) {
         return this.ofClearWater?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.AA_LEVEL) {
         String s3 = "";
         if(this.ofAaLevel != Config.getAntialiasingLevel()) {
            s3 = " (" + Lang.get("of.general.restart") + ")";
         }

         return this.ofAaLevel == 0?s + Lang.getOff() + s3:s + this.ofAaLevel + s3;
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.AF_LEVEL) {
         return this.ofAfLevel == 1?s + Lang.getOff():s + this.ofAfLevel;
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.PROFILER) {
         return this.ofProfiler?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.BETTER_SNOW) {
         return this.ofBetterSnow?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.SWAMP_COLORS) {
         return this.ofSwampColors?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.RANDOM_MOBS) {
         return this.ofRandomMobs?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.SMOOTH_BIOMES) {
         return this.ofSmoothBiomes?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.CUSTOM_FONTS) {
         return this.ofCustomFonts?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.CUSTOM_COLORS) {
         return this.ofCustomColors?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.CUSTOM_SKY) {
         return this.ofCustomSky?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.SHOW_CAPES) {
         return this.ofShowCapes?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.CUSTOM_ITEMS) {
         return this.ofCustomItems?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.NATURAL_TEXTURES) {
         return this.ofNaturalTextures?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.FAST_MATH) {
         return this.ofFastMath?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.FAST_RENDER) {
         return this.ofFastRender?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.TRANSLUCENT_BLOCKS) {
         return this.ofTranslucentBlocks == 1?s + Lang.getFast():(this.ofTranslucentBlocks == 2?s + Lang.getFancy():s + Lang.getDefault());
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.LAZY_CHUNK_LOADING) {
         return this.ofLazyChunkLoading?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.DYNAMIC_FOV) {
         return this.ofDynamicFov?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.DYNAMIC_LIGHTS) {
         int k = indexOf(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
         return s + func_74299_a(KEYS_DYNAMIC_LIGHTS, k);
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.CUSTOM_GUIS) {
         return this.ofCustomGuis?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.FULLSCREEN_MODE) {
         return this.ofFullscreenMode.equals("Default")?s + Lang.getDefault():s + this.ofFullscreenMode;
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.HELD_ITEM_TOOLTIPS) {
         return this.field_92117_D?s + Lang.getOn():s + Lang.getOff();
      } else if(p_getKeyBindingOF_1_ == GameSettings.Options.FRAMERATE_LIMIT) {
         float f = this.func_74296_a(p_getKeyBindingOF_1_);
         return f == 0.0F?s + Lang.get("of.options.framerateLimit.vsync"):(f == p_getKeyBindingOF_1_.field_148272_O?s + I18n.func_135052_a("options.framerateLimit.max", new Object[0]):s + (int)f + " fps");
      } else {
         return null;
      }
   }

   public void loadOfOptions() {
      try {
         File file1 = this.optionsFileOF;
         if(!file1.exists()) {
            file1 = this.field_74354_ai;
         }

         if(!file1.exists()) {
            return;
         }

         BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(file1), "UTF-8"));
         String s = "";

         while((s = bufferedreader.readLine()) != null) {
            try {
               String[] astring = s.split(":");
               if(astring[0].equals("ofRenderDistanceChunks") && astring.length >= 2) {
                  this.field_151451_c = Integer.valueOf(astring[1]).intValue();
                  this.field_151451_c = Config.limit(this.field_151451_c, 2, 1024);
               }

               if(astring[0].equals("ofFogType") && astring.length >= 2) {
                  this.ofFogType = Integer.valueOf(astring[1]).intValue();
                  this.ofFogType = Config.limit(this.ofFogType, 1, 3);
               }

               if(astring[0].equals("ofFogStart") && astring.length >= 2) {
                  this.ofFogStart = Float.valueOf(astring[1]).floatValue();
                  if(this.ofFogStart < 0.2F) {
                     this.ofFogStart = 0.2F;
                  }

                  if(this.ofFogStart > 0.81F) {
                     this.ofFogStart = 0.8F;
                  }
               }

               if(astring[0].equals("ofMipmapType") && astring.length >= 2) {
                  this.ofMipmapType = Integer.valueOf(astring[1]).intValue();
                  this.ofMipmapType = Config.limit(this.ofMipmapType, 0, 3);
               }

               if(astring[0].equals("ofOcclusionFancy") && astring.length >= 2) {
                  this.ofOcclusionFancy = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofSmoothFps") && astring.length >= 2) {
                  this.ofSmoothFps = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofSmoothWorld") && astring.length >= 2) {
                  this.ofSmoothWorld = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofAoLevel") && astring.length >= 2) {
                  this.ofAoLevel = Float.valueOf(astring[1]).floatValue();
                  this.ofAoLevel = Config.limit(this.ofAoLevel, 0.0F, 1.0F);
               }

               if(astring[0].equals("ofClouds") && astring.length >= 2) {
                  this.ofClouds = Integer.valueOf(astring[1]).intValue();
                  this.ofClouds = Config.limit(this.ofClouds, 0, 3);
                  this.updateRenderClouds();
               }

               if(astring[0].equals("ofCloudsHeight") && astring.length >= 2) {
                  this.ofCloudsHeight = Float.valueOf(astring[1]).floatValue();
                  this.ofCloudsHeight = Config.limit(this.ofCloudsHeight, 0.0F, 1.0F);
               }

               if(astring[0].equals("ofTrees") && astring.length >= 2) {
                  this.ofTrees = Integer.valueOf(astring[1]).intValue();
                  this.ofTrees = limit(this.ofTrees, OF_TREES_VALUES);
               }

               if(astring[0].equals("ofDroppedItems") && astring.length >= 2) {
                  this.ofDroppedItems = Integer.valueOf(astring[1]).intValue();
                  this.ofDroppedItems = Config.limit(this.ofDroppedItems, 0, 2);
               }

               if(astring[0].equals("ofRain") && astring.length >= 2) {
                  this.ofRain = Integer.valueOf(astring[1]).intValue();
                  this.ofRain = Config.limit(this.ofRain, 0, 3);
               }

               if(astring[0].equals("ofAnimatedWater") && astring.length >= 2) {
                  this.ofAnimatedWater = Integer.valueOf(astring[1]).intValue();
                  this.ofAnimatedWater = Config.limit(this.ofAnimatedWater, 0, 2);
               }

               if(astring[0].equals("ofAnimatedLava") && astring.length >= 2) {
                  this.ofAnimatedLava = Integer.valueOf(astring[1]).intValue();
                  this.ofAnimatedLava = Config.limit(this.ofAnimatedLava, 0, 2);
               }

               if(astring[0].equals("ofAnimatedFire") && astring.length >= 2) {
                  this.ofAnimatedFire = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofAnimatedPortal") && astring.length >= 2) {
                  this.ofAnimatedPortal = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofAnimatedRedstone") && astring.length >= 2) {
                  this.ofAnimatedRedstone = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofAnimatedExplosion") && astring.length >= 2) {
                  this.ofAnimatedExplosion = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofAnimatedFlame") && astring.length >= 2) {
                  this.ofAnimatedFlame = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofAnimatedSmoke") && astring.length >= 2) {
                  this.ofAnimatedSmoke = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofVoidParticles") && astring.length >= 2) {
                  this.ofVoidParticles = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofWaterParticles") && astring.length >= 2) {
                  this.ofWaterParticles = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofPortalParticles") && astring.length >= 2) {
                  this.ofPortalParticles = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofPotionParticles") && astring.length >= 2) {
                  this.ofPotionParticles = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofFireworkParticles") && astring.length >= 2) {
                  this.ofFireworkParticles = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofDrippingWaterLava") && astring.length >= 2) {
                  this.ofDrippingWaterLava = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofAnimatedTerrain") && astring.length >= 2) {
                  this.ofAnimatedTerrain = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofAnimatedTextures") && astring.length >= 2) {
                  this.ofAnimatedTextures = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofRainSplash") && astring.length >= 2) {
                  this.ofRainSplash = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofLagometer") && astring.length >= 2) {
                  this.ofLagometer = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofShowFps") && astring.length >= 2) {
                  this.ofShowFps = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofAutoSaveTicks") && astring.length >= 2) {
                  this.ofAutoSaveTicks = Integer.valueOf(astring[1]).intValue();
                  this.ofAutoSaveTicks = Config.limit(this.ofAutoSaveTicks, 40, '\u9c40');
               }

               if(astring[0].equals("ofBetterGrass") && astring.length >= 2) {
                  this.ofBetterGrass = Integer.valueOf(astring[1]).intValue();
                  this.ofBetterGrass = Config.limit(this.ofBetterGrass, 1, 3);
               }

               if(astring[0].equals("ofConnectedTextures") && astring.length >= 2) {
                  this.ofConnectedTextures = Integer.valueOf(astring[1]).intValue();
                  this.ofConnectedTextures = Config.limit(this.ofConnectedTextures, 1, 3);
               }

               if(astring[0].equals("ofWeather") && astring.length >= 2) {
                  this.ofWeather = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofSky") && astring.length >= 2) {
                  this.ofSky = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofStars") && astring.length >= 2) {
                  this.ofStars = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofSunMoon") && astring.length >= 2) {
                  this.ofSunMoon = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofVignette") && astring.length >= 2) {
                  this.ofVignette = Integer.valueOf(astring[1]).intValue();
                  this.ofVignette = Config.limit(this.ofVignette, 0, 2);
               }

               if(astring[0].equals("ofChunkUpdates") && astring.length >= 2) {
                  this.ofChunkUpdates = Integer.valueOf(astring[1]).intValue();
                  this.ofChunkUpdates = Config.limit(this.ofChunkUpdates, 1, 5);
               }

               if(astring[0].equals("ofChunkUpdatesDynamic") && astring.length >= 2) {
                  this.ofChunkUpdatesDynamic = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofTime") && astring.length >= 2) {
                  this.ofTime = Integer.valueOf(astring[1]).intValue();
                  this.ofTime = Config.limit(this.ofTime, 0, 2);
               }

               if(astring[0].equals("ofClearWater") && astring.length >= 2) {
                  this.ofClearWater = Boolean.valueOf(astring[1]).booleanValue();
                  this.updateWaterOpacity();
               }

               if(astring[0].equals("ofAaLevel") && astring.length >= 2) {
                  this.ofAaLevel = Integer.valueOf(astring[1]).intValue();
                  this.ofAaLevel = Config.limit(this.ofAaLevel, 0, 16);
               }

               if(astring[0].equals("ofAfLevel") && astring.length >= 2) {
                  this.ofAfLevel = Integer.valueOf(astring[1]).intValue();
                  this.ofAfLevel = Config.limit(this.ofAfLevel, 1, 16);
               }

               if(astring[0].equals("ofProfiler") && astring.length >= 2) {
                  this.ofProfiler = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofBetterSnow") && astring.length >= 2) {
                  this.ofBetterSnow = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofSwampColors") && astring.length >= 2) {
                  this.ofSwampColors = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofRandomMobs") && astring.length >= 2) {
                  this.ofRandomMobs = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofSmoothBiomes") && astring.length >= 2) {
                  this.ofSmoothBiomes = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofCustomFonts") && astring.length >= 2) {
                  this.ofCustomFonts = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofCustomColors") && astring.length >= 2) {
                  this.ofCustomColors = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofCustomItems") && astring.length >= 2) {
                  this.ofCustomItems = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofCustomSky") && astring.length >= 2) {
                  this.ofCustomSky = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofShowCapes") && astring.length >= 2) {
                  this.ofShowCapes = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofNaturalTextures") && astring.length >= 2) {
                  this.ofNaturalTextures = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofLazyChunkLoading") && astring.length >= 2) {
                  this.ofLazyChunkLoading = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofDynamicFov") && astring.length >= 2) {
                  this.ofDynamicFov = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofDynamicLights") && astring.length >= 2) {
                  this.ofDynamicLights = Integer.valueOf(astring[1]).intValue();
                  this.ofDynamicLights = limit(this.ofDynamicLights, OF_DYNAMIC_LIGHTS);
               }

               if(astring[0].equals("ofCustomGuis") && astring.length >= 2) {
                  this.ofCustomGuis = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofFullscreenMode") && astring.length >= 2) {
                  this.ofFullscreenMode = astring[1];
               }

               if(astring[0].equals("ofFastMath") && astring.length >= 2) {
                  this.ofFastMath = Boolean.valueOf(astring[1]).booleanValue();
                  MathHelper.fastMath = this.ofFastMath;
               }

               if(astring[0].equals("ofFastRender") && astring.length >= 2) {
                  this.ofFastRender = Boolean.valueOf(astring[1]).booleanValue();
               }

               if(astring[0].equals("ofTranslucentBlocks") && astring.length >= 2) {
                  this.ofTranslucentBlocks = Integer.valueOf(astring[1]).intValue();
                  this.ofTranslucentBlocks = Config.limit(this.ofTranslucentBlocks, 0, 2);
               }

               if(astring[0].equals("key_" + this.ofKeyBindZoom.func_151464_g())) {
                  this.ofKeyBindZoom.func_151462_b(Integer.parseInt(astring[1]));
               }
            } catch (Exception exception) {
               Config.dbg("Skipping bad option: " + s);
               exception.printStackTrace();
            }
         }

         KeyBinding.func_74508_b();
         bufferedreader.close();
      } catch (Exception exception1) {
         Config.warn("Failed to load options");
         exception1.printStackTrace();
      }

   }

   public void saveOfOptions() {
      try {
         PrintWriter printwriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFileOF), "UTF-8"));
         printwriter.println("ofFogType:" + this.ofFogType);
         printwriter.println("ofFogStart:" + this.ofFogStart);
         printwriter.println("ofMipmapType:" + this.ofMipmapType);
         printwriter.println("ofOcclusionFancy:" + this.ofOcclusionFancy);
         printwriter.println("ofSmoothFps:" + this.ofSmoothFps);
         printwriter.println("ofSmoothWorld:" + this.ofSmoothWorld);
         printwriter.println("ofAoLevel:" + this.ofAoLevel);
         printwriter.println("ofClouds:" + this.ofClouds);
         printwriter.println("ofCloudsHeight:" + this.ofCloudsHeight);
         printwriter.println("ofTrees:" + this.ofTrees);
         printwriter.println("ofDroppedItems:" + this.ofDroppedItems);
         printwriter.println("ofRain:" + this.ofRain);
         printwriter.println("ofAnimatedWater:" + this.ofAnimatedWater);
         printwriter.println("ofAnimatedLava:" + this.ofAnimatedLava);
         printwriter.println("ofAnimatedFire:" + this.ofAnimatedFire);
         printwriter.println("ofAnimatedPortal:" + this.ofAnimatedPortal);
         printwriter.println("ofAnimatedRedstone:" + this.ofAnimatedRedstone);
         printwriter.println("ofAnimatedExplosion:" + this.ofAnimatedExplosion);
         printwriter.println("ofAnimatedFlame:" + this.ofAnimatedFlame);
         printwriter.println("ofAnimatedSmoke:" + this.ofAnimatedSmoke);
         printwriter.println("ofVoidParticles:" + this.ofVoidParticles);
         printwriter.println("ofWaterParticles:" + this.ofWaterParticles);
         printwriter.println("ofPortalParticles:" + this.ofPortalParticles);
         printwriter.println("ofPotionParticles:" + this.ofPotionParticles);
         printwriter.println("ofFireworkParticles:" + this.ofFireworkParticles);
         printwriter.println("ofDrippingWaterLava:" + this.ofDrippingWaterLava);
         printwriter.println("ofAnimatedTerrain:" + this.ofAnimatedTerrain);
         printwriter.println("ofAnimatedTextures:" + this.ofAnimatedTextures);
         printwriter.println("ofRainSplash:" + this.ofRainSplash);
         printwriter.println("ofLagometer:" + this.ofLagometer);
         printwriter.println("ofShowFps:" + this.ofShowFps);
         printwriter.println("ofAutoSaveTicks:" + this.ofAutoSaveTicks);
         printwriter.println("ofBetterGrass:" + this.ofBetterGrass);
         printwriter.println("ofConnectedTextures:" + this.ofConnectedTextures);
         printwriter.println("ofWeather:" + this.ofWeather);
         printwriter.println("ofSky:" + this.ofSky);
         printwriter.println("ofStars:" + this.ofStars);
         printwriter.println("ofSunMoon:" + this.ofSunMoon);
         printwriter.println("ofVignette:" + this.ofVignette);
         printwriter.println("ofChunkUpdates:" + this.ofChunkUpdates);
         printwriter.println("ofChunkUpdatesDynamic:" + this.ofChunkUpdatesDynamic);
         printwriter.println("ofTime:" + this.ofTime);
         printwriter.println("ofClearWater:" + this.ofClearWater);
         printwriter.println("ofAaLevel:" + this.ofAaLevel);
         printwriter.println("ofAfLevel:" + this.ofAfLevel);
         printwriter.println("ofProfiler:" + this.ofProfiler);
         printwriter.println("ofBetterSnow:" + this.ofBetterSnow);
         printwriter.println("ofSwampColors:" + this.ofSwampColors);
         printwriter.println("ofRandomMobs:" + this.ofRandomMobs);
         printwriter.println("ofSmoothBiomes:" + this.ofSmoothBiomes);
         printwriter.println("ofCustomFonts:" + this.ofCustomFonts);
         printwriter.println("ofCustomColors:" + this.ofCustomColors);
         printwriter.println("ofCustomItems:" + this.ofCustomItems);
         printwriter.println("ofCustomSky:" + this.ofCustomSky);
         printwriter.println("ofShowCapes:" + this.ofShowCapes);
         printwriter.println("ofNaturalTextures:" + this.ofNaturalTextures);
         printwriter.println("ofLazyChunkLoading:" + this.ofLazyChunkLoading);
         printwriter.println("ofDynamicFov:" + this.ofDynamicFov);
         printwriter.println("ofDynamicLights:" + this.ofDynamicLights);
         printwriter.println("ofCustomGuis:" + this.ofCustomGuis);
         printwriter.println("ofFullscreenMode:" + this.ofFullscreenMode);
         printwriter.println("ofFastMath:" + this.ofFastMath);
         printwriter.println("ofFastRender:" + this.ofFastRender);
         printwriter.println("ofTranslucentBlocks:" + this.ofTranslucentBlocks);
         printwriter.println("key_" + this.ofKeyBindZoom.func_151464_g() + ":" + this.ofKeyBindZoom.func_151463_i());
         printwriter.close();
      } catch (Exception exception) {
         Config.warn("Failed to save options");
         exception.printStackTrace();
      }

   }

   private void updateRenderClouds() {
      switch(this.ofClouds) {
      case 1:
         this.field_74345_l = 1;
         break;
      case 2:
         this.field_74345_l = 2;
         break;
      case 3:
         this.field_74345_l = 0;
         break;
      default:
         if(this.field_74347_j) {
            this.field_74345_l = 2;
         } else {
            this.field_74345_l = 1;
         }
      }

   }

   public void resetSettings() {
      this.field_151451_c = 8;
      this.field_74336_f = true;
      this.field_74337_g = false;
      this.field_74350_i = (int)GameSettings.Options.FRAMERATE_LIMIT.func_148267_f();
      this.field_74352_v = false;
      this.updateVSync();
      this.field_151442_I = 4;
      this.field_74347_j = true;
      this.field_74348_k = 2;
      this.field_74345_l = 2;
      this.field_74334_X = 70.0F;
      this.field_74333_Y = 0.0F;
      this.field_74335_Z = 0;
      this.field_74362_aa = 0;
      this.field_92117_D = true;
      this.field_178881_t = false;
      this.field_178880_u = true;
      this.field_151455_aw = false;
      this.ofFogType = 1;
      this.ofFogStart = 0.8F;
      this.ofMipmapType = 0;
      this.ofOcclusionFancy = false;
      this.ofSmoothFps = false;
      Config.updateAvailableProcessors();
      this.ofSmoothWorld = Config.isSingleProcessor();
      this.ofLazyChunkLoading = Config.isSingleProcessor();
      this.ofFastMath = false;
      this.ofFastRender = false;
      this.ofTranslucentBlocks = 0;
      this.ofDynamicFov = true;
      this.ofDynamicLights = 3;
      this.ofCustomGuis = true;
      this.ofAoLevel = 1.0F;
      this.ofAaLevel = 0;
      this.ofAfLevel = 1;
      this.ofClouds = 0;
      this.ofCloudsHeight = 0.0F;
      this.ofTrees = 0;
      this.ofRain = 0;
      this.ofBetterGrass = 3;
      this.ofAutoSaveTicks = 4000;
      this.ofLagometer = false;
      this.ofShowFps = false;
      this.ofProfiler = false;
      this.ofWeather = true;
      this.ofSky = true;
      this.ofStars = true;
      this.ofSunMoon = true;
      this.ofVignette = 0;
      this.ofChunkUpdates = 1;
      this.ofChunkUpdatesDynamic = false;
      this.ofTime = 0;
      this.ofClearWater = false;
      this.ofBetterSnow = false;
      this.ofFullscreenMode = "Default";
      this.ofSwampColors = true;
      this.ofRandomMobs = true;
      this.ofSmoothBiomes = true;
      this.ofCustomFonts = true;
      this.ofCustomColors = true;
      this.ofCustomItems = true;
      this.ofCustomSky = true;
      this.ofShowCapes = true;
      this.ofConnectedTextures = 2;
      this.ofNaturalTextures = false;
      this.ofAnimatedWater = 0;
      this.ofAnimatedLava = 0;
      this.ofAnimatedFire = true;
      this.ofAnimatedPortal = true;
      this.ofAnimatedRedstone = true;
      this.ofAnimatedExplosion = true;
      this.ofAnimatedFlame = true;
      this.ofAnimatedSmoke = true;
      this.ofVoidParticles = true;
      this.ofWaterParticles = true;
      this.ofRainSplash = true;
      this.ofPortalParticles = true;
      this.ofPotionParticles = true;
      this.ofFireworkParticles = true;
      this.ofDrippingWaterLava = true;
      this.ofAnimatedTerrain = true;
      this.ofAnimatedTextures = true;
      Shaders.setShaderPack(Shaders.packNameNone);
      Shaders.configAntialiasingLevel = 0;
      Shaders.uninit();
      Shaders.storeConfig();
      this.updateWaterOpacity();
      this.field_74317_L.func_110436_a();
      this.func_74303_b();
   }

   public void updateVSync() {
      Display.setVSyncEnabled(this.field_74352_v);
   }

   private void updateWaterOpacity() {
      if(this.field_74317_L.func_71387_A() && this.field_74317_L.func_71401_C() != null) {
         Config.waterOpacityChanged = true;
      }

      ClearWater.updateWaterOpacity(this, this.field_74317_L.field_71441_e);
   }

   public void setAllAnimations(boolean p_setAllAnimations_1_) {
      int i = p_setAllAnimations_1_?0:2;
      this.ofAnimatedWater = i;
      this.ofAnimatedLava = i;
      this.ofAnimatedFire = p_setAllAnimations_1_;
      this.ofAnimatedPortal = p_setAllAnimations_1_;
      this.ofAnimatedRedstone = p_setAllAnimations_1_;
      this.ofAnimatedExplosion = p_setAllAnimations_1_;
      this.ofAnimatedFlame = p_setAllAnimations_1_;
      this.ofAnimatedSmoke = p_setAllAnimations_1_;
      this.ofVoidParticles = p_setAllAnimations_1_;
      this.ofWaterParticles = p_setAllAnimations_1_;
      this.ofRainSplash = p_setAllAnimations_1_;
      this.ofPortalParticles = p_setAllAnimations_1_;
      this.ofPotionParticles = p_setAllAnimations_1_;
      this.ofFireworkParticles = p_setAllAnimations_1_;
      this.field_74362_aa = p_setAllAnimations_1_?0:2;
      this.ofDrippingWaterLava = p_setAllAnimations_1_;
      this.ofAnimatedTerrain = p_setAllAnimations_1_;
      this.ofAnimatedTextures = p_setAllAnimations_1_;
   }

   private static int nextValue(int p_nextValue_0_, int[] p_nextValue_1_) {
      int i = indexOf(p_nextValue_0_, p_nextValue_1_);
      if(i < 0) {
         return p_nextValue_1_[0];
      } else {
         ++i;
         if(i >= p_nextValue_1_.length) {
            i = 0;
         }

         return p_nextValue_1_[i];
      }
   }

   private static int limit(int p_limit_0_, int[] p_limit_1_) {
      int i = indexOf(p_limit_0_, p_limit_1_);
      return i < 0?p_limit_1_[0]:p_limit_0_;
   }

   private static int indexOf(int p_indexOf_0_, int[] p_indexOf_1_) {
      for(int i = 0; i < p_indexOf_1_.length; ++i) {
         if(p_indexOf_1_[i] == p_indexOf_0_) {
            return i;
         }
      }

      return -1;
   }

   static final class GameSettings$2 {
      static final int[] field_151477_a = new int[GameSettings.Options.values().length];
      private static final String __OBFID = "CL_00000652";

      static {
         try {
            field_151477_a[GameSettings.Options.INVERT_MOUSE.ordinal()] = 1;
         } catch (NoSuchFieldError var17) {
            ;
         }

         try {
            field_151477_a[GameSettings.Options.VIEW_BOBBING.ordinal()] = 2;
         } catch (NoSuchFieldError var16) {
            ;
         }

         try {
            field_151477_a[GameSettings.Options.ANAGLYPH.ordinal()] = 3;
         } catch (NoSuchFieldError var15) {
            ;
         }

         try {
            field_151477_a[GameSettings.Options.FBO_ENABLE.ordinal()] = 4;
         } catch (NoSuchFieldError var14) {
            ;
         }

         try {
            field_151477_a[GameSettings.Options.CHAT_COLOR.ordinal()] = 5;
         } catch (NoSuchFieldError var13) {
            ;
         }

         try {
            field_151477_a[GameSettings.Options.CHAT_LINKS.ordinal()] = 6;
         } catch (NoSuchFieldError var12) {
            ;
         }

         try {
            field_151477_a[GameSettings.Options.CHAT_LINKS_PROMPT.ordinal()] = 7;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            field_151477_a[GameSettings.Options.SNOOPER_ENABLED.ordinal()] = 8;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            field_151477_a[GameSettings.Options.USE_FULLSCREEN.ordinal()] = 9;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            field_151477_a[GameSettings.Options.ENABLE_VSYNC.ordinal()] = 10;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            field_151477_a[GameSettings.Options.USE_VBO.ordinal()] = 11;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            field_151477_a[GameSettings.Options.TOUCHSCREEN.ordinal()] = 12;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            field_151477_a[GameSettings.Options.STREAM_SEND_METADATA.ordinal()] = 13;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            field_151477_a[GameSettings.Options.FORCE_UNICODE_FONT.ordinal()] = 14;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            field_151477_a[GameSettings.Options.BLOCK_ALTERNATIVES.ordinal()] = 15;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            field_151477_a[GameSettings.Options.REDUCED_DEBUG_INFO.ordinal()] = 16;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            field_151477_a[GameSettings.Options.ENTITY_SHADOWS.ordinal()] = 17;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum Options {
      INVERT_MOUSE("INVERT_MOUSE", 0, "options.invertMouse", false, true),
      SENSITIVITY("SENSITIVITY", 1, "options.sensitivity", true, false),
      FOV("FOV", 2, "options.fov", true, false, 30.0F, 110.0F, 1.0F),
      GAMMA("GAMMA", 3, "options.gamma", true, false),
      SATURATION("SATURATION", 4, "options.saturation", true, false),
      RENDER_DISTANCE("RENDER_DISTANCE", 5, "options.renderDistance", true, false, 2.0F, 16.0F, 1.0F),
      VIEW_BOBBING("VIEW_BOBBING", 6, "options.viewBobbing", false, true),
      ANAGLYPH("ANAGLYPH", 7, "options.anaglyph", false, true),
      FRAMERATE_LIMIT("FRAMERATE_LIMIT", 8, "options.framerateLimit", true, false, 0.0F, 260.0F, 5.0F),
      FBO_ENABLE("FBO_ENABLE", 9, "options.fboEnable", false, true),
      RENDER_CLOUDS("RENDER_CLOUDS", 10, "options.renderClouds", false, false),
      GRAPHICS("GRAPHICS", 11, "options.graphics", false, false),
      AMBIENT_OCCLUSION("AMBIENT_OCCLUSION", 12, "options.ao", false, false),
      GUI_SCALE("GUI_SCALE", 13, "options.guiScale", false, false),
      PARTICLES("PARTICLES", 14, "options.particles", false, false),
      CHAT_VISIBILITY("CHAT_VISIBILITY", 15, "options.chat.visibility", false, false),
      CHAT_COLOR("CHAT_COLOR", 16, "options.chat.color", false, true),
      CHAT_LINKS("CHAT_LINKS", 17, "options.chat.links", false, true),
      CHAT_OPACITY("CHAT_OPACITY", 18, "options.chat.opacity", true, false),
      CHAT_LINKS_PROMPT("CHAT_LINKS_PROMPT", 19, "options.chat.links.prompt", false, true),
      SNOOPER_ENABLED("SNOOPER_ENABLED", 20, "options.snooper", false, true),
      USE_FULLSCREEN("USE_FULLSCREEN", 21, "options.fullscreen", false, true),
      ENABLE_VSYNC("ENABLE_VSYNC", 22, "options.vsync", false, true),
      USE_VBO("USE_VBO", 23, "options.vbo", false, true),
      TOUCHSCREEN("TOUCHSCREEN", 24, "options.touchscreen", false, true),
      CHAT_SCALE("CHAT_SCALE", 25, "options.chat.scale", true, false),
      CHAT_WIDTH("CHAT_WIDTH", 26, "options.chat.width", true, false),
      CHAT_HEIGHT_FOCUSED("CHAT_HEIGHT_FOCUSED", 27, "options.chat.height.focused", true, false),
      CHAT_HEIGHT_UNFOCUSED("CHAT_HEIGHT_UNFOCUSED", 28, "options.chat.height.unfocused", true, false),
      MIPMAP_LEVELS("MIPMAP_LEVELS", 29, "options.mipmapLevels", true, false, 0.0F, 4.0F, 1.0F),
      FORCE_UNICODE_FONT("FORCE_UNICODE_FONT", 30, "options.forceUnicodeFont", false, true),
      STREAM_BYTES_PER_PIXEL("STREAM_BYTES_PER_PIXEL", 31, "options.stream.bytesPerPixel", true, false),
      STREAM_VOLUME_MIC("STREAM_VOLUME_MIC", 32, "options.stream.micVolumne", true, false),
      STREAM_VOLUME_SYSTEM("STREAM_VOLUME_SYSTEM", 33, "options.stream.systemVolume", true, false),
      STREAM_KBPS("STREAM_KBPS", 34, "options.stream.kbps", true, false),
      STREAM_FPS("STREAM_FPS", 35, "options.stream.fps", true, false),
      STREAM_COMPRESSION("STREAM_COMPRESSION", 36, "options.stream.compression", false, false),
      STREAM_SEND_METADATA("STREAM_SEND_METADATA", 37, "options.stream.sendMetadata", false, true),
      STREAM_CHAT_ENABLED("STREAM_CHAT_ENABLED", 38, "options.stream.chat.enabled", false, false),
      STREAM_CHAT_USER_FILTER("STREAM_CHAT_USER_FILTER", 39, "options.stream.chat.userFilter", false, false),
      STREAM_MIC_TOGGLE_BEHAVIOR("STREAM_MIC_TOGGLE_BEHAVIOR", 40, "options.stream.micToggleBehavior", false, false),
      BLOCK_ALTERNATIVES("BLOCK_ALTERNATIVES", 41, "options.blockAlternatives", false, true),
      REDUCED_DEBUG_INFO("REDUCED_DEBUG_INFO", 42, "options.reducedDebugInfo", false, true),
      ENTITY_SHADOWS("ENTITY_SHADOWS", 43, "options.entityShadows", false, true),
      REALMS_NOTIFICATIONS("REALMS_NOTIFICATIONS", 44, "options.realmsNotifications", false, true),
      FOG_FANCY("", 999, "of.options.FOG_FANCY", false, false),
      FOG_START("", 999, "of.options.FOG_START", false, false),
      MIPMAP_TYPE("", 999, "of.options.MIPMAP_TYPE", true, false, 0.0F, 3.0F, 1.0F),
      SMOOTH_FPS("", 999, "of.options.SMOOTH_FPS", false, false),
      CLOUDS("", 999, "of.options.CLOUDS", false, false),
      CLOUD_HEIGHT("", 999, "of.options.CLOUD_HEIGHT", true, false),
      TREES("", 999, "of.options.TREES", false, false),
      RAIN("", 999, "of.options.RAIN", false, false),
      ANIMATED_WATER("", 999, "of.options.ANIMATED_WATER", false, false),
      ANIMATED_LAVA("", 999, "of.options.ANIMATED_LAVA", false, false),
      ANIMATED_FIRE("", 999, "of.options.ANIMATED_FIRE", false, false),
      ANIMATED_PORTAL("", 999, "of.options.ANIMATED_PORTAL", false, false),
      AO_LEVEL("", 999, "of.options.AO_LEVEL", true, false),
      LAGOMETER("", 999, "of.options.LAGOMETER", false, false),
      SHOW_FPS("", 999, "of.options.SHOW_FPS", false, false),
      AUTOSAVE_TICKS("", 999, "of.options.AUTOSAVE_TICKS", false, false),
      BETTER_GRASS("", 999, "of.options.BETTER_GRASS", false, false),
      ANIMATED_REDSTONE("", 999, "of.options.ANIMATED_REDSTONE", false, false),
      ANIMATED_EXPLOSION("", 999, "of.options.ANIMATED_EXPLOSION", false, false),
      ANIMATED_FLAME("", 999, "of.options.ANIMATED_FLAME", false, false),
      ANIMATED_SMOKE("", 999, "of.options.ANIMATED_SMOKE", false, false),
      WEATHER("", 999, "of.options.WEATHER", false, false),
      SKY("", 999, "of.options.SKY", false, false),
      STARS("", 999, "of.options.STARS", false, false),
      SUN_MOON("", 999, "of.options.SUN_MOON", false, false),
      VIGNETTE("", 999, "of.options.VIGNETTE", false, false),
      CHUNK_UPDATES("", 999, "of.options.CHUNK_UPDATES", false, false),
      CHUNK_UPDATES_DYNAMIC("", 999, "of.options.CHUNK_UPDATES_DYNAMIC", false, false),
      TIME("", 999, "of.options.TIME", false, false),
      CLEAR_WATER("", 999, "of.options.CLEAR_WATER", false, false),
      SMOOTH_WORLD("", 999, "of.options.SMOOTH_WORLD", false, false),
      VOID_PARTICLES("", 999, "of.options.VOID_PARTICLES", false, false),
      WATER_PARTICLES("", 999, "of.options.WATER_PARTICLES", false, false),
      RAIN_SPLASH("", 999, "of.options.RAIN_SPLASH", false, false),
      PORTAL_PARTICLES("", 999, "of.options.PORTAL_PARTICLES", false, false),
      POTION_PARTICLES("", 999, "of.options.POTION_PARTICLES", false, false),
      FIREWORK_PARTICLES("", 999, "of.options.FIREWORK_PARTICLES", false, false),
      PROFILER("", 999, "of.options.PROFILER", false, false),
      DRIPPING_WATER_LAVA("", 999, "of.options.DRIPPING_WATER_LAVA", false, false),
      BETTER_SNOW("", 999, "of.options.BETTER_SNOW", false, false),
      FULLSCREEN_MODE("", 999, "of.options.FULLSCREEN_MODE", false, false),
      ANIMATED_TERRAIN("", 999, "of.options.ANIMATED_TERRAIN", false, false),
      SWAMP_COLORS("", 999, "of.options.SWAMP_COLORS", false, false),
      RANDOM_MOBS("", 999, "of.options.RANDOM_MOBS", false, false),
      SMOOTH_BIOMES("", 999, "of.options.SMOOTH_BIOMES", false, false),
      CUSTOM_FONTS("", 999, "of.options.CUSTOM_FONTS", false, false),
      CUSTOM_COLORS("", 999, "of.options.CUSTOM_COLORS", false, false),
      SHOW_CAPES("", 999, "of.options.SHOW_CAPES", false, false),
      CONNECTED_TEXTURES("", 999, "of.options.CONNECTED_TEXTURES", false, false),
      CUSTOM_ITEMS("", 999, "of.options.CUSTOM_ITEMS", false, false),
      AA_LEVEL("", 999, "of.options.AA_LEVEL", true, false, 0.0F, 16.0F, 1.0F),
      AF_LEVEL("", 999, "of.options.AF_LEVEL", true, false, 1.0F, 16.0F, 1.0F),
      ANIMATED_TEXTURES("", 999, "of.options.ANIMATED_TEXTURES", false, false),
      NATURAL_TEXTURES("", 999, "of.options.NATURAL_TEXTURES", false, false),
      HELD_ITEM_TOOLTIPS("", 999, "of.options.HELD_ITEM_TOOLTIPS", false, false),
      DROPPED_ITEMS("", 999, "of.options.DROPPED_ITEMS", false, false),
      LAZY_CHUNK_LOADING("", 999, "of.options.LAZY_CHUNK_LOADING", false, false),
      CUSTOM_SKY("", 999, "of.options.CUSTOM_SKY", false, false),
      FAST_MATH("", 999, "of.options.FAST_MATH", false, false),
      FAST_RENDER("", 999, "of.options.FAST_RENDER", false, false),
      TRANSLUCENT_BLOCKS("", 999, "of.options.TRANSLUCENT_BLOCKS", false, false),
      DYNAMIC_FOV("", 999, "of.options.DYNAMIC_FOV", false, false),
      DYNAMIC_LIGHTS("", 999, "of.options.DYNAMIC_LIGHTS", false, false),
      CUSTOM_GUIS("", 999, "of.options.CUSTOM_GUIS", false, false);

      private final boolean field_74385_A;
      private final boolean field_74386_B;
      private final String field_74387_C;
      private final float field_148270_M;
      private float field_148271_N;
      private float field_148272_O;
      private static final GameSettings.Options[] $VALUES = new GameSettings.Options[]{INVERT_MOUSE, SENSITIVITY, FOV, GAMMA, SATURATION, RENDER_DISTANCE, VIEW_BOBBING, ANAGLYPH, FRAMERATE_LIMIT, FBO_ENABLE, RENDER_CLOUDS, GRAPHICS, AMBIENT_OCCLUSION, GUI_SCALE, PARTICLES, CHAT_VISIBILITY, CHAT_COLOR, CHAT_LINKS, CHAT_OPACITY, CHAT_LINKS_PROMPT, SNOOPER_ENABLED, USE_FULLSCREEN, ENABLE_VSYNC, USE_VBO, TOUCHSCREEN, CHAT_SCALE, CHAT_WIDTH, CHAT_HEIGHT_FOCUSED, CHAT_HEIGHT_UNFOCUSED, MIPMAP_LEVELS, FORCE_UNICODE_FONT, STREAM_BYTES_PER_PIXEL, STREAM_VOLUME_MIC, STREAM_VOLUME_SYSTEM, STREAM_KBPS, STREAM_FPS, STREAM_COMPRESSION, STREAM_SEND_METADATA, STREAM_CHAT_ENABLED, STREAM_CHAT_USER_FILTER, STREAM_MIC_TOGGLE_BEHAVIOR, BLOCK_ALTERNATIVES, REDUCED_DEBUG_INFO, ENTITY_SHADOWS};
      private static final String __OBFID = "CL_00000653";

      public static GameSettings.Options func_74379_a(int p_74379_0_) {
         for(GameSettings.Options gamesettings$options : values()) {
            if(gamesettings$options.func_74381_c() == p_74379_0_) {
               return gamesettings$options;
            }
         }

         return null;
      }

      private Options(String p_i1_3_, int p_i1_4_, String p_i1_5_, boolean p_i1_6_, boolean p_i1_7_) {
         this(p_i1_3_, p_i1_4_, p_i1_5_, p_i1_6_, p_i1_7_, 0.0F, 1.0F, 0.0F);
      }

      private Options(String p_i2_3_, int p_i2_4_, String p_i2_5_, boolean p_i2_6_, boolean p_i2_7_, float p_i2_8_, float p_i2_9_, float p_i2_10_) {
         this.field_74387_C = p_i2_5_;
         this.field_74385_A = p_i2_6_;
         this.field_74386_B = p_i2_7_;
         this.field_148271_N = p_i2_8_;
         this.field_148272_O = p_i2_9_;
         this.field_148270_M = p_i2_10_;
      }

      public boolean func_74380_a() {
         return this.field_74385_A;
      }

      public boolean func_74382_b() {
         return this.field_74386_B;
      }

      public int func_74381_c() {
         return this.ordinal();
      }

      public String func_74378_d() {
         return this.field_74387_C;
      }

      public float func_148267_f() {
         return this.field_148272_O;
      }

      public void func_148263_a(float p_148263_1_) {
         this.field_148272_O = p_148263_1_;
      }

      public float func_148266_c(float p_148266_1_) {
         return MathHelper.func_76131_a((this.func_148268_e(p_148266_1_) - this.field_148271_N) / (this.field_148272_O - this.field_148271_N), 0.0F, 1.0F);
      }

      public float func_148262_d(float p_148262_1_) {
         return this.func_148268_e(this.field_148271_N + (this.field_148272_O - this.field_148271_N) * MathHelper.func_76131_a(p_148262_1_, 0.0F, 1.0F));
      }

      public float func_148268_e(float p_148268_1_) {
         p_148268_1_ = this.func_148264_f(p_148268_1_);
         return MathHelper.func_76131_a(p_148268_1_, this.field_148271_N, this.field_148272_O);
      }

      protected float func_148264_f(float p_148264_1_) {
         if(this.field_148270_M > 0.0F) {
            p_148264_1_ = this.field_148270_M * (float)Math.round(p_148264_1_ / this.field_148270_M);
         }

         return p_148264_1_;
      }
   }
}
