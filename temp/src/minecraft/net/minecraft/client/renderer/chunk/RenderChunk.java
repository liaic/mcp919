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
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.src.BlockPosM;
import net.minecraft.src.Config;
import net.minecraft.src.Reflector;
import net.minecraft.src.ReflectorForge;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import shadersmod.client.SVertexBuilder;

public class RenderChunk {
   private World field_178588_d;
   private final RenderGlobal field_178589_e;
   public static int field_178592_a;
   private BlockPos field_178586_f;
   public CompiledChunk field_178590_b = CompiledChunk.field_178502_a;
   private final ReentrantLock field_178587_g = new ReentrantLock();
   private final ReentrantLock field_178598_h = new ReentrantLock();
   private ChunkCompileTaskGenerator field_178599_i = null;
   private final Set field_181056_j = Sets.newHashSet();
   private final int field_178596_j;
   private final FloatBuffer field_178597_k = GLAllocation.func_74529_h(16);
   private final VertexBuffer[] field_178594_l = new VertexBuffer[EnumWorldBlockLayer.values().length];
   public AxisAlignedBB field_178591_c;
   private int field_178595_m = -1;
   private boolean field_178593_n = true;
   private EnumMap field_181702_p;
   private static final String __OBFID = "CL_00002452";
   private BlockPos[] positionOffsets16 = new BlockPos[EnumFacing.field_82609_l.length];
   private static EnumWorldBlockLayer[] ENUM_WORLD_BLOCK_LAYERS = EnumWorldBlockLayer.values();
   private EnumWorldBlockLayer[] blockLayersSingle = new EnumWorldBlockLayer[1];
   private boolean isMipmaps = Config.isMipmaps();
   private boolean fixBlockLayer = !Reflector.BetterFoliageClient.exists();
   private boolean playerUpdate = false;
   private Chunk chunk;

   public RenderChunk(World p_i46197_1_, RenderGlobal p_i46197_2_, BlockPos p_i46197_3_, int p_i46197_4_) {
      this.field_178588_d = p_i46197_1_;
      this.field_178589_e = p_i46197_2_;
      this.field_178596_j = p_i46197_4_;
      if(!p_i46197_3_.equals(this.func_178568_j())) {
         this.func_178576_a(p_i46197_3_);
      }

      if(OpenGlHelper.func_176075_f()) {
         for(int i = 0; i < EnumWorldBlockLayer.values().length; ++i) {
            this.field_178594_l[i] = new VertexBuffer(DefaultVertexFormats.field_176600_a);
         }
      }

   }

   public boolean func_178577_a(int p_178577_1_) {
      if(this.field_178595_m == p_178577_1_) {
         return false;
      } else {
         this.field_178595_m = p_178577_1_;
         return true;
      }
   }

   public VertexBuffer func_178565_b(int p_178565_1_) {
      return this.field_178594_l[p_178565_1_];
   }

   public void func_178576_a(BlockPos p_178576_1_) {
      this.func_178585_h();
      this.field_178586_f = p_178576_1_;
      this.field_178591_c = new AxisAlignedBB(p_178576_1_, p_178576_1_.func_177982_a(16, 16, 16));
      this.func_178567_n();

      for(int i = 0; i < this.positionOffsets16.length; ++i) {
         this.positionOffsets16[i] = null;
      }

      this.chunk = null;
   }

   public void func_178570_a(float p_178570_1_, float p_178570_2_, float p_178570_3_, ChunkCompileTaskGenerator p_178570_4_) {
      CompiledChunk compiledchunk = p_178570_4_.func_178544_c();
      if(compiledchunk.func_178487_c() != null && !compiledchunk.func_178491_b(EnumWorldBlockLayer.TRANSLUCENT)) {
         WorldRenderer worldrenderer = p_178570_4_.func_178545_d().func_179038_a(EnumWorldBlockLayer.TRANSLUCENT);
         this.func_178573_a(worldrenderer, this.field_178586_f);
         worldrenderer.func_178993_a(compiledchunk.func_178487_c());
         this.func_178584_a(EnumWorldBlockLayer.TRANSLUCENT, p_178570_1_, p_178570_2_, p_178570_3_, worldrenderer, compiledchunk);
      }

   }

   public void func_178581_b(float p_178581_1_, float p_178581_2_, float p_178581_3_, ChunkCompileTaskGenerator p_178581_4_) {
      CompiledChunk compiledchunk = new CompiledChunk();
      boolean flag = true;
      BlockPos blockpos = this.field_178586_f;
      BlockPos blockpos1 = blockpos.func_177982_a(15, 15, 15);
      p_178581_4_.func_178540_f().lock();

      RegionRenderCache regionrendercache;
      try {
         if(p_178581_4_.func_178546_a() != ChunkCompileTaskGenerator.Status.COMPILING) {
            return;
         }

         if(this.field_178588_d == null) {
            return;
         }

         regionrendercache = this.createRegionRenderCache(this.field_178588_d, blockpos.func_177982_a(-1, -1, -1), blockpos1.func_177982_a(1, 1, 1), 1);
         if(Reflector.MinecraftForgeClient_onRebuildChunk.exists()) {
            Reflector.call(Reflector.MinecraftForgeClient_onRebuildChunk, new Object[]{this.field_178588_d, this.field_178586_f, regionrendercache});
         }

         p_178581_4_.func_178543_a(compiledchunk);
      } finally {
         p_178581_4_.func_178540_f().unlock();
      }

      VisGraph var10 = new VisGraph();
      HashSet var11 = Sets.newHashSet();
      if(!regionrendercache.func_72806_N()) {
         ++field_178592_a;
         boolean[] aboolean = new boolean[ENUM_WORLD_BLOCK_LAYERS.length];
         BlockRendererDispatcher blockrendererdispatcher = Minecraft.func_71410_x().func_175602_ab();
         Iterator iterator = BlockPosM.getAllInBoxMutable(blockpos, blockpos1).iterator();
         boolean flag1 = Reflector.ForgeBlock_hasTileEntity.exists();
         boolean flag2 = Reflector.ForgeBlock_canRenderInLayer.exists();
         boolean flag3 = Reflector.ForgeHooksClient_setRenderLayer.exists();

         while(iterator.hasNext()) {
            BlockPosM blockposm = (BlockPosM)iterator.next();
            IBlockState iblockstate = regionrendercache.func_180495_p(blockposm);
            Block block = iblockstate.func_177230_c();
            if(block.func_149662_c()) {
               var10.func_178606_a(blockposm);
            }

            if(ReflectorForge.blockHasTileEntity(iblockstate)) {
               TileEntity tileentity = regionrendercache.func_175625_s(new BlockPos(blockposm));
               TileEntitySpecialRenderer tileentityspecialrenderer = TileEntityRendererDispatcher.field_147556_a.func_147547_b(tileentity);
               if(tileentity != null && tileentityspecialrenderer != null) {
                  compiledchunk.func_178490_a(tileentity);
                  if(tileentityspecialrenderer.func_181055_a()) {
                     var11.add(tileentity);
                  }
               }
            }

            EnumWorldBlockLayer[] aenumworldblocklayer;
            if(flag2) {
               aenumworldblocklayer = ENUM_WORLD_BLOCK_LAYERS;
            } else {
               aenumworldblocklayer = this.blockLayersSingle;
               aenumworldblocklayer[0] = block.func_180664_k();
            }

            for(int i = 0; i < aenumworldblocklayer.length; ++i) {
               EnumWorldBlockLayer enumworldblocklayer = aenumworldblocklayer[i];
               if(flag2) {
                  boolean flag4 = Reflector.callBoolean(block, Reflector.ForgeBlock_canRenderInLayer, new Object[]{enumworldblocklayer});
                  if(!flag4) {
                     continue;
                  }
               }

               if(flag3) {
                  Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, new Object[]{enumworldblocklayer});
               }

               if(this.fixBlockLayer) {
                  enumworldblocklayer = this.fixBlockLayer(block, enumworldblocklayer);
               }

               int j = enumworldblocklayer.ordinal();
               if(block.func_149645_b() != -1) {
                  WorldRenderer worldrenderer = p_178581_4_.func_178545_d().func_179039_a(j);
                  worldrenderer.setBlockLayer(enumworldblocklayer);
                  if(!compiledchunk.func_178492_d(enumworldblocklayer)) {
                     compiledchunk.func_178493_c(enumworldblocklayer);
                     this.func_178573_a(worldrenderer, blockpos);
                  }

                  aboolean[j] |= blockrendererdispatcher.func_175018_a(iblockstate, blockposm, regionrendercache, worldrenderer);
               }
            }
         }

         for(EnumWorldBlockLayer enumworldblocklayer1 : ENUM_WORLD_BLOCK_LAYERS) {
            if(aboolean[enumworldblocklayer1.ordinal()]) {
               compiledchunk.func_178486_a(enumworldblocklayer1);
            }

            if(compiledchunk.func_178492_d(enumworldblocklayer1)) {
               if(Config.isShaders()) {
                  SVertexBuilder.calcNormalChunkLayer(p_178581_4_.func_178545_d().func_179038_a(enumworldblocklayer1));
               }

               this.func_178584_a(enumworldblocklayer1, p_178581_1_, p_178581_2_, p_178581_3_, p_178581_4_.func_178545_d().func_179038_a(enumworldblocklayer1), compiledchunk);
            }
         }
      }

      compiledchunk.func_178488_a(var10.func_178607_a());
      this.field_178587_g.lock();

      try {
         HashSet hashset1 = Sets.newHashSet(var11);
         HashSet hashset2 = Sets.newHashSet(this.field_181056_j);
         hashset1.removeAll(this.field_181056_j);
         hashset2.removeAll(var11);
         this.field_181056_j.clear();
         this.field_181056_j.addAll(var11);
         this.field_178589_e.func_181023_a(hashset2, hashset1);
      } finally {
         this.field_178587_g.unlock();
      }

   }

   protected void func_178578_b() {
      this.field_178587_g.lock();

      try {
         if(this.field_178599_i != null && this.field_178599_i.func_178546_a() != ChunkCompileTaskGenerator.Status.DONE) {
            this.field_178599_i.func_178542_e();
            this.field_178599_i = null;
         }
      } finally {
         this.field_178587_g.unlock();
      }

   }

   public ReentrantLock func_178579_c() {
      return this.field_178587_g;
   }

   public ChunkCompileTaskGenerator func_178574_d() {
      this.field_178587_g.lock();

      ChunkCompileTaskGenerator chunkcompiletaskgenerator;
      try {
         this.func_178578_b();
         this.field_178599_i = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.REBUILD_CHUNK);
         chunkcompiletaskgenerator = this.field_178599_i;
      } finally {
         this.field_178587_g.unlock();
      }

      return chunkcompiletaskgenerator;
   }

   public ChunkCompileTaskGenerator func_178582_e() {
      this.field_178587_g.lock();

      ChunkCompileTaskGenerator chunkcompiletaskgenerator1;
      try {
         if(this.field_178599_i != null && this.field_178599_i.func_178546_a() == ChunkCompileTaskGenerator.Status.PENDING) {
            ChunkCompileTaskGenerator chunkcompiletaskgenerator2 = null;
            return chunkcompiletaskgenerator2;
         }

         if(this.field_178599_i != null && this.field_178599_i.func_178546_a() != ChunkCompileTaskGenerator.Status.DONE) {
            this.field_178599_i.func_178542_e();
            this.field_178599_i = null;
         }

         this.field_178599_i = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY);
         this.field_178599_i.func_178543_a(this.field_178590_b);
         ChunkCompileTaskGenerator chunkcompiletaskgenerator = this.field_178599_i;
         chunkcompiletaskgenerator1 = chunkcompiletaskgenerator;
      } finally {
         this.field_178587_g.unlock();
      }

      return chunkcompiletaskgenerator1;
   }

   private void func_178573_a(WorldRenderer p_178573_1_, BlockPos p_178573_2_) {
      p_178573_1_.func_181668_a(7, DefaultVertexFormats.field_176600_a);
      p_178573_1_.func_178969_c((double)(-p_178573_2_.func_177958_n()), (double)(-p_178573_2_.func_177956_o()), (double)(-p_178573_2_.func_177952_p()));
   }

   private void func_178584_a(EnumWorldBlockLayer p_178584_1_, float p_178584_2_, float p_178584_3_, float p_178584_4_, WorldRenderer p_178584_5_, CompiledChunk p_178584_6_) {
      if(p_178584_1_ == EnumWorldBlockLayer.TRANSLUCENT && !p_178584_6_.func_178491_b(p_178584_1_)) {
         p_178584_5_.func_181674_a(p_178584_2_, p_178584_3_, p_178584_4_);
         p_178584_6_.func_178494_a(p_178584_5_.func_181672_a());
      }

      p_178584_5_.func_178977_d();
   }

   private void func_178567_n() {
      GlStateManager.func_179094_E();
      GlStateManager.func_179096_D();
      float f = 1.000001F;
      GlStateManager.func_179109_b(-8.0F, -8.0F, -8.0F);
      GlStateManager.func_179152_a(f, f, f);
      GlStateManager.func_179109_b(8.0F, 8.0F, 8.0F);
      GlStateManager.func_179111_a(2982, this.field_178597_k);
      GlStateManager.func_179121_F();
   }

   public void func_178572_f() {
      GlStateManager.func_179110_a(this.field_178597_k);
   }

   public CompiledChunk func_178571_g() {
      return this.field_178590_b;
   }

   public void func_178580_a(CompiledChunk p_178580_1_) {
      this.field_178598_h.lock();

      try {
         this.field_178590_b = p_178580_1_;
      } finally {
         this.field_178598_h.unlock();
      }

   }

   public void func_178585_h() {
      this.func_178578_b();
      this.field_178590_b = CompiledChunk.field_178502_a;
   }

   public void func_178566_a() {
      this.func_178585_h();
      this.field_178588_d = null;

      for(int i = 0; i < EnumWorldBlockLayer.values().length; ++i) {
         if(this.field_178594_l[i] != null) {
            this.field_178594_l[i].func_177362_c();
         }
      }

   }

   public BlockPos func_178568_j() {
      return this.field_178586_f;
   }

   public void func_178575_a(boolean p_178575_1_) {
      this.field_178593_n = p_178575_1_;
      if(this.field_178593_n) {
         if(this.isWorldPlayerUpdate()) {
            this.playerUpdate = true;
         }
      } else {
         this.playerUpdate = false;
      }

   }

   public boolean func_178569_m() {
      return this.field_178593_n;
   }

   public BlockPos func_181701_a(EnumFacing p_181701_1_) {
      return this.getPositionOffset16(p_181701_1_);
   }

   public BlockPos getPositionOffset16(EnumFacing p_getPositionOffset16_1_) {
      int i = p_getPositionOffset16_1_.func_176745_a();
      BlockPos blockpos = this.positionOffsets16[i];
      if(blockpos == null) {
         blockpos = this.func_178568_j().func_177967_a(p_getPositionOffset16_1_, 16);
         this.positionOffsets16[i] = blockpos;
      }

      return blockpos;
   }

   private boolean isWorldPlayerUpdate() {
      if(this.field_178588_d instanceof WorldClient) {
         WorldClient worldclient = (WorldClient)this.field_178588_d;
         return worldclient.isPlayerUpdate();
      } else {
         return false;
      }
   }

   public boolean isPlayerUpdate() {
      return this.playerUpdate;
   }

   protected RegionRenderCache createRegionRenderCache(World p_createRegionRenderCache_1_, BlockPos p_createRegionRenderCache_2_, BlockPos p_createRegionRenderCache_3_, int p_createRegionRenderCache_4_) {
      return new RegionRenderCache(p_createRegionRenderCache_1_, p_createRegionRenderCache_2_, p_createRegionRenderCache_3_, p_createRegionRenderCache_4_);
   }

   private EnumWorldBlockLayer fixBlockLayer(Block p_fixBlockLayer_1_, EnumWorldBlockLayer p_fixBlockLayer_2_) {
      if(this.isMipmaps) {
         if(p_fixBlockLayer_2_ == EnumWorldBlockLayer.CUTOUT) {
            if(p_fixBlockLayer_1_ instanceof BlockRedstoneWire) {
               return p_fixBlockLayer_2_;
            }

            if(p_fixBlockLayer_1_ instanceof BlockCactus) {
               return p_fixBlockLayer_2_;
            }

            return EnumWorldBlockLayer.CUTOUT_MIPPED;
         }
      } else if(p_fixBlockLayer_2_ == EnumWorldBlockLayer.CUTOUT_MIPPED) {
         return EnumWorldBlockLayer.CUTOUT;
      }

      return p_fixBlockLayer_2_;
   }

   public Chunk getChunk() {
      return this.getChunk(this.field_178586_f);
   }

   private Chunk getChunk(BlockPos p_getChunk_1_) {
      Chunk chunk = this.chunk;
      if(chunk != null && chunk.func_177410_o()) {
         return chunk;
      } else {
         chunk = this.field_178588_d.func_175726_f(p_getChunk_1_);
         this.chunk = chunk;
         return chunk;
      }
   }

   public boolean isChunkRegionEmpty() {
      return this.isChunkRegionEmpty(this.field_178586_f);
   }

   private boolean isChunkRegionEmpty(BlockPos p_isChunkRegionEmpty_1_) {
      int i = p_isChunkRegionEmpty_1_.func_177956_o();
      int j = i + 15;
      return this.getChunk(p_isChunkRegionEmpty_1_).func_76606_c(i, j);
   }
}
