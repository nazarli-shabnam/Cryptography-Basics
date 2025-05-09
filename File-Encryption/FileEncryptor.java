import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.Key;
import java.util.Base64;
//pw4
public class FileEncryptor {
    
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    
    public static void encryptFile(String key, String inputFile, String outputFile) throws Exception {
        doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
    }
    
    public static void decryptFile(String key, String inputFile, String outputFile) throws Exception {
        doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
    }
    
    private static void doCrypto(int cipherMode, String key, String inputFile, String outputFile) throws Exception {
        Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(cipherMode, secretKey);
        
        byte[] inputBytes = Files.readAllBytes(Paths.get(inputFile));
        byte[] outputBytes = cipher.doFinal(inputBytes);
        
        Files.write(Paths.get(outputFile), outputBytes, StandardOpenOption.CREATE);
    }
    
    public static void main(String[] args) {
        try {
            String key = "ThisIsASecretKey";
            String originalFile = "original.txt";
            String encryptedFile = "encrypted.enc";
            String decryptedFile = "decrypted.txt";
            
            encryptFile(key, originalFile, encryptedFile);
            System.out.println("File encrypted successfully!");
            
            decryptFile(key, encryptedFile, decryptedFile);
            System.out.println("File decrypted successfully!");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}