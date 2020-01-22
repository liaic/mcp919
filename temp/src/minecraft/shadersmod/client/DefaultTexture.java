package shadersmod.client;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.resources.IResourceManager;
import shadersmod.client.ShadersTex;

public class DefaultTexture extends AbstractTexture {
   public DefaultTexture() {
      this.func_110551_a((IResourceManager)null);
   }

   public void func_110551_a(IResourceManager resourcemanager) {
      int[] aint = ShadersTex.createAIntImage(1, -1);
      ShadersTex.setupTexture(this.getMultiTexID(), aint, 1, 1, false, false);
   }
}
