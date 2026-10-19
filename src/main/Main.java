package main;

import View.Splash_GUI;
import java.awt.Color;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.put("nimbusOrange", new Color(138, 75, 42));   // cor da barra de carregamento
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Não foi possível aplicar o visual Nimbus.");
        }
        new Splash_GUI().setVisible(true);
    }
}