package pkg43_kaland;

import java.util.Scanner;

/**
 * Szöveges játék begépelős változata
 *
 * @author rolika
 */
public class Konzol {

  private final Vilag vilag;
  Scanner bevitel;
  Parancs parancs;

  public Konzol(Vilag terkep) {
    this.vilag = terkep;
    bevitel = new Scanner(System.in);
    parancs = new Parancs();
  }

  public void jatek() {

    while (true) {
      if (vilag.getHelyszin().isSotet() && !vilag.getTargy("zseblámpát").isAktiv()) {
        System.out.println(vilag.getUzenet(5));
      } else {
        System.out.println(vilag.getHelyszin().getLeiras());
        System.out.println(vilag.getLathatoTargyak());
        vilag.getHelyszin().setBejart(true);
      }
      System.out.print("> ");
      parancs.szetszed(bevitel.nextLine());
      if (parancs.isIrany()) {
        System.out.println(vilag.ujHelyszin(parancs.getIrany()));
      } else if (parancs.isBekapcsol()) {
        System.out.println(vilag.beallit(parancs.getTargy(), true));
      } else if (parancs.isKikapcsol()) {
        System.out.println(vilag.beallit(parancs.getTargy(), false));
      } else if (parancs.isLeltar()) {
        
      } else {
        System.out.println(vilag.getUzenet(6));
      }
    }

  }

}
