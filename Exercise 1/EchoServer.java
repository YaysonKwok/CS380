/**
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public final class EchoServer {

    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(22222)) {
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    String address = socket.getInetAddress().getHostAddress();
                    System.out.printf("Client connected: %s%n", address);
                    OutputStream os = socket.getOutputStream();
                    PrintStream out = new PrintStream(os, true, "UTF-8");
                    out.printf("Hi %s, thanks for connecting!%n", address);
                }
            }
        }
    }
}
**/


import java.net.*;
import java.io.*;

public final class EchoServer {
    public static void main(String[] args) throws IOException {

    	
        try (ServerSocket serverSocket = new ServerSocket(22222)) {
        	while (true) {
        		try (Socket socket = serverSocket.accept()) {
        				String address = socket.getInetAddress().getHostAddress();
                    	System.out.printf("Client connected: %s%n", address);
                    	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);                   
                    	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                        String inputLine;

                        while ((inputLine = in.readLine()) != null) {
                            if (inputLine.equals("exit")){
                            	System.out.printf("Client disconnected: %s%n", address);
                            }
                            else
                            	out.println("Server> " + inputLine);
                        }
        		}
        	}
        }
    }
}