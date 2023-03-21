package Code;

import java.io.*;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class Download implements Runnable {

    public ServerSocket server;
    public Socket socket;
    public int port;
    public String saveTo = "";
    public InputStream In;
    public FileOutputStream Out;
    InfomationUser info = new InfomationUser();
    public PrivateKey privateKey;
    public Cipher cipher;
    public String encryptKey;
    public RC4Encryption rc4 = new RC4Encryption();
    public Download(String saveTo, String key) {
        try {
            server = new ServerSocket(0);
            port = server.getLocalPort();
            this.saveTo = saveTo;
            this.encryptKey = rc4.decryptKeyRSA(key);
            
            cipher = rc4.giaima(encryptKey);
        } catch (IOException ex) {
            System.out.println("Exception [Download : Download(...)]");
        }
    }

    @Override
    public void run() {
        try {
            socket = server.accept();
            System.out.println("Download : " + socket.getRemoteSocketAddress());
            In = socket.getInputStream();
            Out = new FileOutputStream(saveTo);
            
            byte[] buffer = new byte[64];
            int count;
            while ((count = In.read(buffer)) >= 0) {
                byte[] output = cipher.doFinal(buffer, 0, count);
                Out.write(output, 0, output.length);
            }

            Out.flush();

            if (Out != null) {
                Out.close();
            }
            if (In != null) {
                In.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (Exception ex) {
            System.out.println("Exception [Download : run(...)]");
            ex.printStackTrace();
        }
    }
}
