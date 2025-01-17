package Homework;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    final static int SERVER_PORT = 7777;
    final static String SERVER_IP = "127.0.0.1";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("프로그램 실행방법 : java TcpIpMultiChattingClient 사용자명");
            System.exit(0);
        }

        try {
            String serverIp = SERVER_IP;
            // 소켓을 생성하여 연결을 요청한다.
            Socket socket = new Socket(serverIp, SERVER_PORT);
            System.out.println("서버에 연결되었습니다.");

            Thread sender = new Thread(new ClientSender(socket, args[0]));
            Thread receiver = new Thread(new ClientReceiver(socket));

            sender.start();
            receiver.start();
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
        }
    }

    static class ClientSender extends Thread {
        Socket socket;
        DataOutputStream out;
        String name;

        public ClientSender(Socket socket, String name) {
            this.socket = socket;
            try {
                out = new DataOutputStream(socket.getOutputStream());
                this.name = name;
            } catch (Exception e) {
            }
        }

        public void run() {
            Scanner scanner = new Scanner(System.in);
            try {
                if (out != null) {
                    out.writeUTF(name);
                }

                while (out != null) {
                 //   out.writeUTF("[" + name + "]" + scanner.nextLine());
                    String now = scanner.nextLine();
                    out.writeUTF(now);
                    if (now.equals("STOP")) {
                        System.exit(0);
                        return;
                    }
                }
            } catch (IOException e) {
            }
        }
    }

    static class ClientReceiver extends Thread {
        Socket socket;
        DataInputStream in;

        public ClientReceiver(Socket socket) {
            this.socket = socket;
            try {
                in = new DataInputStream(socket.getInputStream());
            } catch (IOException io) {
            }
        }

        public void run() {
            while (in != null) {
                try {
                    System.out.println(in.readUTF());
                } catch (IOException ie) {
                }
            }
        }
    }
}
