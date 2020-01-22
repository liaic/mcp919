package net.minecraft.client.renderer.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import optifine.Config;
import shadersmod.client.ShadersTex;

public class TextureCompass extends TextureAtlasSprite {

   public double currentAngle;
   public double angleDelta;
   public static String locationSprite;
   public static final String __OBFID = "CL_00001071";


   public TextureCompass(String p_i1286_1_) {
      super(p_i1286_1_);
      locationSprite = p_i1286_1_;
   }

   public void updateAnimation() {
      Minecraft var1 = Minecraft.getMinecraft();
      if(var1.theWorld != null && var1.thePlayer != null) {
         this.updateCompass(var1.theWorld, var1.thePlayer.posX, var1.thePlayer.posZ, (double)var1.thePlayer.rotationYaw, false, false);
      } else {
         this.updateCompass((World)null, 0.0D, 0.0D, 0.0D, true, false);
      }

   }

   public void updateCompass(World worldIn, double p_94241_2_, double p_94241_4_, double p_94241_6_, boolean p_94241_8_, boolean p_94241_9_) {
      if(!this.framesTextureData.isEmpty()) {
         double var10 = 0.0D;
         if(worldIn != null && !p_94241_8_) {
            BlockPos var18 = worldIn.getSpawnPoint();
            double var13 = (double)var18.getX() - p_94241_2_;
            double var15 = (double)var18.getZ() - p_94241_4_;
            p_94241_6_ %= 360.0D;
            var10 = -((p_94241_6_ - 90.0D) * 3.141592653589793D / 180.0D - Math.atan2(var15, var13));
            if(!worldIn.provider.isSurfaceWorld()) {
               var10 = Math.random() * 3.141592653589793D * 2.0D;
            }
         }

         if(p_94241_9_) {
            this.currentAngle = var10;
         } else {
            double var17;
            for(var17 = var10 - this.currentAngle; var17 < -3.141592653589793D; var17 += 6.283185307179586D) {
               ;
            }

            while(var17 >= 3.141592653589793D) {
               var17 -= 6.283185307179586D;
            }

            var17 = MathHelper.clamp_double(var17, -1.0D, 1.0D);
            this.angleDelta += var17 * 0.1D;
            this.angleDelta *= 0.8D;
            this.currentAngle += this.angleDelta;
         }

         int var181;
         for(var181 = (int)((this.currentAngle / 6.283185307179586D + 1.0D) * (double)this.framesTextureData.size()) % this.framesTextureData.size(); var181 < 0; var181 = (var181 + this.framesTextureData.size()) % this.framesTextureData.size()) {
            ;
         }

         if(var181 != this.frameCounter) {
            this.frameCounter = var181;
            if(Config.isShaders()) {
               ShadersTex.uploadTexSub((int[][])((int[][])this.framesTextureData.get(this.frameCounter)), this.width, this.height, this.originX, this.originY, false, false);
            } else {
               TextureUtil.uploadTextureMipmap((int[][])((int[][])this.framesTextureData.get(this.frameCounter)), this.width, this.height, this.originX, this.originY, false, false);
            }
         }
      }

   }
}
