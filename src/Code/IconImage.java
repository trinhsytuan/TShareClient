/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Code;

import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author Hp
 */
public class IconImage {

    public Icon WrongPassword() {
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/password.png"));
        Icon icon = new ImageIcon(image);
        return icon;
    }

    public Icon Happy() {
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/smiley.png"));
        Icon icon = new ImageIcon(image);
        return icon;
    }

    public Icon Error() {
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/warning.png"));
        Icon icon = new ImageIcon(image);
        return icon;
    }

    public Icon Welcome() {
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Welcome.png"));
        Icon icon = new ImageIcon(image);
        return icon;
    }

    public Icon OffineIcon() {
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/no-signal.png"));
        Icon icon = new ImageIcon(image);
        return icon;
    }

    public Icon FileLarge() {
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/FileLarge.png"));
        Icon icon = new ImageIcon(image);
        return icon;
    }

    public Icon IconFileWaiting() {
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/sentFileUser.png"));
        Icon icon = new ImageIcon(image);
        return icon;
    }

    public Icon Accept() {
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/accept.png"));
        Icon icon = new ImageIcon(image);
        return icon;
    }

    public Icon Cancel() {
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/cancel.png"));
        Icon icon = new ImageIcon(image);
        return icon;
    }

    public Icon Lock() {
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/lock.png"));
        Icon icon = new ImageIcon(image);
        return icon;
    }
}
