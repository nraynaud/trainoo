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

public class Helper {
    public static final String HEX_CHARS = "0123456789ABCDEF";
    private static final SecretKeySpec CIPHER_KEY;

    static {
        try {
            CIPHER_KEY = new SecretKeySpec(new BASE64Decoder().decodeBuffer(System.getenv("SPORT_KEY")), "AES");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Helper() {
    }

    public static String escaped(final String string) {
        final String s = string != null && !"".equals(string) ? string : "";
        final StringBuilder str = new StringBuilder();
        escape(s, str);
        return str.toString();
    }

    public static void escape(final String input, final StringBuilder collector) {
        for (int j = 0; j < input.length(); j++) {
            final char c = input.charAt(j);

            // encode standard ASCII characters into HTML entities where needed
            if (c < '\200') {
                switch (c) {
                    case '"':
                        collector.append("&quot;");
                        break;
                    case '&':
                        collector.append("&amp;");
                        break;
                    case '<':
                        collector.append("&lt;");
                        break;
                    case '>':
                        collector.append("&gt;");
                        break;
                    default:
                        collector.append(c);
                }
            }
            // encode 'ugly' characters (ie Word "curvy" quotes etc)
            else if (c < '\377') {
                final int a = c % 16;
                final int b = (c - a) / 16;
                final String hex = "" + HEX_CHARS.charAt(b) + HEX_CHARS.charAt(a);
                collector.append("&#x").append(hex).append(";");
            }
            //add other characters back in - to handle charactersets
            //other than ascii
            else {
                collector.append(c);
            }
        }
    }

    public static String nonEscaped(final UserString string) {
        return string == null ? null : string.nonEscaped();
    }

    public static String escaped(final UserString string) {
        return escaped(string.nonEscaped());
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
