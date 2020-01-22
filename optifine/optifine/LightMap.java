package optifine;

import net.minecraft.world.World;
import optifine.Config;
import optifine.CustomColormap;

public class LightMap {

   public CustomColormap lightMapRgb = null;
   public float[][] sunRgbs = new float[16][3];
   public float[][] torchRgbs = new float[16][3];


   public LightMap(CustomColormap lightMapRgb) {
      this.lightMapRgb = lightMapRgb;
   }

   public CustomColormap getColormap() {
      return this.lightMapRgb;
   }

   public boolean updateLightmap(World world, float torchFlickerX, int[] lmColors, boolean nightvision) {
      if(this.lightMapRgb == null) {
         return false;
      } else {
         int height = this.lightMapRgb.getHeight();
         if(nightvision && height < 64) {
            return false;
         } else {
            int width = this.lightMapRgb.getWidth();
            if(width < 16) {
               warn("Invalid lightmap width: " + width);
               this.lightMapRgb = null;
               return false;
            } else {
               int startIndex = 0;
               if(nightvision) {
                  startIndex = width * 16 * 2;
               }

               float sun = 1.1666666F * (world.getSunBrightness(1.0F) - 0.2F);
               if(world.getLastLightningBolt() > 0) {
                  sun = 1.0F;
               }

               sun = Config.limitTo1(sun);
               float sunX = sun * (float)(width - 1);
               float torchX = Config.limitTo1(torchFlickerX + 0.5F) * (float)(width - 1);
               float gamma = Config.limitTo1(Config.getGameSettings().gammaSetting);
               boolean hasGamma = gamma > 1.0E-4F;
               float[][] colorsRgb = this.lightMapRgb.getColorsRgb();
               this.getLightMapColumn(colorsRgb, sunX, startIndex, width, this.sunRgbs);
               this.getLightMapColumn(colorsRgb, torchX, startIndex + 16 * width, width, this.torchRgbs);
               float[] rgb = new float[3];

               for(int is = 0; is < 16; ++is) {
                  for(int it = 0; it < 16; ++it) {
                     int r;
                     for(r = 0; r < 3; ++r) {
                        float g = Config.limitTo1(this.sunRgbs[is][r] + this.torchRgbs[it][r]);
                        if(hasGamma) {
                           float b = 1.0F - g;
                           b = 1.0F - b * b * b * b;
                           g = gamma * b + (1.0F - gamma) * g;
                        }

                        rgb[r] = g;
                     }

                     r = (int)(rgb[0] * 255.0F);
                     int var20 = (int)(rgb[1] * 255.0F);
                     int var21 = (int)(rgb[2] * 255.0F);
                     lmColors[is * 16 + it] = -16777216 | r << 16 | var20 << 8 | var21;
                  }
               }

               return true;
            }
         }
      }
   }

   public void getLightMapColumn(float[][] origMap, float x, int offset, int width, float[][] colRgb) {
      int xLow = (int)Math.floor((double)x);
      int xHigh = (int)Math.ceil((double)x);
      if(xLow == xHigh) {
         for(int var15 = 0; var15 < 16; ++var15) {
            float[] var16 = origMap[offset + var15 * width + xLow];
            float[] var17 = colRgb[var15];

            for(int var18 = 0; var18 < 3; ++var18) {
               var17[var18] = var16[var18];
            }
         }

      } else {
         float dLow = 1.0F - (x - (float)xLow);
         float dHigh = 1.0F - ((float)xHigh - x);

         for(int y = 0; y < 16; ++y) {
            float[] rgbLow = origMap[offset + y * width + xLow];
            float[] rgbHigh = origMap[offset + y * width + xHigh];
            float[] rgb = colRgb[y];

            for(int i = 0; i < 3; ++i) {
               rgb[i] = rgbLow[i] * dLow + rgbHigh[i] * dHigh;
            }
         }

      }
   }

   public static void dbg(String str) {
      Config.dbg("CustomColors: " + str);
   }

   public static void warn(String str) {
      Config.warn("CustomColors: " + str);
   }
}
