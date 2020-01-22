package net.minecraft.util;

public class LongHashMap {
   private transient LongHashMap.Entry[] field_76169_a = new LongHashMap.Entry[4096];
   private transient int field_76167_b;
   private int field_180201_c;
   private int field_76168_c = 3072;
   private final float field_76165_d = 0.75F;
   private transient volatile int field_76166_e;
   private static final String __OBFID = "CL_00001492";

   public LongHashMap() {
      this.field_180201_c = this.field_76169_a.length - 1;
   }

   private static int func_76155_g(long p_76155_0_) {
      return (int)(p_76155_0_ ^ p_76155_0_ >>> 27);
   }

   private static int func_76157_a(int p_76157_0_) {
      p_76157_0_ = p_76157_0_ ^ p_76157_0_ >>> 20 ^ p_76157_0_ >>> 12;
      return p_76157_0_ ^ p_76157_0_ >>> 7 ^ p_76157_0_ >>> 4;
   }

   private static int func_76158_a(int p_76158_0_, int p_76158_1_) {
      return p_76158_0_ & p_76158_1_;
   }

   public int func_76162_a() {
      return this.field_76167_b;
   }

   public Object func_76164_a(long p_76164_1_) {
      int i = func_76155_g(p_76164_1_);

      for(LongHashMap.Entry longhashmap$entry = this.field_76169_a[func_76158_a(i, this.field_180201_c)]; longhashmap$entry != null; longhashmap$entry = longhashmap$entry.field_76149_c) {
         if(longhashmap$entry.field_76150_a == p_76164_1_) {
            return longhashmap$entry.field_76148_b;
         }
      }

      return null;
   }

   public boolean func_76161_b(long p_76161_1_) {
      return this.func_76160_c(p_76161_1_) != null;
   }

   final LongHashMap.Entry func_76160_c(long p_76160_1_) {
      int i = func_76155_g(p_76160_1_);

      for(LongHashMap.Entry longhashmap$entry = this.field_76169_a[func_76158_a(i, this.field_180201_c)]; longhashmap$entry != null; longhashmap$entry = longhashmap$entry.field_76149_c) {
         if(longhashmap$entry.field_76150_a == p_76160_1_) {
            return longhashmap$entry;
         }
      }

      return null;
   }

   public void func_76163_a(long p_76163_1_, Object p_76163_3_) {
      int i = func_76155_g(p_76163_1_);
      int j = func_76158_a(i, this.field_180201_c);

      for(LongHashMap.Entry longhashmap$entry = this.field_76169_a[j]; longhashmap$entry != null; longhashmap$entry = longhashmap$entry.field_76149_c) {
         if(longhashmap$entry.field_76150_a == p_76163_1_) {
            longhashmap$entry.field_76148_b = p_76163_3_;
            return;
         }
      }

      ++this.field_76166_e;
      this.func_76156_a(i, p_76163_1_, p_76163_3_, j);
   }

   private void func_76153_b(int p_76153_1_) {
      LongHashMap.Entry[] alonghashmap$entry = this.field_76169_a;
      int i = alonghashmap$entry.length;
      if(i == 1073741824) {
         this.field_76168_c = Integer.MAX_VALUE;
      } else {
         LongHashMap.Entry[] alonghashmap$entry1 = new LongHashMap.Entry[p_76153_1_];
         this.func_76154_a(alonghashmap$entry1);
         this.field_76169_a = alonghashmap$entry1;
         this.field_180201_c = this.field_76169_a.length - 1;
         float f = (float)p_76153_1_;
         this.getClass();
         this.field_76168_c = (int)(f * 0.75F);
      }

   }

   private void func_76154_a(LongHashMap.Entry[] p_76154_1_) {
      LongHashMap.Entry[] alonghashmap$entry = this.field_76169_a;
      int i = p_76154_1_.length;

      for(int j = 0; j < alonghashmap$entry.length; ++j) {
         LongHashMap.Entry longhashmap$entry = alonghashmap$entry[j];
         if(longhashmap$entry != null) {
            alonghashmap$entry[j] = null;

            while(true) {
               LongHashMap.Entry longhashmap$entry1 = longhashmap$entry.field_76149_c;
               int k = func_76158_a(longhashmap$entry.field_76147_d, i - 1);
               longhashmap$entry.field_76149_c = p_76154_1_[k];
               p_76154_1_[k] = longhashmap$entry;
               longhashmap$entry = longhashmap$entry1;
               if(longhashmap$entry1 == null) {
                  break;
               }
            }
         }
      }

   }

   public Object func_76159_d(long p_76159_1_) {
      LongHashMap.Entry longhashmap$entry = this.func_76152_e(p_76159_1_);
      return longhashmap$entry == null?null:longhashmap$entry.field_76148_b;
   }

   final LongHashMap.Entry func_76152_e(long p_76152_1_) {
      int i = func_76155_g(p_76152_1_);
      int j = func_76158_a(i, this.field_180201_c);
      LongHashMap.Entry longhashmap$entry = this.field_76169_a[j];

      LongHashMap.Entry longhashmap$entry1;
      LongHashMap.Entry longhashmap$entry2;
      for(longhashmap$entry1 = longhashmap$entry; longhashmap$entry1 != null; longhashmap$entry1 = longhashmap$entry2) {
         longhashmap$entry2 = longhashmap$entry1.field_76149_c;
         if(longhashmap$entry1.field_76150_a == p_76152_1_) {
            ++this.field_76166_e;
            --this.field_76167_b;
            if(longhashmap$entry == longhashmap$entry1) {
               this.field_76169_a[j] = longhashmap$entry2;
            } else {
               longhashmap$entry.field_76149_c = longhashmap$entry2;
            }

            return longhashmap$entry1;
         }

         longhashmap$entry = longhashmap$entry1;
      }

      return longhashmap$entry1;
   }

   private void func_76156_a(int p_76156_1_, long p_76156_2_, Object p_76156_4_, int p_76156_5_) {
      LongHashMap.Entry longhashmap$entry = this.field_76169_a[p_76156_5_];
      this.field_76169_a[p_76156_5_] = new LongHashMap.Entry(p_76156_1_, p_76156_2_, p_76156_4_, longhashmap$entry);
      if(this.field_76167_b++ >= this.field_76168_c) {
         this.func_76153_b(2 * this.field_76169_a.length);
      }

   }

   public double getKeyDistribution() {
      int i = 0;

      for(int j = 0; j < this.field_76169_a.length; ++j) {
         if(this.field_76169_a[j] != null) {
            ++i;
         }
      }

      return 1.0D * (double)i / (double)this.field_76167_b;
   }

   static class Entry {
      final long field_76150_a;
      Object field_76148_b;
      LongHashMap.Entry field_76149_c;
      final int field_76147_d;
      private static final String __OBFID = "CL_00001493";

      Entry(int p_i1553_1_, long p_i1553_2_, Object p_i1553_4_, LongHashMap.Entry p_i1553_5_) {
         this.field_76148_b = p_i1553_4_;
         this.field_76149_c = p_i1553_5_;
         this.field_76150_a = p_i1553_2_;
         this.field_76147_d = p_i1553_1_;
      }

      public final long func_76146_a() {
         return this.field_76150_a;
      }

      public final Object func_76145_b() {
         return this.field_76148_b;
      }

      public final boolean equals(Object p_equals_1_) {
         if(!(p_equals_1_ instanceof LongHashMap.Entry)) {
            return false;
         } else {
            LongHashMap.Entry longhashmap$entry = (LongHashMap.Entry)p_equals_1_;
            Long olong = Long.valueOf(this.func_76146_a());
            Long olong1 = Long.valueOf(longhashmap$entry.func_76146_a());
            if(olong == olong1 || olong != null && olong.equals(olong1)) {
               Object object = this.func_76145_b();
               Object object1 = longhashmap$entry.func_76145_b();
               if(object == object1 || object != null && object.equals(object1)) {
                  return true;
               }
            }

            return false;
         }
      }

      public final int hashCode() {
         return LongHashMap.func_76155_g(this.field_76150_a);
      }

      public final String toString() {
         return this.func_76146_a() + "=" + this.func_76145_b();
      }
   }
}
