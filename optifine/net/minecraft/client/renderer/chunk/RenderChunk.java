package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Sets;
import java.nio.FloatBuffer;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RegionRenderCache;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator.Status;
import net.minecraft.client.renderer.chunk.ChunkCompileTaskGenerator.Type;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import optifine.BlockPosM;
import optifine.Config;
import optifine.Reflector;
import optifine.ReflectorForge;
import shadersmod.client.SVertexBuilder;

public class RenderChunk {

   public World world;
   public final RenderGlobal renderGlobal;
   public static int renderChunksUpdated;
   public BlockPos position;
   public CompiledChunk compiledChunk;
   public final ReentrantLock lockCompileTask;
   public final ReentrantLock lockCompiledChunk;
   public ChunkCompileTaskGenerator compileTask;
   public final Set setTileEntities;
   public final int index;
   public final FloatBuffer modelviewMatrix;
   public final VertexBuffer[] vertexBuffers;
   public AxisAlignedBB boundingBox;
   public int frameIndex;
   public boolean needsUpdate;
   public EnumMap mapEnumFacing;
   public static final String __OBFID = "CL_00002452";
   public BlockPos[] positionOffsets16;
   public static EnumWorldBlockLayer[] ENUM_WORLD_BLOCK_LAYERS = EnumWorldBlockLayer.values();
   public EnumWorldBlockLayer[] blockLayersSingle;
   public boolean isMipmaps;
   public boolean fixBlockLayer;
   public boolean playerUpdate;
   public Chunk chunk;


   public RenderChunk(World worldIn, RenderGlobal renderGlobalIn, BlockPos blockPosIn, int indexIn) {
      this.positionOffsets16 = new BlockPos[EnumFacing.VALUES.length];
      this.blockLayersSingle = new EnumWorldBlockLayer[1];
      this.isMipmaps = Config.isMipmaps();
      this.fixBlockLayer = !Reflector.BetterFoliageClient.exists();
      this.playerUpdate = false;
      this.compiledChunk = CompiledChunk.DUMMY;
      this.lockCompileTask = new ReentrantLock();
      this.lockCompiledChunk = new ReentrantLock();
      this.compileTask = null;
      this.setTileEntities = Sets.newHashSet();
      this.modelviewMatrix = GLAllocation.createDirectFloatBuffer(16);
      this.vertexBuffers = new VertexBuffer[EnumWorldBlockLayer.values().length];
      this.frameIndex = -1;
      this.needsUpdate = true;
      this.world = worldIn;
      this.renderGlobal = renderGlobalIn;
      this.index = indexIn;
      if(!blockPosIn.equals(this.getPosition())) {
         this.setPosition(blockPosIn);
      }

      if(OpenGlHelper.useVbo()) {
         for(int var5 = 0; var5 < EnumWorldBlockLayer.values().length; ++var5) {
            this.vertexBuffers[var5] = new VertexBuffer(DefaultVertexFormats.BLOCK);
         }
      }

   }

   public boolean setFrameIndex(int frameIndexIn) {
      if(this.frameIndex == frameIndexIn) {
         return false;
      } else {
         this.frameIndex = frameIndexIn;
         return true;
      }
   }

   public VertexBuffer getVertexBufferByLayer(int p_178565_1_) {
      return this.vertexBuffers[p_178565_1_];
   }

   public void setPosition(BlockPos p_178576_1_) {
      this.stopCompileTask();
      this.position = p_178576_1_;
      this.boundingBox = new AxisAlignedBB(p_178576_1_, p_178576_1_.add(16, 16, 16));
      this.initModelviewMatrix();

      for(int i = 0; i < this.positionOffsets16.length; ++i) {
         this.positionOffsets16[i] = null;
      }

      this.chunk = null;
   }

   public void resortTransparency(float p_178570_1_, float p_178570_2_, float p_178570_3_, ChunkCompileTaskGenerator p_178570_4_) {
      CompiledChunk var5 = p_178570_4_.getCompiledChunk();
      if(var5.getState() != null && !var5.isLayerEmpty(EnumWorldBlockLayer.TRANSLUCENT)) {
         WorldRenderer worldRenderer = p_178570_4_.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT);
         this.preRenderBlocks(worldRenderer, this.position);
         worldRenderer.setVertexState(var5.getState());
         this.postRenderBlocks(EnumWorldBlockLayer.TRANSLUCENT, p_178570_1_, p_178570_2_, p_178570_3_, worldRenderer, var5);
      }

   }

   public void rebuildChunk(float p_178581_1_, float p_178581_2_, float p_178581_3_, ChunkCompileTaskGenerator p_178581_4_) {
      CompiledChunk var5 = new CompiledChunk();
      boolean var6 = true;
      BlockPos var7 = this.position;
      BlockPos var8 = var7.add(15, 15, 15);
      p_178581_4_.getLock().lock();

      RegionRenderCache var9;
      label382: {
         try {
            if(p_178581_4_.getStatus() != Status.COMPILING) {
               return;
            }

            if(this.world != null) {
               var9 = this.createRegionRenderCache(this.world, var7.add(-1, -1, -1), var8.add(1, 1, 1), 1);
               if(Reflector.MinecraftForgeClient_onRebuildChunk.exists()) {
                  Reflector.call(Reflector.MinecraftForgeClient_onRebuildChunk, new Object[]{this.world, this.position, var9});
               }

               p_178581_4_.setCompiledChunk(var5);
               break label382;
            }
         } finally {
            p_178581_4_.getLock().unlock();
         }

         return;
      }

      VisGraph var10 = new VisGraph();
      HashSet var11 = Sets.newHashSet();
      if(!var9.extendedLevelsInChunkCache()) {
         ++renderChunksUpdated;
         boolean[] var28 = new boolean[ENUM_WORLD_BLOCK_LAYERS.length];
         BlockRendererDispatcher var29 = Minecraft.getMinecraft().getBlockRendererDispatcher();
         Iterator var14 = BlockPosM.getAllInBoxMutable(var7, var8).iterator();
         boolean forgeHasTileEntityExists = Reflector.ForgeBlock_hasTileEntity.exists();
         boolean forgeBlockCanRenderInLayerExists = Reflector.ForgeBlock_canRenderInLayer.exists();
         boolean forgeHooksSetRenderLayerExists = Reflector.ForgeHooksClient_setRenderLayer.exists();

         while(var14.hasNext()) {
            BlockPosM var30 = (BlockPosM)var14.next();
            IBlockState var31 = var9.getBlockState(var30);
            Block var32 = var31.getBlock();
            if(var32.isOpaqueCube()) {
               var10.func_178606_a(var30);
            }

            if(ReflectorForge.blockHasTileEntity(var31)) {
               TileEntity var33 = var9.getTileEntity(new BlockPos(var30));
               TileEntitySpecialRenderer i = TileEntityRendererDispatcher.instance.getSpecialRenderer(var33);
               if(var33 != null && i != null) {
                  var5.addTileEntity(var33);
                  if(i.forceTileEntityRender()) {
                     var11.add(var33);
                  }
               }
            }

            EnumWorldBlockLayer[] var38;
            if(forgeBlockCanRenderInLayerExists) {
               var38 = ENUM_WORLD_BLOCK_LAYERS;
            } else {
               var38 = this.blockLayersSingle;
               var38[0] = var32.getBlockLayer();
            }

            for(int var40 = 0; var40 < var38.length; ++var40) {
               EnumWorldBlockLayer var34 = var38[var40];
               if(forgeBlockCanRenderInLayerExists) {
                  boolean var35 = Reflector.callBoolean(var32, Reflector.ForgeBlock_canRenderInLayer, new Object[]{var34});
                  if(!var35) {
                     continue;
                  }
               }

               if(forgeHooksSetRenderLayerExists) {
                  Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, new Object[]{var34});
               }

               if(this.fixBlockLayer) {
                  var34 = this.fixBlockLayer(var32, var34);
               }

               int var41 = var34.ordinal();
               if(var32.getRenderType() != -1) {
                  WorldRenderer var20 = p_178581_4_.getRegionRenderCacheBuilder().getWorldRendererByLayerId(var41);
                  var20.setBlockLayer(var34);
                  if(!var5.isLayerStarted(var34)) {
                     var5.setLayerStarted(var34);
                     this.preRenderBlocks(var20, var7);
                  }

                  var28[var41] |= var29.renderBlock(var31, var30, var9, var20);
               }
            }
         }

         EnumWorldBlockLayer[] var351 = ENUM_WORLD_BLOCK_LAYERS;
         int var36 = var351.length;

         for(int var37 = 0; var37 < var36; ++var37) {
            EnumWorldBlockLayer var39 = var351[var37];
            if(var28[var39.ordinal()]) {
               var5.setLayerUsed(var39);
            }

            if(var5.isLayerStarted(var39)) {
               if(Config.isShaders()) {
                  SVertexBuilder.calcNormalChunkLayer(p_178581_4_.getRegionRenderCacheBuilder().getWorldRendererByLayer(var39));
               }

               this.postRenderBlocks(var39, p_178581_1_, p_178581_2_, p_178581_3_, p_178581_4_.getRegionRenderCacheBuilder().getWorldRendererByLayer(var39), var5);
            }
         }
      }

      var5.setVisibility(var10.computeVisibility());
      this.lockCompileTask.lock();

      try {
         HashSet var331 = Sets.newHashSet(var11);
         HashSet var341 = Sets.newHashSet(this.setTileEntities);
         var331.removeAll(this.setTileEntities);
         var341.removeAll(var11);
         this.setTileEntities.clear();
         this.setTileEntities.addAll(var11);
         this.renderGlobal.updateTileEntities(var341, var331);
      } finally {
         this.lockCompileTask.unlock();
      }

   }

   public void finishCompileTask() {
      this.lockCompileTask.lock();

      try {
         if(this.compileTask != null && this.compileTask.getStatus() != Status.DONE) {
            this.compileTask.finish();
            this.compileTask = null;
         }
      } finally {
         this.lockCompileTask.unlock();
      }

   }

   public ReentrantLock getLockCompileTask() {
      return this.lockCompileTask;
   }

   public ChunkCompileTaskGenerator makeCompileTaskChunk() {
      this.lockCompileTask.lock();

      ChunkCompileTaskGenerator var1;
      try {
         this.finishCompileTask();
         this.compileTask = new ChunkCompileTaskGenerator(this, Type.REBUILD_CHUNK);
         var1 = this.compileTask;
      } finally {
         this.lockCompileTask.unlock();
      }

      return var1;
   }

   public ChunkCompileTaskGenerator makeCompileTaskTransparency() {
      this.lockCompileTask.lock();

      ChunkCompileTaskGenerator var1;
      try {
         if(this.compileTask == null || this.compileTask.getStatus() != Status.PENDING) {
            if(this.compileTask != null && this.compileTask.getStatus() != Status.DONE) {
               this.compileTask.finish();
               this.compileTask = null;
            }

            this.compileTask = new ChunkCompileTaskGenerator(this, Type.RESORT_TRANSPARENCY);
            this.compileTask.setCompiledChunk(this.compiledChunk);
            var1 = this.compileTask;
            ChunkCompileTaskGenerator var2 = var1;
            return var2;
         }

         var1 = null;
      } finally {
         this.lockCompileTask.unlock();
      }

      return var1;
   }

   public void preRenderBlocks(WorldRenderer p_178573_1_, BlockPos p_178573_2_) {
      p_178573_1_.begin(7, DefaultVertexFormats.BLOCK);
      p_178573_1_.setTranslation((double)(-p_178573_2_.getX()), (double)(-p_178573_2_.getY()), (double)(-p_178573_2_.getZ()));
   }

   public void postRenderBlocks(EnumWorldBlockLayer p_178584_1_, float p_178584_2_, float p_178584_3_, float p_178584_4_, WorldRenderer p_178584_5_, CompiledChunk p_178584_6_) {
      if(p_178584_1_ == EnumWorldBlockLayer.TRANSLUCENT && !p_178584_6_.isLayerEmpty(p_178584_1_)) {
         p_178584_5_.sortVertexData(p_178584_2_, p_178584_3_, p_178584_4_);
         p_178584_6_.setState(p_178584_5_.getVertexState());
      }

      p_178584_5_.finishDrawing();
   }

   public void initModelviewMatrix() {
      GlStateManager.pushMatrix();
      GlStateManager.loadIdentity();
      float var1 = 1.000001F;
      GlStateManager.translate(-8.0F, -8.0F, -8.0F);
      GlStateManager.scale(var1, var1, var1);
      GlStateManager.translate(8.0F, 8.0F, 8.0F);
      GlStateManager.getFloat(2982, this.modelviewMatrix);
      GlStateManager.popMatrix();
   }

   public void multModelviewMatrix() {
      GlStateManager.multMatrix(this.modelviewMatrix);
   }

   public CompiledChunk getCompiledChunk() {
      return this.compiledChunk;
   }

   public void setCompiledChunk(CompiledChunk p_178580_1_) {
      this.lockCompiledChunk.lock();

      try {
         this.compiledChunk = p_178580_1_;
      } finally {
         this.lockCompiledChunk.unlock();
      }

   }

   public void stopCompileTask() {
      this.finishCompileTask();
      this.compiledChunk = CompiledChunk.DUMMY;
   }

   public void deleteGlResources() {
      this.stopCompileTask();
      this.world = null;

      for(int var1 = 0; var1 < EnumWorldBlockLayer.values().length; ++var1) {
         if(this.vertexBuffers[var1] != null) {
            this.vertexBuffers[var1].deleteGlBuffers();
         }
      }

   }

   public BlockPos getPosition() {
      return this.position;
   }

   public void setNeedsUpdate(boolean p_178575_1_) {
      this.needsUpdate = p_178575_1_;
      if(this.needsUpdate) {
         if(this.isWorldPlayerUpdate()) {
            this.playerUpdate = true;
         }
      } else {
         this.playerUpdate = false;
      }

   }

   public boolean isNeedsUpdate() {
      return this.needsUpdate;
   }

   public BlockPos getBlockPosOffset16(EnumFacing p_500163_1_) {
      return this.getPositionOffset16(p_500163_1_);
   }

   public BlockPos getPositionOffset16(EnumFacing facing) {
      int index = facing.getIndex();
      BlockPos posOffset = this.positionOffsets16[index];
      if(posOffset == null) {
         posOffset = this.getPosition().offset(facing, 16);
         this.positionOffsets16[index] = posOffset;
      }

      return posOffset;
   }

   public boolean isWorldPlayerUpdate() {
      if(this.world instanceof WorldClient) {
         WorldClient worldClient = (WorldClient)this.world;
         return worldClient.isPlayerUpdate();
      } else {
         return false;
      }
   }

   public boolean isPlayerUpdate() {
      return this.playerUpdate;
   }

   public RegionRenderCache createRegionRenderCache(World world, BlockPos from, BlockPos to, int subtract) {
      return new RegionRenderCache(world, from, to, subtract);
   }

   public EnumWorldBlockLayer fixBlockLayer(Block block, EnumWorldBlockLayer layer) {
      if(this.isMipmaps) {
         if(layer == EnumWorldBlockLayer.CUTOUT) {
            if(block instanceof BlockRedstoneWire) {
               return layer;
            }

            if(block instanceof BlockCactus) {
               return layer;
            }

            return EnumWorldBlockLayer.CUTOUT_MIPPED;
         }
      } else if(layer == EnumWorldBlockLayer.CUTOUT_MIPPED) {
         return EnumWorldBlockLayer.CUTOUT;
      }

      return layer;
   }

   public Chunk getChunk() {
      return this.getChunk(this.position);
   }

   public Chunk getChunk(BlockPos posIn) {
      Chunk chunkLocal = this.chunk;
      if(chunkLocal != null && chunkLocal.isLoaded()) {
         return chunkLocal;
      } else {
         chunkLocal = this.world.getChunkFromBlockCoords(posIn);
         this.chunk = chunkLocal;
         return chunkLocal;
      }
   }

   public boolean isChunkRegionEmpty() {
      return this.isChunkRegionEmpty(this.position);
   }

   public boolean isChunkRegionEmpty(BlockPos posIn) {
      int yStart = posIn.getY();
      int yEnd = yStart + 15;
      return this.getChunk(posIn).getAreLevelsEmpty(yStart, yEnd);
   }

}
