package com.jfish;

import com.jfish.crypt.CryptoException;
import com.jfish.crypt.Twofish;

import java.security.InvalidKeyException;
import java.util.Arrays;


/**
 * @author neil
 */
public class JFish {

    private Twofish algorithm;
    private Object key;

    //The constructor
    public JFish() {
        algorithm = new Twofish();
        key = null;
    }

    //Generates an internal Twofish key from the supplied passphrase
    public void generateKey(final String passphrase, final int keysize) {
        String pass = "";

        if (keysize == 8) {
            pass = "uuuuuuuu";
        } else if (keysize == 16) {
            pass = "uuuuuuuuuuuuuuuu";
        } else if (keysize == 24) {
            pass = "uuuuuuuuuuuuuuuuuuuuuuuu";
        } else if (keysize == 32) {
            pass = "uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu";
        }

        pass = passphrase + pass.substring(passphrase.length());

        try {
            key = Twofish.makeKey(pass.getBytes());
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
        }
    }

    //Encrypts a supplied string using the internal key
    public byte[] encryptString(final String string) {
        return encryptString(string, 0);
    }

    //Encrypts a supplied string from the offset point on using the internal key
    public byte[] encryptString(final String string, final int offset) {
        byte[] result = null;

        try {
            result = algorithm.encryptArray(string.getBytes(), offset, key);
        } catch (CryptoException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    //Decrypts a supplied string using the internal key
    public byte[] decryptString(final String string) {
        return decryptString(string, 0);
    }

    //Decrypts a supplied string from the offset point on using the internal key
    public byte[] decryptString(final String string, final int offset) {
        byte[] result = null;

        try {
            result = algorithm.decryptArray(string.getBytes(), offset, key);
        } catch (CryptoException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    //Encrypts a supplied byte array using the internal key
    public byte[] encryptByteArray(final byte[] bytes) {

        return encryptByteArray(bytes, 0);
    }

    //Encrypts a supplied byte array from the offset point on using the internal key
    public byte[] encryptByteArray(final byte[] bytes, final int offset) {
        byte[] result = null;

        try {
            result = algorithm.encryptArray(bytes, offset, key);
        } catch (CryptoException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    //Decrypts a supplied byte array using the internal key
    public byte[] decryptByteArray(final byte[] bytes) {

        return decryptByteArray(bytes, 0);
    }

    //Decrypts a supplied string from the offset point on using the internal key
    public byte[] decryptByteArray(final byte[] bytes, final int offset) {
        byte[] result = null;

        try {
            result = algorithm.decryptArray(bytes, offset, key);
        } catch (CryptoException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    //Encrypts a supplied block using the internal key
    public byte[] encryptBlock(final byte[] block) {

        return encryptBlock(block, 0);
    }

    //Encrypts a supplied block from the offset point on using the internal key
    public byte[] encryptBlock(final byte[] block, final int offset) {

        return Twofish.encryptBlock(block, offset, key);
    }

    //Decrypts a supplied block using the internal key
    public byte[] decryptBlock(final byte[] block) {

        return decryptBlock(block, 0);
    }

    //Decrypts a supplied block from the offset point on using the internal key
    public byte[] decryptBlock(final byte[] block, final int offset) {

        return Twofish.decryptBlock(block, offset, key);
    }

    //Compares two byte arrays to determine whether they are equal
    public static boolean compareArrays(final byte[] a, final byte[] b) {
        return Arrays.equals(a, b);
    }

    //Returns the Twofish algorithm's block size
    public int getBlockSize() {

        return Twofish.blockSize();
    }

    //Clears the internal variables
    public static void clear() {
        new JFish();
    }
}
