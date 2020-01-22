package net.minecraft.client.renderer.entity.layers;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.CustomItems;
import optifine.Reflector;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersRender;

public abstract class LayerArmorBase implements LayerRenderer {

   public static final ResourceLocation ENCHANTED_ITEM_GLINT_RES = new ResourceLocation("textures/misc/enchanted_item_glint.png");
   public ModelBase modelLeggings;
   public ModelBase modelArmor;
   public final RendererLivingEntity renderer;
   public float alpha = 1.0F;
   public float colorR = 1.0F;
   public float colorG = 1.0F;
   public float colorB = 1.0F;
   public boolean skipRenderGlint;
   public static final Map ARMOR_TEXTURE_RES_MAP = Maps.newHashMap();
   public static final String __OBFID = "CL_00002428";


   public LayerArmorBase(RendererLivingEntity p_i46125_1_) {
      this.renderer = p_i46125_1_;
      this.initArmor();
   }

   public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
      this.renderLayer(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_, 4);
      this.renderLayer(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_, 3);
      this.renderLayer(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_, 2);
      this.renderLayer(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_, 1);
   }

   public boolean shouldCombineTextures() {
      return false;
   }

   public void renderLayer(EntityLivingBase p_177182_1_, float p_177182_2_, float p_177182_3_, float p_177182_4_, float p_177182_5_, float p_177182_6_, float p_177182_7_, float p_177182_8_, int p_177182_9_) {
      ItemStack var10 = this.getCurrentArmor(p_177182_1_, p_177182_9_);
      if(var10 != null && var10.getItem() instanceof ItemArmor) {
         ItemArmor var11 = (ItemArmor)var10.getItem();
         ModelBase var12 = this.getArmorModel(p_177182_9_);
         var12.setModelAttributes(this.renderer.getMainModel());
         var12.setLivingAnimations(p_177182_1_, p_177182_2_, p_177182_3_, p_177182_4_);
         if(Reflector.ForgeHooksClient.exists()) {
            var12 = this.getArmorModelHook(p_177182_1_, var10, p_177182_9_, var12);
         }

         this.setModelPartVisible(var12, p_177182_9_);
         boolean var13 = this.isSlotForLeggings(p_177182_9_);
         if(!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(var10, var13?2:1, (String)null)) {
            if(Reflector.ForgeHooksClient_getArmorTexture.exists()) {
               this.renderer.bindTexture(this.getArmorResource(p_177182_1_, var10, var13?2:1, (String)null));
            } else {
               this.renderer.bindTexture(this.getArmorResource(var11, var13));
            }
         }

         int var14;
         float var15;
         float var16;
         float var17;
         if(Reflector.ForgeHooksClient_getArmorTexture.exists()) {
            var14 = var11.getColor(var10);
            if(var14 != -1) {
               var15 = (float)(var14 >> 16 & 255) / 255.0F;
               var16 = (float)(var14 >> 8 & 255) / 255.0F;
               var17 = (float)(var14 & 255) / 255.0F;
               GlStateManager.color(this.colorR * var15, this.colorG * var16, this.colorB * var17, this.alpha);
               var12.render(p_177182_1_, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
               if(!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(var10, var13?2:1, "overlay")) {
                  this.renderer.bindTexture(this.getArmorResource(p_177182_1_, var10, var13?2:1, "overlay"));
               }
            }

            GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
            var12.render(p_177182_1_, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
            if(!this.skipRenderGlint && var10.isItemEnchanted() && (!Config.isCustomItems() || !CustomItems.renderCustomArmorEffect(p_177182_1_, var10, var12, p_177182_2_, p_177182_3_, p_177182_4_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_))) {
               this.renderGlint(p_177182_1_, var12, p_177182_2_, p_177182_3_, p_177182_4_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
            }

            return;
         }

         switch(LayerArmorBase.SwitchArmorMaterial.field_178747_a[var11.getArmorMaterial().ordinal()]) {
         case 1:
            var14 = var11.getColor(var10);
            var15 = (float)(var14 >> 16 & 255) / 255.0F;
            var16 = (float)(var14 >> 8 & 255) / 255.0F;
            var17 = (float)(var14 & 255) / 255.0F;
            GlStateManager.color(this.colorR * var15, this.colorG * var16, this.colorB * var17, this.alpha);
            var12.render(p_177182_1_, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
            if(!Config.isCustomItems() || !CustomItems.bindCustomArmorTexture(var10, var13?2:1, "overlay")) {
               this.renderer.bindTexture(this.getArmorResource(var11, var13, "overlay"));
            }
         case 2:
         case 3:
         case 4:
         case 5:
            GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);
            var12.render(p_177182_1_, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
         default:
            if(!this.skipRenderGlint && var10.isItemEnchanted() && (!Config.isCustomItems() || !CustomItems.renderCustomArmorEffect(p_177182_1_, var10, var12, p_177182_2_, p_177182_3_, p_177182_4_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_))) {
               this.renderGlint(p_177182_1_, var12, p_177182_2_, p_177182_3_, p_177182_4_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
            }
         }
      }

   }

   public ItemStack getCurrentArmor(EntityLivingBase p_177176_1_, int p_177176_2_) {
      return p_177176_1_.getCurrentArmor(p_177176_2_ - 1);
   }

   public ModelBase getArmorModel(int p_177175_1_) {
      return this.isSlotForLeggings(p_177175_1_)?this.modelLeggings:this.modelArmor;
   }

   public boolean isSlotForLeggings(int p_177180_1_) {
      return p_177180_1_ == 2;
   }

   public void renderGlint(EntityLivingBase p_177183_1_, ModelBase p_177183_2_, float p_177183_3_, float p_177183_4_, float p_177183_5_, float p_177183_6_, float p_177183_7_, float p_177183_8_, float p_177183_9_) {
      if(!Config.isCustomItems() || CustomItems.isUseGlint()) {
         if(!Config.isShaders() || !Shaders.isShadowPass) {
            float var10 = (float)p_177183_1_.ticksExisted + p_177183_5_;
            this.renderer.bindTexture(ENCHANTED_ITEM_GLINT_RES);
            if(Config.isShaders()) {
               ShadersRender.renderEnchantedGlintBegin();
            }

            GlStateManager.enableBlend();
            GlStateManager.depthFunc(514);
            GlStateManager.depthMask(false);
            float var11 = 0.5F;
            GlStateManager.color(var11, var11, var11, 1.0F);

            for(int var12 = 0; var12 < 2; ++var12) {
               GlStateManager.disableLighting();
               GlStateManager.blendFunc(768, 1);
               float var13 = 0.76F;
               GlStateManager.color(0.5F * var13, 0.25F * var13, 0.8F * var13, 1.0F);
               GlStateManager.matrixMode(5890);
               GlStateManager.loadIdentity();
               float var14 = 0.33333334F;
               GlStateManager.scale(var14, var14, var14);
               GlStateManager.rotate(30.0F - (float)var12 * 60.0F, 0.0F, 0.0F, 1.0F);
               GlStateManager.translate(0.0F, var10 * (0.001F + (float)var12 * 0.003F) * 20.0F, 0.0F);
               GlStateManager.matrixMode(5888);
               p_177183_2_.render(p_177183_1_, p_177183_3_, p_177183_4_, p_177183_6_, p_177183_7_, p_177183_8_, p_177183_9_);
            }

            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode(5888);
            GlStateManager.enableLighting();
            GlStateManager.depthMask(true);
            GlStateManager.depthFunc(515);
            GlStateManager.disableBlend();
            if(Config.isShaders()) {
               ShadersRender.renderEnchantedGlintEnd();
            }

         }
      }
   }

   public ResourceLocation getArmorResource(ItemArmor p_177181_1_, boolean p_177181_2_) {
      return this.getArmorResource(p_177181_1_, p_177181_2_, (String)null);
   }

   public ResourceLocation getArmorResource(ItemArmor p_177178_1_, boolean p_177178_2_, String p_177178_3_) {
      String var4 = String.format("textures/models/armor/%s_layer_%d%s.png", new Object[]{p_177178_1_.getArmorMaterial().getName(), Integer.valueOf(p_177178_2_?2:1), p_177178_3_ == null?"":String.format("_%s", new Object[]{p_177178_3_})});
      ResourceLocation var5 = (ResourceLocation)ARMOR_TEXTURE_RES_MAP.get(var4);
      if(var5 == null) {
         var5 = new ResourceLocation(var4);
         ARMOR_TEXTURE_RES_MAP.put(var4, var5);
      }

      return var5;
   }

   public abstract void initArmor();

   public abstract void setModelPartVisible(ModelBase var1, int var2);

   public ModelBase getArmorModelHook(EntityLivingBase entity, ItemStack itemStack, int slot, ModelBase model) {
      return model;
   }

   public ResourceLocation getArmorResource(Entity entity, ItemStack stack, int slot, String type) {
      ItemArmor item = (ItemArmor)stack.getItem();
      String texture = item.getArmorMaterial().getName();
      String domain = "minecraft";
      int idx = texture.indexOf(58);
      if(idx != -1) {
         domain = texture.substring(0, idx);
         texture = texture.substring(idx + 1);
      }

      String s1 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", new Object[]{domain, texture, Integer.valueOf(slot == 2?2:1), type == null?"":String.format("_%s", new Object[]{type})});
      s1 = Reflector.callString(Reflector.ForgeHooksClient_getArmorTexture, new Object[]{entity, stack, s1, Integer.valueOf(slot), type});
      ResourceLocation resourcelocation = (ResourceLocation)ARMOR_TEXTURE_RES_MAP.get(s1);
      if(resourcelocation == null) {
         resourcelocation = new ResourceLocation(s1);
         ARMOR_TEXTURE_RES_MAP.put(s1, resourcelocation);
      }

      return resourcelocation;
   }


   public static final class SwitchArmorMaterial {

      public static final int[] field_178747_a = new int[ArmorMaterial.values().length];
      public static final String __OBFID = "CL_00002427";


      static {
         try {
            field_178747_a[ArmorMaterial.LEATHER.ordinal()] = 1;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            field_178747_a[ArmorMaterial.CHAIN.ordinal()] = 2;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            field_178747_a[ArmorMaterial.IRON.ordinal()] = 3;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            field_178747_a[ArmorMaterial.GOLD.ordinal()] = 4;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            field_178747_a[ArmorMaterial.DIAMOND.ordinal()] = 5;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
