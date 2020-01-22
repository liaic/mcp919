package net.optifine.entity.model;

import net.minecraft.util.ResourceLocation;
import net.optifine.entity.model.CustomModelRenderer;

public class CustomEntityRenderer {

   public String name = null;
   public String basePath = null;
   public ResourceLocation textureLocation = null;
   public CustomModelRenderer[] customModelRenderers = null;
   public float shadowSize = 0.0F;


   public CustomEntityRenderer(String name, String basePath, ResourceLocation textureLocation, CustomModelRenderer[] customModelRenderers, float shadowSize) {
      this.name = name;
      this.basePath = basePath;
      this.textureLocation = textureLocation;
      this.customModelRenderers = customModelRenderers;
      this.shadowSize = shadowSize;
   }

   public String getName() {
      return this.name;
   }

   public String getBasePath() {
      return this.basePath;
   }

   public ResourceLocation getTextureLocation() {
      return this.textureLocation;
   }

   public CustomModelRenderer[] getCustomModelRenderers() {
      return this.customModelRenderers;
   }

   public float getShadowSize() {
      return this.shadowSize;
   }
}
