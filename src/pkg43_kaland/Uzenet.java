package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Üzenetek kezelése kalandjétékban
 * @author Roland
 */
public class Uzenet implements Elem<Uzenet> {
  
  private final String szoveg;

  /**
   * Konstruktor adatbázisból kiolvasott találokra
   * @param rs
   * @throws SQLException
   */
  public Uzenet(ResultSet rs) throws SQLException {
    szoveg = rs.getString("szoveg");
  }

  /**
  * Üzenet kiolvasása
  * @return üzenet szövege
  */
  @Override
  public String getNev() {
    return szoveg;
  }
  
}
