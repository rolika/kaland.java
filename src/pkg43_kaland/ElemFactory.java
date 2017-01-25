package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Factory különböző típusú elemek visszaadására
 * @author Roland
 */
public class ElemFactory {
  
  public static Elem uj(String tabla, ResultSet rs) throws SQLException {
    switch (tabla) {
      case "helyszin":
        return new Helyszin(rs);
      case "kijarat":
        return new Kijarat(rs);
      default:
        return null;
    }
  }
  
}
