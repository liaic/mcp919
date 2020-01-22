package shadersmod.client;

import java.util.Iterator;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.src.BlockPosM;
import net.minecraft.util.BlockPos;
import shadersmod.client.Iterator3d;

public class IteratorRenderChunks implements Iterator<RenderChunk> {
   private ViewFrustum viewFrustum;
   private Iterator3d Iterator3d;
   private BlockPosM posBlock = new BlockPosM(0, 0, 0);

   public IteratorRenderChunks(ViewFrustum viewFrustum, BlockPos posStart, BlockPos posEnd, int width, int height) {
      this.viewFrustum = viewFrustum;
      this.Iterator3d = new Iterator3d(posStart, posEnd, width, height);
   }

   public boolean hasNext() {
      return this.Iterator3d.hasNext();
   }

   public RenderChunk next() {
      BlockPos blockpos = this.Iterator3d.next();
      this.posBlock.setXyz(blockpos.func_177958_n() << 4, blockpos.func_177956_o() << 4, blockpos.func_177952_p() << 4);
      RenderChunk renderchunk = this.viewFrustum.func_178161_a(this.posBlock);
      return renderchunk;
   }

   public void remove() {
      throw new RuntimeException("Not implemented");
   }
}
