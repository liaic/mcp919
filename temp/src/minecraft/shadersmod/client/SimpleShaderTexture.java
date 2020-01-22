package shadersmod.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import org.apache.commons.io.IOUtils;
import shadersmod.client.Shaders;
import shadersmod.common.SMCLog;

public class SimpleShaderTexture extends AbstractTexture {
   private String texturePath;
   private static final IMetadataSerializer METADATA_SERIALIZER = makeMetadataSerializer();

   public SimpleShaderTexture(String texturePath) {
      this.texturePath = texturePath;
   }

   public void func_110551_a(IResourceManager resourceManager) throws IOException {
      this.func_147631_c();
      InputStream inputstream = Shaders.getShaderPackResourceStream(this.texturePath);
      if(inputstream == null) {
         throw new FileNotFoundException("Shader texture not found: " + this.texturePath);
      } else {
         try {
            BufferedImage bufferedimage = TextureUtil.func_177053_a(inputstream);
            TextureMetadataSection texturemetadatasection = this.loadTextureMetadataSection();
            TextureUtil.func_110989_a(this.func_110552_b(), bufferedimage, texturemetadatasection.func_110479_a(), texturemetadatasection.func_110480_b());
         } finally {
            IOUtils.closeQuietly(inputstream);
         }

      }
   }

   private TextureMetadataSection loadTextureMetadataSection() {
      String s = this.texturePath + ".mcmeta";
      String s1 = "texture";
      InputStream inputstream = Shaders.getShaderPackResourceStream(s);
      if(inputstream != null) {
         IMetadataSerializer imetadataserializer = METADATA_SERIALIZER;
         BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));

         TextureMetadataSection texturemetadatasection1;
         try {
            JsonObject jsonobject = (new JsonParser()).parse((Reader)bufferedreader).getAsJsonObject();
            TextureMetadataSection texturemetadatasection = (TextureMetadataSection)imetadataserializer.func_110503_a(s1, jsonobject);
            if(texturemetadatasection == null) {
               return new TextureMetadataSection(false, false, new ArrayList());
            }

            texturemetadatasection1 = texturemetadatasection;
         } catch (RuntimeException runtimeexception) {
            SMCLog.warning("Error reading metadata: " + s);
            SMCLog.warning("" + runtimeexception.getClass().getName() + ": " + runtimeexception.getMessage());
            return new TextureMetadataSection(false, false, new ArrayList());
         } finally {
            IOUtils.closeQuietly((Reader)bufferedreader);
            IOUtils.closeQuietly(inputstream);
         }

         return texturemetadatasection1;
      } else {
         return new TextureMetadataSection(false, false, new ArrayList());
      }
   }

   private static IMetadataSerializer makeMetadataSerializer() {
      IMetadataSerializer imetadataserializer = new IMetadataSerializer();
      imetadataserializer.func_110504_a(new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
      imetadataserializer.func_110504_a(new FontMetadataSectionSerializer(), FontMetadataSection.class);
      imetadataserializer.func_110504_a(new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
      imetadataserializer.func_110504_a(new PackMetadataSectionSerializer(), PackMetadataSection.class);
      imetadataserializer.func_110504_a(new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
      return imetadataserializer;
   }
}
