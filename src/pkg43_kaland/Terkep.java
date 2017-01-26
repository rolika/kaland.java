package pkg43_kaland;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Terkep<E extends Elem>  {
  
  private final Map<String, Helyszin> helyszinek;
  private final Map<String, Kijarat> kijaratok;
  private final Map<String, Akadaly> akadalyok;
  private final Map<String, AkadalyIrany> akadalyIranyok;
  
  public Terkep(List<E> helyszinek, List<E> kijaratok, List<E> akadalyok, List<E> akadalyIranyok) {
    this.helyszinek = new HashMap<>();
    this.kijaratok = new HashMap<>();
    this.akadalyok = new HashMap<>();
    this.akadalyIranyok = new HashMap<>();
    helyszinek.forEach(helyszin -> this.helyszinek.put(helyszin.getNev(), (Helyszin) helyszin));
    kijaratok.forEach(kijarat -> this.kijaratok.put(kijarat.getNev(), (Kijarat) kijarat));
    akadalyok.forEach(akadaly -> this.akadalyok.put(akadaly.getNev(), (Akadaly) akadaly));
    akadalyIranyok.forEach(irany -> this.akadalyIranyok.put(irany.getNev(), (AkadalyIrany) irany));
  }
  
  public Helyszin getCel(Helyszin helyszin, String irany) {
    Kijarat kijarat = kijaratok.get(helyszin.getNev());
    String celNev = kijarat.getCel(irany);
    return celNev == null ? null : getHelyszin(celNev);
  }
  
  public Helyszin getHelyszin(String nev) {
    return helyszinek.get(nev);
  }
  
  public String getLeiras(String helyszin) {
    return helyszinek.get(helyszin).getLeiras();
  }
  
  public Akadaly getAkadaly(Helyszin helyszin, String irany) {
    AkadalyIrany ai = akadalyIranyok.get(helyszin.getNev());
    String akadalyNev = ai.getAkadaly(irany);
    return akadalyNev == null ? null : akadalyok.get(akadalyNev);
  }
  
}
