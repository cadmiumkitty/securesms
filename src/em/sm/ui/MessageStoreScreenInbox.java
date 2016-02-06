/**
 * MessageStoreScreenInbox.java
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

package em.sm.ui;

import java.util.Calendar;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import em.sm.api.*;

/** Extension of the Message store screen. Contains the "Reply" command.
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public class MessageStoreScreenInbox extends MessageStoreScreen {

    // -----------------------------------------------------------------------
    //
    // Public static data
    //
    //
    
    public static final Command REPLY = new Command(
            ResourceLocator.getResource(ResourceLocator.REPLY), 
            Command.SCREEN, 2);

    
    // -----------------------------------------------------------------------
    //
    // Private member data
    //
    //
    
    
    // -----------------------------------------------------------------------
    //
    // Constructor
    //
    //
    
    /** Creates new MessageStoreScreen 
     */
    public MessageStoreScreenInbox(SecureMessenger sm) {
                
        // Create the list
        super(sm, ResourceLocator.getResource(ResourceLocator.INBOX));
        
        // Add commnads
        addCommand(REPLY);
    }
    
    
    // -----------------------------------------------------------------------
    //
    // Public methods - inherited from the superclass
    //
    //
    
    /** 
    /** Updates the content of the screen
     */
    public synchronized void updateMessageList(MessageStore messageStore)
            throws SecureMessengerException {
    
        // Remove the REPLAY command
        removeCommand(REPLY);
        
        // Call the superclass' method
        super.updateMessageList(messageStore);
        
        // Check the length of the addresses and add the REMOVE command 
        // if nesesary.
        if (messageIds != null && messageIds.length > 0) {
            addCommand(REPLY);
        }
    }
    
}
