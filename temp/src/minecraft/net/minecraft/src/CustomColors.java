package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import net.minecraft.src.BlockPosM;
import net.minecraft.src.Config;
import net.minecraft.src.ConnectedParser;
import net.minecraft.src.CustomColorFader;
import net.minecraft.src.CustomColormap;
import net.minecraft.src.LightMap;
import net.minecraft.src.LightMapPack;
import net.minecraft.src.MatchBlock;
import net.minecraft.src.Reflector;
import net.minecraft.src.RenderEnv;
import net.minecraft.src.ResUtils;
import net.minecraft.src.StrUtils;
import net.minecraft.src.TextureUtils;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class CustomColors {
   private static String paletteFormatDefault = "vanilla";
   private static CustomColormap waterColors = null;
   private static CustomColormap foliagePineColors = null;
   private static CustomColormap foliageBirchColors = null;
   private static CustomColormap swampFoliageColors = null;
   private static CustomColormap swampGrassColors = null;
   private static CustomColormap[] colorsBlockColormaps = null;
   private static CustomColormap[][] blockColormaps = (CustomColormap[][])null;
   private static CustomColormap skyColors = null;
   private static CustomColorFader skyColorFader = new CustomColorFader();
   private static CustomColormap fogColors = null;
   private static CustomColorFader fogColorFader = new CustomColorFader();
   private static CustomColormap underwaterColors = null;
   private static CustomColorFader underwaterColorFader = new CustomColorFader();
   private static CustomColormap underlavaColors = null;
   private static CustomColorFader underlavaColorFader = new CustomColorFader();
   private static LightMapPack[] lightMapPacks = null;
   private static int lightmapMinDimensionId = 0;
   private static CustomColormap redstoneColors = null;
   private static CustomColormap xpOrbColors = null;
   private static int xpOrbTime = -1;
   private static CustomColormap durabilityColors = null;
   private static CustomColormap stemColors = null;
   private static CustomColormap stemMelonColors = null;
   private static CustomColormap stemPumpkinColors = null;
   private static CustomColormap myceliumParticleColors = null;
   private static boolean useDefaultGrassFoliageColors = true;
   private static int particleWaterColor = -1;
   private static int particlePortalColor = -1;
   private static int lilyPadColor = -1;
   private static int expBarTextColor = -1;
   private static int bossTextColor = -1;
   private static int signTextColor = -1;
   private static Vec3 fogColorNether = null;
   private static Vec3 fogColorEnd = null;
   private static Vec3 skyColorEnd = null;
   private static int[] spawnEggPrimaryColors = null;
   private static int[] spawnEggSecondaryColors = null;
   private static float[][] wolfCollarColors = (float[][])null;
   private static float[][] sheepColors = (float[][])null;
   private static int[] textColors = null;
   private static int[] mapColorsOriginal = null;
   private static int[] potionColors = null;
   private static final IBlockState BLOCK_STATE_DIRT = Blocks.field_150346_d.func_176223_P();
   private static final IBlockState BLOCK_STATE_WATER = Blocks.field_150355_j.func_176223_P();
   public static Random random = new Random();
   private static final CustomColors.IColorizer COLORIZER_GRASS = new CustomColors.IColorizer() {
      public int getColor(IBlockAccess p_getColor_1_, BlockPos p_getColor_2_) {
         BiomeGenBase biomegenbase = CustomColors.getColorBiome(p_getColor_1_, p_getColor_2_);
         return CustomColors.swampGrassColors != null && biomegenbase == BiomeGenBase.field_76780_h?CustomColors.swampGrassColors.getColor(biomegenbase, p_getColor_2_):biomegenbase.func_180627_b(p_getColor_2_);
      }

      public boolean isColorConstant() {
         return false;
      }
   };
   private static final CustomColors.IColorizer COLORIZER_FOLIAGE = new CustomColors.IColorizer() {
      public int getColor(IBlockAccess p_getColor_1_, BlockPos p_getColor_2_) {
         BiomeGenBase biomegenbase = CustomColors.getColorBiome(p_getColor_1_, p_getColor_2_);
         return CustomColors.swampFoliageColors != null && biomegenbase == BiomeGenBase.field_76780_h?CustomColors.swampFoliageColors.getColor(biomegenbase, p_getColor_2_):biomegenbase.func_180625_c(p_getColor_2_);
      }

      public boolean isColorConstant() {
         return false;
      }
   };
   private static final CustomColors.IColorizer COLORIZER_FOLIAGE_PINE = new CustomColors.IColorizer() {
      public int getColor(IBlockAccess p_getColor_1_, BlockPos p_getColor_2_) {
         return CustomColors.foliagePineColors != null?CustomColors.foliagePineColors.getColor(p_getColor_1_, p_getColor_2_):ColorizerFoliage.func_77466_a();
      }

      public boolean isColorConstant() {
         return CustomColors.foliagePineColors == null;
      }
   };
   private static final CustomColors.IColorizer COLORIZER_FOLIAGE_BIRCH = new CustomColors.IColorizer() {
      public int getColor(IBlockAccess p_getColor_1_, BlockPos p_getColor_2_) {
         return CustomColors.foliageBirchColors != null?CustomColors.foliageBirchColors.getColor(p_getColor_1_, p_getColor_2_):ColorizerFoliage.func_77469_b();
      }

      public boolean isColorConstant() {
         return CustomColors.foliageBirchColors == null;
      }
   };
   private static final CustomColors.IColorizer COLORIZER_WATER = new CustomColors.IColorizer() {
      public int getColor(IBlockAccess p_getColor_1_, BlockPos p_getColor_2_) {
         BiomeGenBase biomegenbase = CustomColors.getColorBiome(p_getColor_1_, p_getColor_2_);
         return CustomColors.waterColors != null?CustomColors.waterColors.getColor(biomegenbase, p_getColor_2_):(Reflector.ForgeBiome_getWaterColorMultiplier.exists()?Reflector.callInt(biomegenbase, Reflector.ForgeBiome_getWaterColorMultiplier, new Object[0]):biomegenbase.field_76759_H);
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
      String s = "mcpatcher/colormap/";
      String[] astring = new String[]{"water.png", "watercolorX.png"};
      waterColors = getCustomColors(s, astring, 256, 256);
      updateUseDefaultGrassFoliageColors();
      if(Config.isCustomColors()) {
         String[] astring1 = new String[]{"pine.png", "pinecolor.png"};
         foliagePineColors = getCustomColors(s, astring1, 256, 256);
         String[] astring2 = new String[]{"birch.png", "birchcolor.png"};
         foliageBirchColors = getCustomColors(s, astring2, 256, 256);
         String[] astring3 = new String[]{"swampgrass.png", "swampgrasscolor.png"};
         swampGrassColors = getCustomColors(s, astring3, 256, 256);
         String[] astring4 = new String[]{"swampfoliage.png", "swampfoliagecolor.png"};
         swampFoliageColors = getCustomColors(s, astring4, 256, 256);
         String[] astring5 = new String[]{"sky0.png", "skycolor0.png"};
         skyColors = getCustomColors(s, astring5, 256, 256);
         String[] astring6 = new String[]{"fog0.png", "fogcolor0.png"};
         fogColors = getCustomColors(s, astring6, 256, 256);
         String[] astring7 = new String[]{"underwater.png", "underwatercolor.png"};
         underwaterColors = getCustomColors(s, astring7, 256, 256);
         String[] astring8 = new String[]{"underlava.png", "underlavacolor.png"};
         underlavaColors = getCustomColors(s, astring8, 256, 256);
         String[] astring9 = new String[]{"redstone.png", "redstonecolor.png"};
         redstoneColors = getCustomColors(s, astring9, 16, 1);
         xpOrbColors = getCustomColors(s + "xporb.png", -1, -1);
         durabilityColors = getCustomColors(s + "durability.png", -1, -1);
         String[] astring10 = new String[]{"stem.png", "stemcolor.png"};
         stemColors = getCustomColors(s, astring10, 8, 1);
         stemPumpkinColors = getCustomColors(s + "pumpkinstem.png", 8, 1);
         stemMelonColors = getCustomColors(s + "melonstem.png", 8, 1);
         String[] astring11 = new String[]{"myceliumparticle.png", "myceliumparticlecolor.png"};
         myceliumParticleColors = getCustomColors(s, astring11, -1, -1);
         Pair<LightMapPack[], Integer> pair = parseLightMapPacks();
         lightMapPacks = (LightMapPack[])pair.getLeft();
         lightmapMinDimensionId = ((Integer)pair.getRight()).intValue();
         readColorProperties("mcpatcher/color.properties");
         blockColormaps = readBlockColormaps(new String[]{s + "custom/", s + "blocks/"}, colorsBlockColormaps, 256, 256);
         updateUseDefaultGrassFoliageColors();
      }
   }

   private static String getValidProperty(String p_getValidProperty_0_, String p_getValidProperty_1_, String[] p_getValidProperty_2_, String p_getValidProperty_3_) {
      try {
         ResourceLocation resourcelocation = new ResourceLocation(p_getValidProperty_0_);
         InputStream inputstream = Config.getResourceStream(resourcelocation);
         if(inputstream == null) {
            return p_getValidProperty_3_;
         } else {
            Properties properties = new Properties();
            properties.load(inputstream);
            inputstream.close();
            String s = properties.getProperty(p_getValidProperty_1_);
            if(s == null) {
               return p_getValidProperty_3_;
            } else {
               List<String> list = Arrays.<String>asList(p_getValidProperty_2_);
               if(!list.contains(s)) {
                  warn("Invalid value: " + p_getValidProperty_1_ + "=" + s);
                  warn("Expected values: " + Config.arrayToString((Object[])p_getValidProperty_2_));
                  return p_getValidProperty_3_;
               } else {
                  dbg("" + p_getValidProperty_1_ + "=" + s);
                  return s;
               }
            }
         }
      } catch (FileNotFoundException var9) {
         return p_getValidProperty_3_;
      } catch (IOException ioexception) {
         ioexception.printStackTrace();
         return p_getValidProperty_3_;
      }
   }

   private static Pair<LightMapPack[], Integer> parseLightMapPacks() {
      String s = "mcpatcher/lightmap/world";
      String s1 = ".png";
      String[] astring = ResUtils.collectFiles(s, s1);
      Map<Integer, String> map = new HashMap();

      for(int i = 0; i < astring.length; ++i) {
         String s2 = astring[i];
         String s3 = StrUtils.removePrefixSuffix(s2, s, s1);
         int j = Config.parseInt(s3, Integer.MIN_VALUE);
         if(j == Integer.MIN_VALUE) {
            warn("Invalid dimension ID: " + s3 + ", path: " + s2);
         } else {
            map.put(Integer.valueOf(j), s2);
         }
      }

      Set<Integer> set = map.keySet();
      Integer[] ainteger = (Integer[])set.toArray(new Integer[set.size()]);
      Arrays.sort((Object[])ainteger);
      if(ainteger.length <= 0) {
         return new ImmutablePair((Object)null, Integer.valueOf(0));
      } else {
         int j1 = ainteger[0].intValue();
         int k1 = ainteger[ainteger.length - 1].intValue();
         int k = k1 - j1 + 1;
         CustomColormap[] acustomcolormap = new CustomColormap[k];

         for(int l = 0; l < ainteger.length; ++l) {
            Integer integer = ainteger[l];
            String s4 = (String)map.get(integer);
            CustomColormap customcolormap = getCustomColors(s4, -1, -1);
            if(customcolormap != null) {
               if(customcolormap.getWidth() < 16) {
                  warn("Invalid lightmap width: " + customcolormap.getWidth() + ", path: " + s4);
               } else {
                  int i1 = integer.intValue() - j1;
                  acustomcolormap[i1] = customcolormap;
               }
            }
         }

         LightMapPack[] alightmappack = new LightMapPack[acustomcolormap.length];

         for(int l1 = 0; l1 < acustomcolormap.length; ++l1) {
            CustomColormap customcolormap3 = acustomcolormap[l1];
            if(customcolormap3 != null) {
               String s5 = customcolormap3.name;
               String s6 = customcolormap3.basePath;
               CustomColormap customcolormap1 = getCustomColors(s6 + "/" + s5 + "_rain.png", -1, -1);
               CustomColormap customcolormap2 = getCustomColors(s6 + "/" + s5 + "_thunder.png", -1, -1);
               LightMap lightmap = new LightMap(customcolormap3);
               LightMap lightmap1 = customcolormap1 != null?new LightMap(customcolormap1):null;
               LightMap lightmap2 = customcolormap2 != null?new LightMap(customcolormap2):null;
               LightMapPack lightmappack = new LightMapPack(lightmap, lightmap1, lightmap2);
               alightmappack[l1] = lightmappack;
            }
         }

         return new ImmutablePair(alightmappack, Integer.valueOf(j1));
      }
   }

   private static int getTextureHeight(String p_getTextureHeight_0_, int p_getTextureHeight_1_) {
      try {
         InputStream inputstream = Config.getResourceStream(new ResourceLocation(p_getTextureHeight_0_));
         if(inputstream == null) {
            return p_getTextureHeight_1_;
         } else {
            BufferedImage bufferedimage = ImageIO.read(inputstream);
            inputstream.close();
            return bufferedimage == null?p_getTextureHeight_1_:bufferedimage.getHeight();
         }
      } catch (IOException var4) {
         return p_getTextureHeight_1_;
      }
   }

   private static void readColorProperties(String p_readColorProperties_0_) {
      try {
         ResourceLocation resourcelocation = new ResourceLocation(p_readColorProperties_0_);
         InputStream inputstream = Config.getResourceStream(resourcelocation);
         if(inputstream == null) {
            return;
         }

         dbg("Loading " + p_readColorProperties_0_);
         Properties properties = new Properties();
         properties.load(inputstream);
         inputstream.close();
         particleWaterColor = readColor(properties, new String[]{"particle.water", "drop.water"});
         particlePortalColor = readColor(properties, "particle.portal");
         lilyPadColor = readColor(properties, "lilypad");
         expBarTextColor = readColor(properties, "text.xpbar");
         bossTextColor = readColor(properties, "text.boss");
         signTextColor = readColor(properties, "text.sign");
         fogColorNether = readColorVec3(properties, "fog.nether");
         fogColorEnd = readColorVec3(properties, "fog.end");
         skyColorEnd = readColorVec3(properties, "sky.end");
         colorsBlockColormaps = readCustomColormaps(properties, p_readColorProperties_0_);
         spawnEggPrimaryColors = readSpawnEggColors(properties, p_readColorProperties_0_, "egg.shell.", "Spawn egg shell");
         spawnEggSecondaryColors = readSpawnEggColors(properties, p_readColorProperties_0_, "egg.spots.", "Spawn egg spot");
         wolfCollarColors = readDyeColors(properties, p_readColorProperties_0_, "collar.", "Wolf collar");
         sheepColors = readDyeColors(properties, p_readColorProperties_0_, "sheep.", "Sheep");
         textColors = readTextColors(properties, p_readColorProperties_0_, "text.code.", "Text");
         int[] aint = readMapColors(properties, p_readColorProperties_0_, "map.", "Map");
         if(aint != null) {
            if(mapColorsOriginal == null) {
               mapColorsOriginal = getMapColors();
            }

            setMapColors(aint);
         }

         potionColors = readPotionColors(properties, p_readColorProperties_0_, "potion.", "Potion");
         xpOrbTime = Config.parseInt(properties.getProperty("xporb.time"), -1);
      } catch (FileNotFoundException var5) {
         return;
      } catch (IOException ioexception) {
         ioexception.printStackTrace();
      }

   }

   private static CustomColormap[] readCustomColormaps(Properties p_readCustomColormaps_0_, String p_readCustomColormaps_1_) {
      List list = new ArrayList();
      String s = "palette.block.";
      Map map = new HashMap();

      for(String s1 : p_readCustomColormaps_0_.keySet()) {
         String s2 = p_readCustomColormaps_0_.getProperty(s1);
         if(s1.startsWith(s)) {
            map.put(s1, s2);
         }
      }

      String[] astring = (String[])((String[])map.keySet().toArray(new String[map.size()]));

      for(int j = 0; j < astring.length; ++j) {
         String s6 = astring[j];
         String s3 = p_readCustomColormaps_0_.getProperty(s6);
         dbg("Block palette: " + s6 + " = " + s3);
         String s4 = s6.substring(s.length());
         String s5 = TextureUtils.getBasePath(p_readCustomColormaps_1_);
         s4 = TextureUtils.fixResourcePath(s4, s5);
         CustomColormap customcolormap = getCustomColors(s4, 256, 256);
         if(customcolormap == null) {
            warn("Colormap not found: " + s4);
         } else {
            ConnectedParser connectedparser = new ConnectedParser("CustomColors");
            MatchBlock[] amatchblock = connectedparser.parseMatchBlocks(s3);
            if(amatchblock != null && amatchblock.length > 0) {
               for(int i = 0; i < amatchblock.length; ++i) {
                  MatchBlock matchblock = amatchblock[i];
                  customcolormap.addMatchBlock(matchblock);
               }

               list.add(customcolormap);
            } else {
               warn("Invalid match blocks: " + s3);
            }
         }
      }

      if(list.size() <= 0) {
         return null;
      } else {
         CustomColormap[] acustomcolormap = (CustomColormap[])((CustomColormap[])list.toArray(new CustomColormap[list.size()]));
         return acustomcolormap;
      }
   }

   private static CustomColormap[][] readBlockColormaps(String[] p_readBlockColormaps_0_, CustomColormap[] p_readBlockColormaps_1_, int p_readBlockColormaps_2_, int p_readBlockColormaps_3_) {
      String[] astring = ResUtils.collectFiles(p_readBlockColormaps_0_, new String[]{".properties"});
      Arrays.sort((Object[])astring);
      List list = new ArrayList();

      for(int i = 0; i < astring.length; ++i) {
         String s = astring[i];
         dbg("Block colormap: " + s);

         try {
            ResourceLocation resourcelocation = new ResourceLocation("minecraft", s);
            InputStream inputstream = Config.getResourceStream(resourcelocation);
            if(inputstream == null) {
               warn("File not found: " + s);
            } else {
               Properties properties = new Properties();
               properties.load(inputstream);
               CustomColormap customcolormap = new CustomColormap(properties, s, p_readBlockColormaps_2_, p_readBlockColormaps_3_, paletteFormatDefault);
               if(customcolormap.isValid(s) && customcolormap.isValidMatchBlocks(s)) {
                  addToBlockList(customcolormap, list);
               }
            }
         } catch (FileNotFoundException var12) {
            warn("File not found: " + s);
         } catch (Exception exception) {
            exception.printStackTrace();
         }
      }

      if(p_readBlockColormaps_1_ != null) {
         for(int j = 0; j < p_readBlockColormaps_1_.length; ++j) {
            CustomColormap customcolormap1 = p_readBlockColormaps_1_[j];
            addToBlockList(customcolormap1, list);
         }
      }

      if(list.size() <= 0) {
         return (CustomColormap[][])null;
      } else {
         CustomColormap[][] acustomcolormap = blockListToArray(list);
         return acustomcolormap;
      }
   }

   private static void addToBlockList(CustomColormap p_addToBlockList_0_, List p_addToBlockList_1_) {
      int[] aint = p_addToBlockList_0_.getMatchBlockIds();
      if(aint != null && aint.length > 0) {
         for(int i = 0; i < aint.length; ++i) {
            int j = aint[i];
            if(j < 0) {
               warn("Invalid block ID: " + j);
            } else {
               addToList(p_addToBlockList_0_, p_addToBlockList_1_, j);
            }
         }

      } else {
         warn("No match blocks: " + Config.arrayToString(aint));
      }
   }

   private static void addToList(CustomColormap p_addToList_0_, List p_addToList_1_, int p_addToList_2_) {
      while(p_addToList_2_ >= p_addToList_1_.size()) {
         p_addToList_1_.add(null);
      }

      List list = (List)p_addToList_1_.get(p_addToList_2_);
      if(list == null) {
         list = new ArrayList();
         p_addToList_1_.set(p_addToList_2_, list);
      }

      list.add(p_addToList_0_);
   }

   private static CustomColormap[][] blockListToArray(List p_blockListToArray_0_) {
      CustomColormap[][] acustomcolormap = new CustomColormap[p_blockListToArray_0_.size()][];

      for(int i = 0; i < p_blockListToArray_0_.size(); ++i) {
         List list = (List)p_blockListToArray_0_.get(i);
         if(list != null) {
            CustomColormap[] acustomcolormap1 = (CustomColormap[])((CustomColormap[])list.toArray(new CustomColormap[list.size()]));
            acustomcolormap[i] = acustomcolormap1;
         }
      }

      return acustomcolormap;
   }

   private static int readColor(Properties p_readColor_0_, String[] p_readColor_1_) {
      for(int i = 0; i < p_readColor_1_.length; ++i) {
         String s = p_readColor_1_[i];
         int j = readColor(p_readColor_0_, s);
         if(j >= 0) {
            return j;
         }
      }

      return -1;
   }

   private static int readColor(Properties p_readColor_0_, String p_readColor_1_) {
      String s = p_readColor_0_.getProperty(p_readColor_1_);
      if(s == null) {
         return -1;
      } else {
         s = s.trim();
         int i = parseColor(s);
         if(i < 0) {
            warn("Invalid color: " + p_readColor_1_ + " = " + s);
            return i;
         } else {
            dbg(p_readColor_1_ + " = " + s);
            return i;
         }
      }
   }

   private static int parseColor(String p_parseColor_0_) {
      if(p_parseColor_0_ == null) {
         return -1;
      } else {
         p_parseColor_0_ = p_parseColor_0_.trim();

         try {
            int i = Integer.parseInt(p_parseColor_0_, 16) & 16777215;
            return i;
         } catch (NumberFormatException var2) {
            return -1;
         }
      }
   }

   private static Vec3 readColorVec3(Properties p_readColorVec3_0_, String p_readColorVec3_1_) {
      int i = readColor(p_readColorVec3_0_, p_readColorVec3_1_);
      if(i < 0) {
         return null;
      } else {
         int j = i >> 16 & 255;
         int k = i >> 8 & 255;
         int l = i & 255;
         float f = (float)j / 255.0F;
         float f1 = (float)k / 255.0F;
         float f2 = (float)l / 255.0F;
         return new Vec3((double)f, (double)f1, (double)f2);
      }
   }

   private static CustomColormap getCustomColors(String p_getCustomColors_0_, String[] p_getCustomColors_1_, int p_getCustomColors_2_, int p_getCustomColors_3_) {
      for(int i = 0; i < p_getCustomColors_1_.length; ++i) {
         String s = p_getCustomColors_1_[i];
         s = p_getCustomColors_0_ + s;
         CustomColormap customcolormap = getCustomColors(s, p_getCustomColors_2_, p_getCustomColors_3_);
         if(customcolormap != null) {
            return customcolormap;
         }
      }

      return null;
   }

   public static CustomColormap getCustomColors(String p_getCustomColors_0_, int p_getCustomColors_1_, int p_getCustomColors_2_) {
      try {
         ResourceLocation resourcelocation = new ResourceLocation(p_getCustomColors_0_);
         if(!Config.hasResource(resourcelocation)) {
            return null;
         } else {
            dbg("Colormap " + p_getCustomColors_0_);
            Properties properties = new Properties();
            String s = StrUtils.replaceSuffix(p_getCustomColors_0_, ".png", ".properties");
            ResourceLocation resourcelocation1 = new ResourceLocation(s);
            if(Config.hasResource(resourcelocation1)) {
               InputStream inputstream = Config.getResourceStream(resourcelocation1);
               properties.load(inputstream);
               inputstream.close();
               dbg("Colormap properties: " + s);
            } else {
               properties.put("format", paletteFormatDefault);
               properties.put("source", p_getCustomColors_0_);
               s = p_getCustomColors_0_;
            }

            CustomColormap customcolormap = new CustomColormap(properties, s, p_getCustomColors_1_, p_getCustomColors_2_, paletteFormatDefault);
            return !customcolormap.isValid(s)?null:customcolormap;
         }
      } catch (Exception exception) {
         exception.printStackTrace();
         return null;
      }
   }

   public static void updateUseDefaultGrassFoliageColors() {
      useDefaultGrassFoliageColors = foliageBirchColors == null && foliagePineColors == null && swampGrassColors == null && swampFoliageColors == null && Config.isSwampColors() && Config.isSmoothBiomes();
   }

   public static int getColorMultiplier(BakedQuad p_getColorMultiplier_0_, Block p_getColorMultiplier_1_, IBlockAccess p_getColorMultiplier_2_, BlockPos p_getColorMultiplier_3_, RenderEnv p_getColorMultiplier_4_) {
      if(blockColormaps != null) {
         IBlockState iblockstate = p_getColorMultiplier_4_.getBlockState();
         if(!p_getColorMultiplier_0_.func_178212_b()) {
            if(p_getColorMultiplier_1_ == Blocks.field_150349_c) {
               iblockstate = BLOCK_STATE_DIRT;
            }

            if(p_getColorMultiplier_1_ == Blocks.field_150488_af) {
               return -1;
            }
         }

         if(p_getColorMultiplier_1_ == Blocks.field_150398_cm && p_getColorMultiplier_4_.getMetadata() >= 8) {
            p_getColorMultiplier_3_ = p_getColorMultiplier_3_.func_177977_b();
            iblockstate = p_getColorMultiplier_2_.func_180495_p(p_getColorMultiplier_3_);
         }

         CustomColormap customcolormap = getBlockColormap(iblockstate);
         if(customcolormap != null) {
            if(Config.isSmoothBiomes() && !customcolormap.isColorConstant()) {
               return getSmoothColorMultiplier(p_getColorMultiplier_2_, p_getColorMultiplier_3_, customcolormap, p_getColorMultiplier_4_.getColorizerBlockPosM());
            }

            return customcolormap.getColor(p_getColorMultiplier_2_, p_getColorMultiplier_3_);
         }
      }

      if(!p_getColorMultiplier_0_.func_178212_b()) {
         return -1;
      } else if(p_getColorMultiplier_1_ == Blocks.field_150392_bi) {
         return getLilypadColorMultiplier(p_getColorMultiplier_2_, p_getColorMultiplier_3_);
      } else if(p_getColorMultiplier_1_ == Blocks.field_150488_af) {
         return getRedstoneColor(p_getColorMultiplier_4_.getBlockState());
      } else if(p_getColorMultiplier_1_ instanceof BlockStem) {
         return getStemColorMultiplier(p_getColorMultiplier_1_, p_getColorMultiplier_2_, p_getColorMultiplier_3_, p_getColorMultiplier_4_);
      } else if(useDefaultGrassFoliageColors) {
         return -1;
      } else {
         int i = p_getColorMultiplier_4_.getMetadata();
         CustomColors.IColorizer customcolors$icolorizer;
         if(p_getColorMultiplier_1_ != Blocks.field_150349_c && p_getColorMultiplier_1_ != Blocks.field_150329_H && p_getColorMultiplier_1_ != Blocks.field_150398_cm) {
            if(p_getColorMultiplier_1_ == Blocks.field_150398_cm) {
               customcolors$icolorizer = COLORIZER_GRASS;
               if(i >= 8) {
                  p_getColorMultiplier_3_ = p_getColorMultiplier_3_.func_177977_b();
               }
            } else if(p_getColorMultiplier_1_ == Blocks.field_150362_t) {
               switch(i & 3) {
               case 0:
                  customcolors$icolorizer = COLORIZER_FOLIAGE;
                  break;
               case 1:
                  customcolors$icolorizer = COLORIZER_FOLIAGE_PINE;
                  break;
               case 2:
                  customcolors$icolorizer = COLORIZER_FOLIAGE_BIRCH;
                  break;
               default:
                  customcolors$icolorizer = COLORIZER_FOLIAGE;
               }
            } else if(p_getColorMultiplier_1_ == Blocks.field_150361_u) {
               customcolors$icolorizer = COLORIZER_FOLIAGE;
            } else {
               if(p_getColorMultiplier_1_ != Blocks.field_150395_bd) {
                  return -1;
               }

               customcolors$icolorizer = COLORIZER_FOLIAGE;
            }
         } else {
            customcolors$icolorizer = COLORIZER_GRASS;
         }

         return Config.isSmoothBiomes() && !customcolors$icolorizer.isColorConstant()?getSmoothColorMultiplier(p_getColorMultiplier_2_, p_getColorMultiplier_3_, customcolors$icolorizer, p_getColorMultiplier_4_.getColorizerBlockPosM()):customcolors$icolorizer.getColor(p_getColorMultiplier_2_, p_getColorMultiplier_3_);
      }
   }

   protected static BiomeGenBase getColorBiome(IBlockAccess p_getColorBiome_0_, BlockPos p_getColorBiome_1_) {
      BiomeGenBase biomegenbase = p_getColorBiome_0_.func_180494_b(p_getColorBiome_1_);
      if(biomegenbase == BiomeGenBase.field_76780_h && !Config.isSwampColors()) {
         biomegenbase = BiomeGenBase.field_76772_c;
      }

      return biomegenbase;
   }

   private static CustomColormap getBlockColormap(IBlockState p_getBlockColormap_0_) {
      if(blockColormaps == null) {
         return null;
      } else if(!(p_getBlockColormap_0_ instanceof BlockStateBase)) {
         return null;
      } else {
         BlockStateBase blockstatebase = (BlockStateBase)p_getBlockColormap_0_;
         int i = blockstatebase.getBlockId();
         if(i >= 0 && i < blockColormaps.length) {
            CustomColormap[] acustomcolormap = blockColormaps[i];
            if(acustomcolormap == null) {
               return null;
            } else {
               for(int j = 0; j < acustomcolormap.length; ++j) {
                  CustomColormap customcolormap = acustomcolormap[j];
                  if(customcolormap.matchesBlock(blockstatebase)) {
                     return customcolormap;
                  }
               }

               return null;
            }
         } else {
            return null;
         }
      }
   }

   private static int getSmoothColorMultiplier(IBlockAccess p_getSmoothColorMultiplier_0_, BlockPos p_getSmoothColorMultiplier_1_, CustomColors.IColorizer p_getSmoothColorMultiplier_2_, BlockPosM p_getSmoothColorMultiplier_3_) {
      int i = 0;
      int j = 0;
      int k = 0;
      int l = p_getSmoothColorMultiplier_1_.func_177958_n();
      int i1 = p_getSmoothColorMultiplier_1_.func_177956_o();
      int j1 = p_getSmoothColorMultiplier_1_.func_177952_p();
      BlockPosM blockposm = p_getSmoothColorMultiplier_3_;

      for(int k1 = l - 1; k1 <= l + 1; ++k1) {
         for(int l1 = j1 - 1; l1 <= j1 + 1; ++l1) {
            blockposm.setXyz(k1, i1, l1);
            int i2 = p_getSmoothColorMultiplier_2_.getColor(p_getSmoothColorMultiplier_0_, blockposm);
            i += i2 >> 16 & 255;
            j += i2 >> 8 & 255;
            k += i2 & 255;
         }
      }

      int j2 = i / 9;
      int k2 = j / 9;
      int l2 = k / 9;
      return j2 << 16 | k2 << 8 | l2;
   }

   public static int getFluidColor(IBlockAccess p_getFluidColor_0_, IBlockState p_getFluidColor_1_, BlockPos p_getFluidColor_2_, RenderEnv p_getFluidColor_3_) {
      Block block = p_getFluidColor_1_.func_177230_c();
      CustomColors.IColorizer customcolors$icolorizer = getBlockColormap(p_getFluidColor_1_);
      if(customcolors$icolorizer == null && block.func_149688_o() == Material.field_151586_h) {
         customcolors$icolorizer = COLORIZER_WATER;
      }

      return customcolors$icolorizer == null?block.func_176202_d(p_getFluidColor_0_, p_getFluidColor_2_):(Config.isSmoothBiomes() && !customcolors$icolorizer.isColorConstant()?getSmoothColorMultiplier(p_getFluidColor_0_, p_getFluidColor_2_, customcolors$icolorizer, p_getFluidColor_3_.getColorizerBlockPosM()):customcolors$icolorizer.getColor(p_getFluidColor_0_, p_getFluidColor_2_));
   }

   public static void updatePortalFX(EntityFX p_updatePortalFX_0_) {
      if(particlePortalColor >= 0) {
         int i = particlePortalColor;
         int j = i >> 16 & 255;
         int k = i >> 8 & 255;
         int l = i & 255;
         float f = (float)j / 255.0F;
         float f1 = (float)k / 255.0F;
         float f2 = (float)l / 255.0F;
         p_updatePortalFX_0_.func_70538_b(f, f1, f2);
      }
   }

   public static void updateMyceliumFX(EntityFX p_updateMyceliumFX_0_) {
      if(myceliumParticleColors != null) {
         int i = myceliumParticleColors.getColorRandom();
         int j = i >> 16 & 255;
         int k = i >> 8 & 255;
         int l = i & 255;
         float f = (float)j / 255.0F;
         float f1 = (float)k / 255.0F;
         float f2 = (float)l / 255.0F;
         p_updateMyceliumFX_0_.func_70538_b(f, f1, f2);
      }
   }

   private static int getRedstoneColor(IBlockState p_getRedstoneColor_0_) {
      if(redstoneColors == null) {
         return -1;
      } else {
         int i = getRedstoneLevel(p_getRedstoneColor_0_, 15);
         int j = redstoneColors.getColor(i);
         return j;
      }
   }

   public static void updateReddustFX(EntityFX p_updateReddustFX_0_, IBlockAccess p_updateReddustFX_1_, double p_updateReddustFX_2_, double p_updateReddustFX_4_, double p_updateReddustFX_6_) {
      if(redstoneColors != null) {
         IBlockState iblockstate = p_updateReddustFX_1_.func_180495_p(new BlockPos(p_updateReddustFX_2_, p_updateReddustFX_4_, p_updateReddustFX_6_));
         int i = getRedstoneLevel(iblockstate, 15);
         int j = redstoneColors.getColor(i);
         int k = j >> 16 & 255;
         int l = j >> 8 & 255;
         int i1 = j & 255;
         float f = (float)k / 255.0F;
         float f1 = (float)l / 255.0F;
         float f2 = (float)i1 / 255.0F;
         p_updateReddustFX_0_.func_70538_b(f, f1, f2);
      }
   }

   private static int getRedstoneLevel(IBlockState p_getRedstoneLevel_0_, int p_getRedstoneLevel_1_) {
      Block block = p_getRedstoneLevel_0_.func_177230_c();
      if(!(block instanceof BlockRedstoneWire)) {
         return p_getRedstoneLevel_1_;
      } else {
         Object object = p_getRedstoneLevel_0_.func_177229_b(BlockRedstoneWire.field_176351_O);
         if(!(object instanceof Integer)) {
            return p_getRedstoneLevel_1_;
         } else {
            Integer integer = (Integer)object;
            return integer.intValue();
         }
      }
   }

   public static float getXpOrbTimer(float p_getXpOrbTimer_0_) {
      if(xpOrbTime <= 0) {
         return p_getXpOrbTimer_0_;
      } else {
         float f = 628.0F / (float)xpOrbTime;
         return p_getXpOrbTimer_0_ * f;
      }
   }

   public static int getXpOrbColor(float p_getXpOrbColor_0_) {
      if(xpOrbColors == null) {
         return -1;
      } else {
         int i = (int)Math.round((double)((MathHelper.func_76126_a(p_getXpOrbColor_0_) + 1.0F) * (float)(xpOrbColors.getLength() - 1)) / 2.0D);
         int j = xpOrbColors.getColor(i);
         return j;
      }
   }

   public static int getDurabilityColor(int p_getDurabilityColor_0_) {
      if(durabilityColors == null) {
         return -1;
      } else {
         int i = p_getDurabilityColor_0_ * durabilityColors.getLength() / 255;
         int j = durabilityColors.getColor(i);
         return j;
      }
   }

   public static void updateWaterFX(EntityFX p_updateWaterFX_0_, IBlockAccess p_updateWaterFX_1_, double p_updateWaterFX_2_, double p_updateWaterFX_4_, double p_updateWaterFX_6_) {
      if(waterColors != null || blockColormaps != null) {
         BlockPos blockpos = new BlockPos(p_updateWaterFX_2_, p_updateWaterFX_4_, p_updateWaterFX_6_);
         RenderEnv renderenv = RenderEnv.getInstance(p_updateWaterFX_1_, BLOCK_STATE_WATER, blockpos);
         int i = getFluidColor(p_updateWaterFX_1_, BLOCK_STATE_WATER, blockpos, renderenv);
         int j = i >> 16 & 255;
         int k = i >> 8 & 255;
         int l = i & 255;
         float f = (float)j / 255.0F;
         float f1 = (float)k / 255.0F;
         float f2 = (float)l / 255.0F;
         if(particleWaterColor >= 0) {
            int i1 = particleWaterColor >> 16 & 255;
            int j1 = particleWaterColor >> 8 & 255;
            int k1 = particleWaterColor & 255;
            f *= (float)i1 / 255.0F;
            f1 *= (float)j1 / 255.0F;
            f2 *= (float)k1 / 255.0F;
         }

         p_updateWaterFX_0_.func_70538_b(f, f1, f2);
      }
   }

   private static int getLilypadColorMultiplier(IBlockAccess p_getLilypadColorMultiplier_0_, BlockPos p_getLilypadColorMultiplier_1_) {
      return lilyPadColor < 0?Blocks.field_150392_bi.func_176202_d(p_getLilypadColorMultiplier_0_, p_getLilypadColorMultiplier_1_):lilyPadColor;
   }

   private static Vec3 getFogColorNether(Vec3 p_getFogColorNether_0_) {
      return fogColorNether == null?p_getFogColorNether_0_:fogColorNether;
   }

   private static Vec3 getFogColorEnd(Vec3 p_getFogColorEnd_0_) {
      return fogColorEnd == null?p_getFogColorEnd_0_:fogColorEnd;
   }

   private static Vec3 getSkyColorEnd(Vec3 p_getSkyColorEnd_0_) {
      return skyColorEnd == null?p_getSkyColorEnd_0_:skyColorEnd;
   }

   public static Vec3 getSkyColor(Vec3 p_getSkyColor_0_, IBlockAccess p_getSkyColor_1_, double p_getSkyColor_2_, double p_getSkyColor_4_, double p_getSkyColor_6_) {
      if(skyColors == null) {
         return p_getSkyColor_0_;
      } else {
         int i = skyColors.getColorSmooth(p_getSkyColor_1_, p_getSkyColor_2_, p_getSkyColor_4_, p_getSkyColor_6_, 3);
         int j = i >> 16 & 255;
         int k = i >> 8 & 255;
         int l = i & 255;
         float f = (float)j / 255.0F;
         float f1 = (float)k / 255.0F;
         float f2 = (float)l / 255.0F;
         float f3 = (float)p_getSkyColor_0_.field_72450_a / 0.5F;
         float f4 = (float)p_getSkyColor_0_.field_72448_b / 0.66275F;
         float f5 = (float)p_getSkyColor_0_.field_72449_c;
         f = f * f3;
         f1 = f1 * f4;
         f2 = f2 * f5;
         Vec3 vec3 = skyColorFader.getColor((double)f, (double)f1, (double)f2);
         return vec3;
      }
   }

   private static Vec3 getFogColor(Vec3 p_getFogColor_0_, IBlockAccess p_getFogColor_1_, double p_getFogColor_2_, double p_getFogColor_4_, double p_getFogColor_6_) {
      if(fogColors == null) {
         return p_getFogColor_0_;
      } else {
         int i = fogColors.getColorSmooth(p_getFogColor_1_, p_getFogColor_2_, p_getFogColor_4_, p_getFogColor_6_, 3);
         int j = i >> 16 & 255;
         int k = i >> 8 & 255;
         int l = i & 255;
         float f = (float)j / 255.0F;
         float f1 = (float)k / 255.0F;
         float f2 = (float)l / 255.0F;
         float f3 = (float)p_getFogColor_0_.field_72450_a / 0.753F;
         float f4 = (float)p_getFogColor_0_.field_72448_b / 0.8471F;
         float f5 = (float)p_getFogColor_0_.field_72449_c;
         f = f * f3;
         f1 = f1 * f4;
         f2 = f2 * f5;
         Vec3 vec3 = fogColorFader.getColor((double)f, (double)f1, (double)f2);
         return vec3;
      }
   }

   public static Vec3 getUnderwaterColor(IBlockAccess p_getUnderwaterColor_0_, double p_getUnderwaterColor_1_, double p_getUnderwaterColor_3_, double p_getUnderwaterColor_5_) {
      return getUnderFluidColor(p_getUnderwaterColor_0_, p_getUnderwaterColor_1_, p_getUnderwaterColor_3_, p_getUnderwaterColor_5_, underwaterColors, underwaterColorFader);
   }

   public static Vec3 getUnderlavaColor(IBlockAccess p_getUnderlavaColor_0_, double p_getUnderlavaColor_1_, double p_getUnderlavaColor_3_, double p_getUnderlavaColor_5_) {
      return getUnderFluidColor(p_getUnderlavaColor_0_, p_getUnderlavaColor_1_, p_getUnderlavaColor_3_, p_getUnderlavaColor_5_, underlavaColors, underlavaColorFader);
   }

   public static Vec3 getUnderFluidColor(IBlockAccess p_getUnderFluidColor_0_, double p_getUnderFluidColor_1_, double p_getUnderFluidColor_3_, double p_getUnderFluidColor_5_, CustomColormap p_getUnderFluidColor_7_, CustomColorFader p_getUnderFluidColor_8_) {
      if(p_getUnderFluidColor_7_ == null) {
         return null;
      } else {
         int i = p_getUnderFluidColor_7_.getColorSmooth(p_getUnderFluidColor_0_, p_getUnderFluidColor_1_, p_getUnderFluidColor_3_, p_getUnderFluidColor_5_, 3);
         int j = i >> 16 & 255;
         int k = i >> 8 & 255;
         int l = i & 255;
         float f = (float)j / 255.0F;
         float f1 = (float)k / 255.0F;
         float f2 = (float)l / 255.0F;
         Vec3 vec3 = p_getUnderFluidColor_8_.getColor((double)f, (double)f1, (double)f2);
         return vec3;
      }
   }

   private static int getStemColorMultiplier(Block p_getStemColorMultiplier_0_, IBlockAccess p_getStemColorMultiplier_1_, BlockPos p_getStemColorMultiplier_2_, RenderEnv p_getStemColorMultiplier_3_) {
      CustomColormap customcolormap = stemColors;
      if(p_getStemColorMultiplier_0_ == Blocks.field_150393_bb && stemPumpkinColors != null) {
         customcolormap = stemPumpkinColors;
      }

      if(p_getStemColorMultiplier_0_ == Blocks.field_150394_bc && stemMelonColors != null) {
         customcolormap = stemMelonColors;
      }

      if(customcolormap == null) {
         return -1;
      } else {
         int i = p_getStemColorMultiplier_3_.getMetadata();
         return customcolormap.getColor(i);
      }
   }

   public static boolean updateLightmap(World p_updateLightmap_0_, float p_updateLightmap_1_, int[] p_updateLightmap_2_, boolean p_updateLightmap_3_, float p_updateLightmap_4_) {
      if(p_updateLightmap_0_ == null) {
         return false;
      } else if(lightMapPacks == null) {
         return false;
      } else {
         int i = p_updateLightmap_0_.field_73011_w.func_177502_q();
         int j = i - lightmapMinDimensionId;
         if(j >= 0 && j < lightMapPacks.length) {
            LightMapPack lightmappack = lightMapPacks[j];
            return lightmappack == null?false:lightmappack.updateLightmap(p_updateLightmap_0_, p_updateLightmap_1_, p_updateLightmap_2_, p_updateLightmap_3_, p_updateLightmap_4_);
         } else {
            return false;
         }
      }
   }

   public static Vec3 getWorldFogColor(Vec3 p_getWorldFogColor_0_, WorldClient p_getWorldFogColor_1_, Entity p_getWorldFogColor_2_, float p_getWorldFogColor_3_) {
      int i = p_getWorldFogColor_1_.field_73011_w.func_177502_q();
      switch(i) {
      case -1:
         p_getWorldFogColor_0_ = getFogColorNether(p_getWorldFogColor_0_);
         break;
      case 0:
         Minecraft minecraft = Minecraft.func_71410_x();
         p_getWorldFogColor_0_ = getFogColor(p_getWorldFogColor_0_, minecraft.field_71441_e, p_getWorldFogColor_2_.field_70165_t, p_getWorldFogColor_2_.field_70163_u + 1.0D, p_getWorldFogColor_2_.field_70161_v);
         break;
      case 1:
         p_getWorldFogColor_0_ = getFogColorEnd(p_getWorldFogColor_0_);
      }

      return p_getWorldFogColor_0_;
   }

   public static Vec3 getWorldSkyColor(Vec3 p_getWorldSkyColor_0_, World p_getWorldSkyColor_1_, Entity p_getWorldSkyColor_2_, float p_getWorldSkyColor_3_) {
      int i = p_getWorldSkyColor_1_.field_73011_w.func_177502_q();
      switch(i) {
      case 0:
         Minecraft minecraft = Minecraft.func_71410_x();
         p_getWorldSkyColor_0_ = getSkyColor(p_getWorldSkyColor_0_, minecraft.field_71441_e, p_getWorldSkyColor_2_.field_70165_t, p_getWorldSkyColor_2_.field_70163_u + 1.0D, p_getWorldSkyColor_2_.field_70161_v);
         break;
      case 1:
         p_getWorldSkyColor_0_ = getSkyColorEnd(p_getWorldSkyColor_0_);
      }

      return p_getWorldSkyColor_0_;
   }

   private static int[] readSpawnEggColors(Properties p_readSpawnEggColors_0_, String p_readSpawnEggColors_1_, String p_readSpawnEggColors_2_, String p_readSpawnEggColors_3_) {
      List<Integer> list = new ArrayList();
      Set set = p_readSpawnEggColors_0_.keySet();
      int i = 0;

      for(String s : set) {
         String s1 = p_readSpawnEggColors_0_.getProperty(s);
         if(s.startsWith(p_readSpawnEggColors_2_)) {
            String s2 = StrUtils.removePrefix(s, p_readSpawnEggColors_2_);
            int j = getEntityId(s2);
            int k = parseColor(s1);
            if(j >= 0 && k >= 0) {
               while(((List)list).size() <= j) {
                  list.add(Integer.valueOf(-1));
               }

               list.set(j, Integer.valueOf(k));
               ++i;
            } else {
               warn("Invalid spawn egg color: " + s + " = " + s1);
            }
         }
      }

      if(i <= 0) {
         return null;
      } else {
         dbg(p_readSpawnEggColors_3_ + " colors: " + i);
         int[] aint = new int[list.size()];

         for(int l = 0; l < aint.length; ++l) {
            aint[l] = ((Integer)list.get(l)).intValue();
         }

         return aint;
      }
   }

   private static int getSpawnEggColor(ItemMonsterPlacer p_getSpawnEggColor_0_, ItemStack p_getSpawnEggColor_1_, int p_getSpawnEggColor_2_, int p_getSpawnEggColor_3_) {
      int i = p_getSpawnEggColor_1_.func_77960_j();
      int[] aint = p_getSpawnEggColor_2_ == 0?spawnEggPrimaryColors:spawnEggSecondaryColors;
      if(aint == null) {
         return p_getSpawnEggColor_3_;
      } else if(i >= 0 && i < aint.length) {
         int j = aint[i];
         return j < 0?p_getSpawnEggColor_3_:j;
      } else {
         return p_getSpawnEggColor_3_;
      }
   }

   public static int getColorFromItemStack(ItemStack p_getColorFromItemStack_0_, int p_getColorFromItemStack_1_, int p_getColorFromItemStack_2_) {
      if(p_getColorFromItemStack_0_ == null) {
         return p_getColorFromItemStack_2_;
      } else {
         Item item = p_getColorFromItemStack_0_.func_77973_b();
         return item == null?p_getColorFromItemStack_2_:(item instanceof ItemMonsterPlacer?getSpawnEggColor((ItemMonsterPlacer)item, p_getColorFromItemStack_0_, p_getColorFromItemStack_1_, p_getColorFromItemStack_2_):p_getColorFromItemStack_2_);
      }
   }

   private static float[][] readDyeColors(Properties p_readDyeColors_0_, String p_readDyeColors_1_, String p_readDyeColors_2_, String p_readDyeColors_3_) {
      EnumDyeColor[] aenumdyecolor = EnumDyeColor.values();
      Map<String, EnumDyeColor> map = new HashMap();

      for(int i = 0; i < aenumdyecolor.length; ++i) {
         EnumDyeColor enumdyecolor = aenumdyecolor[i];
         map.put(enumdyecolor.func_176610_l(), enumdyecolor);
      }

      float[][] afloat1 = new float[aenumdyecolor.length][];
      int k = 0;

      for(String s : p_readDyeColors_0_.keySet()) {
         String s1 = p_readDyeColors_0_.getProperty(s);
         if(s.startsWith(p_readDyeColors_2_)) {
            String s2 = StrUtils.removePrefix(s, p_readDyeColors_2_);
            if(s2.equals("lightBlue")) {
               s2 = "light_blue";
            }

            EnumDyeColor enumdyecolor1 = (EnumDyeColor)map.get(s2);
            int j = parseColor(s1);
            if(enumdyecolor1 != null && j >= 0) {
               float[] afloat = new float[]{(float)(j >> 16 & 255) / 255.0F, (float)(j >> 8 & 255) / 255.0F, (float)(j & 255) / 255.0F};
               afloat1[enumdyecolor1.ordinal()] = afloat;
               ++k;
            } else {
               warn("Invalid color: " + s + " = " + s1);
            }
         }
      }

      if(k <= 0) {
         return (float[][])null;
      } else {
         dbg(p_readDyeColors_3_ + " colors: " + k);
         return afloat1;
      }
   }

   private static float[] getDyeColors(EnumDyeColor p_getDyeColors_0_, float[][] p_getDyeColors_1_, float[] p_getDyeColors_2_) {
      if(p_getDyeColors_1_ == null) {
         return p_getDyeColors_2_;
      } else if(p_getDyeColors_0_ == null) {
         return p_getDyeColors_2_;
      } else {
         float[] afloat = p_getDyeColors_1_[p_getDyeColors_0_.ordinal()];
         return afloat == null?p_getDyeColors_2_:afloat;
      }
   }

   public static float[] getWolfCollarColors(EnumDyeColor p_getWolfCollarColors_0_, float[] p_getWolfCollarColors_1_) {
      return getDyeColors(p_getWolfCollarColors_0_, wolfCollarColors, p_getWolfCollarColors_1_);
   }

   public static float[] getSheepColors(EnumDyeColor p_getSheepColors_0_, float[] p_getSheepColors_1_) {
      return getDyeColors(p_getSheepColors_0_, sheepColors, p_getSheepColors_1_);
   }

   private static int[] readTextColors(Properties p_readTextColors_0_, String p_readTextColors_1_, String p_readTextColors_2_, String p_readTextColors_3_) {
      int[] aint = new int[32];
      Arrays.fill((int[])aint, (int)-1);
      int i = 0;

      for(String s : p_readTextColors_0_.keySet()) {
         String s1 = p_readTextColors_0_.getProperty(s);
         if(s.startsWith(p_readTextColors_2_)) {
            String s2 = StrUtils.removePrefix(s, p_readTextColors_2_);
            int j = Config.parseInt(s2, -1);
            int k = parseColor(s1);
            if(j >= 0 && j < aint.length && k >= 0) {
               aint[j] = k;
               ++i;
            } else {
               warn("Invalid color: " + s + " = " + s1);
            }
         }
      }

      if(i <= 0) {
         return null;
      } else {
         dbg(p_readTextColors_3_ + " colors: " + i);
         return aint;
      }
   }

   public static int getTextColor(int p_getTextColor_0_, int p_getTextColor_1_) {
      if(textColors == null) {
         return p_getTextColor_1_;
      } else if(p_getTextColor_0_ >= 0 && p_getTextColor_0_ < textColors.length) {
         int i = textColors[p_getTextColor_0_];
         return i < 0?p_getTextColor_1_:i;
      } else {
         return p_getTextColor_1_;
      }
   }

   private static int[] readMapColors(Properties p_readMapColors_0_, String p_readMapColors_1_, String p_readMapColors_2_, String p_readMapColors_3_) {
      int[] aint = new int[MapColor.field_76281_a.length];
      Arrays.fill((int[])aint, (int)-1);
      int i = 0;

      for(String s : p_readMapColors_0_.keySet()) {
         String s1 = p_readMapColors_0_.getProperty(s);
         if(s.startsWith(p_readMapColors_2_)) {
            String s2 = StrUtils.removePrefix(s, p_readMapColors_2_);
            int j = getMapColorIndex(s2);
            int k = parseColor(s1);
            if(j >= 0 && j < aint.length && k >= 0) {
               aint[j] = k;
               ++i;
            } else {
               warn("Invalid color: " + s + " = " + s1);
            }
         }
      }

      if(i <= 0) {
         return null;
      } else {
         dbg(p_readMapColors_3_ + " colors: " + i);
         return aint;
      }
   }

   private static int[] readPotionColors(Properties p_readPotionColors_0_, String p_readPotionColors_1_, String p_readPotionColors_2_, String p_readPotionColors_3_) {
      int[] aint = new int[Potion.field_76425_a.length];
      Arrays.fill((int[])aint, (int)-1);
      int i = 0;

      for(String s : p_readPotionColors_0_.keySet()) {
         String s1 = p_readPotionColors_0_.getProperty(s);
         if(s.startsWith(p_readPotionColors_2_)) {
            int j = getPotionId(s);
            int k = parseColor(s1);
            if(j >= 0 && j < aint.length && k >= 0) {
               aint[j] = k;
               ++i;
            } else {
               warn("Invalid color: " + s + " = " + s1);
            }
         }
      }

      if(i <= 0) {
         return null;
      } else {
         dbg(p_readPotionColors_3_ + " colors: " + i);
         return aint;
      }
   }

   private static int getPotionId(String p_getPotionId_0_) {
      if(p_getPotionId_0_.equals("potion.water")) {
         return 0;
      } else {
         Potion[] apotion = Potion.field_76425_a;

         for(int i = 0; i < apotion.length; ++i) {
            Potion potion = apotion[i];
            if(potion != null && potion.func_76393_a().equals(p_getPotionId_0_)) {
               return potion.func_76396_c();
            }
         }

         return -1;
      }
   }

   public static int getPotionColor(int p_getPotionColor_0_, int p_getPotionColor_1_) {
      if(potionColors == null) {
         return p_getPotionColor_1_;
      } else if(p_getPotionColor_0_ >= 0 && p_getPotionColor_0_ < potionColors.length) {
         int i = potionColors[p_getPotionColor_0_];
         return i < 0?p_getPotionColor_1_:i;
      } else {
         return p_getPotionColor_1_;
      }
   }

   private static int getMapColorIndex(String p_getMapColorIndex_0_) {
      return p_getMapColorIndex_0_ == null?-1:(p_getMapColorIndex_0_.equals("air")?MapColor.field_151660_b.field_76290_q:(p_getMapColorIndex_0_.equals("grass")?MapColor.field_151661_c.field_76290_q:(p_getMapColorIndex_0_.equals("sand")?MapColor.field_151658_d.field_76290_q:(p_getMapColorIndex_0_.equals("cloth")?MapColor.field_151659_e.field_76290_q:(p_getMapColorIndex_0_.equals("tnt")?MapColor.field_151656_f.field_76290_q:(p_getMapColorIndex_0_.equals("ice")?MapColor.field_151657_g.field_76290_q:(p_getMapColorIndex_0_.equals("iron")?MapColor.field_151668_h.field_76290_q:(p_getMapColorIndex_0_.equals("foliage")?MapColor.field_151669_i.field_76290_q:(p_getMapColorIndex_0_.equals("clay")?MapColor.field_151667_k.field_76290_q:(p_getMapColorIndex_0_.equals("dirt")?MapColor.field_151664_l.field_76290_q:(p_getMapColorIndex_0_.equals("stone")?MapColor.field_151665_m.field_76290_q:(p_getMapColorIndex_0_.equals("water")?MapColor.field_151662_n.field_76290_q:(p_getMapColorIndex_0_.equals("wood")?MapColor.field_151663_o.field_76290_q:(p_getMapColorIndex_0_.equals("quartz")?MapColor.field_151677_p.field_76290_q:(p_getMapColorIndex_0_.equals("gold")?MapColor.field_151647_F.field_76290_q:(p_getMapColorIndex_0_.equals("diamond")?MapColor.field_151648_G.field_76290_q:(p_getMapColorIndex_0_.equals("lapis")?MapColor.field_151652_H.field_76290_q:(p_getMapColorIndex_0_.equals("emerald")?MapColor.field_151653_I.field_76290_q:(p_getMapColorIndex_0_.equals("podzol")?MapColor.field_151654_J.field_76290_q:(p_getMapColorIndex_0_.equals("netherrack")?MapColor.field_151655_K.field_76290_q:(!p_getMapColorIndex_0_.equals("snow") && !p_getMapColorIndex_0_.equals("white")?(!p_getMapColorIndex_0_.equals("adobe") && !p_getMapColorIndex_0_.equals("orange")?(p_getMapColorIndex_0_.equals("magenta")?MapColor.field_151675_r.field_76290_q:(!p_getMapColorIndex_0_.equals("light_blue") && !p_getMapColorIndex_0_.equals("lightBlue")?(p_getMapColorIndex_0_.equals("yellow")?MapColor.field_151673_t.field_76290_q:(p_getMapColorIndex_0_.equals("lime")?MapColor.field_151672_u.field_76290_q:(p_getMapColorIndex_0_.equals("pink")?MapColor.field_151671_v.field_76290_q:(p_getMapColorIndex_0_.equals("gray")?MapColor.field_151670_w.field_76290_q:(p_getMapColorIndex_0_.equals("silver")?MapColor.field_151680_x.field_76290_q:(p_getMapColorIndex_0_.equals("cyan")?MapColor.field_151679_y.field_76290_q:(p_getMapColorIndex_0_.equals("purple")?MapColor.field_151678_z.field_76290_q:(p_getMapColorIndex_0_.equals("blue")?MapColor.field_151649_A.field_76290_q:(p_getMapColorIndex_0_.equals("brown")?MapColor.field_151650_B.field_76290_q:(p_getMapColorIndex_0_.equals("green")?MapColor.field_151651_C.field_76290_q:(p_getMapColorIndex_0_.equals("red")?MapColor.field_151645_D.field_76290_q:(p_getMapColorIndex_0_.equals("black")?MapColor.field_151646_E.field_76290_q:-1)))))))))))):MapColor.field_151674_s.field_76290_q)):MapColor.field_151676_q.field_76290_q):MapColor.field_151666_j.field_76290_q)))))))))))))))))))));
   }

   private static int[] getMapColors() {
      MapColor[] amapcolor = MapColor.field_76281_a;
      int[] aint = new int[amapcolor.length];
      Arrays.fill((int[])aint, (int)-1);

      for(int i = 0; i < amapcolor.length && i < aint.length; ++i) {
         MapColor mapcolor = amapcolor[i];
         if(mapcolor != null) {
            aint[i] = mapcolor.field_76291_p;
         }
      }

      return aint;
   }

   private static void setMapColors(int[] p_setMapColors_0_) {
      if(p_setMapColors_0_ != null) {
         MapColor[] amapcolor = MapColor.field_76281_a;
         boolean flag = false;

         for(int i = 0; i < amapcolor.length && i < p_setMapColors_0_.length; ++i) {
            MapColor mapcolor = amapcolor[i];
            if(mapcolor != null) {
               int j = p_setMapColors_0_[i];
               if(j >= 0 && mapcolor.field_76291_p != j) {
                  mapcolor.field_76291_p = j;
                  flag = true;
               }
            }
         }

         if(flag) {
            Minecraft.func_71410_x().func_110434_K().reloadBannerTextures();
         }

      }
   }

   private static int getEntityId(String p_getEntityId_0_) {
      if(p_getEntityId_0_ == null) {
         return -1;
      } else {
         int i = EntityList.func_180122_a(p_getEntityId_0_);
         if(i < 0) {
            return -1;
         } else {
            String s = EntityList.func_75617_a(i);
            return !Config.equals(p_getEntityId_0_, s)?-1:i;
         }
      }
   }

   private static void dbg(String p_dbg_0_) {
      Config.dbg("CustomColors: " + p_dbg_0_);
   }

   private static void warn(String p_warn_0_) {
      Config.warn("CustomColors: " + p_warn_0_);
   }

   public static int getExpBarTextColor(int p_getExpBarTextColor_0_) {
      return expBarTextColor < 0?p_getExpBarTextColor_0_:expBarTextColor;
   }

   public static int getBossTextColor(int p_getBossTextColor_0_) {
      return bossTextColor < 0?p_getBossTextColor_0_:bossTextColor;
   }

   public static int getSignTextColor(int p_getSignTextColor_0_) {
      return signTextColor < 0?p_getSignTextColor_0_:signTextColor;
   }

   public interface IColorizer {
      int getColor(IBlockAccess var1, BlockPos var2);

      boolean isColorConstant();
   }
}
