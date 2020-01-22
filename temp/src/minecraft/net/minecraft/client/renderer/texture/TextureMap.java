package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.IIconCreator;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.src.Config;
import net.minecraft.src.CounterInt;
import net.minecraft.src.TextureUtils;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shadersmod.client.ShadersTex;

public class TextureMap extends AbstractTexture implements ITickableTextureObject {
   private static final boolean ENABLE_SKIP = Boolean.parseBoolean(System.getProperty("fml.skipFirstTextureLoad", "true"));
   private static final Logger field_147635_d = LogManager.getLogger();
   public static final ResourceLocation field_174945_f = new ResourceLocation("missingno");
   public static final ResourceLocation field_110575_b = new ResourceLocation("textures/atlas/blocks.png");
   private final List field_94258_i;
   private final Map field_110574_e;
   private final Map field_94252_e;
   private final String field_94254_c;
   private final IIconCreator field_174946_m;
   private int field_147636_j;
   private final TextureAtlasSprite field_94249_f;
   private static final String __OBFID = "CL_00001058";
   private boolean skipFirst;
   private TextureAtlasSprite[] iconGrid;
   private int iconGridSize;
   private int iconGridCountX;
   private int iconGridCountY;
   private double iconGridSizeU;
   private double iconGridSizeV;
   private CounterInt counterIndexInMap;
   public int atlasWidth;
   public int atlasHeight;

   public TextureMap(String p_i46099_1_) {
      this(p_i46099_1_, (IIconCreator)null);
   }

   public TextureMap(String p_i17_1_, boolean p_i17_2_) {
      this(p_i17_1_, (IIconCreator)null, p_i17_2_);
   }

   public TextureMap(String p_i46100_1_, IIconCreator p_i46100_2_) {
      this(p_i46100_1_, p_i46100_2_, false);
   }

   public TextureMap(String p_i18_1_, IIconCreator p_i18_2_, boolean p_i18_3_) {
      this.skipFirst = false;
      this.iconGrid = null;
      this.iconGridSize = -1;
      this.iconGridCountX = -1;
      this.iconGridCountY = -1;
      this.iconGridSizeU = -1.0D;
      this.iconGridSizeV = -1.0D;
      this.counterIndexInMap = new CounterInt(0);
      this.atlasWidth = 0;
      this.atlasHeight = 0;
      this.field_94258_i = Lists.newArrayList();
      this.field_110574_e = Maps.newHashMap();
      this.field_94252_e = Maps.newHashMap();
      this.field_94249_f = new TextureAtlasSprite("missingno");
      this.field_94254_c = p_i18_1_;
      this.field_174946_m = p_i18_2_;
      this.skipFirst = p_i18_3_ && ENABLE_SKIP;
   }

   private void func_110569_e() {
      int i = this.getMinSpriteSize();
      int[] aint = this.getMissingImageData(i);
      this.field_94249_f.func_110966_b(i);
      this.field_94249_f.func_110969_c(i);
      int[][] aint1 = new int[this.field_147636_j + 1][];
      aint1[0] = aint;
      this.field_94249_f.func_110968_a(Lists.newArrayList(new int[][][]{aint1}));
      this.field_94249_f.setIndexInMap(this.counterIndexInMap.nextValue());
   }

   public void func_110551_a(IResourceManager p_110551_1_) throws IOException {
      ShadersTex.resManager = p_110551_1_;
      if(this.field_174946_m != null) {
         this.func_174943_a(p_110551_1_, this.field_174946_m);
      }

   }

   public void func_174943_a(IResourceManager p_174943_1_, IIconCreator p_174943_2_) {
      this.field_110574_e.clear();
      this.counterIndexInMap.reset();
      p_174943_2_.func_177059_a(this);
      if(this.field_147636_j >= 4) {
         this.field_147636_j = this.detectMaxMipmapLevel(this.field_110574_e, p_174943_1_);
         Config.log("Mipmap levels: " + this.field_147636_j);
      }

      this.func_110569_e();
      this.func_147631_c();
      this.func_110571_b(p_174943_1_);
   }

   public void func_110571_b(IResourceManager param1) {
      // $FF: Couldn't be decompiled
   }

   public ResourceLocation func_147634_a(ResourceLocation p_147634_1_, int p_147634_2_) {
      return this.isAbsoluteLocation(p_147634_1_)?(p_147634_2_ == 0?new ResourceLocation(p_147634_1_.func_110624_b(), p_147634_1_.func_110623_a() + ".png"):new ResourceLocation(p_147634_1_.func_110624_b(), p_147634_1_.func_110623_a() + "mipmap" + p_147634_2_ + ".png")):(p_147634_2_ == 0?new ResourceLocation(p_147634_1_.func_110624_b(), String.format("%s/%s%s", new Object[]{this.field_94254_c, p_147634_1_.func_110623_a(), ".png"})):new ResourceLocation(p_147634_1_.func_110624_b(), String.format("%s/mipmaps/%s.%d%s", new Object[]{this.field_94254_c, p_147634_1_.func_110623_a(), Integer.valueOf(p_147634_2_), ".png"})));
   }

   public TextureAtlasSprite func_110572_b(String p_110572_1_) {
      TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)this.field_94252_e.get(p_110572_1_);
      if(textureatlassprite == null) {
         textureatlassprite = this.field_94249_f;
      }

      return textureatlassprite;
   }

   public void func_94248_c() {
      if(Config.isShaders()) {
         ShadersTex.updatingTex = this.getMultiTexID();
      }

      boolean flag = false;
      boolean flag1 = false;
      TextureUtil.func_94277_a(this.func_110552_b());

      for(TextureAtlasSprite textureatlassprite : this.field_94258_i) {
         if(this.isTerrainAnimationActive(textureatlassprite)) {
            textureatlassprite.func_94219_l();
            if(textureatlassprite.spriteNormal != null) {
               flag = true;
            }

            if(textureatlassprite.spriteSpecular != null) {
               flag1 = true;
            }
         }
      }

      if(Config.isMultiTexture()) {
         for(TextureAtlasSprite textureatlassprite1 : this.field_94258_i) {
            if(this.isTerrainAnimationActive(textureatlassprite1)) {
               TextureAtlasSprite textureatlassprite2 = textureatlassprite1.spriteSingle;
               if(textureatlassprite2 != null) {
                  if(textureatlassprite1 == TextureUtils.iconClock || textureatlassprite1 == TextureUtils.iconCompass) {
                     textureatlassprite2.field_110973_g = textureatlassprite1.field_110973_g;
                  }

                  textureatlassprite1.bindSpriteTexture();
                  textureatlassprite2.func_94219_l();
               }
            }
         }

         TextureUtil.func_94277_a(this.func_110552_b());
      }

      if(Config.isShaders()) {
         if(flag) {
            TextureUtil.func_94277_a(this.getMultiTexID().norm);

            for(TextureAtlasSprite textureatlassprite3 : this.field_94258_i) {
               if(textureatlassprite3.spriteNormal != null && this.isTerrainAnimationActive(textureatlassprite3)) {
                  if(textureatlassprite3 == TextureUtils.iconClock || textureatlassprite3 == TextureUtils.iconCompass) {
                     textureatlassprite3.spriteNormal.field_110973_g = textureatlassprite3.field_110973_g;
                  }

                  textureatlassprite3.spriteNormal.func_94219_l();
               }
            }
         }

         if(flag1) {
            TextureUtil.func_94277_a(this.getMultiTexID().spec);

            for(TextureAtlasSprite textureatlassprite4 : this.field_94258_i) {
               if(textureatlassprite4.spriteSpecular != null && this.isTerrainAnimationActive(textureatlassprite4)) {
                  if(textureatlassprite4 == TextureUtils.iconClock || textureatlassprite4 == TextureUtils.iconCompass) {
                     textureatlassprite4.spriteNormal.field_110973_g = textureatlassprite4.field_110973_g;
                  }

                  textureatlassprite4.spriteSpecular.func_94219_l();
               }
            }
         }

         if(flag || flag1) {
            TextureUtil.func_94277_a(this.func_110552_b());
         }
      }

      if(Config.isShaders()) {
         ShadersTex.updatingTex = null;
      }

   }

   public TextureAtlasSprite func_174942_a(ResourceLocation p_174942_1_) {
      if(p_174942_1_ == null) {
         throw new IllegalArgumentException("Location cannot be null!");
      } else {
         TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)this.field_110574_e.get(p_174942_1_.toString());
         if(textureatlassprite == null) {
            textureatlassprite = TextureAtlasSprite.func_176604_a(p_174942_1_);
            this.field_110574_e.put(p_174942_1_.toString(), textureatlassprite);
            textureatlassprite.updateIndexInMap(this.counterIndexInMap);
         }

         return textureatlassprite;
      }
   }

   public void func_110550_d() {
      this.func_94248_c();
   }

   public void func_147633_a(int p_147633_1_) {
      this.field_147636_j = p_147633_1_;
   }

   public TextureAtlasSprite func_174944_f() {
      return this.field_94249_f;
   }

   public TextureAtlasSprite getTextureExtry(String p_getTextureExtry_1_) {
      ResourceLocation resourcelocation = new ResourceLocation(p_getTextureExtry_1_);
      return (TextureAtlasSprite)this.field_110574_e.get(resourcelocation.toString());
   }

   public boolean setTextureEntry(String p_setTextureEntry_1_, TextureAtlasSprite p_setTextureEntry_2_) {
      if(!this.field_110574_e.containsKey(p_setTextureEntry_1_)) {
         this.field_110574_e.put(p_setTextureEntry_1_, p_setTextureEntry_2_);
         p_setTextureEntry_2_.updateIndexInMap(this.counterIndexInMap);
         return true;
      } else {
         return false;
      }
   }

   public boolean setTextureEntry(TextureAtlasSprite p_setTextureEntry_1_) {
      return this.setTextureEntry(p_setTextureEntry_1_.func_94215_i(), p_setTextureEntry_1_);
   }

   public String getBasePath() {
      return this.field_94254_c;
   }

   public int getMipmapLevels() {
      return this.field_147636_j;
   }

   private boolean isAbsoluteLocation(ResourceLocation p_isAbsoluteLocation_1_) {
      String s = p_isAbsoluteLocation_1_.func_110623_a();
      return this.isAbsoluteLocationPath(s);
   }

   private boolean isAbsoluteLocationPath(String p_isAbsoluteLocationPath_1_) {
      String s = p_isAbsoluteLocationPath_1_.toLowerCase();
      return s.startsWith("mcpatcher/") || s.startsWith("optifine/");
   }

   public TextureAtlasSprite getSpriteSafe(String p_getSpriteSafe_1_) {
      ResourceLocation resourcelocation = new ResourceLocation(p_getSpriteSafe_1_);
      return (TextureAtlasSprite)this.field_110574_e.get(resourcelocation.toString());
   }

   public TextureAtlasSprite getRegisteredSprite(ResourceLocation p_getRegisteredSprite_1_) {
      return (TextureAtlasSprite)this.field_110574_e.get(p_getRegisteredSprite_1_.toString());
   }

   private boolean isTerrainAnimationActive(TextureAtlasSprite p_isTerrainAnimationActive_1_) {
      return p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterStill && p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterFlow?(p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaStill && p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaFlow?(p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer0 && p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer1?(p_isTerrainAnimationActive_1_ == TextureUtils.iconPortal?Config.isAnimatedPortal():(p_isTerrainAnimationActive_1_ != TextureUtils.iconClock && p_isTerrainAnimationActive_1_ != TextureUtils.iconCompass?Config.isAnimatedTerrain():true)):Config.isAnimatedFire()):Config.isAnimatedLava()):Config.isAnimatedWater();
   }

   public int getCountRegisteredSprites() {
      return this.counterIndexInMap.getValue();
   }

   private int detectMaxMipmapLevel(Map p_detectMaxMipmapLevel_1_, IResourceManager p_detectMaxMipmapLevel_2_) {
      int i = this.detectMinimumSpriteSize(p_detectMaxMipmapLevel_1_, p_detectMaxMipmapLevel_2_, 20);
      if(i < 16) {
         i = 16;
      }

      i = MathHelper.func_151236_b(i);
      if(i > 16) {
         Config.log("Sprite size: " + i);
      }

      int j = MathHelper.func_151239_c(i);
      if(j < 4) {
         j = 4;
      }

      return j;
   }

   private int detectMinimumSpriteSize(Map p_detectMinimumSpriteSize_1_, IResourceManager p_detectMinimumSpriteSize_2_, int p_detectMinimumSpriteSize_3_) {
      Map map = new HashMap();

      for(Entry entry : p_detectMinimumSpriteSize_1_.entrySet()) {
         TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)entry.getValue();
         ResourceLocation resourcelocation = new ResourceLocation(textureatlassprite.func_94215_i());
         ResourceLocation resourcelocation1 = this.func_147634_a(resourcelocation, 0);
         if(!textureatlassprite.hasCustomLoader(p_detectMinimumSpriteSize_2_, resourcelocation)) {
            try {
               IResource iresource = p_detectMinimumSpriteSize_2_.func_110536_a(resourcelocation1);
               if(iresource != null) {
                  InputStream inputstream = iresource.func_110527_b();
                  if(inputstream != null) {
                     Dimension dimension = TextureUtils.getImageSize(inputstream, "png");
                     if(dimension != null) {
                        int i = dimension.width;
                        int j = MathHelper.func_151236_b(i);
                        if(!map.containsKey(Integer.valueOf(j))) {
                           map.put(Integer.valueOf(j), Integer.valueOf(1));
                        } else {
                           int k = ((Integer)map.get(Integer.valueOf(j))).intValue();
                           map.put(Integer.valueOf(j), Integer.valueOf(k + 1));
                        }
                     }
                  }
               }
            } catch (Exception var17) {
               ;
            }
         }
      }

      int l = 0;
      Set set = map.keySet();
      Set set1 = new TreeSet(set);

      int l1;
      for(Iterator iterator = set1.iterator(); iterator.hasNext(); l += l1) {
         int j1 = ((Integer)iterator.next()).intValue();
         l1 = ((Integer)map.get(Integer.valueOf(j1))).intValue();
      }

      int i1 = 16;
      int k1 = 0;
      l1 = l * p_detectMinimumSpriteSize_3_ / 100;
      Iterator iterator1 = set1.iterator();

      while(iterator1.hasNext()) {
         int i2 = ((Integer)iterator1.next()).intValue();
         int j2 = ((Integer)map.get(Integer.valueOf(i2))).intValue();
         k1 += j2;
         if(i2 > i1) {
            i1 = i2;
         }

         if(k1 > l1) {
            return i1;
         }
      }

      return i1;
   }

   private int getMinSpriteSize() {
      int i = 1 << this.field_147636_j;
      if(i < 8) {
         i = 8;
      }

      return i;
   }

   private int[] getMissingImageData(int p_getMissingImageData_1_) {
      BufferedImage bufferedimage = new BufferedImage(16, 16, 2);
      bufferedimage.setRGB(0, 0, 16, 16, TextureUtil.field_110999_b, 0, 16);
      BufferedImage bufferedimage1 = TextureUtils.scaleToPowerOfTwo(bufferedimage, p_getMissingImageData_1_);
      int[] aint = new int[p_getMissingImageData_1_ * p_getMissingImageData_1_];
      bufferedimage1.getRGB(0, 0, p_getMissingImageData_1_, p_getMissingImageData_1_, aint, 0, p_getMissingImageData_1_);
      return aint;
   }

   public boolean isTextureBound() {
      int i = GlStateManager.getBoundTexture();
      int j = this.func_110552_b();
      return i == j;
   }

   private void updateIconGrid(int p_updateIconGrid_1_, int p_updateIconGrid_2_) {
      this.iconGridCountX = -1;
      this.iconGridCountY = -1;
      this.iconGrid = null;
      if(this.iconGridSize > 0) {
         this.iconGridCountX = p_updateIconGrid_1_ / this.iconGridSize;
         this.iconGridCountY = p_updateIconGrid_2_ / this.iconGridSize;
         this.iconGrid = new TextureAtlasSprite[this.iconGridCountX * this.iconGridCountY];
         this.iconGridSizeU = 1.0D / (double)this.iconGridCountX;
         this.iconGridSizeV = 1.0D / (double)this.iconGridCountY;

         for(TextureAtlasSprite textureatlassprite : this.field_94252_e.values()) {
            double d0 = 0.5D / (double)p_updateIconGrid_1_;
            double d1 = 0.5D / (double)p_updateIconGrid_2_;
            double d2 = (double)Math.min(textureatlassprite.func_94209_e(), textureatlassprite.func_94212_f()) + d0;
            double d3 = (double)Math.min(textureatlassprite.func_94206_g(), textureatlassprite.func_94210_h()) + d1;
            double d4 = (double)Math.max(textureatlassprite.func_94209_e(), textureatlassprite.func_94212_f()) - d0;
            double d5 = (double)Math.max(textureatlassprite.func_94206_g(), textureatlassprite.func_94210_h()) - d1;
            int i = (int)(d2 / this.iconGridSizeU);
            int j = (int)(d3 / this.iconGridSizeV);
            int k = (int)(d4 / this.iconGridSizeU);
            int l = (int)(d5 / this.iconGridSizeV);

            for(int i1 = i; i1 <= k; ++i1) {
               if(i1 >= 0 && i1 < this.iconGridCountX) {
                  for(int j1 = j; j1 <= l; ++j1) {
                     if(j1 >= 0 && j1 < this.iconGridCountX) {
                        int k1 = j1 * this.iconGridCountX + i1;
                        this.iconGrid[k1] = textureatlassprite;
                     } else {
                        Config.warn("Invalid grid V: " + j1 + ", icon: " + textureatlassprite.func_94215_i());
                     }
                  }
               } else {
                  Config.warn("Invalid grid U: " + i1 + ", icon: " + textureatlassprite.func_94215_i());
               }
            }
         }

      }
   }

   public TextureAtlasSprite getIconByUV(double p_getIconByUV_1_, double p_getIconByUV_3_) {
      if(this.iconGrid == null) {
         return null;
      } else {
         int i = (int)(p_getIconByUV_1_ / this.iconGridSizeU);
         int j = (int)(p_getIconByUV_3_ / this.iconGridSizeV);
         int k = j * this.iconGridCountX + i;
         return k >= 0 && k <= this.iconGrid.length?this.iconGrid[k]:null;
      }
   }
}
