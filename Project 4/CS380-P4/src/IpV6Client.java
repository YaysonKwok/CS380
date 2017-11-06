import java.io.*;
import java.net.Socket;
public final class IpV6Client {
    public static void main(String[] args) throws Exception {
        try (Socket socket = new Socket("18.221.102.182",38004)) {
            System.out.println("Connected to server.");
            InputStream is = socket.getInputStream();
            PrintStream out = new PrintStream((socket.getOutputStream()),true,"UTF-8");
            
            for(int packetN=0; packetN<12; packetN++) {
                System.out.println("Packet " + packetN);
                int packetNumber = (int) Math.pow(2,(packetN+1));
                byte[] Octet = new byte[40 + packetNumber];
                //Version + 4 bytes of Traffic Class
                Octet[0] = 0x60;
                //4 last bits of Traffic class + 4 first bits of Flow Label
                Octet[1] = 0x00;
                //Next byte of Flow Label 
                Octet[2] = 0x00;
                //Last byte of Flow Label
                Octet[3] = 0x00;
                String packetHex = Integer.toHexString(packetNumber);
                if (packetHex.length() < 4){
                    while (packetHex.length() < 4)
                    	packetHex = "0" + packetHex;
                }
                //First byte of Payload Length
                Octet[4] =(byte) Integer.parseInt(packetHex.substring(0, 2).toUpperCase(), 16);
                //Last 4 bits of Payload length
                Octet[5] = (byte) Integer.parseInt(packetHex.substring(2).toUpperCase(), 16);
                //Next Header
                Octet[6] = 0x11;
                //Hop Limit
                Octet[7] = 0x14;
                //Source Address
                Octet[18] = (byte) 0xff;
                Octet[19] = (byte) 0xff;
                Octet[20] = (byte) 0xc0;
                Octet[21] = (byte) 0xa8;
                Octet[22] = 0x01;
                Octet[23] = 0x01;
                //Destination Address
                Octet[34] = (byte) 0xff;        
                Octet[35] = (byte) 0xff;        
                Octet[36] = 0x12;         
                Octet[37] = (byte) 0xdd;        
                Octet[38] = (byte) 0x66;                
                Octet[39] = (byte) 0xb6;
                out.write(Octet);
                System.out.println("Response: 0x"+(Integer.toHexString(is.read())+Integer.toHexString(is.read())+Integer.toHexString(is.read())+Integer.toHexString(is.read())).toUpperCase());
            }
            
            is.close();
            socket.close();
            System.out.println("Disconnected from server.");
            }
    }
}
