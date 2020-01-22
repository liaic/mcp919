package net.minecraft.src;

import net.minecraft.client.Minecraft;
import net.minecraft.src.HttpPipeline;
import net.minecraft.src.IFileDownloadListener;

public class FileDownloadThread extends Thread {
   private String urlString = null;
   private IFileDownloadListener listener = null;

   public FileDownloadThread(String p_i47_1_, IFileDownloadListener p_i47_2_) {
      this.urlString = p_i47_1_;
      this.listener = p_i47_2_;
   }

   public void run() {
      try {
         byte[] abyte = HttpPipeline.get(this.urlString, Minecraft.func_71410_x().func_110437_J());
         this.listener.fileDownloadFinished(this.urlString, abyte, (Throwable)null);
      } catch (Exception exception) {
         this.listener.fileDownloadFinished(this.urlString, (byte[])null, exception);
      }

   }

   public String getUrlString() {
      return this.urlString;
   }

   public IFileDownloadListener getListener() {
      return this.listener;
   }
}
