package net.minecraft.src;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class BetterSnow {
   private static IBakedModel modelSnowLayer = null;

   public static void update() {
      modelSnowLayer = Config.getMinecraft().func_175602_ab().func_175023_a().func_178125_b(Blocks.field_150431_aC.func_176223_P());
   }

   public static IBakedModel getModelSnowLayer() {
      return modelSnowLayer;
   }

   public static IBlockState getStateSnowLayer() {
      return Blocks.field_150431_aC.func_176223_P();
   }

   public static boolean shouldRender(IBlockAccess p_shouldRender_0_, Block p_shouldRender_1_, IBlockState p_shouldRender_2_, BlockPos p_shouldRender_3_) {
      return !checkBlock(p_shouldRender_1_, p_shouldRender_2_)?false:hasSnowNeighbours(p_shouldRender_0_, p_shouldRender_3_);
   }

   private static boolean hasSnowNeighbours(IBlockAccess p_hasSnowNeighbours_0_, BlockPos p_hasSnowNeighbours_1_) {
      Block block = Blocks.field_150431_aC;
      return p_hasSnowNeighbours_0_.func_180495_p(p_hasSnowNeighbours_1_.func_177978_c()).func_177230_c() != block && p_hasSnowNeighbours_0_.func_180495_p(p_hasSnowNeighbours_1_.func_177968_d()).func_177230_c() != block && p_hasSnowNeighbours_0_.func_180495_p(p_hasSnowNeighbours_1_.func_177976_e()).func_177230_c() != block && p_hasSnowNeighbours_0_.func_180495_p(p_hasSnowNeighbours_1_.func_177974_f()).func_177230_c() != block?false:p_hasSnowNeighbours_0_.func_180495_p(p_hasSnowNeighbours_1_.func_177977_b()).func_177230_c().func_149662_c();
   }

   private static boolean checkBlock(Block p_checkBlock_0_, IBlockState p_checkBlock_1_) {
      if(p_checkBlock_0_.func_149686_d()) {
         return false;
      } else if(p_checkBlock_0_.func_149662_c()) {
         return false;
      } else if(p_checkBlock_0_ instanceof BlockSnow) {
         return false;
      } else if(!(p_checkBlock_0_ instanceof BlockBush) || !(p_checkBlock_0_ instanceof BlockDoublePlant) && !(p_checkBlock_0_ instanceof BlockFlower) && !(p_checkBlock_0_ instanceof BlockMushroom) && !(p_checkBlock_0_ instanceof BlockSapling) && !(p_checkBlock_0_ instanceof BlockTallGrass)) {
         if(!(p_checkBlock_0_ instanceof BlockFence) && !(p_checkBlock_0_ instanceof BlockFenceGate) && !(p_checkBlock_0_ instanceof BlockFlowerPot) && !(p_checkBlock_0_ instanceof BlockPane) && !(p_checkBlock_0_ instanceof BlockReed) && !(p_checkBlock_0_ instanceof BlockWall)) {
            if(p_checkBlock_0_ instanceof BlockRedstoneTorch && p_checkBlock_1_.func_177229_b(BlockTorch.field_176596_a) == EnumFacing.UP) {
               return true;
            } else {
               if(p_checkBlock_0_ instanceof BlockLever) {
                  Object object = p_checkBlock_1_.func_177229_b(BlockLever.field_176360_a);
                  if(object == BlockLever.EnumOrientation.UP_X || object == BlockLever.EnumOrientation.UP_Z) {
                     return true;
                  }
               }

               return false;
            }
         } else {
            return true;
         }
      } else {
         return true;
      }
   }
}
