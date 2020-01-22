package optifine;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import optifine.Config;
import optifine.CustomGuiProperties;
import optifine.PlayerControllerOF;
import optifine.ResUtils;

public class CustomGuis {

   public static Minecraft mc = Config.getMinecraft();
   public static PlayerControllerOF playerControllerOF = null;
   public static CustomGuiProperties[][] guiProperties = (CustomGuiProperties[][])null;
   public static boolean isChristmas = isChristmas();


   public static ResourceLocation getTextureLocation(ResourceLocation loc) {
      if(guiProperties == null) {
         return loc;
      } else {
         GuiScreen screen = mc.currentScreen;
         if(!(screen instanceof GuiContainer)) {
            return loc;
         } else if(loc.getResourceDomain().equals("minecraft") && loc.getResourcePath().startsWith("textures/gui/")) {
            if(playerControllerOF == null) {
               return loc;
            } else {
               WorldClient world = mc.theWorld;
               if(world == null) {
                  return loc;
               } else if(screen instanceof GuiContainerCreative) {
                  return getTexturePos(CustomGuiProperties.EnumContainer.CREATIVE, mc.thePlayer.getPosition(), world, loc, screen);
               } else if(screen instanceof GuiInventory) {
                  return getTexturePos(CustomGuiProperties.EnumContainer.INVENTORY, mc.thePlayer.getPosition(), world, loc, screen);
               } else {
                  BlockPos pos = playerControllerOF.getLastClickBlockPos();
                  if(pos != null) {
                     if(screen instanceof GuiRepair) {
                        return getTexturePos(CustomGuiProperties.EnumContainer.ANVIL, pos, world, loc, screen);
                     }

                     if(screen instanceof GuiBeacon) {
                        return getTexturePos(CustomGuiProperties.EnumContainer.BEACON, pos, world, loc, screen);
                     }

                     if(screen instanceof GuiBrewingStand) {
                        return getTexturePos(CustomGuiProperties.EnumContainer.BREWING_STAND, pos, world, loc, screen);
                     }

                     if(screen instanceof GuiChest) {
                        return getTexturePos(CustomGuiProperties.EnumContainer.CHEST, pos, world, loc, screen);
                     }

                     if(screen instanceof GuiCrafting) {
                        return getTexturePos(CustomGuiProperties.EnumContainer.CRAFTING, pos, world, loc, screen);
                     }

                     if(screen instanceof GuiDispenser) {
                        return getTexturePos(CustomGuiProperties.EnumContainer.DISPENSER, pos, world, loc, screen);
                     }

                     if(screen instanceof GuiEnchantment) {
                        return getTexturePos(CustomGuiProperties.EnumContainer.ENCHANTMENT, pos, world, loc, screen);
                     }

                     if(screen instanceof GuiFurnace) {
                        return getTexturePos(CustomGuiProperties.EnumContainer.FURNACE, pos, world, loc, screen);
                     }

                     if(screen instanceof GuiHopper) {
                        return getTexturePos(CustomGuiProperties.EnumContainer.HOPPER, pos, world, loc, screen);
                     }
                  }

                  Entity entity = playerControllerOF.getLastClickEntity();
                  if(entity != null) {
                     if(screen instanceof GuiScreenHorseInventory) {
                        return getTextureEntity(CustomGuiProperties.EnumContainer.HORSE, entity, world, loc);
                     }

                     if(screen instanceof GuiMerchant) {
                        return getTextureEntity(CustomGuiProperties.EnumContainer.VILLAGER, entity, world, loc);
                     }
                  }

                  return loc;
               }
            }
         } else {
            return loc;
         }
      }
   }

   public static ResourceLocation getTexturePos(CustomGuiProperties.EnumContainer container, BlockPos pos, IBlockAccess blockAccess, ResourceLocation loc, GuiScreen screen) {
      CustomGuiProperties[] props = guiProperties[container.ordinal()];
      if(props == null) {
         return loc;
      } else {
         for(int i = 0; i < props.length; ++i) {
            CustomGuiProperties prop = props[i];
            if(prop.matchesPos(container, pos, blockAccess, screen)) {
               return prop.getTextureLocation(loc);
            }
         }

         return loc;
      }
   }

   public static ResourceLocation getTextureEntity(CustomGuiProperties.EnumContainer container, Entity entity, IBlockAccess blockAccess, ResourceLocation loc) {
      CustomGuiProperties[] props = guiProperties[container.ordinal()];
      if(props == null) {
         return loc;
      } else {
         for(int i = 0; i < props.length; ++i) {
            CustomGuiProperties prop = props[i];
            if(prop.matchesEntity(container, entity, blockAccess)) {
               return prop.getTextureLocation(loc);
            }
         }

         return loc;
      }
   }

   public static void update() {
      guiProperties = (CustomGuiProperties[][])null;
      if(Config.isCustomGuis()) {
         ArrayList listProps = new ArrayList();
         IResourcePack[] rps = Config.getResourcePacks();

         for(int i = rps.length - 1; i >= 0; --i) {
            IResourcePack rp = rps[i];
            update(rp, listProps);
         }

         guiProperties = propertyListToArray(listProps);
      }
   }

   public static CustomGuiProperties[][] propertyListToArray(List<List<CustomGuiProperties>> listProps) {
      if(listProps.isEmpty()) {
         return (CustomGuiProperties[][])null;
      } else {
         CustomGuiProperties[][] cgps = new CustomGuiProperties[CustomGuiProperties.EnumContainer.VALUES.length][];

         for(int i = 0; i < cgps.length; ++i) {
            if(listProps.size() > i) {
               List subList = (List)listProps.get(i);
               if(subList != null) {
                  CustomGuiProperties[] subArr = (CustomGuiProperties[])((CustomGuiProperties[])subList.toArray(new CustomGuiProperties[subList.size()]));
                  cgps[i] = subArr;
               }
            }
         }

         return cgps;
      }
   }

   public static void update(IResourcePack rp, List<List<CustomGuiProperties>> listProps) {
      String[] paths = ResUtils.collectFiles(rp, "optifine/gui/container/", ".properties", (String[])null);
      Arrays.sort(paths);

      for(int i = 0; i < paths.length; ++i) {
         String name = paths[i];
         Config.dbg("CustomGuis: " + name);

         try {
            ResourceLocation e = new ResourceLocation(name);
            InputStream in = rp.getInputStream(e);
            if(in == null) {
               Config.warn("CustomGuis file not found: " + name);
            } else {
               Properties props = new Properties();
               props.load(in);
               in.close();
               CustomGuiProperties cgp = new CustomGuiProperties(props, name);
               if(cgp.isValid(name)) {
                  addToList(cgp, listProps);
               }
            }
         } catch (FileNotFoundException var9) {
            Config.warn("CustomGuis file not found: " + name);
         } catch (Exception var10) {
            var10.printStackTrace();
         }
      }

   }

   public static void addToList(CustomGuiProperties cgp, List<List<CustomGuiProperties>> listProps) {
      if(cgp.getContainer() == null) {
         warn("Invalid container: " + cgp.getContainer());
      } else {
         int indexContainer = cgp.getContainer().ordinal();

         while(listProps.size() <= indexContainer) {
            listProps.add((Object)null);
         }

         Object subList = (List)listProps.get(indexContainer);
         if(subList == null) {
            subList = new ArrayList();
            listProps.set(indexContainer, subList);
         }

         ((List)subList).add(cgp);
      }
   }

   public static PlayerControllerOF getPlayerControllerOF() {
      return playerControllerOF;
   }

   public static void setPlayerControllerOF(PlayerControllerOF playerControllerOF) {
      playerControllerOF = playerControllerOF;
   }

   public static boolean isChristmas() {
      Calendar calendar = Calendar.getInstance();
      return calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26;
   }

   public static void warn(String str) {
      Config.warn("[CustomGuis] " + str);
   }

}
