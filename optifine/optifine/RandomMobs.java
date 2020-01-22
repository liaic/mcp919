package optifine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import optifine.Config;
import optifine.RandomMobsProperties;
import optifine.Reflector;

public class RandomMobs {

   public static Map locationProperties = new HashMap();
   public static RenderGlobal renderGlobal = null;
   public static boolean initialized = false;
   public static Random random = new Random();
   public static boolean working = false;
   public static final String SUFFIX_PNG = ".png";
   public static final String SUFFIX_PROPERTIES = ".properties";
   public static final String PREFIX_TEXTURES_ENTITY = "textures/entity/";
   public static final String PREFIX_MCPATCHER_MOB = "mcpatcher/mob/";
   public static final String[] DEPENDANT_SUFFIXES = new String[]{"_armor", "_eyes", "_exploding", "_shooting", "_fur", "_eyes", "_invulnerable", "_angry", "_tame", "_collar"};


   public static void entityLoaded(Entity entity, World world) {
      if(entity instanceof EntityLiving) {
         if(world != null) {
            EntityLiving el = (EntityLiving)entity;
            el.spawnPosition = el.getPosition();
            el.spawnBiome = world.getBiomeGenForCoords(el.spawnPosition);
            UUID uuid = el.getUniqueID();
            if(el instanceof EntityVillager) {
               updateEntityVillager(uuid, (EntityVillager)el);
            }

            long uuidLow = uuid.getLeastSignificantBits();
            int id = (int)(uuidLow & 2147483647L);
            el.randomMobsId = id;
         }
      }
   }

   public static void updateEntityVillager(UUID uuid, EntityVillager ev) {
      Entity se = Config.getIntegratedServerEntity(uuid);
      if(se instanceof EntityVillager) {
         EntityVillager sev = (EntityVillager)se;
         int profSev = sev.getProfession();
         ev.setProfession(profSev);
         int careerId = Reflector.getFieldValueInt(sev, Reflector.EntityVillager_careerId, 0);
         Reflector.setFieldValueInt(ev, Reflector.EntityVillager_careerId, careerId);
         int careerLevel = Reflector.getFieldValueInt(sev, Reflector.EntityVillager_careerLevel, 0);
         Reflector.setFieldValueInt(ev, Reflector.EntityVillager_careerLevel, careerLevel);
      }
   }

   public static void worldChanged(World oldWorld, World newWorld) {
      if(newWorld != null) {
         List entityList = newWorld.getLoadedEntityList();

         for(int e = 0; e < entityList.size(); ++e) {
            Entity entity = (Entity)entityList.get(e);
            entityLoaded(entity, newWorld);
         }
      }

   }

   public static ResourceLocation getTextureLocation(ResourceLocation loc) {
      if(working) {
         return loc;
      } else {
         ResourceLocation entity;
         try {
            working = true;
            if(!initialized) {
               initialize();
            }

            if(renderGlobal != null) {
               Entity entity1 = renderGlobal.renderedEntity;
               if(!(entity1 instanceof EntityLiving)) {
                  ResourceLocation entityLiving1 = loc;
                  return entityLiving1;
               }

               EntityLiving entityLiving = (EntityLiving)entity1;
               String name = loc.getResourcePath();
               if(!name.startsWith("textures/entity/")) {
                  ResourceLocation props1 = loc;
                  return props1;
               }

               RandomMobsProperties props = getProperties(loc);
               ResourceLocation var5;
               if(props == null) {
                  var5 = loc;
                  return var5;
               }

               var5 = props.getTextureLocation(loc, entityLiving);
               return var5;
            }

            entity = loc;
         } finally {
            working = false;
         }

         return entity;
      }
   }

   public static RandomMobsProperties getProperties(ResourceLocation loc) {
      String name = loc.getResourcePath();
      RandomMobsProperties props = (RandomMobsProperties)locationProperties.get(name);
      if(props == null) {
         props = makeProperties(loc);
         locationProperties.put(name, props);
      }

      return props;
   }

   public static RandomMobsProperties makeProperties(ResourceLocation loc) {
      String path = loc.getResourcePath();
      ResourceLocation propLoc = getPropertyLocation(loc);
      if(propLoc != null) {
         RandomMobsProperties variants = parseProperties(propLoc, loc);
         if(variants != null) {
            return variants;
         }
      }

      ResourceLocation[] variants1 = getTextureVariants(loc);
      return new RandomMobsProperties(path, variants1);
   }

   public static RandomMobsProperties parseProperties(ResourceLocation propLoc, ResourceLocation resLoc) {
      try {
         String e = propLoc.getResourcePath();
         Config.dbg("RandomMobs: " + resLoc.getResourcePath() + ", properties: " + e);
         InputStream in = Config.getResourceStream(propLoc);
         if(in == null) {
            Config.warn("RandomMobs properties not found: " + e);
            return null;
         } else {
            Properties props = new Properties();
            props.load(in);
            in.close();
            RandomMobsProperties rmp = new RandomMobsProperties(props, e, resLoc);
            return !rmp.isValid(e)?null:rmp;
         }
      } catch (FileNotFoundException var6) {
         Config.warn("RandomMobs file not found: " + resLoc.getResourcePath());
         return null;
      } catch (IOException var7) {
         var7.printStackTrace();
         return null;
      }
   }

   public static ResourceLocation getPropertyLocation(ResourceLocation loc) {
      ResourceLocation locMcp = getMcpatcherLocation(loc);
      if(locMcp == null) {
         return null;
      } else {
         String domain = locMcp.getResourceDomain();
         String path = locMcp.getResourcePath();
         String pathBase = path;
         if(path.endsWith(".png")) {
            pathBase = path.substring(0, path.length() - ".png".length());
         }

         String pathProps = pathBase + ".properties";
         ResourceLocation locProps = new ResourceLocation(domain, pathProps);
         if(Config.hasResource(locProps)) {
            return locProps;
         } else {
            String pathParent = getParentPath(pathBase);
            if(pathParent == null) {
               return null;
            } else {
               ResourceLocation locParentProps = new ResourceLocation(domain, pathParent + ".properties");
               return Config.hasResource(locParentProps)?locParentProps:null;
            }
         }
      }
   }

   public static ResourceLocation getMcpatcherLocation(ResourceLocation loc) {
      String path = loc.getResourcePath();
      if(!path.startsWith("textures/entity/")) {
         return null;
      } else {
         String pathMcp = "mcpatcher/mob/" + path.substring("textures/entity/".length());
         return new ResourceLocation(loc.getResourceDomain(), pathMcp);
      }
   }

   public static ResourceLocation getLocationIndexed(ResourceLocation loc, int index) {
      if(loc == null) {
         return null;
      } else {
         String path = loc.getResourcePath();
         int pos = path.lastIndexOf(46);
         if(pos < 0) {
            return null;
         } else {
            String prefix = path.substring(0, pos);
            String suffix = path.substring(pos);
            String pathNew = prefix + index + suffix;
            ResourceLocation locNew = new ResourceLocation(loc.getResourceDomain(), pathNew);
            return locNew;
         }
      }
   }

   public static String getParentPath(String path) {
      for(int i = 0; i < DEPENDANT_SUFFIXES.length; ++i) {
         String suffix = DEPENDANT_SUFFIXES[i];
         if(path.endsWith(suffix)) {
            String pathParent = path.substring(0, path.length() - suffix.length());
            return pathParent;
         }
      }

      return null;
   }

   public static ResourceLocation[] getTextureVariants(ResourceLocation loc) {
      ArrayList list = new ArrayList();
      list.add(loc);
      ResourceLocation locMcp = getMcpatcherLocation(loc);
      if(locMcp == null) {
         return null;
      } else {
         for(int locs = 1; locs < list.size() + 10; ++locs) {
            int index = locs + 1;
            ResourceLocation locIndex = getLocationIndexed(locMcp, index);
            if(Config.hasResource(locIndex)) {
               list.add(locIndex);
            }
         }

         if(list.size() <= 1) {
            return null;
         } else {
            ResourceLocation[] var6 = (ResourceLocation[])((ResourceLocation[])list.toArray(new ResourceLocation[list.size()]));
            Config.dbg("RandomMobs: " + loc.getResourcePath() + ", variants: " + var6.length);
            return var6;
         }
      }
   }

   public static void resetTextures() {
      locationProperties.clear();
      if(Config.isRandomMobs()) {
         initialize();
      }

   }

   public static void initialize() {
      renderGlobal = Config.getRenderGlobal();
      if(renderGlobal != null) {
         initialized = true;
         ArrayList list = new ArrayList();
         list.add("bat");
         list.add("blaze");
         list.add("cat/black");
         list.add("cat/ocelot");
         list.add("cat/red");
         list.add("cat/siamese");
         list.add("chicken");
         list.add("cow/cow");
         list.add("cow/mooshroom");
         list.add("creeper/creeper");
         list.add("enderman/enderman");
         list.add("enderman/enderman_eyes");
         list.add("ghast/ghast");
         list.add("ghast/ghast_shooting");
         list.add("iron_golem");
         list.add("pig/pig");
         list.add("sheep/sheep");
         list.add("sheep/sheep_fur");
         list.add("silverfish");
         list.add("skeleton/skeleton");
         list.add("skeleton/wither_skeleton");
         list.add("slime/slime");
         list.add("slime/magmacube");
         list.add("snowman");
         list.add("spider/cave_spider");
         list.add("spider/spider");
         list.add("spider_eyes");
         list.add("squid");
         list.add("villager/villager");
         list.add("villager/butcher");
         list.add("villager/farmer");
         list.add("villager/librarian");
         list.add("villager/priest");
         list.add("villager/smith");
         list.add("wither/wither");
         list.add("wither/wither_armor");
         list.add("wither/wither_invulnerable");
         list.add("wolf/wolf");
         list.add("wolf/wolf_angry");
         list.add("wolf/wolf_collar");
         list.add("wolf/wolf_tame");
         list.add("zombie_pigman");
         list.add("zombie/zombie");
         list.add("zombie/zombie_villager");

         for(int i = 0; i < list.size(); ++i) {
            String name = (String)list.get(i);
            String tex = "textures/entity/" + name + ".png";
            ResourceLocation texLoc = new ResourceLocation(tex);
            if(!Config.hasResource(texLoc)) {
               Config.warn("Not found: " + texLoc);
            }

            getProperties(texLoc);
         }

      }
   }

}
