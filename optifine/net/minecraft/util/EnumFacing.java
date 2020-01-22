package net.minecraft.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;

public enum EnumFacing implements IStringSerializable {

   DOWN("DOWN", 0, "DOWN", 0, 0, 1, -1, "down", EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.Y, new Vec3i(0, -1, 0)),
   UP("UP", 1, "UP", 1, 1, 0, -1, "up", EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.Y, new Vec3i(0, 1, 0)),
   NORTH("NORTH", 2, "NORTH", 2, 2, 3, 2, "north", EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.Z, new Vec3i(0, 0, -1)),
   SOUTH("SOUTH", 3, "SOUTH", 3, 3, 2, 0, "south", EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.Z, new Vec3i(0, 0, 1)),
   WEST("WEST", 4, "WEST", 4, 4, 5, 1, "west", EnumFacing.AxisDirection.NEGATIVE, EnumFacing.Axis.X, new Vec3i(-1, 0, 0)),
   EAST("EAST", 5, "EAST", 5, 5, 4, 3, "east", EnumFacing.AxisDirection.POSITIVE, EnumFacing.Axis.X, new Vec3i(1, 0, 0));
   public final int index;
   public final int opposite;
   public final int horizontalIndex;
   public final String name;
   public final EnumFacing.Axis axis;
   public final EnumFacing.AxisDirection axisDirection;
   public final Vec3i directionVec;
   public static final EnumFacing[] VALUES = new EnumFacing[6];
   public static final EnumFacing[] HORIZONTALS = new EnumFacing[4];
   public static final Map NAME_LOOKUP = Maps.newHashMap();
   public static final EnumFacing[] $VALUES = new EnumFacing[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};
   public static final String __OBFID = "CL_00001201";
   // $FF: synthetic field
   public static final EnumFacing[] $VALUES$ = new EnumFacing[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};


   public EnumFacing(String var1, int var2, String p_i46016_1_, int p_i46016_2_, int indexIn, int oppositeIn, int horizontalIndexIn, String nameIn, EnumFacing.AxisDirection axisDirectionIn, EnumFacing.Axis axisIn, Vec3i directionVecIn) {
      this.index = indexIn;
      this.horizontalIndex = horizontalIndexIn;
      this.opposite = oppositeIn;
      this.name = nameIn;
      this.axis = axisIn;
      this.axisDirection = axisDirectionIn;
      this.directionVec = directionVecIn;
   }

   public int getIndex() {
      return this.index;
   }

   public int getHorizontalIndex() {
      return this.horizontalIndex;
   }

   public EnumFacing.AxisDirection getAxisDirection() {
      return this.axisDirection;
   }

   public EnumFacing getOpposite() {
      return VALUES[this.opposite];
   }

   public EnumFacing rotateAround(EnumFacing.Axis axis) {
      switch(EnumFacing.SwitchPlane.field_179515_a[axis.ordinal()]) {
      case 1:
         if(this != WEST && this != EAST) {
            return this.rotateX();
         }

         return this;
      case 2:
         if(this != UP && this != DOWN) {
            return this.rotateY();
         }

         return this;
      case 3:
         if(this != NORTH && this != SOUTH) {
            return this.rotateZ();
         }

         return this;
      default:
         throw new IllegalStateException("Unable to get CW facing for axis " + axis);
      }
   }

   public EnumFacing rotateY() {
      switch(EnumFacing.SwitchPlane.field_179513_b[this.ordinal()]) {
      case 1:
         return EAST;
      case 2:
         return SOUTH;
      case 3:
         return WEST;
      case 4:
         return NORTH;
      default:
         throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
      }
   }

   public EnumFacing rotateX() {
      switch(EnumFacing.SwitchPlane.field_179513_b[this.ordinal()]) {
      case 1:
         return DOWN;
      case 2:
      case 4:
      default:
         throw new IllegalStateException("Unable to get X-rotated facing of " + this);
      case 3:
         return UP;
      case 5:
         return NORTH;
      case 6:
         return SOUTH;
      }
   }

   public EnumFacing rotateZ() {
      switch(EnumFacing.SwitchPlane.field_179513_b[this.ordinal()]) {
      case 2:
         return DOWN;
      case 3:
      default:
         throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
      case 4:
         return UP;
      case 5:
         return EAST;
      case 6:
         return WEST;
      }
   }

   public EnumFacing rotateYCCW() {
      switch(EnumFacing.SwitchPlane.field_179513_b[this.ordinal()]) {
      case 1:
         return WEST;
      case 2:
         return NORTH;
      case 3:
         return EAST;
      case 4:
         return SOUTH;
      default:
         throw new IllegalStateException("Unable to get CCW facing of " + this);
      }
   }

   public int getFrontOffsetX() {
      return this.axis == EnumFacing.Axis.X?this.axisDirection.getOffset():0;
   }

   public int getFrontOffsetY() {
      return this.axis == EnumFacing.Axis.Y?this.axisDirection.getOffset():0;
   }

   public int getFrontOffsetZ() {
      return this.axis == EnumFacing.Axis.Z?this.axisDirection.getOffset():0;
   }

   public String getName2() {
      return this.name;
   }

   public EnumFacing.Axis getAxis() {
      return this.axis;
   }

   public static EnumFacing byName(String name) {
      return name == null?null:(EnumFacing)NAME_LOOKUP.get(name.toLowerCase());
   }

   public static EnumFacing getFront(int index) {
      return VALUES[MathHelper.abs_int(index % VALUES.length)];
   }

   public static EnumFacing getHorizontal(int p_176731_0_) {
      return HORIZONTALS[MathHelper.abs_int(p_176731_0_ % HORIZONTALS.length)];
   }

   public static EnumFacing fromAngle(double angle) {
      return getHorizontal(MathHelper.floor_double(angle / 90.0D + 0.5D) & 3);
   }

   public static EnumFacing random(Random rand) {
      return values()[rand.nextInt(values().length)];
   }

   public static EnumFacing getFacingFromVector(float p_176737_0_, float p_176737_1_, float p_176737_2_) {
      EnumFacing var3 = NORTH;
      float var4 = Float.MIN_VALUE;
      EnumFacing[] var5 = values();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         EnumFacing var8 = var5[var7];
         float var9 = p_176737_0_ * (float)var8.directionVec.getX() + p_176737_1_ * (float)var8.directionVec.getY() + p_176737_2_ * (float)var8.directionVec.getZ();
         if(var9 > var4) {
            var4 = var9;
            var3 = var8;
         }
      }

      return var3;
   }

   public String toString() {
      return this.name;
   }

   public String getName() {
      return this.name;
   }

   public static EnumFacing getFacingFromAxis(EnumFacing.AxisDirection p_500005_0_, EnumFacing.Axis p_500005_1_) {
      EnumFacing[] var2 = values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EnumFacing var5 = var2[var4];
         if(var5.getAxisDirection() == p_500005_0_ && var5.getAxis() == p_500005_1_) {
            return var5;
         }
      }

      throw new IllegalArgumentException("No such direction: " + p_500005_0_ + " " + p_500005_1_);
   }

   public Vec3i getDirectionVec() {
      return this.directionVec;
   }

   static {
      EnumFacing[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         EnumFacing var3 = var0[var2];
         VALUES[var3.index] = var3;
         if(var3.getAxis().isHorizontal()) {
            HORIZONTALS[var3.horizontalIndex] = var3;
         }

         NAME_LOOKUP.put(var3.getName2().toLowerCase(), var3);
      }

   }

   public static final class SwitchPlane {

      public static final int[] field_179515_a;
      public static final int[] field_179513_b;
      public static final int[] field_179514_c = new int[EnumFacing.Plane.values().length];
      public static final String __OBFID = "CL_00002322";


      static {
         try {
            field_179514_c[EnumFacing.Plane.HORIZONTAL.ordinal()] = 1;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            field_179514_c[EnumFacing.Plane.VERTICAL.ordinal()] = 2;
         } catch (NoSuchFieldError var10) {
            ;
         }

         field_179513_b = new int[EnumFacing.values().length];

         try {
            field_179513_b[EnumFacing.NORTH.ordinal()] = 1;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            field_179513_b[EnumFacing.EAST.ordinal()] = 2;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            field_179513_b[EnumFacing.SOUTH.ordinal()] = 3;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            field_179513_b[EnumFacing.WEST.ordinal()] = 4;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            field_179513_b[EnumFacing.UP.ordinal()] = 5;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            field_179513_b[EnumFacing.DOWN.ordinal()] = 6;
         } catch (NoSuchFieldError var4) {
            ;
         }

         field_179515_a = new int[EnumFacing.Axis.values().length];

         try {
            field_179515_a[EnumFacing.Axis.X.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            field_179515_a[EnumFacing.Axis.Y.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            field_179515_a[EnumFacing.Axis.Z.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum Axis implements Predicate, IStringSerializable {

      X("X", 0, "X", 0, "x", EnumFacing.Plane.HORIZONTAL),
      Y("Y", 1, "Y", 1, "y", EnumFacing.Plane.VERTICAL),
      Z("Z", 2, "Z", 2, "z", EnumFacing.Plane.HORIZONTAL);
      public static final Map NAME_LOOKUP = Maps.newHashMap();
      public final String name;
      public final EnumFacing.Plane plane;
      public static final EnumFacing.Axis[] $VALUES = new EnumFacing.Axis[]{X, Y, Z};
      public static final String __OBFID = "CL_00002321";
      // $FF: synthetic field
      public static final EnumFacing.Axis[] $VALUES$ = new EnumFacing.Axis[]{X, Y, Z};


      public Axis(String var1, int var2, String p_i46015_1_, int p_i46015_2_, String name, EnumFacing.Plane plane) {
         this.name = name;
         this.plane = plane;
      }

      public static EnumFacing.Axis byName(String name) {
         return name == null?null:(EnumFacing.Axis)NAME_LOOKUP.get(name.toLowerCase());
      }

      public String getName2() {
         return this.name;
      }

      public boolean isVertical() {
         return this.plane == EnumFacing.Plane.VERTICAL;
      }

      public boolean isHorizontal() {
         return this.plane == EnumFacing.Plane.HORIZONTAL;
      }

      public String toString() {
         return this.name;
      }

      public boolean apply(EnumFacing facing) {
         return facing != null && facing.getAxis() == this;
      }

      public EnumFacing.Plane getPlane() {
         return this.plane;
      }

      public String getName() {
         return this.name;
      }

      public boolean apply(Object p_apply_1_) {
         return this.apply((EnumFacing)p_apply_1_);
      }

      static {
         EnumFacing.Axis[] var0 = values();
         int var1 = var0.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            EnumFacing.Axis var3 = var0[var2];
            NAME_LOOKUP.put(var3.getName2().toLowerCase(), var3);
         }

      }
   }

   public static enum AxisDirection {

      POSITIVE("POSITIVE", 0, "POSITIVE", 0, 1, "Towards positive"),
      NEGATIVE("NEGATIVE", 1, "NEGATIVE", 1, -1, "Towards negative");
      public final int offset;
      public final String description;
      public static final EnumFacing.AxisDirection[] $VALUES = new EnumFacing.AxisDirection[]{POSITIVE, NEGATIVE};
      public static final String __OBFID = "CL_00002320";
      // $FF: synthetic field
      public static final EnumFacing.AxisDirection[] $VALUES$ = new EnumFacing.AxisDirection[]{POSITIVE, NEGATIVE};


      public AxisDirection(String var1, int var2, String p_i46014_1_, int p_i46014_2_, int offset, String description) {
         this.offset = offset;
         this.description = description;
      }

      public int getOffset() {
         return this.offset;
      }

      public String toString() {
         return this.description;
      }

   }

   public static enum Plane implements Predicate, Iterable {

      HORIZONTAL("HORIZONTAL", 0, "HORIZONTAL", 0),
      VERTICAL("VERTICAL", 1, "VERTICAL", 1);
      public static final EnumFacing.Plane[] $VALUES = new EnumFacing.Plane[]{HORIZONTAL, VERTICAL};
      public static final String __OBFID = "CL_00002319";
      // $FF: synthetic field
      public static final EnumFacing.Plane[] $VALUES$ = new EnumFacing.Plane[]{HORIZONTAL, VERTICAL};


      public Plane(String var1, int var2, String p_i46013_1_, int p_i46013_2_) {}

      public EnumFacing[] facings() {
         switch(EnumFacing.SwitchPlane.field_179514_c[this.ordinal()]) {
         case 1:
            return new EnumFacing[]{EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};
         case 2:
            return new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN};
         default:
            throw new Error("Someone\'s been tampering with the universe!");
         }
      }

      public EnumFacing random(Random rand) {
         EnumFacing[] var2 = this.facings();
         return var2[rand.nextInt(var2.length)];
      }

      public boolean apply(EnumFacing facing) {
         return facing != null && facing.getAxis().getPlane() == this;
      }

      public Iterator iterator() {
         return Iterators.forArray(this.facings());
      }

      public boolean apply(Object p_apply_1_) {
         return this.apply((EnumFacing)p_apply_1_);
      }

   }
}
