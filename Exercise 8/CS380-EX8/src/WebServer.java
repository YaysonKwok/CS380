import java.util.*;
import java.io.*;
import java.net.*;

public class WebServer {
  private static ServerSocket serverSocket;

  public static void main(String[] args) throws IOException {
    serverSocket = new ServerSocket(8080);
    while (true) {
      try {
        Socket socket=serverSocket.accept();
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintStream out = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));

        String getString = br.readLine();

        //Grabs filename
        String filename="";
        StringTokenizer StringToken=new StringTokenizer(getString);
        try {
          if (StringToken.hasMoreElements() && StringToken.nextToken().equalsIgnoreCase("GET") && StringToken.hasMoreElements())
            filename=StringToken.nextToken();
          else
            throw new FileNotFoundException();
          //Navigates to the www folder and looks for file
          InputStream file=new FileInputStream("www/" + filename);
          //Successful
          out.print("HTTP/1.1 200 OK\r\n"+ "Content-type:text/html \r\n\r\n");
          
          byte[] a=new byte[1000];
          int counter;
          while ((counter=file.read(a))>0)
            out.write(a, 0, counter);
          out.close();
          file.close();
        }
        //404
        catch (FileNotFoundException x) {
          out.println("HTTP/1.1 404 Not Found\r\n"+ "Content-type: text/html\r\n\r\n"+ "<html><head><title>Not Found</title></head><body>Sorry, the object you requested was not found.</body></html>\n");
          out.close();
        }
      }
      catch (Exception x) {
        System.out.println(x);
      }
    }
  }
}
