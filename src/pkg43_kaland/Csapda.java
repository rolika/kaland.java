package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Csapda kezelése: a csapda egy adott helyiséghez tartozik,
 * adott helyiség felé történő elmozdulással
 * lehet kiváltani, ez a viszonylat: helyszin;irany.
 * @author rolika
 */
public class Csapda implements Elem<Csapda> {
  
  private final String nev, viszonylat, halalUzenet, felfedezesUzenet, hatastalanUzenet;
  private boolean aktiv;
  
  /**
   * A konstruktor beolvassa a csapda nevét, helyét, a halál, felfedezés és hatástalanított 
   * üzeneteket, és aktiválja is a csapdát.
   * @param rs resultset az adatbázisból
   * @throws SQLException
   */
  public Csapda(ResultSet rs) throws SQLException {
    nev = rs.getString("nev");
    viszonylat = rs.getString("viszonylat");
    halalUzenet = rs.getString("halal");
    felfedezesUzenet = rs.getString("felfed");
    hatastalanUzenet = rs.getString("hatastalan");
    aktiv = true;
  }

  /**
   * Visszaadja a csapda nevét (hashmap-kulcs)
   */
  @Override
  public String getNev() {
    return nev;
  }
  
  /**
   * Aktiválja vagy deaktiválja a csapdát
   * @param aktiv hamis, ha kikapcsolni kell
   */
  public void setAktiv(boolean aktiv) {
    this.aktiv = aktiv;
  }
  
  /**
   * Visszaadja a csapda helyét
   * @return helyszín rövid neve
   */
  public String getHely() {
    return viszonylat.split(";")[0];
  }
  
  /**
   * Visszaadja a csapda irányát az adott helyiségben
   * @return irány ékezetlen neve
   */
  public String getCel() {
    return viszonylat.split(";")[1];
  }
  
  /**
   * Visszaadja a csapdába esett játékos halál-leírását :-)
   * @return
   */
  public String getHalalUzenet() {
    return halalUzenet;
  }
  
  /**
   * Amikor a játékos felfedezi, vagy hatástalanítja a csapdát
   * @return
   */
  public String getFelfedezesUzenet() {
    return felfedezesUzenet;
  }
  
  /**
   * Amikor a játékos elmegy egy hatástalanított csapda mellett
   * @return
   */
  public String getHatastalanUzenet() {
    return hatastalanUzenet;
  }
  
  /**
   * Aktív-e a csapda
   * @return igaz, ha aktív
   */
  public boolean isAktiv() {
    return aktiv;
  }
  
}
