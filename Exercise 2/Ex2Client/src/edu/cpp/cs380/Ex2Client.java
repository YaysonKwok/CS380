package edu.cpp.cs380;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class Ex2Client {

	public static void main(String[] args) {
        byte[] messageByte = new byte[200];
        
		try (Socket clientSocket = new Socket("18.221.102.182", 38102);
			)
			 
		{
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());

            for (int x = 0; x < 200; x++){
                messageByte[x] = in.readByte();
                System.out.println(messageByte[x]);
                
            }
            
            
            Checksum checksum = new CRC32();
            checksum.update(messageByte, 0, messageByte.length);
            long checksumValue = checksum.getValue();
            System.out.println("Generated CRC32: " + checksumValue);


		} catch (UnknownHostException e) {
			System.err.println("Unknown Host");
			e.printStackTrace();
		} catch (IOException e) {
			 System.err.println("Couldn't get I/O for the connection");
			e.printStackTrace();
		}
	}

}
