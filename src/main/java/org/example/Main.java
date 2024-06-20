package org.example;

import javax.swing.*;

import java.io.IOException;

import static org.example.GatosService.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws IOException {
        int opcion_menu=-1;
        String[] botones = {
                "1. Ver gatos",
                "2. Ver favoritos",
                "3. Salir"
        };
        do{
            String option = (String) JOptionPane.showInputDialog(null,
                "Gatitos Java", "Menú Principal", JOptionPane.INFORMATION_MESSAGE,
                null, botones, botones[0]);
            //validamos opcion que tomo el usuario
            for (int i=0; i<botones.length;i++){
                System.out.println("valor de i"+i);
                if (option.equals(botones[i])) {
                    opcion_menu=i;
                }
            }
            switch (opcion_menu){
                case 0:
                    GatosService.verGatitos();
                    break;
                case 1:
                    Gato gato = new Gato();
                    GatosService.verFavoritos(gato.getApikey());
                default:
                    break;
            }
        } while (opcion_menu!=2);

    }
}