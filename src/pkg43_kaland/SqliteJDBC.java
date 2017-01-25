package pkg43_kaland;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class SqliteJDBC {
  
  private final PreparedStatement minden;
  
  public SqliteJDBC(Connection kon) throws SQLException {
    minden = kon.prepareStatement("SELECT * FROM helyiseg");
  }
  
  List<Helyszin> minden() throws SQLException {
    return tobbTalalat(minden.executeQuery());
  }
    
  private Helyszin egyTalalat(ResultSet rs) throws SQLException {    
    return Helyszin.uj(rs);
  }
  
  private List<Helyszin> tobbTalalat(ResultSet rs) throws SQLException {
    List<Helyszin> helyszinek = new ArrayList<>();
    while (rs.next()) {
      helyszinek.add(egyTalalat(rs));
    }
    return helyszinek;
  }
  
}
