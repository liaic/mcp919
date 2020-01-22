package optifine;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.CustomPanoramaProperties;
import optifine.MathUtils;

public class CustomPanorama {

   public static CustomPanoramaProperties customPanoramaProperties = null;
   public static final Random random = new Random();


   public static CustomPanoramaProperties getCustomPanoramaProperties() {
      return customPanoramaProperties;
   }

   public static void update() {
      customPanoramaProperties = null;
      String[] folders = getPanoramaFolders();
      if(folders.length > 1) {
         Properties[] properties = getPanoramaProperties(folders);
         int[] weights = getWeights(properties);
         int index = getRandomIndex(weights);
         String folder = folders[index];
         Properties props = properties[index];
         if(props == null) {
            props = properties[0];
         }

         if(props == null) {
            props = new Properties();
         }

         CustomPanoramaProperties cpp = new CustomPanoramaProperties(folder, props);
         customPanoramaProperties = cpp;
      }
   }

   public static String[] getPanoramaFolders() {
      ArrayList listFolders = new ArrayList();
      listFolders.add("textures/gui/title/background");

      for(int folders = 0; folders < 100; ++folders) {
         String folder = "optifine/gui/background" + folders;
         String path = folder + "/panorama_0.png";
         ResourceLocation loc = new ResourceLocation(path);
         if(Config.hasResource(loc)) {
            listFolders.add(folder);
         }
      }

      String[] var5 = (String[])((String[])listFolders.toArray(new String[listFolders.size()]));
      return var5;
   }

   public static Properties[] getPanoramaProperties(String[] folders) {
      Properties[] propsArr = new Properties[folders.length];

      for(int i = 0; i < folders.length; ++i) {
         String folder = folders[i];
         if(i == 0) {
            folder = "optifine/gui";
         } else {
            Config.dbg("CustomPanorama: " + folder);
         }

         ResourceLocation propLoc = new ResourceLocation(folder + "/background.properties");

         try {
            InputStream e = Config.getResourceStream(propLoc);
            if(e != null) {
               Properties props = new Properties();
               props.load(e);
               Config.dbg("CustomPanorama: " + propLoc.getResourcePath());
               propsArr[i] = props;
               e.close();
            }
         } catch (IOException var7) {
            ;
         }
      }

      return propsArr;
   }

   public static int[] getWeights(Properties[] properties) {
      int[] weights = new int[properties.length];

      for(int i = 0; i < weights.length; ++i) {
         Properties prop = properties[i];
         if(prop == null) {
            prop = properties[0];
         }

         if(prop == null) {
            weights[i] = 1;
         } else {
            String str = prop.getProperty("weight", (String)null);
            weights[i] = Config.parseInt(str, 1);
         }
      }

      return weights;
   }

   public static int getRandomIndex(int[] weights) {
      int sumWeights = MathUtils.getSum(weights);
      int r = random.nextInt(sumWeights);
      int sum = 0;

      for(int i = 0; i < weights.length; ++i) {
         sum += weights[i];
         if(sum > r) {
            return i;
         }
      }

      return weights.length - 1;
   }

}
