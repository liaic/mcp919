package optifine;

import java.util.Map;
import optifine.HttpUtils;
import optifine.IFileUploadListener;

public class FileUploadThread extends Thread {

   public String urlString;
   public Map headers;
   public byte[] content;
   public IFileUploadListener listener;


   public FileUploadThread(String urlString, Map headers, byte[] content, IFileUploadListener listener) {
      this.urlString = urlString;
      this.headers = headers;
      this.content = content;
      this.listener = listener;
   }

   public void run() {
      try {
         HttpUtils.post(this.urlString, this.headers, this.content);
         this.listener.fileUploadFinished(this.urlString, this.content, (Throwable)null);
      } catch (Exception var2) {
         this.listener.fileUploadFinished(this.urlString, this.content, var2);
      }

   }

   public String getUrlString() {
      return this.urlString;
   }

   public byte[] getContent() {
      return this.content;
   }

   public IFileUploadListener getListener() {
      return this.listener;
   }
}
