package net.minecraft.client.renderer;

import java.util.ArrayDeque;
import java.util.Arrays;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.Chunk.EnumCreateEntityType;
import optifine.Config;
import optifine.DynamicLights;

public class RegionRenderCache extends ChunkCache {

   public static final IBlockState DEFAULT_STATE = Blocks.air.getDefaultState();
   public final BlockPos position;
   public int[] combinedLights;
   public IBlockState[] blockStates;
   public static final String __OBFID = "CL_00002565";
   public static ArrayDeque<int[]> cacheLights = new ArrayDeque();
   public static ArrayDeque<IBlockState[]> cacheStates = new ArrayDeque();
   public static int maxCacheSize = Config.limit(Runtime.getRuntime().availableProcessors(), 1, 32);


   public RegionRenderCache(World worldIn, BlockPos posFromIn, BlockPos posToIn, int subIn) {
      super(worldIn, posFromIn, posToIn, subIn);
      this.position = posFromIn.subtract(new Vec3i(subIn, subIn, subIn));
      boolean var5 = true;
      this.combinedLights = allocateLights(8000);
      Arrays.fill(this.combinedLights, -1);
      this.blockStates = allocateStates(8000);
   }

   public TileEntity getTileEntity(BlockPos pos) {
      int var2 = (pos.getX() >> 4) - this.chunkX;
      int var3 = (pos.getZ() >> 4) - this.chunkZ;
      if(var2 >= 0 && var2 < this.chunkArray.length) {
         Chunk[] chunks = this.chunkArray[var2];
         if(var3 >= 0 && var3 < chunks.length) {
            Chunk chunk = chunks[var3];
            return chunk == null?null:chunk.getTileEntity(pos, EnumCreateEntityType.QUEUED);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public int getCombinedLight(BlockPos p_175626_1_, int p_175626_2_) {
      int var3 = this.getPositionIndex(p_175626_1_);
      if(var3 >= 0 && var3 < this.combinedLights.length) {
         int var4 = this.combinedLights[var3];
         if(var4 == -1) {
            var4 = this.getCombinedLightRaw(p_175626_1_, p_175626_2_);
            this.combinedLights[var3] = var4;
         }

         return var4;
      } else {
         return this.getCombinedLightRaw(p_175626_1_, p_175626_2_);
      }
   }

   public int getCombinedLightRaw(BlockPos pos, int lightValue) {
      int light = super.getCombinedLight(pos, lightValue);
      if(Config.isDynamicLights() && !this.getBlockState(pos).getBlock().isOpaqueCube()) {
         light = DynamicLights.getCombinedLight(pos, light);
      }

      return light;
   }

   public IBlockState getBlockState(BlockPos pos) {
      int var2 = this.getPositionIndex(pos);
      if(var2 >= 0 && var2 < this.blockStates.length) {
         IBlockState var3 = this.blockStates[var2];
         if(var3 == null) {
            var3 = this.getBlockStateRaw(pos);
            this.blockStates[var2] = var3;
         }

         return var3;
      } else {
         return this.getBlockStateRaw(pos);
      }
   }

   public IBlockState getBlockStateRaw(BlockPos pos) {
      return super.getBlockState(pos);
   }

   public int getPositionIndex(BlockPos p_175630_1_) {
      int var2 = p_175630_1_.getX() - this.position.getX();
      int var3 = p_175630_1_.getY() - this.position.getY();
      int var4 = p_175630_1_.getZ() - this.position.getZ();
      return var2 >= 0 && var3 >= 0 && var4 >= 0 && var2 < 20 && var3 < 20 && var4 < 20?var2 * 400 + var4 * 20 + var3:-1;
   }

   public void freeBuffers() {
      freeLights(this.combinedLights);
      freeStates(this.blockStates);
   }

   public static int[] allocateLights(int size) {
      ArrayDeque var1 = cacheLights;
      synchronized(cacheLights) {
         int[] ints = (int[])cacheLights.pollLast();
         if(ints == null || ints.length < size) {
            ints = new int[size];
         }

         return ints;
      }
   }

   public static void freeLights(int[] ints) {
      ArrayDeque var1 = cacheLights;
      synchronized(cacheLights) {
         if(cacheLights.size() < maxCacheSize) {
            cacheLights.add(ints);
         }

      }
   }

   public static IBlockState[] allocateStates(int size) {
      ArrayDeque var1 = cacheStates;
      synchronized(cacheStates) {
         IBlockState[] states = (IBlockState[])cacheStates.pollLast();
         if(states != null && states.length >= size) {
            Arrays.fill(states, (Object)null);
         } else {
            states = new IBlockState[size];
         }

         return states;
      }
   }

   public static void freeStates(IBlockState[] states) {
      ArrayDeque var1 = cacheStates;
      synchronized(cacheStates) {
         if(cacheStates.size() < maxCacheSize) {
            cacheStates.add(states);
         }

      }
   }

}
