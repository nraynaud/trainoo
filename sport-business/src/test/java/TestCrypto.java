import org.junit.Test;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class TestCrypto {
    private static final byte[] KEY = {9, -41, -108, 91, 110, 22, 79, -105, -47, -33, 90, 46, 16, 96, 40, 13};

    @Test
    public void testLol() {

    }

    public static void goCrypto() throws
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException,
            IOException {

        final SecretKeySpec spec = new SecretKeySpec(KEY, "AES");
        final Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, spec);
        final byte[] cryted = cipher.doFinal("lol éà".getBytes("UTF-8"));
        System.out.println(new BASE64Encoder().encode(KEY));
        System.out.println(Arrays.toString(new BASE64Decoder().decodeBuffer("CdeUW24WT5fR31ouEGAoDQ==")));
        final byte[] c = {92, 113, 125, 117, -125, 90, -64, 120, 10, -109, 98, -18, -12, 44, 77, 5};
        System.out.println("chiffré " + Arrays.toString(cryted));
        cipher.init(Cipher.DECRYPT_MODE, spec);
        final byte[] decrypted = cipher.doFinal(c);
        System.out.println(new String(decrypted, "UTF-8"));
    }

    public static void main(final String[] args) throws Exception {
        goCrypto();
    }
}
