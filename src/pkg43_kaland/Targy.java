package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Tárgyak kezelése a kalandjátékban
 * @author Roland
 */
public class Targy implements Elem<Targy>{
  
  private final String nev, leiras, targyeset, reszeset; // hely adatbázisból beolvasáshoz
  private String hely;
  private boolean aktiv, felveheto, lathato;
  private Helyszin helyszin;
  private final boolean aktivalhato; // használható
  
  /**
   * Konstruktor adatbázisból kiolvasott találokra
   * @param rs
   * @throws SQLException
   */
  public Targy(ResultSet rs) throws SQLException {
    nev = rs.getString("nev");
    leiras = rs.getString("leiras");
    hely = rs.getString("hely");
    targyeset = rs.getString("targyeset");
    reszeset = rs.getString("reszeset");
    felveheto = rs.getBoolean("felveheto");
    aktivalhato = rs.getBoolean("aktivalhato");
    lathato = rs.getBoolean("lathato");
    aktiv = false;
  }
  
  public void setAktiv(boolean aktiv) {
    this.aktiv = aktiv;
  }
  
  public void setFelveheto(boolean felveheto) {
    this.felveheto = felveheto;
  }
  
  public void setLathato(boolean lathato) {
    this.lathato = lathato;
  }
  
  /*public void setHelyszin(Helyszin helyszin) {
    this.helyszin = helyszin;
  }*/
  
  public void setHely(String hely) {
    this.hely = hely;
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

  public boolean isLathato() {
    return lathato;
  }

  boolean isAktivalhato() {
    return aktivalhato;
  }
  
}
