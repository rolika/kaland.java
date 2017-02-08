package pkg43_kaland;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Játékmotor, mely reagál a játékos által kezdeményezett szándékra. Alapvetően megkísérli elvégezni
 * az adott parancsot, és az eredménytől függően mindig valamilyen üzenettel tér vissza. Pl.
 * RENDBEN, ha sikerült végrehajtani, vagy ARRA NEM MEHETSZ, ha a játékos falba ütközne, ha arra
 * menne.
 *
 * @author Roland
 * @param <E> generikus, hogy kezelni tudja az adatbázisból képzett különböző osztályokat
 */
public class Vilag<E extends Elem> {

  private final Map<String, Helyszin> helyszinek;
  private final List<String> uzenetek;
  private final Map<String, Targy> targyak;
  private final Map<String, Ajto> ajtok;
  private final Map<String, Csapda> csapdak;
  private Helyszin aktualisHelyszin;

  /**
   * Felépíti a világot, beállítja a kezdő helyszínt Az egyes osztályokat type cast-olja az Elem-ből
   *
   * @param helyszinek
   * @param kijaratok
   * @param uzenetek
   * @param targyak
   * @param ajtok
   * @param csapdak
   */
  public Vilag(List<E> helyszinek, List<E> kijaratok, List<E> uzenetek, List<E> targyak,
    List<E> ajtok, List<E> csapdak) {
    // helyszínek és kijáratok
    Map<String, Kijarat> tempKijarat = new HashMap<>();
    this.helyszinek = helyszinek.stream().map(helyszin -> (Helyszin) helyszin)
      .collect(Collectors.toMap(Helyszin::getNev, Helyszin::getThis));
    //helyszinek.forEach(helyszin -> this.helyszinek.put(helyszin.getNev(), (Helyszin) helyszin));
    kijaratok.forEach(kijarat -> tempKijarat.put(kijarat.getNev(), (Kijarat) kijarat));
    this.helyszinek.keySet().forEach(nev
      -> this.helyszinek.get(nev).setKijarat(tempKijarat.get(nev))); // kijárat a helyszín része
    // üzenetek
    this.uzenetek = new ArrayList<>();
    this.uzenetek.add(""); // egyezzen az sqlite id a lista indexével
    uzenetek.forEach(uzenet -> this.uzenetek.add(uzenet.getNev()));
    // tárgyak
    this.targyak = new HashMap<>();
    targyak.forEach(targy -> this.targyak.put(targy.getNev(), (Targy) targy));
    // ajtók
    this.ajtok = new HashMap<>();
    ajtok.forEach(ajto -> this.ajtok.put(ajto.getNev(), (Ajto) ajto));
    // csapdák beállítása
    this.csapdak = new HashMap<>();
    csapdak.forEach(csapda -> this.csapdak.put(csapda.getNev(), (Csapda) csapda));
    // kezdő helyszín beállítása
    aktualisHelyszin = this.helyszinek.get("Padlás vége");
  }

  /**
   * Aktuális helyszín, mint osztály
   *
   * @return helyszín
   */
  public Helyszin getAktualisHelyszin() {
    return aktualisHelyszin;
  }

  /**
   * Adott azonosítójú üzenet
   *
   * @param id adatbázis primary key
   * @return üzenet mint string
   */
  public String getUzenet(int id) {
    return uzenetek.get(id);
  }

  /**
   * Világos van-e az adott helyszínen
   *
   * @return igaz, ha világít a zseblámpa vagy egyébként nincs sötét
   */
  public boolean isVilagos() {
    return getTargy("zseblámpát").isAktiv() || !aktualisHelyszin.isSotet();
  }

  /**
   * Mozgás esetén új helyszín beállítása, vagy régi megtartása
   *
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
          return ajto.getZarvaUzenet();
        } else {
          return ajto.getCsukva();
        }
      }
    }
  }

  /**
   * Visszaadja a helyszínről történő elmozdulás célját
   *
   * @param irany
   * @return célhelyszín rövid neve (lehet null is!)
   */
  public String getCelNev(String irany) {
    return aktualisHelyszin.getKijarat(irany);
  }

  /**
   * Tárgy aktiválása (bekapcsolása)
   *
   * @param nevTargyeset parancsból tárgyesetet kap
   * @param aktiv igaz, ha bekapcsol, hamis, ha lekapcsol
   * @return eredményjelző üzenet
   */
  public String aktival(String nevTargyeset, boolean aktiv) {
    if (nevTargyeset.isEmpty()) {
      return uzenetek.get(9); // ennek nincs értelme
    }
    Targy targy = getTargy(nevTargyeset);
    if (targy == null || !keznelVan(targy)) {
      return uzenetek.get(7); // nincs itt ilyesmi
    } else if (!targy.isAktivalhato()) {
      return uzenetek.get(9); // ennek nincs értelme
    }
    targy.setAktiv(aktiv);
    return uzenetek.get(2);
  }

  /**
   * Tárgyesettel meghatározott Tárgy visszaadása
   *
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

  /**
   * Részesesettel meghatározott Tárgy visszaadása
   *
   * @param nevReszeset parancsból kapott részes eset
   * @return adott tárgy-objektum vagy null, ha nincs ilyen (hívónak kezelni kell)
   */
  public Targy getReszes(String nevReszeset) {
    for (String kulcs : targyak.keySet()) {
      if (targyak.get(kulcs).getReszeset().equals(nevReszeset)) {
        return targyak.get(kulcs);
      }
    }
    return null;
  }

  private boolean keznelVan(Targy targy) {
    if (targy.getHely().equals("Leltár")) { // leltárban van, vagy
      return true;
    } else if (targy.getHely().equals(aktualisHelyszin.getNev()) && targy.isLathato()
      && isVilagos()) {
      return true;
    }
    return false;
  }

  /**
   * Játékosnál lévő tárgyak felsorolása
   *
   * @return megfelelő üzenet-string
   */
  public String getLeltar() {
    String leltar = this.getTargyakFromHelyszin(helyszinek.get("Leltár"));
    if (leltar.isEmpty()) {
      return uzenetek.get(10);
    } else {
      return uzenetek.get(12) + leltar;
    }
  }

  /**
   * Aktuális helyszínen lévő tárgyak felsorolása
   *
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
      .filter(targy
        -> (targy.getHely().equals(helyszin.getNev()) && targy.isFelveheto() && targy.isLathato()))
      .forEach(targy -> {
        eredmeny.append(" egy ");
        eredmeny.append(targy.getNev());
        eredmeny.append(",");
      });
    int hossz = eredmeny.length();
    if (hossz > 0) {
      eredmeny.replace(hossz - 1, hossz, "."); // pontot tesz a végére
    }
    return eredmeny.toString();
  }

  private Ajto getAjto(Helyszin cel) {
    for (String ajto : ajtok.keySet()) {
      if (ajtok.get(ajto).vanAjtoArra(aktualisHelyszin, cel)) {
        return ajtok.get(ajto);
      }
    }
    return null;
  }

  private Ajto getAjto(String targyeset) {
    for (String ajto : ajtok.keySet()) {
      if (ajtok.get(ajto).getTargyeset().equals(targyeset)) {
        return ajtok.get(ajto);
      }
    }
    return null;
  }

  /**
   * Megpróbálja kinyitni a tárgyesettel megadott ajtót
   *
   * @param targy ajtó tárgyesete
   * @param reszes ha az ajtó zárva van, ezzel lehet kinyitni a zárat
   * @return a kísérlet eredményét jelző üzenet
   */
  public String kinyit(String targy, String reszes) {
    if (targy.isEmpty()) {
      return uzenetek.get(6); // értelmetlen
    } else {
      for (String k : ajtok.keySet()) {
        Ajto ajto = ajtok.get(k);
        if (ajto.getTargyeset().equals(targy) && ajto.vanAjto(aktualisHelyszin)) {
          switch (ajto.getAllapot()) {
            case "csukva":
              ajto.setAllapot("nyitva");
              return uzenetek.get(2);
            case "zárva":
              Targy kulcs = getReszes(reszes);
              if (reszes.isEmpty()) {
                return ajto.getZarvaUzenet();
              } else if (kulcs == null || !keznelVan(kulcs)
                || !ajto.getKulcs().equals(kulcs.getNev())) {
                return uzenetek.get(15); // nincs kulcs
              } else {
                ajto.setAllapot("nyitva");
                return ajto.getNyitvaUzenet();
              }
            default:
              return uzenetek.get(13); // már nyitva
          }
        }
      }
      return uzenetek.get(7); // nincs ilyen ajtó
    }
  }

  /**
   * Megpróbálja felvenni a tárgyat
   *
   * @param targyeset szóban forgó tárgy tárgy esete
   * @return megfelelő üzenet
   */
  public String felvesz(String targyeset) {
    Targy targy = getTargy(targyeset);
    if (targy == null) {
      return uzenetek.get(6); // nem értelmezett tárgy
    } else if (!targy.getHely().equals(aktualisHelyszin.getNev())) {
      return uzenetek.get(7); // nincs itt ilyen tárgy
    } else if (getLeltar().contains(targy.getNev())) {
      return uzenetek.get(14); // már a leltárban van
    } else if (getLathatoTargyak().contains(targy.getNev())) {
      targy.setHely("Leltár");
      return uzenetek.get(2); // rendben
    } else {
      return uzenetek.get(3); // nem felvehető
    }
  }

  /**
   * Letesz egy játékosnál lévő tárgyat
   *
   * @param targyeset szóban forgó tárgy tárgy esete
   * @return megfelelő üzenet
   */
  public String letesz(String targyeset) {
    Targy targy = getTargy(targyeset);
    if (targy == null) {
      return uzenetek.get(6); // nem értelmezett tárgy
    } else {
      if (getLeltar().contains(targy.getNev())) {
        targy.setHely(aktualisHelyszin.getNev());
        return uzenetek.get(2); // rendben
      } else {
        return uzenetek.get(15); // nincs a játékosnál
      }
    }
  }

  /**
   * Ha van a helyiségben csapda, visszaadja
   *
   * @return egy csapda, vagy null, ha nincs
   */
  public Csapda getCsapda() {
    for (String kulcs : csapdak.keySet()) {
      if (csapdak.get(kulcs).getHely().equals(aktualisHelyszin.getNev())) {
        return csapdak.get(kulcs);
      }
    }
    return null;
  }
  
  /**
   * Visszaad egy bizonyos csapdát a neve alapján
   * 
   * @param nev
   * @return egy csapda, vagy null, ha nincs
   */
  public Csapda getCsapda(String nev) {
    for (String kulcs : csapdak.keySet()) {
      if (csapdak.get(kulcs).getNev().equals(nev)) {
        return csapdak.get(kulcs);
      }
    }
    return null;
  }

  /**
   * Játékos megvizsgálja az adott tárgyat
   *
   * @param targyeset vizsgálandó tárgy tárgyesete
   * @return megfelelő üzenet
   */
  public String vizsgal(String targyeset) {
    Targy targy = getTargy(targyeset);
    Ajto ajto = getAjto(targyeset);
    if (targy != null && keznelVan(targy)) {
      return targy.getLeiras();
    } else if (ajto != null && ajto.vanAjto(aktualisHelyszin)) {
      return ajto.getLeiras();
    } else {
      return uzenetek.get(6); // nem értelmezett tárgy
    }
  }
  
  /**
   * Adott helyszínen adott helyzet megtörténik-e
   * 
   * @param helyszin melyik helyszin
   * @param mit egyeztessen
   * @param mivel egyezzen
   * @return
   */
  public boolean checkHelyzet(String helyszin, String mit, String mivel) {
    return aktualisHelyszin.getNev().equals(helyszin) && mit.equals(mivel);
  }

}
