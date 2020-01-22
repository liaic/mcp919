package shadersmod.uniform;

import net.minecraft.world.biome.BiomeGenBase;
import net.optifine.entity.model.anim.ExpressionType;
import net.optifine.entity.model.anim.IExpressionFloat;
import shadersmod.client.Shaders;
import shadersmod.uniform.LegacyUniforms;

public enum ShaderParameterFloat implements IExpressionFloat {

   BIOME("BIOME", 0, "biome"),
   HELD_ITEM_ID("HELD_ITEM_ID", 1, "heldItemId"),
   HELD_BLOCK_LIGHT_VALUE("HELD_BLOCK_LIGHT_VALUE", 2, "heldBlockLightValue"),
   HELD_ITEM_ID2("HELD_ITEM_ID2", 3, "heldItemId2"),
   HELD_BLOCK_LIGHT_VALUE2("HELD_BLOCK_LIGHT_VALUE2", 4, "heldBlockLightValue2"),
   FOG_MODE("FOG_MODE", 5, "fogMode"),
   WORLD_TIME("WORLD_TIME", 6, "worldTime"),
   WORLD_DAY("WORLD_DAY", 7, "worldDay"),
   MOON_PHASE("MOON_PHASE", 8, "moonPhase"),
   FRAME_COUNTER("FRAME_COUNTER", 9, "frameCounter"),
   FRAME_TIME("FRAME_TIME", 10, "frameTime"),
   FRAME_TIME_COUNTER("FRAME_TIME_COUNTER", 11, "frameTimeCounter"),
   SUN_ANGLE("SUN_ANGLE", 12, "sunAngle"),
   SHADOW_ANGLE("SHADOW_ANGLE", 13, "shadowAngle"),
   RAIN_STRENGTH("RAIN_STRENGTH", 14, "rainStrength"),
   ASPECT_RATIO("ASPECT_RATIO", 15, "aspectRatio"),
   VIEW_WIDTH("VIEW_WIDTH", 16, "viewWidth"),
   VIEW_HEIGHT("VIEW_HEIGHT", 17, "viewHeight"),
   NEAR("NEAR", 18, "near"),
   FAR("FAR", 19, "far"),
   WETNESS("WETNESS", 20, "wetness"),
   EYE_ALTITUDE("EYE_ALTITUDE", 21, "eyeAltitude"),
   EYE_BRIGHTNESS_X("EYE_BRIGHTNESS_X", 22, "eyeBrightness.x"),
   EYE_BRIGHTNESS_Y("EYE_BRIGHTNESS_Y", 23, "eyeBrightness.y"),
   TERRAIN_TEXTURE_SIZE_X("TERRAIN_TEXTURE_SIZE_X", 24, "terrainTextureSize.x"),
   TERRAIN_TEXTURE_SIZE_Y("TERRAIN_TEXTURE_SIZE_Y", 25, "terrainTextureSize.y"),
   TERRRAIN_ICON_SIZE("TERRRAIN_ICON_SIZE", 26, "terrainIconSize"),
   IS_EYE_IN_WATER("IS_EYE_IN_WATER", 27, "isEyeInWater"),
   NIGHT_VISION("NIGHT_VISION", 28, "nightVision"),
   BLINDNESS("BLINDNESS", 29, "blindness"),
   SCREEN_BRIGHTNESS("SCREEN_BRIGHTNESS", 30, "screenBrightness"),
   HIDE_GUI("HIDE_GUI", 31, "hideGUI"),
   CENTER_DEPT_SMOOTH("CENTER_DEPT_SMOOTH", 32, "centerDepthSmooth"),
   ATLAS_SIZE_X("ATLAS_SIZE_X", 33, "atlasSize.x"),
   ATLAS_SIZE_Y("ATLAS_SIZE_Y", 34, "atlasSize.y"),
   CAMERA_POSITION_X("CAMERA_POSITION_X", 35, "cameraPosition.x"),
   CAMERA_POSITION_Y("CAMERA_POSITION_Y", 36, "cameraPosition.y"),
   CAMERA_POSITION_Z("CAMERA_POSITION_Z", 37, "cameraPosition.z"),
   PREVIOUS_CAMERA_POSITION_X("PREVIOUS_CAMERA_POSITION_X", 38, "previousCameraPosition.x"),
   PREVIOUS_CAMERA_POSITION_Y("PREVIOUS_CAMERA_POSITION_Y", 39, "previousCameraPosition.y"),
   PREVIOUS_CAMERA_POSITION_Z("PREVIOUS_CAMERA_POSITION_Z", 40, "previousCameraPosition.z"),
   SUN_POSITION_X("SUN_POSITION_X", 41, "sunPosition.x"),
   SUN_POSITION_Y("SUN_POSITION_Y", 42, "sunPosition.y"),
   SUN_POSITION_Z("SUN_POSITION_Z", 43, "sunPosition.z"),
   MOON_POSITION_X("MOON_POSITION_X", 44, "moonPosition.x"),
   MOON_POSITION_Y("MOON_POSITION_Y", 45, "moonPosition.y"),
   MOON_POSITION_Z("MOON_POSITION_Z", 46, "moonPosition.z"),
   SHADOW_LIGHT_POSITION_X("SHADOW_LIGHT_POSITION_X", 47, "shadowLightPosition.x"),
   SHADOW_LIGHT_POSITION_Y("SHADOW_LIGHT_POSITION_Y", 48, "shadowLightPosition.y"),
   SHADOW_LIGHT_POSITION_Z("SHADOW_LIGHT_POSITION_Z", 49, "shadowLightPosition.z"),
   UP_POSITION_X("UP_POSITION_X", 50, "upPosition.x"),
   UP_POSITION_Y("UP_POSITION_Y", 51, "upPosition.y"),
   UP_POSITION_Z("UP_POSITION_Z", 52, "upPosition.z"),
   FOG_COLOR_R("FOG_COLOR_R", 53, "fogColor.r"),
   FOG_COLOR_G("FOG_COLOR_G", 54, "fogColor.g"),
   FOG_COLOR_B("FOG_COLOR_B", 55, "fogColor.b"),
   SKY_COLOR_R("SKY_COLOR_R", 56, "skyColor.r"),
   SKY_COLOR_G("SKY_COLOR_G", 57, "skyColor.g"),
   SKY_COLOR_B("SKY_COLOR_B", 58, "skyColor.b");
   public String name;
   // $FF: synthetic field
   public static final ShaderParameterFloat[] $VALUES = new ShaderParameterFloat[]{BIOME, HELD_ITEM_ID, HELD_BLOCK_LIGHT_VALUE, HELD_ITEM_ID2, HELD_BLOCK_LIGHT_VALUE2, FOG_MODE, WORLD_TIME, WORLD_DAY, MOON_PHASE, FRAME_COUNTER, FRAME_TIME, FRAME_TIME_COUNTER, SUN_ANGLE, SHADOW_ANGLE, RAIN_STRENGTH, ASPECT_RATIO, VIEW_WIDTH, VIEW_HEIGHT, NEAR, FAR, WETNESS, EYE_ALTITUDE, EYE_BRIGHTNESS_X, EYE_BRIGHTNESS_Y, TERRAIN_TEXTURE_SIZE_X, TERRAIN_TEXTURE_SIZE_Y, TERRRAIN_ICON_SIZE, IS_EYE_IN_WATER, NIGHT_VISION, BLINDNESS, SCREEN_BRIGHTNESS, HIDE_GUI, CENTER_DEPT_SMOOTH, ATLAS_SIZE_X, ATLAS_SIZE_Y, CAMERA_POSITION_X, CAMERA_POSITION_Y, CAMERA_POSITION_Z, PREVIOUS_CAMERA_POSITION_X, PREVIOUS_CAMERA_POSITION_Y, PREVIOUS_CAMERA_POSITION_Z, SUN_POSITION_X, SUN_POSITION_Y, SUN_POSITION_Z, MOON_POSITION_X, MOON_POSITION_Y, MOON_POSITION_Z, SHADOW_LIGHT_POSITION_X, SHADOW_LIGHT_POSITION_Y, SHADOW_LIGHT_POSITION_Z, UP_POSITION_X, UP_POSITION_Y, UP_POSITION_Z, FOG_COLOR_R, FOG_COLOR_G, FOG_COLOR_B, SKY_COLOR_R, SKY_COLOR_G, SKY_COLOR_B};


   public ShaderParameterFloat(String var1, int var2, String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public ExpressionType getExpressionType() {
      return ExpressionType.FLOAT;
   }

   public float eval() {
      switch(ShaderParameterFloat.NamelessClass719165381.$SwitchMap$shadersmod$uniform$ShaderParameterFloat[this.ordinal()]) {
      case 1:
         BiomeGenBase valLegacy = Shaders.getCurrentWorld().getBiomeGenForCoords(Shaders.getCameraPosition());
         return (float)valLegacy.biomeID;
      default:
         Number valLegacy1 = LegacyUniforms.getNumber(this.name);
         return valLegacy1 != null?valLegacy1.floatValue():0.0F;
      }
   }


   // $FF: synthetic class
   public static class NamelessClass719165381 {

      // $FF: synthetic field
      public static final int[] $SwitchMap$shadersmod$uniform$ShaderParameterFloat = new int[ShaderParameterFloat.values().length];


      static {
         try {
            $SwitchMap$shadersmod$uniform$ShaderParameterFloat[ShaderParameterFloat.BIOME.ordinal()] = 1;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
