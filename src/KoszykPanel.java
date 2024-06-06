import javax.swing.*;
import java.awt.*;

public class KoszykPanel extends JDialog{
    private JPanel KoszykPanel;
    private JButton signInButton;
    private JButton BuyBtn;
    private JButton BtnCancel;
    private JLabel CardLabel;
    private JLabel price;

    public KoszykPanel(JFrame parent, Koszyk koszyk, User user) {
        super(parent);
        setTitle("Koszyk");
        setContentPane(KoszykPanel);
        setMinimumSize(new Dimension(450, 475));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }

}
