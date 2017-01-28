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
  Jatekos jatekos;
  Parancs parancs;
  
  public Konzol(Terkep terkep) {
    this.terkep = terkep;
    bevitel = new Scanner(System.in);
    jatekos = new Jatekos(terkep.getHelyszin("Ház előtt"));
    cel = null;
    parancs = new Parancs();
  }
  
  public void jatek() {
    
    while (true) {
      helyszin = jatekos.holVan();
      System.out.println(helyszin.getLeiras());
      helyszin.setBejart(true);
      System.out.print("> ");
      parancs.szetszed(bevitel.nextLine());
      if (parancs.isIrany()) {
        String celHelyszin = jatekos.megy(parancs.getIrany());
        if (celHelyszin == null) {
          System.out.println("Arra nem mehetsz!");
        } else {
          jatekos.setHelyszin(terkep.getHelyszin(celHelyszin));
        }
      }
    }
    
  }
  
}
