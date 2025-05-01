def cipher(text, shift):
    result = []
    for char in text:
        if char.isalpha():
            base = ord('a') if char.islower() else ord('A')
            new_char = chr((ord(char) - base + shift) % 26 + base)
            result.append(new_char)
        else:
            result.append(char)
    return ''.join(result)

def decipher(text, shift):
    return cipher(text, 26 - (shift % 26))

if __name__ == "__main__":
    text = input("Enter text to encrypt: ")
    shift = int(input("Enter shift value: "))
    
    encrypted = cipher(text, shift)
    print("Encrypted:", encrypted)
    
    decrypted = decipher(encrypted, shift)
    print("Decrypted:", decrypted)