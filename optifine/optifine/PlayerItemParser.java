package optifine;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.optifine.entity.model.CustomEntityModelParser;
import optifine.Config;
import optifine.Json;
import optifine.ModelPlayerItem;
import optifine.PlayerItemModel;
import optifine.PlayerItemRenderer;

public class PlayerItemParser {

   public static JsonParser jsonParser = new JsonParser();
   public static final String ITEM_TYPE = "type";
   public static final String ITEM_TEXTURE_SIZE = "textureSize";
   public static final String ITEM_USE_PLAYER_TEXTURE = "usePlayerTexture";
   public static final String ITEM_MODELS = "models";
   public static final String MODEL_ID = "id";
   public static final String MODEL_BASE_ID = "baseId";
   public static final String MODEL_TYPE = "type";
   public static final String MODEL_TEXTURE = "texture";
   public static final String MODEL_TEXTURE_SIZE = "textureSize";
   public static final String MODEL_ATTACH_TO = "attachTo";
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
   public static final String BOX_UV_DOWN = "uvDown";
   public static final String BOX_UV_UP = "uvUp";
   public static final String BOX_UV_NORTH = "uvNorth";
   public static final String BOX_UV_SOUTH = "uvSouth";
   public static final String BOX_UV_WEST = "uvWest";
   public static final String BOX_UV_EAST = "uvEast";
   public static final String BOX_UV_FRONT = "uvFront";
   public static final String BOX_UV_BACK = "uvBack";
   public static final String BOX_UV_LEFT = "uvLeft";
   public static final String BOX_UV_RIGHT = "uvRight";
   public static final String ITEM_TYPE_MODEL = "PlayerItem";
   public static final String MODEL_TYPE_BOX = "ModelBox";


   public static PlayerItemModel parseItemModel(JsonObject obj) {
      String type = Json.getString(obj, "type");
      if(!Config.equals(type, "PlayerItem")) {
         throw new JsonParseException("Unknown model type: " + type);
      } else {
         int[] textureSize = Json.parseIntArray(obj.get("textureSize"), 2);
         checkNull(textureSize, "Missing texture size");
         Dimension textureDim = new Dimension(textureSize[0], textureSize[1]);
         boolean usePlayerTexture = Json.getBoolean(obj, "usePlayerTexture", false);
         JsonArray models = (JsonArray)obj.get("models");
         checkNull(models, "Missing elements");
         HashMap mapModelJsons = new HashMap();
         ArrayList listModels = new ArrayList();
         new ArrayList();

         for(int modelRenderers = 0; modelRenderers < models.size(); ++modelRenderers) {
            JsonObject elem = (JsonObject)models.get(modelRenderers);
            String baseId = Json.getString(elem, "baseId");
            if(baseId != null) {
               JsonObject id = (JsonObject)mapModelJsons.get(baseId);
               if(id == null) {
                  Config.warn("BaseID not found: " + baseId);
                  continue;
               }

               Set mr = id.entrySet();
               Iterator iterator = mr.iterator();

               while(iterator.hasNext()) {
                  Entry entry = (Entry)iterator.next();
                  if(!elem.has((String)entry.getKey())) {
                     elem.add((String)entry.getKey(), (JsonElement)entry.getValue());
                  }
               }
            }

            String var17 = Json.getString(elem, "id");
            if(var17 != null) {
               if(!mapModelJsons.containsKey(var17)) {
                  mapModelJsons.put(var17, elem);
               } else {
                  Config.warn("Duplicate model ID: " + var17);
               }
            }

            PlayerItemRenderer var18 = parseItemRenderer(elem, textureDim);
            if(var18 != null) {
               listModels.add(var18);
            }
         }

         PlayerItemRenderer[] var16 = (PlayerItemRenderer[])((PlayerItemRenderer[])listModels.toArray(new PlayerItemRenderer[listModels.size()]));
         return new PlayerItemModel(textureDim, usePlayerTexture, var16);
      }
   }

   public static void checkNull(Object obj, String msg) {
      if(obj == null) {
         throw new JsonParseException(msg);
      }
   }

   public static ResourceLocation makeResourceLocation(String texture) {
      int pos = texture.indexOf(58);
      if(pos < 0) {
         return new ResourceLocation(texture);
      } else {
         String domain = texture.substring(0, pos);
         String path = texture.substring(pos + 1);
         return new ResourceLocation(domain, path);
      }
   }

   public static int parseAttachModel(String attachModelStr) {
      if(attachModelStr == null) {
         return 0;
      } else if(attachModelStr.equals("body")) {
         return 0;
      } else if(attachModelStr.equals("head")) {
         return 1;
      } else if(attachModelStr.equals("leftArm")) {
         return 2;
      } else if(attachModelStr.equals("rightArm")) {
         return 3;
      } else if(attachModelStr.equals("leftLeg")) {
         return 4;
      } else if(attachModelStr.equals("rightLeg")) {
         return 5;
      } else if(attachModelStr.equals("cape")) {
         return 6;
      } else {
         Config.warn("Unknown attachModel: " + attachModelStr);
         return 0;
      }
   }

   public static PlayerItemRenderer parseItemRenderer(JsonObject elem, Dimension textureDim) {
      String type = Json.getString(elem, "type");
      if(!Config.equals(type, "ModelBox")) {
         Config.warn("Unknown model type: " + type);
         return null;
      } else {
         String attachToStr = Json.getString(elem, "attachTo");
         int attachTo = parseAttachModel(attachToStr);
         ModelPlayerItem modelBase = new ModelPlayerItem();
         modelBase.textureWidth = textureDim.width;
         modelBase.textureHeight = textureDim.height;
         ModelRenderer mr = parseModelRenderer(elem, modelBase, (int[])null, (String)null);
         PlayerItemRenderer pir = new PlayerItemRenderer(attachTo, mr);
         return pir;
      }
   }

   public static ModelRenderer parseModelRenderer(JsonObject elem, ModelBase modelBase, int[] parentTextureSize, String basePath) {
      ModelRenderer mr = new ModelRenderer(modelBase);
      String id = Json.getString(elem, "id");
      mr.setId(id);
      float scale = Json.getFloat(elem, "scale", 1.0F);
      mr.scaleX = scale;
      mr.scaleY = scale;
      mr.scaleZ = scale;
      String texture = Json.getString(elem, "texture");
      if(texture != null) {
         mr.setTextureLocation(CustomEntityModelParser.getResourceLocation(basePath, texture, ".png"));
      }

      int[] textureSize = Json.parseIntArray(elem.get("textureSize"), 2);
      if(textureSize == null) {
         textureSize = parentTextureSize;
      }

      if(textureSize != null) {
         mr.setTextureSize(textureSize[0], textureSize[1]);
      }

      String invertAxis = Json.getString(elem, "invertAxis", "").toLowerCase();
      boolean invertX = invertAxis.contains("x");
      boolean invertY = invertAxis.contains("y");
      boolean invertZ = invertAxis.contains("z");
      float[] translate = Json.parseFloatArray(elem.get("translate"), 3, new float[3]);
      if(invertX) {
         translate[0] = -translate[0];
      }

      if(invertY) {
         translate[1] = -translate[1];
      }

      if(invertZ) {
         translate[2] = -translate[2];
      }

      float[] rotateAngles = Json.parseFloatArray(elem.get("rotate"), 3, new float[3]);

      for(int mirrorTexture = 0; mirrorTexture < rotateAngles.length; ++mirrorTexture) {
         rotateAngles[mirrorTexture] = rotateAngles[mirrorTexture] / 180.0F * 3.1415927F;
      }

      if(invertX) {
         rotateAngles[0] = -rotateAngles[0];
      }

      if(invertY) {
         rotateAngles[1] = -rotateAngles[1];
      }

      if(invertZ) {
         rotateAngles[2] = -rotateAngles[2];
      }

      mr.setRotationPoint(translate[0], translate[1], translate[2]);
      mr.rotateAngleX = rotateAngles[0];
      mr.rotateAngleY = rotateAngles[1];
      mr.rotateAngleZ = rotateAngles[2];
      String var26 = Json.getString(elem, "mirrorTexture", "").toLowerCase();
      boolean invertU = var26.contains("u");
      boolean invertV = var26.contains("v");
      if(invertU) {
         mr.mirror = true;
      }

      if(invertV) {
         mr.mirrorV = true;
      }

      JsonArray boxes = elem.getAsJsonArray("boxes");
      JsonObject submodel;
      float[] sm;
      float subMr;
      if(boxes != null) {
         for(int sprites = 0; sprites < boxes.size(); ++sprites) {
            submodel = boxes.get(sprites).getAsJsonObject();
            int[] submodels = Json.parseIntArray(submodel.get("textureOffset"), 2);
            int[][] i = parseFaceUvs(submodel);
            if(submodels == null && i == null) {
               throw new JsonParseException("Texture offset not specified");
            }

            sm = Json.parseFloatArray(submodel.get("coordinates"), 6);
            if(sm == null) {
               throw new JsonParseException("Coordinates not specified");
            }

            if(invertX) {
               sm[0] = -sm[0] - sm[3];
            }

            if(invertY) {
               sm[1] = -sm[1] - sm[4];
            }

            if(invertZ) {
               sm[2] = -sm[2] - sm[5];
            }

            subMr = Json.getFloat(submodel, "sizeAdd", 0.0F);
            if(i != null) {
               mr.addBox(i, sm[0], sm[1], sm[2], sm[3], sm[4], sm[5], subMr);
            } else {
               mr.setTextureOffset(submodels[0], submodels[1]);
               mr.addBox(sm[0], sm[1], sm[2], (int)sm[3], (int)sm[4], (int)sm[5], subMr);
            }
         }
      }

      JsonArray var27 = elem.getAsJsonArray("sprites");
      if(var27 != null) {
         for(int var28 = 0; var28 < var27.size(); ++var28) {
            JsonObject var29 = var27.get(var28).getAsJsonObject();
            int[] var32 = Json.parseIntArray(var29.get("textureOffset"), 2);
            if(var32 == null) {
               throw new JsonParseException("Texture offset not specified");
            }

            sm = Json.parseFloatArray(var29.get("coordinates"), 6);
            if(sm == null) {
               throw new JsonParseException("Coordinates not specified");
            }

            if(invertX) {
               sm[0] = -sm[0] - sm[3];
            }

            if(invertY) {
               sm[1] = -sm[1] - sm[4];
            }

            if(invertZ) {
               sm[2] = -sm[2] - sm[5];
            }

            subMr = Json.getFloat(var29, "sizeAdd", 0.0F);
            mr.setTextureOffset(var32[0], var32[1]);
            mr.addSprite(sm[0], sm[1], sm[2], (int)sm[3], (int)sm[4], (int)sm[5], subMr);
         }
      }

      submodel = (JsonObject)elem.get("submodel");
      if(submodel != null) {
         ModelRenderer var30 = parseModelRenderer(submodel, modelBase, textureSize, basePath);
         mr.addChild(var30);
      }

      JsonArray var31 = (JsonArray)elem.get("submodels");
      if(var31 != null) {
         for(int var33 = 0; var33 < var31.size(); ++var33) {
            JsonObject var34 = (JsonObject)var31.get(var33);
            ModelRenderer var35 = parseModelRenderer(var34, modelBase, textureSize, basePath);
            if(var35.getId() != null) {
               ModelRenderer subMrId = mr.getChild(var35.getId());
               if(subMrId != null) {
                  Config.warn("Duplicate model ID: " + var35.getId());
               }
            }

            mr.addChild(var35);
         }
      }

      return mr;
   }

   public static int[][] parseFaceUvs(JsonObject box) {
      int[][] uvs = new int[][]{Json.parseIntArray(box.get("uvDown"), 4), Json.parseIntArray(box.get("uvUp"), 4), Json.parseIntArray(box.get("uvNorth"), 4), Json.parseIntArray(box.get("uvSouth"), 4), Json.parseIntArray(box.get("uvWest"), 4), Json.parseIntArray(box.get("uvEast"), 4)};
      if(uvs[2] == null) {
         uvs[2] = Json.parseIntArray(box.get("uvFront"), 4);
      }

      if(uvs[3] == null) {
         uvs[3] = Json.parseIntArray(box.get("uvBack"), 4);
      }

      if(uvs[4] == null) {
         uvs[4] = Json.parseIntArray(box.get("uvLeft"), 4);
      }

      if(uvs[5] == null) {
         uvs[5] = Json.parseIntArray(box.get("uvRight"), 4);
      }

      boolean defined = false;

      for(int i = 0; i < uvs.length; ++i) {
         if(uvs[i] != null) {
            defined = true;
         }
      }

      if(!defined) {
         return (int[][])null;
      } else {
         return uvs;
      }
   }

}
