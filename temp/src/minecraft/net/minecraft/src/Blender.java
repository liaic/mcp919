package net.minecraft.src;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.src.Config;

public class Blender {
   public static final int BLEND_ALPHA = 0;
   public static final int BLEND_ADD = 1;
   public static final int BLEND_SUBSTRACT = 2;
   public static final int BLEND_MULTIPLY = 3;
   public static final int BLEND_DODGE = 4;
   public static final int BLEND_BURN = 5;
   public static final int BLEND_SCREEN = 6;
   public static final int BLEND_OVERLAY = 7;
   public static final int BLEND_REPLACE = 8;
   public static final int BLEND_DEFAULT = 1;

   public static int parseBlend(String p_parseBlend_0_) {
      if(p_parseBlend_0_ == null) {
         return 1;
      } else {
         p_parseBlend_0_ = p_parseBlend_0_.toLowerCase().trim();
         if(p_parseBlend_0_.equals("alpha")) {
            return 0;
         } else if(p_parseBlend_0_.equals("add")) {
            return 1;
         } else if(p_parseBlend_0_.equals("subtract")) {
            return 2;
         } else if(p_parseBlend_0_.equals("multiply")) {
            return 3;
         } else if(p_parseBlend_0_.equals("dodge")) {
            return 4;
         } else if(p_parseBlend_0_.equals("burn")) {
            return 5;
         } else if(p_parseBlend_0_.equals("screen")) {
            return 6;
         } else if(p_parseBlend_0_.equals("overlay")) {
            return 7;
         } else if(p_parseBlend_0_.equals("replace")) {
            return 8;
         } else {
            Config.warn("Unknown blend: " + p_parseBlend_0_);
            return 1;
         }
      }
   }

   public static void setupBlend(int p_setupBlend_0_, float p_setupBlend_1_) {
      switch(p_setupBlend_0_) {
      case 0:
         GlStateManager.func_179118_c();
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(770, 771);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, p_setupBlend_1_);
         break;
      case 1:
         GlStateManager.func_179118_c();
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(770, 1);
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, p_setupBlend_1_);
         break;
      case 2:
         GlStateManager.func_179118_c();
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(775, 0);
         GlStateManager.func_179131_c(p_setupBlend_1_, p_setupBlend_1_, p_setupBlend_1_, 1.0F);
         break;
      case 3:
         GlStateManager.func_179118_c();
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(774, 771);
         GlStateManager.func_179131_c(p_setupBlend_1_, p_setupBlend_1_, p_setupBlend_1_, p_setupBlend_1_);
         break;
      case 4:
         GlStateManager.func_179118_c();
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(1, 1);
         GlStateManager.func_179131_c(p_setupBlend_1_, p_setupBlend_1_, p_setupBlend_1_, 1.0F);
         break;
      case 5:
         GlStateManager.func_179118_c();
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(0, 769);
         GlStateManager.func_179131_c(p_setupBlend_1_, p_setupBlend_1_, p_setupBlend_1_, 1.0F);
         break;
      case 6:
         GlStateManager.func_179118_c();
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(1, 769);
         GlStateManager.func_179131_c(p_setupBlend_1_, p_setupBlend_1_, p_setupBlend_1_, 1.0F);
         break;
      case 7:
         GlStateManager.func_179118_c();
         GlStateManager.func_179147_l();
         GlStateManager.func_179112_b(774, 768);
         GlStateManager.func_179131_c(p_setupBlend_1_, p_setupBlend_1_, p_setupBlend_1_, 1.0F);
         break;
      case 8:
         GlStateManager.func_179141_d();
         GlStateManager.func_179084_k();
         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, p_setupBlend_1_);
      }

      GlStateManager.func_179098_w();
   }

   public static void clearBlend(float p_clearBlend_0_) {
      GlStateManager.func_179118_c();
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 1);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, p_clearBlend_0_);
   }
}
