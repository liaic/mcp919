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
import net.minecraft.src.Config;
import net.minecraft.src.ModelSprite;
import net.minecraft.util.ResourceLocation;
import net.optifine.entity.model.anim.ModelUpdater;
import org.lwjgl.opengl.GL11;

public class ModelRenderer {
   public float field_78801_a;
   public float field_78799_b;
   private int field_78803_o;
   private int field_78813_p;
   public float field_78800_c;
   public float field_78797_d;
   public float field_78798_e;
   public float field_78795_f;
   public float field_78796_g;
   public float field_78808_h;
   private boolean field_78812_q;
   private int field_78811_r;
   public boolean field_78809_i;
   public boolean field_78806_j;
   public boolean field_78807_k;
   public List field_78804_l;
   public List field_78805_m;
   public final String field_78802_n;
   private ModelBase field_78810_s;
   public float field_82906_o;
   public float field_82908_p;
   public float field_82907_q;
   private static final String __OBFID = "CL_00000874";
   public List spriteList;
   public boolean mirrorV;
   public float scaleX;
   public float scaleY;
   public float scaleZ;
   private float savedScale;
   private ResourceLocation textureLocation;
   private String id;
   private ModelUpdater modelUpdater;
   private RenderGlobal renderGlobal;

   public ModelRenderer(ModelBase p_i1172_1_, String p_i1172_2_) {
      this.spriteList = new ArrayList();
      this.mirrorV = false;
      this.scaleX = 1.0F;
      this.scaleY = 1.0F;
      this.scaleZ = 1.0F;
      this.textureLocation = null;
      this.id = null;
      this.renderGlobal = Config.getRenderGlobal();
      this.field_78801_a = 64.0F;
      this.field_78799_b = 32.0F;
      this.field_78806_j = true;
      this.field_78804_l = Lists.newArrayList();
      this.field_78810_s = p_i1172_1_;
      p_i1172_1_.field_78092_r.add(this);
      this.field_78802_n = p_i1172_2_;
      this.func_78787_b(p_i1172_1_.field_78090_t, p_i1172_1_.field_78089_u);
   }

   public ModelRenderer(ModelBase p_i1173_1_) {
      this(p_i1173_1_, (String)null);
   }

   public ModelRenderer(ModelBase p_i46358_1_, int p_i46358_2_, int p_i46358_3_) {
      this(p_i46358_1_);
      this.func_78784_a(p_i46358_2_, p_i46358_3_);
   }

   public void func_78792_a(ModelRenderer p_78792_1_) {
      if(this.field_78805_m == null) {
         this.field_78805_m = Lists.newArrayList();
      }

      this.field_78805_m.add(p_78792_1_);
   }

   public ModelRenderer func_78784_a(int p_78784_1_, int p_78784_2_) {
      this.field_78803_o = p_78784_1_;
      this.field_78813_p = p_78784_2_;
      return this;
   }

   public ModelRenderer func_78786_a(String p_78786_1_, float p_78786_2_, float p_78786_3_, float p_78786_4_, int p_78786_5_, int p_78786_6_, int p_78786_7_) {
      p_78786_1_ = this.field_78802_n + "." + p_78786_1_;
      TextureOffset textureoffset = this.field_78810_s.func_78084_a(p_78786_1_);
      this.func_78784_a(textureoffset.field_78783_a, textureoffset.field_78782_b);
      this.field_78804_l.add((new ModelBox(this, this.field_78803_o, this.field_78813_p, p_78786_2_, p_78786_3_, p_78786_4_, p_78786_5_, p_78786_6_, p_78786_7_, 0.0F)).func_78244_a(p_78786_1_));
      return this;
   }

   public ModelRenderer func_78789_a(float p_78789_1_, float p_78789_2_, float p_78789_3_, int p_78789_4_, int p_78789_5_, int p_78789_6_) {
      this.field_78804_l.add(new ModelBox(this, this.field_78803_o, this.field_78813_p, p_78789_1_, p_78789_2_, p_78789_3_, p_78789_4_, p_78789_5_, p_78789_6_, 0.0F));
      return this;
   }

   public ModelRenderer func_178769_a(float p_178769_1_, float p_178769_2_, float p_178769_3_, int p_178769_4_, int p_178769_5_, int p_178769_6_, boolean p_178769_7_) {
      this.field_78804_l.add(new ModelBox(this, this.field_78803_o, this.field_78813_p, p_178769_1_, p_178769_2_, p_178769_3_, p_178769_4_, p_178769_5_, p_178769_6_, 0.0F, p_178769_7_));
      return this;
   }

   public void func_78790_a(float p_78790_1_, float p_78790_2_, float p_78790_3_, int p_78790_4_, int p_78790_5_, int p_78790_6_, float p_78790_7_) {
      this.field_78804_l.add(new ModelBox(this, this.field_78803_o, this.field_78813_p, p_78790_1_, p_78790_2_, p_78790_3_, p_78790_4_, p_78790_5_, p_78790_6_, p_78790_7_));
   }

   public void func_78793_a(float p_78793_1_, float p_78793_2_, float p_78793_3_) {
      this.field_78800_c = p_78793_1_;
      this.field_78797_d = p_78793_2_;
      this.field_78798_e = p_78793_3_;
   }

   public void func_78785_a(float p_78785_1_) {
      if(!this.field_78807_k && this.field_78806_j) {
         if(!this.field_78812_q) {
            this.func_78788_d(p_78785_1_);
         }

         int i = 0;
         if(this.textureLocation != null && !this.renderGlobal.renderOverlayDamaged) {
            if(this.renderGlobal.renderOverlayEyes) {
               return;
            }

            i = GlStateManager.getBoundTexture();
            Config.getTextureManager().func_110577_a(this.textureLocation);
         }

         if(this.modelUpdater != null) {
            this.modelUpdater.update();
         }

         boolean flag = this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F;
         GlStateManager.func_179109_b(this.field_82906_o, this.field_82908_p, this.field_82907_q);
         if(this.field_78795_f == 0.0F && this.field_78796_g == 0.0F && this.field_78808_h == 0.0F) {
            if(this.field_78800_c == 0.0F && this.field_78797_d == 0.0F && this.field_78798_e == 0.0F) {
               if(flag) {
                  GlStateManager.func_179152_a(this.scaleX, this.scaleY, this.scaleZ);
               }

               GlStateManager.func_179148_o(this.field_78811_r);
               if(this.field_78805_m != null) {
                  for(int l = 0; l < this.field_78805_m.size(); ++l) {
                     ((ModelRenderer)this.field_78805_m.get(l)).func_78785_a(p_78785_1_);
                  }
               }

               if(flag) {
                  GlStateManager.func_179152_a(1.0F / this.scaleX, 1.0F / this.scaleY, 1.0F / this.scaleZ);
               }
            } else {
               GlStateManager.func_179109_b(this.field_78800_c * p_78785_1_, this.field_78797_d * p_78785_1_, this.field_78798_e * p_78785_1_);
               if(flag) {
                  GlStateManager.func_179152_a(this.scaleX, this.scaleY, this.scaleZ);
               }

               GlStateManager.func_179148_o(this.field_78811_r);
               if(this.field_78805_m != null) {
                  for(int k = 0; k < this.field_78805_m.size(); ++k) {
                     ((ModelRenderer)this.field_78805_m.get(k)).func_78785_a(p_78785_1_);
                  }
               }

               if(flag) {
                  GlStateManager.func_179152_a(1.0F / this.scaleX, 1.0F / this.scaleY, 1.0F / this.scaleZ);
               }

               GlStateManager.func_179109_b(-this.field_78800_c * p_78785_1_, -this.field_78797_d * p_78785_1_, -this.field_78798_e * p_78785_1_);
            }
         } else {
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b(this.field_78800_c * p_78785_1_, this.field_78797_d * p_78785_1_, this.field_78798_e * p_78785_1_);
            if(this.field_78808_h != 0.0F) {
               GlStateManager.func_179114_b(this.field_78808_h * 57.295776F, 0.0F, 0.0F, 1.0F);
            }

            if(this.field_78796_g != 0.0F) {
               GlStateManager.func_179114_b(this.field_78796_g * 57.295776F, 0.0F, 1.0F, 0.0F);
            }

            if(this.field_78795_f != 0.0F) {
               GlStateManager.func_179114_b(this.field_78795_f * 57.295776F, 1.0F, 0.0F, 0.0F);
            }

            if(flag) {
               GlStateManager.func_179152_a(this.scaleX, this.scaleY, this.scaleZ);
            }

            GlStateManager.func_179148_o(this.field_78811_r);
            if(this.field_78805_m != null) {
               for(int j = 0; j < this.field_78805_m.size(); ++j) {
                  ((ModelRenderer)this.field_78805_m.get(j)).func_78785_a(p_78785_1_);
               }
            }

            GlStateManager.func_179121_F();
         }

         GlStateManager.func_179109_b(-this.field_82906_o, -this.field_82908_p, -this.field_82907_q);
         if(i != 0) {
            GlStateManager.func_179144_i(i);
         }
      }

   }

   public void func_78791_b(float p_78791_1_) {
      if(!this.field_78807_k && this.field_78806_j) {
         if(!this.field_78812_q) {
            this.func_78788_d(p_78791_1_);
         }

         int i = 0;
         if(this.textureLocation != null && !this.renderGlobal.renderOverlayDamaged) {
            if(this.renderGlobal.renderOverlayEyes) {
               return;
            }

            i = GlStateManager.getBoundTexture();
            Config.getTextureManager().func_110577_a(this.textureLocation);
         }

         if(this.modelUpdater != null) {
            this.modelUpdater.update();
         }

         boolean flag = this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F;
         GlStateManager.func_179094_E();
         GlStateManager.func_179109_b(this.field_78800_c * p_78791_1_, this.field_78797_d * p_78791_1_, this.field_78798_e * p_78791_1_);
         if(this.field_78796_g != 0.0F) {
            GlStateManager.func_179114_b(this.field_78796_g * 57.295776F, 0.0F, 1.0F, 0.0F);
         }

         if(this.field_78795_f != 0.0F) {
            GlStateManager.func_179114_b(this.field_78795_f * 57.295776F, 1.0F, 0.0F, 0.0F);
         }

         if(this.field_78808_h != 0.0F) {
            GlStateManager.func_179114_b(this.field_78808_h * 57.295776F, 0.0F, 0.0F, 1.0F);
         }

         if(flag) {
            GlStateManager.func_179152_a(this.scaleX, this.scaleY, this.scaleZ);
         }

         GlStateManager.func_179148_o(this.field_78811_r);
         if(this.field_78805_m != null) {
            for(int j = 0; j < this.field_78805_m.size(); ++j) {
               ((ModelRenderer)this.field_78805_m.get(j)).func_78785_a(p_78791_1_);
            }
         }

         GlStateManager.func_179121_F();
         if(i != 0) {
            GlStateManager.func_179144_i(i);
         }
      }

   }

   public void func_78794_c(float p_78794_1_) {
      if(!this.field_78807_k && this.field_78806_j) {
         if(!this.field_78812_q) {
            this.func_78788_d(p_78794_1_);
         }

         if(this.field_78795_f == 0.0F && this.field_78796_g == 0.0F && this.field_78808_h == 0.0F) {
            if(this.field_78800_c != 0.0F || this.field_78797_d != 0.0F || this.field_78798_e != 0.0F) {
               GlStateManager.func_179109_b(this.field_78800_c * p_78794_1_, this.field_78797_d * p_78794_1_, this.field_78798_e * p_78794_1_);
            }
         } else {
            GlStateManager.func_179109_b(this.field_78800_c * p_78794_1_, this.field_78797_d * p_78794_1_, this.field_78798_e * p_78794_1_);
            if(this.field_78808_h != 0.0F) {
               GlStateManager.func_179114_b(this.field_78808_h * 57.295776F, 0.0F, 0.0F, 1.0F);
            }

            if(this.field_78796_g != 0.0F) {
               GlStateManager.func_179114_b(this.field_78796_g * 57.295776F, 0.0F, 1.0F, 0.0F);
            }

            if(this.field_78795_f != 0.0F) {
               GlStateManager.func_179114_b(this.field_78795_f * 57.295776F, 1.0F, 0.0F, 0.0F);
            }
         }
      }

   }

   private void func_78788_d(float p_78788_1_) {
      if(this.field_78811_r == 0) {
         this.savedScale = p_78788_1_;
         this.field_78811_r = GLAllocation.func_74526_a(1);
      }

      GL11.glNewList(this.field_78811_r, 4864);
      WorldRenderer worldrenderer = Tessellator.func_178181_a().func_178180_c();

      for(int i = 0; i < this.field_78804_l.size(); ++i) {
         ((ModelBox)this.field_78804_l.get(i)).func_178780_a(worldrenderer, p_78788_1_);
      }

      for(int j = 0; j < this.spriteList.size(); ++j) {
         ModelSprite modelsprite = (ModelSprite)this.spriteList.get(j);
         modelsprite.render(Tessellator.func_178181_a(), p_78788_1_);
      }

      GL11.glEndList();
      this.field_78812_q = true;
   }

   public ModelRenderer func_78787_b(int p_78787_1_, int p_78787_2_) {
      this.field_78801_a = (float)p_78787_1_;
      this.field_78799_b = (float)p_78787_2_;
      return this;
   }

   public void addSprite(float p_addSprite_1_, float p_addSprite_2_, float p_addSprite_3_, int p_addSprite_4_, int p_addSprite_5_, int p_addSprite_6_, float p_addSprite_7_) {
      this.spriteList.add(new ModelSprite(this, this.field_78803_o, this.field_78813_p, p_addSprite_1_, p_addSprite_2_, p_addSprite_3_, p_addSprite_4_, p_addSprite_5_, p_addSprite_6_, p_addSprite_7_));
   }

   public boolean getCompiled() {
      return this.field_78812_q;
   }

   public int getDisplayList() {
      return this.field_78811_r;
   }

   public void resetDisplayList() {
      if(this.field_78812_q) {
         this.field_78812_q = false;
         this.func_78788_d(this.savedScale);
      }

   }

   public ResourceLocation getTextureLocation() {
      return this.textureLocation;
   }

   public void setTextureLocation(ResourceLocation p_setTextureLocation_1_) {
      this.textureLocation = p_setTextureLocation_1_;
   }

   public String getId() {
      return this.id;
   }

   public void setId(String p_setId_1_) {
      this.id = p_setId_1_;
   }

   public void addBox(int[][] p_addBox_1_, float p_addBox_2_, float p_addBox_3_, float p_addBox_4_, float p_addBox_5_, float p_addBox_6_, float p_addBox_7_, float p_addBox_8_) {
      this.field_78804_l.add(new ModelBox(this, p_addBox_1_, p_addBox_2_, p_addBox_3_, p_addBox_4_, p_addBox_5_, p_addBox_6_, p_addBox_7_, p_addBox_8_, this.field_78809_i));
   }

   public ModelRenderer getChild(String p_getChild_1_) {
      if(p_getChild_1_ == null) {
         return null;
      } else {
         if(this.field_78805_m != null) {
            for(int i = 0; i < this.field_78805_m.size(); ++i) {
               ModelRenderer modelrenderer = (ModelRenderer)this.field_78805_m.get(i);
               if(p_getChild_1_.equals(modelrenderer.getId())) {
                  return modelrenderer;
               }
            }
         }

         return null;
      }
   }

   public ModelRenderer getChildDeep(String p_getChildDeep_1_) {
      if(p_getChildDeep_1_ == null) {
         return null;
      } else {
         ModelRenderer modelrenderer = this.getChild(p_getChildDeep_1_);
         if(modelrenderer != null) {
            return modelrenderer;
         } else {
            if(this.field_78805_m != null) {
               for(int i = 0; i < this.field_78805_m.size(); ++i) {
                  ModelRenderer modelrenderer1 = (ModelRenderer)this.field_78805_m.get(i);
                  ModelRenderer modelrenderer2 = modelrenderer1.getChildDeep(p_getChildDeep_1_);
                  if(modelrenderer2 != null) {
                     return modelrenderer2;
                  }
               }
            }

            return null;
         }
      }
   }

   public void setModelUpdater(ModelUpdater p_setModelUpdater_1_) {
      this.modelUpdater = p_setModelUpdater_1_;
   }

   public String toString() {
      StringBuffer stringbuffer = new StringBuffer();
      stringbuffer.append("id: " + this.id + ", boxes: " + (this.field_78804_l != null?Integer.valueOf(this.field_78804_l.size()):null) + ", submodels: " + (this.field_78805_m != null?Integer.valueOf(this.field_78805_m.size()):null));
      return stringbuffer.toString();
   }
}
