/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Code;

import java.io.IOException;
import javax.swing.UIManager;

/**
 *
 * @author Admin
 */
public class TShare {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.out.println("LAF ERROR");
        }
        HomeUser home = new HomeUser();
        SocketClient socket = new SocketClient(home);
        HomePage page = new HomePage(socket);
        page.setVisible(true);

    }

}
