package pkg43_kaland;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    
  public static void main(String[] args) {    
    
    Vilag vilag = null;
    
    //Jatekter.main();
    try (Connection kon = DriverManager.getConnection("jdbc:sqlite:kaland.sql")) {
      SqliteJDBC sql = new SqliteJDBC(kon);
      vilag = new Vilag(sql.minden("helyszin"), sql.minden("kijarat"), sql.minden("uzenet"),
        sql.minden("targy"), sql.minden(("ajto")));
    } catch (SQLException ex) {
      System.out.println("SQL hiba\n" + ex.getMessage());
    }
    
    if (vilag != null) {
      Konzol konzol = new Konzol(vilag);
      konzol.jatek();
    }
    
  }

}
