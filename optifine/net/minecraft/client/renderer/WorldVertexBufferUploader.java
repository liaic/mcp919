package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
import optifine.Config;
import optifine.Reflector;
import org.lwjgl.opengl.GL11;
import shadersmod.client.SVertexBuilder;

public class WorldVertexBufferUploader {

   public static final String __OBFID = "CL_00002567";


   public void draw(WorldRenderer p_178177_1_) {
      if(p_178177_1_.getVertexCount() > 0) {
         VertexFormat var2 = p_178177_1_.getVertexFormat();
         int var3 = var2.getNextOffset();
         ByteBuffer var4 = p_178177_1_.getByteBuffer();
         List var5 = var2.getElements();
         boolean forgePreDrawExists = Reflector.ForgeVertexFormatElementEnumUseage_preDraw.exists();
         boolean forgePostDrawExists = Reflector.ForgeVertexFormatElementEnumUseage_postDraw.exists();

         int var6;
         int var10;
         for(var6 = 0; var6 < var5.size(); ++var6) {
            VertexFormatElement wr = (VertexFormatElement)var5.get(var6);
            EnumUsage var11 = wr.getUsage();
            if(forgePreDrawExists) {
               Reflector.callVoid(var11, Reflector.ForgeVertexFormatElementEnumUseage_preDraw, new Object[]{var2, Integer.valueOf(var6), Integer.valueOf(var3), var4});
            } else {
               int var12 = wr.getType().getGlConstant();
               var10 = wr.getIndex();
               var4.position(var2.getOffset(var6));
               switch(WorldVertexBufferUploader.SwitchEnumUseage.field_178958_a[var11.ordinal()]) {
               case 1:
                  GL11.glVertexPointer(wr.getElementCount(), var12, var3, var4);
                  GL11.glEnableClientState('\u8074');
                  break;
               case 2:
                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + var10);
                  GL11.glTexCoordPointer(wr.getElementCount(), var12, var3, var4);
                  GL11.glEnableClientState('\u8078');
                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                  break;
               case 3:
                  GL11.glColorPointer(wr.getElementCount(), var12, var3, var4);
                  GL11.glEnableClientState('\u8076');
                  break;
               case 4:
                  GL11.glNormalPointer(var12, var3, var4);
                  GL11.glEnableClientState('\u8075');
               }
            }
         }

         if(p_178177_1_.isMultiTexture()) {
            p_178177_1_.drawMultiTexture();
         } else if(Config.isShaders()) {
            SVertexBuilder.drawArrays(p_178177_1_.getDrawMode(), 0, p_178177_1_.getVertexCount(), p_178177_1_);
         } else {
            GL11.glDrawArrays(p_178177_1_.getDrawMode(), 0, p_178177_1_.getVertexCount());
         }

         var6 = 0;

         for(int var14 = var5.size(); var6 < var14; ++var6) {
            VertexFormatElement var15 = (VertexFormatElement)var5.get(var6);
            EnumUsage var13 = var15.getUsage();
            if(forgePostDrawExists) {
               Reflector.callVoid(var13, Reflector.ForgeVertexFormatElementEnumUseage_postDraw, new Object[]{var2, Integer.valueOf(var6), Integer.valueOf(var3), var4});
            } else {
               var10 = var15.getIndex();
               switch(WorldVertexBufferUploader.SwitchEnumUseage.field_178958_a[var13.ordinal()]) {
               case 1:
                  GL11.glDisableClientState('\u8074');
                  break;
               case 2:
                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + var10);
                  GL11.glDisableClientState('\u8078');
                  OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                  break;
               case 3:
                  GL11.glDisableClientState('\u8076');
                  GlStateManager.resetColor();
                  break;
               case 4:
                  GL11.glDisableClientState('\u8075');
               }
            }
         }
      }

      p_178177_1_.reset();
   }

   public static final class SwitchEnumUseage {

      public static final int[] field_178958_a = new int[EnumUsage.values().length];
      public static final String __OBFID = "CL_00002566";


      static {
         try {
            field_178958_a[EnumUsage.POSITION.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            field_178958_a[EnumUsage.UV.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            field_178958_a[EnumUsage.COLOR.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            field_178958_a[EnumUsage.NORMAL.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
