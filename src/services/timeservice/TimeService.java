package services.timeservice;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeService {

    public static void main(String[] args) {
        executeServer();
    }

    public static void executeServer() {
        try (ServerSocket serverSocket = new ServerSocket(75)) {
            while (true) {
                Socket socket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                String clientAddress = socket.getInetAddress().toString();
                System.out.printf("Client (%s) connected %n", clientAddress);

                writer.write("time service");
                writer.newLine();
                writer.flush();

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.equals("date")) {
                        System.out.printf("%s requested date %n", clientAddress);
                        writer.write(Clock.date());
                        writer.newLine();
                        writer.flush();
                    } else if (line.equals("time")) {
                        System.out.printf("%s requested time %n", clientAddress);
                        writer.write(Clock.time());
                        writer.newLine();
                        writer.flush();
                    } else {
                        break;
                    }
                }

                socket.close();
                System.out.printf("Client (%s) disconnected %n", clientAddress);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
