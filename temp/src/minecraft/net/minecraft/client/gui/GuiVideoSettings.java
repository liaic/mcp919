package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.src.Config;
import net.minecraft.src.GuiAnimationSettingsOF;
import net.minecraft.src.GuiDetailSettingsOF;
import net.minecraft.src.GuiOptionButtonOF;
import net.minecraft.src.GuiOptionSliderOF;
import net.minecraft.src.GuiOtherSettingsOF;
import net.minecraft.src.GuiPerformanceSettingsOF;
import net.minecraft.src.GuiQualitySettingsOF;
import net.minecraft.src.GuiScreenOF;
import net.minecraft.src.Lang;
import net.minecraft.src.TooltipManager;
import net.minecraft.src.TooltipProviderOptions;
import shadersmod.client.GuiShaders;

public class GuiVideoSettings extends GuiScreenOF {
   private GuiScreen field_146498_f;
   protected String field_146500_a = "Video Settings";
   private GameSettings field_146499_g;
   private static GameSettings.Options[] field_146502_i = new GameSettings.Options[]{GameSettings.Options.GRAPHICS, GameSettings.Options.RENDER_DISTANCE, GameSettings.Options.AMBIENT_OCCLUSION, GameSettings.Options.FRAMERATE_LIMIT, GameSettings.Options.AO_LEVEL, GameSettings.Options.VIEW_BOBBING, GameSettings.Options.GUI_SCALE, GameSettings.Options.USE_VBO, GameSettings.Options.GAMMA, GameSettings.Options.BLOCK_ALTERNATIVES, GameSettings.Options.DYNAMIC_LIGHTS, GameSettings.Options.DYNAMIC_FOV};
   private static final String __OBFID = "CL_00000718";
   private TooltipManager tooltipManager = new TooltipManager(this, new TooltipProviderOptions());

   public GuiVideoSettings(GuiScreen p_i1062_1_, GameSettings p_i1062_2_) {
      this.field_146498_f = p_i1062_1_;
      this.field_146499_g = p_i1062_2_;
   }

   public void func_73866_w_() {
      this.field_146500_a = I18n.func_135052_a("options.videoTitle", new Object[0]);
      this.field_146292_n.clear();

      for(int i = 0; i < field_146502_i.length; ++i) {
         GameSettings.Options gamesettings$options = field_146502_i[i];
         if(gamesettings$options != null) {
            int j = this.field_146294_l / 2 - 155 + i % 2 * 160;
            int k = this.field_146295_m / 6 + 21 * (i / 2) - 12;
            if(gamesettings$options.func_74380_a()) {
               this.field_146292_n.add(new GuiOptionSliderOF(gamesettings$options.func_74381_c(), j, k, gamesettings$options));
            } else {
               this.field_146292_n.add(new GuiOptionButtonOF(gamesettings$options.func_74381_c(), j, k, gamesettings$options, this.field_146499_g.func_74297_c(gamesettings$options)));
            }
         }
      }

      int l = this.field_146295_m / 6 + 21 * (field_146502_i.length / 2) - 12;
      int i1 = 0;
      i1 = this.field_146294_l / 2 - 155 + 0;
      this.field_146292_n.add(new GuiOptionButton(231, i1, l, Lang.get("of.options.shaders")));
      i1 = this.field_146294_l / 2 - 155 + 160;
      this.field_146292_n.add(new GuiOptionButton(202, i1, l, Lang.get("of.options.quality")));
      l = l + 21;
      i1 = this.field_146294_l / 2 - 155 + 0;
      this.field_146292_n.add(new GuiOptionButton(201, i1, l, Lang.get("of.options.details")));
      i1 = this.field_146294_l / 2 - 155 + 160;
      this.field_146292_n.add(new GuiOptionButton(212, i1, l, Lang.get("of.options.performance")));
      l = l + 21;
      i1 = this.field_146294_l / 2 - 155 + 0;
      this.field_146292_n.add(new GuiOptionButton(211, i1, l, Lang.get("of.options.animations")));
      i1 = this.field_146294_l / 2 - 155 + 160;
      this.field_146292_n.add(new GuiOptionButton(222, i1, l, Lang.get("of.options.other")));
      l = l + 21;
      this.field_146292_n.add(new GuiButton(200, this.field_146294_l / 2 - 100, this.field_146295_m / 6 + 168 + 11, I18n.func_135052_a("gui.done", new Object[0])));
   }

   protected void func_146284_a(GuiButton p_146284_1_) throws IOException {
      this.actionPerformed(p_146284_1_, 1);
   }

   protected void actionPerformedRightClick(GuiButton p_actionPerformedRightClick_1_) {
      if(p_actionPerformedRightClick_1_.field_146127_k == GameSettings.Options.GUI_SCALE.ordinal()) {
         this.actionPerformed(p_actionPerformedRightClick_1_, -1);
      }

   }

   private void actionPerformed(GuiButton p_actionPerformed_1_, int p_actionPerformed_2_) {
      if(p_actionPerformed_1_.field_146124_l) {
         int i = this.field_146499_g.field_74335_Z;
         if(p_actionPerformed_1_.field_146127_k < 200 && p_actionPerformed_1_ instanceof GuiOptionButton) {
            this.field_146499_g.func_74306_a(((GuiOptionButton)p_actionPerformed_1_).func_146136_c(), p_actionPerformed_2_);
            p_actionPerformed_1_.field_146126_j = this.field_146499_g.func_74297_c(GameSettings.Options.func_74379_a(p_actionPerformed_1_.field_146127_k));
         }

         if(p_actionPerformed_1_.field_146127_k == 200) {
            this.field_146297_k.field_71474_y.func_74303_b();
            this.field_146297_k.func_147108_a(this.field_146498_f);
         }

         if(this.field_146499_g.field_74335_Z != i) {
            ScaledResolution scaledresolution = new ScaledResolution(this.field_146297_k);
            int j = scaledresolution.func_78326_a();
            int k = scaledresolution.func_78328_b();
            this.func_146280_a(this.field_146297_k, j, k);
         }

         if(p_actionPerformed_1_.field_146127_k == 201) {
            this.field_146297_k.field_71474_y.func_74303_b();
            GuiDetailSettingsOF guidetailsettingsof = new GuiDetailSettingsOF(this, this.field_146499_g);
            this.field_146297_k.func_147108_a(guidetailsettingsof);
         }

         if(p_actionPerformed_1_.field_146127_k == 202) {
            this.field_146297_k.field_71474_y.func_74303_b();
            GuiQualitySettingsOF guiqualitysettingsof = new GuiQualitySettingsOF(this, this.field_146499_g);
            this.field_146297_k.func_147108_a(guiqualitysettingsof);
         }

         if(p_actionPerformed_1_.field_146127_k == 211) {
            this.field_146297_k.field_71474_y.func_74303_b();
            GuiAnimationSettingsOF guianimationsettingsof = new GuiAnimationSettingsOF(this, this.field_146499_g);
            this.field_146297_k.func_147108_a(guianimationsettingsof);
         }

         if(p_actionPerformed_1_.field_146127_k == 212) {
            this.field_146297_k.field_71474_y.func_74303_b();
            GuiPerformanceSettingsOF guiperformancesettingsof = new GuiPerformanceSettingsOF(this, this.field_146499_g);
            this.field_146297_k.func_147108_a(guiperformancesettingsof);
         }

         if(p_actionPerformed_1_.field_146127_k == 222) {
            this.field_146297_k.field_71474_y.func_74303_b();
            GuiOtherSettingsOF guiothersettingsof = new GuiOtherSettingsOF(this, this.field_146499_g);
            this.field_146297_k.func_147108_a(guiothersettingsof);
         }

         if(p_actionPerformed_1_.field_146127_k == 231) {
            if(Config.isAntialiasing() || Config.isAntialiasingConfigured()) {
               Config.showGuiMessage(Lang.get("of.message.shaders.aa1"), Lang.get("of.message.shaders.aa2"));
               return;
            }

            if(Config.isAnisotropicFiltering()) {
               Config.showGuiMessage(Lang.get("of.message.shaders.af1"), Lang.get("of.message.shaders.af2"));
               return;
            }

            if(Config.isFastRender()) {
               Config.showGuiMessage(Lang.get("of.message.shaders.fr1"), Lang.get("of.message.shaders.fr2"));
               return;
            }

            if(Config.getGameSettings().field_74337_g) {
               Config.showGuiMessage(Lang.get("of.message.shaders.an1"), Lang.get("of.message.shaders.an2"));
               return;
            }

            this.field_146297_k.field_71474_y.func_74303_b();
            GuiShaders guishaders = new GuiShaders(this, this.field_146499_g);
            this.field_146297_k.func_147108_a(guishaders);
         }

      }
   }

   public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
      this.func_146276_q_();
      this.func_73732_a(this.field_146289_q, this.field_146500_a, this.field_146294_l / 2, 15, 16777215);
      String s = Config.getVersion();
      String s1 = "HD_U";
      if(s1.equals("HD")) {
         s = "OptiFine HD I7";
      }

      if(s1.equals("HD_U")) {
         s = "OptiFine HD I7 Ultra";
      }

      if(s1.equals("L")) {
         s = "OptiFine I7 Light";
      }

      this.func_73731_b(this.field_146289_q, s, 2, this.field_146295_m - 10, 8421504);
      String s2 = "Minecraft 1.8.9";
      int i = this.field_146289_q.func_78256_a(s2);
      this.func_73731_b(this.field_146289_q, s2, this.field_146294_l - i - 2, this.field_146295_m - 10, 8421504);
      super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
      this.tooltipManager.drawTooltips(p_73863_1_, p_73863_2_, this.field_146292_n);
   }

   public static int getButtonWidth(GuiButton p_getButtonWidth_0_) {
      return p_getButtonWidth_0_.field_146120_f;
   }

   public static int getButtonHeight(GuiButton p_getButtonHeight_0_) {
      return p_getButtonHeight_0_.field_146121_g;
   }

   public static void drawGradientRect(GuiScreen p_drawGradientRect_0_, int p_drawGradientRect_1_, int p_drawGradientRect_2_, int p_drawGradientRect_3_, int p_drawGradientRect_4_, int p_drawGradientRect_5_, int p_drawGradientRect_6_) {
      p_drawGradientRect_0_.func_73733_a(p_drawGradientRect_1_, p_drawGradientRect_2_, p_drawGradientRect_3_, p_drawGradientRect_4_, p_drawGradientRect_5_, p_drawGradientRect_6_);
   }
}
