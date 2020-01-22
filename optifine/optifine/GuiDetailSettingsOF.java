package optifine;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import optifine.GuiOptionButtonOF;
import optifine.GuiOptionSliderOF;
import optifine.TooltipManager;
import optifine.TooltipProviderOptions;

public class GuiDetailSettingsOF extends GuiScreen {

   public GuiScreen prevScreen;
   public String title;
   public GameSettings settings;
   public static GameSettings.Options[] enumOptions = new GameSettings.Options[]{GameSettings.Options.CLOUDS, GameSettings.Options.CLOUD_HEIGHT, GameSettings.Options.TREES, GameSettings.Options.RAIN, GameSettings.Options.SKY, GameSettings.Options.STARS, GameSettings.Options.SUN_MOON, GameSettings.Options.SHOW_CAPES, GameSettings.Options.FOG_FANCY, GameSettings.Options.FOG_START, GameSettings.Options.TRANSLUCENT_BLOCKS, GameSettings.Options.HELD_ITEM_TOOLTIPS, GameSettings.Options.DROPPED_ITEMS, GameSettings.Options.ENTITY_SHADOWS, GameSettings.Options.SWAMP_COLORS, GameSettings.Options.SMOOTH_BIOMES, GameSettings.Options.VIGNETTE};
   public TooltipManager tooltipManager = new TooltipManager(this, new TooltipProviderOptions());


   public GuiDetailSettingsOF(GuiScreen guiscreen, GameSettings gamesettings) {
      this.prevScreen = guiscreen;
      this.settings = gamesettings;
   }

   public void initGui() {
      this.title = I18n.format("of.options.detailsTitle", new Object[0]);
      this.buttonList.clear();

      for(int i = 0; i < enumOptions.length; ++i) {
         GameSettings.Options opt = enumOptions[i];
         int x = this.width / 2 - 155 + i % 2 * 160;
         int y = this.height / 6 + 21 * (i / 2) - 12;
         if(!opt.getEnumFloat()) {
            this.buttonList.add(new GuiOptionButtonOF(opt.returnEnumOrdinal(), x, y, opt, this.settings.getKeyBinding(opt)));
         } else {
            this.buttonList.add(new GuiOptionSliderOF(opt.returnEnumOrdinal(), x, y, opt));
         }
      }

      this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / 6 + 168 + 11, I18n.format("gui.done", new Object[0])));
   }

   public void actionPerformed(GuiButton guibutton) {
      if(guibutton.enabled) {
         if(guibutton.id < 200 && guibutton instanceof GuiOptionButton) {
            this.settings.setOptionValue(((GuiOptionButton)guibutton).returnEnumOptions(), 1);
            guibutton.displayString = this.settings.getKeyBinding(GameSettings.Options.getEnumOptions(guibutton.id));
         }

         if(guibutton.id == 200) {
            this.mc.gameSettings.saveOptions();
            this.mc.displayGuiScreen(this.prevScreen);
         }

      }
   }

   public void drawScreen(int x, int y, float f) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 15, 16777215);
      super.drawScreen(x, y, f);
      this.tooltipManager.drawTooltips(x, y, this.buttonList);
   }

}
