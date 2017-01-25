package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Helyszin implements Elem<Helyszin> {
  
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
  
  public String helyszinLeiras() {
    return sotet ? SOTET : (bejart ? nev : leiras);
  }
  
  @Override
  public String toString() {
    return String.format("%s: %ssötét, %sbejárt",
      nev, sotet ? "" : "nincs ", bejart ? "" : "nem ");
  }

  @Override
  public String getCel(String irany) {
    return null; // nem használt
  }
  
}
