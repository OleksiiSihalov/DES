import java.util.Scanner;

public class App {
    public static void main(String[] args) {

        DES cipher = new DES();
        String text;
        String key = "";


        Scanner in = new Scanner(System.in);
        System.out.print("Enter text: \n");
        text = in.nextLine();


        while (key.length() < 8) {
            System.out.print("Enter key: \n");
            key = in.nextLine(); // cipher.hexToString("e0e0e0e0f1f1f1f1") это "ààààññññ"
            if (cipher.checkKey(key)) {
                System.out.println("Your key is weak! Enter new one: ");
                key = in.nextLine();
            }
        }

        System.out.println("Your text: " + text);
        System.out.println("Your key: " + key);
        in.close();

        System.out.println("\nEncryption:");
        System.out.println("Text: " + text);
        System.out.println("Text(HEX): " + cipher.stringToHex(text));
        text = cipher.encrypt(text, key);
        System.out.println("Cipher Text: "+ cipher.hexToString(text));
        System.out.println("Cipher Text(HEX): " + text + "\n");

        System.out.println("Decryption:");
        text = cipher.decrypt(text, key);
        System.out.println("Decrypted Text: " + text);
        System.out.println("Decrypted Text(HEX): " + cipher.stringToHex(text));
    }
}
//