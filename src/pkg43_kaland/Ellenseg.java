package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Ellenségek kezelésére szolgáló osztály
 * @author rolika
 */
public class Ellenseg implements Elem<Ellenseg> {
  
  private final String nev, leiras, hely, fegyver, halalUzenet, elpusztultUzenet;
  private boolean aktiv;
  
  /**
   * A konstruktor beolvassa az ellenség adatait az adatbázisból
   *
   * @param rs resultset az adatbázisból
   * @throws SQLException
   */
  public Ellenseg(ResultSet rs) throws SQLException {
    nev = rs.getString("nev");
    leiras = rs.getString("leiras");
    hely = rs.getString("hely");
    fegyver = rs.getString("fegyver");
    halalUzenet = rs.getString("halalUzenet");    
    elpusztultUzenet = rs.getString("elpusztultUzenet");
    aktiv = true;
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
  
  public String getFegyver() {
    return fegyver; // a fegyvert részes esettel tárolom
  }
  
  public String getHalalUzenet() {
    return halalUzenet;
  }
  
  public String getElpusztultUzenet() {
    return elpusztultUzenet;
  }
  
  public boolean isAktiv() {
    return aktiv;
  }
  
  public void setAktiv(boolean aktiv) {
    this.aktiv = aktiv;
  }
  
}
