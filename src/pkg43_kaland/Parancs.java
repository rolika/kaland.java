package pkg43_kaland;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Parancs {
  
  // irányszavak mozgáshoz
  private static final String[] ESZAK = { "é", "észak","északra", "északnak" };
  private static final String[] DEL = { "d", "dél","délre", "délnek" };
  private static final String[] KELET = { "k", "kelet","keletre", "keletnek" };
  private static final String[] NYUGAT = { "ny", "nyugat","nyugatra", "nyugatnak" };
  private static final String[] LE = { "le", "lefelé", "lefele" };
  private static final String[] FEL = { "fel", "felfelé", "felfele" };
  private static final String[] INDIREKT = { "ki", "be" };
  
  // szavak bekapcsoláshoz
  private static final String[] BEKAPCSOL = { "bekapcsolom", "felkapcsolom" };
  private static final String[] KIKAPCSOL = { "kikapcsolom", "lekapcsolom" };
  
  private final Map<String, Set<String>> iranyok;
  private String irany;
  private List<String> szavak;
  
  public Parancs() {
    iranyok = new HashMap<>();
    iranyok.put("eszak", new HashSet(Arrays.asList(ESZAK)));
    iranyok.put("del", new HashSet(Arrays.asList(DEL)));
    iranyok.put("kelet", new HashSet(Arrays.asList(KELET)));
    iranyok.put("nyugat", new HashSet(Arrays.asList(NYUGAT)));
    iranyok.put("le", new HashSet(Arrays.asList(LE)));
    iranyok.put("fel", new HashSet(Arrays.asList(FEL)));
    iranyok.put("indirekt", new HashSet(Arrays.asList(INDIREKT)));
    irany = "";
    szavak = new ArrayList<>();
  }
  
  public void szetszed(String parancs) {
    szavak = Arrays.asList(parancs.split("\\sa?z?\\s?"));
  }
  
  public boolean isIrany() {
    for (String szo : szavak) {
      for (String kulcs : iranyok.keySet()) {
        if (iranyok.get(kulcs).contains(szo)) {
          irany = kulcs;
          return true;
        }
      }
    }
    return false;
  }
  
  public String getIrany() {
    return irany;
  }
  
  public boolean isBekapcsol() {
    for (String szo : BEKAPCSOL) {
      if (szo.equals(szavak.get(0))) {
        return true;
      }
    }
    return false;
  }
  
  public String getTargy() {
    return szavak.get(1);
  }
  
  public boolean isLeltar() {
    return false;
  }
  
}
