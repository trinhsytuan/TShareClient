package Code;

import java.io.*;
import java.net.*;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;

public class Upload implements Runnable {

    public String addr;
    public int port;
    public Socket socket;
    public FileInputStream In;
    public OutputStream Out;
    public File file;
    public Cipher cipher;
    public PublicKey pk;
    public RC4Encryption rc4 = new RC4Encryption();
    public Upload(String addr, int port, File filepath, String publicKey) {
        super();
        try {
            file = filepath;
            System.out.println(addr + " " + port);
            socket = new Socket(InetAddress.getByName(addr), port);
            Out = socket.getOutputStream();
            In = new FileInputStream(filepath);
            cipher = rc4.maHoa(InfomationUser.getRc4Key());
        } catch (Exception ex) {
            System.out.println("Exception [Upload : Upload(...)]");
        }
    }

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[64];
            int count;
            while ((count = In.read(buffer)) != -1) {
                byte[] output = cipher.doFinal(buffer, 0, count);
                Out.write(output, 0, output.length);
            }
            Out.flush();
            if (In != null) {
                In.close();
            }
            if (Out != null) {
                Out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (Exception ex) {
            System.out.println("Exception [Upload : run()]");
            ex.printStackTrace();
        }
    }

}
