package optifine.xdelta;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import optifine.xdelta.SimplePrime;

public class Checksum {

   public static final int BASE = 65521;
   public static final int S = 16;
   public static boolean debug = false;
   public int[] hashtable;
   public long[] checksums;
   public int prime;
   public static final char[] single_hash = new char[]{'\ubcd1', '\ubb65', '\u42c2', '\udffe', '\u9666', '\u431b', '\u8504', '\ueb46', '\u6379', '\ud460', '\ucf14', '\u53cf', '\udb51', '\udb08', '\u12c8', '\uf602', '\ue766', '\u2394', '\u250d', '\udcbb', '\ua678', '\u02af', '\ua5c6', '\u7ea6', '\ub645', '\ucb4d', '\uc44b', '\ue5dc', '\u9fe6', '\u5b5c', '\u35f5', '\u701a', '\u220f', '\u6c38', '\u1a56', '\u4ca3', '\uffc6', '\ub152', '\u8d61', '\u7a58', '\u9025', '\u8b3d', '\ubf0f', '\u95a3', '\ue5f4', '\uc127', '\u3bed', '\u320b', '\ub7f3', '\u6054', '\u333c', '\ud383', '\u8154', '\u5242', '\u4e0d', '\u0a94', '\u7028', '\u8689', '\u3a22', '\u0980', '\u1847', '\ub0f1', '\u9b5c', '\u4176', '\ub858', '\ud542', '\u1f6c', '\u2497', '\u6a5a', '\u9fa9', '\u8c5a', '\u7743', '\ua8a9', '\u9a02', '\u4918', '\u438c', '\uc388', '\u9e2b', '\u4cad', '\u01b6', '\uab19', '\uf777', '\u365f', '\u1eb2', '\u091e', '\u7bf8', '\u7a8e', '\u5227', '\ueab1', '\u2074', '\u4523', '\ue781', '\u01a3', '\u163d', '\u3b2e', '\u287d', '\u5e7f', '\ua063', '\ub134', '\u8fae', '\u5e8e', '\ub7b7', '\u4548', '\u1f5a', '\ufa56', '\u7a24', '\u900f', '\u42dc', '\ucc69', '\u02a0', '\u0b22', '\udb31', '\u71fe', '\u0c7d', '\u1732', '\u1159', '\ucb09', '\ue1d2', '\u1351', '\u52e9', '\uf536', '\u5a4f', '\uc316', '\u6bf9', '\u8994', '\ub774', '\u5f3e', '\uf6d6', '\u3a61', '\uf82c', '\ucc22', '\u9d06', '\u299c', '\u09e5', '\u1eec', '\u514f', '\u8d53', '\ua650', '\u5c6e', '\uc577', '\u7958', '\u71ac', '\u8916', '\u9b4f', '\u2c09', '\u5211', '\uf6d8', '\ucaaa', '\uf7ef', '\u287f', '\u7a94', '\uab49', '\ufa2c', '\u7222', '\ue457', '\ud71a', '\u00c3', '\u1a76', '\ue98c', '\uc037', '\u8208', '\u5c2d', '\udfda', '\ue5f5', '\u0b45', '\u15ce', '\u8a7e', '\ufcad', '\uaa2d', '\u4b5c', '\ud42e', '\ub251', '\u907e', '\u9a47', '\uc9a6', '\ud93f', '\u085e', '\u35ce', '\ua153', '\u7e7b', '\u9f0b', '\u25aa', '\u5d9f', '\uc04d', '\u8a0e', '\u2875', '\u4a1c', '\u295f', '\u1393', '\uf760', '\u9178', '\u0f5b', '\ufa7d', '\u83b4', '\u2082', '\u721d', '\u6462', '\u0368', '\u67e2', '\u8624', '\u194d', '\u22f6', '\u78fb', '\u6791', '\ub238', '\ub332', '\u7276', '\uf272', '\u47ec', '\u4504', '\ua961', '\u9fc8', '\u3fdc', '\ub413', 'z', '\u0806', '\u7458', '\u95c6', '\uccaa', '\u18d6', '\ue2ae', '\u1b06', '\uf3f6', '\u5050', '\uc8e8', '\uf4ac', '\uc04c', '\uf41c', '\u992f', '\uae44', '\u5f1b', '\u1113', '\u1738', '\ud9a8', '\u19ea', '\u2d33', '\u9698', '\u2fe9', '\u323f', '\ucde2', '\u6d71', '\ue37d', '\ub697', '\u2c4f', '\u4373', '\u9102', '\u075d', '\u8e25', '\u1672', '\uec28', '\u6acb', '\u86cc', '\u186e', '\u9414', '\ud674', '\ud1a5'};


   public static long queryChecksum(byte[] buf, int len) {
      int high = 0;
      int low = 0;

      for(int i = 0; i < len; ++i) {
         low += single_hash[buf[i] + 128];
         high += low;
      }

      return (long)((high & '\uffff') << 16 | low & '\uffff');
   }

   public static long incrementChecksum(long checksum, byte out, byte in) {
      char old_c = single_hash[out + 128];
      char new_c = single_hash[in + 128];
      int low = (int)(checksum & 65535L) - old_c + new_c & '\uffff';
      int high = (int)(checksum >> 16) - old_c * 16 + low & '\uffff';
      return (long)(high << 16 | low & '\uffff');
   }

   public static int generateHash(long checksum) {
      long high = checksum >> 16 & 65535L;
      long low = checksum & 65535L;
      long it = (high >> 2) + (low << 3) + (high << 16);
      int hash = (int)(it ^ high ^ low);
      return hash > 0?hash:-hash;
   }

   public void generateChecksums(File sourceFile, int length) throws IOException {
      FileInputStream fis = new FileInputStream(sourceFile);

      try {
         this.generateChecksums((InputStream)fis, length);
      } catch (IOException var8) {
         throw var8;
      } finally {
         fis.close();
      }

   }

   public void generateChecksums(InputStream sis, int length) throws IOException {
      BufferedInputStream is = new BufferedInputStream(sis);
      int checksumcount = length / 16;
      if(debug) {
         System.out.println("generating checksum array of size " + checksumcount);
      }

      this.checksums = new long[checksumcount];
      this.hashtable = new int[checksumcount];
      this.prime = findClosestPrime(checksumcount);
      if(debug) {
         System.out.println("using prime " + this.prime);
      }

      int i;
      for(i = 0; i < checksumcount; ++i) {
         byte[] hash = new byte[16];
         is.read(hash, 0, 16);
         this.checksums[i] = queryChecksum(hash, 16);
      }

      for(i = 0; i < checksumcount; ++i) {
         this.hashtable[i] = -1;
      }

      for(i = 0; i < checksumcount; ++i) {
         int var7 = generateHash(this.checksums[i]) % this.prime;
         if(debug) {
            System.out.println("checking with hash: " + var7);
         }

         if(this.hashtable[var7] != -1) {
            if(debug) {
               System.out.println("hash table collision for index " + i);
            }
         } else {
            this.hashtable[var7] = i;
         }
      }

   }

   public int findChecksumIndex(long checksum) {
      return this.hashtable[generateHash(checksum) % this.prime];
   }

   public static int findClosestPrime(int size) {
      int prime = (int)SimplePrime.belowOrEqual((long)(size - 1));
      return prime < 2?1:prime;
   }

   public String printIntArray(int[] a) {
      String result = "";
      result = result + "[";

      for(int i = 0; i < a.length; ++i) {
         result = result + a[i];
         if(i != a.length - 1) {
            result = result + ",";
         } else {
            result = result + "]";
         }
      }

      return result;
   }

   public String printLongArray(long[] a) {
      String result = "";
      result = result + "[";

      for(int i = 0; i < a.length; ++i) {
         result = result + a[i];
         if(i != a.length - 1) {
            result = result + ",";
         } else {
            result = result + "]";
         }
      }

      return result;
   }
}
