package pkg43_kaland;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Terkep<E extends Elem>  {
  
  private final Map<String, Helyszin> helyszinek;
  private final Map<String, Kijarat> kijaratok;
  
  public Terkep(List<E> helyszinek, List<E> kijaratok) {
    this.helyszinek = new HashMap<>();
    this.kijaratok = new HashMap<>();
    helyszinek.forEach(helyszin -> this.helyszinek.put(helyszin.getNev(), (Helyszin) helyszin));
    kijaratok.forEach(kijarat -> this.kijaratok.put(kijarat.getNev(), (Kijarat) kijarat));
    // és ugyanígy lesznek az akadályok, akadályirányok, tárgyak, üzenetek
  }
  
  public String szandek(String helyszin, String irany) {
    Kijarat kijarat = kijaratok.getOrDefault(helyszin, null);
    return kijarat.getCel(irany);
  }
  
  public String getLeiras(String helyszin) {
    return helyszinek.get(helyszin).helyszinLeiras();
  }
  
}
