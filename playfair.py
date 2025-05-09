#pw6


class PlayfairCipher:
    def __init__(self):
        self.SIZE = 5
        self.key_table = [[None for _ in range(
            self.SIZE)] for _ in range(self.SIZE)]

    def sanitize_input(self, text):
        return ''.join([c for c in text.upper() if c.isalpha()]).replace("J", "I")

    def generate_key_table(self, key):
        key = self.sanitize_input(key)
        alphabet = "ABCDEFGHIKLMNOPQRSTUVWXYZ"

        used = set()
        row, col = 0, 0

        for char in key:
            if char not in used:
                self.key_table[row][col] = char
                used.add(char)
                col += 1
                if col == self.SIZE:
                    col = 0
                    row += 1

        for char in alphabet:
            if char not in used:
                self.key_table[row][col] = char
                used.add(char)
                col += 1
                if col == self.SIZE:
                    col = 0
                    row += 1

    def print_key_table(self):
        for row in self.key_table:
            print(" ".join(row))

    def prepare_text(self, text):
        text = self.sanitize_input(text)
        prepared = []
        i = 0
        while i < len(text):
            if i == len(text) - 1:
                prepared.append(text[i])
                prepared.append('X')
                break
            elif text[i] == text[i+1]:
                prepared.append(text[i])
                prepared.append('X')
                i += 1
            else:
                prepared.append(text[i])
                prepared.append(text[i+1])
                i += 2
        return prepared

    def encrypt(self, plaintext):
        prepared = self.prepare_text(plaintext)
        ciphertext = []

        for i in range(0, len(prepared), 2):
            a, b = prepared[i], prepared[i+1]
            row_a, col_a = self._find_position(a)
            row_b, col_b = self._find_position(b)

            if row_a == row_b:
                ciphertext.append(
                    self.key_table[row_a][(col_a + 1) % self.SIZE])
                ciphertext.append(
                    self.key_table[row_b][(col_b + 1) % self.SIZE])
            elif col_a == col_b:
                ciphertext.append(
                    self.key_table[(row_a + 1) % self.SIZE][col_a])
                ciphertext.append(
                    self.key_table[(row_b + 1) % self.SIZE][col_b])
            else:
                ciphertext.append(self.key_table[row_a][col_b])
                ciphertext.append(self.key_table[row_b][col_a])

        return "".join(ciphertext)

    def decrypt(self, ciphertext):
        ciphertext = self.sanitize_input(ciphertext)
        plaintext = []

        for i in range(0, len(ciphertext), 2):
            a, b = ciphertext[i], ciphertext[i+1]
            row_a, col_a = self._find_position(a)
            row_b, col_b = self._find_position(b)

            if row_a == row_b:
                plaintext.append(
                    self.key_table[row_a][(col_a - 1) % self.SIZE])
                plaintext.append(
                    self.key_table[row_b][(col_b - 1) % self.SIZE])
            elif col_a == col_b:
                plaintext.append(
                    self.key_table[(row_a - 1) % self.SIZE][col_a])
                plaintext.append(
                    self.key_table[(row_b - 1) % self.SIZE][col_b])
            else:
                plaintext.append(self.key_table[row_a][col_b])
                plaintext.append(self.key_table[row_b][col_a])

        result = "".join(plaintext)
        if result.endswith("X"):
            result = result[:-1]
        i = 1
        while i < len(result) - 1:
            if result[i] == 'X' and result[i-1] == result[i+1]:
                result = result[:i] + result[i+1:]
            else:
                i += 1
        return result

    def _find_position(self, char):
        for row in range(self.SIZE):
            for col in range(self.SIZE):
                if self.key_table[row][col] == char:
                    return (row, col)
        return (-1, -1)


if __name__ == "__main__":
    cipher = PlayfairCipher()

    key = input("Enter the key: ")
    cipher.generate_key_table(key)

    print("\nKey Table:")
    cipher.print_key_table()

    plaintext = input("\nEnter the text to encrypt: ")
    encrypted = cipher.encrypt(plaintext)
    print(f"\nEncrypted: {encrypted}")

    decrypted = cipher.decrypt(encrypted)
    print(f"Decrypted: {decrypted}")
