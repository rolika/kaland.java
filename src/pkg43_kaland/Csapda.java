package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Csapda kezelése
 * @author rolika
 */
public class Csapda implements Elem<Csapda> {
  
  private final String nev, hely, halalUzenet, felfedezesUzenet, hatastalanUzenet;
  private boolean aktiv;
  
  /**
   * A konstruktor beolvassa a csapda nevét, helyét, a halál, felfedezés és hatástalanított 
   * üzeneteket, és aktiválja is a csapdát
   * @param rs resultset az adatbázisból
   * @throws SQLException
   */
  public Csapda(ResultSet rs) throws SQLException {
    nev = rs.getString("nev");
    hely = rs.getString("hely");
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
    return hely;
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
  
}
