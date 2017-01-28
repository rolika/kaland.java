package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Helyszin implements Elem<Helyszin> {
  
  private static final String SOTET = "Túl sötét van, nem látsz semmit!";
  
  private final String nev;
  private final String leiras;
  private boolean sotet;
  private boolean bejart;
  private Kijarat kijarat;
  
  private Helyszin(String nev, String leiras, boolean sotet) {
    this.nev = nev;
    this.leiras = leiras;
    this.sotet = sotet;
    bejart = false;
  }
  
  public Helyszin(ResultSet rs) throws SQLException {
    nev = rs.getString("nev");
    leiras = rs.getString("leiras");
    sotet = rs.getBoolean("sotet");
    bejart = false;
  }

  @Override
  public String getNev() {
    return nev;
  }
  
  public boolean isSotet() {
    return sotet;
  }
  
  public void setBejart(boolean bejart) {
    this.bejart = bejart;
  }
  
  public void setSotet(boolean sotet) {
    this.sotet = sotet;
  }
  
  public void setKijarat(Kijarat kijarat) {
    this.kijarat = kijarat;
  }
  
  public String getLeiras() {
    return bejart ? nev : leiras;
  }
  
  public String getKijarat(String irany) {
    return kijarat.getCel(irany);
  }
  
  @Override
  public String toString() {
    return String.format("%s: %ssötét, %sbejárt",
      nev, sotet ? "" : "nincs ", bejart ? "" : "nem ");
  }
  
}
