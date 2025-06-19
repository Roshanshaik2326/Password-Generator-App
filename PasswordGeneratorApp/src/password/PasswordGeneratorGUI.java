package password;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.util.Random;

public class PasswordGeneratorGUI extends JFrame {
    private JTextField lengthField;
    private JCheckBox uppercaseBox, lowercaseBox, numbersBox, specialBox;
    private JTextField passwordField;
    private JButton generateButton, copyButton;

    public PasswordGeneratorGUI() {
        setTitle("🔐 Password Generator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450, 300));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 248, 255)); // Light background

        // === Input Form Panel (inside scroll) ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel lengthLabel = new JLabel("Password Length:");
        lengthField = new JTextField("12");

        uppercaseBox = new JCheckBox("Include Uppercase Letters (A-Z)", true);
        lowercaseBox = new JCheckBox("Include Lowercase Letters (a-z)", true);
        numbersBox = new JCheckBox("Include Numbers (0-9)", true);
        specialBox = new JCheckBox("Include Special Characters (!@#$)", true);

        Component[] inputs = {lengthLabel, lengthField, uppercaseBox, lowercaseBox, numbersBox, specialBox};
        for (Component comp : inputs) {
            comp.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            if (comp instanceof JCheckBox) ((JCheckBox) comp).setBackground(new Color(240, 248, 255));
            formPanel.add(comp, gbc);
            gbc.gridy++;
        }

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(100, 160));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // === Password Output Field ===
        passwordField = new JTextField();
        passwordField.setEditable(false);
        passwordField.setFont(new Font("Consolas", Font.BOLD, 18));
        passwordField.setHorizontalAlignment(JTextField.CENTER);
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(120, 144, 156), 2));
        passwordField.setBackground(Color.WHITE);

        // === Button Panel ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(new Color(240, 248, 255));
        generateButton = new JButton("Generate");
        copyButton = new JButton("Copy");

        JButton[] buttons = {generateButton, copyButton};
        for (JButton button : buttons) {
            button.setFocusPainted(false);
            button.setBackground(new Color(33, 150, 243)); // Blue
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.BOLD, 14));
            button.setMargin(new Insets(8, 20, 8, 20)); // Padding inside button
            buttonPanel.add(button);
        }

        // === Layout Panels ===
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        centerPanel.setBackground(new Color(240, 248, 255));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(passwordField, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // === Event Listeners ===
        generateButton.addActionListener(e -> generatePasswordAction());
        copyButton.addActionListener(e -> copyPasswordAction());

        // === Dynamic Font Resizing ===
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int fontSize = Math.max(14, width / 40);
                Font normalFont = new Font("Segoe UI", Font.PLAIN, fontSize);
                Font boldFont = new Font("Segoe UI", Font.BOLD, fontSize + 2);

                for (Component comp : inputs) comp.setFont(normalFont);
                for (JButton button : buttons) button.setFont(boldFont);
                passwordField.setFont(new Font("Consolas", Font.BOLD, fontSize + 4));
            }
        });

        setVisible(true);
    }

    private void generatePasswordAction() {
        try {
            int length = Integer.parseInt(lengthField.getText().trim());
            boolean useUpper = uppercaseBox.isSelected();
            boolean useLower = lowercaseBox.isSelected();
            boolean useNums = numbersBox.isSelected();
            boolean useSpecial = specialBox.isSelected();

            String password = generatePassword(length, useUpper, useLower, useNums, useSpecial);
            passwordField.setText(password);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid number for length.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Selection Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void copyPasswordAction() {
        String password = passwordField.getText();
        if (!password.isEmpty()) {
            StringSelection selection = new StringSelection(password);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
            JOptionPane.showMessageDialog(this, "Password copied to clipboard!");
        }
    }

    private String generatePassword(int length, boolean useUppercase, boolean useLowercase,
                                    boolean useNumbers, boolean useSpecialChars) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String special = "!@#$%^&*()-_=+[]{}|;:,.<>?";

        StringBuilder pool = new StringBuilder();
        if (useUppercase) pool.append(upper);
        if (useLowercase) pool.append(lower);
        if (useNumbers) pool.append(numbers);
        if (useSpecialChars) pool.append(special);

        if (pool.length() == 0)
            throw new IllegalArgumentException("Please select at least one character type.");

        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(pool.length());
            password.append(pool.charAt(index));
        }
        return password.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PasswordGeneratorGUI::new);
    }
}
