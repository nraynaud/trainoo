import com.nraynaud.sport.Helper;
import org.junit.Test;
import sun.misc.BASE64Encoder;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class TestCrypto {

    @Test
    public void testLol() {
    }

    public static void goCrypto() {
        final String input = "lol éàdfgdfgdfggdfgdffgdfgddfgf";
        final String encoded = Helper.cipher(input);
        System.out.println("chiffré: " + encoded);
        final String result = Helper.decipher(encoded);
        System.out.println(result);
    }

    private static void generateKey() throws NoSuchAlgorithmException {
        final KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        final Key encryptionKey = generator.generateKey();
        System.out.println("Acskey: " + new BASE64Encoder().encode(encryptionKey.getEncoded()));
        System.out.println("Hexkey: " + Arrays.toString(encryptionKey.getEncoded()));
    }

    public static void main(final String[] args) throws Exception {
        generateKey();
        goCrypto();
    }
}
