/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Code;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
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
    public SecretKey secretKey;
    public RC4Encryption() {

    }

    public void init() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("RC4");
            keyGenerator.init(512);
            secretKey = keyGenerator.generateKey();
            byte[] key = secretKey.getEncoded();
            infomationUser = new InfomationUser(key);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
    }

    public Cipher maHoa(String Key) {
        try {
            byte[] keyByte = Base64.getDecoder().decode(Key);
            Cipher cipher = Cipher.getInstance("RC4");
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyByte, "RC4");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return cipher;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Cipher giaima(String Key) {
        try {
            InfomationUser inf = new InfomationUser();
            System.out.println("GiaiMa:" + Key);
            byte[] keyByte = Base64.getDecoder().decode(Key);
            Cipher cipher = Cipher.getInstance("RC4");
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyByte, "RC4");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return cipher;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String encryptKeyRSA(String PublicKey) {
        try {
            InfomationUser user = new InfomationUser();
            byte[] byteKey = Base64.getDecoder().decode(PublicKey);
            X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey pk = kf.generatePublic(X509publicKey);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            byte[] RC4Key = Base64.getDecoder().decode(InfomationUser.getRc4Key());
            byte[] cipherText = cipher.doFinal(RC4Key);
            String res = Base64.getEncoder().encodeToString(cipherText);
            
            return res;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String decryptKeyRSA(String Data) {
        try {
            System.out.println(Data);
            InfomationUser user = new InfomationUser();
            KeyFactory kf = KeyFactory.getInstance("RSA");
            byte[] byteKey = Base64.getDecoder().decode(infomationUser.getPrivateKey());
            PKCS8EncodedKeySpec PKCS8privateKey = new PKCS8EncodedKeySpec(byteKey);
            PrivateKey privateKey = kf.generatePrivate(PKCS8privateKey);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[]keyData = Base64.getDecoder().decode(Data);
            byte[] cipherText = cipher.doFinal(keyData);
            String res =  Base64.getEncoder().encodeToString(cipherText);
            return res;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
