package pkg43_kaland;

import java.util.List;
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
  List<String> uzenetek;
  
  public Konzol(Terkep terkep, List<String> uzenetek) {
    this.terkep = terkep;
    bevitel = new Scanner(System.in);
    jatekos = new Jatekos(terkep.getHelyszin("Ház előtt"));
    cel = null;
    parancs = new Parancs();
    this.uzenetek = uzenetek;
  }
  
  public void jatek() {
    
    while (true) {
      helyszin = jatekos.holVan();
      if (helyszin.isSotet() && !terkep.isZseblampaAktiv()) {
        System.out.println(uzenetek.get(5));
      } else {
        System.out.println(helyszin.getLeiras());
        helyszin.setBejart(true);
      }
      System.out.print("> ");
      parancs.szetszed(bevitel.nextLine());
      if (parancs.isIrany()) {
        String celHelyszin = jatekos.megy(parancs.getIrany());
        if (celHelyszin == null) {
          System.out.println(uzenetek.get(1));
        } else {
          Helyszin ujHelyszin = terkep.getHelyszin(celHelyszin);
          jatekos.setHelyszin(ujHelyszin);
        }
      } else {
        System.out.println(uzenetek.get(6));
      }
    }
    
  }
  
}
