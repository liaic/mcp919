package optifine;

import java.lang.reflect.Constructor;
import optifine.Config;
import optifine.Reflector;
import optifine.ReflectorClass;

public class ReflectorConstructor {

   public ReflectorClass reflectorClass = null;
   public Class[] parameterTypes = null;
   public boolean checked = false;
   public Constructor targetConstructor = null;


   public ReflectorConstructor(ReflectorClass reflectorClass, Class[] parameterTypes) {
      this.reflectorClass = reflectorClass;
      this.parameterTypes = parameterTypes;
      Constructor c = this.getTargetConstructor();
   }

   public Constructor getTargetConstructor() {
      if(this.checked) {
         return this.targetConstructor;
      } else {
         this.checked = true;
         Class cls = this.reflectorClass.getTargetClass();
         if(cls == null) {
            return null;
         } else {
            try {
               this.targetConstructor = findConstructor(cls, this.parameterTypes);
               if(this.targetConstructor == null) {
                  Config.dbg("(Reflector) Constructor not present: " + cls.getName() + ", params: " + Config.arrayToString((Object[])this.parameterTypes));
               }

               if(this.targetConstructor != null) {
                  this.targetConstructor.setAccessible(true);
               }
            } catch (Throwable var3) {
               var3.printStackTrace();
            }

            return this.targetConstructor;
         }
      }
   }

   public static Constructor findConstructor(Class cls, Class[] paramTypes) {
      Constructor[] cs = cls.getDeclaredConstructors();

      for(int i = 0; i < cs.length; ++i) {
         Constructor c = cs[i];
         Class[] types = c.getParameterTypes();
         if(Reflector.matchesTypes(paramTypes, types)) {
            return c;
         }
      }

      return null;
   }

   public boolean exists() {
      return this.checked?this.targetConstructor != null:this.getTargetConstructor() != null;
   }

   public void deactivate() {
      this.checked = true;
      this.targetConstructor = null;
   }
}
