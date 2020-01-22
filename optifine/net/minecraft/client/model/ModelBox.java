package net.minecraft.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.WorldRenderer;

public class ModelBox {

   public PositionTextureVertex[] vertexPositions;
   public TexturedQuad[] quadList;
   public final float posX1;
   public final float posY1;
   public final float posZ1;
   public final float posX2;
   public final float posY2;
   public final float posZ2;
   public String boxName;
   public static final String __OBFID = "CL_00000872";


   public ModelBox(ModelRenderer p_i46359_1_, int p_i46359_2_, int p_i46359_3_, float p_i46359_4_, float p_i46359_5_, float p_i46359_6_, int p_i46359_7_, int p_i46359_8_, int p_i46359_9_, float p_i46359_10_) {
      this(p_i46359_1_, p_i46359_2_, p_i46359_3_, p_i46359_4_, p_i46359_5_, p_i46359_6_, p_i46359_7_, p_i46359_8_, p_i46359_9_, p_i46359_10_, p_i46359_1_.mirror);
   }

   public ModelBox(ModelRenderer renderer, int[][] faceUvs, float x, float y, float z, float dx, float dy, float dz, float delta, boolean mirror) {
      this.posX1 = x;
      this.posY1 = y;
      this.posZ1 = z;
      this.posX2 = x + dx;
      this.posY2 = y + dy;
      this.posZ2 = z + dz;
      this.vertexPositions = new PositionTextureVertex[8];
      this.quadList = new TexturedQuad[6];
      float f = x + dx;
      float f1 = y + dy;
      float f2 = z + dz;
      x -= delta;
      y -= delta;
      z -= delta;
      f += delta;
      f1 += delta;
      f2 += delta;
      if(mirror) {
         float pos0 = f;
         f = x;
         x = pos0;
      }

      PositionTextureVertex var26 = new PositionTextureVertex(x, y, z, 0.0F, 0.0F);
      PositionTextureVertex pos1 = new PositionTextureVertex(f, y, z, 0.0F, 8.0F);
      PositionTextureVertex pos2 = new PositionTextureVertex(f, f1, z, 8.0F, 8.0F);
      PositionTextureVertex pos3 = new PositionTextureVertex(x, f1, z, 8.0F, 0.0F);
      PositionTextureVertex pos4 = new PositionTextureVertex(x, y, f2, 0.0F, 0.0F);
      PositionTextureVertex pos5 = new PositionTextureVertex(f, y, f2, 0.0F, 8.0F);
      PositionTextureVertex pos6 = new PositionTextureVertex(f, f1, f2, 8.0F, 8.0F);
      PositionTextureVertex pos7 = new PositionTextureVertex(x, f1, f2, 8.0F, 0.0F);
      this.vertexPositions[0] = var26;
      this.vertexPositions[1] = pos1;
      this.vertexPositions[2] = pos2;
      this.vertexPositions[3] = pos3;
      this.vertexPositions[4] = pos4;
      this.vertexPositions[5] = pos5;
      this.vertexPositions[6] = pos6;
      this.vertexPositions[7] = pos7;
      this.quadList[0] = this.makeTexturedQuad(new PositionTextureVertex[]{pos5, pos1, pos2, pos6}, faceUvs[4], false, renderer.textureWidth, renderer.textureHeight);
      this.quadList[1] = this.makeTexturedQuad(new PositionTextureVertex[]{var26, pos4, pos7, pos3}, faceUvs[5], false, renderer.textureWidth, renderer.textureHeight);
      this.quadList[2] = this.makeTexturedQuad(new PositionTextureVertex[]{pos5, pos4, var26, pos1}, faceUvs[1], true, renderer.textureWidth, renderer.textureHeight);
      this.quadList[3] = this.makeTexturedQuad(new PositionTextureVertex[]{pos2, pos3, pos7, pos6}, faceUvs[0], true, renderer.textureWidth, renderer.textureHeight);
      this.quadList[4] = this.makeTexturedQuad(new PositionTextureVertex[]{pos1, var26, pos3, pos2}, faceUvs[2], false, renderer.textureWidth, renderer.textureHeight);
      this.quadList[5] = this.makeTexturedQuad(new PositionTextureVertex[]{pos4, pos5, pos6, pos7}, faceUvs[3], false, renderer.textureWidth, renderer.textureHeight);
      if(mirror) {
         TexturedQuad[] arr$ = this.quadList;
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            TexturedQuad texturedquad = arr$[i$];
            texturedquad.flipFace();
         }
      }

   }

   public TexturedQuad makeTexturedQuad(PositionTextureVertex[] positionTextureVertexs, int[] faceUvs, boolean reverseUV, float textureWidth, float textureHeight) {
      return faceUvs == null?null:(reverseUV?new TexturedQuad(positionTextureVertexs, faceUvs[2], faceUvs[3], faceUvs[0], faceUvs[1], textureWidth, textureHeight):new TexturedQuad(positionTextureVertexs, faceUvs[0], faceUvs[1], faceUvs[2], faceUvs[3], textureWidth, textureHeight));
   }

   public ModelBox(ModelRenderer p_i46301_1_, int p_i46301_2_, int p_i46301_3_, float p_i46301_4_, float p_i46301_5_, float p_i46301_6_, int p_i46301_7_, int p_i46301_8_, int p_i46301_9_, float p_i46301_10_, boolean p_i46301_11_) {
      this.posX1 = p_i46301_4_;
      this.posY1 = p_i46301_5_;
      this.posZ1 = p_i46301_6_;
      this.posX2 = p_i46301_4_ + (float)p_i46301_7_;
      this.posY2 = p_i46301_5_ + (float)p_i46301_8_;
      this.posZ2 = p_i46301_6_ + (float)p_i46301_9_;
      this.vertexPositions = new PositionTextureVertex[8];
      this.quadList = new TexturedQuad[6];
      float var12 = p_i46301_4_ + (float)p_i46301_7_;
      float var13 = p_i46301_5_ + (float)p_i46301_8_;
      float var14 = p_i46301_6_ + (float)p_i46301_9_;
      p_i46301_4_ -= p_i46301_10_;
      p_i46301_5_ -= p_i46301_10_;
      p_i46301_6_ -= p_i46301_10_;
      var12 += p_i46301_10_;
      var13 += p_i46301_10_;
      var14 += p_i46301_10_;
      if(p_i46301_11_) {
         float var24 = var12;
         var12 = p_i46301_4_;
         p_i46301_4_ = var24;
      }

      PositionTextureVertex var241 = new PositionTextureVertex(p_i46301_4_, p_i46301_5_, p_i46301_6_, 0.0F, 0.0F);
      PositionTextureVertex var16 = new PositionTextureVertex(var12, p_i46301_5_, p_i46301_6_, 0.0F, 8.0F);
      PositionTextureVertex var17 = new PositionTextureVertex(var12, var13, p_i46301_6_, 8.0F, 8.0F);
      PositionTextureVertex var18 = new PositionTextureVertex(p_i46301_4_, var13, p_i46301_6_, 8.0F, 0.0F);
      PositionTextureVertex var19 = new PositionTextureVertex(p_i46301_4_, p_i46301_5_, var14, 0.0F, 0.0F);
      PositionTextureVertex var20 = new PositionTextureVertex(var12, p_i46301_5_, var14, 0.0F, 8.0F);
      PositionTextureVertex var21 = new PositionTextureVertex(var12, var13, var14, 8.0F, 8.0F);
      PositionTextureVertex var22 = new PositionTextureVertex(p_i46301_4_, var13, var14, 8.0F, 0.0F);
      this.vertexPositions[0] = var241;
      this.vertexPositions[1] = var16;
      this.vertexPositions[2] = var17;
      this.vertexPositions[3] = var18;
      this.vertexPositions[4] = var19;
      this.vertexPositions[5] = var20;
      this.vertexPositions[6] = var21;
      this.vertexPositions[7] = var22;
      this.quadList[0] = new TexturedQuad(new PositionTextureVertex[]{var20, var16, var17, var21}, p_i46301_2_ + p_i46301_9_ + p_i46301_7_, p_i46301_3_ + p_i46301_9_, p_i46301_2_ + p_i46301_9_ + p_i46301_7_ + p_i46301_9_, p_i46301_3_ + p_i46301_9_ + p_i46301_8_, p_i46301_1_.textureWidth, p_i46301_1_.textureHeight);
      this.quadList[1] = new TexturedQuad(new PositionTextureVertex[]{var241, var19, var22, var18}, p_i46301_2_, p_i46301_3_ + p_i46301_9_, p_i46301_2_ + p_i46301_9_, p_i46301_3_ + p_i46301_9_ + p_i46301_8_, p_i46301_1_.textureWidth, p_i46301_1_.textureHeight);
      this.quadList[2] = new TexturedQuad(new PositionTextureVertex[]{var20, var19, var241, var16}, p_i46301_2_ + p_i46301_9_, p_i46301_3_, p_i46301_2_ + p_i46301_9_ + p_i46301_7_, p_i46301_3_ + p_i46301_9_, p_i46301_1_.textureWidth, p_i46301_1_.textureHeight);
      this.quadList[3] = new TexturedQuad(new PositionTextureVertex[]{var17, var18, var22, var21}, p_i46301_2_ + p_i46301_9_ + p_i46301_7_, p_i46301_3_ + p_i46301_9_, p_i46301_2_ + p_i46301_9_ + p_i46301_7_ + p_i46301_7_, p_i46301_3_, p_i46301_1_.textureWidth, p_i46301_1_.textureHeight);
      this.quadList[4] = new TexturedQuad(new PositionTextureVertex[]{var16, var241, var18, var17}, p_i46301_2_ + p_i46301_9_, p_i46301_3_ + p_i46301_9_, p_i46301_2_ + p_i46301_9_ + p_i46301_7_, p_i46301_3_ + p_i46301_9_ + p_i46301_8_, p_i46301_1_.textureWidth, p_i46301_1_.textureHeight);
      this.quadList[5] = new TexturedQuad(new PositionTextureVertex[]{var19, var20, var21, var22}, p_i46301_2_ + p_i46301_9_ + p_i46301_7_ + p_i46301_9_, p_i46301_3_ + p_i46301_9_, p_i46301_2_ + p_i46301_9_ + p_i46301_7_ + p_i46301_9_ + p_i46301_7_, p_i46301_3_ + p_i46301_9_ + p_i46301_8_, p_i46301_1_.textureWidth, p_i46301_1_.textureHeight);
      if(p_i46301_11_) {
         for(int var23 = 0; var23 < this.quadList.length; ++var23) {
            this.quadList[var23].flipFace();
         }
      }

   }

   public void render(WorldRenderer p_178780_1_, float p_178780_2_) {
      for(int var3 = 0; var3 < this.quadList.length; ++var3) {
         TexturedQuad texturedquad = this.quadList[var3];
         if(texturedquad != null) {
            texturedquad.draw(p_178780_1_, p_178780_2_);
         }
      }

   }

   public ModelBox setBoxName(String p_78244_1_) {
      this.boxName = p_78244_1_;
      return this;
   }
}
