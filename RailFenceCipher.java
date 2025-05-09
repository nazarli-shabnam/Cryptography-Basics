// pw7;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RailFenceCipher {
    
    public static String encrypt(String plaintext, int depth) {
        if (depth < 2) {
            throw new IllegalArgumentException("Depth must be 2 or greater");
        }
        
        List<StringBuilder> rails = new ArrayList<>();
        for (int i = 0; i < depth; i++) {
            rails.add(new StringBuilder());
        }
        
        int currentRail = 0;
        boolean goingDown = true;
        
        for (char c : plaintext.toCharArray()) {
            rails.get(currentRail).append(c);
            
            if (goingDown) {
                currentRail++;
                if (currentRail == depth - 1) {
                    goingDown = false;
                }
            } else {
                currentRail--;
                if (currentRail == 0) {
                    goingDown = true;
                }
            }
        }
        
        StringBuilder ciphertext = new StringBuilder();
        for (StringBuilder rail : rails) {
            ciphertext.append(rail);
        }
        
        return ciphertext.toString();
    }
    
    public static String decrypt(String ciphertext, int depth) {
        if (depth < 2) {
            throw new IllegalArgumentException("Depth must be 2 or greater");
        }
        
        char[][] railMatrix = new char[depth][ciphertext.length()];
        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < ciphertext.length(); j++) {
                railMatrix[i][j] = '\0';
            }
        }
        
        int currentRail = 0;
        boolean goingDown = true;
        
        for (int i = 0; i < ciphertext.length(); i++) {
            railMatrix[currentRail][i] = '*';
            
            if (goingDown) {
                currentRail++;
                if (currentRail == depth - 1) {
                    goingDown = false;
                }
            } else {
                currentRail--;
                if (currentRail == 0) {
                    goingDown = true;
                }
            }
        }
        
        int index = 0;
        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < ciphertext.length(); j++) {
                if (railMatrix[i][j] == '*' && index < ciphertext.length()) {
                    railMatrix[i][j] = ciphertext.charAt(index++);
                }
            }
        }
        
        StringBuilder plaintext = new StringBuilder();
        currentRail = 0;
        goingDown = true;
        
        for (int i = 0; i < ciphertext.length(); i++) {
            plaintext.append(railMatrix[currentRail][i]);
            
            if (goingDown) {
                currentRail++;
                if (currentRail == depth - 1) {
                    goingDown = false;
                }
            } else {
                currentRail--;
                if (currentRail == 0) {
                    goingDown = true;
                }
            }
        }
        
        return plaintext.toString();
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter the depth (3 or higher): ");
        int depth = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter the text to encrypt: ");
        String plaintext = scanner.nextLine();
        
        String encrypted = encrypt(plaintext, depth);
        System.out.println("Encrypted: " + encrypted);
        
        String decrypted = decrypt(encrypted, depth);
        System.out.println("Decrypted: " + decrypted);
    }
}
