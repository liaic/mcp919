package optifine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import optifine.DynamicLight;

public class DynamicLightsMap {

   public Map<Integer, DynamicLight> map = new HashMap();
   public List<DynamicLight> list = new ArrayList();
   public boolean dirty = false;


   public DynamicLight put(int id, DynamicLight dynamicLight) {
      DynamicLight old = (DynamicLight)this.map.put(Integer.valueOf(id), dynamicLight);
      this.setDirty();
      return old;
   }

   public DynamicLight get(int id) {
      return (DynamicLight)this.map.get(Integer.valueOf(id));
   }

   public int size() {
      return this.map.size();
   }

   public DynamicLight remove(int id) {
      DynamicLight old = (DynamicLight)this.map.remove(Integer.valueOf(id));
      if(old != null) {
         this.setDirty();
      }

      return old;
   }

   public void clear() {
      this.map.clear();
      this.setDirty();
   }

   public void setDirty() {
      this.dirty = true;
   }

   public List<DynamicLight> valueList() {
      if(this.dirty) {
         this.list.clear();
         this.list.addAll(this.map.values());
         this.dirty = false;
      }

      return this.list;
   }
}
