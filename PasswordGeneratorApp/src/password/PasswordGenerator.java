package password;

import java.util.*;

public class PasswordGenerator {
    public static String generatePassword(int length, boolean useUppercase, boolean useLowercase,
                                          boolean useNumbers, boolean useSpecialChars) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String special = "!@#$%^&*()-_=+[]{}|;:,.<>?";

        StringBuilder characterPool = new StringBuilder();

        if (useUppercase) characterPool.append(upper);
        if (useLowercase) characterPool.append(lower);
        if (useNumbers) characterPool.append(numbers);
        if (useSpecialChars) characterPool.append(special);

        if (characterPool.length() == 0) {
            throw new IllegalArgumentException("At least one character type must be selected.");
        }

        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characterPool.length());
            password.append(characterPool.charAt(index));
        }

        return password.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("---- Password Generator ----");
        System.out.print("Enter password length: ");
        int length = scanner.nextInt();

        System.out.print("Include uppercase letters? (true/false): ");
        boolean useUppercase = scanner.nextBoolean();

        System.out.print("Include lowercase letters? (true/false): ");
        boolean useLowercase = scanner.nextBoolean();

        System.out.print("Include numbers? (true/false): ");
        boolean useNumbers = scanner.nextBoolean();

        System.out.print("Include special characters? (true/false): ");
        boolean useSpecialChars = scanner.nextBoolean();

        try {
            String password = generatePassword(length, useUppercase, useLowercase, useNumbers, useSpecialChars);
            System.out.println("Generated Password: " + password);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}
