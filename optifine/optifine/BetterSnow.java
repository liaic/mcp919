package optifine;

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
import net.minecraft.block.BlockLever.EnumOrientation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import optifine.Config;

public class BetterSnow {

   public static IBakedModel modelSnowLayer = null;


   public static void update() {
      modelSnowLayer = Config.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Blocks.snow_layer.getDefaultState());
   }

   public static IBakedModel getModelSnowLayer() {
      return modelSnowLayer;
   }

   public static IBlockState getStateSnowLayer() {
      return Blocks.snow_layer.getDefaultState();
   }

   public static boolean shouldRender(IBlockAccess blockAccess, Block block, IBlockState blockState, BlockPos blockPos) {
      return !checkBlock(block, blockState)?false:hasSnowNeighbours(blockAccess, blockPos);
   }

   public static boolean hasSnowNeighbours(IBlockAccess blockAccess, BlockPos pos) {
      Block blockSnow = Blocks.snow_layer;
      return blockAccess.getBlockState(pos.north()).getBlock() != blockSnow && blockAccess.getBlockState(pos.south()).getBlock() != blockSnow && blockAccess.getBlockState(pos.west()).getBlock() != blockSnow && blockAccess.getBlockState(pos.east()).getBlock() != blockSnow?false:blockAccess.getBlockState(pos.down()).getBlock().isOpaqueCube();
   }

   public static boolean checkBlock(Block block, IBlockState blockState) {
      if(block.isFullCube()) {
         return false;
      } else if(block.isOpaqueCube()) {
         return false;
      } else if(block instanceof BlockSnow) {
         return false;
      } else if(block instanceof BlockBush && (block instanceof BlockDoublePlant || block instanceof BlockFlower || block instanceof BlockMushroom || block instanceof BlockSapling || block instanceof BlockTallGrass)) {
         return true;
      } else if(!(block instanceof BlockFence) && !(block instanceof BlockFenceGate) && !(block instanceof BlockFlowerPot) && !(block instanceof BlockPane) && !(block instanceof BlockReed) && !(block instanceof BlockWall)) {
         if(block instanceof BlockRedstoneTorch && blockState.getValue(BlockTorch.FACING) == EnumFacing.UP) {
            return true;
         } else {
            if(block instanceof BlockLever) {
               Comparable orient = blockState.getValue(BlockLever.FACING);
               if(orient == EnumOrientation.UP_X || orient == EnumOrientation.UP_Z) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return true;
      }
   }

}
