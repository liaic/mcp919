package optifine;

import java.lang.reflect.Field;
import optifine.Config;
import optifine.IFieldLocator;
import optifine.ReflectorClass;

public class FieldLocatorName implements IFieldLocator {

   public ReflectorClass reflectorClass = null;
   public String targetFieldName = null;


   public FieldLocatorName(ReflectorClass reflectorClass, String targetFieldName) {
      this.reflectorClass = reflectorClass;
      this.targetFieldName = targetFieldName;
   }

   public Field getField() {
      Class cls = this.reflectorClass.getTargetClass();
      if(cls == null) {
         return null;
      } else {
         try {
            Field e = this.getDeclaredField(cls, this.targetFieldName);
            e.setAccessible(true);
            return e;
         } catch (NoSuchFieldException var3) {
            Config.log("(Reflector) Field not present: " + cls.getName() + "." + this.targetFieldName);
            return null;
         } catch (SecurityException var4) {
            var4.printStackTrace();
            return null;
         } catch (Throwable var5) {
            var5.printStackTrace();
            return null;
         }
      }
   }

   public Field getDeclaredField(Class cls, String name) throws NoSuchFieldException {
      Field[] fields = cls.getDeclaredFields();

      for(int i = 0; i < fields.length; ++i) {
         Field field = fields[i];
         if(field.getName().equals(name)) {
            return field;
         }
      }

      if(cls == Object.class) {
         throw new NoSuchFieldException(name);
      } else {
         return this.getDeclaredField(cls.getSuperclass(), name);
      }
   }
}
