package edu.cpp.cs380;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class PhysLayerClient {

	public static void main(String[] args) {
		double preambleByte = 0;
		int[] binaryByte = new int[320];
		int[] fourFive = new int[320];
		String[] five = new String[64];
		int[] four = new int[64];
		
		try (Socket clientSocket = new Socket("18.221.102.182", 38002);
			 DataInputStream in = new DataInputStream(clientSocket.getInputStream());
			 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true))
			{
	            for (int x = 0; x < 64; x++){
	            	preambleByte += in.readByte();
	            }
	            preambleByte = preambleByte/64;
            	System.out.println(preambleByte);
            	
            	//Converts numbers into 1 or 0 depending on baseline
	            for (int x = 0; x < 320; x++){
	            	
	            	if (in.readByte() > preambleByte){
	            		binaryByte[x] = 1;
	            	}
	            	else{
	            		binaryByte[x] = 0;
	            	}
	            	//System.out.print(binaryByte[x]);
	            }
	            System.out.println();
	            
	            //NRZI Decoding
	            int Signal = 1;
	            for (int x = 0; x < binaryByte.length; x++){
	            	if (Signal == binaryByte[x]){
	            		fourFive[x] = 0;
	            		Signal = 0;
	            	}
	            	else {
	            		fourFive[x] = 1;
	            		Signal = 1;
	            	}
	            }
	            
	            System.out.println();
            	int y = 0;
	            //Grouping into 5ths.
	            for (int x = 0; x < fourFive.length; x=x+5){

	            	String result = "" + fourFive[x] + fourFive[x+1] + fourFive[x+2] + fourFive[x+3] + fourFive[x+4];
	            	five[y] = result;
	            	//System.out.println(five[y]);
	            	y++;
	            }
	            //Decoding
	            for (int x = 0; x < five.length; x++){
	            	int value = Integer.parseInt(five[x]);
	            	switch (value) {
	            		case 11110: four[x] = 0000;
	            					break;	
	            		case 01001: four[x] = 0001;
    								break;
	            		case 10100: four[x] = 0010;
    								break;
	            		case 10101: four[x] = 0011;
    								break;
	            		case 01010: four[x] = 0100;
    								break;
	            		case 01011: four[x] = 0101;
    								break;
	            		case 01110: four[x] = 0110;
    								break;
	            		case 01111: four[x] = 0111;
    								break;
	            		case 10010: four[x] = 1000;
    								break;
	            		case 10011: four[x] = 1001;
    								break;
	            		case 10110: four[x] = 1010;
    								break;
	            		case 10111: four[x] = 1011;
    								break;
	            		case 11010: four[x] = 1100;
    								break;
	            		case 11011: four[x] = 1101;
    								break;
	            		case 11100: four[x] = 1110;
    								break;
	            		case 11101: four[x] = 1111;
    								break;	
	            	}
	            	System.out.println(four[x]);
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
