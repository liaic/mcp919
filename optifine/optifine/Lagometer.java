package optifine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.profiler.Profiler;
import optifine.Config;
import org.lwjgl.opengl.GL11;

public class Lagometer {

   public static Minecraft mc;
   public static GameSettings gameSettings;
   public static Profiler profiler;
   public static boolean active = false;
   public static Lagometer.TimerNano timerTick = new Lagometer.TimerNano();
   public static Lagometer.TimerNano timerScheduledExecutables = new Lagometer.TimerNano();
   public static Lagometer.TimerNano timerChunkUpload = new Lagometer.TimerNano();
   public static Lagometer.TimerNano timerChunkUpdate = new Lagometer.TimerNano();
   public static Lagometer.TimerNano timerVisibility = new Lagometer.TimerNano();
   public static Lagometer.TimerNano timerTerrain = new Lagometer.TimerNano();
   public static Lagometer.TimerNano timerServer = new Lagometer.TimerNano();
   public static long[] timesFrame = new long[512];
   public static long[] timesTick = new long[512];
   public static long[] timesScheduledExecutables = new long[512];
   public static long[] timesChunkUpload = new long[512];
   public static long[] timesChunkUpdate = new long[512];
   public static long[] timesVisibility = new long[512];
   public static long[] timesTerrain = new long[512];
   public static long[] timesServer = new long[512];
   public static boolean[] gcs = new boolean[512];
   public static int numRecordedFrameTimes = 0;
   public static long prevFrameTimeNano = -1L;
   public static long renderTimeNano = 0L;
   public static long memTimeStartMs = System.currentTimeMillis();
   public static long memStart = getMemoryUsed();
   public static long memTimeLast = memTimeStartMs;
   public static long memLast = memStart;
   public static long memTimeDiffMs = 1L;
   public static long memDiff = 0L;
   public static int memMbSec = 0;


   public static boolean updateMemoryAllocation() {
      long timeNowMs = System.currentTimeMillis();
      long memNow = getMemoryUsed();
      boolean gc = false;
      if(memNow < memLast) {
         double memDiffMb = (double)memDiff / 1000000.0D;
         double timeDiffSec = (double)memTimeDiffMs / 1000.0D;
         int mbSec = (int)(memDiffMb / timeDiffSec);
         if(mbSec > 0) {
            memMbSec = mbSec;
         }

         memTimeStartMs = timeNowMs;
         memStart = memNow;
         memTimeDiffMs = 0L;
         memDiff = 0L;
         gc = true;
      } else {
         memTimeDiffMs = timeNowMs - memTimeStartMs;
         memDiff = memNow - memStart;
      }

      memTimeLast = timeNowMs;
      memLast = memNow;
      return gc;
   }

   public static long getMemoryUsed() {
      Runtime r = Runtime.getRuntime();
      return r.totalMemory() - r.freeMemory();
   }

   public static void updateLagometer() {
      if(mc == null) {
         mc = Minecraft.getMinecraft();
         gameSettings = mc.gameSettings;
         profiler = mc.mcProfiler;
      }

      if(gameSettings.showDebugInfo && (gameSettings.ofLagometer || gameSettings.showLagometer)) {
         active = true;
         long timeNowNano = System.nanoTime();
         if(prevFrameTimeNano == -1L) {
            prevFrameTimeNano = timeNowNano;
         } else {
            int frameIndex = numRecordedFrameTimes & timesFrame.length - 1;
            ++numRecordedFrameTimes;
            boolean gc = updateMemoryAllocation();
            timesFrame[frameIndex] = timeNowNano - prevFrameTimeNano - renderTimeNano;
            timesTick[frameIndex] = timerTick.timeNano;
            timesScheduledExecutables[frameIndex] = timerScheduledExecutables.timeNano;
            timesChunkUpload[frameIndex] = timerChunkUpload.timeNano;
            timesChunkUpdate[frameIndex] = timerChunkUpdate.timeNano;
            timesVisibility[frameIndex] = timerVisibility.timeNano;
            timesTerrain[frameIndex] = timerTerrain.timeNano;
            timesServer[frameIndex] = timerServer.timeNano;
            gcs[frameIndex] = gc;
            timerTick.reset();
            timerScheduledExecutables.reset();
            timerVisibility.reset();
            timerChunkUpdate.reset();
            timerChunkUpload.reset();
            timerTerrain.reset();
            timerServer.reset();
            prevFrameTimeNano = System.nanoTime();
         }
      } else {
         active = false;
         prevFrameTimeNano = -1L;
      }
   }

   public static void showLagometer(ScaledResolution scaledResolution) {
      if(gameSettings != null) {
         if(gameSettings.ofLagometer || gameSettings.showLagometer) {
            long timeRenderStartNano = System.nanoTime();
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.pushMatrix();
            GlStateManager.enableColorMaterial();
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, (double)mc.displayWidth, (double)mc.displayHeight, 0.0D, 1000.0D, 3000.0D);
            GlStateManager.matrixMode(5888);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -2000.0F);
            GL11.glLineWidth(1.0F);
            GlStateManager.disableTexture2D();
            Tessellator tess = Tessellator.getInstance();
            WorldRenderer tessellator = tess.getWorldRenderer();
            tessellator.begin(1, DefaultVertexFormats.POSITION_COLOR);

            int y60;
            int y30;
            float lumMem;
            for(y60 = 0; y60 < timesFrame.length; ++y60) {
               y30 = (y60 - numRecordedFrameTimes & timesFrame.length - 1) * 100 / timesFrame.length;
               y30 += 155;
               lumMem = (float)mc.displayHeight;
               long heightFrame = 0L;
               if(gcs[y60]) {
                  renderTime(y60, timesFrame[y60], y30, y30 / 2, 0, lumMem, tessellator);
               } else {
                  renderTime(y60, timesFrame[y60], y30, y30, y30, lumMem, tessellator);
                  lumMem -= (float)renderTime(y60, timesServer[y60], y30 / 2, y30 / 2, y30 / 2, lumMem, tessellator);
                  lumMem -= (float)renderTime(y60, timesTerrain[y60], 0, y30, 0, lumMem, tessellator);
                  lumMem -= (float)renderTime(y60, timesVisibility[y60], y30, y30, 0, lumMem, tessellator);
                  lumMem -= (float)renderTime(y60, timesChunkUpdate[y60], y30, 0, 0, lumMem, tessellator);
                  lumMem -= (float)renderTime(y60, timesChunkUpload[y60], y30, 0, y30, lumMem, tessellator);
                  lumMem -= (float)renderTime(y60, timesScheduledExecutables[y60], 0, 0, y30, lumMem, tessellator);
                  float var10000 = lumMem - (float)renderTime(y60, timesTick[y60], 0, y30, y30, lumMem, tessellator);
               }
            }

            renderTimeDivider(0, timesFrame.length, 33333333L, 196, 196, 196, (float)mc.displayHeight, tessellator);
            renderTimeDivider(0, timesFrame.length, 16666666L, 196, 196, 196, (float)mc.displayHeight, tessellator);
            tess.draw();
            GlStateManager.enableTexture2D();
            y60 = mc.displayHeight - 80;
            y30 = mc.displayHeight - 160;
            mc.fontRendererObj.drawString("30", 2, y30 + 1, -8947849);
            mc.fontRendererObj.drawString("30", 1, y30, -3881788);
            mc.fontRendererObj.drawString("60", 2, y60 + 1, -8947849);
            mc.fontRendererObj.drawString("60", 1, y60, -3881788);
            GlStateManager.matrixMode(5889);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            GlStateManager.popMatrix();
            GlStateManager.enableTexture2D();
            lumMem = 1.0F - (float)((double)(System.currentTimeMillis() - memTimeStartMs) / 1000.0D);
            lumMem = Config.limit(lumMem, 0.0F, 1.0F);
            int memColR = (int)(170.0F + lumMem * 85.0F);
            int memColG = (int)(100.0F + lumMem * 55.0F);
            int memColB = (int)(10.0F + lumMem * 10.0F);
            int colMem = memColR << 16 | memColG << 8 | memColB;
            int posX = 512 / scaledResolution.getScaleFactor() + 2;
            int posY = mc.displayHeight / scaledResolution.getScaleFactor() - 8;
            GuiIngame var16 = mc.ingameGUI;
            GuiIngame.drawRect(posX - 1, posY - 1, posX + 50, posY + 10, -1605349296);
            mc.fontRendererObj.drawString(" " + memMbSec + " MB/s", posX, posY, colMem);
            renderTimeNano = System.nanoTime() - timeRenderStartNano;
         }
      }
   }

   public static long renderTime(int frameNum, long time, int r, int g, int b, float baseHeight, WorldRenderer tessellator) {
      long heightTime = time / 200000L;
      if(heightTime < 3L) {
         return 0L;
      } else {
         tessellator.pos((double)((float)frameNum + 0.5F), (double)(baseHeight - (float)heightTime + 0.5F), 0.0D).color(r, g, b, 255).endVertex();
         tessellator.pos((double)((float)frameNum + 0.5F), (double)(baseHeight + 0.5F), 0.0D).color(r, g, b, 255).endVertex();
         return heightTime;
      }
   }

   public static long renderTimeDivider(int frameStart, int frameEnd, long time, int r, int g, int b, float baseHeight, WorldRenderer tessellator) {
      long heightTime = time / 200000L;
      if(heightTime < 3L) {
         return 0L;
      } else {
         tessellator.pos((double)((float)frameStart + 0.5F), (double)(baseHeight - (float)heightTime + 0.5F), 0.0D).color(r, g, b, 255).endVertex();
         tessellator.pos((double)((float)frameEnd + 0.5F), (double)(baseHeight - (float)heightTime + 0.5F), 0.0D).color(r, g, b, 255).endVertex();
         return heightTime;
      }
   }

   public static boolean isActive() {
      return active;
   }


   public static class TimerNano {

      public long timeStartNano = 0L;
      public long timeNano = 0L;


      public void start() {
         if(Lagometer.active) {
            if(this.timeStartNano == 0L) {
               this.timeStartNano = System.nanoTime();
            }

         }
      }

      public void end() {
         if(Lagometer.active) {
            if(this.timeStartNano != 0L) {
               this.timeNano += System.nanoTime() - this.timeStartNano;
               this.timeStartNano = 0L;
            }

         }
      }

      public void reset() {
         this.timeNano = 0L;
         this.timeStartNano = 0L;
      }
   }
}
