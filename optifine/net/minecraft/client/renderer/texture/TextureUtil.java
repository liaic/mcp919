package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.Mipmaps;
import optifine.Reflector;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class TextureUtil {

   public static final Logger logger = LogManager.getLogger();
   public static final IntBuffer dataBuffer = GLAllocation.createDirectIntBuffer(4194304);
   public static final DynamicTexture missingTexture = new DynamicTexture(16, 16);
   public static final int[] missingTextureData = missingTexture.getTextureData();
   public static final int[] mipmapBuffer;
   public static final String __OBFID = "CL_00001067";


   public static int glGenTextures() {
      return GlStateManager.generateTexture();
   }

   public static void deleteTexture(int p_147942_0_) {
      GlStateManager.deleteTexture(p_147942_0_);
   }

   public static int uploadTextureImage(int p_110987_0_, BufferedImage p_110987_1_) {
      return uploadTextureImageAllocate(p_110987_0_, p_110987_1_, false, false);
   }

   public static void uploadTexture(int p_110988_0_, int[] p_110988_1_, int p_110988_2_, int p_110988_3_) {
      bindTexture(p_110988_0_);
      uploadTextureSub(0, p_110988_1_, p_110988_2_, p_110988_3_, 0, 0, false, false, false);
   }

   public static int[][] generateMipmapData(int p_147949_0_, int p_147949_1_, int[][] p_147949_2_) {
      int[][] var3 = new int[p_147949_0_ + 1][];
      var3[0] = p_147949_2_[0];
      if(p_147949_0_ > 0) {
         boolean var4 = false;

         int var5;
         for(var5 = 0; var5 < p_147949_2_.length; ++var5) {
            if(p_147949_2_[0][var5] >> 24 == 0) {
               var4 = true;
               break;
            }
         }

         for(var5 = 1; var5 <= p_147949_0_; ++var5) {
            if(p_147949_2_[var5] != null) {
               var3[var5] = p_147949_2_[var5];
            } else {
               int[] var6 = var3[var5 - 1];
               int[] var7 = new int[var6.length >> 2];
               int var8 = p_147949_1_ >> var5;
               int var9 = var7.length / var8;
               int var10 = var8 << 1;

               for(int var11 = 0; var11 < var8; ++var11) {
                  for(int var12 = 0; var12 < var9; ++var12) {
                     int var13 = 2 * (var11 + var12 * var10);
                     var7[var11 + var12 * var8] = blendColors(var6[var13 + 0], var6[var13 + 1], var6[var13 + 0 + var10], var6[var13 + 1 + var10], var4);
                  }
               }

               var3[var5] = var7;
            }
         }
      }

      return var3;
   }

   public static int blendColors(int p_147943_0_, int p_147943_1_, int p_147943_2_, int p_147943_3_, boolean p_147943_4_) {
      return Mipmaps.alphaBlend(p_147943_0_, p_147943_1_, p_147943_2_, p_147943_3_);
   }

   public static int blendColorComponent(int p_147944_0_, int p_147944_1_, int p_147944_2_, int p_147944_3_, int p_147944_4_) {
      float var5 = (float)Math.pow((double)((float)(p_147944_0_ >> p_147944_4_ & 255) / 255.0F), 2.2D);
      float var6 = (float)Math.pow((double)((float)(p_147944_1_ >> p_147944_4_ & 255) / 255.0F), 2.2D);
      float var7 = (float)Math.pow((double)((float)(p_147944_2_ >> p_147944_4_ & 255) / 255.0F), 2.2D);
      float var8 = (float)Math.pow((double)((float)(p_147944_3_ >> p_147944_4_ & 255) / 255.0F), 2.2D);
      float var9 = (float)Math.pow((double)(var5 + var6 + var7 + var8) * 0.25D, 0.45454545454545453D);
      return (int)((double)var9 * 255.0D);
   }

   public static void uploadTextureMipmap(int[][] p_147955_0_, int p_147955_1_, int p_147955_2_, int p_147955_3_, int p_147955_4_, boolean p_147955_5_, boolean p_147955_6_) {
      for(int var7 = 0; var7 < p_147955_0_.length; ++var7) {
         int[] var8 = p_147955_0_[var7];
         uploadTextureSub(var7, var8, p_147955_1_ >> var7, p_147955_2_ >> var7, p_147955_3_ >> var7, p_147955_4_ >> var7, p_147955_5_, p_147955_6_, p_147955_0_.length > 1);
      }

   }

   public static void uploadTextureSub(int p_147947_0_, int[] p_147947_1_, int p_147947_2_, int p_147947_3_, int p_147947_4_, int p_147947_5_, boolean p_147947_6_, boolean p_147947_7_, boolean p_147947_8_) {
      int var9 = 4194304 / p_147947_2_;
      setTextureBlurMipmap(p_147947_6_, p_147947_8_);
      setTextureClamped(p_147947_7_);

      int var12;
      for(int var10 = 0; var10 < p_147947_2_ * p_147947_3_; var10 += p_147947_2_ * var12) {
         int var11 = var10 / p_147947_2_;
         var12 = Math.min(var9, p_147947_3_ - var11);
         int var13 = p_147947_2_ * var12;
         copyToBufferPos(p_147947_1_, var10, var13);
         GL11.glTexSubImage2D(3553, p_147947_0_, p_147947_4_, p_147947_5_ + var11, p_147947_2_, var12, '\u80e1', '\u8367', dataBuffer);
      }

   }

   public static int uploadTextureImageAllocate(int p_110989_0_, BufferedImage p_110989_1_, boolean p_110989_2_, boolean p_110989_3_) {
      allocateTexture(p_110989_0_, p_110989_1_.getWidth(), p_110989_1_.getHeight());
      return uploadTextureImageSub(p_110989_0_, p_110989_1_, 0, 0, p_110989_2_, p_110989_3_);
   }

   public static void allocateTexture(int p_110991_0_, int p_110991_1_, int p_110991_2_) {
      allocateTextureImpl(p_110991_0_, 0, p_110991_1_, p_110991_2_);
   }

   public static void allocateTextureImpl(int p_180600_0_, int p_180600_1_, int p_180600_2_, int p_180600_3_) {
      Class monitor = TextureUtil.class;
      if(Reflector.SplashScreen.exists()) {
         monitor = Reflector.SplashScreen.getTargetClass();
      }

      synchronized(monitor) {
         deleteTexture(p_180600_0_);
         bindTexture(p_180600_0_);
      }

      if(p_180600_1_ >= 0) {
         GL11.glTexParameteri(3553, '\u813d', p_180600_1_);
         GL11.glTexParameterf(3553, '\u813a', 0.0F);
         GL11.glTexParameterf(3553, '\u813b', (float)p_180600_1_);
         GL11.glTexParameterf(3553, '\u8501', 0.0F);
      }

      for(int var4 = 0; var4 <= p_180600_1_; ++var4) {
         GL11.glTexImage2D(3553, var4, 6408, p_180600_2_ >> var4, p_180600_3_ >> var4, 0, '\u80e1', '\u8367', (IntBuffer)null);
      }

   }

   public static int uploadTextureImageSub(int p_110995_0_, BufferedImage p_110995_1_, int p_110995_2_, int p_110995_3_, boolean p_110995_4_, boolean p_110995_5_) {
      bindTexture(p_110995_0_);
      uploadTextureImageSubImpl(p_110995_1_, p_110995_2_, p_110995_3_, p_110995_4_, p_110995_5_);
      return p_110995_0_;
   }

   public static void uploadTextureImageSubImpl(BufferedImage p_110993_0_, int p_110993_1_, int p_110993_2_, boolean p_110993_3_, boolean p_110993_4_) {
      int var5 = p_110993_0_.getWidth();
      int var6 = p_110993_0_.getHeight();
      int var7 = 4194304 / var5;
      int[] var8 = new int[var7 * var5];
      setTextureBlurred(p_110993_3_);
      setTextureClamped(p_110993_4_);

      for(int var9 = 0; var9 < var5 * var6; var9 += var5 * var7) {
         int var10 = var9 / var5;
         int var11 = Math.min(var7, var6 - var10);
         int var12 = var5 * var11;
         p_110993_0_.getRGB(0, var10, var5, var11, var8, 0, var5);
         copyToBuffer(var8, var12);
         GL11.glTexSubImage2D(3553, 0, p_110993_1_, p_110993_2_ + var10, var5, var11, '\u80e1', '\u8367', dataBuffer);
      }

   }

   public static void setTextureClamped(boolean p_110997_0_) {
      if(p_110997_0_) {
         GL11.glTexParameteri(3553, 10242, '\u812f');
         GL11.glTexParameteri(3553, 10243, '\u812f');
      } else {
         GL11.glTexParameteri(3553, 10242, 10497);
         GL11.glTexParameteri(3553, 10243, 10497);
      }

   }

   public static void setTextureBlurred(boolean p_147951_0_) {
      setTextureBlurMipmap(p_147951_0_, false);
   }

   public static void setTextureBlurMipmap(boolean p_147954_0_, boolean p_147954_1_) {
      if(p_147954_0_) {
         GL11.glTexParameteri(3553, 10241, p_147954_1_?9987:9729);
         GL11.glTexParameteri(3553, 10240, 9729);
      } else {
         int mipmapType = Config.getMipmapType();
         GL11.glTexParameteri(3553, 10241, p_147954_1_?mipmapType:9728);
         GL11.glTexParameteri(3553, 10240, 9728);
      }

   }

   public static void copyToBuffer(int[] p_110990_0_, int p_110990_1_) {
      copyToBufferPos(p_110990_0_, 0, p_110990_1_);
   }

   public static void copyToBufferPos(int[] p_110994_0_, int p_110994_1_, int p_110994_2_) {
      int[] var3 = p_110994_0_;
      if(Minecraft.getMinecraft().gameSettings.anaglyph) {
         var3 = updateAnaglyph(p_110994_0_);
      }

      dataBuffer.clear();
      dataBuffer.put(var3, p_110994_1_, p_110994_2_);
      dataBuffer.position(0).limit(p_110994_2_);
   }

   public static void bindTexture(int p_94277_0_) {
      GlStateManager.bindTexture(p_94277_0_);
   }

   public static int[] readImageData(IResourceManager p_110986_0_, ResourceLocation p_110986_1_) throws IOException {
      BufferedImage var2 = readBufferedImage(p_110986_0_.getResource(p_110986_1_).getInputStream());
      if(var2 == null) {
         return null;
      } else {
         int var3 = var2.getWidth();
         int var4 = var2.getHeight();
         int[] var5 = new int[var3 * var4];
         var2.getRGB(0, 0, var3, var4, var5, 0, var3);
         return var5;
      }
   }

   public static BufferedImage readBufferedImage(InputStream p_177053_0_) throws IOException, IOException {
      if(p_177053_0_ == null) {
         return null;
      } else {
         BufferedImage var1;
         try {
            var1 = ImageIO.read(p_177053_0_);
         } finally {
            IOUtils.closeQuietly(p_177053_0_);
         }

         return var1;
      }
   }

   public static int[] updateAnaglyph(int[] p_110985_0_) {
      int[] var1 = new int[p_110985_0_.length];

      for(int var2 = 0; var2 < p_110985_0_.length; ++var2) {
         var1[var2] = anaglyphColor(p_110985_0_[var2]);
      }

      return var1;
   }

   public static int anaglyphColor(int p_177054_0_) {
      int var1 = p_177054_0_ >> 24 & 255;
      int var2 = p_177054_0_ >> 16 & 255;
      int var3 = p_177054_0_ >> 8 & 255;
      int var4 = p_177054_0_ & 255;
      int var5 = (var2 * 30 + var3 * 59 + var4 * 11) / 100;
      int var6 = (var2 * 30 + var3 * 70) / 100;
      int var7 = (var2 * 30 + var4 * 70) / 100;
      return var1 << 24 | var5 << 16 | var6 << 8 | var7;
   }

   public static void saveGlTexture(String name, int textureId, int mipmapLevels, int width, int height) {
      bindTexture(textureId);
      GL11.glPixelStorei(3333, 1);
      GL11.glPixelStorei(3317, 1);

      for(int var5 = 0; var5 <= mipmapLevels; ++var5) {
         File var6 = new File(name + "_" + var5 + ".png");
         int var7 = width >> var5;
         int var8 = height >> var5;
         int var9 = var7 * var8;
         IntBuffer var10 = BufferUtils.createIntBuffer(var9);
         int[] var11 = new int[var9];
         GL11.glGetTexImage(3553, var5, '\u80e1', '\u8367', var10);
         var10.get(var11);
         BufferedImage var12 = new BufferedImage(var7, var8, 2);
         var12.setRGB(0, 0, var7, var8, var11, 0, var7);

         try {
            ImageIO.write(var12, "png", var6);
            logger.debug("Exported png to: {}", new Object[]{var6.getAbsolutePath()});
         } catch (Exception var14) {
            logger.debug("Unable to write: ", var14);
         }
      }

   }

   public static void processPixelValues(int[] p_147953_0_, int p_147953_1_, int p_147953_2_) {
      int[] var3 = new int[p_147953_1_];
      int var4 = p_147953_2_ / 2;

      for(int var5 = 0; var5 < var4; ++var5) {
         System.arraycopy(p_147953_0_, var5 * p_147953_1_, var3, 0, p_147953_1_);
         System.arraycopy(p_147953_0_, (p_147953_2_ - 1 - var5) * p_147953_1_, p_147953_0_, var5 * p_147953_1_, p_147953_1_);
         System.arraycopy(var3, 0, p_147953_0_, (p_147953_2_ - 1 - var5) * p_147953_1_, p_147953_1_);
      }

   }

   static {
      int var0 = -16777216;
      int var1 = -524040;
      int[] var2 = new int[]{-524040, -524040, -524040, -524040, -524040, -524040, -524040, -524040};
      int[] var3 = new int[]{-16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216, -16777216};
      int var4 = var2.length;

      for(int var5 = 0; var5 < 16; ++var5) {
         System.arraycopy(var5 < var4?var2:var3, 0, missingTextureData, 16 * var5, var4);
         System.arraycopy(var5 < var4?var3:var2, 0, missingTextureData, 16 * var5 + var4, var4);
      }

      missingTexture.updateDynamicTexture();
      mipmapBuffer = new int[4];
   }
}
