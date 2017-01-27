package pkg43_kaland;

import java.util.Scanner;

/**
 * Szöveges játék begépelős változata
 * @author rolika
 */
public class Konzol {
  
  private final Terkep terkep;
  Scanner bevitel;
  Helyszin helyszin, cel;
  Akadaly akadaly;
  Jatekos jatekos;
  
  public Konzol(Terkep terkep) {
    this.terkep = terkep;
    bevitel = new Scanner(System.in);
    jatekos = new Jatekos(terkep.getHelyszin("Ház előtt"));
    helyszin = jatekos.holVan();
    cel = null;
  }
  
  public void jatek() {
    
    String parancs;
    
    while (true) {
      System.out.println(helyszin.getLeiras());
      helyszin.setBejart(true);
      System.out.print("> ");
      parancs = bevitel.nextLine();
      akadaly = terkep.getAkadaly(helyszin, parancs);
      if (akadaly != null && akadaly.isAktiv()) {
        System.out.println(akadaly.getLeiras());
        continue;
      }
      cel = terkep.getCel(helyszin, parancs);
      if (cel == null) {
        System.out.println("Arra nem mehetsz!");
      } else {
        helyszin = cel;
      }
    }
    
  }
  
}
