package net.minecraft.client.renderer.entity;

import java.util.List;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt.DirtType;
import net.minecraft.block.BlockDoublePlant.EnumPlantType;
import net.minecraft.block.BlockFlower.EnumFlowerType;
import net.minecraft.block.BlockWall.EnumType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemFishFood.FishType;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3i;
import optifine.Config;
import optifine.CustomColors;
import optifine.CustomItems;
import optifine.Reflector;
import shadersmod.client.Shaders;
import shadersmod.client.ShadersRender;

public class RenderItem implements IResourceManagerReloadListener {

   public static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
   public boolean notRenderingEffectsInGUI = true;
   public float zLevel;
   public final ItemModelMesher itemModelMesher;
   public final TextureManager textureManager;
   public static final String __OBFID = "CL_00001003";
   public ModelResourceLocation modelLocation = null;
   public boolean renderItemGui = false;
   public ModelManager modelManager = null;


   public RenderItem(TextureManager p_i46165_1_, ModelManager p_i46165_2_) {
      this.textureManager = p_i46165_1_;
      this.modelManager = p_i46165_2_;
      if(Reflector.ItemModelMesherForge_Constructor.exists()) {
         this.itemModelMesher = (ItemModelMesher)Reflector.newInstance(Reflector.ItemModelMesherForge_Constructor, new Object[]{p_i46165_2_});
      } else {
         this.itemModelMesher = new ItemModelMesher(p_i46165_2_);
      }

      this.registerItems();
   }

   public void isNotRenderingEffectsInGUI(boolean p_175039_1_) {
      this.notRenderingEffectsInGUI = p_175039_1_;
   }

   public ItemModelMesher getItemModelMesher() {
      return this.itemModelMesher;
   }

   public void registerItem(Item p_175048_1_, int p_175048_2_, String p_175048_3_) {
      this.itemModelMesher.register(p_175048_1_, p_175048_2_, new ModelResourceLocation(p_175048_3_, "inventory"));
   }

   public void registerBlock(Block p_175029_1_, int p_175029_2_, String p_175029_3_) {
      this.registerItem(Item.getItemFromBlock(p_175029_1_), p_175029_2_, p_175029_3_);
   }

   public void registerBlock(Block p_175031_1_, String p_175031_2_) {
      this.registerBlock(p_175031_1_, 0, p_175031_2_);
   }

   public void registerItem(Item p_175047_1_, String p_175047_2_) {
      this.registerItem(p_175047_1_, 0, p_175047_2_);
   }

   public void renderModel(IBakedModel p_175036_1_, ItemStack p_175036_2_) {
      this.renderModel(p_175036_1_, -1, p_175036_2_);
   }

   public void renderModel(IBakedModel p_175035_1_, int p_175035_2_) {
      this.renderModel(p_175035_1_, p_175035_2_, (ItemStack)null);
   }

   public void renderModel(IBakedModel p_175045_1_, int p_175045_2_, ItemStack p_175045_3_) {
      Tessellator var4 = Tessellator.getInstance();
      WorldRenderer var5 = var4.getWorldRenderer();
      boolean renderTextureMap = Minecraft.getMinecraft().getTextureMapBlocks().isTextureBound();
      boolean multiTexture = Config.isMultiTexture() && renderTextureMap;
      if(multiTexture) {
         var5.setBlockLayer(EnumWorldBlockLayer.SOLID);
      }

      var5.begin(7, DefaultVertexFormats.ITEM);
      EnumFacing[] var6 = EnumFacing.VALUES;
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         EnumFacing var9 = var6[var8];
         this.renderQuads(var5, p_175045_1_.getFaceQuads(var9), p_175045_2_, p_175045_3_);
      }

      this.renderQuads(var5, p_175045_1_.getGeneralQuads(), p_175045_2_, p_175045_3_);
      var4.draw();
      if(multiTexture) {
         var5.setBlockLayer((EnumWorldBlockLayer)null);
         GlStateManager.bindCurrentTexture();
      }

   }

   public void renderItem(ItemStack p_180454_1_, IBakedModel p_180454_2_) {
      if(p_180454_1_ != null) {
         GlStateManager.pushMatrix();
         GlStateManager.scale(0.5F, 0.5F, 0.5F);
         if(p_180454_2_.isBuiltInRenderer()) {
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableRescaleNormal();
            TileEntityItemStackRenderer.instance.renderByItem(p_180454_1_);
         } else {
            if(Config.isCustomItems()) {
               p_180454_2_ = CustomItems.getCustomItemModel(p_180454_1_, p_180454_2_, this.modelLocation);
            }

            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
            this.renderModel(p_180454_2_, p_180454_1_);
            if(p_180454_1_.hasEffect() && (!Config.isCustomItems() || !CustomItems.renderCustomEffect(this, p_180454_1_, p_180454_2_))) {
               this.renderEffect(p_180454_2_);
            }
         }

         GlStateManager.popMatrix();
      }

   }

   public void renderEffect(IBakedModel p_180451_1_) {
      if(!Config.isCustomItems() || CustomItems.isUseGlint()) {
         if(!Config.isShaders() || !Shaders.isShadowPass) {
            GlStateManager.depthMask(false);
            GlStateManager.depthFunc(514);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(768, 1);
            this.textureManager.bindTexture(RES_ITEM_GLINT);
            if(Config.isShaders() && !this.renderItemGui) {
               ShadersRender.renderEnchantedGlintBegin();
            }

            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(8.0F, 8.0F, 8.0F);
            float var2 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
            GlStateManager.translate(var2, 0.0F, 0.0F);
            GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
            this.renderModel(p_180451_1_, -8372020);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.scale(8.0F, 8.0F, 8.0F);
            float var3 = (float)(Minecraft.getSystemTime() % 4873L) / 4873.0F / 8.0F;
            GlStateManager.translate(-var3, 0.0F, 0.0F);
            GlStateManager.rotate(10.0F, 0.0F, 0.0F, 1.0F);
            this.renderModel(p_180451_1_, -8372020);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            GlStateManager.blendFunc(770, 771);
            GlStateManager.enableLighting();
            GlStateManager.depthFunc(515);
            GlStateManager.depthMask(true);
            this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
            if(Config.isShaders() && !this.renderItemGui) {
               ShadersRender.renderEnchantedGlintEnd();
            }

         }
      }
   }

   public void putQuadNormal(WorldRenderer p_175038_1_, BakedQuad p_175038_2_) {
      Vec3i var3 = p_175038_2_.getFace().getDirectionVec();
      p_175038_1_.putNormal((float)var3.getX(), (float)var3.getY(), (float)var3.getZ());
   }

   public void renderQuad(WorldRenderer p_175033_1_, BakedQuad p_175033_2_, int p_175033_3_) {
      if(p_175033_1_.isMultiTexture()) {
         p_175033_1_.addVertexData(p_175033_2_.getVertexDataSingle());
         p_175033_1_.putSprite(p_175033_2_.getSprite());
      } else {
         p_175033_1_.addVertexData(p_175033_2_.getVertexData());
      }

      if(Reflector.IColoredBakedQuad.exists() && Reflector.IColoredBakedQuad.isInstance(p_175033_2_)) {
         forgeHooksClient_putQuadColor(p_175033_1_, p_175033_2_, p_175033_3_);
      } else {
         p_175033_1_.putColor4(p_175033_3_);
      }

      this.putQuadNormal(p_175033_1_, p_175033_2_);
   }

   public void renderQuads(WorldRenderer p_175032_1_, List p_175032_2_, int p_175032_3_, ItemStack p_175032_4_) {
      boolean var5 = p_175032_3_ == -1 && p_175032_4_ != null;
      int var6 = 0;

      for(int var7 = p_175032_2_.size(); var6 < var7; ++var6) {
         BakedQuad var8 = (BakedQuad)p_175032_2_.get(var6);
         int var9 = p_175032_3_;
         if(var5 && var8.hasTintIndex()) {
            var9 = p_175032_4_.getItem().getColorFromItemStack(p_175032_4_, var8.getTintIndex());
            if(Config.isCustomColors()) {
               var9 = CustomColors.getColorFromItemStack(p_175032_4_, var8.getTintIndex(), var9);
            }

            if(EntityRenderer.anaglyphEnable) {
               var9 = TextureUtil.anaglyphColor(var9);
            }

            var9 |= -16777216;
         }

         this.renderQuad(p_175032_1_, var8, var9);
      }

   }

   public boolean shouldRenderItemIn3D(ItemStack p_175050_1_) {
      IBakedModel var2 = this.itemModelMesher.getItemModel(p_175050_1_);
      return var2 == null?false:var2.isGui3d();
   }

   public void preTransform(ItemStack p_175046_1_) {
      IBakedModel var2 = this.itemModelMesher.getItemModel(p_175046_1_);
      Item var3 = p_175046_1_.getItem();
      if(var3 != null) {
         boolean var4 = var2.isGui3d();
         if(!var4) {
            GlStateManager.scale(2.0F, 2.0F, 2.0F);
         }

         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      }

   }

   public void renderItem(ItemStack p_500168_1_, TransformType p_500168_2_) {
      if(p_500168_1_ != null) {
         IBakedModel var3 = this.itemModelMesher.getItemModel(p_500168_1_);
         this.renderItemModelTransform(p_500168_1_, var3, p_500168_2_);
      }

   }

   public void renderItemModelForEntity(ItemStack p_175049_1_, EntityLivingBase p_175049_2_, TransformType p_175049_3_) {
      if(p_175049_1_ != null && p_175049_2_ != null) {
         IBakedModel var4 = this.itemModelMesher.getItemModel(p_175049_1_);
         if(p_175049_2_ instanceof EntityPlayer) {
            EntityPlayer var5 = (EntityPlayer)p_175049_2_;
            Item var6 = p_175049_1_.getItem();
            ModelResourceLocation var7 = null;
            if(var6 == Items.fishing_rod && var5.fishEntity != null) {
               var7 = new ModelResourceLocation("fishing_rod_cast", "inventory");
            } else if(var6 == Items.bow && var5.getItemInUse() != null) {
               int var8 = p_175049_1_.getMaxItemUseDuration() - var5.getItemInUseCount();
               if(var8 >= 18) {
                  var7 = new ModelResourceLocation("bow_pulling_2", "inventory");
               } else if(var8 > 13) {
                  var7 = new ModelResourceLocation("bow_pulling_1", "inventory");
               } else if(var8 > 0) {
                  var7 = new ModelResourceLocation("bow_pulling_0", "inventory");
               }
            } else if(Reflector.ForgeItem_getModel.exists()) {
               var7 = (ModelResourceLocation)Reflector.call(var6, Reflector.ForgeItem_getModel, new Object[]{p_175049_1_, var5, Integer.valueOf(var5.getItemInUseCount())});
            }

            this.modelLocation = var7;
            if(var7 != null) {
               var4 = this.itemModelMesher.getModelManager().getModel(var7);
            }
         }

         this.renderItemModelTransform(p_175049_1_, var4, p_175049_3_);
         this.modelLocation = null;
      }

   }

   public void renderItemModelTransform(ItemStack p_175040_1_, IBakedModel p_175040_2_, TransformType p_175040_3_) {
      this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
      this.textureManager.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
      this.preTransform(p_175040_1_);
      GlStateManager.enableRescaleNormal();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.pushMatrix();
      if(Reflector.ForgeHooksClient_handleCameraTransforms.exists()) {
         p_175040_2_ = (IBakedModel)Reflector.call(Reflector.ForgeHooksClient_handleCameraTransforms, new Object[]{p_175040_2_, p_175040_3_});
      } else {
         ItemCameraTransforms var4 = p_175040_2_.getItemCameraTransforms();
         var4.applyTransform(p_175040_3_);
         if(this.isThereOneNegativeScale(var4.getTransform(p_175040_3_))) {
            GlStateManager.cullFace(1028);
         }
      }

      this.renderItem(p_175040_1_, p_175040_2_);
      GlStateManager.cullFace(1029);
      GlStateManager.popMatrix();
      GlStateManager.disableRescaleNormal();
      GlStateManager.disableBlend();
      this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
      this.textureManager.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
   }

   public boolean isThereOneNegativeScale(ItemTransformVec3f p_500169_1_) {
      return p_500169_1_.scale.x < 0.0F ^ p_500169_1_.scale.y < 0.0F ^ p_500169_1_.scale.z < 0.0F;
   }

   public void renderItemIntoGUI(ItemStack p_175042_1_, int p_175042_2_, int p_175042_3_) {
      this.renderItemGui = true;
      IBakedModel var4 = this.itemModelMesher.getItemModel(p_175042_1_);
      GlStateManager.pushMatrix();
      this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
      this.textureManager.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
      GlStateManager.enableRescaleNormal();
      GlStateManager.enableAlpha();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.enableBlend();
      GlStateManager.blendFunc(770, 771);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.setupGuiTransform(p_175042_2_, p_175042_3_, var4.isGui3d());
      if(Reflector.ForgeHooksClient_handleCameraTransforms.exists()) {
         var4 = (IBakedModel)Reflector.call(Reflector.ForgeHooksClient_handleCameraTransforms, new Object[]{var4, TransformType.GUI});
      } else {
         var4.getItemCameraTransforms().applyTransform(TransformType.GUI);
      }

      this.renderItem(p_175042_1_, var4);
      GlStateManager.disableAlpha();
      GlStateManager.disableRescaleNormal();
      GlStateManager.disableLighting();
      GlStateManager.popMatrix();
      this.textureManager.bindTexture(TextureMap.locationBlocksTexture);
      this.textureManager.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
      this.renderItemGui = false;
   }

   public void setupGuiTransform(int p_180452_1_, int p_180452_2_, boolean p_180452_3_) {
      GlStateManager.translate((float)p_180452_1_, (float)p_180452_2_, 100.0F + this.zLevel);
      GlStateManager.translate(8.0F, 8.0F, 0.0F);
      GlStateManager.scale(1.0F, 1.0F, -1.0F);
      GlStateManager.scale(0.5F, 0.5F, 0.5F);
      if(p_180452_3_) {
         GlStateManager.scale(40.0F, 40.0F, 40.0F);
         GlStateManager.rotate(210.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
         GlStateManager.enableLighting();
      } else {
         GlStateManager.scale(64.0F, 64.0F, 64.0F);
         GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
         GlStateManager.disableLighting();
      }

   }

   public void renderItemAndEffectIntoGUI(final ItemStack p_180450_1_, int p_180450_2_, int p_180450_3_) {
      if(p_180450_1_ != null && p_180450_1_.getItem() != null) {
         this.zLevel += 50.0F;

         try {
            this.renderItemIntoGUI(p_180450_1_, p_180450_2_, p_180450_3_);
         } catch (Throwable var7) {
            CrashReport var5 = CrashReport.makeCrashReport(var7, "Rendering item");
            CrashReportCategory var6 = var5.makeCategory("Item being rendered");
            var6.addCrashSectionCallable("Item Type", new Callable() {

               public static final String __OBFID = "CL_00001004";

               public String call() throws Exception {
                  return String.valueOf(p_180450_1_.getItem());
               }
               // $FF: synthetic method
               // $FF: bridge method
               public Object call() throws Exception {
                  return this.call();
               }
            });
            var6.addCrashSectionCallable("Item Aux", new Callable() {

               public static final String __OBFID = "CL_00001005";

               public String call() throws Exception {
                  return String.valueOf(p_180450_1_.getMetadata());
               }
               // $FF: synthetic method
               // $FF: bridge method
               public Object call() throws Exception {
                  return this.call();
               }
            });
            var6.addCrashSectionCallable("Item NBT", new Callable() {

               public static final String __OBFID = "CL_00001006";

               public String call() throws Exception {
                  return String.valueOf(p_180450_1_.getTagCompound());
               }
               // $FF: synthetic method
               // $FF: bridge method
               public Object call() throws Exception {
                  return this.call();
               }
            });
            var6.addCrashSectionCallable("Item Foil", new Callable() {

               public static final String __OBFID = "CL_00001007";

               public String call() throws Exception {
                  return String.valueOf(p_180450_1_.hasEffect());
               }
               // $FF: synthetic method
               // $FF: bridge method
               public Object call() throws Exception {
                  return this.call();
               }
            });
            throw new ReportedException(var5);
         }

         this.zLevel -= 50.0F;
      }

   }

   public void renderItemOverlays(FontRenderer p_175030_1_, ItemStack p_175030_2_, int p_175030_3_, int p_175030_4_) {
      this.renderItemOverlayIntoGUI(p_175030_1_, p_175030_2_, p_175030_3_, p_175030_4_, (String)null);
   }

   public void renderItemOverlayIntoGUI(FontRenderer p_180453_1_, ItemStack p_180453_2_, int p_180453_3_, int p_180453_4_, String p_180453_5_) {
      if(p_180453_2_ != null) {
         if(p_180453_2_.stackSize != 1 || p_180453_5_ != null) {
            String itemDamaged = p_180453_5_ == null?String.valueOf(p_180453_2_.stackSize):p_180453_5_;
            if(p_180453_5_ == null && p_180453_2_.stackSize < 1) {
               itemDamaged = EnumChatFormatting.RED + String.valueOf(p_180453_2_.stackSize);
            }

            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.disableBlend();
            p_180453_1_.drawStringWithShadow(itemDamaged, (float)(p_180453_3_ + 19 - 2 - p_180453_1_.getStringWidth(itemDamaged)), (float)(p_180453_4_ + 6 + 3), 16777215);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
         }

         boolean itemDamaged1 = p_180453_2_.isItemDamaged();
         if(Reflector.ForgeItem_showDurabilityBar.exists()) {
            itemDamaged1 = Reflector.callBoolean(p_180453_2_.getItem(), Reflector.ForgeItem_showDurabilityBar, new Object[]{p_180453_2_});
         }

         if(itemDamaged1) {
            int var10 = (int)Math.round(13.0D - (double)p_180453_2_.getItemDamage() * 13.0D / (double)p_180453_2_.getMaxDamage());
            int var7 = (int)Math.round(255.0D - (double)p_180453_2_.getItemDamage() * 255.0D / (double)p_180453_2_.getMaxDamage());
            if(Reflector.ForgeItem_getDurabilityForDisplay.exists()) {
               double health = Reflector.callDouble(p_180453_2_.getItem(), Reflector.ForgeItem_getDurabilityForDisplay, new Object[]{p_180453_2_});
               var10 = (int)Math.round(13.0D - health * 13.0D);
               var7 = (int)Math.round(255.0D - health * 255.0D);
            }

            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            Tessellator var8 = Tessellator.getInstance();
            WorldRenderer var9 = var8.getWorldRenderer();
            this.draw(var9, p_180453_3_ + 2, p_180453_4_ + 13, 13, 2, 0, 0, 0, 255);
            this.draw(var9, p_180453_3_ + 2, p_180453_4_ + 13, 12, 1, (255 - var7) / 4, 64, 0, 255);
            this.draw(var9, p_180453_3_ + 2, p_180453_4_ + 13, var10, 1, 255 - var7, var7, 0, 255);
            if(!Reflector.ForgeHooksClient.exists()) {
               GlStateManager.enableBlend();
            }

            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
         }
      }

   }

   public void draw(WorldRenderer p_500170_1_, int p_500170_2_, int p_500170_3_, int p_500170_4_, int p_500170_5_, int p_500170_6_, int p_500170_7_, int p_500170_8_, int p_500170_9_) {
      p_500170_1_.begin(7, DefaultVertexFormats.POSITION_COLOR);
      p_500170_1_.pos((double)(p_500170_2_ + 0), (double)(p_500170_3_ + 0), 0.0D).color(p_500170_6_, p_500170_7_, p_500170_8_, p_500170_9_).endVertex();
      p_500170_1_.pos((double)(p_500170_2_ + 0), (double)(p_500170_3_ + p_500170_5_), 0.0D).color(p_500170_6_, p_500170_7_, p_500170_8_, p_500170_9_).endVertex();
      p_500170_1_.pos((double)(p_500170_2_ + p_500170_4_), (double)(p_500170_3_ + p_500170_5_), 0.0D).color(p_500170_6_, p_500170_7_, p_500170_8_, p_500170_9_).endVertex();
      p_500170_1_.pos((double)(p_500170_2_ + p_500170_4_), (double)(p_500170_3_ + 0), 0.0D).color(p_500170_6_, p_500170_7_, p_500170_8_, p_500170_9_).endVertex();
      Tessellator.getInstance().draw();
   }

   public void registerItems() {
      this.registerBlock(Blocks.anvil, "anvil_intact");
      this.registerBlock(Blocks.anvil, 1, "anvil_slightly_damaged");
      this.registerBlock(Blocks.anvil, 2, "anvil_very_damaged");
      this.registerBlock(Blocks.carpet, EnumDyeColor.BLACK.getMetadata(), "black_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.BLUE.getMetadata(), "blue_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.BROWN.getMetadata(), "brown_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.CYAN.getMetadata(), "cyan_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.GRAY.getMetadata(), "gray_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.GREEN.getMetadata(), "green_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.LIME.getMetadata(), "lime_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.MAGENTA.getMetadata(), "magenta_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.ORANGE.getMetadata(), "orange_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.PINK.getMetadata(), "pink_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.PURPLE.getMetadata(), "purple_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.RED.getMetadata(), "red_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.SILVER.getMetadata(), "silver_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.WHITE.getMetadata(), "white_carpet");
      this.registerBlock(Blocks.carpet, EnumDyeColor.YELLOW.getMetadata(), "yellow_carpet");
      this.registerBlock(Blocks.cobblestone_wall, EnumType.MOSSY.getMetadata(), "mossy_cobblestone_wall");
      this.registerBlock(Blocks.cobblestone_wall, EnumType.NORMAL.getMetadata(), "cobblestone_wall");
      this.registerBlock(Blocks.dirt, DirtType.COARSE_DIRT.getMetadata(), "coarse_dirt");
      this.registerBlock(Blocks.dirt, DirtType.DIRT.getMetadata(), "dirt");
      this.registerBlock(Blocks.dirt, DirtType.PODZOL.getMetadata(), "podzol");
      this.registerBlock(Blocks.double_plant, EnumPlantType.FERN.getMeta(), "double_fern");
      this.registerBlock(Blocks.double_plant, EnumPlantType.GRASS.getMeta(), "double_grass");
      this.registerBlock(Blocks.double_plant, EnumPlantType.PAEONIA.getMeta(), "paeonia");
      this.registerBlock(Blocks.double_plant, EnumPlantType.ROSE.getMeta(), "double_rose");
      this.registerBlock(Blocks.double_plant, EnumPlantType.SUNFLOWER.getMeta(), "sunflower");
      this.registerBlock(Blocks.double_plant, EnumPlantType.SYRINGA.getMeta(), "syringa");
      this.registerBlock(Blocks.leaves, net.minecraft.block.BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_leaves");
      this.registerBlock(Blocks.leaves, net.minecraft.block.BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_leaves");
      this.registerBlock(Blocks.leaves, net.minecraft.block.BlockPlanks.EnumType.OAK.getMetadata(), "oak_leaves");
      this.registerBlock(Blocks.leaves, net.minecraft.block.BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_leaves");
      this.registerBlock(Blocks.leaves2, net.minecraft.block.BlockPlanks.EnumType.ACACIA.getMetadata() - 4, "acacia_leaves");
      this.registerBlock(Blocks.leaves2, net.minecraft.block.BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4, "dark_oak_leaves");
      this.registerBlock(Blocks.log, net.minecraft.block.BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_log");
      this.registerBlock(Blocks.log, net.minecraft.block.BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_log");
      this.registerBlock(Blocks.log, net.minecraft.block.BlockPlanks.EnumType.OAK.getMetadata(), "oak_log");
      this.registerBlock(Blocks.log, net.minecraft.block.BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_log");
      this.registerBlock(Blocks.log2, net.minecraft.block.BlockPlanks.EnumType.ACACIA.getMetadata() - 4, "acacia_log");
      this.registerBlock(Blocks.log2, net.minecraft.block.BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4, "dark_oak_log");
      this.registerBlock(Blocks.monster_egg, net.minecraft.block.BlockSilverfish.EnumType.CHISELED_STONEBRICK.getMetadata(), "chiseled_brick_monster_egg");
      this.registerBlock(Blocks.monster_egg, net.minecraft.block.BlockSilverfish.EnumType.COBBLESTONE.getMetadata(), "cobblestone_monster_egg");
      this.registerBlock(Blocks.monster_egg, net.minecraft.block.BlockSilverfish.EnumType.CRACKED_STONEBRICK.getMetadata(), "cracked_brick_monster_egg");
      this.registerBlock(Blocks.monster_egg, net.minecraft.block.BlockSilverfish.EnumType.MOSSY_STONEBRICK.getMetadata(), "mossy_brick_monster_egg");
      this.registerBlock(Blocks.monster_egg, net.minecraft.block.BlockSilverfish.EnumType.STONE.getMetadata(), "stone_monster_egg");
      this.registerBlock(Blocks.monster_egg, net.minecraft.block.BlockSilverfish.EnumType.STONEBRICK.getMetadata(), "stone_brick_monster_egg");
      this.registerBlock(Blocks.planks, net.minecraft.block.BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_planks");
      this.registerBlock(Blocks.planks, net.minecraft.block.BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_planks");
      this.registerBlock(Blocks.planks, net.minecraft.block.BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_planks");
      this.registerBlock(Blocks.planks, net.minecraft.block.BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_planks");
      this.registerBlock(Blocks.planks, net.minecraft.block.BlockPlanks.EnumType.OAK.getMetadata(), "oak_planks");
      this.registerBlock(Blocks.planks, net.minecraft.block.BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_planks");
      this.registerBlock(Blocks.prismarine, net.minecraft.block.BlockPrismarine.EnumType.BRICKS.getMetadata(), "prismarine_bricks");
      this.registerBlock(Blocks.prismarine, net.minecraft.block.BlockPrismarine.EnumType.DARK.getMetadata(), "dark_prismarine");
      this.registerBlock(Blocks.prismarine, net.minecraft.block.BlockPrismarine.EnumType.ROUGH.getMetadata(), "prismarine");
      this.registerBlock(Blocks.quartz_block, net.minecraft.block.BlockQuartz.EnumType.CHISELED.getMetadata(), "chiseled_quartz_block");
      this.registerBlock(Blocks.quartz_block, net.minecraft.block.BlockQuartz.EnumType.DEFAULT.getMetadata(), "quartz_block");
      this.registerBlock(Blocks.quartz_block, net.minecraft.block.BlockQuartz.EnumType.LINES_Y.getMetadata(), "quartz_column");
      this.registerBlock(Blocks.red_flower, EnumFlowerType.ALLIUM.getMeta(), "allium");
      this.registerBlock(Blocks.red_flower, EnumFlowerType.BLUE_ORCHID.getMeta(), "blue_orchid");
      this.registerBlock(Blocks.red_flower, EnumFlowerType.HOUSTONIA.getMeta(), "houstonia");
      this.registerBlock(Blocks.red_flower, EnumFlowerType.ORANGE_TULIP.getMeta(), "orange_tulip");
      this.registerBlock(Blocks.red_flower, EnumFlowerType.OXEYE_DAISY.getMeta(), "oxeye_daisy");
      this.registerBlock(Blocks.red_flower, EnumFlowerType.PINK_TULIP.getMeta(), "pink_tulip");
      this.registerBlock(Blocks.red_flower, EnumFlowerType.POPPY.getMeta(), "poppy");
      this.registerBlock(Blocks.red_flower, EnumFlowerType.RED_TULIP.getMeta(), "red_tulip");
      this.registerBlock(Blocks.red_flower, EnumFlowerType.WHITE_TULIP.getMeta(), "white_tulip");
      this.registerBlock(Blocks.sand, net.minecraft.block.BlockSand.EnumType.RED_SAND.getMetadata(), "red_sand");
      this.registerBlock(Blocks.sand, net.minecraft.block.BlockSand.EnumType.SAND.getMetadata(), "sand");
      this.registerBlock(Blocks.sandstone, net.minecraft.block.BlockSandStone.EnumType.CHISELED.getMetadata(), "chiseled_sandstone");
      this.registerBlock(Blocks.sandstone, net.minecraft.block.BlockSandStone.EnumType.DEFAULT.getMetadata(), "sandstone");
      this.registerBlock(Blocks.sandstone, net.minecraft.block.BlockSandStone.EnumType.SMOOTH.getMetadata(), "smooth_sandstone");
      this.registerBlock(Blocks.red_sandstone, net.minecraft.block.BlockRedSandstone.EnumType.CHISELED.getMetadata(), "chiseled_red_sandstone");
      this.registerBlock(Blocks.red_sandstone, net.minecraft.block.BlockRedSandstone.EnumType.DEFAULT.getMetadata(), "red_sandstone");
      this.registerBlock(Blocks.red_sandstone, net.minecraft.block.BlockRedSandstone.EnumType.SMOOTH.getMetadata(), "smooth_red_sandstone");
      this.registerBlock(Blocks.sapling, net.minecraft.block.BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_sapling");
      this.registerBlock(Blocks.sapling, net.minecraft.block.BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_sapling");
      this.registerBlock(Blocks.sapling, net.minecraft.block.BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_sapling");
      this.registerBlock(Blocks.sapling, net.minecraft.block.BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_sapling");
      this.registerBlock(Blocks.sapling, net.minecraft.block.BlockPlanks.EnumType.OAK.getMetadata(), "oak_sapling");
      this.registerBlock(Blocks.sapling, net.minecraft.block.BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_sapling");
      this.registerBlock(Blocks.sponge, 0, "sponge");
      this.registerBlock(Blocks.sponge, 1, "sponge_wet");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.BLACK.getMetadata(), "black_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.BLUE.getMetadata(), "blue_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.BROWN.getMetadata(), "brown_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.GRAY.getMetadata(), "gray_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.GREEN.getMetadata(), "green_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.LIME.getMetadata(), "lime_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.PINK.getMetadata(), "pink_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.RED.getMetadata(), "red_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.SILVER.getMetadata(), "silver_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.WHITE.getMetadata(), "white_stained_glass");
      this.registerBlock(Blocks.stained_glass, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_glass");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.BLACK.getMetadata(), "black_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.BLUE.getMetadata(), "blue_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.BROWN.getMetadata(), "brown_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.GRAY.getMetadata(), "gray_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.GREEN.getMetadata(), "green_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.LIME.getMetadata(), "lime_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.PINK.getMetadata(), "pink_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.RED.getMetadata(), "red_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.SILVER.getMetadata(), "silver_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.WHITE.getMetadata(), "white_stained_glass_pane");
      this.registerBlock(Blocks.stained_glass_pane, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_glass_pane");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.BLACK.getMetadata(), "black_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.BLUE.getMetadata(), "blue_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.BROWN.getMetadata(), "brown_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.CYAN.getMetadata(), "cyan_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.GRAY.getMetadata(), "gray_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.GREEN.getMetadata(), "green_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.LIME.getMetadata(), "lime_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.MAGENTA.getMetadata(), "magenta_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.ORANGE.getMetadata(), "orange_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.PINK.getMetadata(), "pink_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.PURPLE.getMetadata(), "purple_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.RED.getMetadata(), "red_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.SILVER.getMetadata(), "silver_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.WHITE.getMetadata(), "white_stained_hardened_clay");
      this.registerBlock(Blocks.stained_hardened_clay, EnumDyeColor.YELLOW.getMetadata(), "yellow_stained_hardened_clay");
      this.registerBlock(Blocks.stone, net.minecraft.block.BlockStone.EnumType.ANDESITE.getMetadata(), "andesite");
      this.registerBlock(Blocks.stone, net.minecraft.block.BlockStone.EnumType.ANDESITE_SMOOTH.getMetadata(), "andesite_smooth");
      this.registerBlock(Blocks.stone, net.minecraft.block.BlockStone.EnumType.DIORITE.getMetadata(), "diorite");
      this.registerBlock(Blocks.stone, net.minecraft.block.BlockStone.EnumType.DIORITE_SMOOTH.getMetadata(), "diorite_smooth");
      this.registerBlock(Blocks.stone, net.minecraft.block.BlockStone.EnumType.GRANITE.getMetadata(), "granite");
      this.registerBlock(Blocks.stone, net.minecraft.block.BlockStone.EnumType.GRANITE_SMOOTH.getMetadata(), "granite_smooth");
      this.registerBlock(Blocks.stone, net.minecraft.block.BlockStone.EnumType.STONE.getMetadata(), "stone");
      this.registerBlock(Blocks.stonebrick, net.minecraft.block.BlockStoneBrick.EnumType.CRACKED.getMetadata(), "cracked_stonebrick");
      this.registerBlock(Blocks.stonebrick, net.minecraft.block.BlockStoneBrick.EnumType.DEFAULT.getMetadata(), "stonebrick");
      this.registerBlock(Blocks.stonebrick, net.minecraft.block.BlockStoneBrick.EnumType.CHISELED.getMetadata(), "chiseled_stonebrick");
      this.registerBlock(Blocks.stonebrick, net.minecraft.block.BlockStoneBrick.EnumType.MOSSY.getMetadata(), "mossy_stonebrick");
      this.registerBlock(Blocks.stone_slab, net.minecraft.block.BlockStoneSlab.EnumType.BRICK.getMetadata(), "brick_slab");
      this.registerBlock(Blocks.stone_slab, net.minecraft.block.BlockStoneSlab.EnumType.COBBLESTONE.getMetadata(), "cobblestone_slab");
      this.registerBlock(Blocks.stone_slab, net.minecraft.block.BlockStoneSlab.EnumType.WOOD.getMetadata(), "old_wood_slab");
      this.registerBlock(Blocks.stone_slab, net.minecraft.block.BlockStoneSlab.EnumType.NETHERBRICK.getMetadata(), "nether_brick_slab");
      this.registerBlock(Blocks.stone_slab, net.minecraft.block.BlockStoneSlab.EnumType.QUARTZ.getMetadata(), "quartz_slab");
      this.registerBlock(Blocks.stone_slab, net.minecraft.block.BlockStoneSlab.EnumType.SAND.getMetadata(), "sandstone_slab");
      this.registerBlock(Blocks.stone_slab, net.minecraft.block.BlockStoneSlab.EnumType.SMOOTHBRICK.getMetadata(), "stone_brick_slab");
      this.registerBlock(Blocks.stone_slab, net.minecraft.block.BlockStoneSlab.EnumType.STONE.getMetadata(), "stone_slab");
      this.registerBlock(Blocks.stone_slab2, net.minecraft.block.BlockStoneSlabNew.EnumType.RED_SANDSTONE.getMetadata(), "red_sandstone_slab");
      this.registerBlock(Blocks.tallgrass, net.minecraft.block.BlockTallGrass.EnumType.DEAD_BUSH.getMeta(), "dead_bush");
      this.registerBlock(Blocks.tallgrass, net.minecraft.block.BlockTallGrass.EnumType.FERN.getMeta(), "fern");
      this.registerBlock(Blocks.tallgrass, net.minecraft.block.BlockTallGrass.EnumType.GRASS.getMeta(), "tall_grass");
      this.registerBlock(Blocks.wooden_slab, net.minecraft.block.BlockPlanks.EnumType.ACACIA.getMetadata(), "acacia_slab");
      this.registerBlock(Blocks.wooden_slab, net.minecraft.block.BlockPlanks.EnumType.BIRCH.getMetadata(), "birch_slab");
      this.registerBlock(Blocks.wooden_slab, net.minecraft.block.BlockPlanks.EnumType.DARK_OAK.getMetadata(), "dark_oak_slab");
      this.registerBlock(Blocks.wooden_slab, net.minecraft.block.BlockPlanks.EnumType.JUNGLE.getMetadata(), "jungle_slab");
      this.registerBlock(Blocks.wooden_slab, net.minecraft.block.BlockPlanks.EnumType.OAK.getMetadata(), "oak_slab");
      this.registerBlock(Blocks.wooden_slab, net.minecraft.block.BlockPlanks.EnumType.SPRUCE.getMetadata(), "spruce_slab");
      this.registerBlock(Blocks.wool, EnumDyeColor.BLACK.getMetadata(), "black_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.BLUE.getMetadata(), "blue_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.BROWN.getMetadata(), "brown_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.CYAN.getMetadata(), "cyan_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.GRAY.getMetadata(), "gray_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.GREEN.getMetadata(), "green_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.LIGHT_BLUE.getMetadata(), "light_blue_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.LIME.getMetadata(), "lime_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.MAGENTA.getMetadata(), "magenta_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.ORANGE.getMetadata(), "orange_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.PINK.getMetadata(), "pink_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.PURPLE.getMetadata(), "purple_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.RED.getMetadata(), "red_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.SILVER.getMetadata(), "silver_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.WHITE.getMetadata(), "white_wool");
      this.registerBlock(Blocks.wool, EnumDyeColor.YELLOW.getMetadata(), "yellow_wool");
      this.registerBlock(Blocks.acacia_stairs, "acacia_stairs");
      this.registerBlock(Blocks.activator_rail, "activator_rail");
      this.registerBlock(Blocks.beacon, "beacon");
      this.registerBlock(Blocks.bedrock, "bedrock");
      this.registerBlock(Blocks.birch_stairs, "birch_stairs");
      this.registerBlock(Blocks.bookshelf, "bookshelf");
      this.registerBlock(Blocks.brick_block, "brick_block");
      this.registerBlock(Blocks.brick_block, "brick_block");
      this.registerBlock(Blocks.brick_stairs, "brick_stairs");
      this.registerBlock(Blocks.brown_mushroom, "brown_mushroom");
      this.registerBlock(Blocks.cactus, "cactus");
      this.registerBlock(Blocks.clay, "clay");
      this.registerBlock(Blocks.coal_block, "coal_block");
      this.registerBlock(Blocks.coal_ore, "coal_ore");
      this.registerBlock(Blocks.cobblestone, "cobblestone");
      this.registerBlock(Blocks.crafting_table, "crafting_table");
      this.registerBlock(Blocks.dark_oak_stairs, "dark_oak_stairs");
      this.registerBlock(Blocks.daylight_detector, "daylight_detector");
      this.registerBlock(Blocks.deadbush, "dead_bush");
      this.registerBlock(Blocks.detector_rail, "detector_rail");
      this.registerBlock(Blocks.diamond_block, "diamond_block");
      this.registerBlock(Blocks.diamond_ore, "diamond_ore");
      this.registerBlock(Blocks.dispenser, "dispenser");
      this.registerBlock(Blocks.dropper, "dropper");
      this.registerBlock(Blocks.emerald_block, "emerald_block");
      this.registerBlock(Blocks.emerald_ore, "emerald_ore");
      this.registerBlock(Blocks.enchanting_table, "enchanting_table");
      this.registerBlock(Blocks.end_portal_frame, "end_portal_frame");
      this.registerBlock(Blocks.end_stone, "end_stone");
      this.registerBlock(Blocks.oak_fence, "oak_fence");
      this.registerBlock(Blocks.spruce_fence, "spruce_fence");
      this.registerBlock(Blocks.birch_fence, "birch_fence");
      this.registerBlock(Blocks.jungle_fence, "jungle_fence");
      this.registerBlock(Blocks.dark_oak_fence, "dark_oak_fence");
      this.registerBlock(Blocks.acacia_fence, "acacia_fence");
      this.registerBlock(Blocks.oak_fence_gate, "oak_fence_gate");
      this.registerBlock(Blocks.spruce_fence_gate, "spruce_fence_gate");
      this.registerBlock(Blocks.birch_fence_gate, "birch_fence_gate");
      this.registerBlock(Blocks.jungle_fence_gate, "jungle_fence_gate");
      this.registerBlock(Blocks.dark_oak_fence_gate, "dark_oak_fence_gate");
      this.registerBlock(Blocks.acacia_fence_gate, "acacia_fence_gate");
      this.registerBlock(Blocks.furnace, "furnace");
      this.registerBlock(Blocks.glass, "glass");
      this.registerBlock(Blocks.glass_pane, "glass_pane");
      this.registerBlock(Blocks.glowstone, "glowstone");
      this.registerBlock(Blocks.golden_rail, "golden_rail");
      this.registerBlock(Blocks.gold_block, "gold_block");
      this.registerBlock(Blocks.gold_ore, "gold_ore");
      this.registerBlock(Blocks.grass, "grass");
      this.registerBlock(Blocks.gravel, "gravel");
      this.registerBlock(Blocks.hardened_clay, "hardened_clay");
      this.registerBlock(Blocks.hay_block, "hay_block");
      this.registerBlock(Blocks.heavy_weighted_pressure_plate, "heavy_weighted_pressure_plate");
      this.registerBlock(Blocks.hopper, "hopper");
      this.registerBlock(Blocks.ice, "ice");
      this.registerBlock(Blocks.iron_bars, "iron_bars");
      this.registerBlock(Blocks.iron_block, "iron_block");
      this.registerBlock(Blocks.iron_ore, "iron_ore");
      this.registerBlock(Blocks.iron_trapdoor, "iron_trapdoor");
      this.registerBlock(Blocks.jukebox, "jukebox");
      this.registerBlock(Blocks.jungle_stairs, "jungle_stairs");
      this.registerBlock(Blocks.ladder, "ladder");
      this.registerBlock(Blocks.lapis_block, "lapis_block");
      this.registerBlock(Blocks.lapis_ore, "lapis_ore");
      this.registerBlock(Blocks.lever, "lever");
      this.registerBlock(Blocks.light_weighted_pressure_plate, "light_weighted_pressure_plate");
      this.registerBlock(Blocks.lit_pumpkin, "lit_pumpkin");
      this.registerBlock(Blocks.melon_block, "melon_block");
      this.registerBlock(Blocks.mossy_cobblestone, "mossy_cobblestone");
      this.registerBlock(Blocks.mycelium, "mycelium");
      this.registerBlock(Blocks.netherrack, "netherrack");
      this.registerBlock(Blocks.nether_brick, "nether_brick");
      this.registerBlock(Blocks.nether_brick_fence, "nether_brick_fence");
      this.registerBlock(Blocks.nether_brick_stairs, "nether_brick_stairs");
      this.registerBlock(Blocks.noteblock, "noteblock");
      this.registerBlock(Blocks.oak_stairs, "oak_stairs");
      this.registerBlock(Blocks.obsidian, "obsidian");
      this.registerBlock(Blocks.packed_ice, "packed_ice");
      this.registerBlock(Blocks.piston, "piston");
      this.registerBlock(Blocks.pumpkin, "pumpkin");
      this.registerBlock(Blocks.quartz_ore, "quartz_ore");
      this.registerBlock(Blocks.quartz_stairs, "quartz_stairs");
      this.registerBlock(Blocks.rail, "rail");
      this.registerBlock(Blocks.redstone_block, "redstone_block");
      this.registerBlock(Blocks.redstone_lamp, "redstone_lamp");
      this.registerBlock(Blocks.redstone_ore, "redstone_ore");
      this.registerBlock(Blocks.redstone_torch, "redstone_torch");
      this.registerBlock(Blocks.red_mushroom, "red_mushroom");
      this.registerBlock(Blocks.sandstone_stairs, "sandstone_stairs");
      this.registerBlock(Blocks.red_sandstone_stairs, "red_sandstone_stairs");
      this.registerBlock(Blocks.sea_lantern, "sea_lantern");
      this.registerBlock(Blocks.slime_block, "slime");
      this.registerBlock(Blocks.snow, "snow");
      this.registerBlock(Blocks.snow_layer, "snow_layer");
      this.registerBlock(Blocks.soul_sand, "soul_sand");
      this.registerBlock(Blocks.spruce_stairs, "spruce_stairs");
      this.registerBlock(Blocks.sticky_piston, "sticky_piston");
      this.registerBlock(Blocks.stone_brick_stairs, "stone_brick_stairs");
      this.registerBlock(Blocks.stone_button, "stone_button");
      this.registerBlock(Blocks.stone_pressure_plate, "stone_pressure_plate");
      this.registerBlock(Blocks.stone_stairs, "stone_stairs");
      this.registerBlock(Blocks.tnt, "tnt");
      this.registerBlock(Blocks.torch, "torch");
      this.registerBlock(Blocks.trapdoor, "trapdoor");
      this.registerBlock(Blocks.tripwire_hook, "tripwire_hook");
      this.registerBlock(Blocks.vine, "vine");
      this.registerBlock(Blocks.waterlily, "waterlily");
      this.registerBlock(Blocks.web, "web");
      this.registerBlock(Blocks.wooden_button, "wooden_button");
      this.registerBlock(Blocks.wooden_pressure_plate, "wooden_pressure_plate");
      this.registerBlock(Blocks.yellow_flower, EnumFlowerType.DANDELION.getMeta(), "dandelion");
      this.registerBlock(Blocks.chest, "chest");
      this.registerBlock(Blocks.trapped_chest, "trapped_chest");
      this.registerBlock(Blocks.ender_chest, "ender_chest");
      this.registerItem(Items.iron_shovel, "iron_shovel");
      this.registerItem(Items.iron_pickaxe, "iron_pickaxe");
      this.registerItem(Items.iron_axe, "iron_axe");
      this.registerItem(Items.flint_and_steel, "flint_and_steel");
      this.registerItem(Items.apple, "apple");
      this.registerItem(Items.bow, 0, "bow");
      this.registerItem(Items.bow, 1, "bow_pulling_0");
      this.registerItem(Items.bow, 2, "bow_pulling_1");
      this.registerItem(Items.bow, 3, "bow_pulling_2");
      this.registerItem(Items.arrow, "arrow");
      this.registerItem(Items.coal, 0, "coal");
      this.registerItem(Items.coal, 1, "charcoal");
      this.registerItem(Items.diamond, "diamond");
      this.registerItem(Items.iron_ingot, "iron_ingot");
      this.registerItem(Items.gold_ingot, "gold_ingot");
      this.registerItem(Items.iron_sword, "iron_sword");
      this.registerItem(Items.wooden_sword, "wooden_sword");
      this.registerItem(Items.wooden_shovel, "wooden_shovel");
      this.registerItem(Items.wooden_pickaxe, "wooden_pickaxe");
      this.registerItem(Items.wooden_axe, "wooden_axe");
      this.registerItem(Items.stone_sword, "stone_sword");
      this.registerItem(Items.stone_shovel, "stone_shovel");
      this.registerItem(Items.stone_pickaxe, "stone_pickaxe");
      this.registerItem(Items.stone_axe, "stone_axe");
      this.registerItem(Items.diamond_sword, "diamond_sword");
      this.registerItem(Items.diamond_shovel, "diamond_shovel");
      this.registerItem(Items.diamond_pickaxe, "diamond_pickaxe");
      this.registerItem(Items.diamond_axe, "diamond_axe");
      this.registerItem(Items.stick, "stick");
      this.registerItem(Items.bowl, "bowl");
      this.registerItem(Items.mushroom_stew, "mushroom_stew");
      this.registerItem(Items.golden_sword, "golden_sword");
      this.registerItem(Items.golden_shovel, "golden_shovel");
      this.registerItem(Items.golden_pickaxe, "golden_pickaxe");
      this.registerItem(Items.golden_axe, "golden_axe");
      this.registerItem(Items.string, "string");
      this.registerItem(Items.feather, "feather");
      this.registerItem(Items.gunpowder, "gunpowder");
      this.registerItem(Items.wooden_hoe, "wooden_hoe");
      this.registerItem(Items.stone_hoe, "stone_hoe");
      this.registerItem(Items.iron_hoe, "iron_hoe");
      this.registerItem(Items.diamond_hoe, "diamond_hoe");
      this.registerItem(Items.golden_hoe, "golden_hoe");
      this.registerItem(Items.wheat_seeds, "wheat_seeds");
      this.registerItem(Items.wheat, "wheat");
      this.registerItem(Items.bread, "bread");
      this.registerItem(Items.leather_helmet, "leather_helmet");
      this.registerItem(Items.leather_chestplate, "leather_chestplate");
      this.registerItem(Items.leather_leggings, "leather_leggings");
      this.registerItem(Items.leather_boots, "leather_boots");
      this.registerItem(Items.chainmail_helmet, "chainmail_helmet");
      this.registerItem(Items.chainmail_chestplate, "chainmail_chestplate");
      this.registerItem(Items.chainmail_leggings, "chainmail_leggings");
      this.registerItem(Items.chainmail_boots, "chainmail_boots");
      this.registerItem(Items.iron_helmet, "iron_helmet");
      this.registerItem(Items.iron_chestplate, "iron_chestplate");
      this.registerItem(Items.iron_leggings, "iron_leggings");
      this.registerItem(Items.iron_boots, "iron_boots");
      this.registerItem(Items.diamond_helmet, "diamond_helmet");
      this.registerItem(Items.diamond_chestplate, "diamond_chestplate");
      this.registerItem(Items.diamond_leggings, "diamond_leggings");
      this.registerItem(Items.diamond_boots, "diamond_boots");
      this.registerItem(Items.golden_helmet, "golden_helmet");
      this.registerItem(Items.golden_chestplate, "golden_chestplate");
      this.registerItem(Items.golden_leggings, "golden_leggings");
      this.registerItem(Items.golden_boots, "golden_boots");
      this.registerItem(Items.flint, "flint");
      this.registerItem(Items.porkchop, "porkchop");
      this.registerItem(Items.cooked_porkchop, "cooked_porkchop");
      this.registerItem(Items.painting, "painting");
      this.registerItem(Items.golden_apple, "golden_apple");
      this.registerItem(Items.golden_apple, 1, "golden_apple");
      this.registerItem(Items.sign, "sign");
      this.registerItem(Items.oak_door, "oak_door");
      this.registerItem(Items.spruce_door, "spruce_door");
      this.registerItem(Items.birch_door, "birch_door");
      this.registerItem(Items.jungle_door, "jungle_door");
      this.registerItem(Items.acacia_door, "acacia_door");
      this.registerItem(Items.dark_oak_door, "dark_oak_door");
      this.registerItem(Items.bucket, "bucket");
      this.registerItem(Items.water_bucket, "water_bucket");
      this.registerItem(Items.lava_bucket, "lava_bucket");
      this.registerItem(Items.minecart, "minecart");
      this.registerItem(Items.saddle, "saddle");
      this.registerItem(Items.iron_door, "iron_door");
      this.registerItem(Items.redstone, "redstone");
      this.registerItem(Items.snowball, "snowball");
      this.registerItem(Items.boat, "boat");
      this.registerItem(Items.leather, "leather");
      this.registerItem(Items.milk_bucket, "milk_bucket");
      this.registerItem(Items.brick, "brick");
      this.registerItem(Items.clay_ball, "clay_ball");
      this.registerItem(Items.reeds, "reeds");
      this.registerItem(Items.paper, "paper");
      this.registerItem(Items.book, "book");
      this.registerItem(Items.slime_ball, "slime_ball");
      this.registerItem(Items.chest_minecart, "chest_minecart");
      this.registerItem(Items.furnace_minecart, "furnace_minecart");
      this.registerItem(Items.egg, "egg");
      this.registerItem(Items.compass, "compass");
      this.registerItem(Items.fishing_rod, "fishing_rod");
      this.registerItem(Items.fishing_rod, 1, "fishing_rod_cast");
      this.registerItem(Items.clock, "clock");
      this.registerItem(Items.glowstone_dust, "glowstone_dust");
      this.registerItem(Items.fish, FishType.COD.getMetadata(), "cod");
      this.registerItem(Items.fish, FishType.SALMON.getMetadata(), "salmon");
      this.registerItem(Items.fish, FishType.CLOWNFISH.getMetadata(), "clownfish");
      this.registerItem(Items.fish, FishType.PUFFERFISH.getMetadata(), "pufferfish");
      this.registerItem(Items.cooked_fish, FishType.COD.getMetadata(), "cooked_cod");
      this.registerItem(Items.cooked_fish, FishType.SALMON.getMetadata(), "cooked_salmon");
      this.registerItem(Items.dye, EnumDyeColor.BLACK.getDyeDamage(), "dye_black");
      this.registerItem(Items.dye, EnumDyeColor.RED.getDyeDamage(), "dye_red");
      this.registerItem(Items.dye, EnumDyeColor.GREEN.getDyeDamage(), "dye_green");
      this.registerItem(Items.dye, EnumDyeColor.BROWN.getDyeDamage(), "dye_brown");
      this.registerItem(Items.dye, EnumDyeColor.BLUE.getDyeDamage(), "dye_blue");
      this.registerItem(Items.dye, EnumDyeColor.PURPLE.getDyeDamage(), "dye_purple");
      this.registerItem(Items.dye, EnumDyeColor.CYAN.getDyeDamage(), "dye_cyan");
      this.registerItem(Items.dye, EnumDyeColor.SILVER.getDyeDamage(), "dye_silver");
      this.registerItem(Items.dye, EnumDyeColor.GRAY.getDyeDamage(), "dye_gray");
      this.registerItem(Items.dye, EnumDyeColor.PINK.getDyeDamage(), "dye_pink");
      this.registerItem(Items.dye, EnumDyeColor.LIME.getDyeDamage(), "dye_lime");
      this.registerItem(Items.dye, EnumDyeColor.YELLOW.getDyeDamage(), "dye_yellow");
      this.registerItem(Items.dye, EnumDyeColor.LIGHT_BLUE.getDyeDamage(), "dye_light_blue");
      this.registerItem(Items.dye, EnumDyeColor.MAGENTA.getDyeDamage(), "dye_magenta");
      this.registerItem(Items.dye, EnumDyeColor.ORANGE.getDyeDamage(), "dye_orange");
      this.registerItem(Items.dye, EnumDyeColor.WHITE.getDyeDamage(), "dye_white");
      this.registerItem(Items.bone, "bone");
      this.registerItem(Items.sugar, "sugar");
      this.registerItem(Items.cake, "cake");
      this.registerItem(Items.bed, "bed");
      this.registerItem(Items.repeater, "repeater");
      this.registerItem(Items.cookie, "cookie");
      this.registerItem(Items.shears, "shears");
      this.registerItem(Items.melon, "melon");
      this.registerItem(Items.pumpkin_seeds, "pumpkin_seeds");
      this.registerItem(Items.melon_seeds, "melon_seeds");
      this.registerItem(Items.beef, "beef");
      this.registerItem(Items.cooked_beef, "cooked_beef");
      this.registerItem(Items.chicken, "chicken");
      this.registerItem(Items.cooked_chicken, "cooked_chicken");
      this.registerItem(Items.rabbit, "rabbit");
      this.registerItem(Items.cooked_rabbit, "cooked_rabbit");
      this.registerItem(Items.mutton, "mutton");
      this.registerItem(Items.cooked_mutton, "cooked_mutton");
      this.registerItem(Items.rabbit_foot, "rabbit_foot");
      this.registerItem(Items.rabbit_hide, "rabbit_hide");
      this.registerItem(Items.rabbit_stew, "rabbit_stew");
      this.registerItem(Items.rotten_flesh, "rotten_flesh");
      this.registerItem(Items.ender_pearl, "ender_pearl");
      this.registerItem(Items.blaze_rod, "blaze_rod");
      this.registerItem(Items.ghast_tear, "ghast_tear");
      this.registerItem(Items.gold_nugget, "gold_nugget");
      this.registerItem(Items.nether_wart, "nether_wart");
      this.itemModelMesher.register(Items.potionitem, new ItemMeshDefinition() {

         public static final String __OBFID = "CL_00002440";

         public ModelResourceLocation getModelLocation(ItemStack p_178113_1_) {
            return ItemPotion.isSplash(p_178113_1_.getMetadata())?new ModelResourceLocation("bottle_splash", "inventory"):new ModelResourceLocation("bottle_drinkable", "inventory");
         }
      });
      this.registerItem(Items.glass_bottle, "glass_bottle");
      this.registerItem(Items.spider_eye, "spider_eye");
      this.registerItem(Items.fermented_spider_eye, "fermented_spider_eye");
      this.registerItem(Items.blaze_powder, "blaze_powder");
      this.registerItem(Items.magma_cream, "magma_cream");
      this.registerItem(Items.brewing_stand, "brewing_stand");
      this.registerItem(Items.cauldron, "cauldron");
      this.registerItem(Items.ender_eye, "ender_eye");
      this.registerItem(Items.speckled_melon, "speckled_melon");
      this.itemModelMesher.register(Items.spawn_egg, new ItemMeshDefinition() {

         public static final String __OBFID = "CL_00002439";

         public ModelResourceLocation getModelLocation(ItemStack p_178113_1_) {
            return new ModelResourceLocation("spawn_egg", "inventory");
         }
      });
      this.registerItem(Items.experience_bottle, "experience_bottle");
      this.registerItem(Items.fire_charge, "fire_charge");
      this.registerItem(Items.writable_book, "writable_book");
      this.registerItem(Items.emerald, "emerald");
      this.registerItem(Items.item_frame, "item_frame");
      this.registerItem(Items.flower_pot, "flower_pot");
      this.registerItem(Items.carrot, "carrot");
      this.registerItem(Items.potato, "potato");
      this.registerItem(Items.baked_potato, "baked_potato");
      this.registerItem(Items.poisonous_potato, "poisonous_potato");
      this.registerItem(Items.map, "map");
      this.registerItem(Items.golden_carrot, "golden_carrot");
      this.registerItem(Items.skull, 0, "skull_skeleton");
      this.registerItem(Items.skull, 1, "skull_wither");
      this.registerItem(Items.skull, 2, "skull_zombie");
      this.registerItem(Items.skull, 3, "skull_char");
      this.registerItem(Items.skull, 4, "skull_creeper");
      this.registerItem(Items.carrot_on_a_stick, "carrot_on_a_stick");
      this.registerItem(Items.nether_star, "nether_star");
      this.registerItem(Items.pumpkin_pie, "pumpkin_pie");
      this.registerItem(Items.firework_charge, "firework_charge");
      this.registerItem(Items.comparator, "comparator");
      this.registerItem(Items.netherbrick, "netherbrick");
      this.registerItem(Items.quartz, "quartz");
      this.registerItem(Items.tnt_minecart, "tnt_minecart");
      this.registerItem(Items.hopper_minecart, "hopper_minecart");
      this.registerItem(Items.armor_stand, "armor_stand");
      this.registerItem(Items.iron_horse_armor, "iron_horse_armor");
      this.registerItem(Items.golden_horse_armor, "golden_horse_armor");
      this.registerItem(Items.diamond_horse_armor, "diamond_horse_armor");
      this.registerItem(Items.lead, "lead");
      this.registerItem(Items.name_tag, "name_tag");
      this.itemModelMesher.register(Items.banner, new ItemMeshDefinition() {

         public static final String __OBFID = "CL_00002438";

         public ModelResourceLocation getModelLocation(ItemStack p_178113_1_) {
            return new ModelResourceLocation("banner", "inventory");
         }
      });
      this.registerItem(Items.record_13, "record_13");
      this.registerItem(Items.record_cat, "record_cat");
      this.registerItem(Items.record_blocks, "record_blocks");
      this.registerItem(Items.record_chirp, "record_chirp");
      this.registerItem(Items.record_far, "record_far");
      this.registerItem(Items.record_mall, "record_mall");
      this.registerItem(Items.record_mellohi, "record_mellohi");
      this.registerItem(Items.record_stal, "record_stal");
      this.registerItem(Items.record_strad, "record_strad");
      this.registerItem(Items.record_ward, "record_ward");
      this.registerItem(Items.record_11, "record_11");
      this.registerItem(Items.record_wait, "record_wait");
      this.registerItem(Items.prismarine_shard, "prismarine_shard");
      this.registerItem(Items.prismarine_crystals, "prismarine_crystals");
      this.itemModelMesher.register(Items.enchanted_book, new ItemMeshDefinition() {

         public static final String __OBFID = "CL_00002437";

         public ModelResourceLocation getModelLocation(ItemStack p_178113_1_) {
            return new ModelResourceLocation("enchanted_book", "inventory");
         }
      });
      this.itemModelMesher.register(Items.filled_map, new ItemMeshDefinition() {

         public static final String __OBFID = "CL_00002436";

         public ModelResourceLocation getModelLocation(ItemStack p_178113_1_) {
            return new ModelResourceLocation("filled_map", "inventory");
         }
      });
      this.registerBlock(Blocks.command_block, "command_block");
      this.registerItem(Items.fireworks, "fireworks");
      this.registerItem(Items.command_block_minecart, "command_block_minecart");
      this.registerBlock(Blocks.barrier, "barrier");
      this.registerBlock(Blocks.mob_spawner, "mob_spawner");
      this.registerItem(Items.written_book, "written_book");
      this.registerBlock(Blocks.brown_mushroom_block, net.minecraft.block.BlockHugeMushroom.EnumType.ALL_INSIDE.getMetadata(), "brown_mushroom_block");
      this.registerBlock(Blocks.red_mushroom_block, net.minecraft.block.BlockHugeMushroom.EnumType.ALL_INSIDE.getMetadata(), "red_mushroom_block");
      this.registerBlock(Blocks.dragon_egg, "dragon_egg");
      if(Reflector.ModelLoader_onRegisterItems.exists()) {
         Reflector.call(Reflector.ModelLoader_onRegisterItems, new Object[]{this.itemModelMesher});
      }

   }

   public void onResourceManagerReload(IResourceManager resourceManager) {
      this.itemModelMesher.rebuildCache();
   }

   public static void forgeHooksClient_putQuadColor(WorldRenderer renderer, BakedQuad quad, int color) {
      float cr = (float)(color & 255);
      float cg = (float)(color >>> 8 & 255);
      float cb = (float)(color >>> 16 & 255);
      float ca = (float)(color >>> 24 & 255);
      int[] vd = quad.getVertexData();
      int step = vd.length / 4;

      for(int i = 0; i < 4; ++i) {
         int vc = vd[3 + step * i];
         float vcr = (float)(vc & 255);
         float vcg = (float)(vc >>> 8 & 255);
         float vcb = (float)(vc >>> 16 & 255);
         float vca = (float)(vc >>> 24 & 255);
         int ncr = Math.min(255, (int)(cr * vcr / 255.0F));
         int ncg = Math.min(255, (int)(cg * vcg / 255.0F));
         int ncb = Math.min(255, (int)(cb * vcb / 255.0F));
         int nca = Math.min(255, (int)(ca * vca / 255.0F));
         renderer.putColorRGBA(renderer.getColorIndex(4 - i), ncr, ncg, ncb, nca);
      }

   }

}
