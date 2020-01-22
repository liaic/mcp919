package shadersmod.client;

import java.util.ArrayList;
import optifine.Lang;
import shadersmod.client.ShaderOption;
import shadersmod.client.ShaderProfile;
import shadersmod.client.ShaderUtils;
import shadersmod.client.Shaders;

public class ShaderOptionProfile extends ShaderOption {

   public ShaderProfile[] profiles = null;
   public ShaderOption[] options = null;
   public static final String NAME_PROFILE = "<profile>";
   public static final String VALUE_CUSTOM = "<custom>";


   public ShaderOptionProfile(ShaderProfile[] profiles, ShaderOption[] options) {
      super("<profile>", "", detectProfileName(profiles, options), getProfileNames(profiles), detectProfileName(profiles, options, true), (String)null);
      this.profiles = profiles;
      this.options = options;
   }

   public void nextValue() {
      super.nextValue();
      if(this.getValue().equals("<custom>")) {
         super.nextValue();
      }

      this.applyProfileOptions();
   }

   public void updateProfile() {
      ShaderProfile prof = this.getProfile(this.getValue());
      if(prof == null || !ShaderUtils.matchProfile(prof, this.options, false)) {
         String val = detectProfileName(this.profiles, this.options);
         this.setValue(val);
      }
   }

   public void applyProfileOptions() {
      ShaderProfile prof = this.getProfile(this.getValue());
      if(prof != null) {
         String[] opts = prof.getOptions();

         for(int i = 0; i < opts.length; ++i) {
            String name = opts[i];
            ShaderOption so = this.getOption(name);
            if(so != null) {
               String val = prof.getValue(name);
               so.setValue(val);
            }
         }

      }
   }

   public ShaderOption getOption(String name) {
      for(int i = 0; i < this.options.length; ++i) {
         ShaderOption so = this.options[i];
         if(so.getName().equals(name)) {
            return so;
         }
      }

      return null;
   }

   public ShaderProfile getProfile(String name) {
      for(int i = 0; i < this.profiles.length; ++i) {
         ShaderProfile prof = this.profiles[i];
         if(prof.getName().equals(name)) {
            return prof;
         }
      }

      return null;
   }

   public String getNameText() {
      return Lang.get("of.shaders.profile");
   }

   public String getValueText(String val) {
      return val.equals("<custom>")?Lang.get("of.general.custom", "<custom>"):Shaders.translate("profile." + val, val);
   }

   public String getValueColor(String val) {
      return val.equals("<custom>")?"\u00a7c":"\u00a7a";
   }

   public static String detectProfileName(ShaderProfile[] profs, ShaderOption[] opts) {
      return detectProfileName(profs, opts, false);
   }

   public static String detectProfileName(ShaderProfile[] profs, ShaderOption[] opts, boolean def) {
      ShaderProfile prof = ShaderUtils.detectProfile(profs, opts, def);
      return prof == null?"<custom>":prof.getName();
   }

   public static String[] getProfileNames(ShaderProfile[] profs) {
      ArrayList list = new ArrayList();

      for(int names = 0; names < profs.length; ++names) {
         ShaderProfile prof = profs[names];
         list.add(prof.getName());
      }

      list.add("<custom>");
      String[] var4 = (String[])((String[])list.toArray(new String[list.size()]));
      return var4;
   }
}
