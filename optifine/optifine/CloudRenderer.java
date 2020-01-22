package optifine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class CloudRenderer {

   public Minecraft mc;
   public boolean updated = false;
   public boolean renderFancy = false;
   public int cloudTickCounter;
   public Vec3 cloudColor;
   public float partialTicks;
   public boolean updateRenderFancy = false;
   public int updateCloudTickCounter = 0;
   public Vec3 updateCloudColor = new Vec3(-1.0D, -1.0D, -1.0D);
   public double updatePlayerX = 0.0D;
   public double updatePlayerY = 0.0D;
   public double updatePlayerZ = 0.0D;
   public int glListClouds = -1;


   public CloudRenderer(Minecraft mc) {
      this.mc = mc;
      this.glListClouds = GLAllocation.generateDisplayLists(1);
   }

   public void prepareToRender(boolean renderFancy, int cloudTickCounter, float partialTicks, Vec3 cloudColor) {
      this.renderFancy = renderFancy;
      this.cloudTickCounter = cloudTickCounter;
      this.partialTicks = partialTicks;
      this.cloudColor = cloudColor;
   }

   public boolean shouldUpdateGlList() {
      if(!this.updated) {
         return true;
      } else if(this.renderFancy != this.updateRenderFancy) {
         return true;
      } else if(this.cloudTickCounter >= this.updateCloudTickCounter + 20) {
         return true;
      } else if(Math.abs(this.cloudColor.xCoord - this.updateCloudColor.xCoord) > 0.003D) {
         return true;
      } else if(Math.abs(this.cloudColor.yCoord - this.updateCloudColor.yCoord) > 0.003D) {
         return true;
      } else if(Math.abs(this.cloudColor.zCoord - this.updateCloudColor.zCoord) > 0.003D) {
         return true;
      } else {
         Entity rve = this.mc.getRenderViewEntity();
         boolean belowCloudsPrev = this.updatePlayerY + (double)rve.getEyeHeight() < 128.0D + (double)(this.mc.gameSettings.ofCloudsHeight * 128.0F);
         boolean belowClouds = rve.prevPosY + (double)rve.getEyeHeight() < 128.0D + (double)(this.mc.gameSettings.ofCloudsHeight * 128.0F);
         return belowClouds != belowCloudsPrev;
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
      this.updatePlayerX = this.mc.getRenderViewEntity().prevPosX;
      this.updatePlayerY = this.mc.getRenderViewEntity().prevPosY;
      this.updatePlayerZ = this.mc.getRenderViewEntity().prevPosZ;
      this.updated = true;
      GlStateManager.resetColor();
   }

   public void renderGlList() {
      Entity entityliving = this.mc.getRenderViewEntity();
      double exactPlayerX = entityliving.prevPosX + (entityliving.posX - entityliving.prevPosX) * (double)this.partialTicks;
      double exactPlayerY = entityliving.prevPosY + (entityliving.posY - entityliving.prevPosY) * (double)this.partialTicks;
      double exactPlayerZ = entityliving.prevPosZ + (entityliving.posZ - entityliving.prevPosZ) * (double)this.partialTicks;
      double dc = (double)((float)(this.cloudTickCounter - this.updateCloudTickCounter) + this.partialTicks);
      float cdx = (float)(exactPlayerX - this.updatePlayerX + dc * 0.03D);
      float cdy = (float)(exactPlayerY - this.updatePlayerY);
      float cdz = (float)(exactPlayerZ - this.updatePlayerZ);
      GlStateManager.pushMatrix();
      if(this.renderFancy) {
         GlStateManager.translate(-cdx / 12.0F, -cdy, -cdz / 12.0F);
      } else {
         GlStateManager.translate(-cdx, -cdy, -cdz);
      }

      GlStateManager.callList(this.glListClouds);
      GlStateManager.popMatrix();
      GlStateManager.resetColor();
   }

   public void reset() {
      this.updated = false;
   }
}
