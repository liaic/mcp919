package net.minecraft.block;

import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class BlockLeavesBase extends Block {
   protected boolean field_150121_P;
   private static final String __OBFID = "CL_00000326";
   private static Map mapOriginalOpacity = new IdentityHashMap();

   protected BlockLeavesBase(Material p_i45433_1_, boolean p_i45433_2_) {
      super(p_i45433_1_);
      this.field_150121_P = p_i45433_2_;
   }

   public boolean func_149662_c() {
      return false;
   }

   public boolean func_176225_a(IBlockAccess p_176225_1_, BlockPos p_176225_2_, EnumFacing p_176225_3_) {
      return Config.isCullFacesLeaves() && p_176225_1_.func_180495_p(p_176225_2_).func_177230_c() == this?false:super.func_176225_a(p_176225_1_, p_176225_2_, p_176225_3_);
   }

   public static void setLightOpacity(Block p_setLightOpacity_0_, int p_setLightOpacity_1_) {
      if(!mapOriginalOpacity.containsKey(p_setLightOpacity_0_)) {
         mapOriginalOpacity.put(p_setLightOpacity_0_, Integer.valueOf(p_setLightOpacity_0_.func_149717_k()));
      }

      p_setLightOpacity_0_.func_149713_g(p_setLightOpacity_1_);
   }

   public static void restoreLightOpacity(Block p_restoreLightOpacity_0_) {
      if(mapOriginalOpacity.containsKey(p_restoreLightOpacity_0_)) {
         int i = ((Integer)mapOriginalOpacity.get(p_restoreLightOpacity_0_)).intValue();
         setLightOpacity(p_restoreLightOpacity_0_, i);
      }
   }
}
