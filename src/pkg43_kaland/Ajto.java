package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Ajto implements Elem<Ajto>{
  
  private final String egyik, masik; // a két érintett helyiség neve
  private int allapot; // 0: nyitott; 1: becsukott; 2: bezart
  
  public Ajto(ResultSet rs) throws SQLException {
    this.egyik = rs.getString("egyik");
    this.masik = rs.getString("masik");
    this.allapot = rs.getInt("allapot");
  }
  
  public void setAllapot(int allapot) {
    this.allapot = allapot;
  }
  
  public int getAllapot() {
    return allapot;
  }
  
  public boolean vanAjto(Helyszin start, Helyszin cel) {
    return (start.getNev().equals(egyik) && cel.getNev().equals(masik)) || // oda és
      (start.getNev().equals(masik) && cel.getNev().equals(egyik)); // vissza
  }

  @Override
  public String getNev() {
    return null; // itt nincs szükség rá
  }
  
}
