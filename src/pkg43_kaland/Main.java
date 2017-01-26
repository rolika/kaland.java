package pkg43_kaland;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    
  public static void main(String[] args) {    
    
    Terkep terkep = null;
    
    //Jatekter.main();
    try (Connection kon = DriverManager.getConnection("jdbc:sqlite:kaland.sql")) {
      SqliteJDBC sql = new SqliteJDBC(kon);
      terkep = new Terkep(sql.minden("helyszin"), sql.minden("kijarat"), sql.minden("akadaly"),
        sql.minden("akadalyirany"));
    } catch (SQLException ex) {
      System.out.println("SQL hiba\n" + ex.getMessage());
    }
    
    if (terkep != null) {
      Konzol konzol = new Konzol(terkep);
      konzol.jatek();
    }
    
  }

}
