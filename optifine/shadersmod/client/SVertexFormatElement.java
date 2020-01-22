package shadersmod.client;

import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;

public class SVertexFormatElement extends VertexFormatElement {

   public int sUsage;


   public SVertexFormatElement(int sUsage, EnumType type, int count) {
      super(0, type, EnumUsage.PADDING, count);
      this.sUsage = sUsage;
   }
}
