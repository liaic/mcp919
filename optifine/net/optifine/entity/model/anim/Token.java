package net.optifine.entity.model.anim;

import net.optifine.entity.model.anim.TokenType;

public class Token {

   public TokenType type;
   public String text;


   public Token(TokenType type, String text) {
      this.type = type;
      this.text = text;
   }

   public TokenType getType() {
      return this.type;
   }

   public String getText() {
      return this.text;
   }

   public String toString() {
      return this.text;
   }
}
