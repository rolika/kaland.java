package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Akadaly implements Elem<Akadaly> {
  
  private final String nev;
  private final String leiras;
  private boolean aktiv;
  private boolean halalos;
  
  public Akadaly(ResultSet rs) throws SQLException {
    nev = rs.getString("nev");
    leiras = rs.getString("leiras");
    aktiv = rs.getBoolean("aktiv");
    halalos = rs.getBoolean("halalos");
  }

  @Override
  public String getNev() {
    return nev;
  }
  
  public String getLeiras() {
    return leiras;
  }
  
  public boolean isAktiv() {
    return aktiv;
  }
  
  public boolean isHalalos() {
    return halalos;
  }
  
  public void setAktiv(boolean aktiv) {
    this.aktiv = aktiv;
  }
  
  public void setHalalos(boolean halalos) {
    this.halalos = halalos;
  }
  
}
