package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Targy implements Elem<Targy>{
  
  private final String nev, leiras, hely, targyeset, reszeset; // hely adatbázisból beolvasáshoz
  private boolean aktiv;
  private boolean felveheto;
  private Helyszin helyszin;
  
  public Targy(ResultSet rs) throws SQLException {
    nev = rs.getString("nev");
    leiras = rs.getString("leiras");
    hely = rs.getString("hely");
    targyeset = rs.getString("targyeset");
    reszeset = rs.getString("reszeset");
    felveheto = rs.getBoolean("felveheto");
    aktiv = false;
  }
  
  public void setAktiv(boolean aktiv) {
    this.aktiv = aktiv;
  }
  
  public void setFelveheto(boolean felveheto) {
    this.felveheto = felveheto;
  }
  
  public void setHelyszin(Helyszin helyszin) {
    this.helyszin = helyszin;
  }

  @Override
  public String getNev() {
    return nev;
  }
  
  public String getLeiras() {
    return leiras;
  }
  
  public String getHely() {
    return hely;
  }
  
  public String getTargyeset() {
    return targyeset;
  }
  
  public String getReszeset() {
    return reszeset;
  }
  
  public boolean isAktiv() {
    return aktiv;
  }
  
  public boolean isFelveheto() {
    return felveheto;
  }
  
  public Helyszin getHelyszin() {
    return helyszin;
  }
  
}
