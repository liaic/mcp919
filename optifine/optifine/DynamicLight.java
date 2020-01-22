package optifine;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.BlockPos.MutableBlockPos;
import optifine.Config;
import optifine.DynamicLights;

public class DynamicLight {

   public Entity entity = null;
   public double offsetY = 0.0D;
   public double lastPosX = -2.147483648E9D;
   public double lastPosY = -2.147483648E9D;
   public double lastPosZ = -2.147483648E9D;
   public int lastLightLevel = 0;
   public boolean underwater = false;
   public long timeCheckMs = 0L;
   public Set<BlockPos> setLitChunkPos = new HashSet();
   public MutableBlockPos blockPosMutable = new MutableBlockPos();


   public DynamicLight(Entity entity) {
      this.entity = entity;
      this.offsetY = (double)entity.getEyeHeight();
   }

   public void update(RenderGlobal renderGlobal) {
      if(Config.isDynamicLightsFast()) {
         long posX = System.currentTimeMillis();
         if(posX < this.timeCheckMs + 500L) {
            return;
         }

         this.timeCheckMs = posX;
      }

      double posX1 = this.entity.posX - 0.5D;
      double posY = this.entity.posY - 0.5D + this.offsetY;
      double posZ = this.entity.posZ - 0.5D;
      int lightLevel = DynamicLights.getLightLevel(this.entity);
      double dx = posX1 - this.lastPosX;
      double dy = posY - this.lastPosY;
      double dz = posZ - this.lastPosZ;
      double delta = 0.1D;
      if(Math.abs(dx) > delta || Math.abs(dy) > delta || Math.abs(dz) > delta || this.lastLightLevel != lightLevel) {
         this.lastPosX = posX1;
         this.lastPosY = posY;
         this.lastPosZ = posZ;
         this.lastLightLevel = lightLevel;
         this.underwater = false;
         WorldClient world = renderGlobal.getWorld();
         if(world != null) {
            this.blockPosMutable.set(MathHelper.floor_double(posX1), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
            IBlockState setNewPos = world.getBlockState(this.blockPosMutable);
            Block dirX = setNewPos.getBlock();
            this.underwater = dirX == Blocks.water;
         }

         HashSet setNewPos1 = new HashSet();
         if(lightLevel > 0) {
            EnumFacing dirX1 = (MathHelper.floor_double(posX1) & 15) >= 8?EnumFacing.EAST:EnumFacing.WEST;
            EnumFacing dirY = (MathHelper.floor_double(posY) & 15) >= 8?EnumFacing.UP:EnumFacing.DOWN;
            EnumFacing dirZ = (MathHelper.floor_double(posZ) & 15) >= 8?EnumFacing.SOUTH:EnumFacing.NORTH;
            BlockPos pos = new BlockPos(posX1, posY, posZ);
            RenderChunk chunkView = renderGlobal.getRenderChunk(pos);
            RenderChunk chunkX = renderGlobal.getRenderChunk(chunkView, dirX1);
            RenderChunk chunkZ = renderGlobal.getRenderChunk(chunkView, dirZ);
            RenderChunk chunkXZ = renderGlobal.getRenderChunk(chunkX, dirZ);
            RenderChunk chunkY = renderGlobal.getRenderChunk(chunkView, dirY);
            RenderChunk chunkYX = renderGlobal.getRenderChunk(chunkY, dirX1);
            RenderChunk chunkYZ = renderGlobal.getRenderChunk(chunkY, dirZ);
            RenderChunk chunkYXZ = renderGlobal.getRenderChunk(chunkYX, dirZ);
            this.updateChunkLight(chunkView, this.setLitChunkPos, setNewPos1);
            this.updateChunkLight(chunkX, this.setLitChunkPos, setNewPos1);
            this.updateChunkLight(chunkZ, this.setLitChunkPos, setNewPos1);
            this.updateChunkLight(chunkXZ, this.setLitChunkPos, setNewPos1);
            this.updateChunkLight(chunkY, this.setLitChunkPos, setNewPos1);
            this.updateChunkLight(chunkYX, this.setLitChunkPos, setNewPos1);
            this.updateChunkLight(chunkYZ, this.setLitChunkPos, setNewPos1);
            this.updateChunkLight(chunkYXZ, this.setLitChunkPos, setNewPos1);
         }

         this.updateLitChunks(renderGlobal);
         this.setLitChunkPos = setNewPos1;
      }
   }

   public void updateChunkLight(RenderChunk renderChunk, Set<BlockPos> setPrevPos, Set<BlockPos> setNewPos) {
      if(renderChunk != null) {
         CompiledChunk compiledChunk = renderChunk.getCompiledChunk();
         if(compiledChunk != null && !compiledChunk.isEmpty()) {
            renderChunk.setNeedsUpdate(true);
         }

         BlockPos pos = renderChunk.getPosition();
         if(setPrevPos != null) {
            setPrevPos.remove(pos);
         }

         if(setNewPos != null) {
            setNewPos.add(pos);
         }

      }
   }

   public void updateLitChunks(RenderGlobal renderGlobal) {
      Iterator it = this.setLitChunkPos.iterator();

      while(it.hasNext()) {
         BlockPos posOld = (BlockPos)it.next();
         RenderChunk chunkOld = renderGlobal.getRenderChunk(posOld);
         this.updateChunkLight(chunkOld, (Set)null, (Set)null);
      }

   }

   public Entity getEntity() {
      return this.entity;
   }

   public double getLastPosX() {
      return this.lastPosX;
   }

   public double getLastPosY() {
      return this.lastPosY;
   }

   public double getLastPosZ() {
      return this.lastPosZ;
   }

   public int getLastLightLevel() {
      return this.lastLightLevel;
   }

   public boolean isUnderwater() {
      return this.underwater;
   }

   public double getOffsetY() {
      return this.offsetY;
   }

   public String toString() {
      return "Entity: " + this.entity + ", offsetY: " + this.offsetY;
   }
}
