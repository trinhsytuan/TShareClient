/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Code;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class GetIPAdd {
    public String IPGet() {
        try {
            String IP = InetAddress.getLocalHost().toString();
            String []IPArr = IP.split("/");
            return IPArr[IPArr.length-1];
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
}
