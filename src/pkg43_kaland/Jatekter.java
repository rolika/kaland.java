package pkg43_kaland;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.OK_CANCEL_OPTION;

/**
 * GUI megvalósítása
 *
 * @author rolika
 */
public class Jatekter extends javax.swing.JFrame {

  private Parancs parancs;
  private Jatekos jatekos;
  private Vilag vilag;
  private StringBuilder jatekSzoveg;

  /**
   * Creates new form Jatekter
   */
  public Jatekter() {
    initComponents();
    setLocationRelativeTo(null);
    ujJatek();
  }

  private void ujJatek() {
    parancs = new Parancs();
    jatekos = new Jatekos();
    vilag = getVilag();
    taJatek.setText("");
    jatekSzoveg = new StringBuilder();
    tfParancs.setEnabled(true);
    btEszak.setEnabled(true);
    btDel.setEnabled(true);
    btNyugat.setEnabled(true);
    btKelet.setEnabled(true);
    btIndirekt.setEnabled(true);
    btFel.setEnabled(true);
    btLe.setEnabled(true);
    btLeltar.setEnabled(true);
    tfParancs.setText("");
    tfParancs.requestFocusInWindow();
    rbNormal.setSelected(true);
    helyzet();
  }

  // beolvasom a táblákat az adatbázisból
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
  
  private void vege() {
    if (jatekos.getOttRagadt()) {
      fuz(vilag.getUzenet(21)); // ottragadtál a túloldalon
    } else if (jatekos.getVisszaJott()) {
      fuz(vilag.getUzenet(22)); // megnyerted a játékot
    } else {
      fuz(vilag.getUzenet(16)); // meghaltál
    }
    taJatek.setText(jatekSzoveg.toString());
    // ne lehessen semmit csinálni
    tfParancs.setEnabled(false);
    btEszak.setEnabled(false);
    btDel.setEnabled(false);
    btNyugat.setEnabled(false);
    btKelet.setEnabled(false);
    btIndirekt.setEnabled(false);
    btFel.setEnabled(false);
    btLe.setEnabled(false);
    btLeltar.setEnabled(false);
  }

  private void helyzet() {
    
    if (vilag.getLeiroMod() == 1) {
      vilag.getAktualisHelyszin().setBejart(false);
    } else if (vilag.getLeiroMod() == 2) {
      vilag.getAktualisHelyszin().setBejart(true);
    }
    Ellenseg ellenseg = vilag.getEllenseg();
    
    if (vilag.isVilagos()) {
      fuz(vilag.getAktualisHelyszin().getLeiras());
      fuz(vilag.getLathatoTargyak());
      if (ellenseg != null && !ellenseg.isAktiv()) {
        fuz(vilag.getUzenet(24)); // döglött pók
      }
      vilag.getAktualisHelyszin().setBejart(true);
      if (ellenseg != null && ellenseg.isAktiv()) {
        fuz(ellenseg.getLeiras());
        jatekos.csokkentPok();
        if (jatekos.tamadPok()) {
          fuz(ellenseg.getHalalUzenet());
          jatekos.setEletbenVan(false);
        }
      }
      
    } else {
        fuz(vilag.getUzenet(5)); // sötét van
      if (ellenseg != null && ellenseg.isAktiv()) {
        fuz(vilag.getUzenet(25)); // sötétben támad az ellen
        jatekos.setEletbenVan(false);
        vege();
      }
    }
    
    taJatek.setText(jatekSzoveg.toString());
  }

  private void reakcio() {
    //Ellenseg ellenseg = vilag.getEllenseg();
    Csapda csapda = vilag.getCsapda();
    
    // a játékos menne valamerre
    if (parancs.isIrany()) {
      irany(csapda);
      
    // a játékos aktiválna (használna, mozgatna, bekapcsolna) valamit
    } else if (parancs.isAktival()) {
      if (vilag.isVilagos()) {
        aktival();
      }
      
    // a játékos deaktiválna (lekapcsolna) valamit
    } else if (parancs.isDeaktival()) {
      if (vilag.isVilagos()) {
        fuz(vilag.aktival(parancs.getTargy(), false));
      }
      
    // a játékos megnézi a leltárt
    } else if (parancs.isLeltar()) {
      if (vilag.isVilagos()) {
        fuz(vilag.getLeltar());
      }
      
    // a játékos kinyitna valamit
    } else if (parancs.isKinyit()) {
      fuz(vilag.kinyit(parancs.getTargy(), parancs.getReszes()));
      
    // a játékos felvenne valamit
    } else if (parancs.isFelvesz()) {
      if (vilag.isVilagos()) {
        fuz(vilag.felvesz(parancs.getTargy()));
        if (vilag.getLeltar().contains("lábtörlő")) {
          vilag.getTargy("kulcsot").setLathato(true);
        }
      }
    
    // a játékos letenne valamit
    } else if (parancs.isLetesz()) {
      fuz(vilag.letesz(parancs.getTargy()));
      
    // a játékos megvizsgálna (elolvasna) valamit
    } else if (parancs.isVizsgal()) {
      if (vilag.isVilagos()) {
        if (vilag.checkHelyzet("Előtér", parancs.getTargy(), "padlót")) {
          fuz(csapda.getFelfedezesUzenet());
          csapda.setAktiv(false);
          return;
        } else if (vilag.checkHelyzet("Szoba", parancs.getTargy(), "kandallót")) {
          vilag.getTargy("piszkavasat").setLathato(true);
        } else if (vilag.checkHelyzet("Konyha", parancs.getTargy(), "szekrényt")) {
          vilag.getTargy("jegyzetet").setLathato(true);
        } else if (vilag.checkHelyzet("Padlás vége", parancs.getTargy(), "papírt")) {
          fuz(vilag.getUzenet(18)); // láda és papír összefügg
          return;
        } else if (vilag.checkHelyzet("Rejtett pince", parancs.getTargy(), "papírt")) {
          fuz(vilag.getUzenet(19)); // gép és papír összefügg
          return;
        } else if (parancs.isAltalanos()) {
          fuz(vilag.getUzenet(17)); // semmi különös
          return;
        }
        fuz(vilag.vizsgal(parancs.getTargy()));
      }
      
    // a játékos megtámadna valamit
    } else if (parancs.isTamad()) {
        tamad();
      
    // értelmes, de ebben a játékban nem kell
    } else if (parancs.isNemKell()) {
      fuz(vilag.getUzenet(28)); // nincs szükség erre
      
    // a játékos a leíró-módot változtatja
    } else if (parancs.isNormal()) {
      fuz(vilag.setLeiroMod(0)); // rendben üzenet      
    } else if (parancs.isHosszu()) {
      fuz(vilag.setLeiroMod(1)); // rendben üzenet      
    } else if (parancs.isRovid()) {
      fuz(vilag.setLeiroMod(2)); // rendben üzenet
    
    // a játékos információt (hosszú leírást) kér
    } else if (parancs.isInfo()) {
      if (vilag.isVilagos() && vilag.getLeiroMod() != 1) {
        fuz(vilag.getAktualisHelyszin().getHosszu());
      }
      
    // nem értelmezett parancs
    } else {
      fuz(vilag.getUzenet(6)); // nem érti az értelmező
    }
    
    // ha a játékos odaát van, elkezd becsukódni a portál
    if (vilag.getAktualisHelyszin().getNev().equals("Odaát")) {
      jatekos.csokkentOdaat();
      fuz(vilag.getUzenet(28+jatekos.getOdaatvan()));
    }
    
  }
  
  /**
   * Kiszervezett szöveg-összefűzés, hozzáadott újsorral
   * @param szovegresz 
   */
  private void fuz(String szovegresz) {
    jatekSzoveg.append(szovegresz);
    jatekSzoveg.append('\n');
  }
  
  /**
   * Kiszervezett mozgás
   * @param csapda 
   */
  private void irany(Csapda csapda) {
    String uzenet = vilag.ujHelyszin(parancs.getIrany());
      String ujHelyszinNev = vilag.getAktualisHelyszin().getNev();
      if (csapda != null) {
        if (csapda.isAktiv()) {
          if (ujHelyszinNev.equals(csapda.getCel())) {
            fuz(csapda.getHalalUzenet());
            jatekos.setEletbenVan(false);
          } else {
            fuz(uzenet);
          }
        } else {
          fuz(uzenet);
          fuz(csapda.getHatastalanUzenet());
        }
      } else {
        fuz(uzenet);
      }
      if (jatekos.getVoltOdaat() && ujHelyszinNev.equals("Rejtett pince")) {
        jatekos.setVisszaJott(true);
      }
  }
  
  private void aktival() {
    if (parancs.getTargy().equals("kötelet")
      && (!vilag.getAktualisHelyszin().getNev().equals("Padlás vége")
      || vilag.getAjto("ládát").getAllapot().equals("zárva"))) {
      fuz(vilag.getUzenet(20)); // nincs értelme használni
      return;
    } else if (parancs.getTargy().equals("gépet")) {
      if (!parancs.getReszes().equals("papírral")) {
        fuz(vilag.getUzenet(20)); // nincs értelme használni
      } else {
        fuz(vilag.kinyit("portált", "papírral"));
      }
    }
    fuz(vilag.aktival(parancs.getTargy(), true));
    if (vilag.getTargy("kart").isAktiv() && vilag.getCsapda("penge").isAktiv()) {
      vilag.getCsapda("penge").setAktiv(false);
      fuz(vilag.getCsapda("penge").getFelfedezesUzenet());
    } else if (vilag.getTargy("kötelet").isAktiv() && vilag.getCsapda("kürtő").isAktiv()) {
      vilag.getCsapda("kürtő").setAktiv(false);
      fuz(vilag.getCsapda("kürtő").getFelfedezesUzenet());
    }
  }
  
  private void tamad() {
    Ellenseg ellenseg = vilag.getEllenseg();
    if (ellenseg == null) {
      fuz(vilag.getUzenet(26)); // nincs ellenség
    } else if (!parancs.getTargy().equals(ellenseg.getNev())) {
      fuz(vilag.getUzenet(26)); // nincs ellenség
    } else if (!parancs.getReszes().equals(ellenseg.getFegyver())) {
      fuz(vilag.getUzenet(27)); // hatástalan kísérlet
      fuz(ellenseg.getHalalUzenet());
      jatekos.setEletbenVan(false);
    } else if (!vilag.keznelVan(vilag.getReszes(parancs.getReszes()))) {
      fuz(vilag.getUzenet(15)); // nincs nála az adott fegyver
      fuz(ellenseg.getHalalUzenet());
      jatekos.setEletbenVan(false);
    } else {
      fuz(ellenseg.getElpusztultUzenet());
      ellenseg.setAktiv(false);
    }
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT
   * modify this code. The content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    bgLeiras = new javax.swing.ButtonGroup();
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
    setTitle("Kalandjáték");

    taJatek.setEditable(false);
    taJatek.setColumns(20);
    taJatek.setLineWrap(true);
    taJatek.setRows(5);
    taJatek.setText("játékablak");
    taJatek.setWrapStyleWord(true);
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

    jMenu2.setText("Infó");

    bgLeiras.add(rbNormal);
    rbNormal.setSelected(true);
    rbNormal.setText("Normál leírás");
    rbNormal.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        rbNormalActionPerformed(evt);
      }
    });
    jMenu2.add(rbNormal);

    bgLeiras.add(rbHosszu);
    rbHosszu.setText("Hosszú leírás");
    rbHosszu.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        rbHosszuActionPerformed(evt);
      }
    });
    jMenu2.add(rbHosszu);

    bgLeiras.add(rbRovid);
    rbRovid.setText("Rövid leírás");
    rbRovid.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        rbRovidActionPerformed(evt);
      }
    });
    jMenu2.add(rbRovid);
    jMenu2.add(jSeparator1);

    jMenuItem5.setText("Segítség");
    jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jMenuItem5ActionPerformed(evt);
      }
    });
    jMenu2.add(jMenuItem5);

    jMenuItem3.setText("Névjegy");
    jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jMenuItem3ActionPerformed(evt);
      }
    });
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
        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
    performUtasitas("nyugat");

  }//GEN-LAST:event_btNyugatActionPerformed

  private void btEszakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEszakActionPerformed
    performUtasitas("észak");
  }//GEN-LAST:event_btEszakActionPerformed

  private void btIndirektActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btIndirektActionPerformed
    performUtasitas("be");
  }//GEN-LAST:event_btIndirektActionPerformed

  private void btDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDelActionPerformed
    performUtasitas("dél");
  }//GEN-LAST:event_btDelActionPerformed

  private void btKeletActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btKeletActionPerformed
    performUtasitas("kelet");
  }//GEN-LAST:event_btKeletActionPerformed

  private void btFelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFelActionPerformed
    performUtasitas("fel");
  }//GEN-LAST:event_btFelActionPerformed

  private void btLeltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLeltarActionPerformed
    performUtasitas("leltár");
  }//GEN-LAST:event_btLeltarActionPerformed

  private void btLeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLeActionPerformed
    performUtasitas("le");
  }//GEN-LAST:event_btLeActionPerformed

  private void tfParancsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfParancsKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
      jatekSzoveg.append(tfParancs.getText().toUpperCase());
      jatekSzoveg.append('\n');
      taJatek.setText(jatekSzoveg.toString());
      parancs.szetszed(tfParancs.getText());
      tfParancs.setText("");
      reakcio();
      if (jatekos.getEletbenVan() && !jatekos.getOttRagadt() && !jatekos.getVisszaJott()) {
        helyzet();
      } else {
        vege();
      }
    }
  }//GEN-LAST:event_tfParancsKeyPressed

  private void menuItemUjJatekActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemUjJatekActionPerformed
    if (JOptionPane.showConfirmDialog(this, "Biztosan újraindítod?", "Új játék", OK_CANCEL_OPTION) == 0) {
      ujJatek();
    }
  }//GEN-LAST:event_menuItemUjJatekActionPerformed

  private void rbHosszuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbHosszuActionPerformed
    performUtasitas("hosszú");
  }//GEN-LAST:event_rbHosszuActionPerformed

  private void rbRovidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbRovidActionPerformed
    performUtasitas("rövid");
  }//GEN-LAST:event_rbRovidActionPerformed

  private void rbNormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNormalActionPerformed
    performUtasitas("normál");
  }//GEN-LAST:event_rbNormalActionPerformed

  private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
    String uzenet = "Szöveges kalandjáték\n"
      + "írta: Weisz Roland (2017)\n"
      + "Vizsgafeladat Pasztuhov Dániel\n"
      + "JAVA távoktatásához";
    JOptionPane.showMessageDialog(this, uzenet, "Kalandjáték", JOptionPane.INFORMATION_MESSAGE);
  }//GEN-LAST:event_jMenuItem3ActionPerformed

  private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
    String uzenet = "A játék két- vagy háromszavas parancsokkal működik,\n"
      + "melyeket egyes szám első személyben kell kiadni. Pl.\n"
      + "MEGÖLÖM A TROLLT A KARDDAL\n"
      + "Ha elakadtál volna, a két aranyszabály:\n"
      + "- Vizsgálj meg mindent!\n"
      + "- Vegyél fel minden mozdíthatót!\n"
      + "Használható utasítások:\n"
      + "É, D, K, NY, FEL, LE, BE, KI, LELTÁR\n"
      + "BEKAPCSOLOM, KIKAPCSOLOM, HASZNÁLOM, MOZGATOM, KINYITOM\n"
      + "FELVESZEM, LETESZEM, MEGNÉZEM, ELOLVASOM, MEGTÁMADOM\n"
      + "HOSSZÚ, RÖVID, NORMÁL, INFÓ\n"
      + "A szövegben lévő főnevek tárgy- és részes-eseteit érti meg.";
    JOptionPane.showMessageDialog(this, uzenet, "Kalandjáték", JOptionPane.INFORMATION_MESSAGE);
  }//GEN-LAST:event_jMenuItem5ActionPerformed

  private void performUtasitas(String utasitas) {
    tfParancs.setText(utasitas);
    tfParancsKeyPressed(new KeyEvent(tfParancs, KeyEvent.KEY_PRESSED, 1, 0, KeyEvent.VK_ENTER));
    tfParancs.requestFocusInWindow();
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
  private javax.swing.ButtonGroup bgLeiras;
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
