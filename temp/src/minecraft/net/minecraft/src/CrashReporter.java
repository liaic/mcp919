package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.src.Config;
import net.minecraft.src.FileUploadThread;
import net.minecraft.src.IFileUploadListener;
import shadersmod.client.Shaders;

public class CrashReporter {
   public static void onCrashReport(CrashReport p_onCrashReport_0_, CrashReportCategory p_onCrashReport_1_) {
      try {
         GameSettings gamesettings = Config.getGameSettings();
         if(gamesettings == null) {
            return;
         }

         if(!gamesettings.field_74355_t) {
            return;
         }

         Throwable throwable = p_onCrashReport_0_.func_71505_b();
         if(throwable == null) {
            return;
         }

         if(throwable.getClass() == Throwable.class) {
            return;
         }

         if(throwable.getClass().getName().contains(".fml.client.SplashProgress")) {
            return;
         }

         extendCrashReport(p_onCrashReport_1_);
         String s = "http://optifine.net/crashReport";
         String s1 = makeReport(p_onCrashReport_0_);
         byte[] abyte = s1.getBytes("ASCII");
         IFileUploadListener ifileuploadlistener = new IFileUploadListener() {
            public void fileUploadFinished(String p_fileUploadFinished_1_, byte[] p_fileUploadFinished_2_, Throwable p_fileUploadFinished_3_) {
            }
         };
         Map map = new HashMap();
         map.put("OF-Version", Config.getVersion());
         map.put("OF-Summary", makeSummary(p_onCrashReport_0_));
         FileUploadThread fileuploadthread = new FileUploadThread(s, map, abyte, ifileuploadlistener);
         fileuploadthread.setPriority(10);
         fileuploadthread.start();
         Thread.sleep(1000L);
      } catch (Exception exception) {
         Config.dbg(exception.getClass().getName() + ": " + exception.getMessage());
      }

   }

   private static String makeReport(CrashReport p_makeReport_0_) {
      StringBuffer stringbuffer = new StringBuffer();
      stringbuffer.append("OptiFineVersion: " + Config.getVersion() + "\n");
      stringbuffer.append("Summary: " + makeSummary(p_makeReport_0_) + "\n");
      stringbuffer.append("\n");
      stringbuffer.append(p_makeReport_0_.func_71502_e());
      stringbuffer.append("\n");
      return stringbuffer.toString();
   }

   private static String makeSummary(CrashReport p_makeSummary_0_) {
      Throwable throwable = p_makeSummary_0_.func_71505_b();
      if(throwable == null) {
         return "Unknown";
      } else {
         StackTraceElement[] astacktraceelement = throwable.getStackTrace();
         String s = "unknown";
         if(astacktraceelement.length > 0) {
            s = astacktraceelement[0].toString().trim();
         }

         String s1 = throwable.getClass().getName() + ": " + throwable.getMessage() + " (" + p_makeSummary_0_.func_71501_a() + ")" + " [" + s + "]";
         return s1;
      }
   }

   public static void extendCrashReport(CrashReportCategory p_extendCrashReport_0_) {
      p_extendCrashReport_0_.func_71507_a("OptiFine Version", Config.getVersion());
      if(Config.getGameSettings() != null) {
         p_extendCrashReport_0_.func_71507_a("Render Distance Chunks", "" + Config.getChunkViewDistance());
         p_extendCrashReport_0_.func_71507_a("Mipmaps", "" + Config.getMipmapLevels());
         p_extendCrashReport_0_.func_71507_a("Anisotropic Filtering", "" + Config.getAnisotropicFilterLevel());
         p_extendCrashReport_0_.func_71507_a("Antialiasing", "" + Config.getAntialiasingLevel());
         p_extendCrashReport_0_.func_71507_a("Multitexture", "" + Config.isMultiTexture());
      }

      p_extendCrashReport_0_.func_71507_a("Shaders", "" + Shaders.getShaderPackName());
      p_extendCrashReport_0_.func_71507_a("OpenGlVersion", "" + Config.openGlVersion);
      p_extendCrashReport_0_.func_71507_a("OpenGlRenderer", "" + Config.openGlRenderer);
      p_extendCrashReport_0_.func_71507_a("OpenGlVendor", "" + Config.openGlVendor);
      p_extendCrashReport_0_.func_71507_a("CpuCount", "" + Config.getAvailableProcessors());
   }
}
