package pkg43_kaland;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.commons.lang3.text.WordUtils;

/**
 * GUI megvalósítása
 *
 * @author rolika
 */
public class Jatekter extends javax.swing.JFrame {

  private final static int WRAP = 50;

  private Parancs parancs;
  private Jatekos jatekos;
  private Vilag vilag;
  private StringBuilder jatekSzoveg;

  /**
   * Creates new form Jatekter
   */
  public Jatekter() {
    initComponents();
    ujJatek();
  }

  private void ujJatek() {
    parancs = new Parancs();
    jatekos = new Jatekos();
    vilag = getVilag();
    taJatek.setText("");
    jatekSzoveg = new StringBuilder();
    tfParancs.setText("");
    tfParancs.requestFocusInWindow();
    helyzet();
  }

  private Vilag getVilag() {
    try (Connection kon = DriverManager.getConnection("jdbc:sqlite:kaland.sql")) {
      SqliteJDBC sql = new SqliteJDBC(kon);
      return new Vilag(sql.minden("helyszin"), sql.minden("kijarat"), sql.minden("uzenet"),
        sql.minden("targy"), sql.minden("ajto"), sql.minden("csapda"), sql.minden("ellenseg"));
    } catch (SQLException ex) {
      System.out.println("SQL hiba\n" + ex.getMessage());
      return null;
    }
  }

  private void helyzet() {
    if (vilag.getLeiroMod() == 1) {
      vilag.getAktualisHelyszin().setBejart(false);
    } else if (vilag.getLeiroMod() == 2) {
      vilag.getAktualisHelyszin().setBejart(true);
    }
    Ellenseg ellenseg = vilag.getEllenseg();
    if (vilag.isVilagos()) {
      jatekSzoveg.append(vilag.getAktualisHelyszin().getLeiras());
      jatekSzoveg.append('\n');
      jatekSzoveg.append(vilag.getLathatoTargyak());
      jatekSzoveg.append('\n');
      if (ellenseg != null && !ellenseg.isAktiv()) {
        jatekSzoveg.append(vilag.getUzenet(24)); // döglött pók
        jatekSzoveg.append('\n');
      }
      vilag.getAktualisHelyszin().setBejart(true);
      if (ellenseg != null && ellenseg.isAktiv()) {
        jatekSzoveg.append(ellenseg.getLeiras());
        jatekSzoveg.append('\n');
        jatekos.csokkentPok();
        if (jatekos.tamadPok()) {
          jatekSzoveg.append(ellenseg.getHalalUzenet());
          jatekSzoveg.append('\n');
          jatekos.setEletbenVan(false);
        }
      }
    } else {
        jatekSzoveg.append(vilag.getUzenet(5)); // sötét van
        jatekSzoveg.append('\n');
      if (ellenseg != null && ellenseg.isAktiv()) {
        jatekSzoveg.append(vilag.getUzenet(25)); // sötétben támad az ellen
        jatekSzoveg.append('\n');
        jatekos.setEletbenVan(false);
      }
    }
    taJatek.setText(WordUtils.wrap(jatekSzoveg.toString(), WRAP));
  }

  private void ertelmezo() {
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
      //parancs.szetszed(bevitel.nextLine());
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
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT
   * modify this code. The content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jScrollPane1 = new javax.swing.JScrollPane();
    taJatek = new javax.swing.JTextArea();
    tfParancs = new javax.swing.JTextField();
    btEszak = new javax.swing.JButton();
    btIndirekt = new javax.swing.JButton();
    btDel = new javax.swing.JButton();
    btKelet = new javax.swing.JButton();
    btNyugat = new javax.swing.JButton();
    btFel = new javax.swing.JButton();
    btLe = new javax.swing.JButton();
    btLeltar = new javax.swing.JButton();
    jMenuBar1 = new javax.swing.JMenuBar();
    menuJatek = new javax.swing.JMenu();
    menuItemUjJatek = new javax.swing.JMenuItem();
    jMenu2 = new javax.swing.JMenu();
    rbNormal = new javax.swing.JRadioButtonMenuItem();
    rbHosszu = new javax.swing.JRadioButtonMenuItem();
    rbRovid = new javax.swing.JRadioButtonMenuItem();
    jSeparator1 = new javax.swing.JPopupMenu.Separator();
    jMenuItem5 = new javax.swing.JMenuItem();
    jMenuItem3 = new javax.swing.JMenuItem();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    taJatek.setEditable(false);
    taJatek.setColumns(20);
    taJatek.setRows(5);
    taJatek.setText("játékablak");
    taJatek.setFocusable(false);
    taJatek.setRequestFocusEnabled(false);
    jScrollPane1.setViewportView(taJatek);

    tfParancs.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyPressed(java.awt.event.KeyEvent evt) {
        tfParancsKeyPressed(evt);
      }
    });

    btEszak.setText("észak");
    btEszak.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btEszakActionPerformed(evt);
      }
    });

    btIndirekt.setText("be / ki");
    btIndirekt.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btIndirektActionPerformed(evt);
      }
    });

    btDel.setText("dél");
    btDel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btDelActionPerformed(evt);
      }
    });

    btKelet.setText("kelet");
    btKelet.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btKeletActionPerformed(evt);
      }
    });

    btNyugat.setText("nyugat");
    btNyugat.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btNyugatActionPerformed(evt);
      }
    });

    btFel.setText("fel");
    btFel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btFelActionPerformed(evt);
      }
    });

    btLe.setText("le");
    btLe.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btLeActionPerformed(evt);
      }
    });

    btLeltar.setText("leltár");
    btLeltar.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btLeltarActionPerformed(evt);
      }
    });

    menuJatek.setText("Játék");

    menuItemUjJatek.setText("Új játék");
    menuItemUjJatek.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        menuItemUjJatekActionPerformed(evt);
      }
    });
    menuJatek.add(menuItemUjJatek);

    jMenuBar1.add(menuJatek);

    jMenu2.setText("Info");

    rbNormal.setSelected(true);
    rbNormal.setText("Normál leírás");
    jMenu2.add(rbNormal);

    rbHosszu.setText("Hosszú leírás");
    jMenu2.add(rbHosszu);

    rbRovid.setText("Rövid leírás");
    jMenu2.add(rbRovid);
    jMenu2.add(jSeparator1);

    jMenuItem5.setText("Segítség");
    jMenu2.add(jMenuItem5);

    jMenuItem3.setText("Névjegy");
    jMenu2.add(jMenuItem3);

    jMenuBar1.add(jMenu2);

    setJMenuBar(jMenuBar1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane1)
          .addComponent(tfParancs)
          .addGroup(layout.createSequentialGroup()
            .addComponent(btNyugat, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(btDel, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btLe, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(btEszak, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                  .addGroup(layout.createSequentialGroup()
                    .addComponent(btIndirekt, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(btKelet, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                  .addComponent(btFel, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                  .addComponent(btLeltar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(18, 18, 18)
        .addComponent(tfParancs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btEszak)
          .addComponent(btFel))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btIndirekt)
          .addComponent(btKelet)
          .addComponent(btNyugat)
          .addComponent(btLeltar))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(btDel)
          .addComponent(btLe))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void btNyugatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNyugatActionPerformed
    tfParancs.setText("nyugat");
    tfParancs.requestFocusInWindow();

  }//GEN-LAST:event_btNyugatActionPerformed

  private void btEszakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEszakActionPerformed
    tfParancs.setText("észak");
    tfParancs.requestFocusInWindow();
  }//GEN-LAST:event_btEszakActionPerformed

  private void btIndirektActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btIndirektActionPerformed
    tfParancs.setText("be");
    tfParancs.requestFocusInWindow();
  }//GEN-LAST:event_btIndirektActionPerformed

  private void btDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDelActionPerformed
    tfParancs.setText("dél");
    tfParancs.requestFocusInWindow();
  }//GEN-LAST:event_btDelActionPerformed

  private void btKeletActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btKeletActionPerformed
    tfParancs.setText("kelet");
    tfParancs.requestFocusInWindow();
  }//GEN-LAST:event_btKeletActionPerformed

  private void btFelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFelActionPerformed
    tfParancs.setText("fel");
    tfParancs.requestFocusInWindow();
  }//GEN-LAST:event_btFelActionPerformed

  private void btLeltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLeltarActionPerformed
    tfParancs.setText("leltár");
    tfParancs.requestFocusInWindow();
  }//GEN-LAST:event_btLeltarActionPerformed

  private void btLeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLeActionPerformed
    tfParancs.setText("le");
    tfParancs.requestFocusInWindow();
  }//GEN-LAST:event_btLeActionPerformed

  private void tfParancsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfParancsKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
      taJatek.setText(tfParancs.getText());
    }
  }//GEN-LAST:event_tfParancsKeyPressed

  private void menuItemUjJatekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemUjJatekActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_menuItemUjJatekActionPerformed

  private void vezerlo(String utasitas) {

  }

  /**
   */
  public static void main() {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(Jatekter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(Jatekter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(Jatekter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(Jatekter.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new Jatekter().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton btDel;
  private javax.swing.JButton btEszak;
  private javax.swing.JButton btFel;
  private javax.swing.JButton btIndirekt;
  private javax.swing.JButton btKelet;
  private javax.swing.JButton btLe;
  private javax.swing.JButton btLeltar;
  private javax.swing.JButton btNyugat;
  private javax.swing.JMenu jMenu2;
  private javax.swing.JMenuBar jMenuBar1;
  private javax.swing.JMenuItem jMenuItem3;
  private javax.swing.JMenuItem jMenuItem5;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JPopupMenu.Separator jSeparator1;
  private javax.swing.JMenuItem menuItemUjJatek;
  private javax.swing.JMenu menuJatek;
  private javax.swing.JRadioButtonMenuItem rbHosszu;
  private javax.swing.JRadioButtonMenuItem rbNormal;
  private javax.swing.JRadioButtonMenuItem rbRovid;
  private javax.swing.JTextArea taJatek;
  private javax.swing.JTextField tfParancs;
  // End of variables declaration//GEN-END:variables
}
