package shadersmod.client;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import shadersmod.client.BlockAliases;
import shadersmod.client.Shaders;

public class SVertexBuilder {
   int vertexSize;
   int offsetNormal;
   int offsetUV;
   int offsetUVCenter;
   boolean hasNormal;
   boolean hasTangent;
   boolean hasUV;
   boolean hasUVCenter;
   long[] entityData = new long[10];
   int entityDataIndex = 0;

   public SVertexBuilder() {
      this.entityData[this.entityDataIndex] = 0L;
   }

   public static void initVertexBuilder(WorldRenderer wrr) {
      wrr.sVertexBuilder = new SVertexBuilder();
   }

   public void pushEntity(long data) {
      ++this.entityDataIndex;
      this.entityData[this.entityDataIndex] = data;
   }

   public void popEntity() {
      this.entityData[this.entityDataIndex] = 0L;
      --this.entityDataIndex;
   }

   public static void pushEntity(IBlockState blockState, BlockPos blockPos, IBlockAccess blockAccess, WorldRenderer wrr) {
      Block block = blockState.func_177230_c();
      int i;
      int j;
      if(blockState instanceof BlockStateBase) {
         BlockStateBase blockstatebase = (BlockStateBase)blockState;
         i = blockstatebase.getBlockId();
         j = blockstatebase.getMetadata();
      } else {
         i = Block.func_149682_b(block);
         j = block.func_176201_c(blockState);
      }

      i = BlockAliases.getMappedBlockId(i, j);
      int i1 = block.func_149645_b();
      int k = ((i1 & '\uffff') << 16) + (i & '\uffff');
      int l = j & '\uffff';
      wrr.sVertexBuilder.pushEntity(((long)l << 32) + (long)k);
   }

   public static void popEntity(WorldRenderer wrr) {
      wrr.sVertexBuilder.popEntity();
   }

   public static boolean popEntity(boolean value, WorldRenderer wrr) {
      wrr.sVertexBuilder.popEntity();
      return value;
   }

   public static void endSetVertexFormat(WorldRenderer wrr) {
      SVertexBuilder svertexbuilder = wrr.sVertexBuilder;
      VertexFormat vertexformat = wrr.func_178973_g();
      svertexbuilder.vertexSize = vertexformat.func_177338_f() / 4;
      svertexbuilder.hasNormal = vertexformat.func_177350_b();
      svertexbuilder.hasTangent = svertexbuilder.hasNormal;
      svertexbuilder.hasUV = vertexformat.func_177347_a(0);
      svertexbuilder.offsetNormal = svertexbuilder.hasNormal?vertexformat.func_177342_c() / 4:0;
      svertexbuilder.offsetUV = svertexbuilder.hasUV?vertexformat.func_177344_b(0) / 4:0;
      svertexbuilder.offsetUVCenter = 8;
   }

   public static void beginAddVertex(WorldRenderer wrr) {
      if(wrr.field_178997_d == 0) {
         endSetVertexFormat(wrr);
      }

   }

   public static void endAddVertex(WorldRenderer wrr) {
      SVertexBuilder svertexbuilder = wrr.sVertexBuilder;
      if(svertexbuilder.vertexSize == 14) {
         if(wrr.field_179006_k == 7 && wrr.field_178997_d % 4 == 0) {
            svertexbuilder.calcNormal(wrr, wrr.func_181664_j() - 4 * svertexbuilder.vertexSize);
         }

         long i = svertexbuilder.entityData[svertexbuilder.entityDataIndex];
         int j = wrr.func_181664_j() - 14 + 12;
         wrr.field_178999_b.put(j, (int)i);
         wrr.field_178999_b.put(j + 1, (int)(i >> 32));
      }

   }

   public static void beginAddVertexData(WorldRenderer wrr, int[] data) {
      if(wrr.field_178997_d == 0) {
         endSetVertexFormat(wrr);
      }

      SVertexBuilder svertexbuilder = wrr.sVertexBuilder;
      if(svertexbuilder.vertexSize == 14) {
         long i = svertexbuilder.entityData[svertexbuilder.entityDataIndex];

         for(int j = 12; j + 1 < data.length; j += 14) {
            data[j] = (int)i;
            data[j + 1] = (int)(i >> 32);
         }
      }

   }

   public static void endAddVertexData(WorldRenderer wrr) {
      SVertexBuilder svertexbuilder = wrr.sVertexBuilder;
      if(svertexbuilder.vertexSize == 14 && wrr.field_179006_k == 7 && wrr.field_178997_d % 4 == 0) {
         svertexbuilder.calcNormal(wrr, wrr.func_181664_j() - 4 * svertexbuilder.vertexSize);
      }

   }

   public void calcNormal(WorldRenderer wrr, int baseIndex) {
      FloatBuffer floatbuffer = wrr.field_179000_c;
      IntBuffer intbuffer = wrr.field_178999_b;
      int i = wrr.func_181664_j();
      float f = floatbuffer.get(baseIndex + 0 * this.vertexSize);
      float f1 = floatbuffer.get(baseIndex + 0 * this.vertexSize + 1);
      float f2 = floatbuffer.get(baseIndex + 0 * this.vertexSize + 2);
      float f3 = floatbuffer.get(baseIndex + 0 * this.vertexSize + this.offsetUV);
      float f4 = floatbuffer.get(baseIndex + 0 * this.vertexSize + this.offsetUV + 1);
      float f5 = floatbuffer.get(baseIndex + 1 * this.vertexSize);
      float f6 = floatbuffer.get(baseIndex + 1 * this.vertexSize + 1);
      float f7 = floatbuffer.get(baseIndex + 1 * this.vertexSize + 2);
      float f8 = floatbuffer.get(baseIndex + 1 * this.vertexSize + this.offsetUV);
      float f9 = floatbuffer.get(baseIndex + 1 * this.vertexSize + this.offsetUV + 1);
      float f10 = floatbuffer.get(baseIndex + 2 * this.vertexSize);
      float f11 = floatbuffer.get(baseIndex + 2 * this.vertexSize + 1);
      float f12 = floatbuffer.get(baseIndex + 2 * this.vertexSize + 2);
      float f13 = floatbuffer.get(baseIndex + 2 * this.vertexSize + this.offsetUV);
      float f14 = floatbuffer.get(baseIndex + 2 * this.vertexSize + this.offsetUV + 1);
      float f15 = floatbuffer.get(baseIndex + 3 * this.vertexSize);
      float f16 = floatbuffer.get(baseIndex + 3 * this.vertexSize + 1);
      float f17 = floatbuffer.get(baseIndex + 3 * this.vertexSize + 2);
      float f18 = floatbuffer.get(baseIndex + 3 * this.vertexSize + this.offsetUV);
      float f19 = floatbuffer.get(baseIndex + 3 * this.vertexSize + this.offsetUV + 1);
      float f20 = f10 - f;
      float f21 = f11 - f1;
      float f22 = f12 - f2;
      float f23 = f15 - f5;
      float f24 = f16 - f6;
      float f25 = f17 - f7;
      float f30 = f21 * f25 - f24 * f22;
      float f31 = f22 * f23 - f25 * f20;
      float f32 = f20 * f24 - f23 * f21;
      float f33 = f30 * f30 + f31 * f31 + f32 * f32;
      float f34 = (double)f33 != 0.0D?(float)(1.0D / Math.sqrt((double)f33)):1.0F;
      f30 = f30 * f34;
      f31 = f31 * f34;
      f32 = f32 * f34;
      f20 = f5 - f;
      f21 = f6 - f1;
      f22 = f7 - f2;
      float f26 = f8 - f3;
      float f27 = f9 - f4;
      f23 = f10 - f;
      f24 = f11 - f1;
      f25 = f12 - f2;
      float f28 = f13 - f3;
      float f29 = f14 - f4;
      float f35 = f26 * f29 - f28 * f27;
      float f36 = f35 != 0.0F?1.0F / f35:1.0F;
      float f37 = (f29 * f20 - f27 * f23) * f36;
      float f38 = (f29 * f21 - f27 * f24) * f36;
      float f39 = (f29 * f22 - f27 * f25) * f36;
      float f40 = (f26 * f23 - f28 * f20) * f36;
      float f41 = (f26 * f24 - f28 * f21) * f36;
      float f42 = (f26 * f25 - f28 * f22) * f36;
      f33 = f37 * f37 + f38 * f38 + f39 * f39;
      f34 = (double)f33 != 0.0D?(float)(1.0D / Math.sqrt((double)f33)):1.0F;
      f37 = f37 * f34;
      f38 = f38 * f34;
      f39 = f39 * f34;
      f33 = f40 * f40 + f41 * f41 + f42 * f42;
      f34 = (double)f33 != 0.0D?(float)(1.0D / Math.sqrt((double)f33)):1.0F;
      f40 = f40 * f34;
      f41 = f41 * f34;
      f42 = f42 * f34;
      float f43 = f32 * f38 - f31 * f39;
      float f44 = f30 * f39 - f32 * f37;
      float f45 = f31 * f37 - f30 * f38;
      float f46 = f40 * f43 + f41 * f44 + f42 * f45 < 0.0F?-1.0F:1.0F;
      int j = (int)(f30 * 127.0F) & 255;
      int k = (int)(f31 * 127.0F) & 255;
      int l = (int)(f32 * 127.0F) & 255;
      int i1 = (l << 16) + (k << 8) + j;
      intbuffer.put(baseIndex + 0 * this.vertexSize + this.offsetNormal, i1);
      intbuffer.put(baseIndex + 1 * this.vertexSize + this.offsetNormal, i1);
      intbuffer.put(baseIndex + 2 * this.vertexSize + this.offsetNormal, i1);
      intbuffer.put(baseIndex + 3 * this.vertexSize + this.offsetNormal, i1);
      int j1 = ((int)(f37 * 32767.0F) & '\uffff') + (((int)(f38 * 32767.0F) & '\uffff') << 16);
      int k1 = ((int)(f39 * 32767.0F) & '\uffff') + (((int)(f46 * 32767.0F) & '\uffff') << 16);
      intbuffer.put(baseIndex + 0 * this.vertexSize + 10, j1);
      intbuffer.put(baseIndex + 0 * this.vertexSize + 10 + 1, k1);
      intbuffer.put(baseIndex + 1 * this.vertexSize + 10, j1);
      intbuffer.put(baseIndex + 1 * this.vertexSize + 10 + 1, k1);
      intbuffer.put(baseIndex + 2 * this.vertexSize + 10, j1);
      intbuffer.put(baseIndex + 2 * this.vertexSize + 10 + 1, k1);
      intbuffer.put(baseIndex + 3 * this.vertexSize + 10, j1);
      intbuffer.put(baseIndex + 3 * this.vertexSize + 10 + 1, k1);
      float f47 = (f3 + f8 + f13 + f18) / 4.0F;
      float f48 = (f4 + f9 + f14 + f19) / 4.0F;
      floatbuffer.put(baseIndex + 0 * this.vertexSize + 8, f47);
      floatbuffer.put(baseIndex + 0 * this.vertexSize + 8 + 1, f48);
      floatbuffer.put(baseIndex + 1 * this.vertexSize + 8, f47);
      floatbuffer.put(baseIndex + 1 * this.vertexSize + 8 + 1, f48);
      floatbuffer.put(baseIndex + 2 * this.vertexSize + 8, f47);
      floatbuffer.put(baseIndex + 2 * this.vertexSize + 8 + 1, f48);
      floatbuffer.put(baseIndex + 3 * this.vertexSize + 8, f47);
      floatbuffer.put(baseIndex + 3 * this.vertexSize + 8 + 1, f48);
   }

   public static void calcNormalChunkLayer(WorldRenderer wrr) {
      if(wrr.func_178973_g().func_177350_b() && wrr.field_179006_k == 7 && wrr.field_178997_d % 4 == 0) {
         SVertexBuilder svertexbuilder = wrr.sVertexBuilder;
         endSetVertexFormat(wrr);
         int i = wrr.field_178997_d * svertexbuilder.vertexSize;

         for(int j = 0; j < i; j += svertexbuilder.vertexSize * 4) {
            svertexbuilder.calcNormal(wrr, j);
         }
      }

   }

   public static void drawArrays(int drawMode, int first, int count, WorldRenderer wrr) {
      if(count != 0) {
         VertexFormat vertexformat = wrr.func_178973_g();
         int i = vertexformat.func_177338_f();
         if(i == 56) {
            ByteBuffer bytebuffer = wrr.func_178966_f();
            bytebuffer.position(32);
            GL20.glVertexAttribPointer(Shaders.midTexCoordAttrib, 2, 5126, false, i, bytebuffer);
            bytebuffer.position(40);
            GL20.glVertexAttribPointer(Shaders.tangentAttrib, 4, 5122, false, i, bytebuffer);
            bytebuffer.position(48);
            GL20.glVertexAttribPointer(Shaders.entityAttrib, 3, 5122, false, i, bytebuffer);
            bytebuffer.position(0);
            GL20.glEnableVertexAttribArray(Shaders.midTexCoordAttrib);
            GL20.glEnableVertexAttribArray(Shaders.tangentAttrib);
            GL20.glEnableVertexAttribArray(Shaders.entityAttrib);
            GL11.glDrawArrays(drawMode, first, count);
            GL20.glDisableVertexAttribArray(Shaders.midTexCoordAttrib);
            GL20.glDisableVertexAttribArray(Shaders.tangentAttrib);
            GL20.glDisableVertexAttribArray(Shaders.entityAttrib);
         } else {
            GL11.glDrawArrays(drawMode, first, count);
         }
      }

   }
}
