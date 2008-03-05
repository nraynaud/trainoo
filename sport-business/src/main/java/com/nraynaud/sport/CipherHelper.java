package com.nraynaud.sport;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class CipherHelper {
    static final SecretKeySpec CIPHER_KEY;

    static {
        try {
            CIPHER_KEY = new SecretKeySpec(new BASE64Decoder().decodeBuffer(System.getenv("SPORT_KEY")), "AES");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private CipherHelper() {
    }

    public static String decipher(final String encoded) {
        try {
            final Cipher cipher2 = Cipher.getInstance("AES");
            cipher2.init(Cipher.DECRYPT_MODE, CIPHER_KEY);
            return new String(cipher2.doFinal(new BASE64Decoder().decodeBuffer(encoded)), "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String cipher(final String input) {
        try {
            final Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, CIPHER_KEY);
            return new BASE64Encoder().encode(cipher.doFinal(input.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void generateKey() throws NoSuchAlgorithmException {
        final KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        final Key encryptionKey = generator.generateKey();
        System.out.println("Acskey: " + new BASE64Encoder().encode(encryptionKey.getEncoded()));
        System.out.println("Hexkey: " + Arrays.toString(encryptionKey.getEncoded()));
    }
}
