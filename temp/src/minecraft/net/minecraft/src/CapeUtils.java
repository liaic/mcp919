package net.minecraft.src;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.src.CapeImageBuffer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FilenameUtils;

public class CapeUtils {
   public static void downloadCape(AbstractClientPlayer p_downloadCape_0_) {
      String s = p_downloadCape_0_.getNameClear();
      if(s != null && !s.isEmpty() && !s.contains("\u0000")) {
         String s1 = "http://s.optifine.net/capes/" + s + ".png";
         String s2 = FilenameUtils.getBaseName(s1);
         ResourceLocation resourcelocation = new ResourceLocation("capeof/" + s2);
         TextureManager texturemanager = Minecraft.func_71410_x().func_110434_K();
         ITextureObject itextureobject = texturemanager.func_110581_b(resourcelocation);
         if(itextureobject != null && itextureobject instanceof ThreadDownloadImageData) {
            ThreadDownloadImageData threaddownloadimagedata = (ThreadDownloadImageData)itextureobject;
            if(threaddownloadimagedata.imageFound != null) {
               if(threaddownloadimagedata.imageFound.booleanValue()) {
                  p_downloadCape_0_.setLocationOfCape(resourcelocation);
               }

               return;
            }
         }

         CapeImageBuffer capeimagebuffer = new CapeImageBuffer(p_downloadCape_0_, resourcelocation);
         ThreadDownloadImageData threaddownloadimagedata1 = new ThreadDownloadImageData((File)null, s1, (ResourceLocation)null, capeimagebuffer);
         threaddownloadimagedata1.pipeline = true;
         texturemanager.func_110579_a(resourcelocation, threaddownloadimagedata1);
      }

   }

   public static BufferedImage parseCape(BufferedImage p_parseCape_0_) {
      int i = 64;
      int j = 32;
      int k = p_parseCape_0_.getWidth();

      for(int l = p_parseCape_0_.getHeight(); i < k || j < l; j *= 2) {
         i *= 2;
      }

      BufferedImage bufferedimage = new BufferedImage(i, j, 2);
      Graphics graphics = bufferedimage.getGraphics();
      graphics.drawImage(p_parseCape_0_, 0, 0, (ImageObserver)null);
      graphics.dispose();
      return bufferedimage;
   }
}
