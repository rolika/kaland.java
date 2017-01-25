package pkg43_kaland;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Különböző típusú rekordok kiolvasásához különböző típusú osztályokba
 * @author Roland
 * @param <E> elem típusa: helyszín, kijárat stb.
 */
public interface Elem<E> {
  
  E uj(ResultSet rs) throws SQLException;
  
}
