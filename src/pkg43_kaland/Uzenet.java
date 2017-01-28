package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Uzenet implements Elem<Uzenet> {
  
  private final String szoveg;

  public Uzenet(ResultSet rs) throws SQLException {
    szoveg = rs.getString("szoveg");
  }

  @Override
  public String getNev() {
    return szoveg;
  }
  
}
