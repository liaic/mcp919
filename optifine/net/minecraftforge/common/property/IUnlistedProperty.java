package net.minecraftforge.common.property;


public interface IUnlistedProperty<V extends Object> {

   String getName();

   boolean isValid(V var1);

   Class<V> getType();

   String valueToString(V var1);
}
