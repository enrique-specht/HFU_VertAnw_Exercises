package services.timeservice_client;

import java.io.*;
import java.net.Socket;

public class TimeServiceClient {

    public static void main (String[] args) throws IOException {
        String serverAddress = "127.0.0.1";

        TimeServiceClient client1 = new TimeServiceClient();
        System.out.println(client1.dateFromServer(serverAddress));
        System.out.println(client1.timeFromServer(serverAddress));
        System.out.println(client1.dateFromServer(serverAddress));

        TimeServiceClient client2 = new TimeServiceClient();
        System.out.println(client2.timeFromServer(serverAddress));
    }

    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;

    public String dateFromServer(String ipAddress) throws IOException {
        connectToServer(ipAddress);
        initializeBuffer();

        writer.write("date");
        writer.newLine();
        writer.flush();

        String date = reader.readLine();

        disconnectFromServer();
        return date;
    }

    public String timeFromServer(String ipAddress) throws IOException {
        connectToServer(ipAddress);
        initializeBuffer();

        writer.write("time");
        writer.newLine();
        writer.flush();

        String time = reader.readLine();

        disconnectFromServer();
        return time;
    }

    private void initializeBuffer() throws IOException {
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        reader.readLine(); // Catch initial "time service"-Response
    }

    private void connectToServer(String ipAddress) throws IOException {
        socket = new Socket(ipAddress, 75);
    }

    private void disconnectFromServer() throws IOException {
        socket.close();
    }
}
