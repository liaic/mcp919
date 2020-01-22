package net.minecraft.src;

import java.util.Map;
import java.util.Set;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.src.Config;
import net.minecraft.src.PlayerConfigurations;

public class PlayerItemsLayer implements LayerRenderer {
   private RenderPlayer renderPlayer = null;

   public PlayerItemsLayer(RenderPlayer p_i84_1_) {
      this.renderPlayer = p_i84_1_;
   }

   public void func_177141_a(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
      this.renderEquippedItems(p_177141_1_, p_177141_8_, p_177141_4_);
   }

   protected void renderEquippedItems(EntityLivingBase p_renderEquippedItems_1_, float p_renderEquippedItems_2_, float p_renderEquippedItems_3_) {
      if(Config.isShowCapes()) {
         if(p_renderEquippedItems_1_ instanceof AbstractClientPlayer) {
            AbstractClientPlayer abstractclientplayer = (AbstractClientPlayer)p_renderEquippedItems_1_;
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.func_179101_C();
            GlStateManager.func_179089_o();
            ModelBiped modelbiped = (ModelBiped)this.renderPlayer.func_177087_b();
            PlayerConfigurations.renderPlayerItems(modelbiped, abstractclientplayer, p_renderEquippedItems_2_, p_renderEquippedItems_3_);
            GlStateManager.func_179129_p();
         }
      }
   }

   public boolean func_177142_b() {
      return false;
   }

   public static void register(Map p_register_0_) {
      Set set = p_register_0_.keySet();
      boolean flag = false;

      for(Object object : set) {
         Object object1 = p_register_0_.get(object);
         if(object1 instanceof RenderPlayer) {
            RenderPlayer renderplayer = (RenderPlayer)object1;
            renderplayer.func_177094_a(new PlayerItemsLayer(renderplayer));
            flag = true;
         }
      }

      if(!flag) {
         Config.warn("PlayerItemsLayer not registered");
      }

   }
}
