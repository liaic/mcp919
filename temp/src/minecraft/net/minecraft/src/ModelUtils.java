package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.src.Config;
import net.minecraft.util.EnumFacing;

public class ModelUtils {
   public static void dbgModel(IBakedModel p_dbgModel_0_) {
      if(p_dbgModel_0_ != null) {
         Config.dbg("Model: " + p_dbgModel_0_ + ", ao: " + p_dbgModel_0_.func_177555_b() + ", gui3d: " + p_dbgModel_0_.func_177556_c() + ", builtIn: " + p_dbgModel_0_.func_177553_d() + ", particle: " + p_dbgModel_0_.func_177554_e());
         EnumFacing[] aenumfacing = EnumFacing.field_82609_l;

         for(int i = 0; i < aenumfacing.length; ++i) {
            EnumFacing enumfacing = aenumfacing[i];
            List list = p_dbgModel_0_.func_177551_a(enumfacing);
            dbgQuads(enumfacing.func_176610_l(), list, "  ");
         }

         List list1 = p_dbgModel_0_.func_177550_a();
         dbgQuads("General", list1, "  ");
      }
   }

   private static void dbgQuads(String p_dbgQuads_0_, List p_dbgQuads_1_, String p_dbgQuads_2_) {
      for(BakedQuad bakedquad : p_dbgQuads_1_) {
         dbgQuad(p_dbgQuads_0_, bakedquad, p_dbgQuads_2_);
      }

   }

   public static void dbgQuad(String p_dbgQuad_0_, BakedQuad p_dbgQuad_1_, String p_dbgQuad_2_) {
      Config.dbg(p_dbgQuad_2_ + "Quad: " + p_dbgQuad_1_.getClass().getName() + ", type: " + p_dbgQuad_0_ + ", face: " + p_dbgQuad_1_.func_178210_d() + ", tint: " + p_dbgQuad_1_.func_178211_c() + ", sprite: " + p_dbgQuad_1_.getSprite());
      dbgVertexData(p_dbgQuad_1_.func_178209_a(), "  " + p_dbgQuad_2_);
   }

   public static void dbgVertexData(int[] p_dbgVertexData_0_, String p_dbgVertexData_1_) {
      int i = p_dbgVertexData_0_.length / 4;
      Config.dbg(p_dbgVertexData_1_ + "Length: " + p_dbgVertexData_0_.length + ", step: " + i);

      for(int j = 0; j < 4; ++j) {
         int k = j * i;
         float f = Float.intBitsToFloat(p_dbgVertexData_0_[k + 0]);
         float f1 = Float.intBitsToFloat(p_dbgVertexData_0_[k + 1]);
         float f2 = Float.intBitsToFloat(p_dbgVertexData_0_[k + 2]);
         int l = p_dbgVertexData_0_[k + 3];
         float f3 = Float.intBitsToFloat(p_dbgVertexData_0_[k + 4]);
         float f4 = Float.intBitsToFloat(p_dbgVertexData_0_[k + 5]);
         Config.dbg(p_dbgVertexData_1_ + j + " xyz: " + f + "," + f1 + "," + f2 + " col: " + l + " u,v: " + f3 + "," + f4);
      }

   }

   public static IBakedModel duplicateModel(IBakedModel p_duplicateModel_0_) {
      List list = duplicateQuadList(p_duplicateModel_0_.func_177550_a());
      EnumFacing[] aenumfacing = EnumFacing.field_82609_l;
      List list1 = new ArrayList();

      for(int i = 0; i < aenumfacing.length; ++i) {
         EnumFacing enumfacing = aenumfacing[i];
         List list2 = p_duplicateModel_0_.func_177551_a(enumfacing);
         List list3 = duplicateQuadList(list2);
         list1.add(list3);
      }

      SimpleBakedModel simplebakedmodel = new SimpleBakedModel(list, list1, p_duplicateModel_0_.func_177555_b(), p_duplicateModel_0_.func_177556_c(), p_duplicateModel_0_.func_177554_e(), p_duplicateModel_0_.func_177552_f());
      return simplebakedmodel;
   }

   public static List duplicateQuadList(List p_duplicateQuadList_0_) {
      List list = new ArrayList();

      for(BakedQuad bakedquad : p_duplicateQuadList_0_) {
         BakedQuad bakedquad1 = duplicateQuad(bakedquad);
         list.add(bakedquad1);
      }

      return list;
   }

   public static BakedQuad duplicateQuad(BakedQuad p_duplicateQuad_0_) {
      BakedQuad bakedquad = new BakedQuad((int[])p_duplicateQuad_0_.func_178209_a().clone(), p_duplicateQuad_0_.func_178211_c(), p_duplicateQuad_0_.func_178210_d(), p_duplicateQuad_0_.getSprite());
      return bakedquad;
   }
}
