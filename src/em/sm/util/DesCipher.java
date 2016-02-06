/**
 * DesCipher.java
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

package em.sm.util;

/** Implementation of the DES algorithm.
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public class DesCipher {

    // -----------------------------------------------------------------------
    //
    // Private member data
    //
    //
    
    // Note: declared private to reduce JAR size after obfuscation.
    private final int keyReducePerm[] = {
        60, 52, 44, 36, 59, 51, 43, 35, 27, 19, 11,  3, 58, 50,
        42, 34, 26, 18, 10,  2, 57, 49, 41, 33, 25, 17,  9,  1,
        28, 20, 12,  4, 61, 53, 45, 37, 29, 21, 13,  5, 62, 54,
        46, 38, 30, 22, 14,  6, 63, 55, 47, 39, 31, 23, 15,  7
    };
 
    private final int keyCompressPerm[] = {
        24, 27, 20,  6, 14, 10,  3, 22,  0, 17,  7, 12,
         8, 23, 11,  5, 16, 26,  1,  9, 19, 25,  4, 15,
        54, 43, 36, 29, 49, 40, 48, 30, 52, 44, 37, 33,
        46, 35, 50, 41, 28, 53, 51, 55, 32, 45, 39, 42
    };

    private final int keyRot[] = {
        1, 2, 4, 6, 8, 10, 12, 14, 15, 17, 19, 21, 23, 25, 27, 28
    };

    private final int initPerm[] = {
        57, 49, 41, 33 , 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7,
        56, 48, 40, 32 ,24, 16,  8, 0, 58, 50, 42, 34, 26, 18, 10, 2,
        60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6
    };

    private final int finPerm[] = {
        39,  7, 47, 15, 55, 23, 63, 31, 38,  6, 46, 14, 54, 22, 62, 30,
        37,  5, 45, 13, 53, 21, 61, 29, 36,  4, 44, 12, 52, 20, 60, 28,
        35,  3, 43, 11, 51, 19, 59, 27, 34,  2, 42, 10, 50, 18, 58, 26,
        33,  1, 41,  9, 49, 17, 57, 25, 32,  0, 40,  8, 48, 16, 56, 24
    };

    private final int sBoxP[][] = {
        {   0x00808200, 0x00000000, 0x00008000, 0x00808202,
            0x00808002, 0x00008202, 0x00000002, 0x00008000,
            0x00000200, 0x00808200, 0x00808202, 0x00000200,
            0x00800202, 0x00808002, 0x00800000, 0x00000002,
            0x00000202, 0x00800200, 0x00800200, 0x00008200,
            0x00008200, 0x00808000, 0x00808000, 0x00800202,
            0x00008002, 0x00800002, 0x00800002, 0x00008002,
            0x00000000, 0x00000202, 0x00008202, 0x00800000,
            0x00008000, 0x00808202, 0x00000002, 0x00808000,
            0x00808200, 0x00800000, 0x00800000, 0x00000200,
            0x00808002, 0x00008000, 0x00008200, 0x00800002,
            0x00000200, 0x00000002, 0x00800202, 0x00008202,
            0x00808202, 0x00008002, 0x00808000, 0x00800202,
            0x00800002, 0x00000202, 0x00008202, 0x00808200,
            0x00000202, 0x00800200, 0x00800200, 0x00000000,
            0x00008002, 0x00008200, 0x00000000, 0x00808002  },
        {   0x40084010, 0x40004000, 0x00004000, 0x00084010,
            0x00080000, 0x00000010, 0x40080010, 0x40004010,
            0x40000010, 0x40084010, 0x40084000, 0x40000000,
            0x40004000, 0x00080000, 0x00000010, 0x40080010,
            0x00084000, 0x00080010, 0x40004010, 0x00000000,
            0x40000000, 0x00004000, 0x00084010, 0x40080000,
            0x00080010, 0x40000010, 0x00000000, 0x00084000,
            0x00004010, 0x40084000, 0x40080000, 0x00004010,
            0x00000000, 0x00084010, 0x40080010, 0x00080000,
            0x40004010, 0x40080000, 0x40084000, 0x00004000,
            0x40080000, 0x40004000, 0x00000010, 0x40084010,
            0x00084010, 0x00000010, 0x00004000, 0x40000000,
            0x00004010, 0x40084000, 0x00080000, 0x40000010,
            0x00080010, 0x40004010, 0x40000010, 0x00080010,
            0x00084000, 0x00000000, 0x40004000, 0x00004010,
            0x40000000, 0x40080010, 0x40084010, 0x00084000  },
        {   0x00000104, 0x04010100, 0x00000000, 0x04010004,
            0x04000100, 0x00000000, 0x00010104, 0x04000100,
            0x00010004, 0x04000004, 0x04000004, 0x00010000,
            0x04010104, 0x00010004, 0x04010000, 0x00000104,
            0x04000000, 0x00000004, 0x04010100, 0x00000100,
            0x00010100, 0x04010000, 0x04010004, 0x00010104,
            0x04000104, 0x00010100, 0x00010000, 0x04000104,
            0x00000004, 0x04010104, 0x00000100, 0x04000000,
            0x04010100, 0x04000000, 0x00010004, 0x00000104,
            0x00010000, 0x04010100, 0x04000100, 0x00000000,
            0x00000100, 0x00010004, 0x04010104, 0x04000100,
            0x04000004, 0x00000100, 0x00000000, 0x04010004,
            0x04000104, 0x00010000, 0x04000000, 0x04010104,
            0x00000004, 0x00010104, 0x00010100, 0x04000004,
            0x04010000, 0x04000104, 0x00000104, 0x04010000,
            0x00010104, 0x00000004, 0x04010004, 0x00010100  },
        {   0x80401000, 0x80001040, 0x80001040, 0x00000040,
            0x00401040, 0x80400040, 0x80400000, 0x80001000,
            0x00000000, 0x00401000, 0x00401000, 0x80401040,
            0x80000040, 0x00000000, 0x00400040, 0x80400000,
            0x80000000, 0x00001000, 0x00400000, 0x80401000,
            0x00000040, 0x00400000, 0x80001000, 0x00001040,
            0x80400040, 0x80000000, 0x00001040, 0x00400040,
            0x00001000, 0x00401040, 0x80401040, 0x80000040,
            0x00400040, 0x80400000, 0x00401000, 0x80401040,
            0x80000040, 0x00000000, 0x00000000, 0x00401000,
            0x00001040, 0x00400040, 0x80400040, 0x80000000,
            0x80401000, 0x80001040, 0x80001040, 0x00000040,
            0x80401040, 0x80000040, 0x80000000, 0x00001000,
            0x80400000, 0x80001000, 0x00401040, 0x80400040,
            0x80001000, 0x00001040, 0x00400000, 0x80401000,
            0x00000040, 0x00400000, 0x00001000, 0x00401040  },
        {   0x00000080, 0x01040080, 0x01040000, 0x21000080,
            0x00040000, 0x00000080, 0x20000000, 0x01040000,
            0x20040080, 0x00040000, 0x01000080, 0x20040080,
            0x21000080, 0x21040000, 0x00040080, 0x20000000,
            0x01000000, 0x20040000, 0x20040000, 0x00000000,
            0x20000080, 0x21040080, 0x21040080, 0x01000080,
            0x21040000, 0x20000080, 0x00000000, 0x21000000,
            0x01040080, 0x01000000, 0x21000000, 0x00040080,
            0x00040000, 0x21000080, 0x00000080, 0x01000000,
            0x20000000, 0x01040000, 0x21000080, 0x20040080,
            0x01000080, 0x20000000, 0x21040000, 0x01040080,
            0x20040080, 0x00000080, 0x01000000, 0x21040000,
            0x21040080, 0x00040080, 0x21000000, 0x21040080,
            0x01040000, 0x00000000, 0x20040000, 0x21000000,
            0x00040080, 0x01000080, 0x20000080, 0x00040000,
            0x00000000, 0x20040000, 0x01040080, 0x20000080  },
        {   0x10000008, 0x10200000, 0x00002000, 0x10202008,
            0x10200000, 0x00000008, 0x10202008, 0x00200000,
            0x10002000, 0x00202008, 0x00200000, 0x10000008,
            0x00200008, 0x10002000, 0x10000000, 0x00002008,
            0x00000000, 0x00200008, 0x10002008, 0x00002000,
            0x00202000, 0x10002008, 0x00000008, 0x10200008,
            0x10200008, 0x00000000, 0x00202008, 0x10202000,
            0x00002008, 0x00202000, 0x10202000, 0x10000000,
            0x10002000, 0x00000008, 0x10200008, 0x00202000,
            0x10202008, 0x00200000, 0x00002008, 0x10000008,
            0x00200000, 0x10002000, 0x10000000, 0x00002008,
            0x10000008, 0x10202008, 0x00202000, 0x10200000,
            0x00202008, 0x10202000, 0x00000000, 0x10200008,
            0x00000008, 0x00002000, 0x10200000, 0x00202008,
            0x00002000, 0x00200008, 0x10002008, 0x00000000,
            0x10202000, 0x10000000, 0x00200008, 0x10002008  },
        {   0x00100000, 0x02100001, 0x02000401, 0x00000000,
            0x00000400, 0x02000401, 0x00100401, 0x02100400,
            0x02100401, 0x00100000, 0x00000000, 0x02000001,
            0x00000001, 0x02000000, 0x02100001, 0x00000401,
            0x02000400, 0x00100401, 0x00100001, 0x02000400,
            0x02000001, 0x02100000, 0x02100400, 0x00100001,
            0x02100000, 0x00000400, 0x00000401, 0x02100401,
            0x00100400, 0x00000001, 0x02000000, 0x00100400,
            0x02000000, 0x00100400, 0x00100000, 0x02000401,
            0x02000401, 0x02100001, 0x02100001, 0x00000001,
            0x00100001, 0x02000000, 0x02000400, 0x00100000,
            0x02100400, 0x00000401, 0x00100401, 0x02100400,
            0x00000401, 0x02000001, 0x02100401, 0x02100000,
            0x00100400, 0x00000000, 0x00000001, 0x02100401,
            0x00000000, 0x00100401, 0x02100000, 0x00000400,
            0x02000001, 0x02000400, 0x00000400, 0x00100001  },
        {   0x08000820, 0x00000800, 0x00020000, 0x08020820,
            0x08000000, 0x08000820, 0x00000020, 0x08000000,
            0x00020020, 0x08020000, 0x08020820, 0x00020800,
            0x08020800, 0x00020820, 0x00000800, 0x00000020,
            0x08020000, 0x08000020, 0x08000800, 0x00000820,
            0x00020800, 0x00020020, 0x08020020, 0x08020800,
            0x00000820, 0x00000000, 0x00000000, 0x08020020,
            0x08000020, 0x08000800, 0x00020820, 0x00020000,
            0x00020820, 0x00020000, 0x08020800, 0x00000800,
            0x00000020, 0x08020020, 0x00000800, 0x00020820,
            0x08000800, 0x00000020, 0x08000020, 0x08020000,
            0x08020020, 0x08000000, 0x00020000, 0x08000820,
            0x00000000, 0x08020820, 0x00020020, 0x08000020,
            0x08020000, 0x08000800, 0x08000820, 0x00000000,
            0x08020820, 0x00020800, 0x00020800, 0x00000820,
            0x00000820, 0x00020020, 0x08000000, 0x08020800  }
    };
    
    
    // -----------------------------------------------------------------------
    //
    // Protected member data
    //
    //
    
    protected long[] keys;

    
    // -----------------------------------------------------------------------
    //
    // Constructor
    //
    //
    
    /** Creates the instance of the <code>DesCipher</code>.
     */
    public DesCipher() {
        keys = makeKeys(0x00L);
    }

    
    // -----------------------------------------------------------------------
    //
    // Public methods
    //
    //
    
    /** Sets the key to use for the nex encryption/decryption.
     *
     * @param key The key to be used.
     * @param keyOffset The key offset.
     */
    public final void setKey(byte[] key, int keyOffset) {
        keys = makeKeys(EncDec.encodeBytesToLong(key, keyOffset));
    }

    /** Encrypts the block of bytes using the key set for the DesCipher.
     *
     * @param plainBytes The bytes of the plaintext.
     * @param plainBytesOffset The plaintext offset.
     * @param cipherBytes The bytes of the ciphertext.
     * @param cipherBytesOffset The ciphertext offset.
     */
    public final void encryptBlock(byte[] plainBytes, int plainBytesOffset, 
            byte[] cipherBytes, int cipherBytesOffset) {
        long plainText = EncDec.encodeBytesToLong(
                plainBytes, plainBytesOffset);
        long cipherText = encrypt(plainText);
        EncDec.encodeLongToBytes(cipherText, cipherBytes, cipherBytesOffset);
    }

    /** Decrypts the block of bytes using the key set for the DesCipher.
     *
     * @param cipherBytes The bytes of the ciphertext.
     * @param cipherBytesOffset The ciphertext offset.
     * @param plainBytes The plaintext bytes.
     * @param plainBytesOffset The plaintext offset.
     */
    public void decryptBlock (byte[] cipherBytes, int cipherBytesOffset, 
            byte[] plainBytes, int plainBytesOffset) {
        long cipherText = EncDec.encodeBytesToLong(
                cipherBytes, cipherBytesOffset);
        long plainText = decrypt(cipherText);
        EncDec.encodeLongToBytes(plainText, plainBytes, plainBytesOffset);
    }

    
    // -----------------------------------------------------------------------
    //
    // Private methods
    //
    //
    
    /** Encrypts the 8 byte using the currect cypher key.
     */
    private final long encrypt(long plain) {
    
        // Initial permutation
        long x = initialPerm(plain);
        int l = (int)(x >>> 32);
        int r = (int)x;
        
        int tmp;
        for (int i = 0; i < 16; i++) {
            tmp = desFunc(r, keys[i]) ^ l;
            l = r;
            r = tmp;
        }
        
        // Final permutation
        long y = ((long) r << 32) | ((long) l & 0xffffffffL);
        return finalPerm (y);
    }

    /** Decrypts the 8 bytes using the current key.
     */
    private final long decrypt(long cipher) {
        
        // Initial permutation
        long x = initialPerm(cipher);
        int l = (int) (x >>> 32);
        int r = (int) x;
        
        int tmp;
        for (int i = 15; i >= 0; i--) {
            tmp = desFunc(r, keys[i]) ^ l;
            l = r;
            r = tmp;
        }
        
        // Final permutation
        long y = ((long) r << 32) | ((long) l & 0xffffffffL);
        return finalPerm(y);
    }

    /** Sets the parity.
     */
    private final long paritySet(long key) {
        long pKey = (key >> 1)
                ^ (key >> 2)
                ^ (key >> 3)
                ^ (key >> 4)
                ^ (key >> 5)
                ^ (key >> 6)
                ^ (key >> 7);
        return (key | 0x0101010101010101L) ^ (pKey & 0x0101010101010101L);
    }

    /** Checks for the key parity.
     */
    private final boolean isParity(long key) {
        return (key == paritySet(key));
    }

    
    // -----------------------------------------------------------------------
    //
    // Private helper methods
    //
    //
    
    /** Creates the key array.
     */
    private final long[] makeKeys(long key) {
        long reduced = perm(key, keyReducePerm);
        int l = (int)(reduced >> 28);
        int r = (int)(reduced & 0xfffffff);
        long[] keys = new long[16];
        for (int i = 0; i < 16; ++ i) {
            keys[i] = perm(rotate(l, r, keyRot[i]), keyCompressPerm);
        }
        return keys;
    }

    /** Performs the initial permutation.
     */
    private final long initialPerm(long x) {
        return perm(x, initPerm);
    }

    /** Expansion pBox and sBox functions.
     */
    private final int desFunc(int x, long k) {
        int p = x >>> 27;
        int q = (p & 3) << 4;
        int r = x << 5;
        p |=  r;
        r = sBoxP[0][(int)((k >> 42) ^ p) & 0x3f]; 
        p >>>= 4;
        r |= sBoxP[7][(int)((k  >> 0) ^ p) & 0x3f]; 
        p >>>= 4;
        r |= sBoxP[6][(int)((k  >> 6) ^ p) & 0x3f]; 
        p >>>= 4;
        r |= sBoxP[5][(int)((k  >> 12) ^ p) & 0x3f]; 
        p >>>= 4;
        r |= sBoxP[4][(int)((k  >> 18) ^ p) & 0x3f]; 
        p >>>= 4;
        r |= sBoxP[3][(int)((k  >> 24) ^ p) & 0x3f]; 
        p >>>= 4;
        r |= sBoxP[2][(int)((k  >> 30) ^ p) & 0x3f]; 
        p >>>= 4;
        r |= sBoxP[1][(int)((k  >> 36) ^ (p | q)) & 0x3f];
        return r;
    }

    /** Performs the final permutation.
     */
    private final long finalPerm(long x) {
        return perm(x, finPerm);
    }

    /** Performs the permutation.
     */
    private final long perm(long k, int p[]) {
        long s = 0;
        int pLength = p.length;
        for (int i = 0; i < pLength; i++) {
            if ((k & (1L << p[i])) != 0) {
                s |= 1L << i;
            }
        }
        return s;
    }

    /** Performs the rotation.
     */
    private final long rotate(int l, int r, int s) {
        return ((long)(((l << s) & 0xfffffff) | (l >>> (28-s))) << 28) 
                | ((r << s) & 0xfffffff) | (r >> (28-s));
    }
    
}
