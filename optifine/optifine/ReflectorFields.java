package optifine;

import optifine.ReflectorClass;
import optifine.ReflectorField;

public class ReflectorFields {

   public ReflectorClass reflectorClass;
   public Class fieldType;
   public int fieldCount;
   public ReflectorField[] reflectorFields;


   public ReflectorFields(ReflectorClass reflectorClass, Class fieldType, int fieldCount) {
      this.reflectorClass = reflectorClass;
      this.fieldType = fieldType;
      if(reflectorClass.exists()) {
         if(fieldType != null) {
            this.reflectorFields = new ReflectorField[fieldCount];

            for(int i = 0; i < this.reflectorFields.length; ++i) {
               this.reflectorFields[i] = new ReflectorField(reflectorClass, fieldType, i);
            }

         }
      }
   }

   public ReflectorClass getReflectorClass() {
      return this.reflectorClass;
   }

   public Class getFieldType() {
      return this.fieldType;
   }

   public int getFieldCount() {
      return this.fieldCount;
   }

   public ReflectorField getReflectorField(int index) {
      return index >= 0 && index < this.reflectorFields.length?this.reflectorFields[index]:null;
   }
}
