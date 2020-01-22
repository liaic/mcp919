package net.optifine.entity.model.anim;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.MathHelper;
import net.optifine.entity.model.anim.ExpressionType;
import net.optifine.entity.model.anim.IExpression;
import net.optifine.entity.model.anim.IExpressionBool;
import net.optifine.entity.model.anim.IExpressionFloat;
import net.optifine.entity.model.anim.IParameters;
import net.optifine.entity.model.anim.Parameters;
import net.optifine.entity.model.anim.ParametersVariable;
import optifine.Config;
import optifine.MathUtils;
import shadersmod.uniform.Smoother;

public enum FunctionType {

   PLUS("PLUS", 0, 10, ExpressionType.FLOAT, "+", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   MINUS("MINUS", 1, 10, ExpressionType.FLOAT, "-", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   MUL("MUL", 2, 11, ExpressionType.FLOAT, "*", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   DIV("DIV", 3, 11, ExpressionType.FLOAT, "/", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   MOD("MOD", 4, 11, ExpressionType.FLOAT, "%", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   NEG("NEG", 5, 12, ExpressionType.FLOAT, "neg", new ExpressionType[]{ExpressionType.FLOAT}),
   PI("PI", 6, ExpressionType.FLOAT, "pi", new ExpressionType[0]),
   SIN("SIN", 7, ExpressionType.FLOAT, "sin", new ExpressionType[]{ExpressionType.FLOAT}),
   COS("COS", 8, ExpressionType.FLOAT, "cos", new ExpressionType[]{ExpressionType.FLOAT}),
   ASIN("ASIN", 9, ExpressionType.FLOAT, "asin", new ExpressionType[]{ExpressionType.FLOAT}),
   ACOS("ACOS", 10, ExpressionType.FLOAT, "acos", new ExpressionType[]{ExpressionType.FLOAT}),
   TAN("TAN", 11, ExpressionType.FLOAT, "tan", new ExpressionType[]{ExpressionType.FLOAT}),
   ATAN("ATAN", 12, ExpressionType.FLOAT, "atan", new ExpressionType[]{ExpressionType.FLOAT}),
   ATAN2("ATAN2", 13, ExpressionType.FLOAT, "atan2", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   TORAD("TORAD", 14, ExpressionType.FLOAT, "torad", new ExpressionType[]{ExpressionType.FLOAT}),
   TODEG("TODEG", 15, ExpressionType.FLOAT, "todeg", new ExpressionType[]{ExpressionType.FLOAT}),
   MIN("MIN", 16, ExpressionType.FLOAT, "min", (new ParametersVariable()).first(new ExpressionType[]{ExpressionType.FLOAT}).repeat(new ExpressionType[]{ExpressionType.FLOAT})),
   MAX("MAX", 17, ExpressionType.FLOAT, "max", (new ParametersVariable()).first(new ExpressionType[]{ExpressionType.FLOAT}).repeat(new ExpressionType[]{ExpressionType.FLOAT})),
   CLAMP("CLAMP", 18, ExpressionType.FLOAT, "clamp", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT, ExpressionType.FLOAT}),
   ABS("ABS", 19, ExpressionType.FLOAT, "abs", new ExpressionType[]{ExpressionType.FLOAT}),
   FLOOR("FLOOR", 20, ExpressionType.FLOAT, "floor", new ExpressionType[]{ExpressionType.FLOAT}),
   CEIL("CEIL", 21, ExpressionType.FLOAT, "ceil", new ExpressionType[]{ExpressionType.FLOAT}),
   EXP("EXP", 22, ExpressionType.FLOAT, "exp", new ExpressionType[]{ExpressionType.FLOAT}),
   FRAC("FRAC", 23, ExpressionType.FLOAT, "frac", new ExpressionType[]{ExpressionType.FLOAT}),
   LOG("LOG", 24, ExpressionType.FLOAT, "log", new ExpressionType[]{ExpressionType.FLOAT}),
   POW("POW", 25, ExpressionType.FLOAT, "pow", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   RANDOM("RANDOM", 26, ExpressionType.FLOAT, "random", new ExpressionType[0]),
   ROUND("ROUND", 27, ExpressionType.FLOAT, "round", new ExpressionType[]{ExpressionType.FLOAT}),
   SIGNUM("SIGNUM", 28, ExpressionType.FLOAT, "signum", new ExpressionType[]{ExpressionType.FLOAT}),
   SQRT("SQRT", 29, ExpressionType.FLOAT, "sqrt", new ExpressionType[]{ExpressionType.FLOAT}),
   FMOD("FMOD", 30, ExpressionType.FLOAT, "fmod", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   TIME("TIME", 31, ExpressionType.FLOAT, "time", new ExpressionType[0]),
   IF("IF", 32, ExpressionType.FLOAT, "if", (new ParametersVariable()).first(new ExpressionType[]{ExpressionType.BOOL, ExpressionType.FLOAT}).repeat(new ExpressionType[]{ExpressionType.BOOL, ExpressionType.FLOAT}).last(new ExpressionType[]{ExpressionType.FLOAT})),
   NOT("NOT", 33, 12, ExpressionType.BOOL, "!", new ExpressionType[]{ExpressionType.BOOL}),
   AND("AND", 34, 3, ExpressionType.BOOL, "&&", new ExpressionType[]{ExpressionType.BOOL, ExpressionType.BOOL}),
   OR("OR", 35, 2, ExpressionType.BOOL, "||", new ExpressionType[]{ExpressionType.BOOL, ExpressionType.BOOL}),
   GREATER("GREATER", 36, 8, ExpressionType.BOOL, ">", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   GREATER_OR_EQUAL("GREATER_OR_EQUAL", 37, 8, ExpressionType.BOOL, ">=", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   SMALLER("SMALLER", 38, 8, ExpressionType.BOOL, "<", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   SMALLER_OR_EQUAL("SMALLER_OR_EQUAL", 39, 8, ExpressionType.BOOL, "<=", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   EQUAL("EQUAL", 40, 7, ExpressionType.BOOL, "==", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   NOT_EQUAL("NOT_EQUAL", 41, 7, ExpressionType.BOOL, "!=", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}),
   BETWEEN("BETWEEN", 42, 7, ExpressionType.BOOL, "between", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT, ExpressionType.FLOAT}),
   EQUALS("EQUALS", 43, 7, ExpressionType.BOOL, "equals", new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT, ExpressionType.FLOAT}),
   IN("IN", 44, ExpressionType.BOOL, "in", (new ParametersVariable()).first(new ExpressionType[]{ExpressionType.FLOAT}).repeat(new ExpressionType[]{ExpressionType.FLOAT}).last(new ExpressionType[]{ExpressionType.FLOAT})),
   SMOOTH("SMOOTH", 45, ExpressionType.FLOAT, "smooth", (new ParametersVariable()).first(new ExpressionType[]{ExpressionType.FLOAT, ExpressionType.FLOAT}).repeat(new ExpressionType[]{ExpressionType.FLOAT}).maxCount(4)),
   TRUE("TRUE", 46, ExpressionType.BOOL, "true", new ExpressionType[0]),
   FALSE("FALSE", 47, ExpressionType.BOOL, "false", new ExpressionType[0]);
   public int precedence;
   public ExpressionType expressionType;
   public String name;
   public IParameters parameters;
   public static FunctionType[] VALUES = values();
   public static final Map<Integer, Float> mapSmooth = new HashMap();
   // $FF: synthetic field
   public static final FunctionType[] $VALUES = new FunctionType[]{PLUS, MINUS, MUL, DIV, MOD, NEG, PI, SIN, COS, ASIN, ACOS, TAN, ATAN, ATAN2, TORAD, TODEG, MIN, MAX, CLAMP, ABS, FLOOR, CEIL, EXP, FRAC, LOG, POW, RANDOM, ROUND, SIGNUM, SQRT, FMOD, TIME, IF, NOT, AND, OR, GREATER, GREATER_OR_EQUAL, SMALLER, SMALLER_OR_EQUAL, EQUAL, NOT_EQUAL, BETWEEN, EQUALS, IN, SMOOTH, TRUE, FALSE};


   public FunctionType(String var1, int var2, ExpressionType expressionType, String name, ExpressionType ... parameterTypes) {
      this(var1, var2, 0, expressionType, name, parameterTypes);
   }

   public FunctionType(String var1, int var2, int precedence, ExpressionType expressionType, String name, ExpressionType ... parameterTypes) {
      this(var1, var2, precedence, expressionType, name, (IParameters)(new Parameters(parameterTypes)));
   }

   public FunctionType(String var1, int var2, ExpressionType expressionType, String name, IParameters parameters) {
      this(var1, var2, 0, expressionType, name, parameters);
   }

   public FunctionType(String var1, int var2, int precedence, ExpressionType expressionType, String name, IParameters parameters) {
      this.precedence = precedence;
      this.expressionType = expressionType;
      this.name = name;
      this.parameters = parameters;
   }

   public String getName() {
      return this.name;
   }

   public int getPrecedence() {
      return this.precedence;
   }

   public ExpressionType getExpressionType() {
      return this.expressionType;
   }

   public IParameters getParameters() {
      return this.parameters;
   }

   public int getParameterCount(IExpression[] arguments) {
      return this.parameters.getParameterTypes(arguments).length;
   }

   public ExpressionType[] getParameterTypes(IExpression[] arguments) {
      return this.parameters.getParameterTypes(arguments);
   }

   public float evalFloat(IExpression[] args) {
      int id;
      switch(FunctionType.NamelessClass409737672.$SwitchMap$net$optifine$entity$model$anim$FunctionType[this.ordinal()]) {
      case 1:
         return evalFloat(args, 0) + evalFloat(args, 1);
      case 2:
         return evalFloat(args, 0) - evalFloat(args, 1);
      case 3:
         return evalFloat(args, 0) * evalFloat(args, 1);
      case 4:
         return evalFloat(args, 0) / evalFloat(args, 1);
      case 5:
         float modX = evalFloat(args, 0);
         float modY = evalFloat(args, 1);
         return modX - modY * (float)((int)(modX / modY));
      case 6:
         return -evalFloat(args, 0);
      case 7:
         return 3.1415927F;
      case 8:
         return MathHelper.sin(evalFloat(args, 0));
      case 9:
         return MathHelper.cos(evalFloat(args, 0));
      case 10:
         return (float)Math.asin((double)evalFloat(args, 0));
      case 11:
         return (float)Math.acos((double)evalFloat(args, 0));
      case 12:
         return (float)Math.tan((double)evalFloat(args, 0));
      case 13:
         return (float)Math.atan((double)evalFloat(args, 0));
      case 14:
         return (float)MathHelper.atan2((double)evalFloat(args, 0), (double)evalFloat(args, 1));
      case 15:
         return MathUtils.toRad(evalFloat(args, 0));
      case 16:
         return MathUtils.toDeg(evalFloat(args, 0));
      case 17:
         return this.getMin(args);
      case 18:
         return this.getMax(args);
      case 19:
         return MathHelper.clamp_float(evalFloat(args, 0), evalFloat(args, 1), evalFloat(args, 2));
      case 20:
         return MathHelper.abs(evalFloat(args, 0));
      case 21:
         return (float)Math.exp((double)evalFloat(args, 0));
      case 22:
         return (float)MathHelper.floor_float(evalFloat(args, 0));
      case 23:
         return (float)MathHelper.ceiling_float_int(evalFloat(args, 0));
      case 24:
         return (float)MathHelper.func_181162_h((double)evalFloat(args, 0));
      case 25:
         return (float)Math.log((double)evalFloat(args, 0));
      case 26:
         return (float)Math.pow((double)evalFloat(args, 0), (double)evalFloat(args, 1));
      case 27:
         return (float)Math.random();
      case 28:
         return (float)Math.round(evalFloat(args, 0));
      case 29:
         return Math.signum(evalFloat(args, 0));
      case 30:
         return MathHelper.sqrt_float(evalFloat(args, 0));
      case 31:
         float fmodX = evalFloat(args, 0);
         float fmodY = evalFloat(args, 1);
         return fmodX - fmodY * (float)MathHelper.floor_float(fmodX / fmodY);
      case 32:
         Minecraft mc = Minecraft.getMinecraft();
         WorldClient world = mc.theWorld;
         if(world == null) {
            return 0.0F;
         }

         return (float)(world.getTotalWorldTime() % 24000L) + Config.renderPartialTicks;
      case 33:
         int countChecks = (args.length - 1) / 2;

         for(id = 0; id < countChecks; ++id) {
            int var14 = id * 2;
            if(evalBool(args, var14)) {
               return evalFloat(args, var14 + 1);
            }
         }

         return evalFloat(args, countChecks * 2);
      case 34:
         id = (int)evalFloat(args, 0);
         float valRaw = evalFloat(args, 1);
         float valFadeUp = args.length > 2?evalFloat(args, 2):1.0F;
         float valFadeDown = args.length > 3?evalFloat(args, 3):valFadeUp;
         float valSmooth = Smoother.getSmoothValue(id, valRaw, valFadeUp, valFadeDown);
         return valSmooth;
      default:
         Config.warn("Unknown function type: " + this);
         return 0.0F;
      }
   }

   public float getMin(IExpression[] exprs) {
      if(exprs.length == 2) {
         return Math.min(evalFloat(exprs, 0), evalFloat(exprs, 1));
      } else {
         float valMin = evalFloat(exprs, 0);

         for(int i = 1; i < exprs.length; ++i) {
            float valExpr = evalFloat(exprs, i);
            if(valExpr < valMin) {
               valMin = valExpr;
            }
         }

         return valMin;
      }
   }

   public float getMax(IExpression[] exprs) {
      if(exprs.length == 2) {
         return Math.max(evalFloat(exprs, 0), evalFloat(exprs, 1));
      } else {
         float valMax = evalFloat(exprs, 0);

         for(int i = 1; i < exprs.length; ++i) {
            float valExpr = evalFloat(exprs, i);
            if(valExpr > valMax) {
               valMax = valExpr;
            }
         }

         return valMax;
      }
   }

   public static float evalFloat(IExpression[] exprs, int index) {
      IExpressionFloat ef = (IExpressionFloat)exprs[index];
      float val = ef.eval();
      return val;
   }

   public boolean evalBool(IExpression[] args) {
      switch(FunctionType.NamelessClass409737672.$SwitchMap$net$optifine$entity$model$anim$FunctionType[this.ordinal()]) {
      case 35:
         return true;
      case 36:
         return false;
      case 37:
         return !evalBool(args, 0);
      case 38:
         return evalBool(args, 0) && evalBool(args, 1);
      case 39:
         return evalBool(args, 0) || evalBool(args, 1);
      case 40:
         return evalFloat(args, 0) > evalFloat(args, 1);
      case 41:
         return evalFloat(args, 0) >= evalFloat(args, 1);
      case 42:
         return evalFloat(args, 0) < evalFloat(args, 1);
      case 43:
         return evalFloat(args, 0) <= evalFloat(args, 1);
      case 44:
         return evalFloat(args, 0) == evalFloat(args, 1);
      case 45:
         return evalFloat(args, 0) != evalFloat(args, 1);
      case 46:
         float val = evalFloat(args, 0);
         return val >= evalFloat(args, 1) && val <= evalFloat(args, 2);
      case 47:
         float diff = evalFloat(args, 0) - evalFloat(args, 1);
         float delta = evalFloat(args, 2);
         return Math.abs(diff) <= delta;
      case 48:
         float valIn = evalFloat(args, 0);

         for(int i = 1; i < args.length; ++i) {
            float valCheck = evalFloat(args, i);
            if(valIn == valCheck) {
               return true;
            }
         }

         return false;
      default:
         Config.warn("Unknown function type: " + this);
         return false;
      }
   }

   public static boolean evalBool(IExpression[] exprs, int index) {
      IExpressionBool eb = (IExpressionBool)exprs[index];
      boolean val = eb.eval();
      return val;
   }

   public static FunctionType parse(String str) {
      for(int i = 0; i < VALUES.length; ++i) {
         FunctionType ef = VALUES[i];
         if(ef.getName().equals(str)) {
            return ef;
         }
      }

      return null;
   }


   // $FF: synthetic class
   public static class NamelessClass409737672 {

      // $FF: synthetic field
      public static final int[] $SwitchMap$net$optifine$entity$model$anim$FunctionType = new int[FunctionType.values().length];


      static {
         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.PLUS.ordinal()] = 1;
         } catch (NoSuchFieldError var48) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.MINUS.ordinal()] = 2;
         } catch (NoSuchFieldError var47) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.MUL.ordinal()] = 3;
         } catch (NoSuchFieldError var46) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.DIV.ordinal()] = 4;
         } catch (NoSuchFieldError var45) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.MOD.ordinal()] = 5;
         } catch (NoSuchFieldError var44) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.NEG.ordinal()] = 6;
         } catch (NoSuchFieldError var43) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.PI.ordinal()] = 7;
         } catch (NoSuchFieldError var42) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.SIN.ordinal()] = 8;
         } catch (NoSuchFieldError var41) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.COS.ordinal()] = 9;
         } catch (NoSuchFieldError var40) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.ASIN.ordinal()] = 10;
         } catch (NoSuchFieldError var39) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.ACOS.ordinal()] = 11;
         } catch (NoSuchFieldError var38) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.TAN.ordinal()] = 12;
         } catch (NoSuchFieldError var37) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.ATAN.ordinal()] = 13;
         } catch (NoSuchFieldError var36) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.ATAN2.ordinal()] = 14;
         } catch (NoSuchFieldError var35) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.TORAD.ordinal()] = 15;
         } catch (NoSuchFieldError var34) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.TODEG.ordinal()] = 16;
         } catch (NoSuchFieldError var33) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.MIN.ordinal()] = 17;
         } catch (NoSuchFieldError var32) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.MAX.ordinal()] = 18;
         } catch (NoSuchFieldError var31) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.CLAMP.ordinal()] = 19;
         } catch (NoSuchFieldError var30) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.ABS.ordinal()] = 20;
         } catch (NoSuchFieldError var29) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.EXP.ordinal()] = 21;
         } catch (NoSuchFieldError var28) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.FLOOR.ordinal()] = 22;
         } catch (NoSuchFieldError var27) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.CEIL.ordinal()] = 23;
         } catch (NoSuchFieldError var26) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.FRAC.ordinal()] = 24;
         } catch (NoSuchFieldError var25) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.LOG.ordinal()] = 25;
         } catch (NoSuchFieldError var24) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.POW.ordinal()] = 26;
         } catch (NoSuchFieldError var23) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.RANDOM.ordinal()] = 27;
         } catch (NoSuchFieldError var22) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.ROUND.ordinal()] = 28;
         } catch (NoSuchFieldError var21) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.SIGNUM.ordinal()] = 29;
         } catch (NoSuchFieldError var20) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.SQRT.ordinal()] = 30;
         } catch (NoSuchFieldError var19) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.FMOD.ordinal()] = 31;
         } catch (NoSuchFieldError var18) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.TIME.ordinal()] = 32;
         } catch (NoSuchFieldError var17) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.IF.ordinal()] = 33;
         } catch (NoSuchFieldError var16) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.SMOOTH.ordinal()] = 34;
         } catch (NoSuchFieldError var15) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.TRUE.ordinal()] = 35;
         } catch (NoSuchFieldError var14) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.FALSE.ordinal()] = 36;
         } catch (NoSuchFieldError var13) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.NOT.ordinal()] = 37;
         } catch (NoSuchFieldError var12) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.AND.ordinal()] = 38;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.OR.ordinal()] = 39;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.GREATER.ordinal()] = 40;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.GREATER_OR_EQUAL.ordinal()] = 41;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.SMALLER.ordinal()] = 42;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.SMALLER_OR_EQUAL.ordinal()] = 43;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.EQUAL.ordinal()] = 44;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.NOT_EQUAL.ordinal()] = 45;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.BETWEEN.ordinal()] = 46;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.EQUALS.ordinal()] = 47;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$net$optifine$entity$model$anim$FunctionType[FunctionType.IN.ordinal()] = 48;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }
}
