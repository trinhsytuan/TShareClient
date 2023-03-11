/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Code;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

/**
 *
 * @author Admin
 */
public class Database {

    Statement tv;
    MainServer srv;
    boolean ok = true;

    public Database(MainServer _srv, String host, String port, String Username, String passowrd) throws Exception {
        srv = _srv;
        Connection conn = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        String infoConnection = "jdbc:mysql://" + host + ":" + port + "/tShare";
        Connection con = DriverManager.getConnection(
                infoConnection, Username, passowrd);
        tv = con.createStatement();
        System.out.println("Connect OK");
        srv.jTextArea1.append("Connect DB Succerfully, Socket is Started port 2023\n");
        srv.jTextArea1.append("Server Started Addres:" + InetAddress.getLocalHost() + " or localhost address\n");
        srv.btnStart.setEnabled(false);

    }

    public boolean checkUsernameRegister(String Username) {
        try {
            ResultSet rs = tv.executeQuery("SELECT CheckRegister('" + Username + "') AS res");
            rs.next();
            if (rs.getInt("res") == 1) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            return false;
        }

    }

    public void addUser(String Username, String password) {
        try {
            tv.executeUpdate("CALL CreateUser('" + Username + "','" + password + "')");
        } catch (Exception ex) {
            System.out.println("Error addUser " + Username);
            ex.printStackTrace();
        }
    }

    public boolean login(String Username, String password, String publicKey) {
        try {
            ResultSet rs = tv.executeQuery("CALL Login('" + Username + "','" + password + "','" + publicKey + "')");
            rs.next();
            if (rs.wasNull()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception ex) {
            System.out.println("Error login " + Username);

        }
        return false;
    }

    public Vector<String> ListUser(String Username) {
        try {
            ResultSet rs = tv.executeQuery("CALL GetUser()");
            Vector<String> user = new Vector<String>();
            while (rs.next()) {
                if (!rs.getString("Username").equals(Username)) {
                    user.add(rs.getString("Username"));
                }

            }
            return user;

        } catch (Exception ex) {
            System.out.println("Error GetListUser " + Username);
            ex.printStackTrace();
            return null;
        }

    }
    public Vector<String> GetKeyUser(String Username) {
        try {
            ResultSet rs = tv.executeQuery("CALL GetKeyUser('"+Username+"')");
            Vector<String> user = new Vector<String>();
            rs.next();
            user.add(rs.getString("Username"));
            user.add(rs.getString("PublicKey"));
            return user;

        } catch (Exception ex) {
            System.out.println("Error GetListUser " + Username);
            ex.printStackTrace();
            return null;
        }

    }
}
