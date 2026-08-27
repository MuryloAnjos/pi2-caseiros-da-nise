package Controller;

import View.Menu_GUI;
import javax.swing.JOptionPane;

/**
 *
 * @author caio.gpacheco
 */
public class Config_DAO {
    public static void verificarLogin(String usuario, String senha) {
        usuario = usuario.toLowerCase();
        senha = senha.toLowerCase();

        if (usuario.equals("admin") && senha.equals("1234")) {
            JOptionPane.showMessageDialog(null, "Bem-vindo ao sistema");
            new Menu_GUI().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Usuário ou senha incorretos.");
        }
    }
}