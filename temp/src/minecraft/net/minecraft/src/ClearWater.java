package net.minecraft.src;

import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.src.BlockPosM;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;

public class ClearWater {
   public static void updateWaterOpacity(GameSettings p_updateWaterOpacity_0_, World p_updateWaterOpacity_1_) {
      if(p_updateWaterOpacity_0_ != null) {
         int i = 3;
         if(p_updateWaterOpacity_0_.ofClearWater) {
            i = 1;
         }

         BlockLeavesBase.setLightOpacity(Blocks.field_150355_j, i);
         BlockLeavesBase.setLightOpacity(Blocks.field_150358_i, i);
      }

      if(p_updateWaterOpacity_1_ != null) {
         IChunkProvider ichunkprovider = p_updateWaterOpacity_1_.func_72863_F();
         if(ichunkprovider != null) {
            Entity entity = Config.getMinecraft().func_175606_aa();
            if(entity != null) {
               int j = (int)entity.field_70165_t / 16;
               int k = (int)entity.field_70161_v / 16;
               int l = j - 512;
               int i1 = j + 512;
               int j1 = k - 512;
               int k1 = k + 512;
               int l1 = 0;

               for(int i2 = l; i2 < i1; ++i2) {
                  for(int j2 = j1; j2 < k1; ++j2) {
                     if(ichunkprovider.func_73149_a(i2, j2)) {
                        Chunk chunk = ichunkprovider.func_73154_d(i2, j2);
                        if(chunk != null && !(chunk instanceof EmptyChunk)) {
                           int k2 = i2 << 4;
                           int l2 = j2 << 4;
                           int i3 = k2 + 16;
                           int j3 = l2 + 16;
                           BlockPosM blockposm = new BlockPosM(0, 0, 0);
                           BlockPosM blockposm1 = new BlockPosM(0, 0, 0);

                           for(int k3 = k2; k3 < i3; ++k3) {
                              for(int l3 = l2; l3 < j3; ++l3) {
                                 blockposm.setXyz(k3, 0, l3);
                                 BlockPos blockpos = p_updateWaterOpacity_1_.func_175725_q(blockposm);

                                 for(int i4 = 0; i4 < blockpos.func_177956_o(); ++i4) {
                                    blockposm1.setXyz(k3, i4, l3);
                                    IBlockState iblockstate = p_updateWaterOpacity_1_.func_180495_p(blockposm1);
                                    if(iblockstate.func_177230_c().func_149688_o() == Material.field_151586_h) {
                                       p_updateWaterOpacity_1_.func_72975_g(k3, l3, blockposm1.func_177956_o(), blockpos.func_177956_o());
                                       ++l1;
                                       break;
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               if(l1 > 0) {
                  String s = "server";
                  if(Config.isMinecraftThread()) {
                     s = "client";
                  }

                  Config.dbg("ClearWater (" + s + ") relighted " + l1 + " chunks");
               }

            }
         }
      }
   }
}
