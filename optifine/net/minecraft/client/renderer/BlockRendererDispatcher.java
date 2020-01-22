package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.ChestRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.WeightedBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel.Builder;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import optifine.Config;
import optifine.Reflector;
import shadersmod.client.SVertexBuilder;

public class BlockRendererDispatcher implements IResourceManagerReloadListener {

   public BlockModelShapes blockModelShapes;
   public final GameSettings gameSettings;
   public final BlockModelRenderer blockModelRenderer = new BlockModelRenderer();
   public final ChestRenderer chestRenderer = new ChestRenderer();
   public final BlockFluidRenderer fluidRenderer = new BlockFluidRenderer();
   public static final String __OBFID = "CL_00002520";


   public BlockRendererDispatcher(BlockModelShapes p_i46237_1_, GameSettings p_i46237_2_) {
      this.blockModelShapes = p_i46237_1_;
      this.gameSettings = p_i46237_2_;
   }

   public BlockModelShapes getBlockModelShapes() {
      return this.blockModelShapes;
   }

   public void renderBlockDamage(IBlockState p_175020_1_, BlockPos p_175020_2_, TextureAtlasSprite p_175020_3_, IBlockAccess p_175020_4_) {
      Block var5 = p_175020_1_.getBlock();
      int var6 = var5.getRenderType();
      if(var6 == 3) {
         p_175020_1_ = var5.getActualState(p_175020_1_, p_175020_4_, p_175020_2_);
         IBakedModel var7 = this.blockModelShapes.getModelForState(p_175020_1_);
         if(Reflector.ISmartBlockModel.isInstance(var7)) {
            IBlockState var15 = (IBlockState)Reflector.call(var5, Reflector.ForgeBlock_getExtendedState, new Object[]{p_175020_1_, p_175020_4_, p_175020_2_});
            EnumWorldBlockLayer[] arr$ = EnumWorldBlockLayer.values();
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
               EnumWorldBlockLayer layer = arr$[i$];
               if(Reflector.callBoolean(var5, Reflector.ForgeBlock_canRenderInLayer, new Object[]{layer})) {
                  Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, new Object[]{layer});
                  IBakedModel targetLayer = (IBakedModel)Reflector.call(var7, Reflector.ISmartBlockModel_handleBlockState, new Object[]{var15});
                  IBakedModel damageModel = (new Builder(targetLayer, p_175020_3_)).makeBakedModel();
                  this.blockModelRenderer.renderModel(p_175020_4_, damageModel, p_175020_1_, p_175020_2_, Tessellator.getInstance().getWorldRenderer());
               }
            }

            return;
         }

         IBakedModel var8 = (new Builder(var7, p_175020_3_)).makeBakedModel();
         this.blockModelRenderer.renderModel(p_175020_4_, var8, p_175020_1_, p_175020_2_, Tessellator.getInstance().getWorldRenderer());
      }

   }

   public boolean renderBlock(IBlockState p_175018_1_, BlockPos p_175018_2_, IBlockAccess p_175018_3_, WorldRenderer p_175018_4_) {
      try {
         int var8 = p_175018_1_.getBlock().getRenderType();
         if(var8 == -1) {
            return false;
         } else {
            switch(var8) {
            case 1:
               if(Config.isShaders()) {
                  SVertexBuilder.pushEntity(p_175018_1_, p_175018_2_, p_175018_3_, p_175018_4_);
               }

               boolean var61 = this.fluidRenderer.renderFluid(p_175018_3_, p_175018_1_, p_175018_2_, p_175018_4_);
               if(Config.isShaders()) {
                  SVertexBuilder.popEntity(p_175018_4_);
               }

               return var61;
            case 2:
               return false;
            case 3:
               IBakedModel var71 = this.getModelFromBlockState(p_175018_1_, p_175018_3_, p_175018_2_);
               if(Config.isShaders()) {
                  SVertexBuilder.pushEntity(p_175018_1_, p_175018_2_, p_175018_3_, p_175018_4_);
               }

               boolean flag3 = this.blockModelRenderer.renderModel(p_175018_3_, var71, p_175018_1_, p_175018_2_, p_175018_4_);
               if(Config.isShaders()) {
                  SVertexBuilder.popEntity(p_175018_4_);
               }

               return flag3;
            default:
               return false;
            }
         }
      } catch (Throwable var9) {
         CrashReport var6 = CrashReport.makeCrashReport(var9, "Tesselating block in world");
         CrashReportCategory var7 = var6.makeCategory("Block being tesselated");
         CrashReportCategory.addBlockInfo(var7, p_175018_2_, p_175018_1_.getBlock(), p_175018_1_.getBlock().getMetaFromState(p_175018_1_));
         throw new ReportedException(var6);
      }
   }

   public BlockModelRenderer getBlockModelRenderer() {
      return this.blockModelRenderer;
   }

   public IBakedModel getBakedModel(IBlockState p_175017_1_, BlockPos p_175017_2_) {
      IBakedModel var3 = this.blockModelShapes.getModelForState(p_175017_1_);
      if(p_175017_2_ != null && this.gameSettings.allowBlockAlternatives && var3 instanceof WeightedBakedModel) {
         var3 = ((WeightedBakedModel)var3).getAlternativeModel(MathHelper.getPositionRandom(p_175017_2_));
      }

      return var3;
   }

   public IBakedModel getModelFromBlockState(IBlockState p_175022_1_, IBlockAccess p_175022_2_, BlockPos p_175022_3_) {
      Block var4 = p_175022_1_.getBlock();
      if(p_175022_2_.getWorldType() != WorldType.DEBUG_WORLD) {
         try {
            p_175022_1_ = var4.getActualState(p_175022_1_, p_175022_2_, p_175022_3_);
         } catch (Exception var7) {
            ;
         }
      }

      IBakedModel var5 = this.blockModelShapes.getModelForState(p_175022_1_);
      if(p_175022_3_ != null && this.gameSettings.allowBlockAlternatives && var5 instanceof WeightedBakedModel) {
         var5 = ((WeightedBakedModel)var5).getAlternativeModel(MathHelper.getPositionRandom(p_175022_3_));
      }

      if(Reflector.ISmartBlockModel.isInstance(var5)) {
         IBlockState extendedState = (IBlockState)Reflector.call(var4, Reflector.ForgeBlock_getExtendedState, new Object[]{p_175022_1_, p_175022_2_, p_175022_3_});
         var5 = (IBakedModel)Reflector.call(var5, Reflector.ISmartBlockModel_handleBlockState, new Object[]{extendedState});
      }

      return var5;
   }

   public void renderBlockBrightness(IBlockState p_175016_1_, float p_175016_2_) {
      int var3 = p_175016_1_.getBlock().getRenderType();
      if(var3 != -1) {
         switch(var3) {
         case 1:
         default:
            break;
         case 2:
            this.chestRenderer.renderChestBrightness(p_175016_1_.getBlock(), p_175016_2_);
            break;
         case 3:
            IBakedModel var4 = this.getBakedModel(p_175016_1_, (BlockPos)null);
            this.blockModelRenderer.renderModelBrightness(var4, p_175016_1_, p_175016_2_, true);
         }
      }

   }

   public boolean isRenderTypeChest(Block p_175021_1_, int p_175021_2_) {
      if(p_175021_1_ == null) {
         return false;
      } else {
         int var3 = p_175021_1_.getRenderType();
         return var3 == 3?false:var3 == 2;
      }
   }

   public void onResourceManagerReload(IResourceManager resourceManager) {
      this.fluidRenderer.initAtlasSprites();
   }
}
