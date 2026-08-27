package View;

import Controller.Config_DAO;

/**
 *
 * @author caio.gpacheco
 */
public class Login_GUI extends javax.swing.JFrame {
    public Login_GUI() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        log_txt = new javax.swing.JTextField();
        sen_txt = new javax.swing.JPasswordField();
        btnLoginActionPerformed = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(235, 222, 203));
        jPanel1.setLayout(null);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Image/login.png"))); // NOI18N
        jPanel1.add(jLabel1);
        jLabel1.setBounds(730, 80, 360, 200);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 2, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Senha:");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(720, 360, 120, 60);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 2, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Usuário:");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(710, 290, 120, 60);

        log_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                log_txtActionPerformed(evt);
            }
        });
        jPanel1.add(log_txt);
        log_txt.setBounds(820, 310, 200, 22);
        jPanel1.add(sen_txt);
        sen_txt.setBounds(820, 380, 200, 22);

        btnLoginActionPerformed.setText("Entrar");
        btnLoginActionPerformed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformedActionPerformed(evt);
            }
        });
        jPanel1.add(btnLoginActionPerformed);
        btnLoginActionPerformed.setBounds(860, 460, 150, 60);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        setBounds(0, 0, 1405, 709);
    }// </editor-fold>//GEN-END:initComponents

    private void log_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_log_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_log_txtActionPerformed

    private void btnLoginActionPerformedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformedActionPerformed
    String usuario = log_txt.getText();
    String senha = sen_txt.getText();
    Config_DAO.verificarLogin(usuario, senha);
    }//GEN-LAST:event_btnLoginActionPerformedActionPerformed

    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(Login_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login_GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLoginActionPerformed;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    public javax.swing.JTextField log_txt;
    public javax.swing.JPasswordField sen_txt;
    // End of variables declaration//GEN-END:variables
}
