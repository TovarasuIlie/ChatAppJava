package org.example.Windows;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.example.Windows.RegisterWindow.*;

public class LoginWindow extends JFrame {

    public LoginWindow() throws IOException {
        this.setTitle("Login Form");
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

        JLabel logInLabel = new JLabel("LOG IN");
        logInLabel.setBounds(150,10,150,50);
        logInLabel.setFont(new Font("Arial", 3, 30));
        rightContainer.add(logInLabel);


        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(80, 110, 100, 20);
        usernameLabel.setFont(new Font("Arial", 3, 15));
        JTextField usernameTextField = new JTextField();
        usernameTextField.setBounds(70, 130, 280,35);
        rightContainer.add(usernameLabel);
        rightContainer.add(usernameTextField);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(80, 180, 100, 20);
        passwordLabel.setFont(new Font("Arial", 3, 15));
        JTextField passwordTextField = new JPasswordField();
        passwordTextField.setBounds(70, 200, 280,35);
        rightContainer.add(passwordLabel);
        rightContainer.add(passwordTextField);

        JButton loginButton = new JButton("Log In!");
        loginButton.setBackground(new Color(15, 87, 21));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBounds(70, 270, 150, 40);
        loginButton.addActionListener(new LoginAccount(usernameTextField, passwordTextField));
        rightContainer.add(loginButton);

        JLabel haveAccountLabel = new JLabel("You've an account?");
        haveAccountLabel.setBounds(50, 370,130,30);
        JButton signinButton = new JButton("Sign In!");
        signinButton.setBackground(Color.WHITE);
        signinButton.setBounds(170, 370, 150,30);
        signinButton.setBorder(null);
        signinButton.addActionListener(e -> {
            dispose();
            try {
                new RegisterWindow();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        rightContainer.add(signinButton);
        rightContainer.add(haveAccountLabel);

        rightContainer.setPreferredSize(new Dimension(400,450));

        this.add(rightContainer, BorderLayout.LINE_END);

        this.setSize(800, 450);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    class LoginAccount implements ActionListener {
        private JTextField usernameTextField;
        private JTextField passwordTextField;
        public LoginAccount(JTextField usernameTextField, JTextField passwordTextField) {
            this.usernameTextField = usernameTextField;
            this.passwordTextField = passwordTextField;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            if(!username.isEmpty() && !password.isEmpty()) {
                if(!isValidUsernameOrEmail(username, "username")) {
                    System.out.println(username + ": " + password);
                } else {
                    JOptionPane.showMessageDialog(null, "Your username isn't valid. Verify if the username contain special characters!", "Invalid username",JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "All fields must be completed!", "Empty fields",JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}
