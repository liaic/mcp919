package net.minecraft.src;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiVideoSettings;

public class GuiScreenOF extends GuiScreen {
   protected void actionPerformedRightClick(GuiButton p_actionPerformedRightClick_1_) throws IOException {
   }

   protected void func_73864_a(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException {
      super.func_73864_a(p_73864_1_, p_73864_2_, p_73864_3_);
      if(p_73864_3_ == 1) {
         GuiButton guibutton = getSelectedButton(p_73864_1_, p_73864_2_, this.field_146292_n);
         if(guibutton != null && guibutton.field_146124_l) {
            guibutton.func_146113_a(this.field_146297_k.func_147118_V());
            this.actionPerformedRightClick(guibutton);
         }
      }

   }

   public static GuiButton getSelectedButton(int p_getSelectedButton_0_, int p_getSelectedButton_1_, List<GuiButton> p_getSelectedButton_2_) {
      for(int i = 0; i < p_getSelectedButton_2_.size(); ++i) {
         GuiButton guibutton = (GuiButton)p_getSelectedButton_2_.get(i);
         if(guibutton.field_146125_m) {
            int j = GuiVideoSettings.getButtonWidth(guibutton);
            int k = GuiVideoSettings.getButtonHeight(guibutton);
            if(p_getSelectedButton_0_ >= guibutton.field_146128_h && p_getSelectedButton_1_ >= guibutton.field_146129_i && p_getSelectedButton_0_ < guibutton.field_146128_h + j && p_getSelectedButton_1_ < guibutton.field_146129_i + k) {
               return guibutton;
            }
         }
      }

      return null;
   }
}
