package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Helyiségek közötti átjárók kezelése
 * @author Roland
 */
public class Ajto implements Elem<Ajto>{
  
  private final String nev, viszonylat, leiras, zarva, csukva, nyitva, targyeset; // viszonylat CSV
  private String allapot; // lehet "nyitva" || "csukva" ||"zárva"
  
  /**
   * Konstruktor adatbázisból kiolvasott találokra
   * @param rs
   * @throws SQLException
   */
  public Ajto(ResultSet rs) throws SQLException {
    nev = rs.getString("nev");
    viszonylat = rs.getString("viszonylat"); // CSV: helyszín-név;helyszín-név
    leiras = rs.getString("leiras");
    zarva = rs.getString("zarva");
    csukva = rs.getString("csukva");
    nyitva = rs.getString("nyitva");
    targyeset = rs.getString("targyeset");
    allapot = rs.getString("allapot");
  }
  
  /**
   * Ajtó rövid neve
   * @return
   */
  @Override
  public String getNev() {
    return nev;
  }
  
  /**
   * Ajtó leírása (viszgálathoz)
   * @return
   */
  public String getLeiras() {
    return leiras;
  }
  
  /**
   * Üzenet, ha az ajtó zárva van
   * @return
   */
  public String getZarvaUzenet() {
    return zarva;
  }
  
  /**
   * Üzenet, ha az ajtó be van csukva.
   * @return
   */
  public String getCsukva() {
    return csukva;
  }
  
  /**
   * Üzenet, amikor az ajtó ki lett nyitva.
   * @return
   */
  public String getNyitvaUzenet() {
    return nyitva;
  }
  
  public String getTargyeset() {
    return targyeset;
  }
  
  /**
   * Ajtó állapotának kiolvasása
   * @return "nyitva" || "csukva" ||"zárva"
   */
  public String getAllapot() {
    return allapot;
  }
  
  /**
   * Ajtó állapotának beállítása
   * @param allapot "nyitva" || "csukva" ||"zárva"
   */
  public void setAllapot(String allapot) {
    this.allapot = allapot;
  }
  
  /**
   * Ellenőrzi van-e ajtó két helyiség között
   * @param start kiindulási helyszín
   * @param cel elérni szándékozott helyszín
   * @return igaz, ha van ajtó
   */
  public boolean vanAjto(Helyszin start, Helyszin cel) {
    return viszonylat.contains(start.getNev()) && viszonylat.contains(cel.getNev());
  }
  
}
