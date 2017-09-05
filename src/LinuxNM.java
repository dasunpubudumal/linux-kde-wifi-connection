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
    private JButton btnCheckStatus;
    private JButton btnExit;
    static String output;
    static String error;
    static String response;
    static ImageIcon img;
    JFrame frame;

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
                JOptionPane.showMessageDialog(null,"This was created to resolve the issues " +
                        "in new KDE Plasma 5.9\nwhere the Network Monitor not being present.\n \nCreated by Dasun Pubudumal.\nDate: 5/09/2017","About",JOptionPane.QUESTION_MESSAGE);
            }
        });
        btnCheckStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand("nmcli -p g",true);
                displayOutput();
            }
        });
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
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
//                System.out.println(response);
                displayOutput();
                shellIn.close();
            }
        }

        catch (IOException e) {
//            System.out.println("Error occured while executing Linux command. Error Description: "
//                    + e.getMessage());
            displayError(e.getMessage());   //If there are any error, they will be displayed in the terminal output.
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

        img = new ImageIcon("./networking.png");

        JFrame frame = new JFrame("Linux Network Connector");
        frame.setSize(900,600);
        frame.setContentPane(new LinuxNM().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setIconImage(img.getImage());
        frame.setVisible(true);

    }

    public void initializeComponents(){
        insertSSID.setHorizontalAlignment(JTextField.CENTER);
        terminalOutput.setSize(800,100);
        terminalOutput.setFont(new Font("DejaVu Sans Mono", Font.BOLD, 16));
        terminalOutput.setEditable(false);
        terminalOutput.setForeground(Color.green);
        terminalOutput.setLineWrap(true);

    }
}
