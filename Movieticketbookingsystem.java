import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class MovieTicketBookingSystem {

    static String username = "user";
    static String password = "password";
    static String loggedInUser = "";
    static JFrame frame;
    static JPanel panel;
    static CardLayout cardLayout;

    static String selectedMovie = "";
    static int selectedSeats = 0;
    static int ticketPrice = 150;  // Default ticket price per seat
    static String seatSelection = "";
    static Map<String, Integer> moviePrices = new HashMap<>();
    static ArrayList<String> bookedSeats = new ArrayList<>();
    static JLabel priceLabel;
    static JLabel paymentDetailsLabel;

    public static void main(String[] args) {
        // Initialize movie prices
        moviePrices.put("Avengers: Endgame", 250);
        moviePrices.put("Spider-Man: No Way Home", 300);
        moviePrices.put("The Dark Knight", 200);
        moviePrices.put("Inception", 180);
        moviePrices.put("Titanic", 220);
        moviePrices.put("The Lion King", 150);

        frame = new JFrame("Movie Ticket Booking");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        panel = new JPanel(cardLayout);

        panel.add(loginPage(), "LoginPage");
        panel.add(movieListPage(), "MovieListPage");
        panel.add(seatSelectionPage(), "SeatSelectionPage");
        panel.add(paymentPage(), "PaymentPage");
        panel.add(ticketDetailsPage(), "TicketDetailsPage");

        frame.add(panel);
        frame.setVisible(true);
    }

    // Beautified Login Page
    public static JPanel loginPage() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(30, 30, 30));

        JLabel titleLabel = new JLabel("Welcome to Movie Ticket Booking");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        JTextField usernameField = new JTextField(15);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        JPasswordField passwordField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(0, 153, 0));
        loginButton.setForeground(Color.WHITE);

        loginButton.addActionListener(e -> {
            String enteredUsername = usernameField.getText();
            String enteredPassword = new String(passwordField.getPassword());

            if (enteredUsername.equals(username) && enteredPassword.equals(password)) {
                loggedInUser = enteredUsername;
                cardLayout.show(panel, "MovieListPage");
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password.");
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        loginPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        loginPanel.add(loginButton, gbc);

        return loginPanel;
    }

    // Beautified Movie List Page
    public static JPanel movieListPage() {
        JPanel moviePanel = new JPanel(new BorderLayout());
        moviePanel.setBackground(new Color(45, 45, 45));

        JLabel titleLabel = new JLabel("Select a Movie", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        gridPanel.setBackground(new Color(45, 45, 45));

        for (String movie : moviePrices.keySet()) {
            JButton movieButton = new JButton("<html><center>" + movie + "<br>₹" + moviePrices.get(movie) + "</center></html>");
            movieButton.setFont(new Font("Arial", Font.BOLD, 14));
            movieButton.setBackground(new Color(70, 130, 180));
            movieButton.setForeground(Color.WHITE);

            movieButton.addActionListener(e -> {
                selectedMovie = movie;
                ticketPrice = moviePrices.get(movie);  // Get the price of the selected movie
                cardLayout.show(panel, "SeatSelectionPage");
            });
            gridPanel.add(movieButton);
        }

        moviePanel.add(titleLabel, BorderLayout.NORTH);
        moviePanel.add(gridPanel, BorderLayout.CENTER);

        return moviePanel;
    }

    // Seat Selection Page
    public static JPanel seatSelectionPage() {
        JPanel seatPanel = new JPanel();
        seatPanel.setLayout(new GridLayout(6, 10, 5, 5));  // 60 seats in total

        ArrayList<JButton> seatButtons = new ArrayList<>();
        for (int i = 1; i <= 60; i++) {
            String seatLabel = "Seat " + i;
            JButton seatButton = new JButton(seatLabel);
            seatButton.setBackground(Color.GREEN);
            seatButton.addActionListener(e -> {
                if (seatButton.getBackground() == Color.GREEN) {
                    seatButton.setBackground(Color.RED);
                    bookedSeats.add(seatLabel);
                    selectedSeats++;
                } else {
                    seatButton.setBackground(Color.GREEN);
                    bookedSeats.remove(seatLabel);
                    selectedSeats--;
                }
                updatePriceLabel();
            });
            seatButtons.add(seatButton);
            seatPanel.add(seatButton);
        }

        priceLabel = new JLabel("Total Price: ₹" + (selectedSeats * ticketPrice));
        JButton proceedButton = new JButton("Proceed to Payment");
        proceedButton.addActionListener(e -> {
            if (selectedSeats == 0) {
                JOptionPane.showMessageDialog(frame, "Please select at least one seat.");
            } else {
                StringBuilder seatDetails = new StringBuilder();
                for (String seat : bookedSeats) {
                    seatDetails.append(seat).append(" ");
                }

                paymentDetailsLabel.setText("<html>Movie: " + selectedMovie +
                        "<br>Seats Booked: " + selectedSeats +
                        "<br>Seats: " + seatDetails.toString() +
                        "<br>Total Price: ₹" + (selectedSeats * ticketPrice) + "</html>");
                cardLayout.show(panel, "PaymentPage");
            }
        });

        seatPanel.add(proceedButton);
        seatPanel.add(priceLabel);

        return seatPanel;
    }

    // Payment Page
    public static JPanel paymentPage() {
        JPanel paymentPanel = new JPanel();
        paymentPanel.setLayout(new BoxLayout(paymentPanel, BoxLayout.Y_AXIS));

        paymentDetailsLabel = new JLabel("");
        JButton confirmButton = new JButton("Confirm Payment");

        confirmButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Payment Successful!");
            cardLayout.show(panel, "TicketDetailsPage");
        });

        paymentPanel.add(paymentDetailsLabel);
        paymentPanel.add(confirmButton);

        return paymentPanel;
    }

    // Ticket Details Page
    public static JPanel ticketDetailsPage() {
        JPanel ticketPanel = new JPanel();
        ticketPanel.setLayout(new BoxLayout(ticketPanel, BoxLayout.Y_AXIS));

        JLabel ticketLabel = new JLabel();
        JButton backButton = new JButton("Back to Movie List");

        backButton.addActionListener(e -> {
            selectedSeats = 0;
            bookedSeats.clear();
            cardLayout.show(panel, "MovieListPage");
        });

        ticketPanel.add(ticketLabel);
        ticketPanel.add(backButton);

        return ticketPanel;
    }

    // Update price label based on the number of seats selected
    public static void updatePriceLabel() {
        priceLabel.setText("Total Price: ₹" + (selectedSeats * ticketPrice));
    }
}