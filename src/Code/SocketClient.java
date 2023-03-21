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
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Admin
 */
public class SocketClient implements Runnable {

    public int port = 2023;
    public RC4Encryption rc4 = new RC4Encryption();
    public String serverAddr;
    public SendFile send;
    public GetIPAdd IpAdd = new GetIPAdd();
    public IconImage image = new IconImage();
    public SignUpFrame sg = new SignUpFrame();
    public Socket socket;
    public HomeUser ui;
    public HomePage page;
    public ObjectInputStream In;
    public ObjectOutputStream Out;

    public SocketClient(HomeUser user) throws IOException {
        this.ui = user;
    }

    public void updatehomePage(HomePage pageHome) {
        page = pageHome;
    }

    public void connect(String srv) throws IOException {
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
            //System.out.println("Outgoing : " + msg.toString());

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
                if (msg.type.equals("SIGNUP")) {
                    if (msg.content.equals("ACCEPT")) {
                        JOptionPane.showMessageDialog(null, "You have successfully registered", "Register User", JOptionPane.INFORMATION_MESSAGE, image.Welcome());
                    } else {
                        JOptionPane.showMessageDialog(null, "Username already exists in the system", "Register User", JOptionPane.ERROR_MESSAGE, image.Error());
                    }
                } else if (msg.type.equals("LOGIN")) {
                    if (msg.content.equals("ACCEPT")) {
                        ui.setVisible(true);
                        page.setVisible(false);
                        ui.setTitle();
                        ui.getListUser();
                    } else {
                        JOptionPane.showMessageDialog(null, "Login Failer, please check Username and Password!", "Login Failer", JOptionPane.ERROR_MESSAGE, image.WrongPassword());

                    }
                } else if (msg.type.equals("GetUser")) {
                    ui.UpdateUser(msg.rs);
                } else if (msg.type.equals("NEWUSER")) {
                    if (ui != null) {
                        ui.newUser(msg.content);
                    }
                } else if (msg.type.equals("SendMem")) {
                    if (msg.content.equals("OFFINE")) {
                        JOptionPane.showMessageDialog(null, "This user " + msg.recipient + " is offline, you cannot send files to this user", "User is Offine", JOptionPane.ERROR_MESSAGE, image.OffineIcon());
                    } else {
                        String user = msg.rs.get(0);
                        String PK = msg.rs.get(1);
                        send = new SendFile(PK, user, this);
                        send.setVisible(true);

                    }
                } else if (msg.type.equals("upload_req")) {
                    int con = JOptionPane.showConfirmDialog(page, "Accept file:" + msg.content + " From User:" + msg.sender + " ?", "Accept file", JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION, image.IconFileWaiting());
                    if (con == JOptionPane.YES_OPTION) {
                        JFileChooser jf = new JFileChooser();
                        String fileExtension = "";
                        String nameWithoutExtension = "";
                        int lastDotIndex = msg.content.lastIndexOf(".");
                        if (lastDotIndex > 0) {
                            fileExtension = msg.content.substring(lastDotIndex + 1);
                            nameWithoutExtension = msg.content.substring(0, lastDotIndex);
                        } else {
                            nameWithoutExtension = msg.content;
                        }
                        jf.setSelectedFile(new File(nameWithoutExtension));
                        FileFilter filter = new FileNameExtensionFilter("File " + fileExtension, fileExtension);
                        jf.setFileFilter(filter);
                        int returnVal = jf.showSaveDialog(null);
                        String saveTo = jf.getSelectedFile().getPath() + "." + fileExtension;
                        if (saveTo != null && returnVal == JFileChooser.APPROVE_OPTION) {
                            String Key = msg.rs.get(0);
                            Download dwn = new Download(saveTo, Key);
                            Thread t = new Thread(dwn);
                            t.start();
                            Vector<String> vt = new Vector<String>();
                            vt.add(IpAdd.IPGet());
                            send(new Message("upload_res", msg.sender, ("" + dwn.port), msg.sender,vt));
                        }
                    } else if (con == JOptionPane.NO_OPTION) {
                        send(new Message("upload_res", msg.sender, "NO", msg.sender));
                    }
                } else if (msg.type.equals("upload_res")) {
                    if (!msg.content.equals("NO")) {
                        int port = Integer.parseInt(msg.content);
                        String addr = msg.sender;
                        Upload upl = new Upload(addr, port, send.file, send.getPublicKey());
                        Thread t = new Thread(upl);
                        t.start();
                        JOptionPane.showMessageDialog(null, "User accepted File, File is Uploading", "Notification", JOptionPane.INFORMATION_MESSAGE, image.Accept());
                    }
                } else if (msg.type.equals("upload_res_noaccept")) {
                    JOptionPane.showMessageDialog(null, "User rejected file, Upload file Cancel", "Notification", JOptionPane.INFORMATION_MESSAGE, image.Cancel());
                }

            } catch (Exception ex) {
                running = false;
                System.out.println("Disconnected Server");
                ex.printStackTrace();

            }

        }

    }

    public void closeThread(Thread t) {
        t = null;
    }
}
