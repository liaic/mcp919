package optifine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.ModelUtils;

public class SmartLeaves {

   public static IBakedModel modelLeavesCullAcacia = null;
   public static IBakedModel modelLeavesCullBirch = null;
   public static IBakedModel modelLeavesCullDarkOak = null;
   public static IBakedModel modelLeavesCullJungle = null;
   public static IBakedModel modelLeavesCullOak = null;
   public static IBakedModel modelLeavesCullSpruce = null;
   public static List generalQuadsCullAcacia = null;
   public static List generalQuadsCullBirch = null;
   public static List generalQuadsCullDarkOak = null;
   public static List generalQuadsCullJungle = null;
   public static List generalQuadsCullOak = null;
   public static List generalQuadsCullSpruce = null;
   public static IBakedModel modelLeavesDoubleAcacia = null;
   public static IBakedModel modelLeavesDoubleBirch = null;
   public static IBakedModel modelLeavesDoubleDarkOak = null;
   public static IBakedModel modelLeavesDoubleJungle = null;
   public static IBakedModel modelLeavesDoubleOak = null;
   public static IBakedModel modelLeavesDoubleSpruce = null;


   public static IBakedModel getLeavesModel(IBakedModel model) {
      if(!Config.isTreesSmart()) {
         return model;
      } else {
         List generalQuads = model.getGeneralQuads();
         return generalQuads == generalQuadsCullAcacia?modelLeavesDoubleAcacia:(generalQuads == generalQuadsCullBirch?modelLeavesDoubleBirch:(generalQuads == generalQuadsCullDarkOak?modelLeavesDoubleDarkOak:(generalQuads == generalQuadsCullJungle?modelLeavesDoubleJungle:(generalQuads == generalQuadsCullOak?modelLeavesDoubleOak:(generalQuads == generalQuadsCullSpruce?modelLeavesDoubleSpruce:model)))));
      }
   }

   public static boolean isSameLeaves(IBlockState state1, IBlockState state2) {
      if(state1 == state2) {
         return true;
      } else {
         Block block1 = state1.getBlock();
         Block block2 = state2.getBlock();
         return block1 != block2?false:(block1 instanceof BlockOldLeaf?state1.getValue(BlockOldLeaf.VARIANT).equals(state2.getValue(BlockOldLeaf.VARIANT)):(block1 instanceof BlockNewLeaf?state1.getValue(BlockNewLeaf.VARIANT).equals(state2.getValue(BlockNewLeaf.VARIANT)):false));
      }
   }

   public static void updateLeavesModels() {
      ArrayList updatedTypes = new ArrayList();
      modelLeavesCullAcacia = getModelCull("acacia", updatedTypes);
      modelLeavesCullBirch = getModelCull("birch", updatedTypes);
      modelLeavesCullDarkOak = getModelCull("dark_oak", updatedTypes);
      modelLeavesCullJungle = getModelCull("jungle", updatedTypes);
      modelLeavesCullOak = getModelCull("oak", updatedTypes);
      modelLeavesCullSpruce = getModelCull("spruce", updatedTypes);
      generalQuadsCullAcacia = getGeneralQuadsSafe(modelLeavesCullAcacia);
      generalQuadsCullBirch = getGeneralQuadsSafe(modelLeavesCullBirch);
      generalQuadsCullDarkOak = getGeneralQuadsSafe(modelLeavesCullDarkOak);
      generalQuadsCullJungle = getGeneralQuadsSafe(modelLeavesCullJungle);
      generalQuadsCullOak = getGeneralQuadsSafe(modelLeavesCullOak);
      generalQuadsCullSpruce = getGeneralQuadsSafe(modelLeavesCullSpruce);
      modelLeavesDoubleAcacia = getModelDoubleFace(modelLeavesCullAcacia);
      modelLeavesDoubleBirch = getModelDoubleFace(modelLeavesCullBirch);
      modelLeavesDoubleDarkOak = getModelDoubleFace(modelLeavesCullDarkOak);
      modelLeavesDoubleJungle = getModelDoubleFace(modelLeavesCullJungle);
      modelLeavesDoubleOak = getModelDoubleFace(modelLeavesCullOak);
      modelLeavesDoubleSpruce = getModelDoubleFace(modelLeavesCullSpruce);
      if(updatedTypes.size() > 0) {
         Config.dbg("Enable face culling: " + Config.arrayToString(updatedTypes.toArray()));
      }

   }

   public static List getGeneralQuadsSafe(IBakedModel model) {
      return model == null?null:model.getGeneralQuads();
   }

   public static IBakedModel getModelCull(String type, List updatedTypes) {
      ModelManager modelManager = Config.getModelManager();
      if(modelManager == null) {
         return null;
      } else {
         ResourceLocation locState = new ResourceLocation("blockstates/" + type + "_leaves.json");
         if(Config.getDefiningResourcePack(locState) != Config.getDefaultResourcePack()) {
            return null;
         } else {
            ResourceLocation locModel = new ResourceLocation("models/block/" + type + "_leaves.json");
            if(Config.getDefiningResourcePack(locModel) != Config.getDefaultResourcePack()) {
               return null;
            } else {
               ModelResourceLocation mrl = new ModelResourceLocation(type + "_leaves", "normal");
               IBakedModel model = modelManager.getModel(mrl);
               if(model != null && model != modelManager.getMissingModel()) {
                  List listGeneral = model.getGeneralQuads();
                  if(listGeneral.size() == 0) {
                     return model;
                  } else if(listGeneral.size() != 6) {
                     return null;
                  } else {
                     Iterator it = listGeneral.iterator();

                     while(it.hasNext()) {
                        BakedQuad quad = (BakedQuad)it.next();
                        List listFace = model.getFaceQuads(quad.getFace());
                        if(listFace.size() > 0) {
                           return null;
                        }

                        listFace.add(quad);
                     }

                     listGeneral.clear();
                     updatedTypes.add(type + "_leaves");
                     return model;
                  }
               } else {
                  return null;
               }
            }
         }
      }
   }

   public static IBakedModel getModelDoubleFace(IBakedModel model) {
      if(model == null) {
         return null;
      } else if(model.getGeneralQuads().size() > 0) {
         Config.warn("SmartLeaves: Model is not cube, general quads: " + model.getGeneralQuads().size() + ", model: " + model);
         return model;
      } else {
         EnumFacing[] faces = EnumFacing.VALUES;

         for(int model2 = 0; model2 < faces.length; ++model2) {
            EnumFacing faceQuads = faces[model2];
            List i = model.getFaceQuads(faceQuads);
            if(i.size() != 1) {
               Config.warn("SmartLeaves: Model is not cube, side: " + faceQuads + ", quads: " + i.size() + ", model: " + model);
               return model;
            }
         }

         IBakedModel var12 = ModelUtils.duplicateModel(model);
         List[] var13 = new List[faces.length];

         for(int var14 = 0; var14 < faces.length; ++var14) {
            EnumFacing face = faces[var14];
            List quads = var12.getFaceQuads(face);
            BakedQuad quad = (BakedQuad)quads.get(0);
            BakedQuad quad2 = new BakedQuad((int[])quad.getVertexData().clone(), quad.getTintIndex(), quad.getFace(), quad.getSprite());
            int[] vd = quad2.getVertexData();
            int[] vd2 = (int[])vd.clone();
            int step = vd.length / 4;
            System.arraycopy(vd, 0 * step, vd2, 3 * step, step);
            System.arraycopy(vd, 1 * step, vd2, 2 * step, step);
            System.arraycopy(vd, 2 * step, vd2, 1 * step, step);
            System.arraycopy(vd, 3 * step, vd2, 0 * step, step);
            System.arraycopy(vd2, 0, vd, 0, vd2.length);
            quads.add(quad2);
         }

         return var12;
      }
   }

}
