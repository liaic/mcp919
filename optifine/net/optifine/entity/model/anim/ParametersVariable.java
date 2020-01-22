package net.optifine.entity.model.anim;

import java.util.ArrayList;
import java.util.Arrays;
import net.optifine.entity.model.anim.ExpressionType;
import net.optifine.entity.model.anim.IExpression;
import net.optifine.entity.model.anim.IParameters;

public class ParametersVariable implements IParameters {

   public ExpressionType[] first;
   public ExpressionType[] repeat;
   public ExpressionType[] last;
   public int maxCount;
   public static final ExpressionType[] EMPTY = new ExpressionType[0];


   public ParametersVariable() {
      this((ExpressionType[])null, (ExpressionType[])null, (ExpressionType[])null);
   }

   public ParametersVariable(ExpressionType[] first, ExpressionType[] repeat, ExpressionType[] last) {
      this(first, repeat, last, Integer.MAX_VALUE);
   }

   public ParametersVariable(ExpressionType[] first, ExpressionType[] repeat, ExpressionType[] last, int maxCount) {
      this.maxCount = Integer.MAX_VALUE;
      this.first = normalize(first);
      this.repeat = normalize(repeat);
      this.last = normalize(last);
      this.maxCount = maxCount;
   }

   public static ExpressionType[] normalize(ExpressionType[] exprs) {
      return exprs == null?EMPTY:exprs;
   }

   public ExpressionType[] getFirst() {
      return this.first;
   }

   public ExpressionType[] getRepeat() {
      return this.repeat;
   }

   public ExpressionType[] getLast() {
      return this.last;
   }

   public int getCountRepeat() {
      return this.first == null?0:this.first.length;
   }

   public ExpressionType[] getParameterTypes(IExpression[] arguments) {
      int countFixedParams = this.first.length + this.last.length;
      int countVarArgs = arguments.length - countFixedParams;
      int countRepeat = 0;

      for(int countVarParams = 0; countVarParams + this.repeat.length <= countVarArgs && countFixedParams + countVarParams + this.repeat.length <= this.maxCount; countVarParams += this.repeat.length) {
         ++countRepeat;
      }

      ArrayList list = new ArrayList();
      list.addAll(Arrays.asList(this.first));

      for(int ets = 0; ets < countRepeat; ++ets) {
         list.addAll(Arrays.asList(this.repeat));
      }

      list.addAll(Arrays.asList(this.last));
      ExpressionType[] var8 = (ExpressionType[])list.toArray(new ExpressionType[list.size()]);
      return var8;
   }

   public ParametersVariable first(ExpressionType ... first) {
      return new ParametersVariable(first, this.repeat, this.last);
   }

   public ParametersVariable repeat(ExpressionType ... repeat) {
      return new ParametersVariable(this.first, repeat, this.last);
   }

   public ParametersVariable last(ExpressionType ... last) {
      return new ParametersVariable(this.first, this.repeat, last);
   }

   public ParametersVariable maxCount(int maxCount) {
      return new ParametersVariable(this.first, this.repeat, this.last, maxCount);
   }

}
