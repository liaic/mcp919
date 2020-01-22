package net.minecraft.util;

import java.util.Random;
import java.util.UUID;
import net.minecraft.util.Vec3i;

public class MathHelper {

   public static final float SQRT_2 = sqrt_float(2.0F);
   public static final int SIN_BITS = 12;
   public static final int SIN_MASK = 4095;
   public static final int SIN_COUNT = 4096;
   public static final float PI = 3.1415927F;
   public static final float PI2 = 6.2831855F;
   public static final float PId2 = 1.5707964F;
   public static final float radFull = 6.2831855F;
   public static final float degFull = 360.0F;
   public static final float radToIndex = 651.8986F;
   public static final float degToIndex = 11.377778F;
   public static final float deg2Rad = 0.017453292F;
   public static final float[] SIN_TABLE_FAST = new float[4096];
   public static boolean fastMath = false;
   public static final float[] SIN_TABLE = new float[65536];
   public static final int[] multiplyDeBruijnBitPosition;
   public static final double field_181163_d;
   public static final double[] field_181164_e;
   public static final double[] field_181165_f;
   public static final String __OBFID = "CL_00001496";


   public static float sin(float p_76126_0_) {
      return fastMath?SIN_TABLE_FAST[(int)(p_76126_0_ * 651.8986F) & 4095]:SIN_TABLE[(int)(p_76126_0_ * 10430.378F) & '\uffff'];
   }

   public static float cos(float p_76134_0_) {
      return fastMath?SIN_TABLE_FAST[(int)((p_76134_0_ + 1.5707964F) * 651.8986F) & 4095]:SIN_TABLE[(int)(p_76134_0_ * 10430.378F + 16384.0F) & '\uffff'];
   }

   public static float sqrt_float(float p_76129_0_) {
      return (float)Math.sqrt((double)p_76129_0_);
   }

   public static float sqrt_double(double p_76133_0_) {
      return (float)Math.sqrt(p_76133_0_);
   }

   public static int floor_float(float p_76141_0_) {
      int var1 = (int)p_76141_0_;
      return p_76141_0_ < (float)var1?var1 - 1:var1;
   }

   public static int truncateDoubleToInt(double p_76140_0_) {
      return (int)(p_76140_0_ + 1024.0D) - 1024;
   }

   public static int floor_double(double p_76128_0_) {
      int var2 = (int)p_76128_0_;
      return p_76128_0_ < (double)var2?var2 - 1:var2;
   }

   public static long floor_double_long(double p_76124_0_) {
      long var2 = (long)p_76124_0_;
      return p_76124_0_ < (double)var2?var2 - 1L:var2;
   }

   public static int func_154353_e(double p_154353_0_) {
      return (int)(p_154353_0_ >= 0.0D?p_154353_0_:-p_154353_0_ + 1.0D);
   }

   public static float abs(float p_76135_0_) {
      return p_76135_0_ >= 0.0F?p_76135_0_:-p_76135_0_;
   }

   public static int abs_int(int p_76130_0_) {
      return p_76130_0_ >= 0?p_76130_0_:-p_76130_0_;
   }

   public static int ceiling_float_int(float p_76123_0_) {
      int var1 = (int)p_76123_0_;
      return p_76123_0_ > (float)var1?var1 + 1:var1;
   }

   public static int ceiling_double_int(double p_76143_0_) {
      int var2 = (int)p_76143_0_;
      return p_76143_0_ > (double)var2?var2 + 1:var2;
   }

   public static int clamp_int(int p_76125_0_, int p_76125_1_, int p_76125_2_) {
      return p_76125_0_ < p_76125_1_?p_76125_1_:(p_76125_0_ > p_76125_2_?p_76125_2_:p_76125_0_);
   }

   public static float clamp_float(float p_76131_0_, float p_76131_1_, float p_76131_2_) {
      return p_76131_0_ < p_76131_1_?p_76131_1_:(p_76131_0_ > p_76131_2_?p_76131_2_:p_76131_0_);
   }

   public static double clamp_double(double p_151237_0_, double p_151237_2_, double p_151237_4_) {
      return p_151237_0_ < p_151237_2_?p_151237_2_:(p_151237_0_ > p_151237_4_?p_151237_4_:p_151237_0_);
   }

   public static double denormalizeClamp(double p_151238_0_, double p_151238_2_, double p_151238_4_) {
      return p_151238_4_ < 0.0D?p_151238_0_:(p_151238_4_ > 1.0D?p_151238_2_:p_151238_0_ + (p_151238_2_ - p_151238_0_) * p_151238_4_);
   }

   public static double abs_max(double p_76132_0_, double p_76132_2_) {
      if(p_76132_0_ < 0.0D) {
         p_76132_0_ = -p_76132_0_;
      }

      if(p_76132_2_ < 0.0D) {
         p_76132_2_ = -p_76132_2_;
      }

      return p_76132_0_ > p_76132_2_?p_76132_0_:p_76132_2_;
   }

   public static int bucketInt(int p_76137_0_, int p_76137_1_) {
      return p_76137_0_ < 0?-((-p_76137_0_ - 1) / p_76137_1_) - 1:p_76137_0_ / p_76137_1_;
   }

   public static int getRandomIntegerInRange(Random p_76136_0_, int p_76136_1_, int p_76136_2_) {
      return p_76136_1_ >= p_76136_2_?p_76136_1_:p_76136_0_.nextInt(p_76136_2_ - p_76136_1_ + 1) + p_76136_1_;
   }

   public static float randomFloatClamp(Random p_151240_0_, float p_151240_1_, float p_151240_2_) {
      return p_151240_1_ >= p_151240_2_?p_151240_1_:p_151240_0_.nextFloat() * (p_151240_2_ - p_151240_1_) + p_151240_1_;
   }

   public static double getRandomDoubleInRange(Random p_82716_0_, double p_82716_1_, double p_82716_3_) {
      return p_82716_1_ >= p_82716_3_?p_82716_1_:p_82716_0_.nextDouble() * (p_82716_3_ - p_82716_1_) + p_82716_1_;
   }

   public static double average(long[] p_76127_0_) {
      long var1 = 0L;
      long[] var3 = p_76127_0_;
      int var4 = p_76127_0_.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         long var6 = var3[var5];
         var1 += var6;
      }

      return (double)var1 / (double)p_76127_0_.length;
   }

   public static boolean epsilonEquals(float p_180185_0_, float p_180185_1_) {
      return abs(p_180185_1_ - p_180185_0_) < 1.0E-5F;
   }

   public static int normalizeAngle(int p_180184_0_, int p_180184_1_) {
      return (p_180184_0_ % p_180184_1_ + p_180184_1_) % p_180184_1_;
   }

   public static float wrapAngleTo180_float(float p_76142_0_) {
      p_76142_0_ %= 360.0F;
      if(p_76142_0_ >= 180.0F) {
         p_76142_0_ -= 360.0F;
      }

      if(p_76142_0_ < -180.0F) {
         p_76142_0_ += 360.0F;
      }

      return p_76142_0_;
   }

   public static double wrapAngleTo180_double(double p_76138_0_) {
      p_76138_0_ %= 360.0D;
      if(p_76138_0_ >= 180.0D) {
         p_76138_0_ -= 360.0D;
      }

      if(p_76138_0_ < -180.0D) {
         p_76138_0_ += 360.0D;
      }

      return p_76138_0_;
   }

   public static int parseIntWithDefault(String p_82715_0_, int p_82715_1_) {
      try {
         return Integer.parseInt(p_82715_0_);
      } catch (Throwable var3) {
         return p_82715_1_;
      }
   }

   public static int parseIntWithDefaultAndMax(String p_82714_0_, int p_82714_1_, int p_82714_2_) {
      return Math.max(p_82714_2_, parseIntWithDefault(p_82714_0_, p_82714_1_));
   }

   public static double parseDoubleWithDefault(String p_82712_0_, double p_82712_1_) {
      try {
         return Double.parseDouble(p_82712_0_);
      } catch (Throwable var4) {
         return p_82712_1_;
      }
   }

   public static double parseDoubleWithDefaultAndMax(String p_82713_0_, double p_82713_1_, double p_82713_3_) {
      return Math.max(p_82713_3_, parseDoubleWithDefault(p_82713_0_, p_82713_1_));
   }

   public static int roundUpToPowerOfTwo(int p_151236_0_) {
      int var1 = p_151236_0_ - 1;
      var1 |= var1 >> 1;
      var1 |= var1 >> 2;
      var1 |= var1 >> 4;
      var1 |= var1 >> 8;
      var1 |= var1 >> 16;
      return var1 + 1;
   }

   public static boolean isPowerOfTwo(int p_151235_0_) {
      return p_151235_0_ != 0 && (p_151235_0_ & p_151235_0_ - 1) == 0;
   }

   public static int calculateLogBaseTwoDeBruijn(int p_151241_0_) {
      p_151241_0_ = isPowerOfTwo(p_151241_0_)?p_151241_0_:roundUpToPowerOfTwo(p_151241_0_);
      return multiplyDeBruijnBitPosition[(int)((long)p_151241_0_ * 125613361L >> 27) & 31];
   }

   public static int calculateLogBaseTwo(int p_151239_0_) {
      return calculateLogBaseTwoDeBruijn(p_151239_0_) - (isPowerOfTwo(p_151239_0_)?0:1);
   }

   public static int roundUp(int p_154354_0_, int p_154354_1_) {
      if(p_154354_1_ == 0) {
         return 0;
      } else if(p_154354_0_ == 0) {
         return p_154354_1_;
      } else {
         if(p_154354_0_ < 0) {
            p_154354_1_ *= -1;
         }

         int var2 = p_154354_0_ % p_154354_1_;
         return var2 == 0?p_154354_0_:p_154354_0_ + p_154354_1_ - var2;
      }
   }

   public static int func_180183_b(float p_180183_0_, float p_180183_1_, float p_180183_2_) {
      return func_180181_b(floor_float(p_180183_0_ * 255.0F), floor_float(p_180183_1_ * 255.0F), floor_float(p_180183_2_ * 255.0F));
   }

   public static int func_180181_b(int p_180181_0_, int p_180181_1_, int p_180181_2_) {
      int var3 = (p_180181_0_ << 8) + p_180181_1_;
      var3 = (var3 << 8) + p_180181_2_;
      return var3;
   }

   public static int func_180188_d(int p_180188_0_, int p_180188_1_) {
      int var2 = (p_180188_0_ & 16711680) >> 16;
      int var3 = (p_180188_1_ & 16711680) >> 16;
      int var4 = (p_180188_0_ & '\uff00') >> 8;
      int var5 = (p_180188_1_ & '\uff00') >> 8;
      int var6 = (p_180188_0_ & 255) >> 0;
      int var7 = (p_180188_1_ & 255) >> 0;
      int var8 = (int)((float)var2 * (float)var3 / 255.0F);
      int var9 = (int)((float)var4 * (float)var5 / 255.0F);
      int var10 = (int)((float)var6 * (float)var7 / 255.0F);
      return p_180188_0_ & -16777216 | var8 << 16 | var9 << 8 | var10;
   }

   public static double func_181162_h(double p_500016_0_) {
      return p_500016_0_ - Math.floor(p_500016_0_);
   }

   public static long getPositionRandom(Vec3i pos) {
      return getCoordinateRandom(pos.getX(), pos.getY(), pos.getZ());
   }

   public static long getCoordinateRandom(int x, int y, int z) {
      long var3 = (long)(x * 3129871) ^ (long)z * 116129781L ^ (long)y;
      var3 = var3 * var3 * 42317861L + var3 * 11L;
      return var3;
   }

   public static UUID getRandomUuid(Random p_180182_0_) {
      long var1 = p_180182_0_.nextLong() & -61441L | 16384L;
      long var3 = p_180182_0_.nextLong() & 4611686018427387903L | Long.MIN_VALUE;
      return new UUID(var1, var3);
   }

   public static double func_181160_c(double p_500017_0_, double p_500017_2_, double p_500017_4_) {
      return (p_500017_0_ - p_500017_2_) / (p_500017_4_ - p_500017_2_);
   }

   public static double atan2(double p_500018_0_, double p_500018_2_) {
      double var4 = p_500018_2_ * p_500018_2_ + p_500018_0_ * p_500018_0_;
      if(Double.isNaN(var4)) {
         return Double.NaN;
      } else {
         boolean var6 = p_500018_0_ < 0.0D;
         if(var6) {
            p_500018_0_ = -p_500018_0_;
         }

         boolean var7 = p_500018_2_ < 0.0D;
         if(var7) {
            p_500018_2_ = -p_500018_2_;
         }

         boolean var8 = p_500018_0_ > p_500018_2_;
         double var9;
         if(var8) {
            var9 = p_500018_2_;
            p_500018_2_ = p_500018_0_;
            p_500018_0_ = var9;
         }

         var9 = func_181161_i(var4);
         p_500018_2_ *= var9;
         p_500018_0_ *= var9;
         double var11 = field_181163_d + p_500018_0_;
         int var13 = (int)Double.doubleToRawLongBits(var11);
         double var14 = field_181164_e[var13];
         double var16 = field_181165_f[var13];
         double var18 = var11 - field_181163_d;
         double var20 = p_500018_0_ * var16 - p_500018_2_ * var18;
         double var22 = (6.0D + var20 * var20) * var20 * 0.16666666666666666D;
         double var24 = var14 + var22;
         if(var8) {
            var24 = 1.5707963267948966D - var24;
         }

         if(var7) {
            var24 = 3.141592653589793D - var24;
         }

         if(var6) {
            var24 = -var24;
         }

         return var24;
      }
   }

   public static double func_181161_i(double p_500019_0_) {
      double var2 = 0.5D * p_500019_0_;
      long var4 = Double.doubleToRawLongBits(p_500019_0_);
      var4 = 6910469410427058090L - (var4 >> 1);
      p_500019_0_ = Double.longBitsToDouble(var4);
      p_500019_0_ *= 1.5D - var2 * p_500019_0_ * p_500019_0_;
      return p_500019_0_;
   }

   public static int hsvToRGB(float p_500020_0_, float p_500020_1_, float p_500020_2_) {
      int var3 = (int)(p_500020_0_ * 6.0F) % 6;
      float var4 = p_500020_0_ * 6.0F - (float)var3;
      float var5 = p_500020_2_ * (1.0F - p_500020_1_);
      float var6 = p_500020_2_ * (1.0F - var4 * p_500020_1_);
      float var7 = p_500020_2_ * (1.0F - (1.0F - var4) * p_500020_1_);
      float var8;
      float var9;
      float var10;
      switch(var3) {
      case 0:
         var8 = p_500020_2_;
         var9 = var7;
         var10 = var5;
         break;
      case 1:
         var8 = var6;
         var9 = p_500020_2_;
         var10 = var5;
         break;
      case 2:
         var8 = var5;
         var9 = p_500020_2_;
         var10 = var7;
         break;
      case 3:
         var8 = var5;
         var9 = var6;
         var10 = p_500020_2_;
         break;
      case 4:
         var8 = var7;
         var9 = var5;
         var10 = p_500020_2_;
         break;
      case 5:
         var8 = p_500020_2_;
         var9 = var5;
         var10 = var6;
         break;
      default:
         throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + p_500020_0_ + ", " + p_500020_1_ + ", " + p_500020_2_);
      }

      int var11 = clamp_int((int)(var8 * 255.0F), 0, 255);
      int var12 = clamp_int((int)(var9 * 255.0F), 0, 255);
      int var13 = clamp_int((int)(var10 * 255.0F), 0, 255);
      return var11 << 16 | var12 << 8 | var13;
   }

   static {
      int var0;
      for(var0 = 0; var0 < 65536; ++var0) {
         SIN_TABLE[var0] = (float)Math.sin((double)var0 * 3.141592653589793D * 2.0D / 65536.0D);
      }

      int i;
      for(i = 0; i < 4096; ++i) {
         SIN_TABLE_FAST[i] = (float)Math.sin((double)(((float)i + 0.5F) / 4096.0F * 6.2831855F));
      }

      for(i = 0; i < 360; i += 90) {
         SIN_TABLE_FAST[(int)((float)i * 11.377778F) & 4095] = (float)Math.sin((double)((float)i * 0.017453292F));
      }

      multiplyDeBruijnBitPosition = new int[]{0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};
      field_181163_d = Double.longBitsToDouble(4805340802404319232L);
      field_181164_e = new double[257];
      field_181165_f = new double[257];

      for(var0 = 0; var0 < 257; ++var0) {
         double var1 = (double)var0 / 256.0D;
         double var3 = Math.asin(var1);
         field_181165_f[var0] = Math.cos(var3);
         field_181164_e[var0] = var3;
      }

   }
}
