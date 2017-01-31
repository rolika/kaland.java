package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Helyszín kijáratai. Az egyszerű kezelés miatt tartalmazza a helyszín nevét, valamint
 * a lehetésges kijáratokat a helyszínek rövid neveivel jelölve, ill. null-lal, ha arra nincs
 * kijárat
 * @author Roland
 */
public class Kijarat implements Elem<Kijarat> {

  private final String helyszin;
  private final Map<String, String> kijaratok;

  /**
   * Konstruktor adatbázisból kiolvasott találokra
   * @param rs
   * @throws SQLException
   */
  public Kijarat(ResultSet rs) throws SQLException {
    helyszin = rs.getString("helyszin");
    kijaratok = new HashMap<>();
    for (String irany : IRANYOK) {
      String cel = rs.getString(irany);
      kijaratok.put(irany, cel.equals("NULL") ? null : cel);
    }
  }
  
  /**
   * Ehhez a helyszínhez tartozik ez a kijárat
   * @return helyszín rövid neve
   */
  @Override
  public String getNev() {
    return helyszin;
  }
  
  /**
   * kijárat hashmapből visszaadja a célhelyiséget, ill. null-t, ha arra nem lehet menni
   * @param irany
   * @return célhelyszin rövid neve vagy null
   */
  public String getCel(String irany) {
    return kijaratok.get(irany);
  }
  
  /**
   * String reprezentáció tesztelési célból
   * @return
   */
  @Override
  public String toString() {
    String kijarat = "";
    for (String irany : IRANYOK) {
      kijarat += kijaratok.get(irany) + ", ";
    }
    return String.format("%s: %s", helyszin, kijarat);
  }

}
