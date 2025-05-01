package pw5;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.security.*;
import java.util.*;
import java.nio.file.*; 

public class MonolithicEncryptionCBC {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final int KEY_SIZE = 128;
    private static final int BLOCK_SIZE = 16; 
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("PW5: Monolithic Encryption with CBC Mode");
        System.out.println("1. Encrypt/Decrypt text input");
        System.out.println("2. Encrypt/Decrypt text file");
        System.out.print("Choose option (1/2): ");
        
        int option = scanner.nextInt();
        scanner.nextLine(); 
        
        try {
            SecretKey secretKey = generateKey();
            
            if (option == 1) {
                handleTextInput(scanner, secretKey);
            } else if (option == 2) {
                handleFileInput(scanner, secretKey);
            } else {
                System.out.println("Invalid option");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        scanner.close();
    }
    
    private static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(KEY_SIZE);
        return keyGenerator.generateKey();
    }
    
    private static void handleTextInput(Scanner scanner, SecretKey secretKey) throws Exception {
        System.out.print("Enter text: ");
        String text = scanner.nextLine();
        
        System.out.print("Encrypt (E) or Decrypt (D)? ");
        String choice = scanner.nextLine().toUpperCase();
        
        if (choice.equals("E")) {
            byte[] iv = generateIV();
            byte[] encrypted = encrypt(text.getBytes(), secretKey, iv);
            System.out.println("Encrypted (Base64): " + Base64.getEncoder().encodeToString(encrypted));
            System.out.println("IV (Base64): " + Base64.getEncoder().encodeToString(iv));
        } else if (choice.equals("D")) {
            System.out.print("Enter IV (Base64): ");
            byte[] iv = Base64.getDecoder().decode(scanner.nextLine());
            System.out.print("Enter encrypted data (Base64): ");
            byte[] encrypted = Base64.getDecoder().decode(scanner.nextLine());
            byte[] decrypted = decrypt(encrypted, secretKey, iv);
            System.out.println("Decrypted: " + new String(decrypted));
        } else {
            System.out.println("Invalid choice");
        }
    }
    
    private static void handleFileInput(Scanner scanner, SecretKey secretKey) throws Exception {
        System.out.print("Enter file path: ");
        String filePath = scanner.nextLine();
        
        System.out.print("Encrypt (E) or Decrypt (D)? ");
        String choice = scanner.nextLine().toUpperCase();
        
        if (choice.equals("E")) {
            byte[] fileContent = readFileBytes(filePath);
            byte[] iv = generateIV();
            byte[] encrypted = encrypt(fileContent, secretKey, iv);
            
            String outputPath = filePath + ".enc";
            try (DataOutputStream out = new DataOutputStream(new FileOutputStream(outputPath))) {
                out.writeInt(iv.length);
                out.write(iv);
                out.write(encrypted);
            }
            System.out.println("File encrypted and saved as " + outputPath);
        } else if (choice.equals("D")) {
            try (DataInputStream in = new DataInputStream(new FileInputStream(filePath))) {
                int ivLength = in.readInt();
                byte[] iv = new byte[ivLength];
                in.readFully(iv);
                byte[] encrypted = in.readAllBytes();
                
                byte[] decrypted = decrypt(encrypted, secretKey, iv);
                String outputPath = filePath.replace(".enc", ".dec");
                Files.write(Paths.get(outputPath), decrypted);
                System.out.println("File decrypted and saved as " + outputPath);
            }
        } else {
            System.out.println("Invalid choice");
        }
    }
    
    private static byte[] generateIV() {
        byte[] iv = new byte[BLOCK_SIZE];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
    
    private static byte[] encrypt(byte[] input, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        return cipher.doFinal(input);
    }
    
    private static byte[] decrypt(byte[] input, SecretKey key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        return cipher.doFinal(input);
    }
    
    private static byte[] readFileBytes(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath));
    }
}