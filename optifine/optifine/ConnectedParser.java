package optifine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.biome.BiomeGenBase;
import optifine.Config;
import optifine.ConnectedProperties;
import optifine.MatchBlock;
import optifine.NbtTagValue;
import optifine.RangeInt;
import optifine.RangeListInt;
import optifine.VillagerProfession;

public class ConnectedParser {

   public String context = null;
   public static final VillagerProfession[] PROFESSIONS_INVALID = new VillagerProfession[0];


   public ConnectedParser(String context) {
      this.context = context;
   }

   public String parseName(String path) {
      String str = path;
      int pos = path.lastIndexOf(47);
      if(pos >= 0) {
         str = path.substring(pos + 1);
      }

      int pos2 = str.lastIndexOf(46);
      if(pos2 >= 0) {
         str = str.substring(0, pos2);
      }

      return str;
   }

   public String parseBasePath(String path) {
      int pos = path.lastIndexOf(47);
      return pos < 0?"":path.substring(0, pos);
   }

   public MatchBlock[] parseMatchBlocks(String propMatchBlocks) {
      if(propMatchBlocks == null) {
         return null;
      } else {
         ArrayList list = new ArrayList();
         String[] blockStrs = Config.tokenize(propMatchBlocks, " ");

         for(int mbs = 0; mbs < blockStrs.length; ++mbs) {
            String blockStr = blockStrs[mbs];
            MatchBlock[] mbs1 = this.parseMatchBlock(blockStr);
            if(mbs1 != null) {
               list.addAll(Arrays.asList(mbs1));
            }
         }

         MatchBlock[] var7 = (MatchBlock[])((MatchBlock[])list.toArray(new MatchBlock[list.size()]));
         return var7;
      }
   }

   public IBlockState parseBlockState(String str, IBlockState def) {
      MatchBlock[] mbs = this.parseMatchBlock(str);
      if(mbs == null) {
         return def;
      } else if(mbs.length != 1) {
         return def;
      } else {
         MatchBlock mb = mbs[0];
         int blockId = mb.getBlockId();
         Block block = Block.getBlockById(blockId);
         return block.getDefaultState();
      }
   }

   public MatchBlock[] parseMatchBlock(String blockStr) {
      if(blockStr == null) {
         return null;
      } else {
         blockStr = blockStr.trim();
         if(blockStr.length() <= 0) {
            return null;
         } else {
            String[] parts = Config.tokenize(blockStr, ":");
            String domain = "minecraft";
            boolean blockIndex = false;
            byte var14;
            if(parts.length > 1 && this.isFullBlockName(parts)) {
               domain = parts[0];
               var14 = 1;
            } else {
               domain = "minecraft";
               var14 = 0;
            }

            String blockPart = parts[var14];
            String[] params = (String[])Arrays.copyOfRange(parts, var14 + 1, parts.length);
            Block[] blocks = this.parseBlockPart(domain, blockPart);
            if(blocks == null) {
               return null;
            } else {
               MatchBlock[] datas = new MatchBlock[blocks.length];

               for(int i = 0; i < blocks.length; ++i) {
                  Block block = blocks[i];
                  int blockId = Block.getIdFromBlock(block);
                  int[] metadatas = null;
                  if(params.length > 0) {
                     metadatas = this.parseBlockMetadatas(block, params);
                     if(metadatas == null) {
                        return null;
                     }
                  }

                  MatchBlock bd = new MatchBlock(blockId, metadatas);
                  datas[i] = bd;
               }

               return datas;
            }
         }
      }
   }

   public boolean isFullBlockName(String[] parts) {
      if(parts.length < 2) {
         return false;
      } else {
         String part1 = parts[1];
         return part1.length() < 1?false:(this.startsWithDigit(part1)?false:!part1.contains("="));
      }
   }

   public boolean startsWithDigit(String str) {
      if(str == null) {
         return false;
      } else if(str.length() < 1) {
         return false;
      } else {
         char ch = str.charAt(0);
         return Character.isDigit(ch);
      }
   }

   public Block[] parseBlockPart(String domain, String blockPart) {
      if(this.startsWithDigit(blockPart)) {
         int[] var8 = this.parseIntList(blockPart);
         if(var8 == null) {
            return null;
         } else {
            Block[] var9 = new Block[var8.length];

            for(int var10 = 0; var10 < var8.length; ++var10) {
               int id = var8[var10];
               Block block1 = Block.getBlockById(id);
               if(block1 == null) {
                  this.warn("Block not found for id: " + id);
                  return null;
               }

               var9[var10] = block1;
            }

            return var9;
         }
      } else {
         String fullName = domain + ":" + blockPart;
         Block block = Block.getBlockFromName(fullName);
         if(block == null) {
            this.warn("Block not found for name: " + fullName);
            return null;
         } else {
            Block[] blocks = new Block[]{block};
            return blocks;
         }
      }
   }

   public int[] parseBlockMetadatas(Block block, String[] params) {
      if(params.length <= 0) {
         return null;
      } else {
         String param0 = params[0];
         if(this.startsWithDigit(param0)) {
            int[] var19 = this.parseIntList(param0);
            return var19;
         } else {
            IBlockState stateDefault = block.getDefaultState();
            Collection properties = stateDefault.getPropertyNames();
            HashMap mapPropValues = new HashMap();

            for(int listMetadatas = 0; listMetadatas < params.length; ++listMetadatas) {
               String metadatas = params[listMetadatas];
               if(metadatas.length() > 0) {
                  String[] i = Config.tokenize(metadatas, "=");
                  if(i.length != 2) {
                     this.warn("Invalid block property: " + metadatas);
                     return null;
                  }

                  String e = i[0];
                  String valStr = i[1];
                  IProperty prop = ConnectedProperties.getProperty(e, properties);
                  if(prop == null) {
                     this.warn("Property not found: " + e + ", block: " + block);
                     return null;
                  }

                  Object list = (List)mapPropValues.get(e);
                  if(list == null) {
                     list = new ArrayList();
                     mapPropValues.put(prop, list);
                  }

                  String[] vals = Config.tokenize(valStr, ",");

                  for(int v = 0; v < vals.length; ++v) {
                     String val = vals[v];
                     Comparable propVal = parsePropertyValue(prop, val);
                     if(propVal == null) {
                        this.warn("Property value not found: " + val + ", property: " + e + ", block: " + block);
                        return null;
                     }

                     ((List)list).add(propVal);
                  }
               }
            }

            if(mapPropValues.isEmpty()) {
               return null;
            } else {
               ArrayList var20 = new ArrayList();

               int var23;
               for(int var21 = 0; var21 < 16; ++var21) {
                  var23 = var21;

                  try {
                     IBlockState var24 = this.getStateFromMeta(block, var23);
                     if(this.matchState(var24, mapPropValues)) {
                        var20.add(Integer.valueOf(var23));
                     }
                  } catch (IllegalArgumentException var18) {
                     ;
                  }
               }

               if(var20.size() == 16) {
                  return null;
               } else {
                  int[] var22 = new int[var20.size()];

                  for(var23 = 0; var23 < var22.length; ++var23) {
                     var22[var23] = ((Integer)var20.get(var23)).intValue();
                  }

                  return var22;
               }
            }
         }
      }
   }

   public IBlockState getStateFromMeta(Block block, int md) {
      try {
         IBlockState e = block.getStateFromMeta(md);
         if(block == Blocks.double_plant && md > 7) {
            IBlockState bsLow = block.getStateFromMeta(md & 7);
            e = e.withProperty(BlockDoublePlant.VARIANT, bsLow.getValue(BlockDoublePlant.VARIANT));
         }

         return e;
      } catch (IllegalArgumentException var5) {
         return block.getDefaultState();
      }
   }

   public static Comparable parsePropertyValue(IProperty prop, String valStr) {
      Class valueClass = prop.getValueClass();
      Comparable valueObj = parseValue(valStr, valueClass);
      if(valueObj == null) {
         Collection propertyValues = prop.getAllowedValues();
         valueObj = getPropertyValue(valStr, propertyValues);
      }

      return valueObj;
   }

   public static Comparable getPropertyValue(String value, Collection propertyValues) {
      Iterator it = propertyValues.iterator();

      Comparable obj;
      do {
         if(!it.hasNext()) {
            return null;
         }

         obj = (Comparable)it.next();
      } while(!getValueName(obj).equals(value));

      return obj;
   }

   public static Object getValueName(Comparable obj) {
      if(obj instanceof IStringSerializable) {
         IStringSerializable iss = (IStringSerializable)obj;
         return iss.getName();
      } else {
         return obj.toString();
      }
   }

   public static Comparable parseValue(String str, Class cls) {
      return (Comparable)(cls == String.class?str:(cls == Boolean.class?Boolean.valueOf(str):(cls == Float.class?Float.valueOf(str):(cls == Double.class?Double.valueOf(str):(cls == Integer.class?Integer.valueOf(str):(cls == Long.class?Long.valueOf(str):null))))));
   }

   public boolean matchState(IBlockState bs, Map<IProperty, List<Comparable>> mapPropValues) {
      Set keys = mapPropValues.keySet();
      Iterator it = keys.iterator();

      List vals;
      Comparable bsVal;
      do {
         if(!it.hasNext()) {
            return true;
         }

         IProperty prop = (IProperty)it.next();
         vals = (List)mapPropValues.get(prop);
         bsVal = bs.getValue(prop);
         if(bsVal == null) {
            return false;
         }
      } while(vals.contains(bsVal));

      return false;
   }

   public BiomeGenBase[] parseBiomes(String str) {
      if(str == null) {
         return null;
      } else {
         str = str.trim();
         boolean negative = false;
         if(str.startsWith("!")) {
            negative = true;
            str = str.substring(1);
         }

         String[] biomeNames = Config.tokenize(str, " ");
         ArrayList list = new ArrayList();

         for(int biomeArr = 0; biomeArr < biomeNames.length; ++biomeArr) {
            String biomeName = biomeNames[biomeArr];
            BiomeGenBase biome = this.findBiome(biomeName);
            if(biome == null) {
               this.warn("Biome not found: " + biomeName);
            } else {
               list.add(biome);
            }
         }

         if(negative) {
            ArrayList var8 = new ArrayList(Arrays.asList(BiomeGenBase.getBiomeGenArray()));
            var8.removeAll(list);
            list = var8;
         }

         BiomeGenBase[] var9 = (BiomeGenBase[])((BiomeGenBase[])list.toArray(new BiomeGenBase[list.size()]));
         return var9;
      }
   }

   public BiomeGenBase findBiome(String biomeName) {
      biomeName = biomeName.toLowerCase();
      if(biomeName.equals("nether")) {
         return BiomeGenBase.hell;
      } else {
         BiomeGenBase[] biomeList = BiomeGenBase.getBiomeGenArray();

         for(int i = 0; i < biomeList.length; ++i) {
            BiomeGenBase biome = biomeList[i];
            if(biome != null) {
               String name = biome.biomeName.replace(" ", "").toLowerCase();
               if(name.equals(biomeName)) {
                  return biome;
               }
            }
         }

         return null;
      }
   }

   public int parseInt(String str) {
      if(str == null) {
         return -1;
      } else {
         str = str.trim();
         int num = Config.parseInt(str, -1);
         if(num < 0) {
            this.warn("Invalid number: " + str);
         }

         return num;
      }
   }

   public int parseInt(String str, int defVal) {
      if(str == null) {
         return defVal;
      } else {
         str = str.trim();
         int num = Config.parseInt(str, -1);
         if(num < 0) {
            this.warn("Invalid number: " + str);
            return defVal;
         } else {
            return num;
         }
      }
   }

   public int[] parseIntList(String str) {
      if(str == null) {
         return null;
      } else {
         ArrayList list = new ArrayList();
         String[] intStrs = Config.tokenize(str, " ,");

         for(int ints = 0; ints < intStrs.length; ++ints) {
            String i = intStrs[ints];
            if(i.contains("-")) {
               String[] val = Config.tokenize(i, "-");
               if(val.length != 2) {
                  this.warn("Invalid interval: " + i + ", when parsing: " + str);
               } else {
                  int min = Config.parseInt(val[0], -1);
                  int max = Config.parseInt(val[1], -1);
                  if(min >= 0 && max >= 0 && min <= max) {
                     for(int n = min; n <= max; ++n) {
                        list.add(Integer.valueOf(n));
                     }
                  } else {
                     this.warn("Invalid interval: " + i + ", when parsing: " + str);
                  }
               }
            } else {
               int var12 = Config.parseInt(i, -1);
               if(var12 < 0) {
                  this.warn("Invalid number: " + i + ", when parsing: " + str);
               } else {
                  list.add(Integer.valueOf(var12));
               }
            }
         }

         int[] var10 = new int[list.size()];

         for(int var11 = 0; var11 < var10.length; ++var11) {
            var10[var11] = ((Integer)list.get(var11)).intValue();
         }

         return var10;
      }
   }

   public boolean[] parseFaces(String str, boolean[] defVal) {
      if(str == null) {
         return defVal;
      } else {
         EnumSet setFaces = EnumSet.allOf(EnumFacing.class);
         String[] faceStrs = Config.tokenize(str, " ,");

         for(int faces = 0; faces < faceStrs.length; ++faces) {
            String i = faceStrs[faces];
            if(i.equals("sides")) {
               setFaces.add(EnumFacing.NORTH);
               setFaces.add(EnumFacing.SOUTH);
               setFaces.add(EnumFacing.WEST);
               setFaces.add(EnumFacing.EAST);
            } else if(i.equals("all")) {
               setFaces.addAll(Arrays.asList(EnumFacing.VALUES));
            } else {
               EnumFacing face = this.parseFace(i);
               if(face != null) {
                  setFaces.add(face);
               }
            }
         }

         boolean[] var8 = new boolean[EnumFacing.VALUES.length];

         for(int var9 = 0; var9 < var8.length; ++var9) {
            var8[var9] = setFaces.contains(EnumFacing.VALUES[var9]);
         }

         return var8;
      }
   }

   public EnumFacing parseFace(String str) {
      str = str.toLowerCase();
      if(!str.equals("bottom") && !str.equals("down")) {
         if(!str.equals("top") && !str.equals("up")) {
            if(str.equals("north")) {
               return EnumFacing.NORTH;
            } else if(str.equals("south")) {
               return EnumFacing.SOUTH;
            } else if(str.equals("east")) {
               return EnumFacing.EAST;
            } else if(str.equals("west")) {
               return EnumFacing.WEST;
            } else {
               Config.warn("Unknown face: " + str);
               return null;
            }
         } else {
            return EnumFacing.UP;
         }
      } else {
         return EnumFacing.DOWN;
      }
   }

   public void dbg(String str) {
      Config.dbg("" + this.context + ": " + str);
   }

   public void warn(String str) {
      Config.warn("" + this.context + ": " + str);
   }

   public RangeListInt parseRangeListInt(String str) {
      if(str == null) {
         return null;
      } else {
         RangeListInt list = new RangeListInt();
         String[] parts = Config.tokenize(str, " ,");

         for(int i = 0; i < parts.length; ++i) {
            String part = parts[i];
            RangeInt ri = this.parseRangeInt(part);
            if(ri == null) {
               return null;
            }

            list.addRange(ri);
         }

         return list;
      }
   }

   public RangeInt parseRangeInt(String str) {
      if(str == null) {
         return null;
      } else if(str.indexOf(45) >= 0) {
         String[] val1 = Config.tokenize(str, "-");
         if(val1.length != 2) {
            this.warn("Invalid range: " + str);
            return null;
         } else {
            int min = Config.parseInt(val1[0], -1);
            int max = Config.parseInt(val1[1], -1);
            if(min >= 0 && max >= 0) {
               return new RangeInt(min, max);
            } else {
               this.warn("Invalid range: " + str);
               return null;
            }
         }
      } else {
         int val = Config.parseInt(str, -1);
         if(val < 0) {
            this.warn("Invalid integer: " + str);
            return null;
         } else {
            return new RangeInt(val, val);
         }
      }
   }

   public boolean parseBoolean(String str) {
      if(str == null) {
         return false;
      } else {
         String strLower = str.toLowerCase().trim();
         return strLower.equals("true");
      }
   }

   public Boolean parseBooleanObject(String str) {
      if(str == null) {
         return null;
      } else {
         String strLower = str.toLowerCase().trim();
         if(strLower.equals("true")) {
            return Boolean.TRUE;
         } else if(strLower.equals("false")) {
            return Boolean.FALSE;
         } else {
            this.warn("Invalid boolean: " + str);
            return null;
         }
      }
   }

   public static int parseColor(String str, int defVal) {
      if(str == null) {
         return defVal;
      } else {
         str = str.trim();

         try {
            int e = Integer.parseInt(str, 16) & 16777215;
            return e;
         } catch (NumberFormatException var3) {
            return defVal;
         }
      }
   }

   public static int parseColor4(String str, int defVal) {
      if(str == null) {
         return defVal;
      } else {
         str = str.trim();

         try {
            int e = (int)(Long.parseLong(str, 16) & -1L);
            return e;
         } catch (NumberFormatException var3) {
            return defVal;
         }
      }
   }

   public EnumWorldBlockLayer parseBlockRenderLayer(String str, EnumWorldBlockLayer def) {
      if(str == null) {
         return def;
      } else {
         str = str.toLowerCase().trim();
         EnumWorldBlockLayer[] layers = EnumWorldBlockLayer.values();

         for(int i = 0; i < layers.length; ++i) {
            EnumWorldBlockLayer layer = layers[i];
            if(str.equals(layer.name().toLowerCase())) {
               return layer;
            }
         }

         return def;
      }
   }

   public Enum parseEnum(String str, Enum[] enums, String property) {
      if(str == null) {
         return null;
      } else {
         String strLower = str.toLowerCase().trim();

         for(int i = 0; i < enums.length; ++i) {
            Enum en = enums[i];
            if(en.name().toLowerCase().equals(strLower)) {
               return en;
            }
         }

         this.warn("Invalid " + property + ": " + str);
         return null;
      }
   }

   public Enum[] parseEnums(String str, Enum[] enums, String property, Enum[] errValue) {
      if(str == null) {
         return null;
      } else {
         str = str.toLowerCase().trim();
         String[] parts = Config.tokenize(str, " ");
         Enum[] arr = (Enum[])((Enum[])Array.newInstance(enums.getClass().getComponentType(), parts.length));

         for(int i = 0; i < parts.length; ++i) {
            String part = parts[i];
            Enum en = this.parseEnum(part, enums, property);
            if(en == null) {
               return errValue;
            }

            arr[i] = en;
         }

         return arr;
      }
   }

   public NbtTagValue parseNbtTagValue(String path, String value) {
      return path != null && value != null?new NbtTagValue(path, value):null;
   }

   public VillagerProfession[] parseProfessions(String profStr) {
      if(profStr == null) {
         return null;
      } else {
         ArrayList list = new ArrayList();
         String[] tokens = Config.tokenize(profStr, " ");

         for(int arr = 0; arr < tokens.length; ++arr) {
            String str = tokens[arr];
            VillagerProfession prof = this.parseProfession(str);
            if(prof == null) {
               this.warn("Invalid profession: " + str);
               return PROFESSIONS_INVALID;
            }

            list.add(prof);
         }

         if(list.isEmpty()) {
            return null;
         } else {
            VillagerProfession[] var7 = (VillagerProfession[])((VillagerProfession[])list.toArray(new VillagerProfession[list.size()]));
            return var7;
         }
      }
   }

   public VillagerProfession parseProfession(String str) {
      str = str.toLowerCase();
      String[] parts = Config.tokenize(str, ":");
      if(parts.length > 2) {
         return null;
      } else {
         String profStr = parts[0];
         String carStr = null;
         if(parts.length > 1) {
            carStr = parts[1];
         }

         int prof = parseProfessionId(profStr);
         if(prof < 0) {
            return null;
         } else {
            int[] cars = null;
            if(carStr != null) {
               cars = parseCareerIds(prof, carStr);
               if(cars == null) {
                  return null;
               }
            }

            return new VillagerProfession(prof, cars);
         }
      }
   }

   public static int parseProfessionId(String str) {
      int id = Config.parseInt(str, -1);
      return id >= 0?id:(str.equals("farmer")?0:(str.equals("librarian")?1:(str.equals("priest")?2:(str.equals("blacksmith")?3:(str.equals("butcher")?4:(str.equals("nitwit")?5:-1))))));
   }

   public static int[] parseCareerIds(int prof, String str) {
      HashSet set = new HashSet();
      String[] parts = Config.tokenize(str, ",");

      int i;
      for(int integerArr = 0; integerArr < parts.length; ++integerArr) {
         String arr = parts[integerArr];
         i = parseCareerId(prof, arr);
         if(i < 0) {
            return null;
         }

         set.add(Integer.valueOf(i));
      }

      Integer[] var7 = (Integer[])((Integer[])set.toArray(new Integer[set.size()]));
      int[] var8 = new int[var7.length];

      for(i = 0; i < var8.length; ++i) {
         var8[i] = var7[i].intValue();
      }

      return var8;
   }

   public static int parseCareerId(int prof, String str) {
      int id = Config.parseInt(str, -1);
      if(id >= 0) {
         return id;
      } else {
         if(prof == 0) {
            if(str.equals("farmer")) {
               return 1;
            }

            if(str.equals("fisherman")) {
               return 2;
            }

            if(str.equals("shepherd")) {
               return 3;
            }

            if(str.equals("fletcher")) {
               return 4;
            }
         }

         if(prof == 1) {
            if(str.equals("librarian")) {
               return 1;
            }

            if(str.equals("cartographer")) {
               return 2;
            }
         }

         if(prof == 2 && str.equals("cleric")) {
            return 1;
         } else {
            if(prof == 3) {
               if(str.equals("armor")) {
                  return 1;
               }

               if(str.equals("weapon")) {
                  return 2;
               }

               if(str.equals("tool")) {
                  return 3;
               }
            }

            if(prof == 4) {
               if(str.equals("butcher")) {
                  return 1;
               }

               if(str.equals("leather")) {
                  return 2;
               }
            }

            return prof == 5 && str.equals("nitwit")?1:-1;
         }
      }
   }

}
