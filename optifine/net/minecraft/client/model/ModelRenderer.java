package net.minecraft.client.model;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.TextureOffset;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;
import net.optifine.entity.model.anim.ModelUpdater;
import optifine.Config;
import optifine.ModelSprite;
import org.lwjgl.opengl.GL11;

public class ModelRenderer {

   public float textureWidth;
   public float textureHeight;
   public int textureOffsetX;
   public int textureOffsetY;
   public float rotationPointX;
   public float rotationPointY;
   public float rotationPointZ;
   public float rotateAngleX;
   public float rotateAngleY;
   public float rotateAngleZ;
   public boolean compiled;
   public int displayList;
   public boolean mirror;
   public boolean showModel;
   public boolean isHidden;
   public List cubeList;
   public List childModels;
   public final String boxName;
   public ModelBase baseModel;
   public float offsetX;
   public float offsetY;
   public float offsetZ;
   public static final String __OBFID = "CL_00000874";
   public List spriteList;
   public boolean mirrorV;
   public float scaleX;
   public float scaleY;
   public float scaleZ;
   public float savedScale;
   public ResourceLocation textureLocation;
   public String id;
   public ModelUpdater modelUpdater;
   public RenderGlobal renderGlobal;


   public ModelRenderer(ModelBase p_i1172_1_, String p_i1172_2_) {
      this.spriteList = new ArrayList();
      this.mirrorV = false;
      this.scaleX = 1.0F;
      this.scaleY = 1.0F;
      this.scaleZ = 1.0F;
      this.textureLocation = null;
      this.id = null;
      this.renderGlobal = Config.getRenderGlobal();
      this.textureWidth = 64.0F;
      this.textureHeight = 32.0F;
      this.showModel = true;
      this.cubeList = Lists.newArrayList();
      this.baseModel = p_i1172_1_;
      p_i1172_1_.boxList.add(this);
      this.boxName = p_i1172_2_;
      this.setTextureSize(p_i1172_1_.textureWidth, p_i1172_1_.textureHeight);
   }

   public ModelRenderer(ModelBase p_i1173_1_) {
      this(p_i1173_1_, (String)null);
   }

   public ModelRenderer(ModelBase p_i46358_1_, int p_i46358_2_, int p_i46358_3_) {
      this(p_i46358_1_);
      this.setTextureOffset(p_i46358_2_, p_i46358_3_);
   }

   public void addChild(ModelRenderer p_78792_1_) {
      if(this.childModels == null) {
         this.childModels = Lists.newArrayList();
      }

      this.childModels.add(p_78792_1_);
   }

   public ModelRenderer setTextureOffset(int p_78784_1_, int p_78784_2_) {
      this.textureOffsetX = p_78784_1_;
      this.textureOffsetY = p_78784_2_;
      return this;
   }

   public ModelRenderer addBox(String p_78786_1_, float p_78786_2_, float p_78786_3_, float p_78786_4_, int p_78786_5_, int p_78786_6_, int p_78786_7_) {
      p_78786_1_ = this.boxName + "." + p_78786_1_;
      TextureOffset var8 = this.baseModel.getTextureOffset(p_78786_1_);
      this.setTextureOffset(var8.textureOffsetX, var8.textureOffsetY);
      this.cubeList.add((new ModelBox(this, this.textureOffsetX, this.textureOffsetY, p_78786_2_, p_78786_3_, p_78786_4_, p_78786_5_, p_78786_6_, p_78786_7_, 0.0F)).setBoxName(p_78786_1_));
      return this;
   }

   public ModelRenderer addBox(float p_78789_1_, float p_78789_2_, float p_78789_3_, int p_78789_4_, int p_78789_5_, int p_78789_6_) {
      this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, p_78789_1_, p_78789_2_, p_78789_3_, p_78789_4_, p_78789_5_, p_78789_6_, 0.0F));
      return this;
   }

   public ModelRenderer addBox(float p_178769_1_, float p_178769_2_, float p_178769_3_, int p_178769_4_, int p_178769_5_, int p_178769_6_, boolean p_178769_7_) {
      this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, p_178769_1_, p_178769_2_, p_178769_3_, p_178769_4_, p_178769_5_, p_178769_6_, 0.0F, p_178769_7_));
      return this;
   }

   public void addBox(float p_78790_1_, float p_78790_2_, float p_78790_3_, int p_78790_4_, int p_78790_5_, int p_78790_6_, float p_78790_7_) {
      this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, p_78790_1_, p_78790_2_, p_78790_3_, p_78790_4_, p_78790_5_, p_78790_6_, p_78790_7_));
   }

   public void setRotationPoint(float p_78793_1_, float p_78793_2_, float p_78793_3_) {
      this.rotationPointX = p_78793_1_;
      this.rotationPointY = p_78793_2_;
      this.rotationPointZ = p_78793_3_;
   }

   public void render(float p_78785_1_) {
      if(!this.isHidden && this.showModel) {
         if(!this.compiled) {
            this.compileDisplayList(p_78785_1_);
         }

         int lastTextureId = 0;
         if(this.textureLocation != null && !this.renderGlobal.renderOverlayDamaged) {
            if(this.renderGlobal.renderOverlayEyes) {
               return;
            }

            lastTextureId = GlStateManager.getBoundTexture();
            Config.getTextureManager().bindTexture(this.textureLocation);
         }

         if(this.modelUpdater != null) {
            this.modelUpdater.update();
         }

         boolean scaleXYZ = this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F;
         GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
         int var2;
         if(this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
            if(this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
               if(scaleXYZ) {
                  GlStateManager.scale(this.scaleX, this.scaleY, this.scaleZ);
               }

               GlStateManager.callList(this.displayList);
               if(this.childModels != null) {
                  for(var2 = 0; var2 < this.childModels.size(); ++var2) {
                     ((ModelRenderer)this.childModels.get(var2)).render(p_78785_1_);
                  }
               }

               if(scaleXYZ) {
                  GlStateManager.scale(1.0F / this.scaleX, 1.0F / this.scaleY, 1.0F / this.scaleZ);
               }
            } else {
               GlStateManager.translate(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
               if(scaleXYZ) {
                  GlStateManager.scale(this.scaleX, this.scaleY, this.scaleZ);
               }

               GlStateManager.callList(this.displayList);
               if(this.childModels != null) {
                  for(var2 = 0; var2 < this.childModels.size(); ++var2) {
                     ((ModelRenderer)this.childModels.get(var2)).render(p_78785_1_);
                  }
               }

               if(scaleXYZ) {
                  GlStateManager.scale(1.0F / this.scaleX, 1.0F / this.scaleY, 1.0F / this.scaleZ);
               }

               GlStateManager.translate(-this.rotationPointX * p_78785_1_, -this.rotationPointY * p_78785_1_, -this.rotationPointZ * p_78785_1_);
            }
         } else {
            GlStateManager.pushMatrix();
            GlStateManager.translate(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
            if(this.rotateAngleZ != 0.0F) {
               GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
            }

            if(this.rotateAngleY != 0.0F) {
               GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
            }

            if(this.rotateAngleX != 0.0F) {
               GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
            }

            if(scaleXYZ) {
               GlStateManager.scale(this.scaleX, this.scaleY, this.scaleZ);
            }

            GlStateManager.callList(this.displayList);
            if(this.childModels != null) {
               for(var2 = 0; var2 < this.childModels.size(); ++var2) {
                  ((ModelRenderer)this.childModels.get(var2)).render(p_78785_1_);
               }
            }

            GlStateManager.popMatrix();
         }

         GlStateManager.translate(-this.offsetX, -this.offsetY, -this.offsetZ);
         if(lastTextureId != 0) {
            GlStateManager.bindTexture(lastTextureId);
         }
      }

   }

   public void renderWithRotation(float p_78791_1_) {
      if(!this.isHidden && this.showModel) {
         if(!this.compiled) {
            this.compileDisplayList(p_78791_1_);
         }

         int lastTextureId = 0;
         if(this.textureLocation != null && !this.renderGlobal.renderOverlayDamaged) {
            if(this.renderGlobal.renderOverlayEyes) {
               return;
            }

            lastTextureId = GlStateManager.getBoundTexture();
            Config.getTextureManager().bindTexture(this.textureLocation);
         }

         if(this.modelUpdater != null) {
            this.modelUpdater.update();
         }

         boolean scaleXYZ = this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F;
         GlStateManager.pushMatrix();
         GlStateManager.translate(this.rotationPointX * p_78791_1_, this.rotationPointY * p_78791_1_, this.rotationPointZ * p_78791_1_);
         if(this.rotateAngleY != 0.0F) {
            GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
         }

         if(this.rotateAngleX != 0.0F) {
            GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
         }

         if(this.rotateAngleZ != 0.0F) {
            GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
         }

         if(scaleXYZ) {
            GlStateManager.scale(this.scaleX, this.scaleY, this.scaleZ);
         }

         GlStateManager.callList(this.displayList);
         if(this.childModels != null) {
            for(int i = 0; i < this.childModels.size(); ++i) {
               ((ModelRenderer)this.childModels.get(i)).render(p_78791_1_);
            }
         }

         GlStateManager.popMatrix();
         if(lastTextureId != 0) {
            GlStateManager.bindTexture(lastTextureId);
         }
      }

   }

   public void postRender(float p_78794_1_) {
      if(!this.isHidden && this.showModel) {
         if(!this.compiled) {
            this.compileDisplayList(p_78794_1_);
         }

         if(this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
            if(this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F) {
               GlStateManager.translate(this.rotationPointX * p_78794_1_, this.rotationPointY * p_78794_1_, this.rotationPointZ * p_78794_1_);
            }
         } else {
            GlStateManager.translate(this.rotationPointX * p_78794_1_, this.rotationPointY * p_78794_1_, this.rotationPointZ * p_78794_1_);
            if(this.rotateAngleZ != 0.0F) {
               GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
            }

            if(this.rotateAngleY != 0.0F) {
               GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
            }

            if(this.rotateAngleX != 0.0F) {
               GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
            }
         }
      }

   }

   public void compileDisplayList(float p_78788_1_) {
      if(this.displayList == 0) {
         this.savedScale = p_78788_1_;
         this.displayList = GLAllocation.generateDisplayLists(1);
      }

      GL11.glNewList(this.displayList, 4864);
      WorldRenderer var2 = Tessellator.getInstance().getWorldRenderer();

      int i;
      for(i = 0; i < this.cubeList.size(); ++i) {
         ((ModelBox)this.cubeList.get(i)).render(var2, p_78788_1_);
      }

      for(i = 0; i < this.spriteList.size(); ++i) {
         ModelSprite sprite = (ModelSprite)this.spriteList.get(i);
         sprite.render(Tessellator.getInstance(), p_78788_1_);
      }

      GL11.glEndList();
      this.compiled = true;
   }

   public ModelRenderer setTextureSize(int p_78787_1_, int p_78787_2_) {
      this.textureWidth = (float)p_78787_1_;
      this.textureHeight = (float)p_78787_2_;
      return this;
   }

   public void addSprite(float posX, float posY, float posZ, int sizeX, int sizeY, int sizeZ, float sizeAdd) {
      this.spriteList.add(new ModelSprite(this, this.textureOffsetX, this.textureOffsetY, posX, posY, posZ, sizeX, sizeY, sizeZ, sizeAdd));
   }

   public boolean getCompiled() {
      return this.compiled;
   }

   public int getDisplayList() {
      return this.displayList;
   }

   public void resetDisplayList() {
      if(this.compiled) {
         this.compiled = false;
         this.compileDisplayList(this.savedScale);
      }

   }

   public ResourceLocation getTextureLocation() {
      return this.textureLocation;
   }

   public void setTextureLocation(ResourceLocation textureLocation) {
      this.textureLocation = textureLocation;
   }

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public void addBox(int[][] faceUvs, float x, float y, float z, float dx, float dy, float dz, float delta) {
      this.cubeList.add(new ModelBox(this, faceUvs, x, y, z, dx, dy, dz, delta, this.mirror));
   }

   public ModelRenderer getChild(String name) {
      if(name == null) {
         return null;
      } else {
         if(this.childModels != null) {
            for(int i = 0; i < this.childModels.size(); ++i) {
               ModelRenderer child = (ModelRenderer)this.childModels.get(i);
               if(name.equals(child.getId())) {
                  return child;
               }
            }
         }

         return null;
      }
   }

   public ModelRenderer getChildDeep(String name) {
      if(name == null) {
         return null;
      } else {
         ModelRenderer mrChild = this.getChild(name);
         if(mrChild != null) {
            return mrChild;
         } else {
            if(this.childModels != null) {
               for(int i = 0; i < this.childModels.size(); ++i) {
                  ModelRenderer child = (ModelRenderer)this.childModels.get(i);
                  ModelRenderer mr = child.getChildDeep(name);
                  if(mr != null) {
                     return mr;
                  }
               }
            }

            return null;
         }
      }
   }

   public void setModelUpdater(ModelUpdater modelUpdater) {
      this.modelUpdater = modelUpdater;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("id: " + this.id + ", boxes: " + (this.cubeList != null?Integer.valueOf(this.cubeList.size()):null) + ", submodels: " + (this.childModels != null?Integer.valueOf(this.childModels.size()):null));
      return sb.toString();
   }
}
