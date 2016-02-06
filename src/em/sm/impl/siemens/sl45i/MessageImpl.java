/**
 * MessageImpl.java
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

import java.util.*;
import em.sm.api.Message;
import em.sm.api.Address; 

/** Implementation of the Message interface.
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public final class MessageImpl implements Message {

    // -----------------------------------------------------------------------
    //
    // Instance variables
    //
    //
    
    private final long id;
    private final Calendar timestamp;
    private Address address;
    private String text;
    private int status;

    // -----------------------------------------------------------------------
    //
    // Constructor
    //
    //
    
    /** Creates new <code>MessageImpl</code> instance
     */
    MessageImpl(long messageId, Address messageAddress, 
            Calendar messageTimestamp, String messageText, int messageStatus) {
    
        // Set the member variables
        id = messageId;
        address = messageAddress;
        timestamp = messageTimestamp;
        text = messageText;
        status = messageStatus;
    }
     
    
    // -----------------------------------------------------------------------
    //
    // Accessor methods
    //
    //
    
    // Javadoc inherited from the interface
    public long getId() {
        return id;
    }
    
    // Javadoc inherited from the interface
    public Address getAddress() {
        return address;
    }

    // Javadoc inherited from the interface
    public void setAddress(Address messageAddress) {
        address = messageAddress;
    }
    
    // Javadoc inherited from the interface
    public Calendar getTimestamp() {
        return timestamp;
    }
    
    // Javadoc inherited from the interface
    public String getText() {
        return text;
    }

    // Javadoc inherited from the interface
    public void setText(String messageText) {
        text = messageText;
    }
    
    // Javadoc inherited from the interface
    public int getStatus() {
        return status;
    }
    
    // Javadoc inherited from the interface
    public void setStatus(int messageStatus) {
        status = messageStatus;
    }
   
}
