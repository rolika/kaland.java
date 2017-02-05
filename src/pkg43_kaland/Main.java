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
    
    Vilag vilag = null;
    
    //Jatekter.main();
    try (Connection kon = DriverManager.getConnection("jdbc:sqlite:kaland.sql")) {
      SqliteJDBC sql = new SqliteJDBC(kon);
      vilag = new Vilag(sql.minden("helyszin"), sql.minden("kijarat"), sql.minden("uzenet"),
        sql.minden("targy"), sql.minden("ajto"), sql.minden("csapda"));
    } catch (SQLException ex) {
      System.out.println("SQL hiba\n" + ex.getMessage());
    }
    
    if (vilag != null) {
      Konzol konzol = new Konzol(vilag);
      konzol.jatek();
    }
    
  }

}
