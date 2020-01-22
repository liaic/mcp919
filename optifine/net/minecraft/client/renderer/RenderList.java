package net.minecraft.client.renderer;

import java.util.Iterator;
import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.EnumWorldBlockLayer;
import optifine.Config;
import org.lwjgl.opengl.GL11;

public class RenderList extends ChunkRenderContainer {

   public static final String __OBFID = "CL_00000957";


   public void renderChunkLayer(EnumWorldBlockLayer p_178001_1_) {
      if(this.initialized) {
         if(this.renderChunks.size() == 0) {
            return;
         }

         Iterator var2 = this.renderChunks.iterator();

         while(var2.hasNext()) {
            RenderChunk var3 = (RenderChunk)var2.next();
            ListedRenderChunk var4 = (ListedRenderChunk)var3;
            GlStateManager.pushMatrix();
            this.preRenderChunk(var3);
            GL11.glCallList(var4.getDisplayList(p_178001_1_, var4.getCompiledChunk()));
            GlStateManager.popMatrix();
         }

         if(Config.isMultiTexture()) {
            GlStateManager.bindCurrentTexture();
         }

         GlStateManager.resetColor();
         this.renderChunks.clear();
      }

   }
}
