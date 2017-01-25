package pkg43_kaland;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Terkep<E extends Elem>  {
  
  private final Map<String, E> helyszinek;
  private final Map<String, E> kijaratok;
  
  public Terkep(List<E> helyszinek, List<E> kijaratok) {
    this.helyszinek = new HashMap<>();
    this.kijaratok = new HashMap<>();
    helyszinek.forEach(helyszin -> this.helyszinek.put(helyszin.getNev(), helyszin));
    kijaratok.forEach(kijarat -> this.kijaratok.put(kijarat.getNev(), kijarat));
    // és ugyanígy lesznek az akadályok, akadályirányok, tárgyak
  }
  
  public String szandek(String helyszin, String irany) {
    E kijarat = kijaratok.getOrDefault(helyszin, null);
    return kijarat.getCel(irany);
  }
  
  public String getLeiras(String helyszin) {
    return helyszinek.get(helyszin).getNev();
  }
  
}
