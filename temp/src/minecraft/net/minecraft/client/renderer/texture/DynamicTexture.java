package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.src.Config;
import shadersmod.client.ShadersTex;

public class DynamicTexture extends AbstractTexture {
   private final int[] field_110566_b;
   private final int field_94233_j;
   private final int field_94234_k;
   private static final String __OBFID = "CL_00001048";
   private boolean shadersInitialized;

   public DynamicTexture(BufferedImage p_i1270_1_) {
      this(p_i1270_1_.getWidth(), p_i1270_1_.getHeight());
      p_i1270_1_.getRGB(0, 0, p_i1270_1_.getWidth(), p_i1270_1_.getHeight(), this.field_110566_b, 0, p_i1270_1_.getWidth());
      this.func_110564_a();
   }

   public DynamicTexture(int p_i1271_1_, int p_i1271_2_) {
      this.shadersInitialized = false;
      this.field_94233_j = p_i1271_1_;
      this.field_94234_k = p_i1271_2_;
      this.field_110566_b = new int[p_i1271_1_ * p_i1271_2_ * 3];
      if(Config.isShaders()) {
         ShadersTex.initDynamicTexture(this.func_110552_b(), p_i1271_1_, p_i1271_2_, this);
         this.shadersInitialized = true;
      } else {
         TextureUtil.func_110991_a(this.func_110552_b(), p_i1271_1_, p_i1271_2_);
      }

   }

   public void func_110551_a(IResourceManager p_110551_1_) throws IOException {
   }

   public void func_110564_a() {
      if(Config.isShaders()) {
         if(!this.shadersInitialized) {
            ShadersTex.initDynamicTexture(this.func_110552_b(), this.field_94233_j, this.field_94234_k, this);
            this.shadersInitialized = true;
         }

         ShadersTex.updateDynamicTexture(this.func_110552_b(), this.field_110566_b, this.field_94233_j, this.field_94234_k, this);
      } else {
         TextureUtil.func_110988_a(this.func_110552_b(), this.field_110566_b, this.field_94233_j, this.field_94234_k);
      }

   }

   public int[] func_110565_c() {
      return this.field_110566_b;
   }
}
