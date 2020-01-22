package net.minecraft.client.renderer;

import java.util.Iterator;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.EnumWorldBlockLayer;
import optifine.Config;
import org.lwjgl.opengl.GL11;
import shadersmod.client.ShadersRender;

public class VboRenderList extends ChunkRenderContainer {

   public static final String __OBFID = "CL_00002533";


   public void renderChunkLayer(EnumWorldBlockLayer p_178001_1_) {
      if(this.initialized) {
         Iterator var2 = this.renderChunks.iterator();

         while(var2.hasNext()) {
            RenderChunk var3 = (RenderChunk)var2.next();
            VertexBuffer var4 = var3.getVertexBufferByLayer(p_178001_1_.ordinal());
            GlStateManager.pushMatrix();
            this.preRenderChunk(var3);
            var3.multModelviewMatrix();
            var4.bindBuffer();
            this.setupArrayPointers();
            var4.drawArrays(7);
            GlStateManager.popMatrix();
         }

         OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
         GlStateManager.resetColor();
         this.renderChunks.clear();
      }

   }

   public void setupArrayPointers() {
      if(Config.isShaders()) {
         ShadersRender.setupArrayPointersVbo();
      } else {
         GL11.glVertexPointer(3, 5126, 28, 0L);
         GL11.glColorPointer(4, 5121, 28, 12L);
         GL11.glTexCoordPointer(2, 5126, 28, 16L);
         OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
         GL11.glTexCoordPointer(2, 5122, 28, 24L);
         OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
      }
   }
}
