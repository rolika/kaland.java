package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Kalandjáték helyszíneinek megvalósítása
 * @author Roland
 */
public class Helyszin implements Elem<Helyszin> {
  
  private final String nev;
  private final String leiras;
  private boolean sotet;
  private boolean bejart;
  private Kijarat kijarat;
  
  /**
   * Konstruktor adatbázisból kiolvasott találokra
   * @param rs
   * @throws SQLException
   */
  public Helyszin(ResultSet rs) throws SQLException {
    nev = rs.getString("nev");
    leiras = rs.getString("leiras");
    sotet = rs.getBoolean("sotet");
    bejart = false;
  }

  /**
   * Helyiség rövid leírása, ill. kulcs a tároló hashmap-ben
   * @return rövid név
   */
  @Override
  public String getNev() {
    return nev;
  }
  
  /**
   * Sötét van-e a helyiségben
   * @return igaz, ha sötét van
   */
  public boolean isSotet() {
    return sotet;
  }
  
  /**
   * Hosszú helyszínleírás
   * @return szöveg
   */
  public String getHosszu() {
    return leiras;
  }
  
  /**
   * Beállítja a helyszín látogatottságát
   * @param bejart igaz, ha volt már itt a játékos
   */
  public void setBejart(boolean bejart) {
    this.bejart = bejart;
  }
  
  /**
   * Beállítja a helyiség bevilágítottságát
   * Sötétben csak mozogni lehet
   * @param sotet
   */
  public void setSotet(boolean sotet) {
    this.sotet = sotet;
  }
  
  /**
   * Beállítja a helysiég kijáratait (a kijáratok a helyiség részei)
   * @param kijarat Kijárat osztály az adatbázisból
   */
  public void setKijarat(Kijarat kijarat) {
    this.kijarat = kijarat;
  }
  
  /**
   * Helyszín leírása annak függvényében, járt-e itt már a játékos.
   * Ha először jár itt, hosszú leírást kap, egyébként csak rövedet (a helyszín nevét)
   * @return leírás
   */
  public String getLeiras() {
    return bejart ? nev : leiras;
  }
  
  /**
   * Az irány alapján visszaadja a célhelyiség nevét, ill. null-t, ha arra nincs kijárat
   * A null értéket a hívónak kezelnie kell.
   * @param irany ékezetlen égtájak + le-fel-indirekt
   * @return helyiség rövid neve
   */
  public String getKijarat(String irany) {
    return kijarat.getCel(irany);
  }
  
  /**
   * String reprezentáció tesztelési célból
   * @return
   */
  @Override
  public String toString() {
    return String.format("%s: %ssötét, %sbejárt",
      nev, sotet ? "" : "nincs ", bejart ? "" : "nem ");
  }
  
}
