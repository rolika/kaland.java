package pkg43_kaland;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vilag<E extends Elem>  {
  
  private final Map<String, Helyszin> helyszinek;
  private final List<String> uzenetek;
  private final Map<String, Targy> targyak;
  private Helyszin aktualisHelyszin;
  
  public Vilag(List<E> helyszinek, List<E> kijaratok, List<E> uzenetek, List<E> targyak) {
    // helyszínek és kijáratok
    Map<String, Kijarat> tempKijarat = new HashMap<>();
    this.helyszinek = new HashMap<>();
    helyszinek.forEach(helyszin -> this.helyszinek.put(helyszin.getNev(), (Helyszin) helyszin));
    kijaratok.forEach(kijarat -> tempKijarat.put(kijarat.getNev(), (Kijarat) kijarat));
    this.helyszinek.keySet().forEach(nev -> 
      this.helyszinek.get(nev).setKijarat(tempKijarat.get(nev))); // kijárat a helyszín része
    // üzenetek
    this.uzenetek = new ArrayList<>();
    this.uzenetek.add(""); // egyezzen az sqlite id a lista indexével
    uzenetek.forEach(uzenet -> this.uzenetek.add(uzenet.getNev()));
    // tárgyak
    this.targyak =new HashMap<>();
    targyak.forEach(targy -> this.targyak.put(targy.getNev(), (Targy) targy));
    // kezdő helyszín beállítása
    aktualisHelyszin = this.helyszinek.get("Ház előtt");
  }

  public Helyszin getHelyszin() {
    return aktualisHelyszin;
  }
  
  public String getUzenet(int id) {
    return uzenetek.get(id);
  }
  
  public String ujHelyszin(String irany) {
    String celHelyszinNev = aktualisHelyszin.getKijarat(irany);
    if (celHelyszinNev == null) {
      return uzenetek.get(1);
    } else {
      // itt kell majd ráellenőrizni ajtókra és akadályokra
      aktualisHelyszin = helyszinek.get(celHelyszinNev);
      return uzenetek.get(2);
    }
  }
  
  public String beallit(String nevTargyeset, boolean aktiv) {
    Targy targy = getTargy(nevTargyeset);
    if (targy == null || !keznelVan(targy)) {
      return uzenetek.get(7);
    } else if (!targy.isAktivalhato()) {
      return uzenetek.get(9);
    }
    targy.setAktiv(aktiv);
    return uzenetek.get(2);
  }
  
  public Targy getTargy(String nevTargyeset) {
    for (String kulcs : targyak.keySet()) {
      if (targyak.get(kulcs).getTargyeset().equals(nevTargyeset)) {
        return targyak.get(kulcs);
      }
    }
    return null;
  }
  
  private boolean keznelVan(Targy targy) {
    if (targy.getHely().equals("Leltár")) { // leltárban van, vagy
      return true;
      } else if (targy.getHely().equals(aktualisHelyszin.getNev()) && // nem sötét helyszínen
        (!aktualisHelyszin.isSotet() || getTargy("zseblámpát").isAktiv())) {
      return true;
    }
    return false;
  }
  
  public String getLeltar() {
    if (aktualisHelyszin.isSotet()) {
      return uzenetek.get(5);
    } else {
      String leltar = this.getTargyakFromHelyszin(helyszinek.get("Leltár"));
      if (leltar.isEmpty()) {
        return uzenetek.get(10);
      } else {
        return uzenetek.get(12) + leltar;
      }
    }
  }
  
  public String getLathatoTargyak() {
    String leltar = this.getTargyakFromHelyszin(aktualisHelyszin);
    if (leltar.isEmpty()) {
      return uzenetek.get(11);
    } else {
      return uzenetek.get(8) + leltar;
    }
  }
  
  private String getTargyakFromHelyszin(Helyszin helyszin) {
    StringBuilder eredmeny = new StringBuilder();
    targyak.keySet().stream()
      .map(kulcs -> targyak.get(kulcs))
      .filter(targy ->
        (targy.getHely().equals(helyszin.getNev()) && targy.isFelveheto() && targy.isLathato()))
      .forEach(targy -> {
        eredmeny.append(" egy ");
        eredmeny.append(targy.getNev());
        eredmeny.append(",");
      });
    int hossz = eredmeny.length();
    if (hossz > 0) {
      eredmeny.replace(hossz-1, hossz, "."); // pontot tesz a végére
    }
    return eredmeny.toString();
  }
  
}
