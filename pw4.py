from Cryptodome.Cipher import AES
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad
from Crypto.Random import get_random_bytes
import os

class FileEncryptor:
    
    def __init__(self, key=None):
        self.key = key if key else get_random_bytes(16) 
        self.block_size = AES.block_size
    
    def encrypt_file(self, input_file, output_file):
        iv = get_random_bytes(self.block_size)
        cipher = AES.new(self.key, AES.MODE_CBC, iv)
        
        with open(input_file, 'rb') as f:
            plaintext = f.read()
        
        ciphertext = cipher.encrypt(pad(plaintext, self.block_size))
        
        with open(output_file, 'wb') as f:
            f.write(iv + ciphertext)
    
    def decrypt_file(self, input_file, output_file):
        with open(input_file, 'rb') as f:
            data = f.read()
        
        iv = data[:self.block_size]
        ciphertext = data[self.block_size:]
        
        cipher = AES.new(self.key, AES.MODE_CBC, iv)
        plaintext = unpad(cipher.decrypt(ciphertext), self.block_size)
        
        with open(output_file, 'wb') as f:
            f.write(plaintext)

if __name__ == "__main__":
    encryptor = FileEncryptor(b'ThisIsASecretKey') 
    
    original_file = "original.txt"
    encrypted_file = "encrypted.enc"
    decrypted_file = "decrypted.txt"
    
    if not os.path.exists(original_file):
        with open(original_file, 'w') as f:
            f.write("This is a secret message!")
    
    encryptor.encrypt_file(original_file, encrypted_file)
    print("File encrypted successfully!")
    
    encryptor.decrypt_file(encrypted_file, decrypted_file)
    print("File decrypted successfully!")