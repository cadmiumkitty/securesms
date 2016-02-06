/**
 * MessageStoreScreenOutbox.java
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

/** The message store screen extension. Doesn't contain the "Reply" command.
 *
 * @author  Eugene Morozov
 * @version 0.9.0
 */
public class MessageStoreScreenOutbox extends MessageStoreScreen {

    // -----------------------------------------------------------------------
    //
    // Public static data
    //
    //
    
    
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
    public MessageStoreScreenOutbox(SecureMessenger sm) {
                
        // Create the list
        super(sm, ResourceLocator.getResource(ResourceLocator.SENT_ITEMS));
    }
    
    
    // -----------------------------------------------------------------------
    //
    // Public methods - inherited from the superclass
    //
    //
    
}
