package net.minecraft.client.renderer.block.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.IVertexProducer;
import optifine.Config;
import optifine.QuadBounds;
import optifine.Reflector;

public class BakedQuad implements IVertexProducer {

   public int[] vertexData;
   public final int tintIndex;
   public EnumFacing face;
   public static final String __OBFID = "CL_00002512";
   public TextureAtlasSprite sprite = null;
   public int[] vertexDataSingle = null;
   public QuadBounds quadBounds;


   public BakedQuad(int[] p_i46232_1_, int p_i46232_2_, EnumFacing p_i46232_3_, TextureAtlasSprite sprite) {
      this.vertexData = p_i46232_1_;
      this.tintIndex = p_i46232_2_;
      this.face = p_i46232_3_;
      this.sprite = sprite;
      this.fixVertexData();
   }

   public TextureAtlasSprite getSprite() {
      if(this.sprite == null) {
         this.sprite = getSpriteByUv(this.getVertexData());
      }

      return this.sprite;
   }

   public BakedQuad(int[] p_i46232_1_, int p_i46232_2_, EnumFacing p_i46232_3_) {
      this.vertexData = p_i46232_1_;
      this.tintIndex = p_i46232_2_;
      this.face = p_i46232_3_;
      this.fixVertexData();
   }

   public int[] getVertexData() {
      this.fixVertexData();
      return this.vertexData;
   }

   public boolean hasTintIndex() {
      return this.tintIndex != -1;
   }

   public int getTintIndex() {
      return this.tintIndex;
   }

   public EnumFacing getFace() {
      if(this.face == null) {
         this.face = FaceBakery.getFacingFromVertexData(this.getVertexData());
      }

      return this.face;
   }

   public int[] getVertexDataSingle() {
      if(this.vertexDataSingle == null) {
         this.vertexDataSingle = makeVertexDataSingle(this.getVertexData(), this.getSprite());
      }

      return this.vertexDataSingle;
   }

   public static int[] makeVertexDataSingle(int[] vd, TextureAtlasSprite sprite) {
      int[] vdSingle = (int[])vd.clone();
      int ku = sprite.sheetWidth / sprite.getIconWidth();
      int kv = sprite.sheetHeight / sprite.getIconHeight();
      int step = vdSingle.length / 4;

      for(int i = 0; i < 4; ++i) {
         int pos = i * step;
         float tu = Float.intBitsToFloat(vdSingle[pos + 4]);
         float tv = Float.intBitsToFloat(vdSingle[pos + 4 + 1]);
         float u = sprite.toSingleU(tu);
         float v = sprite.toSingleV(tv);
         vdSingle[pos + 4] = Float.floatToRawIntBits(u);
         vdSingle[pos + 4 + 1] = Float.floatToRawIntBits(v);
      }

      return vdSingle;
   }

   public void pipe(IVertexConsumer consumer) {
      Reflector.callVoid(Reflector.LightUtil_putBakedQuad, new Object[]{consumer, this});
   }

   public static TextureAtlasSprite getSpriteByUv(int[] vertexData) {
      float uMin = 1.0F;
      float vMin = 1.0F;
      float uMax = 0.0F;
      float vMax = 0.0F;
      int step = vertexData.length / 4;

      for(int uMid = 0; uMid < 4; ++uMid) {
         int vMid = uMid * step;
         float spriteUv = Float.intBitsToFloat(vertexData[vMid + 4]);
         float tv = Float.intBitsToFloat(vertexData[vMid + 4 + 1]);
         uMin = Math.min(uMin, spriteUv);
         vMin = Math.min(vMin, tv);
         uMax = Math.max(uMax, spriteUv);
         vMax = Math.max(vMax, tv);
      }

      float var10 = (uMin + uMax) / 2.0F;
      float var11 = (vMin + vMax) / 2.0F;
      TextureAtlasSprite var12 = Minecraft.getMinecraft().getTextureMapBlocks().getIconByUV((double)var10, (double)var11);
      return var12;
   }

   public void fixVertexData() {
      if(Config.isShaders()) {
         if(this.vertexData.length == 28) {
            this.vertexData = expandVertexData(this.vertexData);
         }
      } else if(this.vertexData.length == 56) {
         this.vertexData = compactVertexData(this.vertexData);
      }

   }

   public static int[] expandVertexData(int[] vd) {
      int step = vd.length / 4;
      int stepNew = step * 2;
      int[] vdNew = new int[stepNew * 4];

      for(int i = 0; i < 4; ++i) {
         System.arraycopy(vd, i * step, vdNew, i * stepNew, step);
      }

      return vdNew;
   }

   public static int[] compactVertexData(int[] vd) {
      int step = vd.length / 4;
      int stepNew = step / 2;
      int[] vdNew = new int[stepNew * 4];

      for(int i = 0; i < 4; ++i) {
         System.arraycopy(vd, i * step, vdNew, i * stepNew, stepNew);
      }

      return vdNew;
   }

   public QuadBounds getQuadBounds() {
      if(this.quadBounds == null) {
         this.quadBounds = new QuadBounds(this.getVertexData());
      }

      return this.quadBounds;
   }

   public float getMidX() {
      QuadBounds qb = this.getQuadBounds();
      return (qb.getMaxX() + qb.getMinX()) / 2.0F;
   }

   public double getMidY() {
      QuadBounds qb = this.getQuadBounds();
      return (double)((qb.getMaxY() + qb.getMinY()) / 2.0F);
   }

   public double getMidZ() {
      QuadBounds qb = this.getQuadBounds();
      return (double)((qb.getMaxZ() + qb.getMinZ()) / 2.0F);
   }

   public boolean isFaceQuad() {
      QuadBounds qb = this.getQuadBounds();
      return qb.isFaceQuad(this.face);
   }

   public boolean isFullQuad() {
      QuadBounds qb = this.getQuadBounds();
      return qb.isFullQuad(this.face);
   }

   public boolean isFullFaceQuad() {
      return this.isFullQuad() && this.isFaceQuad();
   }

   public String toString() {
      return "vertex: " + this.vertexData.length / 7 + ", tint: " + this.tintIndex + ", facing: " + this.face + ", sprite: " + this.sprite;
   }
}
