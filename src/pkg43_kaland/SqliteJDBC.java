package pkg43_kaland;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqliteJDBC {

  private PreparedStatement minden;
  private String mindenString = "SELECT * FROM helyszin"; // a táblanevet hardkódolni kell
  private final Connection kon;

  public SqliteJDBC(Connection kon) throws SQLException {
    minden = kon.prepareStatement(mindenString);
    this.kon = kon;
  }

  public List<Elem> minden(String tabla) throws SQLException {
    ujTabla(tabla);
    return tobbTalalat(tabla, minden.executeQuery());
  }
  
  private void ujTabla(String tabla) throws SQLException {
    mindenString = mindenString.replaceAll("[a-z]+", tabla);
    minden = kon.prepareStatement(mindenString);
  }

  private Elem egyTalalat(String tabla, ResultSet rs) throws SQLException {
    return ElemFactory.uj(tabla, rs);
  }

  private List<Elem> tobbTalalat(String tabla, ResultSet rs) throws SQLException {
    List<Elem> elemek = new ArrayList<>();
    while (rs.next()) {
      elemek.add(egyTalalat(tabla, rs));
    }
    return elemek;
  }

}
