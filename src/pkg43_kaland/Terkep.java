package pkg43_kaland;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Terkep<E extends Elem>  {
  
  private final Map<String, Helyszin> helyszinek;
  
  public Terkep(List<E> helyszinek, List<E> kijaratok) {
    Map<String, Kijarat> tempKijarat = new HashMap<>();
    this.helyszinek = new HashMap<>();
    helyszinek.forEach(helyszin -> this.helyszinek.put(helyszin.getNev(), (Helyszin) helyszin));
    kijaratok.forEach(kijarat -> tempKijarat.put(kijarat.getNev(), (Kijarat) kijarat));
    this.helyszinek.keySet().forEach(nev -> 
      this.helyszinek.get(nev).setKijarat(tempKijarat.get(nev)));
  }

  public Helyszin getHelyszin(String nev) {
    return helyszinek.get(nev);
  }
  
}
