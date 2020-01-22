package optifine;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import javax.imageio.ImageIO;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.BlockStem;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import optifine.BlockPosM;
import optifine.Config;
import optifine.ConnectedParser;
import optifine.CustomColorFader;
import optifine.CustomColormap;
import optifine.LightMap;
import optifine.LightMapPack;
import optifine.MatchBlock;
import optifine.Reflector;
import optifine.RenderEnv;
import optifine.ResUtils;
import optifine.StrUtils;
import optifine.TextureUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class CustomColors {

   public static String paletteFormatDefault = "vanilla";
   public static CustomColormap waterColors = null;
   public static CustomColormap foliagePineColors = null;
   public static CustomColormap foliageBirchColors = null;
   public static CustomColormap swampFoliageColors = null;
   public static CustomColormap swampGrassColors = null;
   public static CustomColormap[] colorsBlockColormaps = null;
   public static CustomColormap[][] blockColormaps = (CustomColormap[][])null;
   public static CustomColormap skyColors = null;
   public static CustomColorFader skyColorFader = new CustomColorFader();
   public static CustomColormap fogColors = null;
   public static CustomColorFader fogColorFader = new CustomColorFader();
   public static CustomColormap underwaterColors = null;
   public static CustomColorFader underwaterColorFader = new CustomColorFader();
   public static CustomColormap underlavaColors = null;
   public static CustomColorFader underlavaColorFader = new CustomColorFader();
   public static LightMapPack[] lightMapPacks = null;
   public static int lightmapMinDimensionId = 0;
   public static CustomColormap redstoneColors = null;
   public static CustomColormap xpOrbColors = null;
   public static int xpOrbTime = -1;
   public static CustomColormap durabilityColors = null;
   public static CustomColormap stemColors = null;
   public static CustomColormap stemMelonColors = null;
   public static CustomColormap stemPumpkinColors = null;
   public static CustomColormap myceliumParticleColors = null;
   public static boolean useDefaultGrassFoliageColors = true;
   public static int particleWaterColor = -1;
   public static int particlePortalColor = -1;
   public static int lilyPadColor = -1;
   public static int expBarTextColor = -1;
   public static int bossTextColor = -1;
   public static int signTextColor = -1;
   public static Vec3 fogColorNether = null;
   public static Vec3 fogColorEnd = null;
   public static Vec3 skyColorEnd = null;
   public static int[] spawnEggPrimaryColors = null;
   public static int[] spawnEggSecondaryColors = null;
   public static float[][] wolfCollarColors = (float[][])null;
   public static float[][] sheepColors = (float[][])null;
   public static int[] textColors = null;
   public static int[] mapColorsOriginal = null;
   public static int[] potionColors = null;
   public static final IBlockState BLOCK_STATE_DIRT = Blocks.dirt.getDefaultState();
   public static final IBlockState BLOCK_STATE_WATER = Blocks.water.getDefaultState();
   public static Random random = new Random();
   public static final CustomColors.IColorizer COLORIZER_GRASS = new CustomColors.IColorizer() {
      public int getColor(IBlockAccess blockAccess, BlockPos blockPos) {
         BiomeGenBase biome = CustomColors.getColorBiome(blockAccess, blockPos);
         return CustomColors.swampGrassColors != null && biome == BiomeGenBase.swampland?CustomColors.swampGrassColors.getColor(biome, blockPos):biome.getGrassColorAtPos(blockPos);
      }
      public boolean isColorConstant() {
         return false;
      }
   };
   public static final CustomColors.IColorizer COLORIZER_FOLIAGE = new CustomColors.IColorizer() {
      public int getColor(IBlockAccess blockAccess, BlockPos blockPos) {
         BiomeGenBase biome = CustomColors.getColorBiome(blockAccess, blockPos);
         return CustomColors.swampFoliageColors != null && biome == BiomeGenBase.swampland?CustomColors.swampFoliageColors.getColor(biome, blockPos):biome.getFoliageColorAtPos(blockPos);
      }
      public boolean isColorConstant() {
         return false;
      }
   };
   public static final CustomColors.IColorizer COLORIZER_FOLIAGE_PINE = new CustomColors.IColorizer() {
      public int getColor(IBlockAccess blockAccess, BlockPos blockPos) {
         return CustomColors.foliagePineColors != null?CustomColors.foliagePineColors.getColor(blockAccess, blockPos):ColorizerFoliage.getFoliageColorPine();
      }
      public boolean isColorConstant() {
         return CustomColors.foliagePineColors == null;
      }
   };
   public static final CustomColors.IColorizer COLORIZER_FOLIAGE_BIRCH = new CustomColors.IColorizer() {
      public int getColor(IBlockAccess blockAccess, BlockPos blockPos) {
         return CustomColors.foliageBirchColors != null?CustomColors.foliageBirchColors.getColor(blockAccess, blockPos):ColorizerFoliage.getFoliageColorBirch();
      }
      public boolean isColorConstant() {
         return CustomColors.foliageBirchColors == null;
      }
   };
   public static final CustomColors.IColorizer COLORIZER_WATER = new CustomColors.IColorizer() {
      public int getColor(IBlockAccess blockAccess, BlockPos blockPos) {
         BiomeGenBase biome = CustomColors.getColorBiome(blockAccess, blockPos);
         return CustomColors.waterColors != null?CustomColors.waterColors.getColor(biome, blockPos):(Reflector.ForgeBiome_getWaterColorMultiplier.exists()?Reflector.callInt(biome, Reflector.ForgeBiome_getWaterColorMultiplier, new Object[0]):biome.waterColorMultiplier);
      }
      public boolean isColorConstant() {
         return false;
      }
   };


   public static void update() {
      paletteFormatDefault = "vanilla";
      waterColors = null;
      foliageBirchColors = null;
      foliagePineColors = null;
      swampGrassColors = null;
      swampFoliageColors = null;
      skyColors = null;
      fogColors = null;
      underwaterColors = null;
      underlavaColors = null;
      redstoneColors = null;
      xpOrbColors = null;
      xpOrbTime = -1;
      durabilityColors = null;
      stemColors = null;
      myceliumParticleColors = null;
      lightMapPacks = null;
      particleWaterColor = -1;
      particlePortalColor = -1;
      lilyPadColor = -1;
      expBarTextColor = -1;
      bossTextColor = -1;
      signTextColor = -1;
      fogColorNether = null;
      fogColorEnd = null;
      skyColorEnd = null;
      colorsBlockColormaps = null;
      blockColormaps = (CustomColormap[][])null;
      useDefaultGrassFoliageColors = true;
      spawnEggPrimaryColors = null;
      spawnEggSecondaryColors = null;
      wolfCollarColors = (float[][])null;
      sheepColors = (float[][])null;
      textColors = null;
      setMapColors(mapColorsOriginal);
      potionColors = null;
      PotionHelper.clearPotionColorCache();
      paletteFormatDefault = getValidProperty("mcpatcher/color.properties", "palette.format", CustomColormap.FORMAT_STRINGS, "vanilla");
      String mcpColormap = "mcpatcher/colormap/";
      String[] waterPaths = new String[]{"water.png", "watercolorX.png"};
      waterColors = getCustomColors(mcpColormap, waterPaths, 256, 256);
      updateUseDefaultGrassFoliageColors();
      if(Config.isCustomColors()) {
         String[] pinePaths = new String[]{"pine.png", "pinecolor.png"};
         foliagePineColors = getCustomColors(mcpColormap, pinePaths, 256, 256);
         String[] birchPaths = new String[]{"birch.png", "birchcolor.png"};
         foliageBirchColors = getCustomColors(mcpColormap, birchPaths, 256, 256);
         String[] swampGrassPaths = new String[]{"swampgrass.png", "swampgrasscolor.png"};
         swampGrassColors = getCustomColors(mcpColormap, swampGrassPaths, 256, 256);
         String[] swampFoliagePaths = new String[]{"swampfoliage.png", "swampfoliagecolor.png"};
         swampFoliageColors = getCustomColors(mcpColormap, swampFoliagePaths, 256, 256);
         String[] sky0Paths = new String[]{"sky0.png", "skycolor0.png"};
         skyColors = getCustomColors(mcpColormap, sky0Paths, 256, 256);
         String[] fog0Paths = new String[]{"fog0.png", "fogcolor0.png"};
         fogColors = getCustomColors(mcpColormap, fog0Paths, 256, 256);
         String[] underwaterPaths = new String[]{"underwater.png", "underwatercolor.png"};
         underwaterColors = getCustomColors(mcpColormap, underwaterPaths, 256, 256);
         String[] underlavaPaths = new String[]{"underlava.png", "underlavacolor.png"};
         underlavaColors = getCustomColors(mcpColormap, underlavaPaths, 256, 256);
         String[] redstonePaths = new String[]{"redstone.png", "redstonecolor.png"};
         redstoneColors = getCustomColors(mcpColormap, redstonePaths, 16, 1);
         xpOrbColors = getCustomColors(mcpColormap + "xporb.png", -1, -1);
         durabilityColors = getCustomColors(mcpColormap + "durability.png", -1, -1);
         String[] stemPaths = new String[]{"stem.png", "stemcolor.png"};
         stemColors = getCustomColors(mcpColormap, stemPaths, 8, 1);
         stemPumpkinColors = getCustomColors(mcpColormap + "pumpkinstem.png", 8, 1);
         stemMelonColors = getCustomColors(mcpColormap + "melonstem.png", 8, 1);
         String[] myceliumPaths = new String[]{"myceliumparticle.png", "myceliumparticlecolor.png"};
         myceliumParticleColors = getCustomColors(mcpColormap, myceliumPaths, -1, -1);
         Pair lightMaps = parseLightMapPacks();
         lightMapPacks = (LightMapPack[])lightMaps.getLeft();
         lightmapMinDimensionId = ((Integer)lightMaps.getRight()).intValue();
         readColorProperties("mcpatcher/color.properties");
         blockColormaps = readBlockColormaps(new String[]{mcpColormap + "custom/", mcpColormap + "blocks/"}, colorsBlockColormaps, 256, 256);
         updateUseDefaultGrassFoliageColors();
      }
   }

   public static String getValidProperty(String fileName, String key, String[] validValues, String valDef) {
      try {
         ResourceLocation e = new ResourceLocation(fileName);
         InputStream in = Config.getResourceStream(e);
         if(in == null) {
            return valDef;
         } else {
            Properties props = new Properties();
            props.load(in);
            in.close();
            String val = props.getProperty(key);
            if(val == null) {
               return valDef;
            } else {
               List listValidValues = Arrays.asList(validValues);
               if(!listValidValues.contains(val)) {
                  warn("Invalid value: " + key + "=" + val);
                  warn("Expected values: " + Config.arrayToString((Object[])validValues));
                  return valDef;
               } else {
                  dbg("" + key + "=" + val);
                  return val;
               }
            }
         }
      } catch (FileNotFoundException var9) {
         return valDef;
      } catch (IOException var10) {
         var10.printStackTrace();
         return valDef;
      }
   }

   public static Pair<LightMapPack[], Integer> parseLightMapPacks() {
      String lightmapPrefix = "mcpatcher/lightmap/world";
      String lightmapSuffix = ".png";
      String[] pathsLightmap = ResUtils.collectFiles(lightmapPrefix, lightmapSuffix);
      HashMap mapLightmaps = new HashMap();

      int maxDimId;
      for(int setDimIds = 0; setDimIds < pathsLightmap.length; ++setDimIds) {
         String dimIds = pathsLightmap[setDimIds];
         String minDimId = StrUtils.removePrefixSuffix(dimIds, lightmapPrefix, lightmapSuffix);
         maxDimId = Config.parseInt(minDimId, Integer.MIN_VALUE);
         if(maxDimId == Integer.MIN_VALUE) {
            warn("Invalid dimension ID: " + minDimId + ", path: " + dimIds);
         } else {
            mapLightmaps.put(Integer.valueOf(maxDimId), dimIds);
         }
      }

      Set var21 = mapLightmaps.keySet();
      Integer[] var22 = (Integer[])var21.toArray(new Integer[var21.size()]);
      Arrays.sort(var22);
      if(var22.length <= 0) {
         return new ImmutablePair((Object)null, Integer.valueOf(0));
      } else {
         int var23 = var22[0].intValue();
         maxDimId = var22[var22.length - 1].intValue();
         int countDim = maxDimId - var23 + 1;
         CustomColormap[] colormaps = new CustomColormap[countDim];

         for(int lmps = 0; lmps < var22.length; ++lmps) {
            Integer i = var22[lmps];
            String cm = (String)mapLightmaps.get(i);
            CustomColormap name = getCustomColors(cm, -1, -1);
            if(name != null) {
               if(name.getWidth() < 16) {
                  warn("Invalid lightmap width: " + name.getWidth() + ", path: " + cm);
               } else {
                  int basePath = i.intValue() - var23;
                  colormaps[basePath] = name;
               }
            }
         }

         LightMapPack[] var24 = new LightMapPack[colormaps.length];

         for(int var25 = 0; var25 < colormaps.length; ++var25) {
            CustomColormap var26 = colormaps[var25];
            if(var26 != null) {
               String var27 = var26.name;
               String var28 = var26.basePath;
               CustomColormap cmRain = getCustomColors(var28 + "/" + var27 + "_rain.png", -1, -1);
               CustomColormap cmThunder = getCustomColors(var28 + "/" + var27 + "_thunder.png", -1, -1);
               LightMap lm = new LightMap(var26);
               LightMap lmRain = cmRain != null?new LightMap(cmRain):null;
               LightMap lmThunder = cmThunder != null?new LightMap(cmThunder):null;
               LightMapPack lmp = new LightMapPack(lm, lmRain, lmThunder);
               var24[var25] = lmp;
            }
         }

         return new ImmutablePair(var24, Integer.valueOf(var23));
      }
   }

   public static int getTextureHeight(String path, int defHeight) {
      try {
         InputStream e = Config.getResourceStream(new ResourceLocation(path));
         if(e == null) {
            return defHeight;
         } else {
            BufferedImage bi = ImageIO.read(e);
            e.close();
            return bi == null?defHeight:bi.getHeight();
         }
      } catch (IOException var4) {
         return defHeight;
      }
   }

   public static void readColorProperties(String fileName) {
      try {
         ResourceLocation e = new ResourceLocation(fileName);
         InputStream in = Config.getResourceStream(e);
         if(in == null) {
            return;
         }

         dbg("Loading " + fileName);
         Properties props = new Properties();
         props.load(in);
         in.close();
         particleWaterColor = readColor(props, new String[]{"particle.water", "drop.water"});
         particlePortalColor = readColor(props, "particle.portal");
         lilyPadColor = readColor(props, "lilypad");
         expBarTextColor = readColor(props, "text.xpbar");
         bossTextColor = readColor(props, "text.boss");
         signTextColor = readColor(props, "text.sign");
         fogColorNether = readColorVec3(props, "fog.nether");
         fogColorEnd = readColorVec3(props, "fog.end");
         skyColorEnd = readColorVec3(props, "sky.end");
         colorsBlockColormaps = readCustomColormaps(props, fileName);
         spawnEggPrimaryColors = readSpawnEggColors(props, fileName, "egg.shell.", "Spawn egg shell");
         spawnEggSecondaryColors = readSpawnEggColors(props, fileName, "egg.spots.", "Spawn egg spot");
         wolfCollarColors = readDyeColors(props, fileName, "collar.", "Wolf collar");
         sheepColors = readDyeColors(props, fileName, "sheep.", "Sheep");
         textColors = readTextColors(props, fileName, "text.code.", "Text");
         int[] mapColors = readMapColors(props, fileName, "map.", "Map");
         if(mapColors != null) {
            if(mapColorsOriginal == null) {
               mapColorsOriginal = getMapColors();
            }

            setMapColors(mapColors);
         }

         potionColors = readPotionColors(props, fileName, "potion.", "Potion");
         xpOrbTime = Config.parseInt(props.getProperty("xporb.time"), -1);
      } catch (FileNotFoundException var5) {
         return;
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   public static CustomColormap[] readCustomColormaps(Properties props, String fileName) {
      ArrayList list = new ArrayList();
      String palettePrefix = "palette.block.";
      HashMap map = new HashMap();
      Set keys = props.keySet();
      Iterator propNames = keys.iterator();

      String name;
      while(propNames.hasNext()) {
         String cms = (String)propNames.next();
         name = props.getProperty(cms);
         if(cms.startsWith(palettePrefix)) {
            map.put(cms, name);
         }
      }

      String[] var17 = (String[])((String[])map.keySet().toArray(new String[map.size()]));

      for(int var18 = 0; var18 < var17.length; ++var18) {
         name = var17[var18];
         String value = props.getProperty(name);
         dbg("Block palette: " + name + " = " + value);
         String path = name.substring(palettePrefix.length());
         String basePath = TextureUtils.getBasePath(fileName);
         path = TextureUtils.fixResourcePath(path, basePath);
         CustomColormap colors = getCustomColors(path, 256, 256);
         if(colors == null) {
            warn("Colormap not found: " + path);
         } else {
            ConnectedParser cp = new ConnectedParser("CustomColors");
            MatchBlock[] mbs = cp.parseMatchBlocks(value);
            if(mbs != null && mbs.length > 0) {
               for(int m = 0; m < mbs.length; ++m) {
                  MatchBlock mb = mbs[m];
                  colors.addMatchBlock(mb);
               }

               list.add(colors);
            } else {
               warn("Invalid match blocks: " + value);
            }
         }
      }

      if(list.size() <= 0) {
         return null;
      } else {
         CustomColormap[] var19 = (CustomColormap[])((CustomColormap[])list.toArray(new CustomColormap[list.size()]));
         return var19;
      }
   }

   public static CustomColormap[][] readBlockColormaps(String[] basePaths, CustomColormap[] basePalettes, int width, int height) {
      String[] paths = ResUtils.collectFiles(basePaths, new String[]{".properties"});
      Arrays.sort(paths);
      ArrayList blockList = new ArrayList();

      int cmArr;
      for(cmArr = 0; cmArr < paths.length; ++cmArr) {
         String cm = paths[cmArr];
         dbg("Block colormap: " + cm);

         try {
            ResourceLocation e = new ResourceLocation("minecraft", cm);
            InputStream in = Config.getResourceStream(e);
            if(in == null) {
               warn("File not found: " + cm);
            } else {
               Properties props = new Properties();
               props.load(in);
               CustomColormap cm1 = new CustomColormap(props, cm, width, height, paletteFormatDefault);
               if(cm1.isValid(cm) && cm1.isValidMatchBlocks(cm)) {
                  addToBlockList(cm1, blockList);
               }
            }
         } catch (FileNotFoundException var12) {
            warn("File not found: " + cm);
         } catch (Exception var13) {
            var13.printStackTrace();
         }
      }

      if(basePalettes != null) {
         for(cmArr = 0; cmArr < basePalettes.length; ++cmArr) {
            CustomColormap var15 = basePalettes[cmArr];
            addToBlockList(var15, blockList);
         }
      }

      if(blockList.size() <= 0) {
         return (CustomColormap[][])null;
      } else {
         CustomColormap[][] var14 = blockListToArray(blockList);
         return var14;
      }
   }

   public static void addToBlockList(CustomColormap cm, List blockList) {
      int[] ids = cm.getMatchBlockIds();
      if(ids != null && ids.length > 0) {
         for(int i = 0; i < ids.length; ++i) {
            int blockId = ids[i];
            if(blockId < 0) {
               warn("Invalid block ID: " + blockId);
            } else {
               addToList(cm, blockList, blockId);
            }
         }

      } else {
         warn("No match blocks: " + Config.arrayToString(ids));
      }
   }

   public static void addToList(CustomColormap cm, List list, int id) {
      while(id >= list.size()) {
         list.add((Object)null);
      }

      Object subList = (List)list.get(id);
      if(subList == null) {
         subList = new ArrayList();
         list.set(id, subList);
      }

      ((List)subList).add(cm);
   }

   public static CustomColormap[][] blockListToArray(List list) {
      CustomColormap[][] colArr = new CustomColormap[list.size()][];

      for(int i = 0; i < list.size(); ++i) {
         List subList = (List)list.get(i);
         if(subList != null) {
            CustomColormap[] subArr = (CustomColormap[])((CustomColormap[])subList.toArray(new CustomColormap[subList.size()]));
            colArr[i] = subArr;
         }
      }

      return colArr;
   }

   public static int readColor(Properties props, String[] names) {
      for(int i = 0; i < names.length; ++i) {
         String name = names[i];
         int col = readColor(props, name);
         if(col >= 0) {
            return col;
         }
      }

      return -1;
   }

   public static int readColor(Properties props, String name) {
      String str = props.getProperty(name);
      if(str == null) {
         return -1;
      } else {
         str = str.trim();
         int color = parseColor(str);
         if(color < 0) {
            warn("Invalid color: " + name + " = " + str);
            return color;
         } else {
            dbg(name + " = " + str);
            return color;
         }
      }
   }

   public static int parseColor(String str) {
      if(str == null) {
         return -1;
      } else {
         str = str.trim();

         try {
            int e = Integer.parseInt(str, 16) & 16777215;
            return e;
         } catch (NumberFormatException var2) {
            return -1;
         }
      }
   }

   public static Vec3 readColorVec3(Properties props, String name) {
      int col = readColor(props, name);
      if(col < 0) {
         return null;
      } else {
         int red = col >> 16 & 255;
         int green = col >> 8 & 255;
         int blue = col & 255;
         float redF = (float)red / 255.0F;
         float greenF = (float)green / 255.0F;
         float blueF = (float)blue / 255.0F;
         return new Vec3((double)redF, (double)greenF, (double)blueF);
      }
   }

   public static CustomColormap getCustomColors(String basePath, String[] paths, int width, int height) {
      for(int i = 0; i < paths.length; ++i) {
         String path = paths[i];
         path = basePath + path;
         CustomColormap cols = getCustomColors(path, width, height);
         if(cols != null) {
            return cols;
         }
      }

      return null;
   }

   public static CustomColormap getCustomColors(String pathImage, int width, int height) {
      try {
         ResourceLocation e = new ResourceLocation(pathImage);
         if(!Config.hasResource(e)) {
            return null;
         } else {
            dbg("Colormap " + pathImage);
            Properties props = new Properties();
            String pathProps = StrUtils.replaceSuffix(pathImage, ".png", ".properties");
            ResourceLocation locProps = new ResourceLocation(pathProps);
            if(Config.hasResource(locProps)) {
               InputStream cm = Config.getResourceStream(locProps);
               props.load(cm);
               cm.close();
               dbg("Colormap properties: " + pathProps);
            } else {
               props.put("format", paletteFormatDefault);
               props.put("source", pathImage);
               pathProps = pathImage;
            }

            CustomColormap cm1 = new CustomColormap(props, pathProps, width, height, paletteFormatDefault);
            return !cm1.isValid(pathProps)?null:cm1;
         }
      } catch (Exception var8) {
         var8.printStackTrace();
         return null;
      }
   }

   public static void updateUseDefaultGrassFoliageColors() {
      useDefaultGrassFoliageColors = foliageBirchColors == null && foliagePineColors == null && swampGrassColors == null && swampFoliageColors == null && Config.isSwampColors() && Config.isSmoothBiomes();
   }

   public static int getColorMultiplier(BakedQuad quad, Block block, IBlockAccess blockAccess, BlockPos blockPos, RenderEnv renderEnv) {
      if(blockColormaps != null) {
         IBlockState metadata = renderEnv.getBlockState();
         if(!quad.hasTintIndex()) {
            if(block == Blocks.grass) {
               metadata = BLOCK_STATE_DIRT;
            }

            if(block == Blocks.redstone_wire) {
               return -1;
            }
         }

         if(block == Blocks.double_plant && renderEnv.getMetadata() >= 8) {
            blockPos = blockPos.down();
            metadata = blockAccess.getBlockState(blockPos);
         }

         CustomColormap colorizer = getBlockColormap(metadata);
         if(colorizer != null) {
            if(Config.isSmoothBiomes() && !colorizer.isColorConstant()) {
               return getSmoothColorMultiplier(blockAccess, blockPos, colorizer, renderEnv.getColorizerBlockPosM());
            }

            return colorizer.getColor(blockAccess, blockPos);
         }
      }

      if(!quad.hasTintIndex()) {
         return -1;
      } else if(block == Blocks.waterlily) {
         return getLilypadColorMultiplier(blockAccess, blockPos);
      } else if(block == Blocks.redstone_wire) {
         return getRedstoneColor(renderEnv.getBlockState());
      } else if(block instanceof BlockStem) {
         return getStemColorMultiplier(block, blockAccess, blockPos, renderEnv);
      } else if(useDefaultGrassFoliageColors) {
         return -1;
      } else {
         int metadata1 = renderEnv.getMetadata();
         CustomColors.IColorizer colorizer1;
         if(block != Blocks.grass && block != Blocks.tallgrass && block != Blocks.double_plant) {
            if(block == Blocks.double_plant) {
               colorizer1 = COLORIZER_GRASS;
               if(metadata1 >= 8) {
                  blockPos = blockPos.down();
               }
            } else if(block == Blocks.leaves) {
               switch(metadata1 & 3) {
               case 0:
                  colorizer1 = COLORIZER_FOLIAGE;
                  break;
               case 1:
                  colorizer1 = COLORIZER_FOLIAGE_PINE;
                  break;
               case 2:
                  colorizer1 = COLORIZER_FOLIAGE_BIRCH;
                  break;
               default:
                  colorizer1 = COLORIZER_FOLIAGE;
               }
            } else if(block == Blocks.leaves2) {
               colorizer1 = COLORIZER_FOLIAGE;
            } else {
               if(block != Blocks.vine) {
                  return -1;
               }

               colorizer1 = COLORIZER_FOLIAGE;
            }
         } else {
            colorizer1 = COLORIZER_GRASS;
         }

         return Config.isSmoothBiomes() && !colorizer1.isColorConstant()?getSmoothColorMultiplier(blockAccess, blockPos, colorizer1, renderEnv.getColorizerBlockPosM()):colorizer1.getColor(blockAccess, blockPos);
      }
   }

   public static BiomeGenBase getColorBiome(IBlockAccess blockAccess, BlockPos blockPos) {
      BiomeGenBase biome = blockAccess.getBiomeGenForCoords(blockPos);
      if(biome == BiomeGenBase.swampland && !Config.isSwampColors()) {
         biome = BiomeGenBase.plains;
      }

      return biome;
   }

   public static CustomColormap getBlockColormap(IBlockState blockState) {
      if(blockColormaps == null) {
         return null;
      } else if(!(blockState instanceof BlockStateBase)) {
         return null;
      } else {
         BlockStateBase bs = (BlockStateBase)blockState;
         int blockId = bs.getBlockId();
         if(blockId >= 0 && blockId < blockColormaps.length) {
            CustomColormap[] cms = blockColormaps[blockId];
            if(cms == null) {
               return null;
            } else {
               for(int i = 0; i < cms.length; ++i) {
                  CustomColormap cm = cms[i];
                  if(cm.matchesBlock(bs)) {
                     return cm;
                  }
               }

               return null;
            }
         } else {
            return null;
         }
      }
   }

   public static int getSmoothColorMultiplier(IBlockAccess blockAccess, BlockPos blockPos, CustomColors.IColorizer colorizer, BlockPosM blockPosM) {
      int sumRed = 0;
      int sumGreen = 0;
      int sumBlue = 0;
      int x = blockPos.getX();
      int y = blockPos.getY();
      int z = blockPos.getZ();
      BlockPosM posM = blockPosM;

      int r;
      int g;
      int b;
      for(r = x - 1; r <= x + 1; ++r) {
         for(g = z - 1; g <= z + 1; ++g) {
            posM.setXyz(r, y, g);
            b = colorizer.getColor(blockAccess, posM);
            sumRed += b >> 16 & 255;
            sumGreen += b >> 8 & 255;
            sumBlue += b & 255;
         }
      }

      r = sumRed / 9;
      g = sumGreen / 9;
      b = sumBlue / 9;
      return r << 16 | g << 8 | b;
   }

   public static int getFluidColor(IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, RenderEnv renderEnv) {
      Block block = blockState.getBlock();
      Object colorizer = getBlockColormap(blockState);
      if(colorizer == null && block.getMaterial() == Material.water) {
         colorizer = COLORIZER_WATER;
      }

      return colorizer == null?block.colorMultiplier(blockAccess, blockPos):(Config.isSmoothBiomes() && !((CustomColors.IColorizer)colorizer).isColorConstant()?getSmoothColorMultiplier(blockAccess, blockPos, (CustomColors.IColorizer)colorizer, renderEnv.getColorizerBlockPosM()):((CustomColors.IColorizer)colorizer).getColor(blockAccess, blockPos));
   }

   public static void updatePortalFX(EntityFX fx) {
      if(particlePortalColor >= 0) {
         int col = particlePortalColor;
         int red = col >> 16 & 255;
         int green = col >> 8 & 255;
         int blue = col & 255;
         float redF = (float)red / 255.0F;
         float greenF = (float)green / 255.0F;
         float blueF = (float)blue / 255.0F;
         fx.setRBGColorF(redF, greenF, blueF);
      }
   }

   public static void updateMyceliumFX(EntityFX fx) {
      if(myceliumParticleColors != null) {
         int col = myceliumParticleColors.getColorRandom();
         int red = col >> 16 & 255;
         int green = col >> 8 & 255;
         int blue = col & 255;
         float redF = (float)red / 255.0F;
         float greenF = (float)green / 255.0F;
         float blueF = (float)blue / 255.0F;
         fx.setRBGColorF(redF, greenF, blueF);
      }
   }

   public static int getRedstoneColor(IBlockState blockState) {
      if(redstoneColors == null) {
         return -1;
      } else {
         int level = getRedstoneLevel(blockState, 15);
         int col = redstoneColors.getColor(level);
         return col;
      }
   }

   public static void updateReddustFX(EntityFX fx, IBlockAccess blockAccess, double x, double y, double z) {
      if(redstoneColors != null) {
         IBlockState state = blockAccess.getBlockState(new BlockPos(x, y, z));
         int level = getRedstoneLevel(state, 15);
         int col = redstoneColors.getColor(level);
         int red = col >> 16 & 255;
         int green = col >> 8 & 255;
         int blue = col & 255;
         float redF = (float)red / 255.0F;
         float greenF = (float)green / 255.0F;
         float blueF = (float)blue / 255.0F;
         fx.setRBGColorF(redF, greenF, blueF);
      }
   }

   public static int getRedstoneLevel(IBlockState state, int def) {
      Block block = state.getBlock();
      if(!(block instanceof BlockRedstoneWire)) {
         return def;
      } else {
         Comparable val = state.getValue(BlockRedstoneWire.POWER);
         if(!(val instanceof Integer)) {
            return def;
         } else {
            Integer valInt = (Integer)val;
            return valInt.intValue();
         }
      }
   }

   public static float getXpOrbTimer(float timer) {
      if(xpOrbTime <= 0) {
         return timer;
      } else {
         float kt = 628.0F / (float)xpOrbTime;
         return timer * kt;
      }
   }

   public static int getXpOrbColor(float timer) {
      if(xpOrbColors == null) {
         return -1;
      } else {
         int index = (int)Math.round((double)((MathHelper.sin(timer) + 1.0F) * (float)(xpOrbColors.getLength() - 1)) / 2.0D);
         int col = xpOrbColors.getColor(index);
         return col;
      }
   }

   public static int getDurabilityColor(int dur255) {
      if(durabilityColors == null) {
         return -1;
      } else {
         int index = dur255 * durabilityColors.getLength() / 255;
         int col = durabilityColors.getColor(index);
         return col;
      }
   }

   public static void updateWaterFX(EntityFX fx, IBlockAccess blockAccess, double x, double y, double z) {
      if(waterColors != null || blockColormaps != null) {
         BlockPos blockPos = new BlockPos(x, y, z);
         RenderEnv renderEnv = RenderEnv.getInstance(blockAccess, BLOCK_STATE_WATER, blockPos);
         int col = getFluidColor(blockAccess, BLOCK_STATE_WATER, blockPos, renderEnv);
         int red = col >> 16 & 255;
         int green = col >> 8 & 255;
         int blue = col & 255;
         float redF = (float)red / 255.0F;
         float greenF = (float)green / 255.0F;
         float blueF = (float)blue / 255.0F;
         if(particleWaterColor >= 0) {
            int redDrop = particleWaterColor >> 16 & 255;
            int greenDrop = particleWaterColor >> 8 & 255;
            int blueDrop = particleWaterColor & 255;
            redF *= (float)redDrop / 255.0F;
            greenF *= (float)greenDrop / 255.0F;
            blueF *= (float)blueDrop / 255.0F;
         }

         fx.setRBGColorF(redF, greenF, blueF);
      }
   }

   public static int getLilypadColorMultiplier(IBlockAccess blockAccess, BlockPos blockPos) {
      return lilyPadColor < 0?Blocks.waterlily.colorMultiplier(blockAccess, blockPos):lilyPadColor;
   }

   public static Vec3 getFogColorNether(Vec3 col) {
      return fogColorNether == null?col:fogColorNether;
   }

   public static Vec3 getFogColorEnd(Vec3 col) {
      return fogColorEnd == null?col:fogColorEnd;
   }

   public static Vec3 getSkyColorEnd(Vec3 col) {
      return skyColorEnd == null?col:skyColorEnd;
   }

   public static Vec3 getSkyColor(Vec3 skyColor3d, IBlockAccess blockAccess, double x, double y, double z) {
      if(skyColors == null) {
         return skyColor3d;
      } else {
         int col = skyColors.getColorSmooth(blockAccess, x, y, z, 3);
         int red = col >> 16 & 255;
         int green = col >> 8 & 255;
         int blue = col & 255;
         float redF = (float)red / 255.0F;
         float greenF = (float)green / 255.0F;
         float blueF = (float)blue / 255.0F;
         float cRed = (float)skyColor3d.xCoord / 0.5F;
         float cGreen = (float)skyColor3d.yCoord / 0.66275F;
         float cBlue = (float)skyColor3d.zCoord;
         redF *= cRed;
         greenF *= cGreen;
         blueF *= cBlue;
         Vec3 newCol = skyColorFader.getColor((double)redF, (double)greenF, (double)blueF);
         return newCol;
      }
   }

   public static Vec3 getFogColor(Vec3 fogColor3d, IBlockAccess blockAccess, double x, double y, double z) {
      if(fogColors == null) {
         return fogColor3d;
      } else {
         int col = fogColors.getColorSmooth(blockAccess, x, y, z, 3);
         int red = col >> 16 & 255;
         int green = col >> 8 & 255;
         int blue = col & 255;
         float redF = (float)red / 255.0F;
         float greenF = (float)green / 255.0F;
         float blueF = (float)blue / 255.0F;
         float cRed = (float)fogColor3d.xCoord / 0.753F;
         float cGreen = (float)fogColor3d.yCoord / 0.8471F;
         float cBlue = (float)fogColor3d.zCoord;
         redF *= cRed;
         greenF *= cGreen;
         blueF *= cBlue;
         Vec3 newCol = fogColorFader.getColor((double)redF, (double)greenF, (double)blueF);
         return newCol;
      }
   }

   public static Vec3 getUnderwaterColor(IBlockAccess blockAccess, double x, double y, double z) {
      return getUnderFluidColor(blockAccess, x, y, z, underwaterColors, underwaterColorFader);
   }

   public static Vec3 getUnderlavaColor(IBlockAccess blockAccess, double x, double y, double z) {
      return getUnderFluidColor(blockAccess, x, y, z, underlavaColors, underlavaColorFader);
   }

   public static Vec3 getUnderFluidColor(IBlockAccess blockAccess, double x, double y, double z, CustomColormap underFluidColors, CustomColorFader underFluidColorFader) {
      if(underFluidColors == null) {
         return null;
      } else {
         int col = underFluidColors.getColorSmooth(blockAccess, x, y, z, 3);
         int red = col >> 16 & 255;
         int green = col >> 8 & 255;
         int blue = col & 255;
         float redF = (float)red / 255.0F;
         float greenF = (float)green / 255.0F;
         float blueF = (float)blue / 255.0F;
         Vec3 newCol = underFluidColorFader.getColor((double)redF, (double)greenF, (double)blueF);
         return newCol;
      }
   }

   public static int getStemColorMultiplier(Block blockStem, IBlockAccess blockAccess, BlockPos blockPos, RenderEnv renderEnv) {
      CustomColormap colors = stemColors;
      if(blockStem == Blocks.pumpkin_stem && stemPumpkinColors != null) {
         colors = stemPumpkinColors;
      }

      if(blockStem == Blocks.melon_stem && stemMelonColors != null) {
         colors = stemMelonColors;
      }

      if(colors == null) {
         return -1;
      } else {
         int level = renderEnv.getMetadata();
         return colors.getColor(level);
      }
   }

   public static boolean updateLightmap(World world, float torchFlickerX, int[] lmColors, boolean nightvision, float partialTicks) {
      if(world == null) {
         return false;
      } else if(lightMapPacks == null) {
         return false;
      } else {
         int dimensionId = world.provider.getDimensionId();
         int lightMapIndex = dimensionId - lightmapMinDimensionId;
         if(lightMapIndex >= 0 && lightMapIndex < lightMapPacks.length) {
            LightMapPack lightMapPack = lightMapPacks[lightMapIndex];
            return lightMapPack == null?false:lightMapPack.updateLightmap(world, torchFlickerX, lmColors, nightvision, partialTicks);
         } else {
            return false;
         }
      }
   }

   public static Vec3 getWorldFogColor(Vec3 fogVec, WorldClient world, Entity renderViewEntity, float partialTicks) {
      int worldType = world.provider.getDimensionId();
      switch(worldType) {
      case -1:
         fogVec = getFogColorNether(fogVec);
         break;
      case 0:
         Minecraft mc = Minecraft.getMinecraft();
         fogVec = getFogColor(fogVec, mc.theWorld, renderViewEntity.posX, renderViewEntity.posY + 1.0D, renderViewEntity.posZ);
         break;
      case 1:
         fogVec = getFogColorEnd(fogVec);
      }

      return fogVec;
   }

   public static Vec3 getWorldSkyColor(Vec3 skyVec, World world, Entity renderViewEntity, float partialTicks) {
      int worldType = world.provider.getDimensionId();
      switch(worldType) {
      case 0:
         Minecraft mc = Minecraft.getMinecraft();
         skyVec = getSkyColor(skyVec, mc.theWorld, renderViewEntity.posX, renderViewEntity.posY + 1.0D, renderViewEntity.posZ);
         break;
      case 1:
         skyVec = getSkyColorEnd(skyVec);
      }

      return skyVec;
   }

   public static int[] readSpawnEggColors(Properties props, String fileName, String prefix, String logName) {
      ArrayList list = new ArrayList();
      Set keys = props.keySet();
      int countColors = 0;
      Iterator colors = keys.iterator();

      while(colors.hasNext()) {
         String i = (String)colors.next();
         String value = props.getProperty(i);
         if(i.startsWith(prefix)) {
            String name = StrUtils.removePrefix(i, prefix);
            int id = getEntityId(name);
            int color = parseColor(value);
            if(id >= 0 && color >= 0) {
               while(list.size() <= id) {
                  list.add(Integer.valueOf(-1));
               }

               list.set(id, Integer.valueOf(color));
               ++countColors;
            } else {
               warn("Invalid spawn egg color: " + i + " = " + value);
            }
         }
      }

      if(countColors <= 0) {
         return null;
      } else {
         dbg(logName + " colors: " + countColors);
         int[] var13 = new int[list.size()];

         for(int var14 = 0; var14 < var13.length; ++var14) {
            var13[var14] = ((Integer)list.get(var14)).intValue();
         }

         return var13;
      }
   }

   public static int getSpawnEggColor(ItemMonsterPlacer item, ItemStack itemStack, int layer, int color) {
      int id = itemStack.getMetadata();
      int[] eggColors = layer == 0?spawnEggPrimaryColors:spawnEggSecondaryColors;
      if(eggColors == null) {
         return color;
      } else if(id >= 0 && id < eggColors.length) {
         int eggColor = eggColors[id];
         return eggColor < 0?color:eggColor;
      } else {
         return color;
      }
   }

   public static int getColorFromItemStack(ItemStack itemStack, int layer, int color) {
      if(itemStack == null) {
         return color;
      } else {
         Item item = itemStack.getItem();
         return item == null?color:(item instanceof ItemMonsterPlacer?getSpawnEggColor((ItemMonsterPlacer)item, itemStack, layer, color):color);
      }
   }

   public static float[][] readDyeColors(Properties props, String fileName, String prefix, String logName) {
      EnumDyeColor[] dyeValues = EnumDyeColor.values();
      HashMap mapDyes = new HashMap();

      for(int colors = 0; colors < dyeValues.length; ++colors) {
         EnumDyeColor countColors = dyeValues[colors];
         mapDyes.put(countColors.getName(), countColors);
      }

      float[][] var16 = new float[dyeValues.length][];
      int var17 = 0;
      Set keys = props.keySet();
      Iterator iter = keys.iterator();

      while(iter.hasNext()) {
         String key = (String)iter.next();
         String value = props.getProperty(key);
         if(key.startsWith(prefix)) {
            String name = StrUtils.removePrefix(key, prefix);
            if(name.equals("lightBlue")) {
               name = "light_blue";
            }

            EnumDyeColor dye = (EnumDyeColor)mapDyes.get(name);
            int color = parseColor(value);
            if(dye != null && color >= 0) {
               float[] rgb = new float[]{(float)(color >> 16 & 255) / 255.0F, (float)(color >> 8 & 255) / 255.0F, (float)(color & 255) / 255.0F};
               var16[dye.ordinal()] = rgb;
               ++var17;
            } else {
               warn("Invalid color: " + key + " = " + value);
            }
         }
      }

      if(var17 <= 0) {
         return (float[][])null;
      } else {
         dbg(logName + " colors: " + var17);
         return var16;
      }
   }

   public static float[] getDyeColors(EnumDyeColor dye, float[][] dyeColors, float[] colors) {
      if(dyeColors == null) {
         return colors;
      } else if(dye == null) {
         return colors;
      } else {
         float[] customColors = dyeColors[dye.ordinal()];
         return customColors == null?colors:customColors;
      }
   }

   public static float[] getWolfCollarColors(EnumDyeColor dye, float[] colors) {
      return getDyeColors(dye, wolfCollarColors, colors);
   }

   public static float[] getSheepColors(EnumDyeColor dye, float[] colors) {
      return getDyeColors(dye, sheepColors, colors);
   }

   public static int[] readTextColors(Properties props, String fileName, String prefix, String logName) {
      int[] colors = new int[32];
      Arrays.fill(colors, -1);
      int countColors = 0;
      Set keys = props.keySet();
      Iterator iter = keys.iterator();

      while(iter.hasNext()) {
         String key = (String)iter.next();
         String value = props.getProperty(key);
         if(key.startsWith(prefix)) {
            String name = StrUtils.removePrefix(key, prefix);
            int code = Config.parseInt(name, -1);
            int color = parseColor(value);
            if(code >= 0 && code < colors.length && color >= 0) {
               colors[code] = color;
               ++countColors;
            } else {
               warn("Invalid color: " + key + " = " + value);
            }
         }
      }

      if(countColors <= 0) {
         return null;
      } else {
         dbg(logName + " colors: " + countColors);
         return colors;
      }
   }

   public static int getTextColor(int index, int color) {
      if(textColors == null) {
         return color;
      } else if(index >= 0 && index < textColors.length) {
         int customColor = textColors[index];
         return customColor < 0?color:customColor;
      } else {
         return color;
      }
   }

   public static int[] readMapColors(Properties props, String fileName, String prefix, String logName) {
      int[] colors = new int[MapColor.mapColorArray.length];
      Arrays.fill(colors, -1);
      int countColors = 0;
      Set keys = props.keySet();
      Iterator iter = keys.iterator();

      while(iter.hasNext()) {
         String key = (String)iter.next();
         String value = props.getProperty(key);
         if(key.startsWith(prefix)) {
            String name = StrUtils.removePrefix(key, prefix);
            int index = getMapColorIndex(name);
            int color = parseColor(value);
            if(index >= 0 && index < colors.length && color >= 0) {
               colors[index] = color;
               ++countColors;
            } else {
               warn("Invalid color: " + key + " = " + value);
            }
         }
      }

      if(countColors <= 0) {
         return null;
      } else {
         dbg(logName + " colors: " + countColors);
         return colors;
      }
   }

   public static int[] readPotionColors(Properties props, String fileName, String prefix, String logName) {
      int[] colors = new int[Potion.potionTypes.length];
      Arrays.fill(colors, -1);
      int countColors = 0;
      Set keys = props.keySet();
      Iterator iter = keys.iterator();

      while(iter.hasNext()) {
         String key = (String)iter.next();
         String value = props.getProperty(key);
         if(key.startsWith(prefix)) {
            int index = getPotionId(key);
            int color = parseColor(value);
            if(index >= 0 && index < colors.length && color >= 0) {
               colors[index] = color;
               ++countColors;
            } else {
               warn("Invalid color: " + key + " = " + value);
            }
         }
      }

      if(countColors <= 0) {
         return null;
      } else {
         dbg(logName + " colors: " + countColors);
         return colors;
      }
   }

   public static int getPotionId(String name) {
      if(name.equals("potion.water")) {
         return 0;
      } else {
         Potion[] potions = Potion.potionTypes;

         for(int i = 0; i < potions.length; ++i) {
            Potion potion = potions[i];
            if(potion != null && potion.getName().equals(name)) {
               return potion.getId();
            }
         }

         return -1;
      }
   }

   public static int getPotionColor(int potionId, int color) {
      if(potionColors == null) {
         return color;
      } else if(potionId >= 0 && potionId < potionColors.length) {
         int potionColor = potionColors[potionId];
         return potionColor < 0?color:potionColor;
      } else {
         return color;
      }
   }

   public static int getMapColorIndex(String name) {
      return name == null?-1:(name.equals("air")?MapColor.airColor.colorIndex:(name.equals("grass")?MapColor.grassColor.colorIndex:(name.equals("sand")?MapColor.sandColor.colorIndex:(name.equals("cloth")?MapColor.clothColor.colorIndex:(name.equals("tnt")?MapColor.tntColor.colorIndex:(name.equals("ice")?MapColor.iceColor.colorIndex:(name.equals("iron")?MapColor.ironColor.colorIndex:(name.equals("foliage")?MapColor.foliageColor.colorIndex:(name.equals("clay")?MapColor.clayColor.colorIndex:(name.equals("dirt")?MapColor.dirtColor.colorIndex:(name.equals("stone")?MapColor.stoneColor.colorIndex:(name.equals("water")?MapColor.waterColor.colorIndex:(name.equals("wood")?MapColor.woodColor.colorIndex:(name.equals("quartz")?MapColor.quartzColor.colorIndex:(name.equals("gold")?MapColor.goldColor.colorIndex:(name.equals("diamond")?MapColor.diamondColor.colorIndex:(name.equals("lapis")?MapColor.lapisColor.colorIndex:(name.equals("emerald")?MapColor.emeraldColor.colorIndex:(name.equals("podzol")?MapColor.obsidianColor.colorIndex:(name.equals("netherrack")?MapColor.netherrackColor.colorIndex:(!name.equals("snow") && !name.equals("white")?(!name.equals("adobe") && !name.equals("orange")?(name.equals("magenta")?MapColor.magentaColor.colorIndex:(!name.equals("light_blue") && !name.equals("lightBlue")?(name.equals("yellow")?MapColor.yellowColor.colorIndex:(name.equals("lime")?MapColor.limeColor.colorIndex:(name.equals("pink")?MapColor.pinkColor.colorIndex:(name.equals("gray")?MapColor.grayColor.colorIndex:(name.equals("silver")?MapColor.silverColor.colorIndex:(name.equals("cyan")?MapColor.cyanColor.colorIndex:(name.equals("purple")?MapColor.purpleColor.colorIndex:(name.equals("blue")?MapColor.blueColor.colorIndex:(name.equals("brown")?MapColor.brownColor.colorIndex:(name.equals("green")?MapColor.greenColor.colorIndex:(name.equals("red")?MapColor.redColor.colorIndex:(name.equals("black")?MapColor.blackColor.colorIndex:-1)))))))))))):MapColor.lightBlueColor.colorIndex)):MapColor.adobeColor.colorIndex):MapColor.snowColor.colorIndex)))))))))))))))))))));
   }

   public static int[] getMapColors() {
      MapColor[] mapColors = MapColor.mapColorArray;
      int[] colors = new int[mapColors.length];
      Arrays.fill(colors, -1);

      for(int i = 0; i < mapColors.length && i < colors.length; ++i) {
         MapColor mapColor = mapColors[i];
         if(mapColor != null) {
            colors[i] = mapColor.colorValue;
         }
      }

      return colors;
   }

   public static void setMapColors(int[] colors) {
      if(colors != null) {
         MapColor[] mapColors = MapColor.mapColorArray;
         boolean changed = false;

         for(int i = 0; i < mapColors.length && i < colors.length; ++i) {
            MapColor mapColor = mapColors[i];
            if(mapColor != null) {
               int color = colors[i];
               if(color >= 0 && mapColor.colorValue != color) {
                  mapColor.colorValue = color;
                  changed = true;
               }
            }
         }

         if(changed) {
            Minecraft.getMinecraft().getTextureManager().reloadBannerTextures();
         }

      }
   }

   public static int getEntityId(String name) {
      if(name == null) {
         return -1;
      } else {
         int id = EntityList.getIDFromString(name);
         if(id < 0) {
            return -1;
         } else {
            String idName = EntityList.getStringFromID(id);
            return !Config.equals(name, idName)?-1:id;
         }
      }
   }

   public static void dbg(String str) {
      Config.dbg("CustomColors: " + str);
   }

   public static void warn(String str) {
      Config.warn("CustomColors: " + str);
   }

   public static int getExpBarTextColor(int color) {
      return expBarTextColor < 0?color:expBarTextColor;
   }

   public static int getBossTextColor(int color) {
      return bossTextColor < 0?color:bossTextColor;
   }

   public static int getSignTextColor(int color) {
      return signTextColor < 0?color:signTextColor;
   }


   public interface IColorizer {

      int getColor(IBlockAccess var1, BlockPos var2);

      boolean isColorConstant();
   }
}
