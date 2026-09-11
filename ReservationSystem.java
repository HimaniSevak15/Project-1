import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
public class ReservationSystem extends JFrame {

    
    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/reservation_db?createDatabaseIfNotExist=true&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // Change if your MySQL has a password

    private Connection connection;
    private String loggedInUser;
    private static final String[] TRAIN_NUMBERS = {
            "12901", "12902", "12903", "12904", "19011", "19012",
            "12009", "12010", "22953", "22954"
    };

    private static final String[] TRAIN_NAMES = {
            "Gujarat Mail", "Gujarat Mail Return", "Golden Temple Mail",
            "Golden Temple Mail Return", "Gujarat Express",
            "Gujarat Express Return", "Mumbai Central Shatabdi",
            "Mumbai Central Shatabdi Return", "Mumbai Ahmedabad Intercity",
            "Mumbai Ahmedabad Intercity Return"
    };
    private final Color PRIMARY = new Color(30, 90, 160);
    private final Color SUCCESS = new Color(30, 130, 70);
    private final Color DANGER = new Color(190, 50, 50);

    public ReservationSystem() {
        setTitle("Online Reservation System");
        setSize(850, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        connectDatabase();
        showLoginScreen();
    }
    private void connectDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(
                    DB_URL, DB_USER, DB_PASSWORD);

            createTables();
            insertDemoUser();

        } catch (ClassNotFoundException e) {
            showError("MySQL JDBC Driver not found.\n"
                    + "Add MySQL Connector/J to your project.");
        } catch (SQLException e) {
            showError("Database connection failed.\n\n"
                    + e.getMessage()
                    + "\n\nCheck MySQL server, username and password.");
        }
    }

    private void createTables() throws SQLException {

        String usersTable =
                "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(50) UNIQUE NOT NULL," +
                "password VARCHAR(100) NOT NULL)";

        String reservationsTable =
                "CREATE TABLE IF NOT EXISTS reservations (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "pnr VARCHAR(20) UNIQUE NOT NULL," +
                "username VARCHAR(50) NOT NULL," +
                "passenger_name VARCHAR(100) NOT NULL," +
                "train_number INT NOT NULL," +
                "train_name VARCHAR(100) NOT NULL," +
                "class_type VARCHAR(30) NOT NULL," +
                "journey_date DATE NOT NULL," +
                "source_station VARCHAR(100) NOT NULL," +
                "destination_station VARCHAR(100) NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";

        try (Statement st = connection.createStatement()) {
            st.executeUpdate(usersTable);
            st.executeUpdate(reservationsTable);
        }
    }

    private void insertDemoUser() {
        String sql = "INSERT IGNORE INTO users(username, password) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "admin");
            ps.setString(2, "admin123");
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Demo user setup error: " + e.getMessage());
        }
    }
    private void showLoginScreen() {
        getContentPane().removeAll();

        JPanel main = new JPanel(new GridBagLayout());
        main.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(30, 40, 30, 40)));

        JLabel title = new JLabel("ONLINE RESERVATION SYSTEM");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(PRIMARY);

        JLabel subtitle = new JLabel("Login to continue");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");

        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(PRIMARY);
        loginButton.setForeground(Color.WHITE);

        JLabel demo = new JLabel("Demo: admin / admin123");
        demo.setForeground(Color.GRAY);

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        g.gridx = 0; g.gridy = 0; g.gridwidth = 2;
        card.add(title, g);

        g.gridy++;
        card.add(subtitle, g);

        g.gridwidth = 1;
        g.gridy++;
        g.gridx = 0;
        card.add(userLabel, g);
        g.gridx = 1;
        card.add(usernameField, g);

        g.gridy++;
        g.gridx = 0;
        card.add(passLabel, g);
        g.gridx = 1;
        card.add(passwordField, g);

        g.gridy++;
        g.gridx = 0; g.gridwidth = 2;
        card.add(loginButton, g);

        g.gridy++;
        card.add(demo, g);

        main.add(card);
        add(main);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                showWarning("Please enter username and password.");
                return;
            }

            if (validateLogin(username, password)) {
                loggedInUser = username;
                showReservationScreen();
            } else {
                showError("Access denied!\nInvalid username or password.");
            }
        });

        passwordField.addActionListener(e -> loginButton.doClick());

        revalidate();
        repaint();
    }

    private boolean validateLogin(String username, String password) {
        if (connection == null) {
            showError("Database is not connected.");
            return false;
        }

        String sql = "SELECT id FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            showError("Login error: " + e.getMessage());
            return false;
        }
    }

    // ================= RESERVATION FORM =================
    private void showReservationScreen() {
        getContentPane().removeAll();

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel title = new JLabel("ONLINE RESERVATION SYSTEM");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(PRIMARY);

        JLabel loggedUser = new JLabel("Logged in as: " + loggedInUser);
        loggedUser.setForeground(Color.DARK_GRAY);

        JPanel top = new JPanel(new BorderLayout());
        top.add(title, BorderLayout.WEST);
        top.add(loggedUser, BorderLayout.EAST);

        main.add(top, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Reservation Form"));

        JTextField passengerField = new JTextField(20);
        JTextField trainNumberField = new JTextField(20);
        JTextField trainNameField = new JTextField(20);
        trainNameField.setEditable(false);
        trainNameField.setBackground(new Color(240, 240, 240));

        JTextField dateField = new JTextField(20);
        dateField.setToolTipText("Format: yyyy-MM-dd");

        JTextField sourceField = new JTextField(20);
        JTextField destinationField = new JTextField(20);

        JComboBox<String> classBox = new JComboBox<>(
                new String[]{"Sleeper", "AC 3 Tier", "AC 2 Tier", "AC First Class", "Chair Car"}
        );

        JButton bookButton = new JButton("Insert / Book");
        JButton fetchButton = new JButton("Fetch Booking");
        JButton cancelButton = new JButton("Cancel Booking");
        JButton logoutButton = new JButton("Logout");

        bookButton.setBackground(SUCCESS);
        bookButton.setForeground(Color.WHITE);

        fetchButton.setBackground(PRIMARY);
        fetchButton.setForeground(Color.WHITE);

        cancelButton.setBackground(DANGER);
        cancelButton.setForeground(Color.WHITE);

        // Train name auto-population
        trainNumberField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String trainNo = trainNumberField.getText().trim();
                trainNameField.setText(getTrainName(trainNo));
            }
        });

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(7, 7, 7, 7);
        g.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        addField(form, g, row++, "Passenger Name:", passengerField);
        addField(form, g, row++, "Train Number:", trainNumberField);
        addField(form, g, row++, "Train Name:", trainNameField);
        addField(form, g, row++, "Class Type:", classBox);
        addField(form, g, row++, "Journey Date:", dateField);
        addField(form, g, row++, "Source Station:", sourceField);
        addField(form, g, row++, "Destination Station:", destinationField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.add(bookButton);
        buttonPanel.add(fetchButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(logoutButton);

        g.gridx = 0;
        g.gridy = row;
        g.gridwidth = 2;
        form.add(buttonPanel, g);

        main.add(form, BorderLayout.CENTER);

        JLabel note = new JLabel(
                "Enter date in yyyy-MM-dd format. Example: 2026-09-20"
        );
        note.setForeground(Color.GRAY);

        main.add(note, BorderLayout.SOUTH);

        add(main);
        bookButton.addActionListener(e -> {
            String passenger = passengerField.getText().trim();
            String trainNoText = trainNumberField.getText().trim();
            String trainName = trainNameField.getText().trim();
            String classType = (String) classBox.getSelectedItem();
            String journeyDate = dateField.getText().trim();
            String source = sourceField.getText().trim();
            String destination = destinationField.getText().trim();

            // Basic input validation
            if (passenger.isEmpty() ||
                    trainNoText.isEmpty() ||
                    journeyDate.isEmpty() ||
                    source.isEmpty() ||
                    destination.isEmpty()) {

                showWarning("Please fill all required fields.");
                return;
            }

            if (!trainNoText.matches("\\d+")) {
                showWarning("Train number must be numeric.");
                return;
            }

            if (source.equalsIgnoreCase(destination)) {
                showWarning("Source and destination cannot be the same.");
                return;
            }

            if (!isValidDate(journeyDate)) {
                showWarning("Invalid date.\nUse format yyyy-MM-dd.");
                return;
            }

            if (trainName.isEmpty()) {
                showWarning("Train number not found in the sample train list.");
                return;
            }

            String pnr = generatePNR();

            String sql =
                    "INSERT INTO reservations " +
                    "(pnr, username, passenger_name, train_number, train_name, " +
                    "class_type, journey_date, source_station, destination_station) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setString(1, pnr);
                ps.setString(2, loggedInUser);
                ps.setString(3, passenger);
                ps.setInt(4, Integer.parseInt(trainNoText));
                ps.setString(5, trainName);
                ps.setString(6, classType);
                ps.setDate(7, Date.valueOf(journeyDate));
                ps.setString(8, source);
                ps.setString(9, destination);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(
                        this,
                        "Reservation successful!\n\n"
                                + "PNR Number: " + pnr
                                + "\nPassenger: " + passenger
                                + "\nTrain: " + trainName
                                + "\nJourney Date: " + journeyDate,
                        "Booking Confirmation",
                        JOptionPane.INFORMATION_MESSAGE
                );

                clearForm(passengerField, trainNumberField, trainNameField,
                        dateField, sourceField, destinationField);

            } catch (SQLException ex) {
                showError("Reservation failed: " + ex.getMessage());
            }
        });

        // ================= FETCH BOOKING =================
        fetchButton.addActionListener(e -> showFetchDialog());

        // ================= CANCEL BOOKING =================
        cancelButton.addActionListener(e -> showCancelDialog());

        // ================= LOGOUT =================
        logoutButton.addActionListener(e -> {
            loggedInUser = null;
            showLoginScreen();
        });

        revalidate();
        repaint();
    }

    private void addField(JPanel panel, GridBagConstraints g,
                          int row, String label, Component component) {

        g.gridwidth = 1;
        g.gridx = 0;
        g.gridy = row;
        panel.add(new JLabel(label), g);

        g.gridx = 1;
        panel.add(component, g);
    }

    private String getTrainName(String trainNumber) {
        for (int i = 0; i < TRAIN_NUMBERS.length; i++) {
            if (TRAIN_NUMBERS[i].equals(trainNumber)) {
                return TRAIN_NAMES[i];
            }
        }
        return "";
    }

    private void showFetchDialog() {

        JTextField pnrField = new JTextField(15);

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("Enter PNR Number:"));
        panel.add(pnrField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Fetch Booking",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String pnr = pnrField.getText().trim();

        if (pnr.isEmpty()) {
            showWarning("Please enter a PNR number.");
            return;
        }

        String sql =
                "SELECT pnr, passenger_name, train_number, train_name, " +
                "class_type, journey_date, source_station, destination_station " +
                "FROM reservations WHERE pnr = ? AND username = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, pnr);
            ps.setString(2, loggedInUser);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    String details =
                            "PNR Number       : " + rs.getString("pnr") + "\n"
                            + "Passenger Name   : " + rs.getString("passenger_name") + "\n"
                            + "Train Number     : " + rs.getInt("train_number") + "\n"
                            + "Train Name       : " + rs.getString("train_name") + "\n"
                            + "Class Type       : " + rs.getString("class_type") + "\n"
                            + "Journey Date     : " + rs.getDate("journey_date") + "\n"
                            + "Source Station   : " + rs.getString("source_station") + "\n"
                            + "Destination      : " + rs.getString("destination_station");

                    JTextArea area = new JTextArea(details);
                    area.setEditable(false);
                    area.setFont(new Font("Monospaced", Font.PLAIN, 14));

                    JOptionPane.showMessageDialog(
                            this,
                            new JScrollPane(area),
                            "Booking Details",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                } else {
                    showWarning("No booking found for this PNR.");
                }
            }

        } catch (SQLException ex) {
            showError("Fetch failed: " + ex.getMessage());
        }
    }
    private void showCancelDialog() {

        JTextField pnrField = new JTextField(15);

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("Enter PNR Number:"));
        panel.add(pnrField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Cancel Booking",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String pnr = pnrField.getText().trim();

        if (pnr.isEmpty()) {
            showWarning("Please enter a PNR number.");
            return;
        }

        String findSql =
                "SELECT passenger_name, train_name, journey_date " +
                "FROM reservations WHERE pnr = ? AND username = ?";

        try (PreparedStatement ps = connection.prepareStatement(findSql)) {

            ps.setString(1, pnr);
            ps.setString(2, loggedInUser);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    showWarning("No booking found for this PNR.");
                    return;
                }

                String message =
                        "Passenger: " + rs.getString("passenger_name")
                        + "\nTrain: " + rs.getString("train_name")
                        + "\nJourney Date: " + rs.getDate("journey_date")
                        + "\n\nAre you sure?";

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        message,
                        "Confirm Cancellation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    deleteBooking(pnr);
                }
            }

        } catch (SQLException ex) {
            showError("Cancellation check failed: " + ex.getMessage());
        }
    }

    private void deleteBooking(String pnr) {

        String sql =
                "DELETE FROM reservations WHERE pnr = ? AND username = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, pnr);
            ps.setString(2, loggedInUser);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Booking cancelled successfully.\nPNR: " + pnr,
                        "Cancellation Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                showWarning("Booking could not be cancelled.");
            }

        } catch (SQLException ex) {
            showError("Cancellation failed: " + ex.getMessage());
        }
    }

    private String generatePNR() {
        // 10-character PNR-like unique ID
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 10)
                .toUpperCase();
    }

    private boolean isValidDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);

        try {
            format.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void clearForm(JTextField passenger,
                           JTextField trainNumber,
                           JTextField trainName,
                           JTextField date,
                           JTextField source,
                           JTextField destination) {

        passenger.setText("");
        trainNumber.setText("");
        trainName.setText("");
        date.setText("");
        source.setText("");
        destination.setText("");
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Validation",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {
            }

            ReservationSystem app = new ReservationSystem();
            app.setVisible(true);
        });
    }
}
