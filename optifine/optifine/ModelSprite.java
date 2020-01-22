package optifine;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class ModelSprite {

   public ModelRenderer modelRenderer = null;
   public int textureOffsetX = 0;
   public int textureOffsetY = 0;
   public float posX = 0.0F;
   public float posY = 0.0F;
   public float posZ = 0.0F;
   public int sizeX = 0;
   public int sizeY = 0;
   public int sizeZ = 0;
   public float sizeAdd = 0.0F;
   public float minU = 0.0F;
   public float minV = 0.0F;
   public float maxU = 0.0F;
   public float maxV = 0.0F;


   public ModelSprite(ModelRenderer modelRenderer, int textureOffsetX, int textureOffsetY, float posX, float posY, float posZ, int sizeX, int sizeY, int sizeZ, float sizeAdd) {
      this.modelRenderer = modelRenderer;
      this.textureOffsetX = textureOffsetX;
      this.textureOffsetY = textureOffsetY;
      this.posX = posX;
      this.posY = posY;
      this.posZ = posZ;
      this.sizeX = sizeX;
      this.sizeY = sizeY;
      this.sizeZ = sizeZ;
      this.sizeAdd = sizeAdd;
      this.minU = (float)textureOffsetX / modelRenderer.textureWidth;
      this.minV = (float)textureOffsetY / modelRenderer.textureHeight;
      this.maxU = (float)(textureOffsetX + sizeX) / modelRenderer.textureWidth;
      this.maxV = (float)(textureOffsetY + sizeY) / modelRenderer.textureHeight;
   }

   public void render(Tessellator tessellator, float scale) {
      GlStateManager.translate(this.posX * scale, this.posY * scale, this.posZ * scale);
      float rMinU = this.minU;
      float rMaxU = this.maxU;
      float rMinV = this.minV;
      float rMaxV = this.maxV;
      if(this.modelRenderer.mirror) {
         rMinU = this.maxU;
         rMaxU = this.minU;
      }

      if(this.modelRenderer.mirrorV) {
         rMinV = this.maxV;
         rMaxV = this.minV;
      }

      renderItemIn2D(tessellator, rMinU, rMinV, rMaxU, rMaxV, this.sizeX, this.sizeY, scale * (float)this.sizeZ, this.modelRenderer.textureWidth, this.modelRenderer.textureHeight);
      GlStateManager.translate(-this.posX * scale, -this.posY * scale, -this.posZ * scale);
   }

   public static void renderItemIn2D(Tessellator tess, float minU, float minV, float maxU, float maxV, int sizeX, int sizeY, float width, float texWidth, float texHeight) {
      if(width < 6.25E-4F) {
         width = 6.25E-4F;
      }

      float dU = maxU - minU;
      float dV = maxV - minV;
      double dimX = (double)(MathHelper.abs(dU) * (texWidth / 16.0F));
      double dimY = (double)(MathHelper.abs(dV) * (texHeight / 16.0F));
      WorldRenderer tessellator = tess.getWorldRenderer();
      GL11.glNormal3f(0.0F, 0.0F, -1.0F);
      tessellator.begin(7, DefaultVertexFormats.POSITION_TEX);
      tessellator.pos(0.0D, dimY, 0.0D).tex((double)minU, (double)maxV).endVertex();
      tessellator.pos(dimX, dimY, 0.0D).tex((double)maxU, (double)maxV).endVertex();
      tessellator.pos(dimX, 0.0D, 0.0D).tex((double)maxU, (double)minV).endVertex();
      tessellator.pos(0.0D, 0.0D, 0.0D).tex((double)minU, (double)minV).endVertex();
      tess.draw();
      GL11.glNormal3f(0.0F, 0.0F, 1.0F);
      tessellator.begin(7, DefaultVertexFormats.POSITION_TEX);
      tessellator.pos(0.0D, 0.0D, (double)width).tex((double)minU, (double)minV).endVertex();
      tessellator.pos(dimX, 0.0D, (double)width).tex((double)maxU, (double)minV).endVertex();
      tessellator.pos(dimX, dimY, (double)width).tex((double)maxU, (double)maxV).endVertex();
      tessellator.pos(0.0D, dimY, (double)width).tex((double)minU, (double)maxV).endVertex();
      tess.draw();
      float var8 = 0.5F * dU / (float)sizeX;
      float var9 = 0.5F * dV / (float)sizeY;
      GL11.glNormal3f(-1.0F, 0.0F, 0.0F);
      tessellator.begin(7, DefaultVertexFormats.POSITION_TEX);

      int var10;
      float var11;
      float var12;
      for(var10 = 0; var10 < sizeX; ++var10) {
         var11 = (float)var10 / (float)sizeX;
         var12 = minU + dU * var11 + var8;
         tessellator.pos((double)var11 * dimX, dimY, (double)width).tex((double)var12, (double)maxV).endVertex();
         tessellator.pos((double)var11 * dimX, dimY, 0.0D).tex((double)var12, (double)maxV).endVertex();
         tessellator.pos((double)var11 * dimX, 0.0D, 0.0D).tex((double)var12, (double)minV).endVertex();
         tessellator.pos((double)var11 * dimX, 0.0D, (double)width).tex((double)var12, (double)minV).endVertex();
      }

      tess.draw();
      GL11.glNormal3f(1.0F, 0.0F, 0.0F);
      tessellator.begin(7, DefaultVertexFormats.POSITION_TEX);

      float var13;
      for(var10 = 0; var10 < sizeX; ++var10) {
         var11 = (float)var10 / (float)sizeX;
         var12 = minU + dU * var11 + var8;
         var13 = var11 + 1.0F / (float)sizeX;
         tessellator.pos((double)var13 * dimX, 0.0D, (double)width).tex((double)var12, (double)minV).endVertex();
         tessellator.pos((double)var13 * dimX, 0.0D, 0.0D).tex((double)var12, (double)minV).endVertex();
         tessellator.pos((double)var13 * dimX, dimY, 0.0D).tex((double)var12, (double)maxV).endVertex();
         tessellator.pos((double)var13 * dimX, dimY, (double)width).tex((double)var12, (double)maxV).endVertex();
      }

      tess.draw();
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      tessellator.begin(7, DefaultVertexFormats.POSITION_TEX);

      for(var10 = 0; var10 < sizeY; ++var10) {
         var11 = (float)var10 / (float)sizeY;
         var12 = minV + dV * var11 + var9;
         var13 = var11 + 1.0F / (float)sizeY;
         tessellator.pos(0.0D, (double)var13 * dimY, (double)width).tex((double)minU, (double)var12).endVertex();
         tessellator.pos(dimX, (double)var13 * dimY, (double)width).tex((double)maxU, (double)var12).endVertex();
         tessellator.pos(dimX, (double)var13 * dimY, 0.0D).tex((double)maxU, (double)var12).endVertex();
         tessellator.pos(0.0D, (double)var13 * dimY, 0.0D).tex((double)minU, (double)var12).endVertex();
      }

      tess.draw();
      GL11.glNormal3f(0.0F, -1.0F, 0.0F);
      tessellator.begin(7, DefaultVertexFormats.POSITION_TEX);

      for(var10 = 0; var10 < sizeY; ++var10) {
         var11 = (float)var10 / (float)sizeY;
         var12 = minV + dV * var11 + var9;
         tessellator.pos(dimX, (double)var11 * dimY, (double)width).tex((double)maxU, (double)var12).endVertex();
         tessellator.pos(0.0D, (double)var11 * dimY, (double)width).tex((double)minU, (double)var12).endVertex();
         tessellator.pos(0.0D, (double)var11 * dimY, 0.0D).tex((double)minU, (double)var12).endVertex();
         tessellator.pos(dimX, (double)var11 * dimY, 0.0D).tex((double)maxU, (double)var12).endVertex();
      }

      tess.draw();
   }
}
