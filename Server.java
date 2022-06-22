import java.net.ServerSocket;
import java.io.IOException;

// reference: https://www.geeksforgeeks.org/multithreaded-servers-in-java/

// Multithreaded Server class
class Server {
    public static void main(String[] args)
    {
        ServerSocket server = null;
        System.out.println("Server started.");
  
        try {
            // server is listening on port 1234
            server = new ServerSocket(1234);
  
            //Create the dispatcher thread and pass the socket to the dispatcher.
            (new Thread((Runnable) new DispatcherThread(server))).start(); 
        }
        catch (IOException e) {
            e.printStackTrace();

            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException f) {
                    f.printStackTrace();
                }
            }
        }
    }
}