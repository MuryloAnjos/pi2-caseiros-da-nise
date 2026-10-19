package View;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class Splash_GUI extends javax.swing.JFrame {

        
    public Splash_GUI() {
        initComponents();
        
        progress.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
            @Override
            protected java.awt.Color getSelectionForeground() {
                return java.awt.Color.WHITE;                       // texto em cima da parte marrom
            }
            @Override
            protected java.awt.Color getSelectionBackground() {
                return new java.awt.Color(138, 75, 42);            // texto em cima da parte vazia
            }
        });
        progress.setForeground(new java.awt.Color(138, 75, 42));  // cor da barra
        progress.setBackground(new java.awt.Color(240, 225, 195)); // fundo da barra
        progress.setBorderPainted(false);
        
        new Thread() {
            public void run() {

                for (int i = 0; i < 101; i++) {
                    try {
                        sleep(60);

                        progress.setValue(i);

                        if (progress.getValue() == 10) {

                            mess_txt.setText("Fazendo conexão com o banco de dados...");
                            sleep(2000);

                        } else if (progress.getValue() <= 30) {
                            mess_txt.setText("Carregando o sistema...");
                            sleep(100);
                        } else if (progress.getValue() <= 99) {
                            mess_txt.setText("Quase lá...");

                        } else {
                            mess_txt.setText("Carregamento completo... Seu sistema será iniciado...");

                            sleep(3000);

                            new Inicio_GUI().setVisible(true);
                            dispose();
                        }

                    } catch (InterruptedException ex) {
                        Logger.getLogger(Splash_GUI.class.getName()).log(Level.SEVERE, null, ex);

                    }

                }
            }

        }.start();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        mess_txt = new javax.swing.JLabel();
        progress = new javax.swing.JProgressBar();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setBackground(new java.awt.Color(235, 222, 203));
        jPanel1.setLayout(null);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/logo.jpg"))); // NOI18N
        jPanel1.add(jLabel1);
        jLabel1.setBounds(560, 190, 200, 190);
        jPanel1.add(mess_txt);
        mess_txt.setBounds(350, 510, 380, 70);

        progress.setForeground(new java.awt.Color(102, 255, 102));
        progress.setStringPainted(true);
        jPanel1.add(progress);
        progress.setBounds(540, 410, 250, 30);

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(138, 75, 42));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("BEM VINDO");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(600, 150, 120, 26);

        getContentPane().add(jPanel1);

        setSize(new java.awt.Dimension(1299, 660));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(Splash_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Splash_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Splash_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Splash_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Splash_GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel mess_txt;
    private javax.swing.JProgressBar progress;
    // End of variables declaration//GEN-END:variables
}
