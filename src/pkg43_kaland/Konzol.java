package pkg43_kaland;

import java.util.Scanner;
import org.apache.commons.lang3.text.WordUtils;

/**
 * Szöveges játék begépelős változata. A Vilag.java-t használja. Ez valósítja meg a játék logikáját,
 * azaz hangolja össze a világ lehetőségeit (metódusait).
 *
 * @author rolika
 */
public class Konzol {

  private final static int WRAP = 80;

  private final Vilag vilag;
  Scanner bevitel;
  Parancs parancs;
  Jatekos jatekos;

  /**
   * A konstruktor megkapja a világot és inicializálja a parancsértelmezőt
   *
   * @param vilag a játék itt játszódik
   */
  public Konzol(Vilag vilag) {
    this.vilag = vilag;
    bevitel = new Scanner(System.in);
    parancs = new Parancs();
    jatekos = new Jatekos();
  }

  /**
   * Játék indítása
   */
  public void jatek() {

    while (jatekos.getEletbenVan() && !jatekos.getOttRagadt() && !jatekos.getVisszaJott()) {
      if (vilag.getLeiroMod() == 1) {
        vilag.getAktualisHelyszin().setBejart(false);
      } else if (vilag.getLeiroMod() == 2) {
        vilag.getAktualisHelyszin().setBejart(true);
      }
      Ellenseg ellenseg = vilag.getEllenseg();
      if (vilag.isVilagos()) {
        System.out.println(WordUtils.wrap(vilag.getAktualisHelyszin().getLeiras(), WRAP));
        System.out.println(WordUtils.wrap(vilag.getLathatoTargyak(), WRAP));
        if (ellenseg != null && !ellenseg.isAktiv()) {
          System.out.println(WordUtils.wrap(vilag.getUzenet(24), WRAP)); // döglött pók
        }
        vilag.getAktualisHelyszin().setBejart(true);
        if (ellenseg != null && ellenseg.isAktiv()) {
          System.out.println(WordUtils.wrap(ellenseg.getLeiras(), WRAP));
          jatekos.csokkentPok();
          if (jatekos.tamadPok()) {
            System.out.println(WordUtils.wrap(ellenseg.getHalalUzenet(), WRAP));
            jatekos.setEletbenVan(false);
            continue;
          }
        }
      } else {
        System.out.println(WordUtils.wrap(vilag.getUzenet(5), WRAP));
        if (ellenseg != null && ellenseg.isAktiv()) {
          System.out.println(WordUtils.wrap(vilag.getUzenet(25), WRAP)); // sötétben támad az ellen
          jatekos.setEletbenVan(false);
          continue;
        }
      }
      System.out.print("> ");
      parancs.szetszed(bevitel.nextLine());
      Csapda csapda = vilag.getCsapda();
      if (parancs.isIrany()) {
        String uzenet = vilag.ujHelyszin(parancs.getIrany());
        String ujHelyszinNev = vilag.getAktualisHelyszin().getNev();
        if (csapda != null) {
          if (csapda.isAktiv()) {
            if (ujHelyszinNev.equals(csapda.getCel())) {
              System.out.println(WordUtils.wrap(csapda.getHalalUzenet(), WRAP));
              jatekos.setEletbenVan(false);
            } else {
              System.out.println(uzenet);
            }
          } else {
            System.out.println(uzenet);
            System.out.println(WordUtils.wrap(csapda.getHatastalanUzenet(), WRAP));
          }
        } else {
          System.out.println(uzenet);
        }
        if (jatekos.getVoltOdaat() && ujHelyszinNev.equals("Rejtett pince")) {
          jatekos.setVisszaJott(true);
        }
      } else if (parancs.isAktival()) {
        if (vilag.isVilagos()) {
          if (parancs.getTargy().equals("kötelet")
            && (!vilag.getAktualisHelyszin().getNev().equals("Padlás vége")
            || vilag.getAjto("ládát").getAllapot().equals("zárva"))) {
            System.out.println(vilag.getUzenet(20)); // nincs értelme használni
            continue;
          } else if (parancs.getTargy().equals("gépet")) {
            if (!parancs.getReszes().equals("papírral")) {
              System.out.println(vilag.getUzenet(20)); // nincs értelme használni
            } else {
              System.out.println(WordUtils.wrap(vilag.kinyit("portált", "papírral"), WRAP));
            }
          }
          System.out.println(vilag.aktival(parancs.getTargy(), true));
          if (vilag.getTargy("kart").isAktiv() && vilag.getCsapda("penge").isAktiv()) {
            vilag.getCsapda("penge").setAktiv(false);
            System.out.println(WordUtils.wrap(vilag.getCsapda("penge").getFelfedezesUzenet(), WRAP));
          } else if (vilag.getTargy("kötelet").isAktiv() && vilag.getCsapda("kürtő").isAktiv()) {
            vilag.getCsapda("kürtő").setAktiv(false);
            System.out.println(WordUtils.wrap(vilag.getCsapda("kürtő").getFelfedezesUzenet(), WRAP));
          }
        }
      } else if (parancs.isDeaktival()) {
        if (vilag.isVilagos()) {
          System.out.println(vilag.aktival(parancs.getTargy(), false));
        }
      } else if (parancs.isLeltar()) {
        if (vilag.isVilagos()) {
          System.out.println(WordUtils.wrap(vilag.getLeltar(), WRAP));
        }
      } else if (parancs.isKinyit()) {
        System.out.println(vilag.kinyit(parancs.getTargy(), parancs.getReszes()));
      } else if (parancs.isFelvesz()) {
        if (vilag.isVilagos()) {
          System.out.println(vilag.felvesz(parancs.getTargy()));
          if (vilag.getLeltar().contains("lábtörlő")) {
            vilag.getTargy("kulcsot").setLathato(true);
          }
        }
      } else if (parancs.isLetesz()) {
        System.out.println(vilag.letesz(parancs.getTargy()));
      } else if (parancs.isVizsgal()) {
        if (vilag.isVilagos()) {
          if (vilag.checkHelyzet("Előtér", parancs.getTargy(), "padlót")) {
            System.out.println(WordUtils.wrap(csapda.getFelfedezesUzenet(), WRAP));
            csapda.setAktiv(false);
            continue;
          } else if (vilag.checkHelyzet("Szoba", parancs.getTargy(), "kandallót")) {
            vilag.getTargy("piszkavasat").setLathato(true);
          } else if (vilag.checkHelyzet("Konyha", parancs.getTargy(), "szekrényt")) {
            vilag.getTargy("jegyzetet").setLathato(true);
          } else if (vilag.checkHelyzet("Padlás vége", parancs.getTargy(), "papírt")) {
            System.out.println(WordUtils.wrap(vilag.getUzenet(18), WRAP));
            continue;
          } else if (vilag.checkHelyzet("Rejtett pince", parancs.getTargy(), "papírt")) {
            System.out.println(WordUtils.wrap(vilag.getUzenet(19), WRAP));
            continue;
          } else if (parancs.isAltalanos()) {
            System.out.println(WordUtils.wrap(vilag.getUzenet(17), WRAP)); // semmi különös
            continue;
          }
          System.out.println(WordUtils.wrap(vilag.vizsgal(parancs.getTargy()), WRAP));
        }
      } else if (parancs.isTamad()) {
        if (ellenseg == null) {
          System.out.println(vilag.getUzenet(26)); // nincs ellenség
        } else if (!parancs.getTargy().equals(ellenseg.getNev())) {
          System.out.println(vilag.getUzenet(26)); // nincs ellenség
        } else if (!parancs.getReszes().equals(ellenseg.getFegyver())) {
          System.out.println(vilag.getUzenet(27)); // hatástalan kísérlet
        } else if (!vilag.keznelVan(vilag.getReszes(parancs.getReszes()))) {
          System.out.println(vilag.getUzenet(15)); // nincs nála az adott fegyver
        } else {
          System.out.println(WordUtils.wrap(ellenseg.getElpusztultUzenet(), WRAP));
          ellenseg.setAktiv(false);
        }
      } else if (parancs.isNemKell()) {
        System.out.println(vilag.getUzenet(28)); // nincs szükség erre
      } else if (parancs.isNormal()) {
        System.out.println(vilag.setLeiroMod(0)); // rendben üzenet
      } else if (parancs.isHosszu()) {
        System.out.println(vilag.setLeiroMod(1));
      } else if (parancs.isRovid()) {
        System.out.println(vilag.setLeiroMod(2));
      } else {
        System.out.println(vilag.getUzenet(6)); // nem érti az értelmező
      }
      if (vilag.getAktualisHelyszin().getNev().equals("Odaát")) {
        jatekos.csokkentOdaat();
      }
    }

    if (jatekos.getOttRagadt()) {
      System.out.println(vilag.getUzenet(21)); // ottragadtál a túloldalon
    } else if (jatekos.getVisszaJott()) {
      System.out.println(vilag.getUzenet(22)); // megnyerted a játékot
    } else {
      System.out.println(vilag.getUzenet(16)); // meghaltál
    }

  }

}
