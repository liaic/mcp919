package shadersmod.client;

import java.util.Iterator;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;
import optifine.BlockPosM;
import shadersmod.client.Iterator3d;

public class IteratorRenderChunks implements Iterator<RenderChunk> {

   public ViewFrustum viewFrustum;
   public Iterator3d Iterator3d;
   public BlockPosM posBlock = new BlockPosM(0, 0, 0);


   public IteratorRenderChunks(ViewFrustum viewFrustum, BlockPos posStart, BlockPos posEnd, int width, int height) {
      this.viewFrustum = viewFrustum;
      this.Iterator3d = new Iterator3d(posStart, posEnd, width, height);
   }

   public boolean hasNext() {
      return this.Iterator3d.hasNext();
   }

   public RenderChunk next() {
      BlockPos pos = this.Iterator3d.next();
      this.posBlock.setXyz(pos.getX() << 4, pos.getY() << 4, pos.getZ() << 4);
      RenderChunk rc = this.viewFrustum.getRenderChunk(this.posBlock);
      return rc;
   }

   public void remove() {
      throw new RuntimeException("Not implemented");
   }

   // $FF: synthetic method
   // $FF: bridge method
   public Object next() {
      return this.next();
   }
}
