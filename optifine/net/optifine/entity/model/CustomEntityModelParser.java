package net.optifine.entity.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.optifine.entity.model.CustomEntityModel;
import net.optifine.entity.model.CustomEntityRenderer;
import net.optifine.entity.model.CustomModelRenderer;
import net.optifine.entity.model.anim.ModelUpdater;
import net.optifine.entity.model.anim.ModelVariableUpdater;
import optifine.Config;
import optifine.ConnectedParser;
import optifine.Json;
import optifine.PlayerItemParser;

public class CustomEntityModelParser {

   public static final String ENTITY = "entity";
   public static final String TEXTURE = "texture";
   public static final String SHADOW_SIZE = "shadowSize";
   public static final String ITEM_TYPE = "type";
   public static final String ITEM_TEXTURE_SIZE = "textureSize";
   public static final String ITEM_USE_PLAYER_TEXTURE = "usePlayerTexture";
   public static final String ITEM_MODELS = "models";
   public static final String ITEM_ANIMATIONS = "animations";
   public static final String MODEL_ID = "id";
   public static final String MODEL_BASE_ID = "baseId";
   public static final String MODEL_MODEL = "model";
   public static final String MODEL_TYPE = "type";
   public static final String MODEL_PART = "part";
   public static final String MODEL_ATTACH = "attach";
   public static final String MODEL_INVERT_AXIS = "invertAxis";
   public static final String MODEL_MIRROR_TEXTURE = "mirrorTexture";
   public static final String MODEL_TRANSLATE = "translate";
   public static final String MODEL_ROTATE = "rotate";
   public static final String MODEL_SCALE = "scale";
   public static final String MODEL_BOXES = "boxes";
   public static final String MODEL_SPRITES = "sprites";
   public static final String MODEL_SUBMODEL = "submodel";
   public static final String MODEL_SUBMODELS = "submodels";
   public static final String BOX_TEXTURE_OFFSET = "textureOffset";
   public static final String BOX_COORDINATES = "coordinates";
   public static final String BOX_SIZE_ADD = "sizeAdd";
   public static final String ENTITY_MODEL = "EntityModel";
   public static final String ENTITY_MODEL_PART = "EntityModelPart";


   public static CustomEntityRenderer parseEntityRender(JsonObject obj, String path) {
      ConnectedParser cp = new ConnectedParser("CustomEntityModels");
      String name = cp.parseName(path);
      String basePath = cp.parseBasePath(path);
      String texture = Json.getString(obj, "texture");
      int[] textureSize = Json.parseIntArray(obj.get("textureSize"), 2);
      float shadowSize = Json.getFloat(obj, "shadowSize", -1.0F);
      JsonArray models = (JsonArray)obj.get("models");
      checkNull(models, "Missing models");
      HashMap mapModelJsons = new HashMap();
      ArrayList listModels = new ArrayList();

      for(int modelRenderers = 0; modelRenderers < models.size(); ++modelRenderers) {
         JsonObject textureLocation = (JsonObject)models.get(modelRenderers);
         processBaseId(textureLocation, mapModelJsons);
         processExternalModel(textureLocation, mapModelJsons, basePath);
         processId(textureLocation, mapModelJsons);
         CustomModelRenderer cer = parseCustomModelRenderer(textureLocation, textureSize, basePath);
         if(cer != null) {
            listModels.add(cer);
         }
      }

      CustomModelRenderer[] var14 = (CustomModelRenderer[])((CustomModelRenderer[])listModels.toArray(new CustomModelRenderer[listModels.size()]));
      ResourceLocation var15 = null;
      if(texture != null) {
         var15 = getResourceLocation(basePath, texture, ".png");
      }

      CustomEntityRenderer var16 = new CustomEntityRenderer(name, basePath, var15, var14, shadowSize);
      return var16;
   }

   public static void processBaseId(JsonObject elem, Map mapModelJsons) {
      String baseId = Json.getString(elem, "baseId");
      if(baseId != null) {
         JsonObject baseObj = (JsonObject)mapModelJsons.get(baseId);
         if(baseObj == null) {
            Config.warn("BaseID not found: " + baseId);
         } else {
            copyJsonElements(baseObj, elem);
         }
      }
   }

   public static void processExternalModel(JsonObject elem, Map mapModelJsons, String basePath) {
      String modelPath = Json.getString(elem, "model");
      if(modelPath != null) {
         ResourceLocation locJson = getResourceLocation(basePath, modelPath, ".jpm");

         try {
            JsonObject e = loadJson(locJson);
            if(e == null) {
               Config.warn("Model not found: " + locJson);
               return;
            }

            copyJsonElements(e, elem);
         } catch (IOException var6) {
            Config.error("" + var6.getClass().getName() + ": " + var6.getMessage());
         } catch (JsonParseException var7) {
            Config.error("" + var7.getClass().getName() + ": " + var7.getMessage());
         } catch (Exception var8) {
            var8.printStackTrace();
         }

      }
   }

   public static void copyJsonElements(JsonObject objFrom, JsonObject objTo) {
      Set setEntries = objFrom.entrySet();
      Iterator iterator = setEntries.iterator();

      while(iterator.hasNext()) {
         Entry entry = (Entry)iterator.next();
         if(!((String)entry.getKey()).equals("id") && !objTo.has((String)entry.getKey())) {
            objTo.add((String)entry.getKey(), (JsonElement)entry.getValue());
         }
      }

   }

   public static ResourceLocation getResourceLocation(String basePath, String path, String extension) {
      if(!path.endsWith(extension)) {
         path = path + extension;
      }

      if(!path.contains("/")) {
         path = basePath + "/" + path;
      } else if(path.startsWith("./")) {
         path = basePath + "/" + path.substring(2);
      } else if(path.startsWith("~/")) {
         path = "optifine/" + path.substring(2);
      }

      return new ResourceLocation(path);
   }

   public static void processId(JsonObject elem, Map mapModelJsons) {
      String id = Json.getString(elem, "id");
      if(id != null) {
         if(id.length() < 1) {
            Config.warn("Empty model ID: " + id);
         } else if(mapModelJsons.containsKey(id)) {
            Config.warn("Duplicate model ID: " + id);
         } else {
            mapModelJsons.put(id, elem);
         }
      }
   }

   public static CustomModelRenderer parseCustomModelRenderer(JsonObject elem, int[] textureSize, String basePath) {
      String modelPart = Json.getString(elem, "part");
      checkNull(modelPart, "Model part not specified, missing \"replace\" or \"attachTo\".");
      boolean attach = Json.getBoolean(elem, "attach", false);
      CustomEntityModel modelBase = new CustomEntityModel();
      if(textureSize != null) {
         modelBase.textureWidth = textureSize[0];
         modelBase.textureHeight = textureSize[1];
      }

      ModelUpdater mu = null;
      JsonArray animations = (JsonArray)elem.get("animations");
      if(animations != null) {
         ArrayList mr = new ArrayList();

         for(int cmr = 0; cmr < animations.size(); ++cmr) {
            JsonObject anim = (JsonObject)animations.get(cmr);
            Set entries = anim.entrySet();
            Iterator it = entries.iterator();

            while(it.hasNext()) {
               Entry entry = (Entry)it.next();
               String key = (String)entry.getKey();
               String val = ((JsonElement)entry.getValue()).getAsString();
               ModelVariableUpdater mvu = new ModelVariableUpdater(key, val);
               mr.add(mvu);
            }
         }

         if(mr.size() > 0) {
            ModelVariableUpdater[] var18 = (ModelVariableUpdater[])((ModelVariableUpdater[])mr.toArray(new ModelVariableUpdater[mr.size()]));
            mu = new ModelUpdater(var18);
         }
      }

      ModelRenderer var17 = PlayerItemParser.parseModelRenderer(elem, modelBase, textureSize, basePath);
      CustomModelRenderer var19 = new CustomModelRenderer(modelPart, attach, var17, mu);
      return var19;
   }

   public static void checkNull(Object obj, String msg) {
      if(obj == null) {
         throw new JsonParseException(msg);
      }
   }

   public static JsonObject loadJson(ResourceLocation location) throws IOException, JsonParseException {
      InputStream in = Config.getResourceStream(location);
      if(in == null) {
         return null;
      } else {
         String jsonStr = Config.readInputStream(in, "ASCII");
         in.close();
         JsonParser jp = new JsonParser();
         JsonObject jo = (JsonObject)jp.parse(jsonStr);
         return jo;
      }
   }
}
