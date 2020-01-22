package net.minecraft.client.renderer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import net.minecraft.client.renderer.IImageBuffer;

public class ImageBufferDownload implements IImageBuffer {
   private int[] field_78438_a;
   private int field_78436_b;
   private int field_78437_c;
   private static final String __OBFID = "CL_00000956";

   public BufferedImage func_78432_a(BufferedImage p_78432_1_) {
      if(p_78432_1_ == null) {
         return null;
      } else {
         this.field_78436_b = 64;
         this.field_78437_c = 64;
         int i = p_78432_1_.getWidth();
         int j = p_78432_1_.getHeight();

         int k;
         for(k = 1; this.field_78436_b < i || this.field_78437_c < j; k *= 2) {
            this.field_78436_b *= 2;
            this.field_78437_c *= 2;
         }

         BufferedImage bufferedimage = new BufferedImage(this.field_78436_b, this.field_78437_c, 2);
         Graphics graphics = bufferedimage.getGraphics();
         graphics.drawImage(p_78432_1_, 0, 0, (ImageObserver)null);
         if(p_78432_1_.getHeight() == 32 * k) {
            graphics.drawImage(bufferedimage, 24 * k, 48 * k, 20 * k, 52 * k, 4 * k, 16 * k, 8 * k, 20 * k, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 28 * k, 48 * k, 24 * k, 52 * k, 8 * k, 16 * k, 12 * k, 20 * k, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 20 * k, 52 * k, 16 * k, 64 * k, 8 * k, 20 * k, 12 * k, 32 * k, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 24 * k, 52 * k, 20 * k, 64 * k, 4 * k, 20 * k, 8 * k, 32 * k, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 28 * k, 52 * k, 24 * k, 64 * k, 0 * k, 20 * k, 4 * k, 32 * k, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 32 * k, 52 * k, 28 * k, 64 * k, 12 * k, 20 * k, 16 * k, 32 * k, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 40 * k, 48 * k, 36 * k, 52 * k, 44 * k, 16 * k, 48 * k, 20 * k, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 44 * k, 48 * k, 40 * k, 52 * k, 48 * k, 16 * k, 52 * k, 20 * k, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 36 * k, 52 * k, 32 * k, 64 * k, 48 * k, 20 * k, 52 * k, 32 * k, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 40 * k, 52 * k, 36 * k, 64 * k, 44 * k, 20 * k, 48 * k, 32 * k, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 44 * k, 52 * k, 40 * k, 64 * k, 40 * k, 20 * k, 44 * k, 32 * k, (ImageObserver)null);
            graphics.drawImage(bufferedimage, 48 * k, 52 * k, 44 * k, 64 * k, 52 * k, 20 * k, 56 * k, 32 * k, (ImageObserver)null);
         }

         graphics.dispose();
         this.field_78438_a = ((DataBufferInt)bufferedimage.getRaster().getDataBuffer()).getData();
         this.func_78433_b(0, 0, 32 * k, 16 * k);
         this.func_78434_a(32 * k, 0, 64 * k, 32 * k);
         this.func_78433_b(0, 16 * k, 64 * k, 32 * k);
         this.func_78434_a(0, 32 * k, 16 * k, 48 * k);
         this.func_78434_a(16 * k, 32 * k, 40 * k, 48 * k);
         this.func_78434_a(40 * k, 32 * k, 56 * k, 48 * k);
         this.func_78434_a(0, 48 * k, 16 * k, 64 * k);
         this.func_78433_b(16 * k, 48 * k, 48 * k, 64 * k);
         this.func_78434_a(48 * k, 48 * k, 64 * k, 64 * k);
         return bufferedimage;
      }
   }

   public void func_152634_a() {
   }

   private void func_78434_a(int p_78434_1_, int p_78434_2_, int p_78434_3_, int p_78434_4_) {
      if(!this.func_78435_c(p_78434_1_, p_78434_2_, p_78434_3_, p_78434_4_)) {
         for(int i = p_78434_1_; i < p_78434_3_; ++i) {
            for(int j = p_78434_2_; j < p_78434_4_; ++j) {
               this.field_78438_a[i + j * this.field_78436_b] &= 16777215;
            }
         }
      }

   }

   private void func_78433_b(int p_78433_1_, int p_78433_2_, int p_78433_3_, int p_78433_4_) {
      for(int i = p_78433_1_; i < p_78433_3_; ++i) {
         for(int j = p_78433_2_; j < p_78433_4_; ++j) {
            this.field_78438_a[i + j * this.field_78436_b] |= -16777216;
         }
      }

   }

   private boolean func_78435_c(int p_78435_1_, int p_78435_2_, int p_78435_3_, int p_78435_4_) {
      for(int i = p_78435_1_; i < p_78435_3_; ++i) {
         for(int j = p_78435_2_; j < p_78435_4_; ++j) {
            int k = this.field_78438_a[i + j * this.field_78436_b];
            if((k >> 24 & 255) < 128) {
               return true;
            }
         }
      }

      return false;
   }
}
