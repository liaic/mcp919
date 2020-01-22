package net.minecraft.src;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.src.Config;
import net.minecraft.src.DynamicLight;
import net.minecraft.src.DynamicLightsMap;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class DynamicLights {
   private static DynamicLightsMap mapDynamicLights = new DynamicLightsMap();
   private static long timeUpdateMs = 0L;
   private static final double MAX_DIST = 7.5D;
   private static final double MAX_DIST_SQ = 56.25D;
   private static final int LIGHT_LEVEL_MAX = 15;
   private static final int LIGHT_LEVEL_FIRE = 15;
   private static final int LIGHT_LEVEL_BLAZE = 10;
   private static final int LIGHT_LEVEL_MAGMA_CUBE = 8;
   private static final int LIGHT_LEVEL_MAGMA_CUBE_CORE = 13;
   private static final int LIGHT_LEVEL_GLOWSTONE_DUST = 8;
   private static final int LIGHT_LEVEL_PRISMARINE_CRYSTALS = 8;

   public static void entityAdded(Entity p_entityAdded_0_, RenderGlobal p_entityAdded_1_) {
   }

   public static void entityRemoved(Entity p_entityRemoved_0_, RenderGlobal p_entityRemoved_1_) {
      synchronized(mapDynamicLights) {
         DynamicLight dynamiclight = mapDynamicLights.remove(p_entityRemoved_0_.func_145782_y());
         if(dynamiclight != null) {
            dynamiclight.updateLitChunks(p_entityRemoved_1_);
         }

      }
   }

   public static void update(RenderGlobal p_update_0_) {
      long i = System.currentTimeMillis();
      if(i >= timeUpdateMs + 50L) {
         timeUpdateMs = i;
         synchronized(mapDynamicLights) {
            updateMapDynamicLights(p_update_0_);
            if(mapDynamicLights.size() > 0) {
               List<DynamicLight> list = mapDynamicLights.valueList();

               for(int j = 0; j < list.size(); ++j) {
                  DynamicLight dynamiclight = (DynamicLight)list.get(j);
                  dynamiclight.update(p_update_0_);
               }

            }
         }
      }
   }

   private static void updateMapDynamicLights(RenderGlobal p_updateMapDynamicLights_0_) {
      World world = p_updateMapDynamicLights_0_.getWorld();
      if(world != null) {
         for(Entity entity : world.func_72910_y()) {
            int i = getLightLevel(entity);
            if(i > 0) {
               int j = entity.func_145782_y();
               DynamicLight dynamiclight = mapDynamicLights.get(j);
               if(dynamiclight == null) {
                  dynamiclight = new DynamicLight(entity);
                  mapDynamicLights.put(j, dynamiclight);
               }
            } else {
               int k = entity.func_145782_y();
               DynamicLight dynamiclight1 = mapDynamicLights.remove(k);
               if(dynamiclight1 != null) {
                  dynamiclight1.updateLitChunks(p_updateMapDynamicLights_0_);
               }
            }
         }

      }
   }

   public static int getCombinedLight(BlockPos p_getCombinedLight_0_, int p_getCombinedLight_1_) {
      double d0 = getLightLevel(p_getCombinedLight_0_);
      p_getCombinedLight_1_ = getCombinedLight(d0, p_getCombinedLight_1_);
      return p_getCombinedLight_1_;
   }

   public static int getCombinedLight(Entity p_getCombinedLight_0_, int p_getCombinedLight_1_) {
      double d0 = (double)getLightLevel(p_getCombinedLight_0_);
      p_getCombinedLight_1_ = getCombinedLight(d0, p_getCombinedLight_1_);
      return p_getCombinedLight_1_;
   }

   public static int getCombinedLight(double p_getCombinedLight_0_, int p_getCombinedLight_2_) {
      if(p_getCombinedLight_0_ > 0.0D) {
         int i = (int)(p_getCombinedLight_0_ * 16.0D);
         int j = p_getCombinedLight_2_ & 255;
         if(i > j) {
            p_getCombinedLight_2_ = p_getCombinedLight_2_ & -256;
            p_getCombinedLight_2_ = p_getCombinedLight_2_ | i;
         }
      }

      return p_getCombinedLight_2_;
   }

   public static double getLightLevel(BlockPos p_getLightLevel_0_) {
      double d0 = 0.0D;
      synchronized(mapDynamicLights) {
         List<DynamicLight> list = mapDynamicLights.valueList();

         for(int i = 0; i < list.size(); ++i) {
            DynamicLight dynamiclight = (DynamicLight)list.get(i);
            int j = dynamiclight.getLastLightLevel();
            if(j > 0) {
               double d1 = dynamiclight.getLastPosX();
               double d2 = dynamiclight.getLastPosY();
               double d3 = dynamiclight.getLastPosZ();
               double d4 = (double)p_getLightLevel_0_.func_177958_n() - d1;
               double d5 = (double)p_getLightLevel_0_.func_177956_o() - d2;
               double d6 = (double)p_getLightLevel_0_.func_177952_p() - d3;
               double d7 = d4 * d4 + d5 * d5 + d6 * d6;
               if(dynamiclight.isUnderwater() && !Config.isClearWater()) {
                  j = Config.limit(j - 2, 0, 15);
                  d7 *= 2.0D;
               }

               if(d7 <= 56.25D) {
                  double d8 = Math.sqrt(d7);
                  double d9 = 1.0D - d8 / 7.5D;
                  double d10 = d9 * (double)j;
                  if(d10 > d0) {
                     d0 = d10;
                  }
               }
            }
         }
      }

      double d11 = Config.limit(d0, 0.0D, 15.0D);
      return d11;
   }

   public static int getLightLevel(ItemStack p_getLightLevel_0_) {
      if(p_getLightLevel_0_ == null) {
         return 0;
      } else {
         Item item = p_getLightLevel_0_.func_77973_b();
         if(item instanceof ItemBlock) {
            ItemBlock itemblock = (ItemBlock)item;
            Block block = itemblock.func_179223_d();
            if(block != null) {
               return block.func_149750_m();
            }
         }

         return item == Items.field_151129_at?Blocks.field_150353_l.func_149750_m():(item != Items.field_151072_bj && item != Items.field_151065_br?(item == Items.field_151114_aO?8:(item == Items.field_179563_cD?8:(item == Items.field_151064_bs?8:(item == Items.field_151156_bN?Blocks.field_150461_bJ.func_149750_m() / 2:0)))):10);
      }
   }

   public static int getLightLevel(Entity p_getLightLevel_0_) {
      if(p_getLightLevel_0_ == Config.getMinecraft().func_175606_aa() && !Config.isDynamicHandLight()) {
         return 0;
      } else {
         if(p_getLightLevel_0_ instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer)p_getLightLevel_0_;
            if(entityplayer.func_175149_v()) {
               return 0;
            }
         }

         if(p_getLightLevel_0_.func_70027_ad()) {
            return 15;
         } else if(p_getLightLevel_0_ instanceof EntityFireball) {
            return 15;
         } else if(p_getLightLevel_0_ instanceof EntityTNTPrimed) {
            return 15;
         } else if(p_getLightLevel_0_ instanceof EntityBlaze) {
            EntityBlaze entityblaze = (EntityBlaze)p_getLightLevel_0_;
            return entityblaze.func_70845_n()?15:10;
         } else if(p_getLightLevel_0_ instanceof EntityMagmaCube) {
            EntityMagmaCube entitymagmacube = (EntityMagmaCube)p_getLightLevel_0_;
            return (double)entitymagmacube.field_70811_b > 0.6D?13:8;
         } else {
            if(p_getLightLevel_0_ instanceof EntityCreeper) {
               EntityCreeper entitycreeper = (EntityCreeper)p_getLightLevel_0_;
               if((double)entitycreeper.func_70831_j(0.0F) > 0.001D) {
                  return 15;
               }
            }

            if(p_getLightLevel_0_ instanceof EntityLivingBase) {
               EntityLivingBase entitylivingbase = (EntityLivingBase)p_getLightLevel_0_;
               ItemStack itemstack2 = entitylivingbase.func_70694_bm();
               int i = getLightLevel(itemstack2);
               ItemStack itemstack1 = entitylivingbase.func_71124_b(4);
               int j = getLightLevel(itemstack1);
               return Math.max(i, j);
            } else if(p_getLightLevel_0_ instanceof EntityItem) {
               EntityItem entityitem = (EntityItem)p_getLightLevel_0_;
               ItemStack itemstack = getItemStack(entityitem);
               return getLightLevel(itemstack);
            } else {
               return 0;
            }
         }
      }
   }

   public static void removeLights(RenderGlobal p_removeLights_0_) {
      synchronized(mapDynamicLights) {
         List<DynamicLight> list = mapDynamicLights.valueList();

         for(int i = 0; i < list.size(); ++i) {
            DynamicLight dynamiclight = (DynamicLight)list.get(i);
            dynamiclight.updateLitChunks(p_removeLights_0_);
         }

         list.clear();
      }
   }

   public static void clear() {
      synchronized(mapDynamicLights) {
         mapDynamicLights.clear();
      }
   }

   public static int getCount() {
      synchronized(mapDynamicLights) {
         return mapDynamicLights.size();
      }
   }

   public static ItemStack getItemStack(EntityItem p_getItemStack_0_) {
      ItemStack itemstack = p_getItemStack_0_.func_70096_w().func_82710_f(10);
      return itemstack;
   }
}