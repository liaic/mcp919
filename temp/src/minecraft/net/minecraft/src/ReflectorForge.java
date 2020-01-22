package net.minecraft.src;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.src.Reflector;
import net.minecraft.util.ResourceLocation;

public class ReflectorForge {
   public static void FMLClientHandler_trackBrokenTexture(ResourceLocation p_FMLClientHandler_trackBrokenTexture_0_, String p_FMLClientHandler_trackBrokenTexture_1_) {
      if(!Reflector.FMLClientHandler_trackBrokenTexture.exists()) {
         Object object = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
         Reflector.call(object, Reflector.FMLClientHandler_trackBrokenTexture, new Object[]{p_FMLClientHandler_trackBrokenTexture_0_, p_FMLClientHandler_trackBrokenTexture_1_});
      }
   }

   public static void FMLClientHandler_trackMissingTexture(ResourceLocation p_FMLClientHandler_trackMissingTexture_0_) {
      if(!Reflector.FMLClientHandler_trackMissingTexture.exists()) {
         Object object = Reflector.call(Reflector.FMLClientHandler_instance, new Object[0]);
         Reflector.call(object, Reflector.FMLClientHandler_trackMissingTexture, new Object[]{p_FMLClientHandler_trackMissingTexture_0_});
      }
   }

   public static void putLaunchBlackboard(String p_putLaunchBlackboard_0_, Object p_putLaunchBlackboard_1_) {
      Map map = (Map)Reflector.getFieldValue(Reflector.Launch_blackboard);
      if(map != null) {
         map.put(p_putLaunchBlackboard_0_, p_putLaunchBlackboard_1_);
      }
   }

   public static boolean renderFirstPersonHand(RenderGlobal p_renderFirstPersonHand_0_, float p_renderFirstPersonHand_1_, int p_renderFirstPersonHand_2_) {
      return !Reflector.ForgeHooksClient_renderFirstPersonHand.exists()?false:Reflector.callBoolean(Reflector.ForgeHooksClient_renderFirstPersonHand, new Object[]{p_renderFirstPersonHand_0_, Float.valueOf(p_renderFirstPersonHand_1_), Integer.valueOf(p_renderFirstPersonHand_2_)});
   }

   public static InputStream getOptiFineResourceStream(String p_getOptiFineResourceStream_0_) {
      if(!Reflector.OptiFineClassTransformer_instance.exists()) {
         return null;
      } else {
         Object object = Reflector.getFieldValue(Reflector.OptiFineClassTransformer_instance);
         if(object == null) {
            return null;
         } else {
            if(p_getOptiFineResourceStream_0_.startsWith("/")) {
               p_getOptiFineResourceStream_0_ = p_getOptiFineResourceStream_0_.substring(1);
            }

            byte[] abyte = (byte[])((byte[])Reflector.call(object, Reflector.OptiFineClassTransformer_getOptiFineResource, new Object[]{p_getOptiFineResourceStream_0_}));
            if(abyte == null) {
               return null;
            } else {
               InputStream inputstream = new ByteArrayInputStream(abyte);
               return inputstream;
            }
         }
      }
   }

   public static boolean blockHasTileEntity(IBlockState p_blockHasTileEntity_0_) {
      Block block = p_blockHasTileEntity_0_.func_177230_c();
      return !Reflector.ForgeBlock_hasTileEntity.exists()?block.func_149716_u():Reflector.callBoolean(block, Reflector.ForgeBlock_hasTileEntity, new Object[]{p_blockHasTileEntity_0_});
   }

   public static boolean isItemDamaged(ItemStack p_isItemDamaged_0_) {
      return !Reflector.ForgeItem_showDurabilityBar.exists()?p_isItemDamaged_0_.func_77951_h():Reflector.callBoolean(p_isItemDamaged_0_.func_77973_b(), Reflector.ForgeItem_showDurabilityBar, new Object[]{p_isItemDamaged_0_});
   }

   public static boolean armorHasOverlay(ItemArmor p_armorHasOverlay_0_, ItemStack p_armorHasOverlay_1_) {
      int i = p_armorHasOverlay_0_.func_82814_b(p_armorHasOverlay_1_);
      return i != 16777215;
   }

   public static String[] getForgeModIds() {
      if(!Reflector.Loader.exists()) {
         return new String[0];
      } else {
         Object object = Reflector.call(Reflector.Loader_instance, new Object[0]);
         List list = (List)Reflector.call(object, Reflector.Loader_getActiveModList, new Object[0]);
         if(list == null) {
            return new String[0];
         } else {
            List<String> list1 = new ArrayList();

            for(Object object1 : list) {
               if(Reflector.ModContainer.isInstance(object1)) {
                  String s = Reflector.callString(object1, Reflector.ModContainer_getModId, new Object[0]);
                  if(s != null) {
                     list1.add(s);
                  }
               }
            }

            String[] astring = (String[])((String[])list1.toArray(new String[list1.size()]));
            return astring;
         }
      }
   }
}
