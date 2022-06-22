import java.io.BufferedReader;
import java.io.FileReader;
import java.net.Socket;
import java.io.IOException;

// reference: https://www.geeksforgeeks.org/multithreaded-servers-in-java/

// Multithreaded Client class
class Client {
    public static void main(String[] args)
    {
        System.out.println("Client started.");
            
        try {
            // Read the input file
            FileReader file = new FileReader("input.txt");
            BufferedReader buffer = new BufferedReader(file);

            // Create the dispatcher thread and pass the buffer to the dispatcher.
            (new Thread((Runnable) new DispatcherThread(buffer))).start(); 
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}