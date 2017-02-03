package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Az ElemFactory az általános elemekből készít specifikus osztályokat
 * @author Roland
 */
public class ElemFactory {
  
  /**
   * Az általános Elemből specifikus osztályt kéazít a táblanév alapján
   * @param tabla adatbázis táblaneve alapján azonosítja a visszaadandó új osztályt
   * @param rs Resultset az adatbázisból
   * @return specifikus osztály (ami megvalósítja az elem interface-t)
   * @throws SQLException
   */
  public static Elem uj(String tabla, ResultSet rs) throws SQLException {
    switch (tabla) {
      case "helyszin":
        return new Helyszin(rs);
      case "kijarat":
        return new Kijarat(rs);
      case "uzenet":
        return new Uzenet(rs);
      case "targy":
        return new Targy(rs);
      case "ajto":
        return new Ajto(rs);
      case "csapda":
        return new Csapda(rs);
      default:
        return null;
    }
  }
  
}
