package net.optifine.entity.model.anim;


public enum TokenType {

   IDENTIFIER("IDENTIFIER", 0, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_:."),
   NUMBER("NUMBER", 1, "0123456789", "0123456789."),
   OPERATOR("OPERATOR", 2, "+-*/%!&|<>=", "&|="),
   COMMA("COMMA", 3, ","),
   BRACKET_OPEN("BRACKET_OPEN", 4, "("),
   BRACKET_CLOSE("BRACKET_CLOSE", 5, ")");
   public String charsFirst;
   public String charsNext;
   public static final TokenType[] VALUES = values();
   // $FF: synthetic field
   public static final TokenType[] $VALUES = new TokenType[]{IDENTIFIER, NUMBER, OPERATOR, COMMA, BRACKET_OPEN, BRACKET_CLOSE};


   public TokenType(String var1, int var2, String charsFirst) {
      this(var1, var2, charsFirst, "");
   }

   public TokenType(String var1, int var2, String charsFirst, String charsNext) {
      this.charsFirst = charsFirst;
      this.charsNext = charsNext;
   }

   public String getCharsFirst() {
      return this.charsFirst;
   }

   public String getCharsNext() {
      return this.charsNext;
   }

   public static TokenType getTypeByFirstChar(char ch) {
      for(int i = 0; i < VALUES.length; ++i) {
         TokenType type = VALUES[i];
         if(type.getCharsFirst().indexOf(ch) >= 0) {
            return type;
         }
      }

      return null;
   }

   public boolean hasCharNext(char ch) {
      return this.charsNext.indexOf(ch) >= 0;
   }


   public static class Const {

      public static final String ALPHAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
      public static final String DIGITS = "0123456789";


   }
}
