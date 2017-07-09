import javax.swing.*;

/**
 * Created by advai on 7/8/2017.
 */
public class Interface {
    private JTextArea helloTextArea;
    private JPanel mainPane;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Interface");
        frame.setContentPane(new Interface().mainPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
