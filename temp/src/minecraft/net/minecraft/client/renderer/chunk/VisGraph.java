package net.minecraft.client.renderer.chunk;

import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.Set;
import net.minecraft.client.renderer.chunk.SetVisibility;
import net.minecraft.src.IntegerCache;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class VisGraph {
   private static final int field_178616_a = (int)Math.pow(16.0D, 0.0D);
   private static final int field_178614_b = (int)Math.pow(16.0D, 1.0D);
   private static final int field_178615_c = (int)Math.pow(16.0D, 2.0D);
   private final BitSet field_178612_d = new BitSet(4096);
   private static final int[] field_178613_e = new int[1352];
   private int field_178611_f = 4096;
   private static final String __OBFID = "CL_00002450";

   public void func_178606_a(BlockPos p_178606_1_) {
      this.field_178612_d.set(func_178608_c(p_178606_1_), true);
      --this.field_178611_f;
   }

   private static int func_178608_c(BlockPos p_178608_0_) {
      return func_178605_a(p_178608_0_.func_177958_n() & 15, p_178608_0_.func_177956_o() & 15, p_178608_0_.func_177952_p() & 15);
   }

   private static int func_178605_a(int p_178605_0_, int p_178605_1_, int p_178605_2_) {
      return p_178605_0_ << 0 | p_178605_1_ << 8 | p_178605_2_ << 4;
   }

   public SetVisibility func_178607_a() {
      SetVisibility setvisibility = new SetVisibility();
      if(4096 - this.field_178611_f < 256) {
         setvisibility.func_178618_a(true);
      } else if(this.field_178611_f == 0) {
         setvisibility.func_178618_a(false);
      } else {
         for(int i : field_178613_e) {
            if(!this.field_178612_d.get(i)) {
               setvisibility.func_178620_a(this.func_178604_a(i));
            }
         }
      }

      return setvisibility;
   }

   public Set func_178609_b(BlockPos p_178609_1_) {
      return this.func_178604_a(func_178608_c(p_178609_1_));
   }

   private Set func_178604_a(int p_178604_1_) {
      EnumSet enumset = EnumSet.noneOf(EnumFacing.class);
      ArrayDeque arraydeque = new ArrayDeque(384);
      arraydeque.add(IntegerCache.valueOf(p_178604_1_));
      this.field_178612_d.set(p_178604_1_, true);

      while(!arraydeque.isEmpty()) {
         int i = ((Integer)arraydeque.poll()).intValue();
         this.func_178610_a(i, enumset);

         for(EnumFacing enumfacing : EnumFacing.field_82609_l) {
            int j = this.func_178603_a(i, enumfacing);
            if(j >= 0 && !this.field_178612_d.get(j)) {
               this.field_178612_d.set(j, true);
               arraydeque.add(IntegerCache.valueOf(j));
            }
         }
      }

      return enumset;
   }

   private void func_178610_a(int p_178610_1_, Set p_178610_2_) {
      int i = p_178610_1_ >> 0 & 15;
      if(i == 0) {
         p_178610_2_.add(EnumFacing.WEST);
      } else if(i == 15) {
         p_178610_2_.add(EnumFacing.EAST);
      }

      int j = p_178610_1_ >> 8 & 15;
      if(j == 0) {
         p_178610_2_.add(EnumFacing.DOWN);
      } else if(j == 15) {
         p_178610_2_.add(EnumFacing.UP);
      }

      int k = p_178610_1_ >> 4 & 15;
      if(k == 0) {
         p_178610_2_.add(EnumFacing.NORTH);
      } else if(k == 15) {
         p_178610_2_.add(EnumFacing.SOUTH);
      }

   }

   private int func_178603_a(int p_178603_1_, EnumFacing p_178603_2_) {
      switch(VisGraph.VisGraph$1.field_178617_a[p_178603_2_.ordinal()]) {
      case 1:
         if((p_178603_1_ >> 8 & 15) == 0) {
            return -1;
         }

         return p_178603_1_ - field_178615_c;
      case 2:
         if((p_178603_1_ >> 8 & 15) == 15) {
            return -1;
         }

         return p_178603_1_ + field_178615_c;
      case 3:
         if((p_178603_1_ >> 4 & 15) == 0) {
            return -1;
         }

         return p_178603_1_ - field_178614_b;
      case 4:
         if((p_178603_1_ >> 4 & 15) == 15) {
            return -1;
         }

         return p_178603_1_ + field_178614_b;
      case 5:
         if((p_178603_1_ >> 0 & 15) == 0) {
            return -1;
         }

         return p_178603_1_ - field_178616_a;
      case 6:
         if((p_178603_1_ >> 0 & 15) == 15) {
            return -1;
         }

         return p_178603_1_ + field_178616_a;
      default:
         return -1;
      }
   }

   static {
      boolean flag = false;
      boolean flag1 = true;
      int i = 0;

      for(int j = 0; j < 16; ++j) {
         for(int k = 0; k < 16; ++k) {
            for(int l = 0; l < 16; ++l) {
               if(j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                  field_178613_e[i++] = func_178605_a(j, k, l);
               }
            }
         }
      }

   }

   static final class VisGraph$1 {
      static final int[] field_178617_a = new int[EnumFacing.values().length];
      private static final String __OBFID = "CL_00002449";

      static {
         try {
            field_178617_a[EnumFacing.DOWN.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            field_178617_a[EnumFacing.UP.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            field_178617_a[EnumFacing.NORTH.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            field_178617_a[EnumFacing.SOUTH.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            field_178617_a[EnumFacing.WEST.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            field_178617_a[EnumFacing.EAST.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
