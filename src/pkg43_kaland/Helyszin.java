package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;

class Helyszin {
  
  private static final String SOTET = "Túl sötét van, nem látsz semmit!";
  
  private final String nev;
  private final String leiras;
  private boolean sotet;
  private boolean bejart;
  
  private Helyszin(String nev, String leiras, boolean sotet) {
    this.nev = nev;
    this.leiras = leiras;
    this.sotet = sotet;
    bejart = false;
  }
  
  static Helyszin uj(ResultSet rs) throws SQLException {
    return new Helyszin(rs.getString("nev"), rs.getString("leiras"), rs.getBoolean("sotet"));
  }
  
  String getnev() {
    return nev;
  }
  
  boolean isSotet() {
    return sotet;
  }
  
  void setBejart(boolean bejart) {
    this.bejart = bejart;
  }
  
  void setSotet(boolean sotet) {
    this.sotet = sotet;
  }
  
  String helyszinLeiras() {
    return sotet ? SOTET : (bejart ? nev : leiras);
  }
  
  @Override
  public String toString() {
    return String.format("%s: %ssötét, %sbejárt%n",
      nev, sotet ? "" : "nincs ", bejart ? "" : "nem ");
  }
  
}
