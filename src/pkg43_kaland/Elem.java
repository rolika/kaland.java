package pkg43_kaland;

/**
 * Generikus interface az adatbázisból történő kiolvasáshoz
 * @author Roland
 * @param <E>
 */
public interface Elem<E> {
  
  static final String[] IRANYOK =
    { "eszak", "kelet", "del", "nyugat", "le", "fel", "indirekt" };  
  
  /**
   * Az elem neve, pl. helyszín rövid leírása
   * @return név
   */
  String getNev();
  
}
