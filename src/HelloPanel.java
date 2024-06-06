import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelloPanel extends JDialog {
    private JPanel helloPanel;
    private JLabel fieldName;
    private JButton buyGamesButton;
    private JButton exiting;
    private JButton changeMeButton;
    private LoginForm loginForm;

    public HelloPanel(JFrame parent, LoginForm loginForm, User user) {
        super(parent);
        this.loginForm = loginForm;

        setTitle("Welcome!");
        setContentPane(helloPanel);
        setMinimumSize(new Dimension(450,475));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        fieldName.setText("Welcome " + user.getNickname() + "!");

        exiting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                setLoginForm();
            }
        });

    }

    public void setLoginForm() {
        dispose();
        if (loginForm == null) {
            loginForm = new LoginForm(null);
        } else {
            loginForm.setVisible(true);
        }

    }


}