package Homework;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;

import Homework.Components.MysqlEngine;
import Homework.Components.StroageEngine.TableBufferHandler;

public class Server {
    final static int SERVER_PORT = 7777;

    // 서버에 접속한 클라이언트를 HashMap에 저장하여 관리한다.
    HashMap clients; // Name
    TableBufferHandler tables;
    MysqlEngine Engine;
    public Server() throws IOException, CloneNotSupportedException {
        clients = new HashMap();
        Engine = new MysqlEngine();
        Collections.synchronizedMap(clients); // 동기화 처리
    }

    public void start() {
        ServerSocket serverSocket = null;
        Socket socket = null;
        Runtime current = Runtime.getRuntime(); 
        current.addShutdownHook(new ThreadChild()); 
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("서버가 시작되었습니다.");

            while (true) {
                socket = serverSocket.accept();
                System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "에서 접속하였습니다.");

                ServerReceiver thread = new ServerReceiver(socket);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendtoOne(String Id, String msg) {
        try {
            DataOutputStream out = (DataOutputStream)clients.get(Id);
            Engine.Query(msg);
            System.out.println(msg);
            out.writeUTF(Engine.ShowMessage()+"...");
        } catch (Exception e) {
            System.out.println("FAIL");
        }
    }
    class ThreadChild extends Thread { 
      
        public void run() { 
              
            try {
                Engine.flush();
                System.out.println("In shutdown hook"); 
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } 
    } 
    public static void main(String[] args) throws IOException, CloneNotSupportedException {
        new Server().start();
    }

    class ServerReceiver extends Thread {
        Socket socket;
        DataInputStream in;
        DataOutputStream out;
 
        public ServerReceiver(Socket socket) {
            this.socket = socket;
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
            } catch (IOException ie) {
            }
        }
 

        public void run() {
            String name = "";
            try {
                name = in.readUTF();//이름
                sendtoOne(name,"#" + name + "님이 입장했습니다.");
 
                clients.put(name, out);
                System.out.println("현재 서버접속자 수는 " + clients.size() + "입니다.");
 
                while (in != null) {
                    String it=in.readUTF();
                    if(it.equals("STOP")) {clients.remove(name);break;}
                    else if(it.equals("SERVER STOP")) System.exit(0);
                    sendtoOne(name,it);
                }
            } catch (IOException ie) {
            } finally {
                sendtoOne(name,"#" + name + "님이 퇴장했습니다.");
                clients.remove(name);
                System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + "에서 접속을 종료하였습니다.");
                System.out.println("현재 서버접속자 수는 " + clients.size() + "입니다.");
            }
        }
    }
}



