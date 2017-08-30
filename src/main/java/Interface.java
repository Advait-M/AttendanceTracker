import com.alee.laf.WebLookAndFeel;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.*;
import java.util.HashMap;

/**
 * Created by advai on 7/8/2017.
 */
public class Interface {
    private JPanel mainPane;
    private JTextField textField1;
    private JList list1;
    private JTextArea textArea1;
    private JButton enterButton;
    private JButton clearButton;
    private JButton editButton;
    private JLabel edit_number;
    private static HashMap<String, String> configDict = new HashMap<>();

    public Interface() {
        mainPane.setPreferredSize(new Dimension(800, 800));
        textField1.setPreferredSize(new Dimension(600, 30));

        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final Firebase fb = new Firebase();
        //Add hacky console debugging interface to confuse user
        textField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (textField1.getText().length() == 9) {
                    enterButton.doClick();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        enterButton.addActionListener(e -> {

            System.out.println(textField1.getText());
            String number = textField1.getText();
            if (isInteger(number)) {
                fb.addMeetingDay(number, configDict.get("club"));
                if (!((DefaultListModel) list1.getModel()).contains(number)) {
                    ((DefaultListModel) list1.getModel()).add(0, textField1.getText());
                    clearButton.doClick();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please enter a valid student number!");
                clearButton.doClick();

            }


        });
        clearButton.addActionListener(e -> SwingUtilities.invokeLater(() -> textField1.setText(null)));
        list1.addListSelectionListener(e -> edit_number.setText(list1.getSelectedValue().toString()));
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
        File f = new File("config.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            String name = JOptionPane.showInputDialog(
                    "What is your club's name?", null);
            if (name == null) {
                throw new Error("User cancelled program.");
            }
            configDict.put("club", name);
            String[] values = {"No", "Yes"};

            Object selectedPaid = JOptionPane.showInputDialog(null, "Does your club have a membership fee?", "Selection", JOptionPane.PLAIN_MESSAGE, null, values, "0");
            if (selectedPaid != null) {//null if the user cancels.
                String isPaid = selectedPaid.toString();
                if (isPaid.equalsIgnoreCase("Yes")) {
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
                ObjectOutputStream out;

                try {
                    out = new ObjectOutputStream(fileOut);
                    out.writeObject(configDict);
                    out.close();
                    if (fileOut != null) {
                        fileOut.close();
                    }
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
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
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
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });

    }

}

