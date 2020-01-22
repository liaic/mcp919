package shadersmod.client;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import shadersmod.client.IteratorRenderChunks;
import shadersmod.client.Shaders;

public class ShadowUtils {
   public static Iterator<RenderChunk> makeShadowChunkIterator(WorldClient world, double partialTicks, Entity viewEntity, int renderDistanceChunks, ViewFrustum viewFrustum) {
      float f = Shaders.getShadowRenderDistance();
      if(f > 0.0F && f < (float)((renderDistanceChunks - 1) * 16)) {
         int i = MathHelper.func_76123_f(f / 16.0F) + 1;
         float f6 = world.func_72929_e((float)partialTicks);
         float f1 = Shaders.sunPathRotation * 0.017453292F;
         float f2 = f6 > 1.5707964F && f6 < 4.712389F?f6 + 3.1415927F:f6;
         float f3 = -MathHelper.func_76126_a(f2);
         float f4 = MathHelper.func_76134_b(f2) * MathHelper.func_76134_b(f1);
         float f5 = -MathHelper.func_76134_b(f2) * MathHelper.func_76126_a(f1);
         BlockPos blockpos = new BlockPos(MathHelper.func_76128_c(viewEntity.field_70165_t) >> 4, MathHelper.func_76128_c(viewEntity.field_70163_u) >> 4, MathHelper.func_76128_c(viewEntity.field_70161_v) >> 4);
         BlockPos blockpos1 = blockpos.func_177963_a((double)(-f3 * (float)i), (double)(-f4 * (float)i), (double)(-f5 * (float)i));
         BlockPos blockpos2 = blockpos.func_177963_a((double)(f3 * (float)renderDistanceChunks), (double)(f4 * (float)renderDistanceChunks), (double)(f5 * (float)renderDistanceChunks));
         IteratorRenderChunks iteratorrenderchunks = new IteratorRenderChunks(viewFrustum, blockpos1, blockpos2, i, i);
         return iteratorrenderchunks;
      } else {
         List<RenderChunk> list = Arrays.<RenderChunk>asList(viewFrustum.field_178164_f);
         Iterator<RenderChunk> iterator = list.iterator();
         return iterator;
      }
   }
}
