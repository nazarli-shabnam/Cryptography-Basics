#pw7

def encrypt(plaintext, depth):
    if depth < 2:
        raise ValueError("Depth must be 2 or greater")
    
    rails = [[] for _ in range(depth)]
    current_rail = 0
    going_down = True
    
    for char in plaintext:
        rails[current_rail].append(char)
        
        if going_down:
            current_rail += 1
            if current_rail == depth - 1:
                going_down = False
        else:
            current_rail -= 1
            if current_rail == 0:
                going_down = True
    
    ciphertext = ''.join([''.join(rail) for rail in rails])
    return ciphertext

def decrypt(ciphertext, depth):
    if depth < 2:
        raise ValueError("Depth must be 2 or greater")
    
    # Create a matrix to mark the rail positions
    rail_matrix = [['' for _ in range(len(ciphertext))] for _ in range(depth)]
    
    # Mark the positions with '*'
    current_rail = 0
    going_down = True
    
    for i in range(len(ciphertext)):
        rail_matrix[current_rail][i] = '*'
        
        if going_down:
            current_rail += 1
            if current_rail == depth - 1:
                going_down = False
        else:
            current_rail -= 1
            if current_rail == 0:
                going_down = True
    
    # Fill the marked positions with ciphertext characters
    index = 0
    for i in range(depth):
        for j in range(len(ciphertext)):
            if rail_matrix[i][j] == '*' and index < len(ciphertext):
                rail_matrix[i][j] = ciphertext[index]
                index += 1
    
    # Read the plaintext
    plaintext = []
    current_rail = 0
    going_down = True
    
    for i in range(len(ciphertext)):
        plaintext.append(rail_matrix[current_rail][i])
        
        if going_down:
            current_rail += 1
            if current_rail == depth - 1:
                going_down = False
        else:
            current_rail -= 1
            if current_rail == 0:
                going_down = True
    
    return ''.join(plaintext)

if __name__ == "__main__":
    depth = int(input("Enter the depth (3 or higher): "))
    plaintext = input("Enter the text to encrypt: ")
    
    encrypted = encrypt(plaintext, depth)
    print("Encrypted:", encrypted)
    
    decrypted = decrypt(encrypted, depth)
    print("Decrypted:", decrypted)