import java.io.*;
import java.net.*;
import java.util.*;

// reference: https://www.geeksforgeeks.org/multithreaded-servers-in-java/
  
// Single Thread Client class
class SingleClient {
    // driver code
    public static void main(String[] args)
    {
        System.out.println("Client started.");
        
        try {

            FileReader file = new FileReader("input.txt");
            BufferedReader buffer = new BufferedReader(file);

            Socket socket = new Socket("localhost", 1234);
            
            // writing to server
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
  
            // reading from server
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String msg = buffer.readLine();
            while(msg != null){        
                System.out.printf("Sent: %s\n", msg);
                    
                output.println(msg);
                output.flush();
          
                // displaying server reply
                System.out.println("Server replied: "+ input.readLine());

                msg = buffer.readLine();
            }

            output.println("exit");
            output.flush();

            output.close();
            input.close();
        }
        catch (IOException e) {
            System.out.println("Server disconnected.");
        }
    }
}