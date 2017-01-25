package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Kijarat implements Elem<Kijarat> {

  private static final String[] IRANYOK =
    { "eszak", "kelet", "del", "nyugat", "le", "fel", "indirekt" };
  
  private final String helyszin;
  private final Map<String, String> kijaratok;

  public Kijarat(ResultSet rs) throws SQLException {
    helyszin = rs.getString("helyszin");
    kijaratok = new HashMap<>();
    for (String irany : IRANYOK) {
      String cel = rs.getString(irany);
      kijaratok.put(irany, cel.equals("NULL") ? null : cel);
    }
  }
  
  @Override
  public String getNev() {
    return helyszin;
  }
  
  @Override
  public String getCel(String irany) {
    return kijaratok.get(irany);
  }
  
  @Override
  public String toString() {
    String kijarat = "";
    for (String irany : IRANYOK) {
      kijarat += kijaratok.get(irany) + ", ";
    }
    return String.format("%s: %s", helyszin, kijarat);
  }

}
