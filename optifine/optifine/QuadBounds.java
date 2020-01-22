package optifine;

import net.minecraft.util.EnumFacing;

public class QuadBounds {

   public float minX = Float.MAX_VALUE;
   public float minY = Float.MAX_VALUE;
   public float minZ = Float.MAX_VALUE;
   public float maxX = -3.4028235E38F;
   public float maxY = -3.4028235E38F;
   public float maxZ = -3.4028235E38F;


   public QuadBounds(int[] vertexData) {
      int step = vertexData.length / 4;

      for(int i = 0; i < 4; ++i) {
         int pos = i * step;
         float x = Float.intBitsToFloat(vertexData[pos + 0]);
         float y = Float.intBitsToFloat(vertexData[pos + 1]);
         float z = Float.intBitsToFloat(vertexData[pos + 2]);
         if(this.minX > x) {
            this.minX = x;
         }

         if(this.minY > y) {
            this.minY = y;
         }

         if(this.minZ > z) {
            this.minZ = z;
         }

         if(this.maxX < x) {
            this.maxX = x;
         }

         if(this.maxY < y) {
            this.maxY = y;
         }

         if(this.maxZ < z) {
            this.maxZ = z;
         }
      }

   }

   public float getMinX() {
      return this.minX;
   }

   public float getMinY() {
      return this.minY;
   }

   public float getMinZ() {
      return this.minZ;
   }

   public float getMaxX() {
      return this.maxX;
   }

   public float getMaxY() {
      return this.maxY;
   }

   public float getMaxZ() {
      return this.maxZ;
   }

   public boolean isFaceQuad(EnumFacing face) {
      float min;
      float max;
      float val;
      switch(QuadBounds.NamelessClass486505425.$SwitchMap$net$minecraft$util$EnumFacing[face.ordinal()]) {
      case 1:
         min = this.getMinY();
         max = this.getMaxY();
         val = 0.0F;
         break;
      case 2:
         min = this.getMinY();
         max = this.getMaxY();
         val = 1.0F;
         break;
      case 3:
         min = this.getMinZ();
         max = this.getMaxZ();
         val = 0.0F;
         break;
      case 4:
         min = this.getMinZ();
         max = this.getMaxZ();
         val = 1.0F;
         break;
      case 5:
         min = this.getMinX();
         max = this.getMaxX();
         val = 0.0F;
         break;
      case 6:
         min = this.getMinX();
         max = this.getMaxX();
         val = 1.0F;
         break;
      default:
         return false;
      }

      return min == val && max == val;
   }

   public boolean isFullQuad(EnumFacing face) {
      float min1;
      float max1;
      float min2;
      float max2;
      switch(QuadBounds.NamelessClass486505425.$SwitchMap$net$minecraft$util$EnumFacing[face.ordinal()]) {
      case 1:
      case 2:
         min1 = this.getMinX();
         max1 = this.getMaxX();
         min2 = this.getMinZ();
         max2 = this.getMaxZ();
         break;
      case 3:
      case 4:
         min1 = this.getMinX();
         max1 = this.getMaxX();
         min2 = this.getMinY();
         max2 = this.getMaxY();
         break;
      case 5:
      case 6:
         min1 = this.getMinY();
         max1 = this.getMaxY();
         min2 = this.getMinZ();
         max2 = this.getMaxZ();
         break;
      default:
         return false;
      }

      return min1 == 0.0F && max1 == 1.0F && min2 == 0.0F && max2 == 1.0F;
   }

   // $FF: synthetic class
   public static class NamelessClass486505425 {

      // $FF: synthetic field
      public static final int[] $SwitchMap$net$minecraft$util$EnumFacing = new int[EnumFacing.values().length];


      static {
         try {
            $SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.DOWN.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.UP.ordinal()] = 2;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.NORTH.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.SOUTH.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.WEST.ordinal()] = 5;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$util$EnumFacing[EnumFacing.EAST.ordinal()] = 6;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
