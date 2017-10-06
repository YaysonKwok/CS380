package edu.cpp.cs380;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient {
	
    public static void main(String[] args) {
    	BufferedReader in = null;
    	PrintWriter out = null;
    	
    	try {
    		Socket chatSocket = new Socket("18.221.102.182", 38001);
    		in = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));
    		out = new PrintWriter(new OutputStreamWriter(chatSocket.getOutputStream()));
    	} catch (IOException Error) {
    		System.err.println("Can't establish connection");
    	}
    	
        Sender sender = new Sender(out);
        sender.start();
    	
        try {
            String serverMessages;
            while ((serverMessages=in.readLine()) != null) {
                System.out.println(serverMessages);
            }
         } catch (IOException Error) {
            System.err.println("Connection to server broken.");
            Error.printStackTrace();
         }
    }
}

class Sender extends Thread {
	private PrintWriter messageOut;

	public Sender(PrintWriter aOut) {
		messageOut = aOut;
	}
	
	public void run() {
	try {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while (!isInterrupted()) {
			String message = in.readLine();
			messageOut.println(message);
			messageOut.flush();
		}
	} catch (IOException Error) {
	        System.err.println("Connection to server broken");
	        Error.printStackTrace();
		}
	}
}