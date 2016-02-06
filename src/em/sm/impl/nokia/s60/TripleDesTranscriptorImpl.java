/**
 * AddressHandlerImpl.java
 *
 * Copyright (C) 2002 Eugene Morozov (xonixboy@hotmail.com)
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation; either version 2 of the License, or 
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for 
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with 
 * this program; if not, write to the 
 *      Free Software Foundation, Inc., 
 *      59 Temple Place, Suite 330, 
 *      Boston, MA 02111-1307 
 *      USA
 */

package em.sm.impl.nokia.s60;

import em.sm.api.*;
import em.sm.util.*;

/** 
 * Implementation of the Triple DES transcriptor.
 *
 * @author  Eugene Morozov
 * @version 1.1.0
 */
public class TripleDesTranscriptorImpl implements Transcriptor {

    // -----------------------------------------------------------------------
    //
    // Class variables
    //
    //

    // Make sure that there is no more that one DesCipher object exists in 
    // memory at the same time. Transcriptor is a singleton, so there will be 
    // just one DesCipher.
    private static DesCipher desCipher = new DesCipher();
    private static int[] desCipherLock = new int[0];

    
    // -----------------------------------------------------------------------
    //
    // Instance variables
    //
    //
    
    
    // -----------------------------------------------------------------------
    //
    // Constructors
    //
    //
    
    /** Creates new <code>GPLDESTranscriptorImpl </code> instance.
     */
    TripleDesTranscriptorImpl() {
    }

    
    // -----------------------------------------------------------------------
    //
    // Interface implementation
    //
    //
    
    // Javadoc inherited from the interface
    public byte[] encode(byte[] plainText, byte[] key) {

        // Create the result and complement the plainText array with 0x00s
        int plainTextLength = plainText.length;
        byte[] cipherText = new byte[(plainTextLength / 8 + 1) * 8];
        byte[] completePlainText = new byte[(plainTextLength / 8 + 1) * 8];
        System.arraycopy(plainText, 0, completePlainText, 0, plainTextLength);
        
        // Encript the plaintext
        synchronized (desCipherLock) {
            int blocksNumber = plainTextLength / 8 + 1;
            for (int i = 0; i < blocksNumber; i++) {
                desCipher.setKey(key, 0);
                desCipher.encryptBlock(
                        completePlainText, i * 8, 
                        cipherText, i * 8);
                desCipher.setKey(key, 8);
                desCipher.decryptBlock(
                        completePlainText, i * 8, 
                        cipherText, i * 8);
                desCipher.setKey(key, 0);
                desCipher.encryptBlock(
                        completePlainText, i * 8, 
                        cipherText, i * 8);
            }
        }
        
        return cipherText;
    }
    
    // Javadoc inherited from the interface
    public byte[] decode(byte[] cipherText, byte[] key) {

        // Create the result and complement the plainText array with 0x00s
        int cipherTextLength = cipherText.length;
        byte[] plainText = new byte[(cipherTextLength / 8 + 1) * 8];
        byte[] completeCipherText = new byte[(cipherTextLength / 8 + 1) * 8];
        System.arraycopy(cipherText, 0, completeCipherText, 0, 
                cipherTextLength);

        // Decript the plain text
        synchronized (desCipherLock) {
            int blocksNumber = cipherTextLength / 8 + 1;
            for (int i = 0; i < blocksNumber; i++) {
                desCipher.setKey(key, 0);
                desCipher.decryptBlock(completeCipherText, i * 8, 
                        plainText, i * 8);
                desCipher.setKey(key, 8);
                desCipher.encryptBlock(completeCipherText, i * 8, 
                        plainText, i * 8);
                desCipher.setKey(key, 0);
                desCipher.decryptBlock(completeCipherText, i * 8, 
                        plainText, i * 8);
            }
        }
        
        // Detect EOL as 0x00 byte
        int plainTextLength = plainText.length;
        int completePlainTextLength = 0;
        while (completePlainTextLength < plainTextLength
                && plainText[completePlainTextLength] != 0) {
            completePlainTextLength++;
        }
        
        // Copy the resulting array
        byte[] completePlainText = new byte[completePlainTextLength];
        System.arraycopy(plainText, 0, completePlainText, 0, 
                completePlainTextLength);
        
        return completePlainText;
    }
    
}
