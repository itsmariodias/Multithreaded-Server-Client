import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Random;
import java.net.Socket;

// reference: https://github.com/krimamshah/DispatcherWorkerModel

// WorkerThread class
public class WorkerThread implements Runnable {
    private Socket clientSocket = null;
    private int clientId;
    private boolean isServer = true;
    private String msg, currentThread;
    private PrintWriter output = null;
    private BufferedReader input = null;

    // Constructor
    public WorkerThread(Socket socket, int clientId) {
        this.clientSocket = socket;
        this.clientId = clientId;
    }

    public WorkerThread(String line) {
        this.msg = line;
        this.isServer = false;
    }

    public void runServer() throws IOException, InterruptedException {
        // log the client id and assigned worker thread
        System.out.printf(
            "ClientID-%d assigned to %s\n", 
            clientId, 
            currentThread
            );

        Random rand = new Random();
        
        // get the outputstream of client
        output = new PrintWriter(clientSocket.getOutputStream(), true);

        // get the inputstream of client
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String task;
        while (true) {
            task = input.readLine();

            // worker sleeps if there is no input
            if(task == null) {
                Thread.sleep(1000);
                wait();
            }

            if(task.equals("exit")) {
                // worker is done with task so we can exit safely.
                System.out.printf("(%s) task finished.\n", currentThread);
                return;
            }

            // split the line
            String[] taskSplit = task.split(" ");
            String operation = taskSplit[0];
            int x = Integer.parseInt(taskSplit[1]);
            int y = Integer.parseInt(taskSplit[2]);

            // perform operation based on given task
            int result = Task.performTask(operation, x, y);
            Thread.sleep(3000);
            output.println(result);
        }
    }

    public void runClient() throws IOException, InterruptedException {
        // log the client id and assigned worker thread
        System.out.printf(
            "Task assigned to %s\n", 
            currentThread
            );
        Socket socket = new Socket("localhost", 1234);

        // writing to server
        output = new PrintWriter(socket.getOutputStream(), true);

        // reading from server
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        System.out.printf("(%s) Sent: %s\n", currentThread, msg);
                
        output.println(msg);
        output.flush();
  
        // displaying server reply
        System.out.printf("(%s) Server replied: %s \n", currentThread, input.readLine());

        output.println("exit");
        output.flush();
    }

    public void run()
    {
        currentThread = Thread.currentThread().getName();
        try {
            if(isServer) 
                runServer();
            else
                runClient();
        }
        catch (IOException | InterruptedException e) {
            if(isServer)
                System.out.printf("ClientID-%d disconnected.\n", clientId);
            else
                System.out.printf("Server disconnected.\n");
        }
        finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (input != null) {
                    input.close();
                    if(isServer)
                        clientSocket.close();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}