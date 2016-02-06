/**
 * ObjectFactory.java
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

package em.sm.impl.siemens.m50;

import java.util.*;
import em.sm.api.*;

/** This ObjectFactory should be used to obtain any SiemensM50-specific 
 * implementation of the standard interfaces.
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public final class SiemensObjectFactory extends ObjectFactory{

    // -----------------------------------------------------------------------
    //
    // Class variables
    //
    //
    
    private static MessageHandler theMessageHandler;
    private static Transcriptor theTranscriptor;

    
    // -----------------------------------------------------------------------
    //
    // Address-related methods
    //
    //
    
    // Javadoc inherited from the interface
    public Address getAddress(String name, String number, 
            byte[] key) throws SecureMessengerException {
    
        long addressId = System.currentTimeMillis();
        return new AddressImpl(addressId, name, number, key);
    }

    // Javadoc inherited from the interface
    public AddressStore getAddressStore(String storeName) 
            throws SecureMessengerException {
        
        return new AddressStoreImpl(storeName);
    }

    // Javadoc inherited from the interface
    public synchronized AddressHandler getAddressHandler() 
            throws SecureMessengerException {

        // XXX Not yet implemented
        throw new SecureMessengerException(
                "Address handler is not implemented");
    }
    
    
    // -----------------------------------------------------------------------
    //
    // Message-related methods
    //
    //
     
    // Javadoc inherited from the interface
    public Message getMessage(Address address, 
            String text, int status) throws SecureMessengerException {

        long messageId = System.currentTimeMillis();
        Calendar timestamp = Calendar.getInstance();
        timestamp.setTime(new Date(messageId));
        return new MessageImpl(messageId, address, timestamp, text, status);
    }

    // Javadoc inherited from the interface
    public MessageStore getMessageStore(String storeName) 
            throws SecureMessengerException {
        
        return new MessageStoreImpl(storeName);
    }

    // Javadoc inherited from the interface
    public synchronized MessageHandler getMessageHandler() 
            throws SecureMessengerException {
        
        // Singleton
        if (theMessageHandler == null) {
            theMessageHandler = new MessageHandlerImpl();
        }
        
        return theMessageHandler;
    }

    // Javadoc inherited from the interface
    public synchronized Transcriptor getTranscriptor() 
            throws SecureMessengerException {

        // Singleton
        if (theTranscriptor == null) {
            theTranscriptor = new TripleDesTranscriptorImpl();
        }
        
        return theTranscriptor;
    }
    
}
