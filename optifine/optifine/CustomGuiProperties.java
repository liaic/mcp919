package optifine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.biome.BiomeGenBase;
import optifine.Config;
import optifine.ConnectedParser;
import optifine.CustomGuis;
import optifine.Matches;
import optifine.NbtTagValue;
import optifine.RangeListInt;
import optifine.Reflector;
import optifine.ReflectorField;
import optifine.StrUtils;
import optifine.TextureUtils;
import optifine.VillagerProfession;

public class CustomGuiProperties {

   public String fileName = null;
   public String basePath = null;
   public CustomGuiProperties.EnumContainer container = null;
   public Map<ResourceLocation, ResourceLocation> textureLocations = null;
   public NbtTagValue nbtName = null;
   public BiomeGenBase[] biomes = null;
   public RangeListInt heights = null;
   public Boolean large = null;
   public Boolean trapped = null;
   public Boolean christmas = null;
   public Boolean ender = null;
   public RangeListInt levels = null;
   public VillagerProfession[] professions = null;
   public CustomGuiProperties.EnumVariant[] variants = null;
   public EnumDyeColor[] colors = null;
   public static final CustomGuiProperties.EnumVariant[] VARIANTS_HORSE = new CustomGuiProperties.EnumVariant[]{CustomGuiProperties.EnumVariant.HORSE, CustomGuiProperties.EnumVariant.DONKEY, CustomGuiProperties.EnumVariant.MULE, CustomGuiProperties.EnumVariant.LLAMA};
   public static final CustomGuiProperties.EnumVariant[] VARIANTS_DISPENSER = new CustomGuiProperties.EnumVariant[]{CustomGuiProperties.EnumVariant.DISPENSER, CustomGuiProperties.EnumVariant.DROPPER};
   public static final CustomGuiProperties.EnumVariant[] VARIANTS_INVALID = new CustomGuiProperties.EnumVariant[0];
   public static final EnumDyeColor[] COLORS_INVALID = new EnumDyeColor[0];
   public static final ResourceLocation ANVIL_GUI_TEXTURE = new ResourceLocation("textures/gui/container/anvil.png");
   public static final ResourceLocation BEACON_GUI_TEXTURE = new ResourceLocation("textures/gui/container/beacon.png");
   public static final ResourceLocation BREWING_STAND_GUI_TEXTURE = new ResourceLocation("textures/gui/container/brewing_stand.png");
   public static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
   public static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/crafting_table.png");
   public static final ResourceLocation HORSE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/horse.png");
   public static final ResourceLocation DISPENSER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/dispenser.png");
   public static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/enchanting_table.png");
   public static final ResourceLocation FURNACE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/furnace.png");
   public static final ResourceLocation HOPPER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/hopper.png");
   public static final ResourceLocation INVENTORY_GUI_TEXTURE = new ResourceLocation("textures/gui/container/inventory.png");
   public static final ResourceLocation SHULKER_BOX_GUI_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");
   public static final ResourceLocation VILLAGER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/villager.png");


   public CustomGuiProperties(Properties props, String path) {
      ConnectedParser cp = new ConnectedParser("CustomGuis");
      this.fileName = cp.parseName(path);
      this.basePath = cp.parseBasePath(path);
      this.container = (CustomGuiProperties.EnumContainer)cp.parseEnum(props.getProperty("container"), CustomGuiProperties.EnumContainer.values(), "container");
      this.textureLocations = parseTextureLocations(props, "texture", this.container, "textures/gui/", this.basePath);
      this.nbtName = cp.parseNbtTagValue("name", props.getProperty("name"));
      this.biomes = cp.parseBiomes(props.getProperty("biomes"));
      this.heights = cp.parseRangeListInt(props.getProperty("heights"));
      this.large = cp.parseBooleanObject(props.getProperty("large"));
      this.trapped = cp.parseBooleanObject(props.getProperty("trapped"));
      this.christmas = cp.parseBooleanObject(props.getProperty("christmas"));
      this.ender = cp.parseBooleanObject(props.getProperty("ender"));
      this.levels = cp.parseRangeListInt(props.getProperty("levels"));
      this.professions = cp.parseProfessions(props.getProperty("professions"));
      CustomGuiProperties.EnumVariant[] vars = getContainerVariants(this.container);
      this.variants = (CustomGuiProperties.EnumVariant[])((CustomGuiProperties.EnumVariant[])cp.parseEnums(props.getProperty("variants"), vars, "variants", VARIANTS_INVALID));
      this.colors = parseEnumDyeColors(props.getProperty("colors"));
   }

   public static CustomGuiProperties.EnumVariant[] getContainerVariants(CustomGuiProperties.EnumContainer cont) {
      return cont == CustomGuiProperties.EnumContainer.HORSE?VARIANTS_HORSE:(cont == CustomGuiProperties.EnumContainer.DISPENSER?VARIANTS_DISPENSER:new CustomGuiProperties.EnumVariant[0]);
   }

   public static EnumDyeColor[] parseEnumDyeColors(String str) {
      if(str == null) {
         return null;
      } else {
         str = str.toLowerCase();
         String[] tokens = Config.tokenize(str, " ");
         EnumDyeColor[] cols = new EnumDyeColor[tokens.length];

         for(int i = 0; i < tokens.length; ++i) {
            String token = tokens[i];
            EnumDyeColor col = parseEnumDyeColor(token);
            if(col == null) {
               warn("Invalid color: " + token);
               return COLORS_INVALID;
            }

            cols[i] = col;
         }

         return cols;
      }
   }

   public static EnumDyeColor parseEnumDyeColor(String str) {
      if(str == null) {
         return null;
      } else {
         EnumDyeColor[] colors = EnumDyeColor.values();

         for(int i = 0; i < colors.length; ++i) {
            EnumDyeColor enumDyeColor = colors[i];
            if(enumDyeColor.getName().equals(str)) {
               return enumDyeColor;
            }

            if(enumDyeColor.getUnlocalizedName().equals(str)) {
               return enumDyeColor;
            }
         }

         return null;
      }
   }

   public static ResourceLocation parseTextureLocation(String str, String basePath) {
      if(str == null) {
         return null;
      } else {
         str = str.trim();
         String tex = TextureUtils.fixResourcePath(str, basePath);
         if(!tex.endsWith(".png")) {
            tex = tex + ".png";
         }

         return new ResourceLocation(basePath + "/" + tex);
      }
   }

   public static Map<ResourceLocation, ResourceLocation> parseTextureLocations(Properties props, String property, CustomGuiProperties.EnumContainer container, String pathPrefix, String basePath) {
      HashMap map = new HashMap();
      String propVal = props.getProperty(property);
      if(propVal != null) {
         ResourceLocation keyPrefix = getGuiTextureLocation(container);
         ResourceLocation keys = parseTextureLocation(propVal, basePath);
         if(keyPrefix != null && keys != null) {
            map.put(keyPrefix, keys);
         }
      }

      String keyPrefix1 = property + ".";
      Set keys1 = props.keySet();
      Iterator it = keys1.iterator();

      while(it.hasNext()) {
         String key = (String)it.next();
         if(key.startsWith(keyPrefix1)) {
            String pathRel = key.substring(keyPrefix1.length());
            pathRel = pathRel.replace('\\', '/');
            pathRel = StrUtils.removePrefixSuffix(pathRel, "/", ".png");
            String path = pathPrefix + pathRel + ".png";
            String val = props.getProperty(key);
            ResourceLocation locKey = new ResourceLocation(path);
            ResourceLocation locVal = parseTextureLocation(val, basePath);
            map.put(locKey, locVal);
         }
      }

      return map;
   }

   public static ResourceLocation getGuiTextureLocation(CustomGuiProperties.EnumContainer container) {
      switch(CustomGuiProperties.NamelessClass1383409665.$SwitchMap$net$minecraft$src$CustomGuiProperties$EnumContainer[container.ordinal()]) {
      case 1:
         return ANVIL_GUI_TEXTURE;
      case 2:
         return BEACON_GUI_TEXTURE;
      case 3:
         return BREWING_STAND_GUI_TEXTURE;
      case 4:
         return CHEST_GUI_TEXTURE;
      case 5:
         return CRAFTING_TABLE_GUI_TEXTURE;
      case 6:
         return null;
      case 7:
         return DISPENSER_GUI_TEXTURE;
      case 8:
         return ENCHANTMENT_TABLE_GUI_TEXTURE;
      case 9:
         return FURNACE_GUI_TEXTURE;
      case 10:
         return HOPPER_GUI_TEXTURE;
      case 11:
         return HORSE_GUI_TEXTURE;
      case 12:
         return INVENTORY_GUI_TEXTURE;
      case 13:
         return SHULKER_BOX_GUI_TEXTURE;
      case 14:
         return VILLAGER_GUI_TEXTURE;
      default:
         return null;
      }
   }

   public boolean isValid(String path) {
      if(this.fileName != null && this.fileName.length() > 0) {
         if(this.basePath == null) {
            warn("No base path found: " + path);
            return false;
         } else if(this.container == null) {
            warn("No container found: " + path);
            return false;
         } else if(this.textureLocations.isEmpty()) {
            warn("No texture found: " + path);
            return false;
         } else if(this.professions == ConnectedParser.PROFESSIONS_INVALID) {
            warn("Invalid professions or careers: " + path);
            return false;
         } else if(this.variants == VARIANTS_INVALID) {
            warn("Invalid variants: " + path);
            return false;
         } else if(this.colors == COLORS_INVALID) {
            warn("Invalid colors: " + path);
            return false;
         } else {
            return true;
         }
      } else {
         warn("No name found: " + path);
         return false;
      }
   }

   public static void warn(String str) {
      Config.warn("[CustomGuis] " + str);
   }

   public boolean matchesGeneral(CustomGuiProperties.EnumContainer ec, BlockPos pos, IBlockAccess blockAccess) {
      if(this.container != ec) {
         return false;
      } else {
         if(this.biomes != null) {
            BiomeGenBase biome = blockAccess.getBiomeGenForCoords(pos);
            if(!Matches.biome(biome, this.biomes)) {
               return false;
            }
         }

         return this.heights == null || this.heights.isInRange(pos.getY());
      }
   }

   public boolean matchesPos(CustomGuiProperties.EnumContainer ec, BlockPos pos, IBlockAccess blockAccess, GuiScreen screen) {
      if(!this.matchesGeneral(ec, pos, blockAccess)) {
         return false;
      } else {
         if(this.nbtName != null) {
            String name = getName(screen);
            if(!this.nbtName.matchesValue(name)) {
               return false;
            }
         }

         switch(CustomGuiProperties.NamelessClass1383409665.$SwitchMap$net$minecraft$src$CustomGuiProperties$EnumContainer[ec.ordinal()]) {
         case 2:
            return this.matchesBeacon(pos, blockAccess);
         case 4:
            return this.matchesChest(pos, blockAccess);
         case 7:
            return this.matchesDispenser(pos, blockAccess);
         default:
            return true;
         }
      }
   }

   public static String getName(GuiScreen screen) {
      IWorldNameable nameable = getWorldNameable(screen);
      return nameable == null?null:nameable.getDisplayName().getUnformattedText();
   }

   public static IWorldNameable getWorldNameable(GuiScreen screen) {
      return (IWorldNameable)(screen instanceof GuiBeacon?getWorldNameable(screen, Reflector.GuiBeacon_tileBeacon):(screen instanceof GuiBrewingStand?getWorldNameable(screen, Reflector.GuiBrewingStand_tileBrewingStand):(screen instanceof GuiChest?getWorldNameable(screen, Reflector.GuiChest_lowerChestInventory):(screen instanceof GuiDispenser?((GuiDispenser)screen).dispenserInventory:(screen instanceof GuiEnchantment?getWorldNameable(screen, Reflector.GuiEnchantment_nameable):(screen instanceof GuiFurnace?getWorldNameable(screen, Reflector.GuiFurnace_tileFurnace):(screen instanceof GuiHopper?getWorldNameable(screen, Reflector.GuiHopper_hopperInventory):null)))))));
   }

   public static IWorldNameable getWorldNameable(GuiScreen screen, ReflectorField fieldInventory) {
      Object obj = Reflector.getFieldValue(screen, fieldInventory);
      return !(obj instanceof IWorldNameable)?null:(IWorldNameable)obj;
   }

   public boolean matchesBeacon(BlockPos pos, IBlockAccess blockAccess) {
      TileEntity te = blockAccess.getTileEntity(pos);
      if(!(te instanceof TileEntityBeacon)) {
         return false;
      } else {
         TileEntityBeacon teb = (TileEntityBeacon)te;
         if(this.levels != null) {
            NBTTagCompound nbt = new NBTTagCompound();
            teb.writeToNBT(nbt);
            int l = nbt.getInteger("Levels");
            if(!this.levels.isInRange(l)) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean matchesChest(BlockPos pos, IBlockAccess blockAccess) {
      TileEntity te = blockAccess.getTileEntity(pos);
      if(te instanceof TileEntityChest) {
         TileEntityChest teec1 = (TileEntityChest)te;
         return this.matchesChest(teec1, pos, blockAccess);
      } else if(te instanceof TileEntityEnderChest) {
         TileEntityEnderChest teec = (TileEntityEnderChest)te;
         return this.matchesEnderChest(teec, pos, blockAccess);
      } else {
         return false;
      }
   }

   public boolean matchesChest(TileEntityChest tec, BlockPos pos, IBlockAccess blockAccess) {
      boolean isLarge = tec.adjacentChestXNeg != null || tec.adjacentChestXPos != null || tec.adjacentChestZNeg != null || tec.adjacentChestZPos != null;
      boolean isTrapped = tec.getChestType() == 1;
      boolean isChristmas = CustomGuis.isChristmas;
      boolean isEnder = false;
      return this.matchesChest(isLarge, isTrapped, isChristmas, isEnder);
   }

   public boolean matchesEnderChest(TileEntityEnderChest teec, BlockPos pos, IBlockAccess blockAccess) {
      return this.matchesChest(false, false, false, true);
   }

   public boolean matchesChest(boolean isLarge, boolean isTrapped, boolean isChristmas, boolean isEnder) {
      return this.large != null && this.large.booleanValue() != isLarge?false:(this.trapped != null && this.trapped.booleanValue() != isTrapped?false:(this.christmas != null && this.christmas.booleanValue() != isChristmas?false:this.ender == null || this.ender.booleanValue() == isEnder));
   }

   public boolean matchesDispenser(BlockPos pos, IBlockAccess blockAccess) {
      TileEntity te = blockAccess.getTileEntity(pos);
      if(!(te instanceof TileEntityDispenser)) {
         return false;
      } else {
         TileEntityDispenser ted = (TileEntityDispenser)te;
         if(this.variants != null) {
            CustomGuiProperties.EnumVariant var = this.getDispenserVariant(ted);
            if(!Config.equalsOne(var, this.variants)) {
               return false;
            }
         }

         return true;
      }
   }

   public CustomGuiProperties.EnumVariant getDispenserVariant(TileEntityDispenser ted) {
      return ted instanceof TileEntityDropper?CustomGuiProperties.EnumVariant.DROPPER:CustomGuiProperties.EnumVariant.DISPENSER;
   }

   public boolean matchesEntity(CustomGuiProperties.EnumContainer ec, Entity entity, IBlockAccess blockAccess) {
      if(!this.matchesGeneral(ec, entity.getPosition(), blockAccess)) {
         return false;
      } else {
         if(this.nbtName != null) {
            String entityName = entity.getName();
            if(!this.nbtName.matchesValue(entityName)) {
               return false;
            }
         }

         switch(CustomGuiProperties.NamelessClass1383409665.$SwitchMap$net$minecraft$src$CustomGuiProperties$EnumContainer[ec.ordinal()]) {
         case 11:
            return this.matchesHorse(entity, blockAccess);
         case 14:
            return this.matchesVillager(entity, blockAccess);
         default:
            return true;
         }
      }
   }

   public boolean matchesVillager(Entity entity, IBlockAccess blockAccess) {
      if(!(entity instanceof EntityVillager)) {
         return false;
      } else {
         EntityVillager entityVillager = (EntityVillager)entity;
         if(this.professions != null) {
            int profInt = entityVillager.getProfession();
            int careerInt = Reflector.getFieldValueInt(entityVillager, Reflector.EntityVillager_careerId, -1);
            if(careerInt < 0) {
               return false;
            }

            boolean matchProfession = false;

            for(int i = 0; i < this.professions.length; ++i) {
               VillagerProfession prof = this.professions[i];
               if(prof.matches(profInt, careerInt)) {
                  matchProfession = true;
                  break;
               }
            }

            if(!matchProfession) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean matchesHorse(Entity entity, IBlockAccess blockAccess) {
      if(!(entity instanceof EntityHorse)) {
         return false;
      } else {
         EntityHorse ah = (EntityHorse)entity;
         if(this.variants != null) {
            CustomGuiProperties.EnumVariant var = this.getHorseVariant(ah);
            if(!Config.equalsOne(var, this.variants)) {
               return false;
            }
         }

         return true;
      }
   }

   public CustomGuiProperties.EnumVariant getHorseVariant(EntityHorse entity) {
      int type = entity.getHorseType();
      switch(type) {
      case 0:
         return CustomGuiProperties.EnumVariant.HORSE;
      case 1:
         return CustomGuiProperties.EnumVariant.DONKEY;
      case 2:
         return CustomGuiProperties.EnumVariant.MULE;
      default:
         return null;
      }
   }

   public CustomGuiProperties.EnumContainer getContainer() {
      return this.container;
   }

   public ResourceLocation getTextureLocation(ResourceLocation loc) {
      ResourceLocation locNew = (ResourceLocation)this.textureLocations.get(loc);
      return locNew == null?loc:locNew;
   }

   public String toString() {
      return "name: " + this.fileName + ", container: " + this.container + ", textures: " + this.textureLocations;
   }


   // $FF: synthetic class
   public static class NamelessClass1383409665 {

      // $FF: synthetic field
      public static final int[] $SwitchMap$net$minecraft$src$CustomGuiProperties$EnumContainer = new int[CustomGuiProperties.EnumContainer.values().length];


      static {
         try {
            $SwitchMap$net$minecraft$src$CustomGuiProperties$EnumContainer[CustomGuiProperties.EnumContainer.ANVIL.ordinal()] = 1;
         } catch (NoSuchFieldError var14) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$CustomGuiProperties$EnumContainer[CustomGuiProperties.EnumContainer.BEACON.ordinal()] = 2;
         } catch (NoSuchFieldError var13) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$CustomGuiProperties$EnumContainer[CustomGuiProperties.EnumContainer.BREWING_STAND.ordinal()] = 3;
         } catch (NoSuchFieldError var12) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$CustomGuiProperties$EnumContainer[CustomGuiProperties.EnumContainer.CHEST.ordinal()] = 4;
         } catch (NoSuchFieldError var11) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$CustomGuiProperties$EnumContainer[CustomGuiProperties.EnumContainer.CRAFTING.ordinal()] = 5;
         } catch (NoSuchFieldError var10) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$CustomGuiProperties$EnumContainer[CustomGuiProperties.EnumContainer.CREATIVE.ordinal()] = 6;
         } catch (NoSuchFieldError var9) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$CustomGuiProperties$EnumContainer[CustomGuiProperties.EnumContainer.DISPENSER.ordinal()] = 7;
         } catch (NoSuchFieldError var8) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$CustomGuiProperties$EnumContainer[CustomGuiProperties.EnumContainer.ENCHANTMENT.ordinal()] = 8;
         } catch (NoSuchFieldError var7) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$CustomGuiProperties$EnumContainer[CustomGuiProperties.EnumContainer.FURNACE.ordinal()] = 9;
         } catch (NoSuchFieldError var6) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$CustomGuiProperties$EnumContainer[CustomGuiProperties.EnumContainer.HOPPER.ordinal()] = 10;
         } catch (NoSuchFieldError var5) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$CustomGuiProperties$EnumContainer[CustomGuiProperties.EnumContainer.HORSE.ordinal()] = 11;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$CustomGuiProperties$EnumContainer[CustomGuiProperties.EnumContainer.INVENTORY.ordinal()] = 12;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$CustomGuiProperties$EnumContainer[CustomGuiProperties.EnumContainer.SHULKER_BOX.ordinal()] = 13;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            $SwitchMap$net$minecraft$src$CustomGuiProperties$EnumContainer[CustomGuiProperties.EnumContainer.VILLAGER.ordinal()] = 14;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static enum EnumContainer {

      ANVIL("ANVIL", 0),
      BEACON("BEACON", 1),
      BREWING_STAND("BREWING_STAND", 2),
      CHEST("CHEST", 3),
      CRAFTING("CRAFTING", 4),
      DISPENSER("DISPENSER", 5),
      ENCHANTMENT("ENCHANTMENT", 6),
      FURNACE("FURNACE", 7),
      HOPPER("HOPPER", 8),
      HORSE("HORSE", 9),
      VILLAGER("VILLAGER", 10),
      SHULKER_BOX("SHULKER_BOX", 11),
      CREATIVE("CREATIVE", 12),
      INVENTORY("INVENTORY", 13);
      public static final CustomGuiProperties.EnumContainer[] VALUES = values();
      // $FF: synthetic field
      public static final CustomGuiProperties.EnumContainer[] $VALUES = new CustomGuiProperties.EnumContainer[]{ANVIL, BEACON, BREWING_STAND, CHEST, CRAFTING, DISPENSER, ENCHANTMENT, FURNACE, HOPPER, HORSE, VILLAGER, SHULKER_BOX, CREATIVE, INVENTORY};


      public EnumContainer(String var1, int var2) {}

   }

   public static enum EnumVariant {

      HORSE("HORSE", 0),
      DONKEY("DONKEY", 1),
      MULE("MULE", 2),
      LLAMA("LLAMA", 3),
      DISPENSER("DISPENSER", 4),
      DROPPER("DROPPER", 5);
      // $FF: synthetic field
      public static final CustomGuiProperties.EnumVariant[] $VALUES = new CustomGuiProperties.EnumVariant[]{HORSE, DONKEY, MULE, LLAMA, DISPENSER, DROPPER};


      public EnumVariant(String var1, int var2) {}

   }
}
