package org.example.Windows;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.regex.Pattern;

public class RegisterWindow extends JFrame {
    public RegisterWindow() throws IOException {
        this.setTitle("Sign In Form");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        JPanel leftContainer  = new JPanel();
        leftContainer.setBackground(new Color(15, 87, 21));
        leftContainer.setLayout(null);

        JLabel logoImage = new JLabel();
        BufferedImage bufferedImage = ImageIO.read(new File("src/main/java/org/example/Logos/login-logo.png"));
        Image image = bufferedImage.getScaledInstance(300,160, Image.SCALE_DEFAULT);
        ImageIcon icon = new ImageIcon(image);
        logoImage.setIcon(icon);
        logoImage.setBounds(50, 65, icon.getIconWidth(), icon.getIconHeight());
        leftContainer.add(logoImage);

        JLabel footerTitle = new JLabel("© Copyright. All Rights Reseved | by Niculai Ilie-Traian ©");
        footerTitle.setForeground(new Color(255, 255, 255));
        footerTitle.setBounds(40, 370, 350,30);
        leftContainer.add(footerTitle);

        leftContainer.setPreferredSize(new Dimension(400,450));
        this.add(leftContainer, BorderLayout.LINE_START);

        JPanel rightContainer  = new JPanel();
        rightContainer.setLayout(null);
        rightContainer.setBackground(Color.WHITE);

        JLabel logInLabel = new JLabel("SIGN IN");
        logInLabel.setBounds(150,10,150,50);
        logInLabel.setFont(new Font("Arial", 3, 30));
        rightContainer.add(logInLabel);


        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(80, 65, 100, 20);
        usernameLabel.setFont(new Font("Arial", 3, 15));
        JTextField usernameTextField = new JTextField();
        usernameTextField.setBounds(70, 85, 280,35);
        rightContainer.add(usernameLabel);
        rightContainer.add(usernameTextField);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(80, 125, 100, 20);
        emailLabel.setFont(new Font("Arial", 3, 15));
        JTextField emailTextField = new JTextField();
        emailTextField.setBounds(70, 145, 280,35);
        rightContainer.add(emailLabel);
        rightContainer.add(emailTextField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(80, 185, 100, 20);
        passwordLabel.setFont(new Font("Arial", 3, 15));
        JTextField passwordTextField = new JPasswordField();
        passwordTextField.setBounds(70, 205, 280,35);
        rightContainer.add(passwordLabel);
        rightContainer.add(passwordTextField);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password");
        confirmPasswordLabel.setBounds(80, 245, 150, 20);
        confirmPasswordLabel.setFont(new Font("Arial", 3, 15));
        JTextField confirmPasswordTextField = new JPasswordField();
        confirmPasswordTextField.setBounds(70, 265, 280,35);
        rightContainer.add(confirmPasswordLabel);
        rightContainer.add(confirmPasswordTextField);

        JButton signinButton = new JButton("Sign In!");
        signinButton.setBackground(new Color(15, 87, 21));
        signinButton.setForeground(Color.WHITE);
        signinButton.addActionListener(new InsertAccount(usernameTextField, passwordTextField, confirmPasswordTextField, emailTextField));
        signinButton.setBounds(70, 330, 150, 40);
        rightContainer.add(signinButton);

        JLabel haveAccountLabel = new JLabel("You've an account?");
        haveAccountLabel.setBounds(50, 370,130,30);
        JButton loginButton = new JButton("Log In!");
        loginButton.setBackground(Color.WHITE);
        loginButton.setBounds(170, 370, 150,30);
        loginButton.setBorder(null);
        loginButton.addActionListener(e -> {
            dispose();
            try {
                new LoginWindow();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        rightContainer.add(loginButton);
        rightContainer.add(haveAccountLabel);

        rightContainer.setPreferredSize(new Dimension(400,450));

        this.add(rightContainer, BorderLayout.LINE_END);

        this.setSize(800, 450);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    class InsertAccount implements ActionListener {
        private JTextField usernameTextField;
        private JTextField passwordTextField;
        private JTextField confirmPasswordTextField;
        private JTextField emailTextField;

        public InsertAccount(JTextField usernameTextField, JTextField passwordTextField, JTextField confirmPasswordTextField, JTextField emailTextField) {
            this.usernameTextField = usernameTextField;
            this.passwordTextField = passwordTextField;
            this.confirmPasswordTextField = confirmPasswordTextField;
            this.emailTextField = emailTextField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            String confirmPassword = confirmPasswordTextField.getText();
            String email = emailTextField.getText();

            if(username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(null, "All fields must be completed!", "Empty fields",JOptionPane.WARNING_MESSAGE);
            } else {
                if(isValidUsernameOrEmail(username, "username")) {
                    JOptionPane.showMessageDialog(null, "Your username isn't valid. Verify if the username contain special characters!", "Invalid username",JOptionPane.WARNING_MESSAGE);
                } else {
                    if(!password.equals(confirmPassword)) {
                        JOptionPane.showMessageDialog(null, "Password don't match!", "Password verify",JOptionPane.WARNING_MESSAGE);
                    } else {
                        if(!isValidUsernameOrEmail(email, "email")) {
                            JOptionPane.showMessageDialog(null, "Email address isn't valid!", "Invalid email",JOptionPane.WARNING_MESSAGE);
                        } else {
                            if(ifExist(username, "username")) {
                                JOptionPane.showMessageDialog(null, "This username is already taken! Try another username!", "User already exist",JOptionPane.WARNING_MESSAGE);
                            } else {
                                if(ifExist(email, "email")) {
                                    JOptionPane.showMessageDialog(null, "This email is already taken! Try another email!", "Email already exist",JOptionPane.WARNING_MESSAGE);
                                } else {
                                    Connection connection;
                                    try {
                                        Class.forName("com.mysql.cj.jdbc.Driver");
                                        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatproject", "root", "");

                                        PreparedStatement statement;
                                        statement = connection.prepareStatement("INSERT INTO users (username, password, email) VALUES ('" + username + "', '" + password + "', '" + email + "')");

                                        if(statement.execute()) {
                                            JOptionPane.showMessageDialog(null, "Error! Please try again later!", "Error",JOptionPane.WARNING_MESSAGE);
                                        } else {
                                            statement = connection.prepareStatement("SELECT id FROM users WHERE username = '" + username + "' AND email = '" + email + "'");
                                            statement.execute();
                                            ResultSet resultSet = statement.getResultSet();
                                            resultSet.next();
                                            String id = resultSet.getString("id");
                                            statement = connection.prepareStatement("INSERT INTO verify_codes VALUES ('" + id + "', '" + username + "', '" + generateCode() + "')");
                                            if(statement.execute()) {
                                                JOptionPane.showMessageDialog(null, "Error! Please try again later!", "Error", JOptionPane.WARNING_MESSAGE);
                                            } else {
                                                new RegisterDialog(id, username, email);
                                            }
                                        }
                                        statement.close();
                                        connection.close();
                                    }
                                    catch (Exception exception) {
                                        System.out.println(exception);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public static boolean isValidUsernameOrEmail(String string, String type)
    {
        if(type == "email") {
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
            Pattern pat = Pattern.compile(emailRegex);
            if (string == null)
                return false;
            return pat.matcher(string).matches();
        }
        if(type == "username") {
            String emailRegex = "[^a-zA-Z0-9]";

            Pattern pat = Pattern.compile(emailRegex);
            if (string == null)
                return false;
            return pat.matcher(string).find();
        }
        return false;
    }

    public static boolean ifExist(String string, String column) {
        Connection connection;
        boolean existString = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatproject", "root", "");

            Statement statement;
            statement = connection.createStatement();
            ResultSet resultSet;
            resultSet = statement.executeQuery("SELECT * FROM users WHERE " + column + " = '" + string + "'");

            if(resultSet.next())
                existString = true;
            resultSet.close();
            statement.close();
            connection.close();
        }
        catch (Exception exception) {
            System.out.println(exception);
        }
        return existString;
    }

    public static boolean verifyCode(String code, String id, String username) {
        Connection connection;
        boolean existCode = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatproject", "root", "");

            Statement statement;
            statement = connection.createStatement();
            ResultSet resultSet;
            resultSet = statement.executeQuery("SELECT * FROM verify_codes WHERE ID = '" + id + "' AND username = '" + username + "' AND code =  '" + code + "'");

            if(resultSet.next())
                existCode = true;
            resultSet.close();
            statement.close();
            connection.close();
        }
        catch (Exception exception) {
            System.out.println(exception);
        }
        return existCode;
    }

    public static boolean verifyCodeSend(String id, String username) {
        Connection connection;
        boolean codeIsSend = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatproject", "root", "");

            Statement statement;
            statement = connection.createStatement();
            ResultSet resultSet;
            resultSet = statement.executeQuery("SELECT * FROM verify_codes WHERE ID = '" + id + "' AND username = '" + username + "'");

            if(resultSet.next())
                codeIsSend = true;
            resultSet.close();
            statement.close();
            connection.close();
        }
        catch (Exception exception) {
            System.out.println(exception);
        }
        return codeIsSend;
    }

    public static String generateCode() {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789";
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int index = (int)(AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }
}


