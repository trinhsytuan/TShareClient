/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Code;

import com.mysql.cj.callback.UsernameCallback;

/**
 *
 * @author Hp
 */
public class InfomationUser {
    public static String Username;
    public static String publicKey;
    public static String privateKey;


    public InfomationUser() {
    }
    public InfomationUser(String us) {
        Username = us;
    }
    public InfomationUser(String publicKey,String privateKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
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

   
    
}
