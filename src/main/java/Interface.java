
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;

import com.alee.laf.WebLookAndFeel;

/**
 * Created by advai on 7/8/2017.
 */
public class Interface {
    private JPanel mainPane;
    private JTextField textField1;
    private JList list1;
    private JTextArea textArea1;
    private JButton enterButton;
    private JButton button2;
    private JButton editButton;
    private static HashMap<String, String> configDict = new HashMap<String, String>();

    public Interface() {
        mainPane.setPreferredSize(new Dimension(800, 800));
        textField1.setPreferredSize(new Dimension(600, 30));
        final Firebase fb = new Firebase();
        //Add hacky console debugging interface to confuse user
        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(textField1.getText());
                String number = textField1.getText();
                if (isInteger(number)) {
                    fb.addMeetingDay(number, configDict.get("club"));
                    ((DefaultListModel) list1.getModel()).add(0, textField1.getText());
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a valid student number!");
                }


            }
        });
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

        String filePath = "config.ser";


        String line;
        BufferedReader reader = null;

        File f = new File("config.txt");
        try {
            reader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            String name = JOptionPane.showInputDialog(
                    "What is your club's name?", null);
            if (name == null) {
                throw new Error("User cancelled program.");
            }
            configDict.put("club", name);
            String[] values = {"No", "Yes"};

            Object selectedPaid = JOptionPane.showInputDialog(null, "Does your club have a membership fee?", "Selection", JOptionPane.DEFAULT_OPTION, null, values, "0");
            if (selectedPaid != null) {//null if the user cancels.
                String isPaid = selectedPaid.toString();
                if (isPaid == "Yes") {
                    configDict.put("paid", "true");
                } else {
                    configDict.put("paid", "false");
                }


                FileOutputStream fileOut = null;
                try {
                    fileOut = new FileOutputStream("config.ser");
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                ObjectOutputStream out = null;

                try {
                    out = new ObjectOutputStream(fileOut);
                    out.writeObject(configDict);
                    out.close();
                    fileOut.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                System.out.printf("Serialized data is saved in config.ser");
                for (String key : configDict.keySet()) {
                    System.out.println(key + ":" + configDict.get(key));
                }
            } else {
                throw new Error("User cancelled program.");
            }

            try {
                FileInputStream fileIn = new FileInputStream("config.ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                configDict = (HashMap<String, String>) in.readObject();
                in.close();
                fileIn.close();
                System.out.println("HEHREHE");
                for (String key : configDict.keySet()) {
                    System.out.println(key + ":" + configDict.get(key));
                }
            } catch (IOException i) {
                i.printStackTrace();
            } catch (ClassNotFoundException c) {
                c.printStackTrace();
            }

            // UI work inside Event Dispatch Thread (EDT) since it is best practice
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    // Install WebL&F as application L&F
                    WebLookAndFeel.install();

                    JFrame frame = new JFrame("Interface");
                    Interface ui = new Interface();
                    ui.list1.setModel(new DefaultListModel());
                    frame.setContentPane(ui.mainPane);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.pack();
                    frame.setVisible(true);
                }
            });

        }
    }
}
