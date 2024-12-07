import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class User {
    JFrame f;
    JTextField tf;
    JButton b1;
    static Map<Integer, Bank> accounts = new HashMap<>();

    static {
        accounts.put(1234, new Bank(1234, "Sathavva", 15000));
        accounts.put(9812, new Bank(9812, "Rajanna", 20000));
    }

    public User() {
        f = new JFrame("Banking System");
        tf = new JTextField(10);
        b1 = new JButton("Submit");

        b1.addActionListener(ae -> {
            try {
                int accountNumber = Integer.parseInt(tf.getText().trim());
                if (accounts.containsKey(accountNumber)) {
                    Bank bankAccount = accounts.get(accountNumber);
                    new ATM(bankAccount);
                    f.dispose();
                } else {
                    JOptionPane.showMessageDialog(f, "Invalid Account Number");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(f, "Please enter a valid account number.");
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Enter Account Number:"));
        panel.add(tf);
        panel.add(b1);

        f.add(panel);
        f.setSize(300, 150);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}

class ATM {
    JFrame f;
    JTextField tf;
    JButton bWithdraw, bDeposit, bCheckBalance, bSubmit;
    JLabel lMessage;
    Bank bankAccount;

    public ATM(Bank bankAccount) {
        this.bankAccount = bankAccount;

        f = new JFrame("ATM");
        tf = new JTextField(10);
        bWithdraw = new JButton("Withdraw");
        bDeposit = new JButton("Deposit");
        bCheckBalance = new JButton("Check Balance");
        bSubmit = new JButton("Submit");
        lMessage = new JLabel("");

        tf.setVisible(false);
        bSubmit.setVisible(false);

        bWithdraw.addActionListener(e -> setupTransaction("Withdraw"));
        bDeposit.addActionListener(e -> setupTransaction("Deposit"));
        bCheckBalance.addActionListener(e -> showBalance());

        bSubmit.addActionListener(e -> processTransaction());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Welcome, " + bankAccount.getName() + "!"));
        panel.add(bWithdraw);
        panel.add(bDeposit);
        panel.add(bCheckBalance);
        panel.add(new JLabel("Enter Amount:"));
        panel.add(tf);
        panel.add(bSubmit);
        panel.add(lMessage);

        f.add(panel);
        f.setSize(300, 300);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    private void setupTransaction(String type) {
        tf.setVisible(true);
        bSubmit.setVisible(true);
        bSubmit.setActionCommand(type);
    }

    private void processTransaction() {
        String type = bSubmit.getActionCommand();
        try {
            int amount = Integer.parseInt(tf.getText().trim());
            if (type.equals("Withdraw")) {
                if (bankAccount.withdraw(amount)) {
                    lMessage.setText("Withdraw Successful! Balance: " + bankAccount.getBalance());
                } else {
                    lMessage.setText("Insufficient Balance.");
                }
            } else if (type.equals("Deposit")) {
                bankAccount.deposit(amount);
                lMessage.setText("Deposit Successful! Balance: " + bankAccount.getBalance());
            }
        } catch (NumberFormatException e) {
            lMessage.setText("Invalid Amount.");
        }
        tf.setText("");
    }

    private void showBalance() {
        lMessage.setText("Current Balance: " + bankAccount.getBalance());
    }
}

class Bank {
    private int accountNo;
    private String name;
    private int balance;

    public Bank(int accountNo, String name, int balance) {
        this.accountNo = accountNo;
        this.name = name;
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public boolean withdraw(int amount) {
        if (amount > balance) return false;
        balance -= amount;
        return true;
    }

    public void deposit(int amount) {
        balance += amount;
    }
}

public class Main {
    public static void main(String[] args) {
        new User();
    }
}
