package pkg43_kaland;

public class Jatekos {
  
  private boolean el;
  private Helyszin helyszin;
  // private Leltar leltar();
  
  public Jatekos(Helyszin helyszin) {
    this.helyszin = helyszin;
    el = true;
  }
  
  public void meghalt() {
    el = false;
  }
  
  public boolean eletbenVan() {
    return el;
  }
  
  public void mozog(Helyszin helyszin) {
    this.helyszin = helyszin;
  }
  
  public Helyszin holVan() {
    return helyszin;
  }
  
}
