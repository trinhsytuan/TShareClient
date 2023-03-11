/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Code;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Admin
 */
class ServerThread extends Thread {

    public Socket socket = null;
    public SocketServer server = null;
    public String username = "";
    public int ID = -1;
    public ObjectInputStream streamIn = null;
    public ObjectOutputStream streamOut = null;
    public MainServer ui;

    public ServerThread(SocketServer _server, Socket _socket) {
        super();
        server = _server;
        socket = _socket;
        ID = socket.getPort();
        ui = server.ui;
    }

    public void send(Message msg) {
        try {
            streamOut.writeObject(msg);
            System.out.println(msg.content);
            streamOut.flush();
        } catch (Exception ex) {
            System.err.println("Exception: Error");
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    public void run() {
        ui.jTextArea1.append("\nServer Thread " + ID + " running.");
        while (true) {
            try {
                Message msg = (Message) streamIn.readObject();
                server.handle(ID, msg);
            } catch (Exception ioe) {
                System.out.println(ID + " ERROR reading: ");
                ioe.printStackTrace();
                server.remove(ID);
                stop();
            }
        }
    }

    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (streamIn != null) {
            streamIn.close();
        }
        if (streamOut != null) {
            streamOut.close();
        }
    }

    public void open() throws IOException {
        streamOut = new ObjectOutputStream(socket.getOutputStream());
        streamOut.flush();
        streamIn = new ObjectInputStream(socket.getInputStream());
    }

    public int getID() {
        return ID;
    }
}

public class SocketServer implements Runnable {

    public ServerThread cilents[];
    public ServerSocket server = null;
    public Thread thread = null;
    public int clientCount = 0, port = 2023;
    public MainServer ui;
    public Database db;

    public SocketServer(MainServer ui, Database db) {
        cilents = new ServerThread[100];
        this.ui = ui;
        this.db = db;
        try {
            server = new ServerSocket(port);
            port = server.getLocalPort();
            ui.jTextArea1.append("Server started IP:" + InetAddress.getLocalHost() + " Port:" + server.getLocalPort());
            start();
        } catch (IOException IOE) {
            ui.jTextArea1.append("Can not bind to port " + port + "\nRetrying start");
            ui.RetryStart();
        }
    }

    @SuppressWarnings("deprecation")
    public void stop() {
        if (thread != null) {
            thread.stop();
            thread = null;
        }
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    private int findClient(int ID) {
        for (int i = 0; i < clientCount; i++) {
            if (cilents[i].getID() == ID) {
                return i;
            }
        }
        return -1;
    }

    @SuppressWarnings("deprecation")
    public synchronized void remove(int ID) {
        int pos = findClient(ID);
        if (pos >= 0) {
            ServerThread toTerminate = cilents[pos];
            ui.jTextArea1.append("\nRemoving client thread " + ID + " at " + pos);
            if (pos < clientCount - 1) {
                for (int i = pos + 1; i < clientCount; i++) {
                    cilents[i - 1] = cilents[i];
                }
            }
            clientCount--;
            try {
                toTerminate.close();
            } catch (IOException ioe) {
                ui.jTextArea1.append("\nError closing thread: " + ioe);
            }
            toTerminate.stop();
        }
    }

    public void handle(int id, Message msg) {
        if (msg.type.equals("SIGNUP")) {
            if (findUserThread(msg.sender) == null) {
                if (db.checkUsernameRegister(msg.sender)) {
                    db.addUser(msg.sender, msg.content);
                    cilents[findClient(id)].username = msg.sender;
                    sendNewMem(msg.sender);
                    cilents[findClient(id)].send(new Message("SIGNUP", "SERVER", "ACCEPT", msg.sender));
                } else {
                    cilents[findClient(id)].send(new Message("SIGNUP", "SERVER", "NOACCEPT", msg.sender));
                }
            }
        } else if (msg.type.equals("LOGIN")) {
            if (db.login(msg.sender, msg.content, msg.recipient)) {
                cilents[findClient(id)].username = msg.sender;
                cilents[findClient(id)].send(new Message("LOGIN", "SERVER", "ACCEPT", msg.sender));
            } else {
                cilents[findClient(id)].send(new Message("LOGIN", "SERVER", "NOACCEPT", msg.sender));
            }
        } else if (msg.type.equals("BYE")) {
            remove(id);
        } else if (msg.type.equals("GetUser")) {
            cilents[findClient(id)].send(new Message("GetUser", "SERVER", "ListUser", msg.sender, db.ListUser(msg.sender)));
        } else if (msg.type.equals("SendToUser")) {
            if (CheckOnline(msg.content)) {
                cilents[findClient(id)].send(new Message("SendMem", "SERVER", "ONLINE", msg.content, db.GetKeyUser(msg.content)));
            } else {
                cilents[findClient(id)].send(new Message("SendMem", "SERVER", "OFFINE", msg.content));
            }
        } else if (msg.type.equals("upload_req")) {
            ServerThread dba = findUserThread(msg.recipient);
            if (dba != null) {
                dba.send(new Message("upload_req", msg.sender, msg.content, msg.recipient));
            } else {
                cilents[findClient(id)].send(new Message("req_fail", "SERVER","",msg.sender));
            }

        } else if (msg.type.equals("upload_res")) {
            if (!msg.content.equals("NO")) {
                String IP = findUserThread(msg.sender).socket.getInetAddress().getHostAddress();
                ServerThread dba = findUserThread(msg.recipient);
                if (dba != null) {
                    dba.send(new Message("upload_res", IP, msg.content, msg.recipient));
                }
            } else {
                ServerThread dba = findUserThread(msg.recipient);
                if (dba != null) {
                    dba.send(new Message("upload_res_noaccept", msg.sender, msg.content, msg.recipient));
                }
            }
        }
    }

    private void addThread(Socket socket) {
        if (clientCount < cilents.length) {
            ui.jTextArea1.append("\nClient accepted: " + socket);
            cilents[clientCount] = new ServerThread(this, socket);
            try {
                cilents[clientCount].open();
                cilents[clientCount].start();
                clientCount++;
            } catch (IOException ioe) {
                ui.jTextArea1.append("\nError opening thread: " + ioe);
            }
        } else {
            ui.jTextArea1.append("\nClient refused: Maximum " + cilents.length + " reached.");
        }
    }

    public void sendNewMem(String username) {
        Vector<String> mem = db.ListUser(username);
        for (int i = 0; i < mem.size(); i++) {
            ServerThread dba = findUserThread(mem.get(i));
            if (dba != null) {
                dba.send(new Message("NEWUSER", "SERVER", username, mem.get(i)));
            }
        }
    }

    @Override
    public void run() {
        while (thread != null) {
            try {
                ui.jTextArea1.append("\nWaiting for a client ...");
                addThread(server.accept());
            } catch (Exception ioe) {
                ui.jTextArea1.append("\nServer accept error: \n");
                ui.RetryStart();
            }
        }
    }

    public ServerThread findUserThread(String usr) {
        for (int i = 0; i < clientCount; i++) {
            if (cilents[i].username.equals(usr)) {
                return cilents[i];
            }
        }
        return null;
    }

    public boolean CheckOnline(String username) {
        for (int i = 0; i < clientCount; i++) {
            if (cilents[i].username.equals(username)) {
                return true;
            }
        }
        return false;
    }
}
