package com.jfish.io;

import com.jfish.JFish;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;


public class CipherInputStream {

    //The underlying BufferedInputStream instance
    private BufferedInputStream bis;

    //The underlying block cipher instance
    private JFish algorithm;

    /**
     * Creates a new instance of CipherInputStream
     */
    public CipherInputStream(final FileInputStream fis, final String passphrase, final int keylength) {
        //Create a new instance of the BufferedInputStream
        bis = new BufferedInputStream(fis);

        //Create a new instance of the block cipher
        algorithm = new JFish();

        //Initialize the block cipher
        algorithm.generateKey(passphrase, keylength);
    }

    /**
     * Fills the supplied Byte Array with decrypted text from the BufferedInputStream
     */
    public byte[] read(final byte[] bytes) throws IOException {
        //Fill the supplyed array with encrypted bytes
        bis.read(bytes);

        //Decrypt the bytes

        //Return the decrypted bytes
        return algorithm.decryptByteArray(bytes);
    }

}
