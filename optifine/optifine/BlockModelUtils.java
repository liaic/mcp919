package optifine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block.EnumOffsetType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.BreakingFour;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import optifine.Config;
import optifine.ModelUtils;
import org.lwjgl.util.vector.Vector3f;

public class BlockModelUtils {

   public static final float VERTEX_COORD_ACCURACY = 1.0E-6F;


   public static IBakedModel makeModelCube(String spriteName, int tintIndex) {
      TextureAtlasSprite sprite = Config.getMinecraft().getTextureMapBlocks().getAtlasSprite(spriteName);
      return makeModelCube(sprite, tintIndex);
   }

   public static IBakedModel makeModelCube(TextureAtlasSprite sprite, int tintIndex) {
      ArrayList generalQuads = new ArrayList();
      EnumFacing[] facings = EnumFacing.VALUES;
      ArrayList faceQuads = new ArrayList();

      for(int bakedModel = 0; bakedModel < facings.length; ++bakedModel) {
         EnumFacing facing = facings[bakedModel];
         ArrayList quads = new ArrayList();
         quads.add(makeBakedQuad(facing, sprite, tintIndex));
         faceQuads.add(quads);
      }

      SimpleBakedModel var8 = new SimpleBakedModel(generalQuads, faceQuads, true, true, sprite, ItemCameraTransforms.DEFAULT);
      return var8;
   }

   public static IBakedModel joinModelsCube(IBakedModel modelBase, IBakedModel modelAdd) {
      ArrayList generalQuads = new ArrayList();
      generalQuads.addAll(modelBase.getGeneralQuads());
      generalQuads.addAll(modelAdd.getGeneralQuads());
      EnumFacing[] facings = EnumFacing.VALUES;
      ArrayList faceQuads = new ArrayList();

      for(int ao = 0; ao < facings.length; ++ao) {
         EnumFacing builtIn = facings[ao];
         ArrayList sprite = new ArrayList();
         sprite.addAll(modelBase.getFaceQuads(builtIn));
         sprite.addAll(modelAdd.getFaceQuads(builtIn));
         faceQuads.add(sprite);
      }

      boolean var10 = modelBase.isAmbientOcclusion();
      boolean var11 = modelBase.isBuiltInRenderer();
      TextureAtlasSprite var12 = modelBase.getParticleTexture();
      ItemCameraTransforms transforms = modelBase.getItemCameraTransforms();
      SimpleBakedModel bakedModel = new SimpleBakedModel(generalQuads, faceQuads, var10, var11, var12, transforms);
      return bakedModel;
   }

   public static BakedQuad makeBakedQuad(EnumFacing facing, TextureAtlasSprite sprite, int tintIndex) {
      Vector3f posFrom = new Vector3f(0.0F, 0.0F, 0.0F);
      Vector3f posTo = new Vector3f(16.0F, 16.0F, 16.0F);
      BlockFaceUV uv = new BlockFaceUV(new float[]{0.0F, 0.0F, 16.0F, 16.0F}, 0);
      BlockPartFace face = new BlockPartFace(facing, tintIndex, "#" + facing.getName(), uv);
      ModelRotation modelRotation = ModelRotation.X0_Y0;
      Object partRotation = null;
      boolean uvLocked = false;
      boolean shade = true;
      FaceBakery faceBakery = new FaceBakery();
      BakedQuad quad = faceBakery.makeBakedQuad(posFrom, posTo, face, sprite, facing, modelRotation, (BlockPartRotation)partRotation, uvLocked, shade);
      return quad;
   }

   public static IBakedModel makeModel(String modelName, String spriteOldName, String spriteNewName) {
      TextureMap textureMap = Config.getMinecraft().getTextureMapBlocks();
      TextureAtlasSprite spriteOld = textureMap.getSpriteSafe(spriteOldName);
      TextureAtlasSprite spriteNew = textureMap.getSpriteSafe(spriteNewName);
      return makeModel(modelName, spriteOld, spriteNew);
   }

   public static IBakedModel makeModel(String modelName, TextureAtlasSprite spriteOld, TextureAtlasSprite spriteNew) {
      if(spriteOld != null && spriteNew != null) {
         ModelManager modelManager = Config.getModelManager();
         if(modelManager == null) {
            return null;
         } else {
            ModelResourceLocation mrl = new ModelResourceLocation(modelName, "normal");
            IBakedModel model = modelManager.getModel(mrl);
            if(model != null && model != modelManager.getMissingModel()) {
               IBakedModel modelNew = ModelUtils.duplicateModel(model);
               EnumFacing[] faces = EnumFacing.VALUES;

               for(int quadsGeneral = 0; quadsGeneral < faces.length; ++quadsGeneral) {
                  EnumFacing face = faces[quadsGeneral];
                  List quads = modelNew.getFaceQuads(face);
                  replaceTexture(quads, spriteOld, spriteNew);
               }

               List var11 = modelNew.getGeneralQuads();
               replaceTexture(var11, spriteOld, spriteNew);
               return modelNew;
            } else {
               return null;
            }
         }
      } else {
         return null;
      }
   }

   public static void replaceTexture(List<BakedQuad> quads, TextureAtlasSprite spriteOld, TextureAtlasSprite spriteNew) {
      ArrayList quadsNew = new ArrayList();
      Iterator it = quads.iterator();

      while(it.hasNext()) {
         BakedQuad quad = (BakedQuad)it.next();
         if(quad.getSprite() != spriteOld) {
            quadsNew.add(quad);
            break;
         }

         BreakingFour quadNew = new BreakingFour(quad, spriteNew);
         quadsNew.add(quadNew);
      }

      quads.clear();
      quads.addAll(quadsNew);
   }

   public static void snapVertexPosition(Vector3f pos) {
      pos.setX(snapVertexCoord(pos.getX()));
      pos.setY(snapVertexCoord(pos.getY()));
      pos.setZ(snapVertexCoord(pos.getZ()));
   }

   public static float snapVertexCoord(float x) {
      return x > -1.0E-6F && x < 1.0E-6F?0.0F:(x > 0.999999F && x < 1.000001F?1.0F:x);
   }

   public static AxisAlignedBB getOffsetBoundingBox(AxisAlignedBB aabb, EnumOffsetType offsetType, BlockPos pos) {
      int x = pos.getX();
      int z = pos.getZ();
      long k = (long)(x * 3129871) ^ (long)z * 116129781L;
      k = k * k * 42317861L + k * 11L;
      double dx = ((double)((float)(k >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
      double dz = ((double)((float)(k >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
      double dy = 0.0D;
      if(offsetType == EnumOffsetType.XYZ) {
         dy = ((double)((float)(k >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
      }

      return aabb.offset(dx, dy, dz);
   }
}
