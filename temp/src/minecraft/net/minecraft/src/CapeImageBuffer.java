package net.minecraft.src;

import java.awt.image.BufferedImage;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.src.CapeUtils;
import net.minecraft.util.ResourceLocation;

public class CapeImageBuffer extends ImageBufferDownload {
   private AbstractClientPlayer player;
   private ResourceLocation resourceLocation;

   public CapeImageBuffer(AbstractClientPlayer p_i23_1_, ResourceLocation p_i23_2_) {
      this.player = p_i23_1_;
      this.resourceLocation = p_i23_2_;
   }

   public BufferedImage func_78432_a(BufferedImage p_78432_1_) {
      return CapeUtils.parseCape(p_78432_1_);
   }

   public void func_152634_a() {
      if(this.player != null) {
         this.player.setLocationOfCape(this.resourceLocation);
      }

   }

   public void cleanup() {
      this.player = null;
   }
}
