// https://uva.onlinejudge.org/external/10/1052.pdf

import java.util.Scanner;
import java.util.regex.*;

public class BitCompressor {
    public static void main(String[] args) {
        // Scanner s = new Scanner(System.in);
        // while ( s.hasNext() ) {
        //     int l = s.nextInt();
        //     int k = s.nextInt();
        //     String data = s.nextLine();
        //     System.out.println(verify(data));
        // }
        String data = "11111111001001111111111111110011";
                    //   "1000    001001111           0011"
                    //   "1000    00100100010101010010"
        System.out.println(zip(data));
        // System.out.println("111111111111111".length());
        System.out.println("10000010011110011");
    }

    public static String verify(String data) {
        // verificar para cada possivel combinação possivel se o valor fica maior
        return null;
    }

    public static String zip(String bit) {
        Pattern pattern = Pattern.compile("[1]{2,}");
        Matcher m = pattern.matcher(bit);
        while ( m.find() ) {
            bit = bit.replaceAll(m.group(), Integer.toBinaryString(m.group().length()));
            // System.out.printf("group: %s - bin: %s\n", m.group(), Integer.toBinaryString(m.group().length()));
        }
        return bit;
    }
    public static String unzip(String bit) {
        Pattern pattern = Pattern.compile("[1]{2,}");
        Matcher m = pattern.matcher(bit);
        // while ( m.find() )
            // bit = bit.replace(m.group(), Integer.parseInt(m.group(), 2));
        return bit;
    }
}
