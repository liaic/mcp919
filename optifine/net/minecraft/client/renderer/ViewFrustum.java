package net.minecraft.client.renderer;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ViewFrustum {

   public final RenderGlobal renderGlobal;
   public final World world;
   public int countChunksY;
   public int countChunksX;
   public int countChunksZ;
   public RenderChunk[] renderChunks;
   public static final String __OBFID = "CL_00002531";


   public ViewFrustum(World worldIn, int renderDistanceChunks, RenderGlobal p_i46246_3_, IRenderChunkFactory p_i46246_4_) {
      this.renderGlobal = p_i46246_3_;
      this.world = worldIn;
      this.setCountChunksXYZ(renderDistanceChunks);
      this.createRenderChunks(p_i46246_4_);
   }

   public void createRenderChunks(IRenderChunkFactory p_178158_1_) {
      int var2 = this.countChunksX * this.countChunksY * this.countChunksZ;
      this.renderChunks = new RenderChunk[var2];
      int var3 = 0;

      for(int var4 = 0; var4 < this.countChunksX; ++var4) {
         for(int var5 = 0; var5 < this.countChunksY; ++var5) {
            for(int var6 = 0; var6 < this.countChunksZ; ++var6) {
               int var7 = (var6 * this.countChunksY + var5) * this.countChunksX + var4;
               BlockPos var8 = new BlockPos(var4 * 16, var5 * 16, var6 * 16);
               this.renderChunks[var7] = p_178158_1_.makeRenderChunk(this.world, this.renderGlobal, var8, var3++);
            }
         }
      }

   }

   public void deleteGlResources() {
      RenderChunk[] var1 = this.renderChunks;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         RenderChunk var4 = var1[var3];
         var4.deleteGlResources();
      }

   }

   public void setCountChunksXYZ(int renderDistanceChunks) {
      int var2 = renderDistanceChunks * 2 + 1;
      this.countChunksX = var2;
      this.countChunksY = 16;
      this.countChunksZ = var2;
   }

   public void updateChunkPositions(double viewEntityX, double viewEntityZ) {
      int var5 = MathHelper.floor_double(viewEntityX) - 8;
      int var6 = MathHelper.floor_double(viewEntityZ) - 8;
      int var7 = this.countChunksX * 16;

      for(int var8 = 0; var8 < this.countChunksX; ++var8) {
         int var9 = this.func_178157_a(var5, var7, var8);

         for(int var10 = 0; var10 < this.countChunksZ; ++var10) {
            int var11 = this.func_178157_a(var6, var7, var10);

            for(int var12 = 0; var12 < this.countChunksY; ++var12) {
               int var13 = var12 * 16;
               RenderChunk var14 = this.renderChunks[(var10 * this.countChunksY + var12) * this.countChunksX + var8];
               BlockPos posChunk = var14.getPosition();
               if(posChunk.getX() != var9 || posChunk.getY() != var13 || posChunk.getZ() != var11) {
                  BlockPos var15 = new BlockPos(var9, var13, var11);
                  if(!var15.equals(var14.getPosition())) {
                     var14.setPosition(var15);
                  }
               }
            }
         }
      }

   }

   public int func_178157_a(int p_178157_1_, int p_178157_2_, int p_178157_3_) {
      int var4 = p_178157_3_ * 16;
      int var5 = var4 - p_178157_1_ + p_178157_2_ / 2;
      if(var5 < 0) {
         var5 -= p_178157_2_ - 1;
      }

      return var4 - var5 / p_178157_2_ * p_178157_2_;
   }

   public void markBlocksForUpdate(int p_178162_1_, int p_178162_2_, int p_178162_3_, int p_178162_4_, int p_178162_5_, int p_178162_6_) {
      int var7 = MathHelper.bucketInt(p_178162_1_, 16);
      int var8 = MathHelper.bucketInt(p_178162_2_, 16);
      int var9 = MathHelper.bucketInt(p_178162_3_, 16);
      int var10 = MathHelper.bucketInt(p_178162_4_, 16);
      int var11 = MathHelper.bucketInt(p_178162_5_, 16);
      int var12 = MathHelper.bucketInt(p_178162_6_, 16);

      for(int var13 = var7; var13 <= var10; ++var13) {
         int var14 = var13 % this.countChunksX;
         if(var14 < 0) {
            var14 += this.countChunksX;
         }

         for(int var15 = var8; var15 <= var11; ++var15) {
            int var16 = var15 % this.countChunksY;
            if(var16 < 0) {
               var16 += this.countChunksY;
            }

            for(int var17 = var9; var17 <= var12; ++var17) {
               int var18 = var17 % this.countChunksZ;
               if(var18 < 0) {
                  var18 += this.countChunksZ;
               }

               int var19 = (var18 * this.countChunksY + var16) * this.countChunksX + var14;
               RenderChunk var20 = this.renderChunks[var19];
               var20.setNeedsUpdate(true);
            }
         }
      }

   }

   public RenderChunk getRenderChunk(BlockPos p_178161_1_) {
      int var2 = p_178161_1_.getX() >> 4;
      int var3 = p_178161_1_.getY() >> 4;
      int var4 = p_178161_1_.getZ() >> 4;
      if(var3 >= 0 && var3 < this.countChunksY) {
         var2 %= this.countChunksX;
         if(var2 < 0) {
            var2 += this.countChunksX;
         }

         var4 %= this.countChunksZ;
         if(var4 < 0) {
            var4 += this.countChunksZ;
         }

         int var5 = (var4 * this.countChunksY + var3) * this.countChunksX + var2;
         return this.renderChunks[var5];
      } else {
         return null;
      }
   }
}
