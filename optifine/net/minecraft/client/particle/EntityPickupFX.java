package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import optifine.Config;
import shadersmod.client.Shaders;

public class EntityPickupFX extends EntityFX {

   public Entity field_174840_a;
   public Entity field_174843_ax;
   public int age;
   public int maxAge;
   public float field_174841_aA;
   public RenderManager field_174842_aB = Minecraft.getMinecraft().getRenderManager();
   public static final String __OBFID = "CL_00000930";


   public EntityPickupFX(World worldIn, Entity p_i1233_2_, Entity p_i1233_3_, float p_i1233_4_) {
      super(worldIn, p_i1233_2_.posX, p_i1233_2_.posY, p_i1233_2_.posZ, p_i1233_2_.motionX, p_i1233_2_.motionY, p_i1233_2_.motionZ);
      this.field_174840_a = p_i1233_2_;
      this.field_174843_ax = p_i1233_3_;
      this.maxAge = 3;
      this.field_174841_aA = p_i1233_4_;
   }

   public void renderParticle(WorldRenderer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_) {
      int oldShadersProgram = 0;
      if(Config.isShaders()) {
         oldShadersProgram = Shaders.activeProgram;
         Shaders.nextEntity(this.field_174840_a);
      }

      float var9 = ((float)this.age + p_180434_3_) / (float)this.maxAge;
      var9 *= var9;
      double var10 = this.field_174840_a.posX;
      double var12 = this.field_174840_a.posY;
      double var14 = this.field_174840_a.posZ;
      double var16 = this.field_174843_ax.lastTickPosX + (this.field_174843_ax.posX - this.field_174843_ax.lastTickPosX) * (double)p_180434_3_;
      double var18 = this.field_174843_ax.lastTickPosY + (this.field_174843_ax.posY - this.field_174843_ax.lastTickPosY) * (double)p_180434_3_ + (double)this.field_174841_aA;
      double var20 = this.field_174843_ax.lastTickPosZ + (this.field_174843_ax.posZ - this.field_174843_ax.lastTickPosZ) * (double)p_180434_3_;
      double var22 = var10 + (var16 - var10) * (double)var9;
      double var24 = var12 + (var18 - var12) * (double)var9;
      double var26 = var14 + (var20 - var14) * (double)var9;
      int var28 = this.getBrightnessForRender(p_180434_3_);
      int var29 = var28 % 65536;
      int var30 = var28 / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var29 / 1.0F, (float)var30 / 1.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      var22 -= interpPosX;
      var24 -= interpPosY;
      var26 -= interpPosZ;
      this.field_174842_aB.renderEntityWithPosYaw(this.field_174840_a, (double)((float)var22), (double)((float)var24), (double)((float)var26), this.field_174840_a.rotationYaw, p_180434_3_);
      if(Config.isShaders()) {
         Shaders.useProgram(oldShadersProgram);
      }

   }

   public void onUpdate() {
      ++this.age;
      if(this.age == this.maxAge) {
         this.setDead();
      }

   }

   public int getFXLayer() {
      return 3;
   }
}
