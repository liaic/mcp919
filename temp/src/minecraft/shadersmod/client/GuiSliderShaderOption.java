package shadersmod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import shadersmod.client.GuiButtonShaderOption;
import shadersmod.client.GuiShaderOptions;
import shadersmod.client.ShaderOption;

public class GuiSliderShaderOption extends GuiButtonShaderOption {
   private float sliderValue = 1.0F;
   public boolean dragging;
   private ShaderOption shaderOption = null;

   public GuiSliderShaderOption(int buttonId, int x, int y, int w, int h, ShaderOption shaderOption, String text) {
      super(buttonId, x, y, w, h, shaderOption, text);
      this.shaderOption = shaderOption;
      this.sliderValue = shaderOption.getIndexNormalized();
      this.field_146126_j = GuiShaderOptions.getButtonText(shaderOption, this.field_146120_f);
   }

   protected int func_146114_a(boolean mouseOver) {
      return 0;
   }

   protected void func_146119_b(Minecraft mc, int mouseX, int mouseY) {
      if(this.field_146125_m) {
         if(this.dragging) {
            this.sliderValue = (float)(mouseX - (this.field_146128_h + 4)) / (float)(this.field_146120_f - 8);
            this.sliderValue = MathHelper.func_76131_a(this.sliderValue, 0.0F, 1.0F);
            this.shaderOption.setIndexNormalized(this.sliderValue);
            this.sliderValue = this.shaderOption.getIndexNormalized();
            this.field_146126_j = GuiShaderOptions.getButtonText(this.shaderOption, this.field_146120_f);
         }

         mc.func_110434_K().func_110577_a(field_146122_a);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
         this.func_73729_b(this.field_146128_h + (int)(this.sliderValue * (float)(this.field_146120_f - 8)), this.field_146129_i, 0, 66, 4, 20);
         this.func_73729_b(this.field_146128_h + (int)(this.sliderValue * (float)(this.field_146120_f - 8)) + 4, this.field_146129_i, 196, 66, 4, 20);
      }

   }

   public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY) {
      if(super.func_146116_c(mc, mouseX, mouseY)) {
         this.sliderValue = (float)(mouseX - (this.field_146128_h + 4)) / (float)(this.field_146120_f - 8);
         this.sliderValue = MathHelper.func_76131_a(this.sliderValue, 0.0F, 1.0F);
         this.shaderOption.setIndexNormalized(this.sliderValue);
         this.field_146126_j = GuiShaderOptions.getButtonText(this.shaderOption, this.field_146120_f);
         this.dragging = true;
         return true;
      } else {
         return false;
      }
   }

   public void func_146118_a(int mouseX, int mouseY) {
      this.dragging = false;
   }

   public void valueChanged() {
      this.sliderValue = this.shaderOption.getIndexNormalized();
   }
}
