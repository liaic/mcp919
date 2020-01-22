package net.minecraft.src;

import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ClearWater;
import net.minecraft.src.Config;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class WorldServerOF extends WorldServer {
   private MinecraftServer mcServer;

   public WorldServerOF(MinecraftServer p_i116_1_, ISaveHandler p_i116_2_, WorldInfo p_i116_3_, int p_i116_4_, Profiler p_i116_5_) {
      super(p_i116_1_, p_i116_2_, p_i116_3_, p_i116_4_, p_i116_5_);
      this.mcServer = p_i116_1_;
   }

   public void func_72835_b() {
      super.func_72835_b();
      if(!Config.isTimeDefault()) {
         this.fixWorldTime();
      }

      if(Config.waterOpacityChanged) {
         Config.waterOpacityChanged = false;
         ClearWater.updateWaterOpacity(Config.getGameSettings(), this);
      }

   }

   protected void func_72979_l() {
      if(!Config.isWeatherEnabled()) {
         this.fixWorldWeather();
      }

      super.func_72979_l();
   }

   private void fixWorldWeather() {
      if(this.field_72986_A.func_76059_o() || this.field_72986_A.func_76061_m()) {
         this.field_72986_A.func_76080_g(0);
         this.field_72986_A.func_76084_b(false);
         this.func_72894_k(0.0F);
         this.field_72986_A.func_76090_f(0);
         this.field_72986_A.func_76069_a(false);
         this.func_147442_i(0.0F);
         this.mcServer.func_71203_ab().func_148540_a(new S2BPacketChangeGameState(2, 0.0F));
         this.mcServer.func_71203_ab().func_148540_a(new S2BPacketChangeGameState(7, 0.0F));
         this.mcServer.func_71203_ab().func_148540_a(new S2BPacketChangeGameState(8, 0.0F));
      }

   }

   private void fixWorldTime() {
      if(this.field_72986_A.func_76077_q().func_77148_a() == 1) {
         long i = this.func_72820_D();
         long j = i % 24000L;
         if(Config.isTimeDayOnly()) {
            if(j <= 1000L) {
               this.func_72877_b(i - j + 1001L);
            }

            if(j >= 11000L) {
               this.func_72877_b(i - j + 24001L);
            }
         }

         if(Config.isTimeNightOnly()) {
            if(j <= 14000L) {
               this.func_72877_b(i - j + 14001L);
            }

            if(j >= 22000L) {
               this.func_72877_b(i - j + 24000L + 14001L);
            }
         }

      }
   }
}
