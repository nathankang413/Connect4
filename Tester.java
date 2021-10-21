import java.util.*;
import java.io.*;

public class Tester {
    
    public static void main (String[] args) throws IOException {

        PrintWriter write = new PrintWriter("test.txt");
        // Scanner read = new Scanner(new File("test.txt"));
        BufferedReader read = new BufferedReader(new FileReader("test.txt"));
        StringBuilder str = new StringBuilder();

        String line = read.readLine();

        System.out.println(line == null);

        while(line != null) {
            System.out.println(line);
            str.append(line + "\n");
            str.append(line + 1 + "\n");
            line = read.readLine();
        }

        // write.print(str.toString());
        // write.close();

    }

}
