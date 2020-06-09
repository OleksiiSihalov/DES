import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.log;

class DES {

    private int textLength = 0;

    // Початкова перестановка IP
    final private int[] IP = {58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7};

    // Кінцева перестановка
    final private int[] IP1 = {40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25};

    // Функція розширення E
    final private int[] EP = {32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1};

    // Перетворення S
    final private int[][][] sbox = {
            {{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}},

            {{15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}},
            {{10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}},
            {{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}},
            {{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}},
            {{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}},
            {{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}},
            {{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}}
    };

    //– Перестановка Р
    final private int[] P = {16, 7, 20, 21, 29, 12, 28, 17,
            1, 15, 23, 26, 5, 18, 31, 10,
            2, 8, 24, 14, 32, 27, 3, 9,
            19, 13, 30, 6, 22, 11, 4, 25};

    // – Перестановка бітів для знаходження розширеного ключа
    final private int[] PC1 = {57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4};

    // Циклічні зсуви
    final private int[] shiftBits = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

    // 48 біт ключа
    final private int[] PC2 = {14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4,
            26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40,
            51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32};

    //weak keys
    final private List<String> weakKeys = new ArrayList<>(Arrays.asList("0101010101010101", "1f1f1f1f0e0e0e0e",
            "e0e0e0e0f1f1f1f1", "fefefefefefefefe", "00000000000000",
            "0000000fffffff", "fffffff0000000", "ffffffffffffff"));


    private String hexToBin(String input) {
        int n = input.length() * 4;
        StringBuilder inputBuilder = new StringBuilder(Long.toBinaryString(
                Long.parseUnsignedLong(input, 16)));
        while (inputBuilder.length() < n)
            inputBuilder.insert(0, "0");
        return inputBuilder.toString();
    }

    private String binToHex(String input) {
        int n = input.length() / 4;
        StringBuilder inputBuilder = new StringBuilder(Long.toHexString(
                Long.parseUnsignedLong(input, 2)));
        while (inputBuilder.length() < n) {
            inputBuilder.insert(0, "0");
        }
        return inputBuilder.toString();
    }

    public String stringToHex(String asciiStr) {
        char[] chars = asciiStr.toCharArray();
        StringBuilder hex = new StringBuilder();
        for (char ch : chars) {
            hex.append(Integer.toHexString(ch));
        }
        return hex.toString();
    }

    public String hexToString(String hexStr) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }


    private String permutation(int[] sequence, String input) {
        StringBuilder output = new StringBuilder();
        input = hexToBin(input);
        for (int value : sequence) {
            output.append(input.charAt(value - 1));
        }
        output = new StringBuilder(binToHex(output.toString()));
        return output.toString();
    }

    private String xor(String a, String b) {
        long t_a = Long.parseUnsignedLong(a, 16);
        long t_b = Long.parseUnsignedLong(b, 16);

        t_a = t_a ^ t_b;
        StringBuilder aBuilder = new StringBuilder(Long.toHexString(t_a));
        while (aBuilder.length() < b.length()) {
            aBuilder.insert(0, "0");
        }
        return aBuilder.toString();
    }

    private String leftShift(String input, int numBits) {
        int n = input.length() * 4;
        int[] perm = new int[n];
        for (int i = 0; i < n - 1; i++)
            perm[i] = (i + 2);
        perm[n - 1] = 1;
        while (numBits-- > 0)
            input = permutation(perm, input);
        return input;
    }

    private String[] getKeys(String key, int index) {
        if (key.length() < 32) {
            key = key.substring(0, 16);
        } else {
            key = key.substring(index * 16, (index + 1) * 16);
        }

        String[] keys = new String[16];
        key = permutation(PC1, key);
        for (int i = 0; i < 16; i++) {
            key = leftShift(
                    key.substring(0, 7), shiftBits[i])
                    + leftShift(key.substring(7, 14),
                    shiftBits[i]);
            keys[i] = permutation(PC2, key);
        }
        return keys;
    }

    private String sBox(String input) {
        StringBuilder output = new StringBuilder();
        input = hexToBin(input);
        for (int i = 0; i < 48; i += 6) {
            String temp = input.substring(i, i + 6);
            int num = i / 6;
            int row = Integer.parseInt(
                    temp.charAt(0) + "" + temp.charAt(5), 2);
            int col = Integer.parseInt(
                    temp.substring(1, 5), 2);
            output.append(Integer.toHexString(
                    sbox[num][row][col]));
        }
        return output.toString();
    }

    private String f(String input, String key) {
        String left = input.substring(0, 8);
        String right = input.substring(8, 16);
        String temp = right;

        temp = permutation(EP, temp);
        temp = xor(temp, key);
        temp = sBox(temp);
        temp = permutation(P, temp);
        left = xor(left, temp);

        return right + left;
    }

    private double countEntropy(String text) {
        double entropy = 0;
        text = hexToBin(text);
        long count = text.chars().filter(ch -> ch == '1').count();
        double p = (double) count / 64;
        entropy = -(p * log(p) / log(2) + (1 - p) * log(1 - p) / log(2));
        return entropy;
    }

    public Boolean checkKey(String key) {
        key = stringToHex(key);
        return weakKeys.contains(key);
    }

    public String encrypt(String text, String key) {
        textLength = text.length();
        StringBuilder output = new StringBuilder(text);
        while (output.length() % 8 != 0) {
            output.append(" ");
        }

        text = stringToHex(output.toString());
        key = stringToHex(key);
        output.setLength(0);

        for (int i = 0, k = 0; i + 16 < text.length() + 1; i += 16, k = k ^ 1) {
            String[] keys = getKeys(key, k);

            String tempText = text.substring(i, i + 16);
            tempText = permutation(IP, tempText);

            for (int j = 0; j < 16; j++) {
                tempText = f(tempText, keys[j]);
                System.out.println("Entropy in round " + (i / 16 + 1) + "-" + (j + 1) + ": " + countEntropy(tempText));

            }

            tempText = tempText.substring(8, 16)
                    + tempText.substring(0, 8);

            tempText = permutation(IP1, tempText);

            output.append(tempText);
        }
        return output.toString();
    }

    public String decrypt(String text, String key) {
        StringBuilder output = new StringBuilder(text);
        key = stringToHex(key);

        output.setLength(0);

        for (int i = 0, k = 0; i + 16 < text.length() + 1; i += 16, k = k ^ 1) {
            String[] keys = getKeys(key, k);

            String tempText = text.substring(i, i + 16);
            tempText = permutation(IP, tempText);

            for (int j = 15; j > -1; j--) {
                tempText = f(tempText, keys[j]);
            }

            tempText = tempText.substring(8, 16)
                    + tempText.substring(0, 8);

            tempText = permutation(IP1, tempText);

            output.append(tempText);
        }
        output = new StringBuilder(hexToString(output.toString()));
        output.setLength(textLength);
        return output.toString();
    }
}
