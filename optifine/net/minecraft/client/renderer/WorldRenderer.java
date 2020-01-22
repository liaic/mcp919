package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.BitSet;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.WorldRenderer$1;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import optifine.Config;
import optifine.TextureUtils;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.GL11;
import shadersmod.client.SVertexBuilder;

public class WorldRenderer {

   public ByteBuffer byteBuffer;
   public IntBuffer rawIntBuffer;
   public ShortBuffer rawShortBuffer;
   public FloatBuffer rawFloatBuffer;
   public int vertexCount;
   public VertexFormatElement vertexFormatElement;
   public int vertexFormatIndex;
   public boolean noColor;
   public int drawMode;
   public double xOffset;
   public double yOffset;
   public double zOffset;
   public VertexFormat vertexFormat;
   public boolean isDrawing;
   public static final String __OBFID = "CL_00000942";
   public EnumWorldBlockLayer blockLayer = null;
   public boolean[] drawnIcons = new boolean[256];
   public TextureAtlasSprite[] quadSprites = null;
   public TextureAtlasSprite[] quadSpritesPrev = null;
   public TextureAtlasSprite quadSprite = null;
   public SVertexBuilder sVertexBuilder;


   public WorldRenderer(int p_i46275_1_) {
      if(Config.isShaders()) {
         p_i46275_1_ *= 2;
      }

      this.byteBuffer = GLAllocation.createDirectByteBuffer(p_i46275_1_ * 4);
      this.rawIntBuffer = this.byteBuffer.asIntBuffer();
      this.rawShortBuffer = this.byteBuffer.asShortBuffer();
      this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
      SVertexBuilder.initVertexBuilder(this);
   }

   public void growBuffer(int p_178983_1_) {
      if(Config.isShaders()) {
         p_178983_1_ *= 2;
      }

      if(p_178983_1_ > this.rawIntBuffer.remaining()) {
         int var2 = this.byteBuffer.capacity();
         int var3 = var2 % 2097152;
         int var4 = var3 + (((this.rawIntBuffer.position() + p_178983_1_) * 4 - var3) / 2097152 + 1) * 2097152;
         LogManager.getLogger().warn("Needed to grow BufferBuilder buffer: Old size " + var2 + " bytes, new size " + var4 + " bytes.");
         int var5 = this.rawIntBuffer.position();
         ByteBuffer var6 = GLAllocation.createDirectByteBuffer(var4);
         this.byteBuffer.position(0);
         var6.put(this.byteBuffer);
         var6.rewind();
         this.byteBuffer = var6;
         this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
         this.rawIntBuffer = this.byteBuffer.asIntBuffer();
         this.rawIntBuffer.position(var5);
         this.rawShortBuffer = this.byteBuffer.asShortBuffer();
         this.rawShortBuffer.position(var5 << 1);
         if(this.quadSprites != null) {
            TextureAtlasSprite[] sprites = this.quadSprites;
            int quadSize = this.getBufferQuadSize();
            this.quadSprites = new TextureAtlasSprite[quadSize];
            System.arraycopy(sprites, 0, this.quadSprites, 0, Math.min(sprites.length, this.quadSprites.length));
            this.quadSpritesPrev = null;
         }
      }

   }

   public void sortVertexData(float p_500120_1_, float p_500120_2_, float p_500120_3_) {
      int var4 = this.vertexCount / 4;
      float[] var5 = new float[var4];

      for(int var15 = 0; var15 < var4; ++var15) {
         var5[var15] = getDistanceSq(this.rawFloatBuffer, (float)((double)p_500120_1_ + this.xOffset), (float)((double)p_500120_2_ + this.yOffset), (float)((double)p_500120_3_ + this.zOffset), this.vertexFormat.getIntegerSize(), var15 * this.vertexFormat.getNextOffset());
      }

      Integer[] var151 = new Integer[var4];

      for(int var16 = 0; var16 < var151.length; ++var16) {
         var151[var16] = Integer.valueOf(var16);
      }

      Arrays.sort(var151, new WorldRenderer$1(this, var5));
      BitSet var161 = new BitSet();
      int var8 = this.vertexFormat.getNextOffset();
      int[] var9 = new int[var8];

      int quadStep;
      int i;
      int indexQuad;
      for(int quadSpritesSorted = 0; (quadSpritesSorted = var161.nextClearBit(quadSpritesSorted)) < var151.length; ++quadSpritesSorted) {
         quadStep = var151[quadSpritesSorted].intValue();
         if(quadStep != quadSpritesSorted) {
            this.rawIntBuffer.limit(quadStep * var8 + var8);
            this.rawIntBuffer.position(quadStep * var8);
            this.rawIntBuffer.get(var9);
            i = quadStep;

            for(indexQuad = var151[quadStep].intValue(); i != quadSpritesSorted; indexQuad = var151[indexQuad].intValue()) {
               this.rawIntBuffer.limit(indexQuad * var8 + var8);
               this.rawIntBuffer.position(indexQuad * var8);
               IntBuffer indexQuadSorted = this.rawIntBuffer.slice();
               this.rawIntBuffer.limit(i * var8 + var8);
               this.rawIntBuffer.position(i * var8);
               this.rawIntBuffer.put(indexQuadSorted);
               var161.set(i);
               i = indexQuad;
            }

            this.rawIntBuffer.limit(quadSpritesSorted * var8 + var8);
            this.rawIntBuffer.position(quadSpritesSorted * var8);
            this.rawIntBuffer.put(var9);
         }

         var161.set(quadSpritesSorted);
      }

      this.rawIntBuffer.limit(this.rawIntBuffer.capacity());
      this.rawIntBuffer.position(this.getBufferSize());
      if(this.quadSprites != null) {
         TextureAtlasSprite[] var17 = new TextureAtlasSprite[this.vertexCount / 4];
         quadStep = this.vertexFormat.getIntegerSize() / 4 * 4;

         for(i = 0; i < var151.length; ++i) {
            indexQuad = var151[i].intValue();
            var17[i] = this.quadSprites[indexQuad];
         }

         System.arraycopy(var17, 0, this.quadSprites, 0, var17.length);
      }

   }

   public WorldRenderer.State getVertexState() {
      this.rawIntBuffer.rewind();
      int var1 = this.getBufferSize();
      this.rawIntBuffer.limit(var1);
      int[] var2 = new int[var1];
      this.rawIntBuffer.get(var2);
      this.rawIntBuffer.limit(this.rawIntBuffer.capacity());
      this.rawIntBuffer.position(var1);
      TextureAtlasSprite[] quadSpritesCopy = null;
      if(this.quadSprites != null) {
         int countQuads = this.vertexCount / 4;
         quadSpritesCopy = new TextureAtlasSprite[countQuads];
         System.arraycopy(this.quadSprites, 0, quadSpritesCopy, 0, countQuads);
      }

      return new WorldRenderer.State(var2, new VertexFormat(this.vertexFormat), quadSpritesCopy);
   }

   public int getBufferSize() {
      return this.vertexCount * this.vertexFormat.getIntegerSize();
   }

   public static float getDistanceSq(FloatBuffer p_500127_0_, float p_500127_1_, float p_500127_2_, float p_500127_3_, int p_500127_4_, int p_500127_5_) {
      float var6 = p_500127_0_.get(p_500127_5_ + p_500127_4_ * 0 + 0);
      float var7 = p_500127_0_.get(p_500127_5_ + p_500127_4_ * 0 + 1);
      float var8 = p_500127_0_.get(p_500127_5_ + p_500127_4_ * 0 + 2);
      float var9 = p_500127_0_.get(p_500127_5_ + p_500127_4_ * 1 + 0);
      float var10 = p_500127_0_.get(p_500127_5_ + p_500127_4_ * 1 + 1);
      float var11 = p_500127_0_.get(p_500127_5_ + p_500127_4_ * 1 + 2);
      float var12 = p_500127_0_.get(p_500127_5_ + p_500127_4_ * 2 + 0);
      float var13 = p_500127_0_.get(p_500127_5_ + p_500127_4_ * 2 + 1);
      float var14 = p_500127_0_.get(p_500127_5_ + p_500127_4_ * 2 + 2);
      float var15 = p_500127_0_.get(p_500127_5_ + p_500127_4_ * 3 + 0);
      float var16 = p_500127_0_.get(p_500127_5_ + p_500127_4_ * 3 + 1);
      float var17 = p_500127_0_.get(p_500127_5_ + p_500127_4_ * 3 + 2);
      float var18 = (var6 + var9 + var12 + var15) * 0.25F - p_500127_1_;
      float var19 = (var7 + var10 + var13 + var16) * 0.25F - p_500127_2_;
      float var20 = (var8 + var11 + var14 + var17) * 0.25F - p_500127_3_;
      return var18 * var18 + var19 * var19 + var20 * var20;
   }

   public void setVertexState(WorldRenderer.State p_500124_1_) {
      this.rawIntBuffer.clear();
      this.growBuffer(p_500124_1_.getRawBuffer().length);
      this.rawIntBuffer.put(p_500124_1_.getRawBuffer());
      this.vertexCount = p_500124_1_.getVertexCount();
      this.vertexFormat = new VertexFormat(p_500124_1_.getVertexFormat());
      if(p_500124_1_.stateQuadSprites != null) {
         if(this.quadSprites == null) {
            this.quadSprites = this.quadSpritesPrev;
         }

         if(this.quadSprites == null || this.quadSprites.length < this.getBufferQuadSize()) {
            this.quadSprites = new TextureAtlasSprite[this.getBufferQuadSize()];
         }

         TextureAtlasSprite[] src = p_500124_1_.stateQuadSprites;
         System.arraycopy(src, 0, this.quadSprites, 0, src.length);
      } else {
         if(this.quadSprites != null) {
            this.quadSpritesPrev = this.quadSprites;
         }

         this.quadSprites = null;
      }

   }

   public void reset() {
      this.vertexCount = 0;
      this.vertexFormatElement = null;
      this.vertexFormatIndex = 0;
      this.quadSprite = null;
   }

   public void begin(int p_500126_1_, VertexFormat p_500126_2_) {
      if(this.isDrawing) {
         throw new IllegalStateException("Already building!");
      } else {
         this.isDrawing = true;
         this.reset();
         this.drawMode = p_500126_1_;
         this.vertexFormat = p_500126_2_;
         this.vertexFormatElement = p_500126_2_.getElement(this.vertexFormatIndex);
         this.noColor = false;
         this.byteBuffer.limit(this.byteBuffer.capacity());
         if(Config.isShaders()) {
            SVertexBuilder.endSetVertexFormat(this);
         }

         if(Config.isMultiTexture()) {
            if(this.blockLayer != null) {
               if(this.quadSprites == null) {
                  this.quadSprites = this.quadSpritesPrev;
               }

               if(this.quadSprites == null || this.quadSprites.length < this.getBufferQuadSize()) {
                  this.quadSprites = new TextureAtlasSprite[this.getBufferQuadSize()];
               }
            }
         } else {
            if(this.quadSprites != null) {
               this.quadSpritesPrev = this.quadSprites;
            }

            this.quadSprites = null;
         }

      }
   }

   public WorldRenderer tex(double p_500127_1_, double p_500127_3_) {
      if(this.quadSprite != null && this.quadSprites != null) {
         p_500127_1_ = (double)this.quadSprite.toSingleU((float)p_500127_1_);
         p_500127_3_ = (double)this.quadSprite.toSingleV((float)p_500127_3_);
         this.quadSprites[this.vertexCount / 4] = this.quadSprite;
      }

      int var5 = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.getOffset(this.vertexFormatIndex);
      switch(WorldRenderer.SwitchEnumUseage.field_181661_a[this.vertexFormatElement.getType().ordinal()]) {
      case 1:
         this.byteBuffer.putFloat(var5, (float)p_500127_1_);
         this.byteBuffer.putFloat(var5 + 4, (float)p_500127_3_);
         break;
      case 2:
      case 3:
         this.byteBuffer.putInt(var5, (int)p_500127_1_);
         this.byteBuffer.putInt(var5 + 4, (int)p_500127_3_);
         break;
      case 4:
      case 5:
         this.byteBuffer.putShort(var5, (short)((int)p_500127_3_));
         this.byteBuffer.putShort(var5 + 2, (short)((int)p_500127_1_));
         break;
      case 6:
      case 7:
         this.byteBuffer.put(var5, (byte)((int)p_500127_3_));
         this.byteBuffer.put(var5 + 1, (byte)((int)p_500127_1_));
      }

      this.nextVertexFormatIndex();
      return this;
   }

   public WorldRenderer lightmap(int p_500128_1_, int p_500128_2_) {
      int var3 = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.getOffset(this.vertexFormatIndex);
      switch(WorldRenderer.SwitchEnumUseage.field_181661_a[this.vertexFormatElement.getType().ordinal()]) {
      case 1:
         this.byteBuffer.putFloat(var3, (float)p_500128_1_);
         this.byteBuffer.putFloat(var3 + 4, (float)p_500128_2_);
         break;
      case 2:
      case 3:
         this.byteBuffer.putInt(var3, p_500128_1_);
         this.byteBuffer.putInt(var3 + 4, p_500128_2_);
         break;
      case 4:
      case 5:
         this.byteBuffer.putShort(var3, (short)p_500128_2_);
         this.byteBuffer.putShort(var3 + 2, (short)p_500128_1_);
         break;
      case 6:
      case 7:
         this.byteBuffer.put(var3, (byte)p_500128_2_);
         this.byteBuffer.put(var3 + 1, (byte)p_500128_1_);
      }

      this.nextVertexFormatIndex();
      return this;
   }

   public void putBrightness4(int p_178962_1_, int p_178962_2_, int p_178962_3_, int p_178962_4_) {
      int var5 = (this.vertexCount - 4) * this.vertexFormat.getIntegerSize() + this.vertexFormat.getUvOffsetById(1) / 4;
      int var6 = this.vertexFormat.getNextOffset() >> 2;
      this.rawIntBuffer.put(var5, p_178962_1_);
      this.rawIntBuffer.put(var5 + var6, p_178962_2_);
      this.rawIntBuffer.put(var5 + var6 * 2, p_178962_3_);
      this.rawIntBuffer.put(var5 + var6 * 3, p_178962_4_);
   }

   public void putPosition(double p_178987_1_, double p_178987_3_, double p_178987_5_) {
      int var7 = this.vertexFormat.getIntegerSize();
      int var8 = (this.vertexCount - 4) * var7;

      for(int var9 = 0; var9 < 4; ++var9) {
         int var10 = var8 + var9 * var7;
         int var11 = var10 + 1;
         int var12 = var11 + 1;
         this.rawIntBuffer.put(var10, Float.floatToRawIntBits((float)(p_178987_1_ + this.xOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(var10))));
         this.rawIntBuffer.put(var11, Float.floatToRawIntBits((float)(p_178987_3_ + this.yOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(var11))));
         this.rawIntBuffer.put(var12, Float.floatToRawIntBits((float)(p_178987_5_ + this.zOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(var12))));
      }

   }

   public int getColorIndex(int p_78909_1_) {
      return ((this.vertexCount - p_78909_1_) * this.vertexFormat.getNextOffset() + this.vertexFormat.getColorOffset()) / 4;
   }

   public void putColorMultiplier(float p_178978_1_, float p_178978_2_, float p_178978_3_, int p_178978_4_) {
      int var5 = this.getColorIndex(p_178978_4_);
      int var6 = -1;
      if(!this.noColor) {
         var6 = this.rawIntBuffer.get(var5);
         int var7;
         int var8;
         int var9;
         if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            var7 = (int)((float)(var6 & 255) * p_178978_1_);
            var8 = (int)((float)(var6 >> 8 & 255) * p_178978_2_);
            var9 = (int)((float)(var6 >> 16 & 255) * p_178978_3_);
            var6 &= -16777216;
            var6 |= var9 << 16 | var8 << 8 | var7;
         } else {
            var7 = (int)((float)(var6 >> 24 & 255) * p_178978_1_);
            var8 = (int)((float)(var6 >> 16 & 255) * p_178978_2_);
            var9 = (int)((float)(var6 >> 8 & 255) * p_178978_3_);
            var6 &= 255;
            var6 |= var7 << 24 | var8 << 16 | var9 << 8;
         }
      }

      this.rawIntBuffer.put(var5, var6);
   }

   public void putColor(int p_178988_1_, int p_178988_2_) {
      int var3 = this.getColorIndex(p_178988_2_);
      int var4 = p_178988_1_ >> 16 & 255;
      int var5 = p_178988_1_ >> 8 & 255;
      int var6 = p_178988_1_ & 255;
      int var7 = p_178988_1_ >> 24 & 255;
      this.putColorRGBA(var3, var4, var5, var6, var7);
   }

   public void putColorRGB_F(float p_178994_1_, float p_178994_2_, float p_178994_3_, int p_178994_4_) {
      int var5 = this.getColorIndex(p_178994_4_);
      int var6 = MathHelper.clamp_int((int)(p_178994_1_ * 255.0F), 0, 255);
      int var7 = MathHelper.clamp_int((int)(p_178994_2_ * 255.0F), 0, 255);
      int var8 = MathHelper.clamp_int((int)(p_178994_3_ * 255.0F), 0, 255);
      this.putColorRGBA(var5, var6, var7, var8, 255);
   }

   public void putColorRGBA(int p_178972_1_, int p_178972_2_, int p_178972_3_, int p_178972_4_, int p_178972_5_) {
      if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
         this.rawIntBuffer.put(p_178972_1_, p_178972_5_ << 24 | p_178972_4_ << 16 | p_178972_3_ << 8 | p_178972_2_);
      } else {
         this.rawIntBuffer.put(p_178972_1_, p_178972_2_ << 24 | p_178972_3_ << 16 | p_178972_4_ << 8 | p_178972_5_);
      }

   }

   public void noColor() {
      this.noColor = true;
   }

   public WorldRenderer color(float p_500130_1_, float p_500130_2_, float p_500130_3_, float p_500130_4_) {
      return this.color((int)(p_500130_1_ * 255.0F), (int)(p_500130_2_ * 255.0F), (int)(p_500130_3_ * 255.0F), (int)(p_500130_4_ * 255.0F));
   }

   public WorldRenderer color(int p_500131_1_, int p_500131_2_, int p_500131_3_, int p_500131_4_) {
      if(this.noColor) {
         return this;
      } else {
         int var5 = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.getOffset(this.vertexFormatIndex);
         switch(WorldRenderer.SwitchEnumUseage.field_181661_a[this.vertexFormatElement.getType().ordinal()]) {
         case 1:
            this.byteBuffer.putFloat(var5, (float)p_500131_1_ / 255.0F);
            this.byteBuffer.putFloat(var5 + 4, (float)p_500131_2_ / 255.0F);
            this.byteBuffer.putFloat(var5 + 8, (float)p_500131_3_ / 255.0F);
            this.byteBuffer.putFloat(var5 + 12, (float)p_500131_4_ / 255.0F);
            break;
         case 2:
         case 3:
            this.byteBuffer.putFloat(var5, (float)p_500131_1_);
            this.byteBuffer.putFloat(var5 + 4, (float)p_500131_2_);
            this.byteBuffer.putFloat(var5 + 8, (float)p_500131_3_);
            this.byteBuffer.putFloat(var5 + 12, (float)p_500131_4_);
            break;
         case 4:
         case 5:
            this.byteBuffer.putShort(var5, (short)p_500131_1_);
            this.byteBuffer.putShort(var5 + 2, (short)p_500131_2_);
            this.byteBuffer.putShort(var5 + 4, (short)p_500131_3_);
            this.byteBuffer.putShort(var5 + 6, (short)p_500131_4_);
            break;
         case 6:
         case 7:
            if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
               this.byteBuffer.put(var5, (byte)p_500131_1_);
               this.byteBuffer.put(var5 + 1, (byte)p_500131_2_);
               this.byteBuffer.put(var5 + 2, (byte)p_500131_3_);
               this.byteBuffer.put(var5 + 3, (byte)p_500131_4_);
            } else {
               this.byteBuffer.put(var5, (byte)p_500131_4_);
               this.byteBuffer.put(var5 + 1, (byte)p_500131_3_);
               this.byteBuffer.put(var5 + 2, (byte)p_500131_2_);
               this.byteBuffer.put(var5 + 3, (byte)p_500131_1_);
            }
         }

         this.nextVertexFormatIndex();
         return this;
      }
   }

   public void addVertexData(int[] p_178981_1_) {
      if(Config.isShaders()) {
         SVertexBuilder.beginAddVertexData(this, p_178981_1_);
      }

      this.growBuffer(p_178981_1_.length);
      this.rawIntBuffer.position(this.getBufferSize());
      this.rawIntBuffer.put(p_178981_1_);
      this.vertexCount += p_178981_1_.length / this.vertexFormat.getIntegerSize();
      if(Config.isShaders()) {
         SVertexBuilder.endAddVertexData(this);
      }

   }

   public void endVertex() {
      ++this.vertexCount;
      this.growBuffer(this.vertexFormat.getIntegerSize());
      this.vertexFormatIndex = 0;
      this.vertexFormatElement = this.vertexFormat.getElement(this.vertexFormatIndex);
      if(Config.isShaders()) {
         SVertexBuilder.endAddVertex(this);
      }

   }

   public WorldRenderer pos(double p_500133_1_, double p_500133_3_, double p_500133_5_) {
      if(Config.isShaders()) {
         SVertexBuilder.beginAddVertex(this);
      }

      int var7 = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.getOffset(this.vertexFormatIndex);
      switch(WorldRenderer.SwitchEnumUseage.field_181661_a[this.vertexFormatElement.getType().ordinal()]) {
      case 1:
         this.byteBuffer.putFloat(var7, (float)(p_500133_1_ + this.xOffset));
         this.byteBuffer.putFloat(var7 + 4, (float)(p_500133_3_ + this.yOffset));
         this.byteBuffer.putFloat(var7 + 8, (float)(p_500133_5_ + this.zOffset));
         break;
      case 2:
      case 3:
         this.byteBuffer.putInt(var7, Float.floatToRawIntBits((float)(p_500133_1_ + this.xOffset)));
         this.byteBuffer.putInt(var7 + 4, Float.floatToRawIntBits((float)(p_500133_3_ + this.yOffset)));
         this.byteBuffer.putInt(var7 + 8, Float.floatToRawIntBits((float)(p_500133_5_ + this.zOffset)));
         break;
      case 4:
      case 5:
         this.byteBuffer.putShort(var7, (short)((int)(p_500133_1_ + this.xOffset)));
         this.byteBuffer.putShort(var7 + 2, (short)((int)(p_500133_3_ + this.yOffset)));
         this.byteBuffer.putShort(var7 + 4, (short)((int)(p_500133_5_ + this.zOffset)));
         break;
      case 6:
      case 7:
         this.byteBuffer.put(var7, (byte)((int)(p_500133_1_ + this.xOffset)));
         this.byteBuffer.put(var7 + 1, (byte)((int)(p_500133_3_ + this.yOffset)));
         this.byteBuffer.put(var7 + 2, (byte)((int)(p_500133_5_ + this.zOffset)));
      }

      this.nextVertexFormatIndex();
      return this;
   }

   public void putNormal(float p_500134_1_, float p_500134_2_, float p_500134_3_) {
      int var4 = (byte)((int)(p_500134_1_ * 127.0F)) & 255;
      int var5 = (byte)((int)(p_500134_2_ * 127.0F)) & 255;
      int var6 = (byte)((int)(p_500134_3_ * 127.0F)) & 255;
      int var7 = var4 | var5 << 8 | var6 << 16;
      int var8 = this.vertexFormat.getNextOffset() >> 2;
      int var9 = (this.vertexCount - 4) * var8 + this.vertexFormat.getNormalOffset() / 4;
      this.rawIntBuffer.put(var9, var7);
      this.rawIntBuffer.put(var9 + var8, var7);
      this.rawIntBuffer.put(var9 + var8 * 2, var7);
      this.rawIntBuffer.put(var9 + var8 * 3, var7);
   }

   public void nextVertexFormatIndex() {
      ++this.vertexFormatIndex;
      this.vertexFormatIndex %= this.vertexFormat.getElementCount();
      this.vertexFormatElement = this.vertexFormat.getElement(this.vertexFormatIndex);
      if(this.vertexFormatElement.getUsage() == EnumUsage.PADDING) {
         this.nextVertexFormatIndex();
      }

   }

   public WorldRenderer normal(float p_500135_1_, float p_500135_2_, float p_500135_3_) {
      int var4 = this.vertexCount * this.vertexFormat.getNextOffset() + this.vertexFormat.getOffset(this.vertexFormatIndex);
      switch(WorldRenderer.SwitchEnumUseage.field_181661_a[this.vertexFormatElement.getType().ordinal()]) {
      case 1:
         this.byteBuffer.putFloat(var4, p_500135_1_);
         this.byteBuffer.putFloat(var4 + 4, p_500135_2_);
         this.byteBuffer.putFloat(var4 + 8, p_500135_3_);
         break;
      case 2:
      case 3:
         this.byteBuffer.putInt(var4, (int)p_500135_1_);
         this.byteBuffer.putInt(var4 + 4, (int)p_500135_2_);
         this.byteBuffer.putInt(var4 + 8, (int)p_500135_3_);
         break;
      case 4:
      case 5:
         this.byteBuffer.putShort(var4, (short)((int)(p_500135_1_ * 32767.0F) & '\uffff'));
         this.byteBuffer.putShort(var4 + 2, (short)((int)(p_500135_2_ * 32767.0F) & '\uffff'));
         this.byteBuffer.putShort(var4 + 4, (short)((int)(p_500135_3_ * 32767.0F) & '\uffff'));
         break;
      case 6:
      case 7:
         this.byteBuffer.put(var4, (byte)((int)(p_500135_1_ * 127.0F) & 255));
         this.byteBuffer.put(var4 + 1, (byte)((int)(p_500135_2_ * 127.0F) & 255));
         this.byteBuffer.put(var4 + 2, (byte)((int)(p_500135_3_ * 127.0F) & 255));
      }

      this.nextVertexFormatIndex();
      return this;
   }

   public void setTranslation(double p_178969_1_, double p_178969_3_, double p_178969_5_) {
      this.xOffset = p_178969_1_;
      this.yOffset = p_178969_3_;
      this.zOffset = p_178969_5_;
   }

   public void finishDrawing() {
      if(!this.isDrawing) {
         throw new IllegalStateException("Not building!");
      } else {
         this.isDrawing = false;
         this.byteBuffer.position(0);
         this.byteBuffer.limit(this.getBufferSize() * 4);
      }
   }

   public ByteBuffer getByteBuffer() {
      return this.byteBuffer;
   }

   public VertexFormat getVertexFormat() {
      return this.vertexFormat;
   }

   public int getVertexCount() {
      return this.vertexCount;
   }

   public int getDrawMode() {
      return this.drawMode;
   }

   public void putColor4(int p_178968_1_) {
      for(int var2 = 0; var2 < 4; ++var2) {
         this.putColor(p_178968_1_, var2 + 1);
      }

   }

   public void putColorRGB_F4(float p_178990_1_, float p_178990_2_, float p_178990_3_) {
      for(int var4 = 0; var4 < 4; ++var4) {
         this.putColorRGB_F(p_178990_1_, p_178990_2_, p_178990_3_, var4 + 1);
      }

   }

   public void putSprite(TextureAtlasSprite sprite) {
      if(this.quadSprites != null) {
         int countQuads = this.vertexCount / 4;
         this.quadSprites[countQuads - 1] = sprite;
      }
   }

   public void setSprite(TextureAtlasSprite sprite) {
      if(this.quadSprites != null) {
         this.quadSprite = sprite;
      }
   }

   public boolean isMultiTexture() {
      return this.quadSprites != null;
   }

   public void drawMultiTexture() {
      if(this.quadSprites != null) {
         int maxTextureIndex = Config.getMinecraft().getTextureMapBlocks().getCountRegisteredSprites();
         if(this.drawnIcons.length <= maxTextureIndex) {
            this.drawnIcons = new boolean[maxTextureIndex + 1];
         }

         Arrays.fill(this.drawnIcons, false);
         int texSwitch = 0;
         int grassOverlayIndex = -1;
         int countQuads = this.vertexCount / 4;

         for(int i = 0; i < countQuads; ++i) {
            TextureAtlasSprite icon = this.quadSprites[i];
            if(icon != null) {
               int iconIndex = icon.getIndexInMap();
               if(!this.drawnIcons[iconIndex]) {
                  if(icon == TextureUtils.iconGrassSideOverlay) {
                     if(grassOverlayIndex < 0) {
                        grassOverlayIndex = i;
                     }
                  } else {
                     i = this.drawForIcon(icon, i) - 1;
                     ++texSwitch;
                     if(this.blockLayer != EnumWorldBlockLayer.TRANSLUCENT) {
                        this.drawnIcons[iconIndex] = true;
                     }
                  }
               }
            }
         }

         if(grassOverlayIndex >= 0) {
            this.drawForIcon(TextureUtils.iconGrassSideOverlay, grassOverlayIndex);
            ++texSwitch;
         }

         if(texSwitch > 0) {
            ;
         }

      }
   }

   public int drawForIcon(TextureAtlasSprite sprite, int startQuadPos) {
      GL11.glBindTexture(3553, sprite.glSpriteTextureId);
      int firstRegionEnd = -1;
      int lastPos = -1;
      int countQuads = this.vertexCount / 4;

      for(int i = startQuadPos; i < countQuads; ++i) {
         TextureAtlasSprite ts = this.quadSprites[i];
         if(ts == sprite) {
            if(lastPos < 0) {
               lastPos = i;
            }
         } else if(lastPos >= 0) {
            this.draw(lastPos, i);
            if(this.blockLayer == EnumWorldBlockLayer.TRANSLUCENT) {
               return i;
            }

            lastPos = -1;
            if(firstRegionEnd < 0) {
               firstRegionEnd = i;
            }
         }
      }

      if(lastPos >= 0) {
         this.draw(lastPos, countQuads);
      }

      if(firstRegionEnd < 0) {
         firstRegionEnd = countQuads;
      }

      return firstRegionEnd;
   }

   public void draw(int startQuadVertex, int endQuadVertex) {
      int vxQuadCount = endQuadVertex - startQuadVertex;
      if(vxQuadCount > 0) {
         int startVertex = startQuadVertex * 4;
         int vxCount = vxQuadCount * 4;
         GL11.glDrawArrays(this.drawMode, startVertex, vxCount);
      }
   }

   public void setBlockLayer(EnumWorldBlockLayer blockLayer) {
      this.blockLayer = blockLayer;
      if(blockLayer == null) {
         if(this.quadSprites != null) {
            this.quadSpritesPrev = this.quadSprites;
         }

         this.quadSprites = null;
         this.quadSprite = null;
      }

   }

   public int getBufferQuadSize() {
      int quadSize = this.rawIntBuffer.capacity() * 4 / (this.vertexFormat.getIntegerSize() * 4);
      return quadSize;
   }

   public void checkAndGrow() {
      this.growBuffer(this.vertexFormat.getIntegerSize());
   }

   public boolean isColorDisabled() {
      return this.noColor;
   }

   public static final class SwitchEnumUseage {

      public static final int[] field_181661_a = new int[EnumType.values().length];
      public static final String __OBFID = "CL_00002569";


      static {
         try {
            field_181661_a[EnumType.FLOAT.ordinal()] = 1;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            field_181661_a[EnumType.UINT.ordinal()] = 2;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            field_181661_a[EnumType.INT.ordinal()] = 3;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            field_181661_a[EnumType.USHORT.ordinal()] = 4;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            field_181661_a[EnumType.SHORT.ordinal()] = 5;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            field_181661_a[EnumType.UBYTE.ordinal()] = 6;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            field_181661_a[EnumType.BYTE.ordinal()] = 7;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public class State {

      public final int[] stateRawBuffer;
      public final VertexFormat stateVertexFormat;
      public static final String __OBFID = "CL_00002568";
      public TextureAtlasSprite[] stateQuadSprites;


      public State(int[] buf, VertexFormat vertFormat, TextureAtlasSprite[] quadSprites) {
         this.stateRawBuffer = buf;
         this.stateVertexFormat = vertFormat;
         this.stateQuadSprites = quadSprites;
      }

      public State(int[] p_i46380_2_, VertexFormat p_i46380_3_) {
         this.stateRawBuffer = p_i46380_2_;
         this.stateVertexFormat = p_i46380_3_;
      }

      public int[] getRawBuffer() {
         return this.stateRawBuffer;
      }

      public int getVertexCount() {
         return this.stateRawBuffer.length / this.stateVertexFormat.getIntegerSize();
      }

      public VertexFormat getVertexFormat() {
         return this.stateVertexFormat;
      }
   }
}
