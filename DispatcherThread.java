import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

// reference: https://github.com/krimamshah/DispatcherWorkerModel

public class DispatcherThread extends Thread {
    private ServerSocket server;
    private int clientId = 0;
    private boolean isServer = true;
    private BufferedReader buffer;
    // we define a dipatcher thread with 5 worker threads only. (for demonstration)
    private ExecutorService executor = Executors.newFixedThreadPool(5);

    public DispatcherThread(ServerSocket serverSocket) {
          this.server = serverSocket;    
    }

    public DispatcherThread(BufferedReader buffer) {
          this.buffer = buffer;
          this.isServer = false;    
    }

    private void runServer() throws IOException {
        while (true) {
            // socket object to receive incoming client
            // requests
            Socket client = server.accept();

            // Displaying that new client is connected
            // to server
            System.out.printf(
                "New client connected %s, assigned ClientID-%d.\n", 
                client.getInetAddress().getHostName(), 
                clientId
                );

            // create a new thread object
            WorkerThread worker = new WorkerThread(client, clientId);

            clientId++;

            // This thread will handle the client
            // separately
            executor.execute(worker);
        }
    }

    private void runClient() throws IOException, InterruptedException {
        String line = buffer.readLine();

        // assign tasks to thread as long as data in the queue
        while(line != null){
            Thread.sleep(1000);           
            WorkerThread worker = new WorkerThread(line);  //create the worker thread
                       
            executor.execute(worker);   //execute the worker thread
                        
            line = buffer.readLine();
        }

        executor.shutdown(); //initiates shutdown in which previously submitted tasks are executed, but no new tasks will be accepted
    
        //Blocks until all tasks have completed execution after a shutdown request, 
        //or the timeout occurs, or the current thread is interrupted, whichever happens first
        executor.awaitTermination(5000, TimeUnit.MILLISECONDS);

        System.out.println("All tasks done!");

        return;
    }

    public void run() {
        try {
            if(isServer) 
                runServer();
            else
                runClient();
        }
        catch(InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}