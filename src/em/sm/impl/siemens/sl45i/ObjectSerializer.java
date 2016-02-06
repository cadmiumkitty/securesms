/**
 * ObjectSerializer.java
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

package em.sm.impl.siemens.sl45i;

import java.io.*;
import java.util.*;
import em.sm.api.*;
import em.sm.util.*;

/** Serializer/deserializer for the Address and Message objects.
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public class ObjectSerializer {

    // -----------------------------------------------------------------------
    //
    // Static constants
    //
    //
    
    private static final String SERIALIZATION_ENCODING = "UTF-8";
    
    private static final int LONG_LENGTH = 8;
    private static final int INT_LENGTH = 4;

    
    // -----------------------------------------------------------------------
    //
    // Static methods
    //
    //
    
    /** Deserializes the byte[] into the Address object.
     *
     * @param serializedAddress Serialized address (received from RMS store 
     *                          for example)
     * @return The deserialized <code>Address</code> object.
     */
    static Address deserializeAddress(byte[] serializedAddress) {
        
        // Get the id
        long id = EncDec.encodeBytesToLong(serializedAddress, 0);
        
        // Get the name
        int nameLength = EncDec.encodeBytesToInteger(serializedAddress, LONG_LENGTH);
        byte[] name = new byte[nameLength];
        System.arraycopy(serializedAddress, LONG_LENGTH + INT_LENGTH,
                name, 0, nameLength);
        
        // Get the number
        int numberLength = EncDec.encodeBytesToInteger(serializedAddress, 
                LONG_LENGTH + INT_LENGTH + nameLength);
        byte[] number = new byte[numberLength];
        System.arraycopy(serializedAddress, 
                LONG_LENGTH + INT_LENGTH * 2 + nameLength,
                number, 0, numberLength);
        
        // Get the key
        // Note: key is of the variable length. 
        int keyLength = EncDec.encodeBytesToInteger(serializedAddress, 
                LONG_LENGTH + INT_LENGTH * 2 + nameLength + numberLength);
        byte[] key = new byte[keyLength];
        System.arraycopy(serializedAddress, 
                LONG_LENGTH + INT_LENGTH * 3 + nameLength + numberLength,
                key, 0, keyLength);
        
        // Create the address instance
        Address address;
        try {
            
            address = new AddressImpl(id, 
                    new String(name, SERIALIZATION_ENCODING),
                    new String(number, SERIALIZATION_ENCODING),
                    key);
        } catch (UnsupportedEncodingException uee) {
            
            // XXX Do our best to create the address instance - use the 
            // handset's default encoding
            address = new AddressImpl(id, new String(name), 
                    new String(number), key);
        }
        
        return address;
    }

    /** Deserializes the byte[] into the Message object.
     *
     * @param serializedMessage Serialized message (received from RMS store 
     *                          for example)
     * @return The deserialized <code>Message</code> object.
     */
    static Message deserializeMessage(byte[] serializedMessage) {

        // Get the Id
        long id = EncDec.encodeBytesToLong(serializedMessage, 0);
        
        // Get the address
        int addressLength = EncDec.encodeBytesToInteger(serializedMessage, LONG_LENGTH);
        byte[] address = new byte[addressLength];
        System.arraycopy(serializedMessage, LONG_LENGTH + INT_LENGTH,
                address, 0, addressLength);
        
        // Get the text
        int textLength = EncDec.encodeBytesToInteger(serializedMessage, 
                LONG_LENGTH + INT_LENGTH + addressLength);
        byte[] text = new byte[textLength];
        System.arraycopy(serializedMessage, LONG_LENGTH + INT_LENGTH * 2 
                        + addressLength,
                text, 0, textLength);

        // Get the timestamp
        long timestamp = EncDec.encodeBytesToLong(serializedMessage, 
                 LONG_LENGTH + INT_LENGTH * 2 + addressLength + textLength);
        Calendar timestampCalendar = Calendar.getInstance();
        timestampCalendar.setTime(new Date(timestamp));
        
        // Get the status
        int status = EncDec.encodeBytesToInteger(serializedMessage, 
                 LONG_LENGTH * 2 + INT_LENGTH * 2 + addressLength + textLength);
        
        Message message;
        try {
            
            message = new MessageImpl(id, deserializeAddress(address), 
                    timestampCalendar, new String(text, SERIALIZATION_ENCODING), 
                    status);
        } catch (UnsupportedEncodingException uee) {
            
            // XXX Do our best to deserialize the message - use the default 
            // encoding
            message = new MessageImpl(id, deserializeAddress(address), 
                    timestampCalendar, new String(text), status);
        }
        
        return message;
    }
    
    /** Serializes the <code>Address</code> object into the byte[] to be stored
     * using the RMS.
     *
     * @param deserializedAddress The Address object.
     * @return The serialized <code>byte[]</code> representation of the Address.
     */
    static byte[] serializeAddress(Address deserializedAddress) {

        // Format is fairly simple - [Id_len][Id_content]...
        byte[] id = longToBytes(deserializedAddress.getId());
        byte[] name;
        byte[] number;
        byte[] key = deserializedAddress.getKey();
        
        try {
            name = deserializedAddress.getName().getBytes(
                SERIALIZATION_ENCODING);
            number = deserializedAddress.getNumber().getBytes(
                SERIALIZATION_ENCODING);
        } catch (UnsupportedEncodingException uee) {
            
            // XXX Do our best to serialize the objects - if encoding is not 
            // supported - use the default one
            name = deserializedAddress.getName().getBytes();
            number = deserializedAddress.getNumber().getBytes();
        }
        
        // Calculate lengths of the elements
        int nameLength = name.length;
        int numberLength = number.length;
        int keyLength = key.length;
        
        // Create the serialized form array
        byte[] serializedAddress = new byte[LONG_LENGTH + nameLength 
                + numberLength + keyLength + INT_LENGTH * 3];
        
        // Copy the id
        // No need to save the length of the id
        System.arraycopy(id, 0, 
                serializedAddress, 0, 
                LONG_LENGTH);
        
        // Copy the name
        System.arraycopy(intToBytes(nameLength), 0, 
                serializedAddress, LONG_LENGTH, 
                INT_LENGTH);
        System.arraycopy(name, 0, 
                serializedAddress, INT_LENGTH + LONG_LENGTH, 
                nameLength);
        
        // Copy the number
        System.arraycopy(intToBytes(numberLength), 0, 
                serializedAddress, INT_LENGTH + LONG_LENGTH + nameLength, 
                INT_LENGTH);
        System.arraycopy(number, 0, 
                serializedAddress, INT_LENGTH * 2 + LONG_LENGTH + nameLength, 
                numberLength);

        // Copy the key
        System.arraycopy(intToBytes(keyLength), 0, 
                serializedAddress, INT_LENGTH * 2 + LONG_LENGTH + nameLength 
                        + numberLength, 
                INT_LENGTH);
        System.arraycopy(key, 0, 
                serializedAddress, INT_LENGTH * 3 + LONG_LENGTH + nameLength 
                        + numberLength, 
                keyLength);
        
        return serializedAddress;
    }
    
    /** Serializes the <code>Message</code> object into the byte[] to be stored
     * using the RMS.
     *
     * @param deserializedMessage The Message object.
     * @return The serialized <code>byte[]</code> representation of the Message.
     */
    static byte[] serializeMessage(Message deserializedMessage) {
        
        // Format is fairly simple - [Id_len][Id_content]...
        byte[] id = longToBytes(deserializedMessage.getId());
        byte[] address = serializeAddress(deserializedMessage.getAddress());
        byte[] text;
        byte[] timestamp = longToBytes(
                deserializedMessage.getTimestamp().getTime().getTime());
        byte[] status = intToBytes(deserializedMessage.getStatus());
        
        try {
            text = deserializedMessage.getText().getBytes(
                    SERIALIZATION_ENCODING);
        } catch (UnsupportedEncodingException uee) {
            
            // XXX Do our best to serialize the objects - if encoding is not 
            // supported - use the default one
            text = deserializedMessage.getText().getBytes();
        }
        
        // Calculate lengths of the elements
        int addressLength = address.length;
        int textLength = text.length;
        
        // Create the serialized form array
        byte[] serializedAddress = new byte[LONG_LENGTH + addressLength 
                + textLength + LONG_LENGTH + INT_LENGTH + INT_LENGTH * 2];
        
        // Copy the id
        // No need to save the length of the id
        System.arraycopy(id, 0, 
                serializedAddress, 0, 
                LONG_LENGTH);
        
        // Copy the address information
        System.arraycopy(intToBytes(addressLength), 0, 
                serializedAddress, LONG_LENGTH, 
                INT_LENGTH);
        System.arraycopy(address, 0, 
                serializedAddress, INT_LENGTH + LONG_LENGTH, 
                addressLength);
        
        // Copy the text
        System.arraycopy(intToBytes(textLength), 0, 
                serializedAddress, INT_LENGTH + LONG_LENGTH + addressLength, 
                INT_LENGTH);
        System.arraycopy(text, 0, 
                serializedAddress, INT_LENGTH * 2 + LONG_LENGTH + addressLength, 
                textLength);

        // Copy the timestamp
        // No need to save the length of the timestamp
        System.arraycopy(timestamp, 0, 
                serializedAddress, INT_LENGTH * 2 + LONG_LENGTH + addressLength 
                        + textLength, 
                LONG_LENGTH);

        // Copy the message status
        // No need to save the length of the status
        System.arraycopy(status, 0, 
                serializedAddress, INT_LENGTH * 2 + LONG_LENGTH * 2 
                        + addressLength + textLength,
                INT_LENGTH);
        
        return serializedAddress;
    }
    
    
    // -----------------------------------------------------------------------
    //
    // Helper methods
    //
    //

    /** Converts int to byte[].
     */
    private static byte[] intToBytes(int intNumber) {
        
        byte[] result = new byte[4];
        EncDec.encodeIntegerToBytes(intNumber, result, 0);
        return result;
    }
    
    /** Converts long to byte[].
     */
    private static byte[] longToBytes(long longNumber) {
        
        byte[] result = new byte[8];
        EncDec.encodeLongToBytes(longNumber, result, 0);
        return result;
    }
    
}
