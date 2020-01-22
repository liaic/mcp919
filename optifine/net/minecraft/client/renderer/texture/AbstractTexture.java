package net.minecraft.client.renderer.texture;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import org.lwjgl.opengl.GL11;
import shadersmod.client.MultiTexID;
import shadersmod.client.ShadersTex;

public abstract class AbstractTexture implements ITextureObject {

   public int glTextureId = -1;
   public boolean blur;
   public boolean mipmap;
   public boolean blurLast;
   public boolean mipmapLast;
   public static final String __OBFID = "CL_00001047";
   public MultiTexID multiTex;


   public void setBlurMipmapDirect(boolean p_174937_1_, boolean p_174937_2_) {
      this.blur = p_174937_1_;
      this.mipmap = p_174937_2_;
      boolean var3 = true;
      boolean var4 = true;
      int var5;
      short var6;
      if(p_174937_1_) {
         var5 = p_174937_2_?9987:9729;
         var6 = 9729;
      } else {
         var5 = p_174937_2_?9986:9728;
         var6 = 9728;
      }

      GlStateManager.bindTexture(this.getGlTextureId());
      GL11.glTexParameteri(3553, 10241, var5);
      GL11.glTexParameteri(3553, 10240, var6);
   }

   public void setBlurMipmap(boolean p_174936_1_, boolean p_174936_2_) {
      this.blurLast = this.blur;
      this.mipmapLast = this.mipmap;
      this.setBlurMipmapDirect(p_174936_1_, p_174936_2_);
   }

   public void restoreLastBlurMipmap() {
      this.setBlurMipmapDirect(this.blurLast, this.mipmapLast);
   }

   public int getGlTextureId() {
      if(this.glTextureId == -1) {
         this.glTextureId = TextureUtil.glGenTextures();
      }

      return this.glTextureId;
   }

   public void deleteGlTexture() {
      ShadersTex.deleteTextures(this, this.glTextureId);
      if(this.glTextureId != -1) {
         TextureUtil.deleteTexture(this.glTextureId);
         this.glTextureId = -1;
      }

   }

   public MultiTexID getMultiTexID() {
      return ShadersTex.getMultiTexID(this);
   }
}
