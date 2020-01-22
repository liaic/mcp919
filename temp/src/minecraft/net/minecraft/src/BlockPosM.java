package net.minecraft.src;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class BlockPosM extends BlockPos {
   private int mx;
   private int my;
   private int mz;
   private int level;
   private BlockPosM[] facings;
   private boolean needsUpdate;

   public BlockPosM(int p_i14_1_, int p_i14_2_, int p_i14_3_) {
      this(p_i14_1_, p_i14_2_, p_i14_3_, 0);
   }

   public BlockPosM(double p_i15_1_, double p_i15_3_, double p_i15_5_) {
      this(MathHelper.func_76128_c(p_i15_1_), MathHelper.func_76128_c(p_i15_3_), MathHelper.func_76128_c(p_i15_5_));
   }

   public BlockPosM(int p_i16_1_, int p_i16_2_, int p_i16_3_, int p_i16_4_) {
      super(0, 0, 0);
      this.mx = p_i16_1_;
      this.my = p_i16_2_;
      this.mz = p_i16_3_;
      this.level = p_i16_4_;
   }

   public int func_177958_n() {
      return this.mx;
   }

   public int func_177956_o() {
      return this.my;
   }

   public int func_177952_p() {
      return this.mz;
   }

   public void setXyz(int p_setXyz_1_, int p_setXyz_2_, int p_setXyz_3_) {
      this.mx = p_setXyz_1_;
      this.my = p_setXyz_2_;
      this.mz = p_setXyz_3_;
      this.needsUpdate = true;
   }

   public void setXyz(double p_setXyz_1_, double p_setXyz_3_, double p_setXyz_5_) {
      this.setXyz(MathHelper.func_76128_c(p_setXyz_1_), MathHelper.func_76128_c(p_setXyz_3_), MathHelper.func_76128_c(p_setXyz_5_));
   }

   public BlockPos func_177972_a(EnumFacing p_177972_1_) {
      if(this.level <= 0) {
         return super.func_177967_a(p_177972_1_, 1);
      } else {
         if(this.facings == null) {
            this.facings = new BlockPosM[EnumFacing.field_82609_l.length];
         }

         if(this.needsUpdate) {
            this.update();
         }

         int i = p_177972_1_.func_176745_a();
         BlockPosM blockposm = this.facings[i];
         if(blockposm == null) {
            int j = this.mx + p_177972_1_.func_82601_c();
            int k = this.my + p_177972_1_.func_96559_d();
            int l = this.mz + p_177972_1_.func_82599_e();
            blockposm = new BlockPosM(j, k, l, this.level - 1);
            this.facings[i] = blockposm;
         }

         return blockposm;
      }
   }

   public BlockPos func_177967_a(EnumFacing p_177967_1_, int p_177967_2_) {
      return p_177967_2_ == 1?this.func_177972_a(p_177967_1_):super.func_177967_a(p_177967_1_, p_177967_2_);
   }

   private void update() {
      for(int i = 0; i < 6; ++i) {
         BlockPosM blockposm = this.facings[i];
         if(blockposm != null) {
            EnumFacing enumfacing = EnumFacing.field_82609_l[i];
            int j = this.mx + enumfacing.func_82601_c();
            int k = this.my + enumfacing.func_96559_d();
            int l = this.mz + enumfacing.func_82599_e();
            blockposm.setXyz(j, k, l);
         }
      }

      this.needsUpdate = false;
   }

   public static Iterable getAllInBoxMutable(BlockPos p_getAllInBoxMutable_0_, BlockPos p_getAllInBoxMutable_1_) {
      final BlockPos blockpos = new BlockPos(Math.min(p_getAllInBoxMutable_0_.func_177958_n(), p_getAllInBoxMutable_1_.func_177958_n()), Math.min(p_getAllInBoxMutable_0_.func_177956_o(), p_getAllInBoxMutable_1_.func_177956_o()), Math.min(p_getAllInBoxMutable_0_.func_177952_p(), p_getAllInBoxMutable_1_.func_177952_p()));
      final BlockPos blockpos1 = new BlockPos(Math.max(p_getAllInBoxMutable_0_.func_177958_n(), p_getAllInBoxMutable_1_.func_177958_n()), Math.max(p_getAllInBoxMutable_0_.func_177956_o(), p_getAllInBoxMutable_1_.func_177956_o()), Math.max(p_getAllInBoxMutable_0_.func_177952_p(), p_getAllInBoxMutable_1_.func_177952_p()));
      return new Iterable() {
         public Iterator iterator() {
            return new AbstractIterator() {
               private BlockPosM theBlockPosM = null;

               protected BlockPosM computeNext0() {
                  if(this.theBlockPosM == null) {
                     this.theBlockPosM = new BlockPosM(blockpos.func_177958_n(), blockpos.func_177956_o(), blockpos.func_177952_p(), 3);
                     return this.theBlockPosM;
                  } else if(this.theBlockPosM.equals(blockpos1)) {
                     return (BlockPosM)this.endOfData();
                  } else {
                     int i = this.theBlockPosM.func_177958_n();
                     int j = this.theBlockPosM.func_177956_o();
                     int k = this.theBlockPosM.func_177952_p();
                     if(i < blockpos1.func_177958_n()) {
                        ++i;
                     } else if(j < blockpos1.func_177956_o()) {
                        i = blockpos.func_177958_n();
                        ++j;
                     } else if(k < blockpos1.func_177952_p()) {
                        i = blockpos.func_177958_n();
                        j = blockpos.func_177956_o();
                        ++k;
                     }

                     this.theBlockPosM.setXyz(i, j, k);
                     return this.theBlockPosM;
                  }
               }

               protected Object computeNext() {
                  return this.computeNext0();
               }
            };
         }
      };
   }

   public BlockPos getImmutable() {
      return new BlockPos(this.func_177958_n(), this.func_177956_o(), this.func_177952_p());
   }
}
