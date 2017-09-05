import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LinuxNM {
    private JPanel panel1;
    private JButton connectButton;
    private JLabel lblTitle;
    private JLabel lblTerminalOutput;
    private JButton displayUUID;
    private JFormattedTextField formattedTextField1;
    private JTextField insertUUID;
    private JTextArea terminalOutput;
    private JLabel lblDescription;
    private JButton aboutButton;
    private JTextField txtPassword;
    static String output;
    static String error;

    public LinuxNM() {  //Constructor

        insertUUID.setHorizontalAlignment(JTextField.CENTER);
        terminalOutput.setSize(800,100);
        terminalOutput.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 16));
        terminalOutput.setEditable(false);

        displayUUID.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    executeCommand("nmcli d wifi");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                displayOutput();
            }
        });

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println(insertUUID.getText());
                    executeCommand("nmcli d wifi connect " + insertUUID.getText()+ " password " + txtPassword.getText().toString() + "");
//                    nmcli d wifi connect EEE password '12345678901234567890123456'

                    displayOutput();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

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
    }

    private void displayOutput(){

        if((output != "")){
            System.out.println("OUTPUT:\n".concat(output));
        }

        if((error != "")){
            System.out.println("ERROR!".concat(error));
        }
        terminalOutput.setText(output.concat("\n").concat(error));

    }

    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame("Linux Network Connector");
        frame.setSize(900,500);
        frame.setContentPane(new LinuxNM().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
