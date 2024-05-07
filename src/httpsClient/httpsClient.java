package httpsClient;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class httpsClient {

    public static void main(String[] args) throws IOException {
        String url = "https://www.bundestag.de/presse";
        //get(url);
        //System.out.println(urlExists(url));
        System.out.println(getPayload(url));
    }

    public static void get(String urlString) throws IOException {
        URL url = new URL(urlString);
        int port = url.getPort() != -1 ? url.getPort() : url.getDefaultPort();

        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket socket = (SSLSocket) factory.createSocket(url.getHost(), port);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        writer.write(String.format("GET %s HTTP/1.0", url.getFile()));
        writer.newLine();
        writer.write(String.format("Host: %s", url.getHost()));
        writer.newLine();
        writer.newLine();
        writer.flush();

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        System.out.println(response.toString());
    }

    public static boolean urlExists(String urlString) {
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            return false;
        }

        int port = url.getPort() != -1 ? url.getPort() : url.getDefaultPort();

        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        try (
                SSLSocket socket = (SSLSocket) factory.createSocket(url.getHost(), port);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            writer.write(String.format("GET %s HTTP/1.0", url.getFile()));
            writer.newLine();
            writer.write(String.format("Host: %s", url.getHost()));
            writer.newLine();
            writer.newLine();
            writer.flush();

            String line = reader.readLine();
            String statusCode = line.trim().split("\\s++")[1];
            if (!statusCode.startsWith("2")) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static String getPayload(String urlString) throws IOException {
        URL url = new URL(urlString);
        int port = url.getPort() != -1 ? url.getPort() : url.getDefaultPort();

        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket socket = (SSLSocket) factory.createSocket(url.getHost(), port);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        writer.write(String.format("GET %s HTTP/1.0", url.getFile()));
        writer.newLine();
        writer.write(String.format("Host: %s", url.getHost()));
        writer.newLine();
        writer.newLine();
        writer.flush();

        StringBuilder response = new StringBuilder();
        String line;
        boolean payloadStarted = false;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty() && !payloadStarted) {
                payloadStarted = true;
            }

            if (payloadStarted)
                response.append(line);
        }
        reader.close();

        return response.toString();
    }
}
