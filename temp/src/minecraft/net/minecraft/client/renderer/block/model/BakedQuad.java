package net.minecraft.client.renderer.block.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.src.Config;
import net.minecraft.src.QuadBounds;
import net.minecraft.src.Reflector;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.IVertexProducer;

public class BakedQuad implements IVertexProducer {
   protected int[] field_178215_a;
   protected final int field_178213_b;
   protected EnumFacing field_178214_c;
   private static final String __OBFID = "CL_00002512";
   private TextureAtlasSprite sprite = null;
   private int[] vertexDataSingle = null;
   private QuadBounds quadBounds;

   public BakedQuad(int[] p_i11_1_, int p_i11_2_, EnumFacing p_i11_3_, TextureAtlasSprite p_i11_4_) {
      this.field_178215_a = p_i11_1_;
      this.field_178213_b = p_i11_2_;
      this.field_178214_c = p_i11_3_;
      this.sprite = p_i11_4_;
      this.fixVertexData();
   }

   public TextureAtlasSprite getSprite() {
      if(this.sprite == null) {
         this.sprite = getSpriteByUv(this.func_178209_a());
      }

      return this.sprite;
   }

   public BakedQuad(int[] p_i46232_1_, int p_i46232_2_, EnumFacing p_i46232_3_) {
      this.field_178215_a = p_i46232_1_;
      this.field_178213_b = p_i46232_2_;
      this.field_178214_c = p_i46232_3_;
      this.fixVertexData();
   }

   public int[] func_178209_a() {
      this.fixVertexData();
      return this.field_178215_a;
   }

   public boolean func_178212_b() {
      return this.field_178213_b != -1;
   }

   public int func_178211_c() {
      return this.field_178213_b;
   }

   public EnumFacing func_178210_d() {
      if(this.field_178214_c == null) {
         this.field_178214_c = FaceBakery.func_178410_a(this.func_178209_a());
      }

      return this.field_178214_c;
   }

   public int[] getVertexDataSingle() {
      if(this.vertexDataSingle == null) {
         this.vertexDataSingle = makeVertexDataSingle(this.func_178209_a(), this.getSprite());
      }

      return this.vertexDataSingle;
   }

   private static int[] makeVertexDataSingle(int[] p_makeVertexDataSingle_0_, TextureAtlasSprite p_makeVertexDataSingle_1_) {
      int[] aint = (int[])p_makeVertexDataSingle_0_.clone();
      int i = p_makeVertexDataSingle_1_.sheetWidth / p_makeVertexDataSingle_1_.func_94211_a();
      int j = p_makeVertexDataSingle_1_.sheetHeight / p_makeVertexDataSingle_1_.func_94216_b();
      int k = aint.length / 4;

      for(int l = 0; l < 4; ++l) {
         int i1 = l * k;
         float f = Float.intBitsToFloat(aint[i1 + 4]);
         float f1 = Float.intBitsToFloat(aint[i1 + 4 + 1]);
         float f2 = p_makeVertexDataSingle_1_.toSingleU(f);
         float f3 = p_makeVertexDataSingle_1_.toSingleV(f1);
         aint[i1 + 4] = Float.floatToRawIntBits(f2);
         aint[i1 + 4 + 1] = Float.floatToRawIntBits(f3);
      }

      return aint;
   }

   public void pipe(IVertexConsumer p_pipe_1_) {
      Reflector.callVoid(Reflector.LightUtil_putBakedQuad, new Object[]{p_pipe_1_, this});
   }

   private static TextureAtlasSprite getSpriteByUv(int[] p_getSpriteByUv_0_) {
      float f = 1.0F;
      float f1 = 1.0F;
      float f2 = 0.0F;
      float f3 = 0.0F;
      int i = p_getSpriteByUv_0_.length / 4;

      for(int j = 0; j < 4; ++j) {
         int k = j * i;
         float f4 = Float.intBitsToFloat(p_getSpriteByUv_0_[k + 4]);
         float f5 = Float.intBitsToFloat(p_getSpriteByUv_0_[k + 4 + 1]);
         f = Math.min(f, f4);
         f1 = Math.min(f1, f5);
         f2 = Math.max(f2, f4);
         f3 = Math.max(f3, f5);
      }

      float f6 = (f + f2) / 2.0F;
      float f7 = (f1 + f3) / 2.0F;
      TextureAtlasSprite textureatlassprite = Minecraft.func_71410_x().func_147117_R().getIconByUV((double)f6, (double)f7);
      return textureatlassprite;
   }

   private void fixVertexData() {
      if(Config.isShaders()) {
         if(this.field_178215_a.length == 28) {
            this.field_178215_a = expandVertexData(this.field_178215_a);
         }
      } else if(this.field_178215_a.length == 56) {
         this.field_178215_a = compactVertexData(this.field_178215_a);
      }

   }

   private static int[] expandVertexData(int[] p_expandVertexData_0_) {
      int i = p_expandVertexData_0_.length / 4;
      int j = i * 2;
      int[] aint = new int[j * 4];

      for(int k = 0; k < 4; ++k) {
         System.arraycopy(p_expandVertexData_0_, k * i, aint, k * j, i);
      }

      return aint;
   }

   private static int[] compactVertexData(int[] p_compactVertexData_0_) {
      int i = p_compactVertexData_0_.length / 4;
      int j = i / 2;
      int[] aint = new int[j * 4];

      for(int k = 0; k < 4; ++k) {
         System.arraycopy(p_compactVertexData_0_, k * i, aint, k * j, j);
      }

      return aint;
   }

   public QuadBounds getQuadBounds() {
      if(this.quadBounds == null) {
         this.quadBounds = new QuadBounds(this.func_178209_a());
      }

      return this.quadBounds;
   }

   public float getMidX() {
      QuadBounds quadbounds = this.getQuadBounds();
      return (quadbounds.getMaxX() + quadbounds.getMinX()) / 2.0F;
   }

   public double getMidY() {
      QuadBounds quadbounds = this.getQuadBounds();
      return (double)((quadbounds.getMaxY() + quadbounds.getMinY()) / 2.0F);
   }

   public double getMidZ() {
      QuadBounds quadbounds = this.getQuadBounds();
      return (double)((quadbounds.getMaxZ() + quadbounds.getMinZ()) / 2.0F);
   }

   public boolean isFaceQuad() {
      QuadBounds quadbounds = this.getQuadBounds();
      return quadbounds.isFaceQuad(this.field_178214_c);
   }

   public boolean isFullQuad() {
      QuadBounds quadbounds = this.getQuadBounds();
      return quadbounds.isFullQuad(this.field_178214_c);
   }

   public boolean isFullFaceQuad() {
      return this.isFullQuad() && this.isFaceQuad();
   }

   public String toString() {
      return "vertex: " + this.field_178215_a.length / 7 + ", tint: " + this.field_178213_b + ", facing: " + this.field_178214_c + ", sprite: " + this.sprite;
   }
}
