
import java.util.Scanner;
//pw3
public class SubstitutionCipher {
    
    public static String cipher(String text, int shift) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                c = (char)(((c - base + shift) % 26) + base);
            }
            result.append(c);
        }
        return result.toString();
    }
    
    public static String decipher(String text, int shift) {
        return cipher(text, 26 - (shift % 26));
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter text to encrypt:");
        String text = scanner.nextLine();
        
        System.out.println("Enter shift value:");
        int shift = scanner.nextInt();
        
        String encrypted = cipher(text, shift);
        System.out.println("Encrypted: " + encrypted);
        
        String decrypted = decipher(encrypted, shift);
        System.out.println("Decrypted: " + decrypted);
        
        scanner.close();
    }
}