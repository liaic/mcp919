package optifine;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import optifine.PlayerItemModel;

public class PlayerItemRenderer {

   public int attachTo = 0;
   public ModelRenderer modelRenderer = null;


   public PlayerItemRenderer(int attachTo, ModelRenderer modelRenderer) {
      this.attachTo = attachTo;
      this.modelRenderer = modelRenderer;
   }

   public ModelRenderer getModelRenderer() {
      return this.modelRenderer;
   }

   public void render(ModelBiped modelBiped, float scale) {
      ModelRenderer attachModel = PlayerItemModel.getAttachModel(modelBiped, this.attachTo);
      if(attachModel != null) {
         attachModel.postRender(scale);
      }

      this.modelRenderer.render(scale);
   }
}
