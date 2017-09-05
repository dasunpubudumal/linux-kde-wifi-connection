import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class LinuxNM {
    private JPanel panel1;
    private JButton connectButton;
    private JLabel lblTitle;
    private JLabel lblTerminalOutput;
    private JButton displayUUID;
    private JFormattedTextField formattedTextField1;
    private JTextField insertSSID;
    private JTextArea terminalOutput;
    private JLabel lblDescription;
    private JButton aboutButton;
    private JTextField txtPassword;
    static String output;
    static String error;
    static String response;

    public LinuxNM() {  //Constructor

        initializeComponents(); //Initializes form components.

        displayUUID.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand("nmcli d wifi", true);
                displayOutput();
            }
        });

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                    /
                    //SString value = " \"ROM\" ";
                    executeCommand("nmcli d wifi connect \"" + insertSSID.getText() +  "\" password " + txtPassword.getText(), true);
//                    nmcli d wifi connect EEE password '12345678901234567890123456'
            }
        });
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public String executeCommand(String command, boolean waitForResponse) {

        response = "";

        ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
        pb.redirectErrorStream(true);

//        System.out.println("Linux command: " + command);
        try {
            Process shell = pb.start();

            if (waitForResponse) {
                // To capture output from the shell
                InputStream shellIn = shell.getInputStream();

                // Wait for the shell to finish and get the return code
                int shellExitStatus = shell.waitFor();
//                System.out.println("Exit status" + shellExitStatus);

                response = convertStreamToStr(shellIn);
                System.out.println(response);
                displayOutput();
                shellIn.close();
            }

        }

        catch (IOException e) {
//            System.out.println("Error occured while executing Linux command. Error Description: "
//                    + e.getMessage());
            displayError(e.getMessage());
        }

        catch (InterruptedException e) {
//            System.out.println("Error occured while executing Linux command. Error Description: "
//                    + e.getMessage());
            displayError(e.getMessage());
        }

        return response;
    }

    public static String convertStreamToStr(InputStream is) throws IOException {

        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        }
        else {
            return "";
        }
    }

    private void displayOutput(){
        terminalOutput.setText(response);
    }

    private void displayError(String error){
        terminalOutput.setText(error);
    }

    public static void main(String[] args) throws IOException {

        JFrame frame = new JFrame("Linux Network Connector");
        frame.setSize(900,500);
        frame.setContentPane(new LinuxNM().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    public void initializeComponents(){
        insertSSID.setHorizontalAlignment(JTextField.CENTER);
        terminalOutput.setSize(800,100);
        terminalOutput.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 16));
        terminalOutput.setEditable(false);
    }
}
