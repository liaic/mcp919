package shadersmod.client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import optifine.StrUtils;
import shadersmod.client.IShaderPack;

public class ShaderPackZip implements IShaderPack {

   public File packFile;
   public ZipFile packZipFile;


   public ShaderPackZip(String name, File file) {
      this.packFile = file;
      this.packZipFile = null;
   }

   public void close() {
      if(this.packZipFile != null) {
         try {
            this.packZipFile.close();
         } catch (Exception var2) {
            ;
         }

         this.packZipFile = null;
      }
   }

   public InputStream getResourceAsStream(String resName) {
      try {
         if(this.packZipFile == null) {
            this.packZipFile = new ZipFile(this.packFile);
         }

         String excp = StrUtils.removePrefix(resName, "/");
         ZipEntry entry = this.packZipFile.getEntry(excp);
         return entry == null?null:this.packZipFile.getInputStream(entry);
      } catch (Exception var4) {
         return null;
      }
   }

   public boolean hasDirectory(String resName) {
      try {
         if(this.packZipFile == null) {
            this.packZipFile = new ZipFile(this.packFile);
         }

         String e = StrUtils.removePrefix(resName, "/");
         ZipEntry entry = this.packZipFile.getEntry(e);
         return entry != null;
      } catch (IOException var4) {
         return false;
      }
   }

   public String getName() {
      return this.packFile.getName();
   }
}
