package optifine;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import optifine.FileDownloadThread;
import optifine.HttpUtils;
import optifine.PlayerConfiguration;
import optifine.PlayerConfigurationReceiver;

public class PlayerConfigurations {

   public static Map mapConfigurations = null;
   public static boolean reloadPlayerItems = Boolean.getBoolean("player.models.reload");
   public static long timeReloadPlayerItemsMs = System.currentTimeMillis();


   public static void renderPlayerItems(ModelBiped modelBiped, AbstractClientPlayer player, float scale, float partialTicks) {
      PlayerConfiguration cfg = getPlayerConfiguration(player);
      if(cfg != null) {
         cfg.renderPlayerItems(modelBiped, player, scale, partialTicks);
      }

   }

   public static synchronized PlayerConfiguration getPlayerConfiguration(AbstractClientPlayer player) {
      if(reloadPlayerItems && System.currentTimeMillis() > timeReloadPlayerItemsMs + 5000L) {
         EntityPlayerSP name = Minecraft.getMinecraft().thePlayer;
         if(name != null) {
            setPlayerConfiguration(name.getNameClear(), (PlayerConfiguration)null);
            timeReloadPlayerItemsMs = System.currentTimeMillis();
         }
      }

      String name1 = player.getNameClear();
      if(name1 == null) {
         return null;
      } else {
         PlayerConfiguration pc = (PlayerConfiguration)getMapConfigurations().get(name1);
         if(pc == null) {
            pc = new PlayerConfiguration();
            getMapConfigurations().put(name1, pc);
            PlayerConfigurationReceiver pcl = new PlayerConfigurationReceiver(name1);
            String url = HttpUtils.getPlayerItemsUrl() + "/users/" + name1 + ".cfg";
            FileDownloadThread fdt = new FileDownloadThread(url, pcl);
            fdt.start();
         }

         return pc;
      }
   }

   public static synchronized void setPlayerConfiguration(String player, PlayerConfiguration pc) {
      getMapConfigurations().put(player, pc);
   }

   public static Map getMapConfigurations() {
      if(mapConfigurations == null) {
         mapConfigurations = new HashMap();
      }

      return mapConfigurations;
   }

}
