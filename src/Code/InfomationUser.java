/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Code;

import java.util.Base64;

/**
 *
 * @author Hp
 */
public class InfomationUser {

    public static String Username;
    public static String publicKey;
    public static String privateKey;
    public static String rc4Key;
    public InfomationUser() {
    }

    public InfomationUser(String us) {
        Username = us;
    }

    public InfomationUser(String publicKey, String privateKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }
    public InfomationUser(byte[]key){
        rc4Key = Base64.getEncoder().encodeToString(key);
    }
    public static String getUsername() {
        return Username;
    }

    public static String getPublicKey() {
        return publicKey;
    }

    public static void setPublicKey(String publicKey) {
        InfomationUser.publicKey = publicKey;
    }

    public static String getPrivateKey() {
        return privateKey;
    }

    public static void setPrivateKey(String privateKey) {
        InfomationUser.privateKey = privateKey;
    }

    public static String getRc4Key() {
        return rc4Key;
    }

    public static void setRc4Key(String rc4Key) {
        InfomationUser.rc4Key = rc4Key;
    }

}
