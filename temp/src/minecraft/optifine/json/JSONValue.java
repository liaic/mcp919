package optifine.json;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import optifine.json.JSONArray;
import optifine.json.JSONAware;
import optifine.json.JSONObject;
import optifine.json.JSONParser;
import optifine.json.JSONStreamAware;
import optifine.json.ParseException;

public class JSONValue {
   public static Object parse(Reader in) {
      try {
         JSONParser jsonparser = new JSONParser();
         return jsonparser.parse(in);
      } catch (Exception var2) {
         return null;
      }
   }

   public static Object parse(String s) {
      StringReader stringreader = new StringReader(s);
      return parse((Reader)stringreader);
   }

   public static Object parseWithException(Reader in) throws IOException, ParseException {
      JSONParser jsonparser = new JSONParser();
      return jsonparser.parse(in);
   }

   public static Object parseWithException(String s) throws ParseException {
      JSONParser jsonparser = new JSONParser();
      return jsonparser.parse(s);
   }

   public static void writeJSONString(Object value, Writer out) throws IOException {
      if(value == null) {
         out.write("null");
      } else if(value instanceof String) {
         out.write(34);
         out.write(escape((String)value));
         out.write(34);
      } else if(value instanceof Double) {
         if(!((Double)value).isInfinite() && !((Double)value).isNaN()) {
            out.write(value.toString());
         } else {
            out.write("null");
         }

      } else if(!(value instanceof Float)) {
         if(value instanceof Number) {
            out.write(value.toString());
         } else if(value instanceof Boolean) {
            out.write(value.toString());
         } else if(value instanceof JSONStreamAware) {
            ((JSONStreamAware)value).writeJSONString(out);
         } else if(value instanceof JSONAware) {
            out.write(((JSONAware)value).toJSONString());
         } else if(value instanceof Map) {
            JSONObject.writeJSONString((Map)value, out);
         } else if(value instanceof List) {
            JSONArray.writeJSONString((List)value, out);
         } else {
            out.write(value.toString());
         }
      } else {
         if(!((Float)value).isInfinite() && !((Float)value).isNaN()) {
            out.write(value.toString());
         } else {
            out.write("null");
         }

      }
   }

   public static String toJSONString(Object value) {
      return value == null?"null":(value instanceof String?"\"" + escape((String)value) + "\"":(value instanceof Double?(!((Double)value).isInfinite() && !((Double)value).isNaN()?value.toString():"null"):(value instanceof Float?(!((Float)value).isInfinite() && !((Float)value).isNaN()?value.toString():"null"):(value instanceof Number?value.toString():(value instanceof Boolean?value.toString():(value instanceof JSONAware?((JSONAware)value).toJSONString():(value instanceof Map?JSONObject.toJSONString((Map)value):(value instanceof List?JSONArray.toJSONString((List)value):value.toString()))))))));
   }

   public static String escape(String s) {
      if(s == null) {
         return null;
      } else {
         StringBuffer stringbuffer = new StringBuffer();
         escape(s, stringbuffer);
         return stringbuffer.toString();
      }
   }

   static void escape(String s, StringBuffer sb) {
      for(int i = 0; i < s.length(); ++i) {
         char c0 = s.charAt(i);
         switch(c0) {
         case '\b':
            sb.append("\\b");
            continue;
         case '\t':
            sb.append("\\t");
            continue;
         case '\n':
            sb.append("\\n");
            continue;
         case '\f':
            sb.append("\\f");
            continue;
         case '\r':
            sb.append("\\r");
            continue;
         case '\"':
            sb.append("\\\"");
            continue;
         case '\\':
            sb.append("\\\\");
            continue;
         }

         if(c0 >= 0 && c0 <= 31 || c0 >= 127 && c0 <= 159 || c0 >= 8192 && c0 <= 8447) {
            String s = Integer.toHexString(c0);
            sb.append("\\u");

            for(int j = 0; j < 4 - s.length(); ++j) {
               sb.append('0');
            }

            sb.append(s.toUpperCase());
         } else {
            sb.append(c0);
         }
      }

   }
}
