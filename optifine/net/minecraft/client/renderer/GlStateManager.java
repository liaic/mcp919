package net.minecraft.client.renderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import net.minecraft.client.renderer.OpenGlHelper;
import optifine.Config;
import optifine.GlBlendState;
import org.lwjgl.opengl.GL11;

public class GlStateManager {

   public static GlStateManager.AlphaState alphaState = new GlStateManager.AlphaState((GlStateManager.SwitchTexGen)null);
   public static GlStateManager.BooleanState lightingState = new GlStateManager.BooleanState(2896);
   public static GlStateManager.BooleanState[] lightState = new GlStateManager.BooleanState[8];
   public static GlStateManager.ColorMaterialState colorMaterialState = new GlStateManager.ColorMaterialState((GlStateManager.SwitchTexGen)null);
   public static GlStateManager.BlendState blendState = new GlStateManager.BlendState((GlStateManager.SwitchTexGen)null);
   public static GlStateManager.DepthState depthState = new GlStateManager.DepthState((GlStateManager.SwitchTexGen)null);
   public static GlStateManager.FogState fogState = new GlStateManager.FogState((GlStateManager.SwitchTexGen)null);
   public static GlStateManager.CullState cullState = new GlStateManager.CullState((GlStateManager.SwitchTexGen)null);
   public static GlStateManager.PolygonOffsetState polygonOffsetState = new GlStateManager.PolygonOffsetState((GlStateManager.SwitchTexGen)null);
   public static GlStateManager.ColorLogicState colorLogicState = new GlStateManager.ColorLogicState((GlStateManager.SwitchTexGen)null);
   public static GlStateManager.TexGenState texGenState = new GlStateManager.TexGenState((GlStateManager.SwitchTexGen)null);
   public static GlStateManager.ClearState clearState = new GlStateManager.ClearState((GlStateManager.SwitchTexGen)null);
   public static GlStateManager.StencilState stencilState = new GlStateManager.StencilState((GlStateManager.SwitchTexGen)null);
   public static GlStateManager.BooleanState normalizeState = new GlStateManager.BooleanState(2977);
   public static int activeTextureUnit = 0;
   public static GlStateManager.TextureState[] textureState = new GlStateManager.TextureState[32];
   public static int activeShadeModel = 7425;
   public static GlStateManager.BooleanState rescaleNormalState = new GlStateManager.BooleanState('\u803a');
   public static GlStateManager.ColorMask colorMaskState = new GlStateManager.ColorMask((GlStateManager.SwitchTexGen)null);
   public static GlStateManager.Color colorState = new GlStateManager.Color();
   public static final String __OBFID = "CL_00002558";
   public static boolean clearEnabled = true;


   public static void pushAttrib() {
      GL11.glPushAttrib(8256);
   }

   public static void popAttrib() {
      GL11.glPopAttrib();
   }

   public static void disableAlpha() {
      alphaState.alphaTest.setDisabled();
   }

   public static void enableAlpha() {
      alphaState.alphaTest.setEnabled();
   }

   public static void alphaFunc(int p_179092_0_, float p_179092_1_) {
      if(p_179092_0_ != alphaState.func || p_179092_1_ != alphaState.ref) {
         alphaState.func = p_179092_0_;
         alphaState.ref = p_179092_1_;
         GL11.glAlphaFunc(p_179092_0_, p_179092_1_);
      }

   }

   public static void enableLighting() {
      lightingState.setEnabled();
   }

   public static void disableLighting() {
      lightingState.setDisabled();
   }

   public static void enableLight(int p_179085_0_) {
      lightState[p_179085_0_].setEnabled();
   }

   public static void disableLight(int p_179122_0_) {
      lightState[p_179122_0_].setDisabled();
   }

   public static void enableColorMaterial() {
      colorMaterialState.colorMaterial.setEnabled();
   }

   public static void disableColorMaterial() {
      colorMaterialState.colorMaterial.setDisabled();
   }

   public static void colorMaterial(int p_179104_0_, int p_179104_1_) {
      if(p_179104_0_ != colorMaterialState.face || p_179104_1_ != colorMaterialState.mode) {
         colorMaterialState.face = p_179104_0_;
         colorMaterialState.mode = p_179104_1_;
         GL11.glColorMaterial(p_179104_0_, p_179104_1_);
      }

   }

   public static void disableDepth() {
      depthState.depthTest.setDisabled();
   }

   public static void enableDepth() {
      depthState.depthTest.setEnabled();
   }

   public static void depthFunc(int p_179143_0_) {
      if(p_179143_0_ != depthState.depthFunc) {
         depthState.depthFunc = p_179143_0_;
         GL11.glDepthFunc(p_179143_0_);
      }

   }

   public static void depthMask(boolean p_179132_0_) {
      if(p_179132_0_ != depthState.maskEnabled) {
         depthState.maskEnabled = p_179132_0_;
         GL11.glDepthMask(p_179132_0_);
      }

   }

   public static void disableBlend() {
      blendState.blend.setDisabled();
   }

   public static void enableBlend() {
      blendState.blend.setEnabled();
   }

   public static void blendFunc(int p_179112_0_, int p_179112_1_) {
      if(p_179112_0_ != blendState.srcFactor || p_179112_1_ != blendState.dstFactor) {
         blendState.srcFactor = p_179112_0_;
         blendState.dstFactor = p_179112_1_;
         GL11.glBlendFunc(p_179112_0_, p_179112_1_);
      }

   }

   public static void tryBlendFuncSeparate(int p_179120_0_, int p_179120_1_, int p_179120_2_, int p_179120_3_) {
      if(p_179120_0_ != blendState.srcFactor || p_179120_1_ != blendState.dstFactor || p_179120_2_ != blendState.srcFactorAlpha || p_179120_3_ != blendState.dstFactorAlpha) {
         blendState.srcFactor = p_179120_0_;
         blendState.dstFactor = p_179120_1_;
         blendState.srcFactorAlpha = p_179120_2_;
         blendState.dstFactorAlpha = p_179120_3_;
         OpenGlHelper.glBlendFunc(p_179120_0_, p_179120_1_, p_179120_2_, p_179120_3_);
      }

   }

   public static void enableFog() {
      fogState.fog.setEnabled();
   }

   public static void disableFog() {
      fogState.fog.setDisabled();
   }

   public static void setFog(int p_179093_0_) {
      if(p_179093_0_ != fogState.mode) {
         fogState.mode = p_179093_0_;
         GL11.glFogi(2917, p_179093_0_);
      }

   }

   public static void setFogDensity(float p_179095_0_) {
      if(p_179095_0_ != fogState.density) {
         fogState.density = p_179095_0_;
         GL11.glFogf(2914, p_179095_0_);
      }

   }

   public static void setFogStart(float p_179102_0_) {
      if(p_179102_0_ != fogState.start) {
         fogState.start = p_179102_0_;
         GL11.glFogf(2915, p_179102_0_);
      }

   }

   public static void setFogEnd(float p_179153_0_) {
      if(p_179153_0_ != fogState.end) {
         fogState.end = p_179153_0_;
         GL11.glFogf(2916, p_179153_0_);
      }

   }

   public static void enableCull() {
      cullState.cullFace.setEnabled();
   }

   public static void disableCull() {
      cullState.cullFace.setDisabled();
   }

   public static void cullFace(int p_179107_0_) {
      if(p_179107_0_ != cullState.mode) {
         cullState.mode = p_179107_0_;
         GL11.glCullFace(p_179107_0_);
      }

   }

   public static void enablePolygonOffset() {
      polygonOffsetState.polygonOffsetFill.setEnabled();
   }

   public static void disablePolygonOffset() {
      polygonOffsetState.polygonOffsetFill.setDisabled();
   }

   public static void doPolygonOffset(float p_179136_0_, float p_179136_1_) {
      if(p_179136_0_ != polygonOffsetState.factor || p_179136_1_ != polygonOffsetState.units) {
         polygonOffsetState.factor = p_179136_0_;
         polygonOffsetState.units = p_179136_1_;
         GL11.glPolygonOffset(p_179136_0_, p_179136_1_);
      }

   }

   public static void enableColorLogic() {
      colorLogicState.colorLogicOp.setEnabled();
   }

   public static void disableColorLogic() {
      colorLogicState.colorLogicOp.setDisabled();
   }

   public static void colorLogicOp(int p_179116_0_) {
      if(p_179116_0_ != colorLogicState.opcode) {
         colorLogicState.opcode = p_179116_0_;
         GL11.glLogicOp(p_179116_0_);
      }

   }

   public static void enableTexGenCoord(GlStateManager.TexGen p_179087_0_) {
      texGenCoord(p_179087_0_).textureGen.setEnabled();
   }

   public static void disableTexGenCoord(GlStateManager.TexGen p_179100_0_) {
      texGenCoord(p_179100_0_).textureGen.setDisabled();
   }

   public static void texGen(GlStateManager.TexGen p_179149_0_, int p_179149_1_) {
      GlStateManager.TexGenCoord var2 = texGenCoord(p_179149_0_);
      if(p_179149_1_ != var2.param) {
         var2.param = p_179149_1_;
         GL11.glTexGeni(var2.coord, 9472, p_179149_1_);
      }

   }

   public static void texGen(GlStateManager.TexGen p_179105_0_, int p_179105_1_, FloatBuffer p_179105_2_) {
      GL11.glTexGen(texGenCoord(p_179105_0_).coord, p_179105_1_, p_179105_2_);
   }

   public static GlStateManager.TexGenCoord texGenCoord(GlStateManager.TexGen p_179125_0_) {
      switch(GlStateManager.SwitchTexGen.field_179175_a[p_179125_0_.ordinal()]) {
      case 1:
         return texGenState.s;
      case 2:
         return texGenState.t;
      case 3:
         return texGenState.r;
      case 4:
         return texGenState.q;
      default:
         return texGenState.s;
      }
   }

   public static void setActiveTexture(int p_179138_0_) {
      if(activeTextureUnit != p_179138_0_ - OpenGlHelper.defaultTexUnit) {
         activeTextureUnit = p_179138_0_ - OpenGlHelper.defaultTexUnit;
         OpenGlHelper.setActiveTexture(p_179138_0_);
      }

   }

   public static void enableTexture2D() {
      textureState[activeTextureUnit].texture2DState.setEnabled();
   }

   public static void disableTexture2D() {
      textureState[activeTextureUnit].texture2DState.setDisabled();
   }

   public static int generateTexture() {
      return GL11.glGenTextures();
   }

   public static void deleteTexture(int p_179150_0_) {
      if(p_179150_0_ != 0) {
         GL11.glDeleteTextures(p_179150_0_);
         GlStateManager.TextureState[] var1 = textureState;
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            GlStateManager.TextureState var4 = var1[var3];
            if(var4.textureName == p_179150_0_) {
               var4.textureName = 0;
            }
         }

      }
   }

   public static void bindTexture(int p_179144_0_) {
      if(p_179144_0_ != textureState[activeTextureUnit].textureName) {
         textureState[activeTextureUnit].textureName = p_179144_0_;
         GL11.glBindTexture(3553, p_179144_0_);
      }

   }

   public static void bindCurrentTexture() {
      GL11.glBindTexture(3553, textureState[activeTextureUnit].textureName);
   }

   public static void enableNormalize() {
      normalizeState.setEnabled();
   }

   public static void disableNormalize() {
      normalizeState.setDisabled();
   }

   public static void shadeModel(int p_179103_0_) {
      if(p_179103_0_ != activeShadeModel) {
         activeShadeModel = p_179103_0_;
         GL11.glShadeModel(p_179103_0_);
      }

   }

   public static void enableRescaleNormal() {
      rescaleNormalState.setEnabled();
   }

   public static void disableRescaleNormal() {
      rescaleNormalState.setDisabled();
   }

   public static void viewport(int p_179083_0_, int p_179083_1_, int p_179083_2_, int p_179083_3_) {
      GL11.glViewport(p_179083_0_, p_179083_1_, p_179083_2_, p_179083_3_);
   }

   public static void colorMask(boolean p_179135_0_, boolean p_179135_1_, boolean p_179135_2_, boolean p_179135_3_) {
      if(p_179135_0_ != colorMaskState.red || p_179135_1_ != colorMaskState.green || p_179135_2_ != colorMaskState.blue || p_179135_3_ != colorMaskState.alpha) {
         colorMaskState.red = p_179135_0_;
         colorMaskState.green = p_179135_1_;
         colorMaskState.blue = p_179135_2_;
         colorMaskState.alpha = p_179135_3_;
         GL11.glColorMask(p_179135_0_, p_179135_1_, p_179135_2_, p_179135_3_);
      }

   }

   public static void clearDepth(double p_179151_0_) {
      if(p_179151_0_ != clearState.depth) {
         clearState.depth = p_179151_0_;
         GL11.glClearDepth(p_179151_0_);
      }

   }

   public static void clearColor(float p_179082_0_, float p_179082_1_, float p_179082_2_, float p_179082_3_) {
      if(p_179082_0_ != clearState.color.red || p_179082_1_ != clearState.color.green || p_179082_2_ != clearState.color.blue || p_179082_3_ != clearState.color.alpha) {
         clearState.color.red = p_179082_0_;
         clearState.color.green = p_179082_1_;
         clearState.color.blue = p_179082_2_;
         clearState.color.alpha = p_179082_3_;
         GL11.glClearColor(p_179082_0_, p_179082_1_, p_179082_2_, p_179082_3_);
      }

   }

   public static void clear(int p_179086_0_) {
      if(clearEnabled) {
         GL11.glClear(p_179086_0_);
      }
   }

   public static void matrixMode(int p_179128_0_) {
      GL11.glMatrixMode(p_179128_0_);
   }

   public static void loadIdentity() {
      GL11.glLoadIdentity();
   }

   public static void pushMatrix() {
      GL11.glPushMatrix();
   }

   public static void popMatrix() {
      GL11.glPopMatrix();
   }

   public static void getFloat(int p_179111_0_, FloatBuffer p_179111_1_) {
      GL11.glGetFloat(p_179111_0_, p_179111_1_);
   }

   public static void ortho(double p_179130_0_, double p_179130_2_, double p_179130_4_, double p_179130_6_, double p_179130_8_, double p_179130_10_) {
      GL11.glOrtho(p_179130_0_, p_179130_2_, p_179130_4_, p_179130_6_, p_179130_8_, p_179130_10_);
   }

   public static void rotate(float p_179114_0_, float p_179114_1_, float p_179114_2_, float p_179114_3_) {
      GL11.glRotatef(p_179114_0_, p_179114_1_, p_179114_2_, p_179114_3_);
   }

   public static void scale(float p_179152_0_, float p_179152_1_, float p_179152_2_) {
      GL11.glScalef(p_179152_0_, p_179152_1_, p_179152_2_);
   }

   public static void scale(double p_179139_0_, double p_179139_2_, double p_179139_4_) {
      GL11.glScaled(p_179139_0_, p_179139_2_, p_179139_4_);
   }

   public static void translate(float p_179109_0_, float p_179109_1_, float p_179109_2_) {
      GL11.glTranslatef(p_179109_0_, p_179109_1_, p_179109_2_);
   }

   public static void translate(double p_179137_0_, double p_179137_2_, double p_179137_4_) {
      GL11.glTranslated(p_179137_0_, p_179137_2_, p_179137_4_);
   }

   public static void multMatrix(FloatBuffer p_179110_0_) {
      GL11.glMultMatrix(p_179110_0_);
   }

   public static void color(float p_179131_0_, float p_179131_1_, float p_179131_2_, float p_179131_3_) {
      if(p_179131_0_ != colorState.red || p_179131_1_ != colorState.green || p_179131_2_ != colorState.blue || p_179131_3_ != colorState.alpha) {
         colorState.red = p_179131_0_;
         colorState.green = p_179131_1_;
         colorState.blue = p_179131_2_;
         colorState.alpha = p_179131_3_;
         GL11.glColor4f(p_179131_0_, p_179131_1_, p_179131_2_, p_179131_3_);
      }

   }

   public static void color(float p_179124_0_, float p_179124_1_, float p_179124_2_) {
      color(p_179124_0_, p_179124_1_, p_179124_2_, 1.0F);
   }

   public static void resetColor() {
      colorState.red = colorState.green = colorState.blue = colorState.alpha = -1.0F;
   }

   public static void callList(int p_179148_0_) {
      GL11.glCallList(p_179148_0_);
   }

   public static int getActiveTextureUnit() {
      return OpenGlHelper.defaultTexUnit + activeTextureUnit;
   }

   public static int getBoundTexture() {
      return textureState[activeTextureUnit].textureName;
   }

   public static void checkBoundTexture() {
      if(Config.isMinecraftThread()) {
         int glAct = GL11.glGetInteger('\u84e0');
         int glTex = GL11.glGetInteger('\u8069');
         int act = getActiveTextureUnit();
         int tex = getBoundTexture();
         if(tex > 0) {
            if(glAct != act || glTex != tex) {
               Config.dbg("checkTexture: act: " + act + ", glAct: " + glAct + ", tex: " + tex + ", glTex: " + glTex);
            }

         }
      }
   }

   public static void deleteTextures(IntBuffer buf) {
      buf.rewind();

      while(buf.position() < buf.limit()) {
         int texId = buf.get();
         deleteTexture(texId);
      }

      buf.rewind();
   }

   public static boolean isFogEnabled() {
      return fogState.fog.currentState;
   }

   public static void setFogEnabled(boolean state) {
      fogState.fog.setState(state);
   }

   public static void getBlendState(GlBlendState gbs) {
      gbs.enabled = blendState.blend.currentState;
      gbs.srcFactor = blendState.srcFactor;
      gbs.dstFactor = blendState.dstFactor;
   }

   public static void setBlendState(GlBlendState gbs) {
      blendState.blend.setState(gbs.enabled);
      blendFunc(gbs.srcFactor, gbs.dstFactor);
   }

   static {
      int var0;
      for(var0 = 0; var0 < 8; ++var0) {
         lightState[var0] = new GlStateManager.BooleanState(16384 + var0);
      }

      for(var0 = 0; var0 < textureState.length; ++var0) {
         textureState[var0] = new GlStateManager.TextureState((GlStateManager.SwitchTexGen)null);
      }

   }

   public static final class SwitchTexGen {

      public static final int[] field_179175_a = new int[GlStateManager.TexGen.values().length];
      public static final String __OBFID = "CL_00002557";


      static {
         try {
            field_179175_a[GlStateManager.TexGen.S.ordinal()] = 1;
         } catch (NoSuchFieldError var4) {
            ;
         }

         try {
            field_179175_a[GlStateManager.TexGen.T.ordinal()] = 2;
         } catch (NoSuchFieldError var3) {
            ;
         }

         try {
            field_179175_a[GlStateManager.TexGen.R.ordinal()] = 3;
         } catch (NoSuchFieldError var2) {
            ;
         }

         try {
            field_179175_a[GlStateManager.TexGen.Q.ordinal()] = 4;
         } catch (NoSuchFieldError var1) {
            ;
         }

      }
   }

   public static class AlphaState {

      public GlStateManager.BooleanState alphaTest;
      public int func;
      public float ref;
      public static final String __OBFID = "CL_00002556";


      public AlphaState() {
         this.alphaTest = new GlStateManager.BooleanState(3008);
         this.func = 519;
         this.ref = -1.0F;
      }

      public AlphaState(GlStateManager.SwitchTexGen p_i46269_1_) {
         this();
      }
   }

   public static class BlendState {

      public GlStateManager.BooleanState blend;
      public int srcFactor;
      public int dstFactor;
      public int srcFactorAlpha;
      public int dstFactorAlpha;
      public static final String __OBFID = "CL_00002555";


      public BlendState() {
         this.blend = new GlStateManager.BooleanState(3042);
         this.srcFactor = 1;
         this.dstFactor = 0;
         this.srcFactorAlpha = 1;
         this.dstFactorAlpha = 0;
      }

      public BlendState(GlStateManager.SwitchTexGen p_i46268_1_) {
         this();
      }
   }

   public static class BooleanState {

      public final int capability;
      public boolean currentState = false;
      public static final String __OBFID = "CL_00002554";


      public BooleanState(int p_i46267_1_) {
         this.capability = p_i46267_1_;
      }

      public void setDisabled() {
         this.setState(false);
      }

      public void setEnabled() {
         this.setState(true);
      }

      public void setState(boolean p_179199_1_) {
         if(p_179199_1_ != this.currentState) {
            this.currentState = p_179199_1_;
            if(p_179199_1_) {
               GL11.glEnable(this.capability);
            } else {
               GL11.glDisable(this.capability);
            }
         }

      }
   }

   public static class ClearState {

      public double depth;
      public GlStateManager.Color color;
      public int field_179204_c;
      public static final String __OBFID = "CL_00002553";


      public ClearState() {
         this.depth = 1.0D;
         this.color = new GlStateManager.Color(0.0F, 0.0F, 0.0F, 0.0F);
         this.field_179204_c = 0;
      }

      public ClearState(GlStateManager.SwitchTexGen p_i46266_1_) {
         this();
      }
   }

   public static class Color {

      public float red = 1.0F;
      public float green = 1.0F;
      public float blue = 1.0F;
      public float alpha = 1.0F;
      public static final String __OBFID = "CL_00002552";


      public Color() {}

      public Color(float p_i46265_1_, float p_i46265_2_, float p_i46265_3_, float p_i46265_4_) {
         this.red = p_i46265_1_;
         this.green = p_i46265_2_;
         this.blue = p_i46265_3_;
         this.alpha = p_i46265_4_;
      }
   }

   public static class ColorLogicState {

      public GlStateManager.BooleanState colorLogicOp;
      public int opcode;
      public static final String __OBFID = "CL_00002551";


      public ColorLogicState() {
         this.colorLogicOp = new GlStateManager.BooleanState(3058);
         this.opcode = 5379;
      }

      public ColorLogicState(GlStateManager.SwitchTexGen p_i46264_1_) {
         this();
      }
   }

   public static class ColorMask {

      public boolean red;
      public boolean green;
      public boolean blue;
      public boolean alpha;
      public static final String __OBFID = "CL_00002550";


      public ColorMask() {
         this.red = true;
         this.green = true;
         this.blue = true;
         this.alpha = true;
      }

      public ColorMask(GlStateManager.SwitchTexGen p_i46263_1_) {
         this();
      }
   }

   public static class ColorMaterialState {

      public GlStateManager.BooleanState colorMaterial;
      public int face;
      public int mode;
      public static final String __OBFID = "CL_00002549";


      public ColorMaterialState() {
         this.colorMaterial = new GlStateManager.BooleanState(2903);
         this.face = 1032;
         this.mode = 5634;
      }

      public ColorMaterialState(GlStateManager.SwitchTexGen p_i46262_1_) {
         this();
      }
   }

   public static class CullState {

      public GlStateManager.BooleanState cullFace;
      public int mode;
      public static final String __OBFID = "CL_00002548";


      public CullState() {
         this.cullFace = new GlStateManager.BooleanState(2884);
         this.mode = 1029;
      }

      public CullState(GlStateManager.SwitchTexGen p_i46261_1_) {
         this();
      }
   }

   public static class DepthState {

      public GlStateManager.BooleanState depthTest;
      public boolean maskEnabled;
      public int depthFunc;
      public static final String __OBFID = "CL_00002547";


      public DepthState() {
         this.depthTest = new GlStateManager.BooleanState(2929);
         this.maskEnabled = true;
         this.depthFunc = 513;
      }

      public DepthState(GlStateManager.SwitchTexGen p_i46260_1_) {
         this();
      }
   }

   public static class FogState {

      public GlStateManager.BooleanState fog;
      public int mode;
      public float density;
      public float start;
      public float end;
      public static final String __OBFID = "CL_00002546";


      public FogState() {
         this.fog = new GlStateManager.BooleanState(2912);
         this.mode = 2048;
         this.density = 1.0F;
         this.start = 0.0F;
         this.end = 1.0F;
      }

      public FogState(GlStateManager.SwitchTexGen p_i46259_1_) {
         this();
      }
   }

   public static class PolygonOffsetState {

      public GlStateManager.BooleanState polygonOffsetFill;
      public GlStateManager.BooleanState polygonOffsetLine;
      public float factor;
      public float units;
      public static final String __OBFID = "CL_00002545";


      public PolygonOffsetState() {
         this.polygonOffsetFill = new GlStateManager.BooleanState('\u8037');
         this.polygonOffsetLine = new GlStateManager.BooleanState(10754);
         this.factor = 0.0F;
         this.units = 0.0F;
      }

      public PolygonOffsetState(GlStateManager.SwitchTexGen p_i46258_1_) {
         this();
      }
   }

   public static class StencilFunc {

      public int field_179081_a;
      public int field_179079_b;
      public int field_179080_c;
      public static final String __OBFID = "CL_00002544";


      public StencilFunc() {
         this.field_179081_a = 519;
         this.field_179079_b = 0;
         this.field_179080_c = -1;
      }

      public StencilFunc(GlStateManager.SwitchTexGen p_i46257_1_) {
         this();
      }
   }

   public static class StencilState {

      public GlStateManager.StencilFunc field_179078_a;
      public int field_179076_b;
      public int field_179077_c;
      public int field_179074_d;
      public int field_179075_e;
      public static final String __OBFID = "CL_00002543";


      public StencilState() {
         this.field_179078_a = new GlStateManager.StencilFunc((GlStateManager.SwitchTexGen)null);
         this.field_179076_b = -1;
         this.field_179077_c = 7680;
         this.field_179074_d = 7680;
         this.field_179075_e = 7680;
      }

      public StencilState(GlStateManager.SwitchTexGen p_i46256_1_) {
         this();
      }
   }

   public static enum TexGen {

      S("S", 0, "S", 0),
      T("T", 1, "T", 1),
      R("R", 2, "R", 2),
      Q("Q", 3, "Q", 3);
      public static final GlStateManager.TexGen[] $VALUES = new GlStateManager.TexGen[]{S, T, R, Q};
      public static final String __OBFID = "CL_00002542";
      // $FF: synthetic field
      public static final GlStateManager.TexGen[] $VALUES$ = new GlStateManager.TexGen[]{S, T, R, Q};


      public TexGen(String var1, int var2, String p_i46255_1_, int p_i46255_2_) {}

   }

   public static class TexGenCoord {

      public GlStateManager.BooleanState textureGen;
      public int coord;
      public int param = -1;
      public static final String __OBFID = "CL_00002541";


      public TexGenCoord(int p_i46254_1_, int p_i46254_2_) {
         this.coord = p_i46254_1_;
         this.textureGen = new GlStateManager.BooleanState(p_i46254_2_);
      }
   }

   public static class TexGenState {

      public GlStateManager.TexGenCoord s;
      public GlStateManager.TexGenCoord t;
      public GlStateManager.TexGenCoord r;
      public GlStateManager.TexGenCoord q;
      public static final String __OBFID = "CL_00002540";


      public TexGenState() {
         this.s = new GlStateManager.TexGenCoord(8192, 3168);
         this.t = new GlStateManager.TexGenCoord(8193, 3169);
         this.r = new GlStateManager.TexGenCoord(8194, 3170);
         this.q = new GlStateManager.TexGenCoord(8195, 3171);
      }

      public TexGenState(GlStateManager.SwitchTexGen p_i46253_1_) {
         this();
      }
   }

   public static class TextureState {

      public GlStateManager.BooleanState texture2DState;
      public int textureName;
      public static final String __OBFID = "CL_00002539";


      public TextureState() {
         this.texture2DState = new GlStateManager.BooleanState(3553);
         this.textureName = 0;
      }

      public TextureState(GlStateManager.SwitchTexGen p_i46252_1_) {
         this();
      }
   }
}
