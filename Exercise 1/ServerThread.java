import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
	private Socket socket = null;
	
	public ServerThread(Socket socket) {
		super("ServerThread");
		this.socket = socket;
	}
	
	public void run() {
		String address = socket.getInetAddress().getHostAddress();
    	System.out.printf("Client connected: %s%n", address);
		
		try (
        	PrintWriter out = new PrintWriter(socket.getOutputStream(), true);                   
        	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			)
		{
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equals("exit")){
                	System.out.printf("Client disconnected: %s%n", address);
                }
                else
                	out.println("Server> " + inputLine);
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}