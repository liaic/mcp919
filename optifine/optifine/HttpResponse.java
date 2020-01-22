package optifine;

import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse {

   public int status = 0;
   public String statusLine = null;
   public Map<String, String> headers = new LinkedHashMap();
   public byte[] body = null;


   public HttpResponse(int status, String statusLine, Map headers, byte[] body) {
      this.status = status;
      this.statusLine = statusLine;
      this.headers = headers;
      this.body = body;
   }

   public int getStatus() {
      return this.status;
   }

   public String getStatusLine() {
      return this.statusLine;
   }

   public Map getHeaders() {
      return this.headers;
   }

   public String getHeader(String key) {
      return (String)this.headers.get(key);
   }

   public byte[] getBody() {
      return this.body;
   }
}
