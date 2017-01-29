package pkg43_kaland;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Terkep<E extends Elem>  {
  
  private final Map<String, Helyszin> helyszinek;
  private final Map<String, Targy> targyak;
  
  public Terkep(List<E> helyszinek, List<E> kijaratok, List<E> targyak) {
    Map<String, Kijarat> tempKijarat = new HashMap<>();
    this.helyszinek = new HashMap<>();
    this.targyak =new HashMap<>();
    helyszinek.forEach(helyszin -> this.helyszinek.put(helyszin.getNev(), (Helyszin) helyszin));
    kijaratok.forEach(kijarat -> tempKijarat.put(kijarat.getNev(), (Kijarat) kijarat));
    this.helyszinek.keySet().forEach(nev -> 
      this.helyszinek.get(nev).setKijarat(tempKijarat.get(nev))); // kijárat a helyszín része
    targyak.forEach(targy -> this.targyak.put(targy.getNev(), (Targy) targy));
  }

  public Helyszin getHelyszin(String nev) {
    return helyszinek.get(nev);
  }
  
  public boolean isZseblampaAktiv() {
    return targyak.get("zseblámpa").isAktiv();
  }
  
  public boolean hasznal(String targy, String reszes) {
    
    return false;
  }
  
}
