package net.minecraft.client.renderer;

import net.minecraft.client.renderer.ChunkRenderContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.chunk.ListedRenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.src.Config;
import net.minecraft.util.EnumWorldBlockLayer;
import org.lwjgl.opengl.GL11;

public class RenderList extends ChunkRenderContainer {
   private static final String __OBFID = "CL_00000957";

   public void func_178001_a(EnumWorldBlockLayer p_178001_1_) {
      if(this.field_178007_b) {
         if(this.field_178009_a.size() == 0) {
            return;
         }

         for(RenderChunk renderchunk : this.field_178009_a) {
            ListedRenderChunk listedrenderchunk = (ListedRenderChunk)renderchunk;
            GlStateManager.func_179094_E();
            this.func_178003_a(renderchunk);
            GL11.glCallList(listedrenderchunk.func_178600_a(p_178001_1_, listedrenderchunk.func_178571_g()));
            GlStateManager.func_179121_F();
         }

         if(Config.isMultiTexture()) {
            GlStateManager.bindCurrentTexture();
         }

         GlStateManager.func_179117_G();
         this.field_178009_a.clear();
      }

   }
}
