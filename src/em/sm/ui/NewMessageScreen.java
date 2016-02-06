/**
 * NewMessageScreen.java
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

import em.sm.api.*;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

/** New message screen. 
 *
 * @author  Eugene Morozov
 * @version 0.9.1
 */
public class NewMessageScreen extends Form {

    // -----------------------------------------------------------------------
    //
    // Public class data
    //
    //

    public static final Command SEND = new Command(
            ResourceLocator.getResource(ResourceLocator.SEND), 
            Command.OK, 1);
    public static final Command CANCEL = new Command(
            ResourceLocator.getResource(ResourceLocator.CANCEL), 
            Command.CANCEL, 2);
    
    public static final int MAX_TEXT_LENGTH = 5000;
    
    // -----------------------------------------------------------------------
    //
    // Private member data
    //
    //
    
    private TextField text;
    private ChoiceGroup addresses;
    
    private long[] addressIds;
    
    
    // -----------------------------------------------------------------------
    //
    // Constructors
    //
    //
    
    /** Creates new MessageScreen. 
     */
    public NewMessageScreen(SecureMessenger sm) {
        
        // Create the form
        super(ResourceLocator.getResource(ResourceLocator.NEW_MESSAGE));
        
        // Create and add the form items 
        append(text = new TextField(
                ResourceLocator.getResource(ResourceLocator.TEXT), 
                "", MAX_TEXT_LENGTH, TextField.ANY));
        append(addresses = new ChoiceGroup(
                ResourceLocator.getResource(ResourceLocator.TO), 
                Choice.EXCLUSIVE));
        
        // Add commands
        addCommand(SEND);
        addCommand(CANCEL);
        
        // Set the listener
        setCommandListener(sm);
    }

    
    // -----------------------------------------------------------------------
    //
    // Public methods
    //
    //
    
    /** Updates the screen.
     */
    public final void updateMessage(Message message, 
            AddressStore addressStore) throws SecureMessengerException {
        
        // Set the text
        text.setString(message.getText());

        // Get the address of the message;
        Address address = message.getAddress();
        String messageAddress = (address == null) ? "" : address.getNumber();
        
        // Clear the address list
        int addressIdsLength = addresses.size();
        for (int i = 0; i < addressIdsLength; i ++) {
            addresses.delete(i);
        }
        
        // Add possible addresses and mark the one that matches the address
        // in the message (for the case of the reply operation)
        // Get the list of the Ids
        addressIds = addressStore.listIds();
        addressIdsLength = addressIds.length;
        if (addressIdsLength > 0) {
            
            // Add the addresses from the storage
            StringBuffer addressListItem;
            for (int i = 0; i < addressIdsLength; i++) {
                address = addressStore.getById(addressIds[i]);
                addressListItem = new StringBuffer();
                addressListItem.append(address.getName()).append(
                            ' ').append(address.getNumber());
                addresses.insert(0, addressListItem.toString(), null);
                
                // Automatically select the message that has been selected 
                // the last time user left the list
                if (address.getNumber().equals(messageAddress)) {
                    addresses.setSelectedIndex(i, true);
                }
            }
        }
    }
    
    /** Clears the screen.
     */
    public final void clear() {
        
        // Clear the text of the message
        text.setString("");
        
        int addressesLength = addresses.size();
        for (int i = 0; i < addressesLength; i++) {
            addresses.delete(i);
        }
    }
    
    /** Returns the new message text.
     */
    public final String getMessageText() {
        
        return text.getString();
    }
    
    /** Returns the id of teh address to send the message to.
     */
    public final long getSelectedAddressId() {
        
        int selectedIndex = addresses.getSelectedIndex();
        if (addressIds != null
                && selectedIndex >= 0
                && selectedIndex < addressIds.length) {

            // Addresses are added in the reverse order
            return addressIds[addressIds.length - 1 - selectedIndex];
        } 
        
        // This should actually never happen
        return -1;
    }
}
