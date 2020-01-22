package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;
import optifine.CapeUtils;
import optifine.Config;
import optifine.PlayerConfigurations;
import optifine.Reflector;

public abstract class AbstractClientPlayer extends EntityPlayer {

   public NetworkPlayerInfo playerInfo;
   public ResourceLocation locationOfCape = null;
   public String nameClear = null;
   public static final String __OBFID = "CL_00000935";


   public AbstractClientPlayer(World worldIn, GameProfile p_i45074_2_) {
      super(worldIn, p_i45074_2_);
      this.nameClear = p_i45074_2_.getName();
      if(this.nameClear != null && !this.nameClear.isEmpty()) {
         this.nameClear = StringUtils.stripControlCodes(this.nameClear);
      }

      CapeUtils.downloadCape(this);
      PlayerConfigurations.getPlayerConfiguration(this);
   }

   public boolean isSpectator() {
      NetworkPlayerInfo var1 = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(this.getGameProfile().getId());
      return var1 != null && var1.getGameType() == GameType.SPECTATOR;
   }

   public boolean hasPlayerInfo() {
      return this.getPlayerInfo() != null;
   }

   public NetworkPlayerInfo getPlayerInfo() {
      if(this.playerInfo == null) {
         this.playerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(this.getUniqueID());
      }

      return this.playerInfo;
   }

   public boolean hasSkin() {
      NetworkPlayerInfo var1 = this.getPlayerInfo();
      return var1 != null && var1.hasLocationSkin();
   }

   public ResourceLocation getLocationSkin() {
      NetworkPlayerInfo var1 = this.getPlayerInfo();
      return var1 == null?DefaultPlayerSkin.getDefaultSkin(this.getUniqueID()):var1.getLocationSkin();
   }

   public ResourceLocation getLocationCape() {
      if(!Config.isShowCapes()) {
         return null;
      } else if(this.locationOfCape != null) {
         return this.locationOfCape;
      } else {
         NetworkPlayerInfo var1 = this.getPlayerInfo();
         return var1 == null?null:var1.getLocationCape();
      }
   }

   public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
      TextureManager var2 = Minecraft.getMinecraft().getTextureManager();
      Object var3 = var2.getTexture(resourceLocationIn);
      if(var3 == null) {
         var3 = new ThreadDownloadImageData((File)null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", new Object[]{StringUtils.stripControlCodes(username)}), DefaultPlayerSkin.getDefaultSkin(getOfflineUUID(username)), new ImageBufferDownload());
         var2.loadTexture(resourceLocationIn, (ITextureObject)var3);
      }

      return (ThreadDownloadImageData)var3;
   }

   public static ResourceLocation getLocationSkin(String username) {
      return new ResourceLocation("skins/" + StringUtils.stripControlCodes(username));
   }

   public String getSkinType() {
      NetworkPlayerInfo var1 = this.getPlayerInfo();
      return var1 == null?DefaultPlayerSkin.getSkinType(this.getUniqueID()):var1.getSkinType();
   }

   public float getFovModifier() {
      float var1 = 1.0F;
      if(this.capabilities.isFlying) {
         var1 *= 1.1F;
      }

      IAttributeInstance var2 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
      var1 = (float)((double)var1 * ((var2.getAttributeValue() / (double)this.capabilities.getWalkSpeed() + 1.0D) / 2.0D));
      if(this.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(var1) || Float.isInfinite(var1)) {
         var1 = 1.0F;
      }

      if(this.isUsingItem() && this.getItemInUse().getItem() == Items.bow) {
         int var3 = this.getItemInUseDuration();
         float var4 = (float)var3 / 20.0F;
         if(var4 > 1.0F) {
            var4 = 1.0F;
         } else {
            var4 *= var4;
         }

         var1 *= 1.0F - var4 * 0.15F;
      }

      return Reflector.ForgeHooksClient_getOffsetFOV.exists()?Reflector.callFloat(Reflector.ForgeHooksClient_getOffsetFOV, new Object[]{this, Float.valueOf(var1)}):var1;
   }

   public String getNameClear() {
      return this.nameClear;
   }

   public ResourceLocation getLocationOfCape() {
      return this.locationOfCape;
   }

   public void setLocationOfCape(ResourceLocation locationOfCape) {
      this.locationOfCape = locationOfCape;
   }
}
