//pw6


import java.util.Scanner;

public class PlayfairCipher {
    private static final int SIZE = 5;
    private char[][] keyTable;

    public static void main(String[] args) {
        PlayfairCipher cipher = new PlayfairCipher();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the key: ");
        String key = sanitizeInput(scanner.nextLine());
        
        System.out.print("Enter the text to encrypt: ");
        String plaintext = sanitizeInput(scanner.nextLine());

        cipher.generateKeyTable(key);
        System.out.println("\nKey Table:");
        cipher.printKeyTable();

        String encrypted = cipher.encrypt(plaintext);
        System.out.println("\nEncrypted: " + encrypted);

        String decrypted = cipher.decrypt(encrypted);
        System.out.println("Decrypted: " + decrypted);
    }

    private static String sanitizeInput(String input) {
        return input.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
    }

    public void generateKeyTable(String key) {
        keyTable = new char[SIZE][SIZE];
        String keyString = key + "ABCDEFGHIKLMNOPQRSTUVWXYZ";
        
        boolean[] used = new boolean[26];
        used['J' - 'A'] = true; // Skip J as we're using I
        
        int row = 0, col = 0;
        for (char c : keyString.toCharArray()) {
            int index = c - 'A';
            if (!used[index]) {
                keyTable[row][col] = c;
                used[index] = true;
                col++;
                if (col == SIZE) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    public void printKeyTable() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(keyTable[i][j] + " ");
            }
            System.out.println();
        }
    }

    public String encrypt(String plaintext) {
        // Prepare the plaintext (handle double letters and odd length)
        StringBuilder prepared = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i++) {
            char current = plaintext.charAt(i);
            if (i > 0 && current == plaintext.charAt(i - 1) && prepared.length() % 2 == 1) {
                prepared.append('X');
            }
            prepared.append(current);
        }
        if (prepared.length() % 2 != 0) {
            prepared.append('X');
        }
        
        String text = prepared.toString();
        StringBuilder ciphertext = new StringBuilder();
        
        for (int i = 0; i < text.length(); i += 2) {
            char a = text.charAt(i);
            char b = text.charAt(i + 1);
            int[] posA = findPosition(a);
            int[] posB = findPosition(b);
            
            // Same row
            if (posA[0] == posB[0]) {
                ciphertext.append(keyTable[posA[0]][(posA[1] + 1) % SIZE]);
                ciphertext.append(keyTable[posB[0]][(posB[1] + 1) % SIZE]);
            }
            // Same column
            else if (posA[1] == posB[1]) {
                ciphertext.append(keyTable[(posA[0] + 1) % SIZE][posA[1]]);
                ciphertext.append(keyTable[(posB[0] + 1) % SIZE][posB[1]]);
            }
            // Rectangle
            else {
                ciphertext.append(keyTable[posA[0]][posB[1]]);
                ciphertext.append(keyTable[posB[0]][posA[1]]);
            }
        }
        
        return ciphertext.toString();
    }

    public String decrypt(String ciphertext) {
        StringBuilder plaintext = new StringBuilder();
        
        for (int i = 0; i < ciphertext.length(); i += 2) {
            char a = ciphertext.charAt(i);
            char b = ciphertext.charAt(i + 1);
            int[] posA = findPosition(a);
            int[] posB = findPosition(b);
            
            // Same row
            if (posA[0] == posB[0]) {
                plaintext.append(keyTable[posA[0]][(posA[1] - 1 + SIZE) % SIZE]);
                plaintext.append(keyTable[posB[0]][(posB[1] - 1 + SIZE) % SIZE]);
            }
            // Same column
            else if (posA[1] == posB[1]) {
                plaintext.append(keyTable[(posA[0] - 1 + SIZE) % SIZE][posA[1]]);
                plaintext.append(keyTable[(posB[0] - 1 + SIZE) % SIZE][posB[1]]);
            }
            // Rectangle
            else {
                plaintext.append(keyTable[posA[0]][posB[1]]);
                plaintext.append(keyTable[posB[0]][posA[1]]);
            }
        }
        
        // Remove any padding X characters that don't make sense
        String result = plaintext.toString();
        if (result.endsWith("X")) {
            result = result.substring(0, result.length() - 1);
        }
        for (int i = 1; i < result.length() - 1; i++) {
            if (result.charAt(i) == 'X' && result.charAt(i-1) == result.charAt(i+1)) {
                result = result.substring(0, i) + result.substring(i + 1);
            }
        }
        
        return result;
    }

    private int[] findPosition(char c) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (keyTable[i][j] == c) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{-1, -1};
    }
}