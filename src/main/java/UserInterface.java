import javax.swing.*;
import java.awt.*;
/**
 * Created by advai on 7/9/2017.
 */
public class UserInterface extends JFrame {
    private JPanel mainPane;
    private JTextField entryField;
    private JList listResults;
    private JTextArea detailsArea;
    private JButton entryButton;
    private JButton nameButton;
    private JButton editButton;

    public UserInterface() {
        setSize(500, 500);
        JPanel mainPane = new JPanel();
        mainPane.setSize(500, 500);
        JLabel test = new JLabel("asda");
        JTextField entryField = new JTextField();
        entryField.setPreferredSize(new Dimension(400, 100));
        mainPane.add(test);
        mainPane.add(entryField);

        add(mainPane);
    }

    public static void main(String[] args) {
        UserInterface ui = new UserInterface();
        ui.setVisible(true);
    }
}
