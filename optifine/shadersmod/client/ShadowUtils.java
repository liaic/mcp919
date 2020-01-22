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
      float shadowRenderDistance = Shaders.getShadowRenderDistance();
      if(shadowRenderDistance > 0.0F && shadowRenderDistance < (float)((renderDistanceChunks - 1) * 16)) {
         int shadowDistanceChunks1 = MathHelper.ceiling_float_int(shadowRenderDistance / 16.0F) + 1;
         float car1 = world.getCelestialAngleRadians((float)partialTicks);
         float sunTiltRad = Shaders.sunPathRotation * 0.017453292F;
         float sar = car1 > 1.5707964F && car1 < 4.712389F?car1 + 3.1415927F:car1;
         float dx = -MathHelper.sin(sar);
         float dy = MathHelper.cos(sar) * MathHelper.cos(sunTiltRad);
         float dz = -MathHelper.cos(sar) * MathHelper.sin(sunTiltRad);
         BlockPos posEntity = new BlockPos(MathHelper.floor_double(viewEntity.posX) >> 4, MathHelper.floor_double(viewEntity.posY) >> 4, MathHelper.floor_double(viewEntity.posZ) >> 4);
         BlockPos posStart = posEntity.add((double)(-dx * (float)shadowDistanceChunks1), (double)(-dy * (float)shadowDistanceChunks1), (double)(-dz * (float)shadowDistanceChunks1));
         BlockPos posEnd = posEntity.add((double)(dx * (float)renderDistanceChunks), (double)(dy * (float)renderDistanceChunks), (double)(dz * (float)renderDistanceChunks));
         IteratorRenderChunks it = new IteratorRenderChunks(viewFrustum, posStart, posEnd, shadowDistanceChunks1, shadowDistanceChunks1);
         return it;
      } else {
         List shadowDistanceChunks = Arrays.asList(viewFrustum.renderChunks);
         Iterator car = shadowDistanceChunks.iterator();
         return car;
      }
   }
}
