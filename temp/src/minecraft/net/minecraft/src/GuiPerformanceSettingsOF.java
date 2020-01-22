package net.minecraft.src;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.src.GuiOptionButtonOF;
import net.minecraft.src.GuiOptionSliderOF;
import net.minecraft.src.TooltipManager;
import net.minecraft.src.TooltipProviderOptions;

public class GuiPerformanceSettingsOF extends GuiScreen {
   private GuiScreen prevScreen;
   protected String title;
   private GameSettings settings;
   private static GameSettings.Options[] enumOptions = new GameSettings.Options[]{GameSettings.Options.SMOOTH_FPS, GameSettings.Options.SMOOTH_WORLD, GameSettings.Options.FAST_RENDER, GameSettings.Options.FAST_MATH, GameSettings.Options.CHUNK_UPDATES, GameSettings.Options.CHUNK_UPDATES_DYNAMIC, GameSettings.Options.LAZY_CHUNK_LOADING};
   private TooltipManager tooltipManager = new TooltipManager(this, new TooltipProviderOptions());

   public GuiPerformanceSettingsOF(GuiScreen p_i58_1_, GameSettings p_i58_2_) {
      this.prevScreen = p_i58_1_;
      this.settings = p_i58_2_;
   }

   public void func_73866_w_() {
      this.title = I18n.func_135052_a("of.options.performanceTitle", new Object[0]);
      this.field_146292_n.clear();

      for(int i = 0; i < enumOptions.length; ++i) {
         GameSettings.Options gamesettings$options = enumOptions[i];
         int j = this.field_146294_l / 2 - 155 + i % 2 * 160;
         int k = this.field_146295_m / 6 + 21 * (i / 2) - 12;
         if(!gamesettings$options.func_74380_a()) {
            this.field_146292_n.add(new GuiOptionButtonOF(gamesettings$options.func_74381_c(), j, k, gamesettings$options, this.settings.func_74297_c(gamesettings$options)));
         } else {
            this.field_146292_n.add(new GuiOptionSliderOF(gamesettings$options.func_74381_c(), j, k, gamesettings$options));
         }
      }

      this.field_146292_n.add(new GuiButton(200, this.field_146294_l / 2 - 100, this.field_146295_m / 6 + 168 + 11, I18n.func_135052_a("gui.done", new Object[0])));
   }

   protected void func_146284_a(GuiButton p_146284_1_) {
      if(p_146284_1_.field_146124_l) {
         if(p_146284_1_.field_146127_k < 200 && p_146284_1_ instanceof GuiOptionButton) {
            this.settings.func_74306_a(((GuiOptionButton)p_146284_1_).func_146136_c(), 1);
            p_146284_1_.field_146126_j = this.settings.func_74297_c(GameSettings.Options.func_74379_a(p_146284_1_.field_146127_k));
         }

         if(p_146284_1_.field_146127_k == 200) {
            this.field_146297_k.field_71474_y.func_74303_b();
            this.field_146297_k.func_147108_a(this.prevScreen);
         }

      }
   }

   public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
      this.func_146276_q_();
      this.func_73732_a(this.field_146289_q, this.title, this.field_146294_l / 2, 15, 16777215);
      super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
      this.tooltipManager.drawTooltips(p_73863_1_, p_73863_2_, this.field_146292_n);
   }
}
