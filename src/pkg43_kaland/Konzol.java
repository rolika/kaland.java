package pkg43_kaland;

import java.util.Scanner;
import org.apache.commons.lang3.text.WordUtils;

/**
 * Szöveges játék begépelős változata *
 * @author rolika
 */
public class Konzol {
  
  private final static int WRAP = 80;

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
        System.out.println(WordUtils.wrap(vilag.getHelyszin().getLeiras(), WRAP));
        System.out.println(WordUtils.wrap(vilag.getLathatoTargyak(), WRAP));
        vilag.getHelyszin().setBejart(true);
      } else {
        System.out.println(WordUtils.wrap(vilag.getUzenet(5), WRAP));
      }
      System.out.print("> ");
      parancs.szetszed(bevitel.nextLine());
      Csapda csapda = vilag.getCsapda();
      if (parancs.isIrany()) {
        if (csapda != null && vilag.getCelNev(parancs.getIrany()) != null) {
          if (vilag.getCelNev(parancs.getIrany()).equals(csapda.getCel())) {
            if (csapda.isAktiv()) {
              System.out.println(WordUtils.wrap(csapda.getHalalUzenet(), WRAP));
              break;
            }
          }
        }
        String leiras = vilag.ujHelyszin(parancs.getIrany());
        if (csapda != null && vilag.getCelNev(parancs.getIrany()) != null) {
          System.out.println(leiras);
          System.out.println(WordUtils.wrap(csapda.getHatastalanUzenet(), WRAP));
        } else {
          System.out.println(leiras);
        }
      } else if (parancs.isBekapcsol()) {
        System.out.println(vilag.aktival(parancs.getTargy(), true));
      } else if (parancs.isKikapcsol()) {
        System.out.println(vilag.aktival(parancs.getTargy(), false));
      } else if (parancs.isLeltar()) {
        if (vilag.isVilagos()) {
          System.out.println(WordUtils.wrap(vilag.getLeltar(), WRAP));
        }
      } else if (parancs.isKinyit()) {
        System.out.println(vilag.kinyit(parancs.getTargy(), parancs.getReszes()));
      } else if (parancs.isFelvesz()) {
        System.out.println(vilag.felvesz(parancs.getTargy()));
        if (vilag.getLeltar().contains("lábtörlő")) {
          vilag.getTargy("kulcsot").setLathato(true);
        }
      } else if (parancs.isLetesz()) {
        System.out.println(vilag.letesz(parancs.getTargy()));
      } else if (parancs.isVizsgal()) {
        if (vilag.getHelyszin().getNev().equals("Előtér") && parancs.getTargy().equals("padlót")) {
          //Csapda csapda = vilag.getCsapda();
          System.out.println(WordUtils.wrap(csapda.getFelfedezesUzenet(), WRAP));
          csapda.setAktiv(false);
        } else {
          System.out.println(WordUtils.wrap(vilag.vizsgal(parancs.getTargy()), WRAP));
        }
      } else {
        System.out.println(vilag.getUzenet(6)); // nem érti az értelmező
      }
    }
    System.out.println(vilag.getUzenet(16)); // meghaltál!

  }

}
