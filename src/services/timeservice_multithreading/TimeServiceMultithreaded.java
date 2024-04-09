package services.timeservice_multithreading;

import java.io.*;
import java.net.ServerSocket;

public class TimeServiceMultithreaded {

    public static void main(String[] args) {
        new TimeServiceMultithreaded().startServer();
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(75)) {
            while (true)
                new TimeServiceThread(serverSocket.accept()).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

