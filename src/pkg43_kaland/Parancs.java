package pkg43_kaland;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Parancsértelmező osztály A játék alapvető szintaxisa a magyar nyelvnek megfelelő egyes szám első
 * személyben megfogalmazott utasítás, pl. MEGÖLÖM A PÓKOT A KÉSSEL
 *
 * @author Roland
 */
public class Parancs {

  private static final String[] ESZAK = {"é", "észak", "északra", "északnak"};
  private static final String[] DEL = {"d", "dél", "délre", "délnek"};
  private static final String[] KELET = {"k", "kelet", "keletre", "keletnek"};
  private static final String[] NYUGAT = {"ny", "nyugat", "nyugatra", "nyugatnak"};
  private static final String[] LE = {"le", "lefelé", "lefele"};
  private static final String[] FEL = {"fel", "felfelé", "felfele"};
  private static final String[] INDIREKT = {"ki", "be"};

  private static final String LELTAR = "leltár";

  private static final String[] BEKAPCSOL = {"bekapcsolom", "felkapcsolom", "használom", "mozgatom"};
  private static final String[] KIKAPCSOL = {"kikapcsolom", "lekapcsolom"};

  private static final String KINYIT = "kinyitom";

  private static final String[] FELVESZ = {"felveszem", "elteszem", "elrakom"};
  private static final String[] LETESZ = {"leteszem", "lerakom", "eldobom"};  
  
  private static final String[] VIZSGAL = {"megvizsgálom", "megnézem", "ellenőrzöm"};

  private final Map<String, Set<String>> iranyok;
  private String irany;
  private List<String> szavak;

  /**
   * A konstruktor szükség szerint inicializálja a szótárat és a parancsszavakat tartalmazó listát
   */
  public Parancs() {
    iranyok = new HashMap<>();
    iranyok.put("eszak", new HashSet(Arrays.asList(ESZAK)));
    iranyok.put("del", new HashSet(Arrays.asList(DEL)));
    iranyok.put("kelet", new HashSet(Arrays.asList(KELET)));
    iranyok.put("nyugat", new HashSet(Arrays.asList(NYUGAT)));
    iranyok.put("le", new HashSet(Arrays.asList(LE)));
    iranyok.put("fel", new HashSet(Arrays.asList(FEL)));
    iranyok.put("indirekt", new HashSet(Arrays.asList(INDIREKT)));
    irany = "";
    szavak = new ArrayList<>();
  }

  /**
   * Szóközök ill. esetleges névelők (a, az) mentén szavakra bontja a parancsot
   *
   * @param parancs játékostól kapott szöveges parancs, pl. MEGÖLÖM A PÓKOT A KÉSSEL
   */
  public void szetszed(String parancs) {
    szavak = Arrays.asList(parancs.split(" a?z?\\b ?"));
  }

  /**
   * Ellenőrzi, hogy a parancs szavai között van-e irányt jelző szó, ha van, be is állítja az irányt
   *
   * @return igaz, ha volt irányt jelző szó
   */
  public boolean isIrany() {
    for (String szo : szavak) {
      for (String kulcs : iranyok.keySet()) {
        if (iranyok.get(kulcs).contains(szo)) {
          irany = kulcs;
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Ha a parancs mozgási szándék volt, lekérhető az irány
   *
   * @return irány szöveges reprezentációje (ékezetlen égtáj)
   */
  public String getIrany() {
    return irany;
  }

  /**
   * Bekapcsolásra (aktiválásra) utaló szó kiszűrése
   *
   * @return igaz, ha a játékos be akar valamit kapcsolni
   */
  public boolean isAktival() {
    for (String szo : BEKAPCSOL) {
      if (szo.equals(szavak.get(0))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Kikapcsolásra (deaktiválásra) utaló szó kiszűrése
   *
   * @return igaz, ha a játékos ki akar valamit kapcsolni
   */
  public boolean isDeaktival() {
    for (String szo : KIKAPCSOL) {
      if (szo.equals(szavak.get(0))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Magyar nyelvtan szerint a mondat tárgya az ige után jön, azaz a parancs második szava
   *
   * @return parancs második szava (Tárgy tárgyesete)
   */
  public String getTargy() {
    try {
      return szavak.get(1);
    } catch (ArrayIndexOutOfBoundsException ex) {
      return "";
    }
  }

  /**
   * A tárgy után jön a részes eset, azaz a parancs harmadik szava
   *
   * @return parancs harmadik szava (Tárgy részes esete)
   */
  public String getReszes() {
    try {
      return szavak.get(2);
    } catch (ArrayIndexOutOfBoundsException ex) {
      return "";
    }
  }

  /**
   * Leltárra utaló szó kiszűrése
   *
   * @return igaz, ha volt leltár szó a parancsban
   */
  public boolean isLeltar() {
    return (szavak.stream().anyMatch(szo -> szo.contains(LELTAR)));
  }

  /**
   * Ajtónyitásra irányuló kifejezés
   *
   * @return igaz, ha kinyitni akar a játékos
   */
  public boolean isKinyit() {
    try {
      return szavak.get(0).equals(KINYIT);
    } catch (ArrayIndexOutOfBoundsException ex) {
      return false;
    }
  }

  /**
   * Tárgyak felvételére irányuló kifejezés
   *
   * @return igaz, ha a játékos fel akar venni valamit
   */
  public boolean isFelvesz() {
    for (String szo : FELVESZ) {
      if (szo.equals(szavak.get(0))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Tárgyak lerakására irányuló kifejezés
   *
   * @return igaz, ha a játékos le akar tenni valamit
   */
  public boolean isLetesz() {
    for (String szo : LETESZ) {
      if (szo.equals(szavak.get(0))) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Tárgyak megvizsgálására irányuló kifejezés
   * 
   * @return igaz, ha a játékos meg akar vizsgálni valamit
   */
  public boolean isVizsgal() {
    for (String szo : VIZSGAL) {
      if (szo.equals(szavak.get(0))) {
        return true;
      }
    }
    return false;
  }

}
