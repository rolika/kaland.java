package pkg43_kaland;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    
    //Jatekter.main();
    try (Connection kon = DriverManager.getConnection("jdbc:sqlite:kaland.sql")) {
      SqliteJDBC sql = new SqliteJDBC(kon);
      sql.minden("helyiseg").forEach(System.out::println);
      sql.minden("kijarat").forEach(System.out::println);
    } catch (SQLException ex) {
      System.out.println("SQL hiba\n" + ex.getMessage());
    }
    
  }

}
