package pkg43_kaland;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A világ osztály implementálja és működteti a játék világát
 * @author Roland
 * @param <E> generikus, hogy kezelni tudja az adatbázisból képzett különböző osztályokat
 */
public class Vilag<E extends Elem>  {
  
  private final Map<String, Helyszin> helyszinek;
  private final List<String> uzenetek;
  private final Map<String, Targy> targyak;
  private final Map<String, Ajto> ajtok;
  private Helyszin aktualisHelyszin;
  
  /**
   * Felépíti a világot, beállítj a kezdő helyszínt
   * Az egyes osztályokat type cast-olja az Elem-ből
   * @param helyszinek
   * @param kijaratok
   * @param uzenetek
   * @param targyak
   * @param ajtok
   */
  public Vilag(List<E> helyszinek, List<E> kijaratok, List<E> uzenetek, List<E> targyak,
    List<E> ajtok) {
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
    // ajtók
    this.ajtok = new HashMap<>();
    ajtok.forEach(ajto -> this.ajtok.put(ajto.getNev(), (Ajto) ajto));
    // kezdő helyszín beállítása
    aktualisHelyszin = this.helyszinek.get("Ház előtt");
  }

  /**
   * Aktuális helyszín, mint osztály
   * @return helyszín
   */
  public Helyszin getHelyszin() {
    return aktualisHelyszin;
  }
  
  /**
   * Adott azonosítójú üzenet
   * @param id adatbázis primary key
   * @return üzenet mint string
   */
  public String getUzenet(int id) {
    return uzenetek.get(id);
  }
  
  /**
   * Világos van-e az adott helyszínen
   * @return igaz, ha világít a zseblámpa vagy egyébként nincs sötét
   */
  public boolean isVilagos() {
    return getTargy("zseblámpát").isAktiv() || !aktualisHelyszin.isSotet();
  }
  
  /**
   * Mozgás esetén új helyszín beállítása, vagy régi megtartása
   * @param irany ékezetlen égtáj
   * @return mozgási szándék eredményét jelző üzenet
   */
  public String ujHelyszin(String irany) {
    String celHelyszinNev = aktualisHelyszin.getKijarat(irany);
    if (celHelyszinNev == null) {
      return uzenetek.get(1);
    } else {
      Ajto ajto = getAjto(helyszinek.get(celHelyszinNev));
      if (ajto == null || ajto.getAllapot().equals("nyitva")) {
      aktualisHelyszin = helyszinek.get(celHelyszinNev);
      return uzenetek.get(2);
      } else {
        if (ajto.getAllapot().equals("zárva")) {
          return ajto.getZarva();
        } else {
          return ajto.getCsukva();
        }
      }
    }
  }
  
  /**
   * Tárgy aktiválása (bekapcsolása)
   * @param nevTargyeset parancsból tárgyesetet kap
   * @param aktiv igaz, ha bekapcsol, hamis, ha lekapcsol
   * @return eredményjelző üzenet
   */
  public String aktival(String nevTargyeset, boolean aktiv) {
    if (nevTargyeset.isEmpty()) {
      return uzenetek.get(9);
    }
    Targy targy = getTargy(nevTargyeset);
    if (targy == null || !keznelVan(targy)) {
      return uzenetek.get(7);
    } else if (!targy.isAktivalhato()) {
      return uzenetek.get(9);
    }
    targy.setAktiv(aktiv);
    return uzenetek.get(2);
  }
  
  /**
   * Tárgyesettel meghatározott Tárgy visszaadása
   * @param nevTargyeset parancsból kapott tárgyeset
   * @return adott tárgy-objektum vagy null, ha nincs ilyen (hívónak kezelni kell)
   */
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
  
  /**
   * Játékosnál lévő tárgyak felsorolása
   * @return megfelelő üzenet-string
   */
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
  
  /**
   * Aktuális helyszínen lévő tárgyak felsorolása
   * @return megfelelő üzenet-string
   */
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

  private Ajto getAjto(Helyszin cel) {
    for (String ajto : ajtok.keySet()) {
      if (ajtok.get(ajto).vanAjto(aktualisHelyszin, cel)) {
        return ajtok.get(ajto);
      }
    }
    return null;
  }
  
}
