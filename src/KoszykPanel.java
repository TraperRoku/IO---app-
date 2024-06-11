import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class KoszykPanel extends JDialog {
    private JPanel KoszykPanel;
    private JButton outButton;
    private JButton BuyBtn;
    private JButton BtnCancel;
    private JLabel price;
    private JLabel nameGame;
    private JLabel StanKonta;
    private JTextField dodajCash;
    private JButton dodajPieniądzeButton;
    private JLabel icon;

    public KoszykPanel(JFrame parent, Koszyk koszyk, User user) {
        super(parent);
        setTitle("Koszyk");
        setContentPane(KoszykPanel);
        setMinimumSize(new Dimension(600, 775));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        icon.setIcon(new ImageIcon("src/icons/cart.png"));
        StanKonta.setText("Stan konta: " + user.getPortfel().getStanKonta());

        final double[] cena = {0};

        BuyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (user.getPortfel().getStanKonta() >= cena[0]) {
                    JOptionPane.showMessageDialog(KoszykPanel,
                            "You bought the games",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    user.getPortfel().setStanKonta(user.getPortfel().getStanKonta() - cena[0]);
                    StanKonta.setText("Stan konta: " + user.getPortfel().getStanKonta());
                    koszyk.getGames().clear();
                    nameGame.setText("");
                    price.setText("");

                    System.exit(0);
                } else {
                    JOptionPane.showMessageDialog(KoszykPanel,
                            "Not enough money",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        dodajPieniądzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cashToAddString = dodajCash.getText();
                try {
                    double cashToAdd = Double.parseDouble(cashToAddString);

                    addMoneyToAccount(user, cashToAdd);

                    user.getPortfel().setStanKonta(user.getPortfel().getStanKonta() + cashToAdd);

                    StanKonta.setText("Stan konta: " + user.getPortfel().getStanKonta());

                    JOptionPane.showMessageDialog(KoszykPanel,
                            "Added " + cashToAdd + " to your account",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(KoszykPanel,
                            "Invalid amount",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        StringBuilder gameNames = new StringBuilder();
        for (Game game : koszyk.getGames()) {
            gameNames.append(game.getNazwaGry()).append(", ");
            cena[0] += game.getCena();
        }

        String names = gameNames.toString();
        if (!names.isEmpty()) {
            names = names.substring(0, names.length() - 2);
        }
        nameGame.setText(names);
        price.setText(String.valueOf(cena[0]));
    }

    private void addMoneyToAccount(User user, double amount) {
        try {
            Connection connection = DriverManager.getConnection(User.DB_URL, User.USERNAME, User.PASSWORD);

            String updateQuery = "UPDATE Wallet SET balance = balance + ? WHERE player_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
                statement.setDouble(1, amount);
                statement.setInt(2, user.getId());
                statement.executeUpdate();
            }

            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
