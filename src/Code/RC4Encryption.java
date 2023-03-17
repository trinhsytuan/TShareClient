/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Code;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Admin
 */
public class RC4Encryption {

    InfomationUser infomationUser;

    public RC4Encryption() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("RC4");
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] key = secretKey.getEncoded();
            infomationUser = new InfomationUser(key);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }

    }

    public byte[] maHoa(String key, byte[] data) {
        try {
            byte[] keyByte = Base64.getDecoder().decode(key);
            Cipher cipher = Cipher.getInstance("RC4");
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyByte, "RC4");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
