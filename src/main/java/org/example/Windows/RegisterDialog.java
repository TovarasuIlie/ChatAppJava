package org.example.Windows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import static org.example.Windows.RegisterWindow.*;

public class RegisterDialog extends JDialog {
    int minutes = 0;
    int seconds = 10;
    String minutes_string = String.format("%02d", minutes);
    String seconds_string = String.format("%02d", seconds);
    public RegisterDialog(String id, String username, String email) {
        JDialog codeDialog = new JDialog();
        codeDialog.setSize(400,210);
        codeDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        codeDialog.setLocationRelativeTo(null);
        codeDialog.setResizable(false);
        codeDialog.setTitle("Verification Code");
        codeDialog.setLayout(null);

        JTextArea codeDialogTextArea = new JTextArea("For to verify email address, we sent a code on email address \"" + email +  "\". Please insert the code, and press \"Verify!\". If you don't receive the code, press \"Re-Send Code!\", for another code.");
        codeDialogTextArea.setFont(new Font("Arial", 1, 12));
        codeDialogTextArea.setEditable(false);
        codeDialogTextArea.setWrapStyleWord(true);
        codeDialogTextArea.setLineWrap(true);
        codeDialogTextArea.setOpaque(false);
        codeDialogTextArea.setBounds(10,10, 370, 60);

        JLabel codeDialogLabel = new JLabel("Insert the code below:");
        codeDialogLabel.setBounds(10, 70, 150,20);

        JLabel countdownTimerLabel = new JLabel("05:00");
        countdownTimerLabel.setBounds(150, 85, 100, 30);
        Timer timer = new Timer(1000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (seconds > 0) {
                    seconds -= 1;
                } else if (seconds == 0 && minutes > 0) {
                    minutes -= 1;
                    seconds = 59;
                } else if (minutes == 0 && seconds == 0) {
                    ((Timer)e.getSource()).stop();
                    Connection connection;
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatproject", "root", "");
                        PreparedStatement statement;
                        statement = connection.prepareStatement("DELETE FROM verify_codes WHERE id = '" + id + "' AND username = '" + username + "'");
                        statement.execute();
                        statement.close();
                        connection.close();
                    } catch (Exception exception) {
                        System.out.println(exception);
                    }
                    JOptionPane.showMessageDialog(null, "Your code has expired. Press button \"Re-Send Code!\"", "Code expired", JOptionPane.ERROR_MESSAGE);
                }
                minutes_string = String.format("%02d", minutes);
                seconds_string = String.format("%02d", seconds);
                countdownTimerLabel.setText(minutes_string + ":" + seconds_string);
            }
        });
        timer.start();

        JTextField codeDialogText = new JTextField();
        codeDialogText.setBounds(10, 90, 127,25);

        JButton submitButton = new JButton("Verify!");
        submitButton.setBounds(10, 130, 100,30);
        submitButton.setBackground(new Color(15, 87, 21));
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(e1 -> {
            String code = codeDialogText.getText();
            if(verifyCode(code, id, username)) {
                Connection connection1;
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    connection1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatproject", "root", "");
                    PreparedStatement statement1;
                    statement1 = connection1.prepareStatement("UPDATE users SET verified = 1 WHERE id = '" + id + "' AND username = '" + username + "'");
                    if (!statement1.execute()) {
                        codeDialog.setVisible(false);
                        JOptionPane.showMessageDialog(null, "Your account has been created!", "Account Created Successfully", JOptionPane.INFORMATION_MESSAGE);
                        statement1 = connection1.prepareStatement("DELETE FROM verify_codes WHERE id = '" + id + "' AND username = '" + username + "' AND code = '" + code + "'");
                        statement1.execute();
                    }
                    statement1.close();
                    connection1.close();
                } catch(Exception e2) {
                    System.out.println(e2);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Your verification code is incorrect. Please insert again!", "Verification Code Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton resendButton = new JButton("Re-Send Code!");
        resendButton.setBounds(255, 130, 120,30);
        resendButton.setBackground(new Color(15, 87, 21));
        resendButton.addActionListener(e1 -> {
            if(!verifyCodeSend(id, username)) {
                Connection connection1;
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    connection1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatproject", "root", "");
                    PreparedStatement statement1;

                    statement1 = connection1.prepareStatement("INSERT INTO verify_codes VALUES ('" + id + "', '" + username + "', '" + generateCode() + "')");

                    if(!statement1.execute()) {
                        minutes = 5;
                        seconds = 0;
                        timer.start();
                        JOptionPane.showMessageDialog(null, "A new code has been sent. Please check your inbox!", "Code Sent Successfully", JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Try again!", "Code Sent Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (Exception exception) {
                    System.out.println(exception);
                }
            } else {
                Connection connection1;
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    connection1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatproject", "root", "");
                    PreparedStatement statement1;

                    statement1 = connection1.prepareStatement("UPDATE verify_codes SET code = '" + generateCode() + "' WHERE id = '" + id + "' AND username = '" + username + "'");

                    if(!statement1.execute()) {
                        minutes = 5;
                        seconds = 0;
                        timer.start();
                        JOptionPane.showMessageDialog(null, "A new code has been sent. Please check your inbox!", "Code Sent Successfully", JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Try again!", "Code Sent Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception exception) {
                    System.out.println(exception);
                }
            }
        });
        resendButton.setForeground(Color.WHITE);

        codeDialog.add(codeDialogTextArea);
        codeDialog.add(codeDialogLabel);
        codeDialog.add(codeDialogText);
        codeDialog.add(countdownTimerLabel);
        codeDialog.add(submitButton);
        codeDialog.add(resendButton);
        codeDialog.setVisible(true);
    }
}
