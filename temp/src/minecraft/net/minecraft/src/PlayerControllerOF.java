package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class PlayerControllerOF extends PlayerControllerMP {
   private boolean acting = false;
   private BlockPos lastClickBlockPos = null;
   private Entity lastClickEntity = null;

   public PlayerControllerOF(Minecraft p_i81_1_, NetHandlerPlayClient p_i81_2_) {
      super(p_i81_1_, p_i81_2_);
   }

   public boolean func_180511_b(BlockPos p_180511_1_, EnumFacing p_180511_2_) {
      this.acting = true;
      this.lastClickBlockPos = p_180511_1_;
      boolean flag = super.func_180511_b(p_180511_1_, p_180511_2_);
      this.acting = false;
      return flag;
   }

   public boolean func_180512_c(BlockPos p_180512_1_, EnumFacing p_180512_2_) {
      this.acting = true;
      this.lastClickBlockPos = p_180512_1_;
      boolean flag = super.func_180512_c(p_180512_1_, p_180512_2_);
      this.acting = false;
      return flag;
   }

   public boolean func_78769_a(EntityPlayer p_78769_1_, World p_78769_2_, ItemStack p_78769_3_) {
      this.acting = true;
      boolean flag = super.func_78769_a(p_78769_1_, p_78769_2_, p_78769_3_);
      this.acting = false;
      return flag;
   }

   public boolean func_178890_a(EntityPlayerSP p_178890_1_, WorldClient p_178890_2_, ItemStack p_178890_3_, BlockPos p_178890_4_, EnumFacing p_178890_5_, Vec3 p_178890_6_) {
      this.acting = true;
      this.lastClickBlockPos = p_178890_4_;
      boolean flag = super.func_178890_a(p_178890_1_, p_178890_2_, p_178890_3_, p_178890_4_, p_178890_5_, p_178890_6_);
      this.acting = false;
      return flag;
   }

   public boolean func_78768_b(EntityPlayer p_78768_1_, Entity p_78768_2_) {
      this.lastClickEntity = p_78768_2_;
      return super.func_78768_b(p_78768_1_, p_78768_2_);
   }

   public boolean func_178894_a(EntityPlayer p_178894_1_, Entity p_178894_2_, MovingObjectPosition p_178894_3_) {
      this.lastClickEntity = p_178894_2_;
      return super.func_178894_a(p_178894_1_, p_178894_2_, p_178894_3_);
   }

   public boolean isActing() {
      return this.acting;
   }

   public BlockPos getLastClickBlockPos() {
      return this.lastClickBlockPos;
   }

   public Entity getLastClickEntity() {
      return this.lastClickEntity;
   }
}
