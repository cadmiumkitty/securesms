/**
 * MessageStoreScreen.java
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

/** The message store screen. 
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public class MessageStoreScreen extends List {

    // -----------------------------------------------------------------------
    //
    // Public static data
    //
    //
    
    public static final Command DELETE = new Command(
            ResourceLocator.getResource(ResourceLocator.DELETE), 
            Command.SCREEN, 3);
    public static final Command CANCEL = new Command(
            ResourceLocator.getResource(ResourceLocator.CANCEL), 
            Command.CANCEL, 4);

    
    // -----------------------------------------------------------------------
    //
    // Private member data
    //
    //
    
    protected long[] messageIds;
    
    
    // -----------------------------------------------------------------------
    //
    // Constructor
    //
    //
    
    /** Creates new MessageStoreScreen 
     */
    protected MessageStoreScreen(SecureMessenger sm, String title) {
                
        // Create the list
        super(title, Choice.IMPLICIT);
        
        // Add commnads
        addCommand(CANCEL);
        
        // Set listener
        setCommandListener(sm);
    }
    
    
    // -----------------------------------------------------------------------
    //
    // Public methods
    //
    //
    
    /** Returns the Id of the currently selected message.
     */
    public final synchronized long getSelectedMessageId() {
        int selectedIndex = getSelectedIndex();
        if (messageIds != null 
                && selectedIndex >= 0
                && selectedIndex < messageIds.length) {
                    
            // Note: We add addresses in the reverse order (always to the
            // first position in list)        
            return messageIds[messageIds.length - 1 - selectedIndex];
        } 
        
        // This should actually never happen
        return -1;
    }

    /** Updates the content of the screen
     */
    public synchronized void updateMessageList(MessageStore messageStore)
            throws SecureMessengerException {

        // Get the id of the message currently selected
        long id = getSelectedMessageId();
                
        // Remove the DELETE command
        removeCommand(DELETE);
                
        // Delete all messages to avoid synchronization problems
        int messageIdsLength = size();
        for (int i = 0; i < messageIdsLength; i++) {
            delete(0);
        }
        
        // Get the list of the Ids
        messageIds = messageStore.listIds();
        messageIdsLength = messageIds.length;
        if (messageIdsLength > 0) {
            
            // Create new list and populate it with the message "identifiers" - 
            // MessageTimestamp/Name
            Message message;
            Calendar timestamp;
            StringBuffer messageListItem;
            for (int i = 0; i < messageIdsLength; i++) {
                
                // Prepate the item titles
                message = messageStore.getById(messageIds[i]);
                timestamp = message.getTimestamp();
                messageListItem = new StringBuffer();
                messageListItem.append(
                        timestamp.get(Calendar.DAY_OF_MONTH)).append(
                        '-').append(
                        timestamp.get(Calendar.MONTH) + 1).append(
                        '-').append(
                        timestamp.get(Calendar.YEAR)).append(
                        ' ').append(
                        message.getAddress().getName());
                
                // Add the element to the list
                insert(0, messageListItem.toString(), null);
                
                // Automatically select the message that has been selected 
                // the last time user left the list
                if (message.getId() == id) {
                    setSelectedIndex(i, true);
                }
            }
            
            // Add the DELETE command if at least one message has been found
            addCommand(DELETE);
        }
    }

}
