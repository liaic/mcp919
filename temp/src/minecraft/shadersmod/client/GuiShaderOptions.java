package shadersmod.client;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.src.Config;
import net.minecraft.src.GuiScreenOF;
import net.minecraft.src.Lang;
import net.minecraft.src.TooltipManager;
import net.minecraft.src.TooltipProviderShaderOptions;
import net.minecraft.util.MathHelper;
import shadersmod.client.GuiButtonShaderOption;
import shadersmod.client.GuiSliderShaderOption;
import shadersmod.client.ShaderOption;
import shadersmod.client.ShaderOptionProfile;
import shadersmod.client.ShaderOptionScreen;
import shadersmod.client.Shaders;

public class GuiShaderOptions extends GuiScreenOF {
   private GuiScreen prevScreen;
   protected String title;
   private GameSettings settings;
   private TooltipManager tooltipManager;
   private String screenName;
   private String screenText;
   private boolean changed;
   public static final String OPTION_PROFILE = "<profile>";
   public static final String OPTION_EMPTY = "<empty>";
   public static final String OPTION_REST = "*";

   public GuiShaderOptions(GuiScreen guiscreen, GameSettings gamesettings) {
      this.tooltipManager = new TooltipManager(this, new TooltipProviderShaderOptions());
      this.screenName = null;
      this.screenText = null;
      this.changed = false;
      this.title = "Shader Options";
      this.prevScreen = guiscreen;
      this.settings = gamesettings;
   }

   public GuiShaderOptions(GuiScreen guiscreen, GameSettings gamesettings, String screenName) {
      this(guiscreen, gamesettings);
      this.screenName = screenName;
      if(screenName != null) {
         this.screenText = Shaders.translate("screen." + screenName, screenName);
      }

   }

   public void func_73866_w_() {
      this.title = I18n.func_135052_a("of.options.shaderOptionsTitle", new Object[0]);
      int i = 100;
      int j = 0;
      int k = 30;
      int l = 20;
      int i1 = 120;
      int j1 = 20;
      int k1 = Shaders.getShaderPackColumns(this.screenName, 2);
      ShaderOption[] ashaderoption = Shaders.getShaderPackOptions(this.screenName);
      if(ashaderoption != null) {
         int l1 = MathHelper.func_76143_f((double)ashaderoption.length / 9.0D);
         if(k1 < l1) {
            k1 = l1;
         }

         for(int i2 = 0; i2 < ashaderoption.length; ++i2) {
            ShaderOption shaderoption = ashaderoption[i2];
            if(shaderoption != null && shaderoption.isVisible()) {
               int j2 = i2 % k1;
               int k2 = i2 / k1;
               int l2 = Math.min(this.field_146294_l / k1, 200);
               j = (this.field_146294_l - l2 * k1) / 2;
               int i3 = j2 * l2 + 5 + j;
               int j3 = k + k2 * l;
               int k3 = l2 - 10;
               String s = getButtonText(shaderoption, k3);
               GuiButtonShaderOption guibuttonshaderoption;
               if(Shaders.isShaderPackOptionSlider(shaderoption.getName())) {
                  guibuttonshaderoption = new GuiSliderShaderOption(i + i2, i3, j3, k3, j1, shaderoption, s);
               } else {
                  guibuttonshaderoption = new GuiButtonShaderOption(i + i2, i3, j3, k3, j1, shaderoption, s);
               }

               guibuttonshaderoption.field_146124_l = shaderoption.isEnabled();
               this.field_146292_n.add(guibuttonshaderoption);
            }
         }
      }

      this.field_146292_n.add(new GuiButton(201, this.field_146294_l / 2 - i1 - 20, this.field_146295_m / 6 + 168 + 11, i1, j1, I18n.func_135052_a("controls.reset", new Object[0])));
      this.field_146292_n.add(new GuiButton(200, this.field_146294_l / 2 + 20, this.field_146295_m / 6 + 168 + 11, i1, j1, I18n.func_135052_a("gui.done", new Object[0])));
   }

   public static String getButtonText(ShaderOption so, int btnWidth) {
      String s = so.getNameText();
      if(so instanceof ShaderOptionScreen) {
         ShaderOptionScreen shaderoptionscreen = (ShaderOptionScreen)so;
         return s + "...";
      } else {
         FontRenderer fontrenderer = Config.getMinecraft().field_71466_p;

         for(int i = fontrenderer.func_78256_a(": " + Lang.getOff()) + 5; fontrenderer.func_78256_a(s) + i >= btnWidth && s.length() > 0; s = s.substring(0, s.length() - 1)) {
            ;
         }

         String s1 = so.isChanged()?so.getValueColor(so.getValue()):"";
         String s2 = so.getValueText(so.getValue());
         return s + ": " + s1 + s2;
      }
   }

   protected void func_146284_a(GuiButton guibutton) {
      if(guibutton.field_146124_l) {
         if(guibutton.field_146127_k < 200 && guibutton instanceof GuiButtonShaderOption) {
            GuiButtonShaderOption guibuttonshaderoption = (GuiButtonShaderOption)guibutton;
            ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();
            if(shaderoption instanceof ShaderOptionScreen) {
               String s = shaderoption.getName();
               GuiShaderOptions guishaderoptions = new GuiShaderOptions(this, this.settings, s);
               this.field_146297_k.func_147108_a(guishaderoptions);
               return;
            }

            if(func_146272_n()) {
               shaderoption.resetValue();
            } else {
               shaderoption.nextValue();
            }

            this.updateAllButtons();
            this.changed = true;
         }

         if(guibutton.field_146127_k == 201) {
            ShaderOption[] ashaderoption = Shaders.getChangedOptions(Shaders.getShaderPackOptions());

            for(int i = 0; i < ashaderoption.length; ++i) {
               ShaderOption shaderoption1 = ashaderoption[i];
               shaderoption1.resetValue();
               this.changed = true;
            }

            this.updateAllButtons();
         }

         if(guibutton.field_146127_k == 200) {
            if(this.changed) {
               Shaders.saveShaderPackOptions();
               this.changed = false;
               Shaders.uninit();
            }

            this.field_146297_k.func_147108_a(this.prevScreen);
         }

      }
   }

   protected void actionPerformedRightClick(GuiButton btn) {
      if(btn instanceof GuiButtonShaderOption) {
         GuiButtonShaderOption guibuttonshaderoption = (GuiButtonShaderOption)btn;
         ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();
         if(func_146272_n()) {
            shaderoption.resetValue();
         } else {
            shaderoption.prevValue();
         }

         this.updateAllButtons();
         this.changed = true;
      }

   }

   public void func_146281_b() {
      super.func_146281_b();
      if(this.changed) {
         Shaders.saveShaderPackOptions();
         this.changed = false;
         Shaders.uninit();
      }

   }

   private void updateAllButtons() {
      for(GuiButton guibutton : this.field_146292_n) {
         if(guibutton instanceof GuiButtonShaderOption) {
            GuiButtonShaderOption guibuttonshaderoption = (GuiButtonShaderOption)guibutton;
            ShaderOption shaderoption = guibuttonshaderoption.getShaderOption();
            if(shaderoption instanceof ShaderOptionProfile) {
               ShaderOptionProfile shaderoptionprofile = (ShaderOptionProfile)shaderoption;
               shaderoptionprofile.updateProfile();
            }

            guibuttonshaderoption.field_146126_j = getButtonText(shaderoption, guibuttonshaderoption.func_146117_b());
            guibuttonshaderoption.valueChanged();
         }
      }

   }

   public void func_73863_a(int x, int y, float f) {
      this.func_146276_q_();
      if(this.screenText != null) {
         this.func_73732_a(this.field_146289_q, this.screenText, this.field_146294_l / 2, 15, 16777215);
      } else {
         this.func_73732_a(this.field_146289_q, this.title, this.field_146294_l / 2, 15, 16777215);
      }

      super.func_73863_a(x, y, f);
      this.tooltipManager.drawTooltips(x, y, this.field_146292_n);
   }
}
