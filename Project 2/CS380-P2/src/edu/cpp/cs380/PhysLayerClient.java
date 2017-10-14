package edu.cpp.cs380;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class PhysLayerClient {

	public static void main(String[] args) {
		double preambleByte = 0;
		byte[] signalByte = new byte[320];
		int[] binaryByte = new int[320];
		
		try (Socket clientSocket = new Socket("18.221.102.182", 38002);
			 DataInputStream in = new DataInputStream(clientSocket.getInputStream());
			 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true))
			{
	            for (int x = 0; x < 64; x++){
	            	preambleByte += in.readByte();
	            }
	            preambleByte = preambleByte/64;
            	System.out.println(preambleByte);
            	
            	
	            for (int x = 0; x < 320; x++){
	            	if (in.readByte() > preambleByte){
	            		binaryByte[x] = 1;
	            	}
	            	else{
	            		binaryByte[x] = 0;
	            	}
	            	System.out.print(binaryByte[x]);
	            }
	            
			} catch (UnknownHostException e) {
				System.err.println("Unknown Host");
				e.printStackTrace();
			} catch (IOException e) {
				 System.err.println("Couldn't get I/O for the connection");
				e.printStackTrace();
			}

	}

}
