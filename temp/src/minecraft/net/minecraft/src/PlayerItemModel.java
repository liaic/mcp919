package net.minecraft.src;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.src.Config;
import net.minecraft.src.PlayerItemRenderer;
import net.minecraft.util.ResourceLocation;

public class PlayerItemModel {
   private Dimension textureSize = null;
   private boolean usePlayerTexture = false;
   private PlayerItemRenderer[] modelRenderers = new PlayerItemRenderer[0];
   private ResourceLocation textureLocation = null;
   private BufferedImage textureImage = null;
   private DynamicTexture texture = null;
   private ResourceLocation locationMissing = new ResourceLocation("textures/blocks/wool_colored_red.png");
   public static final int ATTACH_BODY = 0;
   public static final int ATTACH_HEAD = 1;
   public static final int ATTACH_LEFT_ARM = 2;
   public static final int ATTACH_RIGHT_ARM = 3;
   public static final int ATTACH_LEFT_LEG = 4;
   public static final int ATTACH_RIGHT_LEG = 5;
   public static final int ATTACH_CAPE = 6;

   public PlayerItemModel(Dimension p_i82_1_, boolean p_i82_2_, PlayerItemRenderer[] p_i82_3_) {
      this.textureSize = p_i82_1_;
      this.usePlayerTexture = p_i82_2_;
      this.modelRenderers = p_i82_3_;
   }

   public void render(ModelBiped p_render_1_, AbstractClientPlayer p_render_2_, float p_render_3_, float p_render_4_) {
      TextureManager texturemanager = Config.getTextureManager();
      if(this.usePlayerTexture) {
         texturemanager.func_110577_a(p_render_2_.func_110306_p());
      } else if(this.textureLocation != null) {
         if(this.texture == null && this.textureImage != null) {
            this.texture = new DynamicTexture(this.textureImage);
            Minecraft.func_71410_x().func_110434_K().func_110579_a(this.textureLocation, this.texture);
         }

         texturemanager.func_110577_a(this.textureLocation);
      } else {
         texturemanager.func_110577_a(this.locationMissing);
      }

      for(int i = 0; i < this.modelRenderers.length; ++i) {
         PlayerItemRenderer playeritemrenderer = this.modelRenderers[i];
         GlStateManager.func_179094_E();
         if(p_render_2_.func_70093_af()) {
            GlStateManager.func_179109_b(0.0F, 0.2F, 0.0F);
         }

         playeritemrenderer.render(p_render_1_, p_render_3_);
         GlStateManager.func_179121_F();
      }

   }

   public static ModelRenderer getAttachModel(ModelBiped p_getAttachModel_0_, int p_getAttachModel_1_) {
      switch(p_getAttachModel_1_) {
      case 0:
         return p_getAttachModel_0_.field_78115_e;
      case 1:
         return p_getAttachModel_0_.field_78116_c;
      case 2:
         return p_getAttachModel_0_.field_178724_i;
      case 3:
         return p_getAttachModel_0_.field_178723_h;
      case 4:
         return p_getAttachModel_0_.field_178722_k;
      case 5:
         return p_getAttachModel_0_.field_178721_j;
      default:
         return null;
      }
   }

   public BufferedImage getTextureImage() {
      return this.textureImage;
   }

   public void setTextureImage(BufferedImage p_setTextureImage_1_) {
      this.textureImage = p_setTextureImage_1_;
   }

   public DynamicTexture getTexture() {
      return this.texture;
   }

   public ResourceLocation getTextureLocation() {
      return this.textureLocation;
   }

   public void setTextureLocation(ResourceLocation p_setTextureLocation_1_) {
      this.textureLocation = p_setTextureLocation_1_;
   }

   public boolean isUsePlayerTexture() {
      return this.usePlayerTexture;
   }
}
