package shadersmod.client;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.src.Config;
import net.minecraft.src.Lang;
import net.minecraft.src.TooltipManager;
import net.minecraft.src.TooltipProviderEnumShaderOptions;
import org.lwjgl.Sys;
import shadersmod.client.EnumShaderOption;
import shadersmod.client.GuiButtonEnumShaderOption;
import shadersmod.client.GuiShaderOptions;
import shadersmod.client.GuiSlotShaders;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersTex;

public class GuiShaders extends GuiScreen {
   protected GuiScreen parentGui;
   protected String screenTitle = "Shaders";
   private TooltipManager tooltipManager = new TooltipManager(this, new TooltipProviderEnumShaderOptions());
   private int updateTimer = -1;
   private GuiSlotShaders shaderList;
   private boolean saved = false;
   private static float[] QUALITY_MULTIPLIERS = new float[]{0.5F, 0.70710677F, 1.0F, 1.4142135F, 2.0F};
   private static String[] QUALITY_MULTIPLIER_NAMES = new String[]{"0.5x", "0.7x", "1x", "1.5x", "2x"};
   private static float[] HAND_DEPTH_VALUES = new float[]{0.0625F, 0.125F, 0.25F};
   private static String[] HAND_DEPTH_NAMES = new String[]{"0.5x", "1x", "2x"};
   public static final int EnumOS_UNKNOWN = 0;
   public static final int EnumOS_WINDOWS = 1;
   public static final int EnumOS_OSX = 2;
   public static final int EnumOS_SOLARIS = 3;
   public static final int EnumOS_LINUX = 4;

   public GuiShaders(GuiScreen par1GuiScreen, GameSettings par2GameSettings) {
      this.parentGui = par1GuiScreen;
   }

   public void func_73866_w_() {
      this.screenTitle = I18n.func_135052_a("of.options.shadersTitle", new Object[0]);
      if(Shaders.shadersConfig == null) {
         Shaders.loadConfig();
      }

      int i = 120;
      int j = 20;
      int k = this.field_146294_l - i - 10;
      int l = 30;
      int i1 = 20;
      int j1 = this.field_146294_l - i - 20;
      this.shaderList = new GuiSlotShaders(this, j1, this.field_146295_m, l, this.field_146295_m - 50, 16);
      this.shaderList.func_148134_d(7, 8);
      this.field_146292_n.add(new GuiButtonEnumShaderOption(EnumShaderOption.ANTIALIASING, k, 0 * i1 + l, i, j));
      this.field_146292_n.add(new GuiButtonEnumShaderOption(EnumShaderOption.NORMAL_MAP, k, 1 * i1 + l, i, j));
      this.field_146292_n.add(new GuiButtonEnumShaderOption(EnumShaderOption.SPECULAR_MAP, k, 2 * i1 + l, i, j));
      this.field_146292_n.add(new GuiButtonEnumShaderOption(EnumShaderOption.RENDER_RES_MUL, k, 3 * i1 + l, i, j));
      this.field_146292_n.add(new GuiButtonEnumShaderOption(EnumShaderOption.SHADOW_RES_MUL, k, 4 * i1 + l, i, j));
      this.field_146292_n.add(new GuiButtonEnumShaderOption(EnumShaderOption.HAND_DEPTH_MUL, k, 5 * i1 + l, i, j));
      this.field_146292_n.add(new GuiButtonEnumShaderOption(EnumShaderOption.OLD_HAND_LIGHT, k, 6 * i1 + l, i, j));
      this.field_146292_n.add(new GuiButtonEnumShaderOption(EnumShaderOption.OLD_LIGHTING, k, 7 * i1 + l, i, j));
      int k1 = Math.min(150, j1 / 2 - 10);
      this.field_146292_n.add(new GuiButton(201, j1 / 4 - k1 / 2, this.field_146295_m - 25, k1, j, Lang.get("of.options.shaders.shadersFolder")));
      this.field_146292_n.add(new GuiButton(202, j1 / 4 * 3 - k1 / 2, this.field_146295_m - 25, k1, j, I18n.func_135052_a("gui.done", new Object[0])));
      this.field_146292_n.add(new GuiButton(203, k, this.field_146295_m - 25, i, j, Lang.get("of.options.shaders.shaderOptions")));
      this.updateButtons();
   }

   public void updateButtons() {
      boolean flag = Config.isShaders();

      for(GuiButton guibutton : this.field_146292_n) {
         if(guibutton.field_146127_k != 201 && guibutton.field_146127_k != 202 && guibutton.field_146127_k != EnumShaderOption.ANTIALIASING.ordinal()) {
            guibutton.field_146124_l = flag;
         }
      }

   }

   public void func_146274_d() throws IOException {
      super.func_146274_d();
      this.shaderList.func_178039_p();
   }

   protected void func_146284_a(GuiButton button) {
      if(button.field_146124_l) {
         if(button instanceof GuiButtonEnumShaderOption) {
            GuiButtonEnumShaderOption guibuttonenumshaderoption = (GuiButtonEnumShaderOption)button;
            switch(guibuttonenumshaderoption.getEnumShaderOption()) {
            case ANTIALIASING:
               Shaders.nextAntialiasingLevel();
               Shaders.uninit();
               break;
            case NORMAL_MAP:
               Shaders.configNormalMap = !Shaders.configNormalMap;
               Shaders.uninit();
               this.field_146297_k.func_175603_A();
               break;
            case SPECULAR_MAP:
               Shaders.configSpecularMap = !Shaders.configSpecularMap;
               Shaders.uninit();
               this.field_146297_k.func_175603_A();
               break;
            case RENDER_RES_MUL:
               float f2 = Shaders.configRenderResMul;
               float[] afloat2 = QUALITY_MULTIPLIERS;
               String[] astring2 = QUALITY_MULTIPLIER_NAMES;
               int k = getValueIndex(f2, afloat2);
               if(func_146272_n()) {
                  --k;
                  if(k < 0) {
                     k = afloat2.length - 1;
                  }
               } else {
                  ++k;
                  if(k >= afloat2.length) {
                     k = 0;
                  }
               }

               Shaders.configRenderResMul = afloat2[k];
               Shaders.uninit();
               Shaders.scheduleResize();
               break;
            case SHADOW_RES_MUL:
               float f1 = Shaders.configShadowResMul;
               float[] afloat1 = QUALITY_MULTIPLIERS;
               String[] astring1 = QUALITY_MULTIPLIER_NAMES;
               int j = getValueIndex(f1, afloat1);
               if(func_146272_n()) {
                  --j;
                  if(j < 0) {
                     j = afloat1.length - 1;
                  }
               } else {
                  ++j;
                  if(j >= afloat1.length) {
                     j = 0;
                  }
               }

               Shaders.configShadowResMul = afloat1[j];
               Shaders.uninit();
               Shaders.scheduleResizeShadow();
               break;
            case HAND_DEPTH_MUL:
               float f = Shaders.configHandDepthMul;
               float[] afloat = HAND_DEPTH_VALUES;
               String[] astring = HAND_DEPTH_NAMES;
               int i = getValueIndex(f, afloat);
               if(func_146272_n()) {
                  --i;
                  if(i < 0) {
                     i = afloat.length - 1;
                  }
               } else {
                  ++i;
                  if(i >= afloat.length) {
                     i = 0;
                  }
               }

               Shaders.configHandDepthMul = afloat[i];
               Shaders.uninit();
               break;
            case OLD_HAND_LIGHT:
               Shaders.configOldHandLight.nextValue();
               Shaders.uninit();
               break;
            case OLD_LIGHTING:
               Shaders.configOldLighting.nextValue();
               Shaders.updateBlockLightLevel();
               Shaders.uninit();
               this.field_146297_k.func_175603_A();
               break;
            case TWEAK_BLOCK_DAMAGE:
               Shaders.configTweakBlockDamage = !Shaders.configTweakBlockDamage;
               break;
            case CLOUD_SHADOW:
               Shaders.configCloudShadow = !Shaders.configCloudShadow;
               break;
            case TEX_MIN_FIL_B:
               Shaders.configTexMinFilB = (Shaders.configTexMinFilB + 1) % 3;
               Shaders.configTexMinFilN = Shaders.configTexMinFilS = Shaders.configTexMinFilB;
               button.field_146126_j = "Tex Min: " + Shaders.texMinFilDesc[Shaders.configTexMinFilB];
               ShadersTex.updateTextureMinMagFilter();
               break;
            case TEX_MAG_FIL_N:
               Shaders.configTexMagFilN = (Shaders.configTexMagFilN + 1) % 2;
               button.field_146126_j = "Tex_n Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilN];
               ShadersTex.updateTextureMinMagFilter();
               break;
            case TEX_MAG_FIL_S:
               Shaders.configTexMagFilS = (Shaders.configTexMagFilS + 1) % 2;
               button.field_146126_j = "Tex_s Mag: " + Shaders.texMagFilDesc[Shaders.configTexMagFilS];
               ShadersTex.updateTextureMinMagFilter();
               break;
            case SHADOW_CLIP_FRUSTRUM:
               Shaders.configShadowClipFrustrum = !Shaders.configShadowClipFrustrum;
               button.field_146126_j = "ShadowClipFrustrum: " + toStringOnOff(Shaders.configShadowClipFrustrum);
               ShadersTex.updateTextureMinMagFilter();
            }

            guibuttonenumshaderoption.updateButtonText();
         } else {
            switch(button.field_146127_k) {
            case 201:
               switch(getOSType()) {
               case 1:
                  String s = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[]{Shaders.shaderpacksdir.getAbsolutePath()});

                  try {
                     Runtime.getRuntime().exec(s);
                     return;
                  } catch (IOException ioexception) {
                     ioexception.printStackTrace();
                     break;
                  }
               case 2:
                  try {
                     Runtime.getRuntime().exec(new String[]{"/usr/bin/open", Shaders.shaderpacksdir.getAbsolutePath()});
                     return;
                  } catch (IOException ioexception1) {
                     ioexception1.printStackTrace();
                  }
               }

               boolean flag = false;

               try {
                  Class oclass = Class.forName("java.awt.Desktop");
                  Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object)null, new Object[0]);
                  oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new Object[]{(new File(this.field_146297_k.field_71412_D, Shaders.shaderpacksdirname)).toURI()});
               } catch (Throwable throwable) {
                  throwable.printStackTrace();
                  flag = true;
               }

               if(flag) {
                  Config.dbg("Opening via system class!");
                  Sys.openURL("file://" + Shaders.shaderpacksdir.getAbsolutePath());
               }
               break;
            case 202:
               new File(Shaders.shadersdir, "current.cfg");
               Shaders.storeConfig();
               this.saved = true;
               this.field_146297_k.func_147108_a(this.parentGui);
               break;
            case 203:
               GuiShaderOptions guishaderoptions = new GuiShaderOptions(this, Config.getGameSettings());
               Config.getMinecraft().func_147108_a(guishaderoptions);
               break;
            default:
               this.shaderList.func_148147_a(button);
            }

         }
      }
   }

   public void func_146281_b() {
      super.func_146281_b();
      if(!this.saved) {
         Shaders.storeConfig();
      }

   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      this.func_146276_q_();
      this.shaderList.func_148128_a(mouseX, mouseY, partialTicks);
      if(this.updateTimer <= 0) {
         this.shaderList.updateList();
         this.updateTimer += 20;
      }

      this.func_73732_a(this.field_146289_q, this.screenTitle + " ", this.field_146294_l / 2, 15, 16777215);
      String s = "OpenGL: " + Shaders.glVersionString + ", " + Shaders.glVendorString + ", " + Shaders.glRendererString;
      int i = this.field_146289_q.func_78256_a(s);
      if(i < this.field_146294_l - 5) {
         this.func_73732_a(this.field_146289_q, s, this.field_146294_l / 2, this.field_146295_m - 40, 8421504);
      } else {
         this.func_73731_b(this.field_146289_q, s, 5, this.field_146295_m - 40, 8421504);
      }

      super.func_73863_a(mouseX, mouseY, partialTicks);
      this.tooltipManager.drawTooltips(mouseX, mouseY, this.field_146292_n);
   }

   public void func_73876_c() {
      super.func_73876_c();
      --this.updateTimer;
   }

   public Minecraft getMc() {
      return this.field_146297_k;
   }

   public void drawCenteredString(String text, int x, int y, int color) {
      this.func_73732_a(this.field_146289_q, text, x, y, color);
   }

   public static String toStringOnOff(boolean value) {
      String s = Lang.getOn();
      String s1 = Lang.getOff();
      return value?s:s1;
   }

   public static String toStringAa(int value) {
      return value == 2?"FXAA 2x":(value == 4?"FXAA 4x":Lang.getOff());
   }

   public static String toStringValue(float val, float[] values, String[] names) {
      int i = getValueIndex(val, values);
      return names[i];
   }

   public static int getValueIndex(float val, float[] values) {
      for(int i = 0; i < values.length; ++i) {
         float f = values[i];
         if(f >= val) {
            return i;
         }
      }

      return values.length - 1;
   }

   public static String toStringQuality(float val) {
      return toStringValue(val, QUALITY_MULTIPLIERS, QUALITY_MULTIPLIER_NAMES);
   }

   public static String toStringHandDepth(float val) {
      return toStringValue(val, HAND_DEPTH_VALUES, HAND_DEPTH_NAMES);
   }

   public static int getOSType() {
      String s = System.getProperty("os.name").toLowerCase();
      return s.contains("win")?1:(s.contains("mac")?2:(s.contains("solaris")?3:(s.contains("sunos")?3:(s.contains("linux")?4:(s.contains("unix")?4:0)))));
   }
}
