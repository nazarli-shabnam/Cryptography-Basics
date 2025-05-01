from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad
from Crypto.Random import get_random_bytes
import base64
import os

BLOCK_SIZE = 16 

def generate_key():
    return get_random_bytes(16)

def encrypt_data(data, key):
    iv = get_random_bytes(BLOCK_SIZE)
    cipher = AES.new(key, AES.MODE_CBC, iv)
    padded_data = pad(data, BLOCK_SIZE)
    encrypted = cipher.encrypt(padded_data)
    return iv + encrypted

def decrypt_data(encrypted_data, key):
    iv = encrypted_data[:BLOCK_SIZE]
    cipher = AES.new(key, AES.MODE_CBC, iv)
    decrypted = cipher.decrypt(encrypted_data[BLOCK_SIZE:])
    return unpad(decrypted, BLOCK_SIZE)

def handle_text_input(key):
    text = input("Enter text: ")
    choice = input("Encrypt (E) or Decrypt (D)? ").upper()
    
    if choice == 'E':
        encrypted = encrypt_data(text.encode(), key)
        print("Encrypted (Base64):", base64.b64encode(encrypted).decode())
    elif choice == 'D':
        encrypted = base64.b64decode(input("Enter encrypted data (Base64): "))
        decrypted = decrypt_data(encrypted, key)
        print("Decrypted:", decrypted.decode())
    else:
        print("Invalid choice")

def handle_file_input(key):
    file_path = input("Enter file path: ")
    choice = input("Encrypt (E) or Decrypt (D)? ").upper()
    
    try:
        if choice == 'E':
            with open(file_path, 'rb') as f:
                data = f.read()
            encrypted = encrypt_data(data, key)
            output_path = file_path + '.enc'
            with open(output_path, 'wb') as f:
                f.write(encrypted)
            print(f"File encrypted and saved as {output_path}")
        elif choice == 'D':
            with open(file_path, 'rb') as f:
                encrypted = f.read()
            decrypted = decrypt_data(encrypted, key)
            output_path = file_path.replace('.enc', '.dec')
            with open(output_path, 'wb') as f:
                f.write(decrypted)
            print(f"File decrypted and saved as {output_path}")
        else:
            print("Invalid choice")
    except Exception as e:
        print(f"Error: {e}")

def main():
    print("PW5: Monolithic Encryption with CBC Mode")
    print("1. Encrypt/Decrypt text input")
    print("2. Encrypt/Decrypt text file")
    
    key = generate_key()
    print(f"Generated key (Base64): {base64.b64encode(key).decode()}")
    
    option = input("Choose option (1/2): ")
    if option == '1':
        handle_text_input(key)
    elif option == '2':
        handle_file_input(key)
    else:
        print("Invalid option")

if __name__ == "__main__":
    main()