public class Task {
    // Add two variables
    public static int addition(int x, int y) {
        return x + y;
    }
    
    // Multiply two variables
    public static int multiplication(int x, int y) {
        return x * y;
    }
    
    //Subtract two variables
    public static int subtraction(int x, int y) {
        return x - y;
    }

    public static int performTask(String operation, int x, int y) {
        switch(operation) {
            case "add": return addition(x, y);
            case "subtract": return subtraction(x, y);
            case "multiply": return multiplication(x, y);
            default: return -1;
        }
    }
}