package edu.cpp.cs380;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Ex3Client {

	public static void main(String[] args) {
		try(Socket Socket = new Socket("18.221.102.182", 38103);
				DataInputStream in = new DataInputStream(Socket.getInputStream());
					PrintWriter out = new PrintWriter(Socket.getOutputStream(), true)){
			
			int size = in.read();
			byte ByteArray[] = new byte[size];
			
			for (int x = 0; x < size; x++){
				ByteArray[x] = (byte) in.read();
			}
			
			byte checkSum = (byte) checksum(ByteArray);
			
			System.out.print(checkSum);
			out.print(checkSum);
			
			byte response = in.readByte();
			
			System.out.print(response);
			
			if (response == 1){
				System.out.print("Response Good");
			}
			else
				System.out.print("Response Bad");
			
			
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		}



	}
	
	public static short checksum(byte[] b){
		int length = b.length;
	    int i = 0;
	    short sum = 0;
	    while (length > 0) {
	        sum += (b[i++]&0xff) << 8;
	        if ((--length)==0) {
	        	break;
	        }
	        sum += (b[i++]&0xff);
	        --length;
	    }

	    return (short) ((~((sum & 0xFFFF)+(sum >> 16)))&0xFFFF);
		
	}
}
