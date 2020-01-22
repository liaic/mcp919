package optifine;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiVideoSettings;

public class GuiScreenOF extends GuiScreen {

   public void actionPerformedRightClick(GuiButton button) throws IOException {}

   public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      if(mouseButton == 1) {
         GuiButton btn = getSelectedButton(mouseX, mouseY, this.buttonList);
         if(btn != null && btn.enabled) {
            btn.playPressSound(this.mc.getSoundHandler());
            this.actionPerformedRightClick(btn);
         }
      }

   }

   public static GuiButton getSelectedButton(int x, int y, List<GuiButton> listButtons) {
      for(int i = 0; i < listButtons.size(); ++i) {
         GuiButton btn = (GuiButton)listButtons.get(i);
         if(btn.visible) {
            int btnWidth = GuiVideoSettings.getButtonWidth(btn);
            int btnHeight = GuiVideoSettings.getButtonHeight(btn);
            if(x >= btn.xPosition && y >= btn.yPosition && x < btn.xPosition + btnWidth && y < btn.yPosition + btnHeight) {
               return btn;
            }
         }
      }

      return null;
   }
}
