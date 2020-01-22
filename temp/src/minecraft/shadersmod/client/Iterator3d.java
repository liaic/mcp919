package shadersmod.client;

import java.util.Iterator;
import net.minecraft.src.BlockPosM;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import shadersmod.client.IteratorAxis;

public class Iterator3d implements Iterator<BlockPos> {
   private IteratorAxis iteratorAxis;
   private BlockPosM blockPos = new BlockPosM(0, 0, 0);
   private int axis = 0;
   private int kX;
   private int kY;
   private int kZ;
   private static final int AXIS_X = 0;
   private static final int AXIS_Y = 1;
   private static final int AXIS_Z = 2;

   public Iterator3d(BlockPos posStart, BlockPos posEnd, int width, int height) {
      boolean flag = posStart.func_177958_n() > posEnd.func_177958_n();
      boolean flag1 = posStart.func_177956_o() > posEnd.func_177956_o();
      boolean flag2 = posStart.func_177952_p() > posEnd.func_177952_p();
      posStart = this.reverseCoord(posStart, flag, flag1, flag2);
      posEnd = this.reverseCoord(posEnd, flag, flag1, flag2);
      this.kX = flag?-1:1;
      this.kY = flag1?-1:1;
      this.kZ = flag2?-1:1;
      Vec3 vec3 = new Vec3((double)(posEnd.func_177958_n() - posStart.func_177958_n()), (double)(posEnd.func_177956_o() - posStart.func_177956_o()), (double)(posEnd.func_177952_p() - posStart.func_177952_p()));
      Vec3 vec31 = vec3.func_72432_b();
      Vec3 vec32 = new Vec3(1.0D, 0.0D, 0.0D);
      double d0 = vec31.func_72430_b(vec32);
      double d1 = Math.abs(d0);
      Vec3 vec33 = new Vec3(0.0D, 1.0D, 0.0D);
      double d2 = vec31.func_72430_b(vec33);
      double d3 = Math.abs(d2);
      Vec3 vec34 = new Vec3(0.0D, 0.0D, 1.0D);
      double d4 = vec31.func_72430_b(vec34);
      double d5 = Math.abs(d4);
      if(d5 >= d3 && d5 >= d1) {
         this.axis = 2;
         BlockPos blockpos3 = new BlockPos(posStart.func_177952_p(), posStart.func_177956_o() - width, posStart.func_177958_n() - height);
         BlockPos blockpos5 = new BlockPos(posEnd.func_177952_p(), posStart.func_177956_o() + width + 1, posStart.func_177958_n() + height + 1);
         int k = posEnd.func_177952_p() - posStart.func_177952_p();
         double d9 = (double)(posEnd.func_177956_o() - posStart.func_177956_o()) / (1.0D * (double)k);
         double d11 = (double)(posEnd.func_177958_n() - posStart.func_177958_n()) / (1.0D * (double)k);
         this.iteratorAxis = new IteratorAxis(blockpos3, blockpos5, d9, d11);
      } else if(d3 >= d1 && d3 >= d5) {
         this.axis = 1;
         BlockPos blockpos2 = new BlockPos(posStart.func_177956_o(), posStart.func_177958_n() - width, posStart.func_177952_p() - height);
         BlockPos blockpos4 = new BlockPos(posEnd.func_177956_o(), posStart.func_177958_n() + width + 1, posStart.func_177952_p() + height + 1);
         int j = posEnd.func_177956_o() - posStart.func_177956_o();
         double d8 = (double)(posEnd.func_177958_n() - posStart.func_177958_n()) / (1.0D * (double)j);
         double d10 = (double)(posEnd.func_177952_p() - posStart.func_177952_p()) / (1.0D * (double)j);
         this.iteratorAxis = new IteratorAxis(blockpos2, blockpos4, d8, d10);
      } else {
         this.axis = 0;
         BlockPos blockpos = new BlockPos(posStart.func_177958_n(), posStart.func_177956_o() - width, posStart.func_177952_p() - height);
         BlockPos blockpos1 = new BlockPos(posEnd.func_177958_n(), posStart.func_177956_o() + width + 1, posStart.func_177952_p() + height + 1);
         int i = posEnd.func_177958_n() - posStart.func_177958_n();
         double d6 = (double)(posEnd.func_177956_o() - posStart.func_177956_o()) / (1.0D * (double)i);
         double d7 = (double)(posEnd.func_177952_p() - posStart.func_177952_p()) / (1.0D * (double)i);
         this.iteratorAxis = new IteratorAxis(blockpos, blockpos1, d6, d7);
      }

   }

   private BlockPos reverseCoord(BlockPos pos, boolean revX, boolean revY, boolean revZ) {
      if(revX) {
         pos = new BlockPos(-pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
      }

      if(revY) {
         pos = new BlockPos(pos.func_177958_n(), -pos.func_177956_o(), pos.func_177952_p());
      }

      if(revZ) {
         pos = new BlockPos(pos.func_177958_n(), pos.func_177956_o(), -pos.func_177952_p());
      }

      return pos;
   }

   public boolean hasNext() {
      return this.iteratorAxis.hasNext();
   }

   public BlockPos next() {
      BlockPos blockpos = this.iteratorAxis.next();
      switch(this.axis) {
      case 0:
         this.blockPos.setXyz(blockpos.func_177958_n() * this.kX, blockpos.func_177956_o() * this.kY, blockpos.func_177952_p() * this.kZ);
         return this.blockPos;
      case 1:
         this.blockPos.setXyz(blockpos.func_177956_o() * this.kX, blockpos.func_177958_n() * this.kY, blockpos.func_177952_p() * this.kZ);
         return this.blockPos;
      case 2:
         this.blockPos.setXyz(blockpos.func_177952_p() * this.kX, blockpos.func_177956_o() * this.kY, blockpos.func_177958_n() * this.kZ);
         return this.blockPos;
      default:
         this.blockPos.setXyz(blockpos.func_177958_n() * this.kX, blockpos.func_177956_o() * this.kY, blockpos.func_177952_p() * this.kZ);
         return this.blockPos;
      }
   }

   public void remove() {
      throw new RuntimeException("Not supported");
   }

   public static void main(String[] args) {
      BlockPos blockpos = new BlockPos(10, 20, 30);
      BlockPos blockpos1 = new BlockPos(30, 40, 20);
      Iterator3d iterator3d = new Iterator3d(blockpos, blockpos1, 1, 1);

      while(iterator3d.hasNext()) {
         BlockPos blockpos2 = iterator3d.next();
         System.out.println("" + blockpos2);
      }

   }
}
