package pkg43_kaland;

/**
 * Különböző típusú rekordok kiolvasásához különböző típusú osztályokba
 * @author Roland
 * @param <E> elem típusa: helyszín, kijárat stb.
 */
public interface Elem<E> {
  
  String getNev();
  String getCel(String irany);
  String helyszinLeiras();
  
}
