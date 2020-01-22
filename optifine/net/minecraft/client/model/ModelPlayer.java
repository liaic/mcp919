package net.minecraft.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelPlayer extends ModelBiped {

   public ModelRenderer bipedLeftArmwear;
   public ModelRenderer bipedRightArmwear;
   public ModelRenderer bipedLeftLegwear;
   public ModelRenderer bipedRightLegwear;
   public ModelRenderer bipedBodyWear;
   public ModelRenderer bipedCape;
   public ModelRenderer bipedDeadmau5Head;
   public boolean smallArms;
   public static final String __OBFID = "CL_00002626";


   public ModelPlayer(float p_i46304_1_, boolean p_i46304_2_) {
      super(p_i46304_1_, 0.0F, 64, 64);
      this.smallArms = p_i46304_2_;
      this.bipedDeadmau5Head = new ModelRenderer(this, 24, 0);
      this.bipedDeadmau5Head.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, p_i46304_1_);
      this.bipedCape = new ModelRenderer(this, 0, 0);
      this.bipedCape.setTextureSize(64, 32);
      this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, p_i46304_1_);
      if(p_i46304_2_) {
         this.bipedLeftArm = new ModelRenderer(this, 32, 48);
         this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, p_i46304_1_);
         this.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);
         this.bipedRightArm = new ModelRenderer(this, 40, 16);
         this.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, p_i46304_1_);
         this.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);
         this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
         this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, p_i46304_1_ + 0.25F);
         this.bipedLeftArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);
         this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
         this.bipedRightArmwear.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, p_i46304_1_ + 0.25F);
         this.bipedRightArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);
      } else {
         this.bipedLeftArm = new ModelRenderer(this, 32, 48);
         this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, p_i46304_1_);
         this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
         this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
         this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, p_i46304_1_ + 0.25F);
         this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
         this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
         this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, p_i46304_1_ + 0.25F);
         this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
      }

      this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
      this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, p_i46304_1_);
      this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
      this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
      this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, p_i46304_1_ + 0.25F);
      this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);
      this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
      this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, p_i46304_1_ + 0.25F);
      this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);
      this.bipedBodyWear = new ModelRenderer(this, 16, 32);
      this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, p_i46304_1_ + 0.25F);
      this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);
   }

   public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
      super.render(p_78088_1_, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_);
      GlStateManager.pushMatrix();
      if(this.isChild) {
         float var8 = 2.0F;
         GlStateManager.scale(1.0F / var8, 1.0F / var8, 1.0F / var8);
         GlStateManager.translate(0.0F, 24.0F * p_78088_7_, 0.0F);
         this.bipedLeftLegwear.render(p_78088_7_);
         this.bipedRightLegwear.render(p_78088_7_);
         this.bipedLeftArmwear.render(p_78088_7_);
         this.bipedRightArmwear.render(p_78088_7_);
         this.bipedBodyWear.render(p_78088_7_);
      } else {
         if(p_78088_1_.isSneaking()) {
            GlStateManager.translate(0.0F, 0.2F, 0.0F);
         }

         this.bipedLeftLegwear.render(p_78088_7_);
         this.bipedRightLegwear.render(p_78088_7_);
         this.bipedLeftArmwear.render(p_78088_7_);
         this.bipedRightArmwear.render(p_78088_7_);
         this.bipedBodyWear.render(p_78088_7_);
      }

      GlStateManager.popMatrix();
   }

   public void renderDeadmau5Head(float p_178727_1_) {
      copyModelAngles(this.bipedHead, this.bipedDeadmau5Head);
      this.bipedDeadmau5Head.rotationPointX = 0.0F;
      this.bipedDeadmau5Head.rotationPointY = 0.0F;
      this.bipedDeadmau5Head.render(p_178727_1_);
   }

   public void renderCape(float p_178728_1_) {
      this.bipedCape.render(p_178728_1_);
   }

   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
      super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
      copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
      copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
      copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
      copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
      copyModelAngles(this.bipedBody, this.bipedBodyWear);
   }

   public void renderRightArm() {
      this.bipedRightArm.render(0.0625F);
      this.bipedRightArmwear.render(0.0625F);
   }

   public void renderLeftArm() {
      this.bipedLeftArm.render(0.0625F);
      this.bipedLeftArmwear.render(0.0625F);
   }

   public void setInvisible(boolean p_178719_1_) {
      super.setInvisible(p_178719_1_);
      this.bipedLeftArmwear.showModel = p_178719_1_;
      this.bipedRightArmwear.showModel = p_178719_1_;
      this.bipedLeftLegwear.showModel = p_178719_1_;
      this.bipedRightLegwear.showModel = p_178719_1_;
      this.bipedBodyWear.showModel = p_178719_1_;
      this.bipedCape.showModel = p_178719_1_;
      this.bipedDeadmau5Head.showModel = p_178719_1_;
   }

   public void postRenderArm(float p_178718_1_) {
      if(this.smallArms) {
         ++this.bipedRightArm.rotationPointX;
         this.bipedRightArm.postRender(p_178718_1_);
         --this.bipedRightArm.rotationPointX;
      } else {
         this.bipedRightArm.postRender(p_178718_1_);
      }

   }
}
