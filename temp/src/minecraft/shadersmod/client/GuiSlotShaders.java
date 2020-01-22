package shadersmod.client;

import java.util.ArrayList;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.src.Lang;
import shadersmod.client.GuiShaders;
import shadersmod.client.Shaders;

class GuiSlotShaders extends GuiSlot {
   private ArrayList shaderslist;
   private int selectedIndex;
   private long lastClickedCached = 0L;
   final GuiShaders shadersGui;

   public GuiSlotShaders(GuiShaders par1GuiShaders, int width, int height, int top, int bottom, int slotHeight) {
      super(par1GuiShaders.getMc(), width, height, top, bottom, slotHeight);
      this.shadersGui = par1GuiShaders;
      this.updateList();
      this.field_148169_q = 0.0F;
      int i = this.selectedIndex * slotHeight;
      int j = (bottom - top) / 2;
      if(i > j) {
         this.func_148145_f(i - j);
      }

   }

   public int func_148139_c() {
      return this.field_148155_a - 20;
   }

   public void updateList() {
      this.shaderslist = Shaders.listOfShaders();
      this.selectedIndex = 0;
      int i = 0;

      for(int j = this.shaderslist.size(); i < j; ++i) {
         if(((String)this.shaderslist.get(i)).equals(Shaders.currentshadername)) {
            this.selectedIndex = i;
            break;
         }
      }

   }

   protected int func_148127_b() {
      return this.shaderslist.size();
   }

   protected void func_148144_a(int index, boolean doubleClicked, int mouseX, int mouseY) {
      if(index != this.selectedIndex || this.field_148167_s != this.lastClickedCached) {
         this.selectedIndex = index;
         this.lastClickedCached = this.field_148167_s;
         Shaders.setShaderPack((String)this.shaderslist.get(index));
         Shaders.uninit();
         this.shadersGui.updateButtons();
      }
   }

   protected boolean func_148131_a(int index) {
      return index == this.selectedIndex;
   }

   protected int func_148137_d() {
      return this.field_148155_a - 6;
   }

   protected int func_148138_e() {
      return this.func_148127_b() * 18;
   }

   protected void func_148123_a() {
   }

   protected void func_180791_a(int index, int posX, int posY, int contentY, int mouseX, int mouseY) {
      String s = (String)this.shaderslist.get(index);
      if(s.equals(Shaders.packNameNone)) {
         s = Lang.get("of.options.shaders.packNone");
      } else if(s.equals(Shaders.packNameDefault)) {
         s = Lang.get("of.options.shaders.packDefault");
      }

      this.shadersGui.drawCenteredString(s, this.field_148155_a / 2, posY + 1, 14737632);
   }

   public int getSelectedIndex() {
      return this.selectedIndex;
   }
}
