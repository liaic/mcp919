package net.minecraft.client.renderer.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.MathHelper;
import optifine.Config;
import shadersmod.client.ShadersTex;

public class TextureClock extends TextureAtlasSprite {

   public double currentAngle;
   public double angleDelta;
   public static final String __OBFID = "CL_00001070";


   public TextureClock(String p_i1285_1_) {
      super(p_i1285_1_);
   }

   public void updateAnimation() {
      if(!this.framesTextureData.isEmpty()) {
         Minecraft var1 = Minecraft.getMinecraft();
         double var2 = 0.0D;
         if(var1.theWorld != null && var1.thePlayer != null) {
            var2 = (double)var1.theWorld.getCelestialAngle(1.0F);
            if(!var1.theWorld.provider.isSurfaceWorld()) {
               var2 = Math.random();
            }
         }

         double var4;
         for(var4 = var2 - this.currentAngle; var4 < -0.5D; ++var4) {
            ;
         }

         while(var4 >= 0.5D) {
            --var4;
         }

         var4 = MathHelper.clamp_double(var4, -1.0D, 1.0D);
         this.angleDelta += var4 * 0.1D;
         this.angleDelta *= 0.8D;
         this.currentAngle += this.angleDelta;

         int var6;
         for(var6 = (int)((this.currentAngle + 1.0D) * (double)this.framesTextureData.size()) % this.framesTextureData.size(); var6 < 0; var6 = (var6 + this.framesTextureData.size()) % this.framesTextureData.size()) {
            ;
         }

         if(var6 != this.frameCounter) {
            this.frameCounter = var6;
            if(Config.isShaders()) {
               ShadersTex.uploadTexSub((int[][])((int[][])this.framesTextureData.get(this.frameCounter)), this.width, this.height, this.originX, this.originY, false, false);
            } else {
               TextureUtil.uploadTextureMipmap((int[][])((int[][])this.framesTextureData.get(this.frameCounter)), this.width, this.height, this.originX, this.originY, false, false);
            }
         }
      }

   }
}
