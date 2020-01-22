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
import net.minecraft.src.Config;
import net.minecraft.src.TextureUtils;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.GL11;
import shadersmod.client.SVertexBuilder;

public class WorldRenderer {
   private ByteBuffer field_179001_a;
   public IntBuffer field_178999_b;
   private ShortBuffer field_181676_c;
   public FloatBuffer field_179000_c;
   public int field_178997_d;
   private VertexFormatElement field_181677_f;
   private int field_181678_g;
   private boolean field_78939_q;
   public int field_179006_k;
   private double field_179004_l;
   private double field_179005_m;
   private double field_179002_n;
   private VertexFormat field_179011_q;
   private boolean field_179010_r;
   private static final String __OBFID = "CL_00000942";
   private EnumWorldBlockLayer blockLayer = null;
   private boolean[] drawnIcons = new boolean[256];
   private TextureAtlasSprite[] quadSprites = null;
   private TextureAtlasSprite[] quadSpritesPrev = null;
   private TextureAtlasSprite quadSprite = null;
   public SVertexBuilder sVertexBuilder;

   public WorldRenderer(int p_i46275_1_) {
      if(Config.isShaders()) {
         p_i46275_1_ *= 2;
      }

      this.field_179001_a = GLAllocation.func_74524_c(p_i46275_1_ * 4);
      this.field_178999_b = this.field_179001_a.asIntBuffer();
      this.field_181676_c = this.field_179001_a.asShortBuffer();
      this.field_179000_c = this.field_179001_a.asFloatBuffer();
      SVertexBuilder.initVertexBuilder(this);
   }

   private void func_181670_b(int p_181670_1_) {
      if(Config.isShaders()) {
         p_181670_1_ *= 2;
      }

      if(p_181670_1_ > this.field_178999_b.remaining()) {
         int i = this.field_179001_a.capacity();
         int j = i % 2097152;
         int k = j + (((this.field_178999_b.position() + p_181670_1_) * 4 - j) / 2097152 + 1) * 2097152;
         LogManager.getLogger().warn("Needed to grow BufferBuilder buffer: Old size " + i + " bytes, new size " + k + " bytes.");
         int l = this.field_178999_b.position();
         ByteBuffer bytebuffer = GLAllocation.func_74524_c(k);
         this.field_179001_a.position(0);
         bytebuffer.put(this.field_179001_a);
         bytebuffer.rewind();
         this.field_179001_a = bytebuffer;
         this.field_179000_c = this.field_179001_a.asFloatBuffer();
         this.field_178999_b = this.field_179001_a.asIntBuffer();
         this.field_178999_b.position(l);
         this.field_181676_c = this.field_179001_a.asShortBuffer();
         this.field_181676_c.position(l << 1);
         if(this.quadSprites != null) {
            TextureAtlasSprite[] atextureatlassprite = this.quadSprites;
            int i1 = this.getBufferQuadSize();
            this.quadSprites = new TextureAtlasSprite[i1];
            System.arraycopy(atextureatlassprite, 0, this.quadSprites, 0, Math.min(atextureatlassprite.length, this.quadSprites.length));
            this.quadSpritesPrev = null;
         }
      }

   }

   public void func_181674_a(float p_181674_1_, float p_181674_2_, float p_181674_3_) {
      int i = this.field_178997_d / 4;
      float[] afloat = new float[i];

      for(int j = 0; j < i; ++j) {
         afloat[j] = func_181665_a(this.field_179000_c, (float)((double)p_181674_1_ + this.field_179004_l), (float)((double)p_181674_2_ + this.field_179005_m), (float)((double)p_181674_3_ + this.field_179002_n), this.field_179011_q.func_181719_f(), j * this.field_179011_q.func_177338_f());
      }

      Integer[] ainteger = new Integer[i];

      for(int k = 0; k < ainteger.length; ++k) {
         ainteger[k] = Integer.valueOf(k);
      }

      Arrays.sort(ainteger, new WorldRenderer$1(this, afloat));
      BitSet bitset = new BitSet();
      int l = this.field_179011_q.func_177338_f();
      int[] aint = new int[l];

      for(int l1 = 0; (l1 = bitset.nextClearBit(l1)) < ainteger.length; ++l1) {
         int i1 = ainteger[l1].intValue();
         if(i1 != l1) {
            this.field_178999_b.limit(i1 * l + l);
            this.field_178999_b.position(i1 * l);
            this.field_178999_b.get(aint);
            int j1 = i1;

            for(int k1 = ainteger[i1].intValue(); j1 != l1; k1 = ainteger[k1].intValue()) {
               this.field_178999_b.limit(k1 * l + l);
               this.field_178999_b.position(k1 * l);
               IntBuffer intbuffer = this.field_178999_b.slice();
               this.field_178999_b.limit(j1 * l + l);
               this.field_178999_b.position(j1 * l);
               this.field_178999_b.put(intbuffer);
               bitset.set(j1);
               j1 = k1;
            }

            this.field_178999_b.limit(l1 * l + l);
            this.field_178999_b.position(l1 * l);
            this.field_178999_b.put(aint);
         }

         bitset.set(l1);
      }

      this.field_178999_b.limit(this.field_178999_b.capacity());
      this.field_178999_b.position(this.func_181664_j());
      if(this.quadSprites != null) {
         TextureAtlasSprite[] atextureatlassprite = new TextureAtlasSprite[this.field_178997_d / 4];
         int i2 = this.field_179011_q.func_181719_f() / 4 * 4;

         for(int j2 = 0; j2 < ainteger.length; ++j2) {
            int k2 = ainteger[j2].intValue();
            atextureatlassprite[j2] = this.quadSprites[k2];
         }

         System.arraycopy(atextureatlassprite, 0, this.quadSprites, 0, atextureatlassprite.length);
      }

   }

   public WorldRenderer.State func_181672_a() {
      this.field_178999_b.rewind();
      int i = this.func_181664_j();
      this.field_178999_b.limit(i);
      int[] aint = new int[i];
      this.field_178999_b.get(aint);
      this.field_178999_b.limit(this.field_178999_b.capacity());
      this.field_178999_b.position(i);
      TextureAtlasSprite[] atextureatlassprite = null;
      if(this.quadSprites != null) {
         int j = this.field_178997_d / 4;
         atextureatlassprite = new TextureAtlasSprite[j];
         System.arraycopy(this.quadSprites, 0, atextureatlassprite, 0, j);
      }

      return new WorldRenderer.State(aint, new VertexFormat(this.field_179011_q), atextureatlassprite);
   }

   public int func_181664_j() {
      return this.field_178997_d * this.field_179011_q.func_181719_f();
   }

   private static float func_181665_a(FloatBuffer p_181665_0_, float p_181665_1_, float p_181665_2_, float p_181665_3_, int p_181665_4_, int p_181665_5_) {
      float f = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 0);
      float f1 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 1);
      float f2 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 0 + 2);
      float f3 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 0);
      float f4 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 1);
      float f5 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 1 + 2);
      float f6 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 0);
      float f7 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 1);
      float f8 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 2 + 2);
      float f9 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 0);
      float f10 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 1);
      float f11 = p_181665_0_.get(p_181665_5_ + p_181665_4_ * 3 + 2);
      float f12 = (f + f3 + f6 + f9) * 0.25F - p_181665_1_;
      float f13 = (f1 + f4 + f7 + f10) * 0.25F - p_181665_2_;
      float f14 = (f2 + f5 + f8 + f11) * 0.25F - p_181665_3_;
      return f12 * f12 + f13 * f13 + f14 * f14;
   }

   public void func_178993_a(WorldRenderer.State p_178993_1_) {
      this.field_178999_b.clear();
      this.func_181670_b(p_178993_1_.func_179013_a().length);
      this.field_178999_b.put(p_178993_1_.func_179013_a());
      this.field_178997_d = p_178993_1_.func_179014_c();
      this.field_179011_q = new VertexFormat(p_178993_1_.func_179016_d());
      if(p_178993_1_.stateQuadSprites != null) {
         if(this.quadSprites == null) {
            this.quadSprites = this.quadSpritesPrev;
         }

         if(this.quadSprites == null || this.quadSprites.length < this.getBufferQuadSize()) {
            this.quadSprites = new TextureAtlasSprite[this.getBufferQuadSize()];
         }

         TextureAtlasSprite[] atextureatlassprite = p_178993_1_.stateQuadSprites;
         System.arraycopy(atextureatlassprite, 0, this.quadSprites, 0, atextureatlassprite.length);
      } else {
         if(this.quadSprites != null) {
            this.quadSpritesPrev = this.quadSprites;
         }

         this.quadSprites = null;
      }

   }

   public void func_178965_a() {
      this.field_178997_d = 0;
      this.field_181677_f = null;
      this.field_181678_g = 0;
      this.quadSprite = null;
   }

   public void func_181668_a(int p_181668_1_, VertexFormat p_181668_2_) {
      if(this.field_179010_r) {
         throw new IllegalStateException("Already building!");
      } else {
         this.field_179010_r = true;
         this.func_178965_a();
         this.field_179006_k = p_181668_1_;
         this.field_179011_q = p_181668_2_;
         this.field_181677_f = p_181668_2_.func_177348_c(this.field_181678_g);
         this.field_78939_q = false;
         this.field_179001_a.limit(this.field_179001_a.capacity());
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

   public WorldRenderer func_181673_a(double p_181673_1_, double p_181673_3_) {
      if(this.quadSprite != null && this.quadSprites != null) {
         p_181673_1_ = (double)this.quadSprite.toSingleU((float)p_181673_1_);
         p_181673_3_ = (double)this.quadSprite.toSingleV((float)p_181673_3_);
         this.quadSprites[this.field_178997_d / 4] = this.quadSprite;
      }

      int i = this.field_178997_d * this.field_179011_q.func_177338_f() + this.field_179011_q.func_181720_d(this.field_181678_g);
      switch(WorldRenderer.WorldRenderer$2.field_181661_a[this.field_181677_f.func_177367_b().ordinal()]) {
      case 1:
         this.field_179001_a.putFloat(i, (float)p_181673_1_);
         this.field_179001_a.putFloat(i + 4, (float)p_181673_3_);
         break;
      case 2:
      case 3:
         this.field_179001_a.putInt(i, (int)p_181673_1_);
         this.field_179001_a.putInt(i + 4, (int)p_181673_3_);
         break;
      case 4:
      case 5:
         this.field_179001_a.putShort(i, (short)((int)p_181673_3_));
         this.field_179001_a.putShort(i + 2, (short)((int)p_181673_1_));
         break;
      case 6:
      case 7:
         this.field_179001_a.put(i, (byte)((int)p_181673_3_));
         this.field_179001_a.put(i + 1, (byte)((int)p_181673_1_));
      }

      this.func_181667_k();
      return this;
   }

   public WorldRenderer func_181671_a(int p_181671_1_, int p_181671_2_) {
      int i = this.field_178997_d * this.field_179011_q.func_177338_f() + this.field_179011_q.func_181720_d(this.field_181678_g);
      switch(WorldRenderer.WorldRenderer$2.field_181661_a[this.field_181677_f.func_177367_b().ordinal()]) {
      case 1:
         this.field_179001_a.putFloat(i, (float)p_181671_1_);
         this.field_179001_a.putFloat(i + 4, (float)p_181671_2_);
         break;
      case 2:
      case 3:
         this.field_179001_a.putInt(i, p_181671_1_);
         this.field_179001_a.putInt(i + 4, p_181671_2_);
         break;
      case 4:
      case 5:
         this.field_179001_a.putShort(i, (short)p_181671_2_);
         this.field_179001_a.putShort(i + 2, (short)p_181671_1_);
         break;
      case 6:
      case 7:
         this.field_179001_a.put(i, (byte)p_181671_2_);
         this.field_179001_a.put(i + 1, (byte)p_181671_1_);
      }

      this.func_181667_k();
      return this;
   }

   public void func_178962_a(int p_178962_1_, int p_178962_2_, int p_178962_3_, int p_178962_4_) {
      int i = (this.field_178997_d - 4) * this.field_179011_q.func_181719_f() + this.field_179011_q.func_177344_b(1) / 4;
      int j = this.field_179011_q.func_177338_f() >> 2;
      this.field_178999_b.put(i, p_178962_1_);
      this.field_178999_b.put(i + j, p_178962_2_);
      this.field_178999_b.put(i + j * 2, p_178962_3_);
      this.field_178999_b.put(i + j * 3, p_178962_4_);
   }

   public void func_178987_a(double p_178987_1_, double p_178987_3_, double p_178987_5_) {
      int i = this.field_179011_q.func_181719_f();
      int j = (this.field_178997_d - 4) * i;

      for(int k = 0; k < 4; ++k) {
         int l = j + k * i;
         int i1 = l + 1;
         int j1 = i1 + 1;
         this.field_178999_b.put(l, Float.floatToRawIntBits((float)(p_178987_1_ + this.field_179004_l) + Float.intBitsToFloat(this.field_178999_b.get(l))));
         this.field_178999_b.put(i1, Float.floatToRawIntBits((float)(p_178987_3_ + this.field_179005_m) + Float.intBitsToFloat(this.field_178999_b.get(i1))));
         this.field_178999_b.put(j1, Float.floatToRawIntBits((float)(p_178987_5_ + this.field_179002_n) + Float.intBitsToFloat(this.field_178999_b.get(j1))));
      }

   }

   public int func_78909_a(int p_78909_1_) {
      return ((this.field_178997_d - p_78909_1_) * this.field_179011_q.func_177338_f() + this.field_179011_q.func_177340_e()) / 4;
   }

   public void func_178978_a(float p_178978_1_, float p_178978_2_, float p_178978_3_, int p_178978_4_) {
      int i = this.func_78909_a(p_178978_4_);
      int j = -1;
      if(!this.field_78939_q) {
         j = this.field_178999_b.get(i);
         if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            int k = (int)((float)(j & 255) * p_178978_1_);
            int l = (int)((float)(j >> 8 & 255) * p_178978_2_);
            int i1 = (int)((float)(j >> 16 & 255) * p_178978_3_);
            j = j & -16777216;
            j = j | i1 << 16 | l << 8 | k;
         } else {
            int j1 = (int)((float)(j >> 24 & 255) * p_178978_1_);
            int k1 = (int)((float)(j >> 16 & 255) * p_178978_2_);
            int l1 = (int)((float)(j >> 8 & 255) * p_178978_3_);
            j = j & 255;
            j = j | j1 << 24 | k1 << 16 | l1 << 8;
         }
      }

      this.field_178999_b.put(i, j);
   }

   private void func_178988_b(int p_178988_1_, int p_178988_2_) {
      int i = this.func_78909_a(p_178988_2_);
      int j = p_178988_1_ >> 16 & 255;
      int k = p_178988_1_ >> 8 & 255;
      int l = p_178988_1_ & 255;
      int i1 = p_178988_1_ >> 24 & 255;
      this.func_178972_a(i, j, k, l, i1);
   }

   public void func_178994_b(float p_178994_1_, float p_178994_2_, float p_178994_3_, int p_178994_4_) {
      int i = this.func_78909_a(p_178994_4_);
      int j = MathHelper.func_76125_a((int)(p_178994_1_ * 255.0F), 0, 255);
      int k = MathHelper.func_76125_a((int)(p_178994_2_ * 255.0F), 0, 255);
      int l = MathHelper.func_76125_a((int)(p_178994_3_ * 255.0F), 0, 255);
      this.func_178972_a(i, j, k, l, 255);
   }

   public void func_178972_a(int p_178972_1_, int p_178972_2_, int p_178972_3_, int p_178972_4_, int p_178972_5_) {
      if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
         this.field_178999_b.put(p_178972_1_, p_178972_5_ << 24 | p_178972_4_ << 16 | p_178972_3_ << 8 | p_178972_2_);
      } else {
         this.field_178999_b.put(p_178972_1_, p_178972_2_ << 24 | p_178972_3_ << 16 | p_178972_4_ << 8 | p_178972_5_);
      }

   }

   public void func_78914_f() {
      this.field_78939_q = true;
   }

   public WorldRenderer func_181666_a(float p_181666_1_, float p_181666_2_, float p_181666_3_, float p_181666_4_) {
      return this.func_181669_b((int)(p_181666_1_ * 255.0F), (int)(p_181666_2_ * 255.0F), (int)(p_181666_3_ * 255.0F), (int)(p_181666_4_ * 255.0F));
   }

   public WorldRenderer func_181669_b(int p_181669_1_, int p_181669_2_, int p_181669_3_, int p_181669_4_) {
      if(this.field_78939_q) {
         return this;
      } else {
         int i = this.field_178997_d * this.field_179011_q.func_177338_f() + this.field_179011_q.func_181720_d(this.field_181678_g);
         switch(WorldRenderer.WorldRenderer$2.field_181661_a[this.field_181677_f.func_177367_b().ordinal()]) {
         case 1:
            this.field_179001_a.putFloat(i, (float)p_181669_1_ / 255.0F);
            this.field_179001_a.putFloat(i + 4, (float)p_181669_2_ / 255.0F);
            this.field_179001_a.putFloat(i + 8, (float)p_181669_3_ / 255.0F);
            this.field_179001_a.putFloat(i + 12, (float)p_181669_4_ / 255.0F);
            break;
         case 2:
         case 3:
            this.field_179001_a.putFloat(i, (float)p_181669_1_);
            this.field_179001_a.putFloat(i + 4, (float)p_181669_2_);
            this.field_179001_a.putFloat(i + 8, (float)p_181669_3_);
            this.field_179001_a.putFloat(i + 12, (float)p_181669_4_);
            break;
         case 4:
         case 5:
            this.field_179001_a.putShort(i, (short)p_181669_1_);
            this.field_179001_a.putShort(i + 2, (short)p_181669_2_);
            this.field_179001_a.putShort(i + 4, (short)p_181669_3_);
            this.field_179001_a.putShort(i + 6, (short)p_181669_4_);
            break;
         case 6:
         case 7:
            if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
               this.field_179001_a.put(i, (byte)p_181669_1_);
               this.field_179001_a.put(i + 1, (byte)p_181669_2_);
               this.field_179001_a.put(i + 2, (byte)p_181669_3_);
               this.field_179001_a.put(i + 3, (byte)p_181669_4_);
            } else {
               this.field_179001_a.put(i, (byte)p_181669_4_);
               this.field_179001_a.put(i + 1, (byte)p_181669_3_);
               this.field_179001_a.put(i + 2, (byte)p_181669_2_);
               this.field_179001_a.put(i + 3, (byte)p_181669_1_);
            }
         }

         this.func_181667_k();
         return this;
      }
   }

   public void func_178981_a(int[] p_178981_1_) {
      if(Config.isShaders()) {
         SVertexBuilder.beginAddVertexData(this, p_178981_1_);
      }

      this.func_181670_b(p_178981_1_.length);
      this.field_178999_b.position(this.func_181664_j());
      this.field_178999_b.put(p_178981_1_);
      this.field_178997_d += p_178981_1_.length / this.field_179011_q.func_181719_f();
      if(Config.isShaders()) {
         SVertexBuilder.endAddVertexData(this);
      }

   }

   public void func_181675_d() {
      ++this.field_178997_d;
      this.func_181670_b(this.field_179011_q.func_181719_f());
      this.field_181678_g = 0;
      this.field_181677_f = this.field_179011_q.func_177348_c(this.field_181678_g);
      if(Config.isShaders()) {
         SVertexBuilder.endAddVertex(this);
      }

   }

   public WorldRenderer func_181662_b(double p_181662_1_, double p_181662_3_, double p_181662_5_) {
      if(Config.isShaders()) {
         SVertexBuilder.beginAddVertex(this);
      }

      int i = this.field_178997_d * this.field_179011_q.func_177338_f() + this.field_179011_q.func_181720_d(this.field_181678_g);
      switch(WorldRenderer.WorldRenderer$2.field_181661_a[this.field_181677_f.func_177367_b().ordinal()]) {
      case 1:
         this.field_179001_a.putFloat(i, (float)(p_181662_1_ + this.field_179004_l));
         this.field_179001_a.putFloat(i + 4, (float)(p_181662_3_ + this.field_179005_m));
         this.field_179001_a.putFloat(i + 8, (float)(p_181662_5_ + this.field_179002_n));
         break;
      case 2:
      case 3:
         this.field_179001_a.putInt(i, Float.floatToRawIntBits((float)(p_181662_1_ + this.field_179004_l)));
         this.field_179001_a.putInt(i + 4, Float.floatToRawIntBits((float)(p_181662_3_ + this.field_179005_m)));
         this.field_179001_a.putInt(i + 8, Float.floatToRawIntBits((float)(p_181662_5_ + this.field_179002_n)));
         break;
      case 4:
      case 5:
         this.field_179001_a.putShort(i, (short)((int)(p_181662_1_ + this.field_179004_l)));
         this.field_179001_a.putShort(i + 2, (short)((int)(p_181662_3_ + this.field_179005_m)));
         this.field_179001_a.putShort(i + 4, (short)((int)(p_181662_5_ + this.field_179002_n)));
         break;
      case 6:
      case 7:
         this.field_179001_a.put(i, (byte)((int)(p_181662_1_ + this.field_179004_l)));
         this.field_179001_a.put(i + 1, (byte)((int)(p_181662_3_ + this.field_179005_m)));
         this.field_179001_a.put(i + 2, (byte)((int)(p_181662_5_ + this.field_179002_n)));
      }

      this.func_181667_k();
      return this;
   }

   public void func_178975_e(float p_178975_1_, float p_178975_2_, float p_178975_3_) {
      int i = (byte)((int)(p_178975_1_ * 127.0F)) & 255;
      int j = (byte)((int)(p_178975_2_ * 127.0F)) & 255;
      int k = (byte)((int)(p_178975_3_ * 127.0F)) & 255;
      int l = i | j << 8 | k << 16;
      int i1 = this.field_179011_q.func_177338_f() >> 2;
      int j1 = (this.field_178997_d - 4) * i1 + this.field_179011_q.func_177342_c() / 4;
      this.field_178999_b.put(j1, l);
      this.field_178999_b.put(j1 + i1, l);
      this.field_178999_b.put(j1 + i1 * 2, l);
      this.field_178999_b.put(j1 + i1 * 3, l);
   }

   private void func_181667_k() {
      ++this.field_181678_g;
      this.field_181678_g %= this.field_179011_q.func_177345_h();
      this.field_181677_f = this.field_179011_q.func_177348_c(this.field_181678_g);
      if(this.field_181677_f.func_177375_c() == VertexFormatElement.EnumUsage.PADDING) {
         this.func_181667_k();
      }

   }

   public WorldRenderer func_181663_c(float p_181663_1_, float p_181663_2_, float p_181663_3_) {
      int i = this.field_178997_d * this.field_179011_q.func_177338_f() + this.field_179011_q.func_181720_d(this.field_181678_g);
      switch(WorldRenderer.WorldRenderer$2.field_181661_a[this.field_181677_f.func_177367_b().ordinal()]) {
      case 1:
         this.field_179001_a.putFloat(i, p_181663_1_);
         this.field_179001_a.putFloat(i + 4, p_181663_2_);
         this.field_179001_a.putFloat(i + 8, p_181663_3_);
         break;
      case 2:
      case 3:
         this.field_179001_a.putInt(i, (int)p_181663_1_);
         this.field_179001_a.putInt(i + 4, (int)p_181663_2_);
         this.field_179001_a.putInt(i + 8, (int)p_181663_3_);
         break;
      case 4:
      case 5:
         this.field_179001_a.putShort(i, (short)((int)(p_181663_1_ * 32767.0F) & '\uffff'));
         this.field_179001_a.putShort(i + 2, (short)((int)(p_181663_2_ * 32767.0F) & '\uffff'));
         this.field_179001_a.putShort(i + 4, (short)((int)(p_181663_3_ * 32767.0F) & '\uffff'));
         break;
      case 6:
      case 7:
         this.field_179001_a.put(i, (byte)((int)(p_181663_1_ * 127.0F) & 255));
         this.field_179001_a.put(i + 1, (byte)((int)(p_181663_2_ * 127.0F) & 255));
         this.field_179001_a.put(i + 2, (byte)((int)(p_181663_3_ * 127.0F) & 255));
      }

      this.func_181667_k();
      return this;
   }

   public void func_178969_c(double p_178969_1_, double p_178969_3_, double p_178969_5_) {
      this.field_179004_l = p_178969_1_;
      this.field_179005_m = p_178969_3_;
      this.field_179002_n = p_178969_5_;
   }

   public void func_178977_d() {
      if(!this.field_179010_r) {
         throw new IllegalStateException("Not building!");
      } else {
         this.field_179010_r = false;
         this.field_179001_a.position(0);
         this.field_179001_a.limit(this.func_181664_j() * 4);
      }
   }

   public ByteBuffer func_178966_f() {
      return this.field_179001_a;
   }

   public VertexFormat func_178973_g() {
      return this.field_179011_q;
   }

   public int func_178989_h() {
      return this.field_178997_d;
   }

   public int func_178979_i() {
      return this.field_179006_k;
   }

   public void func_178968_d(int p_178968_1_) {
      for(int i = 0; i < 4; ++i) {
         this.func_178988_b(p_178968_1_, i + 1);
      }

   }

   public void func_178990_f(float p_178990_1_, float p_178990_2_, float p_178990_3_) {
      for(int i = 0; i < 4; ++i) {
         this.func_178994_b(p_178990_1_, p_178990_2_, p_178990_3_, i + 1);
      }

   }

   public void putSprite(TextureAtlasSprite p_putSprite_1_) {
      if(this.quadSprites != null) {
         int i = this.field_178997_d / 4;
         this.quadSprites[i - 1] = p_putSprite_1_;
      }
   }

   public void setSprite(TextureAtlasSprite p_setSprite_1_) {
      if(this.quadSprites != null) {
         this.quadSprite = p_setSprite_1_;
      }
   }

   public boolean isMultiTexture() {
      return this.quadSprites != null;
   }

   public void drawMultiTexture() {
      if(this.quadSprites != null) {
         int i = Config.getMinecraft().func_147117_R().getCountRegisteredSprites();
         if(this.drawnIcons.length <= i) {
            this.drawnIcons = new boolean[i + 1];
         }

         Arrays.fill(this.drawnIcons, false);
         int j = 0;
         int k = -1;
         int l = this.field_178997_d / 4;

         for(int i1 = 0; i1 < l; ++i1) {
            TextureAtlasSprite textureatlassprite = this.quadSprites[i1];
            if(textureatlassprite != null) {
               int j1 = textureatlassprite.getIndexInMap();
               if(!this.drawnIcons[j1]) {
                  if(textureatlassprite == TextureUtils.iconGrassSideOverlay) {
                     if(k < 0) {
                        k = i1;
                     }
                  } else {
                     i1 = this.drawForIcon(textureatlassprite, i1) - 1;
                     ++j;
                     if(this.blockLayer != EnumWorldBlockLayer.TRANSLUCENT) {
                        this.drawnIcons[j1] = true;
                     }
                  }
               }
            }
         }

         if(k >= 0) {
            this.drawForIcon(TextureUtils.iconGrassSideOverlay, k);
            ++j;
         }

         if(j > 0) {
            ;
         }

      }
   }

   private int drawForIcon(TextureAtlasSprite p_drawForIcon_1_, int p_drawForIcon_2_) {
      GL11.glBindTexture(3553, p_drawForIcon_1_.glSpriteTextureId);
      int i = -1;
      int j = -1;
      int k = this.field_178997_d / 4;

      for(int l = p_drawForIcon_2_; l < k; ++l) {
         TextureAtlasSprite textureatlassprite = this.quadSprites[l];
         if(textureatlassprite == p_drawForIcon_1_) {
            if(j < 0) {
               j = l;
            }
         } else if(j >= 0) {
            this.draw(j, l);
            if(this.blockLayer == EnumWorldBlockLayer.TRANSLUCENT) {
               return l;
            }

            j = -1;
            if(i < 0) {
               i = l;
            }
         }
      }

      if(j >= 0) {
         this.draw(j, k);
      }

      if(i < 0) {
         i = k;
      }

      return i;
   }

   private void draw(int p_draw_1_, int p_draw_2_) {
      int i = p_draw_2_ - p_draw_1_;
      if(i > 0) {
         int j = p_draw_1_ * 4;
         int k = i * 4;
         GL11.glDrawArrays(this.field_179006_k, j, k);
      }
   }

   public void setBlockLayer(EnumWorldBlockLayer p_setBlockLayer_1_) {
      this.blockLayer = p_setBlockLayer_1_;
      if(p_setBlockLayer_1_ == null) {
         if(this.quadSprites != null) {
            this.quadSpritesPrev = this.quadSprites;
         }

         this.quadSprites = null;
         this.quadSprite = null;
      }

   }

   private int getBufferQuadSize() {
      int i = this.field_178999_b.capacity() * 4 / (this.field_179011_q.func_181719_f() * 4);
      return i;
   }

   public void checkAndGrow() {
      this.func_181670_b(this.field_179011_q.func_181719_f());
   }

   public boolean isColorDisabled() {
      return this.field_78939_q;
   }

   static final class WorldRenderer$2 {
      static final int[] field_181661_a = new int[VertexFormatElement.EnumType.values().length];
      private static final String __OBFID = "CL_00002569";

      static {
         try {
            field_181661_a[VertexFormatElement.EnumType.FLOAT.ordinal()] = 1;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            field_181661_a[VertexFormatElement.EnumType.UINT.ordinal()] = 2;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            field_181661_a[VertexFormatElement.EnumType.INT.ordinal()] = 3;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            field_181661_a[VertexFormatElement.EnumType.USHORT.ordinal()] = 4;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            field_181661_a[VertexFormatElement.EnumType.SHORT.ordinal()] = 5;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            field_181661_a[VertexFormatElement.EnumType.UBYTE.ordinal()] = 6;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            field_181661_a[VertexFormatElement.EnumType.BYTE.ordinal()] = 7;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public class State {
      private final int[] field_179019_b;
      private final VertexFormat field_179018_e;
      private static final String __OBFID = "CL_00002568";
      private TextureAtlasSprite[] stateQuadSprites;

      public State(int[] p_i4_2_, VertexFormat p_i4_3_, TextureAtlasSprite[] p_i4_4_) {
         this.field_179019_b = p_i4_2_;
         this.field_179018_e = p_i4_3_;
         this.stateQuadSprites = p_i4_4_;
      }

      public State(int[] p_i46453_2_, VertexFormat p_i46453_3_) {
         this.field_179019_b = p_i46453_2_;
         this.field_179018_e = p_i46453_3_;
      }

      public int[] func_179013_a() {
         return this.field_179019_b;
      }

      public int func_179014_c() {
         return this.field_179019_b.length / this.field_179018_e.func_181719_f();
      }

      public VertexFormat func_179016_d() {
         return this.field_179018_e;
      }
   }
}
