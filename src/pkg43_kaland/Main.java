package pkg43_kaland;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A játék egy szöveges kalandjátékot valósít meg. A szöveges kalandjátékban nincsenek
 * grafikák, a számítógép leírja a játékos által látogatható helyszíneket. A játékos a rendelkezé-
 * re álló területet szabadon bejárhatja, tárgyakat, környezeti elemeket vizsgálhat meg, tárgyakat
 * vehet fel ill. manipulálhat a továbbjutás érdekében. Ebben a játékban a magyar nyelvtan
 * szabályai szerint lehet parancsokat adni: FELVESZEM A KULCSOT, vagy KINYITOM AZ AJTÓT A KULCCSAL.
 * Mozogni az égtájakkal lehet, pl. É, D, K, NY, LE, FEL, de a program megérti a komplexebb
 * megfogalmazást is, pl. ELMEGYEK ÉSZAKRA. A játékosnál lévő tárgyakat a LELTÁR parancs sorolja
 * fel. A parancsokat névelő nélkül is elfogadja az értelmező.
 * @author rolika
 */
public class Main {
    
  public static void main(String[] args) {
    
    /*
    A világ tartalmazza a helyszíneket, kijáratokat, üzeneteket, tárgyakat, ajtókat, csapdákat és
    ellenségeket. Ezeket az adatbázisból kell beolvasni. Viszont mivel mindegyik más osztályban
    "köt ki", kell a beolvasáshoz egy egységes interface, ez az Elem. Hogy a kezdetben általános
    elemből melyik számomra szükséges osztály lesz, azt az ElemFactory dönti el, a megadott
    adatbázis-táblanév alapján.
    */
    
    Vilag vilag = null;
    
    try (Connection kon = DriverManager.getConnection("jdbc:sqlite:kaland.sql")) {
      SqliteJDBC sql = new SqliteJDBC(kon);
      vilag = new Vilag(sql.minden("helyszin"), sql.minden("kijarat"), sql.minden("uzenet"),
        sql.minden("targy"), sql.minden("ajto"), sql.minden("csapda"), sql.minden("ellenseg"));
    } catch (SQLException ex) {
      System.out.println("SQL hiba\n" + ex.getMessage());
    }
    
    if (vilag != null) {
      Jatekter.main();
      
      //Konzol konzol = new Konzol(vilag);
      //konzol.jatek();
    }
    
  }

}
