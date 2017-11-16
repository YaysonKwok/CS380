import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Random;

public class IPv4UDP {

    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket("18.221.102.182",38005)) {
            System.out.println("Connected to server.");
            InputStream is = socket.getInputStream();
            PrintStream out = new PrintStream((socket.getOutputStream()),true,"UTF-8");
            
            out.write(handshake());
            System.out.println("Handshake: 0x"+(Integer.toHexString(is.read())+Integer.toHexString(is.read())+Integer.toHexString(is.read())+Integer.toHexString(is.read())).toUpperCase());
            byte Byte1 = (byte) is.read();
            byte Byte2 = (byte) is.read();
            
            String port = "" + Byte.toUnsignedInt(Byte1) + Byte.toUnsignedInt(Byte2);
            
            System.out.println("Port Number: " + port);
            
            for(int packetN=0; packetN<12; packetN++) {
                System.out.println("Packet " + packetN);
                int udpAmount = (int) Math.pow(2,(packetN+1));
                byte[] Octet = new byte[28+udpAmount];
                //Version + HLen
                Octet[0] = 0x45;
                //TOS
                Octet[1] = 0x00;
                //TOTAL LENGTH
                int size = (int) (28 + Math.pow(2,(packetN+1)));
                String hexSize = Integer.toHexString(size);
                if (hexSize.length() > 4)
                	hexSize = hexSize.substring(hexSize.length() - 4);
                else {
                    while (hexSize.length() < 4)
                    	hexSize = "0" + hexSize;
                }
                Octet[2] = (byte) Integer.parseInt(hexSize.substring(0, 2).toUpperCase(), 16);
                Octet[3] = (byte) Integer.parseInt(hexSize.substring(2).toUpperCase(), 16);
                //Identification
                Octet[4] = 0;
                Octet[5] = 0;
                //Flag assuming no fragmentation
                Octet[6] = 0x40;
                //Offset
                Octet[7] = 0;
                //TTL
                Octet[8] = 0x32;
                //Protocol
                Octet[9] = 0x11;
                //Source IP address
                Octet[12] = 0x10;
                Octet[13] = 0x11;
                Octet[14] = 0x12;
                Octet[15] = 0x13;
                //Server IP address
                Octet[16] = 0x12;
                Octet[17] = (byte) 0xDD;
                Octet[18] = (byte) 0x66;
                Octet[19] = (byte) 0xB6;
                //Checksum calculations
                byte[] checksumBytes=new byte[18];
                for(int i=0;i<18;i++){
                        if(i<10){
                        	checksumBytes[i]=Octet[i];
                        }
                        else{
                        	checksumBytes[i]=Octet[i+2];
                        }
                }
                int checkSum = checksum(checksumBytes);
                String hex = Integer.toHexString(checkSum);
                if (hex.length() > 4)
                    hex = hex.substring(hex.length() - 4);
                else {
                    while (hex.length() < 4)
                        hex = "0" + hex;
                }
                Octet[10] = (byte) Integer.parseInt(hex.substring(0, 2).toUpperCase(), 16);
                Octet[11] = (byte) Integer.parseInt(hex.substring(2).toUpperCase(), 16);   
                
                //Source Port
                Octet[20] = 0x01;
                Octet[21] = 0x02;
                //Destination Port
                Octet[22] = Byte1;
                Octet[23] = Byte2;
                //Length of UDP - 20 is the size of the IPv4
                String udpSize=Integer.toHexString(size-20);
                if (udpSize.length() > 4)
                	udpSize = udpSize.substring(udpSize.length() - 4);
                else {
                    while (udpSize.length() < 4)
                    	udpSize = "0" + udpSize;
                }
                Octet[24] = (byte) Integer.parseInt(udpSize.substring(0, 2).toUpperCase(), 16);
                Octet[25] = (byte) Integer.parseInt(udpSize.substring(2).toUpperCase(), 16);  
                //Data
                Random randomData = new Random();
                byte[] randomBytes = new byte[(int) Math.pow(2,(packetN+1))];
                randomData.nextBytes(randomBytes);
                for(int i=0;i<Math.pow(2,(packetN+1));i++){
                	Octet[28+i]=randomBytes[i];
                }
                //Checksum
                byte[] checksumUDPBytes=new byte[20 + udpAmount];
                //IPv4 Source
                checksumUDPBytes[0] = 0x10;
                checksumUDPBytes[1] = 0x11;
                checksumUDPBytes[2] = 0x12;
                checksumUDPBytes[3] = 0x13;
                //IPv4 Destination
                checksumUDPBytes[4] = 0x12;
                checksumUDPBytes[5] = (byte) 0xDD;
                checksumUDPBytes[6] = (byte) 0x66;
                checksumUDPBytes[7] = (byte) 0xB6;
                //Protocol
                checksumUDPBytes[9] = 0x11;
                //Length
                checksumUDPBytes[10] = Octet[24];
                checksumUDPBytes[11] = Octet[25];
                //UDP Datagram Source Port
                checksumUDPBytes[12] = 0x01;
                checksumUDPBytes[13] = 0x02;
                //UDP Datagram Destination Port
                checksumUDPBytes[14] = Byte1;
                checksumUDPBytes[15] = Byte2; 
                //UDP Length
                checksumUDPBytes[16] = Octet[24];
                checksumUDPBytes[17] = Octet[25];
                //UDP Checksum
                for(int i=0;i<udpAmount;i++){
                	checksumUDPBytes[20+i]=randomBytes[i];
                }
                checkSum = checksum(checksumUDPBytes);
                hex = Integer.toHexString(checkSum);
                if (hex.length() > 4)
                    hex = hex.substring(hex.length() - 4);
                else {
                    while (hex.length() < 4)
                        hex = "0" + hex;
                }
                Octet[26] = (byte) Integer.parseInt(hex.substring(0, 2).toUpperCase(), 16);
                Octet[27] = (byte) Integer.parseInt(hex.substring(2).toUpperCase(), 16);
                out.write(Octet);
                System.out.println("Response: 0x"+(Integer.toHexString(is.read())+Integer.toHexString(is.read())+Integer.toHexString(is.read())+Integer.toHexString(is.read())).toUpperCase());
            }
            
            is.close();
            socket.close();
            System.out.println("Disconnected from server.");
            }
    }
    

	private static byte[] handshake() {
    	byte[] Octet = new byte[24];
        //Version + HLen
        Octet[0] = 0x45;
        //TOS
        Octet[1] = 0x00;
        //TOTAL LENGTH in Octet[2] and Octet[3]
        String hexSize = Integer.toHexString(24);
        if (hexSize.length() > 4)
        	hexSize = hexSize.substring(hexSize.length() - 4);
        else {
            while (hexSize.length() < 4)
            	hexSize = "0" + hexSize;
        }
        Octet[2] = (byte) Integer.parseInt(hexSize.substring(0, 2).toUpperCase(), 16);
        Octet[3] = (byte) Integer.parseInt(hexSize.substring(2).toUpperCase(), 16);
        //Identification
        Octet[4] = 0;
        Octet[5] = 0;
        //Flag assuming no fragmentation
        Octet[6] = 0x40;
        //Offset
        Octet[7] = 0;
        //TTL
        Octet[8] = 0x32;
        //Protocol
        Octet[9] = 0x11;
        //Source IP address
        Octet[12] = 0x11;
        Octet[13] = 0x11;
        Octet[14] = 0x11;
        Octet[15] = 0x11;
        //Server IP address
        Octet[16] = 0x12;
        Octet[17] = (byte) 0xDD;
        Octet[18] = (byte) 0x66;
        Octet[19] = (byte) 0xB6;
        //Data
        Octet[20] = (byte) 0xDE;
        Octet[21] = (byte) 0xAD;
        Octet[22] = (byte) 0xBE;
        Octet[23] = (byte) 0xEF;
        //Checksum
        byte[] checksumBytes=new byte[18];
        for(int i=0;i<18;i++){
                if(i<10){
                	checksumBytes[i]=Octet[i];
                }
                else{
                	checksumBytes[i]=Octet[i+2];
                }
        }
        int checkSum = checksum(checksumBytes);
        String hex = Integer.toHexString(checkSum);
        if (hex.length() > 4)
            hex = hex.substring(hex.length() - 4);
        else {
            while (hex.length() < 4)
                hex = "0" + hex;
        }
        Octet[10] = (byte) Integer.parseInt(hex.substring(0, 2).toUpperCase(), 16);
        Octet[11] = (byte) Integer.parseInt(hex.substring(2).toUpperCase(), 16);    
		return Octet;
	}

	public static short checksum(byte[] b){
        int cSum = 0;
        for(int i=0;i<b.length;i+=2){
            short one = (short) (b[i] & 0xFF);
            try {
                short two = (short) (b[i + 1] & 0xFF);
                cSum += ((256 * one) + two);
                if (cSum >= 65535) {
                    cSum -= (65535);
                }
            }
            catch (ArrayIndexOutOfBoundsException e){
                cSum+=(256*one);
                if (cSum >= 65535) {
                    cSum -= (65535);
                }
            }
        }
        return (short) ((~(cSum))& 0xFFFF);
    }
}

