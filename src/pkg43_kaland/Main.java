package pkg43_kaland;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    
  public static void main(String[] args) {    
    
    Terkep terkep = null;
    List<String> uzenetek = new ArrayList<>();
    uzenetek.add(""); // egyezzen az sqlite id a lista indexével
    
    //Jatekter.main();
    try (Connection kon = DriverManager.getConnection("jdbc:sqlite:kaland.sql")) {
      SqliteJDBC sql = new SqliteJDBC(kon);
      terkep = new Terkep(sql.minden("helyszin"), sql.minden("kijarat"));
      for (Elem elem : sql.minden("uzenet")) {
        uzenetek.add(elem.getNev());
      }
    } catch (SQLException ex) {
      System.out.println("SQL hiba\n" + ex.getMessage());
    }
    
    if (terkep != null) {
      Konzol konzol = new Konzol(terkep, uzenetek);
      konzol.jatek();
    }
    
  }

}
