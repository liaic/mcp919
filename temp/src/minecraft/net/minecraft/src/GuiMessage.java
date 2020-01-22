package net.minecraft.src;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.src.Config;

public class GuiMessage extends GuiScreen {
   private GuiScreen parentScreen;
   private String messageLine1;
   private String messageLine2;
   private final List listLines2 = Lists.newArrayList();
   protected String confirmButtonText;
   private int ticksUntilEnable;

   public GuiMessage(GuiScreen p_i54_1_, String p_i54_2_, String p_i54_3_) {
      this.parentScreen = p_i54_1_;
      this.messageLine1 = p_i54_2_;
      this.messageLine2 = p_i54_3_;
      this.confirmButtonText = I18n.func_135052_a("gui.done", new Object[0]);
   }

   public void func_73866_w_() {
      this.field_146292_n.add(new GuiOptionButton(0, this.field_146294_l / 2 - 74, this.field_146295_m / 6 + 96, this.confirmButtonText));
      this.listLines2.clear();
      this.listLines2.addAll(this.field_146289_q.func_78271_c(this.messageLine2, this.field_146294_l - 50));
   }

   protected void func_146284_a(GuiButton p_146284_1_) throws IOException {
      Config.getMinecraft().func_147108_a(this.parentScreen);
   }

   public void func_73863_a(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
      this.func_146276_q_();
      this.func_73732_a(this.field_146289_q, this.messageLine1, this.field_146294_l / 2, 70, 16777215);
      int i = 90;

      for(String s : this.listLines2) {
         this.func_73732_a(this.field_146289_q, s, this.field_146294_l / 2, i, 16777215);
         i += this.field_146289_q.field_78288_b;
      }

      super.func_73863_a(p_73863_1_, p_73863_2_, p_73863_3_);
   }

   public void setButtonDelay(int p_setButtonDelay_1_) {
      this.ticksUntilEnable = p_setButtonDelay_1_;

      for(GuiButton guibutton : this.field_146292_n) {
         guibutton.field_146124_l = false;
      }

   }

   public void func_73876_c() {
      super.func_73876_c();
      if(--this.ticksUntilEnable == 0) {
         for(GuiButton guibutton : this.field_146292_n) {
            guibutton.field_146124_l = true;
         }
      }

   }
}
