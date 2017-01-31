package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Helyiségek közötti átjárók kezelése
 * @author Roland
 */
public class Ajto implements Elem<Ajto>{
  
  private final String egyik, masik; // a két érintett helyiség neve
  private int allapot; // 0: nyitott; 1: becsukott; 2: bezart
  
  /**
   * Konstruktor adatbázisból kiolvasott találokra
   * @param rs
   * @throws SQLException
   */
  public Ajto(ResultSet rs) throws SQLException {
    this.egyik = rs.getString("egyik");
    this.masik = rs.getString("masik");
    this.allapot = rs.getInt("allapot");
  }
  
  /**
   * Ajtó állapotának beállítása
   * @param allapot 0: nyitott; 1: becsukott; 2: bezart
   */
  public void setAllapot(int allapot) {
    this.allapot = allapot;
  }
  
  /**
   * Ajtó állapotának kiolvasása
   * @return 0: nyitott; 1: becsukott; 2: bezart
   */
  public int getAllapot() {
    return allapot;
  }
  
  /**
   * Ellenőrzi van-e ajtó két helyiség között
   * @param start kiindulási helyszín
   * @param cel elérni szándékozott helyszín
   * @return igaz, ha van ajtó
   */
  public boolean vanAjto(Helyszin start, Helyszin cel) {
    return (start.getNev().equals(egyik) && cel.getNev().equals(masik)) || // oda és
      (start.getNev().equals(masik) && cel.getNev().equals(egyik)); // vissza
  }
  
  /**
   * Csak az Elem interface miatt van itt (egyelőre)
   * @return null
   */
  @Override
  public String getNev() {
    return null; // itt nincs szükség rá
  }
  
}
