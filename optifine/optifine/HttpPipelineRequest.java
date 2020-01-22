package optifine;

import optifine.HttpListener;
import optifine.HttpRequest;

public class HttpPipelineRequest {

   public HttpRequest httpRequest = null;
   public HttpListener httpListener = null;
   public boolean closed = false;


   public HttpPipelineRequest(HttpRequest httpRequest, HttpListener httpListener) {
      this.httpRequest = httpRequest;
      this.httpListener = httpListener;
   }

   public HttpRequest getHttpRequest() {
      return this.httpRequest;
   }

   public HttpListener getHttpListener() {
      return this.httpListener;
   }

   public boolean isClosed() {
      return this.closed;
   }

   public void setClosed(boolean closed) {
      this.closed = closed;
   }
}
