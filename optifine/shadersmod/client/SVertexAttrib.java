package shadersmod.client;

import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumType;

public class SVertexAttrib {

   public int index;
   public int count;
   public EnumType type;
   public int offset;


   public SVertexAttrib(int index, int count, EnumType type) {
      this.index = index;
      this.count = count;
      this.type = type;
   }
}
