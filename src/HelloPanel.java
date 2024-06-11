import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;

public class HelloPanel extends JDialog {
    private JPanel helloPanel;
    private JLabel fieldName;
    private JButton dodajDoKoszykaButton;
    private JButton exiting;
    private JButton przejdzDoKoszykaButton;
    private JButton wyszukajGryButton;
    private JTextField wyszukajGry;
    private JLabel nazwaGry;
    private JLabel cena;
    private JLabel img;
    private JLabel StanKonta;
    private JTextField dodajCash;
    private JButton dodajPieniądzeButton;

    private String imgUrl;
    private LoginForm loginForm;
    private Koszyk koszyk;


    public HelloPanel(JFrame parent, LoginForm loginForm, User user) {
        super(parent);
        this.koszyk = new Koszyk();
        this.loginForm = loginForm;

        setTitle("Welcome!");
        setContentPane(helloPanel);
        setMinimumSize(new Dimension(700, 775));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        fieldName.setText("Welcome " + user.getNickname() + "!");
            setPortfelFromDatabase(user);



        StanKonta.setText("Stan konta: " + user.getPortfel().getStanKonta() );


        dodajPieniądzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cashToAddString = dodajCash.getText();
                try {
                    double cashToAdd = Double.parseDouble(cashToAddString);

                    addMoneyToAccount(user, cashToAdd);

                    user.getPortfel().setStanKonta(user.getPortfel().getStanKonta() + cashToAdd);

                    StanKonta.setText("Stan konta: " + user.getPortfel().getStanKonta());

                    JOptionPane.showMessageDialog(helloPanel,
                            "Added " + cashToAdd + " to your account",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (NumberFormatException ex) {

                    JOptionPane.showMessageDialog(helloPanel,
                            "Invalid amount",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        wyszukajGryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchGame();
            }
        });

        dodajDoKoszykaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 addToKoszyk(koszyk);
            }
        });

        przejdzDoKoszykaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               goToKoszyk(user);
            }
        });


        exiting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                setLoginForm();
            }
        });
        setVisible(true);

    }
    private void searchGame() {
        String gameName = wyszukajGry.getText();

        try {
            Connection connection = DriverManager.getConnection(User.DB_URL, User.USERNAME, User.PASSWORD);
            String query = "SELECT * FROM gra WHERE nazwa_gry LIKE ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, "%" + gameName + "%");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                imgUrl = resultSet.getString("img");
                setImage(imgUrl);
                nazwaGry.setText(resultSet.getString("nazwa_gry"));
                cena.setText(String.valueOf(resultSet.getDouble("cena")));
            } else {
                JOptionPane.showMessageDialog(helloPanel,
                        "Game not found",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void addToKoszyk(Koszyk koszyk){

        String gameName = nazwaGry.getText();


        if(gameName.isEmpty()){
            JOptionPane.showMessageDialog(helloPanel,
                    "No game selected",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(koszyk != null) {


            boolean gameExistsInCart = false;
            for (Game game : koszyk.getGames()) {
                if (game.getNazwaGry().equals(gameName)) {
                    gameExistsInCart = true;
                    break;
                }
            }

            if (gameExistsInCart) {
                JOptionPane.showMessageDialog(helloPanel,
                        "Game already in cart",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        double price = Double.parseDouble(cena.getText());
        Game game = new Game(nazwaGry.getText(), price, imgUrl);

        koszyk.addGame(game);

        JOptionPane.showMessageDialog(helloPanel,
                "Game added to cart",
                "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void goToKoszyk(User user) {
        dispose();
        if (koszyk == null) {
            JOptionPane.showMessageDialog(helloPanel,
                    "Cart is empty, add some games",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            KoszykPanel koszykPanel = new KoszykPanel(null, koszyk, user);
            koszykPanel.setVisible(true);
            dispose();
        }
    }


    private void setImage(String imgUrl) {
        try {
            URL url = new URL(imgUrl);
            BufferedImage originalImage = ImageIO.read(url);
            Image scaledImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledImage);
            img.setIcon(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void setPortfelFromDatabase(User user) {
        try (Connection connection = DriverManager.getConnection(User.DB_URL, User.USERNAME, User.PASSWORD)) {

            String portfelQuery = "SELECT * FROM Wallet WHERE player_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(portfelQuery)) {
                statement.setInt(1, user.getId());
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    double balance = resultSet.getDouble("balance");
                    user.getPortfel().setStanKonta(balance);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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