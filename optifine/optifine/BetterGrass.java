package optifine;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockMycelium;
import net.minecraft.block.BlockDirt.DirtType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import optifine.BlockModelUtils;
import optifine.Config;

public class BetterGrass {

   public static boolean betterGrass = true;
   public static boolean betterMycelium = true;
   public static boolean betterPodzol = true;
   public static boolean betterGrassSnow = true;
   public static boolean betterMyceliumSnow = true;
   public static boolean betterPodzolSnow = true;
   public static boolean grassMultilayer = false;
   public static TextureAtlasSprite spriteGrass = null;
   public static TextureAtlasSprite spriteGrassSide = null;
   public static TextureAtlasSprite spriteMycelium = null;
   public static TextureAtlasSprite spritePodzol = null;
   public static TextureAtlasSprite spriteSnow = null;
   public static boolean spritesLoaded = false;
   public static IBakedModel modelCubeGrass = null;
   public static IBakedModel modelCubeMycelium = null;
   public static IBakedModel modelCubePodzol = null;
   public static IBakedModel modelCubeSnow = null;
   public static boolean modelsLoaded = false;
   public static final String TEXTURE_GRASS_DEFAULT = "blocks/grass_top";
   public static final String TEXTURE_GRASS_SIDE_DEFAULT = "blocks/grass_side";
   public static final String TEXTURE_MYCELIUM_DEFAULT = "blocks/mycelium_top";
   public static final String TEXTURE_PODZOL_DEFAULT = "blocks/dirt_podzol_top";
   public static final String TEXTURE_SNOW_DEFAULT = "blocks/snow";


   public static void updateIcons(TextureMap textureMap) {
      spritesLoaded = false;
      modelsLoaded = false;
      loadProperties(textureMap);
   }

   public static void update() {
      if(spritesLoaded) {
         modelCubeGrass = BlockModelUtils.makeModelCube(spriteGrass, 0);
         if(grassMultilayer) {
            IBakedModel modelCubeGrassSide = BlockModelUtils.makeModelCube(spriteGrassSide, -1);
            modelCubeGrass = BlockModelUtils.joinModelsCube(modelCubeGrassSide, modelCubeGrass);
         }

         modelCubeMycelium = BlockModelUtils.makeModelCube(spriteMycelium, -1);
         modelCubePodzol = BlockModelUtils.makeModelCube(spritePodzol, 0);
         modelCubeSnow = BlockModelUtils.makeModelCube(spriteSnow, -1);
         modelsLoaded = true;
      }
   }

   public static void loadProperties(TextureMap textureMap) {
      betterGrass = true;
      betterMycelium = true;
      betterPodzol = true;
      betterGrassSnow = true;
      betterMyceliumSnow = true;
      betterPodzolSnow = true;
      spriteGrass = textureMap.registerSprite(new ResourceLocation("blocks/grass_top"));
      spriteGrassSide = textureMap.registerSprite(new ResourceLocation("blocks/grass_side"));
      spriteMycelium = textureMap.registerSprite(new ResourceLocation("blocks/mycelium_top"));
      spritePodzol = textureMap.registerSprite(new ResourceLocation("blocks/dirt_podzol_top"));
      spriteSnow = textureMap.registerSprite(new ResourceLocation("blocks/snow"));
      spritesLoaded = true;
      String name = "optifine/bettergrass.properties";

      try {
         ResourceLocation e = new ResourceLocation(name);
         if(!Config.hasResource(e)) {
            return;
         }

         InputStream in = Config.getResourceStream(e);
         if(in == null) {
            return;
         }

         boolean defaultConfig = Config.isFromDefaultResourcePack(e);
         if(defaultConfig) {
            Config.dbg("BetterGrass: Parsing default configuration " + name);
         } else {
            Config.dbg("BetterGrass: Parsing configuration " + name);
         }

         Properties props = new Properties();
         props.load(in);
         betterGrass = getBoolean(props, "grass", true);
         betterMycelium = getBoolean(props, "mycelium", true);
         betterPodzol = getBoolean(props, "podzol", true);
         betterGrassSnow = getBoolean(props, "grass.snow", true);
         betterMyceliumSnow = getBoolean(props, "mycelium.snow", true);
         betterPodzolSnow = getBoolean(props, "podzol.snow", true);
         grassMultilayer = getBoolean(props, "grass.multilayer", false);
         spriteGrass = registerSprite(props, "texture.grass", "blocks/grass_top", textureMap);
         spriteGrassSide = registerSprite(props, "texture.grass_side", "blocks/grass_side", textureMap);
         spriteMycelium = registerSprite(props, "texture.mycelium", "blocks/mycelium_top", textureMap);
         spritePodzol = registerSprite(props, "texture.podzol", "blocks/dirt_podzol_top", textureMap);
         spriteSnow = registerSprite(props, "texture.snow", "blocks/snow", textureMap);
      } catch (IOException var6) {
         Config.warn("Error reading: " + name + ", " + var6.getClass().getName() + ": " + var6.getMessage());
      }

   }

   public static TextureAtlasSprite registerSprite(Properties props, String key, String textureDefault, TextureMap textureMap) {
      String texture = props.getProperty(key);
      if(texture == null) {
         texture = textureDefault;
      }

      ResourceLocation locPng = new ResourceLocation("textures/" + texture + ".png");
      if(!Config.hasResource(locPng)) {
         Config.warn("BetterGrass texture not found: " + locPng);
         texture = textureDefault;
      }

      ResourceLocation locSprite = new ResourceLocation(texture);
      TextureAtlasSprite sprite = textureMap.registerSprite(locSprite);
      return sprite;
   }

   public static List getFaceQuads(IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, EnumFacing facing, List quads) {
      if(facing != EnumFacing.UP && facing != EnumFacing.DOWN) {
         if(!modelsLoaded) {
            return quads;
         } else {
            Block block = blockState.getBlock();
            return block instanceof BlockMycelium?getFaceQuadsMycelium(blockAccess, blockState, blockPos, facing, quads):(block instanceof BlockDirt?getFaceQuadsDirt(blockAccess, blockState, blockPos, facing, quads):(block instanceof BlockGrass?getFaceQuadsGrass(blockAccess, blockState, blockPos, facing, quads):quads));
         }
      } else {
         return quads;
      }
   }

   public static List getFaceQuadsMycelium(IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, EnumFacing facing, List quads) {
      Block blockUp = blockAccess.getBlockState(blockPos.up()).getBlock();
      boolean snowy = blockUp == Blocks.snow || blockUp == Blocks.snow_layer;
      if(Config.isBetterGrassFancy()) {
         if(snowy) {
            if(betterMyceliumSnow && getBlockAt(blockPos, facing, blockAccess) == Blocks.snow_layer) {
               return getQuads(modelCubeSnow, facing);
            }
         } else if(betterMycelium && getBlockAt(blockPos.down(), facing, blockAccess) == Blocks.mycelium) {
            return getQuads(modelCubeMycelium, facing);
         }
      } else if(snowy) {
         if(betterMyceliumSnow) {
            return getQuads(modelCubeSnow, facing);
         }
      } else if(betterMycelium) {
         return getQuads(modelCubeMycelium, facing);
      }

      return quads;
   }

   public static List getFaceQuadsDirt(IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, EnumFacing facing, List quads) {
      Block blockTop = getBlockAt(blockPos, EnumFacing.UP, blockAccess);
      if(blockState.getValue(BlockDirt.VARIANT) != DirtType.PODZOL) {
         return quads;
      } else {
         boolean snowy = blockTop == Blocks.snow || blockTop == Blocks.snow_layer;
         if(Config.isBetterGrassFancy()) {
            if(snowy) {
               if(betterPodzolSnow && getBlockAt(blockPos, facing, blockAccess) == Blocks.snow_layer) {
                  return getQuads(modelCubeSnow, facing);
               }
            } else if(betterPodzol) {
               BlockPos posSide = blockPos.down().offset(facing);
               IBlockState stateSide = blockAccess.getBlockState(posSide);
               if(stateSide.getBlock() == Blocks.dirt && stateSide.getValue(BlockDirt.VARIANT) == DirtType.PODZOL) {
                  return getQuads(modelCubePodzol, facing);
               }
            }
         } else if(snowy) {
            if(betterPodzolSnow) {
               return getQuads(modelCubeSnow, facing);
            }
         } else if(betterPodzol) {
            return getQuads(modelCubePodzol, facing);
         }

         return quads;
      }
   }

   public static List getFaceQuadsGrass(IBlockAccess blockAccess, IBlockState blockState, BlockPos blockPos, EnumFacing facing, List quads) {
      Block blockUp = blockAccess.getBlockState(blockPos.up()).getBlock();
      boolean snowy = blockUp == Blocks.snow || blockUp == Blocks.snow_layer;
      if(Config.isBetterGrassFancy()) {
         if(snowy) {
            if(betterGrassSnow && getBlockAt(blockPos, facing, blockAccess) == Blocks.snow_layer) {
               return getQuads(modelCubeSnow, facing);
            }
         } else if(betterGrass && getBlockAt(blockPos.down(), facing, blockAccess) == Blocks.grass) {
            return getQuads(modelCubeGrass, facing);
         }
      } else if(snowy) {
         if(betterGrassSnow) {
            return getQuads(modelCubeSnow, facing);
         }
      } else if(betterGrass) {
         return getQuads(modelCubeGrass, facing);
      }

      return quads;
   }

   public static List getQuads(IBakedModel model, EnumFacing facing) {
      return facing == null?model.getGeneralQuads():model.getFaceQuads(facing);
   }

   public static Block getBlockAt(BlockPos blockPos, EnumFacing facing, IBlockAccess blockAccess) {
      BlockPos pos = blockPos.offset(facing);
      Block block = blockAccess.getBlockState(pos).getBlock();
      return block;
   }

   public static boolean getBoolean(Properties props, String key, boolean def) {
      String str = props.getProperty(key);
      return str == null?def:Boolean.parseBoolean(str);
   }

}
