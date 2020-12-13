package edu.spbu.serverclient;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.io.IOException;
import java.util.Scanner;

public class Client
{
    private static InputStream inputStream;
    private static OutputStream outputStream;
    private static String host = "example.org";

    public static void main(String[] args) throws IOException {
        Socket client = new Socket(host, 80);
        inputStream = client.getInputStream();
        outputStream = client.getOutputStream();
        sendRequest();
        readInput();
        client.close();
    }

    public static void sendRequest() throws IOException {
        String r = "GET /index.html HTTP/1.1\r\nHost: " + host + "\r\nConnection: close\r\n\r\n";
        outputStream.write(r.getBytes());
        outputStream.flush();
    }

    public static void readInput() {
        Scanner reader = new Scanner(inputStream);
        while (reader.hasNext()) {
            System.out.println(reader.nextLine());
        }
        reader.close();
    }
}