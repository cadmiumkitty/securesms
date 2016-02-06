/**
 * MenuScreen.java
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

/** The main menu screen (Inbox, Send New, Sent Items and Address Book).
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public class MenuScreen extends List {

    // -----------------------------------------------------------------------
    //
    // Public static data
    //
    //
    
    public static final int INBOX = 0;
    public static final int SEND_NEW = 1;
    public static final int SENT_ITEMS = 2;
    public static final int ADDRESS_BOOK = 3;
    
    public static final Command EXIT = new Command(
            ResourceLocator.getResource(ResourceLocator.EXIT), 
            Command.CANCEL, 1);

    
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
    public MenuScreen(SecureMessenger sm) {
                
        // Create the list
        super(ResourceLocator.getResource(ResourceLocator.SECURE_MESSENGER), 
                Choice.IMPLICIT);
        
        // Add items
        insert(INBOX, 
                ResourceLocator.getResource(ResourceLocator.INBOX), 
                null);
        insert(SEND_NEW, 
                ResourceLocator.getResource(ResourceLocator.SEND_NEW), 
                null);
        insert(SENT_ITEMS, 
                ResourceLocator.getResource(ResourceLocator.SENT_ITEMS), 
                null);
        insert(ADDRESS_BOOK, 
                ResourceLocator.getResource(ResourceLocator.ADDRESS_BOOK), 
                null);
        
        // Add commnads
        addCommand(EXIT);
        
        // Set listener
        setCommandListener(sm);
    }
    
    
    // -----------------------------------------------------------------------
    //
    // Public methods
    //
    //
    
    /** Returns an Id of the currently selected item.
     */
    public final synchronized int getSelectedMenuItem() {
        return getSelectedIndex();
    }

}
