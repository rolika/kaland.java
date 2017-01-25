package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Helyiség kijáratait nyilvántartó osztály
 * @author Roland
 */
class Kijarat implements Elem<Kijarat> {

  private static final String[] IRANYOK =
    { "eszak", "kelet", "del", "nyugat", "le", "fel", "indirekt" };
  
  private Map<String, String> kijaratok;

  public Kijarat(ResultSet kijarat) {
    kijaratok = new HashMap<>();
    Arrays.asList(IRANYOK).forEach(irany -> {
      try {
        kijaratok.put(irany, kijarat.getString(irany));
      } catch (SQLException ex) {
        System.out.println("SQL-hiba!");
        System.out.println(ex.getMessage());
      }
    });
  }
  
  public String getKijarat(String irany) {
    return kijaratok.get(irany);
  }

  @Override
  public Kijarat uj(ResultSet rs) throws SQLException {
    return new Kijarat(rs);
  }
  
  @Override
  public String toString() {
    return String.format("");
  }

}
