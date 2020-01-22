package net.minecraft.src;

import java.lang.reflect.Field;
import net.minecraft.src.IFieldLocator;

public class FieldLocatorFixed implements IFieldLocator {
   private Field field;

   public FieldLocatorFixed(Field p_i42_1_) {
      this.field = p_i42_1_;
   }

   public Field getField() {
      return this.field;
   }
}
