package optifine;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import net.minecraft.client.Minecraft;
import optifine.Config;
import optifine.IFieldLocator;
import optifine.ReflectorRaw;

public class FieldLocatorActionKeyF3 implements IFieldLocator {

   public Field getField() {
      Class mcClass = Minecraft.class;
      Field fieldRenderChunksMany = this.getFieldRenderChunksMany();
      if(fieldRenderChunksMany == null) {
         Config.log("(Reflector) Field not present: " + mcClass.getName() + ".actionKeyF3 (field renderChunksMany not found)");
         return null;
      } else {
         Field fieldActionkeyF3 = ReflectorRaw.getFieldAfter(Minecraft.class, fieldRenderChunksMany, Boolean.TYPE, 0);
         if(fieldActionkeyF3 == null) {
            Config.log("(Reflector) Field not present: " + mcClass.getName() + ".actionKeyF3");
            return null;
         } else {
            return fieldActionkeyF3;
         }
      }
   }

   public Field getFieldRenderChunksMany() {
      Minecraft mc = Minecraft.getMinecraft();
      boolean oldRenderChunksMany = mc.renderChunksMany;
      Field[] fields = Minecraft.class.getDeclaredFields();
      mc.renderChunksMany = true;
      Field[] fieldsTrue = ReflectorRaw.getFields(mc, fields, Boolean.TYPE, Boolean.TRUE);
      mc.renderChunksMany = false;
      Field[] fieldsFalse = ReflectorRaw.getFields(mc, fields, Boolean.TYPE, Boolean.FALSE);
      mc.renderChunksMany = oldRenderChunksMany;
      HashSet setTrue = new HashSet(Arrays.asList(fieldsTrue));
      HashSet setFalse = new HashSet(Arrays.asList(fieldsFalse));
      HashSet setFields = new HashSet(setTrue);
      setFields.retainAll(setFalse);
      Field[] fs = (Field[])((Field[])setFields.toArray(new Field[setFields.size()]));
      return fs.length != 1?null:fs[0];
   }
}
