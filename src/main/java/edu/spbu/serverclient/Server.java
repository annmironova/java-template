package edu.spbu.serverclient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Server {

    public static InputStream inputStream;
    public static OutputStream outputStream;

    public static void main(String[] args) throws IOException {
        ServerSocket sc = new ServerSocket(8080); // создание серверного подключения для ожидания клиента

        while(true) {
            Socket client = sc.accept(); // socket чтобы передавать соо клиенту
            System.out.println("Client accepted\n");
            inputStream = client.getInputStream(); // поток в котором есть команды для отправки данных
            outputStream = client.getOutputStream();
            sendRequest(receiveFileName());
            client.close();
        }
    }

    public static String receiveFileName() {
        String fileName;
        Scanner reader = new Scanner(inputStream);
        String request = reader.nextLine();
        System.out.println("Request: " + request);
        String[] get_filename = request.split(" ");
        if (get_filename[0].equals("GET")) {
            fileName = get_filename[1].split("/")[1];
            System.out.println("Opening " + fileName + "...\n");
            return fileName;
        }
        return null;
    }

    public static void sendRequest (String fileName) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            Path path = Paths.get(fileName);
            String str = new String(Files.readAllBytes(path));

            String response = "HTTP/1.1 200 OK\n" +
                              "Content-type: text/html\n\n";
            System.out.println(response);
            outputStream.write((response + str).getBytes());
        }
        else {
            outputStream.write(
                    ("HTTP/1.1 200 OK\n" +
                    "Content-type: text/html\n\n" +
                    "<html><h1>404</h1><h2>NOT FOUND</h2></html>").getBytes());
            outputStream.flush(); //гарантирует что соо будет прямо сейчас отправлено
        }
        outputStream.close();
    }
}

