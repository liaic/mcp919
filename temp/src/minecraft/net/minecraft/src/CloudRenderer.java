package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class CloudRenderer {
   private Minecraft mc;
   private boolean updated = false;
   private boolean renderFancy = false;
   int cloudTickCounter;
   private Vec3 cloudColor;
   float partialTicks;
   private boolean updateRenderFancy = false;
   private int updateCloudTickCounter = 0;
   private Vec3 updateCloudColor = new Vec3(-1.0D, -1.0D, -1.0D);
   private double updatePlayerX = 0.0D;
   private double updatePlayerY = 0.0D;
   private double updatePlayerZ = 0.0D;
   private int glListClouds = -1;

   public CloudRenderer(Minecraft p_i24_1_) {
      this.mc = p_i24_1_;
      this.glListClouds = GLAllocation.func_74526_a(1);
   }

   public void prepareToRender(boolean p_prepareToRender_1_, int p_prepareToRender_2_, float p_prepareToRender_3_, Vec3 p_prepareToRender_4_) {
      this.renderFancy = p_prepareToRender_1_;
      this.cloudTickCounter = p_prepareToRender_2_;
      this.partialTicks = p_prepareToRender_3_;
      this.cloudColor = p_prepareToRender_4_;
   }

   public boolean shouldUpdateGlList() {
      if(!this.updated) {
         return true;
      } else if(this.renderFancy != this.updateRenderFancy) {
         return true;
      } else if(this.cloudTickCounter >= this.updateCloudTickCounter + 20) {
         return true;
      } else if(Math.abs(this.cloudColor.field_72450_a - this.updateCloudColor.field_72450_a) > 0.003D) {
         return true;
      } else if(Math.abs(this.cloudColor.field_72448_b - this.updateCloudColor.field_72448_b) > 0.003D) {
         return true;
      } else if(Math.abs(this.cloudColor.field_72449_c - this.updateCloudColor.field_72449_c) > 0.003D) {
         return true;
      } else {
         Entity entity = this.mc.func_175606_aa();
         boolean flag = this.updatePlayerY + (double)entity.func_70047_e() < 128.0D + (double)(this.mc.field_71474_y.ofCloudsHeight * 128.0F);
         boolean flag1 = entity.field_70167_r + (double)entity.func_70047_e() < 128.0D + (double)(this.mc.field_71474_y.ofCloudsHeight * 128.0F);
         return flag1 != flag;
      }
   }

   public void startUpdateGlList() {
      GL11.glNewList(this.glListClouds, 4864);
   }

   public void endUpdateGlList() {
      GL11.glEndList();
      this.updateRenderFancy = this.renderFancy;
      this.updateCloudTickCounter = this.cloudTickCounter;
      this.updateCloudColor = this.cloudColor;
      this.updatePlayerX = this.mc.func_175606_aa().field_70169_q;
      this.updatePlayerY = this.mc.func_175606_aa().field_70167_r;
      this.updatePlayerZ = this.mc.func_175606_aa().field_70166_s;
      this.updated = true;
      GlStateManager.func_179117_G();
   }

   public void renderGlList() {
      Entity entity = this.mc.func_175606_aa();
      double d0 = entity.field_70169_q + (entity.field_70165_t - entity.field_70169_q) * (double)this.partialTicks;
      double d1 = entity.field_70167_r + (entity.field_70163_u - entity.field_70167_r) * (double)this.partialTicks;
      double d2 = entity.field_70166_s + (entity.field_70161_v - entity.field_70166_s) * (double)this.partialTicks;
      double d3 = (double)((float)(this.cloudTickCounter - this.updateCloudTickCounter) + this.partialTicks);
      float f = (float)(d0 - this.updatePlayerX + d3 * 0.03D);
      float f1 = (float)(d1 - this.updatePlayerY);
      float f2 = (float)(d2 - this.updatePlayerZ);
      GlStateManager.func_179094_E();
      if(this.renderFancy) {
         GlStateManager.func_179109_b(-f / 12.0F, -f1, -f2 / 12.0F);
      } else {
         GlStateManager.func_179109_b(-f, -f1, -f2);
      }

      GlStateManager.func_179148_o(this.glListClouds);
      GlStateManager.func_179121_F();
      GlStateManager.func_179117_G();
   }

   public void reset() {
      this.updated = false;
   }
}
