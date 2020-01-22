package optifine;

import java.awt.image.BufferedImage;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.util.ResourceLocation;
import optifine.CapeUtils;

public class CapeImageBuffer extends ImageBufferDownload {

   public AbstractClientPlayer player;
   public ResourceLocation resourceLocation;


   public CapeImageBuffer(AbstractClientPlayer player, ResourceLocation resourceLocation) {
      this.player = player;
      this.resourceLocation = resourceLocation;
   }

   public BufferedImage parseUserSkin(BufferedImage var1) {
      return CapeUtils.parseCape(var1);
   }

   public void skinAvailable() {
      if(this.player != null) {
         this.player.setLocationOfCape(this.resourceLocation);
      }

   }

   public void cleanup() {
      this.player = null;
   }
}
