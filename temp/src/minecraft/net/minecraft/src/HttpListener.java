package net.minecraft.src;

import net.minecraft.src.HttpRequest;
import net.minecraft.src.HttpResponse;

public interface HttpListener {
   void finished(HttpRequest var1, HttpResponse var2);

   void failed(HttpRequest var1, Exception var2);
}
