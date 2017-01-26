package pkg43_kaland;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    
  public static void main(String[] args) {
    
    Scanner bevitel = new Scanner(System.in);
    Terkep terkep = null;
    String helyszin = "Ház előtt";
    
    //Jatekter.main();
    try (Connection kon = DriverManager.getConnection("jdbc:sqlite:kaland.sql")) {
      SqliteJDBC sql = new SqliteJDBC(kon);
      terkep = new Terkep(sql.minden("helyszin"), sql.minden("kijarat"), sql.minden("akadaly"),
        sql.minden("akadalyirany"));
    } catch (SQLException ex) {
      System.out.println("SQL hiba\n" + ex.getMessage());
    }
    
    while (true && terkep != null) {
      System.out.println(terkep.getLeiras(helyszin));
      System.out.print("Merre mész? ");
      String parancs = bevitel.nextLine();
      String szandek = terkep.szandek(helyszin, parancs);
      if (szandek != null) {
        helyszin = szandek;
      } else {
        System.out.println("Arra nem mehetsz!");
      }
    }
    
  }

}
