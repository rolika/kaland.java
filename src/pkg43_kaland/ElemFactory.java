package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ElemFactory {
  
  public static Elem uj(String tabla, ResultSet rs) throws SQLException {
    switch (tabla) {
      case "helyszin":
        return new Helyszin(rs);
      case "kijarat":
        return new Kijarat(rs);
      case "akadaly":
        return new Akadaly(rs);
      case "akadalyirany":
        return new AkadalyIrany(rs);
      default:
        return null;
    }
  }
  
}
