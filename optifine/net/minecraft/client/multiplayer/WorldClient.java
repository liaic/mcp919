package net.minecraft.client.multiplayer;

import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EntityFirework.StarterFX;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.SaveDataMemoryStorage;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraft.world.storage.WorldInfo;
import optifine.BlockPosM;
import optifine.Config;
import optifine.CustomGuis;
import optifine.DynamicLights;
import optifine.PlayerControllerOF;
import optifine.Reflector;

public class WorldClient extends World {

   public NetHandlerPlayClient sendQueue;
   public ChunkProviderClient clientChunkProvider;
   public final Set entityList = Sets.newHashSet();
   public final Set entitySpawnQueue = Sets.newHashSet();
   public final Minecraft mc = Minecraft.getMinecraft();
   public final Set previousActiveChunkSet = Sets.newHashSet();
   public static final String __OBFID = "CL_00000882";
   public BlockPosM randomTickPosM = new BlockPosM(0, 0, 0, 3);
   public boolean playerUpdate = false;


   public WorldClient(NetHandlerPlayClient p_i45063_1_, WorldSettings p_i45063_2_, int p_i45063_3_, EnumDifficulty p_i45063_4_, Profiler p_i45063_5_) {
      super(new SaveHandlerMP(), new WorldInfo(p_i45063_2_, "MpServer"), WorldProvider.getProviderForDimension(p_i45063_3_), p_i45063_5_, true);
      this.sendQueue = p_i45063_1_;
      this.getWorldInfo().setDifficulty(p_i45063_4_);
      this.provider.registerWorld(this);
      this.setSpawnPoint(new BlockPos(8, 64, 8));
      this.chunkProvider = this.createChunkProvider();
      this.mapStorage = new SaveDataMemoryStorage();
      this.calculateInitialSkylight();
      this.calculateInitialWeather();
      Reflector.postForgeBusEvent(Reflector.WorldEvent_Load_Constructor, new Object[]{this});
      if(this.mc.playerController != null && this.mc.playerController.getClass() == PlayerControllerMP.class) {
         this.mc.playerController = new PlayerControllerOF(this.mc, p_i45063_1_);
         CustomGuis.setPlayerControllerOF((PlayerControllerOF)this.mc.playerController);
      }

   }

   public void tick() {
      super.tick();
      this.setTotalWorldTime(this.getTotalWorldTime() + 1L);
      if(this.getGameRules().getBoolean("doDaylightCycle")) {
         this.setWorldTime(this.getWorldTime() + 1L);
      }

      this.theProfiler.startSection("reEntryProcessing");

      for(int var1 = 0; var1 < 10 && !this.entitySpawnQueue.isEmpty(); ++var1) {
         Entity var2 = (Entity)this.entitySpawnQueue.iterator().next();
         this.entitySpawnQueue.remove(var2);
         if(!this.loadedEntityList.contains(var2)) {
            this.spawnEntityInWorld(var2);
         }
      }

      this.theProfiler.endStartSection("chunkCache");
      this.clientChunkProvider.unloadQueuedChunks();
      this.theProfiler.endStartSection("blocks");
      this.updateBlocks();
      this.theProfiler.endSection();
   }

   public void invalidateBlockReceiveRegion(int p_73031_1_, int p_73031_2_, int p_73031_3_, int p_73031_4_, int p_73031_5_, int p_73031_6_) {}

   public IChunkProvider createChunkProvider() {
      this.clientChunkProvider = new ChunkProviderClient(this);
      return this.clientChunkProvider;
   }

   public void updateBlocks() {
      super.updateBlocks();
      this.previousActiveChunkSet.retainAll(this.activeChunkSet);
      if(this.previousActiveChunkSet.size() == this.activeChunkSet.size()) {
         this.previousActiveChunkSet.clear();
      }

      int var1 = 0;
      Iterator var2 = this.activeChunkSet.iterator();

      while(var2.hasNext()) {
         ChunkCoordIntPair var3 = (ChunkCoordIntPair)var2.next();
         if(!this.previousActiveChunkSet.contains(var3)) {
            int var4 = var3.chunkXPos * 16;
            int var5 = var3.chunkZPos * 16;
            this.theProfiler.startSection("getChunk");
            Chunk var6 = this.getChunkFromChunkCoords(var3.chunkXPos, var3.chunkZPos);
            this.playMoodSoundAndCheckLight(var4, var5, var6);
            this.theProfiler.endSection();
            this.previousActiveChunkSet.add(var3);
            ++var1;
            if(var1 >= 10) {
               return;
            }
         }
      }

   }

   public void doPreChunk(int p_73025_1_, int p_73025_2_, boolean p_73025_3_) {
      if(p_73025_3_) {
         this.clientChunkProvider.loadChunk(p_73025_1_, p_73025_2_);
      } else {
         this.clientChunkProvider.unloadChunk(p_73025_1_, p_73025_2_);
      }

      if(!p_73025_3_) {
         this.markBlockRangeForRenderUpdate(p_73025_1_ * 16, 0, p_73025_2_ * 16, p_73025_1_ * 16 + 15, 256, p_73025_2_ * 16 + 15);
      }

   }

   public boolean spawnEntityInWorld(Entity p_72838_1_) {
      boolean var2 = super.spawnEntityInWorld(p_72838_1_);
      this.entityList.add(p_72838_1_);
      if(!var2) {
         this.entitySpawnQueue.add(p_72838_1_);
      } else if(p_72838_1_ instanceof EntityMinecart) {
         this.mc.getSoundHandler().playSound(new MovingSoundMinecart((EntityMinecart)p_72838_1_));
      }

      return var2;
   }

   public void removeEntity(Entity p_72900_1_) {
      super.removeEntity(p_72900_1_);
      this.entityList.remove(p_72900_1_);
   }

   public void onEntityAdded(Entity p_72923_1_) {
      super.onEntityAdded(p_72923_1_);
      if(this.entitySpawnQueue.contains(p_72923_1_)) {
         this.entitySpawnQueue.remove(p_72923_1_);
      }

   }

   public void onEntityRemoved(Entity p_72847_1_) {
      super.onEntityRemoved(p_72847_1_);
      boolean var2 = false;
      if(this.entityList.contains(p_72847_1_)) {
         if(p_72847_1_.isEntityAlive()) {
            this.entitySpawnQueue.add(p_72847_1_);
            var2 = true;
         } else {
            this.entityList.remove(p_72847_1_);
         }
      }

   }

   public void addEntityToWorld(int p_73027_1_, Entity p_73027_2_) {
      Entity var3 = this.getEntityByID(p_73027_1_);
      if(var3 != null) {
         this.removeEntity(var3);
      }

      this.entityList.add(p_73027_2_);
      p_73027_2_.setEntityId(p_73027_1_);
      if(!this.spawnEntityInWorld(p_73027_2_)) {
         this.entitySpawnQueue.add(p_73027_2_);
      }

      this.entitiesById.addKey(p_73027_1_, p_73027_2_);
   }

   public Entity getEntityByID(int p_73045_1_) {
      return (Entity)(p_73045_1_ == this.mc.thePlayer.getEntityId()?this.mc.thePlayer:super.getEntityByID(p_73045_1_));
   }

   public Entity removeEntityFromWorld(int p_73028_1_) {
      Entity var2 = (Entity)this.entitiesById.removeObject(p_73028_1_);
      if(var2 != null) {
         this.entityList.remove(var2);
         this.removeEntity(var2);
      }

      return var2;
   }

   public boolean invalidateRegionAndSetBlock(BlockPos p_180503_1_, IBlockState p_180503_2_) {
      int var3 = p_180503_1_.getX();
      int var4 = p_180503_1_.getY();
      int var5 = p_180503_1_.getZ();
      this.invalidateBlockReceiveRegion(var3, var4, var5, var3, var4, var5);
      return super.setBlockState(p_180503_1_, p_180503_2_, 3);
   }

   public void sendQuittingDisconnectingPacket() {
      this.sendQueue.getNetworkManager().closeChannel(new ChatComponentText("Quitting"));
   }

   public void updateWeather() {}

   public int getRenderDistanceChunks() {
      return this.mc.gameSettings.renderDistanceChunks;
   }

   public void doVoidFogParticles(int p_73029_1_, int p_73029_2_, int p_73029_3_) {
      byte var4 = 16;
      Random var5 = new Random();
      ItemStack var6 = this.mc.thePlayer.getHeldItem();
      boolean var7 = this.mc.playerController.getCurrentGameType() == GameType.CREATIVE && var6 != null && Block.getBlockFromItem(var6.getItem()) == Blocks.barrier;
      BlockPosM var8 = this.randomTickPosM;

      for(int var9 = 0; var9 < 1000; ++var9) {
         int var10 = p_73029_1_ + this.rand.nextInt(var4) - this.rand.nextInt(var4);
         int var11 = p_73029_2_ + this.rand.nextInt(var4) - this.rand.nextInt(var4);
         int var12 = p_73029_3_ + this.rand.nextInt(var4) - this.rand.nextInt(var4);
         var8.setXyz(var10, var11, var12);
         IBlockState var13 = this.getBlockState(var8);
         var13.getBlock().randomDisplayTick(this, var8, var13, var5);
         if(var7 && var13.getBlock() == Blocks.barrier) {
            this.spawnParticle(EnumParticleTypes.BARRIER, (double)((float)var10 + 0.5F), (double)((float)var11 + 0.5F), (double)((float)var12 + 0.5F), 0.0D, 0.0D, 0.0D, new int[0]);
         }
      }

   }

   public void removeAllEntities() {
      this.loadedEntityList.removeAll(this.unloadedEntityList);

      int var1;
      Entity var2;
      int var3;
      int var4;
      for(var1 = 0; var1 < this.unloadedEntityList.size(); ++var1) {
         var2 = (Entity)this.unloadedEntityList.get(var1);
         var3 = var2.chunkCoordX;
         var4 = var2.chunkCoordZ;
         if(var2.addedToChunk && this.isChunkLoaded(var3, var4, true)) {
            this.getChunkFromChunkCoords(var3, var4).removeEntity(var2);
         }
      }

      for(var1 = 0; var1 < this.unloadedEntityList.size(); ++var1) {
         this.onEntityRemoved((Entity)this.unloadedEntityList.get(var1));
      }

      this.unloadedEntityList.clear();

      for(var1 = 0; var1 < this.loadedEntityList.size(); ++var1) {
         var2 = (Entity)this.loadedEntityList.get(var1);
         if(var2.ridingEntity != null) {
            if(!var2.ridingEntity.isDead && var2.ridingEntity.riddenByEntity == var2) {
               continue;
            }

            var2.ridingEntity.riddenByEntity = null;
            var2.ridingEntity = null;
         }

         if(var2.isDead) {
            var3 = var2.chunkCoordX;
            var4 = var2.chunkCoordZ;
            if(var2.addedToChunk && this.isChunkLoaded(var3, var4, true)) {
               this.getChunkFromChunkCoords(var3, var4).removeEntity(var2);
            }

            this.loadedEntityList.remove(var1--);
            this.onEntityRemoved(var2);
         }
      }

   }

   public CrashReportCategory addWorldInfoToCrashReport(CrashReport report) {
      CrashReportCategory var2 = super.addWorldInfoToCrashReport(report);
      var2.addCrashSectionCallable("Forced entities", new Callable() {

         public static final String __OBFID = "CL_00000883";

         public String call() {
            return WorldClient.this.entityList.size() + " total; " + WorldClient.this.entityList.toString();
         }
         // $FF: synthetic method
         // $FF: bridge method
         public Object call() throws Exception {
            return this.call();
         }
      });
      var2.addCrashSectionCallable("Retry entities", new Callable() {

         public static final String __OBFID = "CL_00000884";

         public String call() {
            return WorldClient.this.entitySpawnQueue.size() + " total; " + WorldClient.this.entitySpawnQueue.toString();
         }
         // $FF: synthetic method
         // $FF: bridge method
         public Object call() throws Exception {
            return this.call();
         }
      });
      var2.addCrashSectionCallable("Server brand", new Callable() {

         public static final String __OBFID = "CL_00000885";

         public String call() throws Exception {
            return WorldClient.this.mc.thePlayer.getClientBrand();
         }
         // $FF: synthetic method
         // $FF: bridge method
         public Object call() throws Exception {
            return this.call();
         }
      });
      var2.addCrashSectionCallable("Server type", new Callable() {

         public static final String __OBFID = "CL_00000886";

         public String call() throws Exception {
            return WorldClient.this.mc.getIntegratedServer() == null?"Non-integrated multiplayer server":"Integrated singleplayer server";
         }
         // $FF: synthetic method
         // $FF: bridge method
         public Object call() throws Exception {
            return this.call();
         }
      });
      return var2;
   }

   public void playSoundAtPos(BlockPos p_175731_1_, String p_175731_2_, float p_175731_3_, float p_175731_4_, boolean p_175731_5_) {
      this.playSound((double)p_175731_1_.getX() + 0.5D, (double)p_175731_1_.getY() + 0.5D, (double)p_175731_1_.getZ() + 0.5D, p_175731_2_, p_175731_3_, p_175731_4_, p_175731_5_);
   }

   public void playSound(double x, double y, double z, String soundName, float volume, float pitch, boolean distanceDelay) {
      double var11 = this.mc.getRenderViewEntity().getDistanceSq(x, y, z);
      PositionedSoundRecord var13 = new PositionedSoundRecord(new ResourceLocation(soundName), volume, pitch, (float)x, (float)y, (float)z);
      if(distanceDelay && var11 > 100.0D) {
         double var14 = Math.sqrt(var11) / 40.0D;
         this.mc.getSoundHandler().playDelayedSound(var13, (int)(var14 * 20.0D));
      } else {
         this.mc.getSoundHandler().playSound(var13);
      }

   }

   public void makeFireworks(double x, double y, double z, double motionX, double motionY, double motionZ, NBTTagCompound compund) {
      this.mc.effectRenderer.addEffect(new StarterFX(this, x, y, z, motionX, motionY, motionZ, this.mc.effectRenderer, compund));
   }

   public void setWorldScoreboard(Scoreboard p_96443_1_) {
      this.worldScoreboard = p_96443_1_;
   }

   public void setWorldTime(long time) {
      if(time < 0L) {
         time = -time;
         this.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
      } else {
         this.getGameRules().setOrCreateGameRule("doDaylightCycle", "true");
      }

      super.setWorldTime(time);
   }

   public int getCombinedLight(BlockPos pos, int lightValue) {
      int combinedLight = super.getCombinedLight(pos, lightValue);
      if(Config.isDynamicLights()) {
         combinedLight = DynamicLights.getCombinedLight(pos, combinedLight);
      }

      return combinedLight;
   }

   public boolean setBlockState(BlockPos pos, IBlockState newState, int flags) {
      this.playerUpdate = this.isPlayerActing();
      boolean res = super.setBlockState(pos, newState, flags);
      this.playerUpdate = false;
      return res;
   }

   public boolean isPlayerActing() {
      if(this.mc.playerController instanceof PlayerControllerOF) {
         PlayerControllerOF pcof = (PlayerControllerOF)this.mc.playerController;
         return pcof.isActing();
      } else {
         return false;
      }
   }

   public boolean isPlayerUpdate() {
      return this.playerUpdate;
   }
}
