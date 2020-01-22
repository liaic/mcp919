package optifine;

import java.util.Properties;
import net.minecraft.util.ResourceLocation;
import optifine.ConnectedParser;

public class CustomPanoramaProperties {

   public String path;
   public ResourceLocation[] panoramaLocations;
   public int weight = 1;
   public int blur1 = 64;
   public int blur2 = 3;
   public int blur3 = 3;
   public int overlay1Top = -2130706433;
   public int overlay1Bottom = 16777215;
   public int overlay2Top = 0;
   public int overlay2Bottom = Integer.MIN_VALUE;


   public CustomPanoramaProperties(String path, Properties props) {
      ConnectedParser cp = new ConnectedParser("CustomPanorama");
      this.path = path;
      this.panoramaLocations = new ResourceLocation[6];

      for(int i = 0; i < this.panoramaLocations.length; ++i) {
         this.panoramaLocations[i] = new ResourceLocation(path + "/panorama_" + i + ".png");
      }

      this.weight = cp.parseInt(props.getProperty("weight"), 1);
      this.blur1 = cp.parseInt(props.getProperty("blur1"), 64);
      this.blur2 = cp.parseInt(props.getProperty("blur2"), 3);
      this.blur3 = cp.parseInt(props.getProperty("blur3"), 3);
      this.overlay1Top = ConnectedParser.parseColor4(props.getProperty("overlay1.top"), -2130706433);
      this.overlay1Bottom = ConnectedParser.parseColor4(props.getProperty("overlay1.bottom"), 16777215);
      this.overlay2Top = ConnectedParser.parseColor4(props.getProperty("overlay2.top"), 0);
      this.overlay2Bottom = ConnectedParser.parseColor4(props.getProperty("overlay2.bottom"), Integer.MIN_VALUE);
   }

   public ResourceLocation[] getPanoramaLocations() {
      return this.panoramaLocations;
   }

   public int getWeight() {
      return this.weight;
   }

   public int getBlur1() {
      return this.blur1;
   }

   public int getBlur2() {
      return this.blur2;
   }

   public int getBlur3() {
      return this.blur3;
   }

   public int getOverlay1Top() {
      return this.overlay1Top;
   }

   public int getOverlay1Bottom() {
      return this.overlay1Bottom;
   }

   public int getOverlay2Top() {
      return this.overlay2Top;
   }

   public int getOverlay2Bottom() {
      return this.overlay2Bottom;
   }

   public String toString() {
      return "" + this.path + ", weight: " + this.weight + ", blur: " + this.blur1 + " " + this.blur2 + " " + this.blur3 + ", overlay: " + this.overlay1Top + " " + this.overlay1Bottom + " " + this.overlay2Top + " " + this.overlay2Bottom;
   }
}
