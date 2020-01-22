package net.minecraft.client.renderer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.Proxy.Type;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.src.CapeImageBuffer;
import net.minecraft.src.Config;
import net.minecraft.src.HttpPipeline;
import net.minecraft.src.HttpRequest;
import net.minecraft.src.HttpResponse;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadDownloadImageData extends SimpleTexture {
   private static final Logger field_147644_c = LogManager.getLogger();
   private static final AtomicInteger field_147643_d = new AtomicInteger(0);
   private final File field_152434_e;
   private final String field_110562_b;
   private final IImageBuffer field_110563_c;
   private BufferedImage field_110560_d;
   private Thread field_110561_e;
   private boolean field_110559_g;
   private static final String __OBFID = "CL_00001049";
   public Boolean imageFound = null;
   public boolean pipeline = false;

   public ThreadDownloadImageData(File p_i1049_1_, String p_i1049_2_, ResourceLocation p_i1049_3_, IImageBuffer p_i1049_4_) {
      super(p_i1049_3_);
      this.field_152434_e = p_i1049_1_;
      this.field_110562_b = p_i1049_2_;
      this.field_110563_c = p_i1049_4_;
   }

   private void func_147640_e() {
      if(!this.field_110559_g && this.field_110560_d != null) {
         this.field_110559_g = true;
         if(this.field_110568_b != null) {
            this.func_147631_c();
         }

         TextureUtil.func_110987_a(super.func_110552_b(), this.field_110560_d);
      }

   }

   public int func_110552_b() {
      this.func_147640_e();
      return super.func_110552_b();
   }

   public void func_147641_a(BufferedImage p_147641_1_) {
      this.field_110560_d = p_147641_1_;
      if(this.field_110563_c != null) {
         this.field_110563_c.func_152634_a();
      }

      this.imageFound = Boolean.valueOf(this.field_110560_d != null);
   }

   public void func_110551_a(IResourceManager p_110551_1_) throws IOException {
      if(this.field_110560_d == null && this.field_110568_b != null) {
         super.func_110551_a(p_110551_1_);
      }

      if(this.field_110561_e == null) {
         if(this.field_152434_e != null && this.field_152434_e.isFile()) {
            field_147644_c.debug("Loading http texture from local cache ({})", new Object[]{this.field_152434_e});

            try {
               this.field_110560_d = ImageIO.read(this.field_152434_e);
               if(this.field_110563_c != null) {
                  this.func_147641_a(this.field_110563_c.func_78432_a(this.field_110560_d));
               }

               this.loadingFinished();
            } catch (IOException ioexception) {
               field_147644_c.error((String)("Couldn\'t load skin " + this.field_152434_e), (Throwable)ioexception);
               this.func_152433_a();
            }
         } else {
            this.func_152433_a();
         }
      }

   }

   protected void func_152433_a() {
      this.field_110561_e = new Thread("Texture Downloader #" + field_147643_d.incrementAndGet()) {
         private static final String __OBFID = "CL_00001050";

         public void run() {
            HttpURLConnection httpurlconnection = null;
            ThreadDownloadImageData.field_147644_c.debug("Downloading http texture from {} to {}", new Object[]{ThreadDownloadImageData.this.field_110562_b, ThreadDownloadImageData.this.field_152434_e});
            if(ThreadDownloadImageData.this.shouldPipeline()) {
               ThreadDownloadImageData.this.loadPipelined();
            } else {
               try {
                  httpurlconnection = (HttpURLConnection)(new URL(ThreadDownloadImageData.this.field_110562_b)).openConnection(Minecraft.func_71410_x().func_110437_J());
                  httpurlconnection.setDoInput(true);
                  httpurlconnection.setDoOutput(false);
                  httpurlconnection.connect();
                  if(httpurlconnection.getResponseCode() / 100 != 2) {
                     if(httpurlconnection.getErrorStream() != null) {
                        Config.readAll(httpurlconnection.getErrorStream());
                     }

                     return;
                  }

                  BufferedImage bufferedimage;
                  if(ThreadDownloadImageData.this.field_152434_e != null) {
                     FileUtils.copyInputStreamToFile(httpurlconnection.getInputStream(), ThreadDownloadImageData.this.field_152434_e);
                     bufferedimage = ImageIO.read(ThreadDownloadImageData.this.field_152434_e);
                  } else {
                     bufferedimage = TextureUtil.func_177053_a(httpurlconnection.getInputStream());
                  }

                  if(ThreadDownloadImageData.this.field_110563_c != null) {
                     bufferedimage = ThreadDownloadImageData.this.field_110563_c.func_78432_a(bufferedimage);
                  }

                  ThreadDownloadImageData.this.func_147641_a(bufferedimage);
               } catch (Exception exception) {
                  ThreadDownloadImageData.field_147644_c.error("Couldn\'t download http texture: " + exception.getClass().getName() + ": " + exception.getMessage());
                  return;
               } finally {
                  if(httpurlconnection != null) {
                     httpurlconnection.disconnect();
                  }

                  ThreadDownloadImageData.this.loadingFinished();
               }

            }
         }
      };
      this.field_110561_e.setDaemon(true);
      this.field_110561_e.start();
   }

   private boolean shouldPipeline() {
      if(!this.pipeline) {
         return false;
      } else {
         Proxy proxy = Minecraft.func_71410_x().func_110437_J();
         return proxy.type() != Type.DIRECT && proxy.type() != Type.SOCKS?false:this.field_110562_b.startsWith("http://");
      }
   }

   private void loadPipelined() {
      try {
         HttpRequest httprequest = HttpPipeline.makeRequest(this.field_110562_b, Minecraft.func_71410_x().func_110437_J());
         HttpResponse httpresponse = HttpPipeline.executeRequest(httprequest);
         if(httpresponse.getStatus() / 100 != 2) {
            return;
         }

         byte[] abyte = httpresponse.getBody();
         ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte);
         BufferedImage bufferedimage;
         if(this.field_152434_e != null) {
            FileUtils.copyInputStreamToFile(bytearrayinputstream, this.field_152434_e);
            bufferedimage = ImageIO.read(this.field_152434_e);
         } else {
            bufferedimage = TextureUtil.func_177053_a(bytearrayinputstream);
         }

         if(this.field_110563_c != null) {
            bufferedimage = this.field_110563_c.func_78432_a(bufferedimage);
         }

         this.func_147641_a(bufferedimage);
      } catch (Exception exception) {
         field_147644_c.error("Couldn\'t download http texture: " + exception.getClass().getName() + ": " + exception.getMessage());
         return;
      } finally {
         this.loadingFinished();
      }

   }

   private void loadingFinished() {
      this.imageFound = Boolean.valueOf(this.field_110560_d != null);
      if(this.field_110563_c instanceof CapeImageBuffer) {
         CapeImageBuffer capeimagebuffer = (CapeImageBuffer)this.field_110563_c;
         capeimagebuffer.cleanup();
      }

   }
}
