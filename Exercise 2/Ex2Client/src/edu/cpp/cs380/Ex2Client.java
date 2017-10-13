package edu.cpp.cs380;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class Ex2Client {

	public static void main(String[] args) {
        byte[] messageByte = new byte[100];
        
		try (Socket clientSocket = new Socket("18.221.102.182", 38102);
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true))
		{
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());

            for (int x = 0; x < 100; x++){
                messageByte[x] = (byte)((in.readByte() & 0x0f) | (in.readByte()& 0xf0));
                //System.out.println(messageByte[x]);
            }
            
            System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(messageByte));
            
            //CRC32 
            Checksum checksum = new CRC32();
            checksum.update(messageByte, 0, messageByte.length);
            long checksumValue = checksum.getValue();
            System.out.println("Generated CRC32: " + Long.toHexString(checksumValue));
            out.println(Long.toHexString(checksumValue));
            
            //Checking if CRC is correct
            byte check = in.readByte();
            if (check == 1){
            	System.out.print("Response Good");
            }
            else
            	System.out.println("Response Bad");

		} catch (UnknownHostException e) {
			System.err.println("Unknown Host");
			e.printStackTrace();
		} catch (IOException e) {
			 System.err.println("Couldn't get I/O for the connection");
			e.printStackTrace();
		}
	}

}
