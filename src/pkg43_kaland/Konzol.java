package pkg43_kaland;

import java.util.Scanner;

/**
 * Szöveges játék begépelős változata *
 * @author rolika
 */
public class Konzol {

  private final Vilag vilag;
  Scanner bevitel;
  Parancs parancs;

  /**
   * A konstruktor megkapja a világot és inicializálja a parancsértelmezőt
   * @param vilag a játék itt játszódik
   */
  public Konzol(Vilag vilag) {
    this.vilag = vilag;
    bevitel = new Scanner(System.in);
    parancs = new Parancs();
  }

  /**
   * Játék indítása
   */
  public void jatek() {

    while (true) { // fő játékciklus
      if (vilag.isVilagos()) {
        //System.out.println("világos " + vilag.isVilagos()); // debug!
        System.out.println(vilag.getHelyszin().getLeiras());
        System.out.println(vilag.getLathatoTargyak());
        vilag.getHelyszin().setBejart(true);
      } else {
        System.out.println(vilag.getUzenet(5));
      }
      System.out.print("> ");
      parancs.szetszed(bevitel.nextLine());
      if (parancs.isIrany()) {
        System.out.println(vilag.ujHelyszin(parancs.getIrany()));
      } else if (parancs.isBekapcsol()) {
        System.out.println(vilag.aktival(parancs.getTargy(), true));
      } else if (parancs.isKikapcsol()) {
        System.out.println(vilag.aktival(parancs.getTargy(), false));
      } else if (parancs.isLeltar()) {
        //System.out.println("idáig eljut " + vilag.isVilagos()); // debug!
        if (vilag.isVilagos()) {
          System.out.println(vilag.getLeltar());
        }
      } else {
        System.out.println(vilag.getUzenet(6));
      }
    }

  }

}
