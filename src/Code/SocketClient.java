/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Code;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class SocketClient implements Runnable{
    public int port = 2023;
    public String serverAddr;
    public SignUpFrame sg = new SignUpFrame();
    public Socket socket;
    public HomeUser ui;
    public ObjectInputStream In;
    public ObjectOutputStream Out;

    public SocketClient(HomeUser user) throws IOException {
        this.ui = user;
    }
    public void connect(String srv) throws IOException{
        this.serverAddr = srv;
        socket = new Socket(InetAddress.getByName(serverAddr), port);
        Out = new ObjectOutputStream(socket.getOutputStream());
        Out.flush();
        In = new ObjectInputStream(socket.getInputStream());
        
    }
    public void send(Message msg) {
        try {
            Out.writeObject(msg);
            Out.flush();
            System.out.println("Outgoing : " + msg.toString());

        } catch (IOException ex) {
            System.out.println("Exception SocketClient send()");
        }
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            try {
                Message msg = (Message) In.readObject();
                System.out.println(msg.content);
                
            } catch (Exception ex) {
                running = false;
                System.out.println("Server Connection Failer, maybe because the program has exited");
                ex.printStackTrace();
            }

        }

    }

    public void closeThread(Thread t) {
        t = null;
    }
}
