import com.alee.laf.WebLookAndFeel;
import com.sun.xml.internal.ws.util.StringUtils;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Interface {
    private JPanel mainPane;
    private JTextField textField1;
    private JButton enterButton;
    private JButton clearButton;
    private JTable table1;
    private static HashMap<String, String> configDict = new HashMap<>();
    private static ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
    private final Firebase fb = new Firebase();

    public Interface() {
        mainPane.setPreferredSize(new Dimension(800, 800));
        textField1.setPreferredSize(new Dimension(600, 30));
        table1.setModel(new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.addColumn("Student Number");

        if (configDict.get("paid").equals("true")) {
            model.addColumn("Paid status");

            class CustomPopup extends JPopupMenu {
                JMenuItem setPaid;

                public CustomPopup(int row) {
                    setPaid = new JMenuItem(new AbstractAction("Set Paid") {
                        @Override
                        public void actionPerformed(ActionEvent ae) {

                            DefaultTableModel model = (DefaultTableModel) table1.getModel();
                            System.out.println("print");
                            System.out.println(model.getValueAt(row, 1));
                            if (model.getValueAt(row, 1) == "Not Paid") {
                                System.out.println((String) model.getValueAt(row, 0));
                                System.out.println(fb.setPaid((String) model.getValueAt(row, 0), configDict.get("club")));
                                model.setValueAt("Paid", row, 1);
                            }
                        }
                    });
                    add(setPaid);
                }
            }

            table1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        onClickEvent(e);
                    }
                }

                // Mac OS X support
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        onClickEvent(e);
                    }
                }

                private void onClickEvent(MouseEvent e) {
                    int r = table1.rowAtPoint(e.getPoint());
                    if (r >= 0 && r < table1.getRowCount()) {
                        table1.setRowSelectionInterval(r, r);
                    } else {
                        table1.clearSelection();
                    }

                    int rowIndex = table1.getSelectedRow();
                    // TODO: NOT SURE if >= is needed (investigate @Advait/Leon)
                    if (rowIndex < 0 || r >= table1.getRowCount())
                        return;
                    if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
                        CustomPopup popup = new CustomPopup(rowIndex);
                        popup.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            });
        }


        textField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (textField1.getText().length() == 9) {
                    //enterButton.doClick();
                    addStudent();
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
            addStudent();


        });
        clearButton.addActionListener(e -> SwingUtilities.invokeLater(() -> textField1.setText(null)));
        table1.getSelectionModel().addListSelectionListener(e -> table1.getValueAt(table1.getSelectedRow(), 0));
        startQueue();
    }
    private void startQueue() {
        new Thread (() -> {
            while (true) {

                if (!queue.isEmpty()) {

                    String number = queue.poll();
                    int ret = fb.addMeetingDay(number, configDict.get("club"));

                    if (ret == 1) {
                        JOptionPane.showMessageDialog(null, "Invalid Firebase data or connection to Firebase. Please contact developers.");
                        throw new Error("Terminated program due to invalid connection to Firebase or invalid data in Firebase.");
                    }
                    if (ret == 2) {
                        JOptionPane.showMessageDialog(null, "User is already signed in to " + configDict.get("club") + " today! Cannot sign in again.");
                    }
                    if (!checkTableExists(number) && ret != 2) {
                        DefaultTableModel model = (DefaultTableModel) table1.getModel();
                        if (configDict.get("paid").equals("true")) {
                            boolean paid = fb.getPaid(number, configDict.get("club"));
                            String paidStatus;
                            if (paid) {
                                paidStatus = "Paid";
                            } else {
                                paidStatus = "Not Paid";
                            }
                            model.insertRow(0, new Object[]{number, paidStatus});
                        } else {
                            model.insertRow(0, new Object[]{number});
                        }
                    }
                }

            }
        }).start();
    }
    private void addStudent() {
        System.out.println(textField1.getText());
        String number = textField1.getText();
        if (isInteger(number)) {
            queue.add(number);
//            new Thread (() -> {
//                int ret = fb.addMeetingDay(number, configDict.get("club"));
//
//                System.out.println("STUFF");
//                System.out.println(ret);
//                //            if (!((DefaultListModel) table1.getModel()).contains(number)) {
//                //                ((DefaultListModel) table1.getModel()).add(0, textField1.getText());
//                //            }
//                if (ret == 1) {
//                    JOptionPane.showMessageDialog(null, "Invalid Firebase data or connection to Firebase. Please contact developers.");
//                    throw new java.lang.Error("Terminated program due to invalid connection to Firebase or invalid data in Firebase.");
//                }
//                if (ret == 2) {
//                    JOptionPane.showMessageDialog(null, "User is already signed in to " + configDict.get("club") + " today! Cannot sign in again.");
//                }
//                if (!checkTableExists(number) && ret != 2) {
//                    DefaultTableModel model = (DefaultTableModel) table1.getModel();
//                    if (configDict.get("paid").equals("true")) {
//                        boolean paid = fb.getPaid(number, configDict.get("club"));
//                        String paidStatus;
//                        if (paid) {
//                            paidStatus = "Paid";
//                        } else {
//                            paidStatus = "Not Paid";
//                        }
//                        model.insertRow(0, new Object[]{number, paidStatus});
//                    } else {
//                        model.insertRow(0, new Object[]{number});
//                    }
//                }
//            }).start();

        } else {
            JOptionPane.showMessageDialog(null, "Please enter a valid student number!");


        }
        clearButton.doClick();
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

    private boolean checkTableExists(String number) {
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        List<String> numdata = new ArrayList<String>();
        for (int count = 0; count < model.getRowCount(); count++) {
            numdata.add(model.getValueAt(count, 0).toString());
        }
        return numdata.contains(number);
    }

    public static void main(String[] args) {

//        String filePath = "config.ser";
//        File f = new File("config.txt");
        try {
            FileInputStream fileIn = new FileInputStream("config.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            configDict = (HashMap<String, String>) in.readObject();
            in.close();
            fileIn.close();

        } catch (IOException | ClassNotFoundException e) {
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

            } else {
                throw new Error("User cancelled program.");
            }
        }

        // UI work inside Event Dispatch Thread (EDT) since it is best practice
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Install WebL&F as application L&F
                WebLookAndFeel.install();

                JFrame frame = new JFrame(StringUtils.capitalize(configDict.get("club")) + " Attendance");
                Interface ui = new Interface();
                frame.setContentPane(ui.mainPane);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);

            }
        });

    }

}

