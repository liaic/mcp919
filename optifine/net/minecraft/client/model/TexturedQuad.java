package net.minecraft.client.model;

import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Vec3;
import optifine.Config;
import shadersmod.client.SVertexFormat;

public class TexturedQuad {

   public PositionTextureVertex[] vertexPositions;
   public int nVertices;
   public boolean invertNormal;
   public static final String __OBFID = "CL_00000850";


   public TexturedQuad(PositionTextureVertex[] vertices) {
      this.vertexPositions = vertices;
      this.nVertices = vertices.length;
   }

   public TexturedQuad(PositionTextureVertex[] vertices, int texcoordU1, int texcoordV1, int texcoordU2, int texcoordV2, float textureWidth, float textureHeight) {
      this(vertices);
      float var8 = 0.0F / textureWidth;
      float var9 = 0.0F / textureHeight;
      vertices[0] = vertices[0].setTexturePosition((float)texcoordU2 / textureWidth - var8, (float)texcoordV1 / textureHeight + var9);
      vertices[1] = vertices[1].setTexturePosition((float)texcoordU1 / textureWidth + var8, (float)texcoordV1 / textureHeight + var9);
      vertices[2] = vertices[2].setTexturePosition((float)texcoordU1 / textureWidth + var8, (float)texcoordV2 / textureHeight - var9);
      vertices[3] = vertices[3].setTexturePosition((float)texcoordU2 / textureWidth - var8, (float)texcoordV2 / textureHeight - var9);
   }

   public void flipFace() {
      PositionTextureVertex[] var1 = new PositionTextureVertex[this.vertexPositions.length];

      for(int var2 = 0; var2 < this.vertexPositions.length; ++var2) {
         var1[var2] = this.vertexPositions[this.vertexPositions.length - var2 - 1];
      }

      this.vertexPositions = var1;
   }

   public void draw(WorldRenderer renderer, float scale) {
      Vec3 var3 = this.vertexPositions[1].vector3D.subtractReverse(this.vertexPositions[0].vector3D);
      Vec3 var4 = this.vertexPositions[1].vector3D.subtractReverse(this.vertexPositions[2].vector3D);
      Vec3 var5 = var4.crossProduct(var3).normalize();
      float var6 = (float)var5.xCoord;
      float var7 = (float)var5.yCoord;
      float var8 = (float)var5.zCoord;
      if(this.invertNormal) {
         var6 = -var6;
         var7 = -var7;
         var8 = -var8;
      }

      if(Config.isShaders()) {
         renderer.begin(7, SVertexFormat.defVertexFormatTextured);
      } else {
         renderer.begin(7, DefaultVertexFormats.OLDMODEL_POSITION_TEX_NORMAL);
      }

      for(int var9 = 0; var9 < 4; ++var9) {
         PositionTextureVertex var10 = this.vertexPositions[var9];
         renderer.pos(var10.vector3D.xCoord * (double)scale, var10.vector3D.yCoord * (double)scale, var10.vector3D.zCoord * (double)scale).tex((double)var10.texturePositionX, (double)var10.texturePositionY).normal(var6, var7, var8).endVertex();
      }

      Tessellator.getInstance().draw();
   }
}
