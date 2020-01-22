package net.minecraft.profiler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.src.Config;
import net.minecraft.src.Lagometer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Profiler {
   private static final Logger field_151234_b = LogManager.getLogger();
   private final List field_76325_b = Lists.newArrayList();
   private final List field_76326_c = Lists.newArrayList();
   public boolean field_76327_a;
   private String field_76323_d = "";
   private final Map field_76324_e = Maps.newHashMap();
   private static final String __OBFID = "CL_00001497";
   public boolean profilerGlobalEnabled = true;
   private boolean profilerLocalEnabled;
   private static final String SCHEDULED_EXECUTABLES = "scheduledExecutables";
   private static final String TICK = "tick";
   private static final String PRE_RENDER_ERRORS = "preRenderErrors";
   private static final String RENDER = "render";
   private static final String DISPLAY = "display";
   private static final int HASH_SCHEDULED_EXECUTABLES = "scheduledExecutables".hashCode();
   private static final int HASH_TICK = "tick".hashCode();
   private static final int HASH_PRE_RENDER_ERRORS = "preRenderErrors".hashCode();
   private static final int HASH_RENDER = "render".hashCode();
   private static final int HASH_DISPLAY = "display".hashCode();

   public Profiler() {
      this.profilerLocalEnabled = this.profilerGlobalEnabled;
   }

   public void func_76317_a() {
      this.field_76324_e.clear();
      this.field_76323_d = "";
      this.field_76325_b.clear();
      this.profilerLocalEnabled = this.profilerGlobalEnabled;
   }

   public void func_76320_a(String p_76320_1_) {
      if(Lagometer.isActive()) {
         int i = p_76320_1_.hashCode();
         if(i == HASH_SCHEDULED_EXECUTABLES && p_76320_1_.equals("scheduledExecutables")) {
            Lagometer.timerScheduledExecutables.start();
         } else if(i == HASH_TICK && p_76320_1_.equals("tick") && Config.isMinecraftThread()) {
            Lagometer.timerScheduledExecutables.end();
            Lagometer.timerTick.start();
         } else if(i == HASH_PRE_RENDER_ERRORS && p_76320_1_.equals("preRenderErrors")) {
            Lagometer.timerTick.end();
         }
      }

      if(Config.isFastRender()) {
         int j = p_76320_1_.hashCode();
         if(j == HASH_RENDER && p_76320_1_.equals("render")) {
            GlStateManager.clearEnabled = false;
         } else if(j == HASH_DISPLAY && p_76320_1_.equals("display")) {
            GlStateManager.clearEnabled = true;
         }
      }

      if(this.profilerLocalEnabled) {
         if(this.field_76327_a) {
            if(this.field_76323_d.length() > 0) {
               this.field_76323_d = this.field_76323_d + ".";
            }

            this.field_76323_d = this.field_76323_d + p_76320_1_;
            this.field_76325_b.add(this.field_76323_d);
            this.field_76326_c.add(Long.valueOf(System.nanoTime()));
         }

      }
   }

   public void func_76319_b() {
      if(this.profilerLocalEnabled) {
         if(this.field_76327_a) {
            long i = System.nanoTime();
            long j = ((Long)this.field_76326_c.remove(this.field_76326_c.size() - 1)).longValue();
            this.field_76325_b.remove(this.field_76325_b.size() - 1);
            long k = i - j;
            if(this.field_76324_e.containsKey(this.field_76323_d)) {
               this.field_76324_e.put(this.field_76323_d, Long.valueOf(((Long)this.field_76324_e.get(this.field_76323_d)).longValue() + k));
            } else {
               this.field_76324_e.put(this.field_76323_d, Long.valueOf(k));
            }

            if(k > 100000000L) {
               field_151234_b.warn("Something\'s taking too long! \'" + this.field_76323_d + "\' took aprox " + (double)k / 1000000.0D + " ms");
            }

            this.field_76323_d = !this.field_76325_b.isEmpty()?(String)this.field_76325_b.get(this.field_76325_b.size() - 1):"";
         }

      }
   }

   public List func_76321_b(String p_76321_1_) {
      this.profilerLocalEnabled = this.profilerGlobalEnabled;
      if(!this.profilerLocalEnabled) {
         return new ArrayList(Arrays.asList(new Profiler.Result[]{new Profiler.Result("root", 0.0D, 0.0D)}));
      } else if(!this.field_76327_a) {
         return null;
      } else {
         long i = this.field_76324_e.containsKey("root")?((Long)this.field_76324_e.get("root")).longValue():0L;
         long j = this.field_76324_e.containsKey(p_76321_1_)?((Long)this.field_76324_e.get(p_76321_1_)).longValue():-1L;
         ArrayList arraylist = Lists.newArrayList();
         if(p_76321_1_.length() > 0) {
            p_76321_1_ = p_76321_1_ + ".";
         }

         long k = 0L;

         for(String s : this.field_76324_e.keySet()) {
            if(s.length() > p_76321_1_.length() && s.startsWith(p_76321_1_) && s.indexOf(".", p_76321_1_.length() + 1) < 0) {
               k += ((Long)this.field_76324_e.get(s)).longValue();
            }
         }

         float f = (float)k;
         if(k < j) {
            k = j;
         }

         if(i < k) {
            i = k;
         }

         for(String s1 : this.field_76324_e.keySet()) {
            if(s1.length() > p_76321_1_.length() && s1.startsWith(p_76321_1_) && s1.indexOf(".", p_76321_1_.length() + 1) < 0) {
               long l = ((Long)this.field_76324_e.get(s1)).longValue();
               double d0 = (double)l * 100.0D / (double)k;
               double d1 = (double)l * 100.0D / (double)i;
               String s2 = s1.substring(p_76321_1_.length());
               arraylist.add(new Profiler.Result(s2, d0, d1));
            }
         }

         for(String s3 : this.field_76324_e.keySet()) {
            this.field_76324_e.put(s3, Long.valueOf(((Long)this.field_76324_e.get(s3)).longValue() * 950L / 1000L));
         }

         if((float)k > f) {
            arraylist.add(new Profiler.Result("unspecified", (double)((float)k - f) * 100.0D / (double)k, (double)((float)k - f) * 100.0D / (double)i));
         }

         Collections.sort(arraylist);
         arraylist.add(0, new Profiler.Result(p_76321_1_, 100.0D, (double)k * 100.0D / (double)i));
         return arraylist;
      }
   }

   public void func_76318_c(String p_76318_1_) {
      if(this.profilerLocalEnabled) {
         this.func_76319_b();
         this.func_76320_a(p_76318_1_);
      }
   }

   public String func_76322_c() {
      return this.field_76325_b.size() == 0?"[UNKNOWN]":(String)this.field_76325_b.get(this.field_76325_b.size() - 1);
   }

   public static final class Result implements Comparable {
      public double field_76332_a;
      public double field_76330_b;
      public String field_76331_c;
      private static final String __OBFID = "CL_00001498";

      public Result(String p_i1554_1_, double p_i1554_2_, double p_i1554_4_) {
         this.field_76331_c = p_i1554_1_;
         this.field_76332_a = p_i1554_2_;
         this.field_76330_b = p_i1554_4_;
      }

      public int compareTo(Profiler.Result p_compareTo_1_) {
         return p_compareTo_1_.field_76332_a < this.field_76332_a?-1:(p_compareTo_1_.field_76332_a > this.field_76332_a?1:p_compareTo_1_.field_76331_c.compareTo(this.field_76331_c));
      }

      public int func_76329_a() {
         return (this.field_76331_c.hashCode() & 11184810) + 4473924;
      }

      public int compareTo(Object p_compareTo_1_) {
         return this.compareTo((Profiler.Result)p_compareTo_1_);
      }
   }
}
