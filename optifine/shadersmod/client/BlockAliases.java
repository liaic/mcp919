package shadersmod.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.ConnectedParser;
import optifine.MatchBlock;
import optifine.PropertiesOrdered;
import optifine.Reflector;
import optifine.ReflectorForge;
import optifine.StrUtils;
import shadersmod.client.BlockAlias;
import shadersmod.client.IShaderPack;
import shadersmod.client.Shaders;

public class BlockAliases {

   public static BlockAlias[][] blockAliases = (BlockAlias[][])null;
   public static boolean updateOnResourcesReloaded;


   public static int getMappedBlockId(int blockId, int metadata) {
      if(blockAliases == null) {
         return blockId;
      } else if(blockId >= 0 && blockId < blockAliases.length) {
         BlockAlias[] aliases = blockAliases[blockId];
         if(aliases == null) {
            return blockId;
         } else {
            for(int i = 0; i < aliases.length; ++i) {
               BlockAlias ba = aliases[i];
               if(ba.matches(blockId, metadata)) {
                  return ba.getBlockId();
               }
            }

            return blockId;
         }
      } else {
         return blockId;
      }
   }

   public static void resourcesReloaded() {
      if(updateOnResourcesReloaded) {
         updateOnResourcesReloaded = false;
         update(Shaders.shaderPack);
      }
   }

   public static void update(IShaderPack shaderPack) {
      reset();
      if(shaderPack != null) {
         if(Reflector.Loader_getActiveModList.exists() && Minecraft.getMinecraft().getResourcePackRepository() == null) {
            Config.dbg("[Shaders] Delayed loading of block mappings after resources are loaded");
            updateOnResourcesReloaded = true;
         } else {
            ArrayList listBlockAliases = new ArrayList();
            String path = "/shaders/block.properties";
            InputStream in = shaderPack.getResourceAsStream(path);
            if(in != null) {
               loadBlockAliases(in, path, listBlockAliases);
            }

            loadModBlockAliases(listBlockAliases);
            if(listBlockAliases.size() > 0) {
               blockAliases = toArrays(listBlockAliases);
            }
         }
      }
   }

   public static void loadModBlockAliases(List<List<BlockAlias>> listBlockAliases) {
      String[] modIds = ReflectorForge.getForgeModIds();

      for(int i = 0; i < modIds.length; ++i) {
         String modId = modIds[i];

         try {
            ResourceLocation e = new ResourceLocation(modId, "shaders/block.properties");
            InputStream in = Config.getResourceStream(e);
            loadBlockAliases(in, e.toString(), listBlockAliases);
         } catch (IOException var6) {
            ;
         }
      }

   }

   public static void loadBlockAliases(InputStream in, String path, List<List<BlockAlias>> listBlockAliases) {
      if(in != null) {
         try {
            PropertiesOrdered e = new PropertiesOrdered();
            e.load(in);
            in.close();
            Config.dbg("[Shaders] Parsing block mappings: " + path);
            ConnectedParser cp = new ConnectedParser("Shaders");
            Set keys = e.keySet();
            Iterator it = keys.iterator();

            while(it.hasNext()) {
               String key = (String)it.next();
               String val = e.getProperty(key);
               String prefix = "block.";
               if(!key.startsWith(prefix)) {
                  Config.warn("[Shaders] Invalid block ID: " + key);
               } else {
                  String blockIdStr = StrUtils.removePrefix(key, prefix);
                  int blockId = Config.parseInt(blockIdStr, -1);
                  if(blockId < 0) {
                     Config.warn("[Shaders] Invalid block ID: " + key);
                  } else {
                     MatchBlock[] matchBlocks = cp.parseMatchBlocks(val);
                     if(matchBlocks != null && matchBlocks.length >= 1) {
                        BlockAlias ba = new BlockAlias(blockId, matchBlocks);
                        addToList(listBlockAliases, ba);
                     } else {
                        Config.warn("[Shaders] Invalid block ID mapping: " + key + "=" + val);
                     }
                  }
               }
            }
         } catch (IOException var14) {
            Config.warn("[Shaders] Error reading: " + path);
         }

      }
   }

   public static void addToList(List<List<BlockAlias>> blocksAliases, BlockAlias ba) {
      int[] blockIds = ba.getMatchBlockIds();

      for(int i = 0; i < blockIds.length; ++i) {
         int blockId = blockIds[i];

         while(blockId >= blocksAliases.size()) {
            blocksAliases.add((Object)null);
         }

         Object blockAliases = (List)blocksAliases.get(blockId);
         if(blockAliases == null) {
            blockAliases = new ArrayList();
            blocksAliases.set(blockId, blockAliases);
         }

         BlockAlias baBlock = new BlockAlias(ba.getBlockId(), ba.getMatchBlocks(blockId));
         ((List)blockAliases).add(baBlock);
      }

   }

   public static BlockAlias[][] toArrays(List<List<BlockAlias>> listBlocksAliases) {
      BlockAlias[][] bas = new BlockAlias[listBlocksAliases.size()][];

      for(int i = 0; i < bas.length; ++i) {
         List listBlockAliases = (List)listBlocksAliases.get(i);
         if(listBlockAliases != null) {
            bas[i] = (BlockAlias[])((BlockAlias[])listBlockAliases.toArray(new BlockAlias[listBlockAliases.size()]));
         }
      }

      return bas;
   }

   public static void reset() {
      blockAliases = (BlockAlias[][])null;
   }

}
