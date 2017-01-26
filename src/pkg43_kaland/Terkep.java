package pkg43_kaland;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Terkep<E extends Elem>  {
  
  private final Map<String, Helyszin> helyszinek;
  private final Map<String, Kijarat> kijaratok;
  private final Map<String, Akadaly> akadalyok;
  private final Map<String, AkadalyIrany> akadalyIranyok;
  
  public Terkep( List<E> helyszinek, List<E> kijaratok, List<E> akadalyok, List<E> akadalyIranyok) {
    this.helyszinek = new HashMap<>();
    this.kijaratok = new HashMap<>();
    this.akadalyok = new HashMap<>();
    this.akadalyIranyok = new HashMap<>();
    helyszinek.forEach(helyszin -> this.helyszinek.put(helyszin.getNev(), (Helyszin) helyszin));
    kijaratok.forEach(kijarat -> this.kijaratok.put(kijarat.getNev(), (Kijarat) kijarat));
    akadalyok.forEach(akadaly -> this.akadalyok.put(akadaly.getNev(), (Akadaly) akadaly));
    akadalyIranyok.forEach(irany -> this.akadalyIranyok.put(irany.getNev(), (AkadalyIrany) irany));
  }
  
  public String szandek(String helyszin, String irany) {
    Kijarat kijarat = kijaratok.getOrDefault(helyszin, null);
    return kijarat.getCel(irany);
  }
  
  public String getLeiras(String helyszin) {
    return helyszinek.get(helyszin).getLeiras();
  }
  
}