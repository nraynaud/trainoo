package com.jfish.io;

import com.jfish.JFish;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class CipherOutputStream {

    //The underlying BufferedOutputStream instance
    private BufferedOutputStream bos;

    //The underlying block cipher instance
    private JFish algorithm;

    /**
     * Creates a new instance of CipherOutputStream
     */
    public CipherOutputStream(final FileOutputStream fos, final String passphrase, final int keylength) {
        //Create a new instance of the BufferedOutputStream
        bos = new BufferedOutputStream(fos);

        //Create a new instance of the block cipher
        algorithm = new JFish();

        //Initialize the block cipher
        algorithm.generateKey(passphrase, keylength);
    }

    /**
     * Encrypts then writes a byte array to the BufferedOutputStream
     */
    public void write(final byte[] b) throws IOException {
        //Encrypt the supplyed byte array
        final byte[] cipherText = algorithm.encryptByteArray(b);

        //Write the encrypted bytes to the BufferedOutputStream
        bos.write(cipherText);
    }

    /**
     * Flushes the BufferedOutputStream
     */
    public void flush() throws IOException {
        //Flush the BufferedOutputStream
        bos.flush();
    }

    /**
     * Clears the cipher, then closes the BufferedOutputStream
     */
    public void close() throws IOException {
        //Clear the block cipher
        algorithm.clear();

        //Close the BufferedOutputStream
        bos.close();
    }
}