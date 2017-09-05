import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LinuxNM {
    private JPanel panel1;
    private JButton button1;
    static String output;
    static String error;

    private static void executeCommand(String command) throws IOException{

        output = "";
        error = "";

        Process proc = Runtime.getRuntime().exec(command);  //Runs the command.

        BufferedReader stdOut = new BufferedReader(new
                InputStreamReader(proc.getInputStream())); //Reads the output for the command.

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

        // read the output from the command
        String o = null;
        String e = null;

        while ((o = stdOut.readLine()) != null) {
            output  = output.concat(o) + "\n";
            //System.out.println(o);;
        }

        // read any errors from the attempted command
        //System.out.println("Here is the standard error of the command (if any):\n");
        while ((e = stdError.readLine()) != null) {
            error = error.concat(e);
            //System.out.println(e);
        }

        if((output != "")){
            System.out.println("OUTPUT:\n".concat(output));
        }

        if((error != "")){
            System.out.println("ERROR!".concat(error));
        }

    }

    public static void main(String[] args) throws IOException {

        executeCommand("nmcli c");


    }
}
