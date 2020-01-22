package shadersmod.client;

import java.util.Iterator;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import optifine.BlockPosM;
import shadersmod.client.IteratorAxis;

public class Iterator3d implements Iterator<BlockPos> {

   public IteratorAxis iteratorAxis;
   public BlockPosM blockPos = new BlockPosM(0, 0, 0);
   public int axis = 0;
   public int kX;
   public int kY;
   public int kZ;
   public static final int AXIS_X = 0;
   public static final int AXIS_Y = 1;
   public static final int AXIS_Z = 2;


   public Iterator3d(BlockPos posStart, BlockPos posEnd, int width, int height) {
      boolean revX = posStart.getX() > posEnd.getX();
      boolean revY = posStart.getY() > posEnd.getY();
      boolean revZ = posStart.getZ() > posEnd.getZ();
      posStart = this.reverseCoord(posStart, revX, revY, revZ);
      posEnd = this.reverseCoord(posEnd, revX, revY, revZ);
      this.kX = revX?-1:1;
      this.kY = revY?-1:1;
      this.kZ = revZ?-1:1;
      Vec3 vec = new Vec3((double)(posEnd.getX() - posStart.getX()), (double)(posEnd.getY() - posStart.getY()), (double)(posEnd.getZ() - posStart.getZ()));
      Vec3 vecN = vec.normalize();
      Vec3 vecX = new Vec3(1.0D, 0.0D, 0.0D);
      double dotX = vecN.dotProduct(vecX);
      double dotXabs = Math.abs(dotX);
      Vec3 vecY = new Vec3(0.0D, 1.0D, 0.0D);
      double dotY = vecN.dotProduct(vecY);
      double dotYabs = Math.abs(dotY);
      Vec3 vecZ = new Vec3(0.0D, 0.0D, 1.0D);
      double dotZ = vecN.dotProduct(vecZ);
      double dotZabs = Math.abs(dotZ);
      BlockPos pos1;
      BlockPos pos2;
      int countX;
      double deltaY;
      double deltaZ;
      if(dotZabs >= dotYabs && dotZabs >= dotXabs) {
         this.axis = 2;
         pos1 = new BlockPos(posStart.getZ(), posStart.getY() - width, posStart.getX() - height);
         pos2 = new BlockPos(posEnd.getZ(), posStart.getY() + width + 1, posStart.getX() + height + 1);
         countX = posEnd.getZ() - posStart.getZ();
         deltaY = (double)(posEnd.getY() - posStart.getY()) / (1.0D * (double)countX);
         deltaZ = (double)(posEnd.getX() - posStart.getX()) / (1.0D * (double)countX);
         this.iteratorAxis = new IteratorAxis(pos1, pos2, deltaY, deltaZ);
      } else if(dotYabs >= dotXabs && dotYabs >= dotZabs) {
         this.axis = 1;
         pos1 = new BlockPos(posStart.getY(), posStart.getX() - width, posStart.getZ() - height);
         pos2 = new BlockPos(posEnd.getY(), posStart.getX() + width + 1, posStart.getZ() + height + 1);
         countX = posEnd.getY() - posStart.getY();
         deltaY = (double)(posEnd.getX() - posStart.getX()) / (1.0D * (double)countX);
         deltaZ = (double)(posEnd.getZ() - posStart.getZ()) / (1.0D * (double)countX);
         this.iteratorAxis = new IteratorAxis(pos1, pos2, deltaY, deltaZ);
      } else {
         this.axis = 0;
         pos1 = new BlockPos(posStart.getX(), posStart.getY() - width, posStart.getZ() - height);
         pos2 = new BlockPos(posEnd.getX(), posStart.getY() + width + 1, posStart.getZ() + height + 1);
         countX = posEnd.getX() - posStart.getX();
         deltaY = (double)(posEnd.getY() - posStart.getY()) / (1.0D * (double)countX);
         deltaZ = (double)(posEnd.getZ() - posStart.getZ()) / (1.0D * (double)countX);
         this.iteratorAxis = new IteratorAxis(pos1, pos2, deltaY, deltaZ);
      }

   }

   public BlockPos reverseCoord(BlockPos pos, boolean revX, boolean revY, boolean revZ) {
      if(revX) {
         pos = new BlockPos(-pos.getX(), pos.getY(), pos.getZ());
      }

      if(revY) {
         pos = new BlockPos(pos.getX(), -pos.getY(), pos.getZ());
      }

      if(revZ) {
         pos = new BlockPos(pos.getX(), pos.getY(), -pos.getZ());
      }

      return pos;
   }

   public boolean hasNext() {
      return this.iteratorAxis.hasNext();
   }

   public BlockPos next() {
      BlockPos pos = this.iteratorAxis.next();
      switch(this.axis) {
      case 0:
         this.blockPos.setXyz(pos.getX() * this.kX, pos.getY() * this.kY, pos.getZ() * this.kZ);
         return this.blockPos;
      case 1:
         this.blockPos.setXyz(pos.getY() * this.kX, pos.getX() * this.kY, pos.getZ() * this.kZ);
         return this.blockPos;
      case 2:
         this.blockPos.setXyz(pos.getZ() * this.kX, pos.getY() * this.kY, pos.getX() * this.kZ);
         return this.blockPos;
      default:
         this.blockPos.setXyz(pos.getX() * this.kX, pos.getY() * this.kY, pos.getZ() * this.kZ);
         return this.blockPos;
      }
   }

   public void remove() {
      throw new RuntimeException("Not supported");
   }

   public static void main(String[] args) {
      BlockPos posStart = new BlockPos(10, 20, 30);
      BlockPos posEnd = new BlockPos(30, 40, 20);
      Iterator3d it = new Iterator3d(posStart, posEnd, 1, 1);

      while(it.hasNext()) {
         BlockPos blockPos = it.next();
         System.out.println("" + blockPos);
      }

   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object next() {
      return this.next();
   }
}
