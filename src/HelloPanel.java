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

    private LoginForm loginForm;
    private Koszyk koszyk;

    public HelloPanel(JFrame parent, LoginForm loginForm, User user) {
        super(parent);
        this.koszyk = new Koszyk();
        this.loginForm = loginForm;

        setTitle("Welcome!");
        setContentPane(helloPanel);
        setMinimumSize(new Dimension(450,475));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        fieldName.setText("Welcome " + user.getNickname() + "!");

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
                nazwaGry.setText(resultSet.getString("nazwa_gry"));
                cena.setText(String.valueOf(resultSet.getDouble("cena")));
                String imgUrl = resultSet.getString("img");
                setImage(imgUrl);
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
            if (koszyk.getGames().equals(gameName)) {
                JOptionPane.showMessageDialog(helloPanel,
                        "Game already in cart",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        if (gameName.isEmpty()) {
            JOptionPane.showMessageDialog(helloPanel,
                    "No game selected",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double price = Double.parseDouble(cena.getText());
        Game game = new Game(nazwaGry.getText(),price,img.getText());
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
            return;
        } else {
            KoszykPanel koszykPanel = new KoszykPanel(null, koszyk, user);
            koszykPanel.setVisible(true);
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

    public void setLoginForm() {
        dispose();
        if (loginForm == null) {
            loginForm = new LoginForm(null);
        } else {
            loginForm.setVisible(true);
        }

    }


}