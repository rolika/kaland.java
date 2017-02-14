package pkg43_kaland;

public class Jatekos {
  
  private static final int ODAAT = 4; // ennyi lépés-1 után csukódik be a portál
  private static final int POK = 2; // ennyi lépés-1 után támad és öl a pók
  
  private boolean eletbenVan, visszajott;
  private int odaatVan, pokTamad;
  
  public Jatekos() {
    eletbenVan = true;
    visszajott = false;
    odaatVan = ODAAT;
    pokTamad =POK;
  }
  
  public void setEletbenVan(boolean eletbenVan) {
    this.eletbenVan = eletbenVan;
  }
  
  public void setVisszaJott(boolean visszajott) {
    this.visszajott = visszajott;
  }
  
  public void setOdaatVan(int odaatVan) {
    this.odaatVan = odaatVan;
  }
  
  public void csokkentOdaat() {
    this.odaatVan -= 1;
  }
  
  public boolean getEletbenVan() {
    return eletbenVan;
  }
  
  public boolean getVisszaJott() {
    return visszajott;
  }
  
  public boolean getOttRagadt() {
    return odaatVan == 0;
  }
  
  public boolean getVoltOdaat() {
    return odaatVan < ODAAT;
  }
  
  public void csokkentPok() {
    pokTamad -= 1;
  }
  
  public boolean tamadPok() {
    return pokTamad == 0;
  }
  
}
