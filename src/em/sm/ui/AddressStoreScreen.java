/**
 * AddressStoreScreen.java
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

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import em.sm.api.*;

/** The address book screen.
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public class AddressStoreScreen extends List {

    // -----------------------------------------------------------------------
    //
    // Public static data
    //
    //
    
    public static final Command ADD = new Command(
            ResourceLocator.getResource(ResourceLocator.ADD), 
            Command.SCREEN, 2);
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
    
    private long[] addressIds;

    
    // -----------------------------------------------------------------------
    //
    // Constructor
    //
    //
    
    /** Creates new MessageStoreScreen 
     */
    public AddressStoreScreen(SecureMessenger sm) {
                
        // Create the list
        super(ResourceLocator.getResource(ResourceLocator.ADDRESS_BOOK), 
                Choice.IMPLICIT);
        
        // Add commnads
        addCommand(ADD);
        addCommand(CANCEL);
        
        // Set listener
        setCommandListener(sm);
    }
    
    
    // -----------------------------------------------------------------------
    //
    // Public methods
    //
    //
    
    /** Returns the Id of the currently selected message
     */
    public final synchronized long getSelectedAddressId() {
        
        // Get the list's selected index and map it into the mesage id
        int selectedIndex = getSelectedIndex();
        if (addressIds != null
                && selectedIndex >= 0
                && selectedIndex < addressIds.length) {
                    
            // Note: We add addresses in the reverse order (always to the
            // first position in list)        
            return addressIds[addressIds.length - 1 - selectedIndex];
        } 
        
        // This should actually never happen
        return -1;
    }

    /** Updates the content of the List.
     */
    public final synchronized void updateAddressList(AddressStore addressStore)
            throws SecureMessengerException {
                
        // Get the currently selected id
        long id = getSelectedAddressId();        

        // Delete all messages to avoid synchronization problems
        int addressIdsLength = size();
        
        for (int i = 0; i < addressIdsLength; i++) {
            delete(0);
        }

        // Delete the command
        removeCommand(DELETE);

        // Get the list of the Ids
        addressIds = addressStore.listIds();
        addressIdsLength = addressIds.length;
        if (addressIdsLength > 0) {
            
            // Add the addresses from the storage
            Address address;
            StringBuffer addressListItem;
            for (int i = 0; i < addressIdsLength; i++) {
                
                address = addressStore.getById(addressIds[i]);
                addressListItem = new StringBuffer();
                addressListItem.append(address.getName()).append(
                            ' ').append(address.getNumber());
                insert(0, addressListItem.toString(), null);
                
                // Automatically select the message that has been selected 
                // the last time user left the list
                if (address.getId() == id) {
                    setSelectedIndex(i, true);
                }
            }
            
            // Add command
            addCommand(DELETE);
        }
    }

}
