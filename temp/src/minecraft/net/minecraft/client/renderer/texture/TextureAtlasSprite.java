package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.client.renderer.texture.TextureClock;
import net.minecraft.client.renderer.texture.TextureCompass;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationFrame;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.src.Config;
import net.minecraft.src.CounterInt;
import net.minecraft.src.TextureUtils;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import shadersmod.client.Shaders;

public class TextureAtlasSprite {
   private final String field_110984_i;
   protected List field_110976_a = Lists.newArrayList();
   protected int[][] field_176605_b;
   private AnimationMetadataSection field_110982_k;
   protected boolean field_130222_e;
   protected int field_110975_c;
   protected int field_110974_d;
   protected int field_130223_c;
   protected int field_130224_d;
   private float field_110979_l;
   private float field_110980_m;
   private float field_110977_n;
   private float field_110978_o;
   protected int field_110973_g;
   protected int field_110983_h;
   private static String field_176607_p = "builtin/clock";
   private static String field_176606_q = "builtin/compass";
   private static final String __OBFID = "CL_00001062";
   private int indexInMap = -1;
   public float baseU;
   public float baseV;
   public int sheetWidth;
   public int sheetHeight;
   public int glSpriteTextureId = -1;
   public TextureAtlasSprite spriteSingle = null;
   public boolean isSpriteSingle = false;
   public int mipmapLevels = 0;
   public TextureAtlasSprite spriteNormal = null;
   public TextureAtlasSprite spriteSpecular = null;
   public boolean isShadersSprite = false;

   private TextureAtlasSprite(String p_i19_1_, boolean p_i19_2_) {
      this.field_110984_i = p_i19_1_;
      this.isSpriteSingle = p_i19_2_;
   }

   protected TextureAtlasSprite(String p_i1282_1_) {
      this.field_110984_i = p_i1282_1_;
      if(Config.isMultiTexture()) {
         this.spriteSingle = new TextureAtlasSprite(this.func_94215_i() + ".spriteSingle", true);
      }

   }

   protected static TextureAtlasSprite func_176604_a(ResourceLocation p_176604_0_) {
      String s = p_176604_0_.toString();
      return (TextureAtlasSprite)(field_176607_p.equals(s)?new TextureClock(s):(field_176606_q.equals(s)?new TextureCompass(s):new TextureAtlasSprite(s)));
   }

   public static void func_176602_a(String p_176602_0_) {
      field_176607_p = p_176602_0_;
   }

   public static void func_176603_b(String p_176603_0_) {
      field_176606_q = p_176603_0_;
   }

   public void func_110971_a(int p_110971_1_, int p_110971_2_, int p_110971_3_, int p_110971_4_, boolean p_110971_5_) {
      this.field_110975_c = p_110971_3_;
      this.field_110974_d = p_110971_4_;
      this.field_130222_e = p_110971_5_;
      float f = (float)(0.009999999776482582D / (double)p_110971_1_);
      float f1 = (float)(0.009999999776482582D / (double)p_110971_2_);
      this.field_110979_l = (float)p_110971_3_ / (float)((double)p_110971_1_) + f;
      this.field_110980_m = (float)(p_110971_3_ + this.field_130223_c) / (float)((double)p_110971_1_) - f;
      this.field_110977_n = (float)p_110971_4_ / (float)p_110971_2_ + f1;
      this.field_110978_o = (float)(p_110971_4_ + this.field_130224_d) / (float)p_110971_2_ - f1;
      this.baseU = Math.min(this.field_110979_l, this.field_110980_m);
      this.baseV = Math.min(this.field_110977_n, this.field_110978_o);
      if(this.spriteSingle != null) {
         this.spriteSingle.func_110971_a(this.field_130223_c, this.field_130224_d, 0, 0, false);
      }

      if(this.spriteNormal != null) {
         this.spriteNormal.func_110971_a(p_110971_1_, p_110971_2_, p_110971_3_, p_110971_4_, p_110971_5_);
      }

      if(this.spriteSpecular != null) {
         this.spriteSpecular.func_110971_a(p_110971_1_, p_110971_2_, p_110971_3_, p_110971_4_, p_110971_5_);
      }

   }

   public void func_94217_a(TextureAtlasSprite p_94217_1_) {
      this.field_110975_c = p_94217_1_.field_110975_c;
      this.field_110974_d = p_94217_1_.field_110974_d;
      this.field_130223_c = p_94217_1_.field_130223_c;
      this.field_130224_d = p_94217_1_.field_130224_d;
      this.field_130222_e = p_94217_1_.field_130222_e;
      this.field_110979_l = p_94217_1_.field_110979_l;
      this.field_110980_m = p_94217_1_.field_110980_m;
      this.field_110977_n = p_94217_1_.field_110977_n;
      this.field_110978_o = p_94217_1_.field_110978_o;
      this.indexInMap = p_94217_1_.indexInMap;
      this.baseU = p_94217_1_.baseU;
      this.baseV = p_94217_1_.baseV;
      this.sheetWidth = p_94217_1_.sheetWidth;
      this.sheetHeight = p_94217_1_.sheetHeight;
      this.glSpriteTextureId = p_94217_1_.glSpriteTextureId;
      this.mipmapLevels = p_94217_1_.mipmapLevels;
      if(this.spriteSingle != null) {
         this.spriteSingle.func_110971_a(this.field_130223_c, this.field_130224_d, 0, 0, false);
      }

   }

   public int func_130010_a() {
      return this.field_110975_c;
   }

   public int func_110967_i() {
      return this.field_110974_d;
   }

   public int func_94211_a() {
      return this.field_130223_c;
   }

   public int func_94216_b() {
      return this.field_130224_d;
   }

   public float func_94209_e() {
      return this.field_110979_l;
   }

   public float func_94212_f() {
      return this.field_110980_m;
   }

   public float func_94214_a(double p_94214_1_) {
      float f = this.field_110980_m - this.field_110979_l;
      return this.field_110979_l + f * (float)p_94214_1_ / 16.0F;
   }

   public float func_94206_g() {
      return this.field_110977_n;
   }

   public float func_94210_h() {
      return this.field_110978_o;
   }

   public float func_94207_b(double p_94207_1_) {
      float f = this.field_110978_o - this.field_110977_n;
      return this.field_110977_n + f * ((float)p_94207_1_ / 16.0F);
   }

   public String func_94215_i() {
      return this.field_110984_i;
   }

   public void func_94219_l() {
      if(this.field_110982_k != null) {
         ++this.field_110983_h;
         if(this.field_110983_h >= this.field_110982_k.func_110472_a(this.field_110973_g)) {
            int i = this.field_110982_k.func_110468_c(this.field_110973_g);
            int j = this.field_110982_k.func_110473_c() == 0?this.field_110976_a.size():this.field_110982_k.func_110473_c();
            this.field_110973_g = (this.field_110973_g + 1) % j;
            this.field_110983_h = 0;
            int k = this.field_110982_k.func_110468_c(this.field_110973_g);
            boolean flag = false;
            boolean flag1 = this.isSpriteSingle;
            if(i != k && k >= 0 && k < this.field_110976_a.size()) {
               TextureUtil.func_147955_a((int[][])((int[][])this.field_110976_a.get(k)), this.field_130223_c, this.field_130224_d, this.field_110975_c, this.field_110974_d, flag, flag1);
            }
         } else if(this.field_110982_k.func_177219_e()) {
            this.func_180599_n();
         }

      }
   }

   private void func_180599_n() {
      double d0 = 1.0D - (double)this.field_110983_h / (double)this.field_110982_k.func_110472_a(this.field_110973_g);
      int i = this.field_110982_k.func_110468_c(this.field_110973_g);
      int j = this.field_110982_k.func_110473_c() == 0?this.field_110976_a.size():this.field_110982_k.func_110473_c();
      int k = this.field_110982_k.func_110468_c((this.field_110973_g + 1) % j);
      if(i != k && k >= 0 && k < this.field_110976_a.size()) {
         int[][] aint = (int[][])((int[][])this.field_110976_a.get(i));
         int[][] aint1 = (int[][])((int[][])this.field_110976_a.get(k));
         if(this.field_176605_b == null || this.field_176605_b.length != aint.length) {
            this.field_176605_b = new int[aint.length][];
         }

         for(int l = 0; l < aint.length; ++l) {
            if(this.field_176605_b[l] == null) {
               this.field_176605_b[l] = new int[aint[l].length];
            }

            if(l < aint1.length && aint1[l].length == aint[l].length) {
               for(int i1 = 0; i1 < aint[l].length; ++i1) {
                  int j1 = aint[l][i1];
                  int k1 = aint1[l][i1];
                  int l1 = (int)((double)((j1 & 16711680) >> 16) * d0 + (double)((k1 & 16711680) >> 16) * (1.0D - d0));
                  int i2 = (int)((double)((j1 & '\uff00') >> 8) * d0 + (double)((k1 & '\uff00') >> 8) * (1.0D - d0));
                  int j2 = (int)((double)(j1 & 255) * d0 + (double)(k1 & 255) * (1.0D - d0));
                  this.field_176605_b[l][i1] = j1 & -16777216 | l1 << 16 | i2 << 8 | j2;
               }
            }
         }

         TextureUtil.func_147955_a(this.field_176605_b, this.field_130223_c, this.field_130224_d, this.field_110975_c, this.field_110974_d, false, false);
      }

   }

   public int[][] func_147965_a(int p_147965_1_) {
      return (int[][])((int[][])this.field_110976_a.get(p_147965_1_));
   }

   public int func_110970_k() {
      return this.field_110976_a.size();
   }

   public void func_110966_b(int p_110966_1_) {
      this.field_130223_c = p_110966_1_;
      if(this.spriteSingle != null) {
         this.spriteSingle.func_110966_b(this.field_130223_c);
      }

   }

   public void func_110969_c(int p_110969_1_) {
      this.field_130224_d = p_110969_1_;
      if(this.spriteSingle != null) {
         this.spriteSingle.func_110969_c(this.field_130224_d);
      }

   }

   public void func_180598_a(BufferedImage[] p_180598_1_, AnimationMetadataSection p_180598_2_) throws IOException {
      this.func_130102_n();
      int i = p_180598_1_[0].getWidth();
      int j = p_180598_1_[0].getHeight();
      this.field_130223_c = i;
      this.field_130224_d = j;
      int[][] aint = new int[p_180598_1_.length][];

      for(int k = 0; k < p_180598_1_.length; ++k) {
         BufferedImage bufferedimage = p_180598_1_[k];
         if(bufferedimage != null) {
            if(k > 0 && (bufferedimage.getWidth() != i >> k || bufferedimage.getHeight() != j >> k)) {
               throw new RuntimeException(String.format("Unable to load miplevel: %d, image is size: %dx%d, expected %dx%d", new Object[]{Integer.valueOf(k), Integer.valueOf(bufferedimage.getWidth()), Integer.valueOf(bufferedimage.getHeight()), Integer.valueOf(i >> k), Integer.valueOf(j >> k)}));
            }

            aint[k] = new int[bufferedimage.getWidth() * bufferedimage.getHeight()];
            bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), aint[k], 0, bufferedimage.getWidth());
         }
      }

      if(p_180598_2_ == null) {
         if(j != i) {
            throw new RuntimeException("broken aspect ratio and not an animation");
         }

         this.field_110976_a.add(aint);
      } else {
         int j1 = j / i;
         int k1 = i;
         int l = i;
         this.field_130224_d = this.field_130223_c;
         if(p_180598_2_.func_110473_c() > 0) {
            Iterator iterator = p_180598_2_.func_130073_e().iterator();

            while(iterator.hasNext()) {
               int i1 = ((Integer)iterator.next()).intValue();
               if(i1 >= j1) {
                  throw new RuntimeException("invalid frameindex " + i1);
               }

               this.func_130099_d(i1);
               this.field_110976_a.set(i1, func_147962_a(aint, k1, l, i1));
            }

            this.field_110982_k = p_180598_2_;
         } else {
            ArrayList arraylist = Lists.newArrayList();

            for(int i2 = 0; i2 < j1; ++i2) {
               this.field_110976_a.add(func_147962_a(aint, k1, l, i2));
               arraylist.add(new AnimationFrame(i2, -1));
            }

            this.field_110982_k = new AnimationMetadataSection(arraylist, this.field_130223_c, this.field_130224_d, p_180598_2_.func_110469_d(), p_180598_2_.func_177219_e());
         }
      }

      if(!this.isShadersSprite) {
         if(Config.isShaders()) {
            this.loadShadersSprites();
         }

         for(int l1 = 0; l1 < this.field_110976_a.size(); ++l1) {
            int[][] aint1 = (int[][])((int[][])this.field_110976_a.get(l1));
            if(aint1 != null && !this.field_110984_i.startsWith("minecraft:blocks/leaves_")) {
               for(int j2 = 0; j2 < aint1.length; ++j2) {
                  int[] aint2 = aint1[j2];
                  this.fixTransparentColor(aint2);
               }
            }
         }

         if(this.spriteSingle != null) {
            this.spriteSingle.func_180598_a(p_180598_1_, p_180598_2_);
         }

      }
   }

   public void func_147963_d(int p_147963_1_) {
      ArrayList arraylist = Lists.newArrayList();

      for(int i = 0; i < this.field_110976_a.size(); ++i) {
         final int[][] aint = (int[][])((int[][])this.field_110976_a.get(i));
         if(aint != null) {
            try {
               arraylist.add(TextureUtil.func_147949_a(p_147963_1_, this.field_130223_c, aint));
            } catch (Throwable throwable) {
               CrashReport crashreport = CrashReport.func_85055_a(throwable, "Generating mipmaps for frame");
               CrashReportCategory crashreportcategory = crashreport.func_85058_a("Frame being iterated");
               crashreportcategory.func_71507_a("Frame index", Integer.valueOf(i));
               crashreportcategory.func_71500_a("Frame sizes", new Callable() {
                  private static final String __OBFID = "CL_00001063";

                  public String call() throws Exception {
                     StringBuilder stringbuilder = new StringBuilder();

                     for(int[] aint1 : aint) {
                        if(stringbuilder.length() > 0) {
                           stringbuilder.append(", ");
                        }

                        stringbuilder.append(aint1 == null?"null":Integer.valueOf(aint1.length));
                     }

                     return stringbuilder.toString();
                  }
               });
               throw new ReportedException(crashreport);
            }
         }
      }

      this.func_110968_a(arraylist);
      if(this.spriteSingle != null) {
         this.spriteSingle.func_147963_d(p_147963_1_);
      }

   }

   private void func_130099_d(int p_130099_1_) {
      if(this.field_110976_a.size() <= p_130099_1_) {
         for(int i = this.field_110976_a.size(); i <= p_130099_1_; ++i) {
            this.field_110976_a.add((Object)null);
         }
      }

      if(this.spriteSingle != null) {
         this.spriteSingle.func_130099_d(p_130099_1_);
      }

   }

   private static int[][] func_147962_a(int[][] p_147962_0_, int p_147962_1_, int p_147962_2_, int p_147962_3_) {
      int[][] aint = new int[p_147962_0_.length][];

      for(int i = 0; i < p_147962_0_.length; ++i) {
         int[] aint1 = p_147962_0_[i];
         if(aint1 != null) {
            aint[i] = new int[(p_147962_1_ >> i) * (p_147962_2_ >> i)];
            System.arraycopy(aint1, p_147962_3_ * aint[i].length, aint[i], 0, aint[i].length);
         }
      }

      return aint;
   }

   public void func_130103_l() {
      this.field_110976_a.clear();
      if(this.spriteSingle != null) {
         this.spriteSingle.func_130103_l();
      }

   }

   public boolean func_130098_m() {
      return this.field_110982_k != null;
   }

   public void func_110968_a(List p_110968_1_) {
      this.field_110976_a = p_110968_1_;
      if(this.spriteSingle != null) {
         this.spriteSingle.func_110968_a(p_110968_1_);
      }

   }

   private void func_130102_n() {
      this.field_110982_k = null;
      this.func_110968_a(Lists.newArrayList());
      this.field_110973_g = 0;
      this.field_110983_h = 0;
      if(this.spriteSingle != null) {
         this.spriteSingle.func_130102_n();
      }

   }

   public String toString() {
      return "TextureAtlasSprite{name=\'" + this.field_110984_i + '\'' + ", frameCount=" + this.field_110976_a.size() + ", rotated=" + this.field_130222_e + ", x=" + this.field_110975_c + ", y=" + this.field_110974_d + ", height=" + this.field_130224_d + ", width=" + this.field_130223_c + ", u0=" + this.field_110979_l + ", u1=" + this.field_110980_m + ", v0=" + this.field_110977_n + ", v1=" + this.field_110978_o + '}';
   }

   public boolean hasCustomLoader(IResourceManager p_hasCustomLoader_1_, ResourceLocation p_hasCustomLoader_2_) {
      return false;
   }

   public boolean load(IResourceManager p_load_1_, ResourceLocation p_load_2_) {
      return true;
   }

   public int getIndexInMap() {
      return this.indexInMap;
   }

   public void setIndexInMap(int p_setIndexInMap_1_) {
      this.indexInMap = p_setIndexInMap_1_;
   }

   public void updateIndexInMap(CounterInt p_updateIndexInMap_1_) {
      if(this.indexInMap < 0) {
         this.indexInMap = p_updateIndexInMap_1_.nextValue();
      }

   }

   private void fixTransparentColor(int[] p_fixTransparentColor_1_) {
      if(p_fixTransparentColor_1_ != null) {
         long i = 0L;
         long j = 0L;
         long k = 0L;
         long l = 0L;

         for(int i1 = 0; i1 < p_fixTransparentColor_1_.length; ++i1) {
            int j1 = p_fixTransparentColor_1_[i1];
            int k1 = j1 >> 24 & 255;
            if(k1 >= 16) {
               int l1 = j1 >> 16 & 255;
               int i2 = j1 >> 8 & 255;
               int j2 = j1 & 255;
               i += (long)l1;
               j += (long)i2;
               k += (long)j2;
               ++l;
            }
         }

         if(l > 0L) {
            int l2 = (int)(i / l);
            int i3 = (int)(j / l);
            int j3 = (int)(k / l);
            int k3 = l2 << 16 | i3 << 8 | j3;

            for(int l3 = 0; l3 < p_fixTransparentColor_1_.length; ++l3) {
               int i4 = p_fixTransparentColor_1_[l3];
               int k2 = i4 >> 24 & 255;
               if(k2 <= 16) {
                  p_fixTransparentColor_1_[l3] = k3;
               }
            }

         }
      }
   }

   public double getSpriteU16(float p_getSpriteU16_1_) {
      float f = this.field_110980_m - this.field_110979_l;
      return (double)((p_getSpriteU16_1_ - this.field_110979_l) / f * 16.0F);
   }

   public double getSpriteV16(float p_getSpriteV16_1_) {
      float f = this.field_110978_o - this.field_110977_n;
      return (double)((p_getSpriteV16_1_ - this.field_110977_n) / f * 16.0F);
   }

   public void bindSpriteTexture() {
      if(this.glSpriteTextureId < 0) {
         this.glSpriteTextureId = TextureUtil.func_110996_a();
         TextureUtil.func_180600_a(this.glSpriteTextureId, this.mipmapLevels, this.field_130223_c, this.field_130224_d);
         TextureUtils.applyAnisotropicLevel();
      }

      TextureUtils.bindTexture(this.glSpriteTextureId);
   }

   public void deleteSpriteTexture() {
      if(this.glSpriteTextureId >= 0) {
         TextureUtil.func_147942_a(this.glSpriteTextureId);
         this.glSpriteTextureId = -1;
      }
   }

   public float toSingleU(float p_toSingleU_1_) {
      p_toSingleU_1_ = p_toSingleU_1_ - this.baseU;
      float f = (float)this.sheetWidth / (float)this.field_130223_c;
      p_toSingleU_1_ = p_toSingleU_1_ * f;
      return p_toSingleU_1_;
   }

   public float toSingleV(float p_toSingleV_1_) {
      p_toSingleV_1_ = p_toSingleV_1_ - this.baseV;
      float f = (float)this.sheetHeight / (float)this.field_130224_d;
      p_toSingleV_1_ = p_toSingleV_1_ * f;
      return p_toSingleV_1_;
   }

   public List<int[][]> getFramesTextureData() {
      List<int[][]> list = new ArrayList();
      list.addAll(this.field_110976_a);
      return list;
   }

   public AnimationMetadataSection getAnimationMetadata() {
      return this.field_110982_k;
   }

   public void setAnimationMetadata(AnimationMetadataSection p_setAnimationMetadata_1_) {
      this.field_110982_k = p_setAnimationMetadata_1_;
   }

   private void loadShadersSprites() {
      this.mipmapLevels = Config.getTextureMap().getMipmapLevels();
      if(Shaders.configNormalMap) {
         String s = this.field_110984_i + "_n";
         ResourceLocation resourcelocation = new ResourceLocation(s);
         resourcelocation = Config.getTextureMap().func_147634_a(resourcelocation, 0);
         if(Config.hasResource(resourcelocation)) {
            try {
               TextureAtlasSprite textureatlassprite = new TextureAtlasSprite(s);
               textureatlassprite.isShadersSprite = true;
               textureatlassprite.func_94217_a(this);
               textureatlassprite.loadShaderSpriteFrames(resourcelocation, this.mipmapLevels + 1);
               textureatlassprite.func_147963_d(this.mipmapLevels);
               this.spriteNormal = textureatlassprite;
            } catch (IOException ioexception1) {
               Config.warn("Error loading normal texture: " + s);
               Config.warn(ioexception1.getClass().getName() + ": " + ioexception1.getMessage());
            }
         }
      }

      if(Shaders.configSpecularMap) {
         String s1 = this.field_110984_i + "_s";
         ResourceLocation resourcelocation1 = new ResourceLocation(s1);
         resourcelocation1 = Config.getTextureMap().func_147634_a(resourcelocation1, 0);
         if(Config.hasResource(resourcelocation1)) {
            try {
               TextureAtlasSprite textureatlassprite1 = new TextureAtlasSprite(s1);
               textureatlassprite1.isShadersSprite = true;
               textureatlassprite1.func_94217_a(this);
               textureatlassprite1.loadShaderSpriteFrames(resourcelocation1, this.mipmapLevels + 1);
               textureatlassprite1.func_147963_d(this.mipmapLevels);
               this.spriteSpecular = textureatlassprite1;
            } catch (IOException ioexception) {
               Config.warn("Error loading specular texture: " + s1);
               Config.warn(ioexception.getClass().getName() + ": " + ioexception.getMessage());
            }
         }
      }

   }

   public void loadShaderSpriteFrames(ResourceLocation p_loadShaderSpriteFrames_1_, int p_loadShaderSpriteFrames_2_) throws IOException {
      IResource iresource = Config.getResource(p_loadShaderSpriteFrames_1_);
      BufferedImage bufferedimage = TextureUtil.func_177053_a(iresource.func_110527_b());
      if(this.field_130223_c != bufferedimage.getWidth()) {
         bufferedimage = TextureUtils.scaleImage(bufferedimage, this.field_130223_c);
      }

      AnimationMetadataSection animationmetadatasection = (AnimationMetadataSection)iresource.func_110526_a("animation");
      int[][] aint = new int[p_loadShaderSpriteFrames_2_][];
      aint[0] = new int[bufferedimage.getWidth() * bufferedimage.getHeight()];
      bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), aint[0], 0, bufferedimage.getWidth());
      if(animationmetadatasection == null) {
         this.field_110976_a.add(aint);
      } else {
         int i = bufferedimage.getHeight() / this.field_130223_c;
         if(animationmetadatasection.func_110473_c() > 0) {
            Iterator iterator = animationmetadatasection.func_130073_e().iterator();

            while(iterator.hasNext()) {
               int j = ((Integer)iterator.next()).intValue();
               if(j >= i) {
                  throw new RuntimeException("invalid frameindex " + j);
               }

               this.func_130099_d(j);
               this.field_110976_a.set(j, func_147962_a(aint, this.field_130223_c, this.field_130223_c, j));
            }

            this.field_110982_k = animationmetadatasection;
         } else {
            List<AnimationFrame> list = Lists.<AnimationFrame>newArrayList();

            for(int k = 0; k < i; ++k) {
               this.field_110976_a.add(func_147962_a(aint, this.field_130223_c, this.field_130223_c, k));
               list.add(new AnimationFrame(k, -1));
            }

            this.field_110982_k = new AnimationMetadataSection(list, this.field_130223_c, this.field_130224_d, animationmetadatasection.func_110469_d(), animationmetadatasection.func_177219_e());
         }
      }

   }
}
