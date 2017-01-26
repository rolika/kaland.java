package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import static pkg43_kaland.Elem.IRANYOK;

public class AkadalyIrany implements Elem<AkadalyIrany> {
  
  private final String helyszin;
  private final Map<String, String> iranyok;
  
  public AkadalyIrany(ResultSet rs) throws SQLException {
    helyszin = rs.getString("helyszin");
    iranyok = new HashMap<>();
    for (String irany : IRANYOK) {
      String akadaly = rs.getString(irany);
      iranyok.put(irany, akadaly.equals("NULL") ? null : akadaly);
    }
  }
  
  public String getAkadaly(String irany) {
    return iranyok.get(irany);
  }

  @Override
  public String getNev() {
    return helyszin;
  }
  
}
