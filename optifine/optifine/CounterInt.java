package optifine;


public class CounterInt {

   public int startValue;
   public int value;


   public CounterInt(int startValue) {
      this.startValue = startValue;
      this.value = startValue;
   }

   public synchronized int nextValue() {
      int valueNow = this.value++;
      return valueNow;
   }

   public synchronized void reset() {
      this.value = this.startValue;
   }

   public int getValue() {
      return this.value;
   }
}
